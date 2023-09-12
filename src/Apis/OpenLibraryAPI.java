package Apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class OpenLibraryAPI {
    public static void main(String[] args) throws JSONException {
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

    private static String sendHttpGetRequest(String apiUrl) throws IOException {
        @SuppressWarnings("deprecation")
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
}
