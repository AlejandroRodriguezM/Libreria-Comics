/**
 * Contiene las las clases que hacen funcionar las APIs
 *  
*/
package Apis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import alarmas.AlarmaList;
import apisFunciones.FuncionesApis;
import comicManagement.Comic;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextArea;
import javafx.scene.control.TextArea;

/**
 * Esta clase demuestra cómo buscar información de un libro utilizando el ISBN a
 * través de la API de OpenLibrary.
 */
public class ApiISBNGeneral {

	/**
	 * Obtiene información sobre un libro a través de su ISBN y lo devuelve en forma
	 * de un array de cadenas.
	 *
	 * @param isbn      El ISBN del libro.
	 * @param prontInfo El componente TextArea utilizado para mostrar información.
	 * @return Un array de cadenas que contiene información sobre el libro, o null
	 *         si no se encuentra el libro.
	 * @throws IOException        Si ocurre un error de entrada/salida al hacer la
	 *                            solicitud HTTP.
	 * @throws JSONException      Si ocurre un error al analizar la respuesta JSON.
	 * @throws URISyntaxException Si ocurre un error al construir la URI.
	 */
	public Comic getBookInfo(String isbn, TextArea prontInfo) throws IOException, JSONException, URISyntaxException {
		String apiUrl = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&jscmd=details&format=json";
		String apiKey = FuncionesApis.cargarApiComicVine();

		if(isbn.length() != 13) {
			return null;
		}
		
		try {
			String jsonResponse = sendHttpGetRequest(apiUrl);
			JSONObject jsonObject = new JSONObject(jsonResponse);

			// Verifica si bookInfo es nulo antes de intentar acceder a sus propiedades.
			JSONObject bookInfo = jsonObject.optJSONObject("ISBN:" + isbn);

			if (jsonObject.length() == 0 || bookInfo.length() == 0) {
			    if (prontInfo != null) {
			        String mensaje = "No se encontró el cómic con código: " + isbn;

			        AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			    }

			    return null;
			}

			// Ahora puedes acceder a las propiedades de bookInfo, ya que se ha verificado
			// que no es nulo.
			JSONObject details = bookInfo.getJSONObject("details");

			printJson(jsonObject, "");

			// Extraer otros datos (sigue el patrón del ejemplo proporcionado)

			Map<String, List<String>> autoresSeparados = obtenerAutoresSeparados(details, apiKey);
			List<String> artistasJson = autoresSeparados.get("artistas");
			List<String> escritoresJson = autoresSeparados.get("escritores");
			String artistas = "";
			String escritores = "";

			for (String a : artistasJson) {

				artistas += a + ",";
			}

			for (String e : escritoresJson) {
				escritores += e + ",";
			}

			String nombre = extraerTitulo(details);
			String issueKey = extraerDescripcion(details);
			String numero = "0";
			String precio = "0";
			String variant = "";
			String formato = extraerFormato(details);
			String fecha = extraerFechaPublicacion(details);
			String referencia_Url = extraerReferencia(bookInfo);
			String portadaImagen = extraerImagen(bookInfo);
			String editorial = extraerEditorial(details);

			Comic comic = new Comic("", nombre, "0", numero, variant, "", editorial, formato,
					"Estados Unidos (United States)", fecha, escritores, artistas, "En posesion", issueKey,
					"Sin puntuacion", portadaImagen, referencia_Url, precio, isbn);

			return comic;
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Extrae y procesa el título del libro a partir de un objeto JSON.
	 * 
	 * @param details Objeto JSON con información del libro.
	 * @return Título del libro procesado.
	 * @throws JSONException Si hay un error al manejar el objeto JSON.
	 */
	private String extraerTitulo(JSONObject details) throws JSONException {
		String titleKey = details.has("full_title") ? "full_title" : "title";

		if (details.has(titleKey)) {
			String title = details.getString(titleKey);
			title = title.replaceAll("\\([^\\)]*\\)", "").replaceAll("#\\d+\\s", "").replaceAll("#\\d+", "").trim();

			if (!title.isEmpty()) {
				title = capitalizarPalabrasConGuion(capitalizarPalabrasConEspacio(title).trim()).trim();
				return title;
			}
		}

		return "";
	}

	/**
	 * Extrae la descripción del libro a partir de un objeto JSON.
	 * 
	 * @param details Objeto JSON con información del libro.
	 * @return Descripción del libro.
	 * @throws JSONException Si hay un error al manejar el objeto JSON.
	 */
	private String extraerDescripcion(JSONObject details) throws JSONException {

		if (details.has("description")) {
			return details.getString("description");
		} else {
			return "";
		}
	}

	/**
	 * Extrae la editorial del libro a partir de un objeto JSON.
	 * 
	 * @param details Objeto JSON con información del libro.
	 * @return Editorial del libro.
	 * @throws JSONException Si hay un error al manejar el objeto JSON.
	 */
	private String extraerEditorial(JSONObject details) throws JSONException {

		String editorial = "";

		if (details.has("publishers")) {
			JSONArray publishersArray = details.getJSONArray("publishers");
			if (publishersArray.length() > 0) {
				String publisher = publishersArray.getString(0);
				editorial += capitalizarPalabrasConEspacio(publisher);
			}
		}
		return editorial;
	}

	/**
	 * Extrae la URL de la imagen del libro a partir de un objeto JSON.
	 * 
	 * @param bookInfo Objeto JSON con información del libro.
	 * @return URL de la imagen del libro.
	 * @throws JSONException Si hay un error al manejar el objeto JSON.
	 */
	private String extraerImagen(JSONObject bookInfo) throws JSONException {
		if (bookInfo.has("thumbnail_url")) {
			String thumbnailUrl = bookInfo.getString("thumbnail_url");
			thumbnailUrl = thumbnailUrl.replace("-S.jpg", "-L.jpg").replace("-M.jpg", "-L.jpg");
			return thumbnailUrl;
		} else {
			// No hay thumbnail_url, agregar la ruta de la imagen predeterminada desde los
			// recursos
			String rutaImagen = "/Funcionamiento/sinPortada.jpg";
			URL url = getClass().getResource(rutaImagen);
			if (url != null) {
				return url.toExternalForm();
			}
		}
		return "";
	}

	/**
	 * Extrae la referencia del libro a partir de un objeto JSON.
	 * 
	 * @param bookInfo Objeto JSON con información del libro.
	 * @return Referencia del libro.
	 * @throws JSONException Si hay un error al manejar el objeto JSON.
	 */
	private String extraerReferencia(JSONObject bookInfo) throws JSONException {
		// Referencia
		if (bookInfo.has("info_url")) {
			String thumbnailUrl = bookInfo.getString("info_url");
			return thumbnailUrl;
		} else {
			return "Sin referencia";
		}
	}

	/**
	 * Extrae la fecha de publicación del libro a partir de un objeto JSON.
	 * 
	 * @param details Objeto JSON con información del libro.
	 * @return Fecha de publicación del libro.
	 * @throws JSONException Si hay un error al manejar el objeto JSON.
	 */
	private String extraerFechaPublicacion(JSONObject details) throws JSONException {
		if (details.has("created")) {
			JSONObject createdObject = details.getJSONObject("created");
			if (createdObject.has("value")) {
				String publishDate = createdObject.getString("value");

				return convertirFecha(publishDate);
			}
		} else {
			return "2000-01-01";
		}
		return "";
	}

	/**
	 * Obtiene los nombres de los autores del libro y los clasifica como artistas o
	 * escritores.
	 * 
	 * @param details Objeto JSON con información del libro.
	 * @param apiKey  Clave de API para realizar consultas externas.
	 * @return Mapa que contiene listas de artistas y escritores.
	 * @throws JSONException      Si hay un error al manejar el objeto JSON.
	 * @throws IOException        Si hay un error de entrada/salida.
	 * @throws URISyntaxException Si hay un error en la sintaxis de la URI.
	 */
	private Map<String, List<String>> obtenerAutoresSeparados(JSONObject details, String apiKey)
			throws JSONException, IOException, URISyntaxException {
		Map<String, List<String>> autoresSeparados = new HashMap<>();
		autoresSeparados.put("artistas", new ArrayList<>());
		autoresSeparados.put("escritores", new ArrayList<>());

		if (details.has("authors") && details.get("authors") instanceof JSONArray) {
			JSONArray authors = details.getJSONArray("authors");

			for (int i = 0; i < authors.length(); i++) {
				JSONObject author = authors.getJSONObject(i);
				String authorName = author.getString("name");

				// Filtrar caracteres no deseados y agregar coma si no es el último autor
				authorName = authorName.replaceAll("[^\\p{L},.? ]", "").trim();

				if (!authorName.isEmpty()) {
					authorName = ApiGoogle.translateText(authorName, "en");

					// Llamar a la función para buscar información de la persona
					String result = ApiComicVine.searchPersonAndExtractInfo(apiKey, authorName);

					// Verificar si el resultado es "Artist" o "Writer" y hacer append en las
					// variables
					if (result != null) {
						if (result.equals("artist")) {
							autoresSeparados.get("artistas").add(authorName);
						} else if (result.equals("writer")) {
							autoresSeparados.get("escritores").add(authorName);
						}
					}
				}
			}
		}

		return autoresSeparados;
	}

	/**
	 * Extrae el formato físico del libro a partir de un objeto JSON.
	 * 
	 * @param details Objeto JSON con información del libro.
	 * @return Formato físico del libro.
	 * @throws JSONException Si hay un error al manejar el objeto JSON.
	 */
	private String extraerFormato(JSONObject details) throws JSONException {
		if (details.has("physical_format")) {
			String physicalFormat = details.getString("physical_format");

			if (physicalFormat.equalsIgnoreCase("Comic")) {
				return "Grapa (Issue individual)";
			} else if (physicalFormat.equalsIgnoreCase("Hardcover")) {
				return "Tapa dura (Hardcover)";
			} else if (physicalFormat.equalsIgnoreCase("Trade Paperback")) {
				return "Tapa blanda (Paperback)";
			} else {
				return physicalFormat;
			}
		}
		return null;
	}

	/**
	 * Imprime un objeto JSON de forma recursiva con un prefijo.
	 *
	 * @param json   El objeto JSON que se imprimirá.
	 * @param prefix El prefijo que se agregará a cada línea impresa.
	 * @throws JSONException Si ocurre un error al manipular el objeto JSON.
	 */
	public static void printJson(JSONObject json, String prefix) throws JSONException {
		Iterator<?> keys = json.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next(); // Casting seguro a String
			Object value = json.get(key);
			if (value instanceof JSONObject) {
				// If it's a JSON object, call the function recursively
				printJson((JSONObject) value, prefix + "  ");
			} else if (value instanceof JSONArray) {
				// If it's an array, print each item in the array
				JSONArray jsonArray = (JSONArray) value;
				for (int i = 0; i < jsonArray.length(); i++) {
					Object arrayItem = jsonArray.get(i);
					if (arrayItem instanceof JSONObject) {
						// Check if the array item is a JSON object
						printJson((JSONObject) arrayItem, prefix + "  - ");
					}
				}
			}
		}
	}

	/**
	 * Envía una solicitud HTTP GET a una URL y devuelve la respuesta como una
	 * cadena.
	 *
	 * @param apiUrl La URL a la que se debe enviar la solicitud GET.
	 * @return La respuesta HTTP como una cadena JSON.
	 * @throws IOException        Si ocurre un error al realizar la solicitud HTTP.
	 * @throws URISyntaxException Si la URL es incorrecta.
	 */
	private static String sendHttpGetRequest(String apiUrl) throws IOException, URISyntaxException {
		URI uri = new URI(apiUrl); // Use .toURI() instead of .toURL()
		URL url = uri.toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			return response.toString();
		} finally {
			connection.disconnect();
		}
	}

	/**
	 * Capitaliza la primera letra de cada palabra en una cadena que contiene
	 * guiones como separadores.
	 * 
	 * @param input La cadena de entrada que se va a capitalizar.
	 * @return La cadena con la primera letra de cada palabra en mayúscula.
	 */
	public static String capitalizarPalabrasConGuion(String input) {
		// Dividir la cadena en palabras utilizando guiones como separadores
		String[] palabras = input.split("-");
		StringBuilder resultado = new StringBuilder();

		for (int i = 0; i < palabras.length; i++) {
			String palabra = palabras[i];
			if (!palabra.isEmpty()) {
				// Capitalizar la primera letra de la palabra y agregar el resto en minúsculas
				palabra = palabra.substring(0, 1).toUpperCase() + palabra.substring(1).toLowerCase();
				resultado.append(palabra);
				// Agregar un guión solo si no es la última palabra
				if (i < palabras.length - 1) {
					resultado.append("-");
				}
			}
		}

		return resultado.toString();
	}

	/**
	 * Capitaliza la primera letra de cada palabra en una cadena que contiene
	 * espacios como separadores.
	 * 
	 * @param input La cadena de entrada que se va a capitalizar.
	 * @return La cadena con la primera letra de cada palabra en mayúscula.
	 */
	public static String capitalizarPalabrasConEspacio(String input) {
		// Dividir la cadena en palabras utilizando espacios como separadores
		String[] palabras = input.split(" ");
		StringBuilder resultado = new StringBuilder();

		for (String palabra : palabras) {
			if (!palabra.isEmpty()) {
				// Capitalizar la primera letra de la palabra y agregar el resto en minúsculas
				palabra = palabra.substring(0, 1).toUpperCase() + palabra.substring(1).toLowerCase();
				resultado.append(palabra).append(" ");
			}
		}

		// Eliminar el espacio adicional al final y devolver la cadena capitalizada
		return resultado.toString().trim();
	}

	/**
	 * Convierte una fecha en formato de texto en otro formato deseado.
	 *
	 * @param fechaEnTexto La fecha en formato de texto que se desea convertir.
	 * @return La fecha formateada en el formato deseado o null si ocurre un error.
	 */
	public static String convertirFecha(String fechaEnTexto) {
		try {
			// Verificar si la fecha proporcionada contiene solo el año
			if (fechaEnTexto.matches("^\\d{4}$")) {
				fechaEnTexto = "Jan 1, " + fechaEnTexto; // Si es solo un año, agrega el mes y el día
			}

			// Verificar si la fecha tiene el formato ISO 8601
			// ("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
			if (fechaEnTexto.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+$")) {
				SimpleDateFormat formatoEntradaISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
				SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");

				// Convierte la fecha en formato ISO 8601 a un objeto Date
				Date fecha = formatoEntradaISO8601.parse(fechaEnTexto);

				// Formatea la fecha como una cadena en el formato deseado
				String fechaFormateada = formatoSalida.format(fecha);
				return fechaFormateada;
			}

			// Obtén el número de mes correspondiente a las tres primeras letras del mes
			String mesEnTexto = fechaEnTexto.substring(0, 3);
			int numeroDeMes = obtenerNumeroDeMes(mesEnTexto);

			// Define el formato de entrada para la fecha actual (sin las tres primeras
			// letras del mes)
			SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd, yyyy");

			// Convierte la fecha en formato de texto (sin las tres primeras letras del mes)
			// a un objeto Date
			Date fecha = formatoEntrada.parse(fechaEnTexto.substring(4));

			// Establece el número de mes en el objeto Date
			// Crear un objeto Calendar y establecer la fecha
			Calendar cal = Calendar.getInstance();
			cal.setTime(fecha);

			// Establecer el mes restando 1
			cal.set(Calendar.MONTH, numeroDeMes - 1);

			// Define el formato de salida deseado para la fecha
			SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");

			// Formatea la fecha como una cadena en el formato deseado
			String fechaFormateada = formatoSalida.format(fecha);

			return fechaFormateada;
		} catch (ParseException e) {
			e.printStackTrace();
			return null; // Manejo de error en caso de que la conversión falle
		}
	}

	/**
	 * Convierte las tres primeras letras del nombre de un mes en su número
	 * correspondiente.
	 *
	 * @param mesEnTexto Las tres primeras letras del nombre del mes (en
	 *                   minúsculas).
	 * @return El número del mes correspondiente (1 para enero, 2 para febrero,
	 *         etc.), o -1 si es inválido.
	 */
	public static int obtenerNumeroDeMes(String mesEnTexto) {
		// Convierte las tres primeras letras del mes en su número correspondiente
		switch (mesEnTexto.toLowerCase()) {
		case "jan":
			return 1;
		case "feb":
			return 2;
		case "mar":
			return 3;
		case "apr":
			return 4;
		case "may":
			return 5;
		case "jun":
			return 6;
		case "jul":
			return 7;
		case "aug":
			return 8;
		case "sep":
			return 9;
		case "oct":
			return 10;
		case "nov":
			return 11;
		case "dec":
			return 12;
		default:
			return -1; // Valor inválido para el mes
		}
	}

}
