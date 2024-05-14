package webScrap;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import comicManagement.Comic;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import ficherosFunciones.FuncionesFicheros;
import funciones_auxiliares.Utilidades;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.coobird.thumbnailator.Thumbnails;

public class WebScrapGooglePreviewsWorld extends Application {

	public static String agregarMasAMayusculas(String cadena) {
		return cadena.toUpperCase();
	}

	public static String buscarURL(String searchTerm) throws URISyntaxException {

		return buscarEnGoogle(searchTerm);

	}

	public static String buscarURLValida(String urlString) throws URISyntaxException {
		try {
			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return url.toString(); // Devuelve la representación de cadena de la URL
			} else {
				System.out.println("La URL proporcionada no es válida.");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String buscarEnGoogle(String searchTerm) throws URISyntaxException {
		searchTerm = agregarMasAMayusculas(searchTerm);
		searchTerm = searchTerm.replace("(", "%28").replace(")", "%29").replace("#", "%23");

		try {
			String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
			String urlString = "https://www.google.com/search?q=" + encodedSearchTerm + "+previews+world";
			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

			int responseCode = con.getResponseCode();
			if (responseCode == 429) {
				// Manejar el error de demasiadas solicitudes
				System.out.println("Demasiadas solicitudes. Esperando y reintentando...");
				Thread.sleep(5000); // Esperar 5 segundos antes de reintentar
				return buscarEnGoogle(searchTerm); // Reintentar la búsqueda
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

			String html = content.toString();
			int startIndex = html.indexOf("https://www.previewsworld.com/");
			if (startIndex != -1) {
				int endIndex = html.indexOf("\"", startIndex);
				String[] urls = html.substring(startIndex, endIndex).split("\"");
				String googleUrl = "https://www.previewsworld.com/";
				return encontrarURLRelevante(urls, searchTerm, googleUrl);
			} else {
				return null;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encontrarURLRelevante(String[] urls, String searchTerm, String googleUrl) {
		String urlElegida = null;
		int minDifference = Integer.MAX_VALUE;
		for (String url : urls) {
			int difference = Math.abs(url.indexOf(googleUrl) - url.indexOf(url));
			if (difference < minDifference) {
				minDifference = difference;
				urlElegida = url;
			}
		}
		return urlElegida;
	}

	public static int contarCoincidencias(String url, String searchTerm) {
		int coincidencia = 0;
		for (String word : searchTerm.split("\\s+")) {
			if (url.contains(word)) {
				coincidencia += word.length();
			}
		}
		return coincidencia;
	}

	public static int verificarCodigoRespuesta(String urlString) throws IOException, URISyntaxException {
		try {
			// Desactivar la validación del certificado SSL/TLS
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}
			} };

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			URI uri = new URI(urlString);
			HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.setRequestMethod("GET");

			// Obtener el código de respuesta HTTP
			int codigoRespuesta = connection.getResponseCode();

			// Cerrar la conexión
			connection.disconnect();

			return codigoRespuesta;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static boolean isDocumentEmpty(Document document) {
		// Verificar si el documento es null
		if (document == null) {
			return true;
		}

		// Verificar si el documento no tiene ningún elemento
		return document.body().children().isEmpty();
	}

	/**
	 * Realiza scraping en una URL dada para extraer información sobre un cómic.
	 * 
	 * @param diamondCode El código del cómic.
	 * @throws URISyntaxException
	 */
	public static void displayComicInfo(Comic comic) throws URISyntaxException {
		try {

			String nomComic = comic.getNombre();
			String numComic = comic.getNumero();
			String variante = comic.getVariante();

			String claveBusqueda = nomComic + " " + numComic + " " + variante;

			claveBusqueda = claveBusqueda.trim();
			String previewsWorldUrl = buscarEnGoogle(claveBusqueda);

			if (previewsWorldUrl == null || previewsWorldUrl.isEmpty()) {
				return;
			}

			Document document = Jsoup.connect(previewsWorldUrl).get();

			if (isDocumentEmpty(document)) {
				System.out.println("La página está vacía.");
				return;
			}

			String titulo = scrapeTitle(document);
			String numero = extractNumeroFromTitle(document);
			String formato = "Grapa (Issue individual)";
			String precio = scrapeAndPrintSRP(document);
			String code = scrapeElementCode(document, "div.ItemCode");
			String procedencia = "Estados Unidos (United States)";
			String estado = "En posesión";
			String puntuacion = "Sin puntuación";
			String gradeo = "NM (Noir Medium)";
			Comic comicInfoArray = new Comic.ComicBuilder(comic.getid(), titulo).valorGradeo(gradeo).numero(numero)
					.variante("").firma("").editorial("").formato(formato).procedencia(procedencia).fecha("")
					.guionista("").dibujante("").estado(estado).keyIssue("").puntuacion(puntuacion).imagen("")
					.referenciaComic(previewsWorldUrl).precioComic(precio).codigoComic(code).build();

			// Escribir en un archivo
			writeToTextFile(comicInfoArray, claveBusqueda);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void displayComicInfoString(String claveBusqueda) throws URISyntaxException {
		try {
			claveBusqueda = claveBusqueda.trim();
			String previewsWorldUrl = buscarEnGoogle(claveBusqueda);

			if (previewsWorldUrl == null || previewsWorldUrl.isEmpty()) {
				return;
			}

			Document document = Jsoup.connect(previewsWorldUrl).get();

			if (isDocumentEmpty(document)) {
				System.out.println("La página está vacía.");
				return;
			}

			String titulo = scrapeTitle(document);
			String numero = extractNumeroFromTitle(document);
			String formato = "Grapa (Issue individual)";
			String precio = scrapeAndPrintSRP(document);
			String code = scrapeElementCode(document, "div.ItemCode");
			String procedencia = "Estados Unidos (United States)";
			String estado = "En posesión";
			String puntuacion = "Sin puntuación";
			String gradeo = "NM (Noir Medium)";
			Comic comicInfoArray = new Comic.ComicBuilder("", titulo).valorGradeo(gradeo).numero(numero).variante("")
					.firma("").editorial("").formato(formato).procedencia(procedencia).fecha("").guionista("")
					.dibujante("").estado(estado).keyIssue("").puntuacion(puntuacion).imagen("")
					.referenciaComic(previewsWorldUrl).precioComic(precio).codigoComic(code).build();

			// Escribir en un archivo
			writeToTextFile(comicInfoArray, claveBusqueda);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Extrae el título del documento HTML proporcionado. Elimina el símbolo "#" y
	 * todo lo que le sigue, luego capitaliza la primera letra de cada palabra en el
	 * título.
	 *
	 * @param document El documento HTML a analizar.
	 * @return El título extraído y formateado, o null si no se encuentra.
	 */
	private static String scrapeTitle(Document document) {
		Element titleElement = document.selectFirst("h1.Title");
		if (titleElement != null) {
			String titleContent = titleElement.text().trim();
			// Eliminar el símbolo "#" y todo lo que le sigue
			titleContent = removeHashtagAndFollowing(titleContent);
			// Capitalizar la primera letra de cada palabra en el título
			titleContent = capitalizeFirstLetter(titleContent);
			return titleContent;
		}
		return null;
	}

	/**
	 * Extrae el número que sigue al símbolo "#" en el título.
	 *
	 * @param document El documento HTML del cual extraer.
	 * @return El número extraído o null si no se encuentra.
	 */
	private static String extractNumeroFromTitle(Document document) {
		Element titleElement = document.selectFirst("h1.Title");
		if (titleElement != null) {
			String titleContent = titleElement.text().trim();
			// Buscar el índice del símbolo "#"
			int hashtagIndex = titleContent.indexOf('#');
			if (hashtagIndex != -1 && hashtagIndex + 1 < titleContent.length()) {
				// Obtener la subcadena que sigue al símbolo "#"
				String subStringAfterHashtag = titleContent.substring(hashtagIndex + 1);

				// Utilizar una expresión regular para extraer todos los números hasta el
				// próximo espacio o caracteres no numéricos
				java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d+).*?")
						.matcher(subStringAfterHashtag);
				if (matcher.find()) {
					return matcher.group(1).trim();
				} else {
					return "0";
				}
			} else {
				return "0";
			}
		}
		return null;
	}

	/**
	 * Elimina el símbolo "#" y todo lo que le sigue del contenido del título.
	 *
	 * @param titleContent El contenido del título a procesar.
	 * @return El contenido del título sin el "#" y el texto siguiente.
	 */
	private static String removeHashtagAndFollowing(String titleContent) {
		if (titleContent.contains("#")) {
			int hashtagIndex = titleContent.indexOf('#');
			return titleContent.substring(0, hashtagIndex).trim().toLowerCase();
		}
		return titleContent;
	}

	/**
	 * Extrae e imprime el precio sugerido de venta (SRP) del documento.
	 *
	 * @param document El documento HTML a analizar.
	 * @return El valor SRP, o null si no se encuentra.
	 */
	private static String scrapeAndPrintSRP(Document document) {
		Element srpElement = document.selectFirst("div.SRP");
		if (srpElement != null) {
			String srpValue = srpElement.text().replaceAll("[^0-9.]", "").trim();
			return srpValue;
		}
		return null;
	}

	/**
	 * Extrae el contenido de texto de un elemento HTML específico identificado por
	 * el selector.
	 *
	 * @param document El documento HTML a analizar.
	 * @param selector El selector CSS para el elemento objetivo.
	 * @return El contenido de texto del elemento seleccionado, o null si no se
	 *         encuentra.
	 */
	private static String scrapeElementCode(Document document, String selector) {
		Elements elements = document.select(selector);
		if (!elements.isEmpty()) {
			Element element = elements.first();
			return element.text();
		}
		return null;
	}

	/**
	 * Capitaliza la primera letra de cada palabra en la cadena de entrada.
	 *
	 * @param input La cadena de entrada a capitalizar.
	 * @return La cadena de entrada con la primera letra de cada palabra en
	 *         mayúscula.
	 */
	private static String capitalizeFirstLetter(String input) {
		StringBuilder result = new StringBuilder();

		input = input.toLowerCase();

		boolean capitalizeNext = true;

		for (char ch : input.toCharArray()) {
			if (Character.isWhitespace(ch)) {
				capitalizeNext = true;
			} else if (capitalizeNext) {
				ch = Character.toTitleCase(ch);
				capitalizeNext = false;
			}

			result.append(ch);
		}

		return result.toString();
	}

	private static void writeToTextFile(Comic comicInfo, String busquedaComic) {

		String desktopPath = System.getProperty("user.home") + "/Desktop/";
		String filePath = desktopPath + "comic_infoUpdate.txt";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

			System.out.println("Búsqueda: " + busquedaComic + " - Código: " + comicInfo.getcodigoComic() + " - URL: "
					+ comicInfo.getUrlReferencia());

			writer.write("UPDATE comicsbbdd set codigo_comic = '" + comicInfo.getcodigoComic() + "' where ID = "
					+ comicInfo.getid() + "; " + comicInfo.getUrlReferencia());

			// Escribe cualquier otra información que desees en el archivo
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	private static void limpiarFichero() {
//
//		String desktopPath = System.getProperty("user.home") + "/Desktop/";
//		String filePath = desktopPath + "comic_infoUpdate.txt";
//
//		String outputFile = desktopPath + "output.txt"; // Ruta del archivo de salida
//
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(filePath));
//			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
//
//			String line;
//			while ((line = reader.readLine()) != null) {
//				int indexOfSemicolon = line.indexOf(';');
//				if (indexOfSemicolon != -1) {
//					// Si se encuentra un punto y coma, se escribe la parte de la línea antes de él
//					writer.write(line.substring(0, indexOfSemicolon + 1)); // +1 para incluir el punto y coma
//					writer.newLine();
//				} else {
//					// Si no se encuentra un punto y coma, se escribe la línea completa
//					writer.write(line);
//					writer.newLine();
//				}
//			}
//
//			reader.close();
//			writer.close();
//
//			System.out.println("El archivo ha sido procesado exitosamente.");
//		} catch (IOException e) {
//			System.err.println("Error al procesar el archivo: " + e.getMessage());
//			e.printStackTrace();
//		}
//
//	}

	public static void compressImages(List<String> inputPaths, double compressionFactor, double scaleRatio) {
		long startTime = System.currentTimeMillis(); // Tiempo de inicio

		int numEntries = inputPaths.size();
		int numConverted = 0;
		try {

			for (String inputPath : inputPaths) {
				System.out.println("Comprimiendo: " + (numConverted + 1) + " de " + numEntries);
				File inputFile = new File(inputPath);
				BufferedImage image = ImageIO.read(inputFile);

				// Redimensionar la imagen al nuevo tamaño y aplicar la calidad de compresión
				Thumbnails.of(image).scale(scaleRatio).outputQuality(compressionFactor).toFile(new File(inputPath));

				numConverted++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis(); // Tiempo de finalización
		long duration = endTime - startTime; // Duración total de la conversión

		long minutes = (duration / 1000) / 60; // Convertir milisegundos a minutos
		long seconds = (duration / 1000) % 60; // Resto de la división para obtener segundos

		System.out.println("¡La conversión ha finalizado! Tiempo total de conversión: " + minutes + " minutos "
				+ seconds + " segundos");
	}

//    public static void readMetadata() {
//        JFrame frame = new JFrame();
//        JFileChooser fileChooser = new JFileChooser();
//        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif", "bmp");
//        fileChooser.setFileFilter(filter);
//
//        int result = fileChooser.showOpenDialog(frame);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = fileChooser.getSelectedFile();
//            try {
//                BufferedImage image = ImageIO.read(selectedFile);
//                ImageInputStream iis = ImageIO.createImageInputStream(selectedFile);
//                Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
//                if (readers.hasNext()) {
//                    ImageReader reader = readers.next();
//                    reader.setInput(iis, true);
//                    IIOMetadata metadata = reader.getImageMetadata(0);
//                    String[] names = metadata.getMetadataFormatNames();
//                    for (String name : names) {
//                        System.out.println("Formato de metadatos: " + name);
//                        displayMetadata(metadata.getAsTree(name));
//                    }
//                } else {
//                    System.out.println("No se encontró lector de imagen.");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    private static void displayMetadata(org.w3c.dom.Node root) {
//        displayMetadata(root, 0);
//    }

//    private static void displayMetadata(org.w3c.dom.Node node, int level) {
//        // Display node name
//        for (int i = 0; i < level; i++)
//            System.out.print("    ");
//        System.out.print(node.getNodeName() + ": ");
//        // Display node value, if any
//        org.w3c.dom.Node child = node.getFirstChild();
//        if (child != null) {
//            System.out.println(child.getNodeValue());
//        } else {
//            System.out.println();
//        }
//        // Recursively display children
//        org.w3c.dom.NodeList children = node.getChildNodes();
//        for (int i = 0; i < children.getLength(); i++) {
//            displayMetadata(children.item(i), level + 1);
//        }
//    }

	public static final String DOCUMENTS_PATH = System.getProperty("user.home") + File.separator + "Documents";

	/**
	 * Elimina archivos en un directorio común que no están presentes en la lista
	 * proporcionada de URLs.
	 *
	 * @param inputPaths Lista de URLs que representan los archivos a conservar.
	 */
	public static void borrarArchivosNoEnLista(List<String> inputPaths) {
		String directorioComun = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
				+ ConectManager.DB_NAME + File.separator + "portadas" + File.separator;
		List<String> nombresArchivosEnDirectorio = Utilidades.obtenerNombresArchivosEnDirectorio(directorioComun);

		for (String nombreArchivo : nombresArchivosEnDirectorio) {
			Path archivoAEliminarPath = Paths.get(nombreArchivo);

			if (!inputPaths.contains(archivoAEliminarPath.toString())) {
				try {
					if (Files.exists(archivoAEliminarPath) && Files.isRegularFile(archivoAEliminarPath)) {
						Files.delete(archivoAEliminarPath);
					}
				} catch (Exception e) {
					Utilidades.manejarExcepcion(e);
				}
			}
		}
	}

	public static List<String> leerArchivoTXT() {
		// Mostrar el diálogo de selección de archivo
		String frase = "Fichero TXT";
		String formatoFichero = "*.txt";

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter(frase, formatoFichero));

		// Obtener el directorio actual del usuario
		File directorioUsuario = new File(System.getProperty("user.home"));
		fileChooser.setInitialDirectory(directorioUsuario);

		File fichero = fileChooser.showOpenDialog(null);

		// Crear una lista para almacenar las líneas del archivo
		List<String> lines = new ArrayList<>();

		// Verificar si se seleccionó un archivo
		if (fichero != null) {
			try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
				String line;
				// Leer cada línea del archivo y agregarla a la lista
				while ((line = reader.readLine()) != null) {
					lines.add(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No se seleccionó ningún archivo.");
		}

		return lines;
	}

	public static void main(String[] args) {

		String datosFichero = FuncionesFicheros.datosEnvioFichero();
		List<String> inputPaths = DBUtilidades.obtenerValoresColumna("portada");
		borrarArchivosNoEnLista(inputPaths);
//		convertJpgToPng(inputPaths);
//		readMetadata();
		// Comprimir las imágenes con un factor de compresión del 0.5 (50%)
//		compressImages(inputPaths, 0.5, 1);

//		String sentenciaSQL = DBUtilidades.construirSentenciaSQL(TipoBusqueda.COMPLETA);

//		List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
//		Platform.runLater(() -> {
//			List<String> listaComics = leerArchivoTXT();
//
//			for (String claveBusqueda : listaComics) {
//
//				// Esperar 1 segundo entre cada solicitud
//				try {
//					displayComicInfoString(claveBusqueda);
//					Thread.sleep(3000);
//				} catch (InterruptedException | URISyntaxException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} // 1000 milisegundos = 1 segundo
//			}
//			return;
//		});
//		limpiarFichero();

	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
