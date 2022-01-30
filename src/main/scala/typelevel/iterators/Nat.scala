package typelevel.iterators

sealed trait Nat {
  type N <: Nat     // type member, carraying "result"
}

case object Z 
    extends Nat {type N = Z.type}
case class S[P <: Nat]() 
    extends Nat {type N = S[P]}

object Nat {
  type _0 = Z.type
  type _1 = S[_0]
  type _2 = S[_1]
  type _3 = S[_2]

  val _0: _0 = Z
  val _1: _1 = S[_0]()
  val _2: _2 = S[S[_0]]()
  val _3: _3 = S[S[S[_0]]]()

  /*
  //...
  transparent inline def toNat(n: Int): Nat = 
    n match {
      case 0 => Z
      case n if n > 0 => S(toNat(n - 1))
    }
  transparent inline def toInt[N <: Nat]: Int = 
    anyValue[N] match {
      case _: Z => 0
      case _: S[n] => toInt[n] + 1
    }
  */

}
