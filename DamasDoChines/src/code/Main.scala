package code.code

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters.*
import scala.io.StdIn
import code.code.Engine
import code.code.GameTick
import code.code.MyRandom
import code.code.Score
import code.code.State

object Main {

	def main(args: Array[String]): Unit = {

		val seed = 0x5DEE6767DL

		val properties = StdIn.readLine("Set Game Properties (usage [cols] [rows] [duration (sec)]): ").split("\\s+")

		val turn = 1

		val (colLength, rowLength, duration) = 
			properties match {
				case Array(c,r,d) => 
					try{
						(c.toInt, r.toInt, d.toInt)
					} catch {
						case _: NumberFormatException =>
							throw new IllegalArgumentException("Invalid properties.")
					}
				case Array() => (8,8,1200)
				case _ => throw new IllegalArgumentException("Invalid properties.")
			}


		val startRandom = MyRandom(seed)
		val (initialBoard, r1, initialOpenCoords) = Engine.initboard(rowLength, colLength, startRandom)
		
		val startTime = getMillis() // Não funcional
		val endTime = startTime + duration*1000L

		val state = State(
			initialBoard,
			Stone.White,
			initialOpenCoords, 
			turn, 
			startRandom,
			startTime, 
			endTime,
			(colLength, rowLength),
			None,
			Score(0,0),
			None,
		)
		
		//Cli(state).showBoard(initialBoard, initialOpenCoords, rowLength, colLength)

		GameTick().onTick(state)


	}

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
			t+ "\t" + pl + "\t" + cp + "\n" + sc
		else if state.hasEnded(instant) then
			"Esgotou o tempo: " + getElapsedTime(state)
		else
			state.getWinner() match {
				case Some(Stone.White) => "Vencedor: Brancas"
				case Some(Stone.Black) => "Vendecor: Pretas"
				case None => "Empate"
			}
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






}
