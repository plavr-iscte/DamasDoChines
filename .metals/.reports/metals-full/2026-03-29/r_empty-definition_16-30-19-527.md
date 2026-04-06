error id: file:///C:/Users/migue/Documents/DamasDoChines/DamasDoChines/src/T4.scala:
file:///C:/Users/migue/Documents/DamasDoChines/DamasDoChines/src/T4.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 648
uri: file:///C:/Users/migue/Documents/DamasDoChines/DamasDoChines/src/T4.scala
text:
```scala


/* 
b -> board
lstOpenCoords -> lista de coordenadas disponíveis
numRows -> duh
numCols -> duh

*/
def renderBoard(board:Board, lstOpenCoords:List[Coord2D], numRows:Int, numCols:Int): Unit = {
    let title = 
        "    " +
        range(0, numCols)
            .map(col -> columnLabel)
            .joinWith(" ")

    let row =
        range(0, numRows)
            .map(row -> renderRow(row, board, openCoords, numCols))
            .joinWith("\n")
    header + "\n" + rows
}

def renderRow(row, board, openCoords, numCols) =
    let rowPrefix =
        if row < 10 then row.toString() + "  "
        else row.toStr@@ing()

def cellSymb(coord, board, openCoords:):String = {
    if openCoords.contains(coord) {
        "."
    } else {
        match board.get(coord) {
            case Some(Black)    => "B"
            case Some(White)    => "W"
            case None           => "."
        }
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 