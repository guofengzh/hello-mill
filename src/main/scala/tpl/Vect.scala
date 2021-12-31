package tpl

object VectDemo:
  import NatMatchType.*
  sealed trait Vect[N <: Nat, +A]:
      def length: Int
      def map[B](f:A => B): Vect[N, B]
      def zip[B](that: Vect[N, B]): Vect[N, (A, B)]
      def concat[M <: Nat, B >: A](that: Vect[M, B]): Vect[Plus[N, M], B]
  end Vect

  case object Empty extends Vect[Zero, Nothing]:
      def length: Int = 0 
      def map[B](f:Nothing => B): Vect[Zero, Nothing] = Empty
      def zip[B](that: Vect[Zero, B]): Vect[Zero, (Nothing, B)] = Empty
      def concat[M <: Nat, B >: Nothing](that: Vect[M, B]): Vect[M, B] = that
  end Empty

  case class Cons[N <: Nat, A](head: A, tail: Vect[N, A]) extends Vect[Succ[N], A]:
      def length: Int = 1 + tail.length
      def map[B](f: A => B): Vect[Succ[N], B] = Cons(f(head), tail.map(f))
      def zip[B](that: Vect[Succ[N], B]): Vect[Succ[N], (A, B)] = that match 
          case Cons(h, t) => Cons((head, h), tail.zip(t)) 
      def concat[M <:Nat, B >: A](that: Vect[M, B]): Vect[Plus[Succ[N], M], B]
          = Cons(head, tail.concat(that))
      override def toString = s"$head :: ${tail.toString}"
  end Cons

  object Vect:
      extension[N <: Nat, A](head: A)
         def ::(tail: Vect[N, A]): Vect[Succ[N], A] = Cons(head, tail)
  end Vect

  def main(args: Array[String]): Unit = 
      val v1 = 1 :: 2 :: 3 :: 4 :: 5 :: Empty
      val v2 = 6 :: 7 :: 8 :: 9 :: 10 :: Empty

      val zipped = v1.zip(v2)
      println(zipped)
  end main
end VectDemo