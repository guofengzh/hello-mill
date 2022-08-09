package typelowering

/**
 * Proposal: Existential types for interfaces and abstract types
 * https://github.com/dotnet/csharplang/issues/1328
 * 
 * we have a theorem in logic that says we can transform existential types into 
 * an encoding of universal types.
 */ 
trait ICounter[T]
:
    def start: T
    def next(current: T): T
    def done(current: T): Boolean 

class Counter extends ICounter[Int] with ICounterWrapper
:
    def start = 0
    def next(current: Int) = current + 1
    def done(current: Int) = current == 42;
    def unwrap[TWitness <: ICounterWitness](witness: TWitness): Unit = 
        witness.invoke(this)  // Here the T in ICounterWitness.invoke[T] is inferenced
/*---------------The above is the codes by the implementer-----------------------*/

/* ----------- the two "intermediate" interfaces -----------*/

/* The two interfaces is created to transform the existential type production into a 
   universal type production. Now T is scoped in ICounterWitness.
*/
trait ICounterWrapper
:
    def unwrap[TWitness <: ICounterWitness](w: TWitness): Unit

/**
 * this trai is the bridge between the implementer and the customer. the imlementer hides 
 * the T by ICounterWitness.invoke[T].
 * 
 * ICounterWitness.invoke[T] make the T is not provided when instantiatse MWitness (the customer side),
 * but bound by Counter.unwrap when it invokes ICounterWitness.invoke[T] (the implementer side).
 * 
 * that's we lower the T from type constructor to the method type parameter, which can be bound based
 * on the customer code.
 */
trait ICounterWitness
:
    def invoke[T](counter: ICounter[T]): Unit

/* ------------ The is the customer codes --------*/
class MWitness extends ICounterWitness
:
    def invoke[T](ic: ICounter[T]): Unit = 
        var x = ic.start;
        while (!ic.done(x)) do
            x = ic.next(x);

class Cc
:
    def m(wrapper: ICounterWrapper): Unit =
        wrapper.unwrap(new MWitness()); // tell wrapper what the customer codes are
