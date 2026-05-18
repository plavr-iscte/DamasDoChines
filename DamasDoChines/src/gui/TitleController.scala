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


class TitleController {

	@FXML private var newbtn: Button = _
	@FXML private var loadbtn: Button = _
	@FXML private var state: State = _
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

	def init(default: State, initialSession: Session): Unit = {
		session = initialSession
		state = session.getState.getOrElse(default)
	}
}
