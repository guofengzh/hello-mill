package slist

enum Nat:
  case Zero
  case Succ[A]()

  def f() = 5

import Nat.*

type Nat2 = Succ[Succ[Zero.type]]
type Nat3 = Succ[Succ[Succ[Zero.type]]]

enum Vector[+N <: Nat, +A] {
  case Cons(head: A, tail: Vector[N, A]) extends Vector[Succ[N], A]
  case Nil extends Vector[Zero.type, Nothing]
}

import Vector.*

val vector2: Vector[Nat2, Int] = Cons(1, Cons(2, Nil))
val vector3: Vector[Nat3, Int] = Cons(1, Cons(2, Cons(3, Nil)))

def typeSafeHead[A](vec: Vector[Succ[Nat], A]):A = vec match
    case Cons(head, _) => head

def add[A](lfs: Vector[Nat, A], rhs: Vector[Nat, A]) : Vector[Nat, A] = 
    ???

def eq[A](lfs: Vector[Nat, A], rhs: Vector[Nat, A]) : Boolean = 
    ???

@main def vectorTest: Unit = 
  val v1: Vector[Nat2, Int] = Cons(1, Cons(2, Nil))
  val v2: Vector[Nat2, Int] = Cons(1, Cons(2, Nil))
  val p = add(v1, v2)
  val b = eq(v1, v2)
  val c = eq(v1, p)
