package singleton

object BoundedConstraint {
  
}

import scala.compiletime.ops.int.*

// Only allowed values are Min <= N <= Max.
type Bounded[MIN <: Int, MAX <: Int] <: Int = MAX match 
  case MIN => MIN
  case _ => MAX | Bounded[MIN,MAX-1]


type Service = { 
  type Config 
  def default: Config 
  def init(data: Config): Unit 
}

type PersistentService = Service { 
  def persist(a: Service, config: a.Config): Unit
}

def identity(x:  Object { type T }) = ((a: x.T) => a )

val o = new Object{type T = Int}

import util.NotGiven.amb1
import util.NotGiven.amb2
val s = identity( o )(5)


