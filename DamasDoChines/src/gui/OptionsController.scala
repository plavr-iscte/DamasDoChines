package gui

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.shape.Circle
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import code.code.Stone
import code.code.State




class OptionsController {
	@FXML private var prevbtn: Button = _
	@FXML private var nextbtn: Button = _
	@FXML private var easybtn: Button = _
	@FXML private var midbtn: Button = _
	@FXML private var hardbtn: Button = _
	@FXML private var txtField: TextField = _
	@FXML private var whiteP: Circle = _
	@FXML private var blackP: Circle = _
	private var selectedStone: Option[Stone] = _
	private var state: State = _

	def setInitialState(s: State): Unit = {
		state = s
	}

	def initialize():Unit = {
		nextbtn.setOnAction(_ => {
			if (txtField.getText.matches(".*\\d.*") && txtField.getText != "" && selectedStone.isDefined ){
				FxApp.changeScene("Game.fxml")
			}
			else{
				txtField.getStyleClass.add("error_time")
			}
			})
		prevbtn.setOnAction(_ => {
			FxApp.changeScene("Title.fxml")
			})
		whiteP.setOnMouseClicked(_ =>{
			selectedStone = Some(Stone.White)
			blackP.getStyleClass.remove("selected_option")
			whiteP.getStyleClass.add("selected_option")
		})
		blackP.setOnMouseClicked(_ =>{
			selectedStone = Some(Stone.Black)
			blackP.getStyleClass.add("selected_option")
			whiteP.getStyleClass.remove("selected_option")
		})
	}
}