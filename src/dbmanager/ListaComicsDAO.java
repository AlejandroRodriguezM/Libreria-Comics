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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Funcionamiento.Utilidades;
import comicManagement.Comic;

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
	private final static String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private final static String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	/**
	 * Construimos la ruta al directorio "libreria_comics" dentro de "Documents" y
	 * añadimos el nombre de la base de datos y la carpeta "portadas".
	 */
	private final static String SOURCE_PATH = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
			+ ConectManager.DB_NAME + File.separator + "portadas" + File.separator;

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
	 * Lista de cómics limpios.
	 */
	public static List<Comic> listaLimpia = new ArrayList<>();

	/**
	 * Lista de sugerencias de autocompletado de entrada limpia.
	 */
	public static List<String> listaLimpiaAutoCompletado = new ArrayList<>();

	public static List<Comic> comicsImportados = new ArrayList<Comic>();

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

	public static boolean verificarIDExistente(String id) {
		for (Comic comic : comicsImportados) {

			if (comic.getID().equalsIgnoreCase(id)) {
				return true;
			}
		}
		return false;
	}

	public static Comic devolverComicLista(String id) {
		for (Comic comic : comicsImportados) {

			if (comic.getID().equalsIgnoreCase(id)) {
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
		listaNombre = DBUtilidades.obtenerValoresColumna("nomComic");
		listaNumeroComic = DBUtilidades.obtenerValoresColumna("numComic");
		listaVariante = DBUtilidades.obtenerValoresColumna("nomVariante");
		listaFirma = DBUtilidades.obtenerValoresColumna("firma");
		listaFormato = DBUtilidades.obtenerValoresColumna("formato");
		listaEditorial = DBUtilidades.obtenerValoresColumna("nomEditorial");
		listaGuionista = DBUtilidades.obtenerValoresColumna("nomGuionista");
		listaDibujante = DBUtilidades.obtenerValoresColumna("nomDibujante");
		listaProcedencia = DBUtilidades.obtenerValoresColumna("procedencia");
		listaCaja = DBUtilidades.obtenerValoresColumna("caja_deposito");
		listaImagenes = DBUtilidades.obtenerValoresColumna("portada");

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
	public void reiniciarBBDD() {
		listaComics.clear(); // Limpia la lista de cómics
	}

	/**
	 * Funcion que permite limpiar de contenido la lista de comics guardados
	 */
	public static void limpiarListaGuardados() {
		comicsGuardadosList.clear();
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
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery()) {

			if (rs != null && rs.first()) {
				do {
					String datosAutocompletado = rs.getString(columna);
					if (columna.equals("nomComic")) {
						listaAutoCompletado.add(datosAutocompletado.trim());
					} else if (columna.equals("portada")) {
						listaAutoCompletado
								.add(SOURCE_PATH + Utilidades.obtenerUltimoSegmentoRuta(datosAutocompletado));
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

				listaAutoCompletado = listaArregladaAutoComplete(listaAutoCompletado);
			}
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
			mapComics.put(c.getID(), c);
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
	public static List<String> listaArregladaAutoComplete(List<String> listaComics) {
		Set<String> uniqueSet = new HashSet<>();
		List<String> result = new ArrayList<>();

		for (String s : listaComics) {
			if (uniqueSet.add(s)) {
				result.add(s);
			}
		}

		// Ordenar la lista resultante de forma ascendente
		result.sort(String::compareTo);

		return result;
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
		try (Connection conn = ConectManager.conexion();
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
		String nombreArchivo = "estadistica_" + fechaActual + ".txt";
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String rutaCompleta = carpetaLibreria + File.separator + nombreArchivo;

		try (PrintWriter writer = new PrintWriter(new FileWriter(rutaCompleta))) {
			writer.print(estadisticaStr);

			// Abrir el archivo con el programa asociado en el sistema
			Utilidades.abrirArchivoConProgramaAsociado(rutaCompleta);

		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

}