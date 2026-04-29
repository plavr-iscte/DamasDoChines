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

		recurGT(state)


	}

	def recurGT(state:State): Unit = {
		GameTick().onTick(state)
		val nextState = Engine.getNextState(state, Engine.getCommand("'restart' or 'quit'", ""))
		recurGT(nextState)

	}

	
	/// Elementos não funcionais

	def getMillis(): Long = {
		System.currentTimeMillis()
	}

	def getElapsedTime(state: State): String = {
		val totalseconds = (getMillis() - state.startTime) / 1000
		val minutes = totalseconds / 60
		val seconds = totalseconds % 60
		minutes + "m:" + seconds + "s"
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
