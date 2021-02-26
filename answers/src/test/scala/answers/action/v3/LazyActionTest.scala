package answers.action.v3

import org.scalatest.funsuite.AnyFunSuite

class LazyActionTest extends AnyFunSuite {

  test("delay is lazy") {
    var state  = 0
    val action = LazyAction.delay(state += 1)

    assert(state == 0)
    action.execute()
    assert(state == 1)
  }

  test("delay can be repeated") {
    var state  = 0
    val action = LazyAction.delay(state += 1)

    action.execute()
    action.execute()
    assert(state == 2)
  }

  test("andThen is lazy") {
    var state     = 1
    val increment = LazyAction.delay(state += 1)
    val double    = LazyAction.delay(state *= 2)
    val combined  = increment.andThen(_ => double)

    assert(state == 1)
    combined.execute()
    assert(state == 4)
  }

  test("map is lazy") {
    var state     = 0
    val increment = LazyAction.delay { state += 1 }
    val combined  = increment.map(identity)

    assert(state == 0)
    combined.execute()
    assert(state == 1)
  }

  test("attempt") {
    assert(LazyAction.delay(()).attempt.execute().isSuccess)

    assert(LazyAction.fail(new Exception("Boom!")).attempt.execute().isFailure)
  }

  test("retry") {
    var state = 0
    val action = LazyAction.delay {
      state += 1
      if (state <= 3) sys.error("Boom")
    }

    assert(action.attempt.execute().isFailure)
    assert(action.retry(2).attempt.execute().isFailure)
    assert(action.retry(3).attempt.execute().isSuccess)
  }

}
