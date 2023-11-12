package UNIT_TEST;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Apis.ApiISBNGeneral;
import Apis.ApiMarvel;
import Controladores.VentanaAccionController;
import Funcionamiento.Comic;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBManager;
import javafx.application.Application;
import javafx.application.Platform;
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
			+ DBManager.DB_NAME + File.separator + "portadas";

	private static Scanner ent = new Scanner(System.in);

	/**
	 * Función principal que realiza varias operaciones.
	 * 
	 * @param args Argumentos de la línea de comandos.
	 * @throws IOException        Si ocurre un error de entrada/salida.
	 * @throws JSONException      Si ocurre un error al procesar JSON.
	 * @throws URISyntaxException Si ocurre un error en la URI.
	 * @throws SQLException
	 */
	public static void main(String[] args) throws IOException, JSONException, URISyntaxException, SQLException {
//		testDescargaImagen();
//		mostrarComicMarvel();
//		mostrarComicGeneral();
//		crearDatabasePrueba();
		envioDatosBasePrueba();
//		pruebaSubidaComic();
//		pruebaModificacionComic();
		
//		getComicInfo("75960609999302511");
		
		launch(args);

//		verLibroGoogle("9788411505963");
		

	}

	public void start(Stage primaryStage) {

		VentanaAccionController ventanaAccion = new VentanaAccionController();

		// Crear la lista de ComboBoxes
		List<ComboBox<String>> comboboxes = Arrays.asList(nombreComic, numeroComic, nombreVariante, nombreProcedencia,
				nombreFormato, nombreDibujante, nombreGuionista, nombreEditorial, nombreFirma, numeroCaja);

		// Pasar la lista de ComboBoxes a VentanaAccionController
		ventanaAccion.pasarComboBoxes(comboboxes);

//		Platform.runLater(() -> {
//			accionComicPruebaAni();
//		});

		Platform.runLater(() -> {
			accionComicPruebaMod();
		});
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
				return null;
			} else {
				System.out.println(apiUrl);
				JSONObject firstComic = resultsArray.getJSONObject(0);
	            // Imprime el contenido del JSON de forma legible
	            System.out.println("Contenido del JSON:");
	            System.out.println(firstComic.toString(4)); // El argumento 4 establece el factor de sangrado
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
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

		String clavesDesdeArchivo = Utilidades.obtenerClaveApiArchivo(); // Obtener las claves desde el archivo

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
	private static String sendHttpGetRequest(String apiUrl) throws IOException, URISyntaxException {
		URI url = new URI(apiUrl); // Create a URI from the API URL
		HttpURLConnection connection = (HttpURLConnection) url.toURL().openConnection();
		connection.setRequestMethod("GET");

		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder response = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null) {
			response.append(line);
		}

		reader.close();
		return response.toString();
	}

	/**
	 * Realiza una prueba de descarga de imagen desde una URL.
	 * 
	 * @throws IOException Si ocurre un error de entrada/salida.
	 */
	public static void testDescargaImagen() throws IOException {
		String URLimagen = "https://covers.openlibrary.org/b/id/12705636-L.jpg";
		Utilidades.descargarImagen(URLimagen, DOCUMENTS_PATH);
	}

	/**
	 * Muestra datos de cómics de Marvel.
	 */
	public static void mostrarComicMarvel() {
		System.err.println("Datos de comics de Marvel: ");

		String comicCode = "75960607918600111";

		String datosMarvel[] = ApiMarvel.infoComicCode(comicCode, null);
		if (datosMarvel.length == 0) {
			System.err.println("No hay datos sobre el comic");
		} else {
			for (String string : datosMarvel) {
				System.out.println(string);
			}
		}
	}

	/**
	 * Muestra datos de cómics de búsqueda genérica.
	 * 
	 * @throws IOException        Si ocurre un error de entrada/salida.
	 * @throws JSONException      Si ocurre un error al procesar JSON.
	 * @throws URISyntaxException Si ocurre un error en la URI.
	 */
	public static void mostrarComicGeneral() throws IOException, JSONException, URISyntaxException {
		System.err.println("Datos de comics de búsqueda genérica: ");

		String comicCode = "978-1684157648";

		String datosGeneral[] = ApiISBNGeneral.getBookInfo(comicCode, null);

		if (datosGeneral.length == 0) {

			System.err.println("No hay datos sobre el comic");

		} else {

			for (String dato : datosGeneral) {
				System.out.println(dato);
			}
		}
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
				portada = Utilidades.descargarImagen(portadaUrl, DOCUMENTS_PATH);
				file = new File(portada);

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

		Comic comic = new Comic("", nombre, numCaja, numero, variante, firma, editorial, formato, procedencia,
				fecha_comic.toString(), guionista, dibujante, estado, key_issue, "Sin puntuar", portada, url_referencia,
				precio_comic);

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
				portada = Utilidades.descargarImagen(portadaUrl, DOCUMENTS_PATH);
				file = new File(portada);

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

		Comic comic = new Comic(id_comic, nombre, numCaja, numero, variante, firma, editorial, formato, procedencia,
				fecha, guionista, dibujante, estado, nombreKeyIssue, puntuacion, portada, url_referencia, precio_comic);

		String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);

		utilidad.nueva_imagen(portada, codigo_imagen);
		comic.setImagen(SOURCE_PATH + File.separator + codigo_imagen + ".jpg");

		actualizarComicPrueba(comic);
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

	public static void actualizarComicPrueba(Comic datos) {
		utilidad = new Utilidades();
		String sentenciaSQL = "UPDATE comicsbbdd SET nomComic = ?, caja_deposito = ?, numComic = ?, nomVariante = ?, "
				+ "Firma = ?, nomEditorial = ?, formato = ?, Procedencia = ?, fecha_publicacion = ?, "
				+ "nomGuionista = ?, nomDibujante = ?, key_issue = ?, portada = ?, estado = ?, url_referencia = ?, precio_comic = ? "
				+ "WHERE ID = ?";

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
		conn = DBManager.conexion();
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
		String imagePath = documentsPath + File.separator + "libreria_comics" + File.separator + DBManager.DB_NAME
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

		DBManager.datosBBDD(datos);
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
		conn = DBManager.conexion();
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
			conn = DBManager.conexion();
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
				comic = new Comic(ID, nombre, numCaja, numero, variante, firma, editorial, formato, procedencia, fecha,
						guionista, dibujante, estado, key_issue, puntuacion, imagen, url_referencia, precio_comic);
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

				Utilidades.createTable();
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
			conn = DBManager.conexion();
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
			String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn ;
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

}
