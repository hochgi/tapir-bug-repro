package com.hochgi.repro.endpoints

import com.typesafe.scalalogging.LazyLogging
import sttp.tapir.{Endpoint, endpoint}

object Base extends LazyLogging {

  val api: Endpoint[Unit, Unit, Unit, Unit, Any] = endpoint.in("api")
}
