package gui

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.shape.Circle
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import code.code.Stone
import code.code.State
import code.code.Difficulty
import code.code.Functions
import code.code.Engine




class OptionsController {
	@FXML private var prevbtn: Button = _
	@FXML private var nextbtn: Button = _
	@FXML private var easyD: Circle = _
	@FXML private var midD: Circle = _
	@FXML private var hardD: Circle = _
	@FXML private var extremeD: Circle = _
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
				val difficulty = 
					if(hardD.getStyleClass.contains("selected_option")) Difficulty.Hard
					else if (midD.getStyleClass.contains("selected_option")) Difficulty.Medium
					else if (extremeD.getStyleClass.contains("selected_option")) Difficulty.Extreme
					else Difficulty.Easy

				val secs = txtField.getText.toInt
				val now = Functions.getMillis()

				if (state == null) return

				state = state.copy(
					botStone = Engine.oppositeStone(selectedStone.get),
					difficulty = difficulty,
					startTime = now,
					duration = now + secs*1000L,
				)

				FxApp.changeSceneWithState("Game.fxml", state)
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

		easyD.setOnMouseClicked(_ =>{
            easyD.getStyleClass.add("selected_option")
            midD.getStyleClass.remove("selected_option")
            hardD.getStyleClass.remove("selected_option")
            extremeD.getStyleClass.remove("selected_option")
        })
		midD.setOnMouseClicked(_ =>{
			easyD.getStyleClass.remove("selected_option")
			midD.getStyleClass.add("selected_option")
			hardD.getStyleClass.remove("selected_option")
			extremeD.getStyleClass.remove("selected_option")
		})
		hardD.setOnMouseClicked(_ =>{
			easyD.getStyleClass.remove("selected_option")
			midD.getStyleClass.remove("selected_option")
			hardD.getStyleClass.add("selected_option")
			extremeD.getStyleClass.remove("selected_option")
		})
		extremeD.setOnMouseClicked(_ =>{
			easyD.getStyleClass.remove("selected_option")
			midD.getStyleClass.remove("selected_option")
			hardD.getStyleClass.remove("selected_option")
			extremeD.getStyleClass.add("selected_option")
		})
	}
}