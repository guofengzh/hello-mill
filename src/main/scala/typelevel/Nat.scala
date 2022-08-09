package typelevel
/**
 * Type-Level Programming in Scala 3, Part 1: Comparing Types
 * https://blog.rockthejvm.com/type-level-programming-scala-3/
 * https://blog.rockthejvm.com/type-level-quicksort/
 * 
 */
trait Nat
class _0 extends Nat
class Succ[A <: Nat] extends Nat

type _1 = Succ[_0]
type _2 = Succ[_1] // Succ[Succ[_0]]
type _3 = Succ[_2] // Succ[Succ[Succ[_0]]]
type _4 = Succ[_3] // ... and so on
type _5 = Succ[_4]

trait <[A <: Nat, B <: Nat]

object `<` :  // `:` after symbolic operator is deprecated; use backticks around operator insteadbloop
  given basic[B <: Nat]: <[_0, Succ[B]]()
  given inductive[A <: Nat, B <: Nat](using evidence: <[A, B]): <[Succ[A], Succ[B]]()
  def apply[A <: Nat, B <: Nat](using evidence: <[A, B]) = evidence

val ltTest = <[_3, _5]
lazy val ltTest2: <[_3, _5] = ???

trait <=[A <: Nat, B <: Nat]
object `<=` :
  given lteBasic[B <: Nat]: <=[_0, B]()
  given inductive[A <: Nat, B <: Nat](using lte: <=[A, B]): <=[Succ[A], Succ[B]]()
  def apply[A <: Nat, B <: Nat](using lte: <=[A, B]) = lte
end <=

val lteTest = <=[_3, _3]
val lteTest2 = <=[_3, _5]

// ADD NUMBER AS TYPES v1
object NatNotFigureOutResult:
  trait +[A <: Nat, B <: Nat, S <: Nat]  // S denote the sum of A and B
  object `+` :
    // 0 + 0 = 0
    given zero: +[_0, _0, _0]()
    // for every A <: Nat and A > 0, we have A + 0 = A and 0 + A = A
    given basicRight[A <: Nat](using _0 < A): + [_0, A, A]()
    given basicLeft[A <: Nat](using _0 < A): + [A, _0, A]()
    // if A + B = S, then Succ[A] + Succ[B] = Succ[Succ[S]]
    given inductive[A <: Nat, B <: Nat, S <: Nat](using plus: +[A, B, S]): +[Succ[A], Succ[B], Succ[Succ[S]]]()
    def apply[A <: Nat, B <: Nat, S <: Nat](using plus: +[A, B, S]) = plus
  end +

  val zero: +[_0, _0, _0] = +.apply[_0, _0, _0]
  val two: +[_2, _2, _4] = +.apply[_2, _2, _4]
  val two2: +[_2, _0, _2] = +.apply[_2, _0, _2]
  val four: +[_1, _3, _4] = +.apply[_1, _3, _4]
  /*
    - I need a given +[壹, 叁, 肆] == +[Succ[零], Succ[贰], Succ[Succ[贰]]] ( == inductive's return type)
    - the compiler can run inductive, but I need a given +[壹, 贰, 贰]
    - the compiler can run basicRight to construct a +[壹, 贰, 贰] (Note: the compiler can check  < 贰)
  */
  // val invalidFour: +[贰, 叁, 肆] = +.apply[贰, 叁, 肆]
end NatNotFigureOutResult

// ADD NUMBER AS TYPES v2
trait +[A <: Nat, B <: Nat] { type Result <: Nat} // Result denote the sum of A and B
object `+` :
  type Plus[A <: Nat, B <: Nat, S <: Nat] = +[A,B] {type Result = S}
  // 0 + 0 = 0
  given zero: Plus[_0, _0, _0] = new +[_0, _0] { type Result = _0}
  // for every A <: Nat and A > 0, we have A + 0 = A and 0 + A = A
  given basicRight[A <: Nat](using _0 < A): Plus[_0, A, A] = new +[_0, A] { type Result = A}
  given basicLeft[A <: Nat](using _0 < A): Plus[A, _0, A] = new +[A, _0] { type Result = A}
  // if A + B = S, then Succ[A] + Succ[B] = Succ[Succ[S]]
  given inductive[A <: Nat, B <: Nat, S <: Nat](using plus: Plus[A, B, S]) : Plus[Succ[A], Succ[B], Succ[Succ[S]]] = 
     new +[Succ[A], Succ[B]] { type Result = Succ[Succ[S]]}
  // def apply[A <: Nat, B <: Nat](using plus: +[A, B]) = plus
  def apply[A <: Nat, B <: Nat](using plus: +[A, B]): Plus[A, B, plus.Result] = plus
end +

val zero: +[_0, _0] = +.apply[_0, _0]
val two: +[_0, _2] = +.apply[_0, _2]
val two2: +[_2, _0] = +.apply[_2, _0]
val four: +[_1, _1] = +.apply[_1, _1]

@main def traitInstTest: Unit =
  println("Done!")
