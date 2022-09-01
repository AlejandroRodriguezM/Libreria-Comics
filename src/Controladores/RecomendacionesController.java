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
 *
 *  Esta clase permite acceder a la ventana que permite mostrar una recomendacion de comics.
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.util.Random;

import Funcionamiento.Libreria;
import Funcionamiento.NavegacionVentanas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Esta clase sirve para visualizar de forma aleatoria un comic de la base de
 * datos para poder leer
 *
 * @author Alejandro Rodriguez
 */
public class RecomendacionesController {

	@FXML
	private Button botonElegir;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private TextArea printComicRecomendado;

	private static NavegacionVentanas nav = new NavegacionVentanas();

	private static Libreria libreria = new Libreria();

	/**
	 * Llama a funcion que genera una lectura recomendada
	 *
	 * @param event
	 */
	@FXML
	void eligePorMi(ActionEvent event) {
		printComicRecomendado.setOpacity(1);

		printComicRecomendado.setText(generarLectura());

	}

	/**
	 * Funcion que devuelve un comic al azar de toda la base de datos.
	 *
	 * @return
	 */
	public String generarLectura() {
		Random r = new Random();

		int n;

		limpiarPront(); // Llamada a funcion para limpiar las pantalla "TextArea"

		if (libreria.verLibreriaPosesion().length != 0) {
			n = (int) (Math.random() * r.nextInt(libreria.verLibreriaPosesion().length));
			return libreria.verLibreriaPosesion()[n].toString(); // Devuelve un comic de la lista de comics
		} else {
			printComicRecomendado.setText("ERROR. No hay ningun dato en la base de datos");
			printComicRecomendado.setStyle("-fx-background-color: #F53636");
		}

		return "";
	}

	/**
	 * Se limpia el pront de informacion de la ventana
	 */
	public void limpiarPront() {
		printComicRecomendado.setText("");
	}

	/**
	 * Permite salir completamente del programa.
	 *
	 * @param event
	 */
	@FXML
	void salirPrograma(ActionEvent event) {

		if (nav.salirPrograma(event)) {
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	public void volverMenu(ActionEvent event) {

		nav.verMenuPrincipal();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}

	/**
	 * Al cerrar la ventana, se cargara la ventana de verBBDD
	 *
	 */
	public void closeWindows() {

		nav.verMenuPrincipal();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}

}