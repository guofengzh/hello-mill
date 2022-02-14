package DOT

/**
 * The first one is the standard parametric version. The second one defines the element type E as a type 
 * member, which can be referenced using a path-dependent type. To see the difference in use, 
 * we also have examples of the two respective signatures of a standard map function:
 */

object parametric_functional_style:
  class List[E] // parametric, functional style

  def map[E,T](xs: List[E])(fn: E => T): List[T] = ???

object modular_style_w_type_member:
  class List { type E } // modular style, w. type member
  def map[T] (xs: List)(fn: xs.E => T): List & { type E = T } = ???

