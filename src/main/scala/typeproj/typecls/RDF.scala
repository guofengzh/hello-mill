package typeproj.typecls

/**
 * Dropped: General Type Projection
 * https://dotty.epfl.ch/docs/reference/dropped-features/type-projection.html
 * 
 * Converting code using simple type projections to dotty
 * https://users.scala-lang.org/t/converting-code-using-simple-type-projections-to-dotty/6516
 * 
 * 
 * banana-rdf 有许多针对各种库的实现。 例如。 对于 IBM 的 RDF4J
 * 
 * Version 2: Use type classes and path-dependent types
 */

 /* 为简化，首先我们假定Rdf4j的有下列类型： */
trait Model
trait Statement
trait Value

 /* 
  * RDF是我们的接口，其中的用到抽象类型
  * 
  * 定义我们的抽象类型Graph、Triple和Node，使用path dependent type，来映射具体实现中的类型 
  */
trait RDF[Rdf <: RDF[Rdf]](using val g: GraphTyp[Rdf], val t: TripleTyp[Rdf], val n: NodeTyp[Rdf]) {
  type Graph = g.Out
  type Triple = t.Out
  type Node = n.Out
}

/* 这是Graph的typeclass */
trait GraphTyp[Rdf <: RDF[Rdf]]:
  type Out

/* 这是Triple的typeclass */
trait TripleTyp[Rdf <: RDF[Rdf]]:
  type Out

/* 这是Node的typeclass */
trait NodeTyp[Rdf <: RDF[Rdf]]:
  type Out

/* 针对Graph、Triple和Node，通过给出given instance来绑定它们到具体实现Rdf4j上 */
trait Rdf4j extends RDF[Rdf4j] 

/* 以下是Rdf4j的given instance */
object GraphTyp: 
  given GraphTyp[Rdf4j] with
    type Out = Model

object TripleTyp:
  given TripleTyp[Rdf4j] with    // // 这是given instance
    type Out = Statement

object NodeTyp:
  given NodeTyp[Rdf4j] with
    type Out = Value

/* 我们使用path dependent type来指称具体类型 */
trait PointedGraph[Rdf <: RDF[Rdf]]:
  def pointer(using n: NodeTyp[Rdf]): n.Out
  def graph(using g: GraphTyp[Rdf]): g.Out
