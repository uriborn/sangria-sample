
val baseName = "sample"

lazy val root = (project in file("."))
  .settings(name := baseName)
  .settings(Settings.basicSettings: _*)
  .aggregate(
    sangriaSample
  )

lazy val sangriaSample = (project in file("sangria-sample"))
  .settings(name := s"$baseName-sangria")
  .settings(Settings.basicSettings: _*)
  .settings(libraryDependencies ++= Dependencies.SangriaProject.dependencies)
