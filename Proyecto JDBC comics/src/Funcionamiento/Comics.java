package Funcionamiento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DBManager.DBManager;

public class Comics {

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

	public Comics(String nombre, String numero, String variante, String firma, String editorial, String formato,
			String procedencia, String fecha, String guionista, String dibujante) {
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
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Comics[] verTodo() throws SQLException {
		String sentenciaSql = "SELECT * from comicsbbdd";

		Comics comics[] = null;

		ResultSet rs = DBManager.getComic(sentenciaSql);

		reiniciarBBDD();
		try {
			do {

				String nombre = rs.getString("nomComic");
				String numero = rs.getString("numComic");
				String variante = rs.getString("nomVariante");
				String firma = rs.getString("firma");
				String editorial = rs.getString("nomEditorial");
				String formato = rs.getString("formato");
				String procedencia = rs.getString("procedencia");
				String anioPubli = rs.getString("anioPubli");
				String guionista = rs.getString("nomGuionista");
				String dibujante = rs.getString("nomDibujante");
				listComics.add(new Comics(nombre, numero, variante, firma, editorial, formato, procedencia, anioPubli,
						guionista, dibujante));
			} while (rs.next());

		} catch (Exception ex) {
			ex.printStackTrace(); // Try to use relevant Exception classes instead of calling the 'Exception'
			// itself.
		}

		// Convert the list to array
		comics = new Comics[listComics.size()];
		comics = listComics.toArray(comics);
		return comics;
	}

	public static Comics[] filtadroBBDD(String nombreC, String numeroC, String varianteC, String firmaC,
			String nomEditorialC, String formatoC, String procedenciaC, String fechaC, String guionistaC,
			String nomDibujanteC) {

		reiniciarBBDD();

		String nombreCom, numeroCom, varianteCom, firmaCom, editorialCom, formatoCom, procedenciaCom, fechaCom,
				guionistaCom, dibujanteCom;
		Comics comics[] = null;

		String sql1 = "SELECT * FROM comicsbbdd where nomComic = '" + nombreC + "';";
		String sql2 = "SELECT * FROM comicsbbdd where numComic = '" + numeroC + "';";
		String sql3 = "SELECT * FROM comicsbbdd where nomVariante = '" + varianteC + "';";
		String sql4 = "SELECT * FROM comicsbbdd where firma = '" + firmaC + "';";
		String sql5 = "SELECT * FROM comicsbbdd where nomEditorial = '" + nomEditorialC + "';";
		String sql6 = "SELECT * FROM comicsbbdd where formato = '" + formatoC + "';";
		String sql7 = "SELECT * FROM comicsbbdd where procedencia = '" + procedenciaC + "';";
		String sql8 = "SELECT * FROM comicsbbdd where anioPubli = '" + fechaC + "';";
		String sql9 = "SELECT * FROM comicsbbdd where nomGuionistas = '" + guionistaC + "';";
		String sql10 = "SELECT * FROM comicsbbdd where nomDibujantes = '" + nomDibujanteC + "';";
//		String []sentenciasSQL = {sql1,sql2,sql3,sql4,sql5,sql6,sql7,sql8,sql9,sql10};
		String[] sentenciasSQL = { sql1 };

//		String sql = "SELECT * FROM comicsbbdd WHERE nomComic = '" + nombreC + "' AND numComic = '" + numeroC
//				+ "' AND nomVariante = '" + varianteC + "' AND firma = '" + firmaC + "' AND nomEditorial = '"
//				+ nomEditorialC + "' and formato = '" + formatoC + "' AND procedencia = '" + procedenciaC
//				+ "' AND anioPubli = '" + fechaC + "' AND nomGuionista = '" + guionistaC + "' AND nomDibujante = '"
//				+ nomDibujanteC + "';";

		try {
			for (int i = 0; i < sentenciasSQL.length; i++) {
				ResultSet rs = DBManager.getComic(sentenciasSQL[i]);

				do {
					nombreCom = rs.getString("nomComic");
					numeroCom = rs.getString("numComic");
					varianteCom = rs.getString("nomVariante");
					firmaCom = rs.getString("firma");
					editorialCom = rs.getString("nomEditorial");
					formatoCom = rs.getString("formato");
					procedenciaCom = rs.getString("procedencia");
					fechaCom = rs.getString("anioPubli");
					guionistaCom = rs.getString("nomGuionista");
					dibujanteCom = rs.getString("nomDibujante");

					FiltrolistComics.add(new Comics(nombreCom, numeroCom, varianteCom, firmaCom, editorialCom,
							formatoCom, procedenciaCom, fechaCom, guionistaCom, dibujanteCom));

				} while (rs.next());
			}

		} catch (SQLException ex) {
			System.err.println(ex);
		}
		comics = new Comics[FiltrolistComics.size()];
		comics = FiltrolistComics.toArray(comics);
		return comics;
	}

	public static void reiniciarBBDD() {
		FiltrolistComics.clear();
		listComics.clear();
	}
}
