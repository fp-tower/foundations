package answers.action.fp.game

import java.time.Instant

// Simple wrapper to avoid using the a player id when we need a game id
// and vice versa.
case class GameId(value: Long)
case class PlayerId(value: Long)

case class Game(
  id: GameId,
  bluePlayerId: PlayerId,
  redPlayerId: PlayerId,
  winnerId: PlayerId,
  startedAt: Instant,
  completedAt: Instant,
)

// A more realistic player record would have more fields
// such as statistics, timestamps, skills and so on.
case class Player(id: PlayerId, name: String)
