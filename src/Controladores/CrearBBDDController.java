/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

import java.io.IOException;
import java.net.URL;

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
 *  Esta clase permite acceder a la ventana de creacion de bases de datos
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.ResourceBundle;

import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import alarmas.AlarmaList;
import javafx.animation.Timeline;
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
import javafx.scene.image.Image;
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

	private Image eyeOpenImage;
	private Image eyeClosedImage;

	/**
	 * Línea de tiempo para animaciones.
	 */
	private Timeline timeline;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		detenerAnimacion();
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

		configureEyeToggle();

	}

	private void configureEyeToggle() {
		eyeOpenImage = new Image(getClass().getResourceAsStream("/imagenes/visible.png"), 20, 20, true, true);
		eyeClosedImage = new Image(getClass().getResourceAsStream("/imagenes/hide.png"), 20, 20, true, true);

		// Configurar el ImageView con la imagen de ojo abierto inicialmente
		toggleEyeImageView.setImage(eyeClosedImage);

		// Establecer el manejador de eventos para el ImageView
		toggleEyeImageView.setOnMouseClicked(event -> toggleEye());
	}

	private void toggleEye() {
		if (toggleEyeImageView.getImage() == eyeOpenImage) {
			passUsuarioText.setVisible(false);
			passUsuarioText.setDisable(true);
			passBBDD.setVisible(true);
			passBBDD.setDisable(false);

			passBBDD.setPromptText(passUsuarioText.getPromptText());
			passUsuarioText.setText(passBBDD.getText());
			toggleEyeImageView.setImage(eyeClosedImage); // Cambiar a la imagen de ojo cerrado
		} else {
			passUsuarioText.setVisible(true);
			passUsuarioText.setDisable(false);
			passBBDD.setVisible(false);
			passBBDD.setDisable(true);

			passUsuarioText.setText(passBBDD.getText());
			passBBDD.setPromptText(passUsuarioText.getPromptText());
			toggleEyeImageView.setImage(eyeOpenImage); // Cambiar a la imagen de ojo abierto
		}
	}

	/**
	 * Llena el formulario de configuración local con valores previamente guardados.
	 */
	public void formulario_local() {

		Map<String, String> datosConfiguracion = Utilidades.devolverDatosConfig();

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
		if (datosBBDD() != null && checkDatabaseExists()) {
			createDataBase();
			DBLibreriaManager.createTable();
			Utilidades.crearCarpeta();
			
			AlarmaList.iniciarAnimacionBaseCreada(prontInformativo, DB_NAME);
			Utilidades.guardarDatosBaseLocal(datosBBDD(), prontInformativo, null);
		}
	}

	/**
	 * Funcion que permite crear una base de datos MySql
	 */
	public void createDataBase() {

		String sentenciaSQL = "CREATE DATABASE " + DB_NAME + ";";

		String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "?serverTimezone=UTC";
		Statement statement;
		try {
			Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASS);

			statement = connection.createStatement();
			statement.executeUpdate(sentenciaSQL);

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Comprueba si existe una base de datos con el nombre especificado para la
	 * creación.
	 *
	 * @return true si la base de datos no existe, false si ya existe o si hay un
	 *         error en la conexión
	 */
	public boolean checkDatabaseExists() {
		detenerAnimacion();
		boolean exists = false;
		String sentenciaSQL = "SELECT COUNT(*) FROM information_schema.tables";

		if (!DB_NAME.isEmpty()) {
			sentenciaSQL += " WHERE table_schema = '" + DB_NAME + "'";
		}

		try (ResultSet rs = comprobarDataBase(sentenciaSQL)) {
			int count = rs.getInt("COUNT(*)");
			exists = count < 1;

			if (exists) {
				return true;
			} else {
				prontInformativo.setStyle("-fx-background-color: #DD370F");
				AlarmaList.iniciarAnimacionBaseExiste(prontInformativo, DB_NAME);
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
		return false;
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
	 * Verifica la base de datos ejecutando una sentencia SQL y devuelve el
	 * ResultSet correspondiente.
	 * 
	 * @param sentenciaSQL la sentencia SQL a ejecutar
	 * @return el ResultSet que contiene los resultados de la consulta
	 */
	public ResultSet comprobarDataBase(String sentenciaSQL) {
		String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "?serverTimezone=UTC";
		Statement statement;

		try {
			Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASS);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = statement.executeQuery(sentenciaSQL);

			if (rs.next()) {
				return rs;
			}
		} catch (SQLException e) {
			e.printStackTrace();

			nav.alertaException("ERROR. Revisa los datos del fichero de conexion.");
		}

		return null;
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

	/**
	 * Detiene la animación actual si está en ejecución.
	 */
	private void detenerAnimacion() {
		if (timeline != null) {
			timeline.stop();
			timeline = null; // Destruir el objeto timeline
		}
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
