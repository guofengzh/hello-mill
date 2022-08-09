package singleton

object Refinement {
  
}

trait Thing:
    type ThisThing <: Thing
    type A
    type B
    def copy[X,Y](a :X, b :Y) :ThisThing & { type A =X; type B = Y }

trait V:

  type S = {def s: Boolean}
  type R = {def r(elem: S): Boolean} // return type needed to match the impl. of the structural type

  def testS (s: S) = println(true)
  def testR (r: R) = println(true)

// the concrete implementations of your structural types
class SImpl:
  def s: Boolean = true
  def wazza = "wazza" // to check structural type is recognized well

class RImpl:
  def r(elem: V#S) = true // by "V#S" you refer to the internal type S of V
  def waddup = "waddup" // to check structural type is recognized well

// concrete implementation of V
class VImpl extends V

@main def refinementTest: Unit =
  // structural conformance tests
  (new VImpl).testS(new SImpl)
  println((new RImpl).r(new SImpl))
  (new VImpl).testR(new RImpl)

