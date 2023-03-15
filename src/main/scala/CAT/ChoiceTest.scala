package CAT

import cats.syntax.all.*

@main
def ChoiceTest: Unit =
    val b: Boolean => String = _.toString + " is a boolean"
    val i: Int => String =  _.toString + " is an integer"
    val f: (Either[Boolean, Int]) => String = b ||| i

    println(f(Right(3)))

    println(f(Left(false)))

