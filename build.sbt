organization := "dummyphase"

name := "dummyphase"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.2"

scalacOptions += "-deprecation"

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-reflect" % _)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ % "provided")

