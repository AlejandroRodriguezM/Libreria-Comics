package Apis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiComicVine {
	public static void main(String[] args) throws JSONException {
		String apiKey = "d8c00f9d781af5da1232e769b78cb0b68f0960a0";
		String nombrePersona = "Mark Waid";

		try {
			System.out.println(searchPersonAndExtractInfo(apiKey, nombrePersona));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Función para buscar una persona y extraer información
	public static String searchPersonAndExtractInfo(String apiKey, String nombrePersona)
			throws IOException, JSONException {
		// Escapar espacios en el nombre de la persona para la URL
		String nombrePersonaEscapado = nombrePersona.replace(" ", "%20");

		// Crear una URL para la solicitud
		URL url = new URL("https://comicvine.gamespot.com/api/people/?api_key=" + apiKey + "&format=json&filter=name:"
				+ nombrePersonaEscapado);

		// Abrir una conexión HTTP
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		connection.setRequestMethod("GET");

		// Verificar el código de respuesta
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			// Leer la respuesta JSON
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder responseBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line);
			}
			reader.close();

			// Obtener el JSON como cadena
			String jsonResponse = responseBuilder.toString();
			JSONObject json = new JSONObject(jsonResponse);

			// Acceder al objeto JSON "results"
			JSONArray resultsArray = json.getJSONArray("results");
			String extractedWords = "";
			// Iterar a través de los objetos dentro del arreglo "results"
			for (int i = 0; i < resultsArray.length(); i++) {
				JSONObject resultObject = resultsArray.getJSONObject(i);

				// Obtener el valor de la clave "deck" y mostrarlo
				String deck = resultObject.optString("deck");

				// Buscar palabras "writer" o "artist" en el valor de "deck"
				extractedWords = extractWriterOrArtist(deck);
			}
			return extractedWords;
		}

		// Cerrar la conexión
		connection.disconnect();
		return null;
		
	}

	// Método para extraer las palabras "writer" o "artist" si aparecen en el texto
	private static String extractWriterOrArtist(String text) {
	    // Utilizar expresiones regulares para extraer la primera coincidencia de "writer" o "artist"
	    Pattern pattern = Pattern.compile("\\b(writer|artist)\\b", Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(text);

	    if (matcher.find()) {
	        return matcher.group().trim().toLowerCase();
	    }

	    return ""; // Devolver una cadena vacía si no se encontraron coincidencias
	}
}
