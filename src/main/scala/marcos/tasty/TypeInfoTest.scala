package marcos.tasty

@main def TypeInfoTest: Unit =
  case class NonEmpty[T](e: T, tail: Option[NonEmpty[T]])

  println(TypeInfo[NonEmpty])
