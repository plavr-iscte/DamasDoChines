package code.code
import code.code.Board
import code.code.Coord2D
import code.code.Engine
import code.code.MyRandom
import code.code.Score
import code.code.Stone

case class State(
    board:Board,
    player: Stone,
    lstOpenCoords: List[Coord2D],
    turn: Int,
    rand: MyRandom,
    startTime: Long,
    duration: Long,
    dimensions: (Int, Int),
    oldState: Option[State],
    score: Score,
    coordPos: Option[Coord2D],
    ) {
  

    def hasVictory(): Boolean = {
        !hasMovesForPlayer(Stone.White) && !hasMovesForPlayer(Stone.Black)
    }

    def hasMovesForPlayer(player: Stone): Boolean = {
        lstOpenCoords.exists { 
            coor => Engine.moves(coor).exists { 
                c => Engine.play(board, player, c, coor, lstOpenCoords)._1.nonEmpty 
            } 
        }
    }

    def hasEnded(curr:Long): Boolean = {
        curr >= duration
    }

    def getWinner(): Option[Stone] = {
        val wMoves = hasMovesForPlayer(Stone.White)
        val bMoves = hasMovesForPlayer(Stone.Black)


        if(!wMoves && !bMoves) then
            if this.score.black > this.score.white then
                Some(Stone.Black)
            else if this.score.white > this.score.black then
                Some(Stone.White)
            else None
        else None
    }


    def hasEndCondition(millis: Long): Boolean = {
        hasVictory() || hasEnded(millis)
    }


}