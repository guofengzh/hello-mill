package typeprojection.typematch


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
 * Version 1: use type match
 */

/* 为简化，首先我们假定Rdf4j的有下列类型： */
trait Model
trait Statement
trait Value

 /* 
  * RDF是我们的接口，其中的用到抽象类型
  * 
  * 我们定义我们的抽象类型Graph、Triple和Node：这些类型依赖于具体的实现，我们使用使用match type来映射具体实现中的类型 
  */
trait RDF { self =>
  type This >: this.type <: RDF /* 这个类型由子类型具体指定，由此也具体化了下列类型 */
  type Graph = GraphTyp[This]
  type Triple = TripleTyp[This]
  type Node = NodeTyp[This]
}

/* 针对不同的实现（例如，RDF4j），我们用match type来确定各种抽象类型对应的具体实现中的类型 */
type GraphTyp[Rdf <: RDF] = Rdf match
  case Rdf4j => Model

type TripleTyp[Rdf <: RDF] = Rdf match
  case Rdf4j => Statement

type NodeTyp[Rdf <: RDF] = Rdf match
  case Rdf4j => Value

/* 这是使用RDF4j实现，所以，This设置成Rdf4j */
trait Rdf4j extends RDF:
  type This = Rdf4j

/* 根据PointedGraph在使用中Rdf所绑定的类型，NodeTyp[Rdf]和GraphTyp[Rdf]分别给出实现中的具体类型 */
trait PointedGraph[Rdf <: RDF]:
  def pointer: NodeTyp[Rdf]  /* 我们不需要写成：Rdf#Node  */
  def graph: GraphTyp[Rdf]   /* 我们不需要写成：Rdf#Graph */
