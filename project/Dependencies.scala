import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val kindProjector = "org.spire-math"  % "kind-projector" % "0.9.3" cross CrossVersion.binary
}
