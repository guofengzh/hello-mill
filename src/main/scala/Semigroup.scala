import scala.annotation.targetName

trait Semigroup[T]:
  extension (t: T)
    infix def combine(other: T): T
    @targetName("plus") def <+>(other: T): T = t.combine(other)

trait Monoid[T] extends Semigroup[T]:
  def unit: T

/*
given StringMonoid: Monoid[String] with
  def unit: String = ""
  extension (s: String) infix def combine(other: String): String = s + other
*/
given StringMonoid2: Monoid[String] = new Monoid[String]:
  println("Initializing StringMonoid2")
  def unit: String = ""
  extension (s: String)
    infix def combine(other: String): String = s + other

given IntMonoid: Monoid[Int] with
  def unit: Int = 0
  extension (i: Int) infix def combine(other: Int): Int = i + other

@main def semigroupMain: Unit = {
  val v = "2" <+> ("3" <+> "4")
  println(v)
}