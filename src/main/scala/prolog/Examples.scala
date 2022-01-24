package prolog

object Examples {
  final val EvilKing = Clause(
    Predicate("evil", List(Variable("X"))), List(
      Predicate("king",   List(Variable("X"))),
      Predicate("greedy", List(Variable("X")))
    )
  )

  final val FamilyKB = KnowledgeBase()

  def familyTree: Unit = {
    // small family tree
    FamilyKB tell Predicate("parent", List(Atom("daniel"),   Atom("wolfgang")))
    FamilyKB tell Predicate("parent", List(Atom("wolfgang"), Atom("werner")))
    FamilyKB tell Predicate("parent", List(Atom("werner"),   Atom("dontknow")))
    FamilyKB tell Predicate("gender", List(Atom("dontknow"), Atom("female")))

    // ancestor recursion
    FamilyKB.tell(
      Predicate("ancestor", List(Variable("X"), Variable("Y"))), 
      List(
        Predicate("parent",   List(Variable("X"), Variable("Z"))),
        Predicate("ancestor", List(Variable("Z"), Variable("Y")))
      )
    )

    // ancestor stop condition
    FamilyKB tell (
      Predicate("ancestor", List(Variable("X"), Variable("Y"))), 
      List(
        Predicate("parent", List(Variable("X"), Variable("Y")))
      )
    )

    // The inference should return no substitutions for false queries
    //FamilyKB ask List(
    //    Predicate("ancestor", List(Atom("notinset"), Variable("X")))
    //  )

    // The inference should return an empty substitutions for true queries
    //FamilyKB ask List(
    //    Predicate("ancestor", List(Atom("daniel"), Variable("werner")))
    //  )
    
    // The inference should handle recursive functions
    val answer = FamilyKB ask List(
      Predicate("ancestor", List(Atom("daniel"), Variable("P"))),
      Predicate("gender",   List(Variable("P"),  Atom("female")))
    )
    println(answer)
    assert(answer(0).get(Variable("P")).get == Atom("dontknow"))
  }
}

@main def ExamplesMain: Unit = {
  import Examples.* 
  familyTree
  println("done")

}
