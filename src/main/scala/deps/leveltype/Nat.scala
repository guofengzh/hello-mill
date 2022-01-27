package deps.leveltype

sealed trait Nat:
  type This >: this.type <: Nat
  type ++ = Succ[This]
  type + [_ <: Nat] <: Nat
  type * [_ <: Nat] <: Nat
 
object Zero extends Nat:
  type This = Zero
  type + [X <: Nat] = X
  type * [X <: Nat] = Zero
 
class Succ[N <: Nat] extends Nat:
  type This = Succ[N]
  type + [X <: Nat] = Succ[N# + [X]]
  type * [X <: Nat] = (N# * [X])# + [X]

type Zero   = Zero.type
type One    = Zero# ++
type Two    = One# ++
type Three  = Two# ++
type Four   = Three# ++
type Five   = Four# ++
type Six    = Five# ++

@main def NatMain: Unit =
  summon[Two# + [Three] =:= Five]
  summon[One# + [Two] =:= Three]
  summon[Two# * [Two] =:= Four]
  //  implicitly[Two# + [Three] =:= Four] // Does not compile
