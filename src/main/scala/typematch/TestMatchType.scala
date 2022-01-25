package typematch

type TestMatchType[T] = T match
  case "String1" => Int
  case "String2" => String

/**
 * Use [T <: Singleton] or use 'input.type' as an argument to TestMatchType
 */
def testMatchType[T](input: T): TestMatchType[input.type] =
  input match
    case "String1": "String1" => 1
    case "String2": "String2" => "Two"

@main def TestMatchTypeMain: Unit =
    val string1Output: String = testMatchType("String2")
    println(string1Output)