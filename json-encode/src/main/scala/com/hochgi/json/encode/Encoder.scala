package com.hochgi.json.encode

import language.experimental.macros
import magnolia._
import com.hochgi.json.ast.Json
import com.hochgi.compat.MapCompat

trait Encoder[T] {
  def encode(t: T): Json
}
object Encoder {

  def apply[T: Encoder](t: T): Json = implicitly[Encoder[T]].encode(t)

  object Auto {
    type Typeclass[T] = Encoder[T]

    def combine[T](cx: CaseClass[Encoder, T]): Encoder[T] = new Encoder[T] {
      override def encode(t: T): Json = Json.Obj (
        cx.parameters
          .view
          .map { p => p.label -> p.typeclass.encode(p.dereference(t)) }
          .collect { case kv @ (_, v) if v ne Json.Undefined => kv }
          .toMap
      )
    }

    /**
     * The encoder defined for sum types is naive,
     * and generates a JSON that cannot always be decoded back to the proper type.
     *
     * For example, if we had a simple sum type like:
     *
     * sealed trait Pet
     * case class Cat(name: String) extends Pet
     * case class Dog(name: String) extends Pet
     *
     * The resulting JSON looks the same for both a Cat & a Dog.
     * Just a simple `{"name":"fluffy"}`
     * There is no "discriminator" to decode back the proper type.
     *
     * This is intentional, as we only use our decoder in known situations.
     * We keep things simple, and we don't force redundant fields in the JSON output.
     */
    def dispatch[T](cx: SealedTrait[Encoder, T]): Encoder[T] = new Encoder[T] {
      override def encode(t: T): Json = {
        cx.dispatch(t) { s =>
          s.typeclass.encode(s.cast(t))
        }
      }
    }

    implicit def gen[T]: Typeclass[T] = macro Magnolia.gen[T]
  }

  implicit def json[OtherJsonAST: Converter]: Encoder[OtherJsonAST] = new Encoder[OtherJsonAST] {
    override def encode(t: OtherJsonAST): Json = implicitly[Converter[OtherJsonAST]].convert(t)
  }

  implicit def map[V: Encoder]: Encoder[Map[String, V]] = new Encoder[Map[String, V]] {
    override def encode(m: Map[String, V]): Json = Json.Obj(m.mapValuesCompat(apply[V]).collect {
      case kv @ (_, v) if v ne Json.Undefined => kv
    }.toMap[String, Json])
  }

  implicit def set[T: Encoder: Ordering]: Encoder[Set[T]] = new Encoder[Set[T]] {
    override def encode(elems: Set[T]): Json = Json.Arr(elems.toVector.sorted.map(apply[T]).filter(Json.Undefined.!=))
  }

  implicit def seq[T:  Encoder]: Encoder[Seq[T]] = new Encoder[Seq[T]] {
    override def encode(elems: Seq[T]): Json = Json.Arr(elems.toVector.map(apply[T]).map {
      case Json.Undefined => Json.Null
      case validJson => validJson
    })
  }

  implicit def option[T: Encoder]: Encoder[Option[T]] = new Encoder[Option[T]] {
    override def encode(elem: Option[T]): Json = elem.fold[Json](Json.Undefined)(apply[T])
  }

  implicit val char: Encoder[Char] = new Encoder[Char] {
    override def encode(char: Char): Json = Json.Str(String.valueOf(char))
  }

  implicit val bool: Encoder[Boolean] = new Encoder[Boolean] {
    override def encode(bool: Boolean): Json = Json.Bool(bool)
  }

  implicit val string: Encoder[String] = new Encoder[String] {
    override def encode(s: String): Json = Json.Str(s)
  }

  implicit val byte: Encoder[Byte] = new Encoder[Byte] {
    override def encode(b: Byte): Json = Json.Num(b)
  }

  implicit val short: Encoder[Short] = new Encoder[Short] {
    override def encode(s: Short): Json = Json.Num(s)
  }

  implicit val int: Encoder[Int] = new Encoder[Int] {
    override def encode(i: Int): Json = Json.Num(i)
  }

  implicit val long: Encoder[Long] = new Encoder[Long] {
    override def encode(l: Long): Json = Json.Num(l)
  }

  implicit val float: Encoder[Float] = new Encoder[Float] {
    override def encode(f: Float): Json = Json.Num(f)
  }

  implicit val double: Encoder[Double] = new Encoder[Double] {
    override def encode(d: Double): Json = Json.Num(d)
  }

  implicit val bigint: Encoder[BigInt] = new Encoder[BigInt] {
    override def encode(i: BigInt): Json = Json.BigIntNumber(i)
  }

  implicit val bigdecimal: Encoder[BigDecimal] = new Encoder[BigDecimal] {
    override def encode(d: BigDecimal): Json = Json.BigDecimalNumber(d)
  }
}