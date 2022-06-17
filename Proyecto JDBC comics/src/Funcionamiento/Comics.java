package Funcionamiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Comics {

	private String ID;
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

	private static List<Comics> listComics = new ArrayList<>();
	private static List<Comics> FiltrolistComics = new ArrayList<>();

	private static Connection conn = DBManager.conexion();

	// Constructor
	public Comics(String ID, String nombre, String numero, String variante, String firma, String editorial,
			String formato, String procedencia, String fecha, String guionista, String dibujante) {
		this.ID = ID;
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
	}

	// Constructor
	public Comics() {
		this.ID = "";
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
	}

	// Getters y setters

	public String getID() {
		return ID;
	}

	public String getNombre() {
		return nombre;
	}

	public String getNumero() {
		return numero;
	}

	public String getVariante() {
		return variante;
	}

	public String getFirma() {
		return firma;
	}

	public String getEditorial() {
		return editorial;
	}

	public String getFormato() {
		return formato;
	}

	public String getProcedencia() {
		return procedencia;
	}

	public String getFecha() {
		return fecha;
	}

	public String getGuionista() {
		return guionista;
	}

	public String getDibujante() {
		return dibujante;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setVariante(String variante) {
		this.variante = variante;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public void setGuionista(String guionista) {
		this.guionista = guionista;
	}

	public void setDibujante(String dibujante) {
		this.dibujante = dibujante;
	}

	/**
	 * Devuelve todos los datos de la base de datos.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Comics[] verTodo() throws SQLException {

		String sentenciaSql = "SELECT * from comicsbbdd where estado = 'En posesion'";

		Comics comics[] = null;

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
				listComics.add(new Comics(this.ID, this.nombre, this.numero, this.variante, this.firma, this.editorial,
						this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante));
			} while (rs.next());

		} catch (Exception ex) {
			ex.printStackTrace();
			// itself.
		}

		comics = new Comics[listComics.size()];
		comics = listComics.toArray(comics);
		return comics;
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
	public Comics[] filtadroBBDD(String ID, String nombreC, String numeroC, String varianteC, String firmaC,
			String editorialC, String formatoC, String procedenciaC, String fechaC, String guionistaC,
			String dibujanteC) throws SQLException {

		reiniciarBBDD();
		ordenarBBDD();

		Comics comics[] = null;

		ArrayList<String> strFilter = new ArrayList<>();

		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd");

		if (ID.length() != 0 && ID != null) {
			sql.append(connector).append("ID = ?");
			connector = " AND ";
			strFilter.add(ID);
		}
		if (nombreC.length() != 0) {
			sql.append(connector).append("nomComic = ?");
			connector = " AND ";
			strFilter.add(nombreC);
		}
		if (numeroC.length() != 0) {
			sql.append(connector).append("numComic = ?");
			connector = " AND ";
			strFilter.add(numeroC);
		}
		if (varianteC.length() != 0) {
			sql.append(connector).append("nomVariante = ?");
			connector = " AND ";
			strFilter.add(varianteC);
		}
		if (firmaC.length() != 0) {
			sql.append(connector).append("firma = ?");
			connector = " AND ";
			strFilter.add(firmaC);
		}
		if (editorialC.length() != 0) {
			sql.append(connector).append("nomEditorial = ?");
			connector = " AND ";
			strFilter.add(editorialC);
		}
		if (formatoC.length() != 0) {
			sql.append(connector).append("formato = ?");
			connector = " AND ";
			strFilter.add(formatoC);
		}
		if (procedenciaC.length() != 0) {
			sql.append(connector).append("procedencia = ?");
			connector = " AND ";
			strFilter.add(procedenciaC);
		}
		if (fechaC.length() != 0) {
			sql.append(connector).append("anioPubli = ?");
			connector = " AND ";
			strFilter.add(fechaC);
		}
		if (guionistaC.length() != 0) {
			sql.append(connector).append("nomGuionista = ?");
			connector = " AND ";
			strFilter.add(guionistaC);
		}
		if (dibujanteC.length() != 0) {
			sql.append(connector).append("nomDibujante = ?");
			strFilter.add(dibujanteC);
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
							new Comics(this.ID, this.nombre, this.numero, this.variante, this.firma, this.editorial,
									this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante));
				}
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		comics = new Comics[FiltrolistComics.size()];
		comics = FiltrolistComics.toArray(comics);
		return comics;
	}

	/**
	 * Permite reiniciar la pantalla donde se muestran los datos
	 */
	public static void reiniciarBBDD() {
		FiltrolistComics.clear();
		listComics.clear();
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

	@Override
	public String toString() {
		return "\nNombre: " + nombre + "\nNumero: " + numero + "\nVariante: " + variante + "\nFirma: " + firma
				+ "\nEditorial: " + editorial + "\nFormato: " + formato + "\nProcedencia: " + procedencia + "\nFecha: "
				+ fecha + "\nGuionista: " + guionista + "\nDibujante: " + dibujante + "\n";
	}

}
