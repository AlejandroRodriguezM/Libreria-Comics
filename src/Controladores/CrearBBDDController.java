package Controladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

public class CrearBBDDController implements Initializable {

	@FXML
	private Button botonCrearBBDD;

	@FXML
	private Button botonLimpiarDatos;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private TextField nombreBBDD;

	@FXML
	private TextField userBBDD;

	@FXML
	private TextField puertoBBDD;

	@FXML
	private ToggleGroup estado;

	@FXML
	private PasswordField passBBDD;

	@FXML
	private PasswordField nombreHost;

	@FXML
	private Label prontInformativo;

	@FXML
	private Label etiquetaHost;

	@FXML
	private RadioButton noOffline;

	@FXML
	private RadioButton siOnline;

	private static Ventanas nav = new Ventanas();
	
	private static AccesoBBDDController acceso = new AccesoBBDDController();
	
	private static CrearBBDDController cbd = null;
	public static String DB_USER;
	public static String DB_PASS;
	public static String DB_PORT;
	public static String DB_NAME;
	public static String DB_HOST;
	public static String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?serverTimezone=UTC";


	private Timeline timeline;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion.conf";

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
	}

	/**
	 * Funcion que guarda los datos de la nueva base de datos.
	 */
	public void datosBBDD() {

		DB_PORT = puertoBBDD.getText();
		DB_NAME = nombreBBDD.getText();
		DB_USER = userBBDD.getText();
		DB_PASS = passBBDD.getText();
		DB_HOST = selectorHost();
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
	 */
	@FXML
	void crearBBDD(ActionEvent event) throws IOException{
		datosBBDD();
		if (checkDatabase()) {
			createDataBase();
			createTable();
			createProcedure();
			crearCarpeta();
			prontInformativo.setStyle("-fx-background-color: #A0F52D");
			iniciarAnimacionBaseCreada();
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
	 * Comprueba si existe una base de datos con el nombre especificado para la creación.
	 *
	 * @return true si la base de datos no existe, false si ya existe o si hay un error en la conexión
	 */
	public boolean checkDatabase() {
	    boolean exists;
	    ResultSet rs;
	    String sentenciaSQL = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '" + DB_NAME + "';";

	    try {
	        rs = comprobarDataBase(sentenciaSQL);
	        
	        if (rs != null && rs.next()) {
	            exists = rs.getInt(1) < 1;
	            
	            if (exists) {
	                return true;
	            }
	        }
	    } catch (SQLException e) {
	        nav.alertaException("Hay algún error en tu conexión a la base de datos. Revisa la configuración.");
	    }
	    
	    detenerAnimacion();
	    prontInformativo.setStyle("-fx-background-color: #DD370F");
	    iniciarAnimacionBaseExiste();
	    return false;
	}

	/**
	 * Funcion que permite conectarse a un host online o usar el local.
	 *
	 * @return
	 */
	public String selectorHost() {

		String host = "localhost";

		if (siOnline.isSelected()) {
			etiquetaHost.setText("Nombre del host: ");
			nombreHost.setDisable(false);
			nombreHost.setOpacity(1);
			host = nombreHost.getText();
		}
		if (noOffline.isSelected()) {
			etiquetaHost.setText("Offline");
			nombreHost.setDisable(true);
			nombreHost.setOpacity(0);
			host = "localhost";
		}
		return host;
	}

	/**
	 * Verifica la base de datos ejecutando una sentencia SQL y devuelve el ResultSet correspondiente.
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
	        nav.alertaException("Hay algún error en tu conexión a la base de datos. Revisa la configuración.");
	    }
	    
	    return null;
	}

	/**
	 * Crea las tablas de la base de datos si no existen.
	 */
	public void createTable() {
				
	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
	         Statement statement = connection.createStatement()) {
	        
	        String dropTableSQL = "DROP TABLE IF EXISTS comicsbbdd";
	        String createTableSQL = "CREATE TABLE comicsbbdd ("
	                + "ID INT NOT NULL AUTO_INCREMENT, "
	                + "nomComic VARCHAR(150) NOT NULL, "
	                + "cajaDeposito TEXT, "
	                + "numComic INT NOT NULL, "
	                + "nomVariante VARCHAR(150) NOT NULL, "
	                + "firma VARCHAR(150) NOT NULL, "
	                + "nomEditorial VARCHAR(150) NOT NULL, "
	                + "formato VARCHAR(150) NOT NULL, "
	                + "procedencia VARCHAR(150) NOT NULL, "
	                + "fechaPublicacion DATE NOT NULL, "
	                + "nomGuionista TEXT NOT NULL, "
	                + "nomDibujante TEXT NOT NULL, "
	                + "puntuacion VARCHAR(300) NOT NULL, "
	                + "portada TEXT, "
	                + "estado TEXT NOT NULL, "
	                + "PRIMARY KEY (ID)) "
	                + "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
	        
	        statement.executeUpdate(dropTableSQL);
	        statement.executeUpdate(createTableSQL);
	        
	        try (PreparedStatement preparedStatement = connection.prepareStatement("ALTER TABLE comicsbbdd AUTO_INCREMENT = 1")) {
	            preparedStatement.executeUpdate();
	        }
	    } catch (SQLException e) {
	        nav.alertaException(e.toString());
	    }
	}


	/**
	 * Crea los procedimientos almacenados en la base de datos.
	 */
	public void createProcedure() {
	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
	         Statement statement = connection.createStatement()) {

	        // Creación de procedimientos almacenados
	        String[] procedures = {
	                "numeroGrapas",
	                "numeros_tapa_dura",
	                "numeros_tapa_blanda",
	                "numeros_libros",
	                "numeroMangas",
	                "numeroDC",
	                "numeroMarvel",
	                "numeroDarkHorse",
	                "numeroPanini",
	                "numeroImage",
	                "numeroUSA",
	                "numeroSpain",
	                "total",
	                "comicsLeidos",
	                "comicsFirmados",
	                "comicsComprados",
	                "comicsPosesion"
	        };

	        for (String procedure : procedures) {
	            String procedureSQL = "CREATE PROCEDURE " + procedure + "()\n" +
	                    "BEGIN\n" +
	                    "SELECT COUNT(*) FROM comicsbbdd\n" +
	                    getProcedureCondition(procedure) +
	                    "END";

	            statement.execute(procedureSQL);
	        }
	    } catch (SQLException e) {
	        nav.alertaException(e.toString());
	    }
	}

	/**
	 * Devuelve la condición WHERE correspondiente al procedimiento almacenado.
	 *
	 * @param procedure El nombre del procedimiento almacenado.
	 * @return La condición WHERE correspondiente.
	 */
	private String getProcedureCondition(String procedure) {
	    switch (procedure) {
	        case "numeroGrapas":
	            return "WHERE formato = 'Grapa';";
	        case "numeros_tapa_dura":
	            return "WHERE formato = 'Tapa dura';";
	        case "numeros_tapa_blanda":
	            return "WHERE formato = 'Tapa blanda';";
	        case "numeros_libros":
	            return "WHERE formato = 'Libro';";
	        case "numeroMangas":
	            return "WHERE formato = 'Manga';";
	        case "numeroDC":
	            return "WHERE nomEditorial = 'DC';";
	        case "numeroMarvel":
	            return "WHERE nomEditorial = 'Marvel';";
	        case "numeroDarkHorse":
	            return "WHERE nomEditorial = 'Dark Horse';";
	        case "numeroPanini":
	            return "WHERE nomEditorial = 'Panini';";
	        case "numeroImage":
	            return "WHERE nomEditorial = 'Image Comics';";
	        case "numeroUSA":
	            return "WHERE procedencia = 'USA';";
	        case "numeroSpain":
	            return "WHERE procedencia = 'Spain';";
	        case "total":
	            return ";";
	        case "comicsLeidos":
	            return "WHERE puntuacion <> '';";
	        case "comicsFirmados":
	            return "WHERE firma <> '';";
	        case "comicsComprados":
	            return "WHERE estado = 'Comprado';";
	        case "comicsPosesion":
	            return "WHERE estado = 'En posesion';";
	        default:
	            return ";";
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
	* Método que crea la carpeta "portadas" para almacenar las imágenes de portada de los cómics.
	* @throws IOException Si ocurre un error al crear la carpeta.
	*/
	public void crearCarpeta() throws IOException {
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String defaultImagePath = documentsPath + File.separator + "libreria_comics" + File.separator + acceso.obtenerDatoDespuesDeDosPuntos("Database") + File.separator 
				+ "portadas";
		File portadasFolder = new File(defaultImagePath);

		if (!portadasFolder.exists()) {
			if (!portadasFolder.mkdirs()) {
				throw new IOException("No se pudo crear la carpeta 'portadas'");
			}
		}
	}

	/**
	* Método que verifica si las tablas de la base de datos existen.
	* Si las tablas existen, devuelve true.
	* Si las tablas no existen, reconstruye la base de datos y devuelve false.
	* @return true si las tablas existen, false si las tablas no existen y se reconstruyó la base de datos.
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
			createProcedure();
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
	 * Metodo que permite detener una animacion
	 * 
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
