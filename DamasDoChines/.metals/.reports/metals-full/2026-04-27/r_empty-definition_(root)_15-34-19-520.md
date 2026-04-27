error id: file://<WORKSPACE>/src/Cli.scala:java/lang/String#split(+1).
file://<WORKSPACE>/src/Cli.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -split.
	 -scala/Predef.split.
offset: 1929
uri: file://<WORKSPACE>/src/Cli.scala
text:
```scala
package code

import scala.io.StdIn
import scala.annotation.switch

import scala.util.Try

case class Cli(
	state: State,
	engine: Engine,
) {

	def renderBoard(board: Board, lstOpenCoords: List[Coord2D], numRows: Int, numCols: Int): String = {
		val header = getHeader(numCols)
		val body = getBody(board, lstOpenCoords, numRows, numCols)
		header + "\n" + body
	}

	def renderRow(row: Int, board: Board, openCoords: List[Coord2D], numCols: Int): String = {
		def cols(col: Int): String =
			if col >= numCols then ""
			else {
			val curr = cellSymb(Coord2D(row, col), board, openCoords)
			if col == numCols - 1 then curr else curr + " " + cols(col + 1)
			}

		val prefix = if row < 10 then s"$row " else s"$row "
		prefix + cols(0)
	}

	def cellSymb(coord: Coord2D, board: Board, openCoords: List[Coord2D]): String =
	if openCoords.contains(coord) then "."
	else
		board.get(coord) match {
		case Some(Stone.Black) => "B"
		case Some(Stone.White) => "W"
		case None              => "."
		}

	def columnLabel(col: Int): String =
		('A' + col).toChar.toString

	def getHeader(numCols: Int): String = {
		def labels(col: Int): String =
			if col >= numCols then ""
			else {
			val curr = columnLabel(col)
			if col == numCols - 1 then curr else curr + " " + labels(col + 1)
			}

		"  " + labels(0)
	}

	def getBody(board: Board, lstOpenCoords: List[Coord2D], numRows: Int, numCols: Int): String = {
		def rows(row: Int): String =
			if row >= numRows then ""
			else {
			val curr = renderRow(row, board, lstOpenCoords, numCols)
			if row == numRows - 1 then curr else curr + "\n" + rows(row + 1)
			}

		rows(0)
	}

	def showBoard(board: Board, openCoords: List[Coord2D], numRows: Int, numCols: Int): Unit = {
		println(renderBoard(board, openCoords, numRows, numCols))
	}

	// get commands
	//// undo
	//// start game requires> col row turns ...
	//// 

	def getCommand(prompt:String, command: String) = {
		val result = .s@@plit("\\s+").toList

		result match {
			case "undo" :: Nil => doUndo()
			case "start" :: Nil => doStart()
			case "quit" :: Nil => doQuit()
			case "change" :: Nil => doChangeTurn()
			case "play" :: colFrom :: rowFrom :: colTo :: rowTo :: Nil =>
				engine.play(
					state.board, 
					state.player, 
					stringToCoord(colFrom.toString, rowFrom.toString), 
					stringToCoord(colTo.toString, rowTo.toString),
					state.lstOpenCoords
				)
			case "pr" :: Nil => 
				engine.playRandomly(
					state.board,
					state.rand,
					state.player,
					state.lstOpenCoords,
					engine.randomMove

				)
			case _ => None
		}

	}

	def stringToCoord(s1: String, s2: String): Coord2D = {
		try {
			Coord2D(s1.toInt, s2.toInt)
		} catch {
			case _: NumberFormatException =>
				throw new IllegalArgumentException(s"Not a valid input")
		}
	}

	///////////////// COMMANDS /////////////////////////

	def doUndo(): Unit = {
		print("Undoing: ")
	}
	
	def doStart(): Unit = {
		print("Starting: ")
	}

	def doQuit(): Unit = {
		print("Quit: ")

	}

	def doChangeTurn(): Unit = {
		print("Swapping Turns: ")
	}
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 