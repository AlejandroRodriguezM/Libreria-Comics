package Funcionamiento;

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



}