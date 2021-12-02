package com.hochgi

import scala.collection.MapView

package object compat {

  implicit class MapCompat[K, V](m: Map[K, V]) {
    def mapValuesCompat[U](f: V => U): MapView[K, U] = m.view.mapValues(f)
  }
}
