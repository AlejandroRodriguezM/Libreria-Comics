package comicManagement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Controladores.VentanaAccionController;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;

/**
 * Esta clase se encargará de manipular los datos relacionados con los cómics,
 * como la creación de instancias de la clase Comic, la actualización de datos y
 * la gestión de la base de datos
 */
public class ComicDataHandler {

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de la clase DBLibreriaManager para gestionar la base de datos.
	 */
	private static DBLibreriaManager libreria = null;

	/**
	 * Instancia de la clase Utilidades para funciones generales.
	 */
	private static Utilidades utilidad = null;
	
	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private final String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private final String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	/**
	 * Construimos la ruta al directorio "libreria_comics" dentro de "Documents" y
	 * añadimos el nombre de la base de datos y la carpeta "portadas".
	 */
	private final String SOURCE_PATH = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
			+ DBManager.DB_NAME + File.separator + "portadas";

	/**
	 * Permite introducir un comic en la base de datos de forma manual
	 * 
	 * @throws IOException
	 * @throws SQLException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws URISyntaxException
	 */
	public void subidaComic()
			throws IOException, SQLException, InterruptedException, ExecutionException, URISyntaxException {
		libreria = new DBLibreriaManager();
		utilidad = new Utilidades();

		File file;
		LocalDate fecha_comic;
		Image imagen = null;

		Utilidades.convertirNombresCarpetas(SOURCE_PATH);
		detenerAnimacionPront();
		iniciarAnimacionCambioImagen();

		String datos[] = camposComic();

		file = new File(datos[10]);

		String portada = "";

		String nombre = datos[0];

		String numero = datos[1];

		String variante = datos[2];

		String firma = datos[3];

		String editorial = datos[4];

		String formato = datos[5];

		String procedencia = datos[6];

		if (datos[7] == null) {
			datos[7] = "2000-01-01";
			fecha_comic = LocalDate.parse(datos[7]);
		} else {
			fecha_comic = LocalDate.parse(datos[7]);
		}

		String guionista = datos[8];

		String dibujante = datos[9];

		if (!datos[10].isEmpty()) {
			file = new File(datos[10]);
			if (Utilidades.isImageURL(datos[10])) {
				// Es una URL en internet
				portada = Utilidades.descargarImagen(datos[10], DOCUMENTS_PATH);
				file = new File(portada);
				imagen = new Image(file.toURI().toString(), 250, 0, true, true);

			} else if (!file.exists()) {
				portada = "Funcionamiento/sinPortada.jpg";
				imagen = new Image(portada, 250, 0, true, true);

			} else {
				portada = datos[10];
				imagen = new Image(file.toURI().toString(), 250, 0, true, true);
			}

		} else {
			portada = "Funcionamiento/sinPortada.jpg";
			imagen = new Image(portada, 250, 0, true, true);
		}

		imagencomic.setImage(imagen);

		String estado = datos[11];

		String numCaja = datos[12];

		if (numCaja.isEmpty()) {
			numCaja = "0";
		}

		String key_issue = "Vacio";
		String key_issue_sinEspacios = datos[13].trim();

		Pattern pattern = Pattern.compile(".*\\w+.*");
		Matcher matcher = pattern.matcher(key_issue_sinEspacios);

		if (!key_issue_sinEspacios.isEmpty() && matcher.matches()) {
			key_issue = key_issue_sinEspacios;
		}

		String url_referencia = datos[14];
		String precio_comic = datos[15];

		if (precio_comic.isEmpty()) {
			precio_comic = "0";
		}

		double valor_comic = Double.parseDouble(precio_comic);

		precio_comic = String.valueOf(Utilidades.convertirMonedaADolar(procedencia, valor_comic));

		if (url_referencia.isEmpty()) {
			url_referencia = "Sin referencia";
		}

		String codigo_comic = datos[16];

		Comic comic = new Comic("", nombre, numCaja, numero, variante, firma, editorial, formato, procedencia,
				fecha_comic.toString(), guionista, dibujante, estado, key_issue, "Sin puntuar", portada, url_referencia,
				precio_comic, codigo_comic);

		if (nombre.isEmpty() || numero.isEmpty() || editorial.isEmpty() || guionista.isEmpty() || dibujante.isEmpty()) {
			String excepcion = "No puedes introducir un comic si no has completado todos los datos";

			validateComicFields(comic);

			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos.");

			nav.alertaException(excepcion);
		} else {

			String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);

			utilidad.nueva_imagen(portada, codigo_imagen);
			comic.setImagen(SOURCE_PATH + File.separator + codigo_imagen + ".jpg");
			libreria.insertarDatos(comic);

			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #A0F52D");
			prontInfo.setText("Comic introducido correctamente");
			libreria.listasAutoCompletado();

			if (Utilidades.isURL(datos[10])) {
				Utilidades.borrarImagen(portada);
			}

			Image imagenDeseo = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
			imagenFondo.setImage(imagenDeseo);
			List<ComboBox<String>> comboboxes = VentanaAccionController.getComboBoxes();

			funcionesCombo.rellenarComboBox(comboboxes);

		}
		detenerAnimacionPront();
	}

	/**
	 * Funcion que permite modificar un comic, segun los datos introducidos
	 * 
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws URISyntaxException
	 */
	public void modificacionComic() throws NumberFormatException, SQLException, IOException, InterruptedException,
			ExecutionException, URISyntaxException {
		libreria = new DBLibreriaManager();
		Comic comic_temp = new Comic();

		Image imagen = null;

		File file;

		Utilidades.convertirNombresCarpetas(SOURCE_PATH);

		String datos[] = camposComic();

		String id_comic = idComicTratar_mod.getText();

		comic_temp = libreria.comicDatos(id_comic);

		String nombre = "";

		String numero = "";

		String variante = "";

		String firma = "";

		String editorial = "";

		String formato = "";

		String procedencia = "";

		String fecha = "";

		String guionista = datos[8];

		String dibujante = datos[9];

		String estado = datos[11];

		String numCaja = "";

		String portada = "";

		String puntuacion = "";

		String nombreKeyIssue = "";

		String url_referencia = "";

		String precio_comic = "";

		String codigo_comic = "";

		if (datos[0].isEmpty()) {
			nombre = comic_temp.getNombre();
		} else {
			nombre = datos[0];
		}

		if (datos[1].isEmpty()) {
			numero = comic_temp.getNumero();
		} else {
			numero = datos[1];
		}

		if (datos[2].isEmpty()) {
			variante = comic_temp.getVariante();
		} else {
			variante = datos[2];
		}

		if (datos[3].isEmpty()) {
			firma = comic_temp.getFirma();
		} else {
			firma = datos[3];
		}

		if (datos[4].isEmpty()) {
			editorial = comic_temp.getEditorial();
		} else {
			editorial = datos[4];
		}

		if (datos[5].isEmpty()) {
			formato = comic_temp.getFormato();
		} else {
			formato = datos[5];
		}

		if (datos[6].isEmpty()) {
			procedencia = comic_temp.getProcedencia();
		} else {
			procedencia = datos[6];
		}

		if (datos[7].isEmpty()) {
			fecha = comic_temp.getFecha();
		} else {
			fecha = datos[7];
		}

		if (datos[8].isEmpty()) {
			guionista = comic_temp.getGuionista();
		} else {
			guionista = datos[8];
		}

		if (datos[9].isEmpty()) {
			dibujante = comic_temp.getDibujante();
		} else {
			dibujante = datos[9];
		}

		if (!datos[10].isEmpty()) {
			file = new File(datos[10]);

			if (Utilidades.isImageURL(datos[10])) {
				// Es una URL en internet
				portada = Utilidades.descargarImagen(datos[10], DOCUMENTS_PATH);
				file = new File(portada);
//				imagen = new Image(file.toURI().toString(), 250, 0, true, true);

			} else if (file.exists()) {
				portada = file.toString();
//				imagen = new Image(portada, 250, 0, true, true);
			} else {
				portada = "Funcionamiento/sinPortada.jpg";
//				imagen = new Image(file.toURI().toString(), 250, 0, true, true);
			}
		} else {
			file = new File(comic_temp.getImagen());
			portada = comic_temp.getImagen();
//			imagen = new Image(portada, 250, 0, true, true);
		}

		if (datos[11].isEmpty()) {
			estado = comic_temp.getEstado();
		} else {
			estado = datos[11];
		}

		if (datos[12].isEmpty()) {
			numCaja = comic_temp.getNumCaja();

		} else {
			numCaja = datos[12];
		}

		if (!comic_temp.getPuntuacion().equals("Sin puntuar")) {
			puntuacion = comic_temp.getPuntuacion();
		} else {
			puntuacion = "Sin puntuar";
		}

		nombreKeyIssue = "Vacio";
		String key_issue_sinEspacios = datos[13].trim();

		Pattern pattern = Pattern.compile(".*\\w+.*");
		Matcher matcher = pattern.matcher(key_issue_sinEspacios);

		if (!key_issue_sinEspacios.isEmpty() && matcher.matches()) {
			nombreKeyIssue = key_issue_sinEspacios;
		}

		if (datos[14].isEmpty()) {
			url_referencia = comic_temp.getUrl_referencia();
		} else {
			url_referencia = datos[14];
		}

		if (datos[15].isEmpty()) {
			precio_comic = comic_temp.getPrecio_comic();
		} else {
			precio_comic = datos[15];
		}

		double valor_comic = Double.parseDouble(precio_comic);

		precio_comic = String.valueOf(Utilidades.convertirMonedaADolar(procedencia, valor_comic));

		if (datos[16].isEmpty()) {
			codigo_comic = comic_temp.getCodigo_comic();
		} else {
			codigo_comic = datos[16];
		}

		Comic comic = new Comic(id_comic, nombre, numCaja, numero, variante, firma, editorial, formato, procedencia,
				fecha, guionista, dibujante, estado, nombreKeyIssue, puntuacion, portada, url_referencia, precio_comic,
				codigo_comic);

		String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);

		utilidad.nueva_imagen(portada, codigo_imagen);
		comic.setImagen(SOURCE_PATH + File.separator + codigo_imagen + ".jpg");

		if (id_comic.length() == 0 || nombre.length() == 0 || numero.length() == 0 || editorial.length() == 0
				|| guionista.length() == 0 || dibujante.length() == 0 || procedencia.length() == 0) {

			String excepcion = "ERROR.Faltan datos por rellenar";
			nav.alertaException(excepcion);
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {
			libreria.actualizarComic(comic);

			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #A0F52D");
			prontInfo.setText("Deseo Concedido..." + "\nHas modificado correctamente el comic");
			libreria.listasAutoCompletado();
			funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
			funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);

			Platform.runLater(() -> {

				funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
			});

			imagen = new Image(file.toURI().toString());
			imagencomic.setImage(imagen);

			Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
			imagenFondo.setImage(nuevaImagen);

//			Utilidades.eliminarFichero(comic_temp.getImagen());

			if (Utilidades.isURL(datos[10])) {
				Utilidades.borrarImagen(portada);
			}

			List<ComboBox<String>> comboboxes = VentanaAccionController.getComboBoxes();

			funcionesCombo.rellenarComboBox(comboboxes);
		}
	}
	
	/**
	 * Funcion que elimina un comic de la base de datos.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@FXML
	void eliminarDatos(ActionEvent event) throws IOException, SQLException, InterruptedException, ExecutionException {
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");
		detenerAnimacionPront();
		iniciarAnimacionCambioImagen();
		if (comprobarID(id_comic)) {
			libreria.eliminarComicBBDD(id_comic);
			libreria.reiniciarBBDD();
			libreria.listasAutoCompletado();
			funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
			funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);
			funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion

			Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
			imagenFondo.setImage(nuevaImagen);

			List<ComboBox<String>> comboboxes = VentanaAccionController.getComboBoxes();

			funcionesCombo.rellenarComboBox(comboboxes);

		}
		detenerAnimacionPront();
	}

	/**
	 * Método asincrónico para realizar la modificación de cómic. Utiliza un hilo
	 * separado para ejecutar la lógica de modificación sin bloquear el hilo
	 * principal.
	 */
	public void modificacionComicAsync() {

		Thread updateThread = new Thread(() -> {
			try {
				iniciarAnimacionCambioImagen();
				modificacionComic();

				detenerAnimacionPront();
			} catch (NumberFormatException | SQLException | IOException | InterruptedException | ExecutionException
					| URISyntaxException e) {
				e.printStackTrace();
			}
		});

		updateThread.setDaemon(true); // Set the thread as a daemon thread
		updateThread.start();
	}

	/**
	 * Método asincrónico para realizar la adición de cómic. Utiliza un hilo
	 * separado para ejecutar la lógica de animación, subida de cómic y detención de
	 * animación sin bloquear el hilo principal.
	 */
	public void aniadirComicAsync() {

		Thread updateThread = new Thread(() -> {
			try {
				iniciarAnimacionCambioImagen();
				subidaComic();

				detenerAnimacionPront();
			} catch (NumberFormatException | SQLException | IOException | InterruptedException | ExecutionException
					| URISyntaxException e) {
				e.printStackTrace();
			}
		});

		updateThread.setDaemon(true); // Set the thread as a daemon thread
		updateThread.start();
	}

}
