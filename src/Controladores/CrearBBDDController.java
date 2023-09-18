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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

/**
 * Esta clase sirve para crear una base de datos donde poder tratar nuestra libreria
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
	 * Controlador para el acceso a la base de datos.
	 */
	private static AccesoBBDDController acceso = new AccesoBBDDController();

	/**
	 * Controlador para la creación de la base de datos.
	 */
	private static CrearBBDDController cbd = null;

	/**
	 * Variables para almacenar información de la base de datos.
	 */
	public static String DB_USER;
	public static String DB_PASS;
	public static String DB_PORT;
	public static String DB_NAME;
	public static String DB_HOST;

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
		iniciarAnimacionEspera();

		TextFormatter<Integer> textFormatterAni = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});
		puertoBBDD.setTextFormatter(textFormatterAni);

		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion_local.conf";

		try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfiguracion))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Usuario: ")) {
					String usuarioTexto = line.substring("Usuario: ".length());
					userBBDD.setText(usuarioTexto);
				} else if (line.startsWith("Password: ")) {
					String passwordTexto = line.substring("Password: ".length());
					passBBDD.setText(passwordTexto);
				} else if (line.startsWith("Puerto: ")) {
					String puertoTexto = line.substring("Puerto: ".length());
					puertoBBDD.setText(puertoTexto);
				} else if (line.startsWith("Database: ")) {
					nombreBBDD.setText("");
				} else if (line.startsWith("Hosting: ")) {
					String hostingTexto = line.substring("Hosting: ".length());
					nombreHost.setText(hostingTexto);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Image eyeOpenImage = new Image(getClass().getResourceAsStream("/imagenes/visible.png"), 20, 20, true, true);
        Image eyeClosedImage = new Image(getClass().getResourceAsStream("/imagenes/hide.png"), 20, 20, true, true);
        

        // Configurar el ImageView con la imagen de ojo abierto inicialmente
		toggleEyeImageView.setImage(eyeClosedImage);

		// Establecer el manejador de eventos para el ImageView
		toggleEyeImageView.setOnMouseClicked(event -> {
			if (toggleEyeImageView.getImage() == eyeOpenImage) {
				passUsuarioText.setVisible(false);
				passUsuarioText.setDisable(true);
				passBBDD.setVisible(true);
				passBBDD.setDisable(false);

				passBBDD.setPromptText(passBBDD.getPromptText());
				passUsuarioText.setText(passBBDD.getText());
				toggleEyeImageView.setImage(eyeClosedImage); // Cambiar a la imagen de ojo cerrado
			} else {
				passUsuarioText.setVisible(true);
				passUsuarioText.setDisable(false);
				passBBDD.setVisible(false);
				passBBDD.setDisable(true);

				passBBDD.setText(passUsuarioText.getText());
				passBBDD.setPromptText(passBBDD.getPromptText());
				toggleEyeImageView.setImage(eyeOpenImage); // Cambiar a la imagen de ojo abierto
			}
		});
        
		// Escuchador para el campo de texto "password"
        passBBDD.textProperty().addListener((observable, oldValue, newValue) -> {

        	passUsuarioText.setText(passBBDD.getText());
		});

		// Escuchador para el campo de texto "puerto"
        passBBDD.textProperty().addListener((observable, oldValue, newValue) -> {
        	passBBDD.setText(passUsuarioText.getText());
		});
		
	}

	/**
	 * Funcion que guarda los datos de la nueva base de datos.
	 */
	public boolean datosBBDD() {

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

		return comprobarEntradas();
	}

	/**
	 * Funcion que permite comprobar si las entradas estan rellenas o no.
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
			iniciarAnimacionBaseError(errorMessage);
			return true;
		}
		return false;
	}

	/**
	 * Metodo que permite seleccionar un host online o el publico.
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
		if (!datosBBDD() && checkDatabaseExists()) {
			createDataBase();
			createTable();
			crearCarpeta();
			prontInformativo.setStyle("-fx-background-color: #A0F52D");
			iniciarAnimacionBaseCreada();
			guardarDatos();
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
			nav.alertaException("No se ha podido crear la base de datos: \n" + e.toString());
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
		ResultSet rs = null;
		String sentenciaSQL = "SELECT COUNT(*) FROM information_schema.tables";

		if (!DB_NAME.isEmpty()) {
			sentenciaSQL += " WHERE table_schema = '" + DB_NAME + "'";
		}

		try {
			rs = comprobarDataBase(sentenciaSQL);
			exists = rs.getInt("COUNT(*)") < 1;

			if (exists) {
				return true;
			} else {
				prontInformativo.setStyle("-fx-background-color: #DD370F");
				iniciarAnimacionBaseExiste();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			nav.alertaException("ERROR. Revisa los datos del fichero de conexion.");
		} finally {
			// Close the ResultSet if it's not null
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
	 * Crea las tablas de la base de datos si no existen.
	 */
	public void createTable() {
		Statement statement;
		PreparedStatement preparedStatement;

		String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?serverTimezone=UTC";
		try {
			Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASS);
			statement = connection.createStatement();

			String dropTableSQL = "DROP TABLE IF EXISTS comicsbbdd";
			String createTableSQL = "CREATE TABLE comicsbbdd (" +
			        "ID INT NOT NULL AUTO_INCREMENT, " +
			        "nomComic VARCHAR(150) NOT NULL, " +
			        "caja_deposito TEXT, " +
			        "precio_comic DOUBLE NOT NULL, " + // Cambiado el tipo a DOUBLE
			        "numComic INT NOT NULL, " +
			        "nomVariante VARCHAR(150) NOT NULL, " +
			        "firma VARCHAR(150) NOT NULL, " +
			        "nomEditorial VARCHAR(150) NOT NULL, " +
			        "formato VARCHAR(150) NOT NULL, " +
			        "procedencia VARCHAR(150) NOT NULL, " +
			        "fecha_publicacion DATE NOT NULL, " +
			        "nomGuionista TEXT NOT NULL, " +
			        "nomDibujante TEXT NOT NULL, " +
			        "puntuacion VARCHAR(300) NOT NULL, " +
			        "portada TEXT, " +
			        "key_issue TEXT, " + // Allow NULL values for key_issue
			        "url_referencia TEXT NOT NULL, " +
			        "estado TEXT NOT NULL, " +
			        "PRIMARY KEY (ID)) " +
			        "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
			statement.executeUpdate(dropTableSQL);
			statement.executeUpdate(createTableSQL);

			preparedStatement = connection.prepareStatement("alter table comicsbbdd AUTO_INCREMENT = 1;");
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			nav.alertaException(e.toString());
		}
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
	 * Método que crea la carpeta "portadas" para almacenar las imágenes de portada
	 * de los cómics.
	 * 
	 * @throws IOException Si ocurre un error al crear la carpeta.
	 */
	public void crearCarpeta() throws IOException {
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String defaultImagePath = documentsPath + File.separator + "libreria_comics" + File.separator
				+ acceso.obtenerDatoDespuesDeDosPuntos("Database") + File.separator + "portadas";
		File portadasFolder = new File(defaultImagePath);

		if (!portadasFolder.exists()) {
			if (!portadasFolder.mkdirs()) {
				throw new IOException("No se pudo crear la carpeta 'portadas'");
			}
		}
	}

	/**
	 * Método que verifica si las tablas de la base de datos existen. Si las tablas
	 * existen, devuelve true. Si las tablas no existen, reconstruye la base de
	 * datos y devuelve false.
	 * 
	 * @return true si las tablas existen, false si las tablas no existen y se
	 *         reconstruyó la base de datos.
	 */
	public boolean chechTables() {
		cbd = new CrearBBDDController();
		DatabaseMetaData dbm;
		try {
			dbm = DBManager.conexion().getMetaData();
			ResultSet tables = dbm.getTables(null, null, "comicsbbdd", null);
			if (tables.next()) {
				return true;
			} else {
				cbd.reconstruirBBDD();
				return false;
			}
		} catch (SQLException e) {

			nav.alertaException(e.toString());
		}
		return false;
	}

	/**
	 * Funcion que reconstruye una base de datos.
	 */
	public void reconstruirBBDD() {
		if (nav.alertaTablaError()) {
			createTable();
		} else {
			String excepcion = "Debes de reconstruir la base de datos. Si no, no podras entrar";
			nav.alertaException(excepcion);
		}
	}

	/**
	 * Metodo que permite crear una animacion
	 * 
	 */
	private void iniciarAnimacionBaseCreada() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInformativo.textProperty(), "Base de datos: " + DB_NAME + " creada correctamente"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.6), new KeyValue(prontInformativo.textProperty(), ""));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(1.1),
				new KeyValue(prontInformativo.textProperty(), "Base de datos: " + DB_NAME + " creada correctamente"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 * 
	 */
	private void iniciarAnimacionBaseExiste() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO, new KeyValue(prontInformativo.textProperty(),
				"ERROR. Ya existe una base de datos llamada: " + DB_NAME));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.6), new KeyValue(prontInformativo.textProperty(), ""));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(1.1), new KeyValue(prontInformativo.textProperty(),
				"ERROR. Ya existe una base de datos llamada: " + DB_NAME));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 * 
	 */
	private void iniciarAnimacionBaseError(String error) {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO, new KeyValue(prontInformativo.textProperty(), error));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.6), new KeyValue(prontInformativo.textProperty(), ""));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(1.1),
				new KeyValue(prontInformativo.textProperty(), error));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite crear una animacion
	 * 
	 */
	private void iniciarAnimacionEspera() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarEsperando = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInformativo.textProperty(), "Esperando entrada de datos"));
		KeyFrame mostrarPunto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontInformativo.textProperty(), "Esperando entrada de datos."));
		KeyFrame mostrarDosPuntos = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInformativo.textProperty(), "Esperando entrada de datos.."));
		KeyFrame mostrarTresPuntos = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontInformativo.textProperty(), "Esperando entrada de datos..."));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(2), new KeyValue(prontInformativo.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarEsperando, mostrarPunto, mostrarDosPuntos, mostrarTresPuntos,
				ocultarTexto);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Guarda los datos de configuración en un archivo de configuración.
	 * @throws SQLException si ocurre un error en la conexión a la base de datos.
	 */
	private void guardarDatos() throws SQLException {

		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String carpetaBackup = carpetaLibreria + File.separator + nombreBBDD.getText() + File.separator + "backups";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion.conf";

		try {
			acceso.crearEstructura();

			FileWriter fileWriter = new FileWriter(archivoConfiguracion);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write("###############################");
			bufferedWriter.newLine();
			bufferedWriter.write("Fichero de configuracion de la libreria");
			bufferedWriter.newLine();
			bufferedWriter.write("###############################");
			bufferedWriter.newLine();
			bufferedWriter.write("Usuario: " + DB_USER);
			bufferedWriter.newLine();
			bufferedWriter.write("Password: " + DB_PASS);
			bufferedWriter.newLine();
			bufferedWriter.write("Puerto: " + DB_PORT);
			bufferedWriter.newLine();
			bufferedWriter.write("Database: " + DB_NAME);
			bufferedWriter.newLine();
			bufferedWriter.write("Hosting: " + DB_HOST);
			bufferedWriter.newLine();

			bufferedWriter.close();
			
			File carpeta_backupsFile = new File(carpetaBackup);
			if (!carpeta_backupsFile.exists()) {
				carpeta_backupsFile.mkdir();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
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
