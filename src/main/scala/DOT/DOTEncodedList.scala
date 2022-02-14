package DOT

object scala_collection_immutable:
  sci =>
    trait List:
        self =>
          type A
          def isEmpty: Boolean
          def head: self.A
          def tail: List{type A <: self.A}

    def nil: sci.List{type A = Nothing} = new List:
      self =>
        type A = Nothing
        def isEmpty = true
        def head: A = ???
        def tail: List{type A = Nothing} = ???

    def cons(x: {type A})(hd: x.A)(tl: sci.List{type A <: x.A})
      : sci.List{type A <: x.A} = new List:
            self =>
              type A = x.A
              def isEmpty = false
              def head = hd
              def tail = tl
