package exercises.function

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._

case class HttpClientBuilder(host: String,
                             port: Int,
                             timeout: FiniteDuration,
                             followRedirect: Boolean,
                             maxParallelRequest: Int) {

  def withTimeout(x: FiniteDuration): HttpClientBuilder =
    copy(timeout = x)

  def withFollowRedirect(x: Boolean): HttpClientBuilder =
    copy(followRedirect = x)

  def withMaxParallelRequest(x: Int): HttpClientBuilder =
    copy(maxParallelRequest = x)
}

object HttpClientBuilder {
  def default(host: String, port: Int): HttpClientBuilder =
    HttpClientBuilder(host, port, timeout = 30.seconds, followRedirect = false, maxParallelRequest = 2)

  def withTimeout(x: FiniteDuration): HttpClientBuilder => HttpClientBuilder =
    _.copy(timeout = x)

  def withFollowRedirect(x: Boolean): HttpClientBuilder => HttpClientBuilder =
    _.copy(followRedirect = x)

  def withMaxParallelRequest(x: Int): HttpClientBuilder => HttpClientBuilder =
    _.copy(maxParallelRequest = x)

}
