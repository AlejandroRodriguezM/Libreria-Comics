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

import alarmas.AlarmaList;
import apisFunciones.FuncionesApis;
import comicManagement.Comic;
import dbmanager.ListaComicsDAO;
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
	public static Comic infoComicCode(String comicCode, TextArea prontInfo) {

		JSONObject jsonComic = null;
		String cleanedCode = comicCode.replaceAll("\\D", "");
		if (cleanedCode.length() == 13) {
			String formattedIsbn = formatIsbn(cleanedCode);
			jsonComic = getComicInfo(formattedIsbn, "isbn", prontInfo);
		} else if (cleanedCode.length() == 17) {
			jsonComic = getComicInfo(cleanedCode, "upc", prontInfo);
		}
		if (jsonComic != null) {
			return displayComicInfo(jsonComic, comicCode);
		}

		return null;
	}

	private static String[][] listaErrores() {

		return new String[][] {
				{ "409", "Clave API Ausente", "Ocurre cuando el parámetro apikey no está incluido en una solicitud." },
				{ "409", "Hash Ausente",
						"Error 409: Ocurre cuando se incluye el parámetro apikey en una solicitud, se presenta un parámetro ts, pero no se envía un parámetro hash. Ocurre solo en aplicaciones del lado del servidor." },
				{ "409", "Timestamp Ausente",
						"Ocurre cuando se incluye el parámetro apikey en una solicitud, se presenta un parámetro hash, pero no se envía un parámetro ts. Ocurre solo en aplicaciones del lado del servidor." },
				{ "401", "Referente Inválido",
						"Ocurre cuando se envía un referente que no es válido para el parámetro apikey pasado." },
				{ "401", "Hash Inválido",
						"Ocurre cuando se envían los parámetros ts, hash y apikey, pero el hash no es válido según la regla de generación de hash anterior." },
				{ "405", "Método No Permitido",
						"Ocurre cuando se accede a un punto de conexión de la API utilizando un verbo HTTP que no está permitido para ese punto de conexión." },
				{ "403", "Prohibido",
						"Ocurre cuando un usuario con una solicitud autenticada intenta acceder a un punto de conexión al que no tiene acceso." } };

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
			// 3-5-7-12
			// Agregar guiones según el formato estándar del ISBN
			if ((i == 3) || (i == 4) || (i == 7) || (i == 12)) {
				formattedIsbn.append('-');
			}

			formattedIsbn.append(digit);

		}

		return formattedIsbn.toString();
	}

	private static JSONObject getComicInfo(String claveComic, String tipoUrl, TextArea prontInfo) {
		long timestamp = System.currentTimeMillis() / 1000;
		String clavePublica = clavesApi()[1].trim();
		String apiUrl = buildApiUrl(claveComic, tipoUrl, clavePublica, timestamp);

		try {
			int codigoRespuesta = getCodigoRespuesta(apiUrl);
			handleResponseErrors(codigoRespuesta, prontInfo);

			String jsonResponse = sendHttpGetRequest(apiUrl);
			JSONObject jsonObject = new JSONObject(jsonResponse);
			JSONArray resultsArray = jsonObject.getJSONObject("data").getJSONArray("results");

			return extractComicInfo(resultsArray, claveComic, prontInfo);

		} catch (IOException | URISyntaxException | JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String buildApiUrl(String claveComic, String tipoUrl, String clavePublica, long timestamp) {
		String endpoint = (tipoUrl.equalsIgnoreCase("isbn")) ? "comics?isbn=" : "comics?upc=";
		return "https://gateway.marvel.com:443/v1/public/" + endpoint + claveComic + "&apikey=" + clavePublica
				+ "&hash=" + newHash(timestamp) + "&ts=" + timestamp;

	}

	private static int getCodigoRespuesta(String apiUrl) throws IOException, URISyntaxException {
		return codigoRespuesta(apiUrl);
	}

	private static void handleResponseErrors(int codigoRespuesta, TextArea prontInfo) {
		int[] codigosError = { 401, 403, 405, 409 };
		String[][] tablaErrores = listaErrores();

		for (int i : codigosError) {
			if (i == codigoRespuesta) {
				for (String[] filaError : tablaErrores) {
					if (filaError[0].equals(String.valueOf(codigoRespuesta))) {
						String mensaje = filaError[1] + ": " + filaError[2];
						AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
					}
				}
				break;
			}
		}
	}

	private static JSONObject extractComicInfo(JSONArray resultsArray, String claveComic, TextArea prontInfo) {
		try {
			if (resultsArray.length() == 0) {
				String mensaje = "No se encontró el cómic con código: " + claveComic;
				AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
				return null;
			} else {
				return resultsArray.getJSONObject(0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unused")
	private static JSONObject getComicInfoUrl(String claveComic, String url, TextArea prontInfo) {

		long timestamp = System.currentTimeMillis() / 1000;

		String[] claves = clavesApi();

		String clavePublica = claves[1].trim();

		String apiUrl = "";

		if (!url.isEmpty()) {
			apiUrl = "https://gateway.marvel.com:443/v1/public/comics?urls=" + claveComic + "&apikey=" + clavePublica
					+ "&hash=" + newHash(timestamp) + "&ts=" + timestamp;
		}

		// Realiza la solicitud HTTP GET
		String jsonResponse;
		try {
			jsonResponse = sendHttpGetRequest(apiUrl);

			// Parsea la respuesta JSON y obtén el cómic
			JSONObject jsonObject = new JSONObject(jsonResponse);
			JSONArray resultsArray = jsonObject.getJSONObject("data").getJSONArray("results");

			if (resultsArray.length() == 0 && ListaComicsDAO.comicsImportados.isEmpty()) {
				prontInfo.setOpacity(1);
				prontInfo.setText("No se encontró el cómic con codigo: " + claveComic);
				return null;
			} else {
				return resultsArray.getJSONObject(0); // Devuelve el primer cómic encontrado
			}
		} catch (IOException | URISyntaxException | JSONException e) {
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
		return getHash(timestamp);
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
		try {
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
		} catch (IOException e) {
			System.err.println("Error de IO al realizar la solicitud HTTP: " + e.getMessage());
			// Puedes lanzar la excepción nuevamente o manejarla según tus necesidades
			throw new IOException("Error al realizar la solicitud HTTP", e);
		}
	}

	private static int codigoRespuesta(String apiUrl) throws IOException, URISyntaxException {
		URI url = new URI(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
		connection.setRequestMethod("GET");

		return connection.getResponseCode();
	}

	/**
	 * Calcula un hash MD5 a partir de una cadena de entrada.
	 *
	 * @param input La cadena de entrada para la cual se calculará el hash.
	 * @return El hash MD5 calculado.
	 */
	private static String getHash(long timestamp) {
		String[] claves = clavesApi();

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
		String[] claves = new String[2]; // Crear un arreglo de dos elementos para almacenar las claves

		String clavesDesdeArchivo = FuncionesApis.obtenerClaveApiMarvel(); // Obtener las claves desde el archivo

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

	public static Comic displayComicInfo(JSONObject jsonComic, String codigoComic) {
		try {
			String title = extractTitle(jsonComic.getString("title"));
			String keyIssue = jsonComic.optString("description", "");
			int issueNumber = jsonComic.getInt("issueNumber");
			String formato = getFormat(jsonComic.getString("format"));
			float price = getPrice(jsonComic.getJSONArray("prices"));
			List<String> pencillers = new ArrayList<>();
			List<String> writers = new ArrayList<>();
			List<String> coverPencillers = new ArrayList<>();
			extractCreators(jsonComic, pencillers, writers, coverPencillers);
			String fecha = getOnSaleDate(jsonComic.getJSONArray("dates"));
			String urlReferencia = getDetailURL(jsonComic.getJSONArray("urls"));
			String imagen = jsonComic.getJSONObject("thumbnail").getString("path") + "."
					+ jsonComic.getJSONObject("thumbnail").getString("extension");
			String editorial = "Marvel";
			String numero = Integer.toString(issueNumber);
			String variante = String.join(", ", coverPencillers);
			String guionistas = String.join(", ", writers);
			String dibujantes = String.join(", ", pencillers);
			String procedencia = "Estados Unidos (United States)";
			String estado = "En posesion";
			String precioComic = Float.toString(price);
			String puntuacion = "Sin puntuacion";
			String gradeo = "NM (Noir Medium)";

			return new Comic.ComicBuilder("", title).valorGradeo(gradeo).numero(numero).variante(variante).firma("")
					.editorial(editorial).formato(formato).procedencia(procedencia).fecha(fecha).guionista(guionistas)
					.dibujante(dibujantes).estado(estado).keyIssue(keyIssue).puntuacion(puntuacion).imagen(imagen)
					.referenciaComic(urlReferencia).precioComic(precioComic).codigoComic(codigoComic).build();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String extractTitle(String title) {
		title = title.replaceAll("\\([^\\)]*\\)", "").replaceAll("#\\d+\\s|#\\d+", "").trim();
		return title;
	}

	private static String getFormat(String format) {
		String lowercaseFormat = format.toLowerCase();
		if ("comic".equals(lowercaseFormat) || "jsoncomic".equals(lowercaseFormat)) {
			return "Grapa (Issue individual)";
		} else if ("hardcover".equals(lowercaseFormat)) {
			return "Tapa dura (Hardcover)";
		} else if ("trade paperback".equals(lowercaseFormat)) {
			return "Tapa blanda (Paperback)";
		} else {
			return format;
		}
	}

	private static float getPrice(JSONArray pricesArray) {
		try {
			if (pricesArray.length() > 0) {
				JSONObject priceObject = pricesArray.getJSONObject(0);
				return (float) priceObject.getDouble("price");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private static void extractCreators(JSONObject jsonComic, List<String> pencillers, List<String> writers,
			List<String> coverPencillers) {
		try {
			JSONArray creatorsArray = jsonComic.getJSONObject("creators").getJSONArray("items");
			for (int i = 0; i < creatorsArray.length(); i++) {
				JSONObject creator = creatorsArray.getJSONObject(i);
				String creatorName = creator.getString("name");
				String creatorRole = creator.getString("role");

				if (creatorRole.matches("penciler|penciller|artist")) {
					pencillers.add(creatorName.replace("-", ""));
				} else if (creatorRole.equals("writer")) {
					writers.add(creatorName.replace("-", ""));
				} else if (creatorRole.matches("penciller \\(cover\\)|penciler \\(cover\\)|painter \\(cover\\)")) {
					coverPencillers.add(creatorName.replace("-", ""));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private static String getOnSaleDate(JSONArray datesArray) {
		try {
			for (int i = 0; i < datesArray.length(); i++) {
				JSONObject dateObject = datesArray.getJSONObject(i);
				if (dateObject.getString("type").equals("onsaleDate")) {
					String[] parts = dateObject.getString("date").split("T")[0].split("-");
					return parts[0] + "-" + parts[1] + "-" + parts[2];
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String getDetailURL(JSONArray urlsArray) {
		try {
			for (int i = 0; i < urlsArray.length(); i++) {
				JSONObject urlObject = urlsArray.getJSONObject(i);
				if (urlObject.getString("type").equals("detail")) {
					return urlObject.getString("url");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

}
