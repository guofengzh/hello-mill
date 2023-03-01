package macros

final class PosInt private (val value: Int) extends AnyVal:
    def unary_+ : PosInt = this
    def +(x: PosInt): PosInt = PosInt.ensuringValid(value + x.value)
    def *(x: PosInt): PosInt = PosInt.ensuringValid(value * x.value)
    override def toString: String = value.toString()

object PosInt:
    inline def apply(inline value: Int): PosInt = PosIntMacro.apply(value)

    def from(value: Int): Option[PosInt] =
        if (PosIntMacro.isValid(value)) Some(new PosInt(value)) else None
    
    given Conversion[PosInt, Int] with
        def apply(pos: PosInt): Int = pos.value
  
    def ensuringValid(value: Int): PosInt =
        if (PosIntMacro.isValid(value)) new PosInt(value) else
          throw new AssertionError(s"${value.toString()} was not a valid PosInt")
