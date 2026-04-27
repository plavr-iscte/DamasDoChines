error id: file://<WORKSPACE>/src/GameTick.scala:java/lang/System#currentTimeMillis().
file://<WORKSPACE>/src/GameTick.scala
empty definition using pc, found symbol in pc: java/lang/System#currentTimeMillis().
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -System.currentTimeMillis.
	 -System.currentTimeMillis#
	 -System.currentTimeMillis().
	 -scala/Predef.System.currentTimeMillis.
	 -scala/Predef.System.currentTimeMillis#
	 -scala/Predef.System.currentTimeMillis().
offset: 177
uri: file://<WORKSPACE>/src/GameTick.scala
text:
```scala
package code

case class GameTick (state: State) {

    def onTick(): Unit = {
        while (!state.hasVictory()) {
            val new_state = setTime(System.currentTimeMillis@@(),state)
        }
    }


    def setTime(time:Long, oldState: State): State = {
        val new_time = time
        
        State(
            oldState.board,
            oldState.player,
            oldState.lstOpenCoords,
            oldState.turn,
            oldState.rand,
            new_time
        )

    }

}

```


#### Short summary: 

empty definition using pc, found symbol in pc: java/lang/System#currentTimeMillis().