package code.code
import code.code.Random
case class MyRandom(seed: Long) extends Random {
    def nextInt(i: Int): (Int, Random) = {
        val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
        val nextRandom = MyRandom(newSeed)
        val n = Math.abs((newSeed >>> 16).toInt)%i
        (n, nextRandom)
    }
}