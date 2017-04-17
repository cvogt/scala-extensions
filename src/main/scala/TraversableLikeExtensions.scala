package org.cvogt.scala.collection

import scala.collection._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.Builder

class TraversableLikeExtensions[A, Repr](val coll: TraversableLike[A, Repr]) extends AnyVal{
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
