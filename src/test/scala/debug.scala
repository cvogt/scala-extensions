package org.cvogt.test.debug

import org.scalatest.FunSuite
import org.scalactic.TypeCheckedTripleEquals._

import org.cvogt.scala.debug._

class ScalarTest extends FunSuite{
  test("debug"){
    SUPPRESS_OUTPUT{
      def foo = 5.println.println
      //assert( Seq(2,4,6) === Seq(1,2,3).println.printlnEach.foreach2(identity).map(_*2) )
    }
  }
}
