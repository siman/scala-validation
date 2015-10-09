import sbt._
import sbt.Keys._

object BuildSettings {
  
  val ossSnapshots = "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
  
  val ossStaging = "Sonatype OSS Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
  
  val buildSettings = Defaults.defaultSettings ++ Seq(
    name := "Plameno/Validation",
    organization := "com.plameno",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.10.2",
    crossScalaVersions := Seq("2.11.7"),
    resolvers += Resolver.sonatypeRepo("snapshots"),
    // credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    // publishTo <<= version((v: String) => Some( if (v.trim endsWith "SNAPSHOT") ossSnapshots else ossStaging)),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := (_ => false),
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.4" % "test",
      "com.novocode" % "junit-interface" % "0.8" % "test->default"
    ),
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
//      "-Ystatistics",
//      "-verbose",
      "-language:_"
	)
  )
}

object ValidationBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "validation",
    file("."),
    settings = buildSettings
  )
}

