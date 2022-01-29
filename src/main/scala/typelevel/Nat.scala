package typelevel
/**
 * Type-Level Programming in Scala 3, Part 1: Comparing Types
 * https://blog.rockthejvm.com/type-level-programming-scala-3/
 * 
 * 
 */
trait Nat
class 零 extends Nat
class Succ[A <: Nat] extends Nat

type 壹 = Succ[零]  
type 贰 = Succ[壹] // Succ[Succ[零]]
type 叁 = Succ[贰] // Succ[Succ[Succ[零]]]
type 肆 = Succ[叁]
type 伍 = Succ[肆]
type 陆 = Succ[伍]
type 柒 = Succ[陆]
type 捌 = Succ[柒]
type 玖 = Succ[捌]
type 拾 = Succ[玖]
type 拾壹 = Succ[拾]
type 拾贰 = Succ[拾壹]

trait <[A <: Nat, B <: Nat]

given basic[B <: Nat]: <[零, Succ[B]]()
given inductive[A <: Nat, B <: Nat](using lt: <[A, B]): <[Succ[A], Succ[B]]()

object < :
  given basic[B <: Nat]: <[零, Succ[B]]()
  given inductive[A <: Nat, B <: Nat](using lt: <[A, B]): <[Succ[A], Succ[B]]()
  def apply[A <: Nat, B <: Nat](using lt: <[A, B]) = lt

val ltTest = <[叁, 伍]
lazy val ltTest2: <[叁, 伍] = ???

trait <=[A <: Nat, B <: Nat]
object <= :
  given lteBasic[B <: Nat]: <=[零, B]()
  given inductive[A <: Nat, B <: Nat](using lte: <=[A, B]): <=[Succ[A], Succ[B]]()
  def apply[A <: Nat, B <: Nat](using lte: <=[A, B]) = lte
end <=

val lteTest = <=[叁, 叁]
val lteTest2 = <=[叁, 伍]

// ADD NUMBER AS TYPES v1
object NatNotFigureOutResult:
  trait +[A <: Nat, B <: Nat, S <: Nat]  // S denote the sum of A and B
  object + :
    // 0 + 0 = 0
    given zero: +[零, 零, 零]()
    // for every A <: Nat and A > 0, we have A + 0 = A and 0 + A = A
    given basicRight[A <: Nat](using 零 < A): + [零, A, A]()
    given basicLeft[A <: Nat](using 零 < A): + [A, 零, A]()
    // if A + B = S, then Succ[A] + Succ[B] = Succ[Succ[S]]
    given inductive[A <: Nat, B <: Nat, S <: Nat](using plus: +[A, B, S]): +[Succ[A], Succ[B], Succ[Succ[S]]]()
    def apply[A <: Nat, B <: Nat, S <: Nat](using plus: +[A, B, S]) = plus
  end +

  val zero: +[零, 零, 零] = +.apply[零, 零, 零]
  val two: +[零, 贰, 贰] = +.apply[零, 贰, 贰]
  val two2: +[贰, 零, 贰] = +.apply[贰, 零, 贰]
  val four: +[壹, 叁, 肆] = +.apply[壹, 叁, 肆]
  /*
    - I need a given +[壹, 叁, 肆] == +[Succ[零], Succ[贰], Succ[Succ[贰]]] ( == inductive's return type)
    - the compiler can run inductive, but I need a given +[壹, 贰, 贰]
    - the compiler can run basicRight to construct a +[壹, 贰, 贰] (Note: the compiler can check  < 贰)
  */
  // val invalidFour: +[贰, 叁, 肆] = +.apply[贰, 叁, 肆]
end NatNotFigureOutResult

// ADD NUMBER AS TYPES v2
trait +[A <: Nat, B <: Nat] { type Result <: Nat} // Result denote the sum of A and B
object + :
  type Plus[A <: Nat, B <: Nat, S <: Nat] = +[A,B] {type Result = S}
  // 0 + 0 = 0
  given zero: Plus[零, 零, 零] = new +[零, 零] { type Result = 零}
  // for every A <: Nat and A > 0, we have A + 0 = A and 0 + A = A
  given basicRight[A <: Nat](using 零 < A): Plus[零, A, A] = new +[零, A] { type Result = A}
  given basicLeft[A <: Nat](using 零 < A): Plus[A, 零, A] = new +[A, 零] { type Result = A}
  // if A + B = S, then Succ[A] + Succ[B] = Succ[Succ[S]]
  given inductive[A <: Nat, B <: Nat, S <: Nat](using plus: Plus[A, B, S]) : Plus[Succ[A], Succ[B], Succ[Succ[S]]] = 
     new +[Succ[A], Succ[B]] { type Result = Succ[Succ[S]]}
  // def apply[A <: Nat, B <: Nat](using plus: +[A, B]) = plus
  def apply[A <: Nat, B <: Nat](using plus: +[A, B]): Plus[A, B, plus.Result] = plus
end +

val zero: +[零, 零] = +.apply[零, 零]
val two: +[零, 贰] = +.apply[零, 贰]
val two2: +[贰, 零] = +.apply[贰, 零]
val four: +[壹, 叁] = +.apply[壹, 叁]

@main def traitInstTest: Unit =
  println("Done!")
