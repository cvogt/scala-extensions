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
  test("""safe"..." """){
    val s = "string"
    val i = 5
    s"s: $s, i: ${i.toString}"
    safe"s: $s, i: ${i.toString}"
    
    s"s: $s, i: $i"
    assertTypeError{ """ safe"s: $s, i: $i" """ }
  }
  test("Option containsTyped"){
    val s = Option(1)
    assert( s contains 1 )
    assert( s containsTyped 1 )
    assert( !(s contains 2) )
    assert( !(s containsTyped 2) )
    assert( !(s contains "asdf") )
    assertTypeError( """ !(s containsTyped "asdf") """ )
  }
  test("Option getOrThrow"){
    assert(1 == Option(1).getOrThrow("asdf"))
    intercept[RuntimeException](None.getOrThrow("asdf"))
  }
  test("""->"""){
    val t: Int -> String = 1 -> "test"
    val t2: (Int, String) = t
    t match {
      case i -> s =>
        assert(i == 1)
        assert(s == "test")
    }
  }
}
