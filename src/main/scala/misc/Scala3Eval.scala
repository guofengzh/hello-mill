package misc

object Scala3Eval extends App:

  enum Exp:
    case Val(value: Int) extends Exp
    case Add(left: Exp, right: Exp) extends Exp
    case Mul(left: Exp, right: Exp) extends Exp
    case Var(identifier: String) extends Exp
  
  type Env = Map[String, Int]
  
  import Exp.*
  
  type WithEnv = Env ?=> Int

  def eval(exp: Exp): WithEnv =
    exp match
      case Var(id) => handleVar(id)
      case Val(value) => value
      case Add(l,r) => handleAdd(l,r)
      case Mul(l,r) => handleMul(l,r)

  def handleAdd(l: Exp, r: Exp): WithEnv = eval(l) + eval(r)
  def handleMul(l: Exp, r: Exp): WithEnv = eval(l) * eval(r)

  def handleVar(s: String): WithEnv =
    summon[Env].get(s).get

  val exp1 : Exp = Add(
      Var("z"),
      Add(
        Val(10),
        Mul(
          Var("x"),
          Var("y"))))
  
  // Provide an environment and eval the expression
  def test1: Unit = 
    given envMap: Env = Map("x" -> 7, "y" -> 6, "z" -> 22)

    val eval1 = eval(exp1)
    
    val eval2 = eval1 + 10

    println(s"Eval exp gives $eval2")

  def test2: Unit =
    // And again with a different environment
    given envMap: Env = Map("x" -> 17, "y" -> 10, "z" -> 2)

    val eval1 = eval(exp1)

    println(s"Eval exp gives $eval1")
