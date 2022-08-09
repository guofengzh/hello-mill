package prolog

/**
 * Depth-first search in a knowledge database with backtracking
 * 
 * In prolog all statements are given in the form of horn clauses. A prolog program can be seen as
 * a list of clauses. A query to that database is a goal list of predicates. All goals have to be
 * found true in the database in order to full fill the query.
 * 
 * Inference in a prolog program.
 * Given a set of clauses (a program)
 * the inference tries to find a substitution
 * such that the query evaluates true.
 *
 * After search, the program finds a path through the
 * renamings in the substitution list such that all variables in the
 * goal clause map to a final atom.
 *
 * [1] Ivan Bratko: "Prolog Programming for Artificial Intelligence",
 *     4th ed., Chapter 2, Adison Wesley, 2011
 */

import scala.collection.mutable;

class Inference(standarizer: VariableStandarization, program: Map[String, List[Clause]]):

  import Unification.*
  import Inference.*

  def inferAll(goals: List[Predicate]): Solutions =
    val x = inferAll(goals, Map.empty[Variable, Term])
    x.map(solution => assign(goals, solution))

  protected def inferAll(goals: List[Predicate], solution: Substitution): Solutions =
    if(goals.isEmpty) then List(solution)
    else
      val goal   = goals.head
      val other  = goals.tail
      val answer = for (clause <- program(goal.functor)) yield
        val Clause(standarizedHead, standarizedBody) = standarizer.standarizeAppart(clause)
        val substitution = mutable.Map.empty[Variable, Term] ++ solution
        val unifyable    = unify(goal, standarizedHead, substitution)
        if(unifyable) then
          val newGoals = substitute(standarizedBody ::: other, substitution.toMap)
          val x = inferAll(newGoals.collect{case x: Predicate => x}, substitution.toMap)
          x
        else Nil
      answer.flatten


/**
 * The search algorithm that finds all solutions is a depth first search algorithm with backtracking.
 * If the goal list is empty the program returns the current substitution. In the implementation 
 * substitution is a map from variables to assigned terms.

 * The search algorithm checks the complete database for a match of a clause head with the first goal.
 * If found, all the variables in the clause head and body are renamed so that there is no collisions
 * and the clause head is then unified with the current goal. If the unification succeeds, the body is 
 * added to the rest of the goal list (all elements except the first) and then all the substitutions 
 * that can be applied will be performed.  The search continues recursively with the new goal list. 
 * The algorithm returns a list of assignments to the variables in the query.
 */
object Inference:

  type Substitution = Map[Variable, Term]

  type Solutions    = List[Substitution]

  def substitute(x: List[Term], subs: Substitution): List[Term] = x.map {
    case term: Variable           => subs.get(term).getOrElse(term)
    case Predicate(functor, body) => Predicate(functor, substitute(body, subs))
    case term                     => term
  }

  /**
   * The last thing we need is to output all the assignments for the variables in the query 
   * from the result list. Since we renamed them multiple times, we have to find a path from 
   * the original variable name to a term that is not a variable.
   */
  def assign(predicate: List[Predicate], substitution: Substitution): Substitution =
    val variables: List[Variable] = predicate.flatMap {
      case Predicate(_, body) => body.flatMap(getAllVariables)
    }.distinct
    variables.map(
      x => x -> findAssignment(x, substitution)
    ).toMap

  def getAllVariables(term: Term): List[Variable] = term match
    case x: Atom[?]         => List.empty
    case x: Variable        => List(x)
    case Predicate(_, body) => body.flatMap(getAllVariables)

  def findAssignment(x: Term, substitution: Substitution): Term = x match
    case x: Variable => findAssignment(substitution(x), substitution)
    case Predicate(functor, body) => Predicate(functor, body.map(x => findAssignment(x, substitution)))
    case x => x

  def apply(program: Map[String, List[Clause]]): Inference = new Inference(new VariableStandarization, program)
  
