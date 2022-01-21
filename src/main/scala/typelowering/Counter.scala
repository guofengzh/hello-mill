package typelowering

object Solution1 {
  
  trait ICounter {
    type T
    def start: T
    def next(current: T): T
    def done(current: T): Boolean 
  }

  def m(ic: ICounter): Unit =
  {
    var x = ic.start;
    while (!ic.done(x)) do
    {
        x = ic.next(x);
    }
  }

  /**
   * this is a sample implementation
   */
  class Counter extends ICounter
  {
    type T = Int
    def start = 0
    def next(current: Int) = current + 1
    def done(current: Int) = current == 42;
  }
}