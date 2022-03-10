/**
 * Parameter Untupling
 * https://docs.scala-lang.org/scala3/reference/other-new-features/parameter-untupling.html
 * https://dotty.epfl.ch/docs/reference/other-new-features/parameter-untupling.html
 *  
 */

package hello

object TFP {
    def insert(rowItems: Tuple) : Unit ={
      println(rowItems.toList)
    }

    def combine(i: Int, j: Int) = i + j

}

@main def TFPEntry: Unit = {
    import TFP.*
    
    insert(1, "Hello", 5.4)  // the parameter is wrapped in a Tuple value

    val xs = List((1,3), (2,4))

    // in Scala 3, the following three ways are allowed
    val a = xs map {
       case (x, y) => x + y // 1, pattern-matching decomposition
    }
    println(a)

    val b = xs.map {
        (x, y) => x + y    // 2, a shorter and clearer alternative
    }
    println(b)

    val c = xs.map(_ + _)  // 3, equivalently
    println(c)

    // Generally, a function value with n > 1 parameters is wrapped in a function type 
    // of the form ((T_1, ..., T_n)) => U if that is the expected type. The tuple parameter
    // is decomposed and its elements are passed directly to the underlying function.
    val r = xs map combine
    println(r)

    // More specifically, the adaptation is applied to the mismatching formal parameter 
    // list. In particular, the adaptation is **not a conversion** between function types. 
    // That is why the function value must be explicitly tupled, rather than the parameters
    // untupled:
    val combiner: (Int, Int) => Int = _ + _
    val s = xs.map(combiner.tupled)     // Type Mismatch
    println(s)

    // A conversion may be provided in user code:
    import scala.language.implicitConversions
    transparent inline implicit def `fallback untupling`(f: (Int, Int) => Int): ((Int, Int)) => Int =
       p => f(p._1, p._2)     // use specialized apply instead of unspecialized `tupled`

    val t = xs.map(combiner)
    println(t)
}