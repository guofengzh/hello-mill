/**
 * https://stackoverflow.com/questions/65189756/scala-3-dotty-pattern-match-a-function-with-a-macro-quotation
 */

package marcos.tree

import scala.quoted.*

inline def getName[T](inline f: T => Any): String = ${getNameImpl('f)}

def getNameImpl[T](f: Expr[T => Any])(using Quotes): Expr[String] =
  import quotes.reflect.*
  val acc = new TreeAccumulator[String]:
    def foldTree(names: String, tree: Tree)(owner: Symbol): String = tree match
      case Select(_, name) => name
      case _ => foldOverTree(names, tree)(owner)
  val fieldName = acc.foldTree(null, f.asTerm )(Symbol.spliceOwner)
  Expr(fieldName)
