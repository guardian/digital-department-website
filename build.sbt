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

enablePlugins(RiffRaffArtifact, JDebPackaging)

import com.typesafe.sbt.packager.archetypes.ServerLoader.Systemd
serverLoading in Debian := Systemd
debianPackageDependencies := Seq("openjdk-8-jre-headless")
maintainer := "The Maintainer <the.maintainer@company.com>"
packageSummary := "Brief description"
packageDescription := """Slightly longer description"""

riffRaffPackageType := (packageBin in Debian).value

riffRaffBuildIdentifier := env("TRAVIS_BUILD_NUMBER").getOrElse("DEV")
riffRaffUploadArtifactBucket := Option("riffraff-artifact")
riffRaffUploadManifestBucket := Option("riffraff-builds")