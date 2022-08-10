package DOT

// https://stackoverflow.com/questions/3508077/how-to-define-type-disjunction-union-types

object UnionEnc:
  type ¬[A] = A => Nothing
  
  type ∨[T, U] = ¬[¬[T] & ¬[U]] // De Morgan's law


  //  auxiliary constructs
  type ¬¬[A] = ¬[¬[A]]
  type |∨|[T, U] = { type λ[X] = ¬¬[X] <:< (T ∨ U) }

  def size[T : (Int |∨| String)#λ](t : T) = t.asInstanceOf[Matchable] match
    case i : Int => i
    case s : String => s.length

// or
// First, declare a class with the types you wish to accept as below:
class StringOrInt[T]
object StringOrInt:
  given IntWitness: StringOrInt[Int]()
  given StringWitness: StringOrInt[String]()


// Next, declare foo like this
object Bar:
  def foo[T <: Matchable: StringOrInt](x: T) = x match
    case _: String => println("str")
    case _: Int => println("int")

  def test: Unit = 
    foo(5)
    foo("abc")

  def fooScala3(xs: (String | Int)*) = xs foreach {
     case _: String => println("str")
     case _: Int => println("int")
  }
