/**
 * Contiene las clases que hacen funcionar las diferentes funciones de uso de back end y front de todo el proyecto
 *  
*/
package comicManagement;

import Funcionamiento.Utilidades;
import dbmanager.ComicManagerDAO;
import dbmanager.ListaComicsDAO;

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
	protected String ID;

	/**
	 * Nombre del cómic.
	 */
	protected String nombre;

	/**
	 * Número de caja del cómic.
	 */
	protected String numCaja;

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
	protected String key_issue;

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
	protected String url_referencia;

	/**
	 * Precio del cómic.
	 */
	protected String precio_comic;

	/**
	 * Precio del cómic.
	 */
	protected String codigo_comic;

	/**
	 * Getter para obtener el ID del cómic.
	 *
	 * @return El ID del cómic.
	 */
	public String getID() {
		return ID;
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
	public String getNumCaja() {
		return numCaja;
	}

	/**
	 * Getter para obtener la clave (key issue) del cómic.
	 *
	 * @return La clave (key issue) del cómic.
	 */
	public String getKey_issue() {
		return key_issue;
	}

	/**
	 * Setter para establecer la clave (key issue) del cómic.
	 *
	 * @param key_issue La clave (key issue) a establecer.
	 */
	public void setKey_issue(String key_issue) {
		this.key_issue = key_issue;
	}

	/**
	 * Setter para establecer la puntuación del cómic.
	 *
	 * @param puntuacion La puntuación a establecer.
	 */
	public void setPuntuacion(String puntuacion) {
		this.puntuacion = puntuacion;
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
	public String getUrl_referencia() {
		return url_referencia;
	}

	/**
	 * Getter para obtener el precio del cómic.
	 *
	 * @return El precio del cómic.
	 */
	public String getPrecio_comic() {
		return precio_comic;
	}

	public String getCodigo_comic() {
		return codigo_comic;
	}

	public void setCodigo_comic(String codigo_comic) {
		this.codigo_comic = codigo_comic;
	}

	/**
	 * Setter para establecer la URL de referencia del cómic.
	 *
	 * @param url_referencia La URL de referencia a establecer.
	 */
	public void setUrl_referencia(String url_referencia) {
		this.url_referencia = url_referencia;
	}

	/**
	 * Setter para establecer el precio del cómic.
	 *
	 * @param precio_comic El precio a establecer.
	 */
	public void setPrecio_comic(String precio_comic) {
		this.precio_comic = precio_comic;
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
	 * Setter para establecer el ID del cómic.
	 *
	 * @param ID El ID a establecer.
	 */
	public void setID(String ID) {
		this.ID = ID;
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
	public void setNumCaja(String numCaja) {
		this.numCaja = numCaja;
	}

	/**
	 * Constructor para crear un objeto Comic con todos los atributos.
	 *
	 * @param ID             El ID único del cómic.
	 * @param nombre         El nombre del cómic.
	 * @param numCaja        El número de caja del cómic.
	 * @param numero         El número de edición del cómic.
	 * @param variante       La variante del cómic.
	 * @param firma          La firma asociada al cómic.
	 * @param editorial      La editorial del cómic.
	 * @param formato        El formato del cómic.
	 * @param procedencia    La procedencia del cómic.
	 * @param fecha          La fecha de venta del cómic.
	 * @param guionista      El guionista del cómic.
	 * @param dibujante      El dibujante del cómic.
	 * @param estado         El estado del cómic.
	 * @param key_issue      La clave (key issue) del cómic.
	 * @param puntuacion     La puntuación del cómic.
	 * @param imagen         La URL de la imagen del cómic.
	 * @param url_referencia La URL de referencia del cómic.
	 * @param precio_comic   El precio del cómic.
	 */
	public Comic(String ID, String nombre, String numCaja, String numero, String variante, String firma,
			String editorial, String formato, String procedencia, String fecha, String guionista, String dibujante,
			String estado, String key_issue, String puntuacion, String imagen, String url_referencia,
			String precio_comic, String codigo_comic) {
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
		this.codigo_comic = codigo_comic;
	}

	/**
	 * Constructor vacío para crear un objeto Comic sin atributos inicializados.
	 */
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
		this.codigo_comic = "";
	}

	public boolean estaVacio() {
		return (ID == null || ID.isEmpty()) && (nombre == null || nombre.isEmpty())
				&& (numCaja == null || numCaja.isEmpty()) && (numero == null || numero.isEmpty())
				&& (variante == null || variante.isEmpty()) && (firma == null || firma.isEmpty())
				&& (editorial == null || editorial.isEmpty()) && (formato == null || formato.isEmpty())
				&& (procedencia == null || procedencia.isEmpty()) && (fecha == null || fecha.isEmpty())
				&& (guionista == null || guionista.isEmpty()) && (dibujante == null || dibujante.isEmpty())
				&& (estado == null || estado.isEmpty()) && (key_issue == null || key_issue.isEmpty())
				&& (puntuacion == null || puntuacion.isEmpty()) && (imagen == null || imagen.isEmpty())
				&& (url_referencia == null || url_referencia.isEmpty())
				&& (precio_comic == null || precio_comic.isEmpty()) && (codigo_comic == null || codigo_comic.isEmpty());
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

		// Limpiar campo key_issue
		comic.setKey_issue(limpiarCampo(comic.getKey_issue()));

	}

	public static String limpiarCampo(String campo) {
		if (campo != null) {
			// Eliminar comas repetidas y otros símbolos al final del campo (comas, guiones,
			// y espacios), con o sin espacios entre ellos
			campo = campo.replaceAll(",\\s*,", ",");
			campo = campo.replaceAll("(?:[,\\s-])+(?=[,\\s-]*$)", "");
		}
		return campo;
	}

	public static Comic obtenerComic(String idComic) {
		boolean existeComic = ComicManagerDAO.comprobarIdentificadorComic(idComic);
		Comic comic_temp = null;
		if (!existeComic) {
			existeComic = ListaComicsDAO.verificarIDExistente(idComic, false);
			if (existeComic) {
				comic_temp = ListaComicsDAO.devolverComicLista(idComic);
			}
		} else {
			comic_temp = ComicManagerDAO.comicDatos(idComic);
		}
		return comic_temp;
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

		Utilidades.appendIfNotEmpty(contenidoComic, "ID", ID);
		Utilidades.appendIfNotEmpty(contenidoComic, "Nombre", nombre);
		Utilidades.appendIfNotEmpty(contenidoComic, "Numero", numero);
		Utilidades.appendIfNotEmpty(contenidoComic, "Precio", !precio_comic.isEmpty() ? precio_comic + " $" : "");
		Utilidades.appendIfNotEmpty(contenidoComic, "Codigo", codigo_comic);
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
		Utilidades.appendIfNotEmpty(contenidoComic, "Url referencia", url_referencia);
		Utilidades.appendIfNotEmpty(contenidoComic, "Caja", numCaja);
		Utilidades.appendIfNotEmpty(contenidoComic, "Key issue", key_issue);
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

		if (!key_issue.equalsIgnoreCase("Vacio")) {
			Utilidades.appendIfNotEmpty(contenidoComic, "Key issue", key_issue);
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
		Utilidades.appendIfNotEmpty(contenidoComic, "Precio", !precio_comic.isEmpty() ? precio_comic + " $" : "");
		Utilidades.appendIfNotEmpty(contenidoComic, "Codigo", codigo_comic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Firma", firma);
		Utilidades.appendIfNotEmpty(contenidoComic, "Editorial", editorial);
		Utilidades.appendIfNotEmpty(contenidoComic, "Formato", formato);
		Utilidades.appendIfNotEmpty(contenidoComic, "Procedencia", procedencia);
		Utilidades.appendIfNotEmpty(contenidoComic, "Fecha", fecha);
		Utilidades.appendIfNotEmpty(contenidoComic, "Guionista", guionista);
		Utilidades.appendIfNotEmpty(contenidoComic, "Dibujante", dibujante);
		Utilidades.appendIfNotEmpty(contenidoComic, "Puntuacion", puntuacion);
		Utilidades.appendIfNotEmpty(contenidoComic, "Estado", estado);
		Utilidades.appendIfNotEmpty(contenidoComic, "Url", url_referencia);
		Utilidades.appendIfNotEmpty(contenidoComic, "", "");
//		Utilidades.appendIfNotEmpty(contenidoComic, "Descripcion", key_issue);

		return contenidoComic.toString();
	}

}
