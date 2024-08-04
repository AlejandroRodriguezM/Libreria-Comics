/**
 * Contiene las clases que hacen funcionar las diferentes funciones de uso de backend y frontend de todo el proyecto.
 *  
 * Este paquete incluye:
 * - Clases de backend que manejan la lógica de negocio, como procesamiento de datos, comunicación con la base de datos, etc.
 * - Clases de frontend que gestionan la interfaz de usuario, interacción con el usuario, presentación de datos, etc.
 * - Clases de utilidad que proporcionan funciones comunes utilizadas tanto en el backend como en el frontend.
 * - Otros componentes necesarios para la funcionalidad completa del proyecto.
 */
package ficherosFunciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Controladores.CargaComicsController;
import comicManagement.Comic;
import comicManagement.ComicFichero;
import dbmanager.ComicManagerDAO;
import dbmanager.DBUtilidades.TipoBusqueda;
import dbmanager.InsertManager;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.DirectoryChooser;

/**
 * Esta clase sirve para crear tanto los ficheros Excel como los ficheros CSV,
 * la exportacion de estos o la importacion
 *
 * @author Alejandro Rodriguez
 */
public class FuncionesExcel {

	/**
	 * Objeto para manejar la navegación en la interfaz gráfica.
	 */
	private static Ventanas nav = new Ventanas();

	private static final String USER_HOME_DIRECTORY = System.getProperty("user.home");
	private static final String DOCUMENTS_PATH = USER_HOME_DIRECTORY + File.separator + "Documents";

	// Para portadas
	public static final String DEFAULT_PORTADA_IMAGE_PATH = DOCUMENTS_PATH + File.separator + "libreria_comics"
			+ File.separator + Utilidades.nombreDB() + File.separator + "portadas";

	// Para la base de la ruta de imágenes predeterminada
	private static final String DEFAULT_IMAGE_PATH_BASE = DOCUMENTS_PATH + File.separator + "libreria_comics"
			+ File.separator + Utilidades.nombreDB();

	private static final String LOG_FILE_NAME = "log_"
			+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".txt";

	private static AtomicInteger numeroLineas = new AtomicInteger(0);
	private static AtomicInteger numeroLeidos = new AtomicInteger(0);

	private static AtomicInteger mensajeIdCounter = new AtomicInteger(0); // Contador para generar IDs únicos

	private static HashSet<String> mensajesUnicos = new HashSet<>(); // Para almacenar mensajes únicos

	private static final Logger logger = Logger.getLogger(FuncionesExcel.class.getName());

	/**
	 * Convierte un archivo Excel (XLSX) en formato CSV y guarda los datos en un
	 * nuevo archivo CSV.
	 *
	 * @param fichero El archivo Excel (XLSX) del cual se extraerán los datos para
	 *                el archivo CSV.
	 */
	public void createCSV(File fichero) {

		StringBuilder data = new StringBuilder();

		try (FileInputStream fis = new FileInputStream(fichero);
				Workbook workbook = new XSSFWorkbook(fis);
				FileOutputStream fos = new FileOutputStream(
						fichero.getAbsolutePath().substring(0, fichero.getAbsolutePath().lastIndexOf(".")) + ".csv");) {

			// Get first sheet from the workbook
			Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					switch (cell.getCellType()) {
					case BOOLEAN:
						data.append(cell.getBooleanCellValue() + ";");
						break;

					case NUMERIC:
						data.append(cell.getNumericCellValue() + ";");
						break;

					case STRING:
						data.append(cell.getStringCellValue() + ";");
						break;

					case BLANK:
						data.append("" + ";");
						break;

					default:
						data.append(cell + ";");
					}
				}
				data.append('\n');
			}

			fos.write(data.toString().getBytes());

		} catch (Exception e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Abre un cuadro de diálogo para seleccionar una carpeta de portadas.
	 *
	 * @return La carpeta seleccionada como objeto File.
	 */
	public File carpetaPortadas() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File directorio = directoryChooser.showDialog(null);
		// Verificar si el usuario ha cancelado la selección
		if (directorio == null) {
			return null; // O puedes lanzar una excepción o hacer algo más, dependiendo de tu lógica
		}
		String carpetaImagenes = directorio.getAbsolutePath() + File.separator + "copiaPortadas";
		return new File(carpetaImagenes);
	}

	/**
	 * Abre un cuadro de diálogo para seleccionar una carpeta de portadas en un hilo
	 * de tarea (Task).
	 *
	 * @return La carpeta seleccionada como objeto File.
	 */
	public static File carpetaPortadasTask() {
		final File[] directorio = new File[1];
		CountDownLatch latch = new CountDownLatch(1);

		Platform.runLater(() -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directorio[0] = directoryChooser.showDialog(null);
			latch.countDown();
		});

		try {
			latch.await(); // Esperar hasta que se complete la selección del directorio
		} catch (InterruptedException e) {
			Utilidades.manejarExcepcion(e);
			Thread.currentThread().interrupt(); // Re-interrupt the thread
		}

		return directorio[0];
	}

	/**
	 * Crea una tarea que se encarga de generar un archivo Excel a partir de los
	 * datos de la base de datos.
	 *
	 * @param fichero El archivo en el que se exportarán los datos.
	 * @return Una tarea que realiza la exportación y devuelve true si se realiza
	 *         con éxito, o false si ocurre un error.
	 */
	public Task<Boolean> crearExcelTask(List<Comic> listaComics, String tipoBusqueda, SimpleDateFormat dateFormat) {
		File[] directorioImagenes = { null };
		File[] directorioFichero = { null };

		numeroLineas.set(0);
		numeroLeidos.set(0);

		nav.cerrarMenuOpciones();

		if (tipoBusqueda.equalsIgnoreCase(TipoBusqueda.ELIMINAR.toString())) {
			String nombreCarpeta = dateFormat.format(new Date());

			String userDir = System.getProperty("user.home");
			String ubicacion = userDir + File.separator + "AppData" + File.separator + "Roaming";
			String direccion = ubicacion + File.separator + "libreria" + File.separator + Utilidades.nombreDB()
					+ File.separator + "backups" + File.separator + nombreCarpeta;

			try {
				Utilidades.copiaSeguridad(dateFormat);

				File carpetaLibreria = new File(direccion);
				if (!carpetaLibreria.exists()) {
					carpetaLibreria.mkdirs(); // This will create all necessary parent directories as well
				}

				File fichero = new File(carpetaLibreria, "BaseDatos.xlsx");
				if (fichero.createNewFile()) {
					logger.info("Fichero creado correctamente");
				} else {
					logger.warning("ERROR. Fichero no creado");
				}

				directorioImagenes[0] = carpetaLibreria;
				directorioFichero[0] = fichero;

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String frase = "Fichero Excel xlsx";

			String formato = "*.xlsx";

			File fichero = Utilidades.tratarFichero(frase, formato, true);

			if (fichero == null) {
				return null;
			}
			directorioFichero[0] = fichero;
			directorioImagenes[0] = carpetaPortadas();

		}

		numeroLineas.set(ComicManagerDAO.countRows());

		return trabajarFichero(listaComics, tipoBusqueda, directorioImagenes, directorioFichero);
	}

	public Task<Boolean> trabajarFichero(List<Comic> listaComics, String tipoBusqueda, File[] directorioImagenes,
			File[] directorioFichero) {
		return new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				try {
					Workbook libro = crearLibro(directorioFichero[0]);
					if (libro == null) {
						return false;
					}

					Sheet hoja = crearHoja(libro, "Base de datos comics");
					if (hoja == null) {
						return false;
					}

					crearEncabezados(hoja);
					AtomicReference<CargaComicsController> cargaComicsControllerRef = new AtomicReference<>();
					nav.verCargaComics(cargaComicsControllerRef);
					if (directorioFichero[0] != null) {

						List<Comic> listaComicsCopia = new ArrayList<>(listaComics);
						escribirDatosComics(listaComicsCopia, hoja, cargaComicsControllerRef, directorioImagenes[0]);
						escribirLibroYCSV(libro, directorioFichero[0]);
						actualizarProgreso(cargaComicsControllerRef);
					}

					if (directorioImagenes[0] != null) {
						copiarImagenesPortadaSiEsNecesario(tipoBusqueda, directorioImagenes[0]);

					}

					return true;
				} catch (IOException ex) {
					Utilidades.manejarExcepcion(ex);
					return false;
				}
			}
		};
	}

	private Workbook crearLibro(File fichero) {
		return fichero != null ? new XSSFWorkbook() : null;
	}

	private Sheet crearHoja(Workbook libro, String nombreHoja) {
		return libro != null ? libro.createSheet(nombreHoja) : null;
	}

	private void crearEncabezados(Sheet hoja) {
		String[] encabezados = { "idComic", // ID del cómic
				"tituloComic", // Título del cómic
				"codigoComic", // Código del cómic
				"numeroComic", // Número del cómic
				"precioComic", // Precio del cómic
				"fechaGradeo", // Fecha de grado
				"editorComic", // Editor del cómic
				"keyComentarios", // Comentarios
				"firmaComic", // Firma del comic
				"artistaComic", // Artista del cómic
				"guionistaComic", // Guionista del cómic
				"varianteComic", // Variante del cómic
				"direccionImagenComic", // Dirección de la imagen
				"urlReferenciaComic" // URL de referencia
		};

		Row fila = hoja.createRow(0);
		for (int i = 0; i < encabezados.length; i++) {
			fila.createCell(i).setCellValue(encabezados[i]);
		}
	}

	private void copiarImagenesPortadaSiEsNecesario(String tipoBusqueda, File directorioImagenes) {

		if (("Completa".equalsIgnoreCase(tipoBusqueda) || "Parcial".equalsIgnoreCase(tipoBusqueda))) {
			Utilidades.copiarDirectorio(directorioImagenes.getAbsolutePath(),
					FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH);
		}
	}

	private int escribirDatosComics(List<Comic> listaComics, Sheet hoja,
			AtomicReference<CargaComicsController> cargaComicsControllerRef, File directorioImagenes) {
		int indiceFinal = 1; // Comenzar desde 1 para omitir la fila de encabezado
		for (Comic comic : listaComics) {

			comic.sustituirCaracteres(comic);

			Row fila = hoja.createRow(indiceFinal);
			llenarFilaConDatos(comic, fila);

			cargaComics(comic, cargaComicsControllerRef, directorioImagenes, false);

			indiceFinal++;

			retrasarCarga();

			if (Thread.interrupted()) {
				break;
			}

		}

		return indiceFinal;
	}

	private void llenarFilaConDatos(Comic comic, Row fila) {
		fila.createCell(0).setCellValue(comic.getIdComic());
		fila.createCell(1).setCellValue(comic.getTituloComic());
		fila.createCell(2).setCellValue(comic.getCodigoComic());
		fila.createCell(3).setCellValue(comic.getNumeroComic());
		fila.createCell(4).setCellValue(comic.getPrecioComic());
		fila.createCell(5).setCellValue(comic.getFechaGradeo());
		fila.createCell(6).setCellValue(comic.getEditorComic());
		fila.createCell(7).setCellValue(comic.getKeyComentarios());
		fila.createCell(8).setCellValue(comic.getFirmaComic());
		fila.createCell(9).setCellValue(comic.getArtistaComic());
		fila.createCell(10).setCellValue(comic.getGuionistaComic());
		fila.createCell(11).setCellValue(comic.getVarianteComic());
		fila.createCell(12).setCellValue(comic.getDireccionImagenComic());
		fila.createCell(13).setCellValue(comic.getUrlReferenciaComic());
	}

	private void actualizarProgreso(AtomicReference<CargaComicsController> cargaComicsControllerRef) {
		Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics("", "100%", 100.0));
	}

	private void escribirLibroYCSV(Workbook libro, File directorioFichero) throws IOException {
		try (FileOutputStream outputStream = new FileOutputStream(directorioFichero)) {
			libro.write(outputStream);
			createCSV(directorioFichero);
		} catch (IOException ex) {
			Utilidades.manejarExcepcion(ex);
		}

	}

	public static void retrasarCarga() {
		try {
			Thread.sleep(10); // Delay de 0.5 segundos
		} catch (InterruptedException e) {
			logger.warning("El hilo fue interrumpido mientras dormía.");
			// Restablecer el estado de interrupción del hilo
			Thread.currentThread().interrupt(); // Re-interrumpir el hilo
		}
	}

	public static Task<Boolean> procesarArchivoCSVTask(File fichero) {

		return new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				AtomicBoolean isCancelled = new AtomicBoolean(false);

				try {
					if (isCancelled.get()) {
						isCancelled.set(true);
						return false; // Salir de la tarea si ha sido cancelada
					}

					checkCSVColumns(fichero.getAbsolutePath());
					numeroLineas.set(Utilidades.contarLineasFichero(fichero));
					procesarCSVInternamente(fichero, isCancelled);
					return true; // Indicar que la operación fue exitosa
				} catch (IOException e) {
					e.printStackTrace();
					Platform.runLater(() -> nav.alertaException("Error al leer el archivo CSV: " + e.getMessage()));
					return false; // Indicar que la operación falló
				}
			}
		};
	}

	private static void procesarCSVInternamente(File fichero, AtomicBoolean isCancelled) throws IOException {
		numeroLeidos.set(0); // Reiniciar el valor a 0 utilizando el método set() de AtomicInteger
		Utilidades.crearCarpeta();
		try (BufferedReader lineReader = new BufferedReader(new FileReader(fichero), 8192)) { // Tamaño del búfer de
																								// 8192 caracteres
			// Inicializar directorio con el valor predeterminado
			File directorio = new File(DEFAULT_PORTADA_IMAGE_PATH + File.separator);

			// Obtener confirmación para continuar la subida de portadas
			CompletableFuture<Boolean> confirmacionFuture = nav.cancelarSubidaPortadas();
			boolean continuarSubida = confirmacionFuture.join();

			// Actualizar el directorio si se va a continuar la subida de portadas
			if (continuarSubida) {
				directorio = carpetaPortadasTask();
				directorio = (directorio == null) ? new File(DEFAULT_PORTADA_IMAGE_PATH + File.separator) : directorio;
			}

			// Inicializar referencia al controlador de carga de cómics
			AtomicReference<CargaComicsController> cargaComicsControllerRef = new AtomicReference<>();
			nav.verCargaComics(cargaComicsControllerRef);

			// Leer la primera línea del archivo
			lineReader.readLine();

			// Copiar directorio al directorio predeterminado
			Utilidades.copyDirectory(directorio.getAbsolutePath(), DEFAULT_PORTADA_IMAGE_PATH);
			AtomicReference<File> directorioRef = new AtomicReference<>(directorio);

			// Procesar líneas restantes del archivo
			lineReader.lines().forEach(lineText -> {
				if (isCancelled.get()) {
					return; // Salir del bucle si la tarea ha sido cancelada
				}

				try {
					if (isCancelled.get()) {
						// Si se produce una interrupción, sal del bucle
						return;
					}

					Comic comicNuevo = ComicFichero.datosComicFichero(lineText);
					comicNuevo.sustituirCaracteres(comicNuevo);
					if (comicNuevo != null) {
						InsertManager.insertarDatos(comicNuevo, true);
					}

					cargaComics(comicNuevo, cargaComicsControllerRef, directorioRef.get(), true);

				} catch (Exception e) {
					// Manejar cualquier excepción durante el procesamiento de la línea
					e.printStackTrace();
				}

				retrasarCarga();

				if (Thread.interrupted()) {
					isCancelled.set(true);
					return;
				}
			});

			// Actualizar UI con el progreso final
			Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics("", "100%", 100.0));

			// Abrir el archivo de registro
			Platform.runLater(() -> Utilidades.abrirArchivo(DEFAULT_IMAGE_PATH_BASE + File.separator + LOG_FILE_NAME));

		} catch (IOException e) {
			// Propagar la excepción al nivel superior
			logger.warning("An IOException occurred while processing the file.");
			throw e;
		}
	}

	public static void cargaComics(Comic comicNuevo, AtomicReference<CargaComicsController> cargaComicsControllerRef,
			File directorio, boolean esImportado) {
		// Verificar si el cómic es nulo
		if (comicNuevo == null || cargaComicsControllerRef == null) {
			return;
		}

		// Obtener nombre de portada y nombre modificado
		String nombrePortada = "";
		String nombreModificado = "";
		if (esImportado) {
			nombrePortada = Utilidades.obtenerNombrePortada(false, comicNuevo.getDireccionImagenComic());
			nombreModificado = Utilidades.convertirNombreArchivo(nombrePortada);
			if (directorio != null && !Utilidades.existeArchivo(directorio.getAbsolutePath(), nombrePortada)) {
				copiarPortadaPredeterminada(DEFAULT_PORTADA_IMAGE_PATH, nombreModificado);
			}
		}

		// Generar un ID único
		String mensajeId = String.valueOf(mensajeIdCounter.getAndIncrement());

		// Construir información del cómic
		String comicInfo = buildComicInfo(comicNuevo);

		// Añadir información del cómic a la lista mensajesUnicos
		mensajesUnicos.add(mensajeId + ": " + comicInfo);

		if (esImportado && !Utilidades.existeArchivo(DEFAULT_PORTADA_IMAGE_PATH, nombreModificado)) {
			mensajesUnicos.add(comicInfo);
		}

		// Calcular el progreso de la carga
		double progress = calculateProgress();

		// Actualizar la interfaz de usuario
		updateUI(progress, comicInfo, cargaComicsControllerRef);
	}

	private static String buildComicInfo(Comic comic) {
		// Construye la información del cómic en un formato más detallado y legible
		StringBuilder info = new StringBuilder();

		// Añade el nombre del cómic
		info.append("Nombre del Cómic: ").append(comic.getTituloComic()).append("\n");

		// Devuelve la cadena construida
		return info.toString();
	}

	private static double calculateProgress() {
		return (double) numeroLeidos.get() / (numeroLineas.get() + 1);
	}

	private static void updateUI(double progress, String comicInfo,
			AtomicReference<CargaComicsController> cargaComicsControllerRef) {

		StringBuilder textoBuilder = new StringBuilder();
		String porcentaje = String.format("%.2f%%", progress * 100);
		if (nav.isVentanaCerrada()) {
			// Código para actualizar la interfaz de usuario cuando la ventana está cerrada
			nav.verCargaComics(cargaComicsControllerRef);

			StringBuilder textoFiltrado = new StringBuilder();
			List<String> mensajesOrdenados = new ArrayList<>(mensajesUnicos);
			Collections.sort(mensajesOrdenados, Comparator.comparingInt(m -> Integer.parseInt(m.split(":")[0])));

			for (String mensajeUnico : mensajesOrdenados) {
				if (!mensajeUnico.equalsIgnoreCase(textoBuilder.toString())) {
					int colonIndex = mensajeUnico.indexOf(":");
					if (colonIndex != -1) {
						textoFiltrado.append(mensajeUnico.substring(colonIndex + 2));
					}
				}
			}

			Platform.runLater(() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics(textoFiltrado.toString(),
					porcentaje, progress));
		}

		Platform.runLater(
				() -> cargaComicsControllerRef.get().cargarDatosEnCargaComics(comicInfo, porcentaje, progress));

		numeroLeidos.incrementAndGet();
	}

	/**
	 * Función que copia la portada predeterminada y cambia su nombre
	 *
	 * @param defaultImagePath El directorio de las portadas
	 * @param nombreModificado El nombre del archivo modificado
	 * @throws IOException
	 */
	public static void copiarPortadaPredeterminada(String defaultImagePath, String nombreModificado) {
		if (defaultImagePath == null || nombreModificado == null) {
			throw new IllegalArgumentException("defaultImagePath y nombreModificado no pueden ser nulos");
		}

		File sourceFile = new File(defaultImagePath, nombreModificado);
		File destinationFile = new File(defaultImagePath, nombreModificado);

		try {
			if (!sourceFile.exists()) {
				try (InputStream input = Utilidades.class.getResourceAsStream("/imagenes/sinPortada.jpg");
						OutputStream output = new FileOutputStream(destinationFile)) {

					File destinationDirectory = destinationFile.getParentFile();
					if (!destinationDirectory.exists()) {
						destinationDirectory.mkdirs();
					}

					if (destinationFile.createNewFile()) {
						logger.info("Fichero copiado correctamente");
					} else {
						logger.warning("ERROR. Fichero no copiado");
					}

					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = input.read(buffer)) != -1) {
						output.write(buffer, 0, bytesRead);
					}
				}
			} else {
				Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	private static void checkCSVColumns(String filePath) throws IOException {
		// Columnas esperadas
		String[] expectedColumns = { "idComic", // ID del cómic
				"tituloComic", // Título del cómic
				"codigoComic", // Código del cómic
				"numeroComic", // Número del cómic
				"precioComic", // Número del cómic
				"fechaGradeo", // Fecha de gradeo
				"editorComic", // Editor del cómic
				"keyComentarios", // Comentarios clave
				"firmaComic", "artistaComic", // Artista del cómic
				"guionistaComic", // Guionista del cómic
				"varianteComic", // Variante del cómic
				"direccionImagenComic", // Dirección de la imagen
				"urlReferenciaComic" // URL de referencia
		};

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			if ((line = br.readLine()) != null) {
				// Obtener las columnas de la primera línea del archivo CSV
				String[] columns = line.split(";");
				// Verificar si las columnas coinciden con las esperadas
				if (columns.length != expectedColumns.length) {
					throw new IOException("El número de columnas no coincide");
				}
				for (int i = 0; i < columns.length; i++) {
					System.out.println(expectedColumns[i]);
					if (!columns[i].trim().equalsIgnoreCase(expectedColumns[i])) {
						System.err.println(expectedColumns[i] + " " + columns[i]);
						throw new IOException("El nombre de la columna en la posición " + i + " no coincide");
					}
				}
			} else {
				throw new IOException("El archivo CSV está vacío");
			}
		}
	}
}
