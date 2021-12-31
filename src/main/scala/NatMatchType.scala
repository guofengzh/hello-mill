object NatMatchType {
  trait Nat
  class Zero extends Nat
  class Succ[A <: Nat] extends Nat
  
  type Plus[A <: Nat, B <: Nat] <: Nat = A match {
      case Zero => B
      case Succ[n] => Plus[n, B]
  }

  type Multiply[A <: Nat, B <: Nat] = A match {
      case Zero => Zero
      case Succ[n] => Plus[B, Multiply[n, B]]
  }

  type One = Succ[Zero]
  type Two = Succ[One]

  def main(args: Array[String]): Unit =
    summon[Succ[One] =:= Two]  // it witness that Succ[One] and Two are equal
}
