package mapk

class MapK[K[_], V[_]](rawMap: Map[K[Any], V[Any]]):
  def apply[A](key: K[A]): V[A] =
    rawMap(key.asInstanceOf[K[Any]]).asInstanceOf[V[A]]

  def get[A](key: K[A]): Option[V[A]] =
    rawMap.get(key.asInstanceOf[K[Any]]).asInstanceOf[Option[V[A]]]

  def foreach(f: KTuple2[K, V]#T => Unit): Unit =
    unsafeForeach( (k, v) => f((k, v).asInstanceOf[KTuple2[K, V]#T]))

  /** note that it use the polymorphic Function Types */
  def foreachK(f: [A] => (K[A], V[A]) => Unit): Unit =
    unsafeForeach((k, v) => f(k, v))

  /** #Unsafe: `f` must NOT rely on `A` being any particular type */
  private def unsafeForeach[A](f: ((K[A], V[A])) => Unit): Unit =
    rawMap.foreach(pair => f(pair.asInstanceOf[(K[A], V[A])]))

object MapK:
  def apply[K[_], V[_]](pairs: KTuple2[K, V]#T*): MapK[K, V] =
    val x: List[KTuple2[K, V]#T] = pairs.toList
    val y: List[(K[Any], V[Any])] = x.map(t => t.asInstanceOf[(K[Any], V[Any])])
    new MapK(Map(y*))


