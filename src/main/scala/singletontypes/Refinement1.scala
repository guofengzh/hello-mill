package singletontypes

//trait Validated[PredRes <: Boolean]
//given Validated[true] = new Validated[true] {}

//trait RefinedInt[Predicate[_ <: Int] <: Boolean]
//def validate[V <: Int, Predicate[_ <: Int] <: Boolean]
//    (using Validated[Predicate[V]]): RefinedInt[Predicate] = new RefinedInt {}


// self-descriptive compilation errors
sealed trait Pred
class And[A <: Pred, B <: Pred]         extends Pred
class Leaf                              extends Pred
class LowerThan[T <: Int & Singleton]   extends Leaf
class GreaterThan[T <: Int & Singleton] extends Leaf

import scala.compiletime.*
import scala.compiletime.ops.int.*

trait Validated[E <: Pred]

import scala.language.implicitConversions
implicit inline def mkVal[V <: Int & Singleton, E <: Pred](v: V): Validated[E] =
  inline erasedValue[E] match
    case _: LowerThan[t] =>
      inline if constValue[V] < constValue[t]
        then new Validated[E] {}
        else
          inline val vs    = constValue[ToString[V]]
          inline val limit = constValue[ToString[t]]
          error("Validation failed: " + vs + " < " + limit)
    //case _: GreaterThan[t] => // ommited here since it's symmetrical to LowerThan
    case _: And[a, b] =>
      inline mkVal[V, a](v) match
        case _: Validated[?] =>
          inline mkVal[V, b](v) match
            case _: Validated[?] => new Validated[E] {}
