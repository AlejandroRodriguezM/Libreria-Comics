package UNIT_TEST;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.HttpStatusException;

public class testScrap {

    public static void main(String[] args) {
        String url = "https://www.cardladder.com/ladder/card/wkLhosA2Ha2UQtsiI9xG";
        try {
            // Conectarse a la URL
            Connection connection = Jsoup.connect(url);
            Connection.Response response = connection.execute();
            
            // Verificar el código de estado de la respuesta
            int statusCode = response.statusCode();
            if (statusCode == 403) {
                System.out.println("La página tiene protección (Error 403 Forbidden).");
            } else {
                System.out.println("La página no tiene protección 403. Código de estado: " + statusCode);
                // Puedes obtener el documento HTML si lo necesitas
                Document document = connection.get();
                // Realizar otras operaciones con el documento
            }
            
        } catch (HttpStatusException e) {
            if (e.getStatusCode() == 403) {
                System.out.println("La página tiene protección (Error 403 Forbidden).");
            } else {
                System.out.println("HTTP error fetching URL. Status=" + e.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
