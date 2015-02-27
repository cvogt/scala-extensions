package org.cvogt.scala.scalar
import scala.collection._
import scala.collection.generic.CanBuildFrom
import scala.annotation.tailrec
/** Extensions for scalar Scala types like String, Boolean, Int, etc. */
object `package`{
  implicit class BooleanExtensions(val left: Boolean) extends AnyVal{
    /** Boolean algebra implication. */
    def implies(right: => Boolean) = !left || right
    
    /** Boolean algebra xor. */
    def xor(right: => Boolean) = (left || right) && !(left && right)

    /** chained syntax for if(bool) Some(value) else None */
    def map[T](value: => T) = if(left) Some(value) else None
  }
  implicit class AnyExtensions[T](val value: T) extends AnyVal{
    /** Tests whether the given sequence contains this value as an element */
    def in(seq: Seq[T]) = seq.contains(value)
    /** Tests whether the given sequence does NOT contains this value as an element */
    def notIn(seq: Seq[T]) = !seq.contains(value)
    /** Tests whether the given set contains this value as an element */
    def in(set: Set[T]) = set.contains(value)
    /** Tests whether the given set does NOT contains this value as an element */
    def notIn(set: Set[T]) = !set.contains(value)
  }
}
