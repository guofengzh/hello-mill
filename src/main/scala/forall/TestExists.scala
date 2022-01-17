package forall

object ExistsMain {
  
  def foo(unapplied: List[∃[[A] =>> (A, A => String)]]): List[String] =
    unapplied map { u =>
      val (v, f) = u.value
      f(v)
    }


  def main(args: Array[String]):Unit = {

    val r = foo(
              List(
                ∃[[A] =>> (A, A => String)]((42, (i: Int) => i.toString)),
                ∃[[A] =>> (A, A => String)](("hi", (s: String) => s))))

      println(r)  // List(42, hi)
  } 
  
}

