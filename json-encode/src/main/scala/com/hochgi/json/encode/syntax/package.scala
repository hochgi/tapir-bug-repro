package com.hochgi.json.encode

import com.hochgi.json.ast.Json

package object syntax {
  implicit final class EncoderOps[A](private val value: A) extends AnyVal {
    def asJson(implicit encoder: Encoder[A]): Json = encoder.encode(value)
  }
}
