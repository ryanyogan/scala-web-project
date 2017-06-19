name := """scala-web-project"""
version := "1.0-SNAPSHOT"
scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
pipelineStages := Seq(digest)

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  evolutions,
  "org.scalikejdbc" %% "scalikejdbc" % "2.3.5",
  "org.scalikejdbc" %% "scalikejdbc-config" % "2.3.5",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "de.svenkubiak" % "jBCrypt" % "0.4.1"
)

libraryDependencies += "joda-time" % "joda-time" % "2.9.9"
libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
libraryDependencies += "com.softwaremill.macwire" %% "util" % "2.3.0"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1207.jre7"

// resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
