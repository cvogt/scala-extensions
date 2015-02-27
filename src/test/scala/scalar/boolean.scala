package org.cvogt.test.scalar

import org.scalatest.FunSuite
import org.scalactic.TypeCheckedTripleEquals._

import org.cvogt.scala.scalar._

class BooleanTest extends FunSuite{
  test("map"){
    assert( Some(5) === true.map(5) )
    assert( None    === false.map(5) )
  }
  test("xor"){
    assert( true xor false )
    assert( false xor true )
    assert( !(true xor true) )
    assert( !(false xor false) )
  }
  test("implies"){
    assert( true implies true )
    assert( !(true implies false) )
    assert( false implies true )
    assert( false implies false )
  }
}
