package logic

case class Not[A](func: A => Boolean) extends (A => Boolean):
  def apply(arg0: A) = !func(arg0)
