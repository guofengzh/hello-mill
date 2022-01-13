package quan

object TestKey {
  case class Key[A](label: String, default: A)

  val boolKey = Key[Boolean]("bool", false)
  val intKey = Key[Int]("int", 0)
  val strKey = Key[String]("str", "")

  def main(args: Array[String]):Unit = {
      import scala.language.implicitConversions
      given KTuple2[T1[_], T2[_], A]: Conversion[(T1[A], T2[A]), KTuple2[T1, T2]#T] = _.asInstanceOf[KTuple2[T1, T2]#T]

      val optionMap = MapK[Key, Option](
         boolKey -> Some(true),
         intKey -> Some(1),
         strKey -> Option("a"),
         strKey -> None
      )

      println(optionMap(intKey) == Some(1))
      println(optionMap(boolKey) == Some(true))

      optionMap.foreach { (k, v) =>
         doSomething(k, v)
         doSomething(k, k.default)
    }
  }

    // Just test that this kind of signature will work
  def doSomething[A, K[_], V[_]](k: K[A], v: V[A]): Unit = {
    //println(s"$k -> $v")
  }
}
