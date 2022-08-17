package disjunctive

object RootsOfQModule:
  trait RootsOfQ
  case object NoRoots extends RootsOfQ
  case class OneRoot(x: Double) extends RootsOfQ
  case class TwoRoots(x: Double, y: Double) extends RootsOfQ

enum RootsOfQ:
  case NoRoots
  case OneRoot(r: Double)
  case TwoRoots(r1: Double, r2: Double)

import RootsOfQ.*
def solve(b: Int, c: Int) : RootsOfQ = 
  val d = b*b - 4*c;
  if d < 0 then
    NoRoots
  else if d == 0 then
    OneRoot(b/2.0)
  else
    TwoRoots((-b+Math.sqrt(d))/2.0, (-b-Math.sqrt(d))/2.0)
  end if

def verify(roots: RootsOfQ): String = roots match
  case NoRoots => s"no roots"
  case OneRoot(r) => s"one root $r"
  case TwoRoots(r1, r2) => s"two roots, $r1 and $r2"

@main def quadraticEquationTest(args: String*): Unit =
  val qe1 = solve(1, 1) // no roots
  val qe2 = solve(2, 1) // one root
  val qe3 = solve(3, 1) // two root
  println(qe1)
  println(qe2)
  println(qe3)
