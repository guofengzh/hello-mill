package PAT

/**
 * Scala implicits and the Curry-Howard correspondence
 * https://rootmos.io/2017-05-07-scala-implicits-and-curry-howard.html
 * 
 * The idea is to elucidate the Curry–Howard correspondence that's present in Scala's 
 * type system, and use implicits to automatically construct proofs. That this is possible 
 * should not be surprising, but what's interesting is how easy it is.
 * 
 * An explicit Curry-Howard correspondence
 * 
 * The Curry-Howard correspondence is the observation that type-systems can be thought of 
 * as logics (and vice versa). This sounds far harder than it actually is:
 *   - Propositions correnpond to types
 *   - Implications correspond to functions
 *   - Conjunctions and disjunctions both correspond to simple ADTs 
 *     (or products and coproducts for the fancy)
 *   - Negations are functions together with a bottom type
 */

 // Propositions
 // 
 // A proposition A is expressed as a type:
trait A

// A proof of proposition A is expressed as an inhabitant of the type A:
given proofOfA: A()

// So we can ask the compiler if it can prove A using:
def proveA = summon[A]

// which would fail with a type-error for unproven propositions
// trait B
// def proveB = summon[B]

// Non-termination
//
// Imagine that a mathematician says: "I have a proof of A, I just have to write it down!" 
// and then never comes back. Do you trust him? The type system would! 

// Consider this definition:
// def diverge[A](): A = diverge[A]()
// which makes the expression diverge() an inhabitant of any type:
// :t diverge(): Int
// :t diverge(): Nothing
// 但作为持怀疑态度的人，我们希望看到实际的证明，Scala在运行时也是如此。 因此，当我们使用隐式请求证明时，
// 等待评估成功至关重要。
//
// Tangent(切线)：作为非终止的异常
// 
// 当然，在 Scala 中有一种更简单的方法来实现 Nothing，那就是 throw：
def ff = (throw new Exception): Nothing
// 但是使用diverge的意义在于，即使不诉诸异常，也有可能（并且很容易）殖民()任何类型，包括底部类型 Nothing。
//
// Tangent：依赖类型系统
// 请注意，在更强大的类型系统中，可以在编译时证明更多属性。 例如，Idris 处理上述示例的方法是diverge不是完全的，
// 因此拒绝使用它的证明，并权衡您可以信任仅使用总函数的证明。
// 
// 蕴含
// 
// An implication is expressed as a function:
trait B
given `A implies B`(using a: A): B()
// Then if we have a proof of A in-scope, we can prove B:
// given proofOfA: A() - see above
def BB = summon[B]
// and of-course we can't use C => D to prove D without a proof of C:
trait C
trait D
given `C implies D`(using c: C): D()
//def errDD = summon[D]

// Conjunction
//
// Conjunction (or and) can be expressed using a tuple:
given `P, Q implies (P and Q)`[P, Q](using p: P, q: Q): (P, Q) = (p, q)
// We can now query if the compiler can prove or conjunction:
// given proofOfA: A() -- see above
given proofOfB: B()
def conj = summon[(A, B)]

// Disjunction
// 
// Disjunction (or or) can be expressed using a simple ADT:
sealed trait Disjunction[T, S]
case class FromT[T, S](t: T) extends Disjunction[T, S]
case class FromS[T, S](s: S) extends Disjunction[T, S]
case class FromTS[T, S](t: T, s: S) extends Disjunction[T, S]

// accompanied by some implicits to hook it up:
trait LowPriorityDisjunctionProofs:
  given disjunctionT[T, S](using t: T): Disjunction[T, S] = FromT(t)
  given disjunctionS[T, S](using s: S): Disjunction[T, S] = FromS(s)

object Disjunction extends LowPriorityDisjunctionProofs:
  given disjunctionTS[T, S](using t: T, s: S): Disjunction[T, S] = FromTS(t, s)

// Let's see it in action:
// given proofOfA: A() - see above
// given proofOfB: B() - see above
def disj =
  summon[Disjunction[A, C]]
  summon[Disjunction[C, B]]
  summon[Disjunction[A, B]]

// Negation
//
// To express the negation of a proposition A one can use the bottom type Nothing 
// to represent the false formula, and define:
given notA: (A => Nothing) = { _ => throw new Exception }
// This encoding relies on observing that A and ¬A is a contraction:
given `A and ¬A is a contradiction`(using a: A, notA: A => Nothing): Nothing = notA(a)
// When we get a Nothing into the implicit system the world becomes quite absurd:
// given proofOfA: A() - see above

def absurd = 
  summon[B]
  summon[Int]
  summon[Nothing]
// 幸运的是，运行时系统不会那么宽松，以上都不会终止（这里它会抛出我们上面 new:ed 的异常）。 
// 请注意，这些可键入的表达式将被新的 REPL 正确拒绝。

// 但是将非常奇怪的表达式偷偷溜过编译器是很有趣的：

given `Embrace the nothingness`[P, Q](using p: P): Q = summon[Nothing]
//val b: B = new A {}
//val i: Int = b

/* Conclusion

Scala 的隐式系统可能被许多人误用和厌恶。 但我认为它是 Scala 更有趣的特性之一，
我更喜欢它的观点是它是嵌入到语言中的现成的证明引擎。

在野外经常遇到的实际使用是JSON编解码器的类型类。 由于类型类在Scala中是作为隐式实现的，
因此当通过隐式传递类型A具有ToJSON类型类实例的证据时，该证据可以作为将类型A的值转换为
JSON的方法的建设性证明，一本食谱。
*/
