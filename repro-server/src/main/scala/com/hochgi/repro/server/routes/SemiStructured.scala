package com.hochgi.repro.server.routes

import akka.http.scaladsl.server.Route
import com.hochgi.repro.{datatypes => dt, endpoints => ep}
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.{ExecutionContext, Future}

object SemiStructured {

  def registerSemiStructured(implicit ec: ExecutionContext): ep.SemiStructured.RegisterTextEndpoint => Route = {
    registerTextEndpoint => AkkaHttpServerInterpreter().toRoute(
      registerTextEndpoint.serverLogic[Future] { SemiStructured =>
        val response: Future[String] = ???
        response.map(Right.apply)
      }
    )
  }

  def querySemiStructured(implicit ec: ExecutionContext): ep.SemiStructured.QuerySemiStructuredEndpoint => Route = {
    querySemiStructuredEndpoint => AkkaHttpServerInterpreter().toRoute(
      querySemiStructuredEndpoint.serverLogic[Future] { (id: String) =>
        val response: Future[Option[dt.SemiStructured]] = ???
        response.map(_.fold[Either[Unit, dt.SemiStructured]](Left(()))(Right.apply))
      }
    )
  }

  def listSemiStructureds(implicit ec:   ExecutionContext): ep.SemiStructured.ListSemiStructuredsEndpoint => Route =
    listSemiStructuredsEndpoint => AkkaHttpServerInterpreter().toRoute(
      listSemiStructuredsEndpoint.serverLogic[Future] { _ =>
        val response: Future[Map[String, dt.SemiStructured]] = ???
        response.map(Right.apply)
      }
    )
}
