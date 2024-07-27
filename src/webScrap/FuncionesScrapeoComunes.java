package webScrap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import comicManagement.Comic;
import funcionesAuxiliares.Utilidades;
import funcionesManagment.AccionFuncionesComunes;
import javafx.concurrent.Task;

public class FuncionesScrapeoComunes {

	public static CompletableFuture<List<String>> executeScraping(String command) {
		CompletableFuture<List<String>> future = new CompletableFuture<>();

		Task<List<String>> task = new Task<>() {
			@Override
			protected List<String> call() throws Exception {
				return executeScript(command);
			}
		};

		task.setOnSucceeded(e -> {
			List<String> result = task.getValue();
			if (result == null || result.isEmpty()) {
				future.complete(Collections.emptyList());
			} else {
				future.complete(result);
			}
		});

		task.setOnFailed(e -> {
			future.completeExceptionally(task.getException());
		});

		new Thread(task).start();

		return future;
	}

	public static List<String> executeScript(String command) {
		List<String> dataArrayList = new ArrayList<>();
		int maxAttempts = 5;
		int backoff = 2000;

		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			try {
				Process process = Runtime.getRuntime().exec(command);
				BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				StringBuilder output = new StringBuilder();
				String line;

				while ((line = processReader.readLine()) != null) {
					output.append(line).append("\n");
				}
				processReader.close();

				int exitCode = process.waitFor();
				if (exitCode == 0) {
					String dataString = output.toString().trim();
					if (!dataString.isEmpty()) {
						String[] keyValuePairs = dataString.split("\n");
						for (String pair : keyValuePairs) {
							dataArrayList.add(pair.trim());
						}
						return dataArrayList;
					} else {
						System.err.println("El resultado obtenido está vacío. Volviendo a intentar...");
					}
				} else {
					System.err.println("Error al ejecutar el script de Puppeteer, código de salida: " + exitCode);
				}

				Thread.sleep(backoff);
				backoff += 2000;

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		return new ArrayList<>();
	}

	public static String buscarEnGoogle(String searchTerm) throws URISyntaxException {
		try {
			// Codificar el término de búsqueda
			String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");

			// Construir la URL de búsqueda en Google
			String urlString = "https://www.google.com/search?q=cardmarket+" + encodedSearchTerm + "+versions";

			// Crear objeto URI y URL
			URI uri = new URI(urlString);
			URL url = uri.toURL();

			// Establecer la conexión HTTP
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			// Establecer el User-Agent para simular una solicitud desde el navegador Chrome
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.6478.127 Safari/537.36");

			// Leer la respuesta
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

			// Convertir la respuesta a String
			String html = content.toString();

			// Buscar la URL de Cardmarket
			int startIndex = html.indexOf("www.cardmarket.com/");
			while (startIndex != -1) {
				int endIndex = html.indexOf("\"", startIndex);
				if (endIndex != -1) {
					String urlFound = html.substring(startIndex, endIndex);
					if (urlFound.endsWith("/Versions")) {
						System.out.println("URL: " + urlFound);
						urlFound = urlFound.replaceFirst("/\\w{2}/", "/en/");
						return extractCardName(urlFound);
					}
					startIndex = html.indexOf("www.cardmarket.com/", endIndex);
				}
			}
			return null; // No se encontró ninguna URL
		} catch (IOException e) {
			e.printStackTrace();
			return null; // Devolver null en caso de excepción
		}
	}

	private static String extractCardName(String url) {
		// Encontrar el índice de "/Cards/" en la URL
		int cardsIndex = url.indexOf("/Cards/");
		if (cardsIndex != -1) {
			// Extraer el nombre de la carta entre "/Cards/" y "/Versions"
			int startIndex = cardsIndex + "/Cards/".length();
			int endIndex = url.indexOf("/Versions", startIndex);
			if (endIndex != -1) {
				String cardName = url.substring(startIndex, endIndex);
				// Reemplazar guiones por espacios
				cardName = cardName.replace("-", " ");
				return cardName;
			}
		}
		return null; // No se encontró el nombre de la carta
	}

	public static List<String> getCartaFromPuppeteer(String url, String scriptPath) {
		List<String> dataArrayList = new ArrayList<>();

		try {
			String command = "node " + scriptPath + " " + url ;

			int attempt = 0;
			int backoff = 2000; // Tiempo de espera inicial en milisegundos

			while (true) {
				attempt++;
				Process process = Runtime.getRuntime().exec(command);

				// Leer la salida del proceso
				BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String outputLine;
				StringBuilder output = new StringBuilder();
				while ((outputLine = processReader.readLine()) != null) {
					output.append(outputLine).append("\n");
				}
				processReader.close();

				// Esperar a que termine el proceso
				int exitCode = process.waitFor();

				// Verificar si el proceso terminó con éxito (código de salida 0)
				if (exitCode == 0) {
					// Proceso terminado exitosamente, obtener la salida del script Node.js
					String dataString = output.toString().trim();
					if (!dataString.isEmpty()) {
						// Dividir los pares clave-valor y añadirlos al List<String>
						String[] keyValuePairs = dataString.split("\n");
						for (String pair : keyValuePairs) {
							dataArrayList.add(pair.trim());
						}
					} else {
						System.err.println("El resultado obtenido está vacío.");
					}
					break; // Salir del bucle, ya tenemos los datos
				} else {
					// Error al ejecutar el script Node.js
					System.err.println("Error al ejecutar el script de Puppeteer. Código de salida: " + exitCode);
					break; // Salir del bucle si hay un error
				}
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.err.println("El hilo fue interrumpido. Terminando la ejecución.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error de entrada/salida al ejecutar el script de Puppeteer.");
		}

		return dataArrayList;
	}

	public static String getImagenFromPuppeteer(String url, String scriptPath) {
		try {
			String command = "node " + scriptPath + " " + url;

			Process process = Runtime.getRuntime().exec(command);

			// Leer la salida del proceso
			BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder output = new StringBuilder();
			String outputLine;
			while ((outputLine = processReader.readLine()) != null) {
				output.append(outputLine).append("\n");
			}
			processReader.close();

			// Esperar a que termine el proceso
			int exitCode = process.waitFor();
			if (exitCode == 0) {
				// Proceso terminado exitosamente, obtener el resultado
				String dataString = output.toString().trim();
				return dataString; // Devolver el resultado como un String
			} else {
				// Error al ejecutar el script
				System.err.println("Error al ejecutar el script de Puppeteer. Código de salida: " + exitCode);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.err.println("El hilo fue interrumpido. Terminando la ejecución.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error de entrada/salida al ejecutar el script de Puppeteer.");
		}
		return ""; // Devolver una cadena vacía en caso de excepción
	}

	public static String searchWebImagen(String query) {
		String googleSearchUrl = "https://www.google.com/search?q=";
		String charset = "UTF-8";
		String userAgent = "Mozilla/5.0";

		String url;
		try {
			url = googleSearchUrl + URLEncoder.encode(query, charset);
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestProperty("User-Agent", userAgent);

			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), charset))) {
				String line;
				StringBuilder response = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}

				// Buscar enlaces que comiencen con "/url?q="
				Pattern pattern = Pattern.compile("<a href=\"/url\\?q=(https://www.cardtrader.com/[^\"]+)\"");
				Matcher matcher = pattern.matcher(response.toString());

				while (matcher.find()) {
					String urlFound = matcher.group(1);
					urlFound = cleanGoogleUrl(urlFound);
					if (!urlFound.contains("/versions")) {
						return urlFound;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ""; // No se encontró un enlace adecuado
	}

	private static String cleanGoogleUrl(String url) {
		// Eliminar fragmentos adicionales de la URL de Google
		int index = url.indexOf("&");
		if (index != -1) {
			return url.substring(0, index);
		}
		return url;
	}

	public static String extraerImagen(Comic carta) {
//		String argument = "cardtrader+" + carta.getNomCarta().replace(" ", "+") + "+" + carta.getNumCarta() + "+"
//				+ carta.getColeccionCarta().replace(" ", "+");
//		String urlCarta = searchWebImagen(argument);
//		if (urlCarta.contains("/cards/")) {
//			System.out.println(urlCarta);
//			return extraerDatosImagen(urlCarta);
//		}
		return "";
	}

	public static String extraerDatosImagen(String url) {
		Document doc;
		try {
			doc = Jsoup.connect(url).get();

			// Adjust selector according to actual HTML structure of the target page
			Element imagenElemento = doc.selectFirst(
					"div.image-flipper.border-radius-10 img[src*='/uploads/'][src$='.jpg'], div.image-flipper.border-radius-10 img[src*='/uploads/'][src$='.png']");

			if (imagenElemento != null) {
				return "https://www.cardtrader.com/" + imagenElemento.attr("src");
			}
		} catch (IOException e) {
			// Log the error or handle it appropriately
			System.err.println("Error fetching image: " + e.getMessage());
		}

		return "";
	}

	public static String carpetaDescarga() {
		String nombreDB = Utilidades.nombreDB();
		String carpetaPortada = AccionFuncionesComunes.carpetaPortadas(nombreDB);
		String codigoImagen = Utilidades.generarCodigoUnico(carpetaPortada);

		String rutaArchivo = carpetaPortada + File.separator + codigoImagen + ".jpg";

		// Reemplaza las barras invertidas con barras normales para compatibilidad con
		// JavaScript
		rutaArchivo = rutaArchivo.replace(File.separator, "/");

		return rutaArchivo;
	}

}
