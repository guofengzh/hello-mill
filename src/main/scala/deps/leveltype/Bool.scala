package deps.leveltype

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

@main def BoolTplMain: Unit =
  summon[False# Not =:= True]
  summon[False# && [True] =:= False]
  summon[False# || [True] =:= True]
  summon[False# IfElse[Any, Int, String] =:= String]
  //  implicitly[True# IfElse[Any, Int, String] =:= String] // This does not compile!
