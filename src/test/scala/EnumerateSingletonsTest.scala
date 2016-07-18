package org.cvogt.scala.test

import org.cvogt.scala.EnumerateSingletons

import org.scalatest.FunSuite

sealed trait A
case object B extends A
sealed trait C extends A
case object D extends C

class EnumerateSingletonsTest extends FunSuite {
  test( "works for hierarchies" ) {
    val s = EnumerateSingletons[A]
    assert(
      s === Set[A]( B, D )
    )
  }
}
