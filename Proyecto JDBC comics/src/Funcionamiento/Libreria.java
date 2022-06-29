package Funcionamiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Libreria extends Comic{
	
	private static List<Comic> listComics = new ArrayList<>();
	private static List<Comic> FiltrolistComics = new ArrayList<>();

	private static Connection conn = DBManager.conexion();
	
	/**
	 * Devuelve todos los datos de la base de datos.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Comic[] verTodo() throws SQLException {

		String sentenciaSql = "SELECT * from Comicsbbdd where estado = 'En posesion'";

		Comic Comic[] = null;

		ordenarBBDD();
		reiniciarBBDD();

		ResultSet rs = DBManager.getComic(sentenciaSql);

		try {
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
				listComics.add(new Comic(this.ID, this.nombre, this.numero, this.variante, this.firma, this.editorial,
						this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante));
			} while (rs.next());

		} catch (Exception ex) {
			System.out.println(ex);
			// itself.
		}

		Comic = new Comic[listComics.size()];
		Comic = listComics.toArray(Comic);
		return Comic;
	}
	
	/**
	 * Devuelve datos de la base de datos segun el parametro.
	 * 
	 * @param nombreC
	 * @param numeroC
	 * @param varianteC
	 * @param firmaC
	 * @param editorialC
	 * @param formatoC
	 * @param procedenciaC
	 * @param fechaC
	 * @param guionistaC
	 * @param dibujanteC
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
		sql.append("SELECT * FROM comicsbbdd");

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
			connector = " AND ";
			strFilter.add(datos.dibujante);
		}
		Collections.sort(strFilter);

		try {
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < strFilter.size();) {
				Collections.sort(strFilter);
				ps.setString(++i, strFilter.get(i - 1));
			}
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

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
				if (rs.getString("estado").equals("En posesion")) {
					FiltrolistComics.add(
							new Comic(this.ID, this.nombre, this.numero, this.variante, this.firma, this.editorial,
									this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante));
				}
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		comic = new Comic[FiltrolistComics.size()];
		comic = FiltrolistComics.toArray(comic);
		return comic;
	}

	/**
	 * Permite reiniciar la pantalla donde se muestran los datos
	 */
	public  void reiniciarBBDD() {
		FiltrolistComics.clear();
		listComics.clear();
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public static void ordenarBBDD() throws SQLException {
		String sql = "ALTER TABLE Comicsbbdd ORDER BY nomComic;";
		Statement st = conn.createStatement();
		st.execute(sql);
	}

}
