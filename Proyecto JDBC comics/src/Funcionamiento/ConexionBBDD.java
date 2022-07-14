package Funcionamiento;

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
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ConexionBBDD {

	// Conexion a la base de datos
	private static Connection conn = null;
	private static NavegacionVentanas nav = new NavegacionVentanas();

	public static String DB_USER = "";
	public static String DB_PASS = "";
	public static String DB_PORT = "";
	public static String DB_NAME = "";
	public static String DB_HOST = "localhost";

	/**
	 * Conecta el proyecto con el driver JBDC
	 *
	 * @return
	 */
	public static boolean loadDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return true;
		} catch (ClassNotFoundException ex) {
			System.out.println("ERROR. LECTURA DRIVER");
			return false;
		}
	}

	/**
	 * Comprueba la conexion y muestra su estado por pantalla
	 *
	 * @return true si la conexión existe y es válida, false en caso contrario
	 */
	public static boolean isConnected() {

		try {
			if (conn != null && conn.isValid(0)) {

				return true;
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
			return false;
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
	 * @return
	 */
	public static void datosBBDD(String[] datos) {

		DB_PORT = datos[0];
		DB_NAME = datos[1];
		DB_USER = datos[2];
		DB_PASS = datos[3];
	}

	/**
	 * Devuelve un objeto Connection en caso de que la conexion sea correcta.
	 *
	 * @param numeroPuerto
	 * @param nombreBBDD
	 * @param nombreUsuario
	 * @param contraBBDD
	 * @return
	 */
	public static Connection conexion() {

		String DB_HOST = "localhost";
		String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?serverTimezone=UTC";
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			return conn;
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
			return null;
		}
	}

	/**
	 * Cierra la conexion con la base de datos
	 */
	public static void close() {
		try {
			System.out.print("Cerrando la conexion...");
			conn.close();
			System.out.println("OK!");
		} catch (SQLException ex) {
			System.out.println("ERROR. No es posible desconectarse de la BBDD ");
		}
	}

	/**
	 * Devuelve un objeto ResultSet para realizar una sentencia en la bbdd
	 *
	 * @param sentenciaSQL
	 * @return
	 */
	public static ResultSet getComic(String sentenciaSQL) {
		try {
			// Realizamos la consulta SQL
			PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			ResultSet rs = stmt.executeQuery();
			if (!rs.first()) {
				return null;
			}

			// Todo bien, devolvemos el cliente
			return rs;

		} catch (NullPointerException ex) {
			nav.alertaException(ex.toString());
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
		return null;
	}

}
