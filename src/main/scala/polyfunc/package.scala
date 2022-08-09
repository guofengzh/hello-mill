package polyfunc

/* polymorphic function type: one type parameter [A] */
def foo[A](a: A): List[A] = List(a)

val f:[A] => A      => List[A] = 
      [A] => (a:A)  => foo(a)

/* polymorphic function type: multiple type parameters [A, B] */
def goo[A, B](a: A) : Either[A, B] = Left(a)

val g:[A, B] => A      => Either[A, B] = 
      [A, B] => (a: A) => goo(a)

@main def TestPf: Unit =
    println(f(1))
    println(f("hi"))

/* type lambda: one type parameter */
type F = [X] =>> (X, X)

/* type lambda: multiple paramters */
type G = [X, Y] =>> Map[Y, X]

/* type lambda: high ordered */ 
type E = [X] =>> [Y] =>> Either[X, Y]
