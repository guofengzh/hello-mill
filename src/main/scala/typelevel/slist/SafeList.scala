package slists

object SafeListModule:
  import typelevel.NatMatchType.*
  sealed trait SafeList[N <: Nat, +A]:
    def length: Int
    def map[B](f:A => B): SafeList[N, B]
    def zip[B](that: SafeList[N, B]): SafeList[N, (A, B)]
    def concat[M <: Nat, B >: A](that: SafeList[M, B]): SafeList[Plus[N, M], B]
  end SafeList

  case object Nil extends SafeList[Zero, Nothing]:
    def length: Int = 0 
    def map[B](f:Nothing => B): SafeList[Zero, Nothing] = Nil
    def zip[B](that: SafeList[Zero, B]): SafeList[Zero, (Nothing, B)] = Nil
    def concat [M <:Nat, B >: Nothing](that: SafeList[M, B]): SafeList[M, B] = that
  end Nil

  case class Cons[N <: Nat, A](head: A, tail: SafeList[N, A]) extends SafeList[Succ[N], A]:
    def length: Int = 1 + tail.length
    def map[B](f: A => B): SafeList[Succ[N], B] = Cons(f(head), tail.map(f))
    def zip[B](that: SafeList[Succ[N], B]): SafeList[Succ[N], (A, B)] = that match 
      case Cons(h, t) => Cons((head, h), tail.zip(t)) 
    def concat[M <:Nat, B >: A](that: SafeList[M, B]): SafeList[Plus[Succ[N], M], B]
      = Cons(head, tail.concat(that))
    override def toString = s"$head :: ${tail.toString}"
  end Cons

  object SafeList:
    //extension[N <: Nat, A](head: A)
    //   def ::(tail: SafeList[N, A]): SafeList[Succ[N], A] = SafeListModule.::(head, tail)
  end SafeList

  def main(args: Array[String]):Unit =
    //val v1 = 1 :: 2 :: 3 :: 4 :: 5 :: Nil
    //val v2 = 6 :: 7 :: 8 :: 9 :: 10 :: Nil
    val v1 = Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Nil)))))
    val v2 = Cons(6, Cons(7, Cons(8, Cons(9, Cons(10, Nil)))))

    val v3 = v1.map((x:Int)=>x%2 == 0)
    println(v3)
    val zipped = v1.zip(v2)
    println(zipped)
  end main
end SafeListModule
