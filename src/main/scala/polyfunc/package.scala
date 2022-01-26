package polyfunc

def foo[A](a: A): List[A] = List(a)

val f:[A] => A => List[A] 
         = [A] => (a:A)  => foo(a)

@main def TestPf: Unit = {
    println(f(1))
    println(f("hi"))
}

type F = [X] =>> (X, X)
