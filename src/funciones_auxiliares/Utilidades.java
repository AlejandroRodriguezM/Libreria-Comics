/**
 * Contiene las clases que hacen funcionar las diferentes funciones de uso de back end y front de todo el proyecto
 *  
*/
package funciones_auxiliares;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
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
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import Apis.ApiISBNGeneral;
import Apis.ApiMarvel;
import Controladores.OpcionesAvanzadasController;
import comicManagement.Comic;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import ficherosFunciones.FuncionesFicheros;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesManagment.AccionReferencias;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import webScrap.WebScraperPreviewsWorld;

/**
 * Esta clase sirve para realizar diferentes funciones realizanas con la
 * comprobacion del sistema operativo que ejecuta este programa, al igual que
 * comprobar cual es tu navegador principal de internet
 *
 * @author Alejandro Rodriguez
 */
public class Utilidades {

	private static boolean fileChooserOpen = false;

	/**
	 * Sistema operativo actual.
	 */
	public static final String os = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);

	/**
	 * Mapa que almacena tasas de cambio.
	 */
	private static final Map<String, Double> tasasDeCambio = new HashMap<>();

	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private static final String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	public static final String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final Random RANDOM = new Random();

	private static AccionReferencias referenciaVentana = getReferenciaVentana();
	private static AccionReferencias referenciaVentanaPrincipal = getReferenciaVentanaPrincipal();

	public static final String DB_FOLDER = System.getProperty("user.home") + File.separator + "Documents"
			+ File.separator + "libreria_comics/";

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
	public static StringBuilder navegador(String url) {
		String[] browsers = { "google-chrome", "firefox", "mozilla", "epiphany", "konqueror", "netscape", "opera",
				"links", "lynx" };

		StringBuilder cmd = new StringBuilder();
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
			Desktop.getDesktop().browse(java.net.URI.create(url));
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

		StringBuilder cmd;
		try {
			cmd = navegador(url);
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
	 * Redimensiona una imagen y la guarda en una ubicación específica para su uso
	 * posterior.
	 *
	 * @param imagen             La ruta de la imagen a redimensionar y guardar.
	 * @param nuevoNombreArchivo El nombre del archivo para la imagen
	 *                           redimensionada.
	 * @throws IOException
	 */
	public static void redimensionarYGuardarImagen(String imagen, String nuevoNombreArchivo) throws IOException {
		File file = new File(imagen);
		InputStream input = null;

		if (!file.exists()) {
			input = Utilidades.class.getResourceAsStream("sinPortada.jpg");
			if (input == null) {
				throw new FileNotFoundException("La imagen predeterminada no se encontró en el paquete");
			}
			try {
				file = File.createTempFile("tmp", ".jpg");
			} catch (IOException e) {
				e.printStackTrace();
			}
			file.deleteOnExit();
			try (OutputStream output = new FileOutputStream(file)) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				try {
					while ((bytesRead = input.read(buffer)) != -1) {
						output.write(buffer, 0, bytesRead);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String defaultImagePath = documentsPath + File.separator + "libreria_comics" + File.separator
				+ Utilidades.nombreDB() + File.separator + "portadas";

		// Esto se modificara para hacerlo dinamico
		String imagePath = defaultImagePath;

		Path portadasFolderPath = Paths.get(imagePath);
		Files.createDirectories(portadasFolderPath);

		File newFile = new File(portadasFolderPath.resolve(nuevoNombreArchivo + ".jpg").toString());
		Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Copia un directorio y cuenta los archivos que no existen durante la copia.
	 *
	 * @param sourceDirectoryPath      Ruta del directorio fuente.
	 * @param destinationDirectoryPath Ruta del directorio de destino.
	 */
	public static void copyDirectory(String sourceDirectoryPath, String destinationDirectoryPath) {

		if (sourceDirectoryPath == null || sourceDirectoryPath.isEmpty()) {
			System.err.println("El directorio de origen no puede estar vacío o ser nulo.");
			return;
		}

		Path sourceDirectory = Paths.get(sourceDirectoryPath);

		if (!Files.exists(sourceDirectory) || !Files.isDirectory(sourceDirectory)) {
			System.err.println("El directorio de origen no es válido.");
			return;
		}

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
		return defaultImagePath + crearNuevoNombre(datos);
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
		return nombre_completo + extension;
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
		return rutaCompleta.substring(posicionSeparador + 1);
	}

	public static void copiaSeguridad(final SimpleDateFormat dateFormat) {
		CompletableFuture<Void> backupFuture = CompletableFuture.runAsync(() -> {
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
				crearCopiaSeg(carpetaLibreria, dateFormat);
			}
		});

		// Espera a que la copia de seguridad se complete antes de continuar con la
		// eliminación de archivos
		backupFuture.exceptionally(ex -> {
			// Manejar cualquier excepción que ocurra dentro del CompletableFuture
			System.err.println("Error en la copia de seguridad: " + ex);
			return null;
		}).join(); // Esperar a que se complete la copia de seguridad
	}

	private static void crearCopiaSeg(final String carpetaLibreria, final SimpleDateFormat dateFormat) {
		Thread thread = new Thread(() -> {
			File backupsFolder = new File(carpetaLibreria);
			if (!backupsFolder.exists()) {
				backupsFolder.mkdirs();
			}
			final String DB_NAME = ConectManager.DB_NAME;
			final String directorioComun = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
					+ DB_NAME + File.separator;
			final String directorioOriginal = directorioComun + "portadas" + File.separator;
			String backupFileName = "portadas_" + dateFormat.format(new Date());
			String backupPath = carpetaLibreria + File.separator + backupFileName;

			Utilidades.copiarDirectorio(backupPath, directorioOriginal);
		});
		thread.start();
	}

	public static void eliminarArchivosEnCarpeta() {
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + ConectManager.DB_NAME
				+ File.separator + "portadas";

		try {
			Path carpetaPath = Paths.get(sourcePath);
			if (Files.exists(carpetaPath) && Files.isDirectory(carpetaPath)) {
				Files.walk(carpetaPath).filter(Files::isRegularFile).forEach(file -> {
					try {
						Files.delete(file);
					} catch (IOException e) {
						System.err.println("Error al eliminar el archivo: " + file.toString());
						e.printStackTrace();
					}
				});
			}
		} catch (IOException e) {
			System.err.println("Error al acceder a la carpeta: " + sourcePath);
			e.printStackTrace();
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

		System.out.println(rutaCarpeta);

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
		return !(indice > 0 && Character.isDigit(nombreArchivo.charAt(indice - 1)))
				&& !(indice < nombreArchivo.length() - 1 && Character.isDigit(nombreArchivo.charAt(indice + 1)));
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

		// Manejo de excepciones si el directorio no existe o no se puede acceder
		if (!directorio.exists() || !directorio.isDirectory()) {
			System.err.println("El directorio no existe o no se puede acceder.");
			return;
		}

		File[] archivos = directorio.listFiles();

		// Usar try-with-resources para cerrar los recursos automáticamente
		if (archivos != null) {
			for (File archivo : archivos) {
				// Si el archivo es el que buscamos, lo renombramos y salimos del bucle
				if (archivo.isFile() && archivo.getName().equals(nombreArchivoBuscado)) {
					File nuevoArchivo = new File(directorio, nuevoNombreArchivo);
					archivo.renameTo(nuevoArchivo);
					break;
				}
			}
		} else {
			System.err.println("No se pueden listar los archivos en el directorio.");
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

		// Manejo de excepciones si el directorio no existe o no se puede acceder
		if (!directorio.exists() || !directorio.isDirectory()) {
			System.err.println("El directorio no existe o no se puede acceder.");
			return null;
		}

		// Genera un nuevo código único y verifica su existencia
		do {
			codigo = generarCodigo();
		} while (new File(directorio, codigo + ".jpg").exists());

		return codigo;
	}

	private static String generarCodigo() {
		StringBuilder codigo = new StringBuilder(10); // Predefinimos el tamaño del StringBuilder

		for (int i = 0; i < 10; i++) {
			int indice = RANDOM.nextInt(CARACTERES.length());
			codigo.append(CARACTERES.charAt(indice));
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

		File archivo = new File(nombreArchivo);
		if (!archivo.exists()) {
			crearArchivoConValoresPredeterminados(nombreArchivo);
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
			String linea;
			while ((linea = reader.readLine()) != null) {
				// Dividir la línea en nombre de país y tasa de cambio
				String[] partes = linea.split(": ");
				String nombrePais = partes[0];
				double tasaCambio = Double.parseDouble(partes[1]);
				// Guardar la tasa de cambio en el mapa
				tasasDeCambio.put(nombrePais, tasaCambio);
			}
		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
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
		String archivoConfiguracion = obtenerCarpetaConfiguracion() + File.separator + "configuracion_usuario.conf";

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
			String extension = obtenerExtension(cadena);

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

	public static String obtenerExtension(String entrada) {
		int ultimoPunto;
		if (entrada.contains("/")) {
			// Si la entrada contiene "/", se asume que es una URL
			int ultimoSlash = entrada.lastIndexOf("/");
			String nombreArchivo = entrada.substring(ultimoSlash + 1);
			ultimoPunto = nombreArchivo.lastIndexOf(".");
		} else {
			// Si no contiene "/", se asume que es solo el nombre del archivo
			ultimoPunto = entrada.lastIndexOf(".");
		}

		if (ultimoPunto == -1) {
			return "";
		}

		return entrada.substring(ultimoPunto + 1).toLowerCase();
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
	 * Descarga una imagen desde una URL y la guarda en una carpeta de destino.
	 * 
	 * @param urlImagen      URI de la imagen a descargar.
	 * @param carpetaDestino Ruta de la carpeta de destino.
	 * @return true si la descarga y conversión son exitosas, false en caso
	 *         contrario.
	 */
	public static CompletableFuture<Boolean> descargarYConvertirImagenAsync(URI urlImagen, String carpetaDestino,
			String nuevoNombre) {
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

				String extension = obtenerExtension(nuevoNombre);
				Path rutaDestino = Path.of(carpetaDestino, nuevoNombre);

				if (!extension.equals("jpg")) {
					try (InputStream in = url.openStream()) {
						BufferedImage image = ImageIO.read(in);
						if (image == null) {
							System.err.println("No se pudo cargar la imagen desde " + urlImagen);
							return false;
						}
						ImageIO.write(image, "jpg", rutaDestino.toFile());
					}
				} else {
					try (InputStream in = url.openStream()) {
						Files.copy(in, rutaDestino, StandardCopyOption.REPLACE_EXISTING);
					}
				}

				return true;
			} catch (MalformedURLException e) {
				System.err.println("La URL no es válida: " + urlImagen);
				return false;
			} catch (IOException e) {
				manejarExcepcion(e);
				return false;
			}
		});
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
		return input.replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"")
				.replace("&#39;", "'").replace("&rsquo;", "'").replace("&ldquo;", "\"").replace("&rdquo;", "\"")
				.replace("&nbsp;", " ").replace("&mdash;", "—").replace("&ndash;", "–").replace("<ul>", "'")
				.replace("<li>", "'").replace("</ul>", "'").replace("</li>", "'");
	}

	/**
	 * Método que crea la carpeta "portadas" para almacenar las imágenes de portada
	 * de los cómics.
	 * 
	 * @throws IOException Si ocurre un error al crear la carpeta.
	 */
	public static void crearCarpeta() {
		Ventanas nav = new Ventanas();
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String nombreCompletoDB = obtenerDatoDespuesDeDosPuntos("Database");
		String nombreCortado[] = nombreCompletoDB.split("\\.");
		String nombredb = nombreCortado[0];
		String defaultImagePath = documentsPath + File.separator + "libreria_comics" + File.separator + nombredb
				+ File.separator + "portadas";

		File portadasFolder = new File(defaultImagePath);

		if (!portadasFolder.exists() && !portadasFolder.mkdirs()) {

			nav.alertaException("No se puede crear la carpeta donde van las portadas descargadas/copiadas");

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
		String archivoConfiguracion = obtenerCarpetaConfiguracion() + File.separator + "configuracion_local.conf";

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

	public static File tratarFichero(String frase, String formato, boolean esGuardado) {
		FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentana);
		FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentanaPrincipal);

		try {

			File fichero = abrirFileChooser(frase, formato, esGuardado);

			getReferenciaVentana().getBotonCancelarSubida().setVisible(false);

			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
//			

			return fichero;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static File abrirFileChooser(String frase, String formato, boolean esGuardado) {
		setFileChooserOpen(true);
		// Obtener todas las ventanas disponibles
		List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();
		Map<Stage, EventHandler<WindowEvent>> originalCloseHandlers = new HashMap<>();

		// Deshabilitar el cierre de todas las ventanas mientras el FileChooser está
		// abierto
		for (Stage stage : stageVentanas) {
			EventHandler<WindowEvent> originalCloseHandler = stage.getOnCloseRequest();
			originalCloseHandlers.put(stage, originalCloseHandler);

			stage.setOnCloseRequest(event -> {
				if (fileChooserOpen) {
					event.consume();
				}
			});
		}

		// Deshabilitar todas las ventanas mientras el FileChooser está abierto
		for (Stage stage : stageVentanas) {
			stage.addEventFilter(MouseEvent.ANY, consumeEvent);
			stage.addEventFilter(KeyEvent.ANY, consumeEvent);
		}

		File fichero = null;
		try {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(frase, formato);
			fileChooser.getExtensionFilters().add(extFilter);

			if (esGuardado) {
				fichero = fileChooser.showSaveDialog(null);
			} else {
				fichero = fileChooser.showOpenDialog(null);
			}

			// Restaurar controladores de eventos de cierre originales
			for (Map.Entry<Stage, EventHandler<WindowEvent>> entry : originalCloseHandlers.entrySet()) {
				Stage stage = entry.getKey();
				EventHandler<WindowEvent> originalCloseHandler = entry.getValue();
				stage.setOnCloseRequest(originalCloseHandler);
			}

			for (Stage stage : stageVentanas) {
				stage.removeEventFilter(MouseEvent.ANY, consumeEvent);
				stage.removeEventFilter(KeyEvent.ANY, consumeEvent);
			}

			for (Map.Entry<Stage, EventHandler<WindowEvent>> entry : originalCloseHandlers.entrySet()) {
				Stage stage = entry.getKey();
				EventHandler<WindowEvent> originalCloseHandler = entry.getValue();
				stage.setOnCloseRequest(originalCloseHandler);
			}

			return fichero;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static EventHandler<Event> consumeEvent = Event::consume;

	// Método para actualizar el estado del FileChooser
	public static void setFileChooserOpen(boolean open) {
		fileChooserOpen = open;
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
	 * @param inputPaths Lista de URLs que representan los archivos a conservar.
	 */
	public static void borrarArchivosNoEnLista(List<String> inputPaths) {
		String directorioComun = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
				+ ConectManager.DB_NAME + File.separator + "portadas" + File.separator;

		List<String> nombresArchivosEnDirectorio = obtenerNombresArchivosEnDirectorio(directorioComun);

		for (String nombreArchivo : nombresArchivosEnDirectorio) {
			Path archivoAEliminarPath = Paths.get("", nombreArchivo).normalize();

			if (!inputPaths.contains(archivoAEliminarPath.toString())) {
				try {
					if (Files.exists(archivoAEliminarPath) && Files.isRegularFile(archivoAEliminarPath)) {
						Files.delete(archivoAEliminarPath);
					}
				} catch (Exception e) {
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
	public static List<String> obtenerNombresArchivosEnDirectorio(String directorio) {
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

		SelectManager.verLibreria(sentenciaSQL, false);

	}

	/**
	 * Obtiene el ID del cómic seleccionado desde la tabla.
	 * 
	 * @return El ID del cómic seleccionado o null si no hay selección.
	 */
	public static String obtenerIdComicSeleccionado(TableView<Comic> tablaBBDD) {
		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();
		return (idRow != null) ? idRow.getid() : null;
	}

	/**
	 * Obtiene el objeto Comic seleccionado según el ID proporcionado.
	 * 
	 * @param idComic El ID del cómic a obtener.
	 * @return El objeto Comic correspondiente al ID proporcionado.
	 * @throws SQLException Si hay un error al acceder a la base de datos.
	 */
	public static Comic obtenerComicSeleccionado(String idComic) {

		if (!ListaComicsDAO.comicsImportados.isEmpty()) {
			return ListaComicsDAO.buscarComicPorID(ListaComicsDAO.comicsImportados, idComic);
		} else {
			return SelectManager.comicDatos(idComic);
		}
	}

	/**
	 * Verifies if the MySQL service is running on the provided host and port.
	 *
	 * @param host       The host of the MySQL service.
	 * @param portString The port number of the MySQL service.
	 * @return true if the service is running, false otherwise.
	 */
	public static boolean isMySQLServiceRunning(String host, String portString) {
		int port;
		try {
			port = Integer.parseInt(portString);
			if (port < 1 || port > 65535) {
				throw new IllegalArgumentException("Port is out of valid range.");
			}
		} catch (IllegalArgumentException e) {
			return false;
		}

		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(InetAddress.getByName(host), port), 1000); // Timeout set to 1 second
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Crea las carpetas necesarias para realizar backups.
	 */
	public static void crearCarpetasBackup() {
		String carpetaBackup = obtenerCarpetaConfiguracion() + File.separator
				+ Utilidades.obtenerDatoDespuesDeDosPuntos("Database") + File.separator + "backups";

		try {
			File carpetaBackupsFile = new File(carpetaBackup);
			Utilidades.crearCarpeta();
			if (!carpetaBackupsFile.exists()) {
				carpetaBackupsFile.mkdirs();
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
		try (InputStream input = Utilidades.class.getResourceAsStream("/imagenes/sinPortada.jpg")) {
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

	private static void copiarImagenDesdeArchivo(File imagenArchivo, File directorio, String nombreImagen) {
		try (InputStream fileInputStream = new FileInputStream(imagenArchivo);
				OutputStream fileOutputStream = new FileOutputStream(
						Paths.get(directorio.getAbsolutePath(), nombreImagen).toFile())) {

			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, bytesRead);
			}
		} catch (FileNotFoundException e) {
			// El archivo no se encontró, proporciona un mensaje de error descriptivo
			System.err.println("No se pudo encontrar el archivo: " + imagenArchivo.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			// Ocurrió un error de E/S, proporciona un mensaje de error genérico
			System.err.println("Ocurrió un error al copiar la imagen.");
			e.printStackTrace();
		}
	}

	public static void manejarExcepcion(Exception e) {

		Ventanas nav = new Ventanas();

		nav.alertaException(e.toString());
		e.printStackTrace();
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
			return comicInfo != null; // Devuelve true si se encontró información del cómic.
		} catch (Exception e) {
			manejarExcepcion(e);
			return false;
		}
	}

	public static int contarLineasFichero(File fichero) {
		int contador = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
			while (br.readLine() != null) {
				contador++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return contador;
	}

	public static Image devolverImagenComic(String direccionComic) {
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

	/**
	 * Función que verifica si un archivo de portada existe
	 *
	 * @param defaultImagePath El directorio de las portadas
	 * @param nombreModificado El nombre del archivo modificado
	 * @return true si el archivo existe, false en caso contrario
	 */
	public static boolean existeArchivo(String defaultImagePath, String nombreModificado) {

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

	public static String buscarProgramasEnDirectorio(String nombreDirectorio, String nomAplicacion) {

		File direccionAplicacion = new File(nombreDirectorio);

		if (direccionAplicacion.exists()) {

			return nombreDirectorio + File.separator + nomAplicacion;

		}
		return "";
	}

	public static void iniciarConexionMySql(String directorio) {
		String rutaScriptControl = buscarProgramasEnDirectorio(directorio, "xampp-control.exe");
		String rutaXamppStart = buscarProgramasEnDirectorio(directorio, "xampp_start.exe");

		try {
			if (!rutaScriptControl.isEmpty() && verificarExistenciaFichero(rutaScriptControl) && !isXAMPPRunning()) {
				abrirPrograma(rutaScriptControl);
			}

			if (!rutaXamppStart.isEmpty()) {
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
		} catch (IOException e) {
			manejarExcepcion(e);
		}
	}

	// Función para verificar la existencia de un archivo
	private static boolean verificarExistenciaFichero(String rutaArchivo) {
		File archivo = new File(rutaArchivo);
		return archivo.exists();
	}

	/**
	 * Funcion que crea una copia de seguridad de la base de datos siempre que el
	 * sistema operativo sea Windows
	 *
	 * @param fichero
	 */
	public static void backupDB(File fichero) {
		try {
			// Ruta de la base de datos actual
			File dbFile = new File(DB_FOLDER + FuncionesFicheros.datosEnvioFichero());

			// Copiar la base de datos a la ubicación deseada
			Files.copy(dbFile.toPath(), fichero.toPath(), StandardCopyOption.REPLACE_EXISTING);

			System.out.println("Copia de seguridad de la base de datos creada en: " + fichero.getAbsolutePath());
		} catch (IOException e) {
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
							InputStream is = getClass().getResourceAsStream("/imagenes/sinPortada.jpg");
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

		String carpetaPortada = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
				+ ConectManager.DB_NAME + File.separator + "portadas";

		String nombreAleatorio = Utilidades.generarCodigoUnico(carpetaPortada);
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

			if (elemento != null) {
				if (verElemento) {
					elemento.setVisible(false);
					elemento.setDisable(true);
				} else {
					elemento.setVisible(true);
					elemento.setDisable(false);
				}
			}

		}
	}

	public static void descargarYAbrirEjecutableDesdeGitHub() {
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

	public static void descargarPDFAsync(File file, ComboBox<String> comboPreviews) {
		String seleccion = comboPreviews.getValue();

		if (seleccion != null) {
			int indiceSeleccionado = comboPreviews.getSelectionModel().getSelectedIndex();

			if (indiceSeleccionado >= 0 && indiceSeleccionado < OpcionesAvanzadasController.urlActualizados.size()) {
				String urlSeleccionada = OpcionesAvanzadasController.urlActualizados.get(indiceSeleccionado);

				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						try (FileOutputStream fileOutputStream = new FileOutputStream(file);) {
							URI uri = new URI(urlSeleccionada);
							URLConnection conexion = uri.toURL().openConnection();
							InputStream inputStream = conexion.getInputStream();
							BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

							byte[] buffer = new byte[1024];
							int bytesRead;
							while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
								fileOutputStream.write(buffer, 0, bytesRead);
							}
							bufferedInputStream.close();

						} catch (IOException | URISyntaxException e) {
							// Manejar excepciones en el hilo de JavaFX
							Platform.runLater(() -> Utilidades.manejarExcepcion(e));
						}
						return null;
					}
				};

				// Manejar excepciones generadas dentro del Task
				task.setOnFailed(event -> Utilidades.manejarExcepcion((Exception) task.getException()));

				// Ejecutar el Task en un nuevo hilo
				Thread thread = new Thread(task);
				thread.setDaemon(true); // Para que el hilo finalice cuando la aplicación principal se cierre
				thread.start();
			}
		}
	}

	public static String eliminarParentesis(String input) {
		StringBuilder resultado = new StringBuilder();
		boolean dentroDeParentesis = false;

		for (int i = 0; i < input.length(); i++) {
			char caracter = input.charAt(i);

			if (caracter == '(') {
				dentroDeParentesis = true;
			} else if (caracter == ')') {
				dentroDeParentesis = false;
			} else if (!dentroDeParentesis) {
				resultado.append(caracter);
			}
		}

		// Convertir la primera letra de cada palabra a mayúscula
		String[] palabras = resultado.toString().trim().split("\\s+");
		StringBuilder resultadoFinal = new StringBuilder();
		for (String palabra : palabras) {
			if (palabra.length() > 0) {
				resultadoFinal.append(Character.toUpperCase(palabra.charAt(0)));
				if (palabra.length() > 1) {
					resultadoFinal.append(palabra.substring(1).toLowerCase());
				}
				resultadoFinal.append(" ");
			}
		}

		return resultadoFinal.toString().trim(); // Elimina espacios en blanco al inicio y al final
	}

	public static String extraerNombreLimpio(String nombreComic) {
		// Encontrar la posición del símbolo #
		int indiceNumeral = nombreComic.indexOf("#");

		// Eliminar todos los números si no hay #
		if (!nombreComic.contains("#")) {
			nombreComic = nombreComic.replaceAll("\\d+", "");
		}

		// Si no se encuentra el símbolo #, devuelve el nombre completo
		if (indiceNumeral == -1) {
			return eliminarPalabrasClave(nombreComic.trim());
		}

		// Extraer el texto antes del símbolo #
		return eliminarPalabrasClave(nombreComic.substring(0, indiceNumeral).trim());
	}

	private static String eliminarPalabrasClave(String texto) {
		// Conservar números seguidos de ' junto con cualquier texto que los preceda
		texto = texto.replaceAll("(.*?\\b\\w*'\\d+)", "$1");

		// Eliminar palabras clave
		texto = texto.replaceAll("(?i)\\b(tp|omnibus|omni|ed|deluxe|dlx|edition|hc|vol|cvr)\\b", "");

		// Eliminar "by" y lo que sigue después de él
		texto = texto.replaceAll("(?i)\\s*by\\s*.*", "");

		// Eliminar espacios adicionales
		texto = texto.trim().replaceAll("\\s+", " ");

		return texto;
	}

	public static String devolverPalabrasClave(String texto) {
		// Definir las palabras clave y sus correspondientes tipos de edición
		String[] palabrasClave = { "absolute", "omnibus hc", "hc omnibus", "tp omnibus", "omnibus tp", "hc omni",
				"omni hc", "tp omni", "omni tp", "omnibus", "omni", "tp", "deluxe", "dlx", "treasury edition", "hc",
				"#", "cvr", "TBD" };

		// Escapar los caracteres especiales en las palabras clave
		StringBuilder regexBuilder = new StringBuilder();
		for (String palabra : palabrasClave) {
			regexBuilder.append(Pattern.quote(palabra)).append("|");
		}
		String regex = regexBuilder.substring(0, regexBuilder.length() - 1); // Eliminar el último "|"

		// Compilar la expresión regular
		Pattern pattern = Pattern.compile(regex);

		// Crear un Matcher para buscar las coincidencias en el texto
		Matcher matcher = pattern.matcher(texto.toLowerCase());

		// Determinar el tipo de edición correspondiente a la palabra clave encontrada
		StringBuilder resultado = new StringBuilder();
		if (matcher.find()) {
			switch (matcher.group()) {
			case "absolute":
				resultado.append("Edición absolute (Absolute Edition)");
				break;
			case "omnibus hc":
			case "hc omnibus":
			case "tp omnibus":
			case "omnibus tp":
			case "hc omni":
			case "omni hc":
			case "tp omni":
			case "omni tp":
			case "omnibus":
			case "omni":
				resultado.append("Edición omnibus (Omnibus)");
				break;
			case "tp":
			case "TBD":
				resultado.append("Tapa blanda (Paperback)");
				break;
			case "deluxe":
			case "dlx":
			case "treasury edition":
				resultado.append("Edición de lujo (Deluxe Edition)");
				break;
			case "hc":
				resultado.append("Tapa dura (Hardcover)");
				break;
			case "#":
			case "cvr":
				resultado.append("Grapa (Issue individual)");
				break;
			default:
				resultado.append("Grapa (Issue individual)");
				break;
			}
		} else {
			resultado.append("Grapa (Issue individual)");
		}
		return resultado.toString();
	}

	public static String extraerNumeroLimpio(String numComic) {

		numComic = numComic.replaceAll("\\(.*?\\)", "");

		// Encontrar la posición del símbolo #
		int indiceNumeral = numComic.indexOf("#");

		// Si no se encuentra el símbolo #, buscar cualquier número en la cadena
		if (indiceNumeral == -1) {
			// Buscar cualquier número en la cadena
			String posibleNumero = numComic.replaceAll("\\D", "").trim();
			if (!posibleNumero.isEmpty()) {
				return posibleNumero;
			} else {
				return "0";
			}
		}

		// Encontrar la posición del primer espacio después del #
		int indiceEspacioDespuesNumeral = numComic.indexOf(" ", indiceNumeral);

		// Si no se encuentra el espacio después del símbolo #,
		// devuelve el texto después del #
		if (indiceEspacioDespuesNumeral == -1) {
			return numComic.substring(indiceNumeral + 1).trim();
		}

		// Buscar "hc", "vol", "omnibus" o "tp" después del espacio
		String textoDespuesNumeral = numComic.substring(indiceNumeral + 1, indiceEspacioDespuesNumeral).trim()
				.toLowerCase();
		int indiceHc = textoDespuesNumeral.indexOf("hc ");
		int indiceVol = textoDespuesNumeral.indexOf("vol ");
		int indiceOmnibus = textoDespuesNumeral.indexOf("omnibus ");
		int indiceTp = textoDespuesNumeral.indexOf("tp ");
		int indiceTermino = Math.min(Math.min(Math.min(indiceHc, indiceVol), indiceOmnibus), indiceTp);

		// Si se encuentra alguna palabra clave, buscar un número después de esa palabra
		// clave
		if (indiceTermino != -1) {
			int indiceEspacioDespuesTermino = numComic.indexOf(" ", indiceEspacioDespuesNumeral + 1);
			if (indiceEspacioDespuesTermino != -1) {
				String posibleNumero = numComic.substring(indiceEspacioDespuesNumeral + 1, indiceEspacioDespuesTermino)
						.trim();
				if (posibleNumero.matches("\\d+")) {
					return posibleNumero;
				}
			}
		}

		// Si no se encontró ningún número después de las palabras clave, buscar un
		// número después del espacio
		// que sigue al símbolo #
		int indiceEspacioDespuesNumeral2 = numComic.indexOf(" ", indiceNumeral + 1);
		if (indiceEspacioDespuesNumeral2 != -1) {
			String posibleNumero = numComic.substring(indiceNumeral + 1, indiceEspacioDespuesNumeral2).trim();
			if (posibleNumero.matches("\\d+")) {
				return posibleNumero;
			}
		}

		// Si no se encontró ningún número después de los términos clave o el espacio
		// después del símbolo #,
		// buscar cualquier número al final de la cadena
		Pattern pattern = Pattern.compile("\\d+$");
		Matcher matcher = pattern.matcher(numComic);
		if (matcher.find()) {
			return matcher.group();
		}

		// Si no se encontró ningún número en la cadena, devolver "0"
		return "0";
	}

	public static void copiarDirectorio(String directorioNuevo, String directorioOriginal) {

		File directorioOrigen = new File(directorioOriginal);
		File directorioDestino = new File(directorioNuevo);

		System.out.println("Tal: " + directorioOrigen);

		// Verificar si el directorio origen existe y es un directorio
		if (!directorioOrigen.exists() || !directorioOrigen.isDirectory()) {
			throw new IllegalArgumentException("El directorio origen no existe o no es un directorio válido.");
		}

		// Verificar si el directorio destino ya existe
		if (!directorioDestino.exists()) {
			// Si el directorio destino ya existe, añadir la fecha actual al nombre
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
			String fechaActual = dateFormat.format(new Date());
			directorioNuevo += "_" + fechaActual + File.separator;
		}

		// Crear el directorio destino
		directorioDestino = new File(directorioNuevo);
		directorioDestino.mkdirs();

		// Obtener la lista de archivos en el directorio origen
		File[] archivos = directorioOrigen.listFiles();

		if (archivos != null) {
			for (File archivo : archivos) {
				if (archivo.isDirectory()) {
					// Si es un directorio, llamar recursivamente a esta función
					copiarDirectorio(directorioNuevo, directorioOriginal);
				} else {
					copiarArchivo(archivo.getAbsolutePath(), directorioNuevo + File.separator + archivo.getName());
				}
			}
		}
	}

	public static String obtenerNombreArchivoSinExtension(String ruta) {
		// Encontrar la última ocurrencia del separador de directorios
		int indiceSeparador = ruta.lastIndexOf(File.separator);

		// Si no se encuentra el separador, devolver toda la ruta
		if (indiceSeparador == -1) {
			return ruta;
		}

		// Obtener la subcadena después del último separador
		String nombreConExtension = ruta.substring(indiceSeparador + 1);

		// Encontrar la última ocurrencia del punto que separa el nombre de la extensión
		int indicePunto = nombreConExtension.lastIndexOf('.');

		// Si no se encuentra el punto, devolver el nombre con extensión
		if (indicePunto == -1) {
			return nombreConExtension;
		}

		// Devolver el nombre del archivo sin extensión
		return nombreConExtension.substring(0, indicePunto);
	}

	public static void copiarArchivo(String origen, String destino) {
		try (FileInputStream entrada = new FileInputStream(origen);
				FileOutputStream salida = new FileOutputStream(destino);) {
			// Verificar si el archivo es de extensión .jpg
			if (!origen.toLowerCase().endsWith(".jpg")) {
				return;
			}

			File file = new File(origen);
			if (!file.exists()) {
				return;
			}

			byte[] buffer = new byte[1024];
			int longitud;
			while ((longitud = entrada.read(buffer)) > 0) {
				salida.write(buffer, 0, longitud);
			}
		} catch (FileNotFoundException e) {
			// Manejar el caso en el que el archivo de origen no existe
			System.err.println("El archivo de origen no existe: " + origen);
			// No hacer nada, simplemente salir de la función
			return;
		} catch (IOException e) {
			// Manejar otras posibles excepciones de E/S
			e.printStackTrace();
			// Otra lógica de manejo de la excepción según tus necesidades
		}
	}

	public static void eliminarArchivosJPG(File dir) {
		// Verifica que el directorio exista y sea un directorio válido
		if (dir.exists() && dir.isDirectory()) {
			// Lista de archivos en el directorio
			File[] archivos = dir.listFiles();

			// Itera sobre cada archivo en el directorio
			for (File archivo : archivos) {
				// Verifica si el archivo es un archivo JPG
				if (archivo.isFile() && archivo.getName().toLowerCase().endsWith(".jpg")) {
					// Intenta eliminar el archivo
					if (archivo.delete()) {
						System.out.println("Se ha eliminado: " + archivo.getName());
					} else {
						System.out.println("No se pudo eliminar: " + archivo.getName());
					}
				}
			}
		} else {
			System.out.println("El directorio especificado no existe o no es válido.");
		}
	}

	public static int compareVersions(String version1, String version2) {
		String[] parts1 = version1.split("\\.");
		String[] parts2 = version2.split("\\.");

		int minLength = Math.min(parts1.length, parts2.length);

		for (int i = 0; i < minLength; i++) {
			int part1 = Integer.parseInt(parts1[i]);
			int part2 = Integer.parseInt(parts2[i]);

			if (part1 != part2) {
				return Integer.compare(part1, part2);
			}
		}

		return Integer.compare(parts1.length, parts2.length);
	}

	public static String convertirFormatoFecha(String fechaStr) {
		// Verificar si la fecha está en el formato incorrecto (dd/MM/yyyy)
		if (!fechaStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
			System.out.println("La fecha no está en el formato incorrecto. Ignorándola.");
			return fechaStr; // Devolver la misma fecha sin cambios
		}

		// Definir el formato de entrada y salida
		SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");

		try {
			// Convertir la fecha de cadena al tipo Date
			Date fecha = formatoEntrada.parse(fechaStr);

			// Formatear la fecha al nuevo formato
			return formatoSalida.format(fecha);
		} catch (ParseException e) {
			// Si hay un error al convertir la fecha, imprimir un mensaje y devolver la
			// fecha original
			System.out.println("Error al convertir la fecha. Se devuelve la fecha original.");
			return fechaStr;
		}
	}

	public static void cerrarCargaComics() {
		List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();
		for (Stage stage : stageVentanas) {

			if (stage.getTitle().equalsIgnoreCase("Carga de comics")) {
				stage.close(); // Close the stage if it's not the current state
			}
		}
	}

	public static void cerrarOpcionesAvanzadas() {
		List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();
		for (Stage stage : stageVentanas) {

			if (stage.getTitle().equalsIgnoreCase("Opciones avanzadas")) {
				stage.close(); // Close the stage if it's not the current state
			}
		}
	}

	public static void deleteFile(String filePath) {
		Path path = Paths.get(filePath);

		try {
			Files.delete(path);
		} catch (IOException e) {
			System.err.println("No se pudo eliminar el archivo " + filePath + ": " + e.getMessage());
		}
	}

	public static void eliminarContenidoCarpeta(String rutaCarpeta) {
		File carpeta = new File(rutaCarpeta);

		// Verificar si la ruta es una carpeta
		if (!carpeta.isDirectory()) {
			System.out.println("La ruta especificada no es una carpeta válida.");
			return;
		}

		// Obtener la lista de archivos en la carpeta
		File[] archivos = carpeta.listFiles();

		// Eliminar cada archivo
		for (File archivo : archivos) {
			if (archivo.isFile()) {
				archivo.delete(); // Si es un archivo, borrarlo
			} else if (archivo.isDirectory()) {
				eliminarContenidoCarpeta(archivo.getAbsolutePath()); // Si es una carpeta, llamar recursivamente
			}
		}
	}

	public static ImageView createIcon(String iconName, double width, double height) {
		Image image = new Image(Utilidades.class.getResourceAsStream(iconName), width, height, true, true);
		ImageView imageView = new ImageView(image);
		return imageView;
	}

//	public static String directorioPortada() {
//
//		String carpeta = System.getProperty("user.home") + "/Documents/libreria_comics/";
//		String nombreCompletoDB = FuncionesFicheros.datosEnvioFichero();
//		String nombreCortado[] = nombreCompletoDB.split("\\.");
//		String nombredb = nombreCortado[0];
//
//		return carpeta + nombredb;
//	}

	public static String nombreDB() {
		String nombreCompletoDB = FuncionesFicheros.datosEnvioFichero();
		String nombreCortado[] = nombreCompletoDB.split("\\.");
		String nombredb = nombreCortado[0];

		return nombredb;
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static AccionReferencias getReferenciaVentanaPrincipal() {
		return referenciaVentanaPrincipal;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		Utilidades.referenciaVentana = referenciaVentana;
	}

	public static void setReferenciaVentanaPrincipal(AccionReferencias referenciaVentana) {
		Utilidades.referenciaVentanaPrincipal = referenciaVentana;
	}

}