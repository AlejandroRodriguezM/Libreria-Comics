package DBManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

/*
 * Por Alejandro Rodriguez Mena
 * 
 * Ejercicio 32
 * 
 * Tenemos una base de datos para gestionar las cuentas corrientes de un banco con las siguientes tablas:

CLIENTES (dni, nombre, teléfono, dirección).

CUENTAS (número cuenta [entero, autoincrementable], dni cliente, activa ó baja)

MOVIMIENTOS (nº cuenta, importe [+], fecha y hora, tipo [ingreso, salida, transferencia enviada, transferencia recibida], nº cuenta transferencia, concepto).

Necesitamos una aplicación para gestionar nuestro sistema bancario:

Gestión de Clientes: alta, baja (solo si no tiene cuentas corrientes), modificación (todo salvo dni). 
Gestión de cuentas corrientes (alta de cuenta, baja de cuenta [no la elimina de la base de datos para no perder los datos], ingreso en cuenta, salida de cuenta, transferencia [tiene una cuenta emisora y una receptora, generará dos movimientos].
Gestión de movimientos de la cuenta corriente de un cliente. Recibe el número de cuenta corriente a gestionar y permite: listar los movimientos entre fechas, ver saldo, ingresar y retirar dinero, hacer transferencias.
 */

public class DBManager {

	// Conexión a la base de datos
	private static Connection conn = null;

	// Configuración de la conexión a la base de datos
	private static final String DB_HOST = "localhost";
	private static final String DB_PORT = "3306";
	private static final String DB_NAME = "banco";
	private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
			+ "?serverTimezone=UTC";



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
			}
		} catch (SQLException ex) {
			return false;
		}
		return false;
	}

	/**
	 * Conecta a la base de datos
	 * 
	 * @return
	 */
	public static Connection conexion() {
		try {
			conn = DriverManager.getConnection(DB_URL, datos("Nombre de usuario: "), datos("Contraseña: "));
			return conn;
		} catch (SQLException ex) {
			return null;
		}
	}
	
	public static String datos(String mens) {
		Scanner ent = new Scanner(System.in);
		String cadena;
		System.out.println(mens);
		cadena = ent.nextLine();
		ent.close();
		return cadena;
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
			System.out.println("ERROR. No es posible desconectarse de la BBDD " + DB_NAME);
		}
	}

}
