package newsyn

/**
 * Scala 3: The _, They Are a’Changing
 * https://medium.com/scala-3/scala-3-the-they-are-achanging-32b1ebb90fa2
 */
object Underscore {

  /*
   * the underscore is used for placeholders in function arguments, 
   * and match expressions.
   */

  // unction parameter placeholder: _.toUpperCase
  val loud = Seq("hello world!").map(_.toUpperCase)

  // Match placeholder: _ in the first two case clauses
  Seq(Seq('a', 'b', 'c'), Seq(1, 2, 3), Seq(1.1, 2.2)).map { 
    seq => seq match { 
      case 'a' +: _ => "Found a"
      case head +: _ +: last +: _ => s"Found $head and $last"
      case head +: tail => s"Other list: $seq"
    }
  } 
  
  // for comprehension
  Seq(List("12345678", "1234", "12345678")).map {
    lst => for {
      _ <- lst
    } yield ()
  }

  // wild arguments of types, use "?"
  def tos(seq: Seq[?]): String = seq.mkString("[", ", ", "]") 

  // but still use "_" for higher-kind type parameter
  def str[T, S[_]](ss: S[T]): String = ss.toString

  // import statements
  //import Obj.{given Marker[Int]}   // Import just the Marker[Int]
  //import Obj.{given Marker[?]}     // Import all given Markers

  // New Import Syntax
  import scala.util.*                   // Instead of _
  import scala.concurrent.{given, *}    // Everything in concurrent, including givens
  import java.util.Queue as JQueue      // "as" replaces => and no braces needed for one import
  // use for _ is retained: hiding items.
  import java.util.{HashMap as _, *}    // Need {} for a list. Still use _ to hide HashMap  

  // Repeated Parameters Syntax
  def count[T](ts: T*): Int = ts.size

  val seq = Seq(1,2,3)
  count(seq)          // Returns 1!
  count(seq*)         // Returns 3. Use seq* instead of seq: _*

  Seq('a', 'b', 'c', 'd', 'e') match {
    // tail* means zero or more elements of the sequence
    case Seq('a', tail*) => s"Found a, $tail"   // Instead of Seq('a', tail: _*)
    // placeholder _ make us not give a name, and also to ignore them
    case Seq('b', _*) => "Found b"              // Instead of Seq('a', _: _*)
    case seq => s"Other list: $seq"
  }

  // '_': when we want to refer to a specific term/name, we will '_', 
  //      if we want to refer to any term, we use "*".
}

// see given instance
// https://docs.scala-lang.org/scala3/reference/contextual/givens.html