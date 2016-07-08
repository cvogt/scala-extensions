package org.cvogt.scala
object `package`{
  implicit class BooleanExtensions(val left: Boolean) extends AnyVal{
    /** Boolean algebra implication. */
    def implies(right: => Boolean) = !left || right
    
    /** Boolean algebra xor. */
    def xor(right: => Boolean) = (left || right) && !(left && right)

    /** chained syntax for if(bool) Some(value) else None */
    def map[T](value: => T) = if(left) Some(value) else None

    /** returns Some of given value if left hand side is true, else None */
    def option[T](value: T) = if(left) Some(value) else None
  }
  implicit class AnyExtensions[T](val value: T) extends AnyVal{
    /** Tests whether the given sequence contains this value as an element */
    def in(seq: Seq[T]) = seq.contains(value)
    /** Tests whether the given sequence does NOT contains this value as an element */
    def notIn(seq: Seq[T]) = !seq.contains(value)
    /** Tests whether the given set contains this value as an element */
    def in(set: Set[T]) = set.contains(value)
    /** Tests whether the given set does NOT contains this value as an element */
    def notIn(set: Set[T]) = !set.contains(value)
  }
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
  implicit class StringExtensions(val s: String) extends AnyVal{
    private def whitespace = "\t ".toSeq
    /** finds a common whitespace prefix of all lines that contain non-whitespace characters and removes it's number of characters from all lines. (Also converts line endings to system default line endings). */
    def stripIndent = {
      val lineSeparator = String.format("%n")
      val size =
        s.lines
        .filterNot(
          _.forall(whitespace.contains)
        ).mkString(lineSeparator)
        .commonLinePrefix
        .takeWhile(whitespace.contains)
        .size
      s.lines.map(_.drop(size)).mkString(lineSeparator)
    }
    /** find the largest common prefix among all lines of the given string */
    def commonLinePrefix = {
      val sorted = s.linesWithSeparators.toVector.sorted
      (sorted.head zip sorted.last).takeWhile{ case (l,r) => l == r }.map(_._1).mkString
    }
    /** trim whitespace from the right of the string */
    def trimRight = {
      val reversed = s.reverse
      val processed = if (
        reversed.startsWith("\r")
        || reversed.startsWith("\n")
      ){
        reversed.take(1) ++ reversed.drop(1).trimLeft
      } else if (
        reversed.startsWith("\n\r")
        || reversed.startsWith("\r\n")
      ){
        reversed.take(2) ++ reversed.drop(2).trimLeft
      } else {
        reversed.trimLeft
        
      }
      processed.reverse
    }
    /** trim whitespace from the left of the string */
    def trimLeft = s.dropWhile(whitespace.contains)
    /** trim whitespace from the right of each line of the string */
    def trimLinesRight = s.linesWithSeparators.map(_.trimRight).mkString
    /** trim whitespace from the left of each line of the string */
    def trimLinesLeft = s.linesWithSeparators.map(_.trimLeft).mkString
    /** prefixes every line with the given prefix */
    def prefixLines(prefix: String) = s.linesWithSeparators.map( prefix + _ ).mkString
    /** indents every line by twice the given number of spaces */
    def indent(width: Int): String = prefixLines("  " * width)
    /** indents every line by two spaces */
    def indent: String = indent(1)
    /** type-safe substring check */
    def containsTyped( s2: String ): Boolean = s contains s2
  }
}
