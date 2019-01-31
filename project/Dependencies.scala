import sbt._

object Dependencies {
  lazy val cats = "org.typelevel" %% "cats-core" % "1.6.0"
  lazy val refined = "eu.timepit" %% "refined" % "0.8.7"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val kindProjector = "org.spire-math"  % "kind-projector" % "0.9.9" cross CrossVersion.binary
}
