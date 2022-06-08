package Funcionamiento;

import java.sql.ResultSet;
import java.sql.SQLException;

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

	public Comics(String nombre, String numero, String variante, String firma, String editorial, String formato,
			String procedencia, String fecha, String guionista, String dibujante) {
		super();
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



	public static Comics[] verTodo() throws SQLException
	{
		int posicion = 0;
		String sentenciaSql = "SELECT * from comics.comicsbbdd";

		Comics comic []= null;

		ResultSet rs = DBManager.getComic(sentenciaSql);

		try {
			if(rs.last()) {
				comic = new Comics[(rs.getRow())];
				System.out.println();
				rs.isBeforeFirst();

				do
				{
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
					comic[posicion] = new Comics(nombre,numero,variante,firma,editorial,formato,procedencia,anioPubli,guionista,dibujante);
					posicion++;
					
				}
				while(rs.next());
			}

		}
		catch(Exception ex)
		{
			System.out.println();
		}
		return comic;
	}

	@Override
	public String toString() {
		return "Comics [nombre=" + nombre + ", numero=" + numero + ", variante=" + variante + ", firma=" + firma
				+ ", editorial=" + editorial + ", formato=" + formato + ", procedencia=" + procedencia + ", fecha="
				+ fecha + ", guionista=" + guionista + ", dibujante=" + dibujante + "]";
	}


}
