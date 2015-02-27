package org.cvogt.scala.collection
import scala.collection._
import scala.collection.generic.CanBuildFrom
import scala.annotation.tailrec
      
object `package`{
  implicit class IterableLikeExtensions[A, Repr](val coll: IterableLike[A, Repr]) extends AnyVal{
    /** Eliminates duplicates based in the given key function.
    There is no guarantee which element stays in case elements are removed.
    @param toKey maps elements to a key, which is used for comparison*/
    def distinctBy[B, That](toKey: A => B)(implicit bf: CanBuildFrom[Repr, A, That]) = {
      val builder = bf(coll.repr)
      val keys = mutable.Set[B]()
      for(element <- coll){
        val key = toKey(element)
        if (!keys(key)) {
          builder += element
          keys += key
        }
      }
      builder.result()
    }
  }
  implicit class GenTraversableOnceExtensions[A](val coll: GenTraversableOnce[A]) extends AnyVal{
    /**
    Fold while accumulation function returns Some. Stops on first None.

    @param initial initual element to start the accumulation on
    @param accumulate accumulation function
    */
    def foldWhile[A1 >: A](initial: A1)
      (accumulate: (A1, A1) => Option[A1]): A1
      = foldLeftWhile[A1](initial)(accumulate)

    /**
    Fold while accumulation function returns Some. Stops on first None.

    @param initial initual element to start the accumulation on
    @param accumulate accumulation function
    */
    def foldLeftWhile[B](initial: B)
      (accumulate: (B, A) => Option[B]): B = {
      @tailrec
      def loop(it: Iterator[A], prev: B): B = {
        if(it.hasNext){
          val next = it.next
          val current = accumulate(prev,next)
          if (current.isEmpty) prev
          else loop(it,current.get)
        } else prev
      }
      loop(coll.toIterator, initial)
    }

    /**
    Reduce while accumulation function returns Some. Stops on first None.

    @param accumulate accumulation function
    */
    def reduceWhile[A1 >: A]
      (accumulate: (A1, A1) => Option[A1]): A1
      = reduceLeftWhile(accumulate)

    /**
    Reduce while accumulation function returns Some. Stops on first None.

    @param accumulate accumulation function
    */
    def reduceLeftWhile[A1 >: A]
      (accumulate: (A1, A1) => Option[A1]): A1 = {
      val it = coll.toIterator
      if(it.hasNext)
        it.foldWhile[A1](it.next)(accumulate)
      else
        throw new UnsupportedOperationException("empty.reduceLeftWhile")
    }
  }
}
