package forall

trait Exists[+F[_]] extends Serializable:
  type A
  val value: F[A]

object Exists:

  /**
   * A forgetful constructor which packs a concrete value into an existential.
   * This is mostly useful for explicitly assisting the compiler in sorting all
   * of this out.
   */
  def apply[F[_]]: PartiallyApplied[F] =
    new PartiallyApplied[F]

  final class PartiallyApplied[F[_]]:
    def apply[A0](fa: F[A0]): Exists[F] =
      new Exists[F]:
        type A = A0
        val value = fa
