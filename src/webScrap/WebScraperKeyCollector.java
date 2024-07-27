package webScrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import alarmas.AlarmaList;
import comicManagement.Comic;
import javafx.scene.control.TextArea;

/**
 * Clase principal para realizar el web scraping de información específica de la
 * página web "https://www.previewsworld.com". Extrae detalles de un cómic, como
 * el título, número, creadores, imagen principal, fecha de salida, valor (SRP),
 * editorial y URL de referencia.
 */
public class WebScraperKeyCollector {

	public static int verificarCodigoRespuesta(String urlString) throws IOException, URISyntaxException {
		try {
			// Desactivar la validación del certificado SSL/TLS
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}
			} };

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			URI uri = new URI(urlString);
			HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.setRequestMethod("GET");

			// Obtener el código de respuesta HTTP
			int codigoRespuesta = connection.getResponseCode();

			// Cerrar la conexión
			connection.disconnect();

			return codigoRespuesta;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static String agregarMasAMayusculas(String cadena) {
		return cadena.toUpperCase();
	}

	public static String buscarEnGoogle(String searchTerm) {
		searchTerm = agregarMasAMayusculas(searchTerm);
		searchTerm = searchTerm.replace("(", "%28").replace(")", "%29").replace("#", "%23");

		try {
			String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
			String urlString = "https://www.google.com/search?q=" + encodedSearchTerm + "+marvel.com";
			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

			int responseCode = con.getResponseCode();
			if (responseCode == 429) {
				// Manejar el error de demasiadas solicitudes
				System.out.println("Demasiadas solicitudes. Esperando y reintentando...");
				Thread.sleep(5000); // Esperar 5 segundos antes de reintentar
				return buscarEnGoogle(searchTerm); // Reintentar la búsqueda
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

			String html = content.toString();
			int startIndex = html.indexOf("https://www.marvel.com/comics/issue/");
			if (startIndex != -1) {
				int endIndex = html.indexOf("\"", startIndex);
				String[] urls = html.substring(startIndex, endIndex).split("\"");
				String googleUrl = "https://www.marvel.com/comics/issue/";
				return encontrarURLRelevante(urls, searchTerm, googleUrl);
			} else {
				return null;
			}
		} catch (IOException | InterruptedException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encontrarURLRelevante(String[] urls, String searchTerm, String googleUrl) {
		String urlElegida = null;
		int minDifference = Integer.MAX_VALUE;
		for (String url : urls) {
			int difference = Math.abs(url.indexOf(googleUrl) - url.indexOf(url));
			if (difference < minDifference) {
				minDifference = difference;
				urlElegida = url;
			}
		}
		return urlElegida;
	}

	public static String referenciaUrl(String codigoBusqueda) {
		String url = "";
		if (codigoBusqueda.contains("www.marvel.com/comics/issue/")) {
			url = codigoBusqueda;
		} else {
			url = buscarEnGoogle(codigoBusqueda);
		}
		return url;
	}

	/**
	 * Realiza scraping en una URL dada para extraer información sobre un cómic.
	 * 
	 * @param url La URL de la página web del cómic.
	 * @return Un array de cadenas que contiene la información del cómic, o un array
	 *         vacío si ocurre un error.
	 * @throws URISyntaxException
	 */
	public static Comic displayComicInfo(String diamondCode, TextArea prontInfo) {

		try {
			String urlReferencia = referenciaUrl(diamondCode);
			int codigoRespuesta = verificarCodigoRespuesta(urlReferencia);

			if (codigoRespuesta == 404) {
				String mensaje = "No se encontró el cómic con Diamond Code: " + diamondCode;
				AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
				return null;
			}
			Document document = Jsoup.connect(urlReferencia).get();

			String titulo = scrapeTitle(document);
			String issueKey = scrapeDescription(document);
			String numero = extractNumeroFromTitle(document);
			// Scraping de la etiqueta div con class "SRP"
			String precio = scrapePrice(document);

			// Variables para almacenar los creadores
			String writer = scrapeGuionista(document);
			String artist = scrapeDibujante(document);
			String variant = scrapePortada(document);
			String codigoComic = scrapeUPC(document);

			// Scraping de la etiqueta div con class "ReleaseDate"
			String fecha = scrapeFechaPublicacion(document);

			// Scraping de la etiqueta img con id "MainContentImage"
			String portadaImagen = scrapeAndPrintMainImage(document);

			// Scraping de la etiqueta div con class "Publisher"
			String editorial = scrapeAndPrintPublisher();

			variant = Comic.limpiarCampo(variant);
			titulo = Comic.limpiarCampo(titulo);
			editorial = Comic.limpiarCampo(editorial);
			writer = Comic.limpiarCampo(writer);
			artist = Comic.limpiarCampo(artist);
			issueKey = Comic.limpiarCampo(issueKey);

			return new Comic.ComicGradeoBuilder("", titulo).codigoComic(codigoComic).precioComic(precio)
					.numeroComic(numero).fechaGradeo(fecha).editorComic(editorial).keyComentarios(issueKey)
					.firmaComic("").artistaComic(artist).guionistaComic(writer).varianteComic(variant)
					.direccionImagenComic(portadaImagen).urlReferenciaComic(urlReferencia).build();

		} catch (IOException | URISyntaxException e) {
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
	private static String scrapeTitle(Document document) {
		Element titleElement = document.selectFirst("span.gtOSm.FbbUW.tUtYa.vOCwz.EQwFq");
		if (titleElement != null) {
			String titleContent = titleElement.text().trim();
			titleContent = capitalizeTitle(titleContent);
			titleContent = removeHashtagAndFollowing(titleContent);

			return titleContent;
		}
		return null;
	}

	private static String scrapeDescription(Document document) {
		Element titleElement = document.selectFirst("span.ComicMasthead__Description.gtOSm");
		if (titleElement != null) {
			String titleContent = titleElement.text().trim();
			return titleContent;
		}
		return null;
	}

	private static String scrapeGuionista(Document document) {
		return scrapeMetaContent(document, "comic_masthead_meta_item-Writer");
	}

	private static String scrapeDibujante(Document document) {
		return scrapeMetaContent(document, "comic_masthead_meta_item-Penciller");
	}

	private static String scrapePortada(Document document) {
		return scrapeMetaContent(document, "comic_masthead_meta_item-Cover Artist");
	}

	private static String scrapeUPC(Document document) {
		Elements upcElements = document.select("li:contains(UPC)");
		for (Element element : upcElements) {
			Elements spans = element.select("span");
			if (spans.size() > 1 && spans.get(0).text().contains("UPC")) {
				return spans.get(1).text().trim();
			}
		}
		return "0";
	}

	private static String scrapePrice(Document document) {
		Elements priceElements = document.select("li:contains(Price)");
		for (Element element : priceElements) {
			Elements spans = element.select("span");
			if (spans.size() > 1 && spans.get(0).text().contains("Price")) {
				return spans.get(1).text().trim();
			}
		}
		return "NA";
	}

	private static String scrapeFechaPublicacion(Document document) {
		Element metaItem = document.selectFirst(
				"ul.ComicMasthead__Meta li:contains(Published) p[data-testid=comic_masthead_meta_content]");
		if (metaItem != null) {
			return convertirFechaMySQL(metaItem.text().trim());
		}
		return "2000-01-01";
	}

	private static String scrapeMetaContent(Document document, String dataTestId) {
		Elements metaItems = document.select("ul.ComicMasthead__Meta li[data-testid=" + dataTestId + "]");
		if (!metaItems.isEmpty()) {
			StringBuilder content = new StringBuilder();
			Elements creators = metaItems.first()
					.select("span.ComicMasthead__Meta_Creator a.ComicMasthead__Meta_Creator_Link");
			for (Element creator : creators) {
				if (content.length() > 0) {
					content.append(", ");
				}
				content.append(creator.text().trim());
			}
			return content.toString();
		}
		return "";
	}

	/**
	 * Elimina el símbolo "#" y todo lo que le sigue hasta un espacio, así como los
	 * paréntesis y todo lo que contienen, del contenido del título.
	 *
	 * @param titleContent El contenido del título a procesar.
	 * @return El contenido del título sin el "#" y el texto siguiente, ni los
	 *         paréntesis y su contenido.
	 */
	private static String removeHashtagAndFollowing(String titleContent) {
		// Elimina el símbolo "#" y todo lo que le sigue hasta un espacio
		if (titleContent.contains("#")) {
			int hashtagIndex = titleContent.indexOf('#');
			titleContent = titleContent.substring(0, hashtagIndex);
		}

		// Elimina los paréntesis y todo su contenido
		titleContent = titleContent.replaceAll("\\(.*?\\)", "");

		// Retorna el contenido procesado, en minúsculas y sin espacios extra al inicio
		// o al final
		return titleContent.trim();
	}

	/**
	 * Formatea el título capitalizando la primera letra de cada palabra.
	 *
	 * @param title El título a formatear.
	 * @return El título formateado con la primera letra de cada palabra en
	 *         mayúscula.
	 */
	private static String capitalizeTitle(String title) {
		// Divide el título en palabras
		title.toLowerCase();
		String[] words = title.split(" ");
		StringBuilder capitalizedTitle = new StringBuilder();

		for (String word : words) {
			if (word.length() > 0) {
				// Capitaliza la primera letra de cada palabra y añade el resto en minúsculas
				capitalizedTitle.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase())
						.append(" ");
			}
		}

		// Elimina el espacio extra al final
		return capitalizedTitle.toString().trim();
	}

	/**
	 * Extrae el número que sigue al símbolo "#" en el título, o el primer número
	 * encontrado si no existe el símbolo "#". Excluye cualquier texto entre
	 * paréntesis si existe y maneja números que comienzan con '0'.
	 *
	 * @param document El documento HTML del cual extraer.
	 * @return El número extraído o null si no se encuentra.
	 */
	private static String extractNumeroFromTitle(Document document) {
		Element titleElement = document.selectFirst("span.gtOSm.FbbUW.tUtYa.vOCwz.EQwFq");
		if (titleElement != null) {
			String titleContent = titleElement.text().trim();

			// Buscar el índice del símbolo "#"
			int hashtagIndex = titleContent.indexOf('#');
			if (hashtagIndex != -1 && hashtagIndex + 1 < titleContent.length()) {
				// Obtener la subcadena que sigue al símbolo "#"
				String subStringAfterHashtag = titleContent.substring(hashtagIndex + 1).trim();

				// Dividir el texto en partes usando espacio como delimitador
				String[] parts = subStringAfterHashtag.split("\\s+", 2); // divide en dos partes

				if (parts.length > 0) {
					String firstPart = parts[0].trim();

					// Limpiar cualquier carácter no numérico
					return firstPart.replaceAll("[^\\d]", "");
				}
			} else {
				// Si no hay símbolo "#", buscar el primer número en el texto
				java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\b(0*\\d+)\\b")
						.matcher(titleContent);
				if (matcher.find()) {
					String number = matcher.group(1).replaceFirst("^0+", ""); // Eliminar ceros a la izquierda
					return number.isEmpty() ? "0" : number; // Devolver "0" si el resultado está vacío
				}
			}
		}
		return "0";
	}

	private static String scrapeAndPrintMainImage(Document document) throws IOException {
		Element mainImageElement = document.selectFirst("img.hsDdd");
		if (mainImageElement != null) {
			return mainImageElement.attr("src");
		}
		return "";
	}

	/**
	 * Extrae e imprime el nombre del editor del documento.
	 *
	 * @param document El documento HTML a analizar.
	 * @return El nombre del editor en mayúsculas, o null si no se encuentra.
	 */
	private static String scrapeAndPrintPublisher() {
		return "Marvel";
	}

	public static String convertirFechaMySQL(String fechaSalida) {
		try {
			// Mapear nombres de los meses en inglés a números
			String[] meses = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
			// Parsear la fecha al formato adecuado
			SimpleDateFormat formatoEntrada = new SimpleDateFormat("MM dd, yyyy");
			// Remover el sufijo ordinal numérico antes de parsear
			fechaSalida = fechaSalida.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
			// Dividir la fecha en partes: mes, día, año
			String[] partesFecha = fechaSalida.split(" ");
			// Convertir el nombre del mes a un número
			int mesNum = -1;
			for (int i = 0; i < meses.length; i++) {
				if (meses[i].equals(partesFecha[0])) {
					mesNum = i + 1; // Añadir 1 porque los meses en SimpleDateFormat comienzan en 0
					break;
				}
			}
			// Formatear la fecha al formato MySQL
			SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
			if (partesFecha.length >= 3) {
				Date fecha = formatoEntrada
						.parse(String.format("%02d", mesNum) + " " + partesFecha[1] + " " + partesFecha[2]);
				return formatoSalida.format(fecha);
			} else {
				return "2000-01-01"; // Devuelve la fecha por defecto
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
