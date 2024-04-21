package dbmanager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Controladores.CrearBBDDController;
import alarmas.AlarmaList;
import comicManagement.Comic;
import funciones_auxiliares.Utilidades;
import funciones_auxiliares.Ventanas;
import javafx.scene.control.Label;

public class DatabaseManagerDAO {

	/**
	 * Permite introducir un nuevo cómic en la base de datos.
	 *
	 * @param sentenciaSQL la sentencia SQL para insertar el cómic
	 * @param datos        los datos del cómic a insertar
	 * @throws IOException  si ocurre un error al manejar el archivo de imagen
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public static void subirComic(Comic datos, boolean esImportar) {
		try (Connection conn = ConectManager.conexion();
				PreparedStatement statement = conn.prepareStatement(InsertManager.INSERT_SENTENCIA)) {

			DBUtilidades.setParameters(statement, datos, false);

			if (esImportar) {
				statement.addBatch();
			}
			statement.executeUpdate();
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
	}

	/**
	 * Crea las tablas de la base de datos si no existen.
	 */
	public static void createTable(String[] datos) {

		String port = datos[0];
		String dbName = datos[1];
		String userName = datos[2];
		String password = datos[3];
		String host = datos[4];

		String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?serverTimezone=UTC";

		try (Connection connection = DriverManager.getConnection(url, userName, password);
				Statement statement = connection.createStatement();
				PreparedStatement preparedStatement = connection
						.prepareStatement("alter table comicsbbdd AUTO_INCREMENT = 1;");) {

			String dropTableSQL = "DROP TABLE IF EXISTS comicsbbdd";
			String createTableSQL = "CREATE TABLE comicsbbdd (" + "ID INT NOT NULL AUTO_INCREMENT, "
					+ "nomComic VARCHAR(150) NOT NULL, " + "caja_deposito TEXT, " + "precio_comic DOUBLE NOT NULL, "
					+ "codigo_comic VARCHAR(150), " + "numComic INT NOT NULL, " + "nomVariante VARCHAR(150) NOT NULL, "
					+ "firma VARCHAR(150) NOT NULL, " + "nomEditorial VARCHAR(150) NOT NULL, "
					+ "formato VARCHAR(150) NOT NULL, " + "procedencia VARCHAR(150) NOT NULL, "
					+ "fecha_publicacion DATE NOT NULL, " + "nomGuionista TEXT NOT NULL, "
					+ "nomDibujante TEXT NOT NULL, " + "puntuacion VARCHAR(300) NOT NULL, " + "portada TEXT, "
					+ "key_issue TEXT, " + "url_referencia TEXT NOT NULL, " + "estado TEXT NOT NULL, "
					+ "PRIMARY KEY (ID)) " + "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
			statement.executeUpdate(dropTableSQL);
			statement.executeUpdate(createTableSQL);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Funcion que reconstruye una base de datos.
	 */
	public static boolean reconstruirBBDD(String[] datos) {
		Ventanas nav = new Ventanas();

		if (nav.alertaTablaError()) {
			createTable(datos);
			return true;
		} else {
			String excepcion = "Debes de reconstruir la base de datos. Si no, no podras entrar";
			nav.alertaException(excepcion);
		}
		return false;
	}

	/**
	 * Método que verifica si las tablas de la base de datos existen y si tienen las
	 * columnas esperadas. Si las tablas y columnas existen, devuelve true. Si no
	 * existen, reconstruye la base de datos y devuelve false.
	 * 
	 * @return true si las tablas y columnas existen, false si no existen y se
	 *         reconstruyó la base de datos.
	 */
	public static boolean checkTablesAndColumns(String[] datos) {
		try (Connection connection = ConectManager.conexion()) {
			DatabaseMetaData metaData = connection.getMetaData();

			String database = datos[1];

			ResultSet tables = metaData.getTables(database, null, "comicsbbdd", null);
			if (tables.next()) {
				// La tabla existe, ahora verifiquemos las columnas
				ResultSet columns = metaData.getColumns(database, null, "comicsbbdd", null);
				Set<String> expectedColumns = Set.of("ID", "nomComic", "caja_deposito", "precio_comic", "codigo_comic",
						"numComic", "nomVariante", "firma", "nomEditorial", "formato", "procedencia",
						"fecha_publicacion", "nomGuionista", "nomDibujante", "puntuacion", "portada", "key_issue",
						"url_referencia", "estado");

				Set<String> actualColumns = new HashSet<>();

				while (columns.next()) {
					String columnName = columns.getString("COLUMN_NAME");
					actualColumns.add(columnName);
				}

				// Verifica si todas las columnas esperadas están presentes
				if (actualColumns.containsAll(expectedColumns) && actualColumns.size() == expectedColumns.size()) {
					return true;
				} else {
					if (reconstruirBBDD(datos)) {
						return true;
					}
				}
			} else {
				// La tabla no existe, reconstruimos la base de datos
				if (reconstruirBBDD(datos)) {
					return true;
				}
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
			return false;
		}
		return false;
	}

	/**
	 * Funcion que permite crear una base de datos MySql
	 */
	public static void createDataBase() {

		String sentenciaSQL = "CREATE DATABASE " + CrearBBDDController.DB_NAME + ";";

		String url = "jdbc:mysql://" + CrearBBDDController.DB_HOST + ":" + CrearBBDDController.DB_PORT
				+ "?serverTimezone=UTC";
		Statement statement;
		try {
			Connection connection = DriverManager.getConnection(url, CrearBBDDController.DB_USER,
					CrearBBDDController.DB_PASS);

			statement = connection.createStatement();
			statement.executeUpdate(sentenciaSQL);

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Comprueba si existe una base de datos con el nombre especificado para la
	 * creación.
	 *
	 * @return true si la base de datos no existe, false si ya existe o si hay un
	 *         error en la conexión
	 */
	public static boolean checkDatabaseExists(Label prontInformativo, String nombreDataBase) {
		AlarmaList.detenerAnimacion();
		boolean exists = false;
		String sentenciaSQL = "SELECT COUNT(*) FROM information_schema.tables";

		if (!CrearBBDDController.DB_NAME.isEmpty()) {
			sentenciaSQL += " WHERE table_schema = '" + CrearBBDDController.DB_NAME + "'";
		}

		try (ResultSet rs = comprobarDataBase(sentenciaSQL)) {
			int count = rs.getInt("COUNT(*)");
			exists = count < 1;

			if (exists) {
				return true;
			} else {
				AlarmaList.iniciarAnimacionBaseExiste(prontInformativo, CrearBBDDController.DB_NAME);
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
		return false;
	}

	/**
	 * Verifica la base de datos ejecutando una sentencia SQL y devuelve el
	 * ResultSet correspondiente.
	 * 
	 * @param sentenciaSQL la sentencia SQL a ejecutar
	 * @return el ResultSet que contiene los resultados de la consulta
	 */
	public static ResultSet comprobarDataBase(String sentenciaSQL) {

		String url = "jdbc:mysql://" + CrearBBDDController.DB_HOST + ":" + CrearBBDDController.DB_PORT
				+ "?serverTimezone=UTC";
		Statement statement;

		try {
			Connection connection = DriverManager.getConnection(url, CrearBBDDController.DB_USER,
					CrearBBDDController.DB_PASS);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = statement.executeQuery(sentenciaSQL);

			if (rs.next()) {
				return rs;
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return null;
	}

	private static void actualizarNombres(String columna, int idColumna, String nombreCorregido) {
		// Construir la consulta de actualización
		String consultaUpdate = "UPDATE " + ConectManager.DB_NAME + ".comicsbbdd SET " + columna + " = ? WHERE ID = "
				+ idColumna;
		String url = "jdbc:mysql://" + ConectManager.DB_HOST + ":" + ConectManager.DB_PORT + "?serverTimezone=UTC";

		try (Connection connection = DriverManager.getConnection(url, ConectManager.DB_USER, ConectManager.DB_PASS);
				Statement stmt = connection.createStatement()) {

			try (PreparedStatement pstmt = connection.prepareStatement(consultaUpdate)) {
				pstmt.setString(1, nombreCorregido);
				pstmt.executeUpdate();

				System.out.println("ID: " + idColumna + " nombre: " + nombreCorregido + " corregido");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void comprobarNormalizado(String columna, Label prontInfo) {
		String url = "jdbc:mysql://" + ConectManager.DB_HOST + ":" + ConectManager.DB_PORT + "?serverTimezone=UTC";
		int contador = 0;
		String cadena = "";
		// Construir la consulta para seleccionar los nombres de la columna
		String consultaSelect = "SELECT ID, " + columna + " FROM " + ConectManager.DB_NAME + ".comicsbbdd";

		try (Connection connection = DriverManager.getConnection(url, ConectManager.DB_USER, ConectManager.DB_PASS);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(consultaSelect)) {

			while (rs.next()) {
				int id = rs.getInt("ID");
				String nombre = rs.getString(columna);
				String nombreCorregido = "";
				if (columna.equalsIgnoreCase("nomComic") || columna.equalsIgnoreCase("firma")
						|| columna.equalsIgnoreCase("nomEditorial") || columna.equalsIgnoreCase("formato")
						|| columna.equalsIgnoreCase("procedencia") || columna.equalsIgnoreCase("key_issue")
						|| columna.equalsIgnoreCase("estado")) {
					nombreCorregido = corregirPatrones(nombre);
				} else {
					nombreCorregido = corregirNombre(nombre);
				}

				if (columna.equalsIgnoreCase("firma")) {
					nombreCorregido = nombreCorregido.replace("-", " - ");
				}

				if (columna.equalsIgnoreCase("nomEditorial")) {
					nombreCorregido = getEditorial(nombreCorregido.replace("-", " - "));
				}

				// Verificar si el nombre no está normalizado
				if (!nombre.equals(nombreCorregido)) {
					actualizarNombres(columna, id, nombreCorregido);
					contador++;
				}
			}

			if (contador == 0) {
				cadena = "Ya esta todo normalizado.";

			} else {
				cadena = " Se han normalizado: " + contador;
			}
			AlarmaList.iniciarAnimacionAvanzado(prontInfo, cadena);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// Método para corregir los nombres según los patrones especificados
	public static String corregirNombre(String nombre) {
		// Convertir primera letra de cada palabra a mayúscula
		nombre = nombre.replaceAll("(^|[-,\\s])(\\p{L})", "$1$2".toUpperCase());

		// Añadir las líneas adicionales
		nombre = nombre.trim();
		// Reemplazar ',' por '-'
		nombre = nombre.replaceAll(",", "-");
		nombre = nombre.replaceAll(", ", " - ");
		// Reemplazar ',-' por '-'
		nombre = nombre.replaceAll(",-", "-");
		// Reemplazar '-,' por '-'
		nombre = nombre.replaceAll("-,", "-");
		// Remover espacios extra alrededor de '-'
		nombre = nombre.replaceAll("\\s*-\\s*", " - ");
		// Remover ',' al final si existe
		nombre = nombre.replaceAll(",$", "");
		// Remover '-' al final si existe
		nombre = nombre.replaceAll("-$", "");

		// Si el nombre comienza con guion o coma, convertirlo en mayúscula
		if (nombre.startsWith("-") || nombre.startsWith(",")) {
			nombre = nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
		}

		// Agregar la lógica para corregir los patrones específicos
		// Reemplazar múltiples espacios entre palabras por un solo espacio
		nombre = corregirPatrones(nombre);

		nombre = nombre.replaceAll("-", " - ");

		return nombre;
	}

	public static String corregirPatrones(String texto) {

		System.out.println(texto);

		// Reemplazar ' o ``´´ por ( seguido de texto y ) al final
		texto = texto.replaceAll("('`´)(\\p{L}+)", "($2)");

		// Reemplazar múltiples espacios entre palabras por un solo espacio
		texto = texto.replaceAll("\\s{2,}", " ");

		// Remover espacios alrededor de '-'
		texto = texto.replaceAll("\\s*-\\s*", "-");

		// Remover espacios alrededor de ','
		texto = texto.replaceAll("\\s*,\\s*", ",");

		// Agregar espacios alrededor de '(' y ')' si no están presentes ya
		texto = texto.replaceAll("(?<!\\s)\\((?!\\s)", " (");
		texto = texto.replaceAll("(?<!\\s)\\)(?!\\s)", ") ");

		// Convertir la letra después de '-' a mayúscula
		Pattern pattern = Pattern.compile("-(\\p{L})");
		Matcher matcher = pattern.matcher(texto);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "-" + matcher.group(1).toUpperCase());
		}

		matcher.appendTail(sb);
		texto = sb.toString();

		texto = texto.replaceAll("'", " ");

		return texto;
	}

	public static String getEditorial(String palabra) {
		// Convertir la palabra a minúsculas para hacer la comparación insensible a
		// mayúsculas
		String palabraLower = palabra.toLowerCase();

		// Comprobar si la palabra contiene "marve" o "dc"
		if (palabraLower.contains("marvel")) {
			return "Marvel";
		} else if (palabraLower.contains("dc")) {
			return "DC";
		} else if (palabraLower.contains("dark horse")) {
			return "Dark Horse";
		} else {
			return palabra;
		}
	}

	/**
	 * Funcion crea el fichero SQL segun el sistema operativo en el que te
	 * encuentres.
	 *
	 * @param fichero
	 */
	public static void makeSQL(Label prontInfo) {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		String frase = "Fichero SQL";

		String formato = "*.sql";

		File fichero = Utilidades.tratarFichero(frase, formato,true);

		// Verificar si se obtuvo un objeto FileChooser válido
		if (fichero != null) {

			if (Utilidades.isWindows()) {
				Utilidades.backupWindows(fichero); // Llamada a funcion

			} else {
				if (Utilidades.isUnix()) {
					Utilidades.backupLinux(fichero); // Llamada a funcion
				}
			}
			String cadena = "FicheroSQL creado correctamente";
			AlarmaList.iniciarAnimacionAvanzado(prontInfo, cadena);

		}
	}

}
