package org.cvogt.scala.constraint
import scala.annotation.implicitNotFound

/** Verifies that From is NOT a subtype of To */
@implicitNotFound(msg =
"""
Cannot prove that
${From}
is not a subtype of
${To}"""
)
trait !<:[From, To]
object !<:{
  /** implicit that verifies !<: */
  implicit def conforms_!<:[A,B](implicit ev: Allow_!<:[A, B]): A !<: B = null
}
/** Helper class for !<: */
trait Allow_!<:[From, To]
object Allow_!<:{
  /** Allow any two types fo !<: */
  implicit def allow_!<:[From, To]: Allow_!<:[From, To] = null
  /** Creates an ambigious implicit for From <: To to prevent that case. */
  implicit def disallow_!<:[From, To](implicit ev: From <:< To): Allow_!<:[From, To] = null  
}

/** Verifies that From is not identical to To */
@implicitNotFound(msg =
"""
Cannot prove that
${From}
is not identical to
${To}"""
)
trait !=:=[From, To]
object !=:={
  /** implicit that verifies !=:= */
  implicit def conforms_!=:=[A,B](implicit ev: Allow_!=:=[A, B]): A !=:= B = null  
}
/** Helper class for !=:= */
trait Allow_!=:=[From, To]
object Allow_!=:={
  /** Allow any two types fo !=:= */
  implicit def allow_!=:=[From, To]: Allow_!=:=[From, To] = null
  /** Creates an ambigious implicit for From =:= To to prevent that case. */
  implicit def disallow_!=:=[From, To](implicit ev: From =:= To): Allow_!=:=[From, To] = null
}
