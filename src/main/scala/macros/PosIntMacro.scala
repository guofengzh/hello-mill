package macros

import scala.quoted.Expr
import scala.quoted.Quotes

object PosIntMacro:
  def isValid(i: Int): Boolean = i > 0

  inline def apply(inline x: Int): PosInt = ${applyImpl('{x})}

  def applyImpl(x: Expr[Int])(using quotes: Quotes): Expr[PosInt] =
    import quotes.reflect.report

    if ( isValid(x.valueOrAbort))
        '{PosInt.ensuringValid($x)}
    else
        report.errorAndAbort("Parameter must be natural number")
        '{PosInt.ensuringValid(0)}
