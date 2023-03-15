package CAT

trait MyChoice[F[_, _]]:
  def choice[A, B, C](fac: F[A, C], fbc: F[B, C]): F[Either[A, B], C]


