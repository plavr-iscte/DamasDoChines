package code

case class GameTick () {

    def onTick(state: State): Unit = {
        val millis = Main.getMillis()
        
        if (!state.hasVictory() && !state.hasEnded(millis)) {
            print("Turn " + state.turn + "\n")
            print("Elapsed: "+ Main.getElapsedTime(state) + "\n")

            val client = Cli(state)
            client.showBoard(state.board, state.lstOpenCoords, state.dimensions._1, state.dimensions._2)

            //wait for CLI user response
            
            val nextState = client.getCommand("Choose: 'play' or 'undo' or 'quit' or 'pr'", "") match {
                case (coordFrom:Coord2D, coordTo:Coord2D) =>
                    //play condition
                    val (newBoard, newOpen) = Engine.play(
                        state.board, 
                        state.player, 
                        coordFrom,
                        coordTo,
                        state.lstOpenCoords
                    )
                    newBoard match {
                        case Some(newBoard) =>
                            State(
                                newBoard,
                                state.player,
                                newOpen,
                                state.turn,
                                state.rand,
                                state.startTime,
                                state.duration,
                                state.dimensions,
                                Some(state),
                            )
                        case None => 
                            println("Invalid move") 
                            state
                    }

                case "pr" =>
                    val (newBoard, newRand, newOpen, newPos) = 
                        Engine.playRandomly(
                            state.board,
                            state.rand,
                            state.player,
                            state.lstOpenCoords,
                            Engine.randomMove
                        )
                    newBoard match {
                        case Some(newBoard) =>
                            println("Random move: " + newPos)
                            State(
                                newBoard,
                                state.player,
                                newOpen,
                                state.turn,
                                newRand,
                                state.startTime,
                                state.duration,
                                state.dimensions,
                                Some(state),
                            )
                        case None =>
                            println("Sem movimentos random")
                            state
                        }
                case "quit" => 
                    client.doQuit()
                    state

                case "change" =>
                    State(
                        state.board,
                        Engine.oppositeStone(state.player),
                        state.lstOpenCoords,
                        state.turn+1,
                        state.rand,
                        state.startTime,
                        state.duration,
                        state.dimensions,
                        Some(state),
                    )
                case "undo" => 
                    state.oldState.getOrElse(state) // Validação para um possível erro de tipo
                case None => 
                    println("Invalid command")
                    state
                case _ => 
                    println("Not a known command")
                    state
                
            }

            onTick(nextState)
        } 

    }


}
