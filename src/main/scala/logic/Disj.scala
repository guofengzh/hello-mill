package logic

/**
 * n-ary unions, n = 3
 *   implicitly[¬¬[¬¬[Int]] <:< ((Int ∨ String) ∨ (Float ∨ Float))]
 */
trait Disj[T] { 
  type or[S] = Disj[T & ¬[S]]
  type apply = ¬[T]
}

// for convenience
type disj[T] = { type or[S] = Disj[¬[T]]#or[S] }

@main def DisjMain: Unit = {
    type T = disj[Int]#or[Float]#or[String]#apply
    implicitly[¬¬[Int] <:< T] // works
    // implicitly[¬¬[Double] <:< T] // doesn't work -- Cannot prove that logic.¬¬[Double] <:< T
}
