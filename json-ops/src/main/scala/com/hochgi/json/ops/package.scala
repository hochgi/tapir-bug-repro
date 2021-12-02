package com.hochgi.json

import java.io.Writer
import org.apache.commons.io.output.StringBuilderWriter
import org.apache.commons.text.StringEscapeUtils
import com.hochgi.json.ast.Json
import com.hochgi.json.ast.Json.{Arr, BigDecimalNumber, BigIntNumber, Bool, Null, Num, Obj, Str}

package object ops {

  @inline private[json] def foldDouble[T](d: Double, ifInfiniteOrNaN: T)(ifReal: Double => T): T = {
    if(java.lang.Double.isFinite(d)) ifReal(d) else ifInfiniteOrNaN
  }

  @inline private[json] def foldFloat[T](f: Float, ifInfiniteOrNaN: T)(ifReal: Float => T): T = {
    if(java.lang.Float.isFinite(f)) ifReal(f) else ifInfiniteOrNaN
  }

  implicit class JsonOps(json: Json) {

    def apply(i: Int): Either[Throwable, Json] = json match {
      case Arr(v) if v.isDefinedAt(i) => Right(v(i))
      case Arr(v) =>
        val bounds = if(v.isEmpty) "empty array" else s"[0-${v.length - 1}]"
        Left(new IndexOutOfBoundsException(s"$i is out of bounds of $bounds"))
      case _ => Left(new Exception(s"cant index access on a ${getClass.getSimpleName}"))
    }

    @inline private[this] def mapGet(jm: Map[String, Json], field: String): Either[Throwable, Json] =
      jm.get(field)
        .fold[Either[Throwable,Json]](Left(new NoSuchElementException(s"No such field '$field'")))(Right.apply)

    def downField(field: String): Either[Throwable, Json] = json match {
      case Obj(jm) => mapGet(jm, field)
      case _ => Left(new Exception(s"cant downField on a ${getClass.getSimpleName}"))
    }

    def downPath(path: List[String]): Either[Throwable, Json] = path match {
      case Nil => Right(json)
      case field :: restOfPath => json match {
        case Obj(m) => mapGet(m, field) match {
          case Right(o) => o.downPath(restOfPath)
          case leftFail => leftFail
        }
        case _ => Left(new Exception(s"cant downPath[${path.mkString(".")}] on a ${getClass.getSimpleName}"))
      }
    }

    private[json] def writeString(s: String, w: Writer): Writer = {
      w.append('"')
      StringEscapeUtils.ESCAPE_JSON.translate(s, w)
      w.append('"')
    }

    private[json] def writeArray(arr: Vector[Json], w: Writer, path: List[String]): Writer = {
      if (arr.isEmpty) w.append("[]")
      else {
        var ch = '['
        arr.zipWithIndex.foreach { case (inner, i) =>
          w.append (ch)
          ch = ','
          writeJson (inner, w, s"[$i]" :: path)
        }
        w.append (']')
      }
    }

    private[json] def writeObject(obj: Map[String, Json], w: Writer, path: List[String]): Writer = {
      if (obj.isEmpty) w.append("{}")
      else {
        var ch = '{'
        // sorting by keys generates a consistent json every time.
        // it may not be performant, but the tradeoff was consistency for testing.
        obj.toSeq.sortBy(_._1).foreach { case (k, v) =>
          w.append(ch)
          ch = ','
          w.append('"')
          StringEscapeUtils.ESCAPE_JSON.translate(k, w)
          w.append('"')
          w.append(':')
          writeJson(v, w, k :: path)
        }
        w.append('}')
      }
    }

    private[this] def wrongNumberClass(unknown: AnyVal, path: List[String]): Writer = {
      // This should never be reached, and case added to rid of the compilation warning.
      // A Json.Number is only defined for [N <: AnyVal : Numeric], which means:
      //
      // 1. it must be an AnyVal (most likely primitive)
      // 2. It must have a Numeric typeclass.
      //
      // It's not that the case isn't possible theoretically.
      // To reach here, one has to define a special weird class & typeclass like e.g:
      //
      // case class Rational(numeratorAndDenominator: Long) extends AnyVal {
      //   def numerator: Int = ((-1L >>> 32) & numeratorAndDenominator).toInt
      //   def denominator: Int = (numeratorAndDenominator >>> 32).toInt
      // }
      // object Rational {
      //   implicit val numeric: Numeric[Rational] = ???
      // }
      //
      // And then manually construct Json.Number(rational),
      // followed by trying to render that Json (or an AST containing it) with some writer.
      // So, not likely to happen, and you have to really know what you're doing to shoot yourself in the foot.
      val jp = renderPathOnError(path)
      val cn = unknown.getClass.getSimpleName
      throw new IllegalArgumentException(s"Json number $jp undefined for class $cn")
    }

    private[json] def writeJson(j: Json, w: Writer, path: List[String]): Writer = j match {
      case Bool(b)             => w.append(String.valueOf(b))
      case Null                => w.append("null")
      case Str(s)              => writeString(s, w)
      case Arr(arr)            => writeArray(arr, w, path)
      case Obj(obj)            => writeObject(obj, w, path)
      case BigIntNumber(v)     => w.append(v.toString())
      case BigDecimalNumber(v) => w.append(v.toString())
      case Num(n: Byte)        => w.append(String.valueOf(n))
      case Num(n: Short)       => w.append(String.valueOf(n))
      case Num(n: Int)         => w.append(String.valueOf(n))
      case Num(n: Long)        => w.append(String.valueOf(n))
      case Num(n: Float)       => w.append(foldFloat(n, "null")(String.valueOf))
      case Num(n: Double)      => w.append(foldDouble(n, "null")(String.valueOf))
      case Num(u)              => wrongNumberClass(u, path)
      case Json.Undefined      => throw new IllegalArgumentException(msgForUndefined(path))
    }

    def writeJson(w: Writer): Writer = writeJson(json, w, Nil)

    def stringify: String = writeJson(json, new StringBuilderWriter(), Nil).toString
  }

  private[json] def renderPathOnError(path: List[String]): String =
    path.reverseIterator.mkString("[at path: ", ".", " ]")

  private[json] def msgForUndefined(path: List[String]): String =
    s"Json.Undefined ${renderPathOnError(path)} is not representable."
}
