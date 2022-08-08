package lists

object EnumList {
  
}

enum List[+A]:
  case Nil
  case ::(head: A, tail: List[A])
  def append(a1: List[A], a2: List[A]): List[A] =
    a1 match
      case Nil => a2
      case ::(h,t) => ::(h, append(t, a2))
 
object List:
  def apply[A](as: A*): List[A] =
    if as.isEmpty then Nil
    else ::(as.head, apply(as.tail*))

  def append[A](a1: List[A], a2: List[A]): List[A] =
    a1 match
      case Nil => a2
      case ::(h,t) => ::(h, append(t, a2))

  def length[A](l: List[A]): Int =
    foldRight(l, 0, (_,acc) => acc + 1)

  def filter[A](l: List[A])(f: A => Boolean): List[A] =
    foldRight(l, Nil: List[A], (h,t) => if f(h) then ::(h,t) else t)

  def flatMap2Rev[A, B](list: List[A])(f: A => List[B]): List[B] = list match
    case (x::xs) => append(f(x), flatMap(xs)(f))
    case _ => Nil

  def flatMap[A, B](list: List[A])(f: A => List[B]): List[B] =
    @annotation.tailrec
    def _flatMap(acc: List[B])(input: List[A])(f: A => List[B]): List[B] = input match
      case ::(x, xs) => _flatMap(append(f(x), acc))(xs)(f)
      case Nil => acc
    _flatMap(List[B]())(list)(f)

  def flatMap2[A, B](l: List[A])(f: A => List[B]): List[B] =
    concat(map(l)(f))

  def concat[A](l: List[List[A]]): List[A] =
    foldRight(l, Nil: List[A], append)

  @annotation.tailrec
  def foldLeft[A, B](l: List[A], acc: B, f: (B, A) => B): B = l match
    case Nil => acc
    case ::(h, t) => foldLeft(t, f(acc, h), f)
    
  def map[A, B](list: List[A])(f: A => B): List[B] =
    @annotation.tailrec
    def _map(acc: List[B])(input: List[A])(f: A => B): List[B] = input match {
      case (x::xs) => _map(::(f(x), acc))(xs)(f)
      case _ => acc
    }
    _map(List[B]())(list)(f)

  def foldRight[A,B](as: List[A], acc: B, f: (A, B) => B): B =
    as match
      case ::(x, xs) => f(x, foldRight(xs, acc, f))
      case Nil => acc

  def map2[A, B](l: List[A])(f: A => B): List[B] =
    foldRight(l, Nil: List[B], (h,t) => ::(f(h),t))


  def drop[A](l: List[A], n: Int): List[A] =
    if n <= 0 then l
    else l match
      case Nil => Nil
      case ::(_,t) => drop(t, n-1)

  def dropWhile[A](l: List[A], f: A => Boolean): List[A] =
    l match
      case ::(h,t) if f(h) => dropWhile(t, f)
      case _ => l

  def zipWith[A, B,C](a: List[A], b: List[B], f: (A, B) => C): List[C] = (a,b) match
    case (Nil, _) => Nil
    case (_, Nil) => Nil
    case (::(h1, t1), ::(h2, t2)) => ::(f(h1, h2), zipWith(t1, t2, f))

  @annotation.tailrec
  def startsWith[A](l: List[A], prefix: List[A]): Boolean = (l,prefix) match
    case (_,Nil) => true
    case (::(h,t),::(h2,t2)) if h == h2 => startsWith(t, t2)
    case _ => false

  @annotation.tailrec
  def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = sup match
    case Nil => sub == Nil
    case _ if startsWith(sup, sub) => true
    case ::(h,t) => hasSubsequence(t, sub)
    