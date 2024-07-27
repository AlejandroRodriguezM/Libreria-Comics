package webScrap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import funcionesAuxiliares.AddToPath;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class WebScrapNodeJSInstall {

	private static final String NODEJS_DIST_URL = "https://nodejs.org/dist/latest/";
	private static final String DOWNLOAD_DIR = System.getProperty("user.home") + File.separator + "nodejs";

	public static void estadoNodeInstallacion() {
		try {

			if (!checkNodeJSVersion()) {
				String nodeZipUrl = getNodeJsDownloadUrl();
				String urlCompleta = NODEJS_DIST_URL + nodeZipUrl;
				downloadAndExtractNodeJS(urlCompleta);
			}

			if (!isPuppeteerInstalled()) {
				installPuppeteer();
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static String getNodeJsDownloadUrl() throws IOException {
		Document doc = Jsoup.connect(NODEJS_DIST_URL).get();
		Elements links = doc.select("a[href]");
		String os = System.getProperty("os.name").toLowerCase();

		for (Element link : links) {
			String href = link.attr("href");

			if (isWindows(os) && href.endsWith("-win-x64.zip")) {
				return href;
			} else if (isLinux(os) && isLinuxArchitecture(href, os)) {
				return href;
			} else if (isMac(os) && isMacArchitecture(href, os)) {
				return href;
			}
		}

		return null;
	}

	private static boolean isWindows(String os) {
		return os.contains("win");
	}

	private static boolean isLinux(String os) {
		return os.contains("linux");
	}

	private static boolean isMac(String os) {
		return os.contains("mac");
	}

	private static boolean isLinuxArchitecture(String href, String os) {
		if (os.contains("arm64")) {
			return href.endsWith("arm64.tar.gz") || href.endsWith("arm64.tar.xz");
		} else if (os.contains("armv7l")) {
			return href.endsWith("armv7l.tar.gz") || href.endsWith("armv7l.tar.xz");
		} else if (os.contains("ppc64le")) {
			return href.endsWith("ppc64le.tar.gz") || href.endsWith("ppc64le.tar.xz");
		} else if (os.contains("s390x")) {
			return href.endsWith("s390x.tar.gz") || href.endsWith("s390x.tar.xz");
		} else {
			return href.endsWith("x64.tar.gz") || href.endsWith("x64.tar.xz");
		}
	}

	private static boolean isMacArchitecture(String href, String os) {
		return href.endsWith("darwin-arm64.tar.gz") || href.endsWith("darwin-arm64.tar.xz")
				|| href.endsWith("darwin-x64.tar.gz") || href.endsWith("darwin-x64.tar.xz");
	}

	public static boolean checkNodeJSVersion() {
		String os = System.getProperty("os.name").toLowerCase();
		String command;

		if (os.contains("win")) {
			command = "cmd /c node --version";
		} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
			command = "node --version";
		} else {
			System.out.println("Sistema operativo no soportado.");
			return false;
		}

		try {
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = reader.readLine();

			if (line != null && !line.isEmpty()) {
				System.out.println("Node.js version: " + line);
				return true;
			} else {
				System.out.println("Node.js no está instalado.");
				return false;
			}
		} catch (IOException e) {
			System.out.println("Node.js no está instalado o ocurrió un error al verificar la versión.");
			return false;
		}
	}

	private static void downloadAndExtractNodeJS(String nodeZipUrl) throws IOException, InterruptedException {
		Path zipPath = Paths.get(DOWNLOAD_DIR, getNodeJsDownloadUrl());
		Path extractPath = Paths.get(DOWNLOAD_DIR);

		System.out.println(DOWNLOAD_DIR);

		File carpeta = new File(DOWNLOAD_DIR);
		carpeta.mkdir();

		// Descargar Node.js si aún no está descargado
		if (!Files.exists(zipPath)) {
			System.out.println("Descargando Node.js desde " + nodeZipUrl);
			downloadFile(nodeZipUrl, zipPath);
		}

		// Esperar a que la descarga se complete
		while (!Files.exists(zipPath) || Files.size(zipPath) == 0) {
			Thread.sleep(1000); // Esperar 1 segundo antes de verificar de nuevo
		}

		// Descomprimir Node.js
		if (!Files.exists(extractPath.resolve("node.exe"))) {
			System.out.println("Descomprimiendo Node.js...");
			unzip(zipPath, extractPath);
		}

		AddToPath.createAndRunPowerShellScript();
	}

	private static void downloadFile(String urlStr, Path destination) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		try (InputStream in = httpConn.getInputStream();
				FileOutputStream out = new FileOutputStream(destination.toFile())) {
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		}
	}

	private static void unzip(Path zipFilePath, Path destDir) throws IOException {
		try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath.toFile()))) {
			ZipEntry entry = zipIn.getNextEntry();
			while (entry != null) {
				Path filePath = destDir.resolve(entry.getName());
				if (!entry.isDirectory()) {
					Files.createDirectories(filePath.getParent());
					try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath.toFile()))) {
						byte[] bytesIn = new byte[4096];
						int read;
						while ((read = zipIn.read(bytesIn)) != -1) {
							bos.write(bytesIn, 0, read);
						}
					}
				} else {
					Files.createDirectories(filePath);
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
		}
		String carpeta = DOWNLOAD_DIR + File.separator + getNodeJsDownloadUrl().split(".zip")[0];

		moverContenido(carpeta);
		Files.delete(zipFilePath); // Borra el archivo ZIP después de descomprimirlo y mover su contenido
	}

	public static void moverContenido(String direccionLocal) {
		// Obtener la ruta de la carpeta local y su carpeta padre
		Path carpetaLocal = Paths.get(direccionLocal);
		Path carpetaPadre = carpetaLocal.getParent();

		if (carpetaPadre != null) {
			try {
				// Mover todos los archivos y subdirectorios a la carpeta padre
				Files.list(carpetaLocal).forEach(item -> {
					try {
						Files.move(item, carpetaPadre.resolve(item.getFileName()));
					} catch (IOException e) {
						System.err.println("Error al mover " + item + ": " + e.getMessage());
					}
				});

				// Eliminar la carpeta local
				Files.delete(carpetaLocal);
				System.out.println("Contenido de " + direccionLocal + " movido y carpeta eliminada exitosamente.");

			} catch (IOException e) {
				System.err.println("Error al mover contenido y eliminar carpeta: " + e.getMessage());
			}
		} else {
			System.err.println("La carpeta no tiene un padre.");
		}
	}

	private static boolean isPuppeteerInstalled() {
		String os = System.getProperty("os.name").toLowerCase();
		String nodeExec = "";
		String npmCli = "";

		if (os.contains("win")) {
			nodeExec = DOWNLOAD_DIR + "\\node.exe";
			npmCli = DOWNLOAD_DIR + "\\node_modules\\npm\\bin\\npm-cli.js";
		} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
			nodeExec = DOWNLOAD_DIR + "/node";
			npmCli = DOWNLOAD_DIR + "/node_modules/npm/bin/npm-cli.js";
		} else {
			System.out.println("Sistema operativo no soportado.");
			return false;
		}

		try {
			// Verificar si Puppeteer está instalado
			ProcessBuilder processBuilder = new ProcessBuilder(nodeExec, npmCli, "list", "puppeteer");
			processBuilder.directory(new File(DOWNLOAD_DIR));

			// Asegurar permisos de ejecución
			File downloadDirFile = new File(DOWNLOAD_DIR);
			if (!downloadDirFile.canExecute()) {
				System.out.println("No se pueden ejecutar comandos en el directorio: " + DOWNLOAD_DIR);
				return false;
			}

			Process process = processBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			boolean puppeteerInstalled = false;

			while ((line = reader.readLine()) != null) {
				if (line.contains("puppeteer")) {
					puppeteerInstalled = true;
					break;
				}
			}

			if (puppeteerInstalled) {
				System.out.println("Librería puppeteer instalada");
			} else {
				System.err.println("Librería puppeteer no instalada");
			}

			return puppeteerInstalled;

		} catch (IOException e) {
			System.out.println("Ocurrió un error al verificar si Puppeteer está instalado.");
			e.printStackTrace();
			return false;
		}
	}

	private static void installPuppeteer() {
		String os = System.getProperty("os.name").toLowerCase();
		String nodeExec = "";
		String npmCli = "";

		if (os.contains("win")) {
			nodeExec = DOWNLOAD_DIR + "\\node.exe";
			npmCli = DOWNLOAD_DIR + "\\node_modules\\npm\\bin\\npm-cli.js";
		} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
			nodeExec = DOWNLOAD_DIR + "/node";
			npmCli = DOWNLOAD_DIR + "/node_modules/npm/bin/npm-cli.js";
		} else {
			System.out.println("Sistema operativo no soportado.");
			return;
		}

		try {
			// Instalar Puppeteer
			ProcessBuilder processBuilder = new ProcessBuilder(nodeExec, npmCli, "install", "puppeteer");
			processBuilder.directory(new File(DOWNLOAD_DIR));
			Process process = processBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			System.out.println("Puppeteer se ha instalado correctamente.");

		} catch (IOException e) {
			System.out.println("Ocurrió un error al instalar Puppeteer.");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// Obtener el valor de la variable de entorno PATH
		String pathValue = System.getenv("PATH");

		String userHome = System.getProperty("user.home");
		String nodeBinPath = userHome.replace("Users" + File.separator, "") + File.separator + "nodejs";

		// Dividir el valor de PATH en las diferentes rutas
		String[] paths = pathValue.split(File.pathSeparator);

		// Ruta que queremos buscar

		// Bandera para indicar si encontramos la ruta
		boolean found = false;

		// Buscar la ruta dentro de las rutas de PATH
		for (String path : paths) {
			if (path.equals(nodeBinPath)) {
				found = true;
				System.out.println("Se encontró la ruta " + nodeBinPath + " en PATH: " + path);
				break;
			}
		}

		if (!found) {
			System.out.println("No se encontró la ruta  " + nodeBinPath + " en las rutas de PATH.");
		}
	}

}
