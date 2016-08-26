name := "digital-department-website"

version := "1.0"

def env(key: String): Option[String] = Option(System.getenv(key))

lazy val root = (project in file(".")).enablePlugins(PlayScala, RiffRaffArtifact, JDebPackaging)
    .settings(
      play.sbt.PlayImport.PlayKeys.playDefaultPort := 8460
    )

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  ws,
  "com.amazonaws" % "aws-java-sdk-core" % "1.10.77",
  "com.gu" %% "scanamo" % "0.6.0"
)

// TODO Default should be changed
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

import com.typesafe.sbt.packager.archetypes.ServerLoader.Systemd
serverLoading in Debian := Systemd
debianPackageDependencies := Seq("openjdk-8-jre-headless")
maintainer := "The Maintainer <the.maintainer@company.com>"
packageSummary := "Brief description"
packageDescription := """Slightly longer description"""

riffRaffPackageType := (packageBin in Debian).value

riffRaffBuildIdentifier := env("BUILD_NUMBER").getOrElse("DEV")
riffRaffUploadArtifactBucket := Option("riffraff-artifact")
riffRaffUploadManifestBucket := Option("riffraff-builds")

routesGenerator := InjectedRoutesGenerator

scalariformSettings
