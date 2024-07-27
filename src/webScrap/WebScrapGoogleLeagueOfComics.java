package webScrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import comicManagement.Comic;

public class WebScrapGoogleLeagueOfComics {

	public static String agregarMasAMayusculas(String cadena) {
		return cadena.toUpperCase();
	}

	public static String buscarURL(String searchTerm) {
		String url = "";
		if (esURL(searchTerm)) {
			url = buscarURLValida(searchTerm);
		} else {
			url = buscarEnGoogle(searchTerm);
		}
		System.out.println("URL: " + url);
		return url;
	}

	public static String buscarURLValida(String urlString) {
		try {
			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");
			try {
				// Introduce un retardo de 1 segundo entre solicitudes
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return url.toString(); // Devuelve la representación de cadena de la URL
			} else {
				System.out.println("La URL proporcionada no es válida.");
				return null;
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String buscarEnGoogle(String searchTerm) {
		searchTerm = agregarMasAMayusculas(searchTerm);
		searchTerm = searchTerm.replace("(", "%28").replace(")", "%29").replace("#", "%23");

		try {
			String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
			String urlString = "https://www.google.com/search?q=" + encodedSearchTerm + "+league+of+comics";

			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			try {
				// Introduce un retardo de 1 segundo entre solicitudes
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

			String html = content.toString();
			int startIndex = html.indexOf("https://leagueofcomicgeeks.com/comic/");
			if (startIndex != -1) {
				int endIndex = html.indexOf("\"", startIndex);
				String[] urls = html.substring(startIndex, endIndex).split("\"");
				return encontrarURLRelevante(urls, searchTerm);
			} else {
				return null;
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encontrarURLRelevante(String[] urls, String searchTerm) {
		String urlElegida = null;
		int maxCoincidencia = 0;
		for (String url : urls) {
			int coincidencia = contarCoincidencias(url.toLowerCase(), searchTerm.toLowerCase());
			if (coincidencia > maxCoincidencia) {
				maxCoincidencia = coincidencia;
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

	public static boolean esURL(String urlString) {
		try {
			// Intenta crear una instancia de URI
			URI uri = new URI(urlString);
			// Verifica si la URI tiene un esquema (protocolo) válido
			if (uri.getScheme() != null
					&& (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https"))) {
				return true; // Si la URI tiene un esquema válido, se considera una URL válida
			}
		} catch (URISyntaxException e) {
			// La cadena no es una URI válida
		}
		return false; // Si hay una excepción o la URI no tiene un esquema válido, la URL no es válida
	}

	public static Comic obtenerDatosDiv(String url) {
		url = buscarURL(url);
		if (url == null) {
			return null;
		}
		try {
			Document doc = Jsoup.connect(url).get();
			String fechaSalida = "";
			String distribuidora = "";
			String valorComic = "0";
			String artistas = "";
			String numComic = "";
			String titulo = "";
			String coverURL = "";
			String key = "";
			String codigoValue = "";

			// Buscar la sección de detalles de la página
			Element detallesPagina = doc.selectFirst("div.page-details");
			if (detallesPagina != null) {
				// Extraer la fecha de salida y la distribuidora
				Element fechaElemento = detallesPagina.selectFirst("a[style]");
				if (fechaElemento != null) {
					fechaSalida = fechaElemento.text();
				}
				Element distribuidoraElemento = detallesPagina.selectFirst("a:not([style])");
				if (distribuidoraElemento != null) {
					distribuidora = distribuidoraElemento.text();
				}
			}

			// Extraer el número de cómic de la etiqueta H1
			Element h1Elemento = doc.selectFirst("h1");
			if (h1Elemento != null) {
				String h1Texto = h1Elemento.text().trim();

				// Busca un patrón de número en el formato #N (donde N es el número del cómic)
				Pattern pattern = Pattern.compile("#(\\d+)");
				Matcher matcher = pattern.matcher(h1Texto);

				if (matcher.find()) {
					// Obtiene el número del cómic
					numComic = matcher.group(1).trim();
				}
				// Asigna el título quitando el número del cómic
				titulo = h1Texto;
				if (matcher.find()) {
					// Quita el número del título
					titulo = h1Texto.substring(0, matcher.start()).trim();
				}
			}

			// Declara conjuntos para almacenar los nombres
			Set<String> coverArtists = new HashSet<>();
			Set<String> writers = new HashSet<>();
			Set<String> artists = new HashSet<>();

			Elements divPadres = doc.select(
					"div.d-flex.flex-column.align-self-start.mt-2, div.row,cover-artists, div.row.details-addtl.copy-small.mt-3");

			for (Element divPadre : divPadres) {
				Element divComentadoAntes = divPadre.selectFirst("div.role.color-offset.copy-really-small");
				Element link = divPadre.selectFirst("a");

				if (divComentadoAntes != null && link != null) {
					String textoDiv = divComentadoAntes.text().toLowerCase();
					String textoEnlace = link.text().replace("-", "");

					// Listas de términos para buscar
					List<String> coverTerms = Arrays.asList("cover artist", "cover penciller");
					List<String> artistTerms = Arrays.asList("artist", "artist, colorist", "penciller");

					// Separar los roles en textoDiv
					String[] roles = textoDiv.split(",");

					for (String role : roles) {
						role = role.trim(); // Eliminar espacios en blanco alrededor de los roles

						if (coverTerms.contains(role)) {
							coverArtists.add(textoEnlace);
						} else if (role.equals("writer")) {
							writers.add(textoEnlace);
						} else if (artistTerms.contains(role)) {
							// Asegurarse de que no sea un cover artist
							if (!coverTerms.contains(role)) {
								artists.add(textoEnlace);
							}
						}
					}
				}
			}

			Element divInfoComic = doc.selectFirst("div.col.copy-small.font-italic");
			if (divInfoComic != null) {
				String info = divInfoComic.text();
				// Utilizar expresión regular para encontrar el valor siguiente al símbolo '$'
				Pattern pattern = Pattern.compile("\\$([\\d.]+)");
				Matcher matcher = pattern.matcher(info);
				if (matcher.find()) {
					valorComic = matcher.group(1);
				}
			}

			Element coverArtDiv = doc.selectFirst("div.cover-art");
			if (coverArtDiv != null) {
				Element img = coverArtDiv.selectFirst("img");
				if (img != null) {
					// Obtener la URL de la imagen
					coverURL = img.attr("src");
					// Eliminar el texto después de ".jpg" si está presente
					int index = coverURL.indexOf(".jpg");
					if (index != -1) {
						coverURL = coverURL.substring(0, index + 4);
					}
				}
			}

			Element listingDescriptionDiv = doc.selectFirst("div.col-12.listing-description");
			if (listingDescriptionDiv != null) {
				Elements parrafos = listingDescriptionDiv.select("p");
				for (Element parrafo : parrafos) {
					key += parrafo.text();
				}
			}

			Elements detailsAddtlDivs = doc.select("div.col-xxl-4.col-lg-6.col-6.mb-3.details-addtl-block");
			for (Element detailsAddtlDiv : detailsAddtlDivs) {
				Element valueDiv = null;

				Element distributorSkuDiv = detailsAddtlDiv.selectFirst("div.name:contains(Distributor SKU)");
				if (distributorSkuDiv != null) {
					valueDiv = distributorSkuDiv.nextElementSibling();
					if (valueDiv != null && valueDiv.hasClass("value")) {
						codigoValue = valueDiv.text();
					}
				}

				Element upcDiv = detailsAddtlDiv.selectFirst("div.name:contains(UPC)");
				if (upcDiv != null) {
					valueDiv = upcDiv.nextElementSibling();
					if (valueDiv != null && valueDiv.hasClass("value")) {
						codigoValue = valueDiv.text();
					}
				}

				Element isbnDiv = detailsAddtlDiv.selectFirst("div.name:contains(ISBN)");
				if (isbnDiv != null) {
					valueDiv = isbnDiv.nextElementSibling();
					if (valueDiv != null && valueDiv.hasClass("value")) {
						codigoValue = valueDiv.text();
					}
				}
			}

			String fecha = convertirFechaMySQL(fechaSalida);

			// Ahora, convierte los conjuntos a cadenas
			String cover = String.join(", ", coverArtists);
			String guionista = String.join(", ", writers);
			String artistasString = String.join(", ", artists);

			cover = Comic.limpiarCampo(cover);
			titulo = Comic.limpiarCampo(titulo);
			distribuidora = Comic.limpiarCampo(distribuidora);
			if (distribuidora.equalsIgnoreCase("Marvel Comics")) {
				distribuidora = "Marvel";
			}
			guionista = Comic.limpiarCampo(guionista);
			artistas = Comic.limpiarCampo(artistasString);
			key = Comic.limpiarCampo(key);
			codigoValue = Comic.limpiarCampo(codigoValue);

			return new Comic.ComicGradeoBuilder("", titulo).codigoComic(codigoValue).precioComic(valorComic)
					.numeroComic(numComic).fechaGradeo(fecha).editorComic(distribuidora).keyComentarios(key)
					.firmaComic("").artistaComic(artistas).guionistaComic(guionista).varianteComic(cover)
					.direccionImagenComic(coverURL).urlReferenciaComic(url).build();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String nombreComicMarvel(String url) throws IOException {
		// Conectarse a la página web y obtener el documento
		Document doc = Jsoup.connect(url).get();

		// Buscar el elemento <h1>
		Element h1Elemento = doc.selectFirst("h1");

		if (h1Elemento != null) {
			// Buscar el <span> dentro del <h1>
			Element spanElemento = h1Elemento.selectFirst("span");

			if (spanElemento != null) {
				// Devolver el texto del <span>
				return spanElemento.text();
			} else {
				return "No se encontró un <span> dentro del <h1>.";
			}
		} else {
			return "No se encontró un <h1> en la página.";
		}
	}

	public static String convertirFechaMySQL(String fechaSalida) {
		try {
			// Mapear nombres de los meses en inglés a números
			String[] meses = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
			// Parsear la fecha al formato adecuado
			SimpleDateFormat formatoEntrada = new SimpleDateFormat("MM dd, yyyy");
			// Remover el sufijo ordinal numérico antes de parsear
			fechaSalida = fechaSalida.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
			// Dividir la fecha en partes: mes, día, año
			String[] partesFecha = fechaSalida.split(" ");
			// Convertir el nombre del mes a un número
			int mesNum = -1;
			for (int i = 0; i < meses.length; i++) {
				if (meses[i].equals(partesFecha[0])) {
					mesNum = i + 1; // Añadir 1 porque los meses en SimpleDateFormat comienzan en 0
					break;
				}
			}
			// Formatear la fecha al formato MySQL
			SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
			if (partesFecha.length >= 3) {
				Date fecha = formatoEntrada
						.parse(String.format("%02d", mesNum) + " " + partesFecha[1] + " " + partesFecha[2]);
				return formatoSalida.format(fecha);
			} else {
				return "2000-01-01"; // Devuelve la fecha por defecto
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

//	public static void main(String[] args) throws URISyntaxException {
//
//		String url = "75960620168600411";
//
//		obtenerDatosDiv(url);
//
//	}
}
