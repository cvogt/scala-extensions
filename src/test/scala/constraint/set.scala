package org.cvogt.test.scala.constraint

import org.scalatest.FunSuite
import org.scalactic.TypeCheckedTripleEquals._

import org.cvogt.scala.constraint.set._

class SetTest extends FunSuite{
  test("type set operations"){
    // FYI: all bets are off for Nothing

                     prove[Any     NotIn (Int,String)]
                     prove[AnyRef  NotIn (Int,String)]
                     prove[AnyVal  NotIn (Int,String)]
    assertTypeError("prove[Int     NotIn (Int,String)]")
    assertTypeError("prove[String  NotIn (Int,String)]")
    assertTypeError("prove[Nothing NotIn (Int,String)]")

    assertTypeError("prove[Any     In (Int,String)]")
    assertTypeError("prove[AnyRef  In (Int,String)]")
    assertTypeError("prove[AnyVal  In (Int,String)]")
                     prove[String  In (Int,String)]
                     prove[Int     In (Int,String)]
    assertTypeError("prove[Nothing In (Int,String)]")

                     prove[Any     NotIn (Int,String,Nothing)]
                     prove[AnyRef  NotIn (Int,String,Nothing)]
                     prove[AnyVal  NotIn (Int,String,Nothing)]
                     prove[String  NotIn (Int,String,Nothing)]
                     prove[Int     NotIn (Int,String,Nothing)]
    assertTypeError("prove[Nothing NotIn (Int,String,Nothing)]")

    assertTypeError("prove[Any     In (Int,String,Nothing)]")
    assertTypeError("prove[AnyRef  In (Int,String,Nothing)]")
    assertTypeError("prove[AnyVal  In (Int,String,Nothing)]")
    assertTypeError("prove[String  In (Int,String,Nothing)]")
    assertTypeError("prove[Int     In (Int,String,Nothing)]")
    assertTypeError("prove[Nothing In (Int,String,Nothing)]")
  }
}
