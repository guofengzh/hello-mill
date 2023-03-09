package propositions

// ⊢p ∨ q → ¬p → q

def proof1[P, Q]: Either[P, Q] => (Not[P] => Q) =
  (e: Either[P, Q]) =>
    (np: P => Nothing) =>
       e match
         case Right(q: Q) => q : Q
         case Left(p: P) => np(p) : Q

// {q ∨ ¬q} ⊢ ¬(p ∧ q) → ¬p ∨ ¬q

def proof2[P, Q](lemq: Either[Q, Not[Q]]): Not[(P, Q)] => Either[Not[P], Not[Q]] = 
    lemq match
        case Left(q: Q) => 
            (npq: Not[(P, Q)]) => 
                Left((p: P) => npq((p : P, q : Q)) : Nothing) : Either[Not[P], Not[Q]]
        case Right(nq: Not[Q]) => 
            (_: Not[(P, Q)]) => 
                Right((q: Q) => nq(q) : Nothing) : Either[Not[P], Not[Q]]
