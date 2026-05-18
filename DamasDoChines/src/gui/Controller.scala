package gui

import code.*
import code.Engine.*
import code.State
import code.Coord2D
import code.MyRandom
import code.Stone
import code.Main
import code.Cli
import code.Score
import code.Board
import code.Session
import code.GameTick
import code.Difficulty
import code.Functions
import javafx.fxml.FXML
import javafx.scene.layout.{GridPane, StackPane}
import code.Main.*
import javafx.scene.shape.Circle

import scala.jdk.CollectionConverters.*
import javafx.scene.input.{ClipboardContent, TransferMode}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.Button
import javafx.scene.control.Label

import javafx.animation.{PauseTransition,AnimationTimer}
import javafx.util.Duration


class Controller {
	@FXML private var tabuleiro: GridPane = _
	@FXML private var B_Quit: Button = _
	@FXML private var B_Restart: Button = _
	@FXML private var B_CTurn: Button = _
	@FXML private var B_Undo: Button = _
	@FXML private var turnL: Label = _
	@FXML private var p_circle: Circle = _
	@FXML private var b_score: Label = _
	@FXML private var w_score: Label = _
	@FXML private var aviso_l: Label = _
	@FXML private var dif_circle: Circle = _
	private var mposx:Double =0
	private var mposy:Double=0
	private var stoneX:Double =0
	private var stoneY:Double =0
	private var gameState: State = _
	private var floatingStone: Option[Circle] = None
	private var selectedCoord: Option[Coord2D] = None
	private var scaleAnim:Double =0
	private var activeBtn:Option[Button] = None
	private var scaleBtn:Double =0
	private var session: Session = Session(None)

	def setInitialState(s: State): Unit = {
		gameState = s
		changeGUI(gameState.board)
		turnL.setText(gameState.turn.toString)
		b_score.setText(gameState.score.black.toString)
		w_score.setText(gameState.score.white.toString)
	}

	val anim:AnimationTimer = new AnimationTimer{
        def handle(now:Long):Unit = {
            floatingStone.foreach(s=>{
                stoneX = stoneX + (mposx - stoneX)*0.1
                stoneY = stoneY + (mposy - stoneY)*0.1
                s.setTranslateX(stoneX - s.getRadius)
                s.setTranslateY(stoneY - s.getRadius)

                scaleAnim=scaleAnim + (20.0 - scaleAnim) * 0.1
                s.setRadius(scaleAnim)
            })

        }
    }


	def initialize(): Unit = {
		if(gameState == null) {
			val rand = MyRandom(Functions.getMillis())
			val start = Functions.getMillis()
			val (board, r, open) = initboard(6,6,rand)
			gameState = State(
				board, Stone.White, open, 1, r,
				start, start+100*1000L, (6,6),
				None, Score(0,0), None,
				Stone.Black, Difficulty.Medium
			)
		}


		//l_id.setText(Functions.getTitle(gameState))

		turnL.setText(""+gameState.turn)
		gameState.player match {
			case Stone.White => p_circle.getStyleClass.add("light_stone")
			case Stone.Black => p_circle.getStyleClass.add("dark_stone")
		}
		b_score.setText(""+gameState.score.black)
		w_score.setText(""+gameState.score.white)

		B_Quit.setOnAction(new EventHandler[ActionEvent] {
			override def handle(event: ActionEvent): Unit = {
				Functions.doQuit()
			}
		})
		B_Undo.setOnAction(new EventHandler[ActionEvent] {
			override def handle(event: ActionEvent): Unit = {
				gameState=getNextState(gameState,getCommand("","undo"))
				changeGUI(gameState.board)
			}
		})
		B_CTurn.setOnAction(new EventHandler[ActionEvent] {
			override def handle(event: ActionEvent): Unit = {
				gameState = getNextState(gameState, getCommand("", "change"))
				changeGUI(gameState.board)

				if(
					gameState.player == gameState.botStone && 
					!gameState.hasEndCondition(Functions.getMillis()) 
				) {
					chainRandomPlayWithDelay()
				}
			}
		})
		B_Restart.setOnAction(new EventHandler[ActionEvent] {
			override def handle(event: ActionEvent): Unit = {
				gameState = getNextState(gameState, getCommand("", "restart"))
				changeGUI(gameState.board)
			}
		})

		
		for (row <- 0 until 6; col <- 0 until 6) {
			val square = new StackPane()

			if ((row + col) % 2 != 0)
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

					val plays = getAllPlaysForCoord(gameState, Coord2D(row, col))
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
						scaleAnim = s.getScaleX
					})
				}

				stoneX = square.getLayoutX + (square.getWidth/2)
				stoneY = square.getLayoutY + (square.getHeight/2)
				anim.start()
				event.consume()
			})


			square.setOnDragOver(event => {
				floatingStone.foreach(s => {
					val mousepos = tabuleiro.sceneToLocal(event.getSceneX, event.getSceneY)
					mposx = mousepos.getX
					mposy = mousepos.getY
				})
				if (event.getGestureSource != square && event.getDragboard.hasString) {
					event.acceptTransferModes(TransferMode.MOVE)
				}
				event.consume()
			})


			square.setOnDragDropped(event => {				
				if(gameState.hasEndCondition(Functions.getMillis())) {
					event.consume()
				}

				square.getStyleClass.remove("highlighted_play")
				val db = event.getDragboard()

				if (db.hasString) {
					val originStr = db.getString
					val coords = originStr.split(",")
					val originRow = coords(0).toInt
					val originCol = coords(1).toInt
					val coordFrom = Coord2D(originRow, originCol)
					val coordTo = Coord2D(row, col)

					if (!gameState.board.get(coordFrom).contains(gameState.player)) {
						event.consume()
					}

					val pColor = gameState.board.get(coordFrom)

					if (pColor.isDefined) {
						val player = pColor.get
						gameState = getNextState(gameState, getCommand("", s"play $originRow $originCol $row $col"))
						floatingStone.foreach(stone => tabuleiro.getChildren.remove(stone))


						changeGUI(gameState.board)
						if (gameState.player == gameState.botStone && !gameState.hasEndCondition(Functions.getMillis())) {
							chainRandomPlayWithDelay()
						}
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
		anim.start()
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
							case Stone.White => vStone.getStyleClass.add("light_stone")
							case Stone.Black => vStone.getStyleClass.add("dark_stone")
						}
						square.getChildren.add(vStone)
				}
			}
		}
		turnL.setText(gameState.turn.toString)
		b_score.setText(gameState.score.black.toString)
		w_score.setText(gameState.score.white.toString)

		dif_circle.getStyleClass.removeAll("easy_diff", "medium_diff", "hard_diff", "extreme_diff")

		gameState.difficulty match {
			case Difficulty.Easy    => dif_circle.getStyleClass.add("easy_diff")
			case Difficulty.Medium  => dif_circle.getStyleClass.add("medium_diff")
			case Difficulty.Hard    => dif_circle.getStyleClass.add("hard_diff")
			case Difficulty.Extreme => dif_circle.getStyleClass.add("extreme_diff")
		}

		if (gameState.hasVictory()) {
			aviso_l.setText(Functions.getVictoryLabel(gameState))
		} else if(gameState.hasEnded(Functions.getMillis())) {
			aviso_l.setText(Functions.getEndLabel(gameState))
		}
	}


	def getSquare(row: Int, col: Int): StackPane = {
		tabuleiro.getChildren.asScala.collectFirst {
			case s: StackPane if GridPane.getRowIndex(s) == row && GridPane.getColumnIndex(s) == col => s
		}.orNull
	}

	def chainRandomPlayWithDelay(): Unit = {
		if (gameState.player != gameState.botStone) return

		val delay = new PauseTransition(Duration.seconds(1))
		delay.setOnFinished(_ => {
		gameState = getNextState(gameState, "pr")

		if (
			gameState.difficulty == Difficulty.Easy &&
			gameState.player == gameState.botStone
		){
			gameState = gameState.changeTurn()
		}

		changeGUI(gameState.board)

		if (
			gameState.difficulty != Difficulty.Easy &&
			gameState.player == gameState.botStone &&
			gameState.coordPos.isDefined &&
			!gameState.hasEndCondition(Functions.getMillis())
		) {
			chainRandomPlayWithDelay()
		}

		})
		delay.play()
	}
}
