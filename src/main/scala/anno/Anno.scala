package anno

import scala.quoted.*

object MacroAnno:

  inline def getAnnotations[A]: List[String] = ${getAnnotationsImpl[A]}

  def getAnnotationsImpl[A: Type](using Quotes): Expr[List[String]] =
    import quotes.reflect.*
    val annotations = TypeRepr.of[A].typeSymbol.annotations.map(_.tpe.show)
    Expr.ofList(annotations.map(Expr(_)))
