package org.cvogt.scala.string
      
object `package`{
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
  }
}
