import sbt._

object Dependencies {
  lazy val cats           = "org.typelevel"  %% "cats-core"            % "2.1.0"
  lazy val catsFree       = "org.typelevel"  %% "cats-free"            % "2.1.0"
  lazy val catsEffect     = "org.typelevel"  %% "cats-effect"          % "2.0.0"
  lazy val refined        = "eu.timepit"     %% "refined"              % "0.9.10"
  lazy val circe          = "io.circe"       %% "circe-parser"         % "0.12.3"
  lazy val scalacheck     = "org.scalacheck" %% "scalacheck"           % "1.14.3"
  lazy val discipline     = "org.typelevel"  %% "discipline-core"      % "1.0.2"
  lazy val disciplineTest = "org.typelevel"  %% "discipline-scalatest" % "1.0.0-RC2"
  lazy val kindProjector  = "org.typelevel"  %% "kind-projector"       % "0.10.3" cross CrossVersion.binary
}
