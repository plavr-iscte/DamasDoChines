error id: file:///C:/Users/migue/Documents/DamasDoChines/DamasDoChines/src/T1.scala:java/lang/Math#
file:///C:/Users/migue/Documents/DamasDoChines/DamasDoChines/src/T1.scala
empty definition using pc, found symbol in pc: java/lang/Math#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 2266
uri: file:///C:/Users/migue/Documents/DamasDoChines/DamasDoChines/src/T1.scala
text:
```scala
package code
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
        return ((@@Math.abs(coordTo._1-coordFrom._1)&&()))
    }
    def verCap(dir:Int):(Boolean,Board,List[Coord2D])={
        if(player==board.get(coordFrom)&&verCoords()) then        
            val coordMid=(((coordTo._1+coordFroom._1)/2),((coordTo._2+coordFroom._2)/2))
        
    }
}

def playRandomly(board:Board, r:MyRandom, player:Stone, lstOpenCoords:List[Coord2D], f:(List[Coord2D],MyRandom)=>(Coord2D,MyRandom)):(Option[Board],MyRandom,List[Coord2D],Option[Coord2D]) = {
    val (rCoord, rs) = f(lstOpenCoords, r)
    val teste = List(Coord2D(rCoord._1 - 2, rCoord._2), Coord2D(rCoord._1 + 2, rCoord._2), Coord2D(rCoord._1, rCoord._2 - 2), Coord2D(rCoord._1, rCoord._2+))

    val (board2, nlstOpenCoords) = play(board, player, ,rCoord, lstOpenCoords)
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: java/lang/Math#