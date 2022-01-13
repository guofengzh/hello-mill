package forall

object ForAllMain {
    
  def foo[A](a: A): List[A] = List(a)

  /*
  def bippy(
    s: String,
    i: Int,
    f: Forall[[A] => A => List[A]]): (List[String], List[Int]) =
        (f[String](s), f[Int](i))    

  def main(args:Array[String]): Unit = {
    val r = bippy("hi", 42, Forall[[A] => A => List[A]]([A] => (a:A)  => foo(a)))   // => (List("hi"), List(42))
    println(r)
  }
*/
}