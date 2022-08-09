package typelevel.iterators

/*
 * This seems somewhat problematic: how to represent vectors of unknown length if length is 
 * part of the type? In dependent typing theory there is a device for this called Σ type 
 * (a.k.a. dependent pair)
 */

// Σ type can be expressed in Scala (awkwardly though)

trait DType[A]:
  type T

object DType:
  type **[A, T0] = DType[A] { type T = T0 }
  def apply[A, T0](): A ** T0 = new DType[A] { type T = T0 }

import DType.* 

abstract class DPair[A, B](implicit P: A ** B):
  def x: A
  def y: B

/*
// Σ type - Scala examples

sealed trait State
case class LoggedIn(user: User) extends State
case object LoggedOut extends State

class LoggedInActions(user: User) {
  def sayHello() = s"Hello ${user.name}!"
}
class LoggedOutActions {
  def logIn(password: String): User = ???
}

implicit val loggedIn: LoggedIn ** LoggedInActions = DType()
implicit val loggedOut: LoggedOut.type ** LoggedOutActions = DType()

val dPairLoggedIn = DPair(LoggedIn(User("Marcin")))(
                          x => new LoggedInActions(x.user))
val dPairLoggedOut = DPair(LoggedOut, new LoggedOutActions())

val noCheating = DPair(LoggedOut, new LoggedInActions(User("Marcin")))
// Error: could not find implicit value for 
// parameter P: LoggedOut.type ** LoggedInActions

---

import Nat._
implicit def mkDepVect[N <: Nat, A]: N ** Vect[N, A] = DType()

val anyVec = DPair(_2, (1 :: 2 :: NIL)) //OK
val noCheating = DPair(_3, (1 :: 2 :: NIL))  //does not compile
//Error:(53, 24) could not find implicit value for 
//parameter P: Nat._3 ** Vect[Nat._2,Int]

So theoretically we could represent vectors of length dependent on a "variable" in Scala. Unfortunately, ...

---

Scala vs Idris

In Scala you can't assert that a type structure is to be deduced from a runtime structure, 
while in Idris you can.

Even though you can express dependent vectors in Scala, it is not practical because you can only 
express them if their length is statically known (not really the case for vectors) and give up 
all the transformations that can't keep track of size (ie. filter)

*/

/**
 * Scala vs Idris
 */
sealed trait Format
case class Num[F <: Format](rest: F) 
    extends Format
//...
case class Str[F <: Format](rest: F) 
    extends Format
case class Lit[F <: Format](string: String, rest: F) 
    extends Format
case object End extends Format

trait PrintfType[F <: Format]:
  type Out
  def apply(fmt: F): String => Out
object PrintfType:
  type Result[F <: Format, Out0] = PrintfType[F] { type Out = Out0 }

  implicit val endFmt: Result[End.type, String] = new PrintfType[End.type]:
    type Out = String
    def apply(fmt: End.type) = identity
  // ...  


import PrintfType.*
//...
implicit def numberFmt[Fmt <: Format, Rest](
    implicit printfFmt: Result[Fmt, Rest])
  : Result[Num[Fmt], Int => Rest] =
  new PrintfType[Num[Fmt]]:
    type Out = Int => Rest
    def apply(fmt: Num[Fmt]) =
      (acc: String) => (i: Int) => printfFmt(fmt.rest)(acc + i)
//...
implicit def litFmt[Fmt <: Format, Rest](
    implicit printfFmt: Result[Fmt, Rest]): Result[Lit[Fmt], Rest] =
  new PrintfType[Lit[Fmt]]:
    type Out = Rest
    def apply(fmt: Lit[Fmt]) =
      (acc: String) => printfFmt(fmt.rest)(acc + fmt.string)

def apply[F <: Format](format: F)(
    implicit printfType: PrintfType[F]): printfType.Out =
  printfType(format)("")

/*

val fmt1 = Str(Lit(" = ", Num(End)))
val t1: String => Int => String = PrintfType(fmt1)
println (t1("answer")(10))
val fmt2 = Str(Lit(" = ", Dbl(End)))
val t2: String => Double => String = PrintfType(fmt2)
println (t2("answer")(10.0))

sprintf looks more practical because you can enjoy increased type-safety with no downside being 
limited to String literals (but you must employ a macro to parse format string)

*/

