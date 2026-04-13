error id: file://<WORKSPACE>/src/Main.scala:`board_=`.
file://<WORKSPACE>/src/Main.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -scala/collection/parallel/CollectionConverters.board.
	 -scala/collection/parallel/CollectionConverters.board#
	 -scala/collection/parallel/CollectionConverters.board().
	 -board.
	 -board#
	 -board().
	 -scala/Predef.board.
	 -scala/Predef.board#
	 -scala/Predef.board().
offset: 1178
uri: file://<WORKSPACE>/src/Main.scala
text:
```scala
package code

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters.*

object Main {

  def randomMove(lstOpenCoords: List[Coord2D], rand: MyRandom): (Coord2D, MyRandom) = {
    require(lstOpenCoords.nonEmpty, "lstOpenCoords must not be empty")
    val (num, nextR) = rand.nextInt
    val rnum = math.abs(num) % lstOpenCoords.length
    (lstOpenCoords(rnum), nextR.asInstanceOf[MyRandom])
  }

  def main(args: Array[String]): Unit = {
    val numRows = 8
    val numCols = 8
    val maxTurns = 40
    val seed = 0x5DEE6767DL
    val openCoords = Nil
    val player = Stone.White
    val stalledTurns = 0
    val turn = 0

    val startRandom = MyRandom(seed)
    val (fullBoard, r1) = initboard(numRows, numCols, startRandom)

    println(s"Starting game on ${numRows}x${numCols}, maxTurns=$maxTurns, seed=$seed")
    showBoard(fullBoard, openCoords, numRows, numCols)

    while turn <= maxTurns && stalledTurns < 2 do {
      val (nextBoardOpt, nextRand, nextOpenCoords, movedTo) =
        playRandomly(board, rand, player, openCoords, randomMove)
      rand = nextRand

      nextBoardOpt match {
        case Some(nextBoard) =>
          boa@@rd = nextBoard
          openCoords = nextOpenCoords
          stalledTurns = 0
          println(s"Turn $turn - $player moved to ${movedTo.get}")
          showBoard(board, openCoords, numRows, numCols)
        case None =>
          stalledTurns += 1
          println(s"Turn $turn - $player has no legal random move")
      }

      player = oppositeStone(player)
      turn += 1
    }

    println("Game finished.")
  }

  private def oppositeStone(player: Stone): Stone = player match {
    case Stone.Black => Stone.White
    case Stone.White => Stone.Black
  }

def initboard(x:Int,y:Int,r:MyRandom): (b:Board,r:MyRandom)={
    val (x2,ry) = r.nextInt
    val x_rem = Math.abs(x2)%x
    val (y2, rd) = ry.asInstanceOf[MyRandom].nextInt
    val y_rem = Math.abs(y2)%y
    val (dr, rfinal) = rd.asInstanceOf[MyRandom].nextInt
    val dir = Math.abs(dr)%4
    val c1_rem = Coord2D(x_rem,y_rem)
    val c2_rem = (dir,(x_rem,y_rem)) match {
        case (0,(0,_)) =>
            Coord2D(x_rem+1,y_rem)
        case (0,_) =>
            Coord2D(x_rem-1,y_rem)
        case(1,(xr,_)) if xr == x-1=>
            Coord2D(x_rem-1,y_rem)
        case (1,_) =>
            Coord2D(x_rem+1,y_rem)
        case(2,(_,0)) =>
            Coord2D(x_rem,y_rem+1)
        case (2,_) =>
            Coord2D(x_rem,y_rem-1)
        case(3,(_,yr)) if yr == y-1 =>
            Coord2D(x_rem,y_rem-1)
        case (3,_) =>
            Coord2D(x_rem,y_rem+1)
    }
    
    def putStone(
        c_rem1: Coord2D,
        c_rem2: Coord2D,
        col: Int,
        lin: Int
    ):(List[(Coord2D,Stone)]) ={ //changed (..,..)
        val s = if (lin+col) % 2 == 0 then Stone.White else Stone.Black
        val c = Coord2D(lin ,col)

        val (new_lin,new_col) = if col == 0 then (lin-1,y-1) else (lin,col-1)


        if c == Coord2D(0,0) then 
            return (c,s)::Nil
        else if c==c_rem1 || c==c_rem2 then 
            if c == Coord2D(7,4) then println("Else IF " + s)
            return putStone(c_rem1, c_rem2, new_col, new_lin)
        else 
            if c == Coord2D(7,4) then println("Else " +s)
            return (c,s)::putStone(c_rem1, c_rem2, new_col, new_lin)



        /**
        (lin,col) match {
            case (0,0) =>
                if (c==c_rem1 || c==c_rem2) return Nil /// IFs errados c-> Coord2D(lin,col) != c_rem1 or c_rem2
                else (c,s)::Nil
            case (0,_) =>
                if (c == c_rem1 || c==c_rem2) return putStone(c_rem1, c_rem2, col-1,lin-1, !b) /// IFs errados c-> Coord2D(lin,col) != c_rem1 or c_rem2
                else (c,s) :: putStone(c_rem1, c_rem2, col-1, x-1, !b) 
            case _ =>
                if (c == c_rem1 || c==c_rem2) return putStone(c_rem1, c_rem2, col,lin-1, !b)  /// IFs errados c-> Coord2D(lin,col) != c_rem1 or c_rem2
                else (c,s) :: putStone(c_rem1, c_rem2, col,lin-1, !b) 
        } 
        **/
    }
    //val bd= Board(putStone(c1_rem,c2_rem,y-1,x-1,true).toMap.par) ////changed ".par" e True->true

    val entries = putStone(c1_rem, c2_rem, y-1,x-1).toMap.par
    (entries.asInstanceOf[Board], rfinal.asInstanceOf[MyRandom])

}

  def play(
      board: Board,
      player: Stone,
      coordFrom: Coord2D,
      coordTo: Coord2D,
      lstOpenCoords: List[Coord2D]
  ): (Option[Board], List[Coord2D]) = {
    def validJump: Boolean = {
      val difx = math.abs(coordTo.x - coordFrom.x)
      val dify = math.abs(coordTo.y - coordFrom.y)
      ((difx == 2) && (dify == 0)) || ((difx == 0) && (dify == 2))
    }

    if board.get(coordFrom) == player && validJump then {
      val coordMid = Coord2D((coordTo.x + coordFrom.x) / 2, (coordTo.y + coordFrom.y) / 2)
      if board.get(coordMid) == oppositeStone(player) && lstOpenCoords.contains(coordTo) then {
        val newB = (board - coordMid - coordFrom) + (coordTo -> player)
        val newlst = coordMid :: coordFrom :: lstOpenCoords.filterNot(_ == coordTo)
        (Some(newB), newlst)
      } else {
        (None, lstOpenCoords)
      }
    } else {
      (None, lstOpenCoords)
    }
  }

  def playRandomly(board: Board, r: MyRandom, player: Stone, lstOpenCoords: List[Coord2D], f: (List[Coord2D], MyRandom) => (Coord2D, MyRandom)): (Option[Board], MyRandom, List[Coord2D], Option[Coord2D]) = {
        val (target, rs) = f(lstOpenCoords, r)
        val pCoordinates = List(
            Coord2D(target.x - 2, target.y), Coord2D(target.x + 2, target.y), Coord2D(target.x, target.y - 2), Coord2D(target.x, target.y + 2)
        )
        val (fBoard, openCoords) = (pCoordinates foldLeft (None:Option[Board], lstOpenCoords)) {
            case ((None, _), coordFrom) =>
                play(board, player, coordFrom, target, lstOpenCoords)
            case (acc, _) =>
                acc
        }
        fBoard match {
            case Some(newBoard) =>
                val (dice, rsN) = rs.nextInt
                val rsNn = rsN.asInstanceOf[MyRandom]
                val decideToContinue = Math.abs(dice) % 2 == 0
                if (decideToContinue) {
                    val (recBoard, recR, recOpen, recCoord) = playRandomly(newBoard, rsNn, player, openCoords, f)
                    recBoard match {
                        case Some(_) => (recBoard, recR, recOpen, recCoord)
                        case None    => (Some(newBoard), rsNn, openCoords, Some(target))
                    }
                } else {
                    (Some(newBoard), rsNn, openCoords, Some(target))
                }
            case None =>
                (None, rs, lstOpenCoords, None)
        }
    }

  /*******************PEDRO***********************/

  /*
  b -> board
  lstOpenCoords -> lista de coordenadas disponíveis
  numRows -> duh
  numCols -> duh
   */
  def renderBoard(board: Board, lstOpenCoords: List[Coord2D], numRows: Int, numCols: Int): String = {
    val header = getHeader(numCols)
    val body = getBody(board, lstOpenCoords, numRows, numCols)

    header + "\n" + body
  }

  def renderRow(row: Int, board: Board, openCoords: List[Coord2D], numCols: Int): String = {
    val append =
      if row < 10 then
        row.toString + " "
      else row.toString + " "

    val cells =
      (0 until numCols)
        .map(col => cellSymb(Coord2D(row, col), board, openCoords))
        .mkString(" ")

    append + cells
  }

  def cellSymb(coord: Coord2D, board: Board, openCoords: List[Coord2D]): String = {
    if openCoords.contains(coord) then "."
    else
      board.get(coord) match {
        case Some(Stone.Black) => "B"
        case Some(Stone.White) => "W"
        case None              => "."
      }
  }

  def columnLabel(col: Int): String = {
    ('A' + col).toChar.toString
  }

  def getHeader(numCols: Int): String = {
    "  " + (0 until numCols).map(columnLabel).mkString(" ")
  }

  def getBody(board: Board, lstOpenCoords: List[Coord2D], numRows: Int, numCols: Int): String = {
    (0 until numRows).map(row => renderRow(row, board, lstOpenCoords, numCols)).mkString("\n")
  }

  def showBoard(board: Board, openCoords: List[Coord2D], numRows: Int, numCols: Int): Unit = {
    println(renderBoard(board, openCoords, numRows, numCols))
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 