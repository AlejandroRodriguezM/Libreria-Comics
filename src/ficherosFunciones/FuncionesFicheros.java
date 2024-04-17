package ficherosFunciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import alarmas.AlarmaList;
import funciones_auxiliares.Utilidades;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;

public class FuncionesFicheros {

	static String userHome = System.getProperty("user.home");
	static String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
	static String carpetaLibreria = ubicacion + File.separator + "libreria";

	public static Map<String, String> devolverDatosConfig() {
		Map<String, String> datosConfiguracion = new HashMap<>();

		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion_local.conf";

		try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfiguracion))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Usuario: ")) {
					datosConfiguracion.put("Usuario",
							Utilidades.defaultIfNullOrEmpty(line.substring("Usuario: ".length()), ""));
				} else if (line.startsWith("Password: ")) {
					datosConfiguracion.put("Password",
							Utilidades.defaultIfNullOrEmpty(line.substring("Password: ".length()), ""));
				} else if (line.startsWith("Puerto: ")) {
					datosConfiguracion.put("Puerto",
							Utilidades.defaultIfNullOrEmpty(line.substring("Puerto: ".length()), ""));
				} else if (line.startsWith("Database: ")) {
					datosConfiguracion.put("Database",
							Utilidades.defaultIfNullOrEmpty(line.substring("Database: ".length()), ""));
				} else if (line.startsWith("Hosting: ")) {
					datosConfiguracion.put("Hosting",
							Utilidades.defaultIfNullOrEmpty(line.substring("Hosting: ".length()), ""));
				} else if (line.startsWith("Xampp: ")) {
					datosConfiguracion.put("Xampp",
							Utilidades.defaultIfNullOrEmpty(line.substring("Xampp: ".length()), ""));
				}
			}
		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}

		return datosConfiguracion;
	}

	public static void guardarDatosBaseLocal(String[] datos, Label prontEstadoFichero, Label alarmaConexion) {
		String puertobbdd = datos[0];
		String nombreBBDD = datos[1];
		String usuario = datos[2];
		String pass = datos[3];
		String nombreHost = datos[4];
		String direccionXampp = datos[5];

		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String carpetaBackup = carpetaLibreria + File.separator + nombreBBDD + File.separator + "backups";
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

			// Actualizar las claves necesarias
			configContent = actualizarClave(configContent, "Usuario", usuario);
			configContent = actualizarClave(configContent, "Password", pass);
			configContent = actualizarClave(configContent, "Puerto", puertobbdd);
			configContent = actualizarClave(configContent, "Database", nombreBBDD);
			configContent = actualizarClave(configContent, "Hosting", nombreHost);

			System.out.println("Contenido actualizado del archivo: " + configContent.toString());

			if (!direccionXampp.isEmpty()) {
				configContent = actualizarClave(configContent, "Xampp", direccionXampp);
			}

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
		String claveBuscada = clave + ": ";
		int startIndex = content.indexOf(claveBuscada);

		if (startIndex != -1) {
			int endIndex = content.indexOf(System.lineSeparator(), startIndex);
			content.replace(startIndex + clave.length() + 1, endIndex, " " + valor);
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
				String archivoConfiguracionOnline = carpetaLibreria + File.separator + "configuracion_usuario.conf";

				File archivoConfiguracionLocalFile = new File(archivoConfiguracionLocal);
				File archivoConfiguracionOnlineFile = new File(archivoConfiguracionOnline);

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
						bufferedWriterLocal.write("Usuario:");
						bufferedWriterLocal.newLine();
						bufferedWriterLocal.write("Password:");
						bufferedWriterLocal.newLine();
						bufferedWriterLocal.write("Puerto:");
						bufferedWriterLocal.newLine();
						bufferedWriterLocal.write("Database:");
						bufferedWriterLocal.newLine();
						bufferedWriterLocal.write("Hosting: Localhost");
						bufferedWriterLocal.newLine();
						bufferedWriterLocal.write("Xampp:");
						bufferedWriterLocal.newLine();
						bufferedWriterLocal.close();
					} catch (IOException e) {
						Utilidades.manejarExcepcion(e);
					}
				} else {
					verificarEstructura(archivoConfiguracionLocal);

				}

				if (!archivoConfiguracionOnlineFile.exists()) {
					try {
						archivoConfiguracionOnlineFile.createNewFile();

						// Escribir líneas en el archivo de configuración online
						FileWriter fileWriterOnline = new FileWriter(archivoConfiguracionOnlineFile);
						BufferedWriter bufferedWriterOnline = new BufferedWriter(fileWriterOnline);
						bufferedWriterOnline.write("###############################");
						bufferedWriterOnline.newLine();
						bufferedWriterOnline.write("Usuario y contraseño del usuario");
						bufferedWriterOnline.newLine();
						bufferedWriterOnline.write("###############################");
						bufferedWriterOnline.newLine();
						bufferedWriterOnline.write("Usuario: ");
						bufferedWriterOnline.newLine();
						bufferedWriterOnline.write("Password: ");
						bufferedWriterOnline.newLine();
						bufferedWriterOnline.write("Xampp:");
						bufferedWriterOnline.newLine();
						bufferedWriterOnline.close();
					} catch (IOException e) {
						Utilidades.manejarExcepcion(e);
					}
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
			String carpetaXampp;
			// Escribir la estructura en el archivo
			bw.write("###############################");
			bw.newLine();
			bw.write("Fichero de configuracion local de la libreria");
			bw.newLine();
			bw.write("###############################");
			bw.newLine();
			bw.write("Usuario: " + valoresClaves.getOrDefault("Usuario", ""));
			bw.newLine();
			bw.write("Password: " + valoresClaves.getOrDefault("Password", ""));
			bw.newLine();
			bw.write("Puerto: " + valoresClaves.getOrDefault("Puerto", ""));
			bw.newLine();
			bw.write("Database: " + valoresClaves.getOrDefault("Database", ""));
			bw.newLine();
			bw.write("Hosting: Localhost");
			bw.newLine();
			carpetaXampp = valoresClaves.getOrDefault("Xampp", "");
			bw.write("Xampp: " + carpetaXampp);
			bw.newLine();
		}
	}

	public static void reemplazarXampp(String rutaArchivo, Map<String, String> nuevosValores) throws IOException {
		File archivo = new File(rutaArchivo);
		File temporal = new File(carpetaLibreria + File.separator + "temporal.txt");

		try (BufferedReader br = new BufferedReader(new FileReader(archivo));
				BufferedWriter bw = new BufferedWriter(new FileWriter(temporal))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				// Si la línea contiene la clave a modificar (Xampp)
				if (linea.startsWith("Xampp:")) {
					// Reemplazar la ruta Xampp con el nuevo valor
					bw.write("Xampp: " + nuevosValores.get("Xampp"));
				} else {
					// Escribir la línea original sin cambios
					bw.write(linea);
				}
				bw.newLine();
			}
		}

		// Eliminar el archivo original
		archivo.delete();

		// Cambiar el nombre del archivo temporal al original
		temporal.renameTo(archivo);
	}

	private static String seleccionarDirectorioXampp() {
		CompletableFuture<String> future = new CompletableFuture<>();

		Platform.runLater(() -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("Seleccionar carpeta Xampp");
			directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

			File directory = directoryChooser.showDialog(null);
			if (directory != null) {
				File xamppControl = new File(directory, "xampp-control.exe");
				File xamppStart = new File(directory, "xampp_start.exe");

				if (xamppControl.exists() && xamppStart.exists()) {
					future.complete(directory.getAbsolutePath());
				} else {
					future.complete(null);
				}
			} else {
				future.complete(null);
			}
		});

		try {
			return future.get(); // Espera a que el futuro se complete y luego devuelve el resultado
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void verificarYReemplazarRutaXampp(String rutaXampp) {
		if (!comprobarInstalacion(rutaXampp)) {
			CompletableFuture<String> future = CompletableFuture
					.supplyAsync(FuncionesFicheros::seleccionarDirectorioXampp);

			future.thenAccept(nuevaRuta -> {
				if (nuevaRuta != null && !nuevaRuta.isEmpty()) {
					Map<String, String> nuevosValores = new HashMap<>();
					nuevosValores.put("Xampp", nuevaRuta);
					try {
						String archivoConfiguracionLocal = carpetaLibreria + File.separator
								+ "configuracion_local.conf";
						reemplazarXampp(archivoConfiguracionLocal, nuevosValores);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public static boolean comprobarInstalacion(String rutaXampp) {
		File xamppControl = new File(rutaXampp, "xampp-control.exe");
		File xamppStart = new File(rutaXampp, "xampp_start.exe");

		if (xamppControl.exists() && xamppStart.exists()) {
			return true;
		}
		return false;
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

		String[] datosFichero = datosEnvioFichero();

		String puerto = datosFichero[0];
		String usuario = datosFichero[2];
		String password = datosFichero[3];
		String hosting = datosFichero[4];

		String url = construirURL(hosting, puerto);
		if (Utilidades.isMySQLServiceRunning(hosting, puerto)) {
			try (Connection connection = DriverManager.getConnection(url, usuario, password)) {
				return true; // La conexión se estableció correctamente
			} catch (SQLException e) {
				AlarmaList.manejarErrorConexion(
						"ERROR. Revisa tu configuracion de XAMPP. Es posible que la contraseña u usuario sea incorrecto en la configuracion del fichero o de XAMPP",
						null);
				Utilidades.manejarExcepcion(e);

			}
		}

		return false; // La conexión no se pudo establecer
	}

	public static String[] datosEnvioFichero() {
		Map<String, String> datosConfiguracion = devolverDatosConfig();

		String puertoTexto = datosConfiguracion.get("Puerto");
		String databaseTexto = datosConfiguracion.get("Database");
		String usuarioTexto = datosConfiguracion.get("Usuario");
		String passwordTexto = datosConfiguracion.get("Password");
		String hostingTexto = datosConfiguracion.get("Hosting");
		String xamppTexto = datosConfiguracion.get("Xampp");

		String[] datosConfiguracionArray = { puertoTexto, databaseTexto, usuarioTexto, passwordTexto, hostingTexto,
				xamppTexto };

		return datosConfiguracionArray;
	}

	private static String construirURL(String hosting, String puerto) {
		return "jdbc:mysql://" + hosting + ":" + puerto + "/";
	}

	/**
	 * Obtiene opciones para el ComboBox de nombreBBDD basado en la configuración.
	 *
	 * @param usuario  El nombre de usuario para la conexión.
	 * @param password La contraseña para la conexión.
	 * @param puerto   El puerto para la conexión.
	 * @param hosting  El host para la conexión.
	 * @return Lista de opciones para el ComboBox de nombreBBDD.
	 */
	public static List<String> obtenerOpcionesNombreBBDD(Map<String, String> datosConfiguracion) {
		List<String> opciones = new ArrayList<>();

		String usuario = datosConfiguracion.get("Usuario");
		String password = datosConfiguracion.get("Password");
		String puerto = datosConfiguracion.get("Puerto");
		String hosting = datosConfiguracion.get("Hosting");

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + hosting + ":" + puerto + "/",
				usuario, password);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SHOW DATABASES");) {

			// Agregar los nombres de las bases de datos a la lista de opciones
			while (resultSet.next()) {
				String nombreBD = resultSet.getString(1);
				String urlBD = "jdbc:mysql://" + hosting + ":" + puerto + "/" + nombreBD;
				try (Connection dbConnection = DriverManager.getConnection(urlBD, usuario, password);
						Statement dbStatement = dbConnection.createStatement();
						ResultSet dbResultSet = dbStatement.executeQuery("SHOW TABLES LIKE 'comicsbbdd'");) {
					if (dbResultSet.next()) {
						opciones.add(nombreBD);
					}
				}
			}
		} catch (SQLException e) {
//			Utilidades.manejarExcepcion(e);
		}
		return opciones;
	}

}
