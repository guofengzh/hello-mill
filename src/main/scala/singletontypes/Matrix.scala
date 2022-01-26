package singletontypes

/**
 * https://engineering.zalando.com/posts/2018/10/scala-three-experiment.html
 */

/*final case class Matrix(n: Int, m: Int) {
  def *(other: Matrix): Matrix = {
    require(m == other.n,
        s"matrix dimensions must fit ($m != ${other.n})")
    Matrix(n, other.m)
  }
}*/

type Dim = Singleton & Int

final case class Matrix[A <: Dim, B <: Dim](n: A, m: B):
  def *[C <: Dim](other: Matrix[B, C]): Matrix[A, C] =
    Matrix(n, other.m)

@main def TestMatrix : Unit =
  val a = Matrix(2, 4) ;
  val b = Matrix(4, 3)

  val m1 = a * b

  //val m2 = b * a
