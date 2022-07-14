package Programa;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xls
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Introducir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase es el programa principal donde se arranca el programa.
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.io.IOException;
import java.sql.SQLException;

import Funcionamiento.NavegacionVentanas;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Programa extends Application {

	private NavegacionVentanas nav = new NavegacionVentanas();

	/**
	 * Carga la ventana principal y arranca el programa.
	 */
	@Override
	public void start(Stage primaryStage) throws IOException, SQLException {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/ventanas/AccesoBBDD.fxml"));
			primaryStage.setScene(new Scene(root));
			primaryStage.setResizable(false);
			primaryStage.setTitle("Aplicacion bbdd comics"); // Titulo de la aplicacion.
			primaryStage.show();
			primaryStage.getIcons().add(new Image("/Icono/icon2.png"));

		} catch (IOException e) {
			nav.alertaException(e.toString());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}