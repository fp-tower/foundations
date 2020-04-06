import sbt._

object Dependencies {
  lazy val cats           = "org.typelevel"  %% "cats-core"            % "2.1.1"
  lazy val circe          = "io.circe"       %% "circe-parser"         % "0.13.0"
  lazy val scalacheck     = "org.scalacheck" %% "scalacheck"           % "1.14.3"
  lazy val disciplineTest = "org.typelevel"  %% "discipline-scalatest" % "1.0.1"
  lazy val kindProjector  = "org.typelevel"  %% "kind-projector"       % "0.10.3" cross CrossVersion.binary
}
