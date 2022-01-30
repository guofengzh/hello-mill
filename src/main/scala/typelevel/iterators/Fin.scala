package typelevel.iterators

sealed trait Fin[SK <: S[?]] // type indexed by integer

case class FZ[SK <: S[?]]() 
    extends Fin[SK]
case class FS[K <: S[?], F <: Fin[K]]() // type-level recursion
    extends Fin[S[K]]

object Fin {
  def finToNat[F <: Fin[?]](f: F)(
    implicit finToNat: FinToNat[F]):  // encode computation as implicit search
    finToNat.Out = finToNat()         // 1. preserve structure representation

  def natToFin[M <: Nat, N <: S[?]](
    implicit natToFin: NatToFin[M, N]): 
    natToFin.Out = natToFin()

  def natToFin[M <: Nat, N <: S[?]](m: M, 
                                    n: N)(
    implicit natToFin: NatToFin[m.N, n.N]): 
    natToFin.Out = natToFin()    

}

/*
scala> FS[_4, FZ[_4]]
res1: FS[Nat._4,FZ[Nat._4]] = FS()

scala> Fin.finToNat(res1)
res2: S[Z.type] = S()                // 2. preserve structure representation
*/

/*
scala> Fin.natToFin(_2, _3)
res1: FS[S[S[Z.type]],...] = FS()

scala> Fin.natToFin(_6, _2)
error: could not find implicit value 
       for parameter natToFin: 
       NatToFin[Nat._6.N,Nat._2.N]
       Fin.natToFin(_6, _2)
*/    

