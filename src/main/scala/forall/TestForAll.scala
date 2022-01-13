package forall

type PF = [A] => A => List[A]
type PP[A] = PF

def foo[A](a: A): List[A] = List(a)

/*
def bippy(
    s: String,
    i: Int,
    f: Forall[PP]): (List[String], List[Int]) =
        (f[String](s), f[Int](i))    

@main def TestForAll: Unit = {
    val r = bippy("hi", 42, Forall[PP]([A] => (a:A)  => foo(a)))   // => (List("hi"), List(42))
    println(r)
}
*/