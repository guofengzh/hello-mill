package deps

sealed trait DepType[N <: Nat] { 
  type T 
} 

given depType0: DepType[_0] with {
  type T = Int 
} 

given depType1: DepType[_1] with { 
  type T = String 
} 

given depType[N <: Nat]: DepType[Succ[Succ[N]]] with { 
  type T = Boolean 
} 

case class DepPair[N <: Nat, V](x: N, value: V)(using depType: DepType[N] { type T = V })

@main def DepTypeMain: Unit = {
  DepPair(_0, 10)
  DepPair( _1, "aaa") 
  DepPair( _2, true) 
  //DepPair( _2, "bbb") // error 
  DepPair( _3, false)
}

