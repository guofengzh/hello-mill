package com.mycompany.mydomainA // this is the enclosing scope of MyClass etc.

class MyClass(s: String):
    class mymethod:
        def g = 5 ;
    def mymethod(t: String): String = 
        s+t

def myfun(s:String): String = s

//MyClass("hello")   // error
//myfun("world")     // error

package dd.bb:
  val myObj = MyClass("hello") // ok
  val res = myfun("world")     // ok


  class mydomainA:
    class BB
    def BB = 5


val pf: PartialFunction[(Int, String), String] = 
    case (fst, snd) => s"Hello, fst is $fst, snd is $snd"

def m2(s: String): String =
  val result = s.toUpperCase
  println(s"output: $result")
  result

def m3(s: String): String =
  val result = s.toUpperCase
  println(s"output: $result")
  result


def inv = 
  m2{val s1 = "hello" ;   val s2 = "world" ; s"we said to them $s1, $s2"}

package p1.p2:

  abstract class C():

    def this(x: Int) =
      this()
      var y =
        x
      end y
      while y > 0 do
        println(y)
        y -= 1
      end while
    end this

    def f: String
  end C

  object C:
    given C =
      new C:
        def f = "!"
        end f
      end new
    end given
  end C

  extension (x: C)
    def ff: String = x.f ++ x.f
  end extension

end p2