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

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Funcionamiento.Ventanas;
import JDBC.DBManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

/**
 * Esta clase sirve para configurar los datos de la base de datos para que el
 * programa pueda operar correctamente
 *
 * @author Alejandro Rodriguez
 */
public class OpcionesDatosController implements Initializable {

	@FXML
	private Label password_label;

	@FXML
	private Label puerto_label;

	@FXML
	private Label nombre_label;

	@FXML
	private Label host_label;

	@FXML
	private Label etiquetaHost;

	@FXML
	private Label prontEstadoFichero;

	@FXML
	private Button botonCrearBBDD;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Button boton_abrir;

	@FXML
	private Button boton_guardar;

	@FXML
	private Button boton_restaurar;

	@FXML
	private ToggleGroup estado;

	@FXML
	private ComboBox<String> nombreBBDD;

	@FXML
	private TextField nombreHost;

	@FXML
	private PasswordField pass;

	@FXML
	private TextField puertobbdd;

	@FXML
	private TextField usuario;

	private Timeline timeline;

	@FXML
	private ComboBox<String> tipoServidorSwitch;

	private static Ventanas nav = new Ventanas();
	private static AccesoBBDDController acceso = new AccesoBBDDController();

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		iniciarAnimacionEspera();
		restringir_entrada_datos();
		acceso.crearEstructura();

		rellenarComboBox();
		formulario_local(); // Mostrar formulario local por defecto

		tipoServidorSwitch.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

			// Actualizar la pantalla en tiempo real
			Platform.runLater(() -> {
				if (newValue.equals("Local")) {
					limpiar_datos();
					formulario_local();
					iniciarAnimacionEspera();
				} else if (newValue.equals("Online")) {
					limpiar_datos();
					formulario_online();
					iniciarAnimacionEspera();
				}

				actualizarComboBoxNombreBBDD();
			});

		});

		// Escuchador para el campo de texto "usuario"
		usuario.textProperty().addListener((observable, oldValue, newValue) -> {
			Platform.runLater(() -> {
				actualizarComboBoxNombreBBDD();
			});
		});

		// Escuchador para el campo de texto "password"
		pass.textProperty().addListener((observable, oldValue, newValue) -> {
			Platform.runLater(() -> {
				actualizarComboBoxNombreBBDD();
			});
		});

		// Escuchador para el campo de texto "puerto"
		puertobbdd.textProperty().addListener((observable, oldValue, newValue) -> {
			Platform.runLater(() -> {
				actualizarComboBoxNombreBBDD();
			});
		});

		// Escuchador para el campo de texto "nombreHost"
		nombreHost.textProperty().addListener((observable, oldValue, newValue) -> {
			Platform.runLater(() -> {
				actualizarComboBoxNombreBBDD();
			});
		});
	}

	/**
	 * Permite rellenar los datos de los comboBox con los datos de las listas
	 */
	public void rellenarComboBox() {
		ObservableList<String> tipoServidor = FXCollections.observableArrayList("Local", "Online");
		tipoServidorSwitch.setItems(tipoServidor);
		tipoServidorSwitch.getSelectionModel().selectFirst();
	}

	public void formulario_local() {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion_local.conf";

		try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfiguracion))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Usuario: ")) {
					String usuarioTexto = line.substring("Usuario: ".length());
					usuario.setText(usuarioTexto);
				} else if (line.startsWith("Password: ")) {
					String passwordTexto = line.substring("Password: ".length());
					pass.setText(passwordTexto);
				} else if (line.startsWith("Puerto: ")) {
					String puertoTexto = line.substring("Puerto: ".length());
					puertobbdd.setText(puertoTexto);
				} else if (line.startsWith("Database: ")) {
					String databaseTexto = line.substring("Database: ".length());
					nombreBBDD.getSelectionModel().select(databaseTexto);
				} else if (line.startsWith("Hosting: ")) {
//					String hostingTexto = line.substring("Hosting: ".length());
					nombreHost.setText("localhost");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		actualizarComboBoxNombreBBDD();
		nombreHost.setEditable(false);
	}

	public void formulario_online() {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion_online.conf";
		nombreHost.setText("");
		try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfiguracion))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Usuario: ")) {
					String usuarioTexto = line.substring("Usuario: ".length());
					usuario.setText(usuarioTexto);
				} else if (line.startsWith("Password: ")) {
					String passwordTexto = line.substring("Password: ".length());
					pass.setText(passwordTexto);
				} else if (line.startsWith("Puerto: ")) {
					String puertoTexto = line.substring("Puerto: ".length());
					puertobbdd.setText(puertoTexto);
				} else if (line.startsWith("Database: ")) {
					String databaseTexto = line.substring("Database: ".length());
					nombreBBDD.getSelectionModel().select(databaseTexto);
				} else if (line.startsWith("Hosting: ")) {
					String hostingTexto = line.substring("Hosting: ".length());
					nombreHost.setText(hostingTexto);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		actualizarComboBoxNombreBBDD();
		nombreHost.setEditable(true);
	}

	private void actualizarComboBoxNombreBBDD() {
		String usuario = this.usuario.getText();
		String password = this.pass.getText();
		String puerto = this.puertobbdd.getText();
		String hosting = this.nombreHost.getText();

		if (usuario.isEmpty() || password.isEmpty() || puerto.isEmpty() || hosting.isEmpty()) {
			nombreBBDD.getSelectionModel().clearSelection();
			nombreBBDD.setDisable(true);
			nombreBBDD.setEditable(false);
		} else {
			nombreBBDD.getItems().clear();
			nombreBBDD.setDisable(false);

			// Lógica para obtener las opciones del ComboBox nombreBBDD
			List<String> opciones = obtenerOpcionesNombreBBDD(usuario, password, puerto, hosting);

			if (!opciones.isEmpty()) {
				nombreBBDD.getItems().addAll(opciones);
				nombreBBDD.getSelectionModel().selectFirst();
			}
		}
	}

	private List<String> obtenerOpcionesNombreBBDD(String usuario, String password, String puerto, String hosting) {
		List<String> opciones = new ArrayList<>();

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			// Validar los datos de conexión

			if (validarDatosConexion(usuario, password, puerto, hosting)) {
				iniciarAnimacionEspera();

				// Establecer conexión
				String url = "jdbc:mysql://" + hosting + ":" + puerto + "/";
				connection = DriverManager.getConnection(url, usuario, password);

				// Crear una sentencia SQL
				statement = connection.createStatement();

				// Ejecutar consulta para obtener las bases de datos
				resultSet = statement.executeQuery("SHOW DATABASES");

				// Agregar los nombres de las bases de datos a la lista de opciones
	            while (resultSet.next()) {
	                String nombreBD = resultSet.getString(1);
	                String urlBD = url + nombreBD;
	                Connection dbConnection = DriverManager.getConnection(urlBD, usuario, password);
	                Statement dbStatement = dbConnection.createStatement();
	                ResultSet dbResultSet = dbStatement.executeQuery("SHOW TABLES LIKE 'comicsbbdd'");
	                if (dbResultSet.next()) {
	                    opciones.add(nombreBD);
	                }
	                dbResultSet.close();
	                dbStatement.close();
	                dbConnection.close();
	            }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Cerrar recursos
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return opciones;
	}

	private boolean validarDatosConexion(String usuario, String password, String puerto, String hosting) {
		try {

			if (tipoServidorSwitch.getSelectionModel().getSelectedItem().equals("online")) {
				return true;
			}

			String url = "jdbc:mysql://" + hosting + ":" + puerto + "/";
			Connection connection = DriverManager.getConnection(url, usuario, password);
			connection.close(); // Cerrar la conexión

			detenerAnimacion();
			prontEstadoFichero.setText(null);
			return true; // La conexión se estableció correctamente

		} catch (SQLException e) {
			nombreBBDD.setEditable(false);
			iniciarAnimacionDatosError(puerto);
		}

		return false; // La conexión no se pudo establecer
	}

	/**
	 * Funcion que permite restringir entrada de datos de todo aquello que no sea un
	 * numero entero en los comboBox numeroComic y caja_comic
	 */
	public void restringir_entrada_datos() {
		puertobbdd.setTextFormatter(validador_Nenteros());
	}

	public TextFormatter<Integer> validador_Nenteros() {
		// Crear un validador para permitir solo números enteros
		TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			if (change.getControlNewText().matches("\\d*")) {
				return change;
			}
			return null;
		});

		return textFormatter;
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
		boolean esLocal = tipoServidorSwitch.getSelectionModel().getSelectedItem().equals("Local");

		if (esLocal) {
			guardar_datos_base_local();
		} else {
			guardar_datos_base_online();
		}

	}

	public void guardar_datos_base_local() throws SQLException {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String carpetaBackup = carpetaLibreria + File.separator + nombreBBDD.getSelectionModel().getSelectedItem()
				+ File.separator + "backups";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion_local.conf";

		try {
			if (verificarDatos()) {
				acceso.crearEstructura();

				FileWriter fileWriter = new FileWriter(archivoConfiguracion);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

				bufferedWriter.write("###############################");
				bufferedWriter.newLine();
				bufferedWriter.write("Fichero de configuracion de la libreria");
				bufferedWriter.newLine();
				bufferedWriter.write("###############################");
				bufferedWriter.newLine();
				bufferedWriter.write("Usuario: " + usuario.getText());
				bufferedWriter.newLine();
				bufferedWriter.write("Password: " + pass.getText());
				bufferedWriter.newLine();
				bufferedWriter.write("Puerto: " + puertobbdd.getText());
				bufferedWriter.newLine();
				bufferedWriter.write("Database: " + nombreBBDD.getSelectionModel().getSelectedItem());
				bufferedWriter.newLine();
				bufferedWriter.write("Hosting: " + nombreHost.getText());
				bufferedWriter.newLine();

				bufferedWriter.close();

				File carpeta_backupsFile = new File(carpetaBackup);
				if (!carpeta_backupsFile.exists()) {
					carpeta_backupsFile.mkdir();
				}

				prontEstadoFichero.setStyle("-fx-background-color: #A0F52D");
				iniciarAnimacionConectado();
			} else {
				detenerAnimacion();
				prontEstadoFichero.setStyle("-fx-background-color: #DD370F");
				iniciarAnimacionBBDDError();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void guardar_datos_base_online() throws SQLException {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String carpetaBackup = carpetaLibreria + File.separator + nombreBBDD.getSelectionModel().getSelectedItem()
				+ File.separator + "backups";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion_online.conf";

		try {
			if (verificarDatos()) {
				acceso.crearEstructura();

				FileWriter fileWriter = new FileWriter(archivoConfiguracion);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

				bufferedWriter.write("###############################");
				bufferedWriter.newLine();
				bufferedWriter.write("Fichero de configuracion de la libreria");
				bufferedWriter.newLine();
				bufferedWriter.write("###############################");
				bufferedWriter.newLine();
				bufferedWriter.write("Usuario: " + usuario.getText());
				bufferedWriter.newLine();
				bufferedWriter.write("Password: " + pass.getText());
				bufferedWriter.newLine();
				bufferedWriter.write("Puerto: " + puertobbdd.getText());
				bufferedWriter.newLine();
				bufferedWriter.write("Database: " + nombreBBDD.getSelectionModel().getSelectedItem());
				bufferedWriter.newLine();
				bufferedWriter.write("Hosting: " + nombreHost.getText());
				bufferedWriter.newLine();

				bufferedWriter.close();

				File carpeta_backupsFile = new File(carpetaBackup);
				if (!carpeta_backupsFile.exists()) {
					carpeta_backupsFile.mkdir();
				}

				prontEstadoFichero.setStyle("-fx-background-color: #A0F52D");
				iniciarAnimacionConectado();
			} else {
				detenerAnimacion();
				prontEstadoFichero.setStyle("-fx-background-color: #DD370F");
				iniciarAnimacionBBDDError();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Comprueba si los datos ingresados coinciden con los datos en la base de
	 * datos.
	 *
	 * @return true si los datos coinciden, false si no coinciden o si hay un error
	 *         en la conexión
	 * @throws SQLException si hay un error al consultar la base de datos
	 */
	private boolean verificarDatos() throws SQLException {

		String datos[] = new String[5];
		datos[0] = puertobbdd.getText();
		datos[1] = nombreBBDD.getSelectionModel().getSelectedItem();
		datos[2] = usuario.getText();
		datos[3] = pass.getText();
		datos[4] = nombreHost.getText();

		DBManager.datosBBDD(datos);

		Connection connection = DBManager.conexion();
		if (connection == null) {
			prontEstadoFichero.setStyle("-fx-background-color: #DD370F");
			iniciarAnimacionBBDDError();
			return false;
		}

		if (JDBC.DBManager.isConnected()) {
			return true;
		} else {
			prontEstadoFichero.setStyle("-fx-background-color: #DD370F");
			iniciarAnimacionBBDDError();
			return false;
		}
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
			String carpetaLibreria = ubicacion + "\\libreria";

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
			
			// Volver a crear los archivos
			acceso.crearEstructura();

			limpiar_datos();
			detenerAnimacion();
			prontEstadoFichero.setStyle("-fx-background-color: #A0F52D");
			iniciarAnimacionRestaurado();
		} else {
			detenerAnimacion();
			prontEstadoFichero.setStyle("-fx-background-color: #DD370F");
			iniciarAnimacionRestauradoError();
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

		if (tipoServidorSwitch.getSelectionModel().getSelectedItem().equals("online")) {
			nombreHost.setText("");
		}
	}

	/**
	 * Inicia la animación de espera en la interfaz.
	 */
	private void iniciarAnimacionEspera() {

		prontEstadoFichero.setOpacity(1);

		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarEsperando = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "Esperando entrada de datos"));
		KeyFrame mostrarPunto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoFichero.textProperty(), "Esperando entrada de datos."));
		KeyFrame mostrarDosPuntos = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoFichero.textProperty(), "Esperando entrada de datos.."));
		KeyFrame mostrarTresPuntos = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontEstadoFichero.textProperty(), "Esperando entrada de datos..."));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(2), new KeyValue(prontEstadoFichero.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarEsperando, mostrarPunto, mostrarDosPuntos, mostrarTresPuntos,
				ocultarTexto);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Inicia la animación de conexión exitosa en la interfaz.
	 */
	private void iniciarAnimacionConectado() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "Fichero guardado"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.6),
				new KeyValue(prontEstadoFichero.textProperty(), ""));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(1.1),
				new KeyValue(prontEstadoFichero.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Inicia la animación de restauración exitosa en la interfaz.
	 */
	private void iniciarAnimacionRestaurado() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "Fichero restaurado correctamente"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoFichero.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoFichero.textProperty(), "ERROR"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Inicia la animación de restauración con error en la interfaz.
	 */
	private void iniciarAnimacionRestauradoError() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "No se ha podido restaurar correctamente"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoFichero.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoFichero.textProperty(), "ERROR"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Inicia la animación de restauración con error en la interfaz.
	 */
	private void iniciarAnimacionBBDDError() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "La base de datos no existe"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoFichero.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoFichero.textProperty(), "ERROR"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Inicia la animación de restauración con error en la interfaz.
	 */
	private void iniciarAnimacionDatosError(String puerto) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO, new KeyValue(prontEstadoFichero.textProperty(),
				"Los datos recibidos estan incorrectos."));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoFichero.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoFichero.textProperty(), "ERROR"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Detiene la animación actual en la interfaz.
	 */
	private void detenerAnimacion() {
		if (timeline != null) {
			timeline.stop();
			timeline.getKeyFrames().clear(); // Eliminar los KeyFrames del Timeline
			timeline = null; // Destruir el objeto timeline
		}
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