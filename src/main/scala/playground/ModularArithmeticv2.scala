package com.mycompany.playground

object ModularArithmeticv2{}

trait ModIfv2:
  val residue: Int  
  def +(rhs: ModIfv2): ModIfv2
  def ==(rhs: ModIfv2): Boolean
  def toText: String

class Modv2(val residue: Int) extends ModIfv2:
  val modulus = 12

  def +( rhs: ModIfv2) =
    val sum = residue + rhs.residue
    Modv2(sum)
    
  def ==(rhs: ModIfv2): Boolean = residue == rhs.residue

  def toText: String = "Mod(" + residue + ")"

val modv23 = Modv2(3)
val modv26 = Modv2(6)


import ModularArithmeticv2.*

@main def modv2Test(arg: String*): Unit =
    val ninev2 = modv23 + modv26
    val ninev2Text = ninev2.toText
    print(ninev2Text)

