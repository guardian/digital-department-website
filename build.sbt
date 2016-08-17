name := "digital-department-website"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
    .settings(
      play.sbt.PlayImport.PlayKeys.playDefaultPort := 8460
    )

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  ws
)

// TODO Default should be changed
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
