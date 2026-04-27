error id: B15F37312AD4E3B0B7F29AEDBA022757
file://<WORKSPACE>/src/Cli.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.



action parameters:
offset: 1919
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
		val r@@if command.nonEmpty:
			
		val result = StdIn.readLine(prompt + ": ").split("\\s+").toList

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


presentation compiler configuration:
Scala version: 3.8.2-bin-nonbootstrapped
Classpath:
<WORKSPACE>/.bloop/root/bloop-bsp-clients-classes/classes-Metals-0pBo-WedSVmdKfCpv57avQ== [exists ], <HOME>/.cache/bloop/semanticdb/com.sourcegraph.semanticdb-javac.0.11.2/semanticdb-javac-0.11.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.8.2/scala3-library_3-3.8.2.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-parallel-collections_3/1.2.0/scala-parallel-collections_3-1.2.0.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/3.8.2/scala-library-3.8.2.jar [exists ], <WORKSPACE>/.bloop/root/bloop-bsp-clients-classes/classes-Metals-0pBo-WedSVmdKfCpv57avQ==/META-INF/best-effort [missing ]
Options:
-Xsemanticdb -sourceroot <WORKSPACE> -Ywith-best-effort-tasty




#### Error stacktrace:

```
scala.collection.LinearSeqOps.apply(LinearSeq.scala:134)
	scala.collection.LinearSeqOps.apply$(LinearSeq.scala:38)
	scala.collection.immutable.List.apply(List.scala:83)
	dotty.tools.pc.InferCompletionType$.inferType(InferExpectedType.scala:94)
	dotty.tools.pc.InferCompletionType$.inferType(InferExpectedType.scala:62)
	dotty.tools.pc.completions.Completions.advancedCompletions(Completions.scala:543)
	dotty.tools.pc.completions.Completions.completions(Completions.scala:131)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:139)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:197)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.$anonfun$1(CompilerAccess.scala:93)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	java.base/java.lang.Thread.run(Thread.java:1583)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: 0