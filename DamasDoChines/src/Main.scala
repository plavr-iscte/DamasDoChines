package code

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters.*
import scala.io.StdIn

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
		
		val startTime = System.currentTimeMillis() // Não funcional
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
			None
		)
		
		//Cli(state).showBoard(initialBoard, initialOpenCoords, rowLength, colLength)

		GameTick().onTick(state)


		

		// call onTick

	}

	
	/// Not Functional elements

	def getMillis(): Long = {
		System.currentTimeMillis()
	}

	def getElapsedTime(state: State): String = {
		val totalseconds = (getMillis() - state.startTime) / 1000
		val minutes = totalseconds / 60
		val seconds = totalseconds % 60
		minutes + "m:" + seconds + "s"
	}


	






}
