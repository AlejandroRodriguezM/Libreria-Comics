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
 *  Version 8.0.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import alarmas.AlarmaList;
import dbmanager.ConectManager;
import ficherosFunciones.FuncionesFicheros;
import funcionesAuxiliares.Ventanas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Esta clase sirve para configurar los datos de la base de datos para que el
 * programa pueda operar correctamente
 *
 * @author Alejandro Rodriguez
 */
public class OpcionesDatosController implements Initializable {

	@FXML
	private Label alarmaConexion;

	@FXML
	private Label alarmaConexionInternet;

	@FXML
	private Label alarmaConexionSql;

	@FXML
	private Button botonAbrir;

	@FXML
	private Button botonCrearBBDD;

	@FXML
	private Button botonGuardar;

	@FXML
	private Button botonRestaurar;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private ComboBox<String> nombreBBDD;

	@FXML
	private Label nombreLabel;

	@FXML
	private Label prontEstadoFichero;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();
	private static AlarmaList alarmaList = new AlarmaList();

	private static final String DB_FOLDER = System.getProperty("user.home") + File.separator + "AppData"
			+ File.separator + "Roaming" + File.separator + "libreria" + File.separator;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		alarmaList.setAlarmaConexion(alarmaConexion);
		alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.iniciarThreadChecker();
		FuncionesFicheros.crearEstructura();

		rellenarComboDB();

		AlarmaList.iniciarAnimacionEspera(prontEstadoFichero);
		AlarmaList.iniciarAnimacionAlarma(alarmaConexion);

	}

	/**
	 * Rellena el ComboBox con las bases de datos .db disponibles en el directorio
	 * DB_FOLDER.
	 */
	private void rellenarComboDB() {
		File directorio = new File(DB_FOLDER);
		File[] ficheros = directorio.listFiles();

		if (ficheros != null) {
			List<String> basesDatos = new ArrayList<>();
			for (File fichero : ficheros) {
				if (fichero.isFile() && fichero.getName().toLowerCase().endsWith(".db")) {
					basesDatos.add(fichero.getName());
				}
			}
			// Ordenar los nombres alfabéticamente
			basesDatos.sort(String::compareToIgnoreCase);

			// Agregar las bases de datos al ComboBox
			nombreBBDD.getItems().addAll(basesDatos);

			// Seleccionar la primera opción si existe
			if (!basesDatos.isEmpty()) {
				nombreBBDD.getSelectionModel().select(0);
			}
		} else {
			nombreBBDD.getItems().clear();
		}
	}

	/**
	 * Abre la ubicación de la carpeta "libreria" en el sistema de archivos del
	 * usuario.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	void abrirUbicacion(ActionEvent event) {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming" + File.separator
				+ "libreria";

		File carpeta = new File(ubicacion);

		if (Desktop.isDesktopSupported() && carpeta.exists()) {
			try {
				Desktop.getDesktop().open(carpeta);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Abre la ventana para crear la base de datos.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	void crearBBDD(ActionEvent event) {
		nav.verCrearBBDD();

//		myStage().close();
	}

	/**
	 * Guarda los datos en un archivo de configuración y realiza otras tareas
	 * relacionadas.
	 *
	 * @param event el evento que desencadena la acción
	 * @throws SQLException
	 */
	@FXML
	void guardarDatos(ActionEvent event) {
		String nombredb = nombreBBDD.getValue();

		FuncionesFicheros.guardarDatosBaseLocal(nombredb, prontEstadoFichero, alarmaConexion);
	}

	/**
	 * Restaura la configuración a los valores predeterminados.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	void restaurarConfiguracion(ActionEvent event) {
		if (nav.borrarContenidoConfiguracion()) {
			String userHome = System.getProperty("user.home");
			String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
			String ficheroLocal = ubicacion + File.separator + "libreria" + File.separator + "configuracion_local.conf";

			// Verificar y eliminar los archivos dentro de la carpeta "libreria"
			File ficheroConfiguracionLocal = new File(ficheroLocal);

			ficheroConfiguracionLocal.delete();

			FuncionesFicheros.crearEstructura();

			limpiarDatos();
			AlarmaList.detenerAnimacion();
			prontEstadoFichero.setStyle("-fx-background-color: #f5af2d");
			alarmaList.iniciarAnimacionRestaurado(prontEstadoFichero);
		} else {
			AlarmaList.detenerAnimacion();
			prontEstadoFichero.setStyle("-fx-background-color: #DD370F");
			alarmaList.iniciarAnimacionRestauradoError(prontEstadoFichero);
		}
	}

	/**
	 * Limpia los datos en los campos de texto.
	 */
	public void limpiarDatos() {

		nombreBBDD.getSelectionModel().clearSelection();
	}

	/**
	 * Vuelve al programa principal desde la ventana actual.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	void volverPrograma(ActionEvent event) {

		ConectManager.close();
		closeWindows();
		nav.verAccesoBBDD(); // Llamada a metodo para abrir la ventana anterior
	}

	private Stage myStage() {
		return (Stage) botonSalir.getScene().getWindow();
	}

	/**
	 * Maneja la acción de salida del programa.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {

		if (nav.salirPrograma(event)) { // Llamada a metodo que permite salir completamente del programa
			closeWindows();
		}
	}

	/**
	 * Cierra el programa a la fuerza correctamente.
	 */
	public void closeWindows() {

		myStage().close();
		nav.cerrarMenuCreacionDB();
	}

	public void stop() {
		alarmaList.detenerThreadChecker();
		closeWindows();
	}

}