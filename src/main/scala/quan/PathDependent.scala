package quan

type KTuple2[T1[_], T2[_]] = {
  type A
  type T = (T1[A], T2[A])
}
