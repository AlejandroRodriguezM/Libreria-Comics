package Apis;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MarvelComicViewer {
	// Define las claves pública y privada
//	private static final String PUBLIC_KEY = "7c66cdfeeab4a19d75606782e4d031ce";
//	private static final String PRIVATE_KEY = "0e9411ea974038b3b1d850efc3e8703875fd94cb";
	
	private static final String PUBLIC_KEY = "f0c866038aa05cd5bb64552a342d2726";
	private static final String PRIVATE_KEY = "483e1a636e93c647ce6335162daf59e02dd40c2d";

	public static void main(String[] args) throws JSONException {
	    String isbn = "978-1-302-94820-7";
	    String upc = "75960607918601411";

	    List<JSONObject> comics = new ArrayList<>();

	    try {
	        JSONObject comic1 = getComicByISBN(isbn);
	        JSONObject comic2 = getComicByUPC(upc);

	        if (comic1 != null) {
	            comics.add(comic1);
	        }

	        if (comic2 != null) {
	            comics.add(comic2);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    for (JSONObject comic : comics) {
	        displayComicInfo(comic);
	    }
	}

	private static JSONObject getComicByISBN(String isbn) throws IOException, JSONException {

		long timestamp = System.currentTimeMillis() / 1000;

		// Define la URL de la API de Marvel para buscar por ISBN
		String apiUrl = "https://gateway.marvel.com:443/v1/public/comics?isbn=" + isbn + "&apikey=" + PUBLIC_KEY
				+ "&hash=" + newHash(timestamp) + "&ts=" + timestamp;

		// Realiza la solicitud HTTP GET
		String jsonResponse = sendHttpGetRequest(apiUrl);

		// Parsea la respuesta JSON y obtén el cómic
		JSONObject jsonObject = new JSONObject(jsonResponse);
		JSONArray resultsArray = jsonObject.getJSONObject("data").getJSONArray("results");

		if (resultsArray.length() == 0) {
			System.out.println("No se encontró el cómic con ISBN: " + isbn);
			return null;
		} else {
			return resultsArray.getJSONObject(0); // Devuelve el primer cómic encontrado
		}
	}
	
	private static JSONObject getComicByUPC(String upc) throws IOException, JSONException {
	    // Calcula el hash MD5
	    long timestamp = System.currentTimeMillis() / 1000;

	    // Define la URL de la API de Marvel para buscar por UPC
	    String apiUrl = "https://gateway.marvel.com:443/v1/public/comics?upc=" + upc + "&apikey=" + PUBLIC_KEY
	            + "&hash=" + newHash(timestamp) + "&ts=" + timestamp;

	    // Realiza la solicitud HTTP GET
	    String jsonResponse = sendHttpGetRequest(apiUrl);

	    // Parsea la respuesta JSON y obtén el cómic
	    JSONObject jsonObject = new JSONObject(jsonResponse);
	    JSONArray resultsArray = jsonObject.getJSONObject("data").getJSONArray("results");

	    if (resultsArray.length() == 0) {
	        System.out.println("No se encontró el cómic con UPC: " + upc);
	        return null;
	    } else {
	        return resultsArray.getJSONObject(0); // Devuelve el primer cómic encontrado
	    }
	}
	
	private static String newHash(long timestamp) {
		String hash = getHash(timestamp);
		return hash;
	}

	private static String sendHttpGetRequest(String apiUrl) throws IOException {
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

	private static String getHash(long timestamp) {
		return md5(timestamp + PRIVATE_KEY + PUBLIC_KEY);
	}

	private static String md5(String input) {
		// Utiliza Apache Commons Codec para calcular el hash MD5
		return DigestUtils.md5Hex(input);
	}

	private static void displayComicInfo(JSONObject comic) throws JSONException {
		// Implementa la lógica para mostrar la información del cómic por pantalla
		// Puedes acceder a los campos del objeto JSON 'comic' para obtener los datos
		// del cómic
		System.out.println();
		String title = comic.getString("title");

		// Eliminar paréntesis y su contenido
		title = title.replaceAll("\\([^\\)]*\\)", "");

		// Eliminar "#" y números que lo preceden hasta el siguiente espacio
		title = title.replaceAll("#\\d+\\s", "");
		title = title.replaceAll("#\\d+", "").trim();

		System.out.println("Titulo: " + title);

		System.out.println("Numero: " + comic.getInt("issueNumber"));
		System.out.println("ISBN: " + comic.getString("isbn"));
		System.out.println("UPC: " + comic.getString("upc"));
		
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

		
		System.out.println("Formato: " + formato);

		JSONArray pricesArray = comic.getJSONArray("prices");
		if (pricesArray.length() > 0) {
			JSONObject priceObject = pricesArray.getJSONObject(0);
			float price = (float) priceObject.getDouble("price");

			System.out.println("Precio: " + price);
		}
		
		JSONObject creatorList = comic.getJSONObject("creators"); // Obtener la lista de creadores
		
		// Crear una lista para artistas
		List<String> artistas = new ArrayList<>();

		// Acceder a la lista de creadores (items)
		JSONArray creatorItemsArray = creatorList.optJSONArray("items");

		for (int i = 0; i < creatorItemsArray.length(); i++) {
		    JSONObject creatorSummary = creatorItemsArray.getJSONObject(i);

		    String creatorName = creatorSummary.optString("name", "");
		    String creatorRole = creatorSummary.optString("role", "");

		    // Determinar si el creador es un artista (penciller o artist)
		    if (creatorRole.equalsIgnoreCase("penciler") || creatorRole.equalsIgnoreCase("artist")) {
		        artistas.add(creatorName);
		    }
		}
		
		JSONObject creatorsObject = comic.getJSONObject("creators");
		JSONArray creatorsArray = creatorsObject.getJSONArray("items");

		String pencillers = "";
		String writers = "";
		String coverPencillers = "";

		for (int i = 0; i < creatorsArray.length(); i++) {
			JSONObject creator = creatorsArray.getJSONObject(i);
			String creatorName = creator.getString("name");
			String creatorRole = creator.getString("role");
						
			if (creatorRole.equals("penciler") || creatorRole.equals("penciller") || creatorRole.equals("artist")) {
				if (!pencillers.isEmpty()) {
					pencillers += ", ";
				}else {
				    pencillers = String.join(", ", artistas);
				}
				pencillers += creatorName;
			} else if (creatorRole.equals("writer")) {
				if (!writers.isEmpty()) {
					writers += ", ";
				}
				writers += creatorName;
			} else if (creatorRole.equals("penciller (cover)") || creatorRole.equals("penciler (cover)")) {
				if (!coverPencillers.isEmpty()) {
					coverPencillers += ", ";
				}
				coverPencillers += creatorName;
			}
		}
		
		System.out.println("Variante: " + coverPencillers);

		System.out.println("Dibujantes: " + pencillers);
		System.out.println("Escritores: " + writers);

		JSONArray datesArray = comic.getJSONArray("dates");

		String onsaleDate = "";

		for (int i = 0; i < datesArray.length(); i++) {
			JSONObject dateObject = datesArray.getJSONObject(i);
			String dateType = dateObject.getString("type");
			String date = dateObject.getString("date");

			if (dateType.equals("onsaleDate")) {
				// Formatea la fecha (asumiendo que 'date' es una fecha en formato ISO8601)
				String[] parts = date.split("T");
				String[] dateParts = parts[0].split("-");

				String year = dateParts[0];
				String month = dateParts[1];
				String day = dateParts[2];

				onsaleDate = year + "-" + month + "-" + day;

				break; // Termina el bucle una vez que se encuentra 'onsaleDate'
			}
		}

		System.out.println("Fecha de venta: " + onsaleDate);

		JSONArray urlsArray = comic.getJSONArray("urls");
		String detailURL = "";

		for (int i = 0; i < urlsArray.length(); i++) {
			JSONObject urlObject = urlsArray.getJSONObject(i);
			String urlType = urlObject.getString("type");
			String url = urlObject.getString("url");

			if (urlType.equals("detail")) {
				detailURL = url;
				break; // Termina el bucle una vez que se encuentra la URL "detail"
			}
		}

		System.out.println("URL de referencia: " + detailURL);

		JSONObject thumbnailObject = comic.getJSONObject("thumbnail");
		String path = thumbnailObject.getString("path");
		String extension = thumbnailObject.getString("extension");
		String thumbnailURL = path + "." + extension;

		System.out.println("URL de la imagen representativa: " + thumbnailURL);

	}
}
