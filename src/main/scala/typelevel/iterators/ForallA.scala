package typelevel.iterators


/*
def pair[A, B](f: A => (A, A), b: B): (B, B) = f(b)

Error: found: (b: B), 
       Required: A

We can't say, in fictional Scala
def pair[B](f: A => (A, A){forAll A}, 
            b: B): (B, B) 
*/

/* this is correct */
trait ForallA[A]:
   def pair[B](f: A => (A, A), b: B): (B, B)

