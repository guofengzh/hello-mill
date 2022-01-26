package typematch

/**
 * https://stackoverflow.com/questions/68687990/scala-3-a-match-type-could-not-be-fully-reduced-with-literal-types
 * 
 * https://dotty.epfl.ch/docs/reference/new-types/match-types.html
 */
type TestMatchType[T] = T match
  case "String1" => Int
  case "String2" => String

/**
 * Use [T <: Singleton] or use 'input.type' as an argument to TestMatchType
 */
def testMatchType[T <: Singleton](input: T): TestMatchType[T] =
  input match
    case "String1": "String1" => 1
    case "String2": "String2" => "Two"

@main def TestMatchTypeMain: Unit =
    val string1Output: Int = testMatchType("String1")
    println(string1Output)
