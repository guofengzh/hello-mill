package forall

type ∀[+F[_]] = Forall[F]
val ∀ = Forall   // Forall is a value, ∀(...) will invoke Forall.apply(...)

type ∃[+F[_]] = Exists[F]
val ∃ = Exists
