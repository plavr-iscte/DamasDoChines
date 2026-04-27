error id: file://<WORKSPACE>/src/Cli.scala:
file://<WORKSPACE>/src/Cli.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -RunTimeException.
	 -RunTimeException#
	 -RunTimeException().
	 -scala/Predef.RunTimeException.
	 -scala/Predef.RunTimeException#
	 -scala/Predef.RunTimeException().
offset: 2540
uri: file://<WORKSPACE>/src/Cli.scala
text:
```scala
package code

import scala.io.StdIn
import scala.annotation.switch

import scala.util.Try

case class Cli(
	state: State,
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
		val result = StdIn.readLine(prompt + ": ")

		result match {
			case "undo" => doUndo()
			case "start" => doStart()
			case "quit" => doQuit()
			case "change" => doChangeTurn()
			case "play" :: colFrom :: rowFrom :: colTo :: rowTo => 
				val engine = Engine()
				val cf = Int(colFrom)
				val rf = 
				engine.play(state.board, state.player, Coord2D(Int(colFrom)), )
			case ""
			case _ => None
		}


		print("Result: " + result)
	}

	def stringToCoord(s1: String, s2: String): Coord2D = {
		val i1 = Try(s1.toInt).toOption
		val i2 = Try(s2.toInt).toOption

		val int1 = if i1.isInstanceOf[Int] then i1.toInt else throw new RunTimeExcep@@tion("Not an Int")


		val res = Coord2D(int1,i2)
		res
	}
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 