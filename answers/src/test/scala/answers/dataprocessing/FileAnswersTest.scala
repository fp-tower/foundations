package answers.dataprocessing

import java.io.File

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import FileAnswers._

class FileAnswersTest extends AnyFunSuite with ScalaCheckDrivenPropertyChecks {
  val scalaSeedProject = new File(getClass.getResource("/scala-seed-project").toURI)

  ignore("diskUsage") {
    assert(diskUsageImperative(scalaSeedProject) == 1946)
    assert(diskUsage(scalaSeedProject) == 1946)
  }

  ignore("largestFileSize") {
    assert(largestFileSize(scalaSeedProject) == 447)
  }

  ignore("filterFiles") {
    assert(
      filterFiles(scalaSeedProject, _.getName.endsWith(".scala")).map(_.getName) ==
        List("Dependencies.scala", "HelloSpec.scala", "Hello.scala")
    )
  }

}
