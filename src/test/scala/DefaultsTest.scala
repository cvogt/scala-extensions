package org.cvogt.scala.test

import org.cvogt.scala.Defaults

import org.scalatest.FunSuite

case class Foo(i: Int = 5, s: String)
class DefaultsTest extends FunSuite {
  test( "case class" ) {
    val d = Defaults[Foo]

    import scala.language.reflectiveCalls
    assert( d.i === 5 )
  }
}
