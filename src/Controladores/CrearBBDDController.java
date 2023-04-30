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

import Funcionamiento.Ventanas;
import JDBC.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class CrearBBDDController {

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

	private static CrearBBDDController cbd = null;
	public static String DB_USER;
	public static String DB_PASS;
	public static String DB_PORT;
	public static String DB_NAME;
	public static String DB_HOST;

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

	public void reconstruirDatos(String[] datos) {
		DB_PORT = datos[0];
		DB_NAME = datos[1];
		DB_USER = datos[2];
		DB_PASS = datos[3];
		DB_HOST = datos[4];
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
	 */
	@FXML
	void crearBBDD(ActionEvent event) {
		datosBBDD();
		if (checkDatabase()) {
			createDataBase();
			createTable();
			createProcedure();
			prontInformativo.setStyle("-fx-background-color: #A0F52D");
			prontInformativo.setText("Base de datos: " + DB_NAME + " creada correctamente");
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
	 * Comprueba si existe una base de datos con el nombre de la base de datos a
	 * crear
	 *
	 * @return
	 */
	public boolean checkDatabase() {

		boolean exists;
		ResultSet rs;

		String sentenciaSQL = "SELECT COUNT(*) " + "FROM information_schema.tables " + "WHERE table_schema = '"
				+ DB_NAME + "';";

		try {

			rs = comprobarDataBase(sentenciaSQL);
			exists = rs.getInt("COUNT(*)") < 1;

			if (exists) {
				return true;
			}

		} catch (SQLException e) {

			nav.alertaException(e.toString());
		}
		prontInformativo.setStyle("-fx-background-color: #DD370F");
		prontInformativo.setText("ERROR. Ya existe una base de datos llamada: " + DB_NAME);
		return false;
	}

	/**
	 * Funcion que permite conectarse a un host online o usar el local.
	 *
	 * @return
	 */
	public String selectorHost() {
	    if (siOnline.isSelected()) {
	        etiquetaHost.setText("Nombre del host: ");
	        nombreHost.setOpacity(1);
	        nombreHost.setVisible(true);
	        return nombreHost.getText();
	    } else if (noOffline.isSelected()) {
	        etiquetaHost.setText("Offline");
	        nombreHost.setOpacity(0);
	        nombreHost.setVisible(true);
	        return "localhost";
	    } else {
	        // Opción por defecto en caso de que ninguna opción esté seleccionada
	        return "localhost";
	    }
	}

 

	/**
	 * Funcion que devuelve un Resulset, permite
	 *
	 * @param sentenciaSQL
	 * @return
	 */
	public ResultSet comprobarDataBase(String sentenciaSQL) {

		String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "?serverTimezone=UTC";

		Statement statement;
		try {

			Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASS);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = statement.executeQuery(sentenciaSQL);
			rs.next();

			return rs;

		} catch (SQLException e) {

			nav.alertaException(e.toString());
		}
		return null;
	}

	/**
	 * Se crean las tablas de la base de datos.
	 */
	public void createTable() {

		Statement statement;
		PreparedStatement preparedStatement1;

		String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?serverTimezone=UTC";

		String sentenciaSQL1 = "DROP TABLE IF EXISTS comicsbbdd";
		String sentenciaSQL2 = "CREATE TABLE comicsbbdd (ID int NOT NULL AUTO_INCREMENT,"
				+ "nomComic varchar(150) NOT NULL," + "numComic varchar(150) NOT NULL,"
				+ "nomVariante varchar(150) NOT NULL," + "firma varchar(150) NOT NULL,"
				+ "nomEditorial varchar(150) NOT NULL," + "formato varchar(150) NOT NULL,"
				+ "procedencia varchar(150) NOT NULL," + "anioPubli varchar(150) NOT NULL,"
				+ "nomGuionista varchar(600) NOT NULL," + "nomDibujante varchar(600) NOT NULL,"
				+ "puntuacion varchar(300) NOT NULL," + "portada blob," + "estado varchar(300) NOT NULL,"
				+ "PRIMARY KEY (`ID`)) "
				+ "ENGINE=InnoDB AUTO_INCREMENT=320 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";

		try {
			Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASS);
			statement = connection.createStatement();

			statement.executeUpdate(sentenciaSQL1);
			statement.executeUpdate(sentenciaSQL2);

			preparedStatement1 = connection.prepareStatement("alter table comicsbbdd AUTO_INCREMENT = 1;");
			preparedStatement1.executeUpdate();

		} catch (SQLException e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Funcion que realiza la creacion de procedimientos almacenados.
	 */
	public void createProcedure() {

		String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?serverTimezone=UTC";

		try {
			Connection connection = DriverManager.getConnection(url, DB_USER, DB_PASS);
			Statement statement;

			statement = connection.createStatement();

			// Creacion de diferentes procesos almacenados
			statement.execute("CREATE PROCEDURE numeroGrapas()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE formato = 'Grapa';\n" + "END");
			
			statement.execute("CREATE PROCEDURE numeros_tapa_dura()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE formato = 'Tapa dura';\n" + "END");
			
			statement.execute("CREATE PROCEDURE numeros_tapa_blanda()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE formato = 'Tapa blanda';\n" + "END");
			
			statement.execute("CREATE PROCEDURE numeros_libros()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE formato = 'Libro';\n" + "END");

			statement.execute("CREATE PROCEDURE numeroMangas()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE formato = 'Manga';\n" + "END");

			statement.execute("CREATE PROCEDURE numeroDC()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE nomEditorial = 'DC';\n" + "END");

			statement.execute("CREATE PROCEDURE numeroMarvel()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE nomEditorial = 'Marvel';\n" + "END");

			statement.execute("CREATE PROCEDURE numeroDarkHorse()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE nomEditorial = 'Dark Horse';\n" + "END");

			statement.execute("CREATE PROCEDURE numeroPanini()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE nomEditorial = 'Panini';\n" + "END");
			
			statement.execute("CREATE PROCEDURE numeroImage()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE nomEditorial = 'Image Comics';\n" + "END");

			statement.execute("CREATE PROCEDURE numeroUSA()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE procedencia = 'USA';\n" + "END");

			statement.execute("CREATE PROCEDURE numeroSpain()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE procedencia = 'Spain';\n" + "END");

			statement.execute("CREATE PROCEDURE total()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd;\n" + "END");

			statement.execute("CREATE PROCEDURE comicsLeidos()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE puntuacion <> '';\n" + "END");

			statement.execute("CREATE PROCEDURE comicsFirmados()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE firma <> '';\n" + "END");

			statement.execute("CREATE PROCEDURE comicsVendidos()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE estado = 'Vendido';\n" + "END");

			statement.execute("CREATE PROCEDURE comicsPosesion()\n" + "BEGIN\n" + "SELECT COUNT(*) FROM comicsbbdd\n"
					+ "WHERE estado = 'En posesion';\n" + "END");

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
	 *
	 * @return
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

		nav.verAccesoBBDD(); // Llamada a metodo para abrir la ventana anterior

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
	 *
	 */
	public void closeWindows() {

		nav.verAccesoBBDD(); // Llamada a metodo para abrir la ventana anterior

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}
}
