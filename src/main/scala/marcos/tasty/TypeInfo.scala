/**
 * TASTY way of (re)writing macros in Scala 3
 * https://medium.com/virtuslab/tasty-way-of-re-writing-macros-in-scala-3-3ce704a2c37c
 */
package marcos.tasty

import scala.quoted.*

object TypeInfo {
  inline def apply[T[_]]: String = ${ typeInfoImpl[T] }

  def typeInfoImpl[T[_]](using qctx: Quotes, tpe: Type[T]): Expr[String] = {
    import qctx.reflect.*

    val tpe = TypeRepr.of[T]
    
    val name = tpe.typeSymbol.name

    import compiletime.asMatchable
    // https://medium.com/scala-3/scala-3-safer-pattern-matching-with-matchable-f0396430ded6
    // The alternative to using asMatchable is to change the type parameter T for first[T] to 
    // be [X <: Matchable]. Then you can use x match ... in the body without problems.

    def fullTypeName(tpe: TypeRepr): String = tpe.asMatchable match
      case t: NamedType =>
        t.name
      case o: OrType =>
        fullTypeName(o.left) + " | " + fullTypeName(o.right)
      case o: AndType =>
        fullTypeName(o.left) + " & " + fullTypeName(o.right)
      case AppliedType(base, args) =>
        fullTypeName(base) + args.map(fullTypeName).mkString("[", ",", "]")

    val caseFields = tpe.typeSymbol.caseFields.map { s =>
      val name = s.name
      val tpe = s.tree match {
        case v: ValDef =>
          fullTypeName(v.tpt.tpe)
      }
      s"$name: $tpe"
    }

    Expr(
      s"$name(${caseFields.mkString(",")})"
    )
  }
}