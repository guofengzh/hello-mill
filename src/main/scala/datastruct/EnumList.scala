package datastruct

object EnumList {
  
}

enum SList[+A]:
  case Nil
  case Cons(head: A, tail: SList[A])

  def sum[B >: A](using num: math.Numeric[B]): B = 
    this match 
      case Nil => num.zero
      case Cons(x,xs) => num.plus(x, xs.sum)

  def sumInt[B >: A](using num: B =:= Int): Int = 
    this match 
      case Nil => 0
      case Cons(x,xs) => x + xs.sumInt

  def product[B >: A](using num: math.Numeric[B]): B =
    this match
      case Nil => num.zero
      case Cons(x,xs) => num.times(x, xs.product)

  def append[B>:A](a2: SList[B]): SList[B] =
    this match
      case Nil => a2
      case Cons(x,xs) => Cons(x, xs.append(a2))

  def foldRight[B](z: B, f: (A, B) => B): B =
    this match
      case Nil => z
      case Cons(x, xs) => f(x, xs.foldRight(z, f))

  def map[B](f: A => B): SList[B] = 
    this match
      case Nil => Nil
      case Cons(x, xs) => Cons(f(x), xs.map(f))

  def flatMap[B](f: A => SList[B]): SList[B] =
    flatten(map(f))

  def flatMap2[B](f: A => SList[B]): SList[B] = 
    this match
      case Nil => Nil
      case Cons(x, xs) => f(x).append(xs.flatMap(f))

  def flatten[A](fhs: SList[SList[A]]): SList[A] =
    fhs match
      case Nil => Nil
      case Cons(x, xs) => x.append(flatten(xs))

  def flattenViaFoldRight[A](l: SList[SList[A]]): SList[A] =
    l.foldRight(Nil: SList[A], (x, acc) => acc.append(x))

  def filter(f: A => Boolean): SList[A] =
    foldRight(Nil: SList[A], (h,t) => if f(h) then Cons(h,t) else t)
  
  def filterViaFoldRight(f: A => Boolean): SList[A] =
    foldRight(Nil: SList[A], (h,t) => if f(h) then Cons(h,t) else t)

  def mapViaFoldRight[B](f: A => B): SList[B] =
    foldRight(Nil: SList[B], (h,t) => Cons(f(h),t))

  def sumViaFoldRight[B >: A](using num: math.Numeric[B]) : B =
    foldRight(num.zero, (x,y) => num.plus(x, y))

  def productViaFoldRight[B >: A](using num: math.Numeric[B]) : B =
    foldRight(num.one, (x,y) => num.times(x, y))

  def drop(n: Int): SList[A] =
    if n <= 0 then this
    else this match
      case Nil => Nil
      case Cons(_,xs) => xs.drop(n-1)

  def dropWhile(f: A => Boolean): SList[A] =
    this match
      case Cons(x,xs) if f(x) => xs.dropWhile(f)
      case _ => this

  def zipWith[B,C](b: SList[B], f: (A, B) => C): SList[C] = 
    (this,b) match
      case (Nil, _) => Nil
      case (_, Nil) => Nil
      case (Cons(x1, xs1), Cons(x2, xs2)) => Cons(f(x1, x2), xs1.zipWith(xs2, f))

  def zip[B](b: SList[B]): SList[(A, B)] = 
    zipWith(b, (a, b) => (a, b))

  @annotation.tailrec
  final def foldLeft[B](acc: B, f: (B, A) => B): B = this match
    case Nil => acc
    case Cons(x, xs) => xs.foldLeft(f(acc, x), f)

  def sumViaFoldLeft[B >: A](using num: math.Numeric[B]) : B =
       foldLeft(num.zero, (y,x) => num.plus(x, y))
  def productViaFoldLeft[B >: A](using num: math.Numeric[B]) : B =
       foldLeft(num.zero, (y,x) => num.times(x, y))

  def lengthViaFoldLeft: Int = foldLeft(0, (acc, h) => acc + 1)

  def reverse: SList[A] = foldLeft(SList[A](), (acc, h) => Cons(h,acc))      

  @annotation.tailrec
  final def startsWith[B](prefix: SList[B]): Boolean = 
    (this,prefix) match
      case (_,Nil) => true
      case (Cons(x1,xs1),Cons(x2,xs2)) if x1 == x2 => xs1.startsWith(xs2)
      case _ => false  

  @annotation.tailrec
  final def hasSubsequence[A](sub: SList[A]): Boolean = 
    this match
      case Nil => sub == Nil
      case _ if startsWith(sub) => true
      case Cons(h,t) => t.hasSubsequence(sub)

object SList:
  def apply[A](as: A*): SList[A] =
    if as.isEmpty then Nil
    else Cons(as.head, apply(as.tail*))
  
@main def listTest: Unit = 
  val ls = SList(1.2, 2.3, 4.4)
  println(ls.sum)

  val ls2 = SList(1, 2, 4)
  println(ls2.sumInt)
