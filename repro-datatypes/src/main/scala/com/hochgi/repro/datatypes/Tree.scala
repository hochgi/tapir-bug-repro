package com.hochgi.repro.datatypes

sealed trait Tree
object Tree {
  case class BoolLeaf(b: Boolean) extends Tree
  case class StringLeaf(s: String) extends Tree
  case class TreeNode(children: List[Tree]) extends Tree
}
