/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Introducir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *  - Puntuar comics que se encuentren dentro de la base de datos.
 *  Esta clase permite acceder al menu principal donde se puede viajar a diferentes ventanas, etc.
 *
 *  Version 8.2.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.io.IOException;
import java.sql.SQLException;

import funcionesAuxiliares.Ventanas;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Esta clase es donde se ejecuta la ventana principal del programa.
 *
 * @author Alejandro Rodriguez
 */
public class VentanaPrincipalController extends Application {

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Carga la ventana principal y arranca el programa.
	 *
	 * @param primaryStage La ventana principal (Stage) que se utiliza para mostrar la interfaz de usuario.
	 * @throws IOException      Si ocurre un error al cargar la interfaz de usuario desde el archivo FXML.
	 * @throws SQLException     Si ocurre un error relacionado con la base de datos.
	 */
	@Override
	public void start(Stage primaryStage) throws IOException, SQLException {
	    try {
	        Parent root = FXMLLoader.load(getClass().getResource("/ventanas/AccesoBBDD.fxml"));
	        primaryStage.setScene(new Scene(root));
	        primaryStage.setResizable(false);
	        primaryStage.setTitle("Aplicacion bbdd comics"); // Título de la aplicación.
	        primaryStage.show();
	        primaryStage.getIcons().add(new Image("/Icono/icon2.png"));

	    } catch (IOException e) {
	        nav.alertaException(e.toString());
	    }
	}

	/**
	 * Método principal para iniciar la aplicación.
	 *
	 * @param args Los argumentos de línea de comandos (no se utilizan en esta aplicación).
	 */
	public static void main(String[] args) {
	    launch(args);
	}

}