package hello

@main def hello2(arg: String*): Unit =
  if arg.isEmpty then println("hello")
  else println(s"hi ${arg.head}")
