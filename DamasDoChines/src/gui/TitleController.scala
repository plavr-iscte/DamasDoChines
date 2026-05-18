package gui
import gui.FxApp

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import code.code.Main
import code.code.Session
import code.code.State
import code.code.Functions
import code.code.MyRandom
import code.code.Difficulty
import code.code.Stone
import code.code.Engine
import code.code.Score


class TitleController {

	@FXML private var newbtn: Button = _
	@FXML private var loadbtn: Button = _
	@FXML private var state: State = defaultState()
	@FXML private var session: Session = Session(None)

	def initialize():Unit = {
		newbtn.setOnAction(_ => {
			FxApp.changeSceneWithState("Options.fxml", state)
		})

		loadbtn.setOnAction(_ => {
			state = Functions.stringToState(Functions.readFromFile("state.txt").split("\n").toList, Some(state))
			state = state.copy(startTime = Functions.getMillis())
			session = session.setState(state)
			FxApp.changeSceneWithState("Game.fxml", state)
		})
	}

	def defaultState(): State = {
		val rand = MyRandom(Functions.getMillis())
		val start = Functions.getMillis()
		val (board, r, open) = Engine.initboard(6,6, rand)
		State(
			board, Stone.White, open, 1, r, 
			start, start + 100*1000L, (6,6), 
			None, Score(0,0), None, Stone.Black,
			Difficulty.Hard
		)
	}
}
