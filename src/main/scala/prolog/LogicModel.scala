package prolog

/** 
 * http://daniel-at-world.blogspot.com/2017/02/ai-logic-programming-in-scala.html
 * 
 * In prolog, a term can be one of a atom, a variable, or a predicate.
 */
sealed trait Term

/**
 * Atoms can be strings or integer numbers.
 */
case class Atom[T](val x: T) extends Term
 
/**
 * Variables in Prolog start with an uppercase letter (atoms and predicates do not).
 * 
 * unification(
 *   Predicate(parent, List(Variable(X), Atom(peter))),
 *   Predicate(parent, List(Atom(sam), Variable(Y)))
 * ) == true, the map is (X -> sam, Y -> peter)
 * 
 * So the variables are assigned with the matching atoms. 
 * 
 * Variables can also be bound to other variables. In this case their values will be the same as 
 * in the following map:
 *   X -> Y
 *   Y -> sam
 */
case class Variable(name: String) extends Term

/**
 * Facts are written down as predicates of the form: name(value1, ..., valueN).
 * 
 * e.g.,
 *   parent(sam, peter).
 *   parent(sam, joana).
 *   parent(peter, maria).
 */
case class Predicate(functor: String, terms: List[Term]) extends Term {
 
  def arity: Int = terms.size
 
  infix def matches(other: Predicate): Boolean = other.functor == functor && other.arity == arity
 
}
 
/**
 * Rules in Prolog equate Horn- Clauses in First Order Logic: y <- x AND y AND z.
 * 
 * e.g., 
 * ancestor(Person,Ancestor):-
 *   parent(Person, Ancestor).
 *
 * ancestor(Person, Ancestor):-
 *   parent(Person,Parent),
 *   ancestor(Parent, Ancestor).
 * 
 * The clause and the body are other predicates separated by commas. If all statements in the body
 * evaluate true, the clause is true, too.
 * 
 * Facts are a clause with an empty body.
 */
case class Clause(head: Predicate, body: List[Predicate])

/**
 * add an easy way to handle lists. Especially to create lists in the binary tree form.
 */
object ListBuilder {

  /**
   * a special predicate name for all list elements '.'.
   */
  final val ListFunctor = "."

  /**
   * an atom indicating the end of a list called nil. G
   */
  final val End = Atom("nil")

  /**
   * Create a split of a list. A 'split' splits the list into its head and the rest of the list. 
   * In prolog this is written as '[A|B]'. A is the head and B is the tail. 
   
   * The split can actually be matched directly to a list predicate.
   */
  def split(head: String, tail: String) = Predicate(ListFunctor, List(Variable(head), Variable(tail)))

  def split(head: Term, tail: Term) = Predicate(ListFunctor, List(head, tail))

  def apply(x: List[Term]):Term = binary(x)

  def binary(x: List[Term]): Term =
    if(x.isEmpty) then End
    else Predicate(ListFunctor, List(x.head, binary(x.tail)))

  def flatten(x: Predicate): List[Term] = (x: @unchecked) match {
    case Predicate(_, List(h, End)) => List(h)
    case Predicate(_, List(h, tail)) => h :: flatten(tail.asInstanceOf[Predicate])
   }

}
