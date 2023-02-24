/**
 * Maths - Dependent Types
 * https://www.euclideanspace.com/maths/discrete/types/dependent/index.htm
 * 
 * Dependent types in Scala
 * https://partialflow.wordpress.com/2017/07/26/dependent-types-type-level-programming/
 */
package deps.v2.ttt

import typelevel.* 

type Zero = _0
type One = _1
type Two = _2
type Three = _3
 
val _00: Zero = new Zero
val _11: One = new Succ[Zero]
val _22: Two = new Succ[One]
val _33: Three = new Succ[Two]

sealed trait DepType[N <: Nat]:
  type T
  def apply(x: N): T

given depType0: DepType[Zero] with
  type T = Int
  override def apply(x: Zero) = 10
 
given depType1: DepType[One] with
  type T = String
  override def apply(x: One) = "abc"
 
given depType[N <: Nat]: DepType[Succ[Succ[N]]] with
  type T = Boolean
  override def apply(x: Succ[Succ[N]]) = true

object DepFunction:
  def apply[N <: Nat](x: N)(using depType: DepType[N]): depType.T = depType(x)

val x: Int = DepFunction(_00)
val y: String = DepFunction(_11)
val z: Boolean = DepFunction(_22)
//val t: Boolean = DepFunction(_11) // This does not compile!
	
case class DepPair[N <: Nat, V](x: N, value: V)(implicit depType: DepType[N] { type T = V })

val d = DepPair(_00, 10)
val e = DepPair(_11, "a")
val f = DepPair(_22, true)
//val g = DepPair(_33, "b") //This does not compile!