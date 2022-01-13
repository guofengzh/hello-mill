package partialapply

// it seems like you want to do some kind of advanced partial type application. 
// Usually you can achieve such an API by introducing an intermediary class. 
// And to preserve as much type information as possible you can use a method 
// with a dependent return type.
class FindApplicablePartial[A] {
  def apply[B](fn: A => B): fn.type = fn
}

def findApplicable[A] = new FindApplicablePartial[A]

// def result = findApplicable[(Int, String)](commutative)

// And actually in this case since findApplicable itself doesn't care about type B 
// (i.e. B doesn't have a context bound or other uses), you don't even need the 
// intermediary class, but can use a wildcard/existential type instead:
def findApplicable2[A](fn: A => _): fn.type = fn