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

// Rewriting Scala code into CCC
// ====================================
object Scala2CCC:
  // a very basic Scala function performing a simple operation on primitive type Int
  def plus = (x:Int) => (y:Int) => x + y

  // First, we need to reify our CCC world
  val K = summon[ClosedCartesianCat[Function1]]
  // now we import CCC language into our context
  import K.*

  // we can always uncurry curried function
  uncurry((x:Int) => (y:Int) => x + y) // = (x:Int, y:Int) => x + y
  
  // now use product
  ⨂((x:Int) => x + 2)((x:Int) => x * 2) // = (x:Int) => (x + 2, x * 2)

  // now compose with + : (Int, Int) => Int
  ○((x:Int) => 1 + x)((x:Int) => x)  // 
  ○(plus)((x:Int)=> x)  // by this line, we know category theory is for compositions
end Scala2CCC

/*
Actually, it’s not exact as + function is not a CCC operation but a Scala function on 
primitive typed value (such as Int, Double, Float, Short, etc…). Thus, we need to augment
our CCC by adding support for such primitive-types operations.
*/
// We can write this type class parameterized by the morphism and a type A 
trait CCCNumExt[->[_, _], A]:
  def negateC: A -> A
  def addC: (A, A) -> A
  def mulC: (A, A) -> A

// and implement it for Function1 and any Numeric A
given Function1CCCNumExt[A](using N: Numeric[A]): CCCNumExt[Function1, A] with
  def negateC: A => A = {case a:A => N.negate(a) }
  def addC: ((A, A)) => A = { case (a, b) => N.plus(a, b) }
  def mulC: ((A, A)) => A = { case (a, b) => N.times(a, b) }


// just note the trick: we put A in the trait type parameters
// to be able to constrain it later with Numeric

// … And finally we can use that CCCNumExt in our previous rewrite.
object Scala2CCCv2:
  def plus = (x:Int) => (y:Int) => x + y
  val K = summon[ClosedCartesianCat[Function1]]
  val E = summon[CCCNumExt[Function1, Int]]
  import K.*, E.*

  // the line on top is strictly equivalent to the following in CCC language
  // TODO: def plusv2 = addC ○ (exl[Int, Int] ⨂ exr[Int, Int])
end Scala2CCCv2

// 为了达到这篇文章的这一点，您遭受了很多痛苦，您当然想知道为什么要付出如此多的努力来将一个不错的小
// Scala函数重写为更不可用的东西。

// The response is…
//   CartesianClosedCat[Function1]是一个CCC，但周围还有***许多其他笛卡尔封闭范畴***，具有相同的内部语言
// 但解释完全不同。
//   因此，一旦您可以将Scala代码重写为CCC语言，您就可以使用任何CCC来解释它。

// Graphs of computation can be CCC
//
// 忘记逻辑，只相信以下内容：可以在类似 kleisli 的结构中表示有向图，接受输入端口并返回提供输出端口的
// State monad 和沿有向图构造的实例化组件列表。
//
// 这是 Scala 中这种 Kleisli-State-monadic-like 有向图的定义。
//
// A port is just identified by an Int
type Port = Int

// A component has a name and input/ouput ports
final case class Comp[A, B](name: String, inputs: Ports[A], outputs: Ports[B])

// the state monad building the list of components Comp
type GraphM[A] = ((Port, List[Comp[?, ?]])) => ((Port, List[Comp[?, ?]]), A)

// the kleisli-like directed graph structure based on state monoad
final case class Graph[A, B](f: Ports[A] => GraphM[Ports[B]])

// For info, the different type of ports
sealed trait Ports[A]
case object UnitP extends Ports[Unit]
final case class BooleanP(port: Port) extends Ports[Boolean]
final case class IntP(port: Port) extends Ports[Int]
final case class DoubleP(port: Port) extends Ports[Double]
final case class PairP[A, B](l: Ports[A], r: Ports[B]) extends Ports[(A, B)]
final case class FunP[A, B](f: Graph[A, B]) extends Ports[A => B]

// dummy
class GenPorts[A] 
given genPorts[A](using N: Numeric[A]): GenPorts[A]()

// 您当然得出结论，这个 Graph[A, B] 结构使用 Graph 作为态射形成了一个很好的封闭笛卡尔范畴。
// our CCC with Graph as morphism
given GraphCCC: ClosedCartesianCat[Graph] with
  // it has 9 unimplemented members.
  /** As seen from module class GraphCCC$, the missing signatures are as follows.
   *  For convenience, these are usable as stub implementations.
  */
  // Members declared in CAT.CartesianCat
  def exl[A, B]: Graph[(A, B), A] = ???
  def exr[A, B]: Graph[(A, B), B] = ???
  def ⨂[A, C, D]: Graph[A, C] => Graph[A, D] => Graph[A, (C, D)] = ???
  
  // Members declared in Cat
  def id[A]: Graph[A, A] = ???
  def ○[A, B, C]: Graph[B, C] => Graph[A, B] => Graph[A, C] = ???
  
  // Members declared in ClosedCartesianCat
  def ap[A, B]: Graph[(Graph[A, B], A), B] = ???
  def curry[A, B, C]: Graph[(A, B), C] => Graph[A, B => C] = ???
  def it[A]: Graph[A, Unit] = ???
  def uncurry[A, B, C]: Graph[A, Graph[B, C]] => Graph[(A, B), C] = ???

// we can also implement CCCNumExt for Graph
given GraphCCCNumExt[A](using N: Numeric[A], gp: GenPorts[A]): CCCNumExt[Graph, A] with
  // genComp just generate a component with 2 inputs, 1 output and a nice name
  def genComp (s: String)= ???
  def negateC: Graph[A, A]   = genComp("-")
  def addC: Graph[(A, A), A] = genComp("+")
  def mulC: Graph[(A, A), A] = genComp("*")

object GraphTest:
  def plus = (x:Int) => (y:Int) => x + y
  // First, we need to reify our CCC language
  val K = summon[ClosedCartesianCat[Graph]]
  // First, we need to reify our CCC numeric extensions for Int (ok it's a bit hard coded but 
  // that's not the point here ;))
  val E = summon[CCCNumExt[Graph, Int]]
  // now we import CCC language into our context
  import K.*, E.*

   // the line on top is strictly equivalent to the following in CCC language
   // TODO: def plusv2 = addC ○ (exl[Int, Int] ⨂ exr[Int, Int])

// 最后一个有趣的事实：2 CCC的积是 CCC，因此您可以用CCC语言编译一次Scala代码，然后将其同时运行到2个不同的CCC
