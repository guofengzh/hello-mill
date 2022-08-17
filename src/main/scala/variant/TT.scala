package tp.variant

sealed trait Animals 
case class Cat() extends Animals 
case class Dog() extends Animals

class Vet[+T](val favoriteAnimal: T) // OK, Types of vals Are in Covariant Position
class MutableSome[T](var contents: T) // OK, the generic type were invariant

abstract class MyList[+T]: // Types of Method Arguments Are in Contravariant Position 
  def head: T 
  def tail: MyList[T] 
  def add[S >: T](elem: S): MyList[T]  // widening: covariant type T occurs in contravariant position

abstract class Vet2[-T]:
  def rescueAnimal[S <: T](): S  // narrowing: contravariant type occurs in covariant position

@main def hello: Unit =
    println("Hello world!")


