package gui
import gui.FxApp

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage


class TitleController {

	@FXML private var newbtn: Button = _
	@FXML private var loadbtn: Button = _

	def initialize():Unit = {
		newbtn.setOnAction(_ => {
			FxApp.changeScene("Options.fxml")
			})
	}
}
