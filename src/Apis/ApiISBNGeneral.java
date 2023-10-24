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
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Funcionamiento.Utilidades;
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
	public static String[] getBookInfo(String isbn, TextArea prontInfo)
			throws IOException, JSONException, URISyntaxException {
		String apiUrl = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&jscmd=details&format=json";
		String apiKey = Utilidades.cargarApiComicVine();

		try {
			String jsonResponse = sendHttpGetRequest(apiUrl);
			JSONObject jsonObject = new JSONObject(jsonResponse);

			// Verifica si bookInfo es nulo antes de intentar acceder a sus propiedades.
			JSONObject bookInfo = jsonObject.optJSONObject("ISBN:" + isbn);

			if (bookInfo.length() == 0) {
				// Configura la visibilidad y el texto de un elemento (prontInfo) para informar
				// que no se encontró el cómic.
				prontInfo.setOpacity(1);
				prontInfo.setText("No se encontró el cómic con código: " + isbn);

				return null;
			}

			// Ahora puedes acceder a las propiedades de bookInfo, ya que se ha verificado
			// que no es nulo.
			JSONObject details = bookInfo.getJSONObject("details");

			printJson(jsonObject, "");

			// Declarar una lista para almacenar los datos
			List<String> bookInfoList = new ArrayList<>();

			// Extraer otros datos (sigue el patrón del ejemplo proporcionado)

			if (details.has("full_title")) {
				String title = details.getString("full_title");
				title = title.replaceAll("\\([^\\)]*\\)", "");
				title = title.replaceAll("#\\d+\\s", "");
				title = title.replaceAll("#\\d+", "").trim();

				bookInfoList.add(capitalizarPalabrasConGuion(capitalizarPalabrasConEspacio(title).trim()).trim());
			} else {
				String title = details.getString("title");

				if (title.isEmpty()) {
					title = "";
				}

				title = title.replaceAll("\\([^\\)]*\\)", "");
				title = title.replaceAll("#\\d+\\s", "");
				title = title.replaceAll("#\\d+", "").trim();

				bookInfoList.add(capitalizarPalabrasConEspacio(title).trim());
			}

			if (details.has("description")) {
				String description = details.getString("description");
				bookInfoList.add(description);
			} else {
				bookInfoList.add("");
			}

			String numero = "0";
			bookInfoList.add(numero);

			if (details.has("physical_format")) {
				String physicalFormat = details.getString("physical_format");
				String formato;
				if (physicalFormat.equalsIgnoreCase("Comic")) {
					formato = "Grapa (Issue individual)";
				} else if (physicalFormat.equalsIgnoreCase("Hardcover")) {
					formato = "Tapa dura (Hardcover)";
				} else if (physicalFormat.equalsIgnoreCase("Trade Paperback")) {
					formato = "Tapa blanda (Paperback)";
				} else {
					formato = physicalFormat;
				}

				bookInfoList.add(formato);
			} else {
				bookInfoList.add("");
			}

			String precio = "0";
			bookInfoList.add(precio);

			String variante = "";
			bookInfoList.add(variante);

			// Extraer autores
			JSONArray authors = details.getJSONArray("authors");
			StringBuilder authorsString = new StringBuilder();

			String artistas = "";
			String escritores = "";

			for (int i = 0; i < authors.length(); i++) {
				JSONObject author = authors.getJSONObject(i);
				String authorName = author.getString("name");

				// Filtrar caracteres no deseados y agregar coma si no es el último autor
				authorName = authorName.replaceAll("[^\\p{L},.? ]", "").trim();
				if (!authorName.isEmpty()) {
					if (authorsString.length() > 0) {
						authorsString.append(", ");
					}
					authorName = ApiGoogle.translateText(authorName, "en");
					authorsString.append(authorName);

					// Llamar a la función para buscar información de la persona
					String result = ApiComicVine.searchPersonAndExtractInfo(apiKey, authorName);

					// Verificar si el resultado es "Artist" o "Writer" y hacer append en las
					// variables
					if (result != null) {
						if (result.equals("artist")) {
							if (!artistas.isEmpty()) {
								artistas += ", ";
							}
							artistas += authorName;
						} else if (result.equals("writer")) {
							if (!escritores.isEmpty()) {
								escritores += ", ";
							}
							escritores += authorName;
						}
					}
				}
			}

			// Dibujantes
			bookInfoList.add(artistas);

			// Escritores
			bookInfoList.add(escritores);

			if (details.has("created")) {
				JSONObject createdObject = details.getJSONObject("created");
				if (createdObject.has("value")) {
					String publishDate = createdObject.getString("value");

					bookInfoList.add(convertirFecha(publishDate));
				}
			} else {
				bookInfoList.add("2000-01-01");
			}

			// Referencia
			if (bookInfo.has("info_url")) {
				String thumbnailUrl = bookInfo.getString("info_url");
				bookInfoList.add(thumbnailUrl);
			} else {
				bookInfoList.add("Sin referencia");
			}

			if (bookInfo.has("thumbnail_url")) {
				String thumbnailUrl = bookInfo.getString("thumbnail_url");
				thumbnailUrl = thumbnailUrl.replace("-S.jpg", "-L.jpg").replace("-M.jpg", "-L.jpg");
				
				System.out.println("URL de la imagen: " + thumbnailUrl);
				
				bookInfoList.add(thumbnailUrl);
			} else {
				bookInfoList.add("");
			}

			if (details.has("publishers")) {
				JSONArray publishersArray = details.getJSONArray("publishers");
				if (publishersArray.length() > 0) {
					String publisher = publishersArray.getString(0);
					bookInfoList.add(capitalizarPalabrasConEspacio(publisher));
				}
			}

			// Convierte la lista en un array de cadenas
			String[] bookInfoArray = new String[bookInfoList.size()];

			bookInfoList.toArray(bookInfoArray);

			return bookInfoArray;
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			throw e;
		}
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
