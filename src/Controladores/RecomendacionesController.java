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
 *  Version 7.0.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.io.IOException;
import java.sql.SQLException;

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

import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Esta clase sirve para visualizar de forma aleatoria un comic de la base de
 * datos para poder leer
 *
 * @author Alejandro Rodriguez
 */
public class RecomendacionesController {

	/**
	 * Elemento del menú para desconectar.
	 */
	@FXML
	private MenuItem menu_archivo_desconectar;

	/**
	 * Elemento del menú para mostrar información sobre el autor.
	 */
	@FXML
	private MenuItem menu_archivo_sobreMi;

	/**
	 * Elemento del menú para cerrar la aplicación.
	 */
	@FXML
	private MenuItem menu_archivo_cerrar;

	/**
	 * Elemento del menú para volver atrás.
	 */
	@FXML
	private MenuItem menu_archivo_volver;

	/**
	 * Elemento del menú para añadir un cómic.
	 */
	@FXML
	private MenuItem menu_comic_aniadir;

	/**
	 * Elemento del menú para eliminar un cómic.
	 */
	@FXML
	private MenuItem menu_comic_eliminar;

	/**
	 * Elemento del menú para modificar un cómic.
	 */
	@FXML
	private MenuItem menu_comic_modificar;

	/**
	 * Elemento del menú para puntuar un cómic.
	 */
	@FXML
	private MenuItem menu_comic_puntuar;

	/**
	 * Barra de menú principal.
	 */
	@FXML
	private MenuBar menu_navegacion;

	/**
	 * Menú para la navegación y cierre.
	 */
	@FXML
	private Menu navegacion_cerrar;

	/**
	 * Menú para la navegación relacionada con cómics.
	 */
	@FXML
	private Menu navegacion_comic;

	/**
	 * Menú para la navegación relacionada con estadísticas.
	 */
	@FXML
	private Menu navegacion_estadistica;

	/**
	 * Botón para elegir un archivo.
	 */
	@FXML
	private Button botonElegir;

	/**
	 * Botón para salir de la aplicación.
	 */
	@FXML
	private Button botonSalir;

	/**
	 * Botón para volver atrás.
	 */
	@FXML
	private Button botonVolver;

	/**
	 * Botón para volver al menú principal.
	 */
	@FXML
	private Button botonVolverMenu;

	/**
	 * Área de texto para mostrar información sobre cómics recomendados.
	 */
	@FXML
	private TextArea printComicRecomendado;

	/**
	 * Imagen de cómic recomendado.
	 */
	@FXML
	private ImageView imagencomic;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de DBLibreriaManager para la gestión de la base de datos.
	 */
	private static DBLibreriaManager libreria = null;

	/**
	 * Instancia de Utilidades para funciones auxiliares.
	 */
	private static Utilidades utilidad = null;


	/**
	 * Llama a funcion que genera una lectura recomendada
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void eligePorMi(ActionEvent event) throws IOException, SQLException {
		printComicRecomendado.setOpacity(1);

		printComicRecomendado.setText(generarLectura());

	}

	/**
	 * Funcion que devuelve un comic al azar de toda la base de datos.
	 *
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public String generarLectura() throws IOException, SQLException {

		Random r = new Random();
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		String ID;

		limpiarPront(); // Llamada a función para limpiar la pantalla "TextArea"

		if (libreria.libreriaCompleta().size() != 0) {
			int n = r.nextInt(libreria.libreriaCompleta().size()); // Generar un número aleatorio dentro del rango
																	// válido

			ID = libreria.libreriaCompleta().get(n).getID();

			imagencomic.setImage(libreria.selectorImage(ID));
			utilidad.deleteImage();
			return libreria.libreriaCompleta().get(n).toString(); // Devuelve un cómic de la lista de cómics
		} else {
			printComicRecomendado.setText("ERROR. No hay ningún dato en la base de datos");
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

	/////////////////////////////////
	//// METODOS LLAMADA A VENTANAS//
	/////////////////////////////////

	/**
	 * Maneja la acción del usuario en relación a los cómics, como agregar, modificar, eliminar o puntuar un cómic.
	 *
	 * @param event El evento de acción que desencadenó la llamada a esta función.
	 */
	@FXML
	void accionComic(ActionEvent event) {

		Object fuente = event.getSource();

		if (fuente instanceof MenuItem) {
			MenuItem menuItemPresionado = (MenuItem) fuente;

			if (menuItemPresionado == menu_comic_aniadir) {
				VentanaAccionController.tipoAccion("aniadir");
			} else if (menuItemPresionado == menu_comic_modificar) {
				VentanaAccionController.tipoAccion("modificar");
			} else if (menuItemPresionado == menu_comic_eliminar) {
				VentanaAccionController.tipoAccion("eliminar");
			} else if (menuItemPresionado == menu_comic_puntuar) {
				VentanaAccionController.tipoAccion("puntuar");
			}
		}

		nav.verAccionComic();
	}

	/**
	 * Metodo que permite abrir la ventana "sobreMiController"
	 *
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {

		nav.verSobreMi();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void desconectar(ActionEvent event) throws IOException {
		nav.verAccesoBBDD();
		DBManager.close();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void volverMenu(ActionEvent event) throws IOException {
		nav.verMenuPrincipal();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Maneja la acción de salida del programa.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {
		// Lógica para manejar la acción de "Salir"
		if (nav.salirPrograma(event)) {
			Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {

		Platform.exit();

	}

}