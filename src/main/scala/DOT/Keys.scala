package DOT

/** 
 * here is a trait Keys that defines an abstract type Key and a way to retrieve a key
 * from a string.
 */
trait Keys:
  type Key
  def key(data: String): Key

/**
 * A concrete implementation of Keys could be
 */
object HashKeys extends Keys:
  type Key = Int
  def key(s: String) = s.hashCode

/**
 * Here is a function which applies a given key generator to every element 
 * of a list of strings.
 */
def mapKeys(k: Keys, ss: List[String]): List[k.Key] = ss.map(k.key)
