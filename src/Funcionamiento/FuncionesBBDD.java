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
 *  Esta clase permite realizar diferentes operaciones pertinentes a la base de datos
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javafx.stage.FileChooser;

/**
 * Esta clase sirve para realizar diferentes operaciones que tengan que ver con
 * la base de datos.
 *
 * @author Alejandro Rodriguez
 */
public class FuncionesBBDD {

	private static Ventanas nav = new Ventanas();

	/**
	 * Funcion que permite contar cuantas filas hay en la base de datos.
	 *
	 * @return
	 */
	public int countRows() {
		Connection conn = FuncionesConexionBBDD.conexion();
		String sql = "SELECT COUNT(*) FROM comicsbbdd";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			int total = -1;

			total = rs.getRow();

			return total;
		} catch (SQLException e) {
			nav.alertaException(e.toString());
		}
		return 0;
	}

	/**
	 * Funcion que permite borrar el contenido de la tabla de la base de datos.
	 *
	 * @return
	 */
	public boolean borrarContenidoTabla() {
		Connection conn = FuncionesConexionBBDD.conexion();
		FuncionesComicsBBDD libreria = new FuncionesComicsBBDD();
		if (nav.borrarContenidoTabla()) {
			try {
				PreparedStatement statement1 = conn.prepareStatement("delete from comicsbbdd");
				PreparedStatement statement2 = conn.prepareStatement("alter table comicsbbdd AUTO_INCREMENT = 1;");

				statement1.executeUpdate();
				statement2.executeUpdate();
				libreria.reiniciarBBDD();
				return true;
			} catch (SQLException e) {
				nav.alertaException(e.toString());
			}
		}
		return false;
	}

	/**
	 *
	 * @return
	 */
	public boolean reloadID() {
		Connection conn = FuncionesConexionBBDD.conexion();
		FuncionesComicsBBDD libreria = new FuncionesComicsBBDD();
		try {
			PreparedStatement statement1 = conn.prepareStatement("ALTER TABLE comicsbbdd DROP ID, order by nomComic");
			PreparedStatement statement2 = conn.prepareStatement(
					"ALTER TABLE comicsbbdd ADD ID int NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST, order by nomComic");
			statement1.executeUpdate();
			statement2.executeUpdate();
			libreria.reiniciarBBDD();
			return true;
		} catch (SQLException e) {
			nav.alertaException(e.toString());
		}

		return false;
	}

	/////////////////////////////////
	//// FUNCIONES CREACION FICHEROS//
	/////////////////////////////////

	/**
	 * Funcion que crea una copia de seguridad de la base de datos siempre que el
	 * sistema operativo sea Linux
	 *
	 * @param fichero
	 */
	public void backupLinux(File fichero) {
		try {
			fichero.createNewFile();
			String command[] = new String[] { "mysqldump", "-u" + FuncionesConexionBBDD.DB_USER,
					"-p" + FuncionesConexionBBDD.DB_PASS, "-B", FuncionesConexionBBDD.DB_NAME, "--routines=true",
					"--result-file=" + fichero };
			ProcessBuilder pb = new ProcessBuilder(Arrays.asList(command));
			pb.redirectError(Redirect.INHERIT);
			pb.redirectOutput(Redirect.to(fichero));
			pb.start();

		} catch (IOException e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Funcion que crea una copia de seguridad de la base de datos siempre que el
	 * sistema operativo sea Windows
	 *
	 * @param fichero
	 */
	public void backupWindows(File fichero) {
		try {
			fichero.createNewFile();

			String pathMySql = "C:\\Program Files\\MySQL";

			File path = new File(pathMySql);

			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(path);

			File directorio = fileChooser.showOpenDialog(null);

			String mysqlDump = directorio.getAbsolutePath();

			String command[] = new String[] { mysqlDump, "-u" + FuncionesConexionBBDD.DB_USER, "-p" + FuncionesConexionBBDD.DB_PASS, "-B", FuncionesConexionBBDD.DB_NAME, "--hex-blob",
					"--routines=true", "--result-file=" + fichero };
			ProcessBuilder pb = new ProcessBuilder(Arrays.asList(command));
			pb.redirectError(Redirect.INHERIT);
			pb.redirectOutput(Redirect.to(fichero));
			pb.start();

		} catch (Exception e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Funcion que llamada a los procedimientos almacenados en la base de datos y
	 * muestra diferentes datos.
	 */
	public String procedimientosEstadistica() {

		int numGrapas, numTomos, numUsa, numEsp, numMarvel, numDC, numPanini, numDarkHorse, numMangas, leidos, vendidos,
				posesion, firmados, total;

		String procedimientos[] = { "call numeroGrapas()", "call numeroTomos()", "call numeroSpain()",
				"call numeroUSA()", "call total()", "call numeroPanini()", "call numeroMarvel()", "call numeroDC()",
				"call numeroDarkHorse()", "call numeroMangas()", "call comicsLeidos()", "call comicsVendidos()",
				"call comicsPosesion()", "call comicsFirmados()" };

		try {

			ResultSet rs1 = ejecucionSQL(procedimientos[0]); // Executa el procedimiento almacenado
			ResultSet rs2 = ejecucionSQL(procedimientos[1]); // Executa el procedimiento almacenado
			ResultSet rs3 = ejecucionSQL(procedimientos[2]); // Executa el procedimiento almacenado
			ResultSet rs4 = ejecucionSQL(procedimientos[3]); // Executa el procedimiento almacenado
			ResultSet rs5 = ejecucionSQL(procedimientos[4]); // Executa el procedimiento almacenado
			ResultSet rs6 = ejecucionSQL(procedimientos[5]); // Executa el procedimiento almacenado
			ResultSet rs7 = ejecucionSQL(procedimientos[6]); // Executa el procedimiento almacenado
			ResultSet rs8 = ejecucionSQL(procedimientos[7]); // Executa el procedimiento almacenado
			ResultSet rs9 = ejecucionSQL(procedimientos[8]); // Executa el procedimiento almacenado
			ResultSet rs10 = ejecucionSQL(procedimientos[9]); // Executa el procedimiento almacenado
			ResultSet rs11 = ejecucionSQL(procedimientos[10]); // Executa el procedimiento almacenado
			ResultSet rs12 = ejecucionSQL(procedimientos[11]); // Executa el procedimiento almacenado
			ResultSet rs13 = ejecucionSQL(procedimientos[12]); // Executa el procedimiento almacenado
			ResultSet rs14 = ejecucionSQL(procedimientos[13]); // Executa el procedimiento almacenado

			// Si no hay dato que comprobar, devolvera un 0
			if (rs1.next()) {
				numGrapas = rs1.getInt(1);
			} else {
				numGrapas = 0;
			}
			if (rs2.next()) {
				numTomos = rs2.getInt(1);
			} else {
				numTomos = 0;
			}
			if (rs3.next()) {
				numEsp = rs3.getInt(1);
			} else {
				numEsp = 0;
			}
			if (rs4.next()) {
				numUsa = rs4.getInt(1);
			} else {
				numUsa = 0;
			}
			if (rs5.next()) {
				total = rs5.getInt(1);
			} else {
				total = 0;
			}
			if (rs6.next()) {
				numMarvel = rs6.getInt(1);
			} else {
				numMarvel = 0;
			}
			if (rs7.next()) {
				numPanini = rs7.getInt(1);
			} else {
				numPanini = 0;
			}
			if (rs8.next()) {
				numDC = rs8.getInt(1);
			} else {
				numDC = 0;
			}
			if (rs9.next()) {
				numDarkHorse = rs9.getInt(1);
			} else {
				numDarkHorse = 0;
			}
			if (rs10.next()) {
				numMangas = rs10.getInt(1);
			} else {
				numMangas = 0;
			}
			if (rs11.next()) {
				leidos = rs11.getInt(1);
			} else {
				leidos = 0;
			}
			if (rs12.next()) {
				vendidos = rs12.getInt(1);
			} else {
				vendidos = 0;
			}
			if (rs13.next()) {
				posesion = rs13.getInt(1);
			} else {
				posesion = 0;
			}
			if (rs14.next()) {
				firmados = rs14.getInt(1);
			} else {
				firmados = 0;
			}

			rs1.close();
			rs2.close();
			rs3.close();
			rs4.close();
			rs5.close();
			rs6.close();
			rs7.close();
			rs8.close();
			rs9.close();
			rs10.close();
			rs11.close();
			rs12.close();
			rs13.close();
			rs14.close();

			return "Numero de grapas: " + numGrapas + "\nNumero de tomos: " + numTomos
					+ "\nNumeros de comics en Castellano: " + numEsp + "\nNumero de comics en USA: " + numUsa
					+ "\nNumero de comics Marvel: " + numMarvel + "\nNumero de comics DC: " + numDC
					+ "\nNumero de comics Dark horse: " + numDarkHorse + "\nNumero de comics de Panini: " + numPanini
					+ "\nNumero de mangas: " + numMangas + "\nComics leidos: " + leidos + "\nComics vendidos: "
					+ vendidos + "\nComics en posesion: " + posesion + "\nComics firmados: " + firmados + "\nTotal: "
					+ total;

		} catch (SQLException e) {
			nav.alertaException(e.toString());
		}
		return null;
	}

	/**
	 * Permite leer datos usando una query
	 *
	 * @return
	 */
	private Statement declaracionSQL() {
		Connection conn = FuncionesConexionBBDD.conexion();
		Statement st;
		try {
			st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			return st;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Permite devolver datos de la base de datos segun la query en parametros
	 *
	 * @param procedimiento
	 * @return
	 */
	public ResultSet ejecucionSQL(String procedimiento) {
		try {
			ResultSet rs = declaracionSQL().executeQuery(procedimiento);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
