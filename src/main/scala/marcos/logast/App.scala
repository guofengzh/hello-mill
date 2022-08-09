/**
 * Expand Code to Raw AST Scala 3 in the REPL
 * https://stackoverflow.com/questions/69420704/expand-code-to-raw-ast-scala-3-in-the-repl
 * 
 * and use it in REPL
 *
 *  sbt console
 *  scala> import marcos.logast.App.reify
 *  scala> reify{ class A }
 */

package marcos.logast

import scala.quoted.*

object App:
  inline def reify(inline a: Any) = ${reifyImpl('a)}

  def reifyImpl(a: Expr[Any])(using Quotes): Expr[String] =
    import quotes.reflect.*
    Literal(StringConstant(Printer.TreeStructure.show(a.asTerm))).asExprOf[String]
