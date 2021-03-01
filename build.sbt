ThisBuild / version      := "0.1.0"
ThisBuild / scalaVersion := "2.13.5"
ThisBuild / organization := "willis"

lazy val circeVersion    = "0.12.3"
//
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
//
mainClass in (Compile, run) := Some("com.simulator.Experiment")
//
lazy val simulator = (project in file("."))
  .settings(
    name := "mage-simulator",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "requests" % "0.6.5",
      "com.typesafe" % "config" % "1.3.4",

      //JSON
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,

      //JSON/Serialization
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,

      //Test
      "org.scalatest" %% "scalatest" % "3.1.0" % "test",
      "org.scalamock" %% "scalamock" % "4.4.0" % "test",
      "org.mockito" % "mockito-all" % "1.10.19" % "test"
    )
  )