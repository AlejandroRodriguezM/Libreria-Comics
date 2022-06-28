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
	
	private static List<Comic> listComic = new ArrayList<>();
	private static List<Comic> FiltrolistComic = new ArrayList<>();

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
				listComic.add(new Comic(this.ID, this.nombre, this.numero, this.variante, this.firma, this.editorial,
						this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante));
			} while (rs.next());

		} catch (Exception ex) {
			System.out.println(ex);
			// itself.
		}

		Comic = new Comic[listComic.size()];
		Comic = listComic.toArray(Comic);
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
	public Comic[] filtadroBBDD(Comic comic) throws SQLException {

		reiniciarBBDD();
		ordenarBBDD();

		Comic Comic[] = null;

		ArrayList<String> strFilter = new ArrayList<>();

		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Comicsbbdd");

		if (this.ID.length() != 0 && ID != null) {
			sql.append(connector).append("ID = ?");
			connector = " AND ";
			strFilter.add(ID);
		}
		if (this.nombre.length() != 0) {
			sql.append(connector).append("nomComic = ?");
			connector = " AND ";
			strFilter.add(this.nombre);
		}
		if (this.numero.length() != 0) {
			sql.append(connector).append("numComic = ?");
			connector = " AND ";
			strFilter.add(this.numero);
		}
		if (this.variante.length() != 0) {
			sql.append(connector).append("nomVariante = ?");
			connector = " AND ";
			strFilter.add(this.variante);
		}
		if (this.firma.length() != 0) {
			sql.append(connector).append("firma = ?");
			connector = " AND ";
			strFilter.add(this.firma);
		}
		if (this.editorial.length() != 0) {
			sql.append(connector).append("nomEditorial = ?");
			connector = " AND ";
			strFilter.add(this.editorial);
		}
		if (this.formato.length() != 0) {
			sql.append(connector).append("formato = ?");
			connector = " AND ";
			strFilter.add(this.formato);
		}
		if (this.procedencia.length() != 0) {
			sql.append(connector).append("procedencia = ?");
			connector = " AND ";
			strFilter.add(this.procedencia);
		}
		if (this.fecha.length() != 0) {
			sql.append(connector).append("anioPubli = ?");
			connector = " AND ";
			strFilter.add(this.fecha);
		}
		if (this.guionista.length() != 0) {
			sql.append(connector).append("nomGuionista = ?");
			connector = " AND ";
			strFilter.add(this.guionista);
		}
		if (this.dibujante.length() != 0) {
			sql.append(connector).append("nomDibujante = ?");
			connector = " AND ";
			strFilter.add(this.dibujante);
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
					FiltrolistComic.add(
							new Comic(this.ID, this.nombre, this.numero, this.variante, this.firma, this.editorial,
									this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante));
				}
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		Comic = new Comic[FiltrolistComic.size()];
		Comic = FiltrolistComic.toArray(Comic);
		return Comic;
	}

	/**
	 * Permite reiniciar la pantalla donde se muestran los datos
	 */
	public static void reiniciarBBDD() {
		FiltrolistComic.clear();
		listComic.clear();
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
