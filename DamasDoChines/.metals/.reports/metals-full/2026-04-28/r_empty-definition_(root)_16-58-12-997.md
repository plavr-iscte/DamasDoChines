file://<WORKSPACE>/src/gui/FxApp.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:
	 -javafx.
	 -scala/Predef.javafx.
offset: 127
uri: file://<WORKSPACE>/src/gui/FxApp.scala
text:
```scala
package gui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import jav@@afx.stage.Stage

class HelloWorld extends Application {
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Kōnane GUI")
    val fxmlLoader = new FXMLLoader(getClass.getResource("Controller.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}

// ESTE É O TEU NOVO MAIN PARA A GUI
object FxApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[HelloWorld], args: _*)
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 