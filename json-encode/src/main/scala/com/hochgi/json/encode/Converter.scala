package com.hochgi.json.encode

import com.hochgi.json.ast.Json

/**
 * This json lib aims to provide a zero-dependency AST.
 * It is not a full pledged JSON library.
 * As such, there is no direct JSON parser.
 * When arbitrary JSONs parsing is needed,
 * choose whatever you dim best fitted for the task at hand,
 * and by simply providing an implicit Converter typeclass
 * instance from the other JSON AST, you can benefit from
 * automatic encoding to this lib's json using [[Encoder.json]].
 */
trait Converter[OtherJsonAST] {

  def convert(other: OtherJsonAST): Json
}
