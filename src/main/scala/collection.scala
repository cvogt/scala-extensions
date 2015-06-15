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
  
  /**
   Extension methods on collections
   */
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
