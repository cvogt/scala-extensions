package org.cvogt.scala.constraint
import scala.annotation.implicitNotFound

import boolean._

/**
type-level constraints for subtyping and type identity checks.
*/
object `package`{
  @implicitNotFound(msg = "Cannot prove ${Left} =:= ${Right}")
  type =:=[Left,Right] = scala.Predef.=:=[Left,Right]
  @implicitNotFound(msg = "Cannot prove ${Left} <:< ${Right}")
  type <:<[Left,Right] = scala.Predef.<:<[Left,Right]

  @implicitNotFound(msg = """Cannot prove ${Left} !<:< ${Right}""")
  type !<:<[Left,Right] = ![<:<[Left,Right]]
  @implicitNotFound(msg = """Cannot prove ${Left} !=:= ${Right}""")
  type !=:=[Left,Right] = ![=:=[Left,Right]]


  @implicitNotFound(msg = """Cannot prove ${Left} >:> ${Right}""")
  type  >:>[Right,Left] =  <:<[Left,Right]
  @implicitNotFound(msg = """Cannot prove ${Left} !>:> ${Right}""")
  type !>:>[Right,Left] = !<:<[Left,Right]
}
/*
/** Proves that Left is NOT a subtype of ${Right} */
@implicitNotFound(msg = "Cannot prove ${Left} !<:< ${Right}")
final class !<:<[Left, ${Right}]
object !<:<{
  implicit def prove[Left,Right](implicit ev: ![Left <:< ${Right}]) = new !<:<[Left,Right]
}

/** Proves that Left is NOT identical to ${Right} */
@implicitNotFound(msg = "Cannot prove ${Left} !=:= ${Right}")
final class !=:=[Left, ${Right}]
object !=:={
  implicit def prove[Left,Right](implicit ev: ![Left =:= ${Right}]) = new !=:=[Left,Right]
}
*/
