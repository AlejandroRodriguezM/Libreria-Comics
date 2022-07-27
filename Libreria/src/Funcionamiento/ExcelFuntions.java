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
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFuntions {

	private Libreria libreria = new Libreria();
	private Connection conn = ConexionBBDD.conexion();
	private NavegacionVentanas nav = new NavegacionVentanas();
	private BBDD db = new BBDD();

	/**
	 * Funcion que permite importar ficheros CSV a la base de datos.
	 * @param fichero
	 * @return
	 * @throws SQLException 
	 */
	public boolean importarCSV(File fichero) {
		String sql = "INSERT INTO comicsbbdd(ID,nomComic,numComic,nomVariante,Firma,nomEditorial,Formato,Procedencia,anioPubli,nomGuionista,nomDibujante,estado)"
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?)";

		if(lecturaCSV(fichero,sql))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	/**
	 * Funcion que permite crear tanto un fichero XLSX cini un fichero CSV
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

		String[] encabezados = { "ID", "nomComic", "numComic", "nomVariante", "Firma", "nomEditorial", "Formato",
				"Procedencia", "anioPubli", "nomGuionista", "nomDibujante", "estado" };
		int indiceFila = 0;

		try {
			fichero.createNewFile();
			libreria.verLibreriaCompleta();
			List<Comic> listaComics = Libreria.listaCompleta;

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
				fila.createCell(11).setCellValue(comic.getEstado());

				indiceFila++;
			}

			try {
				outputStream = new FileOutputStream(fichero);
				libro.write(outputStream);

				libro.close();
				outputStream.close();
				createCSV(fichero);
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
	 * Funcion que permite crear un fichero CSV
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

			
			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
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
			fos.write(65001);
			fos.write(data.toString().getBytes());
			fos.close();
			workbook.close();

		} catch (Exception e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Funcion que permite la lectura de un fichero CSV
	 * @param fichero
	 * @param sql
	 * @return
	 */
	public boolean lecturaCSV(File fichero,String sql)
	{

		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			int batchSize = 20;
			BufferedReader lineReader = new BufferedReader(new FileReader(fichero));
			String lineText = null;

			int count = 0;
			int j = db.countRows();
			lineReader.readLine();

			//Se leeran los datos hasta que no existan mas datos
			while ((lineText = lineReader.readLine()) != null) {
				String[] data = lineText.split(";");
				String id = Integer.toString(j);
				String nombre = data[1];
				String numero = data[2];
				String variante = data[3];
				String firma = data[4];
				String editorial = data[5];
				String formato = data[6];
				String procedencia = data[7];
				String fecha = data[8];
				String guionista = data[9];
				String dibujante = data[10];
				String estado = data[11];

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
				statement.setString(12, estado);

				statement.addBatch();

				if (count % batchSize == 0) {
					statement.executeBatch();
				}
			}

			lineReader.close();
			statement.executeBatch();
			return true;
			
		} catch (FileNotFoundException e) {
			nav.alertaException("Fichero no encontrado: " + e.toString());
		}
		catch (SQLException e) {
			try {
				PreparedStatement statement1 = conn.prepareStatement("delete from comicsbbdd");
				PreparedStatement statement2 = conn.prepareStatement("alter table comicsbbdd AUTO_INCREMENT = 1;");
				statement1.close();
				statement2.close();
				nav.alertaException("El formato del fichero .csv no es correcto: " + e.toString());
			} catch (SQLException e1) {
				nav.alertaException(e1.toString());
			}
		} catch (IOException e) {
			nav.alertaException("El formato del fichero .csv no es correcto: " + e.toString());
		}
		return false;
	}
}
