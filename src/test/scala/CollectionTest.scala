package org.cvogt.test.scala

import org.scalatest.FunSuite
import org.scalactic.TypeCheckedTripleEquals._

import org.cvogt.scala.collection._

case class Person(name: String, age: Int)
class CollectionTest extends FunSuite{
  test("distinctBy"){
    val ps = List(
      Person("Chris",99),
      Person("Marcos",99)
    )
    assert(2 === ps.distinct.size)
    assert(1 === ps.distinctBy(_.age).size)
    assert(2 === ps.distinctBy(identity).size)
    assert(2 === ps.distinctBy(_.name).size)
    val ps2 = ps.distinctBy(_.age)
    val ps3: List[Person] = ps2

    val ps4 = ps.toSet.distinctBy(_.age)
    val ps5: Set[Person] = ps4
  }

  def liftOptionOrElse[A,B,C]
    (f: (A,B) => C)
    : (((Option[A],Option[B])) => Option[C]) => (Option[A],Option[B])  => Option[C]
    = els => (a,b) => liftOption[A,B,C](f)(a,b) orElse els((a,b))

    
  def liftOption[A,B,C]
    (f: (A,B) => C)
    : (Option[A],Option[B]) => Option[C]
    = (a, b) =>
        (for{
          _a <- a
          _b <- b
        } yield f(_a,_b))

  def accumulateInt = liftOptionOrElse[Int,Int,Int](_ - _)
  def accumulateIntLeft = accumulateInt(_._1)
  def accumulateIntRight = accumulateInt(_._2)
  def accumulateStringLeft = liftOptionOrElse[String,Int,String](_ + _)(_._1)
  def accumulateStringRight = liftOptionOrElse[Int,String,String](_ + _)(_._2)

  test("foldWhile and reduceWhile"){
    val l = List(
      Some(5),
      Some(13),
      None,
      Some(29)
    )

    // built-in reduce and fold for comparison
    assert(
      -37 === l.reduce(accumulateIntLeft).get
    )
    assert(
      -47 === l.fold(Option(0))(accumulateIntLeft).get
    )
    assert(
      -37 === l.reduceLeft(accumulateIntLeft).get
    )
    assert(
      "051329" === l.foldLeft(Option("0"))(accumulateStringLeft).get   
    )
    assert(
      21 === l.reduceRight(accumulateIntRight).get
    )
    assert(
      "513290" === l.foldRight(Option("0"))(accumulateStringRight).get   
    )
    assert(
      "0513" === l.foldLeftWhile("0")((current,next) => next.map(current + _))
    )

    // foldWhile and reduceWhile
    assert(
      -18 === l.foldWhile(Option(0)){(current,next) => 
        (for{
          c <- current
          n <- next
        } yield Some(c - n))
      }.get
    )
    assert(
      -8 === l.reduceLeftWhile{(current,next) => 
        (for{
          c <- current
          n <- next
        } yield Some(c - n))
      }.get
    )
    assert(
      -8 === l.reduceWhile{(current,next) => 
        (for{
          c <- current
          n <- next
        } yield Some(c - n))
      }.get
    )
  }

  test("foldWhile and reduceWhile on empty List"){
    // built-in reduce and fold for comparison
    intercept[UnsupportedOperationException](
      List[Option[Int]]().reduce(accumulateIntLeft)
    )
    assert(
      0 === List[Option[Int]]().fold(Option(0))(accumulateIntLeft).get
    )
    intercept[UnsupportedOperationException](
      List[Option[Int]]().reduceLeft(accumulateIntLeft)
    )
    assert(
      "0" === List[Option[Int]]().foldLeft(Option("0"))(accumulateStringLeft).get   
    )
    intercept[UnsupportedOperationException](
      List[Option[Int]]().reduceRight(accumulateIntRight)
    )
    assert(
      "0" === List[Option[Int]]().foldRight(Option("0"))(accumulateStringRight).get   
    )    

    // foldWhile and reduceWhile
    assert(
      "0" === List[Option[Int]]().foldLeftWhile("0")((current,next) => next.map(current + _))
    )
    assert(
      0 === List[Option[Int]]().foldWhile(Option(0)){(current,next) => 
        (for{
          c <- current
          n <- next
        } yield Some(c - n))
      }.get
    )
    intercept[UnsupportedOperationException](
      List[Option[Int]]().reduceLeftWhile{(current,next) => 
        (for{
          c <- current
          n <- next
        } yield Some(c - n))
      }
    )
    intercept[UnsupportedOperationException](
      List[Option[Int]]().reduceWhile{(current,next) => 
        (for{
          c <- current
          n <- next
        } yield Some(c - n))
      }
    )

    assert(
      45 ==
      Stream.unfold(1)(_+1).reduceWhile((a,b) => Option(a+b).filter(_ => b < 10))
    )
  }
  test("Stream.unfold"){
    assert(
      Seq(1,2,3,4,5,6,7,8,9,10) ===
      Stream.unfold(1)(_+1).take(10).toSeq
    )
  }
}
