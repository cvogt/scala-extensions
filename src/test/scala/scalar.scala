package org.cvogt.test.scala.scalar

import org.scalatest.FunSuite
import org.scalactic.TypeCheckedTripleEquals._

import org.cvogt.scala.scalar._

class ScalarTest extends FunSuite{
  test("in"){
    assert( 5 in List(13,2,3,4,5,5,67,675643) )
    assert( 5 in Set(12,2,3,4,5,5,67,675643) )
    assert( 5 in Seq(122,2,3,4,5,5,67,675643) )
    assert( !(9 in List(134,2,3,4,5,5,67,675643)) )
    assert( !(9 in Set(14,2,3,4,5,5,67,675643)) )
    assert( !(9 in Seq(1,2,3,4,5,5,67,675643)) )
  }
  test("notIn"){
    assert( !(5 notIn List(13,2,3,4,5,5,67,675643)) )
    assert( !(5 notIn Set(12,2,3,4,5,5,67,675643)) )
    assert( !(5 notIn Seq(122,2,3,4,5,5,67,675643)) )
    assert( 9 notIn List(134,2,3,4,5,5,67,675643) )
    assert( 9 notIn Set(14,2,3,4,5,5,67,675643) )
    assert( 9 notIn Seq(1,2,3,4,5,5,67,675643) )
  }
}
