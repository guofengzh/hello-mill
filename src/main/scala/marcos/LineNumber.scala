/**
 * https://users.scala-lang.org/t/basic-macro-for-line-number/7833/7
 */

package marcos

import scala.quoted.*

object LineNumber {

  /** Macro for getting the file name and line number of the source code position. */
  inline def posnStr(): String = ${ posnStrImpl }

  def posnStrImpl(using Quotes): Expr[String] =
  {
    val pos = quotes.reflect.Position.ofMacroExpansion
    Expr(pos.sourceFile.path + ":" + (pos.startLine + 1).toString)
  }
}
