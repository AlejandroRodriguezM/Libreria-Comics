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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Funcionamiento.Comic;
import Funcionamiento.FuncionesExcel;
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
	public static List<Comic> listaComicsCheck = new ArrayList<>();

	public static List<String> listaNombre = new ArrayList<>();
	public static List<String> listaNumeroComic = new ArrayList<>();
	public static List<String> listaVariante = new ArrayList<>();
	public static List<String> listaFirma = new ArrayList<>();
	public static List<String> listaFormato = new ArrayList<>();
	public static List<String> listaEditorial = new ArrayList<>();
	public static List<String> listaGuionista = new ArrayList<>();
	public static List<String> listaDibujante = new ArrayList<>();
	public static List<String> listaFecha = new ArrayList<>();
	public static List<String> listaProcedencia = new ArrayList<>();
	public static List<String> listaCaja = new ArrayList<>();

	private static Ventanas nav = new Ventanas();
	private static Connection conn = null;
	private static Utilidades utilidad = new Utilidades();
	private static FuncionesExcel carpeta = new FuncionesExcel();

	/**
	 * @throws SQLException
	 *
	 */
	public void listasAutoCompletado() throws SQLException {
		listaNombre();
		listaVariante();
		listaFirma();
		listaFormato();
		listaEditorial();
		listaGuionista();
		listaDibujante();
		listaProcedencia();
		listaCajas();
		listaNumeroComic();
	}

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
				nav.alertaException(e.toString());
			}
		}
		return 0;
	}

	/**
	 * Borra el contenido de la base de datos.
	 *
	 * @return
	 * @throws SQLException
	 */
	public String[] deleteTable() throws SQLException {
		String sentencia[] = new String[2];
		sentencia[0] = "delete from comicsbbdd";
		sentencia[1] = "alter table comicsbbdd AUTO_INCREMENT = 1;";

		utilidad.copia_seguridad();
		utilidad.eliminarArchivosEnCarpeta();
		listaNombre.clear();
		listaNumeroComic.clear();
		listaVariante.clear();
		listaFirma.clear();
		listaEditorial.clear();
		listaGuionista.clear();
		listaDibujante.clear();
		listaFecha.clear();
		listaFormato.clear();
		listaProcedencia.clear();
		listaCaja.clear();
		return sentencia;
	}

	/**
	 * Función que ejecuta un conjunto de sentencias PreparedStatement en la base de
	 * datos.
	 * 
	 * @param sentencia Un arreglo de cadenas que contiene las sentencias SQL a
	 *                  ejecutar.
	 * @return true si las sentencias se ejecutaron correctamente, false en caso
	 *         contrario.
	 */
	public boolean ejecucionPreparedStatement(String[] sentencia) {
		Connection conn = DBManager.conexion();

		try {
			PreparedStatement statement1 = conn.prepareStatement(sentencia[0]);
			PreparedStatement statement2 = conn.prepareStatement(sentencia[1]);
			statement1.executeUpdate();
			statement2.executeUpdate();
			reiniciarBBDD();
			statement1.close();
			statement2.close();
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
			fileChooser.getExtensionFilters()
					.addAll(new FileChooser.ExtensionFilter("MySqlDump only", "mysqldump.exe"));
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

		int numGrapas, numDuro, numBlando, numLibros, numUsa, numEsp, numMarvel, numDC, numPanini, numDarkHorse,
				numImage, numMangas, leidos, comprados, posesion, firmados, total;

		String procedimientos[] = { "call numeroGrapas()", "call numeros_tapa_dura()", "call numeros_tapa_blanda()",
				"call numeros_libros()", "call numeroSpain()", "call numeroUSA()", "call total()",
				"call numeroPanini()", "call numeroMarvel()", "call numeroDC()", "call numeroDarkHorse()",
				"call numeroImage()", "call numeroMangas()", "call comicsLeidos()", "call comicsComprados",
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
			ResultSet rs15 = ejecucionSQL(procedimientos[14]); // Executa el procedimiento almacenado
			ResultSet rs16 = ejecucionSQL(procedimientos[15]); // Executa el procedimiento almacenado
			ResultSet rs17 = ejecucionSQL(procedimientos[16]); // Executa el procedimiento almacenado

			// Si no hay dato que comprobar, devolvera un 0
			if (rs1.next()) {
				numGrapas = rs1.getInt(1);
			} else {
				numGrapas = 0;
			}
			if (rs2.next()) {
				numDuro = rs2.getInt(1);
			} else {
				numDuro = 0;
			}
			if (rs3.next()) {
				numBlando = rs3.getInt(1);
			} else {
				numBlando = 0;
			}
			if (rs4.next()) {
				numLibros = rs4.getInt(1);
			} else {
				numLibros = 0;
			}
			if (rs5.next()) {
				numEsp = rs5.getInt(1);
			} else {
				numEsp = 0;
			}
			if (rs6.next()) {
				numUsa = rs6.getInt(1);
			} else {
				numUsa = 0;
			}
			if (rs7.next()) {
				total = rs7.getInt(1);
			} else {
				total = 0;
			}
			if (rs8.next()) {
				numPanini = rs8.getInt(1);
			} else {
				numPanini = 0;
			}
			if (rs9.next()) {
				numMarvel = rs9.getInt(1);
			} else {
				numMarvel = 0;
			}

			if (rs10.next()) {
				numDC = rs10.getInt(1);
			} else {
				numDC = 0;
			}
			if (rs11.next()) {
				numDarkHorse = rs11.getInt(1);
			} else {
				numDarkHorse = 0;
			}
			if (rs12.next()) {
				numImage = rs12.getInt(1);
			} else {
				numImage = 0;
			}
			if (rs13.next()) {
				numMangas = rs13.getInt(1);
			} else {
				numMangas = 0;
			}
			if (rs14.next()) {
				leidos = rs14.getInt(1);
			} else {
				leidos = 0;
			}
			if (rs15.next()) {
				comprados = rs15.getInt(1);
			} else {
				comprados = 0;
			}
			if (rs16.next()) {
				posesion = rs16.getInt(1);
			} else {
				posesion = 0;
			}
			if (rs17.next()) {
				firmados = rs17.getInt(1);
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
			rs15.close();
			rs16.close();
			rs17.close();

			return "Numero de grapas: " + numGrapas + "\nNumero de tomos de tapa dura: " + numDuro
					+ "\nNumeros de tomos de tapa blanda: " + numBlando + "\nNumeros de libros: " + numLibros
					+ "\nNumeros de comics en Castellano: " + numEsp + "\nNumero de comics en USA: " + numUsa
					+ "\nNumero de comics Marvel: " + numMarvel + "\nNumero de comics DC: " + numDC
					+ "\nNumero de comics Dark horse: " + numDarkHorse + "\nNumero de comics de Panini: " + numPanini
					+ "\nNumero de comics de Image Comics: " + numImage + "\nNumero de mangas: " + numMangas
					+ "\nComics leidos: " + leidos + "\nComics comprados: " + comprados + "\nComics en posesion: "
					+ posesion + "\nComics firmados: " + firmados + "\nTotal: " + total;

		} catch (SQLException e) {
			nav.alertaException(e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Permite devolver datos de la base de datos segun la query en parametros
	 *
	 * @param procedimiento
	 * @return
	 * @throws SQLException
	 */
	public ResultSet ejecucionSQL(String procedimiento) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		Connection conn = DBManager.conexion();

		try {
			st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = st.executeQuery(procedimiento);
			return rs;
		} catch (SQLException e) {
			nav.alertaException(e.toString());
		}
		return null;
	}

	/**
	* Función que guarda los datos para autocompletado en una lista.
	* @param sentenciaSQL La sentencia SQL para obtener los datos.
	* @param columna El nombre de la columna que contiene los datos para autocompletado.
	* @return Una lista de cadenas con los datos para autocompletado.
	* @throws SQLException Si ocurre algún error al ejecutar la consulta SQL.
	*/
	private List<String> guardarDatosAutoCompletado(String sentenciaSQL, String columna) throws SQLException {
		List<String> listaAutoCompletado = new ArrayList<>();
		listaAutoCompletado.clear();
		ResultSet rs = obtenLibreria(sentenciaSQL);
		try {
			if (verLibreria("SELECT * FROM comicsbbdd").size() != 0) {
				do {
					String datosAutocompletado = rs.getString(columna);
					if (columna.equals("nomComic")) {
						listaAutoCompletado.add(datosAutocompletado.trim());
					} else {
						String[] nombres = datosAutocompletado.split("-"); // Dividir nombres separados por "-"
						for (String nombre : nombres) {
							nombre = nombre.trim(); // Eliminar espacios al inicio y final
							if (!nombre.isEmpty()) {
								listaAutoCompletado.add(nombre); // Agregar a la lista solo si no está vacío
							}
						}
					}
				} while (rs.next());
				listaAutoCompletado = Utilidades.listaArregladaAutoComplete(listaAutoCompletado);
				

				
				return listaAutoCompletado;
			}
		} catch (SQLException e) {
			nav.alertaException(e.toString());
		}
		return listaAutoCompletado;
	}

	// **************************************//
	// ****FUNCIONES DE LA LIBRERIA**********//
	// **************************************//



	/**
	 * Método estático que obtiene el último ID de la tabla "comicsbbdd".
	 * 
	 * @return El último ID como una cadena de texto.
	 * @throws SQLException Si ocurre algún error al ejecutar la consulta SQL.
	 */
	public static String obtener_ultimo_id() throws SQLException {
		String query = "SELECT ID FROM comicsbbdd ORDER BY ID DESC LIMIT 1";
		PreparedStatement ps = null;
		ResultSet resultado;
		String ultimoId = "1"; // Valor predeterminado si no se encuentra ningún resultado

		try {
			ps = conn.prepareStatement(query);
			resultado = ps.executeQuery();

			if (resultado.next()) {
				int id = Integer.parseInt(resultado.getString("ID"));
				ultimoId = String.valueOf(id + 1);
			}

			resultado.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ps.close();
		}

		return ultimoId;
	}

	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<String> listaNombre() throws SQLException {

		String sentenciaSQL = "SELECT nomComic from comicsbbdd ORDER BY nomComic ASC";
		String columna = "nomComic";
		reiniciarBBDD();
		listaNombre = guardarDatosAutoCompletado(sentenciaSQL, columna);
		return listaNombre;

	}
	
	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<String> listaNumeroComic() throws SQLException {

		String sentenciaSQL = "SELECT numComic from comicsbbdd ORDER BY numComic ASC";
		String columna = "numComic";
		reiniciarBBDD();
		listaNumeroComic = guardarDatosAutoCompletado(sentenciaSQL, columna);
				
		return listaNumeroComic;

	}
	
	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<String> listaCajas() throws SQLException {

		String sentenciaSQL = "SELECT caja_deposito FROM comicsbbdd ORDER BY caja_deposito ASC";
		String columna = "caja_deposito";
		reiniciarBBDD();
		listaCaja = guardarDatosAutoCompletado(sentenciaSQL, columna);
		return listaCaja;

	}
	
	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<String> listaProcedencia() throws SQLException {

		String sentenciaSQL = "SELECT procedencia from comicsbbdd ORDER BY procedencia ASC";
		String columna = "procedencia";
		reiniciarBBDD();
		listaProcedencia = guardarDatosAutoCompletado(sentenciaSQL, columna);
		return listaProcedencia;
	}
	
	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<String> listaVariante() throws SQLException {

		String sentenciaSQL = "SELECT nomVariante from comicsbbdd ORDER BY nomVariante ASC";
		String columna = "nomVariante";
		reiniciarBBDD();
		listaVariante = guardarDatosAutoCompletado(sentenciaSQL, columna);
		return listaVariante;
	}

	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<String> listaFirma() throws SQLException {

		String sentenciaSQL = "SELECT firma from comicsbbdd ORDER BY firma ASC";
		String columna = "firma";
		reiniciarBBDD();
		listaFirma = guardarDatosAutoCompletado(sentenciaSQL, columna);
		return listaFirma;
	}

	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<String> listaFormato() throws SQLException {

		String sentenciaSQL = "SELECT formato from comicsbbdd ORDER BY formato ASC";
		String columna = "formato";
		reiniciarBBDD();
		listaFormato = guardarDatosAutoCompletado(sentenciaSQL, columna);
		return listaFormato;
	}

	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<String> listaEditorial() throws SQLException {

		String sentenciaSQL = "SELECT nomEditorial from comicsbbdd ORDER BY nomEditorial ASC";
		String columna = "nomEditorial";
		reiniciarBBDD();
		listaEditorial = guardarDatosAutoCompletado(sentenciaSQL, columna);
		return listaEditorial;
	}

	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<String> listaGuionista() throws SQLException {

		String sentenciaSQL = "SELECT nomGuionista from comicsbbdd ORDER BY nomGuionista ASC";
		String columna = "nomGuionista";
		reiniciarBBDD();
		listaGuionista = guardarDatosAutoCompletado(sentenciaSQL, columna);
		return listaGuionista;
	}

	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<String> listaDibujante() throws SQLException {

		String sentenciaSQL = "SELECT nomDibujante from comicsbbdd ORDER BY nomDibujante ASC";
		String columna = "nomDibujante";
		reiniciarBBDD();
		listaDibujante = guardarDatosAutoCompletado(sentenciaSQL, columna);
		return listaDibujante;
	}

	/**
	 * Método que muestra los cómics de la librería según la sentencia SQL
	 * proporcionada.
	 * 
	 * @param sentenciaSQL La sentencia SQL para obtener los cómics de la librería.
	 * @return Una lista de objetos Comic que representan los cómics de la librería.
	 * @throws SQLException Si ocurre algún error al ejecutar la consulta SQL.
	 */
	public List<Comic> verLibreria(String sentenciaSQL) throws SQLException {
	    listaComics.clear(); // Limpiar la lista existente de cómics

	    ResultSet rs = obtenLibreria(sentenciaSQL); // Obtener el ResultSet

	    if (rs != null) {
	        listaDatos(rs); // Llenar la lista de cómics con los datos del ResultSet
	    }

	    return listaComics;
	}


	/**
	 * Devuelve datos de la base de datos segun el parametro.
	 *
	 * @param datos
	 * @return
	 */
	public List<Comic> filtadroBBDD(Comic datos) {

		reiniciarBBDD();

		String sql = datosConcatenados(datos);
		Connection conn = DBManager.conexion();

		listaComics.clear();

		if (sql.length() != 0) {
			try {
				PreparedStatement ps = conn.prepareStatement(sql);

				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					listaDatos(rs);
				}
				return listaComics;
			} catch (SQLException ex) {
				nav.alertaException(ex.toString());
			}
		}
		return null;
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
		if (comic.getNumCaja().length() != 0) {
			sql.append(connector).append("caja_deposito like'%" + comic.getNumCaja() + "%'");
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
			sql.append(connector).append("fecha_publicacion like'%" + comic.getFecha() + "%'");
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
			System.out.println(sql.toString());
			return sql.toString();
		}

		return "";
	}

	/**
	 * Funcion que permite hacer una busqueda general mediante 1 sola palabra, hace
	 * una busqueda en ciertos identificadores de la tabla.
	 *
	 * @param sentencia
	 * @return
	 * @throws SQLException
	 */
	public List<Comic> verBusquedaGeneral(String busquedaGeneral) throws SQLException {
		Connection conn = DBManager.conexion();
		String sql1 = datosGeneralesNombre(busquedaGeneral);
		String sql2 = datosGeneralesVariante(busquedaGeneral);
		String sql3 = datosGeneralesFirma(busquedaGeneral);
		String sql4 = datosGeneralesGuionista(busquedaGeneral);
		String sql5 = datosGeneralesDibujante(busquedaGeneral);

		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;

		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		ResultSet rs5 = null;

		reiniciarBBDD();

		listaComics.clear();

		try {
			ps1 = conn.prepareStatement(sql1);
			ps2 = conn.prepareStatement(sql2);
			ps3 = conn.prepareStatement(sql3);
			ps4 = conn.prepareStatement(sql4);
			ps5 = conn.prepareStatement(sql5);

			rs1 = ps1.executeQuery();
			rs2 = ps2.executeQuery();
			rs3 = ps3.executeQuery();
			rs4 = ps4.executeQuery();
			rs5 = ps5.executeQuery();

			if (rs1.next()) {
				listaDatos(rs1);
			}
			if (rs2.next()) {
				listaDatos(rs2);
			}
			if (rs3.next()) {
				listaDatos(rs3);
			}
			if (rs4.next()) {
				listaDatos(rs4);
			}
			if (rs5.next()) {
				listaDatos(rs5);
			}

			listaComics = Utilidades.listaArreglada(listaComics);
			return listaComics;

		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		} finally {
			ps1.close();
			ps2.close();
			ps3.close();
			ps4.close();
			ps5.close();

			rs1.close();
			rs2.close();
			rs3.close();
			rs4.close();
			rs5.close();
		}
		return null;
	}

	/**
	 * Funcion que hace una busqueda de un identificador en concreto de la tabla
	 *
	 * @param datos
	 * @return
	 */
	public String datosGeneralesNombre(String busquedaGeneral) {
		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd ");

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
		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd ");

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
		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd ");

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
		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd ");

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
		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd ");

		sql.append(connector).append("nomDibujante like'%" + busquedaGeneral + "%'");

		return sql.toString();
	}

	/**
	 * Crea una lista de objetos Comic a partir del ResultSet proporcionado.
	 *
	 * @param rs el ResultSet con los datos de los cómics
	 * @return una lista de objetos Comic
	 */
	/**
	 * Devuelve solamente 1 comics de la base de datos.
	 *
	 * @param rs
	 * @return
	 */
	public Comic listaDatos(ResultSet rs) {
		Comic comic = new Comic();

		try {
			if (rs != null) {
				do {
					this.ID = rs.getString("ID");
					this.nombre = rs.getString("nomComic");
					this.numCaja = rs.getString("caja_deposito");
					this.numero = rs.getString("numComic");
					this.variante = rs.getString("nomVariante");
					this.firma = rs.getString("firma");
					
//	                if (this.firma.length() >= 9) {
//	                    int mitad = this.firma.length() / 2;
//	                    String parte1 = this.firma.substring(0, mitad);
//	                    String parte2 = this.firma.substring(mitad);
//
//	                    this.firma = parte1 + "\n " + parte2;
//	                }
					
					this.editorial = rs.getString("nomEditorial");
					this.formato = rs.getString("formato");
					this.procedencia = rs.getString("procedencia");
					this.fecha = rs.getString("fecha_publicacion");
					this.guionista = rs.getString("nomGuionista");
					this.dibujante = rs.getString("nomDibujante");
					this.estado = rs.getString("estado");
					this.puntuacion = rs.getString("puntuacion");
					this.imagen = rs.getString("portada");

					comic = new Comic(this.ID, this.nombre, this.numCaja, this.numero, this.variante, this.firma,
							this.editorial, this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante,
							this.estado, this.puntuacion, this.imagen);

					listaComics.add(comic);
				} while (rs.next());
			}
		} catch (SQLException e) {
			nav.alertaException("Datos introducidos incorrectos.");
			e.printStackTrace();
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
		listaComics.clear();
	}

	/**
	 * Devuelve un objeto Comic cuya ID coincida con el parámetro de búsqueda.
	 *
	 * @param identificador el ID del cómic a buscar
	 * @return el objeto Comic encontrado, o null si no se encontró ningún cómic con
	 *         ese ID
	 * @throws SQLException si ocurre algún error al ejecutar la consulta SQL
	 */
	public Comic comicDatos(String identificador) throws SQLException {
		Comic comic = null;

		String sentenciaSQL = "SELECT * FROM comicsbbdd WHERE ID = ?";

		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			statement = conn.prepareStatement(sentenciaSQL);
			statement.setString(1, identificador);
			rs = statement.executeQuery();

			if (rs.next()) {
				String ID = rs.getString("ID");
				String nombre = rs.getString("nomComic");
				String numCaja = rs.getString("caja_deposito");
				String numero = rs.getString("numComic");
				String variante = rs.getString("nomVariante");
				String firma = rs.getString("firma");
				String editorial = rs.getString("nomEditorial");
				String formato = rs.getString("formato");
				String procedencia = rs.getString("procedencia");
				String fecha = rs.getString("fecha_publicacion");
				String guionista = rs.getString("nomGuionista");
				String dibujante = rs.getString("nomDibujante");
				String estado = rs.getString("estado");
				String puntuacion = rs.getString("puntuacion");
				String imagen = rs.getString("portada");

				comic = new Comic(ID, nombre, numCaja, numero, variante, firma, editorial, formato, procedencia, fecha,
						guionista, dibujante, estado, puntuacion, imagen);
			}
		} catch (SQLException e) {
			nav.alertaException(e.toString());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				statement.close();
			}
		}

		return comic;
	}

	/**
	 * Comprueba que el ID introducido existe
	 *
	 * @return
	 * @throws SQLException
	 */
	public boolean checkID(String identificador) throws SQLException {
		if (identificador.length() == 0) {
			return false;
		}

		String sentenciaSQL = "SELECT * FROM comicsbbdd WHERE ID = ?";
		conn = DBManager.conexion();
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement(sentenciaSQL);
			preparedStatement.setString(1, identificador);
			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			nav.alertaException("No existe el " + identificador + " en la base de datos.");
		} finally {
			preparedStatement.close();
			rs.close();
		}

		return false;
	}

	/**
	 * Funcion que permite generar imagenes de formato JPG a la hora de exportar la
	 * base de datos excel.
	 * 
	 * @throws SQLException
	 */
	public void saveImageFromDataBase() throws SQLException {
		String sentenciaSQL = "SELECT * FROM comicsbbdd";

		conn = DBManager.conexion();
		carpeta = new FuncionesExcel();
		File directorio = carpeta.carpetaPortadas();
		InputStream input = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = conn.prepareStatement(sentenciaSQL);
			rs = preparedStatement.executeQuery();

			if (directorio != null) {
				while (rs.next()) {
					String direccionImagen = rs.getString(14);

					String nombreImagen = utilidad.obtenerNombreArchivo(direccionImagen);

					File imagenArchivo = new File(direccionImagen);

					if (!imagenArchivo.exists()) {
						input = getClass().getResourceAsStream("sinPortada.jpg");
						if (input == null) {
							throw new FileNotFoundException("La imagen predeterminada no se encontró en el paquete");
						}
						imagenArchivo = File.createTempFile("tmp", ".jpg");
						imagenArchivo.deleteOnExit();
						try (OutputStream output = new FileOutputStream(imagenArchivo)) {
							byte[] buffer = new byte[4096];
							int bytesRead;
							while ((bytesRead = input.read(buffer)) != -1) {
								output.write(buffer, 0, bytesRead);
							}
						}
					} else {
						FileInputStream fileInputStream = new FileInputStream(imagenArchivo);
						FileOutputStream fileOutputStream = new FileOutputStream(
								directorio.getAbsolutePath() + "/" + nombreImagen);

						byte[] buffer = new byte[4096];
						int bytesRead;
						while ((bytesRead = fileInputStream.read(buffer)) != -1) {
							fileOutputStream.write(buffer, 0, bytesRead);
						}
						fileInputStream.close();
						fileOutputStream.close();

					}
				}
			}
		} catch (SQLException | IOException e) {
			nav.alertaException(e.toString());
		} finally {
			preparedStatement.close();
			rs.close();
		}
	}

	/**
	 * Permite modificar un comic de la base de datos
	 *
	 * @param id
	 * @param sentenciaSQL
	 * @throws SQLException
	 */
	public void modificarDatos(String id, String sentenciaSQL) throws SQLException {
		PreparedStatement stmt = null;
		Connection conn = DBManager.conexion();
		String direccion_portada = obtenerDireccionPortada(id);
		Utilidades.eliminarFichero(direccion_portada);
		try {
			if (id.length() != 0) {
				stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE); // Permite leer y ejecutar la sentencia de MySql

				stmt.setString(1, id);
				if (stmt.executeUpdate() == 1) {
				}
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		} finally {
			stmt.close();
		}
	}

	/**
	 * Funcion que permite cambiar de estado el comic a "Vendido" y hace que no se
	 * muestre en la bbdd
	 * 
	 * @throws SQLException
	 */
	public void venderComicBBDD(String id) throws SQLException {
		String sentenciaSQL;

		sentenciaSQL = "UPDATE comicsbbdd set estado = 'Vendido' where ID = ?";

		modificarDatos(id, sentenciaSQL);
	}

	/**
	 * Funcion que permite cambiar de estado el comic a "Vendido" y hace que no se
	 * muestre en la bbdd
	 * 
	 * @throws SQLException
	 */
	public void enVentaComicBBDD(String id) throws SQLException {
		String sentenciaSQL;

		sentenciaSQL = "UPDATE comicsbbdd set estado = 'En venta' where ID = ?";

		modificarDatos(id, sentenciaSQL);
	}

	/**
	 * Funcion que manda una querry de eliminar comic de la base de datos.
	 * 
	 * @throws SQLException
	 */
	public void eliminarComicBBDD(String id) throws SQLException {
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
	 * @throws SQLException
	 */
	public ResultSet obtenLibreria(String sentenciaSQL) throws SQLException {

		conn = DBManager.conexion();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			rs = stmt.executeQuery();
			if (!rs.first()) {
				return null;
			}
			
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
	 * Inserta los datos de un cómic en la base de datos.
	 *
	 * @param comic_datos los datos del cómic a insertar
	 * @throws IOException  si ocurre un error al manejar el archivo de imagen
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public void insertarDatos(Comic comic_datos) throws IOException, SQLException {
		String sentenciaSQL = "INSERT INTO comicsbbdd (nomComic, caja_deposito, numComic, nomVariante, firma, nomEditorial, formato, procedencia, fecha_publicacion, nomGuionista, nomDibujante, puntuacion, portada, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		subirComic(sentenciaSQL, comic_datos);
	}

	/**
	 * Permite introducir un nuevo cómic en la base de datos.
	 *
	 * @param sentenciaSQL la sentencia SQL para insertar el cómic
	 * @param datos        los datos del cómic a insertar
	 * @throws IOException  si ocurre un error al manejar el archivo de imagen
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public void subirComic(String sentenciaSQL, Comic datos) throws IOException, SQLException {
		conn = DBManager.conexion();
		PreparedStatement statement = null;

		try {
			statement = conn.prepareStatement(sentenciaSQL);
			statement.setString(1, datos.getNombre());
			if (datos.getNumCaja() == null) {
				statement.setString(2, "0");
			} else {
				statement.setString(2, datos.getNumCaja());
			}
			statement.setString(3, datos.getNumero());
			statement.setString(4, datos.getVariante());
			statement.setString(5, datos.getFirma());
			statement.setString(6, datos.getEditorial());
			statement.setString(7, datos.getFormato());
			statement.setString(8, datos.getProcedencia());
			statement.setString(9, datos.getFecha());
			statement.setString(10, datos.getGuionista());
			statement.setString(11, datos.getDibujante());
			statement.setString(12, "Sin puntuar");
			statement.setString(13, datos.getImagen());
			statement.setString(14, "En posesion");

			statement.executeUpdate();
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	/**
	 * Comprueba si el ID introducido existe en la base de datos.
	 *
	 * @param ID el ID a comprobar
	 * @return true si el ID existe, false en caso contrario
	 */
	public boolean comprobarID(String ID) {
		if (ID.isEmpty()) {
			return false;
		}

		try {
			if (checkID(ID)) {
				return true;
			} else {
				nav.alertaException("La ID no existe");
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}

		return false;
	}

	/**
	 * Funcion que comprueba si existe el ID introducido
	 *
	 * @param ps
	 * @return
	 */
	public void actualizar_comic(Comic datos) {
		utilidad = new Utilidades();
		String sentenciaSQL = "UPDATE comicsbbdd set nomComic = ?,caja_deposito = ? ,numComic = ?,nomVariante = ?,"
				+ "Firma = ?,nomEditorial = ?,formato = ?,Procedencia = ?,fecha_publicacion = ?,"
				+ "nomGuionista = ?,nomDibujante = ?,portada = ?,estado = ? where ID = ?";

		try {
			if (comprobarID(datos.getID())) { // Comprueba si la ID introducida existe en la base de datos
				comicModificar(sentenciaSQL, datos); // Llama a funcion que permite cambiar los datos del comic
			}
		} catch (SQLException | IOException ex) {
			nav.alertaException(ex.toString());
		} finally {
			DBManager.resetConnection();
		}
	}

	/**
	 * Modifica la dirección de la portada de un cómic en la base de datos.
	 *
	 * @param nombre_completo el nombre completo de la imagen de la portada
	 * @param ID              el ID del cómic a modificar
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public void modificar_direccion_portada(String nombre_completo, String ID) throws SQLException {
		utilidad = new Utilidades();
		String extension = ".jpg";
		String nuevoNombreArchivo = nombre_completo + extension;

		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String imagePath = documentsPath + File.separator + "libreria_comics" + File.separator
				+ utilidad.obtenerDatoDespuesDeDosPuntos("Database") + File.separator + "portadas" + File.separator
				+ nuevoNombreArchivo;

		String sql = "UPDATE comicsbbdd SET portada = ? WHERE ID = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, imagePath);
			ps.setString(2, ID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Modifica los datos de un cómic en la base de datos.
	 *
	 * @param sentenciaSQL la sentencia SQL para modificar el cómic
	 * @param datos        los nuevos datos del cómic
	 * @throws SQLException si ocurre un error al acceder a la base de datos
	 * @throws IOException  si ocurre un error de lectura/escritura al manejar las
	 *                      imágenes
	 */
	public void comicModificar(String sentenciaSQL, Comic datos) throws SQLException, IOException {
		utilidad = new Utilidades();
		listaComics.clear();

		String ID = datos.getID();
		String nombre = datos.getNombre();
		String numCaja = datos.getNumCaja();
		String numero = datos.getNumero();
		String variante = datos.getVariante();
		String firma = datos.getFirma();
		String editorial = datos.getEditorial();
		String formato = datos.getFormato();
		String procedencia = datos.getProcedencia();
		String fecha = datos.getFecha();
		String guionista = datos.getGuionista();
		String dibujante = datos.getDibujante();
		String estado = datos.getEstado();
		String portada_final = datos.getImagen();
		String codigo_imagen = Utilidades.generarCodigoUnico(portada_final + File.separator);

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sentenciaSQL);

			ps.setString(1, nombre);
			ps.setString(2, numCaja);
			ps.setString(3, numero);
			ps.setString(4, variante);
			ps.setString(5, firma);
			ps.setString(6, editorial);
			ps.setString(7, formato);
			ps.setString(8, procedencia);
			ps.setString(9, fecha);
			ps.setString(10, guionista);
			ps.setString(11, dibujante);
			ps.setString(12, portada_final);
			ps.setString(13, estado);
			ps.setString(14, ID);

			if (ps.executeUpdate() == 1) {
				Comic nuevo_comic = new Comic("", nombre, numCaja, numero, variante, firma, editorial, formato,
						procedencia, fecha, guionista, dibujante, estado, "", portada_final);

				String carpeta = Utilidades.eliminarDespuesUltimoPortadas(codigo_imagen);
				utilidad.nueva_imagen(portada_final, carpeta);
				modificar_direccion_portada(codigo_imagen, datos.getID());

				listaComics.add(nuevo_comic);
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		} finally {
			if (ps != null) {
				ps.close();
			}
			DBManager.resetConnection();
		}
	}

	/**
	 * Funcion que permite insertar una puntuacion a un comic segun la ID
	 * introducida.
	 * 
	 * @throws SQLException
	 */
	public void actualizarPuntuacion(String ID, String puntuacion) throws SQLException {

		String sentenciaSQL = "UPDATE comicsbbdd set puntuacion = ? where ID = ?";

		if (nav.alertaAgregarPuntuacion()) { // Llamada a alerta de modificacion

			comprobarOpinionInsertada(sentenciaSQL, ID, puntuacion);
		}
	}

	/**
	 * Funcion que permite insertar una puntuacion a un comic segun la ID
	 * introducida.
	 * 
	 * @throws SQLException
	 */
	public void borrarPuntuacion(String ID) throws SQLException {

		String sentenciaSQL = "UPDATE comicsbbdd set puntuacion = 'Sin puntuar' where ID = ?";

		if (nav.alertaBorrarPuntuacion()) { // Llamada a alerta de modificacion

			comprobarOpinionBorrada(sentenciaSQL, ID); // Llamada a funcion que permite comprobar el cambio realizado en
			// el
			// comic
		}
	}

	/**
	 * Funcion que comprueba si la opinion se ha introducida correctamente
	 *
	 * @param ps
	 * @return
	 * @throws SQLException
	 */
	public void comprobarOpinionInsertada(String sentenciaSQL, String ID, String puntuacion) throws SQLException {

		conn = DBManager.conexion();
		listaComics.clear();
		PreparedStatement ps = null;
		try {

			ps = conn.prepareStatement(sentenciaSQL);
			if (comprobarID(ID)) // Comprueba si la ID introducida existe en la base de datos
			{
				Comic comic = comicDatos(ID);
				ps.setString(1, puntuacion);
				ps.setString(2, ID);

				if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, saltara el siguiente mensaje
					listaComics.add(comic);
				}
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		} finally {
			ps.close();
		}
	}

	/**
	 * Funcion que comprueba si la opinion se ha introducida correctamente
	 *
	 * @param ps
	 * @return
	 * @throws SQLException
	 */
	public void comprobarOpinionBorrada(String sentenciaSQL, String ID) throws SQLException {

		conn = DBManager.conexion();
		listaComics.clear();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sentenciaSQL);
			if (comprobarID(ID)) // Comprueba si la ID introducida existe en la base de datos
			{
				Comic comic = comicDatos(ID);
				ps.setString(1, ID);

				if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, saltara el siguiente mensaje
					listaComics.add(comic);
				}
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		} finally {
			ps.close();
		}
	}

	/**
	 * Función que permite mostrar la imagen de una portada cuando se hace clic con
	 * el ratón encima del cómic seleccionado.
	 *
	 * @param iD el ID del cómic
	 * @return la imagen de la portada del cómic
	 */
	public Image selectorImage(String iD) {
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			conn = DBManager.conexion(); // Se establece la conexión si no está conectado

			String sentenciaSQL = "SELECT portada FROM comicsbbdd WHERE ID = ?";
			statement = conn.prepareStatement(sentenciaSQL);
			statement.setString(1, iD);
			rs = statement.executeQuery();

			if (rs.next()) {
				String direccionImagen = rs.getString("portada");
				if (direccionImagen != null && !direccionImagen.isEmpty()) {
					try {
						Image imagen = new Image(new File(direccionImagen).toURI().toString());
						imagen = new Image(imagen.getUrl(), 250, 0, true, true);
						return imagen;
					} catch (Exception e) {
						nav.alertaException(e.toString());
					}
				}
			}
		} catch (SQLException e) {
			nav.alertaException(e.toString());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				nav.alertaException(e.toString());
			}
		}

		return null;
	}

	/**
	 * Función que muestra todos los cómics de la base de datos.
	 *
	 * @param sentenciaSQL la sentencia SQL para obtener los cómics
	 * @param excepcion    el mensaje de excepción a mostrar si no se encuentran
	 *                     cómics
	 * @return una lista de cómics obtenidos de la base de datos
	 * @throws SQLException
	 */
	public List<Comic> comprobarLibreria(String sentenciaSQL, String excepcion) throws SQLException {
		
		List<Comic> listComic = FXCollections.observableArrayList(verLibreria(sentenciaSQL));
		
		if (listComic.isEmpty()) {
			
			nav.alertaException(excepcion);
		}

		return listComic;
	}

	/**
	 * Función que busca en el ArrayList el o los cómics que tengan coincidencia con
	 * los datos introducidos en el TextField.
	 *
	 * @param comic           el cómic con los parámetros de búsqueda
	 * @param busquedaGeneral el texto de búsqueda general
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException si ocurre un error al acceder a la base de datos
	 */
	public List<Comic> busquedaParametro(Comic comic, String busquedaGeneral) throws SQLException {
		String sentenciaSQL = "SELECT * from comicsbbdd";
		String excepcion = "No hay ningun comic guardado en la base de datos";
		List<Comic> listComic = comprobarLibreria(sentenciaSQL, excepcion);

		if (listComic.size() != 0) {
			if (busquedaGeneral.length() != 0) {
				listComic = FXCollections.observableArrayList(verBusquedaGeneral(busquedaGeneral));
			} else {
				try {
					listComic = FXCollections.observableArrayList(filtadroBBDD(comic));
				}catch(NullPointerException ex) {
					ex.getStackTrace();
				}
			}
		}
		return listComic;
	}

	/**
	 * Funcion que muestra todos los comics de la base de datos
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public List<Comic> libreriaPosesion() throws SQLException {

		String sentenciaSQL = "SELECT * from comicsbbdd where estado = 'En posesion' ORDER BY nomComic,fecha_publicacion,numComic";
		String excepcion = "No hay ningun comic guardado en la base de datos";

		return comprobarLibreria(sentenciaSQL, excepcion);
	}

	/**
	 * Funcion que muestra todos los comics de la base de datos.
	 *
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public List<Comic> libreriaCompleta() throws IOException, SQLException {
		String query = "SELECT * FROM comicsbbdd ORDER BY nomComic,fecha_publicacion,numComic";

		String excepcion = "No hay ningun comic guardado en la base de datos";


		return comprobarLibreria(query, excepcion);
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public List<Comic> libreriaVendidos() throws SQLException {
		String sentenciaSQL = "SELECT * from comicsbbdd where estado = 'Vendido' ORDER BY nomComic,fecha_publicacion,numComic";
		String excepcion = "No hay comics en vendidos";

		return comprobarLibreria(sentenciaSQL, excepcion);
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public List<Comic> libreriaComprados() throws SQLException {
		String sentenciaSQL = "SELECT * from comicsbbdd where estado = 'Comprado' ORDER BY nomComic,fecha_publicacion,numComic";
		String excepcion = "No hay comics comprados";

		return comprobarLibreria(sentenciaSQL, excepcion);
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public List<Comic> libreriaPuntuacion() throws SQLException {
		String sentenciaSQL = "SELECT * from comicsbbdd where puntuacion <> 'Sin puntuar' ORDER BY nomComic,fecha_publicacion,numComic";
		String excepcion = "No hay comics puntuados";

		return comprobarLibreria(sentenciaSQL, excepcion);
	}
	
	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public List<Comic> libreriaSeleccionado(String datoSeleccionado) throws SQLException {
		String sentenciaSQL = "SELECT * from comicsbbdd where nomVariante = '" + datoSeleccionado +  "' OR nomGuionista = '" + datoSeleccionado +  "' OR firma = '" + datoSeleccionado +  "' OR nomDibujante = '" + datoSeleccionado +  "' ORDER BY nomComic ASC";
		String excepcion = "No hay comics relacionados con esa seleccion";

		return comprobarLibreria(sentenciaSQL, excepcion);
	}
	
	/**
	 * Devuelve el numero total del resultado de la busqueda de un campo individual 
	 * @param datoSeleccionado
	 * @return
	 * @throws SQLException
	 */
	public int numeroTotalSelecionado(String datoSeleccionado) throws SQLException {
	    String sentenciaSQL = "SELECT COUNT(*) FROM comicsbbdd WHERE nomVariante = ? OR nomGuionista = ? OR nomDibujante = ?";
	    int count = 0;

	    try (PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
	        ps.setString(1, datoSeleccionado);
	        ps.setString(2, datoSeleccionado);
	        ps.setString(3, datoSeleccionado);

	        try (ResultSet resultado = ps.executeQuery()) {
	            if (resultado.next()) {
	                count = resultado.getInt(1);
	            }
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }

	    return count;
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * firmados
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public List<Comic> libreriaFirmados() throws SQLException {
		String sentenciaSQL = "SELECT * from comicsbbdd where Firma <> '' ORDER BY nomComic,fecha_publicacion,numComic";
		String excepcion = "No hay comics firmados";

		return comprobarLibreria(sentenciaSQL, excepcion);
	}

	/**
	 * Obtiene la dirección de la portada de un cómic.
	 *
	 * @param idComic ID del cómic
	 * @return Dirección de la portada del cómic
	 * @throws SQLException Si ocurre algún error de SQL
	 */
	public String obtenerDireccionPortada(String idComic) throws SQLException {
		String query = "SELECT portada FROM comicsbbdd WHERE ID = ?";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, idComic);
			ResultSet resultado = ps.executeQuery();
			if (resultado.next()) {
				String portada = resultado.getString("portada");
				if (portada != null && !portada.isEmpty()) {
					return portada;
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			ps.close();
		}

		return null;
	}
}