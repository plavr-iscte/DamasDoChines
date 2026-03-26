package code
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
        match (lin,col)={
            case (0,0):
                if (c_rem1==c || c_rem2=c) return Nil
                return (c,s) 
            case (0,_):
                if (c_rem1==c || c_rem2=c) return putStone(col-1,lin-1, !b) 
                return (c,s) :: putStone(col-1,lin-1, !b) 
            case _:
                if (c_rem1==c || c_rem2=c) return putStone(col,lin-1, !b) 
                return (c,s) :: putStone(col,lin-1, !b) 
        } 
    }
    return Board(putStone(c1_rem,c2_rem,y-1,x-1,True).toMap)

}