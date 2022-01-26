package typematch

/**
 * https://docs.scala-lang.org/scala3/reference/new-types/type-lambdas-spec.html
 */

// type T[X] = (X, X)
// is regarded as a shorthand for an unparameterized definition with a type lambda as right-hand side:
type T = [X] =>> (X, X)

type U[R] = [X] =>> (R, X)
