/**
 * Scala 3 Macro - Retain a generic type in runtime
 * https://stackoverflow.com/questions/69292402/scala-3-macro-retain-a-generic-type-in-runtime
 */

package marcos

import scala.quoted.*
import scala.reflect.ClassTag

trait ClassTagTest {
  def getClassTag[T](using Type[T], Quotes): Expr[ClassTag[T]] = {
    import quotes.reflect.*

    Expr.summon[ClassTag[T]] match {
      case Some(ct) =>
        ct
      case None =>
        report.error(
          s"Unable to find a ClassTag for type ${Type.show[T]}",
          Position.ofMacroExpansion
        )
        throw new Exception("Error when applying macro")
    }
  }
}
