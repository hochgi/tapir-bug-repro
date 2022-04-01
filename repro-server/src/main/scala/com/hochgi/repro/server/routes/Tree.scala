package com.hochgi.repro.server.routes

import akka.http.scaladsl.server.Route
import com.hochgi.repro.datatypes.Tree._
import com.hochgi.repro.endpoints.Tree.TreeEndpoint
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.Future

object Tree {
  val getSample: TreeEndpoint => Route =
    buildEndpoint => AkkaHttpServerInterpreter().toRoute(
      buildEndpoint.serverLogicPure[Future] { _ =>
        Right(TreeNode(List(BoolLeaf(true), TreeNode(Nil), StringLeaf("hello world"))))
      }
    )
}
