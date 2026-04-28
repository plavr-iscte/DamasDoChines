package code.code

import code.code.Engine.getCommand
import code.code.Cli
import code.code.Engine
import code.code.Main
import code.code.State

case class GameTick () {
    
    def onTick(state: State): Unit = {
        val millis = Main.getMillis()

        val client = Cli(state)
        client.showBoard(state.board, state.lstOpenCoords, state.dimensions._1, state.dimensions._2)
        
        if (!state.hasEndCondition(millis)) {
            println(Console.BLUE + "Turn " + state.turn + Console.RESET)


            //wait for CLI user response

            //println(Console.GREEN + Engine.getAllPlays(state, Coord2D(3,3)) + Console.RESET)
            
            val nextState = Engine.getNextState(state, getCommand("Choose: 'play' or 'undo' or 'quit' or 'pr'", ""))
            
            onTick(nextState)
        }

    }


}


