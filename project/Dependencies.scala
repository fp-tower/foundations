import sbt._

object Dependencies {
  lazy val cats           = "org.typelevel"  %% "cats-core"      % "2.0.0-M4"
  lazy val catsEffect     = "org.typelevel"  %% "cats-effect"    % "1.3.1"
  lazy val refined        = "eu.timepit"     %% "refined"        % "0.9.8"
  lazy val typesafeConfig = "com.typesafe"   % "config"          % "1.3.4"
  lazy val scalatest      = "org.scalatest"  %% "scalatest"      % "3.0.7"
  lazy val scalacheck     = "org.scalacheck" %% "scalacheck"     % "1.14.0"
  lazy val discipline     = "org.typelevel"  %% "discipline"     % "0.11.1"
  lazy val kindProjector  = "org.typelevel"  %% "kind-projector" % "0.10.3" cross CrossVersion.binary
}
