package encoding

object ChurchNumerals extends App {

  type zero[s[_], z] = z
  type succ[m[s[_], z], s[_], z] = s[m[s, z]]

  type one[s[_], z] = succ[zero, s, z]
  type two[s[_], z] = succ[one, s, z]

  type plus = [m[_[_], _]] =>> [n[_[_], _]] =>> [s[_], z] =>> m[s, n[s, z]]
  type +[m[_[_], _], n[_[_], _]] = [s[_], z] =>> plus[m][n][s, z]
  
  type times = [m[_[_], _]] =>> [n[_[_], _]] =>> [s[_], z] =>> m[[z0] =>> n[s, z0], z]
  type *[m[_[_], _], n[_[_], _]] = [s[_], z] =>> times[m][n][s, z]

  // use typeclasses to fold over the type structure
  trait Nat[T]:
    def real: Int

  trait Succ[T]
  trait Zero

  given natForSucc[T](using N: Nat[T]): Nat[Succ[T]] with
    override def real: Int = N.real + 1

  given natForZero: Nat[Zero] with
    override def real: Int = 0

  def real[T](using N: Nat[T]): Int =
    N.real
    
  type expr[s[_], z] = (one + one + one)[s, z]
  type expr2[s[_], z] = (expr * expr)[s, z]

  println(real[expr[Succ, Zero]]) // 3

  println(real[expr2[Succ, Zero]]) // 9
  
}