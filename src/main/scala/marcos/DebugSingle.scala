package marcos

import scala.quoted.Expr
import scala.quoted.Quotes

inline def debugSingle(inline expr: Any): Unit = 
  ${debugSingleImpl('expr)} 
  
private def debugSingleImpl(expr: Expr[Any])(
  using Quotes): Expr[Unit] = 
  '{ println("Value of " + ${Expr(expr.show)} + " is " + $expr) }
