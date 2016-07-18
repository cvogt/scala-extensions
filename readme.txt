scala-extensions: Useful extensions for the Scala standard library
http://cvogt.org/scala-extensions/

Contents:

Type-level helpers
- EnumerateSingletons - listing all objects extending a sealed trait
- Defaults - object giving names access to a class's default values

Collection extensions (org.cvogt.collection)
- distinctBy - remove duplicates by key
- foldWhile / reduceWhile - stoppable accumulation
- concat - type-safe alternative to mkString
- containsTyped

String extensions
- stripIndent - alternative to stripMargin not requiring |
- indent/indent(n) - indents each line
- commonLinePrefix, trimLeft, trimRight, trimLinesLeft, trimLinesRight
- containsTyped

Debug (org.cvogt.scala.debug)
- printCodeAfterTyper(...) - Prints code after implicits, macros and code desugaring have been applied.
- (t: Throwable).showStackTrace that returns stack tract as string

Type safety
- safe"..." alternative to s"..." that requires explicit toString conversions rather than implicit

Type-level constraints (org.cvogt.constraints)
- CaseClass and SingletonObject type classes
- Comparisons: <:<, =:=, >:>, !=:=, !<:<, !>:>, e.g. String !=:= And
- Boolean Algebra: True, False, ==, !, &&, ||, Implies, Xor
- Subset tests: In, NotIn, e.g. Int NotIn (Any,AnyRef,AnyVal)

Others
- alternative `->` that works as constructor, extractor, type

For usage see:
src/test/scala/*

Sbt setup:

libraryDependencies += "org.cvogt" %% "scala-extensions" % "0.5.1"
