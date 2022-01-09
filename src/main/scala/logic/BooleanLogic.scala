package logic

/**
   If you want to represent a truly polymorphic function as first-class object, you have to 
   define a trait or abstract class. For booleans, this would be something like

   trait B {
    def apply[X](trueCase: X, elseCase: X): X
   }
   
   Note that now the apply method is polymorphic in X. This allows you to implement Church 
   encodings of Booleans as first-class objects that can be passed around (returned from methods, 
   saved in lists, etc.)
 */

trait B { self =>
  def apply[X](thenCase: X, elseCase: X): X
  def |(other: B): B = new B { 
    def apply[A](t: A, e: A) = self(True, other)(t, e) 
  }
  def &(other: B): B = new B { 
    def apply[A](t: A, e: A) = self(other, False)(t, e) 
  }
  def unary_~ : B = self(False, True)
}

object True extends B { def apply[X](a: X, b: X) = a }
object False extends B { def apply[X](a: X, b: X) = b }

def toBoolean(b: B): Boolean = b(true, false)  // invoke  apply[Boolean](...)

@main def BooleanLogic: Unit = {
println("And: ")
println(toBoolean(True & True))
println(toBoolean(True & False))
println(toBoolean(False & True))
println(toBoolean(False & False))

println("Or:")
println(toBoolean(True | True))
println(toBoolean(True | False))
println(toBoolean(False | True))
println(toBoolean(False | False))  

println("Funny expresssion that should be `true`:")
println(toBoolean((False | True) & (True & ~False)))
}