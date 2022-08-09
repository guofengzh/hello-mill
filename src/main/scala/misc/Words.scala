package misc

class Words(words: Seq[String], index: Int) extends Product:
  def _1 = words
  def _2 = index

  def canEqual(that: Any): Boolean = ???
  def productArity: Int = ???
  def productElement(n: Int): Any = ???

object Words:
  def unapply(si: (String, Int)): Words =
    val words = si._1.split("""\W+""").toSeq
    new Words(words, si._2)

@main def wordsSeq : Unit =
  val books = Seq(
    "Programming Scala",
    "JavaScript: The Good Parts",
    "Scala Cookbook").zipWithIndex // add an "index"

  val result = books.map {
    case Words(words, index) => s"$index: count = ${words.size}"
  }

  println(result)
