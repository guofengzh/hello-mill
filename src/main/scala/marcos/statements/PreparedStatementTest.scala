package macros.statements

import macros.logast.logAST

@main def preparedStatemntTest(): Unit =

  // Type given explicitly - better error messages than if the type is inferred.
  // Replace the name of the table or the argument with a variable - an error reported by the compiler
  // due to the value isn't know on compile time.
  // Change the type or the name of the column to be different that in sql/schema.sql - a compilation error.
  //
  // A call to the macro with arguments: the table name as a String and the column definition as a Tuple 
  // (it is unwrapped, so it looks like a regular call). 
  //
  // createPreparedStatement() generates e (which will be embedded in the final code) with 
  // the type of PreparedStatement.
  val statement: PreparedStatement[(Int, String)] =
    StatementGenerator.createPreparedStatement("user")(ColDef[Int]("id"), ColDef[String]("username"))

  // Call to the generated code. The type is checked.
  statement.insert(1, "John")
  
  // Error will be reported as the type of the first column doesn't match.
  // statement.insert("a", "Brad")
  
  // Logged AST that was used to find out the structure of the code that was matched in the macro.
  logAST {
    (ColDef[Int]("id"), ColDef[String]("lastName"))
  }  
