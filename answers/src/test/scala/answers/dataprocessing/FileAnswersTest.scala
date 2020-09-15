package answers.dataprocessing

import java.io.File

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import FileAnswers._

class FileAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  val scalaSeedProject = new File(getClass.getResource("/scala-seed-project").toURI)

  test("diskUsage") {
    assert(diskUsageImperative(scalaSeedProject) == diskUsage(scalaSeedProject))
  }

  test("largestFileSize") {
    assert(largestFileSize(scalaSeedProject) == 447)
  }

  test("filterFiles") {
    assert(
      filterFiles(scalaSeedProject, _.getName.endsWith(".scala")).map(_.getName).sorted ==
        List("Dependencies.scala", "Hello.scala", "HelloSpec.scala")
    )
  }

}
