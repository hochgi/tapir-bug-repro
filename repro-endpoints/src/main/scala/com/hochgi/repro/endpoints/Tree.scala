package com.hochgi.repro.endpoints

import com.hochgi.repro.{datatypes => dt}
import io.circe.Json.Folder
import sttp.tapir.{Codec, DecodeResult, PublicEndpoint, Schema, SchemaType, customJsonBody}
import io.circe.{Json, JsonNumber, JsonObject, parser}
import sttp.tapir.Codec.JsonCodec

object Tree {

  def treeSchema: Schema[dt.Tree] = {

    val intLeafSchema    = Schema.schemaForBoolean.map(i => Some(dt.Tree.BoolLeaf(i)))(_.b).name(Schema.SName(classOf[dt.Tree.BoolLeaf].getCanonicalName))
    val stringLeafSchema = Schema.schemaForString.map(i => Some(dt.Tree.StringLeaf(i)))(_.s).name(Schema.SName(classOf[dt.Tree.StringLeaf].getCanonicalName))
    val treeNodeSchema   = treeSchema.asIterable[List].map(l => Some(dt.Tree.TreeNode(l)))(_.children).name(Schema.SName(classOf[dt.Tree.TreeNode].getCanonicalName))

    val schemaType = SchemaType.SCoproduct[dt.Tree](List(intLeafSchema, stringLeafSchema, treeNodeSchema), None) {
      case t: dt.Tree.BoolLeaf   => Some(SchemaType.SchemaWithValue(intLeafSchema.asInstanceOf[Schema[Any]], t))
      case t: dt.Tree.StringLeaf => Some(SchemaType.SchemaWithValue(stringLeafSchema.asInstanceOf[Schema[Any]], t))
      case t: dt.Tree.TreeNode   => Some(SchemaType.SchemaWithValue(treeNodeSchema.asInstanceOf[Schema[Any]], t))
    }
    Schema(schemaType, Some(Schema.SName(classOf[dt.Tree].getCanonicalName)))
  }

  object TreeFolder extends Folder[DecodeResult[dt.Tree]] {
    override def onNull: DecodeResult[dt.Tree] = DecodeResult.Mismatch("Boolean,String, or Array", "null")
    override def onNumber(value: JsonNumber): DecodeResult[dt.Tree] = DecodeResult.Mismatch("Boolean,String, or Array", "null")
    override def onObject(value: JsonObject): DecodeResult[dt.Tree] = DecodeResult.Mismatch("Boolean,String, or Array", "null")
    override def onBoolean(value: Boolean): DecodeResult[dt.Tree] = DecodeResult.Value(dt.Tree.BoolLeaf(value))
    override def onString(value: String): DecodeResult[dt.Tree] = DecodeResult.Value(dt.Tree.StringLeaf(value))
    override def onArray(value: Vector[Json]): DecodeResult[dt.Tree] = value.foldRight[DecodeResult[List[dt.Tree]]](DecodeResult.Value(Nil)){
      case (json, decodeResult) => for {
        tail <- decodeResult
        head <- json.foldWith(TreeFolder)
      } yield head :: tail
    }.map(dt.Tree.TreeNode.apply)
  }

  val formatTree: dt.Tree => Json = {
    case dt.Tree.BoolLeaf(bool)  => Json.fromBoolean(bool)
    case dt.Tree.StringLeaf(str) => Json.fromString(str)
    case dt.Tree.TreeNode(trees) => Json.fromValues(trees.map(formatTree))
  }

  val treeCodec: JsonCodec[dt.Tree] = Codec.json[dt.Tree]{ s =>
    parser.parse(s) match {
      case Left(error) => DecodeResult.Error(s, error)
      case Right(json) => json.foldWith(TreeFolder)
    }
  }(formatTree.andThen(_.noSpaces))(treeSchema)

  type TreeEndpoint = PublicEndpoint[Unit, Unit, dt.Tree, Any]
  val printTree: TreeEndpoint = Base
    .api
    .name("Tree")
    .tag("Sandbox")
    .get
    .in("tree")
    .out(customJsonBody[dt.Tree](treeCodec))
}
