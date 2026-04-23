package code

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters.*

case class Engine() {



    def play(board: Board,player: Stone,coordFrom: Coord2D,coordTo: Coord2D,lstOpenCoords: List[Coord2D]): (Option[Board], List[Coord2D]) = {
    def validJump: Boolean = {
        val difx = math.abs(coordTo.x - coordFrom.x)
        val dify = math.abs(coordTo.y - coordFrom.y)
        ((difx == 2) && (dify == 0)) || ((difx == 0) && (dify == 2))
    }

    if board.get(coordFrom).contains(player) && validJump then {
        val coordMid = Coord2D((coordTo.x + coordFrom.x) / 2, (coordTo.y + coordFrom.y) / 2)
        if board.get(coordMid).contains(oppositeStone(player)) && lstOpenCoords.contains(coordTo) then {
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

    def oppositeStone(player: Stone): Stone = player match {
        case Stone.Black => Stone.White
        case Stone.White => Stone.Black
    }

    def initboard(x:Int,y:Int,r:MyRandom): (Board,MyRandom,List[Coord2D])={
        val (x_rem,ry) = r.nextInt(x)
        val (y_rem, rd) = ry.nextInt(y)
        val (dir, rfinal) = rd.nextInt(4)
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
                (c,s)::Nil
            else if c==c_rem1 || c==c_rem2 then 
                putStone(c_rem1, c_rem2, new_col, new_lin)
            else 
                (c,s)::putStone(c_rem1, c_rem2, new_col, new_lin)

        }

        val openCoords = List(c1_rem, c2_rem)
        val entries = putStone(c1_rem, c2_rem, y-1,x-1).toMap.par
        (entries.asInstanceOf[Board], rfinal.asInstanceOf[MyRandom], openCoords)

    }

    def playRandomly(board: Board, r: MyRandom, player: Stone, lstOpenCoords: List[Coord2D], f: (List[Coord2D], MyRandom) => (Coord2D, MyRandom)): (Option[Board], MyRandom, List[Coord2D], Option[Coord2D]) = {
        if lstOpenCoords.isEmpty then { (None, r, lstOpenCoords, None) }
        else {
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
                    val (dice, rsN) = rs.nextInt(2)
                    val rsNn = rsN.asInstanceOf[MyRandom]
                    if (dice == 0) {
                        val (recBoard, recR, recOpen, recCoord) = playRandomly(newBoard, rsNn, player, openCoords, f)
                        recBoard match {
                            case Some(_) => (recBoard, recR, recOpen, recCoord)
                            case None    => (Some(newBoard), rsNn, openCoords, Some(target))
                        }
                    } else {
                        (Some(newBoard), rsNn, openCoords, Some(target))
                    }
                case None =>
                    playRandomly(board, rs, player, lstOpenCoords filter (x => x!=target), f)
            }
        }
    }

    def randomMove(lstOpenCoords: List[Coord2D], rand: MyRandom): (Coord2D, MyRandom) = {
        require(lstOpenCoords.nonEmpty, "lstOpenCoords must not be empty")
        val (rnum, nextR) = rand.nextInt(lstOpenCoords.length)
        (lstOpenCoords(rnum), nextR.asInstanceOf[MyRandom])
    }

    

}