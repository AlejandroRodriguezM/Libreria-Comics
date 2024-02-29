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
import java.util.Map;
import java.util.ResourceBundle;

import Funcionamiento.FuncionesComboBox;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import alarmas.AlarmaList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Esta clase sirve para configurar los datos de la base de datos para que el
 * programa pueda operar correctamente
 *
 * @author Alejandro Rodriguez
 */
public class OpcionesDatosController implements Initializable {

	/**
	 * Etiqueta de alarma para la conexión.
	 */
	@FXML
	private Label alarmaConexion;

	/**
	 * Etiqueta de alarma para la conexión a Internet.
	 */
	@FXML
	private Label alarmaConexionInternet;

	/**
	 * Etiqueta de alarma para la conexión a la base de datos SQL.
	 */
	@FXML
	private Label alarmaConexionSql;

	/**
	 * Botón para crear una nueva base de datos.
	 */
	@FXML
	private Button botonCrearBBDD;

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
	 * Botón para abrir un archivo.
	 */
	@FXML
	private Button boton_abrir;

	/**
	 * Botón para guardar un archivo.
	 */
	@FXML
	private Button boton_guardar;

	/**
	 * Botón para restaurar un archivo.
	 */
	@FXML
	private Button boton_restaurar;

	/**
	 * Etiqueta para mostrar el nombre del host.
	 */
	@FXML
	private Label etiquetaHost;

	/**
	 * Selector para el nombre de la base de datos.
	 */
	@FXML
	private ComboBox<String> nombreBBDD;

	/**
	 * Campo de texto para ingresar el nombre del host.
	 */
	@FXML
	private TextField nombreHost;

	/**
	 * Etiqueta para mostrar el nombre.
	 */
	@FXML
	private Label nombre_label;

	/**
	 * Campo de contraseña.
	 */
	@FXML
	private PasswordField pass;

	/**
	 * Campo de texto para ingresar la contraseña del usuario.
	 */
	@FXML
	private TextField passUsuarioTextField;

	/**
	 * Etiqueta para mostrar la contraseña.
	 */
	@FXML
	private Label password_label;

	/**
	 * Etiqueta para mostrar el estado del fichero.
	 */
	@FXML
	private Label prontEstadoFichero;

	/**
	 * Etiqueta para mostrar el puerto.
	 */
	@FXML
	private Label puerto_label;

	/**
	 * Campo de texto para ingresar el puerto de la base de datos.
	 */
	@FXML
	private TextField puertobbdd;

	/**
	 * Botón para mostrar u ocultar la contraseña.
	 */
	@FXML
	private ToggleButton toggleEye;

	/**
	 * Imagen del ojo para mostrar u ocultar la contraseña.
	 */
	@FXML
	private ImageView toggleEyeImageView;

	/**
	 * Campo de texto para ingresar el nombre de usuario.
	 */
	@FXML
	private TextField usuario;

	/**
	 * Etiqueta para mostrar el nombre de usuario.
	 */
	@FXML
	private Label usuario_label;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		AlarmaList alarmaList = new AlarmaList();

		alarmaList.setAlarmaConexion(alarmaConexion);
		alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
		alarmaList.setAlarmaConexionSql(alarmaConexionSql);

		alarmaList.iniciarThreadChecker(false);

		Utilidades.crearEstructura();
		AlarmaList.configureEyeToggle(toggleEyeImageView, passUsuarioTextField, pass);
		restringir_entrada_datos();

		formulario_local();

		alarmaList.iniciarAnimacionEspera(prontEstadoFichero);
		AlarmaList.iniciarAnimacionAlarma(alarmaConexion);
	}

	/**
	 * Llena el formulario de configuración local con valores previamente guardados.
	 */
	public void formulario_local() {

		Map<String, String> datosConfiguracion = Utilidades.devolverDatosConfig();

		usuario.setText(datosConfiguracion.get("Usuario"));

		pass.setText(datosConfiguracion.get("Password"));

		puertobbdd.setText(datosConfiguracion.get("Puerto"));

		nombreHost.setText(datosConfiguracion.get("Hosting"));

		rellenarComboDB(datosConfiguracion);
	}

	public void rellenarComboDB(Map<String, String> datosConfiguracion) {
		AlarmaList alarmaList = new AlarmaList();

		String puertoTexto = datosConfiguracion.get("Puerto");
		String databaseTexto = datosConfiguracion.get("Database");
		String hostingTexto = datosConfiguracion.get("Hosting");
				
		if (Utilidades.isMySQLServiceRunning(hostingTexto,puertoTexto)) {

			List<String> opciones = Utilidades.obtenerOpcionesNombreBBDD(datosConfiguracion);
			AlarmaList.detenerAnimacion();
			prontEstadoFichero.setText(null);
			if (!opciones.isEmpty()) {

				alarmaList.iniciarAnimacionEspera(prontEstadoFichero);
				nombreBBDD.getItems().addAll(opciones);
				nombreBBDD.getSelectionModel().selectFirst();
			} else {
				alarmaList.iniciarAnimacionDatosError(prontEstadoFichero,puertoTexto);
				opciones.add("");
			}

			devolverDB(opciones, databaseTexto);
			nombreHost.setEditable(false);
		} else {
			alarmaList.iniciarAnimacionConexionRed(alarmaConexionSql);
		}
	}

	private String devolverDB(List<String> database, String databaseFichero) {
		int selectedIndex = -1;

		for (int i = 0; i < database.size(); i++) {
			String nombreDB = database.get(i);
			if (nombreDB.equalsIgnoreCase(databaseFichero)) {
				selectedIndex = i;
				break;
			}
		}

		List<String> updatedList = new ArrayList<>(database);

		if (selectedIndex != -1) {
			// If found, move the matching item to the front
			String removedItem = updatedList.remove(selectedIndex);
			updatedList.add(0, removedItem);
		}

		nombreBBDD.getItems().setAll(updatedList);

		if (!updatedList.isEmpty()) {
			nombreBBDD.getSelectionModel().selectFirst();
			return nombreBBDD.getSelectionModel().getSelectedItem();
		} else {
			return "";
		}
	}

	/**
	 * Funcion que permite restringir entrada de datos de todo aquello que no sea un
	 * numero entero en los comboBox numeroComic y caja_comic
	 */
	public void restringir_entrada_datos() {
		puertobbdd.setTextFormatter(FuncionesComboBox.validador_Nenteros());
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

		Stage myStage = (Stage) this.botonCrearBBDD.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Guarda los datos en un archivo de configuración y realiza otras tareas
	 * relacionadas.
	 *
	 * @param event el evento que desencadena la acción
	 * @throws SQLException
	 */
	@FXML
	void guardarDatos(ActionEvent event) throws SQLException {

		String datos[] = new String[5];
		datos[0] = puertobbdd.getText();
		datos[1] = nombreBBDD.getSelectionModel().getSelectedItem();
		datos[2] = usuario.getText();
		datos[3] = pass.getText();
		datos[4] = nombreHost.getText();

		Utilidades.guardarDatosBaseLocal(datos, prontEstadoFichero, alarmaConexion);
	}

	/**
	 * Restaura la configuración a los valores predeterminados.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	void restaurarConfiguracion(ActionEvent event) {

		AlarmaList alarmaList = new AlarmaList();

		if (nav.borrarContenidoConfiguracion()) {
			String userHome = System.getProperty("user.home");
			String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
			String carpetaLibreria = ubicacion + File.separator + "libreria";

			// Verificar y eliminar los archivos dentro de la carpeta "libreria"
			File carpetaLibreriaFile = new File(carpetaLibreria);
			File[] archivos = carpetaLibreriaFile.listFiles();
			if (archivos != null) {
				for (File archivo : archivos) {
					if (archivo.isFile()) {
						archivo.delete();
					}
				}
			}

			Utilidades.crearEstructura();

			limpiar_datos();
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
	public void limpiar_datos() {
		usuario.setText("");

		pass.setText("");

		puertobbdd.setText("");

		nombreBBDD.getSelectionModel().clearSelection();
	}

	/**
	 * Vuelve al programa principal desde la ventana actual.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	void volverPrograma(ActionEvent event) {
		nav.verAccesoBBDD(); // Llamada a metodo para abrir la ventana anterior

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Maneja la acción de salida del programa.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {

		if (nav.salirPrograma(event)) { // Llamada a metodo que permite salir completamente del programa
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Cierra el programa a la fuerza correctamente.
	 */
	public void closeWindows() { // Metodo que permite cerrar completamente el programa en caso de cerrar a la //
		// fuerza.
		Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
		myStage.close();
	}

}