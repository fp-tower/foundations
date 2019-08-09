import Dependencies._

lazy val baseSettings: Seq[Setting[_]] = Seq(
  scalaVersion := "2.13.0",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:existentials",
    "-language:postfixOps",
    "-unchecked",
    "-Ywarn-value-discard"
  ),
  addCompilerPlugin(kindProjector),
  libraryDependencies ++= Seq(
    cats,
    catsEffect,
    refined,
    typesafeConfig,
    scalacheck,
    discipline,
    disciplineTest % Test
  )
)

lazy val foundation = project
  .in(file("."))
  .settings(moduleName := "foundation")
  .settings(baseSettings: _*)
  .aggregate(exercises, slides)
  .dependsOn(exercises, slides)

lazy val exercises = project
  .settings(moduleName := "foundation-exercises")
  .settings(baseSettings: _*)

lazy val slides = project
  .dependsOn(exercises)
  .settings(moduleName := "foundation-slides")
  .settings(moduleName := "foundation-slides")
  .settings(baseSettings: _*)
  .settings(
    tutSourceDirectory := baseDirectory.value / "tut",
    tutTargetDirectory := baseDirectory.value / "docs"
  )
  .enablePlugins(TutPlugin)


addCommandAlias("testAnswers", "testOnly *AnswersTest")

addCommandAlias("testExercises1", "testOnly function.*ExercisesTest")
addCommandAlias("testExercises2", "testOnly sideeffect.*ExercisesTest")
addCommandAlias("testExercises3", "testOnly types.*ExercisesTest")
addCommandAlias("testExercises4", "testOnly errorhandling.*ExercisesTest")
addCommandAlias("testExercises6", "testOnly typeclass.*ExercisesTest")
addCommandAlias("testExercises7", "testOnly functors.*ExercisesTest")
