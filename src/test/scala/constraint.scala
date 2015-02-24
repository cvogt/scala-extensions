package org.cvogt.test.scala.constraint

import org.scalatest.FunSuite
import org.scalactic.TypeCheckedTripleEquals._

import org.cvogt.scala.constraint._

trait A
trait B extends A
trait C

class ConstraintTest extends FunSuite{
  test("<:<"){
    implicitly[A <:< A]
    implicitly[B <:< A]
    implicitly[B <:< B]
    implicitly[A >:> A]
    implicitly[B !>:> A]
    implicitly[B >:> B]
    implicitly[C !<:< A]
    implicitly[A !>:> C]
    assertTypeError("implicitly[B !<:< A]")
    assertTypeError("implicitly[A !<:< A]")
  }
  test("=:="){
    implicitly[String  =:= String]
    implicitly[Int     =:= Int]
    implicitly[Unit    =:= Unit]
    implicitly[C       =:= C]
    implicitly[Any     =:= Any]
    implicitly[AnyRef  =:= AnyRef]
    implicitly[AnyVal  =:= AnyVal]
    implicitly[Nothing =:= Nothing]

    assertTypeError("implicitly[String  =:= Nothing]")
    assertTypeError("implicitly[Int     =:= Nothing]")
    assertTypeError("implicitly[Unit    =:= Nothing]")
    assertTypeError("implicitly[C       =:= Nothing]")
    assertTypeError("implicitly[Any     =:= Nothing]")
    assertTypeError("implicitly[Nothing =:= String]")
    assertTypeError("implicitly[Nothing =:= Int]")
    assertTypeError("implicitly[Nothing =:= Unit]")
    assertTypeError("implicitly[Nothing =:= C]")
    assertTypeError("implicitly[Nothing =:= Any]")
  }
  test("!=:="){
    implicitly[String  !=:= Any]
    implicitly[String  !=:= AnyVal]
    implicitly[String  !=:= AnyRef]
    implicitly[String  !=:= Unit]
    implicitly[Int     !=:= Any]
    implicitly[Int     !=:= AnyRef]
    implicitly[Int     !=:= AnyVal]
    implicitly[Int     !=:= Unit]
    implicitly[Unit    !=:= Any]
    implicitly[Unit    !=:= AnyRef]
    implicitly[Unit    !=:= AnyVal]

    assertTypeError("implicitly[String !=:= String]")
    assertTypeError("implicitly[Int    !=:= Int]")
    assertTypeError("implicitly[Unit   !=:= Unit]")
    assertTypeError("implicitly[Nothing !=:= Nothing]")

    implicitly[Unit    !=:= Nothing]
    implicitly[String  !=:= Nothing]
    implicitly[Int     !=:= Nothing]
    implicitly[Nothing !=:= Any]
    implicitly[Nothing !=:= AnyRef]
    implicitly[Nothing !=:= AnyVal]
    implicitly[Nothing !=:= Unit]
    implicitly[Nothing !=:= String]
    implicitly[Nothing !=:= Int]
  }
}
