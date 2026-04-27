error id: file://<WORKSPACE>/src/Main.scala:local5
file://<WORKSPACE>/src/Main.scala
empty definition using pc, found symbol in pc: 
found definition using semanticdb; symbol local5
empty definition using fallback
non-local guesses:

offset: 705
uri: file://<WORKSPACE>/src/Main.scala
text:
```scala
package code

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters.*

object Main {

	def main(args: Array[String]): Unit = {

		val seed = 0x5DEE6767DL

		val (colLength, rowLength, turns) = 
			args match {
				case Array(c,r,t) => 
					try{
						(c.toInt, r.toInt, t.toInt)
					} catch {
						case _: NumberFormatException =>
							throw new IllegalArgumentException("Invalid start command. Usar: run [cols] [rows] [turns]")
					}
				case Array() => (8,8,40)
				case _ => throw new IllegalArgumentException("Invalid start command")
			}

		val engine = Engine()

		val startRandom = MyRandom(seed)
		val (initialBoard, r1, initialOpenCoords) = engine.initboard(@@rowLength, colLength, startRandom)
		
		val startTime = System.currentTimeMillis()

		val state = State(initialBoard, Stone.White, initialOpenCoords, turns, startRandom, startTime)
		val client = Cli(state, engine)


		client.getCommand("Start game? (type start)", "")
	}

	/**def main(args: Array[String]): Unit = {
		val numRows = 8
		val numCols = 8
		val maxTurns = 40
		val seed = 0x5DEE6767DL

		val client = Cli()
		val engine = Engine()

		val startRandom = MyRandom(seed)
		val (initialBoard, r1, initialOpenCoords) = engine.initboard(numRows, numCols, startRandom)
		val allCoords = (for {
			row <- 0 until numRows
			col <- 0 until numCols
		} yield Coord2D(row, col)).toList

		var board = initialBoard
		var openCoords: List[Coord2D] = initialOpenCoords
		var player = Stone.White
		var turn = 0
		var rand = r1
		var stalledTurns = 0

		println(s"Starting game on ${numRows}x${numCols}, maxTurns=$maxTurns, seed=$seed")
		println(s"Initial open coordinates: ${openCoords.mkString(", ")}")
		client.showBoard(board, openCoords, numRows, numCols)

		while turn <= maxTurns && stalledTurns < 2 do {
			if openCoords.isEmpty then
				println(s"Turn $turn - no open coordinates available")
				stalledTurns = 2
			else {
				val (nextBoardOpt, nextRand, nextOpenCoords, movedTo) =
				engine.playRandomly(board, rand, player, openCoords, engine.randomMove)

				rand = nextRand

				nextBoardOpt match {
				case Some(nextBoard) =>
					board = nextBoard
					openCoords = nextOpenCoords
					stalledTurns = 0
					println(s"Turn $turn - $player moved to ${movedTo.get}")
					client.showBoard(board, openCoords, numRows, numCols)
				case None =>
					stalledTurns += 1
					println(s"Turn $turn - $player has no legal random move")
				}

				player = engine.oppositeStone(player)
				turn += 1
			}
		}

		println("Game finished.")

	}**/

	/// Not Functional elements

	def setTime(time:String): Unit = {
		val new_time = time

	}






}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 