/**
 * Contiene las clases que hacen funcionar las diferentes funciones de uso de back end y front de todo el proyecto
 *  
*/
package Funcionamiento;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.json.JSONException;

import Apis.ApiCambioDivisas;
import Apis.ApiISBNGeneral;
import Apis.ApiMarvel;
import comicManagement.Comic;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import webScrap.WebScraperPreviewsWorld;

/**
 * Esta clase sirve para realizar diferentes funciones realizanas con la
 * comprobacion del sistema operativo que ejecuta este programa, al igual que
 * comprobar cual es tu navegador principal de internet
 *
 * @author Alejandro Rodriguez
 */
public class Utilidades {

	/**
	 * Sistema operativo actual.
	 */
	public static String os = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);

	/**
	 * Mapa que almacena tasas de cambio.
	 */
	private static final Map<String, Double> tasasDeCambio = new HashMap<>();

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
	public final static String SOURCE_PATH = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
			+ ConectManager.DB_NAME + File.separator + "portadas";

	/**
	 * Verifica si el sistema operativo es Windows.
	 *
	 * @return true si el sistema operativo es Windows, false en caso contrario.
	 */
	public static boolean isWindows() {
		return os.contains("win");
	}

	/**
	 * Verifica si el sistema operativo es macOS (Mac).
	 *
	 * @return true si el sistema operativo es macOS, false en caso contrario.
	 */
	public static boolean isMac() {
		return os.contains("mac");
	}

	/**
	 * Verifica si el sistema operativo es Unix o Linux.
	 *
	 * @return true si el sistema operativo es Unix o Linux, false en caso
	 *         contrario.
	 */
	public static boolean isUnix() {
		return os.contains("nux");
	}

	/**
	 * Funcion que permite comprobar que navegadores tienes instalados en el sistema
	 * operativo linux y abre aquel que tengas en predeterminado.
	 *
	 * @param url
	 * @return
	 */
	public static StringBuffer navegador(String url) {
		String[] browsers = { "google-chrome", "firefox", "mozilla", "epiphany", "konqueror", "netscape", "opera",
				"links", "lynx" };

		StringBuffer cmd = new StringBuffer();
		for (int i = 0; i < browsers.length; i++) {
			if (i == 0) {
				cmd.append(String.format("%s \"%s\"", browsers[i], url));
			} else {
				cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
			}
		}
		return cmd;
	}

	/**
	 * Funcion que permite abrir navegador y pagina web de GitHub en Windows
	 *
	 * @param url
	 */
	public static void accesoWebWindows(String url) {
		try {
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
		} catch (IOException e) {
			manejarExcepcion(e);
		}
	}

	/**
	 * Funcion que permite abrir navegador y pagina web de GitHub en Linux
	 *
	 * @param url
	 */
	public static void accesoWebLinux(String url) {
		Runtime rt = Runtime.getRuntime();

		StringBuffer cmd;
		try {
			cmd = Utilidades.navegador(url);
			rt.exec(new String[] { "sh", "-c", cmd.toString() }); // Ejecuta el bucle y permite abrir el navegador que
			// tengas principal
		} catch (IOException e) {
			manejarExcepcion(e);
		}
	}

	/**
	 * Funcion que permite abrir navegador y pagina web de GitHub en Linux
	 *
	 * @param url
	 */
	public static void accesoWebMac(String url) {
		Runtime runtime = Runtime.getRuntime();
		String[] args = { "osascript", "-e", "open location \"" + url + "\"" };
		try {
			runtime.exec(args);
		} catch (IOException e) {
			manejarExcepcion(e);
		}
	}

	/**
	 * Funcion que cambia una ',' por un guion '-' y un guion '-' sin espacios
	 * delante o detrás por " - "
	 *
	 * @param dato
	 * @return
	 */
	public static String comaYGuionPorEspaciado(String dato) {
		String resultado = "";
		if (dato != null) {
			// Reemplazar guion '-' sin espacios delante o detrás por " - "
			if (dato.contains("-") && !dato.contains(" - ")) {
				dato = dato.replace("-", " - ");
			}
			// Reemplazar coma ',' por " - "
			if (dato.contains(",")) {
				dato = dato.replace(",", " - ");
			}
			resultado = eliminarEspacios(dato);
		}
		return resultado;
	}

	/**
	 * Funcion que elimina los espacios del principios y finales
	 *
	 * @param campos
	 * @return
	 */
	public static String eliminarEspacios(String dato) {

		String resultado = "";
		if (dato != null) {
			// Elimina espacios adicionales al principio y al final.
			dato = dato.trim();

			// Reemplaza espacios múltiples entre palabras por un solo espacio.
			resultado = dato.replaceAll("\\s+", " ");
		}
		return resultado;
	}

	/**
	 * Funcion que permite la redimension de una imagen. Guarda la imagen y es
	 * cargada por otras funciones.
	 *
	 * @param file
	 * @return
	 */
	public static void nueva_imagen(String imagen, String nuevoNombreArchivo) {
		try {
			File file = new File(imagen);
			InputStream input = null;

			if (!file.exists()) {
				input = Utilidades.class.getResourceAsStream("sinPortada.jpg");
				if (input == null) {
					throw new FileNotFoundException("La imagen predeterminada no se encontró en el paquete");
				}
				file = File.createTempFile("tmp", ".jpg");
				file.deleteOnExit();
				try (OutputStream output = new FileOutputStream(file)) {
					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = input.read(buffer)) != -1) {
						output.write(buffer, 0, bytesRead);
					}
				}
			}

			String userDir = System.getProperty("user.home");
			String documentsPath = userDir + File.separator + "Documents";
			String defaultImagePath = documentsPath + File.separator + "libreria_comics" + File.separator
					+ ConectManager.DB_NAME + File.separator + "portadas";

			// Esto se modificara para hacerlo dinamico
			String imagePath = defaultImagePath;

			File portadasFolder = new File(imagePath);

			if (!portadasFolder.exists()) {
				if (!portadasFolder.mkdirs()) {
					throw new IOException("No se pudo crear la carpeta 'portadas'");
				}
			}

			File newFile = new File(portadasFolder.getPath() + File.separator + nuevoNombreArchivo + ".jpg");
			Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			manejarExcepcion(e);
		}
	}

	/**
	 * Copia un directorio y cuenta los archivos que no existen durante la copia.
	 *
	 * @param sourceDirectoryPath      Ruta del directorio fuente.
	 * @param destinationDirectoryPath Ruta del directorio de destino.
	 */
	public static void copyDirectory(String sourceDirectoryPath, String destinationDirectoryPath) {
		Path sourceDirectory = Paths.get(sourceDirectoryPath);
		Path destinationDirectory = Paths.get(destinationDirectoryPath);

		final int[] contadorArchivosNoExistentes = { 0 }; // Array de un solo elemento para almacenar el contador

		try {
			if (!Files.exists(destinationDirectory)) {
				Files.createDirectories(destinationDirectory);
			}

			Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<>() {
				@Override
				public FileVisitResult visitFile(Path sourceFile, BasicFileAttributes attrs) throws IOException {
					if (sourceFile.toString().endsWith(".jpg")) {
						Path destinationPath = destinationDirectory.resolve(sourceDirectory.relativize(sourceFile));
						try {
							Files.copy(sourceFile, destinationPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (NoSuchFileException e) {
							contadorArchivosNoExistentes[0]++; // Incrementar el contador utilizando el array
						} catch (IOException e) {
							System.err.println("Se produjo un error al copiar el archivo: " + e.getMessage());
						}
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					System.err.println("No se pudo visitar el archivo: " + file + ". Motivo: " + exc.getMessage());
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			manejarExcepcion(e);
		}
	}

	/**
	 * Obtiene el nombre completo de un cómic con información específica.
	 *
	 * @param datos Los datos del cómic.
	 * @return El nombre completo del cómic con formato para archivo.
	 */
	public String obtenerNombreCompleto(Comic datos) {
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String defaultImagePath = documentsPath + File.separator + "libreria_comics" + File.separator
				+ ConectManager.DB_NAME + File.separator + "portadas" + File.separator;
		String nombre_comic = datos.getNombre().replace(" ", "_").replace(":", "_").replace("-", "_");
		String numero_comic = datos.getNumero();
		String variante_comic = datos.getVariante().replace(" ", "_").replace(",", "_").replace("-", "_").replace(":",
				"_");
		String fecha_comic = datos.getFecha();
		String nombre_completo = nombre_comic + "_" + numero_comic + "_" + variante_comic + "_" + fecha_comic;
		String extension = ".jpg";
		String nuevoNombreArchivo = defaultImagePath + nombre_completo + extension;
		return nuevoNombreArchivo;
	}

	/**
	 * Crea un nuevo nombre de archivo para un cómic con información específica.
	 *
	 * @param datos Los datos del cómic.
	 * @return El nuevo nombre de archivo del cómic con formato para archivo.
	 */
	public String crearNuevoNombre(Comic datos) {
		String nombre_comic = datos.getNombre().replace(" ", "_").replace(":", "_").replace("-", "_");
		String numero_comic = datos.getNumero();
		String variante_comic = datos.getVariante().replace(" ", "_").replace(",", "_").replace("-", "_").replace(":",
				"_");
		String fecha_comic = datos.getFecha();
		String nombre_completo = nombre_comic + "_" + numero_comic + "_" + variante_comic + "_" + fecha_comic;
		String extension = ".jpg";
		String nuevoNombreArchivo = nombre_completo + extension;
		return nuevoNombreArchivo;
	}

	/**
	 * Obtiene el nombre de archivo de una ruta completa.
	 *
	 * @param rutaCompleta La ruta completa del archivo.
	 * @return El nombre del archivo sin la ruta.
	 */
	public static String obtenerNombreArchivo(String rutaCompleta) {
		// Obtener el separador de ruta del archivo según el sistema operativo
		String separadorRuta = File.separator;

		// Obtener la última posición del separador de ruta del archivo en la ruta
		// completa
		int posicionSeparador = rutaCompleta.lastIndexOf(separadorRuta);

		// Extraer el nombre del archivo sin la ruta
		String nombreArchivo = rutaCompleta.substring(posicionSeparador + 1);

		return nombreArchivo;
	}

	public static void copiaSeguridad() throws IOException, SQLException {
		FuncionesExcel excel = new FuncionesExcel();

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String nombreCarpeta = dateFormat.format(new Date());

			String userDir = System.getProperty("user.home");
			String documentsPath = userDir + File.separator + "Documents";
			String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator
					+ ConectManager.DB_NAME + File.separator + "portadas";
			File sourceFolder = new File(sourcePath);

			String ubicacion = userDir + File.separator + "AppData" + File.separator + "Roaming";
			String carpetaLibreria = ubicacion + File.separator + "libreria" + File.separator + ConectManager.DB_NAME
					+ File.separator + "backups" + File.separator + nombreCarpeta;

			if (sourceFolder.exists()) {
				crearCarpetaBackups(carpetaLibreria);
				crearArchivoZip(sourceFolder, carpetaLibreria, dateFormat);

				// Guardar datos en Excel
				excel.savedataExcel(nombreCarpeta);
			}
		} catch (IOException e) {
			manejarExcepcion(e);
		}
	}

	private static void crearCarpetaBackups(String carpetaLibreria) throws IOException {
		File backupsFolder = new File(carpetaLibreria);
		if (!backupsFolder.exists() && !backupsFolder.mkdirs()) {
			throw new IOException("Error al crear la carpeta 'backups'.");
		}
	}

	private static void crearArchivoZip(File sourceFolder, String carpetaLibreria, SimpleDateFormat dateFormat)
			throws IOException {
		// Crear archivo ZIP con fecha actual
		String backupFileName = "portadas_" + dateFormat.format(new Date()) + ".zip";
		String backupPath = carpetaLibreria + File.separator + backupFileName;
		File backupFile = new File(backupPath);

		// Comprimir carpeta en el archivo ZIP
		try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(backupFile))) {
			zipFile(sourceFolder, sourceFolder.getName(), zipOut);
		}
	}

	/**
	 * Añade un archivo al archivo ZIP especificado con el nombre de entrada dado.
	 *
	 * @param file      El archivo que se va a agregar al ZIP.
	 * @param entryName El nombre de entrada del archivo en el ZIP.
	 * @param zipFile   El archivo ZIP al que se va a agregar el nuevo archivo.
	 * @throws IOException Si ocurre un error de lectura o escritura durante la
	 *                     operación.
	 */
	public static void addFileToZip(File file, String entryName, File zipFile) throws IOException {
		try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile, true))) {
			ZipEntry zipEntry = new ZipEntry(entryName);
			zipOut.putNextEntry(zipEntry);
			try (FileInputStream fileInputStream = new FileInputStream(file)) {
				byte[] buffer = new byte[1024];
				int length;
				while ((length = fileInputStream.read(buffer)) > 0) {
					zipOut.write(buffer, 0, length);
				}
			}
		}
	}

	/**
	 * Comprime un archivo o carpeta en un archivo zip.
	 *
	 * @param fileToZip El archivo o carpeta que se va a comprimir.
	 * @param fileName  El nombre del archivo.
	 * @param zipOut    El flujo de salida del archivo zip.
	 * @throws IOException Si ocurre un error de E/S durante la compresión.
	 */
	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}

		if (fileToZip.isDirectory()) {
			fileName = fileName.endsWith("/") ? fileName : fileName + "/";
			zipOut.putNextEntry(new ZipEntry(fileName));

			try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(fileToZip.toPath())) {
				for (Path childPath : dirStream) {
					zipFile(childPath.toFile(), fileName + childPath.getFileName(), zipOut);
				}
			}
			return;
		}

		try (FileInputStream fis = new FileInputStream(fileToZip)) {
			ZipEntry zipEntry = new ZipEntry(fileName);
			zipOut.putNextEntry(zipEntry);

			byte[] bytes = new byte[4096];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
		}
	}

	/**
	 * Elimina todos los archivos en una carpeta específica.
	 */
	public static void eliminarArchivosEnCarpeta() {

		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + ConectManager.DB_NAME
				+ File.separator + "portadas";

		File carpeta = new File(sourcePath);
		if (carpeta.exists() && carpeta.isDirectory()) {
			File[] archivos = carpeta.listFiles();
			if (archivos != null) {
				for (File archivo : archivos) {
					if (archivo.isFile()) {
						archivo.delete();
					}
				}
			}
		}
	}

	/**
	 * Crea una copia de seguridad de la base de datos.
	 */
	public void crearCopiaBaseDatos() {
		try {

			String userHome = System.getProperty("user.home");
			String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
			String carpeta_backups = ubicacion + File.separator + "libreria" + File.separator + "backups"
					+ File.separator + ConectManager.DB_NAME + File.separator;

			String pathMySql = "C:\\Program Files\\MySQL";
			String mysqlDump = pathMySql + "\\mysqldump.exe";

			String nombreCopia = "copia_base_datos.sql";
			File carpetaDestino = new File(carpeta_backups);
			File archivoCopia = new File(carpetaDestino, nombreCopia);

			String[] command = new String[] { mysqlDump, "-u" + ConectManager.DB_USER, "-p" + ConectManager.DB_PASS,
					"-B", ConectManager.DB_NAME, "--hex-blob", "--routines=true",
					"--result-file=" + archivoCopia.getAbsolutePath() };

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectError(Redirect.INHERIT);
			pb.redirectOutput(Redirect.to(archivoCopia));
			pb.start();

		} catch (Exception e) {
			manejarExcepcion(e);
		}
	}

	/**
	 * Elimina la imagen temporal de muestra de la base de datos.
	 */
	public void deleteImage() {
		try {
			Files.deleteIfExists(Paths.get("tmp.jpg"));
		} catch (IOException e) {
			manejarExcepcion(e);
		}
	}

	/*
	 * Convierte los nombres de los archivos en una carpeta, reemplazando los
	 * guiones por guiones bajos.
	 * 
	 * @param rutaCarpeta La ruta de la carpeta que contiene los archivos.
	 * 
	 * @throws IOException Si hay un error al leer el contenido de la carpeta.
	 */
	public static void convertirNombresCarpetas(String rutaCarpeta) throws IOException {

		File carpeta = new File(rutaCarpeta);

		if (!carpeta.isDirectory()) {
			throw new IllegalArgumentException("La ruta proporcionada no es una carpeta.");
		}

		File[] archivos = carpeta.listFiles();
		if (archivos == null) {
			throw new IOException("Error al leer el contenido de la carpeta.");
		}

		for (File archivo : archivos) {
			if (archivo.isFile()) {
				String nombreArchivoAntiguo = archivo.getName();
				String nombreArchivoNuevo = convertirNombreArchivo(nombreArchivoAntiguo);
				if (!nombreArchivoAntiguo.equals(nombreArchivoNuevo)) {
					File nuevoArchivo = new File(carpeta, nombreArchivoNuevo);
					archivo.renameTo(nuevoArchivo);

				}
			}
		}
	}

	/**
	 * Convierte el nombre de un archivo, reemplazando los guiones por guiones
	 * bajos.
	 * 
	 * @param nombreArchivo El nombre del archivo a convertir.
	 * 
	 * @return El nuevo nombre de archivo convertido.
	 */
	public static String convertirNombreArchivo(String nombreArchivo) {

		StringBuilder nombreConvertido = new StringBuilder();

		for (int i = 0; i < nombreArchivo.length(); i++) {
			char caracterActual = nombreArchivo.charAt(i);

			if (caracterActual == '-' && esPosicionGuionValida(nombreArchivo, i)) {
				nombreConvertido.append('_');
			} else {
				nombreConvertido.append(caracterActual);
			}
		}

		return nombreConvertido.toString();
	}

	/**
	 * Verifica si la posición de un guion en el nombre de archivo es válida para
	 * convertirlo.
	 * 
	 * @param nombreArchivo El nombre del archivo.
	 * 
	 * @param indice        El índice del guion en el nombre del archivo.
	 * 
	 * @return true si la posición del guion es válida, false en caso contrario.
	 */
	public static boolean esPosicionGuionValida(String nombreArchivo, int indice) {

		// Verificar si el guion está precedido por un dígito
		if (indice > 0 && Character.isDigit(nombreArchivo.charAt(indice - 1))) {
			return false;
		}
		// Verificar si el guion está seguido por un dígito
		if (indice < nombreArchivo.length() - 1 && Character.isDigit(nombreArchivo.charAt(indice + 1))) {
			return false;
		}

		return true;
	}

	public static String obtenerNombrePortada(boolean eliminarAntesPortadas, String rutaArchivo) {

		int indicePortada = 0;

		if (eliminarAntesPortadas) {
			indicePortada = rutaArchivo.lastIndexOf("portadas\\");
		} else {
			indicePortada = rutaArchivo.indexOf("portadas\\");
		}
		if (indicePortada != -1) {
			if (eliminarAntesPortadas) {
				return rutaArchivo.substring(0, indicePortada + 9);
			} else {
				return rutaArchivo.substring(indicePortada + 9);
			}
		}
		return "";
	}

	/**
	 * Elimina un fichero en la ubicación especificada.
	 *
	 * @param direccion La dirección del fichero a eliminar.
	 */
	public static void eliminarFichero(String direccion) {

		Platform.runLater(() -> {
			File archivo = new File(direccion);

			archivo.delete();
		});
	}

	/**
	 * Renombra un archivo en una carpeta específica.
	 *
	 * @param carpeta              La carpeta donde se encuentra el archivo.
	 * @param nombreArchivoBuscado El nombre del archivo que se desea renombrar.
	 * @param nuevoNombreArchivo   El nuevo nombre para el archivo.
	 */
	public static void renombrarArchivo(String carpeta, String nombreArchivoBuscado, String nuevoNombreArchivo) {
		File directorio = new File(carpeta);
		File[] archivos = directorio.listFiles();

		if (archivos != null) {
			for (File archivo : archivos) {
				if (archivo.isFile() && !archivo.getName().equals(nombreArchivoBuscado)) {
					File nuevoArchivo = new File(directorio, nuevoNombreArchivo);
					archivo.renameTo(nuevoArchivo);

				}
			}
		}
	}

	/**
	 * Genera un codigo unico para renombrar una imagen solamente si el codigo no
	 * existe, si no, vuelve a crear otro hasta que aparezca uno que sea adecuado
	 * 
	 * @param carpeta
	 * @return
	 */
	public static String generarCodigoUnico(String carpeta) {
		String codigo;
		File directorio = new File(carpeta);
		File archivo = null;
		do {
			codigo = generarCodigo(); // Genera un nuevo código único
			String nombreArchivo = codigo + ".jpg";
			archivo = new File(directorio, nombreArchivo);
		} while (archivo.exists());

		return codigo;
	}

	/**
	 * Genera un codigo de forma aleatoria
	 * 
	 * @return
	 */
	private static String generarCodigo() {
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder codigo = new StringBuilder();

		for (int i = 0; i < 10; i++) {
			int indice = (int) (Math.random() * caracteres.length());
			codigo.append(caracteres.charAt(indice));
		}

		return codigo.toString();
	}

	/**
	 * Obtiene la carpeta de configuración en el sistema de usuario.
	 *
	 * @return La ruta de la carpeta de configuración.
	 */
	public static String obtenerCarpetaConfiguracion() {
		String userDir = System.getProperty("user.home");
		String ubicacion = userDir + File.separator + "AppData" + File.separator + "Roaming";
		String direccion = ubicacion + File.separator + "libreria";

		File directorio = new File(direccion);
		if (!directorio.exists()) {
			directorio.mkdirs(); // Crear directorios si no existen
		}

		return direccion;
	}

	/**
	 * Carga tasas de cambio desde un archivo de configuración y las almacena en un
	 * mapa.
	 */
	public static void cargarTasasDeCambioDesdeArchivo() {
		String nombreArchivo = obtenerCarpetaConfiguracion() + File.separator + "tasas_de_cambio.conf";

		// Verificar si el archivo existe y, si no, crearlo con los valores
		// predeterminados
		File archivo = new File(nombreArchivo);
		if (!archivo.exists()) {
			crearArchivoConValoresPredeterminados(nombreArchivo);
		}

//        Thread imprimirDivisasThread = new Thread(() -> ApiCambioDivisas.imprimirDivisas(nombreArchivo));
//        imprimirDivisasThread.start();

		ApiCambioDivisas.imprimirDivisas(nombreArchivo);
	}

	/**
	 * Crea un archivo de configuración con valores predeterminados.
	 *
	 * @param nombreArchivo El nombre del archivo a crear.
	 */
	private static void crearArchivoConValoresPredeterminados(String nombreArchivo) {
		try (PrintWriter writer = new PrintWriter(nombreArchivo)) {
			// Agregar los valores predeterminados al archivo
			String[] valoresPredeterminados = { "Japón (Japan): 141.75", "Estados Unidos (United States): 1.0",
					"Francia (France): 0.9085", "Italia (Italy): 0.9085", "España (Spain): 0.9085",
					"Reino Unido (United Kingdom): 0.78", "Alemania (Germany): 0.9085", "Brasil (Brazil): 4.87",
					"Corea del Sur (South Korea): 1304.04", "México (Mexico): 17.08", "Canadá (Canada): 1.34",
					"China (China): 7.17", "Australia (Australia): 1.67", "Argentina (Argentina): 276.01",
					"India (India): 82.68", "Bélgica (Belgium): 0.9085", "Países Bajos (Netherlands): 0.9085",
					"Portugal (Portugal): 0.9085", "Suecia (Sweden): 10.59", "Suiza (Switzerland): 0.87",
					"Finlandia (Finland): 0.9085", "Noruega (Norway): 10.14", "Dinamarca (Denmark): 6.77" };

			for (String valor : valoresPredeterminados) {
				writer.println(valor);
			}
		} catch (IOException e) {
			manejarExcepcion(e);
		}
	}

	/**
	 * Convierte una cantidad de moneda local a dólares.
	 *
	 * @param pais                El país del que se quiere convertir la moneda.
	 * @param cantidadMonedaLocal La cantidad de moneda local a convertir.
	 * @return La cantidad equivalente en dólares.
	 */
	public static double convertirMonedaADolar(String pais, String precio_comic) {
		if (tasasDeCambio.containsKey(pais)) {
			double tasaDeCambio = tasasDeCambio.get(pais);
			try {
				double cantidadMonedaLocal = Double.parseDouble(precio_comic);
				if (cantidadMonedaLocal > 0) {
					double resultado = cantidadMonedaLocal / tasaDeCambio;
					return Math.round(resultado * 100.0) / 100.0;
				}
			} catch (NumberFormatException e) {
				// Manejo de error si la conversión de la cadena a double falla
				manejarExcepcion(e);
			}
		}
		return 0.0; // Devolver 0 si el país no está en la lista, la cantidad no es un número o si
					// es negativa
	}

	/**
	 * Verifica si hay conexión a Internet disponible.
	 *
	 * @return true si hay conexión a Internet, false en caso contrario.
	 */
	public static boolean isInternetAvailable() {
		try {
			// Puedes cambiar la dirección a una página web confiable que esté disponible
			InetAddress.getByName("www.google.com").isReachable(1000); // 1000 ms = 1 segundo de tiempo de espera
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Guarda el nombre de usuario y contraseña en un archivo de configuración.
	 *
	 * @param usuario El campo de texto que contiene el nombre de usuario.
	 * @param pass    La contraseña del usuario.
	 * @throws IOException Si ocurre un error al escribir en el archivo.
	 */
	public static void guardarUsuario(TextField usuario, String pass) {

		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion_usuario.conf";

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(archivoConfiguracion);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write("###############################");
			bufferedWriter.newLine();
			bufferedWriter.write("Usuario y contraseño del usuario");
			bufferedWriter.newLine();
			bufferedWriter.write("###############################");
			bufferedWriter.newLine();
			bufferedWriter.write("Usuario: " + usuario.getText());
			bufferedWriter.newLine();
			bufferedWriter.write("Password: " + pass);
			bufferedWriter.newLine();

			bufferedWriter.close();
		} catch (IOException e) {
			manejarExcepcion(e);
		}

	}

	/**
	 * Verifica si una cadena es una URL válida.
	 *
	 * @param cadena Cadena que se va a verificar como URL.
	 * @return true si la cadena es una URL válida, false en caso contrario.
	 */
	public static boolean isImageURL(String cadena) {

		if (isURL(cadena)) {
			// Obtener la extensión del archivo desde la URL
			String extension = getFileExtensionFromURL(cadena);

			// Lista de extensiones de imagen comunes
			String[] imageExtensions = { "jpg", "jpeg", "png", "gif", "bmp" };

			// Verificar si la extensión está en la lista de extensiones de imagen
			for (String imageExtension : imageExtensions) {
				if (extension.equalsIgnoreCase(imageExtension)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Verifica si una cadena es una URL válida.
	 *
	 * @param cadena Cadena que se va a verificar como URL.
	 * @return true si la cadena es una URL válida, false en caso contrario.
	 */
	public static boolean isURL(String cadena) {
		try {
			new URI(cadena);
			return true;
		} catch (URISyntaxException e) {
			return false;
		}
	}

	/**
	 * Verifica si la cadena proporcionada representa una ruta de archivo local.
	 * 
	 * @param cadena La cadena que se va a verificar.
	 * @return true si la cadena es una ruta de archivo local absoluta, false de lo
	 *         contrario.
	 */
	public static boolean isRutaDeArchivo(String cadena) {
		try {
			Path path = Paths.get(cadena);
			return path.isAbsolute(); // Verifica si es una ruta de archivo absoluta
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Obtiene la extensión del archivo desde una URL.
	 *
	 * @param url URL de la que se va a obtener la extensión.
	 * @return La extensión del archivo o una cadena vacía si no se encuentra.
	 */
	public static String getFileExtensionFromURL(String url) {
		int lastDotIndex = url.lastIndexOf('.');
		if (lastDotIndex > 0) {
			return url.substring(lastDotIndex + 1).toLowerCase();
		}
		return "";
	}

	/**
	 * Descarga una imagen desde una URL y la guarda en una carpeta de destino.
	 * 
	 * @param urlImagen      URL de la imagen a descargar.
	 * @param carpetaDestino Ruta de la carpeta de destino.
	 * @return Ruta de destino de la imagen descargada o null si hay un error.
	 * @throws IOException Si ocurre un error de entrada/salida.
	 */
	public static CompletableFuture<String> descargarImagenAsync(String urlImagen, String carpetaDestino) {
		Executor executor = Executors.newCachedThreadPool();
		CompletableFuture<String> downloadTask = new CompletableFuture<>();

		@SuppressWarnings("unused")
		CompletableFuture<Void> asyncTask = CompletableFuture.runAsync(() -> {
			try {

				String nombreImagen = "";
				nombreImagen = obtenerNombreImagen(urlImagen);

				crearCarpetaSiNoExiste(carpetaDestino);
				String rutaDestino = carpetaDestino + File.separator + nombreImagen;

				downloadTask.complete(rutaDestino);
			} catch (IllegalArgumentException e) {
				System.err.println(e.getMessage());
				downloadTask.completeExceptionally(e);
			} catch (Exception e) {
				System.err.println("No se pudo acceder a la URL: " + urlImagen);
				e.printStackTrace();
				downloadTask.completeExceptionally(e);
			}
		}, executor);

		// Handle executor shutdown
		downloadTask.whenComplete((result, throwable) -> {
			((ExecutorService) executor).shutdown();
		});

		return downloadTask;
	}

	/**
	 * Valida una URL y la convierte en una URI.
	 * 
	 * @param urlImagen URL de la imagen a validar.
	 * @return URI válida de la URL.
	 * @throws IllegalArgumentException Si la URL no es válida.
	 */
	@SuppressWarnings("unused")
	private static URI validarURL(String urlImagen) throws IllegalArgumentException {
		try {
			return new URI(urlImagen);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("URL de imagen no válida: " + urlImagen, e);
		}
	}

	/**
	 * Obtiene el nombre de la imagen a partir de una URL.
	 * 
	 * @param urlImagen URL de la imagen.
	 * @return Nombre de la imagen.
	 */
	public static String obtenerNombreImagen(String urlImagen) {
		String[] partesURL = urlImagen.split("/");
		return partesURL[partesURL.length - 1];
	}

	/**
	 * Crea una carpeta de destino si no existe.
	 * 
	 * @param carpetaDestino Ruta de la carpeta de destino.
	 * @return Objeto File de la carpeta de destino.
	 */
	private static File crearCarpetaSiNoExiste(String carpetaDestino) {
		File carpeta = new File(carpetaDestino);
		if (!carpeta.exists()) {
			carpeta.mkdirs();
		}
		return carpeta;
	}

	/**
	 * Descarga una imagen desde una URL y la guarda en una carpeta de destino.
	 * 
	 * @param urlImagen      URI de la imagen a descargar.
	 * @param carpetaDestino Ruta de la carpeta de destino.
	 * @return true si la descarga y conversión son exitosas, false en caso
	 *         contrario.
	 */
	public static CompletableFuture<Boolean> descargarYConvertirImagenAsync(URI urlImagen, String carpetaDestino,
			String nuevoNombre) {

		System.out.println(nuevoNombre);

		return CompletableFuture.supplyAsync(() -> {
			try {
				URL url = urlImagen.toURL();
				URLConnection connection = url.openConnection();

				if (connection instanceof HttpURLConnection) {
					((HttpURLConnection) connection).setRequestMethod("HEAD");
					int responseCode = ((HttpURLConnection) connection).getResponseCode();

					if (responseCode != HttpURLConnection.HTTP_OK) {
						if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
							System.err.println("Error interno del servidor al acceder a la URL: " + url);
						} else {
							System.err.println("La URL no apunta a una imagen válida o no se pudo acceder: " + url);
						}
						return false;
					}
				}

				String finalNuevoNombre = nuevoNombre; // Create a new effectively final variable

				String extension = obtenerExtension(finalNuevoNombre);
				if (!extension.equals("jpg")) {
					BufferedImage image = ImageIO.read(url);

					if (image == null) {
						System.err.println("No se pudo cargar la imagen desde " + urlImagen);
						return false;
					}

					finalNuevoNombre += ".jpg";
					Path rutaDestino = Path.of(carpetaDestino, finalNuevoNombre);

					ImageIO.write(image, "jpg", rutaDestino.toFile());
				} else {
					Path rutaDestino = Path.of(carpetaDestino, finalNuevoNombre);
					try (InputStream in = url.openStream()) {
						java.nio.file.Files.copy(in, rutaDestino, StandardCopyOption.REPLACE_EXISTING);
					}
				}

				return true;
			} catch (IOException e) {
				manejarExcepcion(e);
				return false;
			}
		});
	}

	private static String obtenerExtension(String nombreArchivo) {
		int ultimoPunto = nombreArchivo.lastIndexOf(".");
		if (ultimoPunto == -1) {
			return "";
		}
		return nombreArchivo.substring(ultimoPunto + 1).toLowerCase();
	}

	/**
	 * Borra un archivo de imagen dada su ruta.
	 *
	 * @param rutaImagen Ruta del archivo de imagen que se va a borrar.
	 * @return true si se borra con éxito, false si no se puede borrar o el archivo
	 *         no existe.
	 */
	public static boolean borrarImagen(String rutaImagen) {

		File archivo = new File(rutaImagen);

		// Verificar si el archivo existe antes de intentar borrarlo
		if (archivo.exists()) {
			if (archivo.delete()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Reemplaza entidades HTML en una cadena con sus caracteres correspondientes.
	 *
	 * @param input Cadena que puede contener entidades HTML como &amp;, &lt;, &gt;,
	 *              etc.
	 * @return La cadena con las entidades HTML reemplazadas por los caracteres
	 *         correspondientes.
	 */
	public static String replaceHtmlEntities(String input) {
		return input.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"")
				.replaceAll("&#39;", "'").replaceAll("&rsquo;", "'").replaceAll("&ldquo;", "\"")
				.replaceAll("&rdquo;", "\"").replaceAll("&nbsp;", " ").replaceAll("&mdash;", "—")
				.replaceAll("&ndash;", "–").replaceAll("<ul>", "'").replaceAll("<li>", "'").replaceAll("</ul>", "'")
				.replaceAll("</li>", "'");
	}

	/**
	 * Método que crea la carpeta "portadas" para almacenar las imágenes de portada
	 * de los cómics.
	 * 
	 * @throws IOException Si ocurre un error al crear la carpeta.
	 */
	public static void crearCarpeta() throws IOException {
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String defaultImagePath = documentsPath + File.separator + "libreria_comics" + File.separator
				+ obtenerDatoDespuesDeDosPuntos("Database") + File.separator + "portadas";
		File portadasFolder = new File(defaultImagePath);

		if (!portadasFolder.exists()) {
			if (!portadasFolder.mkdirs()) {
				throw new IOException("No se pudo crear la carpeta 'portadas'");
			}
		}
	}

	/**
	 * Obtiene el dato que sigue a dos puntos (:) en una línea específica del
	 * archivo de configuración.
	 *
	 * @param linea la línea específica para buscar el dato
	 * @return el dato encontrado o una cadena vacía si no se encuentra
	 */
	public static String obtenerDatoDespuesDeDosPuntos(String linea) {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + "\\AppData\\Roaming";
		String carpetaLibreria = ubicacion + "\\libreria";
		String archivoConfiguracion;

		archivoConfiguracion = carpetaLibreria + "\\configuracion_local.conf";

		try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfiguracion))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(linea + ": ")) {
					return line.substring(linea.length() + 2).trim();
				}
			}
		} catch (IOException e) {
			manejarExcepcion(e);
		}
		return "";
	}

	/**
	 * Funcion que abre una ventana que aceptara los formatos de archivos que le
	 * demos como parametro.
	 *
	 * @param frase   Descripción del filtro de archivo (p. ej., "Archivos CSV")
	 * @param formato Extensiones de archivo permitidas (p. ej., "*.csv")
	 * @return FileChooser si el usuario selecciona un fichero; null si el usuario
	 *         cancela la selección o cierra la ventana
	 */
	public static FileChooser tratarFichero(String frase, String formato) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter(frase, formato));

		return fileChooser; // Devuelve el FileChooser para que la interfaz gráfica lo utilice
	}

	/**
	 * Imprime un mensaje de error en un archivo, junto con la fecha y hora actual.
	 * Abre el archivo después de escribir en él.
	 * 
	 * @param mensajeError     Mensaje de error a imprimir.
	 * @param ubicacionArchivo Ruta del directorio donde se guardará el archivo.
	 */
	public static void imprimirEnArchivo(String mensajeError, String ubicacionArchivo) {

		// Obtener la fecha y hora actuales
		LocalDateTime now = LocalDateTime.now();

		// Formatear la fecha y hora
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm");

		// Mostrar la información
		String fechaFormateada = now.format(formatter);

		String direccionCompleta = ubicacionArchivo + File.separator + "CodigosFaltantes_" + fechaFormateada + ".txt";

		try {
			PrintWriter escritor = new PrintWriter(new FileWriter(direccionCompleta, true));
			escritor.println(mensajeError);
			escritor.close();
			// Abrir el archivo
			abrirArchivo(direccionCompleta);
		} catch (IOException e) {
			manejarExcepcion(e);
		}
	}

	/**
	 * Abre un archivo en el sistema por su ubicación.
	 * 
	 * @param ubicacionArchivo Ruta del archivo que se desea abrir.
	 */
	public static void abrirArchivo(String ubicacionArchivo) {
		try {
			File archivo = new File(ubicacionArchivo);

			if (!Desktop.isDesktopSupported()) {
				return;
			}
			Desktop desktop = Desktop.getDesktop();

			if (archivo.exists()) {
				desktop.open(archivo);
			}
		} catch (IOException e) {
			manejarExcepcion(e);
		}
	}

	/**
	 * Elimina archivos en un directorio común que no están presentes en la lista
	 * proporcionada de URLs.
	 * 
	 * @param listaUrls Lista de URLs que representan los archivos a conservar.
	 */
	public static void borrarArchivosNoEnLista(List<String> listaUrls) {
		String directorioComun = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
				+ ConectManager.DB_NAME + File.separator + "portadas" + File.separator;

		List<String> nombresArchivosEnDirectorio = obtenerNombresArchivosEnDirectorio(directorioComun);

		for (String nombreArchivo : nombresArchivosEnDirectorio) {
			Path archivoAEliminarPath = Paths.get(nombreArchivo).normalize();

			if (!listaUrls.stream().anyMatch(url -> url.equalsIgnoreCase(archivoAEliminarPath.toString()))) {
				try {
					if (archivoAEliminarPath.toFile().exists() && archivoAEliminarPath.toFile().isFile()) {
						archivoAEliminarPath.toFile().delete();
					}
				} catch (SecurityException e) {
					manejarExcepcion(e);
				}
			}
		}
	}

	/**
	 * Obtiene la lista de nombres de archivos en un directorio especificado.
	 * 
	 * @param directorio La ruta del directorio.
	 * @return Lista de nombres de archivos en el directorio.
	 */
	private static List<String> obtenerNombresArchivosEnDirectorio(String directorio) {
		List<String> nombresArchivos = new ArrayList<>();

		File directorioComun = new File(directorio);
		File[] archivosEnDirectorio = directorioComun.listFiles();

		if (archivosEnDirectorio != null) {
			for (File archivo : archivosEnDirectorio) {
				nombresArchivos.add(directorio + archivo.getName());
			}
		}

		return nombresArchivos;
	}

	/**
	 * Obtiene la lista de nombres de archivos con extensiones de una lista de URLs.
	 * 
	 * @param listaUrls Lista de URLs que representan archivos.
	 * @return Lista de nombres de archivos con extensiones.
	 */
	public static List<String> obtenerNombresYExtensiones(List<String> listaUrls) {
		List<String> nombresYExtensiones = new ArrayList<>();

		for (String url : listaUrls) {
			File archivo = new File(url);
			String nombreArchivo = archivo.getName();
			nombresYExtensiones.add(nombreArchivo);
		}

		return nombresYExtensiones;
	}

	/**
	 * Parsea la cadena de fecha y devuelve la fecha correspondiente. Si la cadena
	 * es nula o vacía, devuelve la fecha actual.
	 *
	 * @param fechaVenta Cadena de fecha a ser parseada.
	 * @return Objeto LocalDate que representa la fecha parseada.
	 */
	public static LocalDate parseFecha(String fechaVenta) {
		if (fechaVenta == null || fechaVenta.isEmpty()) {
			return LocalDate.of(2000, 1, 1); // Obtener la fecha actual si la cadena de fecha no está presente
		} else {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				return LocalDate.parse(fechaVenta, formatter);
			} catch (DateTimeParseException e) {
				manejarExcepcion(e);
				return LocalDate.of(2000, 1, 1); // Valor predeterminado en caso de error de formato
			}
		}
	}

	/**
	 * Obtiene la dirección de la portada de un cómic.
	 *
	 * @param direccionPortada La dirección de la portada del cómic.
	 * @return La dirección de la portada actualizada después de procesar la lógica.
	 */
	public static String obtenerPortada(String direccionPortada) {

		if (direccionPortada != null && !direccionPortada.isEmpty()) {
			File file = new File(direccionPortada);

			if (Utilidades.isImageURL(direccionPortada)) {
				// Es una URL en internet
				CompletableFuture<String> futurePortada = descargarImagenAsync(direccionPortada, DOCUMENTS_PATH);
				// Esperar a que el CompletableFuture se complete y obtener el resultado
				return futurePortada.join();
			} else if (file.exists()) {
				// Si no existe el archivo, asignar la portada por defecto
				return file.toURI().toString();
			}
		}

		return obtenerRutaSinPortada();
	}

	private static String obtenerRutaSinPortada() {
		return new File("Funcionamiento/sinPortada.jpg").toURI().toString();
	}

	/**
	 * Agrega una etiqueta y un valor al constructor StringBuilder si el valor no
	 * está vacío o nulo.
	 *
	 * @param builder El constructor StringBuilder al que se va a agregar la
	 *                etiqueta y el valor.
	 * @param label   La etiqueta que se va a agregar.
	 * @param value   El valor que se va a agregar.
	 */
	public static void appendIfNotEmpty(StringBuilder builder, String label, String value) {
		if (value != null && !value.isEmpty()) {
			builder.append(label).append(": ").append(value).append("\n");
		}
	}

	/**
	 * Convierte una ruta de archivo a una URL válida.
	 * 
	 * @param rutaArchivo La ruta del archivo a convertir.
	 * @return La URL generada a partir de la ruta del archivo.
	 */
	public static String convertirRutaAURL(String rutaArchivo) {
		String rutaConBarrasInclinadas = "";

		if (rutaArchivo == null || rutaArchivo.isEmpty()) {
			return null;
		} else {
			rutaConBarrasInclinadas = "file:///" + rutaArchivo.replace("\\", "/");
		}
		return rutaConBarrasInclinadas;
	}

	/**
	 * Realiza la comprobación previa para determinar si la lista de cómics está
	 * vacía. En caso afirmativo, se inicializa la librería.
	 */
	public static void comprobacionListaComics() {

		if (ListaComicsDAO.listaComics.isEmpty()) {
			return;
		}

		String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

		SelectManager.verLibreria(sentenciaSQL);

	}

	/**
	 * Obtiene el ID del cómic seleccionado desde la tabla.
	 * 
	 * @return El ID del cómic seleccionado o null si no hay selección.
	 */
	public static String obtenerIdComicSeleccionado(TableView<Comic> tablaBBDD) {
		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();
		return (idRow != null) ? idRow.getID() : null;
	}

	/**
	 * Obtiene el objeto Comic seleccionado según el ID proporcionado.
	 * 
	 * @param id_comic El ID del cómic a obtener.
	 * @return El objeto Comic correspondiente al ID proporcionado.
	 * @throws SQLException Si hay un error al acceder a la base de datos.
	 */
	public static Comic obtenerComicSeleccionado(String id_comic) throws SQLException {
		Comic comic_temp;
		if (!ListaComicsDAO.comicsImportados.isEmpty()) {
			comic_temp = ListaComicsDAO.buscarComicPorID(ListaComicsDAO.comicsImportados, id_comic);
		} else {
			comic_temp = SelectManager.comicDatos(id_comic);
		}

		return comic_temp;
	}

	/**
	 * Verifica si el servicio MySQL está en ejecución en el host y puerto
	 * proporcionados.
	 *
	 * @param host       El host del servicio MySQL.
	 * @param portString El número de puerto del servicio MySQL.
	 * @return true si el servicio está en ejecución, false si no lo está.
	 */
	public static boolean isMySQLServiceRunning(String host, String portString) {
		try {
			int port = Integer.parseInt(portString); // Convertir la cadena a un entero
			InetAddress address = InetAddress.getByName(host);
			Socket socket = new Socket(address, port);
			socket.close();
			return true;
		} catch (Exception e) {
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Crea las carpetas necesarias para realizar backups.
	 */
	public static void crearCarpetasBackup() {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String carpetaBackup = carpetaLibreria + File.separator + Utilidades.obtenerDatoDespuesDeDosPuntos("Database")
				+ File.separator + "backups";

		try {
			File carpeta_backupsFile = new File(carpetaBackup);
			Utilidades.crearCarpeta();
			if (!carpeta_backupsFile.exists()) {
				carpeta_backupsFile.mkdirs();
			}
		} catch (Exception e) {
			manejarExcepcion(e);
		}
	}

	/**
	 * Verifica si el código es válido, es decir, contiene solo números y letras y
	 * tiene una longitud mayor a 0.
	 *
	 * @param codigo El código a validar.
	 * @return true si el código es válido, false de lo contrario.
	 */
	public static boolean esCodigoValido(String codigo) {
		return codigo.length() >= 8 && codigo.matches("^[a-zA-Z0-9]+$");
	}

	public static void saveImageFromDataBase(String direccionImagen, File directorio) {

		try (Connection conn = ConectManager.conexion()) {
			if (directorio != null) {
				String nombreImagen = obtenerNombreArchivo(direccionImagen);
				File imagenArchivo = new File(direccionImagen);

				if (!imagenArchivo.exists()) {
					copiarImagenPredeterminada(directorio, nombreImagen);
				} else {
					copiarImagenDesdeArchivo(imagenArchivo, directorio, nombreImagen);
				}
			}
		} catch (IOException | SQLException e) {
			manejarExcepcion(e);
		}
	}

	// OJO CON ESTA FUNCION, EL ERROR FUE PONER STATIC EN VEZ DE getClass() DONDE
	// UTILIDADES.CLASS

	private static void copiarImagenPredeterminada(File directorio, String nombreImagen) throws IOException {
		try (InputStream input = Utilidades.class.getResourceAsStream("sinPortada.jpg")) {
			if (input == null) {
				throw new FileNotFoundException("La imagen predeterminada no se encontró en el paquete");
			}

			File imagenArchivo = File.createTempFile("tmp", ".jpg");
			imagenArchivo.deleteOnExit();

			Path tempPath = imagenArchivo.toPath();
			Files.copy(input, tempPath, StandardCopyOption.REPLACE_EXISTING);

			Files.copy(tempPath, Paths.get(directorio.getAbsolutePath(), nombreImagen),
					StandardCopyOption.REPLACE_EXISTING);
		}
	}

	private static void copiarImagenDesdeArchivo(File imagenArchivo, File directorio, String nombreImagen)
			throws IOException {
		try (InputStream fileInputStream = new FileInputStream(imagenArchivo);
				OutputStream fileOutputStream = new FileOutputStream(
						Paths.get(directorio.getAbsolutePath(), nombreImagen).toFile())) {

			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, bytesRead);
			}
		}
	}

	public static void manejarExcepcion(Exception e) {

		Ventanas nav = new Ventanas();

		nav.alertaException(e.toString());
		e.printStackTrace();
	}

	public static void procesarArchivo(File archivo) {
		ExecutorService executor = Executors.newSingleThreadExecutor();

		executor.submit(() -> {
			String linea;
			int contadorFaltas = 0;
			ApiISBNGeneral isbnGeneral = new ApiISBNGeneral();
			String codigosFaltantes = "";
			Comic comicInfo = new Comic();
			String sourcePath = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
					+ ConectManager.DB_NAME;

			try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
				while ((linea = br.readLine()) != null) {
					// Procesar cada línea según tus requisitos
					String finalValorCodigo = linea.trim();
					String valorCodigo = finalValorCodigo; // Asignar el valor adecuado

					if (!finalValorCodigo.isEmpty()) {
						if (finalValorCodigo.length() == 9) {
							comicInfo = WebScraperPreviewsWorld.displayComicInfo(finalValorCodigo.trim(), null);

						} else {
							comicInfo = ApiMarvel.infoComicCode(finalValorCodigo.trim(), null);
							contadorFaltas++;

							if (comicInfo == null) {
								comicInfo = isbnGeneral.getBookInfo(finalValorCodigo.trim(), null);

								if (comicInfo == null) {
									contadorFaltas++;
								}

							}
						}

						if (contadorFaltas > 1) {
							codigosFaltantes += "Falta comic con codigo: " + valorCodigo;
						}
					}
				}
			} catch (IOException | URISyntaxException | JSONException e) {
				manejarExcepcion(e);
			}

			// Verificar el contador de faltas al final y actualizar el resultado
			if (contadorFaltas > 1) {
				Utilidades.imprimirEnArchivo(codigosFaltantes, sourcePath);
			}
		});

		executor.shutdown();
	}

	public static boolean codigoCorrectoImportado(String valorCodigo) {
		try {
			String finalValorCodigo = eliminarEspacios(valorCodigo).replace("-", "");
			ApiISBNGeneral isbnGeneral = new ApiISBNGeneral();
			Comic comicInfo = null;

			if (finalValorCodigo.length() == 9) {
				comicInfo = WebScraperPreviewsWorld.displayComicInfo(finalValorCodigo.trim(), null);
			} else {
				comicInfo = ApiMarvel.infoComicCode(finalValorCodigo.trim(), null);

				if (comicInfo == null) {
					comicInfo = isbnGeneral.getBookInfo(finalValorCodigo.trim(), null);
				}
			}

			// Realiza cualquier lógica adicional aquí si es necesario.

			return comicInfo != null; // Devuelve true si se encontró información del cómic.
		} catch (Exception e) {
			manejarExcepcion(e);
			return false;
		}
	}

	public static int contarLineas(File fichero) {
		int contador = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
			// Lee el archivo línea por línea
			while (br.readLine() != null) {
				contador++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return contador;
	}

	public static Image pasarImagenComic(String direccionComic) {
		if (direccionComic != null && !direccionComic.isEmpty()) {
			try {
				Image imagen = new Image(new File(direccionComic).toURI().toString());
				return new Image(imagen.getUrl(), 250, 0, true, true);
			} catch (Exception e) {
				Utilidades.manejarExcepcion(e);
			}
		}
		return null;
	}

	public static boolean comprobarYManejarConexion(Scene ventana) {
		Ventanas nav = new Ventanas();

		if (ventana != null) {
			Stage stage = (Stage) ventana.getWindow();

			if (!ConectManager.conexionActiva()) {
				Platform.runLater(() -> {
					ConectManager.asignarValoresPorDefecto();
					ConectManager.close();

					Ventanas.cerrarVentanaActual(stage);
					nav.alertaException("Error. Servicio MySql apagado o desconectado de forma repentina.");
				});

				return false; // No está conectado
			}
		}

		return true; // Está conectado
	}

	/**
	 * Devuelve el último segmento de una ruta dada.
	 *
	 * @param ruta La ruta de la cual se desea obtener el último segmento.
	 * @return El último segmento de la ruta. Si la ruta es nula o vacía, se
	 *         devuelve una cadena vacía.
	 */
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

	/**
	 * Función que verifica si un archivo de portada existe
	 *
	 * @param defaultImagePath El directorio de las portadas
	 * @param nombreModificado El nombre del archivo modificado
	 * @return true si el archivo existe, false en caso contrario
	 */
	static boolean existeArchivo(String defaultImagePath, String nombreModificado) {
		return Files.exists(Paths.get(defaultImagePath, nombreModificado));
	}

	public static boolean isXAMPPRunning() {
		try {
			// Verificar si el proceso principal del "XAMPP Control Panel" está en ejecución
			ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe", "/FI", "IMAGENAME eq xampp-control.exe");
			Process process = processBuilder.start();

			// Leer la salida del comando para verificar si el proceso está en la lista
			String output = leerSalidaProceso(process);

			// Cerrar el proceso
			process.destroy();

			// Si la salida contiene "xampp-control.exe", entonces el proceso está en
			// ejecución
			return output.contains("xampp-control.exe");
		} catch (IOException e) {
			manejarExcepcion(e);
			return false;
		}
	}

	private static String leerSalidaProceso(Process process) throws IOException {
		StringBuilder salida = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				salida.append(line).append("\n");
			}
		}
		return salida.toString();
	}

	/**
	 * Devuelve el valor predeterminado si la cadena dada es nula o vacía, de lo
	 * contrario, devuelve la cadena original.
	 *
	 * @param value        Cadena a ser verificada.
	 * @param defaultValue Valor predeterminado a ser devuelto si la cadena es nula
	 *                     o vacía.
	 * @return Cadena original o valor predeterminado.
	 */
	public static String defaultIfNullOrEmpty(String value, String defaultValue) {

		Comic.limpiarCampo(value);

		return (value == null || value.isEmpty()) ? defaultValue : value;
	}

	public static boolean iniciarXAMPP() {
		try {
			String[] datosFichero = FuncionesFicheros.datosEnvioFichero();
			String port = datosFichero[0];
			String host = datosFichero[4];
			String xamppRuta = datosFichero[5];
			FuncionesFicheros.verificarYReemplazarRutaXampp(xamppRuta);

			if (!isMySQLServiceRunning(host, port)) {
				iniciarConexionMySql(xamppRuta);
				return true;
			}
		} catch (Exception e) {
			manejarExcepcion(e);
		}
		return false;
	}

	public static String buscarProgramasEnDirectorio(String nombreDirectorio, String nomAplicacion) {

		File direccionAplicacion = new File(nombreDirectorio);

		if (direccionAplicacion.exists()) {

			System.out.println(nombreDirectorio + File.separator + nomAplicacion);

			return nombreDirectorio + File.separator + nomAplicacion;

		}
		return "";
	}

	public static void iniciarConexionMySql(String directorio) {
		String rutaScriptControl = buscarProgramasEnDirectorio(directorio, "xampp-control.exe");
		String rutaXamppStart = buscarProgramasEnDirectorio(directorio, "xampp_start.exe");

		try {
			if (rutaScriptControl != null && verificarExistencia(rutaScriptControl)) {
				if (!isXAMPPRunning()) {
					abrirPrograma(rutaScriptControl);
				}
			}

			if (rutaXamppStart != null) {
				ejecutarComando(rutaXamppStart);
			}
		} catch (Exception e) {
			manejarExcepcion(e);
		}
	}

	/**
	 * Ejecuta un comando en un proceso y maneja la salida según el resultado.
	 *
	 * @param rutaScript   Ruta del script o comando a ejecutar.
	 * @param mensajeExito Mensaje a imprimir en caso de éxito. (Opcional)
	 * @param mensajeError Mensaje a imprimir en caso de error. (Opcional)
	 * @throws IOException Si ocurre un error de E/S al ejecutar el comando.
	 */
	private static void ejecutarComando(String rutaScript) throws IOException {
		try {

			String mensajeExito = "XAMPP iniciado correctamente";
			String mensajeError = "Error al iniciar XAMPP";

			ProcessBuilder processBuilder = new ProcessBuilder(rutaScript);
			Process process = processBuilder.start();

			try {
				int exitCode = process.waitFor();

				if (exitCode == 0) {
					if (mensajeExito != null && !mensajeExito.isEmpty()) {
						System.out.println(mensajeExito);
					} else {
						System.out.println("El comando se ejecutó correctamente.");
					}
				} else {
					if (mensajeError != null && !mensajeError.isEmpty()) {
						System.out.println(mensajeError);
					} else {
						System.out.println("El comando falló con código de salida: " + exitCode);
					}
				}
			} catch (InterruptedException e) {
				System.err.println("El hilo fue interrumpido mientras esperaba el proceso.");
				Thread.currentThread().interrupt();
			} finally {
				process.destroyForcibly();
				System.out.println("El proceso fue destruido: " + !process.isAlive());
			}
		} catch (IOException e) {
			throw new IOException("Error al ejecutar el comando: " + e.getMessage());
		}
	}

	private static void abrirPrograma(String rutaPrograma) {
		File file = new File(rutaPrograma);
		if (!file.exists()) {
			System.err.println("El archivo no existe en la ruta especificada.");
			return;
		}

		try {
			Desktop.getDesktop().open(file);
			System.out.println("Programa abierto correctamente.");
		} catch (IOException e) {
			System.err.println("Error al abrir el programa: " + e.getMessage());
		}
	}

//	// Función para detener XAMPP
//	private static void detenerXAMPP() {
//		try {
//			// Ruta al script de control de XAMPP en Windows
//			String rutaScript = "C:\\xampp\\xampp_stop.exe";
//
//			if (verificarExistencia(rutaScript)) {
//				ejecutarComando(rutaScript, "XAMPP detenido correctamente", "Error al detener XAMPP");
//			}
//
//			// Llamada a la función para ejecutar el comando
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	// Función para verificar la existencia de un archivo
	private static boolean verificarExistencia(String rutaArchivo) {
		File archivo = new File(rutaArchivo);
		return archivo.exists();
	}

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

	/**
	 * Obtiene la fecha y hora actual en el formato "yyyy_MM_dd_HH_mm".
	 *
	 * @return La fecha y hora actual formateada como una cadena de texto.
	 */
	public static String obtenerFechaActual() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
		Date date = new Date();
		return formatter.format(date);
	}

	/**
	 * Carga una imagen de forma asíncrona desde una URL y la muestra en un
	 * ImageView.
	 *
	 * @param urlImagen La URL de la imagen a cargar.
	 * @param imageView El ImageView en el que se mostrará la imagen cargada.
	 */
	public static void cargarImagenAsync(String urlImagen, ImageView imageView) {
		Task<Image> cargarImagenTask = new Task<Image>() {
			@Override
			protected Image call() throws Exception {
				while (true) {
					try {
						File fichero = new File(urlImagen);

						// Verificar si el archivo existe
						if (!fichero.exists()) {
							// Si el archivo no existe, cargar la imagen por defecto desde el proyecto
							InputStream is = getClass().getResourceAsStream("/Funcionamiento/sinPortada.jpg");
							if (is != null) {
								// Intentar cargar la imagen desde el flujo de entrada
								Image imagenCargada = new Image(is, 250, 0, true, true);
								// Verificar si la imagen se ha cargado correctamente
								if (imagenCargada.isError()) {
									throw new IOException("Error al cargar la imagen por defecto.");
								}
								// Actualizar la interfaz de usuario en el hilo de JavaFX
								Platform.runLater(() -> {
									imageView.setImage(imagenCargada);
								});
								return imagenCargada;
							}
						}

						// Si el archivo existe, cargar la imagen desde la ruta especificada
						String imageUrl = fichero.toURI().toURL().toString();
						Image imagenCargada = new Image(imageUrl, 250, 0, true, true);

						// Verificar si la imagen se ha cargado correctamente
						if (imagenCargada.isError()) {
							throw new IOException("Error al cargar la imagen desde la URL local.");
						}

						// Actualizar la interfaz de usuario en el hilo de JavaFX
						Platform.runLater(() -> {
							imageView.setImage(imagenCargada);
						});
						return imagenCargada;
					} catch (FileNotFoundException e) {
						Thread.sleep(1500); // Puedes ajustar el tiempo de espera según sea necesario
					} catch (Exception e) {
						Thread.sleep(1500); // Puedes ajustar el tiempo de espera según sea necesario
					}
				}
			}
		};

		Thread thread = new Thread(cargarImagenTask);
		thread.setDaemon(true);
		thread.start();
	}

	public static boolean existePortada(String direccionImagen) {

		File direccionFichero = new File(direccionImagen);

		return direccionFichero.exists();

	}

	public static String copiarConNombreAleatorio(String nombreCompletoPortadaOriginal) {
		File archivoOriginal = new File(nombreCompletoPortadaOriginal);
		String nombreCarpeta = archivoOriginal.getParent();
		String extension = ".jpg"; // La extensión siempre será .jpg

		String nombreAleatorio = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);
		String rutaDestino = nombreCarpeta + File.separator + nombreAleatorio + extension;
		Path rutaDestinoPath = Path.of(rutaDestino);

		try {

			if (archivoOriginal.exists()) {
				Files.copy(Path.of(nombreCompletoPortadaOriginal), rutaDestinoPath,
						StandardCopyOption.REPLACE_EXISTING);

				archivoOriginal.delete();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return nombreAleatorio + extension;
	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 *
	 * @param elementos Lista de elementos que deseas ocultar y deshabilitar.
	 */
	public static void cambiarVisibilidad(List<Node> elementos, boolean verElemento) {
		// Itera a través de los elementos y oculta/deshabilita cada uno
		for (Node elemento : elementos) {

			if (verElemento) {
				elemento.setVisible(false);
				elemento.setDisable(true);
			} else {
				elemento.setVisible(true);
				elemento.setDisable(false);
			}

		}
	}

	public static void descargarYAbrirEjecutableDesdeGitHub(Stage primaryStage) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				String urlDescarga = "https://github.com/AlejandroRodriguezM/Libreria-Comics/releases/latest/download/Libreria.exe";
				URI uri = new URI(urlDescarga);

				HttpURLConnection httpConn = (HttpURLConnection) uri.toURL().openConnection();
				httpConn.setRequestMethod("GET");

				int responseCode = httpConn.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					// Ejecutar el código del FileChooser en el hilo principal de JavaFX
					Platform.runLater(() -> {
						// Configurar el FileChooser de JavaFX
						try {
							Desktop.getDesktop().browse(uri);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
				}
				httpConn.disconnect();
				return null;
			}
		};

		new Thread(task).start();
	}

}
