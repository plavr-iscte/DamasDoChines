package code

case class Score(
    black: Int,
    white: Int,
)

def getHigherScore(state: State): Option[Stone] = {

    if (state.score.black > state.score.white) then
        Some(Stone.Black)
    else if state.score.black == state.score.white then
        None
    else 
        Some(Stone.White)
}

def getGameScore(state: State): String = {
    Console.GREEN + "Score:" + Console.RESET + 
    "\tWhite: " + state.score.white + 
    Console.BLACK + "\t Black: " + Console.RESET + state.score.black
}