package lambda.maybe

/**
 * 
 */
object Maybe:
  // nothing =    λn.λj.n 
  val nothing =          (n :Int) => (j: Int => Int) => n
  // just = λx.λn.λj.j x
  val just = (x:Int) => (n :Int) => (j: Int => Int) => j(x)

  @main def testEither: Unit =
    def handlerN(a: Int) =
      println(s"nothing is $a")
      a 

    def handlerJ(a: Int) =
      println(s"just is $a")
      a

    val n = nothing
    val j = just(10) 

    println(n(0)(handlerN))
    println(j(0)(handlerJ))
