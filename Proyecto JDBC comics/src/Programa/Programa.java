package Programa;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Programa extends Application {

	/**
	 * Carga la ventana principal y arranca el programa.
	 */
	public void start(Stage primaryStage) throws IOException, SQLException {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/ventanas/MenuPrincipal.fxml"));
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("Aplicacion bbdd comics"); // Titulo de la aplicacion.
			primaryStage.show();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}