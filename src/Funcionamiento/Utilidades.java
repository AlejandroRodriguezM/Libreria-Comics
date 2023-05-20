package Funcionamiento;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 *  Esta clase permite comprobar si el sistema operativo es linux, windows o mac
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import JDBC.DBLibreriaManager;

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
	public String[] comaPorGuion(String[] datos) {
		for (int i = 0; i < datos.length; i++) {

			if (datos[i].contains(",")) {
				datos[i] = datos[i].replace(",", " - ");
			}
		}

		return datos;
	}

	/**
	 * Funcion que elimina la imagen temporal creada a la hora de subir imagenes
	 * mediante importacion de csv, modificacion o introducir datos manualmente
	 *
	 * @param pathFichero
	 */
	public void deleteImage(String pathFichero) {

		File original = new File(pathFichero);
		File tmp = new File(original.toString());

		try {
			Files.deleteIfExists(Paths.get(tmp.getParentFile() + "/tmp.jpg"));
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
	public void nueva_imagen(String direccion) throws IOException {
		try {
			File file;
			if (direccion != null) {
				file = new File(direccion);
			} else {
				String userDir = System.getProperty("user.home");
				String documentsPath = userDir + File.separator + "Documents";
				String imagePath = documentsPath + File.separator + "libreria_comics" + File.separator
						+ "portadas/sinPortada.jpg";
				file = new File(imagePath);
			}

			if (file != null && file.exists()) {
				String userDir = System.getProperty("user.home");
				String documentsPath = userDir + File.separator + "Documents";
				String imagePath = documentsPath + File.separator + "libreria_comics" + File.separator + "portadas";
				File portadasFolder = new File(imagePath);
				String ultimo_id = DBLibreriaManager.obtener_ultimo_id();
				int nuevo_id = Integer.parseInt(ultimo_id);

				if (!portadasFolder.exists()) {
					if (!portadasFolder.mkdirs()) {
						throw new IOException("No se pudo crear la carpeta 'portadas'");
					}
				}

				String nombreOriginal = file.getName();
				String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
				String nuevoNombreArchivo = String.valueOf(nuevo_id) + extension;
				File newFile = new File(portadasFolder.getPath() + File.separator + nuevoNombreArchivo);
				Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} else {
				throw new IOException("El archivo de imagen no existe");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void guardar_imagen(String direccion, String ID) {
	    try {
	        File file = null;
	        if (direccion != null) {
	            file = new File(direccion);
	        }

	        if (file.exists()) {
	            String userDir = System.getProperty("user.home");
	            String documentsPath = userDir + File.separator + "Documents";
	            String imagePath = documentsPath + File.separator + "libreria_comics" + File.separator + "portadas";
	            File portadasFolder = new File(imagePath);

	            if (!portadasFolder.exists()) {
	                if (!portadasFolder.mkdirs()) {
	                    throw new IOException("No se pudo crear la carpeta 'portadas'");
	                }
	            }

	            String nombreOriginal = file.getName();
	            String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
	            String nuevoNombreArchivo = ID + extension;
	            File newFile = new File(portadasFolder.getPath() + File.separator + nuevoNombreArchivo);
	            Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	        } else {
	            String userDir = System.getProperty("user.home");
	            String documentsPath = userDir + File.separator + "Documents";
	            String imagePath = documentsPath + File.separator + "libreria_comics" + File.separator + "portadas/sinPortada.jpg";
	            File sin_portada = new File(imagePath);
	            Files.copy(file.toPath(), sin_portada.toPath(), StandardCopyOption.REPLACE_EXISTING);

	        }
	    } catch (IOException e) {
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
			e.printStackTrace();
		}
	}
}
