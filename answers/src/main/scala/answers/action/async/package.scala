package answers.action

import scala.util.Try

package object async {
  type CallBack[A] = Try[A] => Unit
}
