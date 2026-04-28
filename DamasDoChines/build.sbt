ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.8.2"

lazy val javafxVersion = "21.0.2"
lazy val javafxPlatform = "linux"

Compile / unmanagedSourceDirectories += baseDirectory.value / "src"
Compile / unmanagedResourceDirectories += baseDirectory.value / "src"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.2.0",
  "org.openjfx" % "javafx-base"     % javafxVersion classifier javafxPlatform,
  "org.openjfx" % "javafx-controls" % javafxVersion classifier javafxPlatform,
  "org.openjfx" % "javafx-fxml"     % javafxVersion classifier javafxPlatform
)
