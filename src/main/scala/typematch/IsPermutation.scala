package typematch

/**
 * A (recursive) match type IsPermutation[T1 <: Tuple, T2 <: Tuple] that evaluates to true if T1 and T2 
 * are permutations, allowing for duplicates.
 */
type IsPermutation[T <: Tuple, T2 <: Tuple] <: Boolean = (T, T2) match {
  case (EmptyTuple, EmptyTuple) => true
  case (EmptyTuple, ?) => false
  case (?, EmptyTuple) => false
  case (h *: tail, ?) => IsPermutation[tail, Remove[T2, h]]
}

type Remove[T <: Tuple, X] <: Tuple = T match {
  case EmptyTuple => EmptyTuple
  case head *: tail => head match {
    case X => tail
    case ? => head *: Remove[tail, X]
  }
}

val _1 : IsPermutation[(Int, Int), (Int, Int)] = true
val _2 : IsPermutation[(Int, String), (String, Int)] = true

val _3 : IsPermutation[(Int, String), (Int, Int)] = false
val _4 : IsPermutation[(Int, Int, Int), (Int, Int)] = false
val _5 : IsPermutation[(Int, Int), Tuple1[Int]] = false
