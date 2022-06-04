package Funcionamiento;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Principal extends Application{

	//Metodo que permite la ejecutcion de la aplicacion.
	public void start(Stage primaryStage) throws IOException 
	{
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Principal.class.getResource("/ventanas/MenuPrincipal.fxml"));
			Pane ventana = (Pane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(ventana);
			primaryStage.setTitle("Aplicacion bbdd comics"); //Titulo de la aplicacion.
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) 
	{
		launch(args);
	}
}