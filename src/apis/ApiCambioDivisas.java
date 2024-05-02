package apis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import funciones_auxiliares.Utilidades;

public class ApiCambioDivisas {

	public static void imprimirDivisas(String direccionFichero) {
		ConcurrentHashMap<String, Double> conversionRates = new ConcurrentHashMap<>();

		String[] countries = { "Japón (Japan): JPY", "Estados Unidos (United States): USD", "Francia (France): EUR",
				"Italia (Italy): EUR", "España (Spain): EUR", "Reino Unido (United Kingdom): GBP",
				"Alemania (Germany): EUR", "Brasil (Brazil): BRL", "Corea del Sur (South Korea): KRW",
				"México (Mexico): MXN", "Canadá (Canada): CAD", "China (China): CNY", "Australia (Australia): AUD",
				"Argentina (Argentina): ARS", "India (India): INR", "Bélgica (Belgium): EUR",
				"Países Bajos (Netherlands): EUR", "Portugal (Portugal): EUR", "Suecia (Sweden): SEK",
				"Suiza (Switzerland): CHF", "Finlandia (Finland): EUR", "Noruega (Norway): NOK",
				"Dinamarca (Denmark): DKK" };

		// Crear un hilo para cada país
		Thread[] threads = new Thread[countries.length];
		for (int i = 0; i < countries.length; i++) {
			String country = countries[i];
			String[] parts = country.split(": ");
			String countryName = parts[0];
			String currencyCode = parts[1];
			threads[i] = new Thread(() -> {
				double conversionRate = getConversionRate("USD", currencyCode);
				conversionRates.put(countryName, conversionRate);
			});
			threads[i].start();
		}

//		// Esperar a que todos los hilos terminen
//		for (Thread thread : threads) {
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				Utilidades.manejarExcepcion(e);
//			}
//		}

		// Escribir los resultados en el archivo
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(direccionFichero))) {
			for (String countryName : conversionRates.keySet()) {
				double conversionRate = conversionRates.get(countryName);
				writer.write(countryName + ": " + conversionRate);
				writer.newLine();
			}
		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

    private static double getConversionRate(String fromCurrency, String toCurrency) {
        try {
            String urlStr = "https://api.exchangerate-api.com/v4/latest/" + fromCurrency;
            URI uri = new URI(urlStr);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
			Utilidades.manejarExcepcion(e);
            return -1;
        }
    }

}
