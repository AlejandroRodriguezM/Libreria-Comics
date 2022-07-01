package Funcionamiento;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Añadir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase permite acceder a la base de datos introduciendo los diferentes datos que nos pide.
 *
 *  Version 2.5
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

	private String id;
	private String nombre;
	private String numero;
	private String variante;
	private String firma;
	private String editorial;
	private String formato;
	private String procedencia;
	private String fecha;
	private String guionista;
	private String dibujante;
	private String estado;

	private Libreria libreria = new Libreria();
	private Comic comic = new Comic();

	public Excel(String id, String nombre, String numero, String variante, String firma, String editorial,
			String formato, String procedencia, String fecha, String guionista, String dibujante, String estado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.numero = numero;
		this.variante = variante;
		this.firma = firma;
		this.editorial = editorial;
		this.formato = formato;
		this.procedencia = procedencia;
		this.fecha = fecha;
		this.guionista = guionista;
		this.dibujante = dibujante;
		this.estado = estado;
	}

	public Excel() {
		super();
		this.id = "";
		this.nombre = "";
		this.numero = "";
		this.variante = "";
		this.firma = "";
		this.editorial = "";
		this.formato = "";
		this.procedencia = "";
		this.fecha = "";
		this.guionista = "";
		this.dibujante = "";
		this.estado = "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getVariante() {
		return variante;
	}

	public void setVariante(String variante) {
		this.variante = variante;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public String getEditorial() {
		return editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getProcedencia() {
		return procedencia;
	}

	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getGuionista() {
		return guionista;
	}

	public void setGuionista(String guionista) {
		this.guionista = guionista;
	}

	public String getDibujante() {
		return dibujante;
	}

	public void setDibujante(String dibujante) {
		this.dibujante = dibujante;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public boolean crearExcel(File fichero) throws SQLException, IOException {

		FileOutputStream outputStream;
		Cell celda;
		Row fila;
		Sheet hoja;
		Workbook libro;
		int indiceFila = 0;
		
		fichero.createNewFile();
		
		libreria.filtadroBBDD(comic);
		List<Comic> listaComics = Libreria.filtroComics;

		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Base de datos comics");

		String[] encabezados = { "ID", "nomComic", "numComic", "nomVariante", "Firma", "nomEditor", "Formato",
				"Procedencia", "anioPubli", "nomGuionista", "nomDibujante", "estado" };
		

		fila = hoja.createRow(indiceFila);
		for (int i = 0; i < encabezados.length; i++) {
			String encabezado = encabezados[i];
			celda = fila.createCell(i);
			celda.setCellValue(encabezado);
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
			return true;
		} catch (FileNotFoundException ex) {
			System.out.println(ex);
		} catch (IOException ex) {
			System.out.println("Error de IOException");
		}
		return false;
	}

}