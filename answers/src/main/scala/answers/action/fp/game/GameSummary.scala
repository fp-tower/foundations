package answers.action.fp.game

import java.time.Duration

case class GameSummary(
  id: GameId,
  bluePlayer: Player,
  redPlayer: Player,
  winnerId: PlayerId,
  duration: Duration,
)
