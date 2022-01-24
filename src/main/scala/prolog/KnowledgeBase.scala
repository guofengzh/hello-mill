package prolog

import scala.collection.mutable

/**
  * A knowledge database.
  *
  * tell can be used to add knowledge and ask can be used to run
  * inference queries. Basically a wrapper around the inference [1] 
  *
  * [1] Stuart Russel, Peter Norvig: "Artificial Intelligence a Modern Approach", Pearson, 2011
  *
  */
class KnowledgeBase(val program: mutable.Queue[Clause]) {

  def index = program.groupBy(clause => clause.head.functor).map {
    case (idx, clauses) => idx -> clauses.toList
  }

  infix def tell(rule: Predicate, body: List[Predicate]): Unit = program.enqueue(Clause(rule, body))

  infix def tell(fact: Predicate): Unit = tell(fact, List.empty)

  infix def tell(clause: Clause): Unit = program.enqueue(clause)

  infix def ask(goal: List[Predicate]) = Inference(index).inferAll(goal)

}

object KnowledgeBase {

  def apply(): KnowledgeBase = new KnowledgeBase(mutable.Queue.empty)
  
}
