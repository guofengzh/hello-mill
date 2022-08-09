package typelevel.iterators

import Nat.*

//sealed trait Vect[Len <: Nat, +Elem] {
//  type Repr <: Vect[Len, Elem]
//}

sealed trait Vect[Len <: Nat, +Elem]:
  type Repr <: Vect[Len, Elem]
  protected def _this: Repr

  def head[Elem0 >: Elem](
    implicit ev: IsCons[Repr, Len, Elem0]): 
    Elem0 =
    ev.x(_this)   // these come from the implicit evidence - (S len) is not enough
  def tail[Elem0 >: Elem](
    implicit ev: IsCons[Repr, Len, Elem0]): 
    ev.XS =       // these come from the implicit evidence - (S len) is not enough
    ev.xs(_this)

case object NIL extends Vect[_0, Nothing]:
  type Repr = NIL.type

  protected override def _this: Repr = this    // ??? my own gusess

  def ::[Elem](elem: Elem): 
    Cons[_0, Elem, this.type] = 
    Cons(elem, this)
case class Cons[Len <: Nat, 
                Elem, 
                XS <: Vect[Len, Elem]](
                head: Elem, tail: XS)
    extends Vect[S[Len], Elem] {
  type Repr = Cons[Len, Elem, XS]

  protected override def _this: Repr = this   // ??? my own gusess

  def ::[Elem0 >: Elem](head: Elem0): 
    Cons[S[Len], Elem0, this.type] =
    Cons(head, this)
}