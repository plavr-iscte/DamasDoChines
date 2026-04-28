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
        
        !lstOpenCoords.exists { 
            coor => Engine.moves(coor).exists { 
                c => Engine.play(board, player, c, coor, lstOpenCoords)._1.nonEmpty 
            } 
        }
    
    }

    def hasEnded(curr:Long): Boolean = {
        curr >= duration
    }


    /*def hasVictory(): Boolean = {

        val engine = Engine()

        def checkPlay(coor: Coord2D): Boolean = {
        val nList = List(Coord2D(coor.x+2, coor.y), Coord2D(coor.x-2, coor.y), Coord2D(coor.x, coor.y+2), Coord2D(coor.x, coor.y-2))
        
        val playList = nList.map(c => engine.play(board, player, c, coor, lstOpenCoords)._1).filterNot(_ == None)

        playList.isEmpty


        }

        val plays = lstOpenCoords.map(c => checkPlay(c)).filterNot(_ == true)

        plays.isEmpty

    }*/


    def hasEndCondition(millis: Long): Boolean = {
        hasVictory() || hasEnded(millis)
    }


}