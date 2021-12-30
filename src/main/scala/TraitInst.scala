trait TraitInst {
  
}

given ti: TraitInst() 

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

object < {
  given basic[B <: Nat]: <[零, Succ[B]]()
  given inductive[A <: Nat, B <: Nat](using lt: <[A, B]): <[Succ[A], Succ[B]]()
  def apply[A <: Nat, B <: Nat](using lt: <[A, B]) = lt
}

val validComparison = <[叁, 伍]

@main def traitInstTest: Unit = {
    println(validComparison)
    println("Done!")
}
