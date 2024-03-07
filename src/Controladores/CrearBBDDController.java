/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

import Funcionamiento.FuncionesFicheros;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import alarmas.AlarmaList;
import dbmanager.DatabaseManagerDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

/**
 * Esta clase sirve para crear una base de datos donde poder tratar nuestra
 * libreria
 *
 * @author Alejandro Rodriguez
 */
public class CrearBBDDController implements Initializable {

	/**
	 * Botón utilizado para crear la base de datos.
	 */
	@FXML
	private Button botonCrearBBDD;

	/**
	 * Botón utilizado para limpiar los datos de la interfaz.
	 */
	@FXML
	private Button botonLimpiarDatos;

	/**
	 * Botón utilizado para salir de la interfaz.
	 */
	@FXML
	private Button botonSalir;

	/**
	 * Botón utilizado para volver atrás en la interfaz.
	 */
	@FXML
	private Button botonVolver;

	/**
	 * Etiqueta para mostrar el nombre del host.
	 */
	@FXML
	private Label etiquetaHost;

	/**
	 * Campo de texto para ingresar el nombre de la base de datos.
	 */
	@FXML
	private TextField nombreBBDD;

	/**
	 * Campo de texto para ingresar el nombre del host.
	 */
	@FXML
	private TextField nombreHost;

	/**
	 * Campo de contraseña para ingresar la contraseña de la base de datos.
	 */
	@FXML
	private PasswordField passBBDD;

	/**
	 * Campo de texto para ingresar la contraseña del usuario.
	 */
	@FXML
	private TextField passUsuarioText;

	/**
	 * Etiqueta para mostrar mensajes informativos.
	 */
	@FXML
	private Label prontInformativo;

	/**
	 * Campo de texto para ingresar el puerto de la base de datos.
	 */
	@FXML
	private TextField puertoBBDD;

	/**
	 * Botón de alternancia para mostrar/ocultar la contraseña.
	 */
	@FXML
	private ToggleButton toggleEye;

	/**
	 * Imagen para mostrar/ocultar la contraseña.
	 */
	@FXML
	private ImageView toggleEyeImageView;

	/**
	 * Campo de texto para ingresar el nombre de usuario de la base de datos.
	 */
	@FXML
	private TextField userBBDD;

	/**
	 * Controlador para la navegación entre ventanas.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Variables para almacenar información de la base de datos.
	 */
	public static String DB_USER;
	public static String DB_PASS;
	public static String DB_PORT;
	public static String DB_NAME;
	public static String DB_HOST;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AlarmaList.detenerAnimacion();
		AlarmaList.iniciarAnimacionEsperaCreacion(prontInformativo);

		TextFormatter<Integer> textFormatterAni = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});
		puertoBBDD.setTextFormatter(textFormatterAni);

		formulario_local();

		AlarmaList.configureEyeToggle(toggleEyeImageView, passUsuarioText, passBBDD);

	}

	/**
	 * Llena el formulario de configuración local con valores previamente guardados.
	 */
	public void formulario_local() {

		Map<String, String> datosConfiguracion = FuncionesFicheros.devolverDatosConfig();

		userBBDD.setText(datosConfiguracion.get("Usuario"));

		passBBDD.setText(datosConfiguracion.get("Password"));

		puertoBBDD.setText(datosConfiguracion.get("Puerto"));

		nombreHost.setText(datosConfiguracion.get("Hosting"));

	}

	/**
	 * Funcion que guarda los datos de la nueva base de datos.
	 */
	public String[] datosBBDD() {

		String datos[] = new String[5];
		DB_PORT = puertoBBDD.getText();
		DB_NAME = nombreBBDD.getText();
		DB_USER = userBBDD.getText();
		DB_PASS = passBBDD.getText();
		DB_HOST = selectorHost();

		datos[0] = DB_PORT;
		datos[1] = DB_NAME;
		datos[2] = DB_USER;
		datos[3] = DB_PASS;
		datos[4] = DB_HOST;

		if (!comprobarEntradas()) {
			return datos;
		}
		return null;
	}

	/**
	 * Funcion que permite comprobar si las entradas estan rellenas o no.
	 * 
	 * @return
	 */
	public boolean comprobarEntradas() {
		String errorMessage = "";

		if (DB_PORT.isEmpty()) {
			errorMessage += "El puerto de la base de datos está vacío.\n";
		}

		if (DB_NAME.isEmpty()) {
			errorMessage += "El nombre de la base de datos está vacío.\n";
		}

		if (DB_USER.isEmpty()) {
			errorMessage += "El usuario de la base de datos está vacío.\n";
		}

		if (DB_HOST.isEmpty()) {
			errorMessage += "El host de la base de datos está vacío.\n";
		}

		if (!errorMessage.isEmpty()) {
			prontInformativo.setStyle("-fx-background-color: #DD370F");
			AlarmaList.iniciarAnimacionBaseError(errorMessage, prontInformativo);
			return true;
		}
		return false;
	}

	/**
	 * Metodo que permite seleccionar un host online o el publico.
	 * 
	 * @param event
	 */
	@FXML
	void selectorBotonHost(ActionEvent event) {
		selectorHost();
	}

	/**
	 * Metodo que permite llamada a metodos donde se crean la bbdd y las tablas y
	 * procedimientos almacenados
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void crearBBDD(ActionEvent event) throws IOException, SQLException {

		if (datosBBDD() != null && DatabaseManagerDAO.checkDatabaseExists(prontInformativo, DB_NAME)) {
			DatabaseManagerDAO.createDataBase();
			String port = puertoBBDD.getText(); // Reemplaza con el valor deseado
			String dbName = nombreBBDD.getText(); // Reemplaza con el valor deseado
			String userName = userBBDD.getText(); // Reemplaza con el valor deseado
			String password = passBBDD.getText(); // Reemplaza con el valor deseado
			String host = nombreHost.getText(); // Reemplaza con el valor deseado

			String[] datos = { port, dbName, userName, password, host };
			if (Utilidades.isMySQLServiceRunning(host, port)) {
				DatabaseManagerDAO.createTable(datos);
				Utilidades.crearCarpeta();

				AlarmaList.iniciarAnimacionBaseCreada(prontInformativo, DB_NAME);
				FuncionesFicheros.guardarDatosBaseLocal(datosBBDD(), prontInformativo, null);
			} else {
				AlarmaList.manejarErrorConexion("El servicio MySQL no esta activado. Activalo para crear la base de datos", prontInformativo);
			}

		}
	}

	/**
	 * Funcion que permite conectarse a un host online o usar el local.
	 *
	 * @return
	 */
	public String selectorHost() {

		String host = nombreHost.getText();
		return host;
	}

	/**
	 * Limpia los datos en pantalla
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {

		userBBDD.setText("");
		passBBDD.setText("");
		puertoBBDD.setText("");
		nombreBBDD.setText("");
	}

	/////////////////////////////////
	//// METODO LLAMADA A VENTANA//
	/////////////////////////////////

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void volverMenu(ActionEvent event) {

		nav.verOpciones(); // Llamada a metodo para abrir la ventana anterior

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite salir completamente del programa.
	 *
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {

		if (nav.salirPrograma(event)) {
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 */
	public void closeWindows() {

		Platform.exit();
	}

}
