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
 *  Version 5.5.0.1
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
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	private Label alarmaConexion;

	@FXML
	private Label alarmaConexionInternet;
	
	@FXML
	private Label alarmaConexionSql;

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
	private Label etiquetaHost;

	@FXML
	private ComboBox<String> nombreBBDD;

	@FXML
	private TextField nombreHost;

	@FXML
	private Label nombre_label;

	@FXML
	private PasswordField pass;

	@FXML
	private TextField passUsuarioTextField;

	@FXML
	private Label password_label;

	@FXML
	private Label prontEstadoFichero;

	@FXML
	private Label puerto_label;

	@FXML
	private TextField puertobbdd;

	@FXML
	private ToggleButton toggleEye;

	@FXML
	private ImageView toggleEyeImageView;

	@FXML
	private TextField usuario;

	@FXML
	private Label usuario_label;

	private Timeline timeline;

	private Timeline animacionAlarmaTimeline;
	private Timeline animacionAlarmaOnlineTimeline;
	private Timeline animacionAlarmaTimelineInternet;
	private Timeline animacionAlarmaTimelineMySql;
	
	private static Ventanas nav = new Ventanas();
	private static AccesoBBDDController acceso = new AccesoBBDDController();

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Thread checkerThread = new Thread(() -> {
			try {
				while (true) {
					boolean estadoInternet = Utilidades.isInternetAvailable();
					String port = obtenerDatoDespuesDeDosPuntos("Puerto");
					String host = obtenerDatoDespuesDeDosPuntos("Hosting");
					Platform.runLater(() -> {
					if (estadoInternet) {

						if (animacionAlarmaTimelineInternet != null) {
							animacionAlarmaTimelineInternet.stop();
						}
						asignarTooltip(alarmaConexionInternet, "Tienes conexion a internet");

						alarmaConexionInternet.setStyle("-fx-background-color: blue;");
					} else {
						asignarTooltip(alarmaConexionInternet, "No tienes conexion a internet");

						iniciarAnimacionInternet();
					}

					if (isMySQLServiceRunning(host, port)) {
						if (animacionAlarmaTimelineMySql != null && animacionAlarmaTimelineMySql.getStatus() == Animation.Status.RUNNING) {
						    animacionAlarmaTimelineMySql.stop();
						}
						asignarTooltip(alarmaConexionSql, "Servicio de MySql activado");

						alarmaConexionSql.setStyle("-fx-background-color: green;");
					} else {
						asignarTooltip(alarmaConexionSql, "Servicio de MySql desactivado");

						iniciarAnimacionSql();
					}
					});
					asignarTooltip(alarmaConexion, "Esperando guardado/modificacion de datos de la base de datos local");
					Thread.sleep(15000); // Espera 15 segundos
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		checkerThread.setDaemon(true); // Marcar el hilo como daemon
		checkerThread.start();

		Image eyeOpenImage = new Image(getClass().getResourceAsStream("/imagenes/visible.png"), 20, 20, true, true);
		Image eyeClosedImage = new Image(getClass().getResourceAsStream("/imagenes/hide.png"), 20, 20, true, true);

		// Configurar el ImageView con la imagen de ojo abierto inicialmente
		toggleEyeImageView.setImage(eyeClosedImage);

		// Establecer el manejador de eventos para el ImageView
		toggleEyeImageView.setOnMouseClicked(event -> {
			if (toggleEyeImageView.getImage() == eyeOpenImage) {
				passUsuarioTextField.setVisible(false);
				passUsuarioTextField.setDisable(true);
				pass.setVisible(true);
				pass.setDisable(false);

				pass.setPromptText(pass.getPromptText());
				passUsuarioTextField.setText(pass.getText());
				toggleEyeImageView.setImage(eyeClosedImage); // Cambiar a la imagen de ojo cerrado
			} else {
				passUsuarioTextField.setVisible(true);
				passUsuarioTextField.setDisable(false);
				pass.setVisible(false);
				pass.setDisable(true);

				pass.setText(passUsuarioTextField.getText());
				pass.setPromptText(pass.getPromptText());
				toggleEyeImageView.setImage(eyeOpenImage); // Cambiar a la imagen de ojo abierto
			}
		});

		iniciarAnimacionEspera();
		restringir_entrada_datos();
		acceso.crearEstructura();
		formulario_local(); // Mostrar formulario local por defecto

		limpiar_datos();
		formulario_local();
		iniciarAnimacionEspera();

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
			passUsuarioTextField.setText(pass.getText());
		});

		// Escuchador para el campo de texto "password"
		passUsuarioTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			Platform.runLater(() -> {
				actualizarComboBoxNombreBBDD();
			});
			pass.setText(passUsuarioTextField.getText());
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
		alarmaConexion.setStyle("-fx-background-color: yellow;");
		iniciarAnimacionAlarma();
	}
	
	private void asignarTooltip(Label label, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
		label.setTooltip(tooltip);
	}
	
	public static boolean isMySQLServiceRunning(String host, String portString) {
		try {
			int port = Integer.parseInt(portString); // Convertir la cadena a un entero
			InetAddress address = InetAddress.getByName(host);
			Socket socket = new Socket(address, port);
			socket.close();
			return true;
		} catch (Exception e) {
			return false;
		}
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

	private void actualizarComboBoxNombreBBDD() {
		String usuario = this.usuario.getText();
		String password = this.pass.getText();
		;

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

		guardar_datos_base_local();

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
				detenerAnimacion();
				prontEstadoFichero.setStyle("-fx-background-color: #A0F52D");
				iniciarAnimacionGuardado();

				
				alarmaConexion.setStyle("-fx-background-color: green;");
				iniciarAnimacionAlarmaOnline();
				asignarTooltip(alarmaConexion, "Datos guardados correctamente.");

			} else {
				asignarTooltip(alarmaConexion, "No hay datos para poder guardar");

				detenerAnimacion();
				prontEstadoFichero.setStyle("-fx-background-color: #DD370F");
				iniciarAnimacionBBDDError();
				alarmaConexion.setStyle("-fx-background-color: yellow;");
				iniciarAnimacionAlarma();

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
			DBManager.close();
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
			prontEstadoFichero.setStyle("-fx-background-color: #f5af2d");
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
	}
	
	/**
	 * Obtiene el dato que sigue a dos puntos (:) en una línea específica del
	 * archivo de configuración.
	 *
	 * @param linea la línea específica para buscar el dato
	 * @return el dato encontrado o una cadena vacía si no se encuentra
	 */
	public String obtenerDatoDespuesDeDosPuntos(String linea) {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + "\\AppData\\Roaming";
		String carpetaLibreria = ubicacion + "\\libreria";
		String archivoConfiguracion;

		archivoConfiguracion = carpetaLibreria + "\\configuracion_local.conf";

		try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfiguracion))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(linea + ": ")) {
					return line.substring(linea.length() + 2).trim();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
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
	
	private void iniciarAnimacionSql() {
		animacionAlarmaTimelineMySql = new Timeline();
		animacionAlarmaTimelineMySql.setCycleCount(Timeline.INDEFINITE);

		KeyFrame mostrarAmarillo1 = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo1 = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: yellow;"));
		KeyFrame mostrarAmarillo2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo2 = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: yellow;"));
		KeyFrame mostrarAmarillo3 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo3 = new KeyFrame(Duration.seconds(2.0),
				new KeyValue(alarmaConexionSql.styleProperty(), "-fx-background-color: yellow;"));

		animacionAlarmaTimelineMySql.getKeyFrames().addAll(mostrarAmarillo1, mostratRojo1, mostrarAmarillo2,
				mostratRojo2, mostrarAmarillo3, mostratRojo3);
		animacionAlarmaTimelineMySql.play();
	}

	/**
	 * Inicia la animación de conexión exitosa en la interfaz.
	 */
	private void iniciarAnimacionGuardado() {
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
				new KeyValue(prontEstadoFichero.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
		
		iniciarAnimacionAlarma();
	}

	private void iniciarAnimacionAlarma() {
		animacionAlarmaTimeline = new Timeline();
		animacionAlarmaTimeline.setCycleCount(Timeline.INDEFINITE);

		KeyFrame mostrarAmarillo = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: yellow;"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));
		KeyFrame mostrarTransparente = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: yellow;"));

		KeyFrame mostrarAmarilloNuevamente = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));

		animacionAlarmaTimeline.getKeyFrames().addAll(mostrarAmarillo, ocultarTexto, mostrarTransparente,
				mostrarAmarilloNuevamente);

		animacionAlarmaTimeline.play();
	}

	private void iniciarAnimacionAlarmaOnline() {
		animacionAlarmaOnlineTimeline = new Timeline();
		animacionAlarmaOnlineTimeline.setCycleCount(Timeline.INDEFINITE);

		KeyFrame mostrarVerde = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: green;"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));
		KeyFrame mostrarTransparente = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: green;"));
		KeyFrame mostrarVerdeNuevamente = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexion.styleProperty(), "-fx-background-color: transparent;"));

		animacionAlarmaOnlineTimeline.getKeyFrames().addAll(mostrarVerde, ocultarTexto, mostrarTransparente,
				mostrarVerdeNuevamente);
		animacionAlarmaOnlineTimeline.play();
	}

	private void iniciarAnimacionInternet() {
		animacionAlarmaTimelineInternet = new Timeline();
		animacionAlarmaTimelineInternet.setCycleCount(Timeline.INDEFINITE);

		KeyFrame mostrarAmarillo1 = new KeyFrame(Duration.ZERO,
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo1 = new KeyFrame(Duration.seconds(0.0),
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: red;"));
		KeyFrame mostrarAmarillo2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo2 = new KeyFrame(Duration.seconds(1.0),
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: red;"));
		KeyFrame mostrarAmarillo3 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: orange;"));
		KeyFrame mostratRojo3 = new KeyFrame(Duration.seconds(2.0),
				new KeyValue(alarmaConexionInternet.styleProperty(), "-fx-background-color: red;"));

		animacionAlarmaTimelineInternet.getKeyFrames().addAll(mostrarAmarillo1, mostratRojo1, mostrarAmarillo2,
				mostratRojo2, mostrarAmarillo3, mostratRojo3);
		animacionAlarmaTimelineInternet.play();
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
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "Los datos recibidos estan incorrectos."));
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