error id: file://<WORKSPACE>/src/gui/Controller.scala:code/Engine.
file://<WORKSPACE>/src/gui/Controller.scala
empty definition using pc, found symbol in pc: code/Engine.
empty definition using semanticdb

found definition using fallback; symbol Engine
offset: 41
uri: file://<WORKSPACE>/src/gui/Controller.scala
text:
```scala
package gui

import code._
import code.En@@gine.*
import code.State
import code.Coord2D.*
import code.MyRandom.*
import code.Stone.*
import code.Main.*
import code.Cli.*
import code.GameTick.*
import javafx.fxml.FXML
import javafx.scene.layout.{ GridPane, StackPane }
import code.Main._
import javafx.scene.shape.Circle
import scala.jdk.CollectionConverters._
import javafx.scene.input.{ ClipboardContent, TransferMode }

class Controller {
  @FXML private var tabuleiro: GridPane = _
  private var gameState: State = _
  private var floatingStone: Option[Circle] = None
  private var selectedCoord: Option[Coord2D] = None

  def initialize(): Unit = {
    val rand = MyRandom(0x5DEE6767DL)
    val(initialBoard, r, lstopen) = initboard(6, 6, rand)
    gameState = State(
      initialBoard,
      Stone.White,
      lstopen,
      1,
      r,
      getMillis(),
      100,
      (6, 6),
      None,
      Score(0, 0),
      None
    )

    for (row <- 0 until 6; col <- 0 until 6) {
      val square = new StackPane()

      if ((row + col) % 2 == 0)
        square.getStyleClass.add("light_square")
      else
        square.getStyleClass.add("dark_square")

      tabuleiro.add(square, col, row)
      square.setOnMouseClicked(_ => {
        val clickedSquare = Coord2D(row, col)
        val clickedStone = square.getChildren.asScala.collectFirst { case c: Circle => c }

        selectedCoord match {
          case None =>
            if (clickedStone.isDefined) {
              clickedStone.get.getStyleClass.add("selected_stone")
              selectedCoord = Some(clickedSquare)
            }
          case Some(oldCoord) =>
            val oldSquare = getSquare(oldCoord.getx, oldCoord.gety)
            if (oldSquare != null) {
              oldSquare.getChildren.asScala.collectFirst { case c: Circle => c }
                .foreach(_.getStyleClass.remove("selected_stone"))
            }
            if (oldCoord == clickedSquare) {
              selectedCoord = None
            } else if (clickedStone.isDefined) {
              clickedStone.get.getStyleClass.add("selected_stone")
              selectedCoord = Some(clickedSquare)
            } else {
              selectedCoord = None
            }
        }
      }
      )

      square.setOnDragDetected(event => {
        val clickedStone = square.getChildren.asScala.collectFirst { case c: Circle => c }
        if (clickedStone.isDefined) {
          val db = square.startDragAndDrop(TransferMode.MOVE)
          val content = new ClipboardContent()
          content.putString(s"$row,$col")

          db.setContent(content)

          val dragStone = clickedStone.get
          square.getChildren.remove(dragStone)

          val plays = getAllPlays(gameState, Coord2D(row, col))
          plays.foreach(s =>
            val square = getSquare(s.getx, s.gety)
            val playCircle = Circle(10)
            playCircle.getStyleClass.add("highlighted_play")
            square.getChildren.add(playCircle)
          )

          tabuleiro.getChildren.add(dragStone)

          dragStone.setMouseTransparent(true)

          floatingStone = Some(dragStone)
          floatingStone.foreach(s=>{
            s.getStyleClass.add("selected_stone")
          })
        }
        event.consume()
      })

      square.setOnDragOver(event => {
        floatingStone.foreach(s => {
          val mousepos = tabuleiro.sceneToLocal(event.getSceneX, event.getSceneY)
          s.setTranslateX(mousepos.getX - 15)
          s.setTranslateY(mousepos.getY - 15)
        })
        if (event.getGestureSource != square && event.getDragboard.hasString) {
          event.acceptTransferModes(TransferMode.MOVE)
        }
        event.consume()
      })

      square.setOnDragDropped(event => {
        square.getStyleClass.remove("highlighted_play")
        val db = event.getDragboard()

        if (db.hasString) {
          val originStr = db.getString
          val coords = originStr.split(",")
          val originRow = coords(0).toInt
          val originCol = coords(1).toInt
          val coordFrom = Coord2D(originRow, originCol)
          val coordTo = Coord2D(row, col)

          val pColor = gameState.board.get(coordFrom)

          if (pColor.isDefined) {
            val player = pColor.get
            gameState = getNextState(gameState, getCommand("", s"play $originRow $originCol $row $col"))
            floatingStone.foreach(stone => tabuleiro.getChildren.remove(stone))

            changeGUI(gameState.board)
          }
        }
        event.consume()
      })

      square.setOnDragDone(event => {
        floatingStone.foreach(stone => {
          if (stone.getParent == tabuleiro) {
            tabuleiro.getChildren.remove(stone)
            stone.setTranslateX(0)
            stone.setTranslateY(0)
            stone.setMouseTransparent(false)
            square.getChildren.add(stone)
          }
        })
        floatingStone = None
        event.consume()
      })

    }

    changeGUI(gameState.board)
  }

  def changeGUI(board: Board): Unit = {
    for(row <- 0 until 6; col <- 0 until 6) {
      val square = getSquare(row, col)
      if (square != null) {
        square.getChildren.removeIf(node => node.isInstanceOf[Circle])
        val coord = Coord2D(row, col)
        board.get(coord).foreach {
          bStone =>
            val vStone = new Circle(15)
            bStone match {
              case Stone.White => vStone.getStyleClass.add("dark_stone")
              case Stone.Black => vStone.getStyleClass.add("light_stone")
            }
            square.getChildren.add(vStone)
        }
      }
    }
  }



  def getSquare(row: Int, col: Int): StackPane = {
    tabuleiro.getChildren.asScala.collectFirst {
      case s: StackPane if GridPane.getRowIndex(s) == row && GridPane.getColumnIndex(s) == col => s
    }.orNull
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: code/Engine.