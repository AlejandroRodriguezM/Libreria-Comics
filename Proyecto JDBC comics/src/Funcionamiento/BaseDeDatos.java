package Funcionamiento;

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

public class BaseDeDatos extends Excel{

	private Libreria libreria = new Libreria();
	private Connection conn = DBManager.conexion();

	public boolean importarCSV(File fichero)
	{
		String sql = "INSERT INTO comicsbbdd(ID,nomComic,numComic,nomVariante,Firma,nomEditorial,Formato,Procedencia,anioPubli,nomGuionista,nomDibujante,estado) values (?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			int batchSize = 20;
			BufferedReader lineReader = new BufferedReader(new FileReader(fichero));
			String lineText = null;

			int count = 0;

			lineReader.readLine(); // skip header line

			while ((lineText = lineReader.readLine()) != null) {
				String[] data = lineText.split(";");
				String id = data[0];
				String nombre = data[1];
				String numero = data[2];
				String variante = data[3];
				String firma =data[4];
				String editorial =data[5];
				String formato =data[6];
				String procedencia =data[7];
				String fecha =data[8];
				String guionista =data[9];
				String dibujante =data[10];
				String estado =data[11];

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

		}
		catch (Exception exception){
			exception.printStackTrace();
		}
		return false;
	}

	public boolean crearExcel(File fichero) throws SQLException, IOException {

		FileOutputStream outputStream;
		Cell celda;
		Row fila;
		Sheet hoja;
		Workbook libro;
		String encabezado;

		String[] encabezados = { "ID", "nomComic", "numComic", "nomVariante", "Firma", "nomEditorial", "Formato",
				"Procedencia", "anioPubli", "nomGuionista", "nomDibujante", "estado" };
		int indiceFila = 0;

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
			fila.createCell(0).setCellValue(comic.getID());
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
			System.out.println(ex);
		} catch (IOException ex) {
			System.out.println("Error de IOException");
		}
		return false;
	}

	public void createCSV(File fichero) throws IOException
	{
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

			FileOutputStream fos = new FileOutputStream(fichero.getAbsolutePath().substring(0,fichero.getAbsolutePath().lastIndexOf(".")) + ".csv");
			fos.write(data.toString().getBytes());
			fos.close();
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
