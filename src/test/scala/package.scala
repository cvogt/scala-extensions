package org.cvogt.test.scala

import org.scalatest.FunSuite
import org.scalactic.TypeCheckedTripleEquals._

import org.cvogt.scala._

object Foo{
  import reflect._
  def foo[T](implicit default: T := Int, ct: ClassTag[T]) = ct.toString.split("\\.").last
}

class PackageTest extends FunSuite{
  test("default type argument"){
    val res1 = Foo.foo
    val res2 = Foo.foo[String]
    assert("Int" === Foo.foo)
    assert("String" === Foo.foo[String])
    assert("Int" === res1)
    assert("String" === res2)
  }
}
