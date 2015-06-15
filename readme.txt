scala-extensions: Useful extensions for the Scala standard library
http://cvogt.org/scala-extensions/

Contents:
Type-level constraints (org.cvogt.constraints)
- Comparisons: <:<, =:=, >:>, !=:=, !<:<, !>:>, e.g. String !=:= And
- Boolean Algebra: True, False, ==, !, &&, ||, Implies, Xor
- Subset tests: In, NotIn, e.g. Int NotIn (Any,AnyRef,AnyVal)

Collection extensions (org.cvogt.collection)
- distinctBy - remove duplicates by key
- foldWhile / reduceWhile - stoppable accumulation
- concat - type-safe alternative to mkString

For usage see:
src/test/scala/*

Sbt setup:

libraryDependencies += "org.cvogt" %% "scala-extensions" % "0.3.0"

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "https://oss.sonatype.org/service/local/repositories/releases/content",
  "Sonatype Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
)


