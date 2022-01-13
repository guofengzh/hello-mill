package forall

import scala.annotation.unchecked.uncheckedVariance

private[forall] sealed trait Parent extends Serializable {
  private[forall] type Apply[A]
}

trait Forall[+F[_]] extends Parent { outer =>
  private[forall] type Apply[A] = F[A] @uncheckedVariance
  def apply[A]: F[A]
}

object Forall {
  private[Forall] type τ

  def apply[F[_]](ft: F[τ]): Forall[F] = {
    new Forall[F] {
      def apply[A] = ft.asInstanceOf[F[A]]
    }
  }


}
