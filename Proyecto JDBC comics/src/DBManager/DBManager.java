package DBManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
			System.out.println("LECTURA DRIVER");
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
			System.out.println("ERROR. IS CONECTED");
			return false;
		}
		return false;
	}

	public static Connection conexion(String numeroPuerto, String nombreBBDD, String nombreUsuario, String contraBBDD) {

		// Configuración de la conexión a la base de datos
		String DB_HOST = "localhost";
		String DB_PORT = numeroPuerto;
		String DB_NAME = nombreBBDD;
		String DB_USER = nombreUsuario;
		String DB_PASS = contraBBDD;
		String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?serverTimezone=UTC";
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			return conn;
		} catch (SQLException ex) {
			System.out.println("ERROR. CONEXION");
			return null;
		}
	}

//	/**
//	 * Cierra la conexión con la base de datos
//	 */
//	public static void close() {
//		try {
//			System.out.print("Cerrando la conexion...");
//			conn.close();
//			System.out.println("OK!");
//		} catch (SQLException ex) {
//			System.out.println("ERROR. No es posible desconectarse de la BBDD ");
//		}
//	}

}
