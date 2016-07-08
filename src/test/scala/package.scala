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
  test("""safe"..." """){
    val s = "string"
    val i = 5
    s"s: $s, i: ${i.toString}"
    safe"s: $s, i: ${i.toString}"
    
    s"s: $s, i: $i"
    assertTypeError{ """ safe"s: $s, i: $i" """ }
  }
  test("String containsTypes"){
    assert( "string".contains("str") )
    assert( !"string".contains("asdf") )
    assert( !"string".contains(5) )
    assert( "string".containsTyped("str") )
    assert( !"string".containsTyped("asdf") )
    assertTypeError{ """ "string".containsTyped(5) """ }
  }
  test("Option containsTyped"){
    val s = Option(1)
    assert( s containsTyped 1 )
    assert( !(s containsTyped 2) )
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
