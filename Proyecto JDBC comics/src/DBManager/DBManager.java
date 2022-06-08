package DBManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Por Alejandro Rodriguez Mena
 */

public class DBManager {

	// Conexión a la base de datos
	private static Connection conn = null;

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
	 * Comprueba la conexión y muestra su estado por pantalla
	 *
	 * @return true si la conexión existe y es válida, false en caso contrario
	 */
	public static boolean isConnected() {
		// Comprobamos estado de la conexión
		try {
			if (conn != null && conn.isValid(0)) {
				return true;
			} else {
				System.out.println("PRUEBA ERROR IS CONNECTED");
			}
		} catch (SQLException ex) {
			System.out.println(ex);
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @param numeroPuerto
	 * @param nombreBBDD
	 * @param nombreUsuario
	 * @param contraBBDD
	 * @return
	 */
	public static Connection conexion(String puertobbdd,String nombreBBDD,String usuario,String pass) {

		// Configuración de la conexión a la base de datos
		String DB_HOST = "localhost";
		String DB_PORT = puertobbdd;
		String DB_NAME = nombreBBDD;
		String DB_USER = usuario;
		String DB_PASS = pass;
		String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?serverTimezone=UTC";
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			return conn;
		} catch (SQLException ex) {
			System.out.println(ex);
			return null;
		}
	}

	/**
	 * Cierra la conexión con la base de datos
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
	 * 
	 * @param sentenciaSQL
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getComic(String sentenciaSQL) throws SQLException {
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
			System.err.println("ERROR. No se puede mostrar porque no hay clientes.");
		}
		return null;
	}

}
