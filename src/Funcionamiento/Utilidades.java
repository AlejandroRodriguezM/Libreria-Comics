package Funcionamiento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import JDBC.DBManager;

/**
 * Esta clase sirve para realizar diferentes funciones realizanas con la
 * comprobacion del sistema operativo que ejecuta este programa, al igual que
 * comprobar cual es tu navegador principal de internet
 *
 * @author Alejandro Rodriguez
 */
public class Utilidades {

	public static List<Comic> listaLimpia = new ArrayList<>();
	public static List<String> listaLimpiaAutoCompletado = new ArrayList<>();
	private static FuncionesExcel excel = new FuncionesExcel();

	private static Ventanas nav = new Ventanas();

	public static String os = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);

	public static boolean isWindows() {
		return os.contains("win");
	}

	public static boolean isMac() {
		return os.contains("mac");
	}

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
			nav.alertaException("Error: No funciona el boton \n" + e.toString());
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
			nav.alertaException("Error: No funciona el boton \n" + e.toString());
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
			nav.alertaException("Error: No funciona el boton \n" + e.toString());
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

		ArrayList<String> newList = new ArrayList<>();
		newList.clear();

		for (String s : listaComics) {
			if (!newList.contains(s)) {
				newList.add(s);
			}
		}

		return newList;
	}

	/**
	 * Funcion que cambia una ',' por un guion '-'
	 *
	 * @param campos
	 * @return
	 */
	public String comaPorGuion(String dato) {

		if (dato.contains(",")) {
			dato = dato.replace(",", " - ");
		}

		return dato;
	}

	/**
	 * Funcion que elimina la imagen temporal creada a la hora de subir imagenes
	 * mediante importacion de csv, modificacion o introducir datos manualmente
	 *
	 * @param pathFichero
	 */
	public void deleteImage(String pathFichero) {

		try {
			Files.deleteIfExists(Paths.get(pathFichero));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Funcion que permite la redimension de una imagen. Guarda la imagen y es
	 * cargada por otras funciones.
	 *
	 * @param file
	 * @return
	 */
	public void nueva_imagen(String imagen, String nuevoNombreArchivo) throws IOException {
		try {
			File file = new File(imagen);
			InputStream input = null;

			if (!file.exists()) {
				input = getClass().getResourceAsStream("sinPortada.jpg");
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
					+ DBManager.DB_NAME + File.separator + "portadas";

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
			e.printStackTrace();
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
			System.err.println("Se produjo un error al copiar el directorio: " + e.getMessage());
		}
	}

//	public String obtenerDatoDespuesDeDosPuntos(String linea) {
//		String userHome = System.getProperty("user.home");
//		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
//		String carpetaLibreria = ubicacion + File.separator + "libreria";
//		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion.conf";
//
//		try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfiguracion))) {
//			String line;
//			while ((line = reader.readLine()) != null) {
//				if (line.startsWith(linea + ": ")) {
//					return line.substring(linea.length() + 2).trim();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return "";
//	}

	public String obtenerNombreCompleto(Comic datos) {
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String defaultImagePath = documentsPath + File.separator + "libreria_comics" + File.separator
				+ DBManager.DB_NAME + File.separator + "portadas" + File.separator;
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

	public String obtenerNombreArchivo(String rutaCompleta) {
		// Obtener el separador de ruta del archivo según el sistema operativo
		String separadorRuta = File.separator;

		// Obtener la última posición del separador de ruta del archivo en la ruta
		// completa
		int posicionSeparador = rutaCompleta.lastIndexOf(separadorRuta);

		// Extraer el nombre del archivo sin la ruta
		String nombreArchivo = rutaCompleta.substring(posicionSeparador + 1);

		return nombreArchivo;
	}

	public void copia_seguridad() throws SQLException {
		// Realizar copia de seguridad
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String nombre_carpeta = dateFormat.format(new Date());

			String userDir = System.getProperty("user.home");
			String documentsPath = userDir + File.separator + "Documents";
			String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + DBManager.DB_NAME
					+ File.separator + "portadas";
			File sourceFolder = new File(sourcePath);

			String ubicacion = userDir + File.separator + "AppData" + File.separator + "Roaming";
			String carpetaLibreria = ubicacion + File.separator + "libreria" + File.separator + DBManager.DB_NAME
					+ File.separator + "backups" + File.separator + nombre_carpeta;
//					File libreria_backup = new File(carpetaLibreria);

			if (sourceFolder.exists()) {
				// Create the backups folder if it doesn't exist
				File backupsFolder = new File(carpetaLibreria);
				if (!backupsFolder.exists()) {
					if (!backupsFolder.mkdirs()) {
						throw new IOException("Failed to create 'backups' folder.");
					}
				}

				// Crear archivo zip con fecha actual
				String backupFileName = "portadas_" + dateFormat.format(new Date()) + ".zip";
				String backupPath = carpetaLibreria + File.separator + backupFileName;
				File backupFile = new File(backupPath);

				// Comprimir carpeta en el archivo zip
				try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(backupFile))) {
					zipFile(sourceFolder, sourceFolder.getName(), zipOut);
				}
				excel.savedataExcel(nombre_carpeta);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}
		if (fileToZip.isDirectory()) {
			if (fileName.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}
			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
			return;
		}
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}

		fis.close();
	}

	public void eliminarArchivosEnCarpeta() {

		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + DBManager.DB_NAME
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

	public void crearCopiaBaseDatos() {
		try {

//			String ddbb = obtenerDatoDespuesDeDosPuntos("Database");
//			String usuario = obtenerDatoDespuesDeDosPuntos("Usuario");
//			String password = obtenerDatoDespuesDeDosPuntos("Password");

			String userHome = System.getProperty("user.home");
			String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
			String carpeta_backups = ubicacion + File.separator + "libreria" + File.separator + "backups"
					+ File.separator + DBManager.DB_NAME + File.separator;

			String pathMySql = "C:\\Program Files\\MySQL";
			String mysqlDump = pathMySql + "\\mysqldump.exe";

			String nombreCopia = "copia_base_datos.sql";
			File carpetaDestino = new File(carpeta_backups);
			File archivoCopia = new File(carpetaDestino, nombreCopia);

			String[] command = new String[] { mysqlDump, "-u" + DBManager.DB_USER, "-p" + DBManager.DB_PASS, "-B",
					DBManager.DB_NAME, "--hex-blob", "--routines=true",
					"--result-file=" + archivoCopia.getAbsolutePath() };

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectError(Redirect.INHERIT);
			pb.redirectOutput(Redirect.to(archivoCopia));
			pb.start();

		} catch (Exception e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Elimina la imagen temporal de muestra de la base de datos.
	 */
	public void deleteImage() {
		try {
			Files.deleteIfExists(Paths.get("tmp.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
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

	/*
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

	/*
	 * Verifica si la posición de un guion en el nombre de archivo es válida para
	 * convertirlo.
	 * 
	 * @param nombreArchivo El nombre del archivo.
	 * 
	 * @param indice El índice del guion en el nombre del archivo.
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

	/**
	 * Funcion que devuelve la direccion de una url sin tener en cuenta el fichero y
	 * extension
	 * 
	 * @param rutaArchivo
	 * @return
	 */
	public static String eliminarDespuesUltimoPortadas(String rutaArchivo) {
		int indiceUltimoPortadas = rutaArchivo.lastIndexOf("portadas\\");
		if (indiceUltimoPortadas != -1) {
			return rutaArchivo.substring(0, indiceUltimoPortadas + 9);
		} else {
			return rutaArchivo;
		}
	}

	/**
	 * Funcion que solamente deuvelve el nombre del fichero y la extension dada una
	 * direccion
	 * 
	 * @param rutaArchivo
	 * @return
	 */
	public static String obtenerDespuesPortadas(String rutaArchivo) {
		int indicePortadas = rutaArchivo.indexOf("portadas\\");
		if (indicePortadas != -1) {
			return rutaArchivo.substring(indicePortadas + 9);
		} else {
			return "";
		}
	}

	public static void eliminarFichero(String direccion) {
		File archivo = new File(direccion);

		archivo.delete();
	}

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

}
