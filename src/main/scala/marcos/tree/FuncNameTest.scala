package marcos.tree

@main def FuncNameTest: Unit = 
    case class B(field1: String)
    println(getName[B](_.field1)) // "field1"
