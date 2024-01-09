package webScrap;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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



	/**
	 * Extrae el número que sigue al símbolo "#" en el título.
	 *
	 * @param document El documento HTML del cual extraer.
	 * @return El número extraído o null si no se encuentra.
	 */
	private static String extractNumeroFromTitle(Document document) {
		Element titleElement = document.selectFirst("h1.Title");
		if (titleElement != null) {
			String titleContent = titleElement.text().trim();
			// Obtener el número después del símbolo "#"
			int hashtagIndexNumero = titleContent.indexOf('#');
			if (hashtagIndexNumero != -1 && hashtagIndexNumero + 1 < titleContent.length()) {
				return String.valueOf(titleContent.charAt(hashtagIndexNumero + 1)).trim();
			}else {
				return "0";
			}
		}
		return null;
	}

	/**
	 * Elimina el símbolo "#" y todo lo que le sigue del contenido del título.
	 *
	 * @param titleContent El contenido del título a procesar.
	 * @return El contenido del título sin el "#" y el texto siguiente.
	 */
	private static String removeHashtagAndFollowing(String titleContent) {
	    if (titleContent.contains("#")) {
	        int hashtagIndex = titleContent.indexOf('#');
	        return titleContent.substring(0, hashtagIndex).trim().toLowerCase();
	    }
	    return titleContent;
	}


	/**
	 * Extrae e imprime la URL de la imagen principal del documento.
	 *
	 * @param document El documento HTML a analizar.
	 * @return La URL de la imagen principal, o null si no se encuentra.
	 * @throws IOException 
	 */
	private static String scrapeAndPrintMainImage(Document document) throws IOException {
		Element mainImageElement = document.selectFirst("#MainContentImage");
		if (mainImageElement != null) {
			String mainImageUrl = "https://www.previewsworld.com" + mainImageElement.attr("src");
			
//			String imagen = Utilidades.descargarImagen(mainImageUrl, DOCUMENTS_PATH);
			
			return mainImageUrl;
		}
		return null;
	}

	/**
	 * Extrae e imprime la fecha de lanzamiento del documento.
	 *
	 * @param document El documento HTML a analizar.
	 * @return La fecha de lanzamiento formateada, o null si no se encuentra.
	 */
	private static String scrapeAndPrintReleaseDate(Document document) {
		Element releaseDateElement = document.selectFirst("div.ReleaseDate");
		if (releaseDateElement != null) {
			String releaseDateText = releaseDateElement.text().replace("In Shops: ", "").trim();
			Date releaseDate = parseReleaseDate(releaseDateText);
			return formatDateAsString(releaseDate);
		}
		return null;
	}

	/**
	 * Extrae e imprime el precio sugerido de venta (SRP) del documento.
	 *
	 * @param document El documento HTML a analizar.
	 * @return El valor SRP, o null si no se encuentra.
	 */
	private static String scrapeAndPrintSRP(Document document) {
		Element srpElement = document.selectFirst("div.SRP");
		if (srpElement != null) {
			String srpValue = srpElement.text().replaceAll("[^0-9.]", "").trim();
			return srpValue;
		}
		return null;
	}

	/**
	 * Extrae e imprime el nombre del editor del documento.
	 *
	 * @param document El documento HTML a analizar.
	 * @return El nombre del editor en mayúsculas, o null si no se encuentra.
	 */
	private static String scrapeAndPrintPublisher(Document document) {
		Element publisherElement = document.selectFirst("div.Publisher");
		if (publisherElement != null) {
			String publisherName = capitalizeFirstLetter(publisherElement.text().trim().toLowerCase());
			
			if(publisherName.equalsIgnoreCase("Boom! Studios")) {
				publisherName = "Boom Studios";
			}
			
	        if (publisherName.contains("comics")) {
	            publisherName = publisherName.replace("comics", "").trim();
	        } else if (publisherName.contains("comic")) {
	            publisherName = publisherName.replace("comic", "").trim();
	        }
			
			return publisherName;
		}
		return null;
	}

	/**
	 * Extrae el contenido de texto de un elemento HTML específico identificado por
	 * el selector.
	 *
	 * @param document El documento HTML a analizar.
	 * @param selector El selector CSS para el elemento objetivo.
	 * @return El contenido de texto del elemento seleccionado, o null si no se
	 *         encuentra.
	 */
	private static String scrapeElementText(Document document, String selector) {
		Elements elements = document.select(selector);
		if (!elements.isEmpty()) {
			Element element = elements.first();
			return element.text();
		}
		return null;
	}

	/**
	 * Analiza una cadena de texto de fecha en el formato "MMM d, yyyy" a un objeto
	 * Date.
	 *
	 * @param dateText El texto de la fecha a analizar.
	 * @return El objeto Date analizado o null si hay un error de análisis.
	 */
	private static Date parseReleaseDate(String dateText) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
			return dateFormat.parse(dateText);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Formatea un objeto Date como una cadena en el formato "yyyy-MM-dd".
	 *
	 * @param date El objeto Date a formatear.
	 * @return La cadena de fecha formateada, o null si la fecha de entrada es null.
	 */
	private static String formatDateAsString(Date date) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.format(date);
		}
		return null;
	}

	/**
	 * Capitaliza la primera letra de cada palabra en la cadena de entrada.
	 *
	 * @param input La cadena de entrada a capitalizar.
	 * @return La cadena de entrada con la primera letra de cada palabra en
	 *         mayúscula.
	 */
	private static String capitalizeFirstLetter(String input) {
	    StringBuilder result = new StringBuilder();

	    input = input.toLowerCase();
	    
	    boolean capitalizeNext = true;

	    for (char ch : input.toCharArray()) {
	        if (Character.isWhitespace(ch)) {
	            capitalizeNext = true;
	        } else if (capitalizeNext) {
	            ch = Character.toTitleCase(ch);
	            capitalizeNext = false;
	        }

	        result.append(ch);
	    }

	    return result.toString();
	}
}
