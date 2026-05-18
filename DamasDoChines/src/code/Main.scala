package code.code

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters.*
import scala.io.StdIn
import scala.io.Source
import java.nio.file.{Files, Paths, StandardOpenOption}
import code.code.Engine
import code.code.GameTick
import code.code.MyRandom
import code.code.Score
import code.code.State
import code.code.Board
import code.code.Functions

object Main {

	def main(args: Array[String]): Unit = {

		val seed = Functions.getMillis()

		val properties = 
			StdIn.readLine("Set Game Properties (usage [cols] [rows] [duration (sec)] [difficulty: Easy|Medium|Hard|Extreme] [player: White|Black]): ").split("\\s+")

		val turn = 1

		val (colLength, rowLength, duration, difficulty, player) = 
			properties match {
				case Array(c,r,d,dif,p) => 
					try{
						val difficulty = dif match {
							case "Easy" => Difficulty.Easy
							case "Medium" => Difficulty.Medium
							case "Hard" => Difficulty.Hard
							case "Extreme" => Difficulty.Extreme
						}
						val player = p match {
							case "White" => Stone.White
							case "Black" => Stone.Black
						}
						(c.toInt, r.toInt, d.toInt, difficulty, player)
					} catch {
						case _: NumberFormatException =>
							throw new IllegalArgumentException("Invalid properties.")
					}
				case Array() => (8,8,1200, Difficulty.Easy, Stone.White)
				case _ => throw new IllegalArgumentException("Invalid properties.")
			}


		val startRandom = MyRandom(seed)
		val (initialBoard, r1, initialOpenCoords) = Engine.initboard(rowLength, colLength, startRandom)
		
		val startTime = Functions.getMillis() // Não funcional
		val endTime = startTime + duration*1000L

		val state = State(
			initialBoard,
			player,
			initialOpenCoords, 
			turn, 
			startRandom,
			startTime, 
			endTime,
			(colLength, rowLength),
			None,
			Score(0,0),
			None,
			Engine.oppositeStone(player),
			difficulty,
		)
		

		//Cli(state).showBoard(initialBoard, initialOpenCoords, rowLength, colLength)

		GameTick().onTick(state)


	}

	
	//Main.stringToState(Main.readFromFile("state.txt").split("\n"))


}
