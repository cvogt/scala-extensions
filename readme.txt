scala-extensions: Useful extensions for the Scala standard library

For usage see:
src/test/scala/*

Sbt setup:

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
)

libraryDependencies += "org.cvogt" %% "scala-extensions" % "0.1"
