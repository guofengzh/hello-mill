package deps.pi

/**
 * Dependent Types & Type Level Programming
 * https://partialflow.wordpress.com/2017/07/26/dependent-types-type-level-programming/
 * 
 * Dependent Function Type (Π-Type), also called Pi Type or Dependent Product Type, 
 * it defines a function that resolves the output type based on the input value. 
 * 
 * Pi [x:A] B(x) is the collection of function-like things mapping every input x:A to some y:B(x).
 */

type InputValue = Singleton & Int

/**
 * 注意，对象中有一个apply方法，这个对象就相当于一个函数了：obj(arg)
 */
trait B[T] {
   def apply(x:T): T = x
}

object b0 extends B[Int]
object b1 extends B[String]
object b2 extends B[Boolean]

type DepType [T <: InputValue] = T match
    case 0 => B[Int]
    case 1 => B[String]
    case ? => B[Boolean]

/*
 * data DepProduct : (a : Type) -> (P : a -> Type) -> Type where
 *   MakeDepProduct : {P : a -> Type} -> ((x : a) -> P x) -> DepProduct a P
 * 
 * NOTE: we use DepType[T] for '{P : a -> Type}', B[T] for '((x : a) -> P x)'
 */
case class DepProduct[T <: InputValue](x: T, y : DepType[T])

/*
 * Now we can make use of it, for example:
 *   x : DepProduct Int (\n => depType n)
 *   x = MakeDepProduct (\n => case n of
 *                             0 => 10
 *                             1 => "aaa"
 *                             2 => True)
 */
val pid0 = DepProduct(0, b0)

object DepFunction:
  def apply[N <: InputValue](x: N): DepType[x.type] = 
    x match
      case 0 : 0 => b0
      case 1 : 1 => b1
      case _ : ? => b2

val pi0 = DepFunction(0)
val pi1 = DepFunction(1)
val pi2 = DepFunction(2)

@main def PiMain: Unit = 
   println(pi0(5))
   println(pi1("Hello"))
   println(pi2(true))
