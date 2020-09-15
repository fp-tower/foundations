package answers.dataprocessing

import java.io.File

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import FileAnswers._

class FileAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  val scalaSeedProject = new File(getClass.getResource("/scala-seed-project").toURI)

  test("sub dir") {
    assert(scalaSeedProject.listFiles().map(_.getName).toList == Nil)
  }

  test("path") {
    assert(scalaSeedProject.getAbsolutePath == "")
  }

  test("diskUsage") {
    assert(diskUsageImperative(scalaSeedProject) == 1946)
    assert(diskUsage(scalaSeedProject) == 1946)
  }

  test("largestFileSize") {
    assert(largestFileSize(scalaSeedProject) == 447)
  }

  test("filterFiles") {
    assert(
      filterFiles(scalaSeedProject, _.getName.endsWith(".scala")).map(_.getName) ==
        List("Dependencies.scala", "HelloSpec.scala", "Hello.scala")
    )
  }

}
