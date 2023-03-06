/**
  * https://stackoverflow.com/questions/26729029/intersection-function-for-integer-set
  * https://github.com/xiaoyunyang/coursera-scala-specialization/blob/master/coursera-functional/src/week3/IntSet.scala
  */
package set

trait IntSet:
  def +:(x: Int): IntSet
  def :+(x: Int): IntSet
  def ∈:(x: Int): Boolean
   def ∪(other: IntSet): IntSet
   def ∩(other: IntSet) : IntSet

object Empty extends IntSet:
  def ∈:(x: Int): Boolean = false
  def +:(x: Int): IntSet = NonEmpty(x, Empty, Empty)
  def :+(x: Int): IntSet = NonEmpty(x, Empty, Empty)
  def ∪(other: IntSet): IntSet = other
  def ∩(other: IntSet): IntSet = Empty
  override def toString = "."

case class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet:
  def ∈:(x: Int): Boolean =
    if (x < elem) x ∈: left
    else if (x > elem) x ∈: right
    else true
  def +:(x: Int): IntSet =
    if (x < elem) new NonEmpty(elem, left :+ x, right)
    else if (x > elem) new NonEmpty(elem, left, right :+ x)
    else this

  def :+(x: Int): IntSet = x +: this

  override def toString = "{" + left + elem + right + "}"

  def ∪(other: IntSet): IntSet=
    ((left ∪ right) ∪ other) :+ elem

  def ∩(x: IntSet) : IntSet =
    val y = if (elem ∈: x) NonEmpty(elem, Empty, Empty) else Empty

    y ∪ ((left ∩ x) ∪ (right ∩ x))

@main
def tesIntSet: Unit = 
  val e1 = 1 +: 2 +: 3 +: 5 +: 9 +: 4 +: 7 +: Empty
  val e2 = 1 +: 3 +: 7 +: 9 +: Empty
  val e = e1 ∩ e2
  println(e)