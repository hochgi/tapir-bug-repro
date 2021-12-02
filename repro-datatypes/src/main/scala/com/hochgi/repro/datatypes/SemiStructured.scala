package com.hochgi.repro.datatypes

import com.hochgi.json.ast.Json

final case class SemiStructured(constant:               String,
                                structured:             Int,
                                fields:                 Long,
                                withSemiStructuredData: Option[Json.Obj])
