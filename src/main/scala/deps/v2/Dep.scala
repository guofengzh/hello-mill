/**
 * Maths - Dependent Types
 * https://www.euclideanspace.com/maths/discrete/types/dependent/index.htm
 * 
 * Dependent types in Scala
 * https://partialflow.wordpress.com/2017/07/26/dependent-types-type-level-programming/
 */
package deps.v2

import scala.compiletime.ops.int.S

type Nat = Int

sealed trait DepType[N <: Nat]:
  type T
  def apply(x: N): T
 
given depType0: DepType[0] with
  type T = Int
  override def apply(x: 0) = 10
 
given depType1: DepType[1] with
  type T = String
  override def apply(x: 1) = "abc"
 
given depTypeN[N <: Nat]: DepType[S[S[N]]] with
  type T = Boolean
  override def apply(x: S[S[N]]) = true

object DepFunction:
  def apply[N <: Nat](x: N)(using depType: DepType[N]): depType.T = depType(x)

val _0: 0 = 0
val _1: 1 = 1
val _2: 2 = 2
val _3: 3 = 3

// @main
// def demo: Unit =
//     val x: Int = DepFunction(_0)
//     val y: String = DepFunction(_1)
//     val z: Boolean = DepFunction(_2)
 
//     //val t: Boolean = DepFunction(_1) // This does not compile!

case class DepPair[N <: Nat, V](x: N, value: V)(using depType: DepType[N] { type T = V })

// def demo2: Unit =
//   DepPair(_0, 10)
//   DepPair(_1, "a")
//   DepPair(_2, true)
//   //DepPair(_3, "b") //This does not compile!    
