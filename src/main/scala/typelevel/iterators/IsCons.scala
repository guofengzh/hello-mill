package typelevel.iterators

trait IsCons[V <: Vect[Len, Elem], 
             Len <: Nat, 
             Elem]:
  type XS <: Vect[?, Elem]   // capture type of tail

  def x(vect: V): Elem
  def xs(vect: V): XS

object IsCons:
  type Result[V <: Vect[S[Len], Elem],
              Len <: Nat,
              Elem,
              XS0 <: Vect[Len, Elem]] =
    IsCons[V, S[Len], Elem] { type XS = XS0 }   // missing type-level pattern match


