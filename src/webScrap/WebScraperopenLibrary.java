package webScrap;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Clase principal para realizar el web scraping de información específica de la
 * página web "https://www.previewsworld.com". Extrae detalles de un cómic, como
 * el título, número, creadores, imagen principal, fecha de salida, valor (SRP),
 * editorial y URL de referencia.
 */
public class WebScraperopenLibrary{

	public static int verificarCodigoRespuesta(String urlString) throws IOException, URISyntaxException {
		URI uri = new URI(urlString);
		HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
		connection.setRequestMethod("GET");

		// Obtener el código de respuesta HTTP
		int codigoRespuesta = connection.getResponseCode();

		// Cerrar la conexión
		connection.disconnect();

		return codigoRespuesta;
	}

	/**
	 * Realiza scraping en una URL dada para extraer información sobre un cómic.
	 * 
	 * @param url La URL de la página web del cómic.
	 * @return Un array de cadenas que contiene la información del cómic, o un array
	 *         vacío si ocurre un error.
	 * @throws URISyntaxException
	 */
	public static String displayComicInfo(String urlMarvel) throws URISyntaxException {

		String codeComic = "";

		try {

			// Realizar la conexión y obtener el documento HTML de la página

			int codigoRespuesta = verificarCodigoRespuesta(urlMarvel);

			if (codigoRespuesta != 404) {
				Document document = Jsoup.connect(urlMarvel).get();

				codeComic = scrapeCodigo(document);
				
				System.out.println(codeComic);

				return codeComic;
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Extrae el título del documento HTML proporcionado. Elimina el símbolo "#" y
	 * todo lo que le sigue, luego capitaliza la primera letra de cada palabra en el
	 * título.
	 *
	 * @param document El documento HTML a analizar.
	 * @return El título extraído y formateado, o null si no se encuentra.
	 */
	private static String scrapeCodigo(Document document) {
	    // Buscar la etiqueta dt con el valor "ISBN 13"
	    Element isbnDtElement = document.selectFirst("dt:containsOwn(ISBN 13)");

	    if (isbnDtElement != null) {
	        // Obtener el siguiente hermano (etiqueta dd) con la clase "object itemprop isbn"
	        Element isbnDdElement = isbnDtElement.nextElementSibling();
	        
	        if (isbnDdElement != null && isbnDdElement.hasClass("object") && isbnDdElement.hasClass("itemprop") && isbnDdElement.hasClass("isbn")) {
	            // Obtener el contenido de la etiqueta dd
	            String isbnContent = isbnDdElement.text().trim();
	            // Puedes realizar cualquier otra manipulación necesaria con el contenido de ISBN
	            return isbnContent;
	        }
	    }

	    // Si no se encontró la información deseada, devolver null o un valor por defecto según tu lógica
	    return null;
	}
}
