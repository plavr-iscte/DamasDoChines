package code
object Main  = {
def randomMove(lstOpenCoords: List[Coord2D], rand: MyRandom):(Coord2D, MyRandom) = {
    val (num,nextr)= rand.nextInt
    val rnum = Math.abs(num)%lstOpenCoords.length 
    (lstOpenCoords(rnum),nextr.asInstanceOf[MyRandom])
}

def main (args: Array[String]): Unit ={
    val r = MyRandom(0x5DEECE66DL)
    val c1 = Coord2D(1,2)
    val c2 = Coord2D(2,4)
    val c3 = Coord2D(4,5)
    val (cf, ns) = randomMove(List(c1,c2,c3),r)
    println(cf)
    println(ns)
    println(randomMove(List(c1,c2,c3),ns))
}

def initboard(x:Int,y:Int,r:MyRandom): (b:Board,r:MyRandom)={
    val (x_rem,ry) = Math.abs(r.nextInt)%x
    val (y_rem,rd) = Math.abs(rc.asInstanceOf[MyRandom].nextInt)%y
    val (dir,rfinal) = Math.abs(rd.asInstanceOf[MyRandom].nextInt)%4
    val c1_rem = Coord2D(x_rem,y_rem)
    val c2_rem = match (dir,(x_rem,y_rem)) = {
        case (0,(0,_)) :
            Coord2D(x_rem+1,y_rem)
        case (0,_):
            Coord2D(x_rem-1,y_rem)
        case(1,(x-1,_)):
            Coord2D(x_rem-1,y_rem)
        case (1,_):
            Coord2D(x_rem+1,y_rem)
        case(2,(_,0)):
            Coord2D(x_rem,y_rem+1)
        case (2,_):
            Coord2D(x_rem,y_rem-1)
        case(3,(_,y)):
            Coord2D(x_rem,y_rem-1)
        case (3,_):
            Coord2D(x_rem,y_rem+1)
    }
    
    def putStone(c_rem1: Int,c_rem2: Int,col: Int,lin: Int,b: Boolean):(p: List[Coord2D,Stone]) ={
        if b val s = Stone.Black else val s= Stone.White
        val c= Coord2D(lin ,col)
        (lin,col) match ={
            case (0,0) =>
                if (c_rem1==c || c_rem2=c) return Nil
                    (c,s) 
            case (0,_) =>
                if (c_rem1==c || c_rem2=c) return putStone(col-1,lin-1, !b) 
                    (c,s) :: putStone(col-1,lin-1, !b) 
            case _ =>
                if (c_rem1==c || c_rem2=c) return putStone(col,lin-1, !b) 
                    (c,s) :: putStone(col,lin-1, !b) 
        } 
    }
    Board(putStone(c1_rem,c2_rem,y-1,x-1,True).toMap)

}

def play(board:Board, player: Stone,coordFrom:Coord2D,coordTo:Coord2D,lstOpenCoords:List[Coord2D]):(Option[Board],List[Coord2D]) = {
    def verCoords():(Boolean)={
        val difx = Math.abs(coordTo._1-coordFrom._1)
        val dify = Math.abs(coordTo._2-coordFrom._2)
        ( ( (difx==2) && (dify==0) ) || ( (difx==0) && (dify==2) ) )
    }
    if(player==board.get(coordFrom)&&verCoords()) then        
        val coordMid=(((coordTo._1+coordFroom._1)/2),((coordTo._2+coordFroom._2)/2))
        if(board.get(coordMid)==opositStone(player) && lstOpenCoords.contains(coordTo)) then
            val newB = (((board - coordMid) - coordFrom) + (coordTo -> player))
            val newlst = coordMid :: (coordFrom :: (lstOpenCoords.filterNot(_ == coordTo)))
            (Some(newB),newlst)
    (None,lstOpenCoords)
}

def playRandomly(board:Board, r:MyRandom, player:Stone, lstOpenCoords:List[Coord2D], f:(List[Coord2D],MyRandom)=>(Coord2D,MyRandom)):(Option[Board],MyRandom,List[Coord2D],Option[Coord2D]) = {
    val (rCoord, rs) = f(lstOpenCoords, r)
    val pCoordinates = List(Coord2D(rCoord._1 - 2, rCoord._2), Coord2D(rCoord._1 + 2, rCoord._2), Coord2D(rCoord._1, rCoord._2 - 2), Coord2D(rCoord._1, rCoord._2+2))
    
    @tailrec
    def tryCoordinates(l:List[Coord2D]):(Option[Board],MyRandom,List[Coord2D],Option[Coord2D]):(Option[Board],List[Coord2D]) = l match {
        case Nil =>(None,lstOpenCoords)
        case x :: xs => {
            val (board2, nlstOpenCoords) = play(board, player, x , rCoord , lstOpenCoords)
            board2 match {
                case None => tryCoordinates(xs)
                case Some(_) => (board2,nlstOpenCoords)
            }
        }
    }

    val (fBoard,openCoords) = tryCoordinates(pCoordinates)

    fBoard match {
        case Some(_) => (fBoard, rs, openCoords, Some(rCoord))
        case None => (None, rs, openCoords, None)
    }
    
}