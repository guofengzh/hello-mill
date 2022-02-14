package DOT

/**
 * https://events.inf.ed.ac.uk/wf2016/slides/odersky.pdf
 */
trait Key:
  type Value

// type Value is a abstract type declaration
// key.Value is a path-dependent type.

class Setting(val str: String) extends Key

val sort = new Setting("sort") { type Value = String }
val width = new Setting("width") { type Value = Int }

trait HMap: 
    self =>
      def get(key: Key): Option[key.Value]
      def add(key: Key)(value: key.Value) = new HMap:
        def get(k: Key) =
           if (k == key) then Some(value.asInstanceOf[k.Value])
           else self.get(k)

object HMap:
  def empty = new HMap:
      def get(k: Key) = None

val params = HMap.empty
  .add(width)(120) 
  .add(sort)("time")

