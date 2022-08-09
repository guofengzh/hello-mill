package typepartial

object Solution1:
  // If at some point some call-site knows the types of arguments 
  // (they aren't actually Any => Any) it is doable using type classes:
  trait Commutative[In, Out]:
    def swap(in: In): Out

  object Commutative:

    def swap[In, Out](in: In)(implicit c: Commutative[In, Out]): Out =
      c.swap(in)

    implicit def tuple2[A, B]: Commutative[(A, B), (B, A)] =
      in => in.swap

  // call site
  def use[In, Out](ins: List[In])(implicit c: Commutative[In, Out]): List[Out] =
    ins.map(Commutative.swap(_))

  // However, this way you have to pass both In as well as Out as type parameters.   
  // If there are multiple possible Outs for a single In type, then there is not 
  // much you can do.

object Solution2:
  // But if you want to have Input type => Output type implication, 
  // you can use dependent types:
  trait Commutative[In]:
    type Out
    def swap(in: In): Out

  object Commutative:

    // help us transform dependent types back into generics
    type Aux[In, Out0] = Commutative[In] { type Out = Out0 }

    def swap[In](in: In)(implicit c: Commutative[In]): c.Out =
      c.swap(in)

    implicit def tuple2[A, B]: Commutative.Aux[(A, B), (B, A)] =
      in => in.swap

  // call site
  // This code is similar to the original code, but when the compiler
  // will be looking for In it will automatically figure Out.
  def use[In, Out](ins: List[In])(implicit c: Commutative.Aux[In, Out]): List[Out] =
    ins.map(Commutative.swap(_))

  // Alternatively, without Aux pattern:
  def use2[In](ins: List[In])(implicit c: Commutative[In]): List[c.Out] =
    ins.map(Commutative.swap(_))
// That's how you can solve the issue when you know the exact input type.
