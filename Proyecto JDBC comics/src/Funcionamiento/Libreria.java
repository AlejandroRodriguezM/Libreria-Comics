package Funcionamiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Libreria extends Comic {

	public static List<Comic> listaComics = new ArrayList<>();
	public static List<Comic> listaPosesion = new ArrayList<>();
	public static List<Comic> listaCompleta = new ArrayList<>();
	public static List<Comic> filtroComics = new ArrayList<>();

	private static Connection conn = DBManager.conexion();

	/**
	 * Devuelve todos los datos de la base de datos.
	 *
	 * @return
	 * @throws SQLException
	 */
	public Comic[] verLibreria() throws SQLException {

		String sentenciaSql = "SELECT * from comicsbbdd where estado = 'En posesion'";

		Comic comic[] = null;

		ordenarBBDD();
		reiniciarBBDD();

		ResultSet rs = DBManager.getComic(sentenciaSql);

		listaPosesion = listaDatos(rs);

		comic = new Comic[listaPosesion.size()];
		comic = listaPosesion.toArray(comic);
		return comic;
	}

	/**
	 * Devuelve todos los datos de la base de datos.
	 *
	 * @return
	 * @throws SQLException
	 */
	public Comic[] verLibreriaCompleta() throws SQLException {

		String sentenciaSql = "SELECT * from comicsbbdd";

		Comic comic[] = null;

		ordenarBBDD();
		reiniciarBBDD();

		ResultSet rs = DBManager.getComic(sentenciaSql);

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
	 * @throws SQLException
	 */
	public Comic[] filtadroBBDD(Comic datos) throws SQLException {

		reiniciarBBDD();
		ordenarBBDD();

		Comic comic[] = null;

		ArrayList<String> strFilter = new ArrayList<>();

		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		if (datos.ID.length() != 0 && ID != null) {
			sql.append(connector).append("ID = ?");
			connector = " AND ";
			strFilter.add(datos.ID);
		}
		if (datos.nombre.length() != 0) {
			sql.append(connector).append("nomComic = ?");
			connector = " AND ";
			strFilter.add(datos.nombre);
		}
		if (datos.numero.length() != 0) {
			sql.append(connector).append("numComic = ?");
			connector = " AND ";
			strFilter.add(datos.numero);
		}
		if (datos.variante.length() != 0) {
			sql.append(connector).append("nomVariante = ?");
			connector = " AND ";
			strFilter.add(datos.variante);
		}
		if (datos.firma.length() != 0) {
			sql.append(connector).append("firma = ?");
			connector = " AND ";
			strFilter.add(datos.firma);
		}
		if (datos.editorial.length() != 0) {
			sql.append(connector).append("nomEditorial = ?");
			connector = " AND ";
			strFilter.add(datos.editorial);
		}
		if (datos.formato.length() != 0) {
			sql.append(connector).append("formato = ?");
			connector = " AND ";
			strFilter.add(datos.formato);
		}
		if (datos.procedencia.length() != 0) {
			sql.append(connector).append("procedencia = ?");
			connector = " AND ";
			strFilter.add(datos.procedencia);
		}
		if (datos.fecha.length() != 0) {
			sql.append(connector).append("anioPubli = ?");
			connector = " AND ";
			strFilter.add(datos.fecha);
		}
		if (datos.guionista.length() != 0) {
			sql.append(connector).append("nomGuionista = ?");
			connector = " AND ";
			strFilter.add(datos.guionista);
		}
		if (datos.dibujante.length() != 0) {
			sql.append(connector).append("nomDibujante = ?");
//			connector = " AND ";
			strFilter.add(datos.dibujante);
		}
		Collections.sort(strFilter);

		try {
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < strFilter.size();) {
				ps.setString(i, strFilter.get(i - 1));
			}
			ResultSet rs = ps.executeQuery();

			rs.next();
			filtroComics = listaDatos(rs);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		comic = new Comic[filtroComics.size()];
		comic = filtroComics.toArray(comic);
		return comic;
	}

	public List<Comic> listaDatos(ResultSet rs) throws SQLException {
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

			listaComics.add(new Comic(this.ID, this.nombre, this.numero, this.variante, this.firma, this.editorial,
					this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante, this.estado));

		} while (rs.next());
		return listaComics;
	}

	/**
	 * Permite reiniciar la pantalla donde se muestran los datos
	 */
	public void reiniciarBBDD() {
		filtroComics.clear();
		listaPosesion.clear();
	}

	/**
	 *
	 * @throws SQLException
	 */
	public static void ordenarBBDD() throws SQLException {
		String sql = "ALTER TABLE comicsbbdd ORDER BY nomComic;";
		Statement st = conn.createStatement();
		st.execute(sql);
	}

}
