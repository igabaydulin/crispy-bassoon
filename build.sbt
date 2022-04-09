ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "concurrent-map",
    idePackagePrefix := Some("dev.igabaydulin.scala.concurrent.map")
  )
enablePlugins(JmhPlugin)