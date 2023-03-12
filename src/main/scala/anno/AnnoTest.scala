package anno

import scala.annotation.StaticAnnotation

final class TestAnnotation extends StaticAnnotation

@TestAnnotation
class Annotated

@main def test = 
    println(MacroAnno.getAnnotations[Annotated]) // List(TestAnnotation)

