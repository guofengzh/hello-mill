package typematch

/**
 * TODO https://stackoverflow.com/questions/57835345/how-do-i-write-a-match-type-pattern-with-a-narrowed-string-type-head-of-tuple-ca
 */
trait Remapping
case object ReEmpty extends Remapping
case class ReCons[N1 <: String, N2 <: String, R <: Remapping](n1: N1, n2: N2, rest: R) extends Remapping

type Remapped[X <: String, R <: Remapping] <: String = R match
  case ReEmpty.type     => X
  case ReCons[X, n, ?]  => n
  case ReCons[?, ?, rr] => Remapped[X, rr]

type AllRemapped[T <: Tuple, R <: Remapping] <: Tuple = T match
  case Unit      => EmptyTuple
  case s *: rest => s match
    case String => Remapped[s, R] *: AllRemapped[rest, R]

type Hlp[X <: String, Rest <: Tuple] = X *: Rest

type AllRemapped2[T <: Tuple, R <: Remapping] <: Tuple = T match
  case Unit         => EmptyTuple
  case Hlp[s, rest] => Remapped[s, R] *: AllRemapped[rest, R]

//type AllRemapped3[T <: Tuple, R <: Remapping] = Tuple.Map[T, [X <: String] =>> Remapped[X, R]]
