error id: file://<WORKSPACE>/src/GameTick.scala:local0
file://<WORKSPACE>/src/GameTick.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -millis.
	 -millis#
	 -millis().
	 -scala/Predef.millis.
	 -scala/Predef.millis#
	 -scala/Predef.millis().
offset: 180
uri: file://<WORKSPACE>/src/GameTick.scala
text:
```scala
package code

case class GameTick () {

    def onTick(state: State): Unit = {
        val elapsed = Main.getMillis()
        
        if (!state.hasVictory() && !state.hasEnded(mi@@llis)) {
            //wait for CLI user response
            val client = Cli(state)
            client.getCommand("Choose: 'play' or 'undo' or 'quit'", "")
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

empty definition using pc, found symbol in pc: 