ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.8.2"

lazy val javafxVersion = "21.0.2"
lazy val scalafxVersion = "21.0.0-R32"
lazy val osName = sys.props("os.name").toLowerCase
lazy val javafxPlatform =
  if (osName.contains("win")) "win"
  else if (osName.contains("linux")) "linux"
  else sys.error(s"Unsupported OS for JavaFX: ${sys.props("os.name")}")

Compile / unmanagedSourceDirectories += baseDirectory.value / "src"
Compile / unmanagedResourceDirectories += baseDirectory.value / "src"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.2.0",
  "org.scalafx" %% "scalafx" % scalafxVersion,
  "org.openjfx" % "javafx-base"     % javafxVersion classifier javafxPlatform,
  "org.openjfx" % "javafx-controls" % javafxVersion classifier javafxPlatform,
  "org.openjfx" % "javafx-fxml"     % javafxVersion classifier javafxPlatform
)
