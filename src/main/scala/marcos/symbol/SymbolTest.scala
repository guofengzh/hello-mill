package macros.symbol

import macros.logast.logAST

@main def symbolTest() =

  def printSymbols = Symbols.printSymbols()

  // It will print: Splice owner: macro, parent printSymbols, grandParent symbolTest
  printSymbols()
  
  // To find how the function definition should be constructed in the macro.
  logAST {
    def f() = {

    }
  }

  class MyClass:
    val macroReturnValue = SomeMacro.macroCall("someExpression")
  end MyClass

  // get the enclosing module of a macro call.
  // it will print: macros.symbol.SymbolTest$package$._$MyClass
  println(MyClass().macroReturnValue)
