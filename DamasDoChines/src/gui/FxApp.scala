package gui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import code.code.State

class HelloWorld extends Application {

  override def start(primaryStage: Stage): Unit = {
    FxApp.stage = primaryStage
    primaryStage.setTitle("Kōnane GUI")
    FxApp.changeScene("Title.fxml")
    primaryStage.show()
  }

}

object FxApp {

  var stage:Stage = _

  def changeScene(fileName:String):Unit = {
      val path = s"/gui/$fileName"
      val resource = getClass.getResource(path)

      val loader = new FXMLLoader(resource)
      val sceneToLoad:Parent = loader.load()

      if (stage.getScene == null){
        stage.setScene(new Scene(sceneToLoad))
      }
      else{
        stage.getScene.setRoot(sceneToLoad)
      }
  }

  def changeSceneWithState(filename: String, state: State): Unit = {
    val loader = new FXMLLoader(getClass.getResource(s"/gui/$filename"))
    val root = loader.load[javafx.scene.Parent]()

    val c: Any = loader.getController 
    c match {
      case gc: Controller => gc.setInitialState(state)
      case oc: OptionsController => oc.setInitialState(state)
      case _ =>
    }

    stage.setScene(new Scene(root))
    stage.show()
  }

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[HelloWorld], args*)
  }
}
