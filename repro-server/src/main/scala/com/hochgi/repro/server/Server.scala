package com.hochgi.repro.server

import akka.NotUsed
import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{Behavior, Scheduler, Terminated}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import com.hochgi.repro.server.{routes => rt}
import com.hochgi.repro.swagger.EvalEndpoints
import com.hochgi.repro.swagger.EvalEndpoints.EvaluatedAPI
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import sttp.tapir.swagger.{SwaggerUI, SwaggerUIOptions}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object Server extends App with LazyLogging {

  // avoid slf4j noise by touching it first from single thread
  logger.debug("Starting server")

  def endpointsAndRoutes(config: Config): EvaluatedAPI[Route] = EvalEndpoints.evalAll(
    evalgetTree   = rt.Tree.getSample,
    evalBuild     = rt.Info.build,
    evalAllConfig = rt.Info.allConfig(config),
    evalConfig    = rt.Info.config(config))

  def undocumentedRoutes(context:              ActorContext[NotUsed])
                        (implicit actorSystem: ActorSystem,
                         scheduler:            Scheduler,
                         ec:                   ExecutionContext): Route = get {
    path("kill") {
      complete {
        scheduler.scheduleOnce(1.milli, new Runnable {
          override def run(): Unit = actorSystem.terminate().transform {
            case Success(_) => Try(context.system.terminate())
            case Failure(e) => Try(context.system.terminate()).transform(_ => Failure(e), { t =>
              t.addSuppressed(e)
              Failure(t)
            })
          }.onComplete {
            case Success(_) => logger.info("Server killed!")
            case Failure(e) => logger.error("Server assassination failed!", e)
          }
        })
        HttpResponse(status = StatusCodes.OK, entity = "Bye bye!")
      }
    }
  }

  def apply(): Behavior[NotUsed] = Behaviors.setup { context =>

    implicit val actorSystem: ActorSystem      = context.system.classicSystem
    implicit val execContext: ExecutionContext = actorSystem.dispatcher
    implicit val scheduler:   Scheduler        = context.system.scheduler

    val config: Config = actorSystem.settings.config

    val undocumented          = undocumentedRoutes(context)
    val evaluatedAPI          = endpointsAndRoutes(config)
    val (_, routes)           = evaluatedAPI.endpoints.unzip
    val docsAsYaml:    String = evaluatedAPI.openApiDocs.toYaml
    val uiOptions             = SwaggerUIOptions.default.copy(pathPrefix = List("doc"))
    val swaggerUIEndPs        = SwaggerUI[Future](docsAsYaml, uiOptions)
    val swaggerUIRoute: Route = AkkaHttpServerInterpreter().toRoute(swaggerUIEndPs)
    val allRoutes:      Route = concat(swaggerUIRoute :: undocumented :: routes: _*)
    val serverConfig:  Config = config.getConfig("com.hochgi.repro.server")
    val port:             Int = serverConfig.getInt("port")
    val host:          String = serverConfig.getString("host")
    val bindingFuture = Http()
      .newServerAt(host, port)
      .bind(allRoutes)

    Behaviors.receiveSignal {
      case (_, Terminated(_)) =>
        bindingFuture.foreach(_.terminate(10.seconds))
        Behaviors.stopped
    }
  }

  akka.actor.typed.ActorSystem(Server(), "evidence-manager")
}
