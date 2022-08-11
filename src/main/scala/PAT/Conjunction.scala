package PAT

/*

Scala Cat
https://www.evernote.com/shard/s436/nl/76492406/782376c4-1038-fdc0-d8ac-a5343a157c15?title=**%20Category%E5%9F%BA%E6%9C%AC%E8%A7%A3%E8%AF%B4%20-%20Product/Coproduct

∧

A true B true
-------------∧I
 A ∧ B true

where the rule is that wherever an instance of "A" and "V" appear on lines of a proof, 
a "A ∧ B" can be placed on a subsequent line.

conjunction elimination (also called and elimination, ∧ elimination, or simplification)[

A ∧ B true          A ∧ B true
----------∧EL      ------------ ∧ER
  A true              B true

(A ∧ B) → A         (A ∧ B) → B

The two sub-rules together mean that, whenever an instance of "A ∧ B" appears on a line of a proof,
either "A" or "B" can be placed on a subsequent line by itself. The above example in English is an
application of the first sub-rule.

Conjunction
          A ∧ B ≡ ΠC : * .(A → B → C) → C
           For all C, (A implies (B implies C)) implies C
∨

  A true               B true
------------∨IL    ------------ ∨IR
A ∨ B true          A ∨ B true

where the rule is that whenever instances of "A" appear on lines of a proof, 
"A ∨ B" can be placed on a subsequent line.

Disjunction introduction is not a rule in some paraconsistent logics because in combination 
with other rules of logic, it leads to explosion (i.e. everything becomes provable) and 
paraconsistent logic tries to avoid explosion and to be able to reason with contradictions.

我们如何在非正式推理中使用假设 A ∨ B？ 我们经常进行案例证明：我们在假设 A 下证明结论 C，并在假设 B 下证明 C。然后我们根据假设得出结论 C，因为 A 或 B。 因此，消除规则采用了两个假设判断。

disjunction elimination (sometimes named proof by cases, case analysis, or or elimination)
              A true        B true
              .             .
              .             .
              .             .
A ∨ B true   C true        C true
----------------------------------- VE(u,w)
              C true
(A → C), (B → C), (A ∨ B) |→ C
the rule is that whenever instances of A → C, and B → C, and A ∨ B appear on lines of a proof, 
C can be placed on a subsequent line. "|→"  is a metalogical symbol meaning that C is a syntactic 
consequence of A → C, and B → C, and A ∨ B in some logical system.

Disjunction
         A ∨ B ≡ ΠC : * .(A → C) → (B → C) → C
          For all C, (A → C implies that (B → C implies C)).
*/
object Conjunction:
  trait A
  trait B
  trait C

  trait Implication[X, Y]:
    def apply(using t: Implication[X, Y]) = t 

  given `B implies C`(using b: Implication[B, C]): Implication[B, C]()
  given `A implies (B implies C)`(using a: Implication[A, Implication[B, C]]) : Implication[A, C]()

  def hh[X, Y, Z](using q: Implication[A, C]) = 
      println("done") 

  def main(args: Array[String]) = 
    hh[A, B, C]
