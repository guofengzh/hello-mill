package DOT

/**
 * http://users.sussex.ac.uk/~apj21/dif.pdf
 */
def <= (x: Int, y: Int ) = x <= y

def > (x: Int, y: Int ) = x > y

object explicit_parameterisation:
    def compare (x: Int , y: Int)( comparator: Int => Int => Boolean ): Boolean =
          comparator (x)(y)

object default_parameters:
    def compare (x: Int , y: Int )( comparator: Int => Int => Boolean = <=.curried): Boolean =
          comparator (x)(y)

object implicit_parameterisation:
    def compare (x: Int , y: Int )(using comparator: Int => Int => Boolean ): Boolean =
          comparator (x)(y)

type Comparator = Int => Int => Boolean

given cmp: Comparator = <=.curried

