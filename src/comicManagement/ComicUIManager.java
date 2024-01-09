package comicManagement;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import Funcionamiento.Utilidades;
import javafx.application.Platform;
import javafx.scene.image.Image;

/**
 * Esta clase se encargará de la gestión de la interfaz de usuario, como la
 * obtención de datos de entrada, la validación de campos y la actualización de
 * la interfaz gráfica.
 */
public class ComicUIManager {

	/**
	 * Rellena los campos de la interfaz gráfica con la información del cómic
	 * proporcionada.
	 *
	 * @param comicInfo Un arreglo de strings con información del cómic.
	 * @throws IOException
	 */
	private void rellenarCamposAni(String[] comicInfo) throws IOException {

		Platform.runLater(() -> {

			Image imagen = null;
			String titulo = comicInfo[0];
			String issueKey = comicInfo[1];
			String numero = comicInfo[2];
			String formato = comicInfo[3];
			String precio = comicInfo[4];
			String variante = comicInfo[5];
			String dibujantes = comicInfo[6];
			String escritores = comicInfo[7];
			String fechaVenta = comicInfo[8];
			String referencia = comicInfo[9];
			String urlImagen = comicInfo[10];
			String editorial = comicInfo[11];

			nombreComic.setText(titulo);

			numeroComic.setValue(numero);

			varianteComic.setText(variante);
			editorialComic.setText(editorial);
			formatoComic.setValue(formato);

			// Parsear y establecer la fecha
			LocalDate fecha;

			if (fechaVenta == null || fechaVenta.isEmpty()) {
				// La cadena de fecha no existe o es nula, establecer la fecha predeterminada
				fecha = LocalDate.of(2000, 1, 1);
			} else {
				// La cadena de fecha existe, parsearla
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				fecha = LocalDate.parse(fechaVenta, formatter);
			}

			if (urlImagen == null || urlImagen.isEmpty()) {
				// Cargar la imagen desde la URL
				String rutaImagen = "/Funcionamiento/sinPortada.jpg";
				imagen = new Image(getClass().getResourceAsStream(rutaImagen));
				imagencomic.setImage(imagen);
			} else {
				// Cargar la imagen desde la URL
				cargarImagenAsync(urlImagen, imagencomic);
			}
			fechaComic.setValue(fecha);

			guionistaComic.setText(escritores);
			dibujanteComic.setText(dibujantes);
			direccionImagen.setText(urlImagen);
			precioComic.setText(precio);
			urlReferencia.setText(referencia);

			nombreKeyIssue.setText(issueKey);
		});

	}
	
	/**
	 * Rellena los campos de la interfaz gráfica con la información del cómic
	 * proporcionada.
	 *
	 * @param comicInfo Un arreglo de strings con información del cómic.
	 * @throws IOException
	 */
	private void rellenarTablaImport(String[] comicInfo, String codigo_comic) throws IOException {
		Platform.runLater(() -> {
			String id = "A" + 0 + "" + comicsImportados.size() + 1;
			String titulo = defaultIfNullOrEmpty(comicInfo[0], "Vacio");
			String issueKey = defaultIfNullOrEmpty(comicInfo[1], "Vacio");
			String numero = defaultIfNullOrEmpty(comicInfo[2], "0");
			String variante = defaultIfNullOrEmpty(comicInfo[5], "Vacio");
			String precio = defaultIfNullOrEmpty(comicInfo[4], "0");
			String dibujantes = defaultIfNullOrEmpty(comicInfo[6], "Vacio");
			String escritores = defaultIfNullOrEmpty(comicInfo[7], "Vacio");
			String fechaVenta = comicInfo[8];
			LocalDate fecha = parseFecha(fechaVenta);

			String referencia = defaultIfNullOrEmpty(comicInfo[9], "Vacio");
			String urlImagen = comicInfo[10];
			String editorial = defaultIfNullOrEmpty(comicInfo[11], "Vacio");

			File file = new File(urlImagen);

			if (urlImagen == null) {
				// Si hubo un error al descargar la imagen, agregar la ruta de la imagen
				// predeterminada desde los recursos
				String rutaImagen = "/Funcionamiento/sinPortada.jpg";
				URL url = getClass().getResource(rutaImagen);
				if (url != null) {
					urlImagen = url.toExternalForm();
				} else {
					System.err.println("Error al obtener la ruta de la imagen predeterminada.");
				}
			} else {
				file = new File(urlImagen);
				urlImagen = file.toString();
			}

			String correctedUrl = urlImagen.replace("\\", "/").replace("http:", "https:").replace("https:", "https:/");

			String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);

			URI uri = null;
			try {
				uri = new URI(correctedUrl);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String urlFinal = SOURCE_PATH + File.separator + codigo_imagen + ".jpg";

			Utilidades.descargarYConvertirImagenAsync(uri, SOURCE_PATH, codigo_imagen);

			Comic comic = new Comic(id, titulo, "0", numero, variante, "", editorial, formato(), procedencia(),
					fecha.toString(), escritores, dibujantes, estado(), issueKey, "Sin puntuar", urlFinal, referencia,
					precio, codigo_comic);

			comicsImportados.add(comic);

			funcionesTabla.nombreColumnas(columnList, tablaBBDD);
			funcionesTabla.tablaBBDD(comicsImportados, tablaBBDD, columnList);
		});
	}
	
	/**
	 * Devuelve el valor predeterminado si la cadena dada es nula o vacía, de lo
	 * contrario, devuelve la cadena original.
	 *
	 * @param value        Cadena a ser verificada.
	 * @param defaultValue Valor predeterminado a ser devuelto si la cadena es nula
	 *                     o vacía.
	 * @return Cadena original o valor predeterminado.
	 */
	private String defaultIfNullOrEmpty(String value, String defaultValue) {
		return (value == null || value.isEmpty()) ? defaultValue : value;
	}
	
	/**
	 * Parsea la cadena de fecha y devuelve la fecha correspondiente. Si la cadena
	 * es nula o vacía, devuelve la fecha actual.
	 *
	 * @param fechaVenta Cadena de fecha a ser parseada.
	 * @return Objeto LocalDate que representa la fecha parseada.
	 */
	private LocalDate parseFecha(String fechaVenta) {
		if (fechaVenta == null || fechaVenta.isEmpty()) {
			return LocalDate.of(2000, 1, 1); // Obtener la fecha actual si la cadena de fecha no está presente
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			return LocalDate.parse(fechaVenta, formatter);
		}
	}
	
	/**
	 * Limpia y restablece todos los campos de datos en la sección de animaciones a
	 * sus valores predeterminados. Además, restablece la imagen de fondo y oculta
	 * cualquier mensaje de error o información.
	 */
	public void limpiarDatosPantalla() {
		// Restablecer la imagen de fondo a su valor predeterminado
		Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComic.jpg"));
		imagenFondo.setImage(nuevaImagen);

		// Restablecer los campos de datos
		nombreComic.setText("");
		varianteComic.setText("");
		firmaComic.setText("");
		editorialComic.setText("");
		fechaComic.setValue(null);
		guionistaComic.setText("");
		dibujanteComic.setText("");
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		nombreKeyIssue.setText("");
		numeroComic.getEditor().clear(); // Limpiar el texto en el ComboBox
		formatoComic.getEditor().clear(); // Limpiar el texto en el ComboBox
		procedenciaComic.getEditor().clear(); // Limpiar el texto en el ComboBox
		estadoComic.getEditor().clear(); // Limpiar el texto en el ComboBox
		urlReferencia.setText("");
		precioComic.setText("");
		direccionImagen.setText("");
		imagencomic.setImage(null);
		numeroCajaComic.getEditor().clear();
		codigoComicTratar.setText("");
		botonGuardarComic.setVisible(false);
		botonGuardarCambioComic.setVisible(false);
		tablaBBDD.getItems().clear();
		botonEliminarImportadoComic.setVisible(false);

		if (comicsImportados.size() > 0) {
			comicsImportados.clear();
		}
		// Borrar cualquier mensaje de error presente
		borrarErrores();
	}
	
	/**
	 * Elimina cualquier resaltado de campos en rojo que indique errores.
	 */
	public void borrarErrores() {
		// Restaurar el estilo de fondo de los campos a su estado original
		nombreComic.setStyle("");
		numeroComic.setStyle("");
		editorialComic.setStyle("");
		guionistaComic.setStyle("");
		dibujanteComic.setStyle("");
	}
	
}
