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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Funcionamiento.Utilidades;
import javafx.scene.control.TextArea;

/**
 * Clase que proporciona métodos para interactuar con la API de Marvel Comics y
 * obtener información sobre cómics. Esta clase incluye funciones para consultar
 * detalles de cómics a través de ISBN y UPC, y para mostrar la información de
 * los cómics en un formato estructurado.
 */
public class ApiMarvel {

	/**
	 * Obtiene información de un cómic a través de su ISBN y muestra los detalles en
	 * un TextArea.
	 *
	 * @param isbn      El ISBN del cómic que se desea buscar.
	 * @param prontInfo El TextArea en el que se mostrarán los resultados.
	 * @return Un array de cadenas con la información del cómic o null si no se
	 *         encuentra.
	 */
	public static String[] infoComicIsbn(String isbn, TextArea prontInfo) {

		JSONObject comic = getComicInfo(isbn, "isbn", prontInfo);

		return displayComicInfo(comic);

	}

	/**
	 * Obtiene información de un cómic a través de su UPC y muestra los detalles en
	 * un TextArea.
	 *
	 * @param upc       El UPC del cómic que se desea buscar.
	 * @param prontInfo El TextArea en el que se mostrarán los resultados.
	 * @return Un array de cadenas con la información del cómic o null si no se
	 *         encuentra.
	 */
	public static String[] infoComicUpc(String upc, TextArea prontInfo) {

		JSONObject comic = getComicInfo(upc, "upc", prontInfo);

		return displayComicInfo(comic);

	}

	/**
	 * Realiza una solicitud HTTP para obtener información de un cómic desde la API
	 * de Marvel Comics.
	 *
	 * @param claveComic El código del cómic (ISBN o UPC) que se desea buscar.
	 * @param tipoUrl    El tipo de URL a construir ("isbn" o "upc").
	 * @param prontInfo  El TextArea en el que se mostrarán los resultados o
	 *                   mensajes de error.
	 * @return Un objeto JSON con información del cómic o null si no se encuentra.
	 */
	private static JSONObject getComicInfo(String claveComic, String tipoUrl, TextArea prontInfo) {

		long timestamp = System.currentTimeMillis() / 1000;

		String claves[] = clavesApi();

		String clave_publica = claves[1].trim();

		String apiUrl = "";

		if (tipoUrl.equalsIgnoreCase("isbn")) {
			apiUrl = "https://gateway.marvel.com:443/v1/public/comics?isbn=" + claveComic + "&apikey=" + clave_publica
					+ "&hash=" + newHash(timestamp) + "&ts=" + timestamp;
		} else {
			apiUrl = "https://gateway.marvel.com:443/v1/public/comics?upc=" + claveComic + "&apikey=" + clave_publica
					+ "&hash=" + newHash(timestamp) + "&ts=" + timestamp;
		}

		// Realiza la solicitud HTTP GET
		String jsonResponse;
		try {
			jsonResponse = sendHttpGetRequest(apiUrl);

			// Parsea la respuesta JSON y obtén el cómic
			JSONObject jsonObject = new JSONObject(jsonResponse);
			JSONArray resultsArray = jsonObject.getJSONObject("data").getJSONArray("results");

			if (resultsArray.length() == 0) {
				prontInfo.setOpacity(1);
				prontInfo.setText("No se encontró el cómic con codigo: " + claveComic);
				return null;
			} else {
				return resultsArray.getJSONObject(0); // Devuelve el primer cómic encontrado
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Calcula un nuevo hash a partir de un timestamp.
	 *
	 * @param timestamp El timestamp utilizado en la generación del hash.
	 * @return El hash MD5 calculado.
	 */
	private static String newHash(long timestamp) {
		String hash = getHash(timestamp);
		return hash;
	}

	/**
	 * Realiza una solicitud HTTP GET y obtiene la respuesta como una cadena de
	 * texto.
	 *
	 * @param apiUrl La URL de la API a la que se realiza la solicitud.
	 * @return La respuesta de la solicitud HTTP como una cadena de texto.
	 * @throws IOException        Si ocurre un error de entrada/salida durante la
	 *                            solicitud.
	 * @throws URISyntaxException Si la URL de la API es inválida.
	 */
	private static String sendHttpGetRequest(String apiUrl) throws IOException, URISyntaxException {
		URI url = new URI(apiUrl); // Create a URI from the API URL
		HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
		connection.setRequestMethod("GET");

		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder response = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null) {
			response.append(line);
		}

		reader.close();
		return response.toString();
	}

	/**
	 * Calcula un hash MD5 a partir de una cadena de entrada.
	 *
	 * @param input La cadena de entrada para la cual se calculará el hash.
	 * @return El hash MD5 calculado.
	 */
	private static String getHash(long timestamp) {
		String claves[] = clavesApi();

		String clavePrivada = claves[0].trim();

		String clavePublica = claves[1].trim();

		return md5(timestamp + clavePrivada + clavePublica);
	}

	/**
	 * Obtiene las claves de la API de un archivo o fuente de datos.
	 *
	 * @return Un array de cadenas con las claves pública y privada de la API.
	 */
	private static String[] clavesApi() {
		String claves[] = new String[2]; // Crear un arreglo de dos elementos para almacenar las claves

		String clavesDesdeArchivo = Utilidades.obtenerClaveApiArchivo(); // Obtener las claves desde el archivo

		if (!clavesDesdeArchivo.isEmpty()) {
			String[] partes = clavesDesdeArchivo.split(":");
			if (partes.length == 2) {
				String clavePublica = partes[0].trim();
				String clavePrivada = partes[1].trim();

				claves[0] = clavePublica; // Almacenar la clave pública en el primer elemento del arreglo
				claves[1] = clavePrivada; // Almacenar la clave privada en el segundo elemento del arreglo
			}
		}

		return claves;
	}

	/**
	 * Calcula el hash MD5 de una cadena de entrada utilizando Apache Commons Codec.
	 *
	 * @param input La cadena de entrada para la cual se calculará el hash MD5.
	 * @return El hash MD5 calculado como una cadena de 32 caracteres hexadecimales.
	 */
	private static String md5(String input) {
		// Utiliza Apache Commons Codec para calcular el hash MD5
		return DigestUtils.md5Hex(input);
	}

	/**
	 * Obtiene información detallada sobre un cómic a partir de un objeto JSON y la
	 * organiza en un array de cadenas.
	 *
	 * @param comic El objeto JSON que contiene la información del cómic.
	 * @return Un array de cadenas con la información detallada del cómic.
	 */
	public static String[] displayComicInfo(JSONObject comic) {
		List<String> comicInfoList = new ArrayList<>();

		try {
			// Título
			String title = comic.getString("title");
			title = title.replaceAll("\\([^\\)]*\\)", "");
			title = title.replaceAll("#\\d+\\s", "");
			title = title.replaceAll("#\\d+", "").trim();
			comicInfoList.add(title);

			// Número de edición
			int issueNumber = comic.getInt("issueNumber");
			comicInfoList.add(Integer.toString(issueNumber));

			// Formato
			String format = comic.getString("format");
			String formato;
			if (format.equalsIgnoreCase("Comic")) {
				formato = "Grapa (Issue individual)";
			} else if (format.equalsIgnoreCase("Hardcover")) {
				formato = "Tapa dura (Hardcover)";
			} else if (format.equalsIgnoreCase("Trade Paperback")) {
				formato = "Tapa blanda (Paperback)";
			} else {
				formato = format;
			}
			comicInfoList.add(formato);

			// Precio
			JSONArray pricesArray = comic.getJSONArray("prices");
			if (pricesArray.length() > 0) {
				JSONObject priceObject = pricesArray.getJSONObject(0);
				float price = (float) priceObject.getDouble("price");
				comicInfoList.add(Float.toString(price));
			}

			// Creadores
			JSONArray creatorsArray = comic.getJSONObject("creators").getJSONArray("items");
			List<String> pencillers = new ArrayList<>();
			List<String> writers = new ArrayList<>();
			List<String> coverPencillers = new ArrayList<>();

			for (int i = 0; i < creatorsArray.length(); i++) {
				JSONObject creator = creatorsArray.getJSONObject(i);
				String creatorName = creator.getString("name");
				String creatorRole = creator.getString("role");

				if (creatorRole.equals("penciler") || creatorRole.equals("penciller") || creatorRole.equals("artist")) {
					pencillers.add(creatorName);
				} else if (creatorRole.equals("writer")) {
					writers.add(creatorName);
				} else if (creatorRole.equals("penciller (cover)") || creatorRole.equals("penciler (cover)")) {
					coverPencillers.add(creatorName);
				}
			}

			comicInfoList.add(String.join(", ", coverPencillers));
			comicInfoList.add(String.join(", ", pencillers));
			comicInfoList.add(String.join(", ", writers));

			// Fecha de venta
			JSONArray datesArray = comic.getJSONArray("dates");
			String onsaleDate = "";

			for (int i = 0; i < datesArray.length(); i++) {
				JSONObject dateObject = datesArray.getJSONObject(i);
				String dateType = dateObject.getString("type");
				String date = dateObject.getString("date");

				if (dateType.equals("onsaleDate")) {
					String[] parts = date.split("T");
					String[] dateParts = parts[0].split("-");
					String year = dateParts[0];
					String month = dateParts[1];
					String day = dateParts[2];
					onsaleDate = year + "-" + month + "-" + day;
					break;
				}
			}

			comicInfoList.add(onsaleDate);

			// URL de referencia
			JSONArray urlsArray = comic.getJSONArray("urls");
			String detailURL = "";

			for (int i = 0; i < urlsArray.length(); i++) {
				JSONObject urlObject = urlsArray.getJSONObject(i);
				String urlType = urlObject.getString("type");
				String url = urlObject.getString("url");

				if (urlType.equals("detail")) {
					detailURL = url;
					break;
				}
			}

			comicInfoList.add(detailURL);

			// URL de la imagen representativa
			JSONObject thumbnailObject = comic.getJSONObject("thumbnail");
			String path = thumbnailObject.getString("path");
			String extension = thumbnailObject.getString("extension");
			String thumbnailURL = path + "." + extension;

			comicInfoList.add(thumbnailURL);

			// Editorial (En este caso, siempre es "Marvel")
			comicInfoList.add("Marvel");

			String descripcion = comic.optString("description");

			if (!descripcion.isEmpty()) {
				comicInfoList.add(descripcion);
			}

			// Convierte la lista en un array de cadenas
			String[] comicInfoArray = new String[comicInfoList.size()];
			comicInfoList.toArray(comicInfoArray);

			return comicInfoArray;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new String[0];
	}

}
