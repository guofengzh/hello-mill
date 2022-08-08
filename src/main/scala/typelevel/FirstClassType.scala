package typelevel

// https://blog.oyanglul.us/scala/dotty/en/first-class-types

type IsSingleton[X <: Boolean] = X match {
  case true => Int
  case false => List[Int]
}

