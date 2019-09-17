import sbt._

object Dependencies {
  lazy val cats           = "org.typelevel"  %% "cats-core"            % "2.0.0"
  lazy val catsFree       = "org.typelevel"  %% "cats-free"            % "2.0.0"
  lazy val catsEffect     = "org.typelevel"  %% "cats-effect"          % "2.0.0"
  lazy val refined        = "eu.timepit"     %% "refined"              % "0.9.10"
  lazy val typesafeConfig = "com.typesafe"   % "config"                % "1.3.4"
  lazy val circe          = "io.circe"       %% "circe-parser"         % "0.12.1"
  lazy val scalacheck     = "org.scalacheck" %% "scalacheck"           % "1.14.0"
  lazy val discipline     = "org.typelevel"  %% "discipline-core"      % "1.0.0"
  lazy val disciplineTest = "org.typelevel"  %% "discipline-scalatest" % "1.0.0-M1"
  lazy val kindProjector  = "org.typelevel"  %% "kind-projector"       % "0.10.3" cross CrossVersion.binary
}
