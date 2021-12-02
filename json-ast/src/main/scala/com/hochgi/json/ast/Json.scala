package com.hochgi.json.ast

import scala.collection.immutable

sealed trait Json
object Json {

  final case class Num[N <: AnyVal : Numeric](n: N) extends Json // takes care of Byte,Short,Int,Long,Float,Double
  final case class BigIntNumber(big: BigInt) extends Json
  final case class BigDecimalNumber(big: BigDecimal) extends Json

  final case class Bool(bool: Boolean) extends Json
  final case class Str(str: String) extends Json
  final case object Null extends Json
  final case object Undefined extends Json

  final case class Arr(arr: immutable.Vector[Json]) extends Json
  final case class Obj(obj: immutable.Map[String, Json]) extends Json
}
