package typelevel

object NatMatchType {
  trait Nat
  class Zero extends Nat
  class Succ[A <: Nat] extends Nat
  
  type Plus[A <: Nat, B <: Nat] <: Nat = A match {
      case Zero => B
      case Succ[n] => Succ[Plus[n, B]]
  }

  type Multiply[A <: Nat, B <: Nat] = A match {
      case Zero => Zero
      case Succ[n] => Plus[B, Multiply[n, B]]
  }

  type One = Succ[Zero]
  type Two = Succ[One]
  type Three = Succ[Two]
  type Four = Succ[Three]
  type Five = Succ[Four]
  type Six = Succ[Five]

  def main(args: Array[String]): Unit =
    summon[Succ[One] =:= Two]  // it witness that Succ[One] and Two are equal
    summon[Plus[One, Two] =:= Three]
    summon[Multiply[Two, Three] =:= Six]
}
