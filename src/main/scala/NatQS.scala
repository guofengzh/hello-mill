trait HList
class HNil extends HList
class ::[H <: Nat, T <: HList] extends HList

trait Concat[HA <: HList, HB <: HList, O <: HList]
object Concat {
  given basicEmpty[L <: HList]: Concat[HNil, L, L] with {}
  given inductive[N <: Nat, HA <: HList, HB <: HList, O <: HList](using Concat[HA, HB, O]): Concat[N :: HA, HB, N :: O] with {}
  // a summon-like apply method
  def apply[HA <: HList, HB <: HList, O <: HList](using concat: Concat[HA, HB, O]): Concat[HA, HB, O] = concat
}

// To test
val concat = Concat[零 :: 壹 :: HNil, 贰 :: 叁 :: HNil, 零 :: 壹 :: 贰 :: 叁 :: HNil]

trait Partition[HL <: HList, L <: HList, R <: HList] // pivot included in left
object Partition {
  given basic: Partition[HNil, HNil, HNil] with {}
  given basic2[N <: Nat]: Partition[N :: HNil, N :: HNil, HNil] with {}
  given inductive[P <: Nat, N <: Nat, T <: HList, L <: HList, R <: HList](using P <= N, Partition[P :: T, P :: L, R]): Partition[P :: N :: T, P :: L, N :: R] with {}
  given inductive2[P <: Nat, N <: Nat, T <: HList, L <: HList, R <: HList](using N < P, Partition[P :: T, P :: L, R]): Partition[P :: N :: T, P :: N  :: L, R] with {}
  def apply[HL <: HList, L <: HList, R <: HList](using partition: Partition[HL, L, R]): Partition[HL, L, R] = partition
}

val partition = Partition[贰 :: 叁 :: 壹 :: HNil, 贰 :: 壹 :: HNil, 叁 :: HNil]

trait QSort[HL <: HList, Res <: HList]
object QSort {
  given basicEmpty: QSort[HNil, HNil] with {}
  // given basicOne[N <: Nat]: QSort[N :: HNil, N :: HNil] with {}
  given inductive[N <: Nat, T <: HList, L <: HList, R <: HList, SL <: HList, SR <: HList, O <: HList]
    (
      using
      Partition[N :: T, N :: L, R],
      QSort[L, SL],
      QSort[R, SR],
      Concat[SL, N :: SR, O]
    ): QSort[N :: T, O] with {}

  def apply[HL <: HList, Res <: HList](using sort: QSort[HL, Res]): QSort[HL, Res] = sort
}

val sortTest = QSort[_3 :: _2 :: _1 :: _4 :: HNil, _1 :: _2 :: _3 :: _4 :: HNil] 
val sortTest2 = QSort[_1 :: _2 :: _3 :: _4 :: HNil, _1 :: _2 :: _3 :: _4 :: HNil]
val sortTest3 = QSort[_4 :: _3 :: _2 :: _1 :: HNil, _1 :: _2 :: _3 :: _4 :: HNil] 
val sortTest4 = QSort[_4 :: _4 :: _4 :: HNil, _4 :: _4 :: _4 :: HNil] 
