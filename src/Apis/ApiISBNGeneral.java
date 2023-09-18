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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Esta clase demuestra cómo buscar información de un libro utilizando el ISBN a
 * través de la API de OpenLibrary.
 */
public class ApiISBNGeneral {
	public static void main(String[] args) throws JSONException, URISyntaxException {
		String isbn = "978-1506711980"; // ISBN del libro que deseas buscar
		String apiUrl = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&jscmd=details&format=json";

		try {
			String jsonResponse = sendHttpGetRequest(apiUrl);

			// Parse JSON response
			JSONObject jsonObject = new JSONObject(jsonResponse);
			JSONObject bookInfo = jsonObject.getJSONObject("ISBN:" + isbn);
			JSONObject details = bookInfo.getJSONObject("details");

			// Extraer autores
			JSONArray authors = details.getJSONArray("authors");
			StringBuilder authorsString = new StringBuilder();
			for (int i = 0; i < authors.length(); i++) {
				JSONObject author = authors.getJSONObject(i);
				String authorName = author.getString("name");
				authorsString.append(authorName);
				if (i < authors.length() - 1) {
					authorsString.append(", ");
				}
			}

			// Extraer otros datos
			if (details.has("title")) {
				String title = details.getString("title");
				System.out.println("Titulo: " + title);
			}

			if (details.has("description")) {
				String description = details.getString("description");
				System.out.println("Descripcion: " + description);
			}

			if (details.has("publish_date")) {
				String publishDate = details.getString("publish_date");
				System.out.println("Fecha de Publicacion: " + publishDate);
			}

			if (details.has("publishers")) {
				JSONArray publishersArray = details.getJSONArray("publishers");
				if (publishersArray.length() > 0) {
					String publisher = publishersArray.getString(0);
					System.out.println("Editorial: " + publisher);
				}
			}

			if (details.has("number_of_pages")) {
				int pageCount = details.getInt("number_of_pages");
				System.out.println("Numero de Paginas: " + pageCount);
			}

			if (details.has("physical_format")) {
				String physicalFormat = details.getString("physical_format");
				System.out.println("Formato: " + physicalFormat);
			}

			if (details.has("publish_country")) {
				String publishCountry = details.getString("publish_country");
				System.out.println("Pais de Procedencia: " + publishCountry);
			}
		} catch (IOException e) {
			e.printStackTrace();
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
		URI url = new URI(apiUrl); // Use .toURI() instead of .toURL()
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

}
