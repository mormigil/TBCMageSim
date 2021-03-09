ThisBuild / version      := "0.1.0"
ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "willis"

enablePlugins(ScalaJSPlugin)

lazy val circeVersion    = "0.12.3"
//
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"

// mainClass in (Compile, run) := Some("com.simulator.TutorialApp")

scalaJSUseMainModuleInitializer := true

// mainClass in Compile := Some("com.simulation.TutorialApp")

// scalaJSMainModuleInitializer := Some("com.simulation.TutorialApp")

lazy val simulator = (project in file("."))
  .settings(
    name := "mage-simulator",
    libraryDependencies ++= Seq(

    "org.scala-js" %%% "scalajs-dom" % "1.1.0"
    // "com.lihaoyi" %% "scalatags" % "0.9.3"

      //JSON
      // "io.circe" %% "circe-generic" % circeVersion,
      // "io.circe" %% "circe-parser" % circeVersion,
    )
  )