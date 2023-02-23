package typeprojection

object BAD:

  /*
  * Suppose that, whenever type A can represent a less-than-perfectly-precise 
  * measurement of a physical value, I'll have an instance of Imprecise[A].
  */
  trait Imprecise[A]:
    type Precision
    val ord: Ordering[Precision]
    def precision(a: A): Precision

  /*
   * For example, this Bounds type has a given instance in its companion object:
   */
  case class Bounds[N: Numeric](min: N, max: N):
    if summon[Ordering[N]].compare(min, max) > 1 then
      throw new IllegalArgumentException

  object Bounds:
    import scala.math.Numeric.Implicits.infixNumericOps
    import scala.math.Ordering.Implicits.infixOrderingOps
    given [N: Numeric]: Imprecise[Bounds[N]] with
      type Precision = N
      val ord = summon[Ordering[N]]
      def precision(rng: Bounds[N]) = rng.max - rng.min

  /*
   * Now my program can start dealing with the physical features that need to be observed:
   */
  trait Feature[A:  Imprecise]:
    type Observation = A
    val imprecise = summon[Imprecise[A]]
    type Precision = imprecise.Precision

    def within(bound: Precision) = new RequiredFeature(this, bound)

  class RequiredFeature(val feature: Feature[?], val min_precision: feature.Precision)

  case class Temperature(chamber_id: Int) extends Feature[Bounds[Double]]
  case class Pressure(chamber_id: Int) extends Feature[Bounds[Float]]

  // But when, at last, I try to make a required feature:
  //val rf = Temperature(3).within(7.0) // Compiler complaint: Found:    (7.0d : Double) Required: ?1.Precision

  // 问题出现在: 
  //   trait Feature[A: Imprecise] 被脱糖为 trait Feature[A](使用 ev: Imprecise[A]) 在这里你失去了类型细化，
  //   将 Imprecise[A] { type Precision = ... } 向上转换为 Imprecise[A]。
  //
  // 因此，您应该恢复不精确的类型细化并将类型参数添加到Feature。

end BAD

object GoodSolution1:  // add a type parameter P to Feature

  trait Imprecise[A]:
    type Precision
    val ord: Ordering[Precision]
    def precision(a: A): Precision

  object Imprecise:
    type Aux[A, P] = Imprecise[A]:
      type Precision = P

  case class Bounds[N: Numeric](min: N, max: N):
    if summon[Ordering[N]].compare(min, max) > 0 then
      throw new IllegalArgumentException

  object Bounds:
    import Numeric.Implicits.*
    given [N: Numeric]: Imprecise[Bounds[N]] with
      type Precision = N
      val ord = summon[Ordering[N]]
      def precision(rng: Bounds[N]) = rng.max - rng.min

  trait Feature[A, P](using Imprecise.Aux[A, P]):
    type Observation = A
    val imprecise = summon[Imprecise[A]]
    type Precision = imprecise.Precision

    def within(bound: Precision) = new RequiredFeature(this, bound)

  class RequiredFeature(val feature: Feature[?,?], val min_precision: feature.Precision)

  case class Temperature(chamber_id: Int) extends Feature[Bounds[Double], Double]
  case class Pressure(chamber_id: Int) extends Feature[Bounds[Float], Float]

  val rf = Temperature(3).within(7.0)

end GoodSolution1

object GoodSolution2:  //  add a type member to Feature

  trait Imprecise[A]:
    type Precision
    val ord: Ordering[Precision]
    def precision(a: A): Precision

  object Imprecise:
    type Aux[A, P] = Imprecise[A]:
      type Precision = P

  case class Bounds[N: Numeric](min: N, max: N):
    if summon[Ordering[N]].compare(min, max) > 0 then
      throw new IllegalArgumentException

  object Bounds:
    import Numeric.Implicits.*
    given [N: Numeric]: Imprecise[Bounds[N]] with
      type Precision = N
      val ord = summon[Ordering[N]]
      def precision(rng: Bounds[N]) = rng.max - rng.min

  trait Feature[A]:
    type P
    val ev: Imprecise.Aux[A, P]
    given Imprecise.Aux[A, P] = ev

    type Observation = A
    val imprecise = summon[Imprecise[A]] // ev
    type Precision = imprecise.Precision // P

    def within(bound: Precision) = new RequiredFeature(this, bound)

  class RequiredFeature(val feature: Feature[?], val min_precision: feature.Precision)

  case class Temperature[_P](chamber_id: Int)(using _ev: Imprecise.Aux[Bounds[Double], _P]) extends Feature[Bounds[Double]]:
    type P = _P
    val ev = _ev
  case class Pressure[_P](chamber_id: Int)(using _ev: Imprecise.Aux[Bounds[Float], _P]) extends Feature[Bounds[Float]]:
    type P = _P
    val ev = _ev

  val rf = Temperature(3).within(7.0)

end GoodSolution2


trait Foo[In]:
  type Out
  def f(v: In): Out

implicit val preciseFooInt: Foo[Int] { type Out = String } = new Foo[Int]:
  type Out = String
  def f(v: Int): String = v.toString

val ev: Foo[Int] { type Out = String}= preciseFooInt

def g(ev: Foo[Int]) = ev.f(42)

def f: Unit = 
     val s: String = ev.f(42)
     val t: String = g(ev = preciseFooInt)

trait T[A]:
  type U

type Aux[A, P] = T[A]:
  type U = P
