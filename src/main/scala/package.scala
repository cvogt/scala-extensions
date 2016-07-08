package org.cvogt.scala
object `package`{
  implicit class OptionExtensions[T](val option: Option[T]) extends AnyVal{
    /** type-safe contains check */
    def containsTyped(t: T) = option contains t
    /** returns the value inside of the option or throws an exception with the given error message if None */
    def getOrThrow(msg: String): T = option.getOrElse( throw new RuntimeException(msg) )
  }
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

  /** `->` type alias for tuples and extractor for pattern matches. Complementary to the `Predef.->` Tuple2 constructor.*/
  type -> [L, R] = ( L, R )
  object -> {
    def apply[L, R]( l: L, r: R ) = ( l, r )
    def unapply[L, R]( t: ( L, R ) ) = Option( t )
  }

  /** type-safe alternative to s"..." that requires explicit toString conversions rather than implicit */
  implicit class SafeStringContext( stringContext: StringContext ) {
    def safe( args: String* ): String = {
      val process = StringContext.treatEscapes _
      val pi = stringContext.parts.iterator
      val ai = args.iterator
      val bldr = new java.lang.StringBuilder( process( pi.next() ) )
      while ( ai.hasNext ) {
        bldr append ai.next
        bldr append process( pi.next() )
      }
      bldr.toString
    }
  }
}
