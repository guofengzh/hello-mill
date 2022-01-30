package typelevel.iterators

trait NatToFin[M <: Nat, N <: S[?]] {
  type Out <: Fin[N]
  def apply(): Out
}

object NatToFin {
  type Result[M <: Nat,    // encode the "result" type, known as Aux pattern
              N <: S[?], 
              F <: Fin[N]] = 
    NatToFin[M, N] {
      type Out = F
    }
  // ...

  import Nat.*

  implicit def caseZSj[J <: S[?]]: 
    Result[_0, J, FZ[J]] = 
      new NatToFin[_0, J] {
        type Out = FZ[J]
        def apply(): Out = FZ[J]()
      }

  implicit def caseSkSj[K <: Nat, J <: S[?]](
    implicit natToFinKJ: NatToFin[K, J]):
    Result[S[K], S[J], FS[J, natToFinKJ.Out]] =
      new NatToFin[S[K], S[J]] {
        type Out = FS[J, natToFinKJ.Out]
        def apply(): Out = FS[J, natToFinKJ.Out]()
      }

  def apply[M <: Nat, N <: S[?]](
      implicit natToFin: NatToFin[M, N]): 
      Result[M, N, natToFin.Out] = natToFin  
}


