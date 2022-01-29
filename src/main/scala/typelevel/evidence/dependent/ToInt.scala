package typelevel.evidence.dependent

// Converts a type-level natural number to an integer (runtime)
trait ToInt[N <: Nat]:
  def apply(): Int

object ToInt:
  case class ToIntInstance[N <: Nat](i : Int) extends ToInt[N]:
    def apply() = i

    // Evidence that Zero -> 0
  given ToIntInstance[Zero](0)

  // if we have a `ToInt` for some number
  // the `ToInt` for its successor is easily computed (sum 1)
  given sucToInt[N <: Nat](using nToInt: ToInt[N]): ToInt[Suc[N]] = ToIntInstance[Suc[N]](nToInt() + 1)

  // implicit helper
  def apply[N <: Nat](implicit toInt: ToInt[N]) = toInt
