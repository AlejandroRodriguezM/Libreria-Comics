/**
 * Contiene las clases que hacen funcionar las diferentes funciones de uso de back end y front de todo el proyecto
 *  
*/
package comicManagement;

import dbmanager.ComicManagerDAO;
import dbmanager.ListaComicsDAO;
import funciones_auxiliares.Utilidades;

/**
 * Esta clase objeto sirve para dar forma al comic que estara dentro de la base
 * de datos.
 *
 * @author Alejandro Rodriguez
 */
public class Comic {

	/**
	 * Identificador único del cómic.
	 */
	protected String id;

	/**
	 * Nombre del cómic.
	 */
	protected String nombre;

	/**
	 * Número de caja del cómic.
	 */
	protected String valorGradeo;

	/**
	 * Número del cómic.
	 */
	protected String numero;

	/**
	 * Variante del cómic.
	 */
	protected String variante;

	/**
	 * Firma asociada al cómic.
	 */
	protected String firma;

	/**
	 * Editorial del cómic.
	 */
	protected String editorial;

	/**
	 * Formato del cómic.
	 */
	protected String formato;

	/**
	 * Procedencia del cómic.
	 */
	protected String procedencia;

	/**
	 * Fecha de publicación del cómic.
	 */
	protected String fecha;

	/**
	 * Guionista del cómic.
	 */
	protected String guionista;

	/**
	 * Dibujante del cómic.
	 */
	protected String dibujante;

	/**
	 * Key Issue asociado al cómic.
	 */
	protected String keyIssue;

	/**
	 * Estado del cómic.
	 */
	protected String estado;

	/**
	 * Puntuación del cómic.
	 */
	protected String puntuacion;

	/**
	 * Ruta de la imagen del cómic.
	 */
	protected String imagen;

	/**
	 * URL de referencia relacionada con el cómic.
	 */
	protected String urlReferencia;

	/**
	 * Precio del cómic.
	 */
	protected String precioComic;

	/**
	 * Precio del cómic.
	 */
	protected String codigoComic;

	/**
	 * Constructor para crear un objeto Comic con todos los atributos.
	 *
	 */
	private Comic(ComicBuilder builder) {
		this.id = builder.id;
		this.nombre = builder.nombre;
		this.valorGradeo = builder.valorGradeo;
		this.numero = builder.numero;
		this.variante = builder.variante;
		this.firma = builder.firma;
		this.editorial = builder.editorial;
		this.formato = builder.formato;
		this.procedencia = builder.procedencia;
		this.fecha = builder.fecha;
		this.guionista = builder.guionista;
		this.dibujante = builder.dibujante;
		this.estado = builder.estado;
		this.keyIssue = builder.keyIssue;
		this.puntuacion = builder.puntuacion;
		this.imagen = builder.imagen;
		this.urlReferencia = builder.urlReferencia;
		this.precioComic = builder.precioComic;
		this.codigoComic = builder.codigoComic;
	}

	/**
	 * Constructor vacío para crear un objeto Comic sin atributos inicializados.
	 */
	public Comic() {
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
		this.keyIssue = "";
		this.estado = "";
		this.puntuacion = "";
		this.imagen = null;
		this.valorGradeo = "";
		this.urlReferencia = "";
		this.precioComic = "";
		this.codigoComic = "";
	}

	public static class ComicBuilder {

		private String id;
		private String nombre;
		private String valorGradeo;
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
		private String keyIssue;
		private String puntuacion;
		private String imagen;
		private String urlReferencia;
		private String precioComic;
		private String codigoComic;

		public ComicBuilder(String id, String nombre) {
			this.id = id;
			this.nombre = nombre;
		}

		public ComicBuilder valorGradeo(String valorGradeo) {
			this.valorGradeo = valorGradeo;
			return this;
		}

		public ComicBuilder numero(String numero) {
			this.numero = numero;
			return this;
		}

		public ComicBuilder variante(String variante) {
			this.variante = variante;
			return this;
		}

		public ComicBuilder firma(String firma) {
			this.firma = firma;
			return this;
		}

		public ComicBuilder editorial(String editorial) {
			this.editorial = editorial;
			return this;
		}

		public ComicBuilder formato(String formato) {
			this.formato = formato;
			return this;
		}

		public ComicBuilder procedencia(String procedencia) {
			this.procedencia = procedencia;
			return this;
		}

		public ComicBuilder fecha(String fecha) {
			this.fecha = fecha;
			return this;
		}

		public ComicBuilder guionista(String guionista) {
			this.guionista = guionista;
			return this;
		}

		public ComicBuilder dibujante(String dibujante) {
			this.dibujante = dibujante;
			return this;
		}

		public ComicBuilder estado(String estado) {
			this.estado = estado;
			return this;
		}

		public ComicBuilder keyIssue(String keyIssue) {
			this.keyIssue = keyIssue;
			return this;
		}

		public ComicBuilder puntuacion(String puntuacion) {
			this.puntuacion = puntuacion;
			return this;
		}

		public ComicBuilder imagen(String imagen) {
			this.imagen = imagen;
			return this;
		}

		public ComicBuilder referenciaComic(String urlReferencia) {
			this.urlReferencia = urlReferencia;
			return this;
		}

		public ComicBuilder precioComic(String precioComic) {
			this.precioComic = precioComic;
			return this;
		}

		public ComicBuilder codigoComic(String codigoComic) {
			this.codigoComic = codigoComic;
			return this;
		}

		public Comic build() {
			return new Comic(this);
		}
	}

	/**
	 * Getter para obtener el id del cómic.
	 *
	 * @return El id del cómic.
	 */
	public String getid() {
		return id;
	}

	/**
	 * Getter para obtener el nombre del cómic.
	 *
	 * @return El nombre del cómic.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Getter para obtener el número de edición del cómic.
	 *
	 * @return El número de edición del cómic.
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * Getter para obtener la variante del cómic.
	 *
	 * @return La variante del cómic.
	 */
	public String getVariante() {
		return variante;
	}

	/**
	 * Getter para obtener la firma asociada al cómic.
	 *
	 * @return La firma asociada al cómic.
	 */
	public String getFirma() {
		return firma;
	}

	/**
	 * Getter para obtener la editorial del cómic.
	 *
	 * @return La editorial del cómic.
	 */
	public String getEditorial() {
		return editorial;
	}

	/**
	 * Getter para obtener el formato del cómic.
	 *
	 * @return El formato del cómic.
	 */
	public String getFormato() {
		return formato;
	}

	/**
	 * Getter para obtener la procedencia del cómic.
	 *
	 * @return La procedencia del cómic.
	 */
	public String getProcedencia() {
		return procedencia;
	}

	/**
	 * Getter para obtener la fecha de venta del cómic.
	 *
	 * @return La fecha de venta del cómic.
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * Getter para obtener el guionista del cómic.
	 *
	 * @return El guionista del cómic.
	 */
	public String getGuionista() {
		return guionista;
	}

	/**
	 * Getter para obtener el dibujante del cómic.
	 *
	 * @return El dibujante del cómic.
	 */
	public String getDibujante() {
		return dibujante;
	}

	/**
	 * Getter para obtener el estado del cómic.
	 *
	 * @return El estado del cómic.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Getter para obtener la puntuación del cómic.
	 *
	 * @return La puntuación del cómic.
	 */
	public String getPuntuacion() {
		return puntuacion;
	}

	/**
	 * Getter para obtener el número de caja del cómic.
	 *
	 * @return El número de caja del cómic.
	 */
	public String getValorGradeo() {
		return valorGradeo;
	}

	/**
	 * Getter para obtener la clave (key issue) del cómic.
	 *
	 * @return La clave (key issue) del cómic.
	 */
	public String getkeyIssue() {
		return keyIssue;
	}

	/**
	 * Getter para obtener la URL de la imagen del cómic.
	 *
	 * @return La URL de la imagen del cómic.
	 */
	public String getImagen() {
		return imagen;
	}

	/**
	 * Getter para obtener la URL de referencia del cómic.
	 *
	 * @return La URL de referencia del cómic.
	 */
	public String getUrlReferencia() {
	    return urlReferencia;
	}

	/**
	 * Getter para obtener el precio del cómic.
	 *
	 * @return El precio del cómic.
	 */
	public String getprecioComic() {
		return precioComic;
	}

	public String getcodigoComic() {
		return codigoComic;
	}

	/**
	 * Setter para establecer la clave (key issue) del cómic.
	 *
	 * @param keyIssue La clave (key issue) a establecer.
	 */
	public void setkeyIssue(String keyIssue) {
		this.keyIssue = keyIssue;
	}

	/**
	 * Setter para establecer la puntuación del cómic.
	 *
	 * @param puntuacion La puntuación a establecer.
	 */
	public void setPuntuacion(String puntuacion) {
		this.puntuacion = puntuacion;
	}

	public void setcodigoComic(String codigoComic) {
		this.codigoComic = codigoComic;
	}

	/**
	 * Setter para establecer la URL de referencia del cómic.
	 *
	 * @param urlReferencia La URL de referencia a establecer.
	 */
	public void seturlReferencia(String urlReferencia) {
		this.urlReferencia = urlReferencia;
	}

	/**
	 * Setter para establecer el precio del cómic.
	 *
	 * @param precioComic El precio a establecer.
	 */
	public void setprecioComic(String precioComic) {
		this.precioComic = precioComic;
	}

	/**
	 * Setter para establecer la URL de la imagen del cómic.
	 *
	 * @param imagen La URL de la imagen a establecer.
	 */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	/**
	 * Setter para establecer el id del cómic.
	 *
	 * @param id El id a establecer.
	 */
	public void setID(String id) {
		this.id = id;
	}

	/**
	 * Setter para establecer el nombre del cómic.
	 *
	 * @param nombre El nombre a establecer.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Setter para establecer el número de edición del cómic.
	 *
	 * @param numero El número de edición a establecer.
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * Setter para establecer la variante del cómic.
	 *
	 * @param variante La variante a establecer.
	 */
	public void setVariante(String variante) {
		this.variante = variante;
	}

	/**
	 * Setter para establecer la firma asociada al cómic.
	 *
	 * @param firma La firma a establecer.
	 */
	public void setFirma(String firma) {
		this.firma = firma;
	}

	/**
	 * Setter para establecer la editorial del cómic.
	 *
	 * @param editorial La editorial a establecer.
	 */
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	/**
	 * Setter para establecer el formato del cómic.
	 *
	 * @param formato El formato a establecer.
	 */
	public void setFormato(String formato) {
		this.formato = formato;
	}

	/**
	 * Setter para establecer la procedencia del cómic.
	 *
	 * @param procedencia La procedencia a establecer.
	 */
	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}

	/**
	 * Setter para establecer la fecha de venta del cómic.
	 *
	 * @param fecha La fecha de venta a establecer.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * Setter para establecer el guionista del cómic.
	 *
	 * @param guionista El guionista a establecer.
	 */
	public void setGuionista(String guionista) {
		this.guionista = guionista;
	}

	/**
	 * Setter para establecer el dibujante del cómic.
	 *
	 * @param dibujante El dibujante a establecer.
	 */
	public void setDibujante(String dibujante) {
		this.dibujante = dibujante;
	}

	/**
	 * Setter para establecer el estado del cómic.
	 *
	 * @param estado El estado a establecer.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Setter para establecer el número de caja del cómic.
	 *
	 * @param numCaja El número de caja a establecer.
	 */
	public void setValorGradeo(String valorGradeo) {
		this.valorGradeo = valorGradeo;
	}

	public boolean estaVacio() {
		return isNullOrEmpty(id) && isNullOrEmpty(nombre) && isNullOrEmpty(valorGradeo) && isNullOrEmpty(numero)
				&& isNullOrEmpty(variante) && isNullOrEmpty(firma) && isNullOrEmpty(editorial) && isNullOrEmpty(formato)
				&& isNullOrEmpty(procedencia) && isNullOrEmpty(fecha) && isNullOrEmpty(guionista)
				&& isNullOrEmpty(dibujante) && isNullOrEmpty(estado) && isNullOrEmpty(keyIssue)
				&& isNullOrEmpty(puntuacion) && isNullOrEmpty(imagen) && isNullOrEmpty(urlReferencia)
				&& isNullOrEmpty(precioComic) && isNullOrEmpty(codigoComic);
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static void limpiarCamposComic(Comic comic) {
		// Limpiar campo nombre
		comic.setNombre(limpiarCampo(comic.getNombre()));

		// Limpiar campo variante
		comic.setVariante(limpiarCampo(comic.getVariante()));

		// Limpiar campo firma
		comic.setFirma(limpiarCampo(comic.getFirma()));

		// Limpiar campo editorial
		comic.setEditorial(limpiarCampo(comic.getEditorial()));

		// Limpiar campo guionista
		comic.setGuionista(limpiarCampo(comic.getGuionista()));

		// Limpiar campo dibujante
		comic.setDibujante(limpiarCampo(comic.getDibujante()));

		// Limpiar campo estado
		comic.setEstado(limpiarCampo(comic.getEstado()));

		// Limpiar campo keyIssue
		comic.setkeyIssue(limpiarCampo(comic.getkeyIssue()));

	}

	public static String limpiarCampo(String campo) {
		if (campo != null) {
			// Eliminar comas repetidas y otros símbolos al inicio y al final del campo
			campo = campo.replaceAll("^\\s*[,\\s-]+", ""); // Al principio
			campo = campo.replaceAll("[,\\s-]+\\s*$", ""); // Al final
			campo = campo.replaceAll(",\\s*,", ","); // Comas repetidas
			campo = campo.replaceAll(",\\s*", " - "); // Reemplazar ", " por " - "
			campo = campo.replace("'", " "); // Reemplazar ", " por " - "
		}
		return campo;
	}

	public static Comic obtenerComic(String idComic) {
		boolean existeComic = ComicManagerDAO.comprobarIdentificadorComic(idComic);
		if (!existeComic) {
			existeComic = ListaComicsDAO.verificarIDExistente(idComic, false);
			if (existeComic) {
				return ListaComicsDAO.devolverComicLista(idComic);
			}
		} else {
			return ComicManagerDAO.comicDatos(idComic);
		}
		return null;
	}

	public static boolean validarComic(Comic comic) {
		// Verificar si al menos un campo tiene datos utilizando los campos del objeto
		// Comic pasado como parámetro
		if (comic.getNombre() != null && !comic.getNombre().isEmpty()) {
			return true;
		}
		if (comic.getNumero() != null && !comic.getNumero().isEmpty()) {
			return true;
		}
		if (comic.getVariante() != null && !comic.getVariante().isEmpty()) {
			return true;
		}
		if (comic.getFirma() != null && !comic.getFirma().isEmpty()) {
			return true;
		}
		if (comic.getEditorial() != null && !comic.getEditorial().isEmpty()) {
			return true;
		}
		if (comic.getGuionista() != null && !comic.getGuionista().isEmpty()) {
			return true;
		}
		if (comic.getDibujante() != null && !comic.getDibujante().isEmpty()) {
			return true;
		}
		// Si ninguno de los campos tiene datos, devuelve false
		return false;
	}

	/**
	 * Genera una representación en forma de cadena de texto del cómic, incluyendo
	 * sus atributos no nulos.
	 *
	 * @return Una cadena de texto que representa el cómic.
	 */
	@Override
	public String toString() {
		StringBuilder contenidoComic = new StringBuilder();

		Utilidades.appendIfNotEmpty(contenidoComic, "ID", id);
		Utilidades.appendIfNotEmpty(contenidoComic, "Nombre", nombre);
		Utilidades.appendIfNotEmpty(contenidoComic, "Numero", numero);
		Utilidades.appendIfNotEmpty(contenidoComic, "Precio", !precioComic.isEmpty() ? precioComic + " $" : "");
		Utilidades.appendIfNotEmpty(contenidoComic, "Codigo", codigoComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Variante", variante);
		Utilidades.appendIfNotEmpty(contenidoComic, "Firma", firma);
		Utilidades.appendIfNotEmpty(contenidoComic, "Editorial", editorial);
		Utilidades.appendIfNotEmpty(contenidoComic, "Formato", formato);
		Utilidades.appendIfNotEmpty(contenidoComic, "Procedencia", procedencia);
		Utilidades.appendIfNotEmpty(contenidoComic, "Fecha", fecha);
		Utilidades.appendIfNotEmpty(contenidoComic, "Guionista", guionista);
		Utilidades.appendIfNotEmpty(contenidoComic, "Dibujante", dibujante);
		Utilidades.appendIfNotEmpty(contenidoComic, "Puntuacion", puntuacion);
		Utilidades.appendIfNotEmpty(contenidoComic, "Estado", estado);
		Utilidades.appendIfNotEmpty(contenidoComic, "Url referencia", urlReferencia);
		Utilidades.appendIfNotEmpty(contenidoComic, "Valor gradeo", valorGradeo);
		Utilidades.appendIfNotEmpty(contenidoComic, "Key issue", keyIssue);
		Utilidades.appendIfNotEmpty(contenidoComic, "Portada", imagen);

		return contenidoComic.toString();
	}

	/**
	 * Genera una representación en forma de cadena de texto del cómic, incluyendo
	 * sus atributos no nulos.
	 *
	 * @return Una cadena de texto que representa el cómic.
	 */
	public String devolverKeyIssue() {
		StringBuilder contenidoComic = new StringBuilder();

		if (!keyIssue.equalsIgnoreCase("Vacio")) {
			Utilidades.appendIfNotEmpty(contenidoComic, "Key issue", keyIssue);
			return contenidoComic.toString();
		}
		return "";

	}

	/**
	 * Genera una representación en forma de cadena de texto del cómic, incluyendo
	 * sus atributos no nulos.
	 *
	 * @return Una cadena de texto que representa el cómic.
	 */
	public String infoComic() {
		StringBuilder contenidoComic = new StringBuilder();

		Utilidades.appendIfNotEmpty(contenidoComic, "Nombre", nombre);
		Utilidades.appendIfNotEmpty(contenidoComic, "Variante", variante);
		Utilidades.appendIfNotEmpty(contenidoComic, "Numero", numero);
		Utilidades.appendIfNotEmpty(contenidoComic, "Precio", !precioComic.isEmpty() ? precioComic + " $" : "");
		Utilidades.appendIfNotEmpty(contenidoComic, "Codigo", codigoComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Firma", firma);
		Utilidades.appendIfNotEmpty(contenidoComic, "Editorial", editorial);
		Utilidades.appendIfNotEmpty(contenidoComic, "Formato", formato);
		Utilidades.appendIfNotEmpty(contenidoComic, "Procedencia", procedencia);
		Utilidades.appendIfNotEmpty(contenidoComic, "Fecha", fecha);
		Utilidades.appendIfNotEmpty(contenidoComic, "Guionista", guionista);
		Utilidades.appendIfNotEmpty(contenidoComic, "Dibujante", dibujante);
		Utilidades.appendIfNotEmpty(contenidoComic, "Puntuacion", puntuacion);
		Utilidades.appendIfNotEmpty(contenidoComic, "Estado", estado);
		Utilidades.appendIfNotEmpty(contenidoComic, "Url", urlReferencia);
		Utilidades.appendIfNotEmpty(contenidoComic, "", "");

		return contenidoComic.toString();
	}

}
