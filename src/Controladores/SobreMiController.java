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
import java.net.URL;
import java.util.ResourceBundle;

import alarmas.AlarmaList;
import dbmanager.ConectManager;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesManagment.AccionFuncionesComunes;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Esta clase sirve para poder mostrar a los usuarios los datos del creador del
 * programa
 *
 * @author Alejandro Rodriguez
 */
public class SobreMiController implements Initializable {

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
	 * Elemento del menú para obtener un cómic aleatorio.
	 */
	@FXML
	private MenuItem menu_comic_aleatoria;

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
	 * Etiqueta para mostrar información de texto.
	 */
	@FXML
	private Label TextoInfo;

	/**
	 * Botón para salir de la aplicación.
	 */
	@FXML
	private Button botonSalir;

	/**
	 * Botón para abrir el perfil de LinkedIn.
	 */
	@FXML
	private Button botonLinkedin;

	/**
	 * Botón para volver atrás.
	 */
	@FXML
	private Button botonVolver;

	/**
	 * Botón para abrir el canal de YouTube.
	 */
	@FXML
	private Button botonYoutube;

	/**
	 * Botón para mostrar el número de versión.
	 */
	@FXML
	private Button numeroVersion;

	/**
	 * Botón para realizar una compra.
	 */
	@FXML
	private Button botonCompra;

	/**
	 * Botón para acceder a otro proyecto.
	 */
	@FXML
	private Button botonOtroProyecto;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	@FXML
	private Label alarmaConexionSql;

	private static AlarmaList alarmaList = new AlarmaList();

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.iniciarThreadChecker();
		menu_archivo_cerrar.setGraphic(Utilidades.createIcon("/Icono/Archivo/salir.png", 16, 16));
		Platform.runLater(() -> {
			FuncionesManejoFront.getStageVentanas().add(estadoStage());
			estadoStage().setOnCloseRequest(event -> closeWindows());
		});
	}

	/**
	 * Funcion que permite abrir un navegador con la url de GitHub
	 *
	 * @param event
	 */
	@FXML
	void accesoGitHub(ActionEvent event) {

		String url = "https://github.com/AlejandroRodriguezM";
		abrirEnlace(url);
	}

	/**
	 * Funcion que permite abrir un navegador con la url de twitter
	 *
	 * @param event
	 */
	@FXML
	void accesoLinkedin(ActionEvent event) {

		String url = "https://www.linkedin.com/in/alejandro-rodriguez-mena-497a00179/";
		abrirEnlace(url);
	}

	/**
	 * Funcion que permite abrir un navegador con la url de youtube
	 *
	 * @param event
	 */
	@FXML
	void accesoYoutube(ActionEvent event) {

		String url = "https://www.youtube.com/playlist?list=PL7MV626sbFp6EY0vP8gEEgrVCryitFXCM";
		abrirEnlace(url);

	}

	public void abrirEnlace(String url) {
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

		abrirEnlace(url1);
		abrirEnlace(url2);
	}

	/**
	 * Abre un navegador web y carga la URL especificada en un proyecto externo
	 * relacionado con cómics. La URL predeterminada es "https://www.comicweb.es".
	 * Dependiendo del sistema operativo, se utiliza una función específica para
	 * abrir el navegador.
	 */
	public void otroProyecto() {
		String url = "https://github.com/AlejandroRodriguezM";

		abrirEnlace(url);
	}

	/////////////////////////////////
	//// METODOS LLAMADA A VENTANAS//
	/////////////////////////////////

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	/**
	 * Maneja el evento de desconexión de la base de datos.
	 *
	 * @param event El evento que desencadena la desconexión.
	 * @throws IOException Si ocurre un error de E/S durante la desconexión.
	 */
	@FXML
	public void desconectar(ActionEvent event) throws IOException {
		nav.verAccesoBBDD();
		ConectManager.close();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	public Stage estadoStage() {

		return (Stage) menu_navegacion.getScene().getWindow();
	}

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void volverMenu(ActionEvent event) throws IOException {
		closeWindows();
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {

		if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
			FuncionesManejoFront.getStageVentanas().remove(estadoStage());
		}
		estadoStage().close();

	}

}