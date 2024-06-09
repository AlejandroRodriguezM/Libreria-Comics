
package funcionesAuxiliares;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class VersionService extends Service<String> {
	private static final String GITHUB_URL = "https://raw.githubusercontent.com/AlejandroRodriguezM/Libreria-Comics/main/src/funciones_auxiliares/version.txt";

	@Override
	protected Task<String> createTask() {
		return new Task<String>() {
			@Override
			protected String call() throws Exception {
				return obtenerVersion();
			}
		};
	}

	public static String obtenerVersion() {

		if (Utilidades.isInternetAvailable()) {
			try {

				URI uri = new URI(GITHUB_URL);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(uri.toURL().openStream(), StandardCharsets.UTF_8));
				return reader.lines().collect(Collectors.joining());
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
				return "Error al obtener la versión";
			}
		}
		return null;

	}

	public static String leerVersionDelArchivo() {
		StringBuilder version = new StringBuilder();
		String direccionVersion = "version.txt";

		try (InputStream is = Utilidades.class.getResourceAsStream(direccionVersion);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			String linea;
			while ((linea = reader.readLine()) != null) {
				version.append(linea);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "Error al leer la versión";
		}

		return version.toString();
	}
}
