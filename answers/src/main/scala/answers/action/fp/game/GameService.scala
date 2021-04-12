package answers.action.fp.game

import java.time.Duration

import answers.action.fp.IO

class GameService(client: GameClient) {
  import GameService._

  def getGameSummary(gameId: GameId): IO[GameSummary] =
    for {
      game       <- client.getGame(gameId)
      bluePlayer <- client.getPlayer(game.bluePlayerId)
      redPlayer  <- client.getPlayer(game.redPlayerId)
    } yield buildSummary(game, bluePlayer, redPlayer)

  def getRecentWinRate(playerId: PlayerId): IO[Option[Double]] =
    for {
      gameIds <- client.getRecentGames(playerId)
      games   <- gameIds.traverse(client.getGame)
    } yield calculateWinRate(playerId, games)

  def getRecentGameSummaries(playerId: PlayerId): IO[List[GameSummary]] =
    for {
      gameIds <- client.getRecentGames(playerId)
      games   <- gameIds.traverse(getGameSummary)
    } yield games

}

object GameService {

  def buildSummary(game: Game, bluePlayer: Player, redPlayer: Player): GameSummary =
    GameSummary(
      id = game.id,
      bluePlayer = bluePlayer,
      redPlayer = redPlayer,
      winnerId = game.winnerId,
      duration = Duration.between(game.startedAt, game.completedAt),
    )

  def calculateWinRate(playerId: PlayerId, games: List[Game]): Option[Double] = {
    val total = games.size
    if (total < 5) None
    else {
      val gamesWon = games.count(_.winnerId == playerId)
      Some(gamesWon / games.size)
    }
  }

}

case class GameRateLimitException(awaitDuration: Duration)
    extends Exception(
      s"You reached your maximum limit of request, please retry in $awaitDuration"
    )
