package typelevel.iterators

trait FinToNat[F <: Fin[?]]:

  type Out <: Nat

  def apply(): Out


/*
object FinToNat {
  // encode the "result" type, known as Aux pattern
  type Result[F <: Fin[?], N <: Nat] =  
    FinToNat[F] {type Out = N}

  // ...
}
*/

object FinToNat:
  type Result[F <: Fin[?], N <: Nat] = 
    FinToNat[F] {type Out = N}

  import Nat.*

  implicit def caseFZ[N <: S[?]]: 
    Result[FZ[N], _0] = new FinToNat[FZ[N]]:
    type Out = _0
    def apply() = _0

  implicit def caseFS[K <: S[?], 
                      F <: Fin[K], 
                      SoFar <: Nat](
    implicit finToNatK: Result[F, SoFar]): 
    Result[FS[K, F], S[SoFar]] =
    new FinToNat[FS[K, F]]:
      type Out = S[SoFar]
      def apply() = S[SoFar]()

  def apply[F <: Fin[?]](
    implicit finToNat: FinToNat[F]): 
    Result[F, finToNat.Out] = finToNat
