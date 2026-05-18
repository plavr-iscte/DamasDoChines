package gui

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage




class OptionsController {

	@FXML private var prevbtn: Button = _
	@FXML private var nextbtn: Button = _
	@FXML private var easybtn: Button = _
	@FXML private var midbtn: Button = _
	@FXML private var hardbtn: Button = _
	def initialize():Unit = {
		nextbtn.setOnAction(_ => {
			FxApp.changeScene("Game.fxml")
			})
		prevbtn.setOnAction(_ => {
			FxApp.changeScene("Title.fxml")
			})
	}
}
