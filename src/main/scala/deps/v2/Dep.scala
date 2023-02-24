/**
 * Maths - Dependent Types
 * https://www.euclideanspace.com/maths/discrete/types/dependent/index.htm
 * 
 * Dependent types in Scala
 * https://partialflow.wordpress.com/2017/07/26/dependent-types-type-level-programming/
 * 
 * This cannot work, because int.+ is not inducable like Succ in DepTT.scala
 */
package deps.v2

import scala.compiletime.ops.int

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

// for any other natural number
given depTypeN[N <: Nat & Singleton]: DepType[int.+[N, 2]] with
  type T = Boolean
  override def apply(x: int.+[N,2]) = true

object DepFunction:
  def apply[N <: Nat & Singleton](x: N)(using depType: DepType[N]): depType.T = depType(x)

val _0: 0 = 0
val _1: 1 = 1
val _2: 2 = 2
val _3: 3 = 3

// @main
// def demo: Unit =
val x: Int = DepFunction(_0)
val y: String = DepFunction(_1)
//val z: Boolean = DepFunction(_3)
 
//val t: Boolean = DepFunction(_1) // This does not compile!

case class DepPair[N <: Nat  & Singleton, V](x: N, value: V)(using depType: DepType[N] { type T = V })

// def demo2: Unit =
val d = DepPair(_0, 10)
val e = DepPair(_1, "a")
//val f = DepPair(_2, true)
//val g = DepPair(_3, "b") //This does not compile!    
