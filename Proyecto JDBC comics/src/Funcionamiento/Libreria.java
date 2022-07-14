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
 *  Esta clase permite realizar operaciones con la libreria de comics de la base de datos.
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Libreria extends Comic {

	public static List<Comic> listaComics = new ArrayList<>();
	public static List<Comic> listaPosesion = new ArrayList<>();
	public static List<Comic> listaCompleta = new ArrayList<>();
	public static List<Comic> filtroComics = new ArrayList<>();

	private Connection conn = ConexionBBDD.conexion();
	private NavegacionVentanas nav = new NavegacionVentanas();

	/**
	 * Devuelve todos los datos de la base de datos.
	 *
	 * @return
	 */
	public Comic[] verLibreria() {

		String sentenciaSql = "SELECT * from comicsbbdd where estado = 'En posesion'";

		Comic comic[] = null;

		ordenarBBDD();
		reiniciarBBDD();

		ResultSet rs;

		rs = ConexionBBDD.getComic(sentenciaSql);
		listaPosesion = listaDatos(rs);

		comic = new Comic[listaPosesion.size()];
		comic = listaPosesion.toArray(comic);

		return comic;
	}

	/**
	 * Devuelve todos los datos de la base de datos.
	 *
	 * @return
	 */
	public Comic[] verLibreriaCompleta() {

		String sentenciaSql = "SELECT * from comicsbbdd";

		Comic comic[] = null;

		ordenarBBDD();
		reiniciarBBDD();

		ResultSet rs;
		rs = ConexionBBDD.getComic(sentenciaSql);
		listaCompleta = listaDatos(rs);
		comic = new Comic[listaCompleta.size()];
		comic = listaCompleta.toArray(comic);
		return comic;
	}

	/**
	 * Devuelve datos de la base de datos segun el parametro.
	 *
	 * @param datos
	 * @return
	 */
	public Comic[] filtadroBBDD(Comic datos) {

		reiniciarBBDD();
		ordenarBBDD();

		Comic comic[] = null;

		String sql = datosConcatenados(datos);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			rs.next();
			filtroComics = listaDatos(rs);

		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}

		comic = new Comic[filtroComics.size()];
		comic = filtroComics.toArray(comic);
		return comic;
	}

	/**
	 * Funcion que segun los datos introducir mediante parametros, concatenara las siguientes cadenas de texto. Sirve para hacer busqueda en una base de datos
	 * @param datos
	 * @return
	 */
	public String datosConcatenados(Comic comic) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		if (comic.getID().length() != 0) {

			sql.append(connector).append("ID = " + comic.getID());
			connector = " AND ";
		}
		if (comic.getNombre().length() != 0) {

			sql.append(connector).append("nomComic like'%" + comic.getNombre() + "%'");
			connector = " AND ";
		}
		if (comic.getNumero().length() != 0) {
			sql.append(connector).append("numComic = " + comic.getNumero());
			connector = " AND ";
		}
		if (comic.getVariante().length() != 0) {
			sql.append(connector).append("nomVariante like'%" + comic.getVariante() + "%'");
			connector = " AND ";
		}
		if (comic.getFirma().length() != 0) {
			sql.append(connector).append("firma like'%" + comic.getFirma() + "%'");
			connector = " AND ";
		}
		if (comic.getEditorial().length() != 0) {
			sql.append(connector).append("nomEditorial like'%" + comic.getEditorial() + "%'");
			connector = " AND ";
		}
		if (comic.getFormato().length() != 0) {
			sql.append(connector).append("formato like'%" + comic.getFormato() + "%'");
			connector = " AND ";
		}
		if (comic.getProcedencia().length() != 0) {
			sql.append(connector).append("procedencia like'%" + comic.getProcedencia() + "%'");
			connector = " AND ";
		}
		if (comic.getFecha().length() != 0) {
			sql.append(connector).append("anioPubli like'%" + comic.getFecha() + "%'");
			connector = " AND ";
		}
		if (comic.getGuionista().length() != 0) {
			sql.append(connector).append("nomGuionista like'%" + comic.getGuionista() + "%'");
			connector = " AND ";
		}
		if (comic.getDibujante().length() != 0) {
			sql.append(connector).append("nomDibujante like'%" + comic.getDibujante() + "%'");
			connector = " AND ";
		}

		return sql.toString();
	}

	/**
	 * Funcion que devuelve un comic cuya ID este como parametro de busqueda
	 * @param id
	 * @return
	 */
	public Comic comicDatos(String id) {
		Comic comic = new Comic();

		String sentenciaSQL = "select * from comicsbbdd where ID = " + id;

		ResultSet rs;

		rs = ConexionBBDD.getComic(sentenciaSQL);
		comic = datosIndividual(rs);

		return comic;
	}

	/**
	 *  Devuelve una lista con todos los datos de los comics de la base de datos
	 * @param rs
	 * @return
	 */
	public List<Comic> listaDatos(ResultSet rs) {

		try {
			if (rs != null) {
				do {

					this.ID = rs.getString("ID");
					this.nombre = rs.getString("nomComic");
					this.numero = rs.getString("numComic");
					this.variante = rs.getString("nomVariante");
					this.firma = rs.getString("firma");
					this.editorial = rs.getString("nomEditorial");
					this.formato = rs.getString("formato");
					this.procedencia = rs.getString("procedencia");
					this.fecha = rs.getString("anioPubli");
					this.guionista = rs.getString("nomGuionista");
					this.dibujante = rs.getString("nomDibujante");
					this.estado = rs.getString("estado");

					listaComics.add(new Comic(this.ID, this.nombre, this.numero, this.variante, this.firma,
							this.editorial, this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante,
							this.estado));

				} while (rs.next());
			}
		} catch (SQLException e) {
			nav.alertaException("Datos introducidos incorrectos.");
		}
		return listaComics;
	}

	/**
	 * Devuelve solamente 1 comics de la base de datos.
	 * @param rs
	 * @return
	 */
	public Comic datosIndividual(ResultSet rs) {
		Comic comic = new Comic();

		try {
			if (rs != null) {
				do {

					this.ID = rs.getString("ID");
					this.nombre = rs.getString("nomComic");
					this.numero = rs.getString("numComic");
					this.variante = rs.getString("nomVariante");
					this.firma = rs.getString("firma");
					this.editorial = rs.getString("nomEditorial");
					this.formato = rs.getString("formato");
					this.procedencia = rs.getString("procedencia");
					this.fecha = rs.getString("anioPubli");
					this.guionista = rs.getString("nomGuionista");
					this.dibujante = rs.getString("nomDibujante");
					this.estado = rs.getString("estado");

					comic = new Comic(this.ID, this.nombre, this.numero, this.variante, this.firma, this.editorial,
							this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante, this.estado);

				} while (rs.next());
			}
		} catch (SQLException e) {
			nav.alertaException("Datos introducidos incorrectos.");
		}
		return comic;
	}

	/**
	 * Permite reiniciar la pantalla donde se muestran los datos
	 */
	public void reiniciarBBDD() {
		filtroComics.clear();
		listaPosesion.clear();
	}

	/**
	 * Ordena el contenido de la base de datos por nombre del comic.
	 */
	public void ordenarBBDD() {
		String sql = "ALTER TABLE comicsbbdd ORDER BY nomComic;";
		Statement st;
		try {
			st = conn.createStatement();
			st.execute(sql);
		} catch (SQLException e) {
			nav.alertaException(e.toString());
		}
	}
}
