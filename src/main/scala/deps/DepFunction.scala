package deps

sealed trait DepFunction[N <: Nat] {
  type T 
  def apply(x: N): T 
} 

given depFunction0: DepFunction[_0] with {
  type T = Int 
  def apply(x: _0) = 10 
} 

given depFunction1: DepFunction[_1] with {
  type T = String 
  def apply(x: _1) = "aaa" 
}

given [N <: Nat]:DepFunction[Succ[Succ[N]]] with {
  type T = Boolean 
  def apply(x: Succ[Succ[N]]) = true 
}

object DepProduct { 
  def apply[N <: Nat](x: N)(using depFunction: DepFunction[N]): depFunction.T = depFunction(x) 
} 

val x: Int = DepProduct(_0) 
val x1: String = DepProduct(_1) 
val x2: Boolean = DepProduct(_2) // val x3: String = DepProduct( 2) // error 
val x4: Boolean = DepProduct(_3) 

