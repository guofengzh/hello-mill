package lambda.either

/**
 *  left = λa.λl.λr.l a
 * right = λb.λl.λr.r b
 * 
 * The contract is that, similar to Maybe, the l function argument represents the 
 * left case, whereas the r argument represents the right case. Contrary to Maybe, 
 * both l and r are used as functions. (Everything in lambda calculus is a function, 
 * but we don't always use the arguments as the function that they are.)
 * 
 * The idea, as usual, is that you can partially apply left and right. Such a partially 
 * applied function is a function that still takes the two arguments l and r.
 * 
 * In both cases, you have a function of the form λl.λr.[...]. If you've been given 
 * such a function by an external source, you may not know if it's a left or a right 
 * expression, and that's the point. You must ***supply handlers*** (l and r) that cover 
 * all possible cases.
 * 
 * Boolean values, natural numbers, Maybe, and Either. Common to all four examples is that
 * the data type in question consists of two mutually exclusive cases. 
 */
object Either:
  val left = (a: Int) => (l : Int => Int) => (r: Int=>Int) => l(a)

  val right= (a: Int) => (l : Int => Int) => (r: Int=>Int) => r(a)

  @main def testEither: Unit =
    def handlerL(a: Int) =
      println(s"left is $a")
      a 

    def handlerR(a: Int) =
      println(s"right is $a")
      a
    
    val la = left(5)
    val ra = right(10) 

    println(la(handlerL)(handlerR))
    println(ra(handlerL)(handlerR))
