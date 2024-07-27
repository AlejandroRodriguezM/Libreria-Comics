package funcionesAuxiliares;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AddToPath {

	private static final String DOWNLOAD_DIR = System.getProperty("user.home") + File.separator + "nodejs";
	private static String userHome = System.getProperty("user.home");
	private static String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
	private static String carpetaLibreria = ubicacion + File.separator + "gradeoComics";
	public static String rutaDestinoRecursos = carpetaLibreria + File.separator + "recursos";
	private static Ventanas nav = new Ventanas();

	public static void main(String[] args) {
		try {
			createAndRunPowerShellScript();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void createAndRunPowerShellScript() throws IOException, InterruptedException {
		String os = System.getProperty("os.name").toLowerCase();
		String path = System.getenv("PATH");
		String nodePath = DOWNLOAD_DIR;

		if (!path.contains(nodePath)) {
			if (os.contains("win")) {
				// Agregar Node.js al PATH en Windows
				try {
					// Crear un script PowerShell temporal en rutaDestinoRecursos
					Path tempScript = Files.createTempFile(Paths.get(rutaDestinoRecursos), "addNodeToPath", ".ps1");
					String scriptContent = String.format(
							"$path = [System.Environment]::GetEnvironmentVariable('Path', 'Machine');"
									+ "if (-not $path.Contains('%s')) {"
									+ "    [System.Environment]::SetEnvironmentVariable('Path', $path + ';%s', 'Machine');"
									+ "    Write-Output 'Node.js agregado al PATH del sistema.'" + "} else {"
									+ "    Write-Output 'Node.js ya está en el PATH del sistema.'" + "}",
							nodePath, nodePath);
					Files.write(tempScript, scriptContent.getBytes());

					// Ejecutar el script PowerShell con privilegios elevados
					String command = String.format(
							"powershell.exe -Command \"Start-Process powershell.exe -ArgumentList '-NoProfile -ExecutionPolicy Bypass -File \"%s\"' -Verb RunAs\"",
							tempScript.toString());
					Process process = Runtime.getRuntime().exec(command);
					process.waitFor();

					// Eliminar el script temporal
//					Files.delete(tempScript);

					if (nav.reiniciarOrdenadorVentana()) {
						Utilidades.reiniciarOrdenadorOS();
					}

				} catch (IOException | InterruptedException e) {
					System.out.println("Ocurrió un error al agregar Node.js al PATH.");
					e.printStackTrace();
				}
			} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
				// Agregar Node.js al PATH en Linux/Mac
				try {
					String command = "export PATH=$PATH:" + nodePath;
					Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command });
					System.out.println("Node.js agregado al PATH del sistema.");
				} catch (IOException e) {
					System.out.println("Ocurrió un error al agregar Node.js al PATH.");
					e.printStackTrace();
				}
			} else {
				System.out.println("Sistema operativo no soportado.");
			}
		}
	}

}
