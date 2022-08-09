package logic

/**
 * "a implies b" translating into "!a || b"
 */
object Implies:  
  extension (a: Boolean)
     def implies(b: => Boolean) : Boolean  = 
        !a || b 

  def main(args:Array[String]) : Unit =
      val v1 = true implies { println("evaluated"); true } 
      val v2 = false implies { println("evaluated"); true } 
      println(v1)
      println(v2)
