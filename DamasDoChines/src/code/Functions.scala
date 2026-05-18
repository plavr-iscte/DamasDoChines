package code.code

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters.*
import scala.io.StdIn
import scala.io.Source
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId, ZonedDateTime} 
import code.code.Engine
import code.code.GameTick
import code.code.MyRandom
import code.code.Score
import code.code.State
import code.code.Board

object Functions {
  
def recurGT(state: State): Unit = {
		val nextState = Engine.getNextState(state, Engine.getCommand("'restart' or 'quit'", ""))
		recurGT(nextState)
	}

	def getTitle(state: State): String = {
		val instant = getMillis()
		if(!state.hasEndCondition(instant)) then
			val t = "turno: " + state.turn
			val pl= "player: " + state.player
			val cp = "Coord: " + state.coordPos
			val sc = "Brancas: " + state.score.white + " | Pretas: " + state.score.black
			val d = "Dificuldade: " + state.difficulty
			t+ "\t" + pl + "\t" + cp + "\n" + sc + "\t" + d
		else if state.hasVictory() then
			state.player match {
				case Stone.White => "Vencedor: Pretas"
				case Stone.Black => "Vencedor: Brancas"
			}
		else 
			"Esgotou o tempo: " + getElapsedTime(state)
	}
	

	/// Elementos não funcionais

	def getMillis(): Long = {
		System.currentTimeMillis()
	}

	def getElapsedTime(state: State): String = {
		val (minutes, seconds) = timeConversion(state.startTime)
		minutes + "m:" + seconds + "s"
	}

	def timeConversion(l: Long): (Long, Long) = {
		val totalseconds = (getMillis() - l) / 1000
		val minutes = totalseconds / 60
		val seconds = totalseconds % 60
		(minutes, seconds)
	}

	def doQuit(): Unit = {
		output(Console.YELLOW + "Quitting!" + Console.RESET)
		System.exit(0) //////// não é funcional
	}


	def output(s: String): Unit = {
		println(s)
	}

	
	def readInput(s: String): String = {
		StdIn.readLine(s)
	} 

	def timeout(time: Int): Unit = {
		Thread.sleep(time*1000L) // para que a joga da do computador não seja quase instantanea
	}

	def readFromFile(filename: String): String = {
		val path = Paths.get("src", "resources", filename)
		val src = Source.fromFile(path.toFile)
		try src.getLines().mkString("\n")
		finally src.close()
	}

	def writeInput(filename: String, content:String): Unit = {
		val path = Paths.get("src", "resources", filename)
		Files.writeString(path, content)
	}

	def writeInputAppend(filename: String, content:String): Unit = {
		val path = Paths.get("src", "resources", filename)
		Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND)
	}

	def getStateToString(state: State): String = {
		val boardS = state.board
			.toList
			.sortBy { case (coord, _) => (coord.x, coord.y) }
			.map { case (coord, stone) => s"${coord.x},${coord.y},${stone.toString}" }
			.mkString("|")

		val lstOpenCoordsS = state.lstOpenCoords
			.map { case c => s"${c.x},${c.y}"}.mkString("|")
		
		val cPos = state.coordPos match
			case Some(c) => s"${c.x},${c.y}"
			case None    => "None,None"

		boardS + "###" + 
		state.player.toString + "###" + 
		lstOpenCoordsS + "###" +
		state.turn + "###" +
		state.rand + "###" + 
		"[STARTTIME]###" + 
		state.duration + "###" +
		state.dimensions._1 + "," + state.dimensions._2 + "###" +
		"[OLDSTATE]###" +
		state.score + "###" + 
		cPos + "###" +
		state.botStone.toString + "###" +
		state.difficulty.toString + "\n"
	
	}

	def stringToState(arr: List[String], oldState:Option[State]): State = {
		val head = arr.head
		val listHead = head.split("###")

		val board: Board = listHead(0)
			.split("\\|")
			.toList.filter(_.nonEmpty)
			.map { cell =>
				val c = cell.split(",")
				val coord = Coord2D(c(0).toInt, c(1).toInt)
				val stone = c(2) match {
					case "White" => Stone.White
					case "Black" => Stone.Black
				}
				coord -> stone
			}.toMap.par

		val player: Stone = listHead(1) match {
			case "White" => Stone.White
			case "Black" => Stone.Black
		}

		val lstOpenCoords: List[Coord2D] = listHead(2)
			.split("\\|")
			.toList.filter(_.nonEmpty)
			.map { data =>
				val cc = data.split(",")
				Coord2D(cc(0).toInt, cc(1).toInt)	
			}

		val turn = listHead(3).toInt
		val rand = listHead(4).replace("MyRandom(", "").replace(")", "").toLong
		val startTime = Functions.getMillis()
		val duration = listHead(6).toLong
		val tempDim = listHead(7).split(",")
		val dimensions = (tempDim(0).toInt, tempDim(1).toInt)
		// oldState = oldState
		val tempScore = listHead(9).replace("Score(", "").replace(")", "").split(",")
		val score = Score(tempScore(0).toInt, tempScore(1).toInt)

		val coordPos: Option[Coord2D] = listHead(10) match {
			case "None,None" => None
			case value => 
				val sTemp = value.split(",") 
				Some(Coord2D(sTemp(0).toInt,sTemp(1).toInt))
		}

		val botStone = listHead(11).trim match {
			case "White" => Stone.White
			case "Black" => Stone.Black
			case o => 
				println("ERROR: Setting botStone")
				Stone.White
		}

		val difficulty = listHead(12) match {
			case "Easy" => Difficulty.Easy
			case "Medium" => Difficulty.Medium
			case "Hard" => Difficulty.Hard
			case _ => Difficulty.Easy
		}

		val nState = State(
			board, player, lstOpenCoords,
			turn, MyRandom(rand), startTime, duration,
			dimensions, oldState, score,
			coordPos, botStone, difficulty
		)

		if arr.tail.isEmpty then nState
		else stringToState(arr.tail, Some(nState))
	}



}
