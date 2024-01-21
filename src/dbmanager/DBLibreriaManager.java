package dbmanager;

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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Controladores.CrearBBDDController;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import comicManagement.Comic;
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



	/////////////////////////////////
	//// FUNCIONES CREACION FICHEROS//
	/////////////////////////////////

	/**
	 * Funcion que crea una copia de seguridad de la base de datos siempre que el
	 * sistema operativo sea Linux
	 *
	 * @param fichero
	 */
	public static void backupLinux(File fichero) {
		try {
			fichero.createNewFile();

			// Crear una lista para los comandos
			String[] command = { "mysqldump", "-u" + ConectManager.DB_USER, "-p" + ConectManager.DB_PASS, "-B",
					ConectManager.DB_NAME, "--routines=true", "--result-file=" + fichero.getAbsolutePath() };

			// Crear un ProcessBuilder y configurar los comandos
			ProcessBuilder pb = new ProcessBuilder(command);

			// Redirigir errores y salida estándar al sistema actual
			pb.redirectErrorStream(true);
			pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

			// Iniciar el proceso
			Process process = pb.start();
			int exitCode = process.waitFor();

			// Verificar el código de salida para manejar errores
			if (exitCode == 0) {
				System.out.println("Copia de seguridad creada exitosamente.");
			} else {
				System.err.println("Error al crear la copia de seguridad. Código de salida: " + exitCode);
			}

		} catch (IOException | InterruptedException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Funcion que crea una copia de seguridad de la base de datos siempre que el
	 * sistema operativo sea Windows
	 *
	 * @param fichero
	 */
	public static void backupWindows(File fichero) {
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

			String command[] = new String[] { mysqlDump, "-u" + ConectManager.DB_USER, "-p" + ConectManager.DB_PASS,
					"-B", ConectManager.DB_NAME, "--hex-blob", "--routines=true", "--result-file=" + fichero };
			ProcessBuilder pb = new ProcessBuilder(Arrays.asList(command));
			pb.redirectError(Redirect.INHERIT);
			pb.redirectOutput(Redirect.to(fichero));
			pb.start();

		} catch (Exception e) {
			Utilidades.manejarExcepcion(e);
		}
	}




	// **************************************//
	// ****FUNCIONES DE LA LIBRERIA**********//
	// **************************************//

	/**
	 * Permite reiniciar la lista de cómics.
	 */
	public void reiniciarBBDD() {
		listaComics.clear(); // Limpia la lista de cómics
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

		estadisticaStr.append("Estadisticas de comics de la base de datos: " + ConectManager.DB_NAME + ", a fecha de: "
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
			DBLibreriaManager.createTable(datos);
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
					if (DBLibreriaManager.reconstruirBBDD(datos)) {
						return true;
					}
				}
			} else {
				// La tabla no existe, reconstruimos la base de datos
				if (DBLibreriaManager.reconstruirBBDD(datos)) {
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

}