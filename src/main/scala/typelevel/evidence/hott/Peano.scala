package typelevel.evidence.hott

/**
 * How to ask Scala if evidence exists for all instantiations of type parameter?
 * https://stackoverflow.com/questions/67093833/how-to-ask-scala-if-evidence-exists-for-all-instantiations-of-type-parameter
 */

/* Given the following type-level addition function on Peano numbers */
sealed trait Nat
class O extends Nat
class S[N <: Nat] extends Nat

type plus[a <: Nat, b <: Nat] <: Nat = a match
  case O => b
  case S[n] => S[plus[n, b]]

/* say we want to prove theorem like
 *    for all natural numbers n, n + 0 = n
 * which perhaps can be specified like so */
type plus_n_O = [n <: Nat] =>> (plus[n, O]) =:= n

/* then when it comes to providing evidence for theorem we can easily ask Scala compiler 
 * for evidence in particular cases */
def demo = summon[plus_n_O[S[S[O]]]]  // ok, 2 + 0 = 2

/* but how can we ask Scala if it can generate evidence for all instantiations of [n <: Nat], 
 * thus providing proof of plus_n_0? */

/*******************************************************************************/
/* Here is one possible approach, which is an attempt at a literal interpretation of this paragraph:
 *    When proving a statement 
 *         E:N → U 
 *    about all natural numbers, it suffices to prove it for 0 and for succ(n), assuming it holds for n,
 *    i.e., we construct 
 *         ez:E(0) 
 *    and 
 *         es:∏(n:N)E(n)→E(succ(n)).
 *
 * from the HoTT book (section 5.1). https://hott.github.io/book/nightly/hott-online-1287-g1ac9408.pdf
 * Here is the plan of what was implemented in the code below:
 *    (*) Formulate what it means to have a proof for a statement that 
 *         "Some property P holds for all natural numbers". Below, we will use
 */
trait Forall[N, P[n <: N]]:
  inline def apply[n <: N]: P[n]
/*       where the signature of the apply-method essentially says 
 *         "for all n <: N, we can generate evidence of P[n]".
 *       Note that the method is declared to be inline. This is one possible way to ensure that 
 *       the proof of ∀n.P(n) is constructive and executable at runtime (However, see edit history 
 *       for alternative proposals with manually generated witness terms).
 * 
 *    (*) Postulate some sort of induction principle for natural numbers. Below, we will use the 
 *        following formulation:
 *           If
 *             P(0) holds, and
 *             whenever P(i) holds, then also P(i + 1) holds,
 *           then
 *             For all `n`, P(n) holds
 *        I believe that it should be possible to derive such induction principles using some 
 *        metaprogramming facilities.
 *
 *     (*) Write proofs for the base case and the induction case of the induction principle.
 */
trait NatInductionPrinciple[P[n <: Nat]] extends Forall[Nat, P]:
  def base: P[O]
  def step: [i <: Nat] => (P[i] => P[S[i]])
  inline def apply[n <: Nat]: P[n] =
    (inline compiletime.erasedValue[n] match
      case _: O => base
      case _: S[pred] => step(apply[pred])
    ).asInstanceOf[P[n]]

given liftCoUpperbounded[U, A <: U, B <: U, S[_ <: U]](using ev: A =:= B):
  (S[A] =:= S[B]) = ev.liftCo[[X] =>> Any].asInstanceOf[S[A] =:= S[B]]

type NatPlusZeroEqualsNat[n <: Nat] = (plus[n, O]) =:= n

def trivialLemma[i <: Nat]: ((plus[S[i], O]) =:= S[plus[i, O]]) =
  summon[(plus[S[i], O]) =:= S[plus[i, O]]]

object Proof extends NatInductionPrinciple[NatPlusZeroEqualsNat]:
  val base = summon[(plus[O, O]) =:= O]
  val step: ([i <: Nat] => NatPlusZeroEqualsNat[i] => NatPlusZeroEqualsNat[S[i]]) = 
    [i <: Nat] => (p: NatPlusZeroEqualsNat[i]) =>
      given previousStep: (plus[i, O] =:= i) = p
      given liftPreviousStep: (S[plus[i, O]] =:= S[i]) =
        liftCoUpperbounded[Nat, plus[i, O], i, S]
      given definitionalEquality: ((plus[S[i], O]) =:= S[plus[i, O]]) =
        trivialLemma[i]
      definitionalEquality.andThen(liftPreviousStep)

def demoNat(): Unit =
  println("Running demoNat...")
  type two = S[S[O]]
  val ev = Proof[two]
  val twoInstance: two = new S[S[O]]
  println(ev(twoInstance) == twoInstance)

/* It compiles, runs, and prints:
 *    true
 * meaning that we have successfully invoked the recursively defined method on the executable
 *  evidence-term of type two plus O =:= two. */

 /* Some further comments
  * 
  * (*) The trivialLemma was necessary so that summons inside of other givens don't accidentally 
  *     generate recursive loops, which is a bit annoying.
  * (*) The separate liftCo-method for S[_ <: U] was needed, because =:=.liftCo does not allow 
  *     type constructors with upper-bounded type parameters.
  * (*) compiletime.erasedValue + inline match is awesome! It automatically generates some sort 
  *     of runtime-gizmos that allow us to do pattern matching on an "erased" type. Before I found 
  *     this out, I was attempting to construct appropriate witness terms manually, but this does 
  *     not seem necessary at all, it's provided for free (see edit history for the approach 
  *     with manually constructed witness terms).
  */
