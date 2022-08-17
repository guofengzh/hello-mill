package encoding

object Church:
    // logic
    val `true` = (x:Int) => (y: Int) => x
    val `false` = (x:Int) => (y: Int) => y
    val `if c then a else b` = (c:Int => Int => Int)=>(a:Int)=>(b:Int)=>c(a)(b)

    // pair
    val `(a,b)` = (a:Int)=>(b:Int)=>(x: Int => Int => Int)=>x(a)(b)
    val `fst(a,b)` = (sel:(Int => Int => Int) => Int)=>sel(`true`)
    val `snd(a,b)` = (sel:(Int => Int => Int) => Int)=>sel(`false`)

    // church number 
    // https://stackoverflow.com/questions/41978590/why-the-definition-of-churchs-numerals
    val `0` = (s:Int=>Int)=>(z: Int)=>z
    val succ = (n: (Int=>Int)=>Int=>Int)=>(s:Int=>Int)=>(z:Int)=>s(n(s)(z))
    def square(x:Int) = x*x

    val add = (n: (Int=>Int)=>Int=>Int) => (m: (Int=>Int)=>Int=>Int) => (s:Int=>Int) =>(z:Int) =>  n(s)(m(s)(z))
    val mult = (n: (Int=>Int)=>Int=>Int) => (m: (Int=>Int)=>Int=>Int) => (s:Int=>Int) =>(z:Int) =>  n(m(s))(z)
    val power = (n: (Int=>Int)=>Int=>Int) => (m: (Int=>Int)=>Int=>Int) => (s:Int=>Int) =>(z:Int) => n(m(s))(z)

import Church.*
@main def churchEncondingTest: Unit =
    val whenTrue = `if c then a else b`(`true`)(1)(2)
    val whenFalse = `if c then a else b`(`false`)(1)(2)

    val pair = `(a,b)`(1)(2)
    val proj1 = `fst(a,b)`(pair)
    val proj2 = `snd(a,b)`(pair)

    val `1` = succ(`0`)
    val `2` = succ(`1`)

    val validation = `2`(square)(2)

    val `3` = add(`1`)(`2`)

    val `6` = mult(`2`)(`3`)


    val p = (1 to 6 ).foldLeft(2.0)((aggregation, a)=> {val a = aggregation *aggregation; println(a);a} )
    println(p)

type ConstituentPartOf = [T] =>> T match
  case BigInt => Int
  case String => Char
  case List[t] => t
