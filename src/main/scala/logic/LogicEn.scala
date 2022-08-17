/*
|-------—————----|---------------|-----------------------|
| Curry          | Howard        | Lambek                |
|------—————-----|---------------|-----------------------|
| Logic          | Types         | Category Theory       |
|-—————----------|---------------|-----------------------|
| False ⊥        | Void          | Initial object        |
| True ⊤         | Unit          | Terminal object       |
| Not ¬          | a -> Void     | Morphism to initial   |
| And ∧          | (a, b)        | Product               |
| Or ∨           | Either a b    | Coproduct             |
| If/Then →      | a -> b        | Exponentials          |
| For All ∀      | forall a. a   | ...                   |
| There Exists ∃ | GADTS, &c     | ...                   |
|--------————--—-|---------------|-----------------------|
*/
object LogicEn:
  
  // The analog in types is pairs, or (Cartesian) product types:
  //   type And a b = (a, b)
  def and(a: Boolean, b: Boolean) = (a, b) match
     case (false, false) => false
     case (false, true)   => false
     case (true, false)  => false
     case (true, true)   => true

  def or(a: Boolean, b: Boolean) = (a, b) match
     case (false, false) => false
     case (false, true)   => true
     case (true, false)  => true
     case (true, true)   => true
   
  // implication, or "if/then", next. At the value level:
  def ifThen(a: Boolean, b: Boolean) = (a, b) match
     case (false, false) => true
     case (false, true)   => true
     case (true, false)  => false
     case (true, true)   => true 

  def not(a: Boolean) = ifThen(a, false)
