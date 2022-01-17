package deps

sealed trait Nat { 
  type This >: this.type <: Nat 
  type ++ = Succ[This] 
} 

object Zero extends Nat {
  type This = Zero 
} 

type Zero = Zero.type 

final class Succ[N <: Nat] extends Nat {
  type This = Succ[N] 
} 

type _0 = Zero 
type _1 = _0 # ++ 
type _2 = _1 # ++ 
type _3 = _2 # ++ 
val _0: _0 = Zero 
val _1: _1 = new Succ[_0] 
val _2: _2 = new Succ[_1] 
val _3: _3 = new Succ[_2]

