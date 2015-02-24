package org.cvogt.scala.debug
import java.io.{OutputStream, PrintStream}

/** Suppresses JVM output to stdout and stderr in a block of code
  *
  * !!!USE WITH CAUTION!!!
  *
  * Because this suppresses everything inside this call, even in other files, libs, etc.
  * which can be quite surprising. You have been warned.
  */
object SUPPRESS_OUTPUT{
  def apply[T](compute: => T): T = {
    val oldOut = System.out
    val oldErr = System.err
    val nullStream = new PrintStream(new VoidOutputStream())
    System.setOut(nullStream)
    System.setErr(nullStream)
    val res = compute
    System.setOut(oldOut)
    System.setErr(oldErr)
    res
  }
  private class VoidOutputStream extends OutputStream {
    override def write(b: Int) = ()
    override def write(b: Array[Byte]) = ()
    override def write(b: Array[Byte], off: Int, len: Int) = ()
  }
}
