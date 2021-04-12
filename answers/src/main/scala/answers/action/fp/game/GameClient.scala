package answers.action.fp.game

import answers.action.fp.IO

trait GameClient {
  def getGame(id: GameId): IO[Game]
  def getPlayer(id: PlayerId): IO[Player]

  def getRecentGames(playerId: PlayerId): IO[List[GameId]]
}

object GameClient {

  def mock(games: List[Game], players: List[Player]): GameClient =
    new GameClient {
      def getGame(id: GameId): IO[Game] =
        IO {
          games
            .find(_.id == id)
            .getOrElse(throw new Exception(s"No Game with id $id"))
        }

      def getPlayer(id: PlayerId): IO[Player] =
        IO {
          players
            .find(_.id == id)
            .getOrElse(throw new Exception(s"No Player with id $id"))
        }

      def getRecentGames(playerId: PlayerId): IO[List[GameId]] =
        IO {
          games
            .filter(game => game.bluePlayerId == playerId || game.redPlayerId == playerId)
            .map(_.id)
        }
    }

}
