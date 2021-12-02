package com.hochgi

package object compat {

  implicit class MapCompat[K, V](m: Map[K, V]) {
    def mapValuesCompat[U](f: V => U): Map[K, U] = m.mapValues(f)
  }
}
