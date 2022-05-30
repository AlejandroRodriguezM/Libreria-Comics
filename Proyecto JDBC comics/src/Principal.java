import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Principal extends Application{
	
	//Metodo que permite la ejecutcion de la aplicacion.
		public void start(Stage primaryStage) throws IOException 
		{
			Parent root = FXMLLoader.load(getClass().getResource("gui.fxml")); //Obtenemos el diseño en la ruta y fichero ya creado.
			primaryStage.setScene(new Scene(root)); //Creamos una escena para poder usar la aplicacion.
			primaryStage.setTitle("Aplicacion bbdd comics"); //Titulo de la aplicacion.
			primaryStage.show(); //Mostramos la aplicacion.

		}
		public static void main(String[] args) 
		{
			launch(args);
		}

}
