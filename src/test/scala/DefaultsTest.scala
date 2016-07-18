package org.cvogt.scala.test

import org.cvogt.scala.Defaults

import org.scalatest.FunSuite

case class Foo(i: Int = 5, s: String)
class Bar(i: Int = 5, s: String)
class DefaultsTest extends FunSuite {
  test( "defaults" ) {
    val f = Defaults[Foo]
    val b = Defaults[Bar]

    import scala.language.reflectiveCalls
    assert( f.i === 5 )
    import scala.language.reflectiveCalls
    assert( b.i === 5 )
  }
}
