package example

class FoldFusion {

}

object FoldFusion {
  final case class FoldOp[Z, R, A](init: R, update: (R, Z) => R, transform: R => A)

  implicit class FoldOpSyntax[Z](zs: Seq[Z]) {
    def runFold[R, A](op: FoldOp[Z, R, A]): A = op.transform(zs.foldLeft(op.init)(op.update))
  }

  implicit class FoldOpZip[Z, R, A](op: FoldOp[Z, R, A]) {
    def map2[S, B, C](other: FoldOp[Z, S, B])(f: (A, B) => C): FoldOp[Z, (R, S), C] =
      FoldOp( (op.init, other.init),
        (r, z) => (op.update(r._1, z), other.update(r._2, z)),  // updater
        r=>f(op.transform(r._1), other.transform(r._2))         // transformer
      )
  }

  implicit class FoldOpMath[Z, R](op: FoldOp[Z, R, Double]) {
    def binaryOp[S](other: FoldOp[Z, S, Double])(f: (Double, Double) => Double): FoldOp[Z, (R, S), Double] =
      op.map2(other) { case (x, y) => f(x, y) }
    def +[S](other: FoldOp[Z, S, Double]): FoldOp[Z, (R, S), Double] = op.binaryOp(other)(_ + _)
    def /[S](other: FoldOp[Z, S, Double]): FoldOp[Z, (R, S), Double] = op.binaryOp(other)(_ / _)
  }

  val sum = FoldOp[Double, Double, Double](0, (s, i) => s + i, identity)
  val length = FoldOp[Double, Double, Double](0, (s, _) => s + 1, identity)

  def main(args: Array[String]): Unit = {
    val res = Seq(1.0, 2.0, 3.0).runFold(sum / length)
    println(res)
  }

}