package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Introducir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase permite acceder a la ventana de introducir datos a la base de datos.
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import Funcionamiento.Comic;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

/**
 * Esta clase sirve para introducir datos a la base de datos
 *
 * @author Alejandro Rodriguez
 */
public class IntroducirDatosController implements Initializable {

	@FXML
	private MenuItem menu_archivo_desconectar;

	@FXML
	private MenuItem menu_archivo_sobreMi;

	@FXML
	private MenuItem menu_archivo_cerrar;

	@FXML
	private MenuItem menu_archivo_volver;

	@FXML
	private MenuItem menu_comic_aleatoria;

	@FXML
	private MenuItem menu_comic_eliminar;

	@FXML
	private MenuItem menu_comic_modificar;

	@FXML
	private MenuItem menu_comic_puntuar;
	
	@FXML
	private MenuItem menu_estadistica_estadistica;

	@FXML
	private MenuItem menu_estadistica_firmados;

	@FXML
	private MenuItem menu_estadistica_puntuados;

	@FXML
	private MenuItem menu_estadistica_comprados;

	@FXML
	private MenuItem menu_estadistica_posesion;
	
	@FXML
	private MenuItem menu_estadistica_vendidos;

	@FXML
	private MenuBar menu_navegacion;

	@FXML
	private Menu navegacion_cerrar;

	@FXML
	private Menu navegacion_comic;
	
	@FXML
	private Menu navegacion_estadistica;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonIntroducir;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Button botonbbdd;

	@FXML
	private Button botonNuevaPortada;

	@FXML
	private TextArea pantallaInformativa;

	@FXML
	private DatePicker fechaPublicacion;

	@FXML
	private TextField nombreComic;

	@FXML
	private TextField nombreDibujante;

	@FXML
	private TextField nombreEditorial;

	@FXML
	private TextField nombreFirma;

	@FXML
	private TextField nombreGuionista;

	@FXML
	private TextField nombreVariante;

	@FXML
	private TextField numeroComic;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextField nombreAni;

	@FXML
	private TextField numeroAni;

	@FXML
	private TextField varianteAni;

	@FXML
	private TextField firmaAni;

	@FXML
	private TextField editorialAni;

	@FXML
	private DatePicker fechaAni;

	@FXML
	private TextField guionistaAni;

	@FXML
	private TextField dibujanteAni;

	@FXML
	private TextField direccionImagen;

	@FXML
	private TextField numeroCaja;

	@FXML
	private TextField numeroCajaAni;

	@FXML
	private Label idMod;

	@FXML
	public TableView<Comic> tablaBBDD;

	@FXML
	private TableColumn<Comic, String> ID;

	@FXML
	private TableColumn<Comic, String> numero;

	@FXML
	private TableColumn<Comic, String> caja;

	@FXML
	private TableColumn<Comic, String> procedencia;

	@FXML
	private TableColumn<Comic, String> variante;

	@FXML
	private TableColumn<Comic, String> dibujante;

	@FXML
	private TableColumn<Comic, String> editorial;

	@FXML
	private TableColumn<Comic, String> fecha;

	@FXML
	private TableColumn<Comic, String> firma;

	@FXML
	private TableColumn<Comic, String> formato;

	@FXML
	private TableColumn<Comic, String> guionista;

	@FXML
	private TableColumn<Comic, String> nombre;

	@FXML
	private TableColumn<Comic, String> puntuacion;

	@FXML
	private ComboBox<String> nombreFormato;

	@FXML
	private ComboBox<String> formatoAni;

	@FXML
	private ComboBox<String> nombreProcedencia;

	@FXML
	private ComboBox<String> procedenciaAni;

	@FXML
	private ComboBox<String> estadoComic;

	@FXML
	private ImageView imagencomic;

	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static Utilidades utilidad = null;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		libreria = new DBLibreriaManager();
		try {
			listas_autocompletado();
			libreria.listasAutoCompletado();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ObservableList<String> situacionEstado = FXCollections.observableArrayList("En posesion", "Comprado",
				"En venta");
		estadoComic.setItems(situacionEstado);
		estadoComic.getSelectionModel().selectFirst();

		ObservableList<String> procedenciaEstadoNuevo = FXCollections.observableArrayList("Spain", "USA", "Japon",
				"Italia", "Francia");
		procedenciaAni.setItems(procedenciaEstadoNuevo);
		procedenciaAni.getSelectionModel().selectFirst();

		ObservableList<String> formatoNuevo = FXCollections.observableArrayList("Grapa", "Tapa dura", "Tapa blanda",
				"Manga", "Libro");
		formatoAni.setItems(formatoNuevo);
		formatoAni.getSelectionModel().selectFirst();

		ObservableList<String> procedenciaEstadoActual = FXCollections.observableArrayList("Todo","Spain", "USA", "Japon",
				"Italia", "Francia");
		nombreProcedencia.setItems(procedenciaEstadoActual);
		nombreProcedencia.getSelectionModel().selectFirst();

		ObservableList<String> formatoActual = FXCollections.observableArrayList("Todo","Grapa", "Tapa dura", "Tapa blanda",
				"Manga", "Libro");
		nombreFormato.setItems(formatoActual);
		nombreFormato.getSelectionModel().selectFirst();
		

		TextFormatter<Integer> textFormatterAni = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});

		TextFormatter<Integer> textFormatterComic = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});
		TextFormatter<Integer> textFormatterComic2 = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});
		
		TextFormatter<Integer> textFormatterComic3 = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});
		
		numeroAni.setTextFormatter(textFormatterAni);
		numeroComic.setTextFormatter(textFormatterComic);
		numeroCaja.setTextFormatter(textFormatterComic2);
		numeroCajaAni.setTextFormatter(textFormatterComic3);
	}

	public void listas_autocompletado() throws SQLException {
		TextFields.bindAutoCompletion(nombreComic, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(nombreVariante, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(nombreFirma, DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(nombreEditorial, DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(nombreGuionista, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(nombreDibujante, DBLibreriaManager.listaDibujante);

		TextFields.bindAutoCompletion(nombreAni, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(varianteAni, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(firmaAni, DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(editorialAni, DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(guionistaAni, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(dibujanteAni, DBLibreriaManager.listaDibujante);
		DBLibreriaManager.listaNombre.clear();
	}

	/**
	 * Funcion que permite mostrar la imagen de portada cuando clickeas en una
	 * tabla.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException 
	 */
	@FXML
	void clickRaton(MouseEvent event) throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		libreria.libreriaCompleta();
		String id_comic;
		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();
		pantallaInformativa.setStyle("");
		if (idRow != null) {
			id_comic = idRow.getID();

			Comic comic_temp = libreria.comicDatos(id_comic);

			nombreAni.setText(comic_temp.getNombre());

			numeroAni.setText(comic_temp.getNumero());

			varianteAni.setText(comic_temp.getVariante());

			firmaAni.setText(comic_temp.getFirma());

			editorialAni.setText(comic_temp.getEditorial());

			String formato = comic_temp.getFormato();
			formatoAni.getSelectionModel().select(formato);

			String procedencia = comic_temp.getProcedencia();
			procedenciaAni.getSelectionModel().select(procedencia);

			String fechaString = comic_temp.getFecha();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate fecha = LocalDate.parse(fechaString, formatter);
			fechaAni.setValue(fecha);

			guionistaAni.setText(comic_temp.getGuionista());

			dibujanteAni.setText(comic_temp.getDibujante());

			numeroCajaAni.setText(comic_temp.getNumCaja());

			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));
			imagencomic.setImage(libreria.selectorImage(id_comic));
		}
		DBManager.resetConnection();
	}

	@FXML
	void teclasDireccion(KeyEvent event) throws IOException, SQLException {
		if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
			libreria = new DBLibreriaManager();
			libreria.libreriaCompleta();
			String id_comic;

			Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();
			pantallaInformativa.setStyle("");
			if (idRow != null) {
				id_comic = idRow.getID();

				Comic comic_temp = libreria.comicDatos(id_comic);

				nombreAni.setText(comic_temp.getNombre());

				numeroAni.setText(comic_temp.getNumero());

				varianteAni.setText(comic_temp.getVariante());

				firmaAni.setText(comic_temp.getFirma());

				editorialAni.setText(comic_temp.getEditorial());

				String formato = comic_temp.getFormato();
				formatoAni.getSelectionModel().select(formato);

				String procedencia = comic_temp.getProcedencia();
				procedenciaAni.getSelectionModel().select(procedencia);

				String fechaString = comic_temp.getFecha();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				LocalDate fecha = LocalDate.parse(fechaString, formatter);
				fechaAni.setValue(fecha);

				guionistaAni.setText(comic_temp.getGuionista());

				dibujanteAni.setText(comic_temp.getDibujante());

				numeroCajaAni.setText(comic_temp.getNumCaja());

				pantallaInformativa.setOpacity(1);
				pantallaInformativa.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));

				imagencomic.setImage(libreria.selectorImage(id_comic));
			}
			DBManager.resetConnection();
		}
	}

	/**
	 * Metodo que limpia todos los datos de los textField que hay en pantalla
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {

		nombreComic.setText("");
		numeroComic.setText("");
		nombreVariante.setText("");
		nombreFirma.setText("");
		nombreEditorial.setText("");
		fechaPublicacion.setValue(null);
		nombreDibujante.setText("");
		nombreGuionista.setText("");
		busquedaGeneral.setText("");
		numeroCaja.setText("");

		// Campos de datos a modificar
		nombreAni.setText("");
		numeroAni.setText("");
		nombreAni.setText("");
		firmaAni.setText("");
		editorialAni.setText("");
		fechaAni.setValue(null);
		guionistaAni.setText("");
		dibujanteAni.setText("");
		numeroCajaAni.setText(null);
		pantallaInformativa.setText(null);
		pantallaInformativa.setOpacity(0);
		tablaBBDD.getItems().clear();
		botonNuevaPortada.setStyle(null);
		imagencomic.setImage(null);
		borrarErrores();
	}

	/**
	 * Metodo que añade datos a la base de datos segun los parametros introducidos
	 * en los textField
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException 
	 */
	@FXML
	public void agregarDatos(ActionEvent event) throws IOException, SQLException {

		libreria = new DBLibreriaManager();
		subidaComic();
		libreria.reiniciarBBDD();
		direccionImagen.setText("");
	}

	/**
	 * Funcion que permite la subida de una
	 *
	 * @param event
	 */
	@FXML
	void nuevaPortada(ActionEvent event) {
		imagencomic.setImage(null);
		subirPortada();

	}

	/**
	 * Permite abir una ventana para abrir ficheros de un determinado formato.
	 *
	 * @return
	 */
	public FileChooser tratarFichero() {
		FileChooser fileChooser = new FileChooser(); // Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Subiendo imagen", "*.jpg"));

		return fileChooser;
	}

	/**
	 * Funcion que escribe en el TextField de "Direccion de imagen" la dirrecion de
	 * la imagen
	 */
	public void subirPortada() {
		File file = tratarFichero().showOpenDialog(null); // Llamada a funcion
		if (file != null) {
			direccionImagen.setText(file.getAbsolutePath().toString());
		} else {
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("Has cancelado la subida de portada.");
		}
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String estadoActual() {

		String situacionEstado = estadoComic.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
		// "puntuacion"
		return situacionEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaActual() {

		String procedenciaEstado = nombreProcedencia.getSelectionModel().getSelectedItem().toString(); // Toma el valor
																										// del menu
		// "procedencia"
		return procedenciaEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String formatoActual() {

		String formatoEstado = nombreFormato.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
		// "formato"
		return formatoEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String formatoNuevo() {

		String formatoEstadoNuevo = formatoAni.getSelectionModel().getSelectedItem().toString(); // Toma el valor del
																									// menu
		// "formato"
		return formatoEstadoNuevo;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaNueva() {

		String procedenciaEstadoNuevo = procedenciaAni.getSelectionModel().getSelectedItem().toString(); // Toma el
																											// valor del
																											// menu
		// "puntuacion"
		return procedenciaEstadoNuevo;
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		listaPorParametro(); // Llamada a funcion
		busquedaGeneral.setText("");
	}

	/**
	 * Metodo que muestra toda la base de datos.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException 
	 */
	@FXML
	void verTodabbdd(ActionEvent event) throws IOException, SQLException {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
		borrarErrores();
	}

	/**
	 * Funcion que muestra los comics que coincidan con los parametros introducidos
	 * en los textField
	 *
	 * @return
	 * @throws SQLException 
	 */
	public void listaPorParametro() throws SQLException {
		libreria = new DBLibreriaManager();

		String datos[] = camposComicActuales();

		Comic comic = new Comic("", datos[1], datos[12], datos[2], datos[3], datos[4], datos[5], datos[6],
				datos[7], datos[8], datos[9], datos[10], "", "", null);

		tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()));
		busquedaGeneral.setText("");
		borrarErrores();
	}

	/**
	 * funcion que obtiene los datos de los comics de la base de datos y los
	 * devuelve en el textView
	 *
	 * @param listaComic
	 */
	@SuppressWarnings("unchecked")
	public void tablaBBDD(List<Comic> listaComic) {
		tablaBBDD.getColumns().setAll(nombre, caja, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listaComic);
	}

	/**
	 * Permite introducir un comic en la base de datos de forma manual
	 * 
	 * @throws IOException
	 * @throws SQLException 
	 */
	public void subidaComic() throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		utilidad = new Utilidades();
		File file;
		LocalDate fecha_comic;

		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator
				+ utilidad.obtenerDatoDespuesDeDosPuntos("Database") + File.separator + "portadas";
		Utilidades.convertirNombresCarpetas(sourcePath);

		if (nav.alertaInsertar()) {

			String datos[] = camposComicIntroducir();

			String portada = "";

			String numCaja = "";

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

			if (datos[10] != "") {

				file = new File(datos[10]);

				if (!file.exists()) {
					portada = "Funcionamiento/sinPortada.jpg";
					Image imagen = new Image(portada);
					imagencomic.setImage(imagen);
				} else {
					portada = datos[10];
					Image imagen = new Image(portada);
					imagencomic.setImage(imagen);
				}
			}

			String estado = datos[11];

			if (datos[12] != null) {
				numCaja = datos[12];
			} else {
				numCaja = "0";
			}

			Comic comic = new Comic("", nombre, numCaja, numero, variante, firma, editorial, formato, procedencia,
					fecha_comic.toString(), guionista, dibujante, estado, "Sin puntuar", portada);

			if (nombre.length() == 0 || numero.length() == 0 || editorial.length() == 0 || guionista.length() == 0
					|| dibujante.length() == 0) {
				String excepcion = "No puedes introducir un comic si no has completado todos los datos";

				validateComicFields(comic);

				pantallaInformativa.setOpacity(1);
				pantallaInformativa.setStyle("-fx-background-color: #F53636");
				pantallaInformativa.setText("Error. Debes de introducir los datos correctos.");

				nav.alertaException(excepcion);
			} else {
				
				String codigo_imagen = Utilidades.generarCodigoUnico(sourcePath + File.separator);
				
				utilidad.nueva_imagen(portada,codigo_imagen); //Se genera el fichero del comic y el nuevo nombre de este

//				String nueva_portada = utilidad.obtenerNombreCompleto(comic);
				comic.setImagen(sourcePath + File.separator + codigo_imagen + ".jpg");
				libreria.insertarDatos(comic);

				pantallaInformativa.setOpacity(1);
				pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
				pantallaInformativa.setText(
						"Has introducido correctamente: " + comic.toString().replace("[", "").replace("]", ""));
				libreria.listasAutoCompletado();
				nombreColumnas(); // Llamada a funcion
				tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
			}
		} else {
			borrarErrores();
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("Se ha cancelado la subida del nuevo comic.");
		}
	}

	public void validateComicFields(Comic comic) {

		if (comic.getNombre().length() == 0) {
			nombreAni.setStyle("-fx-background-color: #FF0000;");
		}

		if (comic.getNumero().length() == 0) {
			numeroAni.setStyle("-fx-background-color: #FF0000;");
		}

		if (comic.getEditorial().length() == 0) {
			editorialAni.setStyle("-fx-background-color: #FF0000;");
		}

		if (comic.getGuionista().length() == 0) {
			guionistaAni.setStyle("-fx-background-color: #FF0000;");
		}

		if (comic.getDibujante().length() == 0) {
			dibujanteAni.setStyle("-fx-background-color: #FF0000;");
		}
	}

	public void borrarErrores() {

		nombreAni.setStyle("");

		numeroAni.setStyle("");

		editorialAni.setStyle("");

		guionistaAni.setStyle("");

		dibujanteAni.setStyle("");
	}

	/**
	 * Funcion que devuelve un array con los datos de los TextField correspondientes
	 * a la los comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public String[] camposComicActuales() {
		String campos[] = new String[13];

		campos[1] = nombreComic.getText();

		campos[2] = numeroComic.getText();

		campos[3] = nombreVariante.getText();

		campos[4] = nombreFirma.getText();

		campos[5] = nombreEditorial.getText();

		if(formatoActual() == "Todo") {
			campos[6] = "";
		}else {
			campos[6] = formatoActual();
		}
		
		if(procedenciaActual() == "Todo") {
			campos[7] = "";
		}else {
			campos[7] = procedenciaActual();

		}

		LocalDate fecha = fechaPublicacion.getValue();
		campos[8] = (fecha != null) ? fecha.toString() : "";

		campos[9] = nombreGuionista.getText();

		campos[10] = nombreDibujante.getText();

		campos[12] = numeroCaja.getText();

		return campos;
	}

	/**
	 * Funcion que devuelve un array con los datos de los TextField del comic a
	 * introducir.
	 *
	 * @return
	 */
	public String[] camposComicIntroducir() {

		utilidad = new Utilidades();

		String campos[] = new String[13];

		campos[0] = nombreAni.getText();

		campos[1] = numeroAni.getText();

		campos[2] = varianteAni.getText();

		campos[3] = firmaAni.getText();

		campos[4] = editorialAni.getText();

		campos[5] = formatoNuevo();

		campos[6] = procedenciaNueva();

		LocalDate fecha = fechaAni.getValue();
		campos[7] = (fecha != null) ? fecha.toString() : "2000-01-01";

		campos[8] = guionistaAni.getText();

		campos[9] = dibujanteAni.getText();

		campos[10] = direccionImagen.getText();

		campos[11] = estadoActual();

		if (campos[12] == null) {
			campos[12] = "0";
		} else {
			campos[12] = numeroCajaAni.getText();
		}

		return utilidad.comaPorGuion(campos);
	}

	/**
	 * Permite dar valor a las celdas de la TableView
	 */
	private void nombreColumnas() {
		ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		caja.setCellValueFactory(new PropertyValueFactory<>("numCaja"));
		numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		variante.setCellValueFactory(new PropertyValueFactory<>("variante"));
		firma.setCellValueFactory(new PropertyValueFactory<>("firma"));
		editorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
		formato.setCellValueFactory(new PropertyValueFactory<>("formato"));
		procedencia.setCellValueFactory(new PropertyValueFactory<>("procedencia"));
		fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		guionista.setCellValueFactory(new PropertyValueFactory<>("guionista"));
		dibujante.setCellValueFactory(new PropertyValueFactory<>("dibujante"));
		puntuacion.setCellValueFactory(new PropertyValueFactory<>("puntuacion"));
	}
	
	/**
	 * Funcion que al pulsar el boton de 'botonPuntuacion' se muestran aquellos
	 * comics que tienen una puntuacion
	 *
	 * @param event
	 * @throws SQLException 
	 */
	@FXML
	void comicsPuntuacion(ActionEvent event) throws SQLException {
		pantallaInformativa.setOpacity(0);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaPuntuacion());

	}

	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 * @throws SQLException 
	 */
	@FXML
	void comicsVendidos(ActionEvent event) throws SQLException {
		pantallaInformativa.setOpacity(0);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaVendidos());
	}

	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 * @throws SQLException 
	 */
	@FXML
	void comicsFirmados(ActionEvent event) throws SQLException {
		pantallaInformativa.setOpacity(0);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaFirmados());
	}

	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 * @throws SQLException 
	 */
	@FXML
	void comicsComprados(ActionEvent event) throws SQLException {
		pantallaInformativa.setOpacity(0);
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaComprados());
	}
	
	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 * @throws SQLException 
	 */
	@FXML
	void comicsEnPosesion(ActionEvent event) throws SQLException {
		pantallaInformativa.setOpacity(0);
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaPosesion());
	}
	
	/**
	 * Se llama a funcion que permite ver las estadisticas de la bbdd
	 *
	 * @param event
	 */
	@FXML
	void verEstadistica(ActionEvent event) {
		tablaBBDD.getItems().clear();
		libreria = new DBLibreriaManager();
		pantallaInformativa.setOpacity(1);
		pantallaInformativa.setText(libreria.procedimientosEstadistica());
	}

	/////////////////////////////////
	//// METODOS LLAMADA A VENTANAS//
	/////////////////////////////////

	/**
	 * Permite el cambio de ventana a la ventana de EliminarDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaEliminar(ActionEvent event) {

		nav.verEliminarDatos();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana de ModificarDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaModificar(ActionEvent event) {

		nav.verModificarDatos();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana deRecomendacionesController
	 *
	 * @param event
	 */
	@FXML
	void ventanaRecomendar(ActionEvent event) {

		nav.verRecomendacion();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 *
	 * @param event
	 */
	@FXML
	void ventanaPuntuar(ActionEvent event) {

		nav.verPuntuar();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Metodo que permite abrir la ventana "sobreMiController"
	 *
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {

		nav.verSobreMi();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void desconectar(ActionEvent event) throws IOException {
		nav.verAccesoBBDD();
		DBManager.close();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void volverMenu(ActionEvent event) throws IOException {
		nav.verMenuPrincipal();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Maneja la acción de salida del programa.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {
		// Lógica para manejar la acción de "Salir"
		if (nav.salirPrograma(event)) {
			Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {

		Platform.exit();

	}

}