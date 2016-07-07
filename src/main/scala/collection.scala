package org.cvogt.scala.collection
import scala.collection._
import scala.collection.generic.CanBuildFrom
import scala.annotation.tailrec
import scala.collection.mutable.Builder

      
object `package`{
  implicit class SeqLikeExtensions[A, Repr](val coll: SeqLike[A, Repr]) extends AnyVal{
    /** type-safe contains check */
    def containsTyped(t: A) = coll.contains(t)
  }
  implicit class TraversableLikeExtensions[A, Repr](val coll: TraversableLike[A, Repr]) extends AnyVal{
    /** Eliminates duplicates based on the given key function.
    There is no guarantee which elements stay in case element two elements result in the same key.
    @param toKey maps elements to a key, which is used for comparison*/
    def distinctBy[B, That](toKey: A => B)(implicit bf: CanBuildFrom[Repr, A, That]): That = {
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

    /** Groups elements given an equivalence function.
    @param symmetric comparison function which tests whether the two arguments are considered equivalent. */
    def groupWith[That](equivalent: (A,A) => Boolean)(implicit bf: CanBuildFrom[Repr, A, That]): Seq[That] = {
      var l = List[(A, Builder[A, That])]()
      for (elem <- coll) {
        val b = l.find{
          case (sample, group) => equivalent(elem,sample)
        }.map(_._2).getOrElse{
          val bldr = bf(coll.repr)
          l = (elem, bldr) +: l
          bldr
        }
        b += elem
      }
      val b = Vector.newBuilder[That]
      for ((k, v) <- l.reverse)
        b += v.result
      b.result
    }

    /** Eliminates duplicates based on the given equivalence function.
    There is no guarantee which elements stay in case element two elements are considered equivalent.
    this has runtime O(n^2)
    @param symmetric comparison function which tests whether the two arguments are considered equivalent. */
    def distinctWith[That](equivalent: (A,A) => Boolean)(implicit bf: CanBuildFrom[Repr, A, That]): That = {
      var l = List[A]()
      val b = bf(coll.repr)
      for (elem <- coll) {
        l.find{
          case first => equivalent(elem,first)
        }.getOrElse{
          l = elem +: l
          b += elem
        }
      }
      b.result
    }
  }
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
