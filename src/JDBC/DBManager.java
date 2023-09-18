/**
 * Contiene las clases que hacen funcionar el acceso de la base de datos y las diferentes funciones para el uso de la misma
 *  
*/
package JDBC;
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
 *  Esta clase permite conectarse a la base de datos
 *
 *  Version 7.0.0.0
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Funcionamiento.Ventanas;

/**
 * Clase que gestiona la conexión a la base de datos y proporciona métodos para
 * cargar el controlador JDBC, verificar la conexión y obtener una conexión
 * activa.
 */
public class DBManager {

	// Conexion a la base de datos
	private static Connection conn = null;
	private static Ventanas nav = new Ventanas();

	public static String DB_USER;
	public static String DB_PASS;
	public static String DB_PORT;
	public static String DB_NAME;
	public static String DB_HOST;
	public static String DB_URL;


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
	}
	
	public static boolean datosConexionCorrectos() {
		
	    return !(DB_HOST == null || DB_HOST.isEmpty() || DB_PORT == null || DB_PORT.isEmpty() ||
	             DB_NAME == null || DB_NAME.isEmpty() || DB_USER == null || DB_USER.isEmpty() ||
	             DB_PASS == null || DB_PASS.isEmpty());
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
	    if (!datosConexionCorrectos()) {
//	        nav.alertaException("Los datos de conexión son incorrectos");
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
	        return conn;
	    } catch (SQLException ex) {
	        nav.alertaException("ERROR. Revisa los datos del fichero de conexion.");
	    }
	    catch(NullPointerException ex) {
	        nav.alertaException("ERROR. Revisa los datos del fichero de conexion.");
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
			nav.alertaException("No ha sido posible restablecer la conexion a la base de datos. Parece que esta desconectada");
		}
	}

	/**
	 * Método estático que cierra la conexion con la base de datos
	 */
	public static void close() {
		try {
			conn.close();
			conn = null;
		} catch (SQLException ex) {
			nav.alertaException("No ha sido posible cerrar su conexion con la base de datos");
		}
	}

}