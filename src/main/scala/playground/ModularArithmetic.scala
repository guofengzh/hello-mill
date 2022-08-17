package com.mycompany.playground

object ModularArithmetic{}

trait ModIf:
  val residue: Int  
  def plus(rhs: ModIf): ModIf
  def eq(rhs: ModIf): Boolean
  def toText: String

class Mod(val residue: Int) extends ModIf:
  val modulus = 12

  def plus( rhs: ModIf) =
    val sum = residue + rhs.residue
    Mod(sum)
    
  def eq(rhs: ModIf): Boolean = residue == rhs.residue

  def toText: String = "Mod(" + residue + ")"

val mod3 = Mod(3)
val mod6 = Mod(6)
// ...

import ModularArithmetic.*

@main def modTest(arg: String*): Unit =
    val nine = mod3.plus(mod6)
    val nineText = nine.toText
    print(nineText)
