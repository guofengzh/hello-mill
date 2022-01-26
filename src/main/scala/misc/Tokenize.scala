package misc

object Tokenize:
  // def unapplySeq(s: String): Option[Seq[String]] = Some(tokenize(s))
  def unapplySeq(lim_s: (Int,String)): Option[Seq[String]] =
    val (limit, s) = lim_s
    println(s)
    if limit > s.length then None
    else
      val seq = tokenize(s).filter(_.length >= limit)
      Some(seq)

  def tokenize(s: String): Seq[String] = s.split("""\W+""").toSeq

@main def TokenizeSeq: Unit = {
  val message = "This is Programming Scala v3"
  val limits = Seq(1, 3, 20, 100)

  val results = for limit <- limits yield (limit, message) match
    case Tokenize() => s"No words of length >= $limit!"
    case Tokenize(a, b, c, d*) => s"limit: $limit => $a, $b, $c, d=$d"
    case x => s"limit: $limit => Tokenize refused! x=$x"

    println(results)
}
