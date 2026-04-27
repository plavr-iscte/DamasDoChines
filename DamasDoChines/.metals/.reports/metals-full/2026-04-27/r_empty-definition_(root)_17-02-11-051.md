error id: file://<WORKSPACE>/src/GameTick.scala:
file://<WORKSPACE>/src/GameTick.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -state/hasEnded.
	 -state/hasEnded#
	 -state/hasEnded().
	 -scala/Predef.state.hasEnded.
	 -scala/Predef.state.hasEnded#
	 -scala/Predef.state.hasEnded().
offset: 124
uri: file://<WORKSPACE>/src/GameTick.scala
text:
```scala
package code

case class GameTick (state: State) {

    def onTick(): Unit = {
        if (!state.hasVictory() || state.hasE@@nded()) {
            val new_state = setTime(state)
        }

    }


    def setTime(oldState: State): State = {
        val new_time = System.currentTimeMillis()
        
    }

}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 