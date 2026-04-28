package gui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

class HelloWorld extends Application {
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Kōnane GUI")
    val fxmlLoader = new FXMLLoader(getClass.getResource("/gui/Controller.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}

// ESTE É O TEU NOVO MAIN PARA A GUI
object FxApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[HelloWorld], args*)
  }
}
