import Dependencies._

lazy val baseSettings: Seq[Setting[_]] = Seq(
  scalaVersion       := "2.12.8",
  scalacOptions     ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:higherKinds", "-language:implicitConversions", "-language:existentials", "-language:postfixOps",
    "-unchecked",
    "-Yno-adapted-args",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-Ypartial-unification"
  ),
  addCompilerPlugin(kindProjector),
  libraryDependencies ++= Seq(
    cats,
    refined,
    typesafeConfig,
    scalacheck,
    discipline,
    scalatest % Test,
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