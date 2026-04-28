package code

case class GameTick () {

    def onTick(state: State): Unit = {
        val millis = Main.getMillis()
        
        if (!state.hasEndCondition(millis)) {
            print(Console.BLUE + "Turn " + state.turn + "\n" + Console.RESET)

            val client = Cli(state)
            client.showBoard(state.board, state.lstOpenCoords, state.dimensions._1, state.dimensions._2)

            //wait for CLI user response

            println(Console.GREEN + Engine.getAllPlays(state, Coord2D(3,3)) + Console.RESET)
            
            val nextState = Engine.getCommand("Choose: 'play' or 'undo' or 'quit' or 'pr'", "") match {
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
                            val scorer = state.player match {
                                case Stone.Black => Score(state.score.black + 1, state.score.white)
                                case Stone.White => Score(state.score.black, state.score.white+1)
                            }
                            
                            State(
                                newBoard,
                                state.player,
                                newOpen,
                                state.turn,
                                state.rand,
                                Main.getMillis(),
                                state.duration,
                                state.dimensions,
                                Some(state),
                                scorer,
                            )
                        case None => 
                            println(Console.RED + "Invalid move" + Console.RESET) 
                            state
                    }

                case "pr" =>
                    val scorer = state.player match {
                        case Stone.Black => Score(state.score.black + 1, state.score.white)
                        case Stone.White => Score(state.score.black, state.score.white+1)
                    }
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
                                Main.getMillis(),
                                state.duration,
                                state.dimensions,
                                Some(state),
                                scorer,
                            )
                        case None =>
                            println("Sem movimentos random")
                            state
                        }
                case "quit" => 
                    Main.doQuit()
                    state

                case "change" =>
                    State(
                        state.board,
                        Engine.oppositeStone(state.player),
                        state.lstOpenCoords,
                        state.turn+1,
                        state.rand,
                        Main.getMillis(),
                        state.duration,
                        state.dimensions,
                        Some(state),
                        state.score,
                    )
                case "undo" => 
                    state.oldState.getOrElse(state) // Validação para um possível erro de tipo
                case None => 
                    println(Console.RED + "Invalid command" + Console.RESET)
                    state
                case _ => 
                    println(Console.RED + "Not a known command" + Console.RESET)
                    state
                
            }

            onTick(nextState)
        } 

    }


}
