package DOT
/**
 * https://events.inf.ed.ac.uk/wf2016/slides/odersky.pdf
 * 
 * Virtual Types can model Type Parameters: List[T]
 */
object VirtualType {
  trait List { self =>
    type T
    def isEmpty: Boolean
    def head: T
    def tail: List { type T = self.T }
  }

  def Nil[X] =
    new List { self =>
      type T = X
      def isEmpty = true
      def head = ???
      def tail = ???
    }

  def Cons[X](hd: X, tl: List { type T = X }) =
      new List { self =>
          type T = X
          def isEmpty = false
          def head = hd
          def tail = tl
      }
}

/**
 * Encoding Covariance: Covariant Lists
 */
object Covariance {
  trait List { self =>
    type T
    def isEmpty: Boolean
    def head: T
    def tail: List { type T <: self.T }
  }

  def Nil =
    new List { self =>
      type T = Nothing
      def isEmpty = true
      def head = ???
      def tail = ???
    }

  def Cons[X](hd: X, tl: List { type T <: X }) =
      new List { self =>
          type T = X
          def isEmpty = false
          def head = hd
          def tail = tl
      }
}

/**
 * Polymorphic functions can be modeled as dependent functions.
 */
object PolymorphicFunctions {
    trait TypeParam { type TYPE }

    import Covariance.List
    def Cons(T: TypeParam)(hd: T.TYPE, tl: List { type T <: T.TYPE }) =
        new List { self =>
            type T = T.TYPE
            def isEmpty = false
            def head = hd
            def tail = tl
        }
}