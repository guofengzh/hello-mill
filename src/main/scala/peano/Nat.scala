/**
  * https://raw.githubusercontent.com/xiaoyunyang/coursera-scala-specialization/master/coursera-functional/src/week4/Nat.scala
  */
package peano

//Peano Numbers
trait Nat:
  def isZero: Boolean
  def predecessor: Nat
  def successor: Nat = new NonZero(this) //holds no matter if Zero or NonZero
  def + (that: Nat): Nat
  def - (that: Nat): Nat

//Zero Natural Numbers
object Zero extends Nat:
  def isZero: Boolean = true
  def predecessor: Nat = throw new NoSuchElementException("Zero has no predecessor")
  def + (that: Nat): Nat = that
  def - (that: Nat): Nat =
    if(that.isZero) this
    else throw new Error("Cannot substract to get negative")

  override def toString = "0"

//NonZero Natural Numbers
case class NonZero(n: Nat) extends Nat:
  def isZero: Boolean = false
  def predecessor: Nat = n

  def + (that: Nat): Nat = new NonZero(this + that)
  def - (that: Nat): Nat =
    if(that.isZero) this
    else n - that.predecessor //this is the same as (this - that) because n = this.predessor
    
  override def toString =
    def count(n: Nat, ct: Int): Int = n.isZero match
      case true => ct
      case false => count(n.predecessor, ct+1)
    ""+count(this, 0)

object NonZero:
  def apply[T](x: Nat): Nat = new NonZero(x)

@main  
def mainNat: Unit =
    println(Zero)
    println(new NonZero(new NonZero(Zero)))
