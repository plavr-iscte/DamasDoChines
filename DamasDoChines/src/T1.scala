case class Coord2D(x:Int,y:Int) {
    def getx:(Int) ={
        x
    } 
    def gety:(Int) ={
        y
    } 
}
trait Random{   
    def nextInt:(Int,Random)
}
case class MyRandom(seed: Long) extends Random {
    def nextInt: (Int, Random) = {
        val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
        val nextRandom = MyRandom(newSeed)
        val n = (newSeed >>> 16).toInt
        (n, nextRandom)
    }
}

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