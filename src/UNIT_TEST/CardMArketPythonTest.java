package UNIT_TEST;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import ficherosFunciones.FuncionesFicheros;

public class CardMArketPythonTest {

	public static void main(String[] args) {
		String pythonScriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap.py";
		String url = "https://www.cardmarket.com/en/Magic/Products/Singles/Modern-Horizons-3/Witch-Enchanter-Witch-Blessed-Meadow";

		try {
			// Construir el comando para ejecutar el script de Python
			ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, url);

			// Redireccionar la salida de error del proceso para que sea la misma que la de
			// salida
			processBuilder.redirectErrorStream(true);

			// Iniciar el proceso
			Process process = processBuilder.start();

			// Leer la salida del proceso
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			// Esperar a que el proceso termine
			int exitCode = process.waitFor();
			System.out.println("Script de Python terminado con código de salida: " + exitCode);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
