package marcos

import scala.quoted.*

inline def debug(inline exprs: Any*): Unit = ${debugImpl('exprs)}

private def debugImpl(exprs: Expr[Seq[Any]])(using Quotes): Expr[Unit] = 
  def showWithValue(e: Expr[?]): Expr[String] = 
    '{${Expr(e.show)} + " = " + $e}

  import quotes.reflect.*

  // inspect the passed exprs tree and verify if it corresponds to multiple parameters passed as varargs.
  // we get a sequence of trees (from Expr[Seq[Any]], we get Seq[Expr[Any]]).
  val stringExps: Seq[Expr[String]] = exprs match 
    // inspect the type construct, not the shape of the code
    case Varargs(es) => 
      es.map { e =>
        // if we want to inspect the shape of the code that was passed in (the Abstract Syntax Tree), 
        // we’ll have to match on the expression’s term 
        e.asTerm match {
          case Literal(c: Constant) => Expr(c.value.toString)
          case _ => showWithValue(e)
        }
      }
    case e => List(showWithValue(e))

  // converting the stringExps: Seq[Expr[String]] to an Expr[String] by generating code which 
  // will concatenate all of the strings. Two string expressions can be concatenated by splicing
  // both expressions, combining them as any other two strings, and quoting the result.
  val concatenatedStringsExp = stringExps
    .reduceOption((e1, e2) => '{$e1 + ", " + $e2})
    .getOrElse('{""})

  '{println($concatenatedStringsExp)}
