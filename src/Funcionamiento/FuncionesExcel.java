package Funcionamiento;

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
 *  Version 5.5.0.1
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.awt.Desktop;

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
 *  Esta clase permite hacer las funciones respectivas del Excel y el CSV
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

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
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Controladores.CargaComicsController;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Esta clase sirve para crear tanto los ficheros Excel como los ficheros CSV,
 * la exportacion de estos o la importacion
 *
 * @author Alejandro Rodriguez
 */
public class FuncionesExcel {

	private static Connection conn = null;
	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private CargaComicsController cargaComicsController; // Declare the instance

	public void savedataExcel(String nombre_carpeta) {

		libreria = new DBLibreriaManager();
		Cell celda;
		Row fila;
		Sheet hoja;
		Workbook libro;
		String encabezado;
		String[] encabezados = { "ID", "nomComic", "caja_deposito", "precio_comic", "numComic", "nomVariante", "Firma",
				"nomEditorial", "Formato", "Procedencia", "fecha_publicacion", "nomGuionista", "nomDibujante",
				"puntuacion", "portada", "key_issue", "url_referencia", "estado" };
		int indiceFila = 0;

		String userDir = System.getProperty("user.home");

		String ubicacion = userDir + File.separator + "AppData" + File.separator + "Roaming";
		String direccion = ubicacion + File.separator + "libreria" + File.separator + DBManager.DB_NAME + File.separator
				+ "backups" + File.separator + nombre_carpeta;
		try {
			verCargaComics();
			File carpetaLibreria = new File(direccion);
			File fichero = new File(carpetaLibreria, "BaseDatos.xlsx");
			fichero.createNewFile();
			List<Comic> listaComics = libreria.libreriaCompleta();

			libro = new XSSFWorkbook();

			hoja = libro.createSheet("Base de datos comics");

			fila = hoja.createRow(indiceFila);
			for (int i = 0; i < encabezados.length; i++) {
				encabezado = encabezados[i];
				celda = fila.createCell(i);
				celda.setCellValue(encabezado);
				celda.getStringCellValue().getBytes(Charset.forName("UTF-8"));
			}

			Thread excelThread = new Thread(() -> {
				try {
					Row filaCopy = fila; // Create a copy of fila here
					int indiceFinal = indiceFila;
					indiceFinal++;
					for (Comic comic : listaComics) {
						filaCopy = hoja.createRow(indiceFinal);
						filaCopy.createCell(0).setCellValue("");
						filaCopy.createCell(1).setCellValue(comic.getNombre());
						filaCopy.createCell(2).setCellValue(comic.getNumCaja());
						filaCopy.createCell(3).setCellValue(comic.getPrecio_comic());
						filaCopy.createCell(4).setCellValue(comic.getNumero());
						filaCopy.createCell(5).setCellValue(comic.getVariante());
						filaCopy.createCell(6).setCellValue(comic.getFirma());
						filaCopy.createCell(7).setCellValue(comic.getEditorial());
						filaCopy.createCell(8).setCellValue(comic.getFormato());
						filaCopy.createCell(9).setCellValue(comic.getProcedencia());
						filaCopy.createCell(10).setCellValue(comic.getFecha());
						filaCopy.createCell(11).setCellValue(comic.getGuionista());
						filaCopy.createCell(12).setCellValue(comic.getDibujante());
						filaCopy.createCell(13).setCellValue(comic.getPuntuacion());
						filaCopy.createCell(14).setCellValue(comic.getImagen());
						filaCopy.createCell(15).setCellValue(comic.getKey_issue());
						filaCopy.createCell(16).setCellValue(comic.getUrl_referencia());
						filaCopy.createCell(17).setCellValue(comic.getEstado());

						final long finalProcessedItems = indiceFinal;
						
						Platform.runLater(() -> {
							String texto = ("Comic: " + comic.getNombre() + " - " + comic.getNumero() + " - "
									+ comic.getVariante() + "\n");
							double progress = (double) finalProcessedItems / listaComics.size();
							String porcentaje = String.format("%.2f%%", progress * 100);
							cargarDatosEnCargaComics(texto, porcentaje, progress);
						});

						indiceFinal++;
					}

					try {
						FileOutputStream outputStream = new FileOutputStream(fichero);
						libro.write(outputStream);
						libro.close();
						outputStream.close();

						String zipPath = carpetaLibreria.getAbsolutePath() + File.separator + "excel_" + nombre_carpeta
								+ ".zip";
						File zipFile = new File(zipPath);

						if (!zipFile.exists()) {
							if (!zipFile.createNewFile()) {
								throw new IOException("Failed to create backup zip file.");
							}
						}

						addFileToZip(fichero, "BaseDatos.xlsx", zipFile);
						fichero.delete();
					} catch (IOException ex) {
						nav.alertaException(ex.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
					nav.alertaException(e.toString());
				}
			});

			excelThread.start();
		} catch (

		IOException e) {
			nav.alertaException(e.toString());
		}
	}

	private void addFileToZip(File file, String entryName, File zipFile) throws IOException {
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
	 * Funcion que permite crear un fichero CSV
	 *
	 * @param fichero
	 */
	public void createCSV(File fichero) {

		// For storing data into CSV files
		StringBuffer data = new StringBuffer();

		try {
			// Creating input stream
			FileInputStream fis = new FileInputStream(fichero);

			Workbook workbook = new XSSFWorkbook(fis);

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

			FileOutputStream fos = new FileOutputStream(
					fichero.getAbsolutePath().substring(0, fichero.getAbsolutePath().lastIndexOf(".")) + ".csv");
			fos.write(data.toString().getBytes());
			fos.close();
			workbook.close();

		} catch (Exception e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Funcion que permite comprobar el estado del fichero CSV, si es apto, permita
	 * importarlo.
	 *
	 * @param fichero
	 * @param sql
	 * @return
	 */
	public boolean comprobarCSV(File fichero, String sql) {
		conn = DBManager.conexion();
		try {
			BufferedReader lineReader = new BufferedReader(new FileReader(fichero));
			lecturaCSVTask(sql, lineReader);
			return true;
		} catch (Exception e) {
			try {
				PreparedStatement statement1 = conn.prepareStatement("delete from comicsbbdd");
				PreparedStatement statement2 = conn.prepareStatement("alter table comicsbbdd AUTO_INCREMENT = 1;");
				statement1.close();
				statement2.close();

			} catch (SQLException e1) {
				nav.alertaException("El formato del fichero .csv no es correcto: " + e.toString());
			}
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public File carpetaPortadas() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File directorio = directoryChooser.showDialog(null);
		return directorio;
	}

	/**
	 * 
	 * @return
	 */
	public File carpetaPortadasTask() {
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
			e.printStackTrace();
		}

		return directorio[0];
	}

	/**
	 * Funcion que permite crear tanto un fichero XLSX como un fichero CSV
	 *
	 * @param fichero
	 * @return
	 * @throws SQLException
	 */
	/**
	 * Funcion que permite crear tanto un fichero XLSX como un fichero CSV
	 *
	 * @param fichero
	 * @return
	 * @throws SQLException
	 */
	public boolean crearExcel(File fichero) throws SQLException {
		libreria = new DBLibreriaManager();
		FileOutputStream outputStream;
		Cell celda;
		Row fila;
		Sheet hoja;
		Workbook libro;
		String encabezado;
		String[] encabezados = { "ID", "nomComic", "caja_deposito", "precio_comic", "numComic", "nomVariante", "Firma",
				"nomEditorial", "Formato", "Procedencia", "fecha_publicacion", "nomGuionista", "nomDibujante",
				"puntuacion", "portada", "key_issue", "url_referencia", "estado" };
		int indiceFila = 0;

		try {
			fichero.createNewFile();
			List<Comic> listaComics = libreria.libreriaCompleta();

			libro = new XSSFWorkbook();

			hoja = libro.createSheet("Base de datos comics");

			fila = hoja.createRow(indiceFila);
			for (int i = 0; i < encabezados.length; i++) {
				encabezado = encabezados[i];
				celda = fila.createCell(i);
				celda.setCellValue(encabezado);
				celda.getStringCellValue().getBytes(Charset.forName("UTF-8"));
			}

			indiceFila++;
			for (Comic comic : listaComics) {
				fila = hoja.createRow(indiceFila);
				fila.createCell(0).setCellValue("");
				fila.createCell(1).setCellValue(comic.getNombre());
				fila.createCell(2).setCellValue(comic.getNumCaja());
				fila.createCell(3).setCellValue(comic.getPrecio_comic());
				fila.createCell(4).setCellValue(comic.getNumero());
				fila.createCell(5).setCellValue(comic.getVariante());
				fila.createCell(6).setCellValue(comic.getFirma());
				fila.createCell(7).setCellValue(comic.getEditorial());
				fila.createCell(8).setCellValue(comic.getFormato());
				fila.createCell(9).setCellValue(comic.getProcedencia());
				fila.createCell(10).setCellValue(comic.getFecha());
				fila.createCell(11).setCellValue(comic.getGuionista());
				fila.createCell(12).setCellValue(comic.getDibujante());
				fila.createCell(13).setCellValue(comic.getPuntuacion());
				fila.createCell(14).setCellValue(comic.getImagen());
				fila.createCell(15).setCellValue(comic.getKey_issue());
				fila.createCell(16).setCellValue(comic.getUrl_referencia());
				fila.createCell(17).setCellValue(comic.getEstado());
				indiceFila++;
			}

			try {
				outputStream = new FileOutputStream(fichero);
				libro.write(outputStream);
				libro.close();
				outputStream.close();
				createCSV(fichero);
				libreria.saveImageFromDataBase();
				return true;
			} catch (FileNotFoundException ex) {
				nav.alertaException(ex.toString());
			} catch (IOException ex) {
				nav.alertaException(ex.toString());
			}
		} catch (IOException e) {
			nav.alertaException(e.toString());
		}
		return false;
	}

	/**
	 * Función que permite leer un fichero CSV
	 *
	 * @param sql
	 * @param lineReader
	 * @throws IOException
	 */
	public Task<Void> lecturaCSVTask(String sql, BufferedReader lineReader) {
		libreria = new DBLibreriaManager();
		new CargaComicsController();
		conn = DBManager.conexion();

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					String lineText = null;
					String userDir = System.getProperty("user.home");
					String documentsPath = userDir + File.separator + "Documents";
					String defaultImagePath = documentsPath + File.separator + "libreria_comics" + File.separator
							+ DBManager.DB_NAME + File.separator + "portadas";
					File directorio = new File("");
					CompletableFuture<Boolean> confirmacionFuture = nav.cancelar_subida_portadas();

					boolean continuarSubida = confirmacionFuture.join();
					if (continuarSubida) {
						directorio = carpetaPortadasTask();

						if (directorio == null) {
							directorio = new File(defaultImagePath + File.separator);
						}

					} else {
						directorio = new File(defaultImagePath + File.separator);
					}

					int batchSize = 20;
					Utilidades.convertirNombresCarpetas(defaultImagePath + File.separator);
					Utilidades.convertirNombresCarpetas(directorio.getAbsolutePath());
					String defaultImagePathBase = documentsPath + File.separator + "libreria_comics" + File.separator
							+ DBManager.DB_NAME;

					String logFileName = "log_"
							+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".txt";

					if (conn == null) {
						System.out.println("Error");
					}

					PreparedStatement statement = conn.prepareStatement(sql);
					int count = 0;
					int nuevoID = libreria.countRows();
					long processedItems = 0; // Processed items count

					lineReader.readLine();
					Utilidades.copyDirectory(directorio.getAbsolutePath(), defaultImagePath);

					verCargaComics();
					// Se leerán los datos hasta que no existan más datos
					while ((lineText = lineReader.readLine()) != null) {
						String[] data = lineText.split(";");
						String id = Integer.toString(nuevoID);
						String nombre = data[1];
						String numCaja = data[2];
						String precio_comic = data[3];
						String numero = data[4];
						String variante = data[5];
						String firma = data[6];
						String editorial = data[7];
						String formato = data[8];
						String procedencia = obtenerProcedencia(data[9]);
						String fecha = data[10];
						String guionista = data[11];
						String dibujante = data[12];
						String puntuacion = obtenerPuntuacion(data[12], data[13]);
						String direccion_portada = data[14];
						String nombre_portada = Utilidades.obtenerDespuesPortadas(direccion_portada);
						String nombre_modificado = Utilidades.convertirNombreArchivo(nombre_portada);
						String nombre_completo_portada = defaultImagePath + File.separator + nombre_modificado;
						String key_issue = data[15];
						key_issue = key_issue.replaceAll("\\r|\\n", "");
						String url_referencia = data[16];
						String estado = data[17];

						if (precio_comic.isEmpty()) {
							precio_comic = "0";
						}

						if (url_referencia.isEmpty()) {
							url_referencia = "Sin referencia";
						}

						if (!existeArchivo(defaultImagePath, nombre_modificado) || directorio == null) {
							copiarPortadaPredeterminada(defaultImagePath, nombre_modificado);
							generarLogFaltaPortada(defaultImagePathBase, logFileName, nombre_modificado);
						}

						statement.setString(1, id);
						statement.setString(2, nombre);
						statement.setString(3, numCaja);
						statement.setString(4, precio_comic);
						statement.setString(5, numero);
						statement.setString(6, variante);
						statement.setString(7, firma);
						statement.setString(8, editorial);
						statement.setString(9, formato);
						statement.setString(10, procedencia);
						statement.setString(11, fecha);
						statement.setString(12, guionista);
						statement.setString(13, dibujante);
						statement.setString(14, puntuacion);
						statement.setString(15, nombre_completo_portada);
						statement.setString(16, key_issue);
						statement.setString(17, url_referencia);
						statement.setString(18, estado);

						statement.addBatch();

						final long finalProcessedItems = processedItems;

						// Update UI elements using Platform.runLater
						Platform.runLater(() -> {
							String texto = "";
							if (!existeArchivo(defaultImagePath, nombre_modificado)) {
								texto = ("Comic: " + nombre + " - " + numero + " - " + variante + "\n");
							}else {
								texto = ("Comic: " + nombre + " - " + numero + " - " + variante + "\n");
							}
							
							double progress = (double) finalProcessedItems / (finalProcessedItems + 1);
							String porcentaje = String.format("%.2f%%", progress * 100);

							cargarDatosEnCargaComics(texto, porcentaje, progress);
						});

						processedItems++;

						if (count % batchSize == 0) {
							statement.executeBatch();
						}
					}
					Platform.runLater(() -> {
						cargarDatosEnCargaComics("", "100%", 100.0);
					});

					lineReader.close();
					statement.executeBatch();
					abrirArchivoRegistro(defaultImagePathBase + File.separator + logFileName);
				} catch (SQLException e) {
					e.printStackTrace();
					Platform.runLater(
							() -> nav.alertaException("Error al guardar datos en la base de datos: " + e.getMessage()));
				} catch (IOException e) {
					e.printStackTrace();
					Platform.runLater(() -> nav.alertaException("Error al leer el archivo CSV: " + e.getMessage()));
				}

				return null;
			}
		};

		return task;
	}

	// Function to open the CargaComicsController window
	private void verCargaComics() {
		Platform.runLater(() -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/PantallaCargaComics.fxml"));
				Parent root = loader.load();
				cargaComicsController = loader.getController(); // Get the instance
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setResizable(false);
				stage.setTitle("Carga de comics"); // Titulo de la aplicacion.
				stage.getIcons().add(new Image("/Icono/icon2.png"));

				// Indico que debe hacer al cerrar
				stage.setOnCloseRequest(e -> {
					cargaComicsController.closeWindow();
				});

				// Asocio el stage con el scene
				stage.setScene(scene);
				stage.show();

				// Now you can call methods on cargaComicsController
				cargarDatosEnCargaComics("", "", 0.0); // Call the data passing function

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
	}

	// Function to pass data to methods of CargaComicsController
	private void cargarDatosEnCargaComics(String nombreComic, String porcentaje, Double progreso) {
			cargaComicsController.appendTextToTextArea(nombreComic);
			cargaComicsController.updateLabel(porcentaje);
			cargaComicsController.updateProgress(progreso);
	}

	private void abrirArchivoRegistro(String filePath) {
		File file = new File(filePath);

		if (file.exists()) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				nav.alertaException(e.toString());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Función que obtiene la procedencia según el país
	 *
	 * @param pais El país de origen
	 * @return La procedencia actualizada
	 */
	private String obtenerProcedencia(String pais) {
		String procedencia;
		if (pais.toLowerCase().contains("españa")) {
			procedencia = pais.toLowerCase().replace("españa", "Spain");
		} else {
			procedencia = pais;
		}
		return procedencia;
	}

	/**
	 * Función que obtiene la puntuación del cómic
	 *
	 * @param dibujante  El nombre del dibujante
	 * @param puntuacion La puntuación actual
	 * @return La puntuación actualizada
	 */
	private String obtenerPuntuacion(String dibujante, String puntuacion) {
		if (dibujante.length() != 0) {
			return puntuacion;
		} else {
			return "Sin puntuación";
		}
	}

	/**
	 * Función que verifica si un archivo de portada existe
	 *
	 * @param defaultImagePath El directorio de las portadas
	 * @param nombreModificado El nombre del archivo modificado
	 * @return true si el archivo existe, false en caso contrario
	 */
	private boolean existeArchivo(String defaultImagePath, String nombreModificado) {
		return Files.exists(Paths.get(defaultImagePath, nombreModificado));
	}

	/**
	 * Función que copia la portada predeterminada y cambia su nombre
	 *
	 * @param defaultImagePath El directorio de las portadas
	 * @param nombreModificado El nombre del archivo modificado
	 * @throws IOException
	 */
	public void copiarPortadaPredeterminada(String defaultImagePath, String nombreModificado) throws IOException {
		File sourceFile = new File(defaultImagePath, nombreModificado);
		if (!sourceFile.exists()) {
			InputStream input = getClass().getResourceAsStream("sinPortada.jpg");

			if (input == null) {
				throw new FileNotFoundException("La imagen predeterminada no se encontró en el paquete");
			}

			File destinationFile = new File(defaultImagePath, nombreModificado);
			destinationFile.createNewFile();

			try (OutputStream output = new FileOutputStream(destinationFile)) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = input.read(buffer)) != -1) {
					output.write(buffer, 0, bytesRead);
				}
			}
		} else {
			File destinationFile = new File(defaultImagePath, nombreModificado);
			Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	/**
	 * Función que genera el log cuando falta una portada
	 *
	 * @param defaultImagePathBase El directorio base de las portadas
	 * @param logFileName          El nombre del archivo de log
	 * @param nombreModificado     El nombre del archivo modificado
	 * @throws IOException
	 */
	private void generarLogFaltaPortada(String defaultImagePathBase, String logFileName, String nombreModificado)
			throws IOException {
		String logFilePath = defaultImagePathBase + File.separator + logFileName;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
			writer.write("Falta portada: " + nombreModificado);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
