package deps.levelobj

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

@main def BoolMain: Unit =
  False.not == True
  False.&&(True) == False
  False.||(True) == True
  False.ifElse(1, 2) == 2
