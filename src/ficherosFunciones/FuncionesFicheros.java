package ficherosFunciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import alarmas.AlarmaList;
import funcionesAuxiliares.Utilidades;
import javafx.scene.control.Label;

public class FuncionesFicheros {

	static String userHome = System.getProperty("user.home");
	static String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
	static String carpetaLibreria = ubicacion + File.separator + "libreria";

	public static Map<String, String> devolverDatosConfig() {
		Map<String, String> datosConfiguracion = new HashMap<>();
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion_local.conf";

		File fichero = new File(archivoConfiguracion);

		if (fichero.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfiguracion))) {
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("Database: ")) {
						datosConfiguracion.put("Database",
								Utilidades.defaultIfNullOrEmpty(line.substring("Database: ".length()), ""));
					}
				}
				if (!datosConfiguracion.isEmpty()) {
					return datosConfiguracion;
				}
			} catch (IOException e) {
				Utilidades.manejarExcepcion(e);
			}
		}
		return null;

	}

	public static void guardarDatosBaseLocal(String nombreBBDD, Label prontEstadoFichero, Label alarmaConexion) {

		String[] nombredbLimpio = nombreBBDD.split("\\."); // Dividir por el punto literal

		String carpetaBackup = carpetaLibreria + File.separator + nombredbLimpio[0] + File.separator + "backups";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion_local.conf";
		AlarmaList alarmaList = new AlarmaList();
		try {

			// Leer el archivo de configuración existente
			File configFile = new File(archivoConfiguracion);
			StringBuilder configContent = new StringBuilder();

			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile))) {
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					configContent.append(line).append(System.lineSeparator());
				}
			}
			configContent = actualizarClave(configContent, "Database", nombreBBDD);

			// Escribir el contenido actualizado al archivo de configuración
			try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile))) {
				bufferedWriter.write(configContent.toString());
				bufferedWriter.flush(); // Asegurar que los datos sean escritos en el archivo
				System.out.println("Archivo de configuración actualizado exitosamente.");
			}

			// Crear carpeta de backups si no existe
			File carpetaBackupsFile = new File(carpetaBackup);
			if (!carpetaBackupsFile.exists()) {
				if (carpetaBackupsFile.mkdirs()) {
					System.out.println("Carpeta de backups creada exitosamente.");
				} else {
					System.err.println("No se pudo crear la carpeta de backups.");
				}
			}

			// Mostrar mensaje de respuesta guardado
			if (prontEstadoFichero != null && alarmaConexion != null) {
				alarmaList.mensajeRespuestaGuardado(prontEstadoFichero, alarmaConexion);
			}
		} catch (IOException e) {
			// Manejar errores de entrada/salida
			e.printStackTrace();
		}
	}

	private static StringBuilder actualizarClave(StringBuilder content, String clave, String valor) {
	    String claveBuscada1 = clave + ":";
	    String claveBuscada2 = clave + ": ";
	    int startIndex1 = content.indexOf(claveBuscada1);
	    int startIndex2 = content.indexOf(claveBuscada2);

	    // Buscar la primera ocurrencia válida
	    int startIndex = -1;
	    if (startIndex1 != -1) {
	        startIndex = startIndex1;
	    } else if (startIndex2 != -1) {
	        startIndex = startIndex2;
	    }

	    if (startIndex != -1) {
	        int endIndex = content.indexOf(System.lineSeparator(), startIndex);
	        content.replace(startIndex + clave.length() + 1, endIndex, " " + valor);
	    } else {
	        // Si la clave no se encuentra, agregarla al final
	        content.append(claveBuscada1).append(valor).append(System.lineSeparator());
	    }

	    return content;
	}

	/**
	 * Método que crea la estructura de carpetas y archivos necesarios para la
	 * librería.
	 */
	public static void crearEstructura() {
		Runnable estructuraRunnable = new Runnable() {
			@Override
			public void run() {
				// Verificar y crear la carpeta "libreria" si no existe
				File carpetaLibreriaFile = new File(carpetaLibreria);
				if (!carpetaLibreriaFile.exists()) {
					carpetaLibreriaFile.mkdir();
					carpetaLibreriaFile.setWritable(true);
				}

				// Verificar y crear los archivos de configuración si no existen
				String archivoConfiguracionLocal = carpetaLibreria + File.separator + "configuracion_local.conf";

				File archivoConfiguracionLocalFile = new File(archivoConfiguracionLocal);

				if (!archivoConfiguracionLocalFile.exists()) {
					try {
						archivoConfiguracionLocalFile.createNewFile();

						// Escribir líneas en el archivo de configuración local
						FileWriter fileWriterLocal = new FileWriter(archivoConfiguracionLocalFile);
						BufferedWriter bufferedWriterLocal = new BufferedWriter(fileWriterLocal);
						bufferedWriterLocal.write("###############################");
						bufferedWriterLocal.newLine();
						bufferedWriterLocal.write("Fichero de configuracion local de la libreria");
						bufferedWriterLocal.newLine();
						bufferedWriterLocal.write("###############################");
						bufferedWriterLocal.newLine();
						bufferedWriterLocal.write("Database:");
						bufferedWriterLocal.close();
					} catch (IOException e) {
						Utilidades.manejarExcepcion(e);
					}
				} else {
					verificarEstructura(archivoConfiguracionLocal);

				}
			}
		};

		Thread estructuraThread = new Thread(estructuraRunnable);
		estructuraThread.start();
	}

	public static void verificarEstructura(String rutaArchivo) {
		// Mapa para almacenar las claves y sus valores existentes
		Map<String, String> valoresClaves = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
			String linea;

			// Leer el archivo y almacenar los valores de las claves existentes
			while ((linea = br.readLine()) != null) {
				String[] partes = linea.split(":", 2);
				String clave = partes[0].trim();
				String valor = partes.length > 1 ? partes[1].trim() : ""; // Si no hay valor, se establece como cadena
																			// vacía
				valoresClaves.put(clave, valor);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Reescribir el archivo con la estructura y los valores copiados
		try {
			reescribirArchivo(rutaArchivo, valoresClaves);

			System.out.println("Archivo de configuración recreado correctamente.");
		} catch (IOException e) {
			System.err.println("Error al recrear el archivo de configuración: " + e.getMessage());
		}
	}

	public static void reescribirArchivo(String rutaArchivo, Map<String, String> valoresClaves) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
			// Escribir la estructura en el archivo
			bw.write("###############################");
			bw.newLine();
			bw.write("Fichero de configuracion local de la libreria");
			bw.newLine();
			bw.write("###############################");
			bw.newLine();
			bw.write("Database: " + valoresClaves.getOrDefault("Database", ""));
			bw.newLine();
		}
	}

	/**
	 * Valida los datos de conexión a la base de datos MySQL.
	 *
	 * @param usuario  El nombre de usuario para la conexión.
	 * @param password La contraseña para la conexión.
	 * @param puerto   El puerto para la conexión.
	 * @param hosting  El host para la conexión.
	 * @return true si la validación fue exitosa, false si no lo fue.
	 */
	public static boolean validarDatosConexion() {

		String nombreDataBase = datosEnvioFichero();

		String url = construirURL(nombreDataBase);
		try (Connection connection = DriverManager.getConnection(url)) {
			return true; // La conexión se estableció correctamente
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);

		}

		return false; // La conexión no se pudo establecer
	}

	public static String datosEnvioFichero() {
		Map<String, String> datosConfiguracion = devolverDatosConfig();

		if (datosConfiguracion != null) {
			return datosConfiguracion.get("Database");
		}
		return "";
	}

	private static String construirURL(String direccionDataBase) {
		return "jdbc:sqlite:" + carpetaLibreria + File.separator + direccionDataBase;
	}

}
