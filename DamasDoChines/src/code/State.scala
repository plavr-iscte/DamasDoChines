package code.code
import code.code.Board
import code.code.Coord2D
import code.code.Engine
import code.code.MyRandom
import code.code.Score
import code.code.Stone

case class State(
    board:Board, //0
    player: Stone, //1
    lstOpenCoords: List[Coord2D], //2
    turn: Int, //3
    rand: MyRandom, //4
    startTime: Long, //5
    duration: Long, //6
    dimensions: (Int, Int), //7
    oldState: Option[State], //8
    score: Score, //9
    coordPos: Option[Coord2D], //10
    botStone: Stone, //11
    ) {
  

    def hasVictory(): Boolean = {
        !hasMovesForPlayer(player) && coordPos.isEmpty
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


    def hasEndCondition(millis: Long): Boolean = {
        hasVictory() || hasEnded(millis)
    }


}