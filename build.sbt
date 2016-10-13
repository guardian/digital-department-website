name := "digital-department-website"

version := "1.0"

def env(key: String): Option[String] = Option(System.getenv(key))

lazy val root = (project in file(".")).enablePlugins(PlayScala, RiffRaffArtifact, JDebPackaging)
    .settings(
      play.sbt.PlayImport.PlayKeys.playDefaultPort := 9000
    )

scalaVersion := "2.11.8"

javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null",
  "-J-XX:MaxRAMFraction=2",
  "-J-XX:InitialRAMFraction=2",
  "-J-XX:MaxMetaspaceSize=500m",
  "-J-XX:+PrintGCDetails",
  "-J-XX:+PrintGCDateStamps",
  s"-J-Xloggc:/var/log/${name.value}/gc.log"
)

maintainer := "Anne Byrne <anne.byrne@theguardian.com>"

packageSummary := "Digital Department Website"

packageDescription := """The Guardian Digital website for Dig Dev and Enterprise Technology"""


libraryDependencies ++= Seq(
  ws,
  "com.amazonaws" % "aws-java-sdk-core" % "1.10.77",
  "com.gu" %% "scanamo" % "0.7.0",
  "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B4",
  "com.github.cb372" %% "automagic" % "0.1"
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
riffRaffManifestVcsUrl := "git@github.com:guardian/digital-department-website.git"
riffRaffUploadArtifactBucket := Option("riffraff-artifact")
riffRaffUploadManifestBucket := Option("riffraff-builds")

routesGenerator := InjectedRoutesGenerator

scalariformSettings
