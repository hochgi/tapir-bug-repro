package com.hochgi.repro.endpoints

import io.circe.{Decoder, HCursor, JsonObject, ParsingFailure, Json => CJson}
import io.circe.parser.parse
import com.hochgi.repro.{datatypes => dt}
import com.hochgi.json.ast.{Json => KJson}
import com.hochgi.json.circe.Folder
import com.hochgi.compat.MapCompat
import com.hochgi.json.ops.JsonOps
import org.apache.commons.io.output.StringBuilderWriter
import org.apache.commons.text.StringEscapeUtils
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.Codec.JsonCodec

import java.io.Writer
import scala.util.{Failure, Success, Try}

object SemiStructured {

  private[this] val SemiStructured: Endpoint[Unit, Unit, Unit, Unit, Any] = Base.api.in("SemiStructured")
  private[this] val SemiStructureds: Endpoint[Unit, Unit, Unit, Unit, Any] = Base.api.in("SemiStructureds")

  def tryValue[T](hc: HCursor, field: String)
                 (implicit decoder: Decoder[T]): Try[T] = hc.get[T](field)(decoder) match {
    case Left(failed) => Failure(failed)
    case Right(value) => Success(value)
  }

  def maybeTryValue[T](hc: HCursor, field: String)
                      (implicit decoder: Decoder[T]): Try[Option[T]] =
    hc.downField(field)
      .success
      .map(_.as[T])
      .fold[Try[Option[T]]](Success(None)) {
        case Left(failed) => Failure(failed)
        case Right(value) => Success(Some(value))
      }

  private[this] def appendSemiStructuredToWriter(sw: Writer): dt.SemiStructured => Writer = {
    case dt.SemiStructured(constant, structured, fields, withSemiStructuredData) =>
      sw.append("{\"constant\":\"")
      StringEscapeUtils.ESCAPE_JSON.translate(constant, sw)
      sw.append("\",\"structured\":")
        .append(String.valueOf(structured))
        .append(",\"fields\":")
        .append(String.valueOf(fields))
      withSemiStructuredData.foreach { obj =>
        sw.append(",\"metadata\":")
        obj.writeJson(sw)
      }
      sw.append('}')

  }

  private[this] def decodeSemiStructuredJson(original: => String, circeJson: CJson): DecodeResult[dt.SemiStructured] = {
    val hc = circeJson.hcursor

    // using `Try` instead of `Either` since pre-2.13 versions of scala
    // didn't have right biased either, thus no flatMap, and we want this
    // code to cross compile to all supported versions of scala.
    val decoded: Try[DecodeResult[dt.SemiStructured]] = for {
      c <- tryValue[String](hc, "constant")
      s <- tryValue[Int](hc, "structured")
      f <- tryValue[Long](hc, "fields")
      d <- maybeTryValue[JsonObject](hc, "withSemiStructuredData")
    } yield {
      val semiStructuredData = d.map(obj => KJson.Obj(obj.toMap.mapValuesCompat(_.foldWith(Folder)).toMap))
      DecodeResult.Value(dt.SemiStructured(c, s, f, semiStructuredData))
    }

    // for same reason I'm using `match` instead of `fold` which changed
    decoded match {
      case Success(decodeResultValue) => decodeResultValue
      case Failure(aGeneralException) => DecodeResult.Error(original, aGeneralException)
    }
  }

  val SemiStructuredCodec: JsonCodec[dt.SemiStructured] = {

    val decode: String => DecodeResult[dt.SemiStructured] = s => parse(s) match {
      case Left(ParsingFailure(_, ex)) => DecodeResult.Error(s, ex)
      case Right(circeJson) => decodeSemiStructuredJson(s, circeJson)
    }

    val encode: dt.SemiStructured => String = appendSemiStructuredToWriter(new StringBuilderWriter()).andThen(_.toString)

    Codec.json[dt.SemiStructured](decode)(encode)
  }

  type RegisterTextEndpoint = Endpoint[Unit, dt.SemiStructured, Unit, String, Any]
  val registerText: RegisterTextEndpoint = {
    SemiStructured
      .name("SemiStructured ingest")
      .description("Ingest a text SemiStructured, used in evidence. Retrieve its ID (idempotent API)")
      .post
      .in(customJsonBody[dt.SemiStructured](SemiStructuredCodec))
      .out(stringBody)
  }

  type QuerySemiStructuredEndpoint = Endpoint[Unit, String, Unit, dt.SemiStructured, Any]
  val querySemiStructured: QuerySemiStructuredEndpoint = {
    val SemiStructuredID = path[String].name("SemiStructured ID").description("The unique SHA3-384 base64 ID of this text SemiStructured.")
    SemiStructured
      .name("Get SemiStructured by ID")
      .description("Given an ID, returns the appropriate text SemiStructured.")
      .in(SemiStructuredID)
      .out(customJsonBody[dt.SemiStructured](SemiStructuredCodec))
  }

  val manySemiStructuredsCodec: JsonCodec[Map[String, dt.SemiStructured]] = {

    type Result = DecodeResult[Map[String, dt.SemiStructured]]

    val decode: String => Result = s => parse(s) match {
      case Left(ParsingFailure(_, ex)) => DecodeResult.Error(s, ex)
      case Right(circeJson) => circeJson.asObject.fold[Result](DecodeResult.Mismatch("Object", circeJson.name)) { obj =>
        DecodeResult.sequence(obj.toIterable.toSeq.map { case (k, j) =>
          decodeSemiStructuredJson(j.noSpaces, j).map(k.->)
        }).map(_.toMap)
      }
    }

    val encode: Map[String, dt.SemiStructured] => String = sm => {
      val sw = new StringBuilderWriter()
      val appendFunction = appendSemiStructuredToWriter(sw)
      var sep = '{'
      sm.keys.toList.sorted.foreach { key =>
        sw.append(sep)
          .append('\"')
          .append(key)
          .append("\":")
        appendFunction(sm(key))
        sep = ','
      }
      if (sep == '{') "{}"
      else sw.append('}').toString
    }

    Codec.json[Map[String, dt.SemiStructured]](decode)(encode)
  }

  type ListSemiStructuredsEndpoint = Endpoint[Unit, Unit, Unit, Map[String, dt.SemiStructured], Any]
  val listSemiStructureds: ListSemiStructuredsEndpoint = {
    SemiStructureds
      .name("List SemiStructureds")
      .get
      .description("List all text SemiStructureds")
      .out(customJsonBody[Map[String, dt.SemiStructured]](manySemiStructuredsCodec))
  }
}
