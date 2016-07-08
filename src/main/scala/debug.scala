package org.cvogt.scala.debug
import java.io.{OutputStream, PrintStream}
import scala.collection._

object `package`{
  implicit class AnyExtensions[T](val any: T) extends AnyVal{
    def println = {
      Predef.println(any)
      any
    }
  }  
  /* FIXME: maintain return type
  implicit class GenTraversableOnceDebugExtensions[A](val coll: GenTraversableOnce[A]) extends AnyVal{
    def printlnEach = {
      coll.foreach(println)
      coll
    }
    def foreach2(f: A => Unit) = {
      coll.foreach(f)
      coll
    }
  }
  */
  implicit class ThrowableExtensions( t: Throwable ) {
    def showStackTrace: String = {
      import java.io._
      val sw = new StringWriter()
      val pw = new PrintWriter( sw )
      t.printStackTrace( pw )
      val s = sw.toString
      sw.close
      pw.close
      s
    }
  }
}

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

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object printCodeAfterTyper {
  /** Prints code after implicits, macros and code desugaring have been applied. Returns passed value just like identity(code). */
  def apply[T]( code: T ): T = macro printCodeAfterTyperMacro[T]
  def printCodeAfterTyperMacro[T]( c: Context )( code: c.Expr[T] ): c.Expr[T] = {
    import org.cvogt.scala.StringExtensions
    val fileLine = code.tree.pos.source.file.absolute.path + ":" + code.tree.pos.line
    println("-"*80)
    println( fileLine + ": printCodeAfterTyper")
    println( "Code:\n" + code.tree.toString.indent )
    println()
    println( "AST:\n" + c.universe.showRaw( code.tree ).indent )
    println("-"*80)
    code
  }
}
