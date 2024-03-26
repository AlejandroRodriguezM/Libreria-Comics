package UNIT_TEST;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class CurrencyConverter {
    public static void main(String[] args) {
        // Lista de países y sus monedas respectivas
        String[] countries = {
                "Japón (Japan): JPY",
                "Estados Unidos (United States): USD",
                "Francia (France): EUR",
                "Italia (Italy): EUR",
                "España (Spain): EUR",
                "Reino Unido (United Kingdom): GBP",
                "Alemania (Germany): EUR",
                "Brasil (Brazil): BRL",
                "Corea del Sur (South Korea): KRW",
                "México (Mexico): MXN",
                "Canadá (Canada): CAD",
                "China (China): CNY",
                "Australia (Australia): AUD",
                "Argentina (Argentina): ARS",
                "India (India): INR",
                "Bélgica (Belgium): EUR",
                "Países Bajos (Netherlands): EUR",
                "Portugal (Portugal): EUR",
                "Suecia (Sweden): SEK",
                "Suiza (Switzerland): CHF",
                "Finlandia (Finland): EUR",
                "Noruega (Norway): NOK",
                "Dinamarca (Denmark): DKK"
        };

        // Obtener el valor de 1 USD en diferentes monedas e imprimir los resultados
        for (String country : countries) {
            String[] parts = country.split(": ");
            String countryName = parts[0];
            String currencyCode = parts[1];

            double conversionRate = getConversionRate("USD", currencyCode);

            // Imprimir el resultado en el formato deseado
            System.out.println(countryName + ": " + conversionRate);
        }
    }

    private static double getConversionRate(String fromCurrency, String toCurrency) {
        try {
            String urlStr = "https://api.exchangerate-api.com/v4/latest/" + fromCurrency;
            URI uri = new URI(urlStr);

            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String json = response.toString();

            // Parsear el JSON
            String rateKey = "\"" + toCurrency + "\":";
            int rateIndex = json.indexOf(rateKey) + rateKey.length();
            int endIndex = json.indexOf(",", rateIndex);
            String rateValue = json.substring(rateIndex, endIndex);

            // Obtener la tasa de conversión
            return Double.parseDouble(rateValue.trim());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return -1; // Manejo de errores: en una aplicación real, deberías manejar estos casos de error de manera adecuada
        }
    }
}

