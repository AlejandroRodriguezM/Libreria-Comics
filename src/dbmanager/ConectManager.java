package dbmanager;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ficherosFunciones.FuncionesFicheros;
import funciones_auxiliares.Utilidades;
import funciones_auxiliares.Ventanas;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

/**
 * Clase que gestiona la conexión a la base de datos y proporciona métodos para
 * cargar el controlador JDBC, verificar la conexión y obtener una conexión
 * activa.
 */
public class ConectManager implements Initializable {

	// Conexion a la base de datos
	/**
	 * Conexión a la base de datos.
	 */
	private static Connection conn = null;

	/**
	 * Ventanas de la aplicación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Nombre de la base de datos.
	 */
	public static String DB_NAME;

	/**
	 * URL de la base de datos.
	 */
	public static String DB_URL;

	public static boolean estadoConexion = false;

	public static List<Scene> activeScenes = new ArrayList<>();

	private static final String DB_FOLDER = System.getProperty("user.home") + File.separator + "Documents"
			+ File.separator + "libreria_comics" + File.separator;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		DB_NAME = "";
	}

	/**
	 * Carga el controlador JDBC para el proyecto.
	 * 
	 * @return true si se carga el controlador correctamente, false en caso
	 *         contrario.
	 */
	public static boolean loadDriver() {
		try {
			Class.forName("org.sqlite.JDBC");
			return true;
		} catch (ClassNotFoundException ex) {
			nav.alertaException(ex.toString());
		}
		return false;
	}

	/**
	 * Comprueba la conexion y muestra su estado por pantalla
	 *
	 * @return true si la conexión existe y es válida, false en caso contrario
	 */
	public static boolean isConnected() {
		try {
			if (conn != null && !conn.isClosed()) {
				return true;
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
		return false;
	}

	/**
	 * Comprueba si los datos de conexión a la base de datos son correctos.
	 *
	 * @return true si todos los datos de conexión son válidos y no están vacíos, de
	 *         lo contrario, false.
	 */
	public static boolean comprobarDatosConexion() {

		return !(DB_NAME.isEmpty());
	}

	/**
	 * Devuelve un objeto Connection en caso de que la conexion sea correcta.
	 *
	 * @param numeroPuerto
	 * @param nombreBBDD
	 * @param nombreUsuario
	 * @param contraBBDD
	 * @return objeto Connection
	 */
	public static Connection conexion() {
//		if (!comprobarDatosConexion()) {
//			return null;
//		}

//		if (!FuncionesFicheros.validarDatosConexion()) {
//			nav.alertaException("La URL de conexión no es válida");
//			return null;
//		}

		DB_URL = "jdbc:sqlite:" + DB_FOLDER + FuncionesFicheros.datosEnvioFichero();

		System.out.println(DB_URL);

		// Validar la URL de conexión
		if (!DB_URL.startsWith("jdbc:sqlite:")) {
			nav.alertaException("La URL de conexión no es válida");

			return null;
		}

		try {
			conn = DriverManager.getConnection(DB_URL);

			if (conn == null) {
				nav.alertaException("No se pudo establecer la conexión a la base de datos");
			}

			estadoConexion = true;

			if (!FuncionesFicheros.validarDatosConexion()) {
				estadoConexion = false;
				nav.alertaException("Error. Servicio MySql apagado o desconectado de forma repentina.");
				return null;
			}

			return conn;
		} catch (SQLException ex) {
			nav.alertaException("ERROR. Revisa los datos del fichero de conexión.");
		}
		return null;
	}

	/**
	 * Método estático que restablece la conexión a la base de datos.
	 */
	public static void resetConnection() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
			conn = conexion();
		} catch (SQLException ex) {
			nav.alertaException(
					"No ha sido posible restablecer la conexion a la base de datos. Parece que esta desconectada");
		}
	}

	/**
	 * Método estático que restablece la conexión a la base de datos.
	 */
	public static void closeConnection() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException ex) {
			nav.alertaException(
					"No ha sido posible restablecer la conexion a la base de datos. Parece que esta desconectada");
		}
	}

	/**
	 * Asigna valores predeterminados si las variables están vacías.
	 */
	public static void asignarValoresPorDefecto() {
		DB_NAME = "";
	}

	/**
	 * Método estático que cierra la conexion con la base de datos
	 */
	public static void close() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
				estadoConexion = false;
			}
		} catch (SQLException ex) {
			nav.alertaException("No ha sido posible cerrar su conexion con la base de datos");
		}
	}

}