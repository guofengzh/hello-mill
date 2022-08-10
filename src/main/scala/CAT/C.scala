package CAT

/**
 * Compiling Scala into Closed Cartesian Categories language
 * https://medium.com/@mandubian/experimental-compiling-scala-into-closed-cartesian-categories-language-using-scalafix-rewriter-4771c6494d38
 *
 * So, this CHL correspondence tells us that the 3 following things are the same:
 *  - Types & terms from the simply typed 𝛌.calculus (Church created in ~1940)
 *  - Proofs & propositions from intuitionistic logic (link with 𝛌.calculus established 
 *    in ~1930–40 by Curry and then ~1960–1970 by Curry/Howard)
 *  - Objects & morphisms from closed cartesian categories (link with 𝛌.calculus established 
 *    by Lambek in ~1970–1980)
 * 
 * In logic, any inhabited type (having at least one value) is equivalent to a “proposition” 
 * which can be ***interpreted into some truth value***. Consequently, you can deduce that the 
 * uninhabited type Nothing in Scala (Void in Haskell) can be used to perform reasoning by 
 * contradiction or absurdity… “ex falso (sequitur) quodlibet” (Our latin ancestors weren’t 
 * so far from us ;))
 * 
 * Now, we have everything that defines a Category according to Wikipedia:
*   - objects <> types <> proposition
*   - morphisms <> function <> implication
*   - identity <> identity function <> self-implication
*   - morphism composition <> function composition <> natural deduction
 */

 /*
 在 Scala 中表示范畴

Scala语言尽管有所有假定的默认值，但允许表示“高阶”的抽象，而不是简单的抽象，例如多态。 因此，我们也可以在Scala语言中
将范畴编写为trait（相当于Haskell中的typeclass）：
*/
trait Cat[->[_, _]]:
  def id[A]: A -> A
  def ○[A, B, C]: (B -> C) => (A -> B) => (A -> C)
/*
- 如前所述，Scala 类型已经是Set类别中的对象，因此无需关心，我们已经在操作类别中的对象。
- 要成为一个类别，结构需要一个态射（或箭头->[A, B],因此我们通过表示在类别中从任何对象/类型A到
  任何其他对象/类型B的态射->[A, B]的高阶类型来参数化特征Cat。
- 类别为每个对象/类型提供一个恒等态射 id。
- 范畴提供每个态射之间的复合操作○。
*/

// For information, here are the other laws implied by category (identity & associativity composition).
// in the context of a Category
// 
// def f: A => B = ???
// ≌ representing isomorphism
// f ○ Id ≌ Id ○ f ≌ f
// associativity
// (h ○ g) ○ f ≌ h ○ (g ○ f)

/*
In Scala, we live in category Set and if we take Function1/=> as morphism plus the identity 
function plus the function composition, we can easily write a version of our Cat trait for 
Scala Function1[A, B]/=>.
*/
given Function1Cat: Cat[Function1] with
  def id[A]: A => A = identity
  def ○[A, B, C]: (B => C) => (A => B) => (A => C) = f => g => f compose g

// Cartesian product or logical conjunction
//
// When you solve problems, your mind often thinks in the following way:
//   If I know A implies B and A implies C then I know A implies (B and C)
//   I also know that (A and B) implies A and also B
// 𝛌.calculus is very basic and is often augmented with Cartesian Product of 2 types A x B to 
// simplify syntax.
// lambda calculus augmented with cartesian product (A x B) and 2 projections from (A x B) to A and B
// projection1 from (A x B) to A
def pi1[A,B](a:A, b:B) = a:A
// projection1 from (A x B) to B
def p2[A,B](a:A, b:B) = b:B
// cartesian product expressed in terms of the 2 projections
//trait ⨂[A, B]
//(pi1(c:A ⨂ B), p22(c: A ⨂ B)) = (c: A ⨂ B)

/*
In a program, the usefulness of cartesian product seems obvious:
  - it allows to build more complex types such as case classes (or records in Haskell) 
    and use projections to extract data from them.
  - it allows to “aggregate” 2 functions with the same input type and different output 
  - type into one single function.
*/

/*
In terms of category, this definition of Cartesian Product with the 2 projections 
correspond exactly to the definition of Cartesian Category. Surprising, isn’t it?

So let’s do the same exercise as above and enhance our Cat trait into a CartesianCat 
trait defining the requirements of our Cartesian Category.
*/
trait CartesianCat[->[_, _]] extends Cat[->]:
  def ⨂[A, C, D]: (A -> C) => (A -> D) => (A -> (C, D))
  def exl[A, B]: (A, B) -> A
  def exr[A, B]: (A, B) -> B

/*
- ⨂ is just the expression of above conjunction replacing implication by morphism.
- (C, D) is written using Scala Tuple but keep in mind that it’s the product in the 
  Cartesian Category.
- exl and exr are just the 2 projection operations.

So our category language have been augmented with 3 more operations ⨂, exl, exr

The same can be written about the dual of Product called Coproduct or Disjunction in 
logicA ∨ B (A or B). You can then build a Cocartesian Category but I won’t write it here, 
it’s trivial.

If you mix both Cartesian and Cocartesian categories into one single, you obtain the so-called 
Bicartesian Category.
*/

/*
Apply a function aka Modus Ponens

When you write a function A => B , you naturally expect to apply it on some value A to 
obtain a value B(and again, same A shall always return same B…)

In terms of category, for any object/type A and B, it means we have a unique function 
(A => B, A) => B. This is called “applying function to value” (or evaluating).

That sounds very trivial, right? But, this is one of the most basic rule of logic called 
modus ponens.
  - If you know A implies B and you prove me A then I can prove B
So, applying a function on a value is just applying the modus ponens.
*/

/*
Just for the story, (A => B, A) => B is not exact, it should be (A ^^ B, A) => B with ^^ meaning 
exponential. In our Category Set, the exponential is synonym to => but in other categories, 
exponential can be something else. You can read more about categorical exponentials in Bartosz 
Milewski’s post https://bartoszmilewski.com/2015/03/13/function-types/ and you’ll see how “ 
exponential” name in category was chosen because it behaves like numerical exponentials and 
shows why category theory has become important: it unifies things.
*/

/*
A terminal object for empty products

In general, we want to manipulate products of any arity, from 0 (empty product) to infinity .

The empty product is a product of 0 object and is a unique value(). The type of this value 
is a type being inhabited by the single value () and you know one such type in Scala: Unit.

Moreover, for any object/type A, you can easily build a simple function/morphism:
  A => () = (a:A) => ()

Having this property in a category means () is a terminal object in the category and the 
empty product is also the terminal object itself.

With product defined above and this terminal object, it’s easy to generalise products & projections:
 the product of n elements being the product of the n-th element and a product of n-1 elements, 
 recursively till 0.

*/

/*
Currification / Uncurrification

In 𝛌.calculus, there is no function with multiple parameters like in Scala. A function 
with 2 parameters becomes a function with 1 parameter that returns a function with 1 
parameter. This is called currying a function and Haskell only manages curried functions.

In terms of category, it means we have the following morphism :
  ((A, B) -> C) -> (A -> B -> C)

The dual of that is called uncurrying:
  (A -> B -> C) -> ((A, B) -> C)
*/

/*
… Finally Closed Cartesian Category…

In the context of cartesian category, if we add the 3 previous operations:
  - function apply,
  - terminal object,
  - currying/uncurrying,
… we finally obtain everything required by a Closed Cartesian Category…

Let’s augment our CartesianCat with those new operations:
*/
trait ClosedCartesianCat[->[_, _]] extends CartesianCat[->]:
  def it[A]: A -> Unit
  def ap[A, B]: ((A -> B), A) -> B
  def curry[A, B, C]: ((A, B) -> C) => (A -> (B => C))
  def uncurry[A, B, C]: (A -> (B -> C)) => ((A, B) -> C)

/*
… And again, we can write ClosedCartesianCat[Function1].
*/
given Function1ClosedCartesianCat: ClosedCartesianCat[Function1] with
  def id[A]: A => A = identity
  def ○[A, B, C]: (B => C) => (A => B) => (A => C) = f => g => f compose g

  def ⨂[A, C, D]: (A => C) => (A => D) => (A => (C, D)) = f => g => (a => (f(a), g(a)))
  def exl[A, B]: ((A, B)) => A = _._1
  def exr[A, B]: ((A, B)) => B = _._2

  def it[A]: A => Unit = _ => ()

  def ap[A, B]: ((A => B, A)) => B = f => f._1(f._2)
  def curry[A, B, C]: (((A, B)) => C) => (A => (B => C)) = f => a => (b => f((a, b)))
  def uncurry[A, B, C]: (A => (B => C)) => (((A, B)) => C) = f => { case (a, b) => f(a)(b) }
