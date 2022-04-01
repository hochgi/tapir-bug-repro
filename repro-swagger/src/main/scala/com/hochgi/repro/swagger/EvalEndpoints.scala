package com.hochgi.repro.swagger

import com.hochgi.repro.{endpoints => ep}
import com.hochgi.repro.build.Info
import sttp.tapir.{AnyEndpoint, Endpoint}
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.OpenAPI

object EvalEndpoints {

  type EvaluatedEndpoint[T] = (AnyEndpoint, T)
  case class EvaluatedAPI[T](endpoints: List[EvaluatedEndpoint[T]], openApiDocs: OpenAPI)

  def eval[A, B, C, D, E, F](ep: Endpoint[A, B, C, D, E],
                             fn: Endpoint[A, B, C, D, E] => F): EvaluatedEndpoint[F] = ep -> fn(ep)

  /**
   * This method is a single shared place to configure swagger.
   * It should be used from both the server, and the swagger module,
   * whose sole purpose is to generate swagger yaml during build.
   * Forcing all places to use the same shared method ensures
   * build & runtime swagger definitions stays consistent.
   */
  def evalAll[T](evalgetTree:   ep.Tree.TreeEndpoint      => T,
                 evalBuild:     ep.Info.BuildEndpoint     => T,
                 evalAllConfig: ep.Info.AllConfigEndpoint => T,
                 evalConfig:    ep.Info.ConfigEndpoint    => T): EvaluatedAPI[T] = {
    val endpoints = List(
      ep.Tree.printTree -> evalgetTree(ep.Tree.printTree),
      ep.Info.build     -> evalBuild(ep.Info.build),
      ep.Info.allConfig -> evalAllConfig(ep.Info.allConfig),
      ep.Info.config    -> evalConfig(ep.Info.config))

    val openApiDocs: OpenAPI = OpenAPIDocsInterpreter().toOpenAPI(
      es      = endpoints.map(_._1),
      title   = "Sandbox API",
      version = Info.version)
    EvaluatedAPI(endpoints, openApiDocs)
  }
}
