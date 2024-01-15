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
 *  - Puntuar comics que se encuentren dentro de la base de datos.
 *  Esta clase permite acceder al menu principal donde se puede viajar a diferentes ventanas, etc.
 *
 *  Version 8.0.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Controladores.CrearBBDDController;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import comicManagement.Comic;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

/**
 * Esta clase sirve para realizar diferentes operaciones que tengan que ver con
 * la base de datos.
 *
 * @author Alejandro Rodriguez
 */
public class DBLibreriaManager {

	/**
	 * Lista de cómics.
	 */
	public static List<Comic> listaComics = new ArrayList<>();

	/**
	 * Lista de cómics con verificación.
	 */
	public static List<Comic> listaComicsCheck = new ArrayList<>();

	/**
	 * Lista de nombres.
	 */
	public static List<String> listaNombre = new ArrayList<>();

	/**
	 * Lista de números de cómic.
	 */
	public static List<String> listaNumeroComic = new ArrayList<>();

	/**
	 * Lista de variantes.
	 */
	public static List<String> listaVariante = new ArrayList<>();

	/**
	 * Lista de firmas.
	 */
	public static List<String> listaFirma = new ArrayList<>();

	/**
	 * Lista de formatos.
	 */
	public static List<String> listaFormato = new ArrayList<>();

	/**
	 * Lista de editoriales.
	 */
	public static List<String> listaEditorial = new ArrayList<>();

	/**
	 * Lista de guionistas.
	 */
	public static List<String> listaGuionista = new ArrayList<>();

	/**
	 * Lista de dibujantes.
	 */
	public static List<String> listaDibujante = new ArrayList<>();

	/**
	 * Lista de fechas.
	 */
	public static List<String> listaFecha = new ArrayList<>();

	/**
	 * Lista de procedencias.
	 */
	public static List<String> listaProcedencia = new ArrayList<>();

	/**
	 * Lista de cajas.
	 */
	public static List<String> listaCaja = new ArrayList<>();

	/**
	 * Lista de nombres de cómics.
	 */
	public static List<String> nombreComicList = new ArrayList<>();

	/**
	 * Lista de números de cómic.
	 */
	public static List<String> numeroComicList = new ArrayList<>();

	/**
	 * Lista de nombres de firma.
	 */
	public static List<String> nombreFirmaList = new ArrayList<>();

	/**
	 * Lista de nombres de guionistas.
	 */
	public static List<String> nombreGuionistaList = new ArrayList<>();

	/**
	 * Lista de nombres de variantes.
	 */
	public static List<String> nombreVarianteList = new ArrayList<>();

	/**
	 * Lista de números de caja.
	 */
	public static List<String> numeroCajaList = new ArrayList<>();

	/**
	 * Lista de nombres de procedencia.
	 */
	public static List<String> nombreProcedenciaList = new ArrayList<>();

	/**
	 * Lista de nombres de formato.
	 */
	public static List<String> nombreFormatoList = new ArrayList<>();

	/**
	 * Lista de nombres de editorial.
	 */
	public static List<String> nombreEditorialList = new ArrayList<>();

	/**
	 * Lista de nombres de dibujantes.
	 */
	public static List<String> nombreDibujanteList = new ArrayList<>();

	/**
	 * Lista de nombres de dibujantes.
	 */
	public static List<String> listaImagenes = new ArrayList<>();

	/**
	 * Lista ordenada que contiene todas las listas anteriores.
	 */
	public static List<List<String>> listaOrdenada = Arrays.asList(nombreComicList, numeroComicList, nombreVarianteList,
			nombreProcedenciaList, nombreFormatoList, nombreDibujanteList, nombreGuionistaList, nombreEditorialList,
			nombreFirmaList, numeroCajaList);

	/**
	 * Lista de listas de elementos.
	 */
	public static List<List<String>> itemsList = Arrays.asList(listaNombre, listaNumeroComic, listaVariante,
			listaProcedencia, listaFormato, listaDibujante, listaGuionista, listaEditorial, listaFirma, listaCaja);

	/**
	 * Lista de comics guardados para poder ser impresos
	 */
	public static List<Comic> comicsGuardadosList = new ArrayList<>();

	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private final String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private final String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	/**
	 * Construimos la ruta al directorio "libreria_comics" dentro de "Documents" y
	 * añadimos el nombre de la base de datos y la carpeta "portadas".
	 */
	private final String SOURCE_PATH = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
			+ DBManager.DB_NAME + File.separator + "portadas" + File.separator;

	/**
	 * Agrega elementos únicos a la lista principal de cómics guardados,
	 * ordenándolos por ID en orden descendente de longitud.
	 *
	 * @param listaAnadir Lista de cómics a añadir a la lista principal.
	 */
	public static void agregarElementosUnicos(List<Comic> listaAnadir) {
		// Usamos un Set para mantener los elementos únicos
		Set<String> idsUnicos = comicsGuardadosList.stream().map(Comic::getID).collect(Collectors.toSet());

		// Filtramos la lista a añadir para obtener solo aquellos cuyas ID no estén
		// repetidas
		List<Comic> comicsNoRepetidos = listaAnadir.stream().filter(comic -> !idsUnicos.contains(comic.getID()))
				.sorted(Comparator.comparing(Comic::getID, Comparator.comparingInt(String::length).reversed()))
				.collect(Collectors.toList());

		// Añadimos los cómics no repetidos a la lista principal
		comicsGuardadosList.addAll(comicsNoRepetidos);

		// Verificamos que la lista esté ordenada
		boolean estaOrdenada = IntStream.range(0, comicsGuardadosList.size() - 1).allMatch(
				i -> comicsGuardadosList.get(i).getID().compareTo(comicsGuardadosList.get(i + 1).getID()) <= 0);

		if (!estaOrdenada) {
			// Si no está ordenada, ordenamos la lista
			comicsGuardadosList.sort(Comparator.comparing(Comic::getID));
		}
	}

	/**
	 * Funcion que permite limpiar de contenido la lista de comics guardados
	 */
	public static void limpiarListaGuardados() {
		comicsGuardadosList.clear();
	}

	/**
	 * Realiza llamadas para inicializar listas de autocompletado.
	 *
	 * @throws SQLException si ocurre un error al acceder a la base de datos.
	 */
	public void listasAutoCompletado() {
		listaNombre = obtenerValoresColumna("nomComic");
		listaNumeroComic = obtenerValoresColumna("numComic");
		listaVariante = obtenerValoresColumna("nomVariante");
		listaFirma = obtenerValoresColumna("firma");
		listaFormato = obtenerValoresColumna("formato");
		listaEditorial = obtenerValoresColumna("nomEditorial");
		listaGuionista = obtenerValoresColumna("nomGuionista");
		listaDibujante = obtenerValoresColumna("nomDibujante");
		listaProcedencia = obtenerValoresColumna("procedencia");
		listaCaja = obtenerValoresColumna("caja_deposito");
		listaImagenes = obtenerValoresColumna("portada");

		itemsList = Arrays.asList(listaNombre, listaNumeroComic, listaVariante, listaProcedencia, listaFormato,
				listaDibujante, listaGuionista, listaEditorial, listaFirma, listaCaja);
	}

	/**
	 * Limpia todas las listas utilizadas para autocompletado.
	 */
	public static void limpiarListas() {
		nombreComicList.clear();
		numeroComicList.clear();
		nombreFirmaList.clear();
		nombreGuionistaList.clear();
		nombreVarianteList.clear();
		numeroCajaList.clear();
		nombreProcedenciaList.clear();
		nombreFormatoList.clear();
		nombreEditorialList.clear();
		nombreDibujanteList.clear();

//		itemsList = Arrays.asList(listaNombre, listaNumeroComic, listaVariante, listaProcedencia, listaFormato,
//				listaDibujante, listaGuionista, listaEditorial, listaFirma, listaCaja);
	}

	/**
	 * Funcion que permite contar cuantas filas hay en la base de datos.
	 *
	 * @return
	 */
	public int countRows() {
		String sql = "SELECT COUNT(*) FROM comicsbbdd";
		try (Connection conn = DBManager.conexion();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return 0;
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
			return 0;
		}
	}

	/**
	 * Borra el contenido de la base de datos.
	 *
	 * @return
	 * @throws SQLException
	 */
	public CompletableFuture<Boolean> deleteTable() {
		CompletableFuture<Boolean> futureResult = new CompletableFuture<>();
		Utilidades utilidad = new Utilidades();
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					CompletableFuture<Boolean> ejecucionResult = null;
					boolean ejecucionExitosa = false;
					if (listaNombre.size() > 0) {
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

						// Ejecutar el PreparedStatement asíncronamente
						ejecucionResult = ejecutarPreparedStatementAsync(sentencia);
						ejecucionExitosa = ejecucionResult.join();
					}

					futureResult.complete(ejecucionExitosa); // Completar la CompletableFuture con el resultado
				} catch (Exception e) {
					futureResult.completeExceptionally(e);
				}
				return null;
			}
		};

		task.setOnFailed(e -> futureResult.completeExceptionally(task.getException()));

		Thread thread = new Thread(task);
		thread.start();

		return futureResult;
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
	public CompletableFuture<Boolean> ejecutarPreparedStatementAsync(String[] sentencia) {
		CompletableFuture<Boolean> futureResult = new CompletableFuture<>();

		Task<Void> task = new Task<>() {
			@Override
			protected Void call() {
				try (Connection conn = DBManager.conexion();
						PreparedStatement statement1 = conn.prepareStatement(sentencia[0]);
						PreparedStatement statement2 = conn.prepareStatement(sentencia[1])) {

					statement1.executeUpdate();
					statement2.executeUpdate();

					futureResult.complete(true); // Assume success if it reaches here
				} catch (Exception e) {
					futureResult.completeExceptionally(e);
				}
				return null;
			}
		};

		task.setOnFailed(e -> futureResult.completeExceptionally(task.getException()));

		// Run the task in a background thread managed by JavaFX
		new Thread(task).start();

		return futureResult;
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
			Utilidades.manejarExcepcion(e);
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
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Función que guarda los datos para autocompletado en una lista.
	 * 
	 * @param sentenciaSQL La sentencia SQL para obtener los datos.
	 * @param columna      El nombre de la columna que contiene los datos para
	 *                     autocompletado.
	 * @return Una lista de cadenas con los datos para autocompletado.
	 * @throws SQLException Si ocurre algún error al ejecutar la consulta SQL.
	 */
	public List<String> guardarDatosAutoCompletado(String sentenciaSQL, String columna) {
		List<String> listaAutoCompletado = new ArrayList<>();

		try (Connection conn = DBManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery()) {

			if (rs != null && rs.first()) {
				do {
					String datosAutocompletado = rs.getString(columna);
					if (columna.equals("nomComic")) {
						listaAutoCompletado.add(datosAutocompletado.trim());
					} else if (columna.equals("portada")) {
						listaAutoCompletado.add(SOURCE_PATH + obtenerUltimoSegmentoRuta(datosAutocompletado));
					} else {
						String[] nombres = datosAutocompletado.split("-");
						for (String nombre : nombres) {
							nombre = nombre.trim();
							if (!nombre.isEmpty()) {
								listaAutoCompletado.add(nombre);
							}
						}
					}
				} while (rs.next());

				listaAutoCompletado = Utilidades.listaArregladaAutoComplete(listaAutoCompletado);
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return listaAutoCompletado;
	}

	public static String obtenerUltimoSegmentoRuta(String ruta) {
		if (ruta == null || ruta.isEmpty()) {
			return "";
		}

		int indiceUltimoSeparador = ruta.lastIndexOf(File.separator);
		if (indiceUltimoSeparador != -1 && indiceUltimoSeparador < ruta.length() - 1) {
			return ruta.substring(indiceUltimoSeparador + 1);
		} else {
			// La ruta no contiene separador o es la última parte de la ruta
			return ruta;
		}
	}

	// **************************************//
	// ****FUNCIONES DE LA LIBRERIA**********//
	// **************************************//

	public static String obtenerSiguienteId() throws SQLException {
		String query = "SELECT ID FROM comicsbbdd ORDER BY ID DESC LIMIT 1";
		String ultimoId = "1"; // Valor predeterminado si no se encuentra ningún resultado

		try (Connection conn = DBManager.conexion();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet resultado = ps.executeQuery()) {

			if (resultado.next()) {
				int id = resultado.getInt("ID");
				ultimoId = String.valueOf(id + 1);
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return ultimoId;
	}

	/**
	 * Devuelve una lista de valores de una columna específica de la base de datos.
	 *
	 * @param columna Nombre de la columna de la base de datos.
	 * @return Lista de valores de la columna.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public List<String> obtenerValoresColumna(String columna) {
		String sentenciaSQL = "SELECT " + columna + " FROM comicsbbdd ORDER BY " + columna + " ASC";
		reiniciarBBDD();
//		DBManager.resetConnection();
		return guardarDatosAutoCompletado(sentenciaSQL, columna);
	}

	/**
	 * Método que muestra los cómics de la librería según la sentencia SQL
	 * proporcionada.
	 * 
	 * @param sentenciaSQL La sentencia SQL para obtener los cómics de la librería.
	 * @return Una lista de objetos Comic que representan los cómics de la librería.
	 * @throws SQLException Si ocurre algún error al ejecutar la consulta SQL.
	 */
	public List<Comic> verLibreria(String sentenciaSQL) {
		listaComics.clear(); // Limpiar la lista existente de cómics
		List<Comic> listaComics = new ArrayList<>();

		try (Connection conn = DBManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				listaComics.add(devolverComic(rs));
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return listaComics;
	}

	/**
	 * Filtra y devuelve una lista de cómics de la base de datos según los datos
	 * proporcionados.
	 *
	 * @param datos Objeto Comic con los datos para filtrar.
	 * @return Lista de cómics filtrados.
	 */
	public List<Comic> filtroBBDD(Comic datos) {
		// Reiniciar la lista de cómics antes de realizar el filtrado
		reiniciarBBDD();

		// Crear la consulta SQL a partir de los datos proporcionados
		String sql = datosConcatenados(datos);

		// Verificar si la consulta SQL no está vacía
		if (!sql.isEmpty()) {
			try (Connection conn = DBManager.conexion();
					PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {

				// Llenar la lista de cómics con los resultados obtenidos
				while (rs.next()) {
					listaComics.add(devolverComic(rs));
				}

				return listaComics;
			} catch (SQLException ex) {
				// Manejar la excepción según tus necesidades (en este caso, mostrar una alerta)
				Utilidades.manejarExcepcion(ex);
			}
		}

		return listaComics; // Devolver null si la consulta SQL está vacía
	}

	public String datosConcatenados(Comic comic) {
		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder("SELECT * FROM comicsbbdd");

		connector = agregarCondicion(sql, connector, "ID", comic.getID());
		connector = agregarCondicionLike(sql, connector, "nomComic", comic.getNombre());
		connector = agregarCondicion(sql, connector, "caja_deposito", comic.getNumCaja());
		connector = agregarCondicion(sql, connector, "numComic", comic.getNumero());
		connector = agregarCondicionLike(sql, connector, "nomVariante", comic.getVariante());
		connector = agregarCondicionLike(sql, connector, "firma", comic.getFirma());
		connector = agregarCondicionLike(sql, connector, "nomEditorial", comic.getEditorial());
		connector = agregarCondicionLike(sql, connector, "formato", comic.getFormato());
		connector = agregarCondicionLike(sql, connector, "procedencia", comic.getProcedencia());
		connector = agregarCondicionLike(sql, connector, "fecha_publicacion", comic.getFecha());
		connector = agregarCondicionLike(sql, connector, "nomGuionista", comic.getGuionista());
		connector = agregarCondicionLike(sql, connector, "nomDibujante", comic.getDibujante());

		return (connector.length() > 0) ? sql.toString() : "";
	}

	private String agregarCondicion(StringBuilder sql, String connector, String columna, String valor) {
		if (valor.length() != 0) {
			sql.append(connector).append(columna).append(" = '").append(valor).append("'");
			return " AND ";
		}
		return connector;
	}

	private String agregarCondicionLike(StringBuilder sql, String connector, String columna, String valor) {
		if (valor.length() != 0) {
			sql.append(connector).append(columna).append(" LIKE '%").append(valor).append("%'");
			return " AND ";
		}
		return connector;
	}

	/**
	 * Funcion que permite hacer una busqueda general mediante 1 sola palabra, hace
	 * una busqueda en ciertos identificadores de la tabla.
	 *
	 * @param sentencia
	 * @return
	 * @throws SQLException
	 */
	public List<Comic> verBusquedaGeneral(String busquedaGeneral) {
		String sql1 = datosGenerales("nomcomic", busquedaGeneral);
		String sql2 = datosGenerales("nomvariante", busquedaGeneral);
		String sql3 = datosGenerales("firma", busquedaGeneral);
		String sql4 = datosGenerales("nomguionista", busquedaGeneral);
		String sql5 = datosGenerales("nomdibujante", busquedaGeneral);

		try (Connection conn = DBManager.conexion();
				PreparedStatement ps1 = conn.prepareStatement(sql1);
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				PreparedStatement ps3 = conn.prepareStatement(sql3);
				PreparedStatement ps4 = conn.prepareStatement(sql4);
				PreparedStatement ps5 = conn.prepareStatement(sql5);
				ResultSet rs1 = ps1.executeQuery();
				ResultSet rs2 = ps2.executeQuery();
				ResultSet rs3 = ps3.executeQuery();
				ResultSet rs4 = ps4.executeQuery();
				ResultSet rs5 = ps5.executeQuery()) {

			listaComics.clear();

			agregarSiHayDatos(rs1);
			agregarSiHayDatos(rs2);
			agregarSiHayDatos(rs3);
			agregarSiHayDatos(rs4);
			agregarSiHayDatos(rs5);

			listaComics = Utilidades.listaArreglada(listaComics);
			return listaComics;

		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}

		return null;
	}

	private void agregarSiHayDatos(ResultSet rs) throws SQLException {
		if (rs.next()) {
			devolverComic(rs);
		}
	}

	/**
	 * Función que construye una consulta SQL para buscar un identificador en la
	 * tabla utilizando diferentes criterios de búsqueda.
	 *
	 * @param tipoBusqueda    Tipo de búsqueda (nomComic, nomVariante, firma,
	 *                        nomGuionista, nomDibujante).
	 * @param busquedaGeneral Término de búsqueda.
	 * @return Consulta SQL generada.
	 */
	public String datosGenerales(String tipoBusqueda, String busquedaGeneral) {
		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd ");

		switch (tipoBusqueda.toLowerCase()) {
		case "nomcomic":
			sql.append(connector).append("nomComic LIKE '%" + busquedaGeneral + "%'");
			break;
		case "nomvariante":
			sql.append(connector).append("nomVariante LIKE '%" + busquedaGeneral + "%'");
			break;
		case "firma":
			sql.append(connector).append("firma LIKE '%" + busquedaGeneral + "%'");
			break;
		case "nomguionista":
			sql.append(connector).append("nomGuionista LIKE '%" + busquedaGeneral + "%'");
			break;
		case "nomdibujante":
			sql.append(connector).append("nomDibujante LIKE '%" + busquedaGeneral + "%'");
			break;
		default:
			// Tipo de búsqueda no válido, puedes manejarlo según tus necesidades
			break;
		}

		return sql.toString();
	}

	/**
	 * Crea una lista de objetos Comic a partir del ResultSet proporcionado.
	 *
	 * @param rs el ResultSet con los datos de los cómics
	 * @return una lista de objetos Comic
	 */
	private Comic devolverComic(ResultSet rs) {
		Comic comic = new Comic();

		try {
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
			String key_issue = rs.getString("key_issue");
			String estado = rs.getString("estado");
			String puntuacion = rs.getString("puntuacion");
			String imagen = rs.getString("portada");
			String url_referencia = rs.getString("url_referencia");
			String precio_comic = rs.getString("precio_comic");
			String codigo_comic = rs.getString("codigo_comic");

			comic = new Comic(ID, nombre, numCaja, numero, variante, firma, editorial, formato, procedencia, fecha,
					guionista, dibujante, estado, key_issue, puntuacion, imagen, url_referencia, precio_comic,
					codigo_comic);
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return comic;
	}

	/**
	 * Comprueba si la lista de cómics contiene o no algún dato.
	 *
	 * @param listaComic Lista de cómics a comprobar.
	 * @return true si la lista está vacía, false si contiene elementos.
	 */
	public boolean checkList(List<Comic> listaComic) {
		if (listaComic.size() == 0) {
			return true; // La lista está vacía
		}
		return false; // La lista contiene elementos
	}

	/**
	 * Permite reiniciar la lista de cómics.
	 */
	public void reiniciarBBDD() {
		listaComics.clear(); // Limpia la lista de cómics
	}

	/**
	 * Devuelve un objeto Comic cuya ID coincida con el parámetro de búsqueda.
	 *
	 * @param identificador el ID del cómic a buscar
	 * @return el objeto Comic encontrado, o null si no se encontró ningún cómic con
	 *         ese ID
	 * @throws SQLException si ocurre algún error al ejecutar la consulta SQL
	 */
	public Comic comicDatos(String identificador) {
		Comic comic = null;
		String sentenciaSQL = "SELECT * FROM comicsbbdd WHERE ID = ?";

		try (Connection conn = DBManager.conexion();
				PreparedStatement statement = conn.prepareStatement(sentenciaSQL)) {
			statement.setString(1, identificador);

			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					comic = obtenerComicDesdeResultSet(rs);
				}
			} catch (SQLException e) {
				Utilidades.manejarExcepcion(e);
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return comic;
	}

	private Comic obtenerComicDesdeResultSet(ResultSet rs) throws SQLException {
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
		String key_issue = rs.getString("key_issue");
		String puntuacion = rs.getString("puntuacion");
		String imagen = rs.getString("portada");
		String url_referencia = rs.getString("url_referencia");
		String precio_comic = rs.getString("precio_comic");
		String codigo_comic = rs.getString("codigo_comic");

		return new Comic(ID, nombre, numCaja, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante, estado, key_issue, puntuacion, imagen, url_referencia, precio_comic,
				codigo_comic);
	}

	/**
	 * Comprueba si el identificador introducido existe en la base de datos.
	 *
	 * @param identificador El identificador a verificar.
	 * @return true si el identificador existe en la base de datos, false si no
	 *         existe.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public boolean checkID(String identificador) {
		if (identificador.isEmpty()) {
			return false; // Si el identificador está vacío, se considera que no existe
		}

		String sentenciaSQL = "SELECT COUNT(*) FROM comicsbbdd WHERE ID = ?";
		boolean existe = false; // Variable para almacenar si el identificador existe en la base de datos

		try (Connection conn = DBManager.conexion();
				PreparedStatement preparedStatement = conn.prepareStatement(sentenciaSQL)) {
			preparedStatement.setString(1, identificador);

			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt(1); // Obtener el resultado del conteo de filas con el identificador
					existe = count > 0;
				}
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
			return false; // Devolver false en caso de error
		}

		return existe; // Devolver si el identificador existe en la base de datos o no
	}

	/**
	 * Permite modificar un cómic en la base de datos.
	 *
	 * @param id           Identificador del cómic a modificar.
	 * @param sentenciaSQL La consulta SQL para modificar el cómic.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public void modificarDatos(String id, String sentenciaSQL) throws SQLException {

		try (Connection conn = DBManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);) {
			String direccion_portada = obtenerDireccionPortada(id);
			Utilidades.eliminarFichero(direccion_portada);

			if (id.length() != 0) {
				stmt.setString(1, id);

				// Ejecutar la sentencia SQL
				stmt.executeUpdate();
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
	}

	/**
	 * Cambia el estado de un comic a "Vendido" y actualiza su estado en la base de
	 * datos.
	 *
	 * @param id El ID del comic a cambiar de estado.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public void venderComicBBDD(String id) throws SQLException {
		String sentenciaSQL = "UPDATE comicsbbdd SET estado = 'Vendido' WHERE ID = ?";
		modificarDatos(id, sentenciaSQL);
	}

	/**
	 * Cambia el estado de un comic a "En venta" y actualiza su estado en la base de
	 * datos.
	 *
	 * @param id El ID del comic a cambiar de estado.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public void enVentaComicBBDD(String id) throws SQLException {
		String sentenciaSQL = "UPDATE comicsbbdd SET estado = 'En venta' WHERE ID = ?";
		modificarDatos(id, sentenciaSQL);
	}

	/**
	 * Elimina un comic de la base de datos.
	 *
	 * @param id El ID del comic a eliminar.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public void eliminarComicBBDD(String id) throws SQLException {
		String sentenciaSQL = "DELETE FROM comicsbbdd WHERE ID = ?";
		modificarDatos(id, sentenciaSQL);
	}

	public boolean hayDatosEnLibreria(String sentenciaSQL) {
		try (Connection conn = DBManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery()) {

			// Si hay resultados, devolver true
			return rs.first();

		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}

		// Si hay un error, devolver false
		return false;
	}

	/**
	 * Inserta los datos de un cómic en la base de datos.
	 *
	 * @param comic_datos los datos del cómic a insertar
	 * @throws IOException  si ocurre un error al manejar el archivo de imagen
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public void insertarDatos(Comic comic_datos) throws IOException, SQLException {
		String sentenciaSQL = "INSERT INTO comicsbbdd (" + "nomComic, " + "caja_deposito," + "precio_comic,"
				+ "codigo_comic," + "numComic," + "nomVariante," + "firma," + "nomEditorial," + "formato,"
				+ "procedencia," + "fecha_publicacion," + "nomGuionista," + "nomDibujante," + "puntuacion," + "portada,"
				+ "key_issue," + "url_referencia," + "estado"
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

		try (Connection conn = DBManager.conexion();
				PreparedStatement statement = conn.prepareStatement(sentenciaSQL)) {
			statement.setString(1, datos.getNombre());
			if (datos.getNumCaja() == null) {
				statement.setString(2, "0");
			} else {
				statement.setString(2, datos.getNumCaja());
			}
			statement.setString(3, datos.getPrecio_comic());
			statement.setString(4, datos.getCodigo_comic());
			statement.setString(5, datos.getNumero());
			statement.setString(6, datos.getVariante());
			statement.setString(7, datos.getFirma());
			statement.setString(8, datos.getEditorial());
			statement.setString(9, datos.getFormato());
			statement.setString(10, datos.getProcedencia());
			statement.setString(11, datos.getFecha());
			statement.setString(12, datos.getGuionista());
			statement.setString(13, datos.getDibujante());
			statement.setString(14, "Sin puntuar");
			statement.setString(15, datos.getImagen());
			statement.setString(16, datos.getKey_issue());
			statement.setString(17, datos.getUrl_referencia());
			statement.setString(18, datos.getEstado());

			statement.executeUpdate();
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
		DBManager.resetConnection();
	}

	/**
	 * Función que actualiza los datos de un cómic en la base de datos.
	 *
	 * @param datos Los datos del cómic a actualizar.
	 */
	public void actualizarComic(Comic datos) {
		String sentenciaSQL = "UPDATE comicsbbdd SET nomComic = ?, caja_deposito = ?,codigo_comic = ?, numComic = ?, nomVariante = ?, "
				+ "Firma = ?, nomEditorial = ?, formato = ?, Procedencia = ?, fecha_publicacion = ?, "
				+ "nomGuionista = ?, nomDibujante = ?, key_issue = ?, portada = ?, estado = ?, url_referencia = ?, precio_comic = ? "
				+ "WHERE ID = ?";

		try {
			if (checkID(datos.getID())) { // Comprueba si la ID introducida existe en la base de datos
				comicModificar(sentenciaSQL, datos); // Llama a la función que permite cambiar los datos del cómic
			}
		} catch (SQLException | IOException ex) {
			Utilidades.manejarExcepcion(ex);
		} finally {
			DBManager.resetConnection(); // Restablecer la conexión a la base de datos
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
		String extension = ".jpg";
		String nuevoNombreArchivo = nombre_completo + extension;

		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String imagePath = documentsPath + File.separator + "libreria_comics" + File.separator + DBManager.DB_NAME
				+ File.separator + "portadas" + File.separator + nuevoNombreArchivo;

		String sql = "UPDATE comicsbbdd SET portada = ? WHERE ID = ?";
		try (Connection conn = DBManager.conexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
		Utilidades utilidad = new Utilidades();
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
		String key_issue = datos.getKey_issue();
		String url_referencia = datos.getUrl_referencia();
		String precio_comic = datos.getPrecio_comic();
		String codigo_imagen = Utilidades.generarCodigoUnico(portada_final + File.separator);
		String codigo_comic = datos.getCodigo_comic();

		try (Connection conn = DBManager.conexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {

			ps.setString(1, nombre);
			ps.setString(2, numCaja);
			ps.setString(3, codigo_comic);
			ps.setString(4, numero);
			ps.setString(5, variante);
			ps.setString(6, firma);
			ps.setString(7, editorial);
			ps.setString(8, formato);
			ps.setString(9, procedencia);
			ps.setString(10, fecha);
			ps.setString(11, guionista);
			ps.setString(12, dibujante);
			ps.setString(13, key_issue);
			ps.setString(14, portada_final);
			ps.setString(15, estado);
			ps.setString(16, url_referencia);
			ps.setString(17, precio_comic);
			ps.setString(18, ID);

			if (ps.executeUpdate() == 1) {
				Comic nuevo_comic = new Comic("", nombre, numCaja, numero, variante, firma, editorial, formato,
						procedencia, fecha, guionista, dibujante, estado, key_issue, "", portada_final, url_referencia,
						precio_comic, codigo_comic);

				String carpeta = Utilidades.eliminarDespuesUltimoPortadas(codigo_imagen);
				utilidad.nueva_imagen(portada_final, carpeta);
				modificar_direccion_portada(codigo_imagen, datos.getID());

				listaComics.add(nuevo_comic);
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
		DBManager.resetConnection();
	}

	/**
	 * Función que permite insertar una puntuación a un cómic según la ID
	 * introducida.
	 * 
	 * @param ID         La ID del cómic
	 * @param puntuacion La puntuación a insertar
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public void actualizarPuntuacion(String ID, String puntuacion) throws SQLException {
		String sentenciaSQL = "UPDATE comicsbbdd set puntuacion = ? where ID = ?";

		comprobarOpinionInsertada(sentenciaSQL, ID, puntuacion);
	}

	/**
	 * Función que permite borrar la puntuación de un cómic según la ID introducida.
	 * 
	 * @param ID La ID del cómic
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public void borrarPuntuacion(String ID) throws SQLException {
		String sentenciaSQL = "UPDATE comicsbbdd set puntuacion = 'Sin puntuar' where ID = ?";

		comprobarOpinionBorrada(sentenciaSQL, ID); // Llamada a función que permite comprobar el cambio realizado en

	}

	/**
	 * Función que comprueba si la opinión ha sido introducida correctamente.
	 *
	 * @param sentenciaSQL La sentencia SQL a ejecutar
	 * @param ID           La ID del cómic
	 * @param puntuacion   La puntuación a insertar
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public void comprobarOpinionInsertada(String sentenciaSQL, String ID, String puntuacion) throws SQLException {
		listaComics.clear();
		try (Connection conn = DBManager.conexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL);) {
			if (checkID(ID)) { // Comprueba si la ID introducida existe en la base de datos
				Comic comic = comicDatos(ID);
				ps.setString(1, puntuacion);
				ps.setString(2, ID);
				if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, se añade el cómic a la lista
					listaComics.add(comic);
				}
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
	}

	/**
	 * Función que comprueba si la opinión ha sido borrada correctamente.
	 *
	 * @param sentenciaSQL La sentencia SQL a ejecutar
	 * @param ID           La ID del cómic
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public void comprobarOpinionBorrada(String sentenciaSQL, String ID) throws SQLException {
		listaComics.clear();
		try (Connection conn = DBManager.conexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL);) {
			if (checkID(ID)) { // Comprueba si la ID introducida existe en la base de datos
				Comic comic = comicDatos(ID);
				ps.setString(1, ID);
				if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, se añade el cómic a la lista
					listaComics.add(comic);
				}
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
	}

	/**
	 * Función que permite mostrar la imagen de una portada cuando se hace clic con
	 * el ratón encima del cómic seleccionado.
	 *
	 * @param iD La ID del cómic
	 * @return La imagen de la portada del cómic
	 */
	public Image selectorImage(String iD) {
		try (Connection conn = DBManager.conexion();
				PreparedStatement statement = conn.prepareStatement("SELECT portada FROM comicsbbdd WHERE ID = ?")) {

			statement.setString(1, iD);

			try (ResultSet rs = statement.executeQuery()) {
				return procesarResultado(rs);
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return null;
	}

	private Image procesarResultado(ResultSet rs) throws SQLException {
		if (rs.next()) {
			String direccionImagen = rs.getString("portada");
			if (direccionImagen != null && !direccionImagen.isEmpty()) {
				try {
					Image imagen = new Image(new File(direccionImagen).toURI().toString());
					return new Image(imagen.getUrl(), 250, 0, true, true);
				} catch (Exception e) {
					Utilidades.manejarExcepcion(e);
				}
			}
		}
		return null;
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
	public List<Comic> busquedaParametro(Comic comic, String busquedaGeneral) {
		String sentenciaSQL = "SELECT * from comicsbbdd";
		boolean existeBase = hayDatosEnLibreria(sentenciaSQL);

		List<Comic> listComic = new ArrayList<Comic>();
		if (existeBase) {
			if (busquedaGeneral.length() != 0) {
				return listComic = verBusquedaGeneral(busquedaGeneral);
			} else {
				try {
					return listComic = filtroBBDD(comic);
				} catch (NullPointerException ex) {
					Utilidades.manejarExcepcion(ex);
				}
			}
		}
		return listComic;
	}

	/**
	 * Función que muestra todos los cómics en posesión.
	 *
	 * @return Una lista de cómics que están en posesión
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public List<Comic> libreriaPosesion() {
		String sentenciaSQL = "SELECT * from comicsbbdd where estado = 'En posesion' ORDER BY nomComic, fecha_publicacion, numComic";
		return verLibreria(sentenciaSQL);
	}

	/**
	 * Función que muestra todos los cómics con "key issue".
	 *
	 * @return Una lista de cómics con "key issue"
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public List<Comic> libreriaKeyIssue() {
		String sentenciaSQL = "SELECT * FROM comicsbbdd WHERE key_issue <> 'Vacio' ORDER BY nomComic, fecha_publicacion, numComic";

		return verLibreria(sentenciaSQL);
	}

	/**
	 * Función que muestra todos los cómics de la base de datos.
	 *
	 * @return Una lista de todos los cómics
	 * @throws IOException  Si ocurre un error de entrada/salida
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public List<Comic> libreriaCompleta() {
		String sentenciaSQL = "SELECT * FROM comicsbbdd ORDER BY nomComic, fecha_publicacion, numComic";

		return verLibreria(sentenciaSQL);
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public List<Comic> libreriaVendidos() {
		String sentenciaSQL = "SELECT * from comicsbbdd where estado = 'Vendido' ORDER BY nomComic,fecha_publicacion,numComic";

		return verLibreria(sentenciaSQL);
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public List<Comic> libreriaComprados() {
		String sentenciaSQL = "SELECT * from comicsbbdd where estado = 'Comprado' ORDER BY nomComic,fecha_publicacion,numComic";

		return verLibreria(sentenciaSQL);
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public List<Comic> libreriaPuntuacion() {
		String sentenciaSQL = "SELECT * from comicsbbdd where puntuacion <> 'Sin puntuar' ORDER BY nomComic,fecha_publicacion,numComic";

		return verLibreria(sentenciaSQL);
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * firmados
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public List<Comic> libreriaFirmados() {
		String sentenciaSQL = "SELECT * from comicsbbdd where Firma <> '' ORDER BY nomComic,fecha_publicacion,numComic";

		return verLibreria(sentenciaSQL);
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public List<Comic> libreriaSeleccionado(String datoSeleccionado) throws SQLException {
		String sentenciaSQL = "SELECT * FROM comicsbbdd " + "WHERE nomVariante LIKE '%" + datoSeleccionado
				+ "%' OR nomComic LIKE '%" + datoSeleccionado + "%' OR nomGuionista LIKE '%" + datoSeleccionado
				+ "%' OR firma LIKE '%" + datoSeleccionado + "%' OR nomDibujante LIKE '%" + datoSeleccionado
				+ "%' OR nomComic LIKE '%" + datoSeleccionado + "%' OR nomEditorial LIKE '%" + datoSeleccionado
				+ "%' OR caja_deposito LIKE '%" + datoSeleccionado + "%' OR formato LIKE '%" + datoSeleccionado
				+ "%' OR fecha_publicacion LIKE '%" + datoSeleccionado + "%' OR procedencia LIKE '%" + datoSeleccionado
				+ "%' ORDER BY nomComic, fecha_publicacion, numComic ASC";

		return verLibreria(sentenciaSQL);
	}

	/**
	 * Devuelve el número total de resultados de la búsqueda en varios campos.
	 *
	 * @param comic El dato de búsqueda introducido por el usuario
	 * @return El número total de resultados que coinciden con los criterios de
	 *         búsqueda
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public int numeroTotalSelecionado(Comic comic) {
		// Inicializar con una condición verdadera
		String sentenciaSQL = "SELECT COUNT(*) FROM comicsbbdd WHERE 1=1";

		// Lista de condiciones de búsqueda
		List<String> condiciones = new ArrayList<>();

		// Agregar condiciones según los campos no vacíos o no nulos en el objeto Comic
		agregarCondicion(!comic.getVariante().isEmpty(), "nomVariante = ?", condiciones);
		agregarCondicion(!comic.getNombre().isEmpty(), "nomComic = ?", condiciones);
		agregarCondicion(!comic.getNumero().isEmpty(), "numComic = ?", condiciones);
		agregarCondicion(!comic.getGuionista().isEmpty(), "nomGuionista = ?", condiciones);
		agregarCondicion(!comic.getDibujante().isEmpty(), "nomDibujante = ?", condiciones);
		agregarCondicion(!comic.getFirma().isEmpty(), "firma = ?", condiciones);
		agregarCondicion(!comic.getEditorial().isEmpty(), "nomEditorial = ?", condiciones);
		agregarCondicion(!comic.getNumCaja().isEmpty(), "caja_deposito = ?", condiciones);
		agregarCondicion(!comic.getFormato().isEmpty(), "formato = ?", condiciones);
		agregarCondicion(!comic.getFecha().isEmpty(), "fecha_publicacion = ?", condiciones);
		agregarCondicion(!comic.getProcedencia().isEmpty(), "procedencia = ?", condiciones);

		// Combinar las condiciones en la sentencia SQL
		if (!condiciones.isEmpty()) {
			sentenciaSQL += " AND " + String.join(" AND ", condiciones);
		}

		int count = 0;

		try (Connection conn = DBManager.conexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
			// Establecer parámetros según las condiciones
			int paramIndex = 1;
			for (String condicion : condiciones) {
				if (condicion.endsWith("?")) {
					ps.setString(paramIndex++, obtenerValorSeguro(extraerValorCondicion(condicion, comic)));
				}
			}

			try (ResultSet resultado = ps.executeQuery()) {
				if (resultado.next()) {
					count = resultado.getInt(1);
				}
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);

		}

		return count;
	}

	// Método auxiliar para agregar condiciones a la lista
	private void agregarCondicion(boolean condicion, String sqlFragment, List<String> condiciones) {
		if (condicion) {
			condiciones.add(sqlFragment);
		}
	}

	// Método auxiliar para extraer el valor de la condición (sin el operador LIKE)
	private String extraerValorCondicion(String condicion, Comic comic) {
		// Suponemos que la condición siempre tiene el formato "campo LIKE ?"
		String campo = condicion.split(" ")[0];
		switch (campo) {
		case "nomVariante":
			return comic.getVariante();
		case "nomComic":
			return comic.getNombre();
		case "numComic":
			return comic.getNumero();
		case "nomGuionista":
			return comic.getGuionista();
		case "nomDibujante":
			return comic.getDibujante();
		case "firma":
			return comic.getFirma();
		case "nomEditorial":
			return comic.getEditorial();
		case "caja_deposito":
			return comic.getNumCaja();
		case "formato":
			return comic.getFormato();
		case "fecha_publicacion":
			return comic.getFecha();
		case "procedencia":
			return comic.getProcedencia();
		default:
			return "";
		}
	}

	// Método auxiliar para obtener un valor seguro (no nulo)
	private String obtenerValorSeguro(String valor) {
		return (valor != null) ? valor : "''";
	}

	/**
	 * Obtiene la dirección de la portada de un cómic.
	 *
	 * @param idComic ID del cómic
	 * @return Dirección de la portada del cómic
	 * @throws SQLException Si ocurre algún error de SQL
	 */
	public String obtenerDireccionPortada(String idComic) {
		String query = "SELECT portada FROM comicsbbdd WHERE ID = ?";
		try (Connection conn = DBManager.conexion(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, idComic);
			try (ResultSet resultado = ps.executeQuery()) {
				if (resultado.next()) {
					String portada = resultado.getString("portada");
					if (portada != null && !portada.isEmpty()) {
						return portada;
					}
				}
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}

		return null;
	}

	/**
	 * Ordena un HashMap en orden ascendente por valor (valor de tipo Integer).
	 *
	 * @param map El HashMap a ordenar.
	 * @return Una lista de entradas ordenadas por valor ascendente.
	 */
	public static List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> map) {
		List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
		list.sort(Map.Entry.comparingByKey());
		return list;
	}

	/**
	 * Ordena un HashMap en orden ascendente por valor (valor de tipo Integer).
	 *
	 * @param map El HashMap a ordenar.
	 * @return Una lista de entradas ordenadas por valor ascendente.
	 */
	public static List<Map.Entry<Integer, Integer>> sortByValueInt(Map<Integer, Integer> map) {
		List<Map.Entry<Integer, Integer>> list = new LinkedList<>(map.entrySet());
		list.sort(Map.Entry.comparingByValue());
		return list;
	}

	/**
	 * Genera un archivo de estadísticas basado en los datos de la base de datos de
	 * cómics. Los datos se organizan en diferentes categorías y se cuentan para su
	 * análisis. Luego, se genera un archivo de texto con las estadísticas y se abre
	 * con el programa asociado.
	 */
	public void generar_fichero_estadisticas() {
		// Crear HashMaps para almacenar los datos de cada campo sin repetición y sus
		// conteos
		Map<String, Integer> nomComicEstadistica = new HashMap<>();
		Map<Integer, Integer> cajaDepositoEstadistica = new HashMap<>();
		Map<String, Integer> nomVarianteEstadistica = new HashMap<>();
		Map<String, Integer> firmaEstadistica = new HashMap<>();
		Map<String, Integer> nomEditorialEstadistica = new HashMap<>();
		Map<String, Integer> formatoEstadistica = new HashMap<>();
		Map<String, Integer> procedenciaEstadistica = new HashMap<>();
		Map<String, Integer> fechaPublicacionEstadistica = new HashMap<>();
		Map<String, Integer> nomGuionistaEstadistica = new HashMap<>();
		Map<String, Integer> nomDibujanteEstadistica = new HashMap<>();
		Map<String, Integer> puntuacionEstadistica = new HashMap<>();
		Map<String, Integer> estadoEstadistica = new HashMap<>();
		List<String> keyIssueDataList = new ArrayList<>();

		final String CONSULTA_SQL = "SELECT * FROM comicsbbdd";
		int totalComics = 0;
		try (Connection conn = DBManager.conexion();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(CONSULTA_SQL)) {

			// Procesar los datos y generar la estadística
			while (rs.next()) {
				// Obtener los datos de cada campo
				String nomComic = rs.getString("nomComic");
				int numComic = rs.getInt("numComic");
				int cajaDeposito = rs.getInt("caja_deposito");
				String nomVariante = rs.getString("nomVariante");
				String firma = rs.getString("firma");
				String nomEditorial = rs.getString("nomEditorial");
				String formato = rs.getString("formato");
				String procedencia = rs.getString("procedencia");
				String fechaPublicacion = rs.getString("fecha_publicacion");
				String nomGuionista = rs.getString("nomGuionista");
				String nomDibujante = rs.getString("nomDibujante");
				String puntuacion = rs.getString("puntuacion");
				String estado = rs.getString("estado");
				String clave_comic = rs.getString("key_issue");

				// Actualizar los HashMaps para cada campo
				nomComicEstadistica.put(nomComic, nomComicEstadistica.getOrDefault(nomComic, 0) + 1);
				cajaDepositoEstadistica.put(cajaDeposito, cajaDepositoEstadistica.getOrDefault(cajaDeposito, 0) + 1);

				firmaEstadistica.put(firma, firmaEstadistica.getOrDefault(firma, 0) + 1);
				nomEditorialEstadistica.put(nomEditorial, nomEditorialEstadistica.getOrDefault(nomEditorial, 0) + 1);
				formatoEstadistica.put(formato, formatoEstadistica.getOrDefault(formato, 0) + 1);
				procedenciaEstadistica.put(procedencia, procedenciaEstadistica.getOrDefault(procedencia, 0) + 1);
				fechaPublicacionEstadistica.put(fechaPublicacion,
						fechaPublicacionEstadistica.getOrDefault(fechaPublicacion, 0) + 1);

				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas
				String[] claveList = clave_comic.split("-");
				for (String clave : claveList) {
					clave = clave.trim(); // Remove leading and trailing spaces

					// Aquí verificamos si clave_comic no es "Vacio" ni está vacío antes de agregar
					// a keyIssueDataList
					if (!clave.equalsIgnoreCase("Vacio") && !clave.isEmpty()) {
						String keyIssueData = "Nombre del comic: " + nomComic + " - " + "Numero: " + numComic
								+ " - Key issue:  " + clave;
						keyIssueDataList.add(keyIssueData);
					}
				}

				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas
				String[] varianteList = nomVariante.split("-");
				for (String variante : varianteList) {
					variante = variante.trim(); // Remove leading and trailing spaces
					nomVarianteEstadistica.put(variante, nomVarianteEstadistica.getOrDefault(variante, 0) + 1);
				}

				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas
				String[] guionistaList = nomGuionista.split("-");
				for (String guionista : guionistaList) {
					guionista = guionista.trim(); // Remove leading and trailing spaces
					nomGuionistaEstadistica.put(guionista, nomGuionistaEstadistica.getOrDefault(guionista, 0) + 1);
				}
				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas

				String[] dibujanteList = nomDibujante.split("-");
				for (String dibujante : dibujanteList) {
					dibujante = dibujante.trim(); // Remove leading and trailing spaces
					nomDibujanteEstadistica.put(dibujante, nomDibujanteEstadistica.getOrDefault(dibujante, 0) + 1);
				}

				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas
				String[] firmaList = firma.split("-");
				for (String firmaValor : firmaList) {
					firmaValor = firmaValor.trim(); // Remove leading and trailing spaces
					firmaEstadistica.put(firmaValor, firmaEstadistica.getOrDefault(firmaValor, 0) + 1);
				}

				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas
				String[] procedenciaList = procedencia.split("-");
				for (String procedenciaValor : procedenciaList) {
					procedenciaValor = procedenciaValor.trim(); // Remove leading and trailing spaces
					procedenciaEstadistica.put(procedenciaValor,
							procedenciaEstadistica.getOrDefault(procedenciaValor, 0) + 1);
				}

				puntuacionEstadistica.put(puntuacion, puntuacionEstadistica.getOrDefault(puntuacion, 0) + 1);
				estadoEstadistica.put(estado, estadoEstadistica.getOrDefault(estado, 0) + 1);

				totalComics++; // Incrementar el contador de cómics

			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		// Generar la cadena de estadística
		StringBuilder estadisticaStr = new StringBuilder();
		String lineaDecorativa1 = "\n--------------------------------------------------------";
		String lineaDecorativa2 = "--------------------------------------------------------\n";

		estadisticaStr.append("Estadisticas de comics de la base de datos: " + DBManager.DB_NAME + ", a fecha de: "
				+ obtenerFechaActual() + "\n");

		// Agregar los valores de nomComic a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de los nombres de comics:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nomComicList = sortByValue(nomComicEstadistica);
		for (Map.Entry<String, Integer> entry : nomComicList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de cajaDeposito a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de las cajas:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<Integer, Integer>> cajaDepositoList = sortByValueInt(cajaDepositoEstadistica);
		for (Map.Entry<Integer, Integer> entry : cajaDepositoList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de nomVariante a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de los nombres de variantes:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nomVarianteList = sortByValue(nomVarianteEstadistica);
		for (Map.Entry<String, Integer> entry : nomVarianteList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de firma a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de autores firma:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> firmaList = sortByValue(firmaEstadistica);
		for (Map.Entry<String, Integer> entry : firmaList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de nomGuionista a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de guionistas:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nomGuionistaList = sortByValue(nomGuionistaEstadistica);
		for (Map.Entry<String, Integer> entry : nomGuionistaList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de nomDibujante a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de dibujantes:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nomDibujantesList = sortByValue(nomDibujanteEstadistica);
		for (Map.Entry<String, Integer> entry : nomDibujantesList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de nomEditorial a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de Editoriales:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nomEditorialList = sortByValue(nomEditorialEstadistica);
		for (Map.Entry<String, Integer> entry : nomEditorialList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de procedencia a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de procedencia:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> procedenciaList = sortByValue(procedenciaEstadistica);
		for (Map.Entry<String, Integer> entry : procedenciaList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de fechaPublicacion a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de fecha publicacion:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> fechaPublicacionList = sortByValue(fechaPublicacionEstadistica);
		for (Map.Entry<String, Integer> entry : fechaPublicacionList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de puntuacion a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de puntuacion:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> puntuacionList = sortByValue(puntuacionEstadistica);
		for (Map.Entry<String, Integer> entry : puntuacionList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de estado a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de estado:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> estadoList = sortByValue(estadoEstadistica);
		for (Map.Entry<String, Integer> entry : estadoList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de formato a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de key issue:\n");
		estadisticaStr.append(lineaDecorativa2);
		for (String keyIssueData : keyIssueDataList) {
			estadisticaStr.append(keyIssueData).append("\n");
		}

		// Agregar los valores de formato a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de formato:\n");
		estadisticaStr.append(lineaDecorativa2);
		estadisticaStr.append("Comics en total: " + totalComics).append("\n");
		List<Map.Entry<String, Integer>> formatoList = sortByValue(formatoEstadistica);
		for (Map.Entry<String, Integer> entry : formatoList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		estadisticaStr.append(lineaDecorativa1);

		// Crear el archivo de estadística y escribir los datos en él
		String nombreArchivo = "estadistica_" + obtenerFechaActual() + ".txt";
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String rutaCompleta = carpetaLibreria + File.separator + nombreArchivo;

		try (PrintWriter writer = new PrintWriter(new FileWriter(rutaCompleta))) {
			writer.print(estadisticaStr);

			// Abrir el archivo con el programa asociado en el sistema
			abrirArchivoConProgramaAsociado(rutaCompleta);

		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Obtiene la fecha y hora actual en el formato "yyyy_MM_dd_HH_mm".
	 *
	 * @return La fecha y hora actual formateada como una cadena de texto.
	 */
	private String obtenerFechaActual() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
		Date date = new Date();
		return formatter.format(date);
	}

	/**
	 * Abre un archivo en el programa asociado en el sistema operativo.
	 *
	 * @param rutaArchivo La ruta del archivo que se va a abrir.
	 */
	private void abrirArchivoConProgramaAsociado(String rutaArchivo) {
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(new File(rutaArchivo));
			}
		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Crea las tablas de la base de datos si no existen.
	 */
	public static void createTable() {
		String url = "jdbc:mysql://" + CrearBBDDController.DB_HOST + ":" + CrearBBDDController.DB_PORT + "/"
				+ CrearBBDDController.DB_NAME + "?serverTimezone=UTC";

		try (Connection connection = DriverManager.getConnection(url, CrearBBDDController.DB_USER,
				CrearBBDDController.DB_PASS);
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
	public static void reconstruirBBDD() {
		Ventanas nav = new Ventanas();

		if (nav.alertaTablaError()) {
			DBLibreriaManager.createTable();
		} else {
			String excepcion = "Debes de reconstruir la base de datos. Si no, no podras entrar";
			nav.alertaException(excepcion);
		}
	}

	/**
	 * Método que verifica si las tablas de la base de datos existen. Si las tablas
	 * existen, devuelve true. Si las tablas no existen, reconstruye la base de
	 * datos y devuelve false.
	 * 
	 * @return true si las tablas existen, false si las tablas no existen y se
	 *         reconstruyó la base de datos.
	 */
	public static boolean checkTables() {

		try (Connection connection = DBManager.conexion()) {
			DatabaseMetaData metaData = connection.getMetaData();

			try {
				ResultSet tables = metaData.getTables(null, null, "comicsbbdd", null);
				if (tables.next()) {
					return true;
				} else {
					DBLibreriaManager.reconstruirBBDD();
					return false;
				}
			} catch (SQLException e) {
				Utilidades.manejarExcepcion(e);
				return false;
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
			return false;
		}
	}

}