package dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import ficherosFunciones.FuncionesFicheros;
import javafx.scene.Scene;

/**
 * Clase que gestiona la conexión a la base de datos y proporciona métodos para
 * cargar el controlador JDBC, verificar la conexión y obtener una conexión
 * activa.
 */
public class ConectManager {

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
	 * Usuario de la base de datos.
	 */
	public static String DB_USER;

	/**
	 * Contraseña de la base de datos.
	 */
	public static String DB_PASS;

	/**
	 * Puerto de la base de datos.
	 */
	public static String DB_PORT;

	/**
	 * Nombre de la base de datos.
	 */
	public static String DB_NAME;

	/**
	 * Host de la base de datos.
	 */
	public static String DB_HOST;

	/**
	 * URL de la base de datos.
	 */
	public static String DB_URL;

	public static boolean estadoConexion = false;

	public static List<Scene> activeScenes = new ArrayList<>();

	/**
	 * Carga el controlador JDBC para el proyecto.
	 * 
	 * @return true si se carga el controlador correctamente, false en caso
	 *         contrario.
	 */
	public static boolean loadDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
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
	 * Devuelve un objeto Connection en caso de que la conexion sea correcta, datos
	 * introducidos por parametro
	 *
	 * @param numeroPuerto
	 * @param nombreBBDD
	 * @param nombreUsuario
	 * @param contraBBDD
	 */
	public static void datosBBDD(String[] datos) {
		DB_PORT = datos[0];
		DB_NAME = datos[1];
		DB_USER = datos[2];
		DB_PASS = datos[3];
		DB_HOST = datos[4];

		conexion();
	}

	/**
	 * Comprueba si los datos de conexión a la base de datos son correctos.
	 *
	 * @return true si todos los datos de conexión son válidos y no están vacíos, de
	 *         lo contrario, false.
	 */
	public static boolean comprobarDatosConexion() {

		return !(DB_HOST == null || DB_HOST.isEmpty() || DB_PORT == null || DB_PORT.isEmpty() || DB_NAME == null
				|| DB_NAME.isEmpty() || DB_USER == null || DB_USER.isEmpty() || DB_PASS == null || DB_PASS.isEmpty());
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
		if (!comprobarDatosConexion()) {
			return null;
		}

		if (!FuncionesFicheros.validarDatosConexion()) {
			nav.alertaException("La URL de conexión no es válida");
			return null;
		}

		DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?serverTimezone=UTC";

		// Validar la URL de conexión
		if (!DB_URL.startsWith("jdbc:mysql://") || DB_URL.indexOf(':', 12) == -1 || DB_URL.indexOf('/', 12) == -1) {
			nav.alertaException("La URL de conexión no es válida");

			return null;
		}

		try {
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

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
		return conn;
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

	public static boolean conexionActiva() {

		boolean estadoConexion = Utilidades.isMySQLServiceRunning(DB_HOST, DB_PORT);

		if (!estadoConexion) {
			nav.alertaException("Tu conexion con xampp o el servicio MySql se ha detenido");
		}

		return estadoConexion;
	}

	/**
	 * Asigna valores predeterminados si las variables están vacías.
	 */
	public static void asignarValoresPorDefecto() {
		DB_USER = null;

		DB_PASS = null;

		DB_PORT = null;

		DB_NAME = null;

		DB_HOST = null;
	}

	/**
	 * Método estático que cierra la conexion con la base de datos
	 */
	public static void close() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
				conn = null;
				estadoConexion = false;
			}
		} catch (SQLException ex) {
			nav.alertaException("No ha sido posible cerrar su conexion con la base de datos");
		}
	}

}