package leanlike

/*
 * https://typista.org/lean-for-scala-programmers/
 * 
 * Lean:
 *
 * The expression
*     length ["a", "b"] = 2 
* is a proposition (of type Prop). There are many ways to create propositions; 
* a common one is using the equality operator (=).
* 
* example: <PROPOSITION> := <PROOF>
*              ^              ^
*              |              | 
*            TYPE X       EXPRESSION OF TYPE X
* 
* example: length ["a", "b"] = 2 := rfl
* 
* rfl
* 
* This is a function that constructs propositions of the form a = a, 
* i.e. propositions where the left-hand side is exactly the same as the right-hand side.
* 
* So how do we actually prove that 2 = 2?
* By providing a program (rfl) that can construct a value of this type.
* 
* def map {A B: Type*} (f: A -> B): list A -> list B
*   | [] := []
*   | (x :: xs) := f x :: map xs 
*/
def map[A, B](f: A => B): List[A] => List[B] =
  case Nil => Nil
  case x :: xs => f(x) :: map(f)(xs)

// length function in type position
import compiletime.ops.int.*

type length[tt <: Tuple] <: Int = tt match
  case EmptyTuple => 0
  case h *: t => 1 + length[t]

def example1:Unit = 
    val l = map((x: Int) => x + 1)(List(1, 2, 3))
    println(l)

    // example -- 这是proof?
    summon[ length[("a", "b")] =:= 2 ]

/* This example shows one of the biggest differences between theorem provers 
 * such as Lean and “normal” programming languages like Scala.
 * 
 * Lean has a unified syntax for both type-checking and evaluation of expressions. 
 * So far we’ve only used the type checker, but Lean also has an interpreter that can execute a subset of Leans’ code.
 *
 * Scala in contrast has basically two languages combined into one: the (pure, functional) type-level 
 * language that is “evaluated” at compile time, and the regular Scala term-level code that is 
 * compiled into JVM bytecode.
 */

import compiletime.ops.int.*

type map[A, f[_ <: A], as <: Tuple] <: Tuple = as match
	case EmptyTuple => EmptyTuple
	case x *: xs => f[x] *: map[A, f, xs]

def example2: Unit =
  summon[map[Int, [x <: Int] =>> x + 1, (1, 2, 3)] =:= (2, 3, 4)]

/* term level function
 *   x => x + 1
 * type level function
 *   [x <: Int] =>> x + 1
 */
