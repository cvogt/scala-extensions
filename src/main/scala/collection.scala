package org.cvogt.scala.collection
import scala.collection._
import scala.collection.generic.CanBuildFrom
import scala.annotation.tailrec
import scala.collection.mutable.Builder

sealed abstract class LowPriorityCollectionImplicits {

  implicit def ToTraversableLikeExtensions[A, Repr](coll: TraversableLike[A, Repr]): TraversableLikeExtensions[A, Repr] =
    new TraversableLikeExtensions[A, Repr](coll)

}

object `package` extends LowPriorityCollectionImplicits {
  implicit class SeqLikeExtensions[A, Repr](val coll: SeqLike[A, Repr]) extends AnyVal{
    /** type-safe contains check */
    def containsTyped(t: A) = coll.contains(t)
  }

  implicit def ToStreamExtensions[A](coll: Stream[A]): StreamExtensions[A] =
    new StreamExtensions[A](coll)

  implicit class TraversableExtensions[A](val coll: Traversable[A]) extends AnyVal{
    /** tests weather the given collection has duplicates using default equality == */
    def containsDuplicates = coll.groupBy(identity).exists(_._2.size > 1)

    /** tests weather at least two elements map to the same key using the given function
    @param toKey maps elements to a key, which is used for comparison*/
    def containsDuplicatesBy[B](key: A => B) = coll.groupBy(key).exists(_._2.size > 1)

    /** tests weather at least two elements are considered equivalent using the given function
    this has runtime O(n^2)
    @param symmetric comparison function which tests whether the two arguments are considered equivalent.
    */
    def containsDuplicatesWith(equivalent: (A,A) => Boolean) = coll.groupWith(equivalent).exists(_.size > 1)
  }

  /**
   Extension methods on String collections
   *  @define coll collection or iterator
   */
  implicit class StringGenTraversableOnceExtensions(val coll: GenTraversableOnce[String]) extends AnyVal{
    /** Displays all elements of this $coll in a string using start, end, and
     *  separator strings.
     *  Type-safe alternative to mkString.
     *
     *  @param start the starting string.
     *  @param sep   the separator string.
     *  @param end   the ending string.
     *  @return      a string representation of this $coll. The resulting string
     *               begins with the string `start` and ends with the string
     *               `end`. Inside, the string representations (w.r.t. the method
     *               `toString`) of all elements of this $coll are separated by
     *               the string `sep`.
     *
     *  @example  `List(1, 2, 3).concat("(", "; ", ")") = "(1; 2; 3)"`
     */
    @inline
    def concat(start: String, sep: String, end: String): String = coll.mkString(start: String, sep: String, end: String)

    /** Displays all elements of this $coll in a string using a separator string.
     *  Type-safe alternative to mkString.
     *
     *  @param sep   the separator string.
     *  @return      a string representation of this $coll. In the resulting string
     *               the string representations (w.r.t. the method `toString`)
     *               of all elements of this $coll are separated by the string `sep`.
     *
     *  @example  `List(1, 2, 3).concat("|") = "1|2|3"`
     */
    @inline
    def concat(sep: String): String = coll.mkString(sep: String)

    /** Displays all elements of this $coll in a string.
     *  Type-safe alternative to mkString.
     *
     *  @return a string representation of this $coll. In the resulting string
     *          the string representations (w.r.t. the method `toString`)
     *          of all elements of this $coll follow each other without any
     *          separator string.
     */
    @inline
    def concat: String = coll.mkString
  }  

  implicit class BooleanGenTraversableOnceExtensions(val coll: GenTraversableOnce[Boolean]) extends AnyVal{
    /** returns true if every element is true */
    def allTrue = coll.forall(identity)
    /** returns true if if element returns true */
    def anyTrue = coll.exists(identity)
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
