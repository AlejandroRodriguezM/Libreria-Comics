package UNIT_TEST;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import funcionesAuxiliares.Utilidades;

public class TestApiImagen {

	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private static final String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private static final String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";
	
	public static String carpetaPortadas(String nombreDatabase) {
		return DOCUMENTS_PATH + File.separator + "album_cartas" + File.separator + nombreDatabase + File.separator
				+ "portadas";
	}
	
	public static void main(String[] args) {

		// Corrección y generación de la URL final de la imagen
		String correctedUrl = "https://www.cardtrader.com/uploads/blueprints/image/22426/show_sliver-legion-future-sight.jpg";
		String codigoImagen = Utilidades.generarCodigoUnico(carpetaPortadas(Utilidades.nombreDB()) + File.separator);
		URI uri = null;
		try {
			uri = new URI(correctedUrl);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		descargarYConvertirImagenAsync(uri, carpetaPortadas(Utilidades.nombreDB()), codigoImagen + ".jpg");

	}

	public static CompletableFuture<Boolean> descargarYConvertirImagenAsync(URI urlImagen, String carpetaDestino,
			String nuevoNombre) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				URL url = urlImagen.toURL();
				URLConnection connection = url.openConnection();

				if (connection instanceof HttpURLConnection) {
					((HttpURLConnection) connection).setRequestMethod("HEAD");
					int responseCode = ((HttpURLConnection) connection).getResponseCode();

					if (responseCode != HttpURLConnection.HTTP_OK) {
						if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
							System.err.println("Error interno del servidor al acceder a la URL: " + url);
						} else {
							System.err.println("La URL no apunta a una imagen válida o no se pudo acceder: " + url);
						}
						return false;
					}
				}

				String extension = obtenerExtension(nuevoNombre);
				Path rutaDestino = Path.of(carpetaDestino, nuevoNombre);

				BufferedImage image;
				try (InputStream in = url.openStream()) {
					image = ImageIO.read(in);
					if (image == null) {
						System.err.println("No se pudo cargar la imagen desde " + urlImagen);
						return false;
					}
				}

				ImageIO.write(image, extension, rutaDestino.toFile());

				return true;
			} catch (MalformedURLException e) {
				System.err.println("La URL no es válida: " + urlImagen);
				return false;
			} catch (IOException e) {
				return false;
			}
		});
	}

	public static String obtenerExtension(String entrada) {
		int ultimoPunto;
		if (entrada.contains("/")) {
			// Si la entrada contiene "/", se asume que es una URL
			int ultimoSlash = entrada.lastIndexOf("/");
			String nombreArchivo = entrada.substring(ultimoSlash + 1);
			ultimoPunto = nombreArchivo.lastIndexOf(".");
		} else {
			// Si no contiene "/", se asume que es solo el nombre del archivo
			ultimoPunto = entrada.lastIndexOf(".");
		}

		if (ultimoPunto == -1) {
			return "";
		}

		return entrada.substring(ultimoPunto + 1).toLowerCase();
	}
}
