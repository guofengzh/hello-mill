package misc

import scala.concurrent.{Future, ExecutionContext, Await}
import scala.concurrent.duration.*
import scala.language.postfixOps

object Executable extends App {

  type Executable[T] = ExecutionContext ?=> T

  def f1(n: Int): Executable[Future[Int]] = f2(n + 1)
  def f2(n: Int): Executable[Future[Int]] = f3(n + 1)
  def f3(n: Int): Executable[Future[Int]] = f4(n + 1)
  def f4(n: Int): Executable[Future[Int]] = {
    val ex = summon[ExecutionContext]
    Future {
      println(s"Hi from the future! n is $n")
      n
    }
  }

  {
    given ec: ExecutionContext = scala.concurrent.ExecutionContext.global
    Await.result(f1(10), 1 second)
    // Hi from the future! n is 13
  }

}