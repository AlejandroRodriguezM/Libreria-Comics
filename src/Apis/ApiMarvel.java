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
import java.util.Iterator;
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
	 * Obtiene información de un cómic a través de su codigo y muestra los detalles
	 * en un TextArea.
	 *
	 * @param isbn      El ISBN del cómic que se desea buscar.
	 * @param prontInfo El TextArea en el que se mostrarán los resultados.
	 * @return Un array de cadenas con la información del cómic o null si no se
	 *         encuentra.
	 */
	public static String[] infoComicCode(String comicCode, TextArea prontInfo) {

		JSONObject comic = null;
		String cleanedCode = comicCode.replaceAll("[^0-9]", "");
		if (cleanedCode.length() == 13) {
			String formattedIsbn = formatIsbn(cleanedCode);
			comic = getComicInfo(formattedIsbn, "isbn", prontInfo);
		} else {
			comic = getComicInfo(cleanedCode, "upc", prontInfo);
		}

		contenidoJson(comic, 0);
		return displayComicInfo(comic);
	}

	/**
	 * Formatea un ISBN agregando guiones de acuerdo al formato estándar. Por
	 * ejemplo, convierte "9781302948207" en "978-1-302-94820-7".
	 *
	 * @param isbn El ISBN que se va a formatear.
	 * @return El ISBN formateado con guiones.
	 */
	public static String formatIsbn(String isbn) {

		StringBuilder formattedIsbn = new StringBuilder();

		for (int i = 0; i < isbn.length(); i++) {
			char digit = isbn.charAt(i);
			// Agregar guiones según el formato estándar del ISBN
			if ((i == 3) || (i == 4) || (i == 7) || (i == 12)) {
				formattedIsbn.append('-');
			}

			formattedIsbn.append(digit);

		}

		return formattedIsbn.toString();
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
		
		String claves[] = Utilidades.clavesApiMarvel();

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
	
	@SuppressWarnings("unused")
	private static JSONObject getComicInfoUrl(String claveComic, String url, TextArea prontInfo) {

		long timestamp = System.currentTimeMillis() / 1000;

		String claves[] = clavesApi();

		String clave_publica = claves[1].trim();

		String apiUrl = "";

		if (!url.isEmpty()) {
			apiUrl = "https://gateway.marvel.com:443/v1/public/comics?urls=" + claveComic + "&apikey=" + clave_publica
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
		String claves[] = Utilidades.clavesApiMarvel();

		String clavePrivada = claves[0].trim();

		String clavePublica = claves[1].trim();

		return md5(timestamp + clavePrivada + clavePublica);
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
	 * muestra
	 *
	 * @param comic El objeto JSON que contiene la información del cómic.
	 */
	public static void contenidoJson(JSONObject comic, int nivel) {
		try {
			@SuppressWarnings("unchecked")
			Iterator<String> keys = comic.keys();

			while (keys.hasNext()) {
				String key = keys.next();
				Object value = comic.get(key);

				for (int i = 0; i < nivel; i++) {
					System.out.print("\t");
				}
				System.out.print(key + ": ");

				if (value instanceof JSONObject) {
					// Si el valor es un objeto JSON, llama recursivamente a la función
					System.out.println("{");
					contenidoJson((JSONObject) value, nivel + 1);
					for (int i = 0; i < nivel; i++) {
						System.out.print("\t");
					}
					System.out.println("}");
				} else if (value instanceof JSONArray) {
					// Si el valor es una matriz JSON, itera a través de ella
					JSONArray jsonArray = (JSONArray) value;
					System.out.println();
					for (int i = 0; i < jsonArray.length(); i++) {
						Object arrayValue = jsonArray.get(i);
						if (arrayValue instanceof JSONObject) {
							// Si el elemento de la matriz es un objeto JSON, llama recursivamente a la
							// función
							contenidoJson((JSONObject) arrayValue, nivel + 1);
							if (i < jsonArray.length() - 1) {
								System.out.println();
							}
						} else {
							// Si el elemento es de otro tipo, simplemente imprímelo
							System.out.print(arrayValue);
							if (i < jsonArray.length() - 1) {
//	                            System.out.print(", ");
							}
						}
					}
					System.out.println();
				} else {
					// Si el valor es de otro tipo, simplemente imprímelo
					System.out.println(value);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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

			String description = "";

			if (comic.has("description") && comic.get("description") instanceof String) {
				description = comic.getString("description");

			}

			// Número de edición
			int issueNumber = comic.getInt("issueNumber");

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

			float price = 0;
			// Precio
			JSONArray pricesArray = comic.getJSONArray("prices");
			if (pricesArray.length() > 0) {
				JSONObject priceObject = pricesArray.getJSONObject(0);
				price = (float) priceObject.getDouble("price");

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

			// URL de la imagen representativa
			JSONObject thumbnailObject = comic.getJSONObject("thumbnail");
			String path = thumbnailObject.getString("path");
			String extension = thumbnailObject.getString("extension");
			String thumbnailURL = path + "." + extension;

			comicInfoList.add(title);
			comicInfoList.add(description);
			comicInfoList.add(Integer.toString(issueNumber));
			comicInfoList.add(formato);
			comicInfoList.add(Float.toString(price));
			comicInfoList.add(String.join(", ", coverPencillers));
			comicInfoList.add(String.join(", ", pencillers));
			comicInfoList.add(String.join(", ", writers));
			comicInfoList.add(onsaleDate);
			comicInfoList.add(detailURL);
			comicInfoList.add(thumbnailURL);
			// Editorial (En este caso, siempre es "Marvel")
			comicInfoList.add("Marvel");

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
