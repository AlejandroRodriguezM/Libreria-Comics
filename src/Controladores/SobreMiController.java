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
 *  Version 5.3
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.io.IOException;

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
 *  Esta clase permite acceder a la ventana que permite mostrar los datos sobre el creador de la aplicacion
 *
 *  Version 4.0.0.6
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Esta clase sirve para poder mostrar a los usuarios los datos del creador del programa
 *
 * @author Alejandro Rodriguez
 */
public class SobreMiController {

    @FXML
    private MenuItem menu_archivo_desconectar;
	
    @FXML
    private MenuItem menu_archivo_sobreMi;

	@FXML
	private MenuItem menu_archivo_cerrar;
	
	@FXML
	private MenuItem menu_archivo_volver;

	@FXML
	private MenuItem menu_comic_aniadir;

	@FXML
	private MenuItem menu_comic_eliminar;
	
	@FXML
	private MenuItem menu_comic_aleatoria;

	@FXML
	private MenuItem menu_comic_modificar;

	@FXML
	private MenuItem menu_comic_puntuar;

	@FXML
	private MenuBar menu_navegacion;

	@FXML
	private Menu navegacion_cerrar;

	@FXML
	private Menu navegacion_comic;
	
	@FXML
	private Menu navegacion_estadistica;
	
	@FXML
	private Label TextoInfo;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonLinkedin;

	@FXML
	private Button botonVolver;

	@FXML
	private Button botonYoutube;

	@FXML
	private Button numeroVersion;

	@FXML
	private Button botonCompra;
	
	@FXML
	private Button botonOtroProyecto;

	private static Ventanas nav = new Ventanas();

	/**
	 * Funcion que permite abrir un navegador con la url de GitHub
	 *
	 * @param event
	 */
	@FXML
	void accesoGitHub(ActionEvent event) {

		String url = "https://github.com/AlejandroRodriguezM";
		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url);

			}
		}
	}

	/**
	 * Funcion que permite abrir un navegador con la url de twitter
	 *
	 * @param event
	 */
	@FXML
	void accesoLinkedin(ActionEvent event) {

		String url = "https://www.linkedin.com/in/alejandro-rodriguez-mena-497a00179/";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url);
			}
		}
	}

	/**
	 * Funcion que permite abrir un navegador con la url de youtube
	 *
	 * @param event
	 */
	@FXML
	void accesoYoutube(ActionEvent event) {

		String url = "https://www.youtube.com/playlist?list=PL7MV626sbFp6EY0vP8gEEgrVCryitFXCM";
		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url);

			}
		}
	}

	/**
	 * Se llama a funcion que permite abrir 2 direcciones web junto al navegador
	 * predeterminado
	 *
	 * @param event
	 */
	@FXML
	void comprarComic(ActionEvent event) {
		verPagina();
	}
	
	/**
	 * Se llama a funcion que permite abrir 1 direcciones web junto al navegador
	 * predeterminado
	 *
	 * @param event
	 */
	@FXML
	void misOtroProyectos(ActionEvent event) {
		otroProyecto();
	}

	/**
	 * Funcion que permite llamar al navegador predeterminado del sistema y abrir 2
	 * paginas web.
	 */
	public void verPagina() {
		String url1 = "https://www.radarcomics.com/es/";
		String url2 = "https://www.panini.es/shp_esp_es/comics.html";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url1); // Llamada a funcion
			Utilidades.accesoWebWindows(url2); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url1); // Llamada a funcion
				Utilidades.accesoWebLinux(url2); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url1);
				Utilidades.accesoWebMac(url2);
			}
		}
	}
	
	public void otroProyecto() {
		String url = "https://www.comicweb.es";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url);
			}
		}
	}
	
	/////////////////////////////////
	//// METODOS LLAMADA A VENTANAS//
	/////////////////////////////////

	/**
	 * Permite abrir y cargar la ventana para IntroducirDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaAniadir(ActionEvent event) {

		nav.verIntroducirDatos();
	    Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
	    myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana de EliminarDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaEliminar(ActionEvent event) {

		nav.verEliminarDatos();

	    Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
	    myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana de ModificarDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaModificar(ActionEvent event) {

		nav.verModificarDatos();

	    Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
	    myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana deRecomendacionesController
	 *
	 * @param event
	 */
	@FXML
	void ventanaRecomendar(ActionEvent event) {

		nav.verRecomendacion();

	    Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
	    myStage.close();
	}

	/**
	 *
	 * @param event
	 */
	@FXML
	void ventanaPuntuar(ActionEvent event) {

		nav.verPuntuar();

	    Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
	    myStage.close();
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