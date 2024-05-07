package Apis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que contiene métodos para interactuar con Comic Vine API y extraer información relevante.
 */
public class ApiComicVine {

    private ApiComicVine() {

    }
	
	/**
	 * Esta función toma una cadena de entrada y elimina los espacios entre letras y puntos.
	 * Busca cualquier letra seguida de un punto, seguida de uno o más espacios, y luego otra letra.
	 * Luego, reemplaza esto por la primera letra, seguida de un punto y la segunda letra,
	 * lo que elimina los espacios entre ellas.
	 *
	 * @param input La cadena de entrada que se va a procesar.
	 * @return La cadena modificada sin espacios entre letras y puntos.
	 */
	public static String eliminarEspaciosEntreLetrasYPuntos(String input) {
	    return input.replaceAll("(\\w)\\.(\\s+)(\\w)", "$1.$3");
	}

	/**
	 * Busca información sobre una persona en Comic Vine y extrae palabras clave relevantes.
	 * 
	 * @param apiKey          La clave de la API para acceder al servicio web.
	 * @param nombrePersona   El nombre de la persona a buscar.
	 * @return                Una cadena que contiene palabras clave relevantes como "writer" o "artist".
	 * @throws IOException    Si hay un problema de E/S al realizar la solicitud HTTP.
	 * @throws JSONException  Si hay un problema al analizar la respuesta JSON.
	 * @throws URISyntaxException Si hay un problema al crear la URI para la solicitud.
	 */
	public static String searchPersonAndExtractInfo(String apiKey, String nombrePersona)
	        throws IOException, JSONException, URISyntaxException {
	    // Eliminar espacios entre letras y puntos en el nombre de la persona
	    nombrePersona = eliminarEspaciosEntreLetrasYPuntos(nombrePersona);

	    // Escapar espacios en el nombre de la persona para usarlo en la URL
	    String nombrePersonaEscapado = nombrePersona.replace(" ", "%20");

	    // Crear una URI para la solicitud
	    URI uri = new URI("https://comicvine.gamespot.com/api/people/?api_key=" + apiKey
	            + "&format=json&filter=name:" + nombrePersonaEscapado);

	    // Convertir la URI en una URL
	    URL url = uri.toURL();

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

	        // Obtener el JSON como una cadena
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


	/**
	 * Extrae las palabras "writer" o "artist" si aparecen en el texto.
	 * 
	 * @param text El texto en el que se buscarán las palabras clave.
	 * @return La palabra clave extraída ("writer", "artist") o una cadena vacía si no se encuentra ninguna.
	 */
	private static String extractWriterOrArtist(String text) {
	    // Imprime el texto de entrada para depuración

	    // Utiliza expresiones regulares para buscar la primera coincidencia de "writer" o "artist"
	    // \b indica límites de palabra para que solo coincida con "writer" o "artist" como palabras completas
	    // Pattern.CASE_INSENSITIVE hace que la búsqueda sea insensible a mayúsculas y minúsculas
	    Pattern pattern = Pattern.compile("\\b(writer|artist)\\b", Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(text);

	    if (matcher.find()) {
	        // Devuelve la palabra clave encontrada (en minúsculas) si se encuentra una coincidencia
	        return matcher.group().trim().toLowerCase().replace("-", "");
	    }

	    // Devuelve una cadena vacía si no se encontraron coincidencias
	    return "";
	}

}
