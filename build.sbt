import sbt._
import Dependencies.{tapirCore, _}

def depver(scalaVersion: String): Version = scalaVersion match {
  case "2.11" => Version.S211
  case "2.12" => Version.S212
  case "2.13" => Version.S213
  case binver => throw new NotImplementedError(s"Build not defined for scala $binver")
}

val allScalaCrossVersions     = List(Version.S213, Version.S212, Version.S211).map(_.scalaFullVersion)
val scala212n213CrossVersions = List(Version.S213, Version.S212).map(_.scalaFullVersion)
val scala213CrossVersion      = List(Version.S213.scalaFullVersion)

val sharedSettings = Seq(
  version := "0.0.1-SNAPSHOT",
  organization := "com.hochgi",
  scalaVersion := Version.S213.scalaFullVersion,
  scalacOptions ++= {
    val safeForCrossParams = Seq(
      "-encoding", "UTF-8",    // source files are in UTF-8
      "-deprecation",          // warn about use of deprecated APIs
      "-unchecked",            // warn about unchecked type parameters
      "-feature",              // warn about misused language features
      "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
      "-Xlint"                 // enable handy linter warnings

      // fatal warnings is temporarily off until a workaround is found for:
      // "Block result was adapted via implicit conversion (method apply) taking a by-name parameter"
      // see:
      //   https://users.scala-lang.org/t/2-13-3-by-name-implicit-linting-error/6334/2
      //   https://github.com/scala/scala/pull/8590
      //   https://github.com/scala/bug/issues/12072
      //
      // "-Xfatal-warnings"       // compilation warning fail as errors
    )

    scalaBinaryVersion.value match {
      case "2.11" => safeForCrossParams :+ "-Ypartial-unification" :+ "-language:existentials"
      case "2.12" => safeForCrossParams :+ "-Ypartial-unification"
      case "2.13" => safeForCrossParams
      case binver => throw new NotImplementedError(s"Build not defined for scala $binver")
    }
  },
  scalacOptions ++= Seq("-target:jvm-1.8"),
  Test / scalacOptions ++= Seq("-target:jvm-1.8"),
  libraryDependencies ++= Seq(
    scalacheck % Test,
    scalatest  % Test
  )
)

lazy val compat = (project in file("compat"))
  .settings(
    sharedSettings,
    name := "compat",
    crossScalaVersions := allScalaCrossVersions
  )

lazy val jsonAst = (project in file("json-ast"))
  .settings(
    sharedSettings,
    name := "json-ast",
    crossScalaVersions := allScalaCrossVersions
  )

lazy val jsonOps = (project in file("json-ops"))
  .dependsOn(jsonAst)
  .settings(
    sharedSettings,
    name := "json-ops",
    crossScalaVersions := allScalaCrossVersions,
    libraryDependencies ++= Seq(
      commonsIo,
      commonsText
    )
  )

lazy val jsonEncode = (project in file("json-encode"))
  .dependsOn(compat, jsonOps)
  .settings(
    sharedSettings,
    name := "json-encode",
    crossScalaVersions := allScalaCrossVersions,
    libraryDependencies ++= {
      val v = depver(scalaBinaryVersion.value)
      Seq(
        magnolia(v),
        scalaReflect(v)
      )
    }
  )

lazy val jsonCirce = (project in file("json-circe"))
  .dependsOn(jsonAst, jsonEncode)
  .settings(
    sharedSettings,
    name := "json-circe",
    crossScalaVersions := allScalaCrossVersions,
    libraryDependencies ++= {
      val v = depver(scalaBinaryVersion.value)
      Seq(
        circeCore(v),
        circeGeneric(v) % Test
      )
    }
  )

lazy val datatypes = (project in file("repro-datatypes"))
  .dependsOn(jsonAst)
  .settings(sharedSettings)
  .settings(
    name := "repro-datatypes",
    crossScalaVersions := allScalaCrossVersions
  )

lazy val endpoints = (project in file("repro-endpoints"))
  .dependsOn(datatypes, jsonCirce)
  .settings(sharedSettings)
  .settings(
    name := "repro-endpoints",
    crossScalaVersions := scala212n213CrossVersions, // TODO: add support for 2.11
    libraryDependencies ++= {
      val v = depver(scalaBinaryVersion.value)
      Seq(
        tapirCore(v),
        akkaStream(v),
        tapirJsonCirce(v),
        circeCore(v),
        scalaLogging
      )
    }
  )

val generateYaml = taskKey[File]("run GenerateSwagger to generate the swagger yaml")
lazy val swagger = (project in file("repro-swagger"))
  .enablePlugins(BuildInfoPlugin)
  .dependsOn(endpoints)
  .settings(
    sharedSettings,
    name := "repro-swagger",
    crossScalaVersions := scala213CrossVersion,
    publish / skip := true,
    libraryDependencies ++= {
      val v = depver(scalaBinaryVersion.value)
      Seq(
        tapirOpenapiCirceYaml(v),
        tapirOpenapiDocs(v),
        scopt
      )
    },
    buildInfoPackage := "com.hochgi.repro.build",
    buildInfoObject := "Info",
    buildInfoOptions ++= Seq(BuildInfoOption.ToJson, BuildInfoOption.BuildTime),
    buildInfoKeys := Seq[BuildInfoKey](
      version,
      scalaVersion,
      sbtVersion,
      git.gitCurrentBranch,
      git.gitHeadCommit,
      git.gitHeadCommitDate,
      git.gitUncommittedChanges
    ),
    generateYaml := {
      val logger = streams.value.log
      val cp = Attributed.data((Compile / fullClasspath).value)
      val main = "com.hochgi.repro.swagger.GenerateSwagger"
      val outputFile = target.value / "swagger.yaml"
      val args = Seq("-s", "plain", outputFile.getAbsolutePath)
      (Compile / runner).value.run(main, cp, args, logger).get
      outputFile
    }
  )

lazy val server = (project in file("repro-server"))
  .dependsOn(endpoints, swagger)
  .settings(sharedSettings)
  .settings(
    name := "repro-server",
    crossScalaVersions := scala213CrossVersion,
    publish / skip := true,
    libraryDependencies ++= {
      val v = depver(scalaBinaryVersion.value)
      Seq(
        akkaHttp(v),
        akkaActor(v),
        akkaStream(v),
        tapirCore(v),
        tapirAkkaHttpServer(v),
        tapirSwaggerUi(v),
        tapirOpenapiDocs(v),
        tapirOpenapiCirceYaml(v),
        tapirJsonCirce(v),
        sttpSharedAkka,
        logbackClassic,
        scalaLogging,
        akkaHttpTestkit(v) % Test,
        scalacheck % Test,
        scalatest % Test)
    }
  )

lazy val root = (project in file("."))
  .aggregate(jsonAst, jsonOps, jsonEncode, jsonCirce, datatypes, endpoints, swagger, server)
  .settings(
    // crossScalaVersions must be set to Nil on the aggregating project
    crossScalaVersions := Nil,
    publish / skip := true
  )
