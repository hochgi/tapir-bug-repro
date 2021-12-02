package com.hochgi.repro.server.routes

import akka.http.scaladsl.server.Route
import com.typesafe.config.{Config, ConfigException}
import com.hochgi.repro.build.Info.toJson
import com.hochgi.repro.endpoints.Info.{AllConfigEndpoint, BuildEndpoint, ConfigEndpoint}
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.Future, Future.{successful => fs, failed => ff}
import scala.util.{Failure, Success, Try}

object Info {

  val build: BuildEndpoint => Route =
    buildEndpoint => AkkaHttpServerInterpreter().toRoute(
      buildEndpoint.serverLogic[Future] { _ =>
        Future.successful(Right(toJson))
      }
    )

  def allConfig(config: Config): AllConfigEndpoint => Route =
    allConfigEndpoint => AkkaHttpServerInterpreter().toRoute(
      allConfigEndpoint.serverLogic[Future] { _ =>
        fs(Right(config.root().render()))
      }
    )

  def config(config: Config): ConfigEndpoint => Route =
    configEndpoint => AkkaHttpServerInterpreter().toRoute(
      configEndpoint.serverLogic[Future] { path =>
        Try(config.getConfig(path)) match {
          case Success(c)                            => fs(Right(c.atPath(path).root().render()))
          case Failure(e: ConfigException.Missing)   => fs(Left(s"'$path' is missing, "       + e.getMessage))
          case Failure(e: ConfigException.WrongType) => fs(Left(s"'$path' is of wrong type, " + e.getMessage))
          case Failure(unknown)                      => ff(unknown)
      }
    }
  )
}
