package singletontypes

import scala.compiletime.ops.any.==
import scala.compiletime.ops.boolean.&&
import scala.compiletime.ops.int.+
import scala.compiletime.ops.int.-
import scala.compiletime.ops.int.<
import scala.compiletime.ops.int.>
import scala.compiletime.ops.int.>=

final case class Matrix2[R <: Int, C <: Int, T](row: R, col: C, elem: T)
   (using Matrix.NonNegativeDimensions[R, C])
   (using ValueOf[R], ValueOf[C])

object Matrix:
    type NonNegativeDimensions[R <: Int, C <: Int] = (R > 0 && C > 0) =:= true
