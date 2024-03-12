import sbt._

object Dependencies {
  val kantanGeneric = "com.nrinaudo"      %% "kantan.csv"      % "0.7.10"
  val scalatest     = "org.scalatestplus" %% "scalacheck-1-17" % "3.2.18.0" % Test
  val kindProjector = "org.typelevel"     %% "kind-projector"  % "0.13.3" cross CrossVersion.binary
}
