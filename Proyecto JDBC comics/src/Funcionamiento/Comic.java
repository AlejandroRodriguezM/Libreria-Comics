package Funcionamiento;

public class Comic {
	
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
	
	public Comic(String nombre, String numero, String variante, String firma, String editorial, String formato,
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
	

}
