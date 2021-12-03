import sbt.{ModuleID => M, _}
import Dependencies.{Version => V}

object Dependencies {

  sealed trait Version {
    def scalaBinaryVersion: String
    def scalaFullVersion:   String
    def akkaHttp:           String
    def akka:               String
    def akkaActorArtifact:  String
    def circe:              String
    def magnolia:           String
    def tapir:              Version.Tapir
  }

  object Version {

    case class Tapir(org: String, version: String)

    object S211 extends Version {
      override val scalaBinaryVersion = "2.11"
      override val scalaFullVersion   = "2.11.12"
      override val akkaHttp           = "10.1.14"
      override val akka               = "2.5.32"
      override val circe              = "0.12.0-M3"
      override val magnolia           = "0.10.0"
      override val akkaActorArtifact  = "akka-actor"
      override val tapir: Tapir       = Tapir("com.softwaremill.tapir", "0.8.11")
    }

    object S212 extends Version {
      override val scalaBinaryVersion = "2.12"
      override val scalaFullVersion   = "2.12.15"
      override val akkaHttp           = "10.2.7"
      override val akka               = "2.6.17"
      override val circe              = "0.14.1"
      override val magnolia           = "0.17.0"
      override val akkaActorArtifact  = "akka-actor-typed"
      override val tapir: Tapir       = Tapir("com.softwaremill.sttp.tapir", "0.19.1")
    }

    object S213 extends Version {
      override val scalaBinaryVersion = "2.13"
      override val scalaFullVersion   = "2.13.7"
      override val akkaHttp           = "10.2.7"
      override val akka               = "2.6.17"
      override val circe              = "0.14.1"
      override val magnolia           = "0.17.0"
      override val akkaActorArtifact  = "akka-actor-typed"
      override val tapir: Tapir       = Tapir("com.softwaremill.sttp.tapir", "0.19.1")
    }
  }

  // akka
  val akkaHttp:          V => M = v => "com.typesafe.akka" %% "akka-http"           % v.akkaHttp
  val akkaActor:         V => M = v => "com.typesafe.akka" %% v.akkaActorArtifact   % v.akka
  val akkaStream:        V => M = v => "com.typesafe.akka" %% "akka-stream"         % v.akka

  // akka test
  val akkaHttpTestkit: V => M = v => "com.typesafe.akka" %% "akka-http-testkit" % v.akkaHttp

  // tapir
  val tapirCore:             V => M = v => v.tapir.org %% "tapir-core"               % v.tapir.version
  val tapirAkkaHttpServer:   V => M = v => v.tapir.org %% "tapir-akka-http-server"   % v.tapir.version
  val tapirSwaggerUi:        V => M = v => v.tapir.org %% "tapir-swagger-ui"         % v.tapir.version
  val tapirOpenapiDocs:      V => M = v => v.tapir.org %% "tapir-openapi-docs"       % v.tapir.version
  val tapirOpenapiCirceYaml: V => M = v => v.tapir.org %% "tapir-openapi-circe-yaml" % v.tapir.version
  val tapirJsonCirce:        V => M = v => v.tapir.org %% "tapir-json-circe"         % v.tapir.version

  // sttp
  val sttp3Akka = "com.softwaremill.sttp.client3" %% "akka-http-backend" % "3.3.1"
  val sttpSharedAkka = "com.softwaremill.sttp.shared" %% "akka" % "1.2.3"

  // circe
  val circeCore:    V => M = v => "io.circe" %% "circe-core"    % v.circe
  val circeParser:  V => M = v => "io.circe" %% "circe-parser"  % v.circe
  val circeGeneric: V => M = v => "io.circe" %% "circe-generic" % v.circe

  // magnolia
  val magnolia:     V => M = v => "com.propensive" %% "magnolia"      % v.magnolia
  val scalaReflect: V => M = v => "org.scala-lang" %  "scala-reflect" % v.scalaBinaryVersion % Provided

  // logging
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3"

  // util
  val scopt       = "com.github.scopt"  %% "scopt"        % "4.0.1"
  val commonsText = "org.apache.commons" % "commons-text" % "1.9"
  val commonsIo   = "commons-io"         % "commons-io"   % "2.11.0"

  // test
  val scalacheck = "org.scalacheck" %% "scalacheck"  % "1.15.2"
  val scalatest  = "org.scalatest"  %% "scalatest"   % "3.2.3"
}
