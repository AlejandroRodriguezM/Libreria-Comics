package UNIT_TEST;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import Apis.ApiISBNGeneral;
import Apis.ApiMarvel;
import Controladores.VentanaAccionController;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import comicManagement.Comic;
import dbmanager.ConectManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * Clase que contiene pruebas unitarias para diferentes funcionalidades.
 */
public class Unit_Test extends Application {

	/**
	 * Conexión a la base de datos.
	 */
	private static Connection conn = null;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de la clase Utilidades para funciones generales.
	 */
	private static Utilidades utilidad = null;

	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private static final String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private static final String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	/**
	 * Selector para el nombre del cómic.
	 */
	@FXML
	private static ComboBox<String> nombreComic;

	/**
	 * Selector para el nombre del dibujante.
	 */
	@FXML
	private static ComboBox<String> nombreDibujante;

	/**
	 * Selector para el nombre de la editorial.
	 */
	@FXML
	private static ComboBox<String> nombreEditorial;

	/**
	 * Selector para el nombre de la firma.
	 */
	@FXML
	private static ComboBox<String> nombreFirma;

	/**
	 * Selector para el nombre del formato.
	 */
	@FXML
	private static ComboBox<String> nombreFormato;

	/**
	 * Selector para el nombre del guionista.
	 */
	@FXML
	private static ComboBox<String> nombreGuionista;

	/**
	 * Selector para el nombre de la procedencia.
	 */
	@FXML
	private static ComboBox<String> nombreProcedencia;

	/**
	 * Selector para el nombre de la variante.
	 */
	@FXML
	private static ComboBox<String> nombreVariante;

	/**
	 * Selector para el número de caja.
	 */
	@FXML
	private static ComboBox<String> numeroCaja;

	/**
	 * Selector para el número del cómic.
	 */
	@FXML
	private static ComboBox<String> numeroComic;

	/**
	 * Construimos la ruta al directorio "libreria_comics" dentro de "Documents" y
	 * añadimos el nombre de la base de datos y la carpeta "portadas".
	 */
	private static final String SOURCE_PATH = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
			+ ConectManager.DB_NAME + File.separator + "portadas";

	private static Scanner ent = new Scanner(System.in);

	/**
	 * Función principal que realiza varias operaciones.
	 * 
	 * @param args Argumentos de la línea de comandos.
	 * @throws IOException        Si ocurre un error de entrada/salida.
	 * @throws JSONException      Si ocurre un error al procesar JSON.
	 * @throws URISyntaxException Si ocurre un error en la URI.
	 * @throws SQLException
	 * @throws ComicInfoException
	 */
	public static void main(String[] args)
			throws IOException, JSONException, URISyntaxException, SQLException, ComicInfoException {
//		testDescargaImagen();
//		mostrarComicMarvel();
//		mostrarComicGeneral();
//		crearDatabasePrueba();
//		envioDatosBasePrueba();

//		pruebaSubidaComic();
//		pruebaModificacionComic();
		getComicInfo("9780785198260");
//		pruebaDiamondCode_imagen("JUL220767");
//		launch(args);
//		verLibroGoogle("9788411505963");
//		System.out.println(capitalizeFirstLetter("ANIMAL POUND CVR A ASHCAN GROSS"));

//		System.out.println(getComicInfoUrl("A.X.E.: Judgment Day","Peach Momoko", "1"));

//		System.out.println(displayComicInfo("https://www.marvel.com/comics/issue/55357/all-new_all-different_avengers_2015_4?utm_campaign=apiRef&utm_source=f0c866038aa05cd5bb64552a342d2726"));

//		comicModificarCodigoPrueba();
//		comicModificarCodigoPruebaCompleta();

//		nav.verMenuCodigosBarra();
		
//		nav.verEstadoConexion();

	}

	public void start(Stage primaryStage) {
//
//		VentanaAccionController ventanaAccion = new VentanaAccionController();
//
//		// Crear la lista de ComboBoxes
//		List<ComboBox<String>> comboboxes = Arrays.asList(nombreComic, numeroComic, nombreVariante, nombreProcedencia,
//				nombreFormato, nombreDibujante, nombreGuionista, nombreEditorial, nombreFirma, numeroCaja);
//
//		// Pasar la lista de ComboBoxes a VentanaAccionController
//		ventanaAccion.pasarComboBoxes(comboboxes);

//		Platform.runLater(() -> {
//			accionComicPruebaAni();
//		});

//		Platform.runLater(() -> {
//			accionComicPruebaMod();
//		});
//		
//		Platform.runLater(() -> {
//			accionComicPruebaDelete();
//		});
//		
//		Platform.runLater(() -> {
//			accionComicPruebaPuntuar();
//		});

//		Platform.runLater(() -> {
//			entrarMenuPrueba();
//		});

//		Platform.runLater(() -> {
//			entrarInicioPrueba();
//		});
	}

	public static void pruebaDiamondCode_imagen(String diamondCode) throws IOException {

		String previews_World_Url = "https://www.previewsworld.com/Catalog/" + diamondCode;

		Document document = Jsoup.connect(previews_World_Url).get();

		// Scraping de la etiqueta img con id "MainContentImage"
		scrapeAndPrintMainImageAsync(document);
	}

	/**
	 * Realiza una solicitud HTTP para obtener información de un cómic desde la API
	 * de Marvel Comics.
	 *
	 * @param claveComic El código del cómic (ISBN o UPC) que se desea buscar.
	 * @param tipoUrl    El tipo de URL a construir ("isbn" o "upc").
	 * @param prontInfo  El TextArea en el que se mostrarán los resultados o
	 *                   mensajes de error.
	 * @return Un objeto JSON con información del cómic o null si no se encuentra.
	 */
	@SuppressWarnings("unused")
	private static JSONObject getComicInfo(String claveComic) {

		System.out.println("Info final: " + claveComic);

		long timestamp = System.currentTimeMillis() / 1000;

		String claves[] = clavesApi();

		String clave_publica = claves[1].trim();

		String apiUrl = "";

		apiUrl = "https://gateway.marvel.com:443/v1/public/comics?upc=" + claveComic + "&apikey=" + clave_publica
				+ "&hash=" + newHash(timestamp) + "&ts=" + timestamp;

		// Realiza la solicitud HTTP GET
		String jsonResponse;
		try {
			jsonResponse = sendHttpGetRequest(apiUrl);

			// Parsea la respuesta JSON y obtén el cómic
			JSONObject jsonObject = new JSONObject(jsonResponse);
			JSONArray resultsArray = jsonObject.getJSONObject("data").getJSONArray("results");

			if (resultsArray.length() == 0) {
				System.err.println("No esta vez");
				return null;
			} else {
				System.out.println(apiUrl);
				JSONObject firstComic = resultsArray.getJSONObject(0);
				// Imprime el contenido del JSON de forma legible
				System.out.println("Contenido del JSON:");
				System.out.println(firstComic.toString(4)); // El argumento 4 establece el factor de sangrado
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Obtiene las claves de la API de un archivo o fuente de datos.
	 *
	 * @return Un array de cadenas con las claves pública y privada de la API.
	 */
	private static String[] clavesApi() {
		String claves[] = new String[2]; // Crear un arreglo de dos elementos para almacenar las claves

		String clavesDesdeArchivo = Utilidades.obtenerClaveApiMarvel(); // Obtener las claves desde el archivo

		if (!clavesDesdeArchivo.isEmpty()) {
			String[] partes = clavesDesdeArchivo.split(":");
			if (partes.length == 2) {
				String clavePublica = partes[0].trim();
				String clavePrivada = partes[1].trim();

				claves[0] = clavePublica; // Almacenar la clave pública en el primer elemento del arreglo
				claves[1] = clavePrivada; // Almacenar la clave privada en el segundo elemento del arreglo
			}
		}

		return claves;
	}

	/**
	 * Calcula un hash MD5 a partir de una cadena de entrada.
	 *
	 * @param input La cadena de entrada para la cual se calculará el hash.
	 * @return El hash MD5 calculado.
	 */
	private static String getHash(long timestamp) {
		String claves[] = clavesApi();

		String clavePrivada = claves[0].trim();

		String clavePublica = claves[1].trim();

		return md5(timestamp + clavePrivada + clavePublica);
	}

	/**
	 * Calcula un nuevo hash a partir de un timestamp.
	 *
	 * @param timestamp El timestamp utilizado en la generación del hash.
	 * @return El hash MD5 calculado.
	 */
	private static String newHash(long timestamp) {
		String hash = getHash(timestamp);
		return hash;
	}

	/**
	 * Calcula el hash MD5 de una cadena de entrada utilizando Apache Commons Codec.
	 *
	 * @param input La cadena de entrada para la cual se calculará el hash MD5.
	 * @return El hash MD5 calculado como una cadena de 32 caracteres hexadecimales.
	 */
	private static String md5(String input) {
		// Utiliza Apache Commons Codec para calcular el hash MD5
		return DigestUtils.md5Hex(input);
	}

	/**
	 * Realiza una solicitud HTTP GET y obtiene la respuesta como una cadena de
	 * texto.
	 *
	 * @param apiUrl La URL de la API a la que se realiza la solicitud.
	 * @return La respuesta de la solicitud HTTP como una cadena de texto.
	 * @throws IOException        Si ocurre un error de entrada/salida durante la
	 *                            solicitud.
	 * @throws URISyntaxException Si la URL de la API es inválida.
	 */
	private static String sendHttpGetRequest(String apiUrl) {
		try {
			if (!apiUrl.startsWith("http://") && !apiUrl.startsWith("https://")) {
				// Si la URL no es absoluta, devuelve "0"
				return "0";
			}

			URI url = new URI(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();

			// Verifica el código de estado
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					response.append(line);
				}

				reader.close();
				return response.toString();
			} else {
				return "0";
			}

		} catch (IOException | URISyntaxException e) {
			// Captura la excepción, imprime detalles del error y devuelve "0"
			return "0";
		}
	}

	/**
	 * Realiza una prueba de descarga de imagen desde una URL de forma asíncrona.
	 *
	 * @throws IOException Si ocurre un error de entrada/salida.
	 */
	public static void testDescargaImagenAsync() throws IOException {
		String URLimagen = "https://covers.openlibrary.org/b/id/12705636-L.jpg";

		CompletableFuture<String> descargaImagenFuture = Utilidades.descargarImagenAsync(URLimagen, DOCUMENTS_PATH);

		descargaImagenFuture.thenAccept(rutaImagen -> {
			if (rutaImagen != null) {
				System.out.println("Imagen descargada y guardada como JPG correctamente en: " + rutaImagen);
			} else {
				System.err.println("Error al descargar la imagen.");
			}
		}).join(); // Esperar a que la tarea asíncrona se complete (bloquea el hilo principal hasta
					// que se complete)
	}

	/**
	 * Test de descarga de imagen mediante scrapeWeb
	 * 
	 * @param document
	 * @return
	 * @throws IOException
	 */
	private static void scrapeAndPrintMainImageAsync(Document document) throws IOException {
		Element mainImageElement = document.selectFirst("#MainContentImage");
		if (mainImageElement != null) {
			String mainImageUrl = "https://www.previewsworld.com" + mainImageElement.attr("src");

			System.out.println(mainImageUrl);

			CompletableFuture<String> descargaImagenFuture = Utilidades.descargarImagenAsync(mainImageUrl,
					DOCUMENTS_PATH);

			descargaImagenFuture.thenAccept(rutaImagen -> {
				if (rutaImagen != null) {
					System.out.println("Imagen descargada y guardada como JPG correctamente en: " + rutaImagen);
				} else {
					System.err.println("Error al descargar la imagen.");
				}
			}).join(); // Esperar a que la tarea asíncrona se complete (bloquea el hilo principal hasta
						// que se complete)
		}
	}

	/**
	 * Muestra datos de cómics de Marvel.
	 */
	public static void mostrarComicMarvel() {
		System.err.println("Datos de comics de Marvel: ");

		String comicCode = "75960607918600111";

		Comic comic = ApiMarvel.infoComicCode(comicCode, null);

		System.out.println(comic.toString());
	}

	/**
	 * Muestra datos de cómics de búsqueda genérica.
	 * 
	 * @throws IOException        Si ocurre un error de entrada/salida.
	 * @throws JSONException      Si ocurre un error al procesar JSON.
	 * @throws URISyntaxException Si ocurre un error en la URI.
	 */
	public static void mostrarComicGeneral() throws IOException, JSONException, URISyntaxException {

		ApiISBNGeneral isbnGeneral = new ApiISBNGeneral();

		System.err.println("Datos de comics de búsqueda genérica: ");

		String comicCode = "978-1684157648";

		Comic comic = isbnGeneral.getBookInfo(comicCode, null);

		System.out.println(comic.toString());
	}

	/**
	 * Funcion que sirve para hacer pruebas manuales de subidas de comics a la base
	 * de datos
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void pruebaSubidaComic() throws IOException, SQLException {
		utilidad = new Utilidades();

		File file;
		LocalDate fecha_comic;

		String portadaUrl = "https://covers.openlibrary.org/b/id/12705636-L.jpg";

		file = new File(portadaUrl);

		String portada = "";

		String nombre = "Prueba de comic test unitario";

		String numero = "0";

		String variante = "Variante prueba";

		String firma = "Firma prueba";

		String editorial = "Editorial prueba";

		String formato = "Prueba Unitaria";

		String procedencia = "Prueba unitaria";

		String fecha = "2000-01-01";

		fecha_comic = LocalDate.parse(fecha);

		String guionista = "Guionista prueba";

		String dibujante = "Dibujante prueba";

		if (!portadaUrl.isEmpty()) {
			file = new File(portadaUrl);

			if (Utilidades.isImageURL(portadaUrl)) {
				// Es una URL en internet
				file = new File(portadaUrl);

			} else {
				if (!file.exists()) {
					portada = "Funcionamiento/sinPortada.jpg";

				} else {
					portada = portadaUrl;
				}
			}
		} else {
			portada = "Funcionamiento/sinPortada.jpg";
		}

		String estado = "Estado prueba";

		String numCaja = "0";

		String key_issue = "Key prueba";

		String url_referencia = "referencia prueba";
		String precio_comic = "0";

		String codigoComic = "0";

		Comic comic = new Comic("", nombre, numCaja, numero, variante, firma, editorial, formato, procedencia,
				fecha_comic.toString(), guionista, dibujante, estado, key_issue, "Sin puntuar", portada, url_referencia,
				precio_comic, codigoComic);

		String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);

		utilidad.nueva_imagen(portada, codigo_imagen);
		comic.setImagen(SOURCE_PATH + File.separator + codigo_imagen + ".jpg");
		insertarDatosPrueba(comic);

		System.err.println("Has introducido correctamente: \n" + comic.toString().replace("[", "").replace("]", ""));

		if (Utilidades.isURL(portadaUrl)) {
			Utilidades.borrarImagen(portada);
		}
	}

	/**
	 * Funcion que sirve para hacer pruebas manuales de modificacion de comics a la
	 * base de datos
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void pruebaModificacionComic() throws NumberFormatException, SQLException, IOException {

		for (int ids : obtenerIDsDesdeBaseDeDatos()) {
			System.out.println(ids);
		}

		Comic comic_temp = new Comic();
		File file;
//		Utilidades.convertirNombresCarpetas(SOURCE_PATH);

		System.out.print("Escribe la ID del comic a modificar: ");
		String id_comic = ent.next();

		comic_temp = comicDatos(id_comic);

		String nombre = "Modificacion prueba";

		String numero = "0";

		String variante = "Variante modificacion";

		String firma = "Firma modificacion";

		String editorial = "Editorial modificacion";

		String formato = "Formato modificacion";

		String procedencia = "Procedencia Modificacion";

		String fecha = "2000-01-01";

		String guionista = "Modificacion guionista";

		String dibujante = "Modificacion dibujante";

		String estado = "Modificacion estado";

		String numCaja = "0";

		String portadaUrl = "https://m.media-amazon.com/images/I/81RwspgAKSL._SL1500_.jpg";

		String portada = "";

		if (!portadaUrl.isEmpty()) {
			file = new File(portadaUrl);

			if (Utilidades.isImageURL(portadaUrl)) {
				// Es una URL en internet
				file = new File(portadaUrl);

			} else {
				if (!file.exists()) {
					portada = "Funcionamiento/sinPortada.jpg";

				} else {
					portada = portadaUrl;
				}
			}
		} else {
			portada = "Funcionamiento/sinPortada.jpg";
		}

		String puntuacion = "0";

		String nombreKeyIssue = "Key kmodificacion";

		String url_referencia = "Url modificacion prueba";

		String precio_comic = "0";

		System.out.println("La portada es: " + portada);

		String codigoComic = "Codigo modificado";

		Comic comic = new Comic(id_comic, nombre, numCaja, numero, variante, firma, editorial, formato, procedencia,
				fecha, guionista, dibujante, estado, nombreKeyIssue, puntuacion, portada, url_referencia, precio_comic,
				codigoComic);

		String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);

		utilidad.nueva_imagen(portada, codigo_imagen);
		comic.setImagen(SOURCE_PATH + File.separator + codigo_imagen + ".jpg");

		String sql = "		String sentenciaSQL = \"UPDATE comicsbbdd SET nomComic = ?, caja_deposito = ?, numComic = ?,codigo_comic = ?, nomVariante = ?, \"\r\n"
				+ "				+ \"Firma = ?, nomEditorial = ?, formato = ?, Procedencia = ?, fecha_publicacion = ?, \"\r\n"
				+ "				+ \"nomGuionista = ?, nomDibujante = ?, key_issue = ?, portada = ?, estado = ?, url_referencia = ?, precio_comic = ? \"\r\n"
				+ "				+ \"WHERE ID = ?\";";

		actualizarComicPrueba(comic, sql);
		Utilidades.eliminarFichero(comic_temp.getImagen());

		System.err.println("Has modificado correctamente: " + comic.toString().replace("[", "").replace("]", ""));
	}

	/**
	 * Inserta los datos de un cómic en la base de datos.
	 *
	 * @param comic_datos los datos del cómic a insertar
	 * @throws IOException  si ocurre un error al manejar el archivo de imagen
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public static void insertarDatosPrueba(Comic comic_datos) throws IOException, SQLException {
		String sentenciaSQL = "INSERT INTO comicsbbdd (nomComic, caja_deposito,precio_comic, numComic, nomVariante, firma, nomEditorial, formato, procedencia, fecha_publicacion, nomGuionista, nomDibujante, puntuacion, portada,key_issue,url_referencia, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		subirComicPrueba(sentenciaSQL, comic_datos);
	}

	public static void actualizarComicPrueba(Comic datos, String sentenciaSQL) {
		utilidad = new Utilidades();

		try {
			if (checkID(datos.getID())) { // Comprueba si la ID introducida existe en la base de datos
				comicModificarPrueba(sentenciaSQL, datos); // Llama a la función que permite cambiar los datos del
															// cómic
			}
		} catch (SQLException | IOException ex) {
			nav.alertaException(ex.toString()); // Manejar excepciones y mostrar mensaje de alerta
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Comprueba si el identificador introducido existe en la base de datos.
	 *
	 * @param identificador El identificador a verificar.
	 * @return true si el identificador existe en la base de datos, false si no
	 *         existe.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static boolean checkID(String identificador) throws SQLException {

		if (identificador.length() == 0) {
			String excepcion = "No puedes realizar la accion sin antes no poner un ID valido";
			nav.alertaException(excepcion);
			return false; // Si el identificador está vacío, se considera que no existe
		}

		String sentenciaSQL = "SELECT * FROM comicsbbdd WHERE ID = ?";
		conn = ConectManager.conexion();
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		boolean existe = false; // Variable para almacenar si el identificador existe en la base de datos

		try {
			preparedStatement = conn.prepareStatement(sentenciaSQL);
			preparedStatement.setString(1, identificador);
			rs = preparedStatement.executeQuery();

			existe = rs.next(); // Actualizar la variable 'existe' basándose en si hay resultados en la consulta

		} catch (SQLException e) {
			nav.alertaException("No se pudo verificar la existencia de " + identificador + " en la base de datos.");
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (rs != null) {
				rs.close();
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return existe; // Devolver si el identificador existe en la base de datos o no
	}

	/**
	 * Modifica la dirección de la portada de un cómic en la base de datos.
	 *
	 * @param nombre_completo el nombre completo de la imagen de la portada
	 * @param ID              el ID del cómic a modificar
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public static void modificar_direccion_portada(String nombre_completo, String ID) throws SQLException {
		utilidad = new Utilidades();
		String extension = ".jpg";
		String nuevoNombreArchivo = nombre_completo + extension;

		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String imagePath = documentsPath + File.separator + "libreria_comics" + File.separator + ConectManager.DB_NAME
				+ File.separator + "portadas" + File.separator + nuevoNombreArchivo;

		String sql = "UPDATE comicsbbdd SET portada = ? WHERE ID = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, imagePath);
			ps.setString(2, ID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void envioDatosBasePrueba() {
		String datos[] = new String[5];
		datos[0] = "3306";
		datos[1] = "comicsprueba";
		datos[2] = "root";
		datos[3] = "1234";
		datos[4] = "localhost";

		ConectManager.datosBBDD(datos);
	}

	/**
	 * Funcion que permita la subida de datos a la base de datos de prueba para
	 * poder realizar pruebas unitarias
	 * 
	 * @param sentenciaSQL
	 * @param datos
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void subirComicPrueba(String sentenciaSQL, Comic datos) throws IOException, SQLException {
		conn = ConectManager.conexion();
		PreparedStatement statement = null;

		try {
			statement = conn.prepareStatement(sentenciaSQL);
			statement.setString(1, datos.getNombre());
			if (datos.getNumCaja() == null) {
				statement.setString(2, "0");
			} else {
				statement.setString(2, datos.getNumCaja());
			}
			statement.setString(3, datos.getPrecio_comic());
			statement.setString(4, datos.getNumero());
			statement.setString(5, datos.getVariante());
			statement.setString(6, datos.getFirma());
			statement.setString(7, datos.getEditorial());
			statement.setString(8, datos.getFormato());
			statement.setString(9, datos.getProcedencia());
			statement.setString(10, datos.getFecha());
			statement.setString(11, datos.getGuionista());
			statement.setString(12, datos.getDibujante());
			statement.setString(13, "Sin puntuar");
			statement.setString(14, datos.getImagen());
			statement.setString(15, datos.getKey_issue());
			statement.setString(16, datos.getUrl_referencia());
			statement.setString(17, datos.getEstado());

			statement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Funcion que permite la modificacion de un comic para la<zs pruebas unitarias
	 * 
	 * @param sentenciaSQL
	 * @param datos
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void comicModificarPrueba(String sentenciaSQL, Comic datos) throws SQLException, IOException {
		utilidad = new Utilidades();

		String ID = datos.getID();
		String nombre = datos.getNombre();
		String numCaja = datos.getNumCaja();
		String numero = datos.getNumero();
		String variante = datos.getVariante();
		String firma = datos.getFirma();
		String editorial = datos.getEditorial();
		String formato = datos.getFormato();
		String procedencia = datos.getProcedencia();
		String fecha = datos.getFecha();
		String guionista = datos.getGuionista();
		String dibujante = datos.getDibujante();
		String estado = datos.getEstado();
		String portada_final = datos.getImagen();
		String key_issue = datos.getKey_issue();
		String url_referencia = datos.getUrl_referencia();
		String precio_comic = datos.getPrecio_comic();
		String codigo_imagen = Utilidades.generarCodigoUnico(portada_final + File.separator);

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sentenciaSQL);

			ps.setString(1, nombre);
			ps.setString(2, numCaja);
			ps.setString(3, numero);
			ps.setString(4, variante);
			ps.setString(5, firma);
			ps.setString(6, editorial);
			ps.setString(7, formato);
			ps.setString(8, procedencia);
			ps.setString(9, fecha);
			ps.setString(10, guionista);
			ps.setString(11, dibujante);
			ps.setString(12, key_issue);
			ps.setString(13, portada_final);
			ps.setString(14, estado);
			ps.setString(15, url_referencia);
			ps.setString(16, precio_comic);
			ps.setString(17, ID);

			if (ps.executeUpdate() == 1) {
				System.out.println(datos.toString() + "\nComic modificado correctamente");
				modificar_direccion_portada(codigo_imagen, ID);

			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
			ex.printStackTrace();
		} finally {
			if (ps != null) {
				ps.close();
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Funcion que permite la modificacion de un comic para la<zs pruebas unitarias
	 * 
	 * @param sentenciaSQL
	 * @param datos
	 * @throws SQLException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ComicInfoException
	 */
	public static void comicModificarCodigoPrueba()
			throws SQLException, IOException, URISyntaxException, ComicInfoException {

		// Obtener la lista de IDs desde la base de datos

		String sentenciaSQL = "UPDATE comicsprueba.comicsbbdd SET codigo_comic = ?, url_referencia = ? WHERE ID = ?;";

		PreparedStatement ps = null;
		Connection connection = null;
		String databaseName = "comicsprueba";
		String username = "root";
		String password = "1234";
		String host = "localhost";

		String url = "jdbc:mysql://" + host + ":" + "3306" + "/" + databaseName + "?serverTimezone=UTC";

		try {
			connection = DriverManager.getConnection(url, username, password);

			List<String> ids = obtenerListaDeIDsDesdeBD(connection); // Implementa esta función según tu lógica

//			Statement statement = connection.createStatement();
			ps = connection.prepareStatement(sentenciaSQL);

			for (String ID : ids) {
				Comic comic_temp = comicDatos(ID);
				String referencia_comic = comic_temp.getUrl_referencia();
				String codigo_comic = displayComicInfo(referencia_comic);

				ps.setString(1, codigo_comic);
				ps.setString(2, referencia_comic);
				ps.setString(3, ID);

				if (ps.executeUpdate() == 1) {
					System.out.println(comic_temp.getNombre() + ": " + comic_temp.getVariante() + " New code: "
							+ comic_temp.getCodigo_comic());
				}
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
			ex.printStackTrace();
		} finally {
			if (ps != null) {
				ps.close();
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Funcion que permite la modificacion de un comic para la<zs pruebas unitarias
	 * 
	 * @param sentenciaSQL
	 * @param datos
	 * @throws SQLException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ComicInfoException
	 */
	public static void comicModificarCodigoPruebaCompleta()
			throws SQLException, IOException, URISyntaxException, ComicInfoException {

		// Obtener la lista de IDs desde la base de datos

		String sentenciaSQL = "UPDATE comics.comicsbbdd SET codigo_comic = ? WHERE ID = ?;";

		PreparedStatement ps = null;
		Connection connection = null;
		String databaseName = "comics";
		String username = "root";
		String password = "1234";
		String host = "localhost";
		String codigoComic = "";

		String url = "jdbc:mysql://" + host + ":" + "3306" + "/" + databaseName + "?serverTimezone=UTC";

		try {
			connection = DriverManager.getConnection(url, username, password);

			List<String> ids = obtenerListaDeIDsDesdeBD(connection); // Implementa esta función según tu lógica

//			Statement statement = connection.createStatement();
			ps = connection.prepareStatement(sentenciaSQL);

			for (String ID : ids) {
				Comic comic_temp = comicDatos(ID);

				if (comic_temp.getEditorial().equalsIgnoreCase("Marvel")) {
					String titulo = comic_temp.getNombre();
					String artistaVariante = comic_temp.getVariante();
					String numComic = comic_temp.getNumero();
					String anioComic = obtenerAnio(comic_temp.getFecha());
					codigoComic = getComicInfoUrl(titulo, artistaVariante, numComic, anioComic);
					System.out.println(codigoComic);
				}

				if (codigoComic == null || codigoComic.isEmpty() || codigoComic.equals("0")) {
					String referencia_comic = comic_temp.getUrl_referencia();
					codigoComic = displayComicInfo(referencia_comic);
				}

				ps.setString(1, codigoComic);
				ps.setString(2, ID);

				if (ps.executeUpdate() == 1) {
					System.out.println("ID: " + comic_temp.getID() + " - " + comic_temp.getNombre() + ": "
							+ comic_temp.getVariante() + " - New code: " + codigoComic);
				}
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
			ex.printStackTrace();
		} finally {
			if (ps != null) {
				ps.close();
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static String obtenerAnio(String fecha) {
		try {
			// Formato de la cadena de fecha
			SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");

			// Parsea la cadena de fecha a un objeto Date
			Date fechaDate = formatoEntrada.parse(fecha);

			// Formato de salida para extraer el año
			SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy");

			// Convierte el Date al formato de salida (solo el año)
			return formatoSalida.format(fechaDate);
		} catch (ParseException e) {
			e.printStackTrace();
			// Manejar la excepción según tus necesidades
			return null;
		}
	}

	private static List<String> obtenerListaDeIDsDesdeBD(Connection connect) throws SQLException {
		List<String> ids = new ArrayList<>();

		// Realiza la consulta SQL para obtener la lista de IDs desde la base de datos
		String consultaIDs = "SELECT ID FROM comicsbbdd;"; // Reemplaza "tu_tabla" con el nombre real de tu tabla
		try (Statement statement = connect.createStatement();
				ResultSet resultSet = statement.executeQuery(consultaIDs)) {

			while (resultSet.next()) {
				ids.add(resultSet.getString("ID"));
			}
		}

		return ids;
	}

	/**
	 * Devuelve un objeto Comic cuya ID coincida con el parámetro de búsqueda.
	 *
	 * @param identificador el ID del cómic a buscar
	 * @return el objeto Comic encontrado, o null si no se encontró ningún cómic con
	 *         ese ID
	 * @throws SQLException si ocurre algún error al ejecutar la consulta SQL
	 */
	public static Comic comicDatos(String identificador) throws SQLException {
		Comic comic = null;

		String sentenciaSQL = "SELECT * FROM comicsbbdd WHERE ID = ?";

		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			conn = ConectManager.conexion();
			statement = conn.prepareStatement(sentenciaSQL);
			statement.setString(1, identificador);
			rs = statement.executeQuery();

			if (rs.next()) {
				String ID = rs.getString("ID");
				String nombre = rs.getString("nomComic");
				String numCaja = rs.getString("caja_deposito");
				String numero = rs.getString("numComic");
				String variante = rs.getString("nomVariante");
				String firma = rs.getString("firma");
				String editorial = rs.getString("nomEditorial");
				String formato = rs.getString("formato");
				String procedencia = rs.getString("procedencia");
				String fecha = rs.getString("fecha_publicacion");
				String guionista = rs.getString("nomGuionista");
				String dibujante = rs.getString("nomDibujante");
				String estado = rs.getString("estado");
				String key_issue = rs.getString("key_issue");
				String puntuacion = rs.getString("puntuacion");
				String imagen = rs.getString("portada");
				String url_referencia = rs.getString("url_referencia");
				String precio_comic = rs.getString("precio_comic");
				String codigo_comic = rs.getString("codigo_comic");

				comic = new Comic(ID, nombre, numCaja, numero, variante, firma, editorial, formato, procedencia, fecha,
						guionista, dibujante, estado, key_issue, puntuacion, imagen, url_referencia, precio_comic,
						codigo_comic);
			}
		} catch (SQLException e) {
			nav.alertaException(e.toString());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				statement.close();
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return comic;
	}

	/**
	 * Funcion que permite crear una base de datos de prueba para poder hacer test
	 * unitarios
	 * 
	 * @throws IOException
	 */
	public static void crearDatabasePrueba() throws IOException {
		String databaseName = "comicsprueba";
		String url = "jdbc:mysql://localhost:3306/?serverTimezone=UTC";
		String username = "root";
		String password = "1234";

		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Statement statement = connection.createStatement();

			// Verifica si la base de datos ya existe
			if (!existeBaseDeDatos(statement, databaseName)) {
				// Si no existe, crea la base de datos
				String createDatabaseSQL = "CREATE DATABASE " + databaseName;
				statement.executeUpdate(createDatabaseSQL);

				envioDatosBasePrueba();

				createTable();
				Utilidades.crearCarpeta();

				System.out.println("Base de datos " + databaseName + " creada con éxito.");
			} else {
				System.out.println("La base de datos " + databaseName + " ya existe.");
			}

			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// Asegúrate de cerrar la conexión una vez que hayas terminado.
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void createTable() {
		Statement statement;
		PreparedStatement preparedStatement;

		String databaseName = "comicsprueba";
		String username = "root";
		String password = "1234";
		String host = "localhost";

		String url = "jdbc:mysql://" + host + ":" + "3306" + "/" + databaseName + "?serverTimezone=UTC";

		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			statement = connection.createStatement();

			String dropTableSQL = "DROP TABLE IF EXISTS comicsbbdd";
			String createTableSQL = "CREATE TABLE comicsbbdd (" + "ID INT NOT NULL AUTO_INCREMENT, "
					+ "nomComic VARCHAR(150) NOT NULL, " + "caja_deposito TEXT, " + "precio_comic DOUBLE NOT NULL, "
					+ "codigo_comic VARCHAR(150), " + "numComic INT NOT NULL, " + "nomVariante VARCHAR(150) NOT NULL, "
					+ "firma VARCHAR(150) NOT NULL, " + "nomEditorial VARCHAR(150) NOT NULL, "
					+ "formato VARCHAR(150) NOT NULL, " + "procedencia VARCHAR(150) NOT NULL, "
					+ "fecha_publicacion DATE NOT NULL, " + "nomGuionista TEXT NOT NULL, "
					+ "nomDibujante TEXT NOT NULL, " + "puntuacion VARCHAR(300) NOT NULL, " + "portada TEXT, "
					+ "key_issue TEXT, " + "url_referencia TEXT NOT NULL, " + "estado TEXT NOT NULL, "
					+ "PRIMARY KEY (ID)) " + "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
			statement.executeUpdate(dropTableSQL);
			statement.executeUpdate(createTableSQL);

			preparedStatement = connection.prepareStatement("alter table comicsbbdd AUTO_INCREMENT = 1;");
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			nav.alertaException(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Funcion que prueba si la base de datos de prueba existe
	 * 
	 * @param statement
	 * @param databaseName
	 * @return
	 * @throws SQLException
	 */
	private static boolean existeBaseDeDatos(Statement statement, String databaseName) throws SQLException {
		String checkDatabaseSQL = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = '"
				+ databaseName + "'";
		return statement.executeQuery(checkDatabaseSQL).next();
	}

	/**
	 * Funcion que me devuelve los ID de la base de datos en caso de existir algun
	 * ID
	 * 
	 * @return
	 */
	public static List<Integer> obtenerIDsDesdeBaseDeDatos() {
		List<Integer> ids = new ArrayList<>();

		// Definir la consulta SQL
		String consultaSQL = "SELECT ID FROM comicsbbdd"; // Completa esta consulta con tu tabla y condiciones
															// necesarias
		try {
			conn = ConectManager.conexion();
			PreparedStatement stmt = conn.prepareStatement(consultaSQL);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				// Agregar el ID encontrado a la lista
				ids.add(rs.getInt("ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Manejo de errores, puedes personalizarlo según tus necesidades
		} finally {
			// Asegúrate de cerrar la conexión una vez que hayas terminado.
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return ids;
	}

	public static void accionComicPruebaAni() {

		VentanaAccionController.tipoAccion("aniadir");

		nav.verAccionComic();
	}

	public static void accionComicPruebaMod() {

		VentanaAccionController.tipoAccion("modificar");

		nav.verAccionComic();
	}

	public static void accionComicPruebaDelete() {

		VentanaAccionController.tipoAccion("eliminar");

		nav.verAccionComic();
	}

	public static void accionComicPruebaPuntuar() {

		VentanaAccionController.tipoAccion("puntuar");

		nav.verAccionComic();
	}

	public static void entrarMenuPrueba() {

		nav.verMenuPrincipal(); // Llamada a metodo de la clase NavegacionVentanas. Permite cargar y mostrar el

	}

	public static void entrarInicioPrueba() {
		nav.verAccesoBBDD();
	}

	public static String getBookInfoByISBN(String isbn) throws URISyntaxException {
		try {
			String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
			URI uri = new URI(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

			if (connection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					response.append(line);
				}

				reader.close();
				connection.disconnect();

				return response.toString();
			} else {
				System.out.println("Error al buscar el libro. Código de estado: " + connection.getResponseCode());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void verLibroGoogle(String isbn) throws URISyntaxException, JSONException {
		String bookInfo = getBookInfoByISBN(isbn);

		if (bookInfo != null) {
			// Formatear el JSON de respuesta de forma nativa
			JSONObject json = new JSONObject(bookInfo);
			String formattedJson = json.toString(2); // 2 espacios de sangrado
			System.out.println(formattedJson);
		}
	}

	@SuppressWarnings("unused")
	private static String capitalizeFirstLetter(String input) {

		input = input.toLowerCase();

		StringBuilder result = new StringBuilder();

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

	private static String getComicInfoUrl(String comicCode, String artistaVariante, String numComic, String anioFecha) {
		long timestamp = System.currentTimeMillis() / 1000;
		String claves[] = clavesApi();
		String clave_publica = claves[1].trim();
		String apiUrl = "";
		String apiIDArtista = "";

		if (!comicCode.isEmpty() && !artistaVariante.isEmpty() && !numComic.isEmpty()) {

			comicCode = encodeURL(comicCode);

			artistaVariante = encodeURL(artistaVariante);

			apiIDArtista = "https://gateway.marvel.com:443/v1/public/creators?nameStartsWith=" + artistaVariante
					+ "&apikey=" + clave_publica + "&hash=" + newHash(timestamp) + "&ts=" + timestamp;

			apiUrl = "https://gateway.marvel.com:443/v1/public/comics?titleStartsWith=" + comicCode + "&creators="
					+ encodeURL(idArtista(apiIDArtista)) + "&issueNumber=" + numComic + "&startYear=" + anioFecha
					+ "&apikey=" + clave_publica + "&hash=" + newHash(timestamp) + "&ts=" + timestamp;

		}
		return processComicInfo(apiUrl);
	}

	private static String processComicInfo(String apiUrl) {
		try {
			String jsonResponse = sendHttpGetRequest(apiUrl);

			// Verifica si la respuesta es un objeto JSON válido
			if (isJSONValid(jsonResponse)) {
				// Parsea la respuesta JSON y obtén la información del cómic
				JSONObject jsonObject = new JSONObject(jsonResponse);
				JSONArray resultsArray = jsonObject.getJSONObject("data").getJSONArray("results");

				// Verifica si hay al menos un resultado
				if (resultsArray.length() > 0) {
					JSONObject resultObject = resultsArray.getJSONObject(0);

					// Imprime información detallada del cómic y devuelve la cadena resultante
					return getIsbnOrUpc(resultObject);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "0";
	}

	// Función auxiliar para verificar si una cadena es un JSON válido
	private static boolean isJSONValid(String json) {
		try {
			new JSONObject(json);
			return true;
		} catch (JSONException ex) {
			return false;
		}
	}

	private static String idArtista(String apiIDArtista) {
		try {
			String jsonResponse = sendHttpGetRequest(apiIDArtista);

			// Verifica si la respuesta es válida antes de intentar crear el JSONObject
			if (isValidJSON(jsonResponse)) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				JSONArray resultsArray = jsonObject.getJSONObject("data").getJSONArray("results");

				// Verifica si hay al menos un resultado y obtén el ID del primer resultado
				if (resultsArray.length() > 0) {
					JSONObject firstResult = resultsArray.getJSONObject(0);
					int artistId = firstResult.getInt("id");

					// Devuelve el ID del artista como una cadena
					return String.valueOf(artistId);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Si no se encuentra el artista o hay un error, devuelve null o algún valor
		// predeterminado según tus necesidades
		return "0";
	}

	private static boolean isValidJSON(String json) {
		try {
			new JSONObject(json);
			return true;
		} catch (JSONException ex) {
			return false;
		}
	}

	private static String getIsbnOrUpc(JSONObject comicObject) throws JSONException {
		String isbn = comicObject.optString("isbn", "");
		String upc = comicObject.optString("upc", "");

		if (!isbn.isEmpty()) {
			return isbn;
		} else if (!upc.isEmpty()) {
			return upc;
		} else {
			// Ambos están vacíos o no existen
			return "0";
		}
	}

	/**
	 * Funcion que segun el string que se le de, le hace un encode para que pueda
	 * ser usado en una API
	 * 
	 * @param input
	 * @return
	 */
	private static String encodeURL(String input) {
		try {
			// Verifica si la cadena de entrada es nula
			if (input != null) {
				// Reemplaza espacios con %20 y codifica otros caracteres especiales
				return URLEncoder.encode(input, "UTF-8").replaceAll("\\+", "%20").replaceAll("%21", "!")
						.replaceAll("%27", "'").replaceAll("%28", "(").replaceAll("%29", ")").replaceAll("%7E", "~")
						.replaceAll(":", "%3A").replaceAll("\\.", "%2E");
			} else {
				// Devuelve null o algún valor predeterminado según tus necesidades
				System.err.println("La cadena de entrada es nula.");
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int verificarCodigoRespuesta(String urlString) {
		try {
//			// Verificar que la URL comienza con "http" o "https"
//			if (!urlString.startsWith("http://") && !urlString.startsWith("https://") && !urlString.startsWith("www")) {
//				// Puedes manejar el error de tu preferencia aquí
//				return -1;
//			}

			URI uri = new URI(urlString);
			HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.setRequestMethod("GET");

			// Obtener el código de respuesta HTTP
			int codigoRespuesta = connection.getResponseCode();

			// Cerrar la conexión
			connection.disconnect();

			return codigoRespuesta;
		} catch (IOException | URISyntaxException e) {
			// Manejar la excepción de manera adecuada (puedes imprimir un mensaje,
			// registrar el error, etc.)
			e.printStackTrace(); // Imprime el rastreo de la pila para propósitos de depuración
			return -1; // Puedes devolver otro valor si lo prefieres
		}
	}

	/**
	 * Realiza scraping en una URL dada para extraer información sobre un cómic.
	 * 
	 * @param url La URL de la página web del cómic.
	 * @return Un array de cadenas que contiene la información del cómic, o un array
	 *         vacío si ocurre un error.
	 * @throws URISyntaxException
	 */
	public static String displayComicInfo(String urlMarvel) throws ComicInfoException {
		try {

			// Verificar si la URL contiene la cadena específica y devolver -1
			if (!urlMarvel.contains("https://www.keycollectorcomics.com") && !urlMarvel.contains("Sin referencia")) {
				int codigoRespuesta = verificarCodigoRespuesta(urlMarvel);
				if (codigoRespuesta != 404 || codigoRespuesta != -1) {
					Document document = Jsoup.connect(urlMarvel).get();

					String codeComic = scrapeCodigoMarvel(document);
					String codeComicOpen = scrapeCodigoOpenLibrary(document);
					String codeComicPreview = scrapeCodigoPreviews(urlMarvel);

					if (codeComic != null && !codeComic.isEmpty()) {
						return codeComic;
					}

					if (codeComicOpen != null && !codeComicOpen.isEmpty()) {
						return codeComicOpen;
					}

					if (codeComicPreview != null && !codeComicPreview.isEmpty()) {
						return codeComicPreview;
					}
					return "0";
				}
			} else {
				return "0";
			}

		} catch (IOException e) {
			throw new ComicInfoException("Error al obtener información del cómic: " + e.getMessage(), e);
		}
		return "0";
	}

	// Definir una excepción personalizada para manejar errores específicos
	@SuppressWarnings("serial")
	public static class ComicInfoException extends Exception {
		public ComicInfoException(String message) {
			super(message);
		}

		public ComicInfoException(String message, Throwable cause) {
			super(message, cause);
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
	private static String scrapeCodigoMarvel(Document document) {
		Element detailWrapElement = document.selectFirst("div.detail-wrap");

		if (detailWrapElement != null) {
			// Buscar el li con strong igual a "ISBN:"
			Element isbnElement = detailWrapElement.selectFirst("li:has(strong:containsOwn(ISBN:))");
			if (isbnElement != null) {
				String isbnContent = isbnElement.text().replace("ISBN:", "").trim();
				// Puedes realizar cualquier otra manipulación necesaria con el contenido de
				// ISBN
				return isbnContent;
			}

			// Si no se encontró el ISBN, buscar el li con strong igual a "UPC:"
			Element upcElement = detailWrapElement.selectFirst("li:has(strong:containsOwn(UPC:))");
			if (upcElement != null) {
				String upcContent = upcElement.text().replace("UPC:", "").trim();
				// Puedes realizar cualquier otra manipulación necesaria con el contenido de UPC
				return upcContent;
			}
		}

		// Si no se encontró ni ISBN ni UPC, devolver null o un valor por defecto según
		// tu lógica
		return "";
	}

	private static String scrapeCodigoOpenLibrary(Document document) {
		// Buscar directamente la etiqueta dd con la clase "object" y itemprop="isbn"
		Element isbnDdElement = document.selectFirst("dd.object[itemprop=isbn]");

		if (isbnDdElement != null) {
			// Obtener el contenido de la etiqueta dd
			String isbnContent = isbnDdElement.text().trim();
			// Puedes realizar cualquier otra manipulación necesaria con el contenido de
			// ISBN
			return isbnContent;
		}

		// Si no se encontró la información deseada, devolver null o un valor por
		// defecto según tu lógica
		return "";
	}

	private static String scrapeCodigoPreviews(String enlace) {
		// Define el patrón de la expresión regular
		Pattern patron = Pattern.compile("/([A-Z]+[0-9]+)/?$");

		// Crea un objeto Matcher con el enlace de entrada
		Matcher matcher = patron.matcher(enlace);

		// Busca la coincidencia y extrae la parte final
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			// Si no se encuentra ninguna coincidencia, puedes manejarlo según tus
			// necesidades.
			// En este ejemplo, simplemente se devuelve una cadena vacía.
			return "";
		}
	}

}
