package deps.imprecision

/**
 * What does Dotty offer to replace type projections?
 * https://stackoverflow.com/questions/50043630/what-does-dotty-offer-to-replace-type-projections
 * 
 * In Scala 2.12 type projections sometimes can be replaced with type class + path-dependent types
 */

object BADCase:

  trait Contents
  class Foo extends Contents
  class Bar extends Contents

  trait Container[T <: Contents] { type ContentType = T }
  class FooContainer extends Container[Foo]
  class BarContainer extends Container[Bar]

  trait Manager[T <: Container[?]]:
    type ContainerType = T 
    type ContentType = T#ContentType
    def getContents: ContentType 
    def createContainer(contents: ContentType): ContainerType

end BADCase

object Scala212:

  trait Contents
  class Foo extends Contents
  class Bar extends Contents

  trait Container[T <: Contents] { type ContentType = T }
  class FooContainer extends Container[Foo]
  class BarContainer extends Container[Bar]

  trait ContentType[T <: Container[?]] {
    type Out
  }
  object ContentType {
    type Aux[T <: Container[?], Out0] = ContentType[T] { type Out = Out0 }
    def instance[T <: Container[?], Out0]: Aux[T, Out0] = new ContentType[T] { type Out = Out0 }

    implicit def mk[T <: Contents]: Aux[Container[T], T] = instance
  }

  abstract class Manager[T <: Container[?]](implicit val contentType: ContentType[T]) {
    type ContainerType = T
    def getContents: contentType.Out
    def createContainer(contents: contentType.Out): ContainerType
  }

end Scala212

object Dotty:

  trait Contents
  class Foo extends Contents
  class Bar extends Contents

  trait Container[T <: Contents] { type ContentType = T }
  class FooContainer extends Container[Foo]
  class BarContainer extends Container[Bar]

  trait ContentType[T <: Container[?]] {
    type Out
  }
  object ContentType:
    given [T <: Contents]: ContentType[Container[T]] with
      type Out = T

  trait Manager[T <: Container[?]] (using val contentType: ContentType[T]) {
    type ContainerType = T
    type ContentType = contentType.Out
    def getContents: ContentType
    def createContainer(contents: ContentType): ContainerType
  }

end Dotty

object DottyMatchType:

  trait Contents
  class Foo extends Contents
  class Bar extends Contents

  trait Container[T <: Contents] { type ContentType = T }
  class FooContainer extends Container[Foo]
  class BarContainer extends Container[Bar]

  type ContentType[T <: Container[?]] = T match {
    case Container[t] => t
  }

  trait Manager[T <: Container[?]] {
    type ContainerType = T
    def getContents: ContentType[T]
    def createContainer(contents: ContentType[T]): ContainerType
  }

end DottyMatchType