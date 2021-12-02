package com.hochgi.repro.endpoints

import sttp.model.MediaType
import sttp.tapir._
import sttp.tapir.DecodeResult.Value

object Info {

  type BuildEndpoint = PublicEndpoint[Unit, Unit, String, Any]
  val build: BuildEndpoint = Base
    .api
    .name("Build Info")
    .tag("Info")
    .description("Get static build info")
    .get
    .in("info")
    .out(customJsonBody[String](Codec.json[String](Value.apply)(identity)))

  // https://github.com/lightbend/config/blob/master/HOCON.md#mime-type
  case class Hocon() extends CodecFormat {
    override val mediaType: MediaType = MediaType("application", "hocon")
  }

  val hoconBody: EndpointIO.Body[String, String] = stringBody.copy(codec = stringBody.codec.format(Hocon()))

  type AllConfigEndpoint = PublicEndpoint[Unit, Unit, String, Any]
  val allConfig: AllConfigEndpoint = Base
    .api
    .name("Config")
    .tag("Info")
    .description("Get entire configuration")
    .get
    .in("conf")
    .out(hoconBody)

  type ConfigEndpoint = PublicEndpoint[String, String, String, Any]
  val config: ConfigEndpoint = Base
    .api
    .name("Config at path")
    .tag("Info")
    .description("Get configuration for some path")
    .get
    .in("conf" / path[String].name("path").description("Configuration key path"))
    .errorOut(stringBody)
    .out(hoconBody)
}
