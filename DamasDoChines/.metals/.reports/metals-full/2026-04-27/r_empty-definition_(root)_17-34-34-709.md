error id: file://<WORKSPACE>/src/GameTick.scala:scala/Any#isInstanceOf().
file://<WORKSPACE>/src/GameTick.scala
empty definition using pc, found symbol in pc: scala/Any#isInstanceOf().
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -isInstanceOf.
	 -isInstanceOf#
	 -isInstanceOf().
	 -scala/Predef.isInstanceOf.
	 -scala/Predef.isInstanceOf#
	 -scala/Predef.isInstanceOf().
offset: 504
uri: file://<WORKSPACE>/src/GameTick.scala
text:
```scala
package code

case class GameTick () {

    def onTick(state: State): Unit = {
        val millis = Main.getMillis()
        
        if (!state.hasVictory() && !state.hasEnded(millis)) {
            print("Turn " + state.turn + "\n")
            print("Elapsed: "+ Main.getElapsedTime(state) + "\n")

            //wait for CLI user response
            val client = Cli(state)
            val (coordFrom, coordTo) = client.getCommand("Choose: 'play' or 'undo' or 'quit'", "")
            if coordFrom.i@@sInstanceOf[Coord2D] || coordTo.isInstanceOf[Coord2D]
            
            onTick(
                State(
                    state.board,
                    state.player,
                    state.lstOpenCoords,
                    state.turn+1,
                    state.rand,
                    state.startTime,
                    state.duration
                )
            )
        } 

    }


}

```


#### Short summary: 

empty definition using pc, found symbol in pc: scala/Any#isInstanceOf().