/**
 * Scala: Seq, Map and Set as functions
 * https://www.alessandrolacava.com/blog/scala-seq-map-and-set-as-functions/
 * 
 * PartialFunction is a Function that will just throw for those inputs the partial function 
 * is not defined at. So you can use a PartialFunction wherever a Function is expected. 
 * Just keep in mind you’ll get an exception for some input values.
 * 
 *     trait PartialFunction[-A, +B] extends (A => B) ...
 * 
 * Seqs and Maps are partial functions while Set is a function. 
 */
package coll

val m = Map("a" -> 1, "b" -> 2, "c" -> 3)
val l = List("a", "c", "b")
val r = l collect m   // List: def collect[B](pf: PartialFunction[A, B]): List[B]
// Map is a PartialFunction.

// Seq[A] as PartialFunction[Int, A]
val xs = List("a", "c", "b")
val f1: PartialFunction[Int, String] = xs
val r1 = f1(0)  // a

// Map[A, B] as PartialFunction[A, B]
// val m = Map("a" -> 1, "b" -> 2, "c" -> 3, "d" -> 4)
val f2: PartialFunction[String, Int] = m
val r2 = f2("a") // 1

// Set[A] as A => Boolean, NOTE: Set as Function
val s = Set("a", "b", "c")
val f3: String => Boolean = s
val r3 = f3("a") // true

// so you can use a set to filter a list:
val xs4 = List("a", "c", "b")
val s4 = Set("a", "b", "d")
val r4 = xs4 filter s4 // List(a,b)
