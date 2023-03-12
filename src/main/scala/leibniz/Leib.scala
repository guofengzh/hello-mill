/**
 * https://typelevel.org/blog/2014/07/02/type_equality_to_leibniz.html
 * 
 * The Leibniz’ equality law states that if a and b are identical then they must have identical properties. 
 * 
 * Leibniz’ original definition reads as follows:
 *     a ≡ b = ∀ f .f a ⇔ f b
 * and can be proven to be equivalent to:
       a ≡ b = ∀ f .f a → f b
 */
package leibniz

sealed trait Leib[A, B]:
  def subst[F[_]](fa: F[A]): F[B]


object Leib:
  given refl[A]: Leib[A, A] = new:
    def subst[F[_]](fa: F[A]): F[A] = fa

  //def symm[A, B](ab: Leib[A, B]): Leib[B, A]
  //def compose[A, B, C](ab: Leib[A, B], bc: Leib[B, C]): Leib[A, C]

def sum2[A](xs:List[A])(using ev: Leib[A, Int]): Int =
    ev.subst[List](xs).foldLeft(0)(_ + _)

def lift[F[_], A, B](ab: Leib[A, B]): Leib[F[A], F[B]] =
  ab.subst[[X] =>> Leib[F[A], F[X]]](Leib.refl[F[A]])
