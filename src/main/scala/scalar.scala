package org.cvogt.scala.scalar
import scala.collection._
import scala.collection.generic.CanBuildFrom
import scala.annotation.tailrec
/** Extensions for scalar Scala types like String, Boolean, Int, etc. */
object `package`{
  implicit class BooleanExtensions(left: Boolean){
    /** Boolean algebra implication. */
    def implies(right: => Boolean) = !left || right
    
    /** Boolean algebra xor. */
    def xor(right: => Boolean) = (left || right) && !(left && right)

    /** chained syntax for if(bool) Some(value) else None */
    def map[T](value: => T) = if(left) Some(value) else None
  }
}
