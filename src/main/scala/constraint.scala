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

import scala.reflect.macros.blackbox
import scala.language.experimental.macros

/**
Type class for case classes
*/
final class CaseClass[T]
object CaseClass{
  def checkCaseClassMacro[T:c.WeakTypeTag](c: blackbox.Context) = {
    import c.universe._
    val T = c.weakTypeOf[T]
    if(
      !T.typeSymbol.isClass || !T.typeSymbol.asClass.isCaseClass
    ) c.error(c.enclosingPosition,s"$T does not have case modifier")
    q"new _root_.org.cvogt.scala.constraint.CaseClass[$T]"
  }
  /**
  fails compilation if T is not a case class
  meaning this can be used as an implicit to check
  */
  implicit def checkCaseClass[T]: CaseClass[T] = macro checkCaseClassMacro[T]
}

final class SingletonObject[T]
object SingletonObject{
  def checkSingletonObjectMacro[T:c.WeakTypeTag](c: blackbox.Context) = {
    import c.universe._
    val T = c.weakTypeOf[T]
    if(
      !T.typeSymbol.isClass || !T.typeSymbol.asClass.isModuleClass
    ) c.error(c.enclosingPosition,s"$T is not an object")
    q"new _root_.org.cvogt.scala.constraint.SingletonObject[$T]"
  }
  /**
  fails compilation if T is not a singleton object class
  meaning this can be used as an implicit to check
  */
  implicit def checkSingletonObject[T]: SingletonObject[T] = macro checkSingletonObjectMacro[T]
}
