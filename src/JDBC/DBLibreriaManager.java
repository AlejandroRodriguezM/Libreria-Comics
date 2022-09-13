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
 * Esta clase permite realizar diferentes operaciones pertinentes a la base de datos y tambien sirve para realizar las diferentes operaciones en la base de datos
 * que tenga que ver con la libreria de comics
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Funcionamiento.Comic;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

/**
 * Esta clase sirve para realizar diferentes operaciones que tengan que ver con
 * la base de datos.
 *
 * @author Alejandro Rodriguez
 */
public class DBLibreriaManager extends Comic {

	public static List<Comic> listaComics = new ArrayList<>();
	public static List<Comic> listaPosesion = new ArrayList<>();
	public static List<Comic> listaTratamiento = new ArrayList<>();
	public static List<Comic> listaCompleta = new ArrayList<>();
	public static List<Comic> filtroComics = new ArrayList<>();

	private static Ventanas nav = new Ventanas();
	private static Connection conn = null;
	private static Utilidades utilidad = null;

	/**
	 * Funcion que permite contar cuantas filas hay en la base de datos.
	 *
	 * @return
	 */
	public int countRows() {
		Connection conn = DBManager.conexion();
		String sql = "SELECT COUNT(*) FROM comicsbbdd";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			int total = -1;

			total = rs.getRow();

			return total;
		} catch (SQLException e) {
			nav.alertaException(e.toString());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public String[] deleteTable() {
		String sentencia[] = new String[2];
		if (nav.borrarContenidoTabla()) {
			sentencia[0] = "delete from comicsbbdd";
			sentencia[1] = "alter table comicsbbdd AUTO_INCREMENT = 1;";
		}
		return sentencia;
	}

	public String[] reloadID() {
		String sentencia[] = new String[2];
		sentencia[0] = "ALTER TABLE comicsbbdd DROP ID, order by nomComic";
		sentencia[1] = "ALTER TABLE comicsbbdd ADD ID int NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST, order by nomComic";
		return sentencia;
	}

	/**
	 * Funcion que permite reasignar ID a todos los comics, se realiza a la hora de
	 * introducir, modificar, eliminar un comic.
	 *
	 * @return
	 */
	public boolean ejecucionPreparedStatement(String[] sentencia) {
		Connection conn = DBManager.conexion();

		try {
			PreparedStatement statement1 = conn.prepareStatement(sentencia[0]);
			PreparedStatement statement2 = conn.prepareStatement(sentencia[1]);
			statement1.executeUpdate();
			statement2.executeUpdate();
			reiniciarBBDD();
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
			String command[] = new String[] { "mysqldump", "-u" + DBManager.DB_USER, "-p" + DBManager.DB_PASS, "-B",
					DBManager.DB_NAME, "--routines=true", "--result-file=" + fichero };
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

			String command[] = new String[] { mysqlDump, "-u" + DBManager.DB_USER, "-p" + DBManager.DB_PASS, "-B",
					DBManager.DB_NAME, "--hex-blob", "--routines=true", "--result-file=" + fichero };
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
	 * Permite devolver datos de la base de datos segun la query en parametros
	 *
	 * @param procedimiento
	 * @return
	 */
	public ResultSet ejecucionSQL(String procedimiento) {
		ResultSet rs = null;
		Statement st = null;
		Connection conn = DBManager.conexion();

		try {
			st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery(procedimiento);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// **************************************//
	// ****FUNCIONES DE LA LIBRERIA**********//
	// **************************************//

	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 */
	public Comic[] verLibreriaCompleta() {

		listaCompleta.clear();

		String sentenciaSql = "SELECT * from comicsbbdd";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;
		rs = obtenLibreria(sentenciaSql);
		listaCompleta = listaDatos(rs);
		comic = new Comic[listaCompleta.size()];
		comic = listaCompleta.toArray(comic);
		return comic;
	}

	/**
	 * Devuelve todos los datos de la base de datos que se encuentren en posesion
	 *
	 * @return
	 */
	public Comic[] verLibreriaPosesion() {

		listaPosesion.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where estado = 'En posesion'";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;

		rs = obtenLibreria(sentenciaSql);
		listaPosesion = listaDatos(rs);

		comic = new Comic[listaPosesion.size()];
		comic = listaPosesion.toArray(comic);

		return comic;
	}

	/**
	 * Funcion que permite guardar en un list el total de comics que se han vendido
	 *
	 * @return
	 */
	public Comic[] verLibreriaVendidos() {

		listaTratamiento.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where estado = 'Vendido'";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;

		rs = obtenLibreria(sentenciaSql);
		listaTratamiento = listaDatos(rs);

		comic = new Comic[listaTratamiento.size()];
		comic = listaTratamiento.toArray(comic);

		return comic;
	}

	/**
	 * Funcion que permite guardar en un list el total de comics que se han vendido
	 *
	 * @return
	 */
	public Comic[] verLibreriaEnVenta() {

		listaTratamiento.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where estado = 'En venta'";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;

		rs = obtenLibreria(sentenciaSql);
		listaTratamiento = listaDatos(rs);

		comic = new Comic[listaTratamiento.size()];
		comic = listaTratamiento.toArray(comic);

		return comic;
	}

	/**
	 * Funcion que permite guardar en un list el total de comics que se han vendido
	 *
	 * @return
	 */
	public Comic[] verLibreriaFirmados() {

		listaTratamiento.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where Firma <> ''";

		Comic comic[] = null;

		ResultSet rs;

		rs = obtenLibreria(sentenciaSql);
		listaTratamiento = listaDatos(rs);

		comic = new Comic[listaTratamiento.size()];
		comic = listaTratamiento.toArray(comic);

		return comic;
	}

	/**
	 * Funcion que permite guardar en un list el total de comics que tengan
	 * puntuacion
	 *
	 * @return
	 */
	public Comic[] verLibreriaPuntuacion() {

		listaTratamiento.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where puntuacion <>''";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;

		rs = obtenLibreria(sentenciaSql);
		listaTratamiento = listaDatos(rs);

		comic = new Comic[listaTratamiento.size()];
		comic = listaTratamiento.toArray(comic);

		return comic;
	}

	/**
	 * Devuelve datos de la base de datos segun el parametro.
	 *
	 * @param datos
	 * @return
	 */
	public Comic[] filtadroBBDD(Comic datos) {

		reiniciarBBDD();

		Comic comic[] = null;

		String sql = datosConcatenados(datos);
		Connection conn = DBManager.conexion();

		filtroComics.clear();

		if (sql.length() != 0) {
			try {
				PreparedStatement ps = conn.prepareStatement(sql);

				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					filtroComics = listaDatos(rs);
				}
			} catch (SQLException ex) {
				nav.alertaException(ex.toString());
			}

			comic = new Comic[filtroComics.size()];
			comic = filtroComics.toArray(comic);
		} else {
			comic = new Comic[filtroComics.size()];
		}
		return comic;
	}

	/**
	 * Funcion que permite hacer una busqueda general mediante 1 sola palabra, hace
	 * una busqueda en ciertos identificadores de la tabla.
	 *
	 * @param sentencia
	 * @return
	 */
	public Comic[] verBusquedaGeneral(String busquedaGeneral) {
		Connection conn = DBManager.conexion();
		String sql1 = datosGeneralesNombre(busquedaGeneral);
		String sql2 = datosGeneralesVariante(busquedaGeneral);
		String sql3 = datosGeneralesFirma(busquedaGeneral);
		String sql4 = datosGeneralesGuionista(busquedaGeneral);
		String sql5 = datosGeneralesDibujante(busquedaGeneral);

		Comic comic[] = null;

		reiniciarBBDD();

		filtroComics.clear();

		try {
			PreparedStatement ps1 = conn.prepareStatement(sql1);
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			PreparedStatement ps3 = conn.prepareStatement(sql3);
			PreparedStatement ps4 = conn.prepareStatement(sql4);
			PreparedStatement ps5 = conn.prepareStatement(sql5);

			ResultSet rs1 = ps1.executeQuery();
			ResultSet rs2 = ps2.executeQuery();
			ResultSet rs3 = ps3.executeQuery();
			ResultSet rs4 = ps4.executeQuery();
			ResultSet rs5 = ps5.executeQuery();

			if (rs1.next()) {
				filtroComics = listaDatos(rs1);
			}
			if (rs2.next()) {
				filtroComics = listaDatos(rs2);
			}
			if (rs3.next()) {
				filtroComics = listaDatos(rs3);
			}
			if (rs4.next()) {
				filtroComics = listaDatos(rs4);
			}
			if (rs5.next()) {
				filtroComics = listaDatos(rs5);
			}

			filtroComics = Utilidades.listaArreglada(filtroComics);

		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}

		comic = new Comic[filtroComics.size()];

		comic = filtroComics.toArray(comic);

		return comic;
	}

	/**
	 * Funcion que segun los datos introducir mediante parametros, concatenara las
	 * siguientes cadenas de texto. Sirve para hacer busqueda en una base de datos
	 *
	 * @param datos
	 * @return
	 */
	public String datosConcatenados(Comic comic) {

		int datosRellenados = 0;

		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd");

		if (comic.getID().length() != 0) {

			sql.append(connector).append("ID = " + comic.getID());
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getNombre().length() != 0) {

			sql.append(connector).append("nomComic like'%" + comic.getNombre() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getNumero().length() != 0) {
			sql.append(connector).append("numComic = " + comic.getNumero());
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getVariante().length() != 0) {
			sql.append(connector).append("nomVariante like'%" + comic.getVariante() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getFirma().length() != 0) {
			sql.append(connector).append("firma like'%" + comic.getFirma() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getEditorial().length() != 0) {
			sql.append(connector).append("nomEditorial like'%" + comic.getEditorial() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getFormato().length() != 0) {
			sql.append(connector).append("formato like'%" + comic.getFormato() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getProcedencia().length() != 0) {
			sql.append(connector).append("procedencia like'%" + comic.getProcedencia() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getFecha().length() != 0) {
			sql.append(connector).append("anioPubli like'%" + comic.getFecha() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getGuionista().length() != 0) {
			sql.append(connector).append("nomGuionista like'%" + comic.getGuionista() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getDibujante().length() != 0) {
			sql.append(connector).append("nomDibujante like'%" + comic.getDibujante() + "%'");
			connector = " AND ";
			datosRellenados++;
		}

		if (datosRellenados != 0) {
			return sql.toString();
		}

		return "";
	}

	/**
	 * Funcion que hace una busqueda de un identificador en concreto de la tabla
	 *
	 * @param datos
	 * @return
	 */
	public String datosGeneralesNombre(String busquedaGeneral) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		sql.append(connector).append("nomComic like'%" + busquedaGeneral + "%'");

		return sql.toString();
	}

	/**
	 * Funcion que hace una busqueda de un identificador en concreto de la tabla
	 *
	 * @param datos
	 * @return
	 */
	public String datosGeneralesVariante(String busquedaGeneral) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		sql.append(connector).append("nomVariante like'%" + busquedaGeneral + "%'");

		return sql.toString();
	}

	/**
	 * Funcion que hace una busqueda de un identificador en concreto de la tabla
	 *
	 * @param datos
	 * @return
	 */
	public String datosGeneralesFirma(String busquedaGeneral) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		sql.append(connector).append("firma like'%" + busquedaGeneral + "%'");

		return sql.toString();
	}

	/**
	 * Funcion que hace una busqueda de un identificador en concreto de la tabla
	 *
	 * @param datos
	 * @return
	 */
	public String datosGeneralesGuionista(String busquedaGeneral) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		sql.append(connector).append("nomGuionista like'%" + busquedaGeneral + "%'");

		return sql.toString();
	}

	/**
	 * Funcion que hace una busqueda de un identificador en concreto de la tabla
	 *
	 * @param datos
	 * @return
	 */
	public String datosGeneralesDibujante(String busquedaGeneral) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		sql.append(connector).append("nomDibujante like'%" + busquedaGeneral + "%'");

		return sql.toString();
	}

	/**
	 * Devuelve una lista con todos los datos de los comics de la base de datos
	 *
	 * @param rs
	 * @return
	 */
	public List<Comic> listaDatos(ResultSet rs) {

		try {
			if (rs != null) {
				do {
					this.ID = rs.getString("ID");
					this.nombre = rs.getString("nomComic");
					this.numero = rs.getString("numComic");
					this.variante = rs.getString("nomVariante");
					this.firma = rs.getString("firma");
					this.editorial = rs.getString("nomEditorial");
					this.formato = rs.getString("formato");
					this.procedencia = rs.getString("procedencia");
					this.fecha = rs.getString("anioPubli");
					this.guionista = rs.getString("nomGuionista");
					this.dibujante = rs.getString("nomDibujante");
					this.estado = rs.getString("estado");
					this.puntuacion = rs.getString("puntuacion");
					this.imagen = rs.getBinaryStream("portada");

					Comic comic = new Comic(this.ID, this.nombre, this.numero, this.variante, this.firma,
							this.editorial, this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante,
							this.estado, this.puntuacion, this.imagen);

					listaComics.add(comic);

				} while (rs.next());
			}
		} catch (SQLException e) {
			nav.alertaException("Datos introducidos incorrectos.");
			e.printStackTrace();
		}

		return listaComics;
	}

	/**
	 * Devuelve solamente 1 comics de la base de datos.
	 *
	 * @param rs
	 * @return
	 */
	public Comic datosIndividual(ResultSet rs) {
		Comic comic = new Comic();

		try {
			if (rs != null) {
				do {
					this.ID = rs.getString("ID");
					this.nombre = rs.getString("nomComic");
					this.numero = rs.getString("numComic");
					this.variante = rs.getString("nomVariante");
					this.firma = rs.getString("firma");
					this.editorial = rs.getString("nomEditorial");
					this.formato = rs.getString("formato");
					this.procedencia = rs.getString("procedencia");
					this.fecha = rs.getString("anioPubli");
					this.guionista = rs.getString("nomGuionista");
					this.dibujante = rs.getString("nomDibujante");
					this.estado = rs.getString("estado");
					this.puntuacion = rs.getString("puntuacion");
					this.imagen = rs.getBinaryStream("portada");

					comic = new Comic(this.ID, this.nombre, this.numero, this.variante, this.firma, this.editorial,
							this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante, this.estado,
							this.puntuacion, this.imagen);

				} while (rs.next());
			}
		} catch (SQLException e) {
			nav.alertaException("Datos introducidos incorrectos.");
		}
		return comic;
	}

	/**
	 * Comprueba si la lista de comics contiene o no algun dato
	 *
	 * @param listaComic
	 */
	public boolean checkList(List<Comic> listaComic) {
		if (listaComic.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Permite reiniciar la pantalla donde se muestran los datos
	 */
	public void reiniciarBBDD() {
		filtroComics.clear();
		listaPosesion.clear();
		listaComics.clear();
		listaCompleta.clear();
	}

	/**
	 * Funcion que devuelve un comic cuya ID este como parametro de busqueda
	 *
	 * @param id
	 * @return
	 */
	public Comic comicDatos(String identificador) {
		Comic comic = new Comic();

		String sentenciaSQL = "select * from comicsbbdd where ID = " + identificador;

		ResultSet rs;

		rs = obtenLibreria(sentenciaSQL);

		comic = datosIndividual(rs);

		return comic;
	}

	/**
	 * Comprueba que el ID introducido existe
	 *
	 * @return
	 */
	public boolean chechID(String identificador) {
		String sentenciaSQL = "select * from comicsbbdd where ID = " + identificador;
		conn = DBManager.conexion();
		if (identificador.length() != 0) { // Si has introducido ID a la hora de realizar la modificacion, permitira lo
			// siguiente

			try {
				Statement consultaID = conn.createStatement();
				ResultSet rs = consultaID.executeQuery(sentenciaSQL); // Realiza la consulta en la base de datos con la
				// sentencia de seleccionar datos de un comic
				// segun su ID

				if (rs.next()) // En caso de existir el dato, devolvera true
				{
					return true;
				}

			} catch (SQLException e) {
				nav.alertaException("No existe el " + identificador + " en la base de datos.");
			}
		}
		return false;
	}

	/**
	 * Funcion que permite generar imagenes de formato JPG a la hora de exportar la
	 * base de datos excel.
	 */
	public void saveImageFromDataBase() {
		String sentenciaSQL = "SELECT * FROM comicsbbdd";
		conn = DBManager.conexion();
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sentenciaSQL);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String nombreImagen = rs.getString(1);
				Blob imagenBlob = rs.getBlob(13);
				File directorio = new File("portadas");
				directorio.mkdir();
				FileOutputStream fileops = new FileOutputStream(
						directorio.getAbsoluteFile().toString() + "/" + nombreImagen + ".jpg");
				fileops.write(imagenBlob.getBytes(1, (int) imagenBlob.length()));
				fileops.close();

			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permite modificar un comic de la base de datos
	 *
	 * @param id
	 * @param sentenciaSQL
	 */
	public boolean modificarDatos(String id, String sentenciaSQL) {
		PreparedStatement stmt;
		Connection conn = DBManager.conexion();
		try {
			if (id.length() != 0) {
				stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE); // Permite leer y ejecutar la sentencia de MySql

				stmt.setString(1, id);
				if (stmt.executeUpdate() == 1) {
					ejecucionPreparedStatement(reloadID());
					return true;
				}
			}

			return false;
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
		return false;
	}

	/**
	 * Funcion que permite cambiar de estado el comic a "Vendido" y hace que no se
	 * muestre en la bbdd
	 */
	public void venderComicBBDD(String id) {
		String sentenciaSQL;

		sentenciaSQL = "UPDATE comicsbbdd set estado = 'Vendido' where ID = ?";

		modificarDatos(id, sentenciaSQL);
	}

	/**
	 * Funcion que manda una querry de eliminar comic de la base de datos.
	 */
	public void eliminarComicBBDD(String id) {
		String sentenciaSQL;

		sentenciaSQL = "DELETE from comicsbbdd where ID = ?";

		modificarDatos(id, sentenciaSQL);
	}

	/**
	 * Funcion que permite obtener datos de la libreria de comics almacenada en la
	 * base de datos
	 *
	 * @param sentenciaSQL
	 * @return
	 */
	public ResultSet obtenLibreria(String sentenciaSQL) {
		conn = DBManager.conexion();
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
			ex.printStackTrace();
			nav.alertaException(ex.toString());
		} catch (SQLException ex) {
			ex.printStackTrace();
			nav.alertaException(ex.toString());
		}
		return null;
	}

	/**
	 * Funcion que modifica 1 comic de la base de datos con los parametros que
	 * introduzcamos en los campos.
	 */
	public void insertarDatos(String datos[]) {

		String sentenciaSQL = "insert into comicsbbdd(nomComic,numComic,nomVariante,firma,nomEditorial,formato,procedencia,anioPubli,nomGuionista,nomDibujante,puntuacion,portada,estado) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

		subirComic(sentenciaSQL, datos); // Llamada a funcion que permite comprobar el cambio realizado en el comic

	}

	/**
	 * Funcion que permite introducir un nuevo comic en la base de datos.
	 */
	public void subirComic(String sentenciaSQL, String datos[]) {

		utilidad = new Utilidades();

		InputStream portada = utilidad.direccionImagen(datos[10]);

		conn = DBManager.conexion();

		try {
			PreparedStatement statement = conn.prepareStatement(sentenciaSQL);

			statement.setString(1, datos[0]);
			statement.setString(2, datos[1]);
			statement.setString(3, datos[2]);
			statement.setString(4, datos[3]);
			statement.setString(5, datos[4]);
			statement.setString(6, datos[5]);
			statement.setString(7, datos[6]);
			statement.setString(8, datos[7]);
			statement.setString(9, datos[8]);
			statement.setString(10, datos[9]);
			statement.setString(11, "");

			if (datos[10].length() != 0) {
				statement.setBinaryStream(12, portada);
			} else {
				statement.setBinaryStream(12, portada);
			}
			statement.setString(13, datos[11]);

			statement.executeUpdate();
			statement.close();
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		} finally {
			try {
				portada.close();
				ejecucionPreparedStatement(reloadID());
			} catch (IOException e) {
				nav.alertaException(e.toString());
			}
		}
	}

	/**
	 * Comprueba que el ID introducido existe
	 *
	 * @return
	 */
	public boolean comprobarID(String ID) {

		if (ID.length() != 0) {
			if (chechID(ID)) {
				return true;
			} else {
				nav.alertaException("La ID no existe");
				return false;
			}
		}
		return false;
	}

	/**
	 * Funcion que comprueba si existe el ID introducido
	 *
	 * @param ps
	 * @return
	 */
	public void comprobarCambio(String datos[]) {

		conn = DBManager.conexion();
		String sentenciaSQL = "UPDATE comicsbbdd set nomComic = ?,numComic = ?,nomVariante = ?,"
				+ "Firma = ?,nomEditorial = ?,formato = ?,Procedencia = ?,anioPubli = ?,"
				+ "nomGuionista = ?,nomDibujante = ?,portada = ?,estado = ? where ID = ?";

		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sentenciaSQL);
			if (comprobarID(datos[0])) // Comprueba si la ID introducida existe en la base de datos
			{
				ps.setString(12, datos[0]);
				comicModificar(ps, datos); // Llama a funcion que permite cambiar los datos del comic

			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
	}

	/**
	 * Devuelve un objeto Comic con los nuevos datos de un comic. En caso de tener
	 * el espacio en blanco, el valor del parametro sera el que tenia originalmente.
	 *
	 * @param ps
	 * @return
	 */
	public void comicModificar(PreparedStatement ps, String datos[]) {
		utilidad = new Utilidades();
		listaTratamiento.clear();
		String nombre = "", numero = "", variante = "", firma = "", editorial = "", formato = "", procedencia = "",
				fecha = "", guionista = "", dibujante = "", estado = "";

		InputStream portada = utilidad.direccionImagen(datos[11]);
		Comic comic = comicDatos(datos[0]);
		try {

			if (datos[1].length() != 0) {
				ps.setString(1, datos[1]);
				nombre = datos[1];
			} else {
				nombre = comic.getNombre();
				ps.setString(1, nombre);
			}
			if (datos[2].length() != 0) {
				ps.setString(2, datos[2]);
				numero = datos[2];
			} else {
				numero = comic.getNumero();
				ps.setString(2, numero);
			}
			if (datos[3].length() != 0) {
				ps.setString(3, datos[3]);
				variante = datos[3];
			} else {
				variante = comic.getVariante();
				ps.setString(3, variante);
			}
			if (datos[4].length() != 0) {
				ps.setString(4, datos[4]);
				firma = datos[4];
			} else {
				firma = comic.getFirma();
				ps.setString(4, firma);
			}
			if (datos[5].length() != 0) {
				ps.setString(5, datos[5]);
				editorial = datos[5];
			} else {
				editorial = comic.getEditorial();
				ps.setString(5, editorial);
			}
			if (datos[6].length() != 0) {
				ps.setString(6, datos[6]);
				formato = datos[6];
			} else {
				formato = comic.getFormato();
				ps.setString(6, formato);
			}
			if (datos[7].length() != 0) {
				ps.setString(7, datos[7]);
				procedencia = datos[7];
			} else {
				procedencia = comic.getProcedencia();
				ps.setString(7, procedencia);
			}
			if (datos[8].length() != 0) {
				ps.setString(8, datos[8]);
				fecha = datos[8];
			} else {
				fecha = comic.getFecha();
				ps.setString(8, fecha);
			}
			if (datos[9].length() != 0) {
				ps.setString(9, datos[9]);
				guionista = datos[9];
			} else {
				guionista = comic.getGuionista();
				ps.setString(9, guionista);
			}
			if (datos[10].length() != 0) {
				ps.setString(10, datos[10]);
				dibujante = datos[10];
			} else {
				dibujante = comic.getDibujante();
				ps.setString(10, dibujante);
			}

			if (datos[11].length() != 0) {
				ps.setBinaryStream(11, portada);
			} else {
				ps.setBinaryStream(11, portada);
			}
			if (datos[12].length() != 0) {
				ps.setString(12, datos[12]);
				estado = datos[12];
			}

			ps.setString(13, datos[0]);

			if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, saltara el siguiente mensaje
				comic = new Comic("", nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
						guionista, dibujante, estado, "", null);

				listaTratamiento.add(comic);

			}
			ps.close();

		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		} finally {
			try {
				portada.close();
				ejecucionPreparedStatement(reloadID());
			} catch (IOException ex) {
				nav.alertaException(ex.toString());
			}
		}

	}

	/**
	 * Funcion que permite insertar una puntuacion a un comic segun la ID
	 * introducida.
	 */
	public void actualizarPuntuacion(String ID, String puntuacion) {

		String sentenciaSQL = "UPDATE comicsbbdd set puntuacion = ? where ID = ?";

		if (nav.alertaAgregarPuntuacion()) { // Llamada a alerta de modificacion

			comprobarOpinionInsertada(sentenciaSQL, ID, puntuacion); // Llamada a funcion que permite comprobar el
																		// cambio realizado en
			// el comic

		} else { // Si se cancela la opinion del comic, saltara el siguiente mensaje.

		}
	}

	/**
	 * Funcion que permite insertar una puntuacion a un comic segun la ID
	 * introducida.
	 */
	public void borrarPuntuacion(String ID) {

		String sentenciaSQL = "UPDATE comicsbbdd set puntuacion = 'Sin puntuacion' where ID = ?";

		if (nav.alertaBorrarPuntuacion()) { // Llamada a alerta de modificacion

			comprobarOpinionBorrada(sentenciaSQL, ID); // Llamada a funcion que permite comprobar el cambio realizado en
														// el
														// comic

		} else { // Si se cancela la opinion del comic, saltara el siguiente mensaje.

		}
	}

	/**
	 * Funcion que comprueba si la opinion se ha introducida correctamente
	 *
	 * @param ps
	 * @return
	 */
	public void comprobarOpinionInsertada(String sentenciaSQL, String ID, String puntuacion) {

		conn = DBManager.conexion();
		listaTratamiento.clear();
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sentenciaSQL);
			if (comprobarID(ID)) // Comprueba si la ID introducida existe en la base de datos
			{
				Comic comic = comicDatos(ID);
				ps.setString(1, puntuacion);
				ps.setString(2, ID);

				if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, saltara el siguiente mensaje
					listaTratamiento.add(comic);
				}
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
	}

	/**
	 * Funcion que comprueba si la opinion se ha introducida correctamente
	 *
	 * @param ps
	 * @return
	 */
	public void comprobarOpinionBorrada(String sentenciaSQL, String ID) {

		conn = DBManager.conexion();
		listaTratamiento.clear();
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sentenciaSQL);
			if (comprobarID(ID)) // Comprueba si la ID introducida existe en la base de datos
			{
				Comic comic = comicDatos(ID);
				ps.setString(1, ID);

				if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, saltara el siguiente mensaje
					listaTratamiento.add(comic);
				}
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
	}

	/**
	 * Funcion que permite mostrar imagen de una portada cuando se clickea con el
	 * raton encima del comic seleccionado
	 *
	 * @param ID
	 * @return
	 */
	public Image selectorImage(String ID) {
		conn = DBManager.conexion();

		String sentenciaSQL = "SELECT * FROM comicsbbdd where ID = ?";

		try {

			PreparedStatement statement = conn.prepareStatement(sentenciaSQL);
			statement.setString(1, ID);

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {

				InputStream is = rs.getBinaryStream("portada");
				if (!rs.wasNull()) {
					OutputStream os = new FileOutputStream(new File("tmp.jpg"));
					byte[] content = new byte[1024];

					int size = 0;

					while ((size = is.read(content)) != -1) {

						os.write(content, 0, size);
					}

					os.close();
					is.close();
				}
			}

		} catch (IOException | SQLException e) {
			nav.alertaException(e.toString());
		}

		Image imagen = new Image("file:tmp.jpg", 250, 250, true, true);

		return imagen;
	}

	/**
	 * Funcion que muestra todos los comics de la base de datos
	 *
	 * @return
	 */
	public List<Comic> libreriaCompleta() {

		List<Comic> listComic = FXCollections.observableArrayList(verLibreriaPosesion());

		if (listComic.size() == 0) {

			String excepcion = "No hay ningun comic guardado en la base de datos";
			nav.alertaException(excepcion);
		}

		return listComic;
	}

	/**
	 * Funcion que busca en el arrayList el o los comics que tengan coincidencia con
	 * los datos introducidos en el TextField
	 *
	 * @param comic
	 * @return
	 */
	public List<Comic> busquedaParametro(Comic comic, String busquedaGeneral) {

		List<Comic> listComic = FXCollections.observableArrayList(verLibreriaPosesion());

		if (listComic.size() != 0) {
			if (busquedaGeneral.length() != 0) {
				listComic = FXCollections.observableArrayList(verBusquedaGeneral(busquedaGeneral));
			} else {
				listComic = FXCollections.observableArrayList(filtadroBBDD(comic));
			}
		} else {
			String excepcion = "No hay ningun comic guardado en la base de datos";
			nav.alertaException(excepcion);
		}

		return listComic;
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return
	 */
	public List<Comic> libreriaVendidos() {

		List<Comic> listComic = FXCollections.observableArrayList(verLibreriaVendidos());
		if (listComic.size() == 0) {
			String excepcion = "No hay comics en vendidos";
			nav.alertaException(excepcion);
		}

		return listComic;
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return
	 */
	public List<Comic> libreriaEnVenta() {
		List<Comic> listComic = FXCollections.observableArrayList(verLibreriaEnVenta());

		if (listComic.size() == 0) {
			String excepcion = "No hay comics en venta";
			nav.alertaException(excepcion);
		}
		return listComic;
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return
	 */
	public List<Comic> libreriaPuntuacion() {
		List<Comic> listComic = FXCollections.observableArrayList(verLibreriaPuntuacion());
		if (listComic.size() == 0) {
			String excepcion = "No hay comics puntuados";
			nav.alertaException(excepcion);
		}
		return listComic;
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * firmados
	 *
	 * @return
	 */
	public List<Comic> libreriaFirmados() {
		List<Comic> listComic = FXCollections.observableArrayList(verLibreriaFirmados());
		if (listComic.size() == 0) {
			String excepcion = "No hay comics firmados";
			nav.alertaException(excepcion);
		}
		return listComic;
	}
}