package dbmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

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
public class ListaComicsDAO {

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

	/**
	 * Lista de cómics.
	 */
	public static List<Comic> listaComics = new ArrayList<>();

	/**
	 * Lista de cómics con verificación.
	 */
	public static List<Comic> listaComicsCheck = new ArrayList<>();

	public static List<String> listaID = new ArrayList<>();

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
	 * Lista de cómics limpios.
	 */
	public static List<Comic> listaLimpia = new ArrayList<>();

	/**
	 * Lista de sugerencias de autocompletado de entrada limpia.
	 */
	public static List<String> listaLimpiaAutoCompletado = new ArrayList<>();

	public static List<Comic> comicsImportados = new ArrayList<>();

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
	public static ObservableList<Comic> comicsGuardadosList = FXCollections.observableArrayList();

	public static boolean verificarIDExistente(String id, boolean esGuardado) {
		// Verificar que el id no sea nulo ni esté vacío
		if (id == null || id.isEmpty()) {
			return false;
		}

		// Buscar en comicsGuardadosList si es necesario
		if (esGuardado) {
			for (Comic comic : comicsGuardadosList) {
				if (id.equals(comic.getid())) {
					return true; // Si encuentra un comic con el mismo id, devuelve true
				}
			}
		}

		// Buscar en comicsImportados
		for (Comic comic : comicsImportados) {
			if (id.equalsIgnoreCase(comic.getid())) {
				return true; // Si encuentra un comic con el mismo id, devuelve true
			}
		}

		return false; // Si no encuentra ningún comic con el mismo id, devuelve false
	}

	public static Comic devolverComicLista(String id) {
		for (Comic comic : comicsImportados) {

			if (comic.getid().equalsIgnoreCase(id)) {
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
		listaID = DBUtilidades.obtenerValoresColumna("ID");
		listaNombre = DBUtilidades.obtenerValoresColumna("nomComic");
		listaNumeroComic = DBUtilidades.obtenerValoresColumna("numComic");
		listaVariante = DBUtilidades.obtenerValoresColumna("nomVariante");
		listaFirma = DBUtilidades.obtenerValoresColumna("firma");
		listaFormato = DBUtilidades.obtenerValoresColumna("formato");
		listaEditorial = DBUtilidades.obtenerValoresColumna("nomEditorial");
		listaGuionista = DBUtilidades.obtenerValoresColumna("nomGuionista");
		listaDibujante = DBUtilidades.obtenerValoresColumna("nomDibujante");
		listaProcedencia = DBUtilidades.obtenerValoresColumna("procedencia");
		listaCaja = DBUtilidades.obtenerValoresColumna("nivel_gradeo");
		listaImagenes = DBUtilidades.obtenerValoresColumna("portada");
		
		listaID = ordenarLista(listaID);

		// Ordenar listaNumeroComic como enteros
		List<Integer> numerosComic = listaNumeroComic.stream().map(Integer::parseInt).collect(Collectors.toList());
		Collections.sort(numerosComic);
		listaNumeroComic = numerosComic.stream().map(String::valueOf).collect(Collectors.toList());

		itemsList = Arrays.asList(listaNombre, listaNumeroComic, listaVariante, listaProcedencia, listaFormato,
				listaDibujante, listaGuionista, listaEditorial, listaFirma, listaCaja);

	}

	public static void actualizarDatosAutoCompletado(String sentenciaSQL) {
		List<List<String>> listaOrdenada = new ArrayList<>(); // Cambia el tipo aquí
		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				List<String> nombreComicSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreGuionistaSet = new ArrayList<>(); // Cambia el tipo aquí
				List<Integer> numeroComicSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreVarianteSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> numeroCajaSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreProcedenciaSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreFormatoSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreEditorialSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreDibujanteSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreFirmaSet = new ArrayList<>(); // Cambia el tipo aquí

				do {
					String nomComic = rs.getString("nomComic").trim();
					nombreComicSet.add(nomComic);

					String nomGuionista = rs.getString("nomGuionista").trim();
					nombreGuionistaSet.add(nomGuionista);

					int numComic = rs.getInt("numComic"); // Convertir a entero
					numeroComicSet.add(numComic);

					String nomVariante = rs.getString("nomVariante").trim();
					nombreVarianteSet.add(nomVariante);

					String nivelGradeo = rs.getString("nivel_gradeo").trim();
					numeroCajaSet.add(nivelGradeo);

					String procedencia = rs.getString("procedencia").trim();
					nombreProcedenciaSet.add(procedencia);

					String formato = rs.getString("formato").trim();
					nombreFormatoSet.add(formato);

					String nomEditorial = rs.getString("nomEditorial").trim();
					nombreEditorialSet.add(nomEditorial);

					String nomDibujante = rs.getString("nomDibujante").trim();
					nombreDibujanteSet.add(nomDibujante);

					String firma = rs.getString("firma").trim();
					nombreFirmaSet.add(firma);

				} while (rs.next());

				procesarDatosAutocompletado(nombreGuionistaSet);
				procesarDatosAutocompletado(nombreVarianteSet);
				procesarDatosAutocompletado(nombreEditorialSet);
				procesarDatosAutocompletado(nombreDibujanteSet);
				procesarDatosAutocompletado(nombreFirmaSet);

				// Eliminar elementos repetidos
				nombreComicSet = listaArregladaAutoComplete(nombreComicSet);
				numeroComicSet = listaArregladaAutoComplete(numeroComicSet);
				nombreGuionistaSet = listaArregladaAutoComplete(nombreGuionistaSet);
				nombreVarianteSet = listaArregladaAutoComplete(nombreVarianteSet);
				numeroCajaSet = listaArregladaAutoComplete(numeroCajaSet);
				nombreProcedenciaSet = listaArregladaAutoComplete(nombreProcedenciaSet);
				nombreFormatoSet = listaArregladaAutoComplete(nombreFormatoSet);
				nombreEditorialSet = listaArregladaAutoComplete(nombreEditorialSet);
				nombreDibujanteSet = listaArregladaAutoComplete(nombreDibujanteSet);
				nombreFirmaSet = listaArregladaAutoComplete(nombreFirmaSet);

				// Ordenar lista de números de comic
				Collections.sort(numeroComicSet, new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						return o1.compareTo(o2);
					}
				});

				listaOrdenada.add(nombreComicSet);
				listaOrdenada.add(numeroComicSet.stream().map(String::valueOf).collect(Collectors.toList()));
				listaOrdenada.add(nombreVarianteSet);
				listaOrdenada.add(nombreProcedenciaSet);
				listaOrdenada.add(nombreFormatoSet);
				listaOrdenada.add(nombreDibujanteSet);
				listaOrdenada.add(nombreGuionistaSet);
				listaOrdenada.add(nombreEditorialSet);
				listaOrdenada.add(nombreFirmaSet);
				listaOrdenada.add(numeroCajaSet);

				ListaComicsDAO.listaOrdenada = listaOrdenada;
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
		nombreFirmaList.clear();
		nombreGuionistaList.clear();
		nombreVarianteList.clear();
		numeroCajaList.clear();
		nombreProcedenciaList.clear();
		nombreFormatoList.clear();
		nombreEditorialList.clear();
		nombreDibujanteList.clear();
	}

	/**
	 * Limpia todas las listas principales utilizadas en el contexto actual. Esto
	 * incluye listas de nombres, números de cómic, variantes, firmas, editoriales,
	 * guionistas, dibujantes, fechas, formatos, procedencias y cajas.
	 */
	public static void limpiarListasPrincipales() {
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
	}

	/**
	 * Permite reiniciar la lista de cómics.
	 */
	public static void reiniciarListaComics() {
		listaComics.clear(); // Limpia la lista de cómics
	}

	/**
	 * Funcion que permite limpiar de contenido la lista de comics guardados
	 */
	public static void limpiarListaGuardados() {
		comicsGuardadosList.clear();
	}

	public static void reiniciarListas() {
		listaComics.clear();
		listaComicsCheck.clear();
		listaID.clear();
		listaNombre.clear();
		listaNumeroComic.clear();
		listaVariante.clear();
		listaFirma.clear();
		listaFormato.clear();
		listaEditorial.clear();
		listaGuionista.clear();
		listaDibujante.clear();
		listaFecha.clear();
		listaProcedencia.clear();
		listaCaja.clear();
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
	 * Agrega un elemento único a la lista principal de cómics guardados,
	 * ordenándolos por ID en orden descendente de longitud.
	 *
	 * @param comicToAdd Comic a añadir a la lista principal.
	 */
	public static void agregarElementoUnico(Comic comicToAdd) {
		// Usamos un Set para mantener los elementos únicos
		Set<String> idsUnicos = comicsGuardadosList.stream().map(Comic::getid).collect(Collectors.toSet());

		// Verificamos si la ID del cómic ya está en la lista principal
		if (!idsUnicos.contains(comicToAdd.getid())) {
			// Añadimos el cómic a la lista principal
			comicsGuardadosList.add(comicToAdd);

			// Ordenamos la lista por ID en orden descendente de longitud
			comicsGuardadosList
					.sort(Comparator.comparing(Comic::getid, Comparator.comparingInt(String::length).reversed()));
		}
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
	         PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            String datosAutocompletado = rs.getString(columna);
	            if (columna.equals("nomComic")) {
	                listaAutoCompletado.add(datosAutocompletado.trim());
	            } else if (columna.equals("portada")) {
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

	        listaAutoCompletado = listaArregladaAutoComplete(listaAutoCompletado);

	    } catch (SQLException e) {
	        Utilidades.manejarExcepcion(e);
	    }

	    return listaAutoCompletado;
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
			mapComics.put(c.getid(), c);
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
			if (c.getid().equals(idComic)) {
				return c; // Devuelve el cómic si encuentra la coincidencia por ID
			}
		}
		return null; // Retorna null si no se encuentra ningún cómic con la ID especificada
	}

	public static boolean comprobarLista() {
		if (listaID.isEmpty()) {
			return false;
		}
		return true;
	}

	public static void mostrarTamanioListas() {
		System.out.println("Tamaño de listaComics: " + listaComics.size());
		System.out.println("Tamaño de listaComicsCheck: " + listaComicsCheck.size());
		System.out.println("Tamaño de listaID: " + listaID.size());
		System.out.println("Tamaño de listaNombre: " + listaNombre.size());
		System.out.println("Tamaño de listaNumeroComic: " + listaNumeroComic.size());
		System.out.println("Tamaño de listaVariante: " + listaVariante.size());
		System.out.println("Tamaño de listaFirma: " + listaFirma.size());
		System.out.println("Tamaño de listaFormato: " + listaFormato.size());
		System.out.println("Tamaño de listaEditorial: " + listaEditorial.size());
		System.out.println("Tamaño de listaGuionista: " + listaGuionista.size());
		System.out.println("Tamaño de listaDibujante: " + listaDibujante.size());
		System.out.println("Tamaño de listaFecha: " + listaFecha.size());
		System.out.println("Tamaño de listaProcedencia: " + listaProcedencia.size());
		System.out.println("Tamaño de listaCaja: " + listaCaja.size());
		System.out.println("Tamaño de nombreComicList: " + nombreComicList.size());
		System.out.println("Tamaño de numeroComicList: " + numeroComicList.size());
		System.out.println("Tamaño de nombreFirmaList: " + nombreFirmaList.size());
		System.out.println("Tamaño de nombreGuionistaList: " + nombreGuionistaList.size());
		System.out.println("Tamaño de nombreVarianteList: " + nombreVarianteList.size());
		System.out.println("Tamaño de numeroCajaList: " + numeroCajaList.size());
		System.out.println("Tamaño de nombreProcedenciaList: " + nombreProcedenciaList.size());
		System.out.println("Tamaño de nombreFormatoList: " + nombreFormatoList.size());
		System.out.println("Tamaño de nombreEditorialList: " + nombreEditorialList.size());
		System.out.println("Tamaño de nombreDibujanteList: " + nombreDibujanteList.size());
		System.out.println("Tamaño de listaImagenes: " + listaImagenes.size());
		System.out.println("Tamaño de listaLimpia: " + listaLimpia.size());
		System.out.println("Tamaño de listaLimpiaAutoCompletado: " + listaLimpiaAutoCompletado.size());
		System.out.println("Tamaño de comicsImportados: " + comicsImportados.size());
	}

	/**
	 * Genera un archivo de estadísticas basado en los datos de la base de datos de
	 * cómics. Los datos se organizan en diferentes categorías y se cuentan para su
	 * análisis. Luego, se genera un archivo de texto con las estadísticas y se abre
	 * con el programa asociado.
	 */
	public static void generar_fichero_estadisticas() {
		// Crear HashMaps para almacenar los datos de cada campo sin repetición y sus
		// conteos
		Map<String, Integer> nomComicEstadistica = new HashMap<>();

		Map<String, Integer> nivelGradeoEstadistica = new HashMap<>();
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
		try (Connection conn = ConectManager.conexion();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(CONSULTA_SQL)) {

			// Procesar los datos y generar la estadística
			while (rs.next()) {
				// Obtener los datos de cada campo
				String nomComic = rs.getString("nomComic");
				int numComic = rs.getInt("numComic");
				String nivelGradeo = rs.getString("nivel_gradeo");
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
				nivelGradeoEstadistica.put(nivelGradeo, nivelGradeoEstadistica.getOrDefault(nivelGradeo, 0) + 1);

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

		String fechaActual = Utilidades.obtenerFechaActual();

		estadisticaStr.append("Estadisticas de comics de la base de datos: " + ConectManager.DB_NAME + ", a fecha de: "
				+ fechaActual + "\n");

		// Agregar los valores de nomComic a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de los nombres de comics:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nomComicList = sortByValue(nomComicEstadistica);
		for (Map.Entry<String, Integer> entry : nomComicList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de nivelGradeo a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de las cajas:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nivelGradeoList = sortByValue(nivelGradeoEstadistica);
		for (Map.Entry<String, Integer> entry : nivelGradeoList) {
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
		String nombreArchivo = "estadistica_" + fechaActual + ".txt";
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String rutaCompleta = carpetaLibreria + File.separator + nombreArchivo;

		try (PrintWriter writer = new PrintWriter(new FileWriter(rutaCompleta))) {
			writer.print(estadisticaStr);

			// Abrir el archivo con el programa asociado en el sistema
			Utilidades.abrirArchivo(rutaCompleta);

		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

}