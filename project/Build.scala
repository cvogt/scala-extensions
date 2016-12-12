import sbt._
import Keys._

object MyBuild extends Build{
  val repoKind = SettingKey[String]("repo-kind", "Maven repository kind (\"snapshots\" or \"releases\")")
  val projectName = "scala-extensions"
  lazy val aRootProject = Project(id = projectName, base = file("."),
    settings = Seq(
      name := projectName,
      scalaVersion := "2.12.0",
      crossScalaVersions := Seq("2.10.6","2.11.8",scalaVersion.value),
      description := "Faithful extensions of the Scala standard library",
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "3.0.0" % "test",
        "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
      ),
      scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked"),
      //scalacOptions ++= Seq("-Xprint:patmat", "-Xshow-phases"),
      testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oFD"),
      parallelExecution := false, // <- until TMap thread-safety issues are resolved
      version := "0.5.3",
      organizationName := "Jan Christopher Vogt",
      organization := "org.cvogt",
      scalacOptions in (Compile, doc) <++= (version,sourceDirectory in Compile,name).map((v,src,n) => Seq(
        "-doc-title", n,
        "-doc-version", v,
        "-doc-footer", projectName+" is developed by Jan Christopher Vogt.",
        "-sourcepath", src.getPath, // needed for scaladoc to strip the location of the linked source path
        "-doc-source-url", "https://github.com/cvogt/"+projectName+"/blob/"+v+"/src/main€{FILE_PATH}.scala",
        "-implicits",
        "-diagrams", // requires graphviz
        "-groups"
      )),
      repoKind <<= (version)(v => if(v.trim.endsWith("SNAPSHOT")) "snapshots" else "releases"),
      //publishTo <<= (repoKind)(r => Some(Resolver.file("test", file("c:/temp/repo/"+r)))),
      publishTo <<= (repoKind){
        case "snapshots" => Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
        case "releases" =>  Some("releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
      },
      publishMavenStyle := true,
      publishArtifact in Test := false,
      pomIncludeRepository := { _ => false },
      makePomConfiguration ~= { _.copy(configurations = Some(Seq(Compile, Runtime, Optional))) },
      licenses += ("Apache 2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
      homepage := Some(url("http://github.com/cvogt/"+projectName)),
      startYear := Some(2015),
      pomExtra :=
        <developers>
          <developer>
            <id>cvogt</id>
            <name>Jan Christopher Vogt</name>
            <timezone>-5</timezone>
            <url>https://github.com/cvogt/</url>
          </developer>
        </developers>
          <scm>
            <url>git@github.com:cvogt/{projectName}.git</url>
            <connection>scm:git:git@github.com:cvogt/{projectName}.git</connection>
          </scm>
    )
  )
}
