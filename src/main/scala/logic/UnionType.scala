package logic

/**
   The bottom type (Scala’s Nothing type) maps to logical falsehood.
   the negation of a type,  A ⇒ false, is equivalent to ¬A.
   the **function signature** "A => Nothing" can not be implemented directly, 
   so we consider its negation:
*/
type ¬[A] = A => Nothing

/** 
 * (A ∨ B) ⇔ ¬(¬A ∧ ¬B), or in Scala we have (A ∨ B) =:= ¬[¬[A] with ¬[B]]
 * so we have the following definition:
 */
type ∨[T, U] = ¬[¬[T] & ¬[U]]

/**
 * We encode the union type is itself a function type because of "A => Nothing", 
 * to make T or U being a subtype of ∨[T, U], we have to transform T or U to 
 * a function, then to check the subtype relation using "<:<", so we have the following:
 */
type ¬¬[A] = ¬[¬[A]]


/* 
   test type relationship by  giving evidence
   
   scala> summon[¬¬[String] <:< (Int ∨ String)]
   scala> summon[¬¬[Int] <:< (Int ∨ String)]
*/
object UnionType:

  /**
   * we have subtype relationships which are isomorphic to the ones we want 
   * (because ¬¬[T] is isomorphic to T), but we don’t yet have a way to express 
   * those relationships with respect to the the untransformed types (i.e., T) 
   * that we really want to work with.
   * 
   * We can do that by treating our ¬[T], ¬¬[T] and T ∨ U as phantom types (幻象类型),
   * using them only to **represent** the subtype relationships on the underlying type 
   * rather that working **directly** with their values. 
   * 
   * This is using a generalized **type constraint** (or evidence in Logic) to require 
   * the compiler to be able to prove that any T inferred as the argument type of the 
   * size method must be such that it’s double negation is a subtype of Int ∨ String. 
   * That’s only ever true when T is Int or T is String.
   * 
   * That's why we use a implicit parameter in the size method:
   */
  def size[T](t: T)(using evidence: (¬¬[T] <:< (Int ∨ String))) =
    t.asInstanceOf[Matchable] match
      case i: Int => i
      case s: String => s.length

  /**
   * The implicit evidence parameter is syntactically a bit ugly and heavyweight, 
   * and we can improve things a little by converting it to a **context bound**
   * on the type parameter T like so:
   */
  type |∨|[T, U] = [X] =>> ¬¬[X] <:< (T ∨ U)

  def size2[T: |∨|[T, U], U](t: T) = // def size2[T, U](t: T)(using (T |∨| U)[T]): Int
    t.asInstanceOf[Matchable] match
      case i: Int => i
      case s: String => s.length

  def size3[A: (Int |∨| String)](a: A) = 
    a.asInstanceOf[Matchable] match
      // Scala isn’t doing coverage or possibility tests for “match”.
      // It doesn’t complain the following. But when you try to find the 
      // size of a String you get an error (if no "case s""), and 
      // the Double case of the match is inaccessible.
      case d: Double => d.toInt

/* 
 * NOTE
 *   ¬¬[A] is (A => Nothing) => Nothing
 * which is 
 *   Function1[Function1[A, Nothing], Nothing]. 
 * Function1 is contravariant in it’s first type argument, so 
     Function1[Function1[A, Nothing], Nothing] 
 * is covariant in A, hence if A <: B then 
     Function1[Function1[A, Nothing], Nothing] <: Function1[Function1[B, Nothing], Nothing] 
 * so 
 *   ¬¬[A] <: ¬¬[B]
 */

@main def UnionTypeMain: Unit =
    import UnionType.*
    println(size(3))
    println(size("Hello"))

    println(size2(6))
    println(size2("Hello, world"))

    // println(size3("Hello, world"))
    // println(size3(5.2))
