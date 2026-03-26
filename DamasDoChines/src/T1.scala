package code
def randomMove(lstOpenCoords: List[Coord2D], rand: MyRandom):(Coord2D, MyRandom) = {
    val (num,nextr)= rand.nextInt
    val rnum = Math.abs(num)%lstOpenCoords.length 
    return (lstOpenCoords(rnum),nextr.asInstanceOf[MyRandom])
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