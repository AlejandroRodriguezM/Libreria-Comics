package Funcionamiento;

/**
 * Esta clase objeto sirve para dar forma al comic que estara dentro de la base
 * de datos.
 *
 * @author Alejandro Rodriguez
 */
public class Comic {

	protected String ID;
	protected String nombre;
	protected String numCaja;
	protected String numero;
	protected String variante;
	protected String firma;
	protected String editorial;
	protected String formato;
	protected String procedencia;
	protected String fecha;
	protected String guionista;
	protected String dibujante;
	protected String key_issue;
	protected String estado;
	protected String puntuacion;
	protected String imagen;
	protected String url_referencia;
	protected String precio_comic;

	// Constructor
	public Comic(String ID, String nombre, String numCaja, String numero, String variante, String firma,
			String editorial, String formato, String procedencia, String fecha, String guionista, String dibujante,
			String estado, String key_issue, String puntuacion, String imagen, String url_referencia,
			String precio_comic) {
		this.ID = ID;
		this.nombre = nombre;
		this.numCaja = numCaja;
		this.numero = numero;
		this.variante = variante;
		this.firma = firma;
		this.editorial = editorial;
		this.formato = formato;
		this.procedencia = procedencia;
		this.fecha = fecha;
		this.guionista = guionista;
		this.dibujante = dibujante;
		this.key_issue = key_issue;
		this.estado = estado;
		this.puntuacion = puntuacion;
		this.imagen = imagen;
		this.url_referencia = url_referencia;
		this.precio_comic = precio_comic;

	}

	// Constructor
	public Comic() {
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
		this.key_issue = "";
		this.estado = "";
		this.puntuacion = "";
		this.imagen = null;
		this.numCaja = "";
		this.url_referencia = "";
		this.precio_comic = "";
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

	public String getEstado() {
		return estado;
	}

	public String getPuntuacion() {
		return puntuacion;
	}

	public String getNumCaja() {
		return numCaja;
	}

	public String getKey_issue() {
		return key_issue;
	}

	public void setKey_issue(String key_issue) {
		this.key_issue = key_issue;
	}

	public void setPuntuacion(String puntuacion) {
		this.puntuacion = puntuacion;
	}

	public String getImagen() {
		return imagen;
	}

	public String getUrl_referencia() {
		return url_referencia;
	}

	public String getPrecio_comic() {
		return precio_comic;
	}

	public void setUrl_referencia(String url_referencia) {
		this.url_referencia = url_referencia;
	}

	public void setPrecio_comic(String precio_comic) {
		this.precio_comic = precio_comic;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
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

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setNumCaja(String numCaja) {
		this.numCaja = numCaja;
	}

	private void appendIfNotEmpty(StringBuilder builder, String label, String value) {
		if (value != null && !value.isEmpty()) {
			builder.append(label).append(": ").append(value).append("\n");
		}
	}

	@Override
	public String toString() {
		StringBuilder contenidoComic = new StringBuilder();

		appendIfNotEmpty(contenidoComic, "ID", ID);
		appendIfNotEmpty(contenidoComic, "Nombre", nombre);
		appendIfNotEmpty(contenidoComic, "Numero", numero) ;
		appendIfNotEmpty(contenidoComic, "Precio", !precio_comic.isEmpty() ? precio_comic + " $" : "");
		appendIfNotEmpty(contenidoComic, "Variante", variante);
		appendIfNotEmpty(contenidoComic, "Firma", firma);
		appendIfNotEmpty(contenidoComic, "Editorial", editorial);
		appendIfNotEmpty(contenidoComic, "Formato", formato);
		appendIfNotEmpty(contenidoComic, "Procedencia", procedencia);
		appendIfNotEmpty(contenidoComic, "Fecha", fecha);
		appendIfNotEmpty(contenidoComic, "Guionista", guionista);
		appendIfNotEmpty(contenidoComic, "Dibujante", dibujante);
		appendIfNotEmpty(contenidoComic, "Key issue", key_issue);
		appendIfNotEmpty(contenidoComic, "Puntuacion", puntuacion);
		appendIfNotEmpty(contenidoComic, "Estado", estado);
		appendIfNotEmpty(contenidoComic, "Url referencia", url_referencia);
		appendIfNotEmpty(contenidoComic, "Caja", numCaja);

		return contenidoComic.toString();
	}

}
