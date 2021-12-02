package com.hochgi.json

import com.hochgi.json.encode.{Converter, Encoder}
import com.hochgi.json.ast.{Json => KJ}
import com.hochgi.compat.MapCompat
import io.circe.{JsonNumber, JsonObject, Encoder => CirceEncoder, Json => CJ}

package object circe {

  object Folder extends CJ.Folder[KJ] { me =>
    override def onNull: KJ = KJ.Null
    override def onBoolean(value: Boolean): KJ = KJ.Bool(value)
    override def onString(value: String): KJ = KJ.Str(value)
    override def onArray(value: Vector[CJ]): KJ = KJ.Arr(value.map(_.foldWith(me)))
    override def onObject(value: JsonObject): KJ = KJ.Obj(value.toMap.mapValuesCompat(_.foldWith(me)).toMap[String,KJ])
    override def onNumber(value: JsonNumber): KJ = {
      value.toByte.fold[KJ]{
        value.toShort.fold[KJ]{
          value.toInt.fold[KJ]{
            value.toLong.fold[KJ]{
              value.toBigInt.fold[KJ]{
                packJsonNumberConcisecly(value).getOrElse {
                  val f = value.toFloat
                  val d = value.toDouble
                  if(d == f) KJ.Num(f)
                  else       KJ.Num(d)
                }
              }(KJ.BigIntNumber.apply)
            }(KJ.Num.apply)
          }(KJ.Num.apply)
        }(KJ.Num.apply)
      }(KJ.Num.apply)
    }
    private[this] def packJsonNumberConcisecly(jn: JsonNumber): Option[KJ] = {
      jn.toBigDecimal.map { bd =>
        if(bd.isWhole) {
          if      (bd.isValidByte)  KJ.Num(bd.toByteExact)
          else if (bd.isValidShort) KJ.Num(bd.toShortExact)
          else if (bd.isValidInt)   KJ.Num(bd.toIntExact)
          else if (bd.isValidLong)  KJ.Num(bd.toLongExact)
          else bd.toBigIntExact.fold[KJ](KJ.BigDecimalNumber(bd))(KJ.BigIntNumber)
        } else {
          if      (bd.isExactFloat)  KJ.Num(bd.floatValue)
          else if (bd.isExactDouble) KJ.Num(bd.doubleValue)
          else KJ.BigDecimalNumber(bd)
        }
      }
    }
  }

  implicit val converter: Converter[CJ] = new Converter[CJ] {
    override def convert(other: CJ): KJ = other.foldWith(Folder)
  }

  object Auto {
    // Only import this implicit in the case you need to encode circe compatible Json.
    // i.e: a JSON you also want to be able to read back as a class.
    // most likely, this isn't needed, since for those case you will not use this json
    // lib at all. But I added for completeness.
    implicit def deriveCirceBackingEncoder[T: CirceEncoder]: Encoder[T] = {
      val circenc = implicitly[CirceEncoder[T]]
      new Encoder[T] {
        override def encode(t: T): KJ = circenc(t).foldWith(Folder)
      }
    }
  }
}
