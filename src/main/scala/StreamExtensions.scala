package org.cvogt.scala.collection

class StreamExtensions[A](val coll: Stream[A]) extends AnyVal{
  def distinctBy[B](toKey: A => B): Stream[A]= {
    def loop(seen: Set[B], rest: Stream[A]): Stream[A] = {
      if (rest.isEmpty) {
        Stream.Empty
      } else {
        val elem = toKey(rest.head)
        if (seen(elem)) {
          loop(seen, rest.tail)
        } else {
          Stream.cons(rest.head, loop(seen + elem, rest.tail))
        }
      }
    }

    loop(Set(), coll)
  }
}
