package singleton

object DepValueM {
  
}

trait DepValue:
  type V
  val value: V

def magic(that: DepValue): that.V = that.value

class DepValueFace[T](x: T) extends DepValue:
  type V = T
  val value = x

@main def depValueTest: Unit =
   val depInt = DepValueFace(10)
   val resInt = magic(depInt)

   val depDouble = DepValueFace(10.10)
   val resDouble = magic(depDouble)
   