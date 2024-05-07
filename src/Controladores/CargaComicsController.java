/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

import java.net.URL;
import java.util.ResourceBundle;

import funcionesInterfaz.FuncionesManejoFront;
import javafx.application.Platform;

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
 *  Version 8.0.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Controlador para la carga de cómics en una interfaz gráfica JavaFX.
 */
public class CargaComicsController implements Initializable {

	/**
	 * Barra de progreso para mostrar el progreso de carga de cómics.
	 */
	@FXML
	private ProgressBar cargaComics;

	/**
	 * Área de texto para mostrar información sobre la carga de cómics.
	 */
	@FXML
	private TextArea comicsCarga;

	/**
	 * Etiqueta para mostrar el porcentaje de carga de cómics.
	 */
	@FXML
	private Label porcentajeCarga;

	/**
	 * Campo para almacenar la referencia a la ventana (Stage).
	 */
	private Stage stage; // Campo para almacenar la referencia a la ventana

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateProgress(0);
		appendTextToTextArea("");
		updateLabel("");
		Platform.runLater(() -> FuncionesManejoFront.getStageVentanas().add(estadoStage()));
	}

	/**
	 * Actualiza el progreso de la barra de progreso.
	 *
	 * @param progress El progreso de la carga (valor entre 0.0 y 1.0).
	 */
	public void updateProgress(double progress) {
		cargaComics.setProgress(progress);
	}

	/**
	 * Añade texto al área de texto.
	 *
	 * @param text El texto que se va a añadir al área de texto.
	 */
	public void appendTextToTextArea(String text) {
		comicsCarga.appendText(text);

	}

	/**
	 * Actualiza el texto de la etiqueta.
	 *
	 * @param text El nuevo texto que se mostrará en la etiqueta.
	 */
	public void updateLabel(String text) {
		porcentajeCarga.setText(text);
	}

	/**
	 * Establece la referencia a la ventana principal.
	 *
	 * @param stage La ventana principal de la aplicación.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage estadoStage() {

		return (Stage) comicsCarga.getScene().getWindow();
	}

	/**
	 * Pasa datos a los métodos del controlador de la ventana de carga de cómics.
	 *
	 * @param nombreComic El nombre del cómic a mostrar.
	 * @param porcentaje  El porcentaje de carga a mostrar.
	 * @param progreso    El progreso de carga a mostrar.
	 */
	public void cargarDatosEnCargaComics(String nombreComic, String porcentaje, Double progreso) {
		appendTextToTextArea(nombreComic);
		updateLabel(porcentaje);
		updateProgress(progreso);
	}

	/**
	 * Cierra la ventana actual si está abierta.
	 */
	public void closeWindow() {
		if (stage != null) {

			if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
				FuncionesManejoFront.getStageVentanas().remove(estadoStage());
			}

			Stage myStage = (Stage) comicsCarga.getScene().getWindow();
			myStage.close();
		}

	}

}
