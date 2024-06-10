package apisFunciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import dbmanager.ConectManager;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;

public class FuncionesApis {

	/**
	 * Guarda datos de claves de API de Marvel en un archivo de configuración.
	 */
	public static void guardarDatosClavesMarvel() {
		String nombreArchivo = Utilidades.obtenerCarpetaConfiguracion() + File.separator + "claves_marvel_api.conf";

		File archivo = new File(nombreArchivo);

		if (!archivo.exists()) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
				writer.write("Public Key: ");
				writer.newLine();
				writer.write("Private Key: ");

				writer.close();
			} catch (IOException e) {
				Utilidades.manejarExcepcion(e);
			}
		}
	}

	/**
	 * Obtiene las claves de la API de Marvel desde un archivo de configuración.
	 *
	 * @return Las claves de la API en el formato "Private Key: Public Key".
	 */
	public static String obtenerClaveApiMarvel() {
		String nombreArchivo = Utilidades.obtenerCarpetaConfiguracion() + File.separator + "claves_marvel_api.conf";

		File archivo = new File(nombreArchivo);
		if (!archivo.exists()) {
			guardarDatosClavesMarvel();
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
			String linea;
			String clavePublica = null;
			String clavePrivada = null;

			while ((linea = reader.readLine()) != null) {
				if (linea.startsWith("Public Key: ")) {
					clavePublica = linea.substring("Public Key: ".length()).trim();

				} else if (linea.startsWith("Private Key: ")) {
					clavePrivada = linea.substring("Private Key: ".length()).trim();
				}
			}

			// Verificar que ambas claves se hayan encontrado en el archivo
			if (clavePublica != null && clavePrivada != null) {
				return clavePrivada + ":" + clavePublica; // Retornar ambas claves en un formato deseado
			}
			return ""; // Manejo de error: devuelve una cadena vacía en caso de que falte alguna clave
		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
			return ""; // Manejo de error: devuelve una cadena vacía en caso de error
		}
	}

	/**
	 * Comprueba si el archivo de claves de API de Marvel tiene la estructura
	 * esperada.
	 * 
	 * @return true si el archivo tiene la estructura correcta, false de lo
	 *         contrario.
	 */
	public static boolean verificarEstructuraClavesMarvel() {
		String nombreArchivo = Utilidades.obtenerCarpetaConfiguracion() + File.separator + "claves_marvel_api.conf";

		try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
			String linea;
			int contador = 0;

			while ((linea = reader.readLine()) != null) {
				// Verificar la estructura esperada
				if (contador == 0 && linea.contains("Public Key:")) {
					contador++;
				} else if (contador == 1 && linea.contains("Private Key:")) {
					contador++;
				}
			}

			// Verificar que haya dos líneas en total
			return contador == 2;
		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
			return false;
		}
	}

	/**
	 * Comprueba si el archivo de clave de API de Comic Vine tiene la estructura
	 * esperada.
	 * 
	 * @return true si el archivo tiene la estructura correcta, false de lo
	 *         contrario.
	 */
	public static boolean verificarEstructuraApiComicVine() {
		String nombreArchivo = Utilidades.obtenerCarpetaConfiguracion() + File.separator + "clave_comicVine_api.conf";

		try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
			String linea;
			int contador = 0;

			while ((linea = reader.readLine()) != null) {
				// Verificar la estructura esperada
				if (contador == 0 && linea.contains("Clave Api Comic Vine:")) {
					contador++;
				}

			}

			// Verificar que haya al menos una línea
			return contador == 1;
		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
			return false;
		}
	}

	/**
	 * Esta función guarda una clave de API para Comic Vine en un archivo de
	 * configuración. La clave de API se almacena en un archivo en la ubicación de
	 * la librería del usuario. Si el archivo ya existe, se sobrescribe.
	 *
	 */
	public static void guardarApiComicVine() {
		String nombreArchivo = Utilidades.obtenerCarpetaConfiguracion() + File.separator + "clave_comicVine_api.conf";

		File archivo = new File(nombreArchivo);

		if (!archivo.exists()) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
				writer.write("###############################");
				writer.newLine();
				writer.write("Clave Api Comic Vine: ");
				writer.newLine();
				writer.write("###############################");

				writer.close();
			} catch (IOException e) {
				Utilidades.manejarExcepcion(e);
			}
		}
	}

	public static String cargarApiComicVine() {
		String nombreArchivo = Utilidades.obtenerCarpetaConfiguracion() + File.separator + "clave_comicVine_api.conf";

		// Verificar si el archivo existe y, si no, crearlo con los valores
		// predeterminados
		File archivo = new File(nombreArchivo);
		if (!archivo.exists()) {
			guardarApiComicVine();
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(": ");
				if (parts.length == 2) {
					String clave_api = parts[1];
					return clave_api;
				}
			}
		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
		return null;
	}

	/**
	 * Obtiene las claves de la API de Marvel de un archivo o fuente de datos.
	 *
	 * @return Un array de cadenas con las claves pública y privada de la API.
	 */
	public static String[] clavesApiMarvel() {
		String claves[] = new String[2]; // Crear un arreglo de dos elementos para almacenar las claves

		String clavesDesdeArchivo = obtenerClaveApiMarvel(); // Obtener las claves desde el archivo

		if (!clavesDesdeArchivo.isEmpty()) {
			String[] partes = clavesDesdeArchivo.split(":");
			if (partes.length == 2) {
				String clavePublica = partes[0].trim();
				String clavePrivada = partes[1].trim();

				claves[0] = clavePublica; // Almacenar la clave pública en el primer elemento del arreglo
				claves[1] = clavePrivada; // Almacenar la clave privada en el segundo elemento del arreglo
			}
		}

		return claves;
	}

	/**
	 * Guarda las claves de API de Marvel en un archivo de configuración.
	 *
	 * @param publicKey  Clave pública de Marvel
	 * @param privateKey Clave privada de Marvel
	 */
	public static void reescribirClavesMarvel(String publicKey, String privateKey) {
		String nombreArchivo = Utilidades.obtenerCarpetaConfiguracion() + File.separator + "claves_marvel_api.conf";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
			writer.write("Public Key: " + publicKey);
			writer.newLine();
			writer.write("Private Key: " + privateKey);
		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Guarda la clave de API de Comic Vine en un archivo de configuración.
	 *
	 * @param apiKey Clave de API de Comic Vine
	 */
	public static void reescribirClaveApiComicVine(String apiKey) {
		String nombreArchivo = Utilidades.obtenerCarpetaConfiguracion() + File.separator + "clave_comicVine_api.conf";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
			writer.write("###############################");
			writer.newLine();
			writer.write("Clave Api Comic Vine: " + apiKey);
			writer.newLine();
			writer.write("###############################");
		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	public static void comprobarApisComics() {
		String apiKey = cargarApiComicVine();
		String[] clavesMarvel = clavesApiMarvel();

		if (!verificarClavesAPI(clavesMarvel, apiKey)) {
			return; // Salir si hay errores en las claves API
		}
	}
	
	/**
	 * Verifica si las claves API están ausentes o vacías y muestra una alerta en
	 * caso de error.
	 *
	 * @param clavesMarvel Claves API de Marvel.
	 * @param apiKey       Clave API de Comic Vine.
	 */
	public static boolean verificarClavesAPI(String[] clavesMarvel, String apiKey) {
		String exception = "";
		Ventanas nav = new Ventanas();
		
		if (clavesMarvel.length == 0) {
			exception += "\nDebes obtener una clave API de Marvel. Visita https://developer.marvel.com/";
		}

		if (apiKey.isEmpty()) {
			exception += "\nDebes obtener una clave API de Comic Vine. Visita https://comicvine.gamespot.com/api/ (gratuito)";
		}

		if (!exception.isEmpty()) {
			nav.alertaNoApi(exception); // Mostrar alerta de error
			return false;
		}
		return true;
	}

}
