package mapk

object TestRecordKey:

   trait Record

   object Record:
     case class Person(name: String) extends Record
     case class Dog(name: String) extends Record

   import Record.*

   abstract class RecordType(name: String):
     type Rec <: Record
     val default: Rec  // A bit ridiculous, but just testing the limits

   object RecordType:
     // @TODO[WTF] Why does this not require `A <: Record`? Why does it fail if I specify that?
     type Kind[A] = RecordType { type Rec = A }

     case object PersonType extends RecordType("person"):
        override type Rec = Person
        override val default: Person = Person("Person")

     case object DogType extends RecordType("dog"):
        override type Rec = Dog
        override val default: Dog = Dog("Dog")

   case class RecordKey[A](rt: RecordType, name: String, default: A):
     type Rec = rt.Rec

   import RecordType.*
   object RecordKey:
     val AgeKey: RecordKey[Int] = RecordKey(PersonType, "age", 0)
     val NameKey: RecordKey[String] = RecordKey(PersonType, "name", "")

   def main(args: Array[String]):Unit =

      import scala.language.implicitConversions
      given KTuple2_P1[K[_], A]: Conversion[(K[A], A), KTuple2[K, Id]#T] = _.asInstanceOf[KTuple2[K, Id]#T]

      import RecordKey.*
      val recMap = MapK[RecordKey, Id](
              AgeKey -> 1,
              NameKey -> "a")

      println(recMap(AgeKey) == 1)
      println(recMap(NameKey) == "a")

      recMap.foreach { (k, v) =>
         doSomething(k, v)
         // doSomething(k, k.default) TODO
      }

    // Just test that this kind of signature will work
    def doSomething[A, K[_], V[_]](k: K[A], v: V[A]): Unit = {
      //println(s"$k -> $v")
    }
