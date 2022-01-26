package deps.sigma

/**
 * Dependent Types & Type Level Programming
 * https://partialflow.wordpress.com/2017/07/26/dependent-types-type-level-programming/
 * 
 * Dependent Pair Type (Σ-Type), also called Sigma Type or Dependent Sum Type, it defines a pair 
 * where the second type is dependent on the first value. 
 * 
 * Sigma [x:A] B(x) is the collection of pairs (x,y) for x:A and y:B(x).
 */
type InputValue = Singleton & Int

type DepType [T <: InputValue] = T match
    case 0 => Int
    case 1 => String
    case ? => Boolean

/*
 * data DepPair : (a : Type) -> (P : a -> Type) -> Type where
 *   MakeDepPair : {P : a -> Type} -> (x : a) -> P x -> DepPair a P
 * 
 * NOTE: we use DepType[T] for '{P : a -> Type}'
 */   
case class DepPair[N <: InputValue](x: N, value: DepType[N])

val s0 = DepPair(0, 10)
val s1 = DepPair(1, "a")
val s2 = DepPair(2, true)
val s3 = DepPair(3, false)

@main def PiMain: Unit = 
   println(s1.x)
   println(s1.value)