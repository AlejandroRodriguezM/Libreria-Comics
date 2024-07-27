package dbmanager;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import comicManagement.Comic;
import funcionesAuxiliares.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta clase sirve para realizar diferentes operaciones que tengan que ver con
 * la base de datos.
 *
 * @author Alejandro Rodriguez
 */
public class ListasComicsDAO {

	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private static final String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private static final String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	/**
	 * Construimos la ruta al directorio "libreria_comics" dentro de "Documents" y
	 * añadimos el nombre de la base de datos y la carpeta "portadas".
	 */
	private static final String SOURCE_PATH = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
			+ Utilidades.nombreDB() + File.separator + "portadas" + File.separator;

	public static List<Comic> listaTemporalComics = new ArrayList<>();

	/**
	 * Lista de cómics.
	 */
	public static List<Comic> listaComics = new ArrayList<>();

	public static List<Comic> listaComicsCheck = new ArrayList<>();

	public static List<String> listaID = new ArrayList<>();

	public static List<String> listaNombre = new ArrayList<>();

	public static List<String> listaNumeroComic = new ArrayList<>();

	/**
	 * Lista de editoriales.
	 */
	public static List<String> listaEditor = new ArrayList<>();

	public static List<String> listaFirma = new ArrayList<>();

	public static List<String> listaArtista = new ArrayList<>();

	public static List<String> listaGuionista = new ArrayList<>();

	public static List<String> listaVariante = new ArrayList<>();

	/**
	 * Lista de nombres de cómics.
	 */
	public static List<String> nombreComicList = new ArrayList<>();
	public static List<String> numeroComicList = new ArrayList<>();
	public static List<String> nombreEditorList = new ArrayList<>();
	public static List<String> nombreGuionistaList = new ArrayList<>();
	public static List<String> nombreVarianteList = new ArrayList<>();
	public static List<String> nombreDibujanteList = new ArrayList<>();
	public static List<String> nombreFirmaList = new ArrayList<>();

	/**
	 * Lista de nombres de dibujantes.
	 */
	public static List<String> listaImagenes = new ArrayList<>();

	public static List<String> listaReferencia = new ArrayList<>();

	/**
	 * Lista de cómics limpios.
	 */
	public static List<Comic> listaLimpia = new ArrayList<>();

	/**
	 * Lista de sugerencias de autocompletado de entrada limpia.
	 */
	public static List<String> listaLimpiaAutoCompletado = new ArrayList<>();

	public static ObservableList<Comic> comicsImportados = FXCollections.observableArrayList();
	/**
	 * Lista ordenada que contiene todas las listas anteriores.
	 */
	public static List<List<String>> listaOrdenada = Arrays.asList(nombreComicList, numeroComicList, nombreEditorList,
			nombreFirmaList, nombreGuionistaList, nombreVarianteList, nombreDibujanteList);

	/**
	 * Lista de listas de elementos.
	 */
	public static List<List<String>> itemsList = Arrays.asList(listaNombre, listaNumeroComic, listaEditor, listaFirma,
			listaGuionista, listaVariante, listaArtista);

	public static void eliminarUltimaComicImportada() {
		ObservableList<Comic> comicsImportados = ListasComicsDAO.comicsImportados;
		if (!comicsImportados.isEmpty()) {
			comicsImportados.remove(comicsImportados.size() - 1);
		}
	}

	public static boolean verificarIDExistente(String id) {
		// Verificar que el id no sea nulo ni esté vacío
		if (id == null || id.isEmpty()) {
			return false;
		}

		// Buscar en comicsImportados
		for (Comic comic : comicsImportados) {
			if (id.equalsIgnoreCase(comic.getIdComic())) {
				return true; // Si encuentra un comic con el mismo id, devuelve true
			}
		}

		return false; // Si no encuentra ningún comic con el mismo id, devuelve false
	}

	public static Comic devolverComicLista(String id) {
		for (Comic comic : comicsImportados) {

			if (comic.getIdComic().equalsIgnoreCase(id)) {
				return comic;
			}
		}
		return null;
	}

	/**
	 * Realiza llamadas para inicializar listas de autocompletado.
	 *
	 * @throws SQLException si ocurre un error al acceder a la base de datos.
	 */
	public static void listasAutoCompletado() {
		listaID = DBUtilidades.obtenerValoresColumna("idComic");
		listaNombre = DBUtilidades.obtenerValoresColumna("tituloComic");
		listaNumeroComic = DBUtilidades.obtenerValoresColumna("numeroComic");
		listaEditor = DBUtilidades.obtenerValoresColumna("editorComic");
		listaArtista = DBUtilidades.obtenerValoresColumna("artistaComic");
		listaGuionista = DBUtilidades.obtenerValoresColumna("guionistaComic");
		listaVariante = DBUtilidades.obtenerValoresColumna("varianteComic");
		listaFirma = DBUtilidades.obtenerValoresColumna("firmaComic");

		listaReferencia = DBUtilidades.obtenerValoresColumna("urlReferenciaComic");

		listaID = ordenarLista(listaID);

		itemsList = Arrays.asList(listaNombre, listaNumeroComic, listaEditor, listaFirma, listaGuionista, listaVariante,
				listaArtista, listaReferencia);

	}

	public static void actualizarDatosAutoCompletado(String sentenciaSQL) {
		List<List<String>> listaOrdenada = new ArrayList<>(); // Cambia el tipo aquí
		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				List<String> nombreComicSet = new ArrayList<>();
				List<String> numeroComicSet = new ArrayList<>();
				List<String> nombreEditorSet = new ArrayList<>();
				List<String> nombreArtistaSet = new ArrayList<>();
				List<String> nombreVarianteSet = new ArrayList<>();
				List<String> nombreGuionistaSet = new ArrayList<>();
				List<String> nombreFirmaSet = new ArrayList<>();

				do {
					String nomComic = rs.getString("tituloComic").trim();
					nombreComicSet.add(nomComic);

					String numComic = rs.getString("numeroComic"); // Convertir a entero
					numeroComicSet.add(numComic);

					String nomEditor = rs.getString("editorComic").trim();
					nombreEditorSet.add(nomEditor);

					String nomArtista = rs.getString("artistaComic").trim();
					nombreArtistaSet.add(nomArtista);

					String nomVariante = rs.getString("varianteComic").trim();
					nombreVarianteSet.add(nomVariante);

					String nomGuionista = rs.getString("guionistaComic").trim();
					nombreGuionistaSet.add(nomGuionista);

					String nomFirma = rs.getString("firmaComic").trim();
					nombreFirmaSet.add(nomFirma);

				} while (rs.next());

				procesarDatosAutocompletado(nombreArtistaSet);
				procesarDatosAutocompletado(nombreVarianteSet);
				procesarDatosAutocompletado(nombreGuionistaSet);
				procesarDatosAutocompletado(nombreFirmaSet);

				// Eliminar elementos repetidos
				nombreComicSet = listaArregladaAutoComplete(nombreComicSet);
				numeroComicSet = listaArregladaAutoComplete(numeroComicSet);
				nombreFirmaSet = listaArregladaAutoComplete(nombreFirmaSet);
				nombreEditorSet = listaArregladaAutoComplete(nombreEditorSet);
				nombreArtistaSet = listaArregladaAutoComplete(nombreArtistaSet);
				nombreVarianteSet = listaArregladaAutoComplete(nombreVarianteSet);
				nombreGuionistaSet = listaArregladaAutoComplete(nombreGuionistaSet);

				Collections.sort(numeroComicSet, Comparable::compareTo);

				listaOrdenada.add(nombreComicSet);
				listaOrdenada.add(numeroComicSet.stream().map(String::valueOf).toList());
				listaOrdenada.add(nombreEditorSet);
				listaOrdenada.add(nombreFirmaSet);
				listaOrdenada.add(nombreGuionistaSet);
				listaOrdenada.add(nombreVarianteSet);
				listaOrdenada.add(nombreArtistaSet);

				ListasComicsDAO.listaOrdenada = listaOrdenada;
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	public static List<String> ordenarLista(List<String> listaStrings) {
		// Creamos una lista para almacenar los enteros
		List<Integer> listaEnteros = new ArrayList<>();

		// Convertimos los strings a enteros y los almacenamos en la lista de enteros
		for (String str : listaStrings) {
			listaEnteros.add(Integer.parseInt(str));
		}

		// Ordenamos la lista de enteros de menor a mayor
		Collections.sort(listaEnteros);

		// Creamos una nueva lista para almacenar los strings ordenados
		List<String> listaOrdenada = new ArrayList<>();

		// Convertimos los enteros ordenados a strings y los almacenamos en la lista
		// ordenada
		for (int num : listaEnteros) {
			listaOrdenada.add(String.valueOf(num));
		}

		// Devolvemos la lista ordenada de strings
		return listaOrdenada;
	}

	public static void procesarDatosAutocompletado(List<String> lista) {
		List<String> nombresProcesados = new ArrayList<>();
		for (String cadena : lista) {
			String[] nombres = cadena.split("-");
			for (String nombre : nombres) {
				nombre = nombre.trim();
				if (!nombre.isEmpty()) {
					nombresProcesados.add(nombre);
				}
			}
		}
		lista.clear(); // Limpiar la lista original
		lista.addAll(nombresProcesados); // Agregar los nombres procesados a la lista original
	}

	/**
	 * Limpia todas las listas utilizadas para autocompletado.
	 */
	public static void limpiarListas() {
		nombreComicList.clear();
		numeroComicList.clear();
		nombreEditorList.clear();
		listaFirma.clear();
	}

	/**
	 * Limpia todas las listas principales utilizadas en el contexto actual. Esto
	 * incluye listas de nombres, números de cómic, variantes, firmas, editoriales,
	 * guionistas, dibujantes, fechas, formatos, procedencias y cajas.
	 */
	public static void limpiarListasPrincipales() {
		listaNombre.clear();
		listaNumeroComic.clear();
		listaEditor.clear();
		listaArtista.clear();
		listaGuionista.clear();
		listaVariante.clear();
		nombreComicList.clear();
		nombreEditorList.clear();
		listaFirma.clear();
	}

	/**
	 * Permite reiniciar la lista de cómics.
	 */
	public static void reiniciarListaComics() {
		listaComics.clear(); // Limpia la lista de cómics
	}

	public static void reiniciarListas() {
		listaComics.clear();
		listaComicsCheck.clear();
		listaNombre.clear();
		listaNumeroComic.clear();
		listaEditor.clear();
		listaArtista.clear();
		listaGuionista.clear();
		listaVariante.clear();
		nombreComicList.clear();
		nombreEditorList.clear();
		listaFirma.clear();
		nombreComicList.clear();
		numeroComicList.clear();
		nombreEditorList.clear();
		listaReferencia.clear();
		listaImagenes.clear();
		listaLimpia.clear();
		listaLimpiaAutoCompletado.clear();
		comicsImportados.clear();
	}

	/**
	 * Comprueba si la lista de cómics contiene o no algún dato.
	 *
	 * @param listaComic Lista de cómics a comprobar.
	 * @return true si la lista está vacía, false si contiene elementos.
	 */
	public boolean checkList(List<Comic> listaComic) {
		if (listaComic.isEmpty()) {
			return true; // La lista está vacía
		}
		return false; // La lista contiene elementos
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
	 * Función que guarda los datos para autocompletado en una lista.
	 * 
	 * @param sentenciaSQL La sentencia SQL para obtener los datos.
	 * @param columna      El nombre de la columna que contiene los datos para
	 *                     autocompletado.
	 * @return Una lista de cadenas con los datos para autocompletado.
	 * @throws SQLException Si ocurre algún error al ejecutar la consulta SQL.
	 */
	public static List<String> guardarDatosAutoCompletado(String sentenciaSQL, String columna) {
		List<String> listaAutoCompletado = new ArrayList<>();
		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String datosAutocompletado = rs.getString(columna);
				if (columna.equals("tituloComic")) {
					listaAutoCompletado.add(datosAutocompletado.trim());
				} else if (columna.equals("direccionImagenComic")) {
					listaAutoCompletado.add(SOURCE_PATH + Utilidades.obtenerUltimoSegmentoRuta(datosAutocompletado));
				} else {
					String[] nombres = datosAutocompletado.split("-");
					for (String nombre : nombres) {
						nombre = nombre.trim();
						if (!nombre.isEmpty()) {
							listaAutoCompletado.add(nombre);
						}
					}
				}
			}

			return listaArregladaAutoComplete(listaAutoCompletado);

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e); // Manejar la excepción adecuadamente, por ejemplo, registrándola o
											// notificándola
			// En caso de excepción, podrías decidir retornar una lista parcial o marcar que
			// la operación fue interrumpida
			return listaAutoCompletado; // Retorna lo que se haya procesado hasta el momento
		}
	}

	/**
	 * Funcion que devuelve una lista en la que solamente se guardan aquellos datos
	 * que no se repiten
	 *
	 * @param listaComics
	 * @return
	 */
	public static List<Comic> listaArreglada(List<Comic> listaComics) {

		// Forma número 1 (Uso de Maps).
		Map<String, Comic> mapComics = new HashMap<>(listaComics.size());

		// Aquí está la magia
		for (Comic c : listaComics) {
			mapComics.put(c.getIdComic(), c);
		}

		// Agrego cada elemento del map a una nueva lista y muestro cada elemento.

		for (Entry<String, Comic> c : mapComics.entrySet()) {

			listaLimpia.add(c.getValue());

		}
		return listaLimpia;
	}

	/**
	 * Funcion que devuelve una lista en la que solamente se guardan aquellos datos
	 * que no se repiten
	 *
	 * @param listaComics
	 * @return
	 */
	public static <T extends Comparable<? super T>> List<T> listaArregladaAutoComplete(List<T> listaComics) {
		Set<T> uniqueSet = new HashSet<>();
		List<T> result = new ArrayList<>();

		for (T item : listaComics) {
			if (uniqueSet.add(item)) {
				result.add(item);
			}
		}

		// Ordenar la lista resultante de forma ascendente
		Collections.sort(result);

		return result;
	}

	/**
	 * Busca un cómic por su ID en una lista de cómics.
	 *
	 * @param comics  La lista de cómics en la que se realizará la búsqueda.
	 * @param idComic La ID del cómic que se está buscando.
	 * @return El cómic encontrado por la ID, o null si no se encuentra ninguno.
	 */
	public static Comic buscarComicPorID(List<Comic> comics, String idComic) {
		for (Comic c : comics) {
			if (c.getIdComic().equals(idComic)) {
				return c; // Devuelve el cómic si encuentra la coincidencia por ID
			}
		}
		return null; // Retorna null si no se encuentra ningún cómic con la ID especificada
	}

	/**
	 * Busca un cómic por su ID en una lista de cómics.
	 *
	 * @param comics  La lista de cómics en la que se realizará la búsqueda.
	 * @param idComic La ID del cómic que se está buscando.
	 * @return El cómic encontrado por la ID, o null si no se encuentra ninguno.
	 */
	public static boolean verificarComicPorID(List<Comic> comics, String idComic) {
		for (Comic c : comics) {
			if (c.getIdComic().equals(idComic)) {
				return true; // Devuelve el cómic si encuentra la coincidencia por ID
			}
		}
		return false; // Retorna null si no se encuentra ningún cómic con la ID especificada
	}

	public static boolean comprobarLista() {
		if (listaID.isEmpty()) {
			return false;
		}
		return true;
	}

}