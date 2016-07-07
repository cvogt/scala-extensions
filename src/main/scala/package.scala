package org.cvogt.scala
object `package`{
  /**
  Allows to provide default value for type parameter.
  NOTE: Be careful when you use this. Result type inference order reasoning can be very tricky.

  scala> def foo[T](implicit default: T := Int, ct: ClassTag[T]) = ct.toString
  scala> foo
  Int
  scala> foo[String]
  String

  For foo: useDefault[Int] wins over useProvided[Nothing,Int]
  For foo[String]: useProvided[String,Int] applies, useDefault does not
  */
  class :=[T,Q]
  object :={
    /** Ignore Default */
    implicit def useProvided[Provided,Default] = new :=[Provided,Default]
    /** Infer type argument as Default */
    implicit def useDefault[Default] = new :=[Default,Default]
  }
}
