error id: FF56F81CED19C270F61CB7FA5C2FDAE3
file://<WORKSPACE>/src/Cli.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Ident(Array) is not assigned

occurred in the presentation compiler.



action parameters:
offset: 2057
uri: file://<WORKSPACE>/src/Cli.scala
text:
```scala
	package code

	import scala.io.StdIn
	import scala.annotation.switch

	class Cli {

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
			case "play" + Array[@@] => 
				val engine = Engine()
				engine.play()
			case ""
			case _ => None
		}


		print("Result: " + result)
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
dotty.tools.dotc.ast.Trees$Tree.tpe(Trees.scala:77)
	dotty.tools.dotc.util.Signatures$.applyCallInfo(Signatures.scala:208)
	dotty.tools.dotc.util.Signatures$.computeSignatureHelp(Signatures.scala:104)
	dotty.tools.dotc.util.Signatures$.signatureHelp(Signatures.scala:88)
	dotty.tools.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:46)
	dotty.tools.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:498)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.withNonInterruptableCompiler$$anonfun$1(CompilerAccess.scala:133)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	java.base/java.lang.Thread.run(Thread.java:1583)
```
#### Short summary: 

dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Ident(Array) is not assigned