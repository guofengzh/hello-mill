package propositions

// ⊢ p ∨ q → ¬p → q

def proof1[P, Q]: Either[P, Q] => (Not[P] => Q) =
  (e: Either[P, Q]) =>
    (np: P => Nothing) =>
       e match
         case Right(q: Q) => q : Q
         case Left(p: P) => np(p) : Q

@main 
def testPrrof1:Unit = 
  trait P:
    override def toString() = "p"

  trait Q:
    override def toString() = "q"

  val p = new P{}
  val q = new Q{}

  val lp = proof1(Left(p))
  val rq = proof1(Right(q))
  //val f: P => Nothing = x => ???
  val f: Not[P] = x => ???

  // lp(f)   // scala.NotImplementedError: an implementation is missing
  rq(f)

// {q ∨ ¬q} ⊢ ¬(p ∧ q) → ¬p ∨ ¬q

def proof2[P, Q](lemq: Either[Q, Not[Q]]): Not[(P, Q)] => Either[Not[P], Not[Q]] = 
    lemq match
        case Left(q: Q) => 
            (npq: Not[(P, Q)]) => 
                Left((p: P) => npq((p : P, q : Q)) : Nothing) : Either[Not[P], Not[Q]]
        case Right(nq: Not[Q]) => 
            (_: Not[(P, Q)]) => 
                Right((q: Q) => nq(q) : Nothing) : Either[Not[P], Not[Q]]
