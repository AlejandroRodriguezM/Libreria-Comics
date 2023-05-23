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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.stage.DirectoryChooser;

/**
 * Esta clase sirve para crear tanto los ficheros Excel como los ficheros CSV,
 * la exportacion de estos o la importacion
 *
 * @author Alejandro Rodriguez
 */
public class FuncionesExcel {

	private static DBLibreriaManager libreria = new DBLibreriaManager();
	private static Connection conn = DBManager.conexion();
	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager db = null;
	private static int ID = 0;
	
	private static Utilidades utilidad = new Utilidades();

	/**
	 * Funcion que permite importar ficheros CSV a la base de datos.
	 *
	 * @param fichero
	 * @return
	 * @throws SQLException
	 */
	public boolean importarCSV(File fichero) {
		String sql = "INSERT INTO comicsbbdd(ID,nomComic,numComic,nomVariante,Firma,nomEditorial,Formato,Procedencia,fecha_publicacion,nomGuionista,nomDibujante,puntuacion,portada,estado)"
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		if (comprobarCSV(fichero, sql)) // Llamada a funcion, en caso de devolver true, devolvera un true
		{
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Funcion que permite crear tanto un fichero XLSX cini un fichero CSV
	 *
	 * @param fichero
	 * @return
	 */
	public boolean crearExcel(File fichero) {

		FileOutputStream outputStream;
		Cell celda;
		Row fila;
		Sheet hoja;
		Workbook libro;
		String encabezado;
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + utilidad.obtenerDatoDespuesDeDosPuntos("Database") + File.separator +  "portadas" + File.separator;
		String[] encabezados = { "ID", "nomComic", "numComic", "nomVariante", "Firma", "nomEditorial", "Formato",
				"Procedencia", "fecha_publicacion", "nomGuionista", "nomDibujante", "puntuacion", "portada", "estado" };
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
				fila.createCell(2).setCellValue(comic.getNumero());
				fila.createCell(3).setCellValue(comic.getVariante());
				fila.createCell(4).setCellValue(comic.getFirma());
				fila.createCell(5).setCellValue(comic.getEditorial());
				fila.createCell(6).setCellValue(comic.getFormato());
				fila.createCell(7).setCellValue(comic.getProcedencia());
				fila.createCell(8).setCellValue(comic.getFecha());
				fila.createCell(9).setCellValue(comic.getGuionista());
				fila.createCell(10).setCellValue(comic.getDibujante());
				fila.createCell(11).setCellValue(comic.getPuntuacion());
				String nombreImagen = sourcePath + comic.getNombre().replace(" ", "_").replace(":", "_") + "_" + comic.getNumero() + "_" + comic.getVariante().replace(" ", "_") + "_" + comic.getFecha() + ".jpg";
				fila.createCell(12).setCellValue(nombreImagen);
				fila.createCell(13).setCellValue(comic.getEstado());

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
	
	public void savedataExcel(String nombre_carpeta) {
	    FileOutputStream outputStream;
	    Cell celda;
	    Row fila;
	    Sheet hoja;
	    Workbook libro;
	    String encabezado;
	    String[] encabezados = { "ID", "nomComic", "numComic", "nomVariante", "Firma", "nomEditorial", "Formato",
	            "Procedencia", "fecha_publicacion", "nomGuionista", "nomDibujante", "puntuacion", "portada", "estado" };
	    int indiceFila = 0;
	    
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + utilidad.obtenerDatoDespuesDeDosPuntos("Database") + File.separator + "portadas" + File.separator;

		String ubicacion = userDir + File.separator + "AppData" + File.separator + "Roaming";
		String direccion = ubicacion + File.separator + "libreria" + File.separator + utilidad.obtenerDatoDespuesDeDosPuntos("Database") + File.separator + "backups"
				+ File.separator + nombre_carpeta;
	    try {
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

	        indiceFila++;
	        for (Comic comic : listaComics) {
	            fila = hoja.createRow(indiceFila);
	            fila.createCell(0).setCellValue("");
	            fila.createCell(1).setCellValue(comic.getNombre());
	            fila.createCell(2).setCellValue(comic.getNumero());
	            fila.createCell(3).setCellValue(comic.getVariante());
	            fila.createCell(4).setCellValue(comic.getFirma());
	            fila.createCell(5).setCellValue(comic.getEditorial());
	            fila.createCell(6).setCellValue(comic.getFormato());
	            fila.createCell(7).setCellValue(comic.getProcedencia());
	            fila.createCell(8).setCellValue(comic.getFecha());
	            fila.createCell(9).setCellValue(comic.getGuionista());
	            fila.createCell(10).setCellValue(comic.getDibujante());
	            fila.createCell(11).setCellValue(comic.getPuntuacion());
	            String nombreImagen = sourcePath + comic.getNombre().replace(" ", "_").replace(":", "_") + "_" + comic.getNumero() + "_" + comic.getVariante().replace(" ", "_") + "_" + comic.getFecha() + ".jpg";
	            fila.createCell(12).setCellValue(nombreImagen);
	            fila.createCell(13).setCellValue(comic.getEstado());

	            indiceFila++;
	        }

	        try {
	            outputStream = new FileOutputStream(fichero);
	            libro.write(outputStream);
	            libro.close();
	            outputStream.close();

	            // Get the backup zip file
	            String zipPath = carpetaLibreria.getAbsolutePath() + File.separator + "excel_" +  nombre_carpeta + ".zip";
	            File zipFile = new File(zipPath);
	            // Create a new zip file if it doesn't exist
	            if (!zipFile.exists()) {
	                if (!zipFile.createNewFile()) {
	                    throw new IOException("Failed to create backup zip file.");
	                }
	            }

	            // Add the Excel file to the zip
	            addFileToZip(fichero, "BaseDatos.xlsx", zipFile);
	            fichero.delete();

	        } catch (FileNotFoundException ex) {
	            nav.alertaException(ex.toString());
	        } catch (IOException ex) {
	            nav.alertaException(ex.toString());
	        }
	    } catch (IOException e) {
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

		try {
			BufferedReader lineReader = new BufferedReader(new FileReader(fichero));
			lecturaCSV(sql, lineReader);
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
	 * Funcion que carga la imagen a la hora de importar el csv
	 *
	 * @return
	 * @throws IOException 
	 */
//	public void subirImagenes(File directorio, Comic datos) throws IOException {
//	    ID++;
//	    File portada = new File(directorio.toString() + "/" + ID + ".jpg");
//	    try {
//	        if (directorio.exists()) {
//	            if (portada.exists()) {
//	                String userDir = System.getProperty("user.home");
//	                String documentsPath = userDir + File.separator + "Documents";
//	                String defaultImagePath = documentsPath + File.separator + "libreria_comics" + File.separator + "portadas" + File.separator;
//
//	                File carpetaDestinoFile = new File(defaultImagePath);
//	                if (!carpetaDestinoFile.exists() && !carpetaDestinoFile.mkdirs()) {
//	                    throw new IOException("No se pudo crear la carpeta de destino.");
//	                }
//					
//	                String nuevo_nombre = defaultImagePath + utilidad.crearNuevoNombre(datos);
//
//	                File copia_imagen = new File(nuevo_nombre);
//	                Files.copy(portada.toPath(), copia_imagen.toPath(), StandardCopyOption.REPLACE_EXISTING);
//	            } else {
//	                // Manejo si no existe la imagen
//	                System.out.println("La imagen no existe en el directorio especificado.");
//	            }
//	        }
//	    } catch (FileNotFoundException e) {
//	        String error = "ERROR. Ha cancelado la subida de imágenes de portada. Se van a subir imágenes predeterminadas.";
//	        nav.alertaException(error);
//	    }
//	}

	/**
	 * Funcion que permite leer un fichero CSV
	 *
	 * @param sql
	 * @param lineReader
	 */
	public void lecturaCSV(String sql, BufferedReader lineReader) {
//	    File directorio = carpetaPortadas();
	    String puntuacion;
	    String procedencia;
	    db = new DBLibreriaManager();
	    int batchSize = 20;
		utilidad = new Utilidades();
	    String lineText = null;

	    try {
	        PreparedStatement statement = conn.prepareStatement(sql);
	        int count = 0;
	        int nuevoID = db.countRows();
	        lineReader.readLine();

	        // Se leerán los datos hasta que no existan más datos
	        while ((lineText = lineReader.readLine()) != null) {
	            String[] data = lineText.split(";");
	            String id = Integer.toString(nuevoID);
	            String nombre = data[1];
	            String numero = data[2];
	            String variante = data[3];
	            String firma = data[4];
	            String editorial = data[5];
	            String formato = data[6];
	            if (data[7].toLowerCase().contains("españa")) {
	                procedencia = data[7].toLowerCase().replace("españa", "Spain");
	            } else {
	                procedencia = data[7];
	            }

	            // Conversión de fecha al formato correcto
//	            String fecha = convertirFecha(data[8]);
	            String fecha = data[8];
	            String guionista = data[9];
	            String dibujante = data[10];
	            if (data[11].length() != 0) {
	                puntuacion = data[11];
	            } else {
	                puntuacion = "Sin puntuacion";
	            }
	            String portada = data[12];
	            String estado = data[13];
	            
//				Comic comic = new Comic(id, nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
//						guionista, dibujante, estado, puntuacion, portada);
				
//				String nuevo_nombre = utilidad.obtenerNombreCompleto(comic);
	            
	            statement.setString(1, id);
	            statement.setString(2, nombre);
	            statement.setString(3, numero);
	            statement.setString(4, variante);
	            statement.setString(5, firma);
	            statement.setString(6, editorial);
	            statement.setString(7, formato);
	            statement.setString(8, procedencia);
	            statement.setString(9, fecha);
	            statement.setString(10, guionista);
	            statement.setString(11, dibujante);
	            statement.setString(12, puntuacion);
	            statement.setString(13, portada);
	            statement.setString(14, estado);

	            statement.addBatch();

	            if (count % batchSize == 0) {
	                statement.executeBatch();
	            }

				
//				subirImagenes(directorio, comic);
	        }
	        lineReader.close();
	        statement.executeBatch();
	    } catch (SQLException e) {
	        nav.alertaException(e.toString());
	    } catch (IOException e) {
	        nav.alertaException(e.toString());
	    }
	}

	private String convertirFecha(String fecha) {
	    try {
	        SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
	        SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy/MM/dd");
	        Date fechaDate = formatoEntrada.parse(fecha);
	        return formatoSalida.format(fechaDate);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
