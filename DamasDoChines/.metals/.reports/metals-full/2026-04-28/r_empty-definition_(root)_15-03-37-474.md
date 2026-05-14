error id: file://<WORKSPACE>/src/State.scala:code/Engine.
file://<WORKSPACE>/src/State.scala
empty definition using pc, found symbol in pc: code/Engine.
empty definition using semanticdb

found definition using fallback; symbol Engine
offset: 559
uri: file://<WORKSPACE>/src/State.scala
text:
```scala
package code

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
    ) {
  

    def hasVictory(): Boolean = {
        def moves(coor: Coord2D): List[Coord2D] = List(Coord2D(coor.x+2, coor.y), Coord2D(coor.x-2, coor.y), Coord2D(coor.x, coor.y+2), Coord2D(coor.x, coor.y-2))

        !lstOpenCoords.exists { 
            coor => moves(coor).exists { 
                c => @@Engine.play(board, player, c, coor, lstOpenCoords)._1.nonEmpty 
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
```


#### Short summary: 

empty definition using pc, found symbol in pc: code/Engine.