package typelowering

/**
 * Problem
 *
trait IDataFlow[TLocalState] {
    def start: TLocalState
    def next(state: TLocalState): Unit
    def getCurrentDataFlowDiagnostics(): List[String]
}

trait BitVector
abstract class DefiniteAssignment extends IDataFlow[BitVector] {}

class C
{
    // the worker method must be generic, even though the type parameter is useless to them.
    def doDataFlowAnalysis[T]( df: IDataFlow[T]): Unit = { }
}
*/

/**
 * Solution
 */
trait IDataFlow {
  type TLocalState
}

trait BitVector

// Now only the implementer need to know the existence of the type TLocalState
class DefiniteAssignment extends IDataFlow {
    type TLocalState = BitVector
}

class C
{
    def M(df: IDataFlow):Unit = {}
}
