/**
 * the inlined code will be expanded in the place of usage.
 *   1, For methods their body will be inserted in the place of the call.
 *   2, For expressions, they will be evaluated on compile time.
 *   3, For method parameters they will be inserted in the place of usage (beware of 
 *      methods and side effects passed as inline parameters).
 */

package macros.inline

import scala.compiletime.summonInline

object Inlines:
  given context:String = "definition"

  inline def f(inline p: Int) =
    val ctx = summonInline[String]
    println(s"Inline1: parameter: $p, given $ctx")
    println(s"Inline2: parameter: $p, given $ctx")

  def g(p: Int) =
    val ctx = summonInline[String]
    println(s"Regular1: parameter: $p, given $ctx")
    println(s"Regular2: parameter: $p, given $ctx")

@main() def inlineTest() =
  import Inlines.*

  given context:String = "usage"
  
  var a = 1;
  def sideEffect() : Int =
    a += 1
    a
  val b = 2;
  f(sideEffect())
  g(sideEffect())
