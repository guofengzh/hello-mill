package typelevel.evidence

/**
 * https://gist.github.com/ragb/cb087d1befc85fc695decc38566fec28
 * 
 * 这个例子与
 *    https://partialflow.wordpress.com/2017/07/26/dependent-types-type-level-programming/
 * 给出的Nat的定义的其它方面都相同，唯一不同的地方是：
       这里使用了path dependent type or dependent object type
 */

// This is a natural number
trait Nat:
  type N <: Nat   // 使用依赖类型

// This is Zero
class Zero extends Nat:
  type N = Zero

// This is the successor of Some natural number P,
// which itself happens to be a natural number
class Suc[P <: Nat] extends Nat:
  type N = Suc[P]

// Give proper names to some numbers
type One = Suc[Zero]
type Two = Suc[One]

// Print the runtime integer for the non-believers
val toInt = ToInt[Two]
def stepOne = println(toInt())

// More names
type Three = Suc[Two]
type Four = Suc[Three]
type Five = Suc[Four]
type Six = Suc[Five]

// 为使用依赖类型和给定参数，我们为Plus定义一个依赖类型
// Tells that N + P = Out
trait Plus[N, P]:
  type Out <: Nat

object Plus:
  // Aux pattern: makes the type member a type parameter
  type Aux[N <: Nat, P <: Nat, Out0 <: Nat] = Plus[N, P] { type Out = Out0}

  // 给出证据，首先给出0与其它数相加的情况下的证据
  // 0 + N = N
  given zeroPlusN[N <: Nat]: Aux[Zero, N, N] = new Plus[Zero, N] { type Out = N}

  // 给出下一个数与其它数相加的证据 -- 递归，更进一步地接近0
  // N + (P + 1) = Out -> (N + 1) + P = Out
  // Zero will be found, don't be afraid
  given plus1[N <: Nat, P <: Nat](using plus: Plus[N, Suc[P]]): Aux[Suc[N], P, plus.Out] = 
       new Plus[Suc[N], P] { type Out = plus.Out}

  // implicit helper -- 帮助编译器构造证据（constructive type theory），即类型的某个项
  def apply[N <: Nat, P <: Nat](using plus: Plus[N, P]) = plus

def stepTwo =
  // Ask the compiler when in dobt
  summon[Plus.Aux[Two, Three, Five]]

  summon[Plus[Three, Three]]
  summon[Plus[Two, Five]]


// 我们以同样的方法定义乘
// Tells that N * P = Out
trait Prod[N, P]:
  type Out <: Nat

object Prod:
  type Aux[N, P, Out0] = Prod[N, P] { type Out = Out0}

  // 0 * N = 0
  given prodZero[N <: Nat]: Aux[Zero, N, Zero] = new Prod[Zero, N] {type Out = Zero}

  // N * P = Q -> (N + 1) * P = Q + P,
  // P + Q = Out -> (N + 1) * P = Out
  given prodSuc[N <: Nat, P <: Nat, Q <: Nat](
    using prod: Prod.Aux[N, P, Q], // N * P = Q
    plus: Plus[P, Q]) /* P + Q = plus.Out */ : Prod.Aux[Suc[N], P, plus.Out] // (N + 1) * P = plus.Out
         = new Prod[Suc[N], P] {type Out = plus.Out}

  def apply[N <: Nat, P <: Nat](implicit prod: Prod[N, P]) = prod


// Trust the compiller
def stepThree = 
  summon[Prod.Aux[Two, Two, Four]]

// Tells that Factoria[N] = Out
trait Factorial[N <: Nat]:
  type Out <: Nat

object Factorial:
  type Aux[N <: Nat, Out0 <: Nat] = Factorial[N] {type Out = Out0}

  // Factorial[0] = 1
  given factorialZero: Factorial.Aux[Zero, One] = new Factorial[Zero] { type Out = One}
  // Factorial(n+1) = (n + 1) * Factorial(N)
  given FactorialSuc[N <: Nat, F <: Nat, F1 <: Nat](
    using factN: Factorial.Aux[N, F1], // Factorial(N) = F1
    prod: Prod.Aux[Suc[N], F1, F]) /* (n + 1) * F1 = F */ : Factorial.Aux[Suc[N], F] // Factorial(N + 1) = F
        = new Factorial[Suc[N]] { type Out = F}

  def apply[N <: Nat](implicit factorial: Factorial[N]) = factorial

def stepFour = 
  summon[Factorial.Aux[Zero, One]]
  //summon[Factorial.Aux[Three, Six]]
  //Factorial[Four]
