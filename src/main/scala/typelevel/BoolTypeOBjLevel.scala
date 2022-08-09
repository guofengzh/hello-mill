package typelevel

// https://scala-slick.org/talks/scalaio2014/Type-Level_Computations.pdf

object BoolTypeLevel:

  sealed trait Bool:
    type Not <: Bool
    type && [B <: Bool] <: Bool
    type || [B <: Bool] <: Bool
    type IfElse [C, T <: C, F <: C] <: C

  type True = True.type
  type False = False.type

  object True extends Bool:
    type Not = False
    type && [B <: Bool] = B
    type || [B <: Bool] = True
    type IfElse [C, T <: C, F <: C] = T

  object False extends Bool:
    type Not = True
    type && [B <: Bool] = False
    type || [B <: Bool] = B
    type IfElse [C, T <: C, F <: C] = F

  summon[False# Not =:= True]
  summon[False# && [True] =:= False]
  summon[False# || [True] =:= True]
  summon[False# IfElse[Any, Int, String] =:= String]
//  implicitly[True# IfElse[Any, Int, String] =:= String] // This does not compile!


object BoolValueLevel:

  sealed trait Bool:
    def not: Bool
    def &&(b: Bool): Bool
    def ||(b : Bool): Bool
    def ifElse[C](t: => C, f: => C): C

  case object True extends Bool:
    override def not = False
    override def &&(b: Bool) = b
    override def ||(b: Bool) = True
    override def ifElse[C](t: => C, f: => C) = t

  case object False extends Bool:
    override def not = True
    override def &&(b: Bool) = False
    override def ||(b: Bool) = b
    override def ifElse[C](t: => C, f: => C) = f

  False.not == True
  False.&&(True) == False
  False.||(True) == True
  False.ifElse(1, 2) == 2

