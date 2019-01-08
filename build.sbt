import Dependencies._

lazy val baseSettings: Seq[Setting[_]] = Seq(
  scalaVersion       := "2.12.4",
  scalacOptions     ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:higherKinds",
    "-language:implicitConversions", "-language:existentials",
    "-unchecked",
    "-Xfatal-warnings",
    "-Yno-adapted-args",
    //    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-Ypartial-unification"
  ),
  addCompilerPlugin(kindProjector),
  libraryDependencies ++= Seq(
    cats,
    refined,
    scalaTest % Test
  )
)

lazy val `fp-foundation` = project.in(file("."))
  .settings(moduleName := "fp-foundation")
  .settings(baseSettings: _*)
  .aggregate(exercises, slides)
  .dependsOn(exercises, slides)

lazy val exercises = project
  .settings(moduleName := "fp-foundation-exercises")
  .settings(baseSettings: _*)

lazy val slides = project
  .settings(moduleName := "fp-foundation-slides")
  .settings(baseSettings: _*)
  .settings(
    tutSourceDirectory := baseDirectory.value / "tut",
    tutTargetDirectory := baseDirectory.value / "../docs"
  )
  .enablePlugins(TutPlugin)