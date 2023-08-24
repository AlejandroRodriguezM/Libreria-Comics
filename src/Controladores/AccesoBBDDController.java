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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;

import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Esta clase sirve para acceder a la base de datos y poder realizar diferentes
 * funciones.
 *
 * @author Alejandro Rodriguez
 */
public class AccesoBBDDController implements Initializable {

	@FXML
	private Label alarmaConexion;

	@FXML
	private Label alarmaConexionInternet;

	@FXML
	private Label alarmaConexionSql;

	@FXML
	private Button botonAccesobbdd;

	@FXML
	private Button botonAccesobbddOnline;

	@FXML
	private Button botonDescargaBBDD;

	@FXML
	private Button botonEnviar;

	@FXML
	private Button botonOpciones;

	@FXML
	private Button botonSalir;

	@FXML
	private CheckBox checkRecordar;

	@FXML
	private Label labelServidor;

	@FXML
	private Label nomUsuarioLabel;

	@FXML
	private TextField nomUsuarioText;

	@FXML
	private Label passUsuarioLabel;

	@FXML
	private PasswordField passUsuarioText;

	@FXML
	private Label prontEstadoConexion;

	@FXML
	private ToggleButton toogleButton;

	@FXML
	private ToggleButton toggleEye;

	@FXML
	private ImageView toggleEyeImageView;

	@FXML
	private TextField passUsuarioTextField;

	private Timeline timeline;

	private Timeline animacionAlarmaTimeline;
	
	private Timeline animacionAlarmaOnlineTimeline;

	private Timeline animacionAlarmaTimelineInternet;

	private Timeline animacionAlarmaTimelineMySql;

	private boolean estadoOjo = false;

	private static Ventanas nav = new Ventanas();
	private static CrearBBDDController cbd = null;

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

					String port = obtenerDatoDespuesDeDosPuntos("Puerto");
					String host = obtenerDatoDespuesDeDosPuntos("Hosting");

					boolean estadoInternet = Utilidades.isInternetAvailable();
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
							if (animacionAlarmaTimelineMySql != null
									&& animacionAlarmaTimelineMySql.getStatus() == Animation.Status.RUNNING) {
								animacionAlarmaTimelineMySql.stop();
							}
							asignarTooltip(alarmaConexionSql, "Servicio de MySql activado");

							alarmaConexionSql.setStyle("-fx-background-color: green;");
						} else {
							asignarTooltip(alarmaConexionSql, "Servicio de MySql desactivado");

							iniciarAnimacionSql();
						}
					});
					Thread.sleep(15000); // Espera 15 segundos
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		checkerThread.setDaemon(true); // Marcar el hilo como daemon
		checkerThread.start();

		asignarTooltip(alarmaConexion, "No estas conectado a la base de datos.");

		crearEstructura();
		detenerAnimacion();
		if (!JDBC.DBManager.isConnected()) {
			iniciarAnimacionEspera();

			if (animacionAlarmaTimeline != null) {
				animacionAlarmaTimeline.stop();
			}
			iniciarAnimacionAlarma();

		} else {
			alarmaConexion.setStyle("-fx-background-color: green;");

		}

		Image eyeOpenImage = new Image(getClass().getResourceAsStream("/imagenes/visible.png"), 20, 20, true, true);
		Image eyeClosedImage = new Image(getClass().getResourceAsStream("/imagenes/hide.png"), 20, 20, true, true);

		// Configurar el ImageView con la imagen de ojo abierto inicialmente
		toggleEyeImageView.setImage(eyeOpenImage);

		// Establecer el manejador de eventos para el ImageView
		toggleEyeImageView.setOnMouseClicked(event -> {
			if (toggleEyeImageView.getImage() == eyeOpenImage) {
				passUsuarioTextField.setVisible(true);
				passUsuarioTextField.setDisable(false);

				passUsuarioText.setVisible(false);
				passUsuarioText.setDisable(true);
				passUsuarioText.setPromptText(passUsuarioText.getPromptText());
				passUsuarioTextField.setText(passUsuarioText.getText());
				toggleEyeImageView.setImage(eyeClosedImage); // Cambiar a la imagen de ojo cerrado
				estadoOjo = true;
			} else {
				passUsuarioTextField.setVisible(false);
				passUsuarioTextField.setDisable(true);
				passUsuarioText.setVisible(true);
				passUsuarioText.setDisable(false);

				passUsuarioText.setText(passUsuarioTextField.getText());
				passUsuarioText.setPromptText(passUsuarioText.getPromptText());
				toggleEyeImageView.setImage(eyeOpenImage); // Cambiar a la imagen de ojo abierto
				estadoOjo = false;
			}
		});

		toogleButton.setOnAction(event -> {
			boolean isSelected = toogleButton.isSelected();
			toogleButton.setText(isSelected ? "Online" : "Local");

			// Elementos relacionados con la conexión Online/Local
//		    toggleEye.setVisible(isSelected);
			nomUsuarioLabel.setVisible(isSelected);
			nomUsuarioText.setVisible(isSelected);
			passUsuarioLabel.setVisible(isSelected);
			passUsuarioText.setVisible(isSelected);
			checkRecordar.setVisible(isSelected);
			botonAccesobbddOnline.setVisible(isSelected);
			toggleEyeImageView.setVisible(isSelected);

			// Elementos relacionados con la conexión Offline
			botonEnviar.setVisible(!isSelected);
			botonAccesobbdd.setVisible(!isSelected);

			// Deshabilitar elementos
			botonEnviar.setDisable(isSelected);
			botonAccesobbdd.setDisable(isSelected);
//		    toggleEye.setDisable(!isSelected);
			toggleEyeImageView.setDisable(!isSelected);
			nomUsuarioLabel.setDisable(!isSelected);
			nomUsuarioText.setDisable(!isSelected);
			passUsuarioLabel.setDisable(!isSelected);
			passUsuarioText.setDisable(!isSelected);
			checkRecordar.setDisable(!isSelected);
			botonAccesobbddOnline.setDisable(!isSelected);

			if (isSelected) {

				if (animacionAlarmaOnlineTimeline != null) {
					animacionAlarmaOnlineTimeline.stop();
				}
				formulario_online();
				prontEstadoConexion.setStyle("-fx-background-color: #29B6CC");
				detenerAnimacion();
				alarmaConexion.setStyle("-fx-background-color: yellow;");
				iniciarAnimacionAlarma();
				iniciarAnimacionEspera();

				if (DBManager.isConnected()) {
					DBManager.close();
				}

			} else {
				alarmaConexion.setStyle("-fx-background-color: yellow;");
				iniciarAnimacionAlarma();
				iniciarAnimacionEspera();
			}
		});
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

	/**
	 * Funcion para abrir el navegador y acceder a la URL
	 *
	 * @param event
	 */
	@FXML
	void accesoMySqlWorkbench(ActionEvent event) {
		String url1 = "https://dev.mysql.com/downloads/windows/installer/8.0.html";
		String url2 = "https://www.youtube.com/watch?v=FvXQBKsp0OI&ab_channel=MisterioRojo";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url1); // Llamada a funcion
			Utilidades.accesoWebWindows(url2); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url1); // Llamada a funcion
				Utilidades.accesoWebLinux(url2); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url1); // Llamada a funcion
				Utilidades.accesoWebMac(url2); // Llamada a funcion
			}
		}
	}

	/**
	 * Funcion que permite el acceso a la ventana de menuPrincipal
	 *
	 * @param event
	 */
	@FXML
	void entrarMenu(ActionEvent event) {

		if (JDBC.DBManager.isConnected()) { // Siempre que el metodo de la clase DBManager sea true, permitira acceder
			DBManager.resetConnection();
			// al menu principal
			nav.verMenuPrincipal(); // Llamada a metodo de la clase NavegacionVentanas. Permite cargar y mostrar el
									// menu principal
			Stage myStage = (Stage) this.botonAccesobbdd.getScene().getWindow();
			myStage.close();
		} else { // En caso contrario mostrara el siguiente mensaje.
			detenerAnimacion();
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			iniciarAnimacionConexion();
		}
	}

	@FXML
	void entrarOnline(ActionEvent event) throws IOException {

		if (checkRecordar.isSelected()) {
			String valorPass;
			if (estadoOjo) {
				valorPass = passUsuarioTextField.getText();
				passUsuarioText.setText(valorPass);
			} else {
				valorPass = passUsuarioText.getText();
				passUsuarioTextField.setText(valorPass);
			}

			Utilidades.guardarUsuario(nomUsuarioText, valorPass);
		}
	}

	public void formulario_online() {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion_usuario.conf";

		boolean usuarioEncontrado = false;
		boolean passwordEncontrado = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfiguracion))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Usuario: ")) {
					String usuarioTexto = line.substring("Usuario: ".length());
					nomUsuarioText.setText(usuarioTexto);
					usuarioEncontrado = usuarioTexto.length() > 1;
				} else if (line.startsWith("Password: ")) {
					String passwordTexto = line.substring("Password: ".length());
					passUsuarioText.setText(passwordTexto);
					passwordEncontrado = passwordTexto.length() > 1;
				}
			}

			if (usuarioEncontrado && passwordEncontrado) {
				checkRecordar.setSelected(true); // Marcar el CheckBox
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envia los datos a la clase DBManager y permite conectarse a esta.
	 *
	 * @param event
	 */
	@FXML
	void enviarDatos(ActionEvent event) {
		if (!JDBC.DBManager.loadDriver()) { // Llamada a método que permite comprobar que el driver de conexión a la
											// base de datos
			return;
		}

		envioDatosBBDD(); // Llamada a método que manda los datos de los textField de la ventana hacia la
							// clase DBManager.
		DBManager.conexion(); // Llamada a método que permite conectar con la base de datos.
		cbd = new CrearBBDDController();

		if (JDBC.DBManager.isConnected()) {
			if (cbd.chechTables()) {
				String userHome = System.getProperty("user.home");
				String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
				String carpetaLibreria = ubicacion + File.separator + "libreria";
				String carpetaBackup = carpetaLibreria + File.separator + obtenerDatoDespuesDeDosPuntos("Database")
						+ File.separator + "backups";

				try {
					File carpeta_backupsFile = new File(carpetaBackup);
					cbd.crearCarpeta();
					if (!carpeta_backupsFile.exists()) {
						carpeta_backupsFile.mkdirs();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				detenerAnimacion();
				prontEstadoConexion.setStyle("-fx-background-color: #A0F52D");
				iniciarAnimacionConectado();

				alarmaConexion.setStyle(null);
				alarmaConexion.setStyle("-fx-background-color: green;");
				if (animacionAlarmaOnlineTimeline != null) {
					animacionAlarmaOnlineTimeline.stop(); // Detener la animación anterior
				}
				asignarTooltip(alarmaConexion, "Estas conectado a la base de datos.");
				iniciarAnimacionAlarmaOnline();
				animacionAlarmaOnlineTimeline.play();

			}
		} else { // En caso contrario mostrará el siguiente mensaje
			asignarTooltip(alarmaConexion, "No estas conectado a la base de datos.");
			detenerAnimacion();
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			iniciarAnimacionError();
		}
	}

	/**
	 * Funcion que permite mandar los datos a la clase DBManager
	 *
	 * @return
	 */
	public void envioDatosBBDD() {
		detenerAnimacion();
		cbd = new CrearBBDDController();
		String datos[] = new String[5];
		datos[0] = obtenerDatoDespuesDeDosPuntos("Puerto");
		datos[1] = obtenerDatoDespuesDeDosPuntos("Database");
		datos[2] = obtenerDatoDespuesDeDosPuntos("Usuario");
		datos[3] = obtenerDatoDespuesDeDosPuntos("Password");
		datos[4] = obtenerDatoDespuesDeDosPuntos("Hosting");

		DBManager.datosBBDD(datos);
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
	 * Permite salir completamente del programa.
	 *
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {

		if (nav.salirPrograma(event)) { // Llamada a metodo que permite salir completamente del programa
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Método que se ejecuta al hacer clic en el botón de opciones del programa.
	 * Abre la ventana de opciones y cierra la ventana actual.
	 */
	@FXML
	void opcionesPrograma(ActionEvent event) {
		nav.verOpciones();

		Stage myStage = (Stage) this.botonOpciones.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Cierra el programa a la fuerza correctamente.
	 */
	public void closeWindows() { // Metodo que permite cerrar completamente el programa en caso de cerrar a la //
		// fuerza.
		Stage myStage = (Stage) this.botonEnviar.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Método que crea la estructura de carpetas y archivos necesarios para la
	 * librería.
	 */
	public void crearEstructura() {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";

		// Verificar y crear la carpeta "libreria" si no existe
		File carpetaLibreriaFile = new File(carpetaLibreria);
		if (!carpetaLibreriaFile.exists()) {
			carpetaLibreriaFile.mkdir();
			carpetaLibreriaFile.setWritable(true);
		}

		// Verificar y crear los archivos de configuración si no existen
		String archivoConfiguracionLocal = carpetaLibreria + File.separator + "configuracion_local.conf";
		String archivoConfiguracionOnline = carpetaLibreria + File.separator + "configuracion_usuario.conf";

		File archivoConfiguracionLocalFile = new File(archivoConfiguracionLocal);
		File archivoConfiguracionOnlineFile = new File(archivoConfiguracionOnline);

		if (!archivoConfiguracionLocalFile.exists()) {
			try {
				archivoConfiguracionLocalFile.createNewFile();

				// Escribir líneas en el archivo de configuración local
				FileWriter fileWriterLocal = new FileWriter(archivoConfiguracionLocalFile);
				BufferedWriter bufferedWriterLocal = new BufferedWriter(fileWriterLocal);
				bufferedWriterLocal.write("###############################");
				bufferedWriterLocal.newLine();
				bufferedWriterLocal.write("Fichero de configuracion local de la libreria");
				bufferedWriterLocal.newLine();
				bufferedWriterLocal.write("###############################");
				bufferedWriterLocal.newLine();
				bufferedWriterLocal.write("Usuario:");
				bufferedWriterLocal.newLine();
				bufferedWriterLocal.write("Password:");
				bufferedWriterLocal.newLine();
				bufferedWriterLocal.write("Puerto:");
				bufferedWriterLocal.newLine();
				bufferedWriterLocal.write("Database:");
				bufferedWriterLocal.newLine();
				bufferedWriterLocal.write("Hosting: Localhost");
				bufferedWriterLocal.newLine();
				bufferedWriterLocal.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!archivoConfiguracionOnlineFile.exists()) {
			try {
				archivoConfiguracionOnlineFile.createNewFile();

				// Escribir líneas en el archivo de configuración online
				FileWriter fileWriterOnline = new FileWriter(archivoConfiguracionOnlineFile);
				BufferedWriter bufferedWriterOnline = new BufferedWriter(fileWriterOnline);
				bufferedWriterOnline.write("###############################");
				bufferedWriterOnline.newLine();
				bufferedWriterOnline.write("Usuario y contraseño del usuario");
				bufferedWriterOnline.newLine();
				bufferedWriterOnline.write("###############################");
				bufferedWriterOnline.newLine();
				bufferedWriterOnline.write("Usuario: ");
				bufferedWriterOnline.newLine();
				bufferedWriterOnline.write("Password: ");
				bufferedWriterOnline.newLine();
				bufferedWriterOnline.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	private void iniciarAnimacionEspera() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarEsperando = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoConexion.textProperty(), "Esperando"));
		KeyFrame mostrarPunto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoConexion.textProperty(), "Esperando."));
		KeyFrame mostrarDosPuntos = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoConexion.textProperty(), "Esperando.."));
		KeyFrame mostrarTresPuntos = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontEstadoConexion.textProperty(), "Esperando..."));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(2), new KeyValue(prontEstadoConexion.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarEsperando, mostrarPunto, mostrarDosPuntos, mostrarTresPuntos,
				ocultarTexto);

		// Iniciar la animación
		timeline.play();
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
	 * Metodo que permite crear una animacion
	 */
	private void iniciarAnimacionConectado() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoConexion.textProperty(), "Conectado"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.6),
				new KeyValue(prontEstadoConexion.textProperty(), ""));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(1.1),
				new KeyValue(prontEstadoConexion.textProperty(), "Conectado"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	private void iniciarAnimacionError() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoConexion.textProperty(), "ERROR. Activa MySql"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoConexion.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoConexion.textProperty(), "ERROR. Activa MySql"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	private void iniciarAnimacionConexion() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoConexion.textProperty(), "ERROR. Conectate primero"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoConexion.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoConexion.textProperty(), "ERROR. Conectate primero"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite detener una animacion
	 */
	private void detenerAnimacion() {
		if (timeline != null) {
			timeline.stop();
			timeline = null; // Destruir el objeto timeline
		}
	}
}
