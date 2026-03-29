

/* 
b -> board
lstOpenCoords -> lista de coordenadas disponíveis
numRows -> duh
numCols -> duh

*/
def renderBoard(board:Board, lstOpenCoords:List[Coord2D], numRows:Int, numCols:Int):Unit = {
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

def renderRow(row, board, openCoords, numCols):String = {
    let rowPrefix =
        if row < 10 then row.toString() + "  "
        else row.toString() + " "

    let cells =
        range(0, numCols)
            .map(col -> cellSymb((row, col), board, openCoords))
            .joinWith(" ")
    
    rowPrefix + cells
}



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