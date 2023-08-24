package Controladores;

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
 *  - Puntuar comics que se encuentren dentro de la base de datos.
 *  Esta clase permite acceder al menu principal donde se puede viajar a diferentes ventanas, etc.
 *
 *  Version 5.5.0.1
 *
 *  @author Alejandro Rodriguez
 *
 */
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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Funcionamiento.Comic;
import Funcionamiento.FuncionesComboBox;
import Funcionamiento.FuncionesTableView;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * Esta clase sirve para introducir datos a la base de datos
 *
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
	private MenuItem menu_estadistica_key_issue;

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
	private TextArea prontInfo;

	@FXML
	private DatePicker fechaPublicacion;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextField nombreAni;

	@FXML
	private ComboBox<String> numeroAni;

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
	private TextField nombreKeyIssue;

	@FXML
	private TextField precioComic;

	@FXML
	private TextField urlReferencia;

	@FXML
	private ComboBox<String> numeroCajaAni;

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
	private TableColumn<Comic, String> referencia;

	@FXML
	private ComboBox<String> formatoAni;

	@FXML
	private ComboBox<String> procedenciaAni;

	@FXML
	private ComboBox<String> estadoComic;

	@FXML
	private ComboBox<String> nombreComic;

	@FXML
	private ComboBox<String> numeroComic;

	@FXML
	private ComboBox<String> nombreFirma;

	@FXML
	private ComboBox<String> nombreGuionista;

	@FXML
	private ComboBox<String> nombreVariante;

	@FXML
	private ComboBox<String> numeroCaja;

	@FXML
	private ComboBox<String> nombreProcedencia;

	@FXML
	private ComboBox<String> nombreFormato;

	@FXML
	private ComboBox<String> nombreEditorial;

	@FXML
	private ComboBox<String> nombreDibujante;

	@FXML
	private ImageView imagencomic;

	@FXML
	private VBox rootVBox;

	@FXML
	private VBox vboxContenido;

	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static Utilidades utilidad = null;
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();
	private static FuncionesTableView funcionesTabla = new FuncionesTableView();
	private List<TableColumn<Comic, String>> columnList;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		libreria = new DBLibreriaManager();
		utilidad = new Utilidades();

		prontInfo.textProperty().addListener((observable, oldValue, newValue) -> {
			ajustarAnchoVBox(prontInfo);
		});

		// Asegurarnos de que el VBox ajuste su tamaño correctamente al inicio
		Platform.runLater(() -> ajustarAnchoVBox(prontInfo));

		Platform.runLater(() -> funcionesTabla.seleccionarRaw(tablaBBDD));
		
		Platform.runLater(() -> asignarTooltips());

		List<TableColumn<Comic, String>> columnListCarga = Arrays.asList(nombre, caja, numero, variante, firma,
				editorial, formato, procedencia, fecha, guionista, dibujante, referencia);
		columnList = columnListCarga;
		
		try {
			libreria.listasAutoCompletado();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		funcionesTabla.modificarColumnas(tablaBBDD,columnList);
		restringir_entrada_datos();

		List<ComboBox<String>> comboboxes = Arrays.asList(nombreComic, numeroComic, nombreVariante, nombreProcedencia,
				nombreFormato, nombreDibujante, nombreGuionista, nombreEditorial, nombreFirma, numeroCaja);

		int totalComboboxes = comboboxes.size();
		
		funcionesCombo.rellenarComboBox(comboboxes);
		funcionesCombo.lecturaComboBox(totalComboboxes, comboboxes);
//		rellenarComboBoxNuevo();
		restringirSimbolos(guionistaAni);
		restringirSimbolos(dibujanteAni);
		restringirSimbolos(varianteAni);

		// Desactivar el enfoque en el VBox para evitar que reciba eventos de teclado
		rootVBox.setFocusTraversable(false);

		// Agregar un filtro de eventos para capturar el enfoque en el TableView y
		// desactivar el enfoque en el VBox
		tablaBBDD.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			rootVBox.setFocusTraversable(false);
			tablaBBDD.requestFocus();
		});

		// Agregar un filtro de eventos para capturar el enfoque en el VBox y desactivar
		// el enfoque en el TableView
		rootVBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			tablaBBDD.setFocusTraversable(false);
			rootVBox.requestFocus();
		});

		prontInfo.setEditable(false);
	}

	private void ajustarAnchoVBox(TextArea textArea) {
		// Crear un objeto Text con el contenido del TextArea
		Text text = new Text(textArea.getText());

		// Configurar el mismo estilo que tiene el TextArea
		text.setFont(textArea.getFont());

		double textHeight = text.getLayoutBounds().getHeight();

		textArea.setPrefHeight(textHeight);
	}

	private void asignarTooltips() {
		asignarTooltip(botonbbdd, "Muestra toda la base de datos");
		asignarTooltip(botonLimpiarComic, "Limpia la pantalla y reinicia todos los valores");
		asignarTooltip(botonMostrarParametro, "Muestra los comics o libros o mangas por parametro");

		asignarTooltip(nombreComic, "Nombre de los cómics / libros / mangas");
		asignarTooltip(numeroComic, "Número del cómic / libro / manga");
		asignarTooltip(nombreFirma, "Nombre de la firma del cómic / libro / manga");
		asignarTooltip(nombreGuionista, "Nombre del guionista del cómic / libro / manga");
		asignarTooltip(nombreVariante, "Nombre de la variante del cómic / libro / manga");
		asignarTooltip(numeroCaja, "Número de la caja donde se guarda el cómic / libro / manga");
		asignarTooltip(nombreProcedencia, "Nombre de la procedencia del cómic / libro / manga");
		asignarTooltip(nombreFormato, "Nombre del formato del cómic / libro / manga");
		asignarTooltip(nombreEditorial, "Nombre de la editorial del cómic / libro / manga");
		asignarTooltip(nombreDibujante, "Nombre del dibujante del cómic / libro / manga");

		asignarTooltip(nombreKeyIssue,
				"Aqui puedes añadir si el comic tiene o no alguna clave, esto es para coleccionistas. Puedes dejarlo vacio");
	}

	private void asignarTooltip(Button boton, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
		boton.setTooltip(tooltip);
	}

	private void asignarTooltip(ComboBox<?> comboBox, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
		comboBox.setTooltip(tooltip);
	}

	private void asignarTooltip(TextField textField, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
		textField.setTooltip(tooltip);
	}

	/**
	 * Funcion que permite restringir entrada de datos de todo aquello que no sea un
	 * numero entero en los comboBox numeroComic y caja_comic
	 */
	public void restringir_entrada_datos() {
		numeroComic.getEditor().setTextFormatter(validador_Nenteros());
		numeroAni.getEditor().setTextFormatter(validador_Nenteros());
		numeroCaja.getEditor().setTextFormatter(validador_Nenteros());
		numeroCajaAni.getEditor().setTextFormatter(validador_Nenteros());
		precioComic.setTextFormatter(validador_Ndecimales());
	}

	public TextFormatter<Integer> validador_Nenteros() {
		// Crear un validador para permitir solo números enteros
		TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			if (change.getControlNewText().matches("\\d*")) {
				return change;
			}
			return null;
		});

		return textFormatter;
	}

	public TextFormatter<Double> validador_Ndecimales() {
		// Crear un validador para permitir solo números decimales (double)
		TextFormatter<Double> textFormatter = new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*\\.?\\d*")) {
				return change;
			}
			return null;
		});

		return textFormatter;
	}

	private void restringirSimbolos(TextField textField) {
		Tooltip tooltip = new Tooltip();

		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			String allowedPattern = "[\\p{L}\\p{N}\\s,.-]*"; // Expresión regular para permitir letras, números,
																// espacios, ",", "-" y "."

			if (newValue != null && !newValue.matches(allowedPattern)) {
				textField.setText(oldValue);
			} else {
				String updatedValue = newValue.replaceAll("\\s*(?<![,-])(?=[,-])|(?<=[,-])\\s*", "");

				if (!updatedValue.equals(newValue)) {
					textField.setText(updatedValue);
				}
			}
		});

		textField.setOnMouseEntered(event -> {
			tooltip.setShowDelay(Duration.ZERO);
			tooltip.setHideDelay(Duration.ZERO);

			String mensaje = "En caso de tener varios artistas en variante, guionista o dibujante, separalos usando una coma ',' o guion '-'";
			tooltip.setText(mensaje);
			tooltip.show(textField, event.getSceneX(), event.getSceneY());
			tooltip.setX(event.getScreenX() + 10); // Ajusta el desplazamiento X según tus necesidades
			tooltip.setY(event.getScreenY() - 20); // Ajusta el desplazamiento Y según tus necesidades
		});

		textField.setOnMouseExited(event -> {
			tooltip.hide();
		});
	}

	/**
	 * Permite rellenar los datos de los comboBox con los datos de las listas
	 */
	public void rellenarComboBoxNuevo() {

		ObservableList<String> formatoNuevo = FXCollections.observableArrayList("Grapa (Issue individual)",
				"Tapa blanda (Paperback)", "Cómic de bolsillo (Pocket)", "Edición de lujo (Deluxe Edition)",
				"Edición omnibus (Omnibus)", "Edición integral (Integral)", "Tapa dura (Hardcover)",
				"eBook (libro electrónico)", "Cómic digital (Digital Comic)", "Manga digital (Digital Manga)", "Manga (Manga tome)",
				"PDF (Portable Document Format)", "Revista (Magazine)",
				"Edición de coleccionista (Collector's Edition)", "Edición especial (Special Edition)",
				"Edición con extras (Bonus Edition)" , "Libro (Book)");
		formatoAni.setItems(formatoNuevo);
		formatoAni.getSelectionModel().selectFirst();

		ObservableList<String> procedenciaEstadoNuevo = FXCollections.observableArrayList(
				"Estados Unidos (United States)", "Japón (Japan)", "Francia (France)", "Italia (Italy)",
				"España (Spain)", "Reino Unido (United Kingdom)", "Alemania (Germany)", "Brasil (Brazil)",
				"Corea del Sur (South Korea)", "México (Mexico)", "Canadá (Canada)", "China (China)",
				"Australia (Australia)", "Argentina (Argentina)", "India (India)", "Bélgica (Belgium)",
				"Países Bajos (Netherlands)", "Portugal (Portugal)", "Suecia (Sweden)", "Suiza (Switzerland)",
				"Finlandia (Finland)", "Noruega (Norway)", "Dinamarca (Denmark)");
		procedenciaAni.setItems(procedenciaEstadoNuevo);
		procedenciaAni.getSelectionModel().selectFirst();
		ObservableList<String> situacionEstado = FXCollections.observableArrayList("En posesion", "Comprado",
				"En venta");
		estadoComic.setItems(situacionEstado);
		estadoComic.getSelectionModel().selectFirst();
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
		prontInfo.setStyle("");
		if (idRow != null) {
			id_comic = idRow.getID();

			Comic comic_temp = libreria.comicDatos(id_comic);

			nombreAni.setText(comic_temp.getNombre());

			String numeroNuevo = comic_temp.getNumero();
			numeroAni.getSelectionModel().select(numeroNuevo);

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

			String cajaAni = comic_temp.getNumCaja();
			numeroCajaAni.getSelectionModel().select(cajaAni);

			nombreKeyIssue.setText(comic_temp.getKey_issue());
			estadoComic.getSelectionModel().select(comic_temp.getEstado());

			precioComic.setText(comic_temp.getPrecio_comic());
			urlReferencia.setText(comic_temp.getUrl_referencia());

			prontInfo.setOpacity(1);
			prontInfo.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));
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
			prontInfo.setStyle("");
			if (idRow != null) {
				id_comic = idRow.getID();

				Comic comic_temp = libreria.comicDatos(id_comic);

				nombreAni.setText(comic_temp.getNombre());

				String numeroNuevo = comic_temp.getNumero();
				numeroAni.getSelectionModel().select(numeroNuevo);

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

				String cajaAni = comic_temp.getNumCaja();
				numeroCajaAni.getSelectionModel().select(cajaAni);

				nombreKeyIssue.setText(comic_temp.getKey_issue());
				estadoComic.getSelectionModel().select(comic_temp.getEstado());

				precioComic.setText(comic_temp.getPrecio_comic());
				urlReferencia.setText(comic_temp.getUrl_referencia());

				prontInfo.setOpacity(1);
				prontInfo.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));
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
		limpiezaDatosAni();
		limpiezaDatos();
	}

	public void limpiezaDatosAni() {
		// Campos de datos a modificar
		nombreAni.setText("");
		nombreAni.setText("");
		firmaAni.setText("");
		editorialAni.setText("");
		fechaAni.setValue(null);
		guionistaAni.setText("");
		dibujanteAni.setText("");
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		nombreKeyIssue.setText("");
		tablaBBDD.getItems().clear();
		botonNuevaPortada.setStyle(null);
		imagencomic.setImage(null);
		borrarErrores();
	}

	public void limpiezaDatos() {

		// Clear all ComboBox text fields and values
		for (ComboBox<String> comboBox : Arrays.asList(nombreComic, numeroComic, nombreFirma, nombreGuionista,
				nombreVariante, numeroCaja, nombreProcedencia, nombreFormato, nombreEditorial, nombreDibujante,
				numeroAni, formatoAni, procedenciaAni, numeroCajaAni)) {
			comboBox.setValue("");
			comboBox.getEditor().setText("");
		}

		// Clear additional UI elements
		fechaPublicacion.setValue(null);
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);
		rellenarComboBoxNuevo();
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
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Has cancelado la subida de portada.");
		}
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaActual() {

		String procedenciaEstadoNuevo = "";
		if (nombreProcedencia.getSelectionModel().getSelectedItem() != null) {
			procedenciaEstadoNuevo = nombreProcedencia.getSelectionModel().getSelectedItem().toString();
		}
		return procedenciaEstadoNuevo;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreFormato" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String formatoActual() {

		String formatoEstado = "";
		if (nombreFormato.getSelectionModel().getSelectedItem() != null) {
			formatoEstado = nombreFormato.getSelectionModel().getSelectedItem().toString();
		}
		return formatoEstado;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreEditorial" y lo
	 * devuelve, para la busqueda de comic
	 * 
	 * @return
	 */
	public String editorialActual() {

		String editorialComic = "";

		if (nombreEditorial.getSelectionModel().getSelectedItem() != null) {
			editorialComic = nombreEditorial.getSelectionModel().getSelectedItem().toString();
		}

		return editorialComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreDibujante" y lo
	 * devuelve, para la busqueda de comic
	 * 
	 * @return
	 */
	public String dibujanteActual() {
		String dibujanteComic = "";

		if (nombreDibujante.getSelectionModel().getSelectedItem() != null) {
			dibujanteComic = nombreDibujante.getSelectionModel().getSelectedItem().toString();
		}

		return dibujanteComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreGuionista" y lo
	 * devuelve, para la busqueda de comic
	 * 
	 * @return
	 */
	public String guionistaActual() {
		String guionistaComic = "";

		if (nombreGuionista.getSelectionModel().getSelectedItem() != null) {
			guionistaComic = nombreGuionista.getSelectionModel().getSelectedItem().toString();
		}

		return guionistaComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreFirma" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String firmaActual() {
		String firmaComic = "";

		if (nombreFirma.getSelectionModel().getSelectedItem() != null) {
			firmaComic = nombreFirma.getSelectionModel().getSelectedItem().toString();
		}

		return firmaComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreComic" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String nombreActual() {
		String nombreComics = "";

		if (nombreComic.getSelectionModel().getSelectedItem() != null) {
			nombreComics = nombreComic.getSelectionModel().getSelectedItem().toString();
		}

		return nombreComics;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "numeroComic" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String numeroComicActual() {
		String numComic = "";

		if (numeroComic.getSelectionModel().getSelectedItem() != null) {
			numComic = numeroComic.getSelectionModel().getSelectedItem().toString();
		}

		return numComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "numeroComic" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String numeroComicNuevo() {
		String numComic = "0";

		if (numeroAni.getSelectionModel().getSelectedItem() != null) {
			numComic = numeroAni.getSelectionModel().getSelectedItem().toString();
		}

		return numComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreVariante" y lo
	 * devuelve, para la busqueda de comic
	 * 
	 * @return
	 */
	public String varianteActual() {
		String varianteComics = "";

		if (nombreVariante.getSelectionModel().getSelectedItem() != null) {
			varianteComics = nombreVariante.getSelectionModel().getSelectedItem().toString();
		}

		return varianteComics;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String cajaActual() {

		String cajaComics = "";

		if (numeroCaja.getSelectionModel().getSelectedItem() != null) {
			cajaComics = numeroCaja.getSelectionModel().getSelectedItem().toString();
		}

		return cajaComics;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String cajaNueva() {

		String cajaComics = "0";

		if (numeroCajaAni.getSelectionModel().getSelectedItem() != null) {
			cajaComics = numeroCajaAni.getSelectionModel().getSelectedItem().toString();
		}

		return cajaComics;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String estadoNuevo() {

		String estadoNuevo = estadoComic.getSelectionModel().getSelectedItem().toString();

		return estadoNuevo;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreFormato" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String formatoNuevo() {

		String formatoEstado = formatoAni.getSelectionModel().getSelectedItem().toString();

		return formatoEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaNueva() {

		String procedenciaEstadoNuevo = procedenciaAni.getSelectionModel().getSelectedItem().toString();

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
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.modificarColumnas(tablaBBDD,columnList);
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
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
		funcionesTabla.modificarColumnas(tablaBBDD,columnList);
		limpiezaDatosAni();
		limpiezaDatos();
		tablaBBDD.refresh();
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
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

		Comic comic = new Comic("", datos[1], datos[11], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7],
				datos[8], datos[9], datos[10], "", "", "", null,"","");

		funcionesTabla.tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()), tablaBBDD, columnList); 
		prontInfo.setOpacity(1);
		prontInfo.setText(funcionesTabla.resultadoBusquedaPront(comic).getText());
		busquedaGeneral.setText("");
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
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + DBManager.DB_NAME
				+ File.separator + "portadas";
		Utilidades.convertirNombresCarpetas(sourcePath);

		if (nav.alertaInsertar()) {

			String datos[] = camposComicIntroducir();

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

			Comic comic = new Comic("", nombre, numCaja, numero, variante, firma, editorial, formato, procedencia,
					fecha_comic.toString(), guionista, dibujante, estado, key_issue, "Sin puntuar", portada,
					url_referencia, precio_comic);

			if (nombre.isEmpty() || numero.isEmpty() || editorial.isEmpty() || guionista.isEmpty()
					|| dibujante.isEmpty()) {
				String excepcion = "No puedes introducir un comic si no has completado todos los datos";

				validateComicFields(comic);

				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("Error. Debes de introducir los datos correctos.");

				nav.alertaException(excepcion);
			} else {

				String codigo_imagen = Utilidades.generarCodigoUnico(sourcePath + File.separator);

				utilidad.nueva_imagen(portada, codigo_imagen);
				comic.setImagen(sourcePath + File.separator + codigo_imagen + ".jpg");
				libreria.insertarDatos(comic);

				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo.setText(
						"Has introducido correctamente: \n" + comic.toString().replace("[", "").replace("]", ""));
				libreria.listasAutoCompletado();
				funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
				funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
			}
		} else {
			borrarErrores();
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Se ha cancelado la subida del nuevo comic.");
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
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public String[] camposComicActuales() {
		String campos[] = new String[12];

		if (nombreActual().isEmpty()) {
			campos[1] = "";
		} else {
			campos[1] = nombreActual();
		}

		if (numeroComicActual().isEmpty()) {
			campos[2] = "";
		} else {
			campos[2] = numeroComicActual();
		}

		if (varianteActual().isEmpty()) {
			campos[3] = "";
		} else {
			campos[3] = varianteActual();
		}

		if (firmaActual().isEmpty()) {
			campos[4] = "";
		} else {
			campos[4] = firmaActual();
		}

		if (editorialActual().isEmpty()) {
			campos[5] = "";
		} else {
			campos[5] = editorialActual();
		}

		if (formatoActual().isEmpty()) {
			campos[6] = "";
		} else {
			campos[6] = formatoActual();
		}

		if (procedenciaActual().isEmpty()) {
			campos[7] = "";
		} else {
			campos[7] = procedenciaActual();
		}
		LocalDate fecha = fechaPublicacion.getValue();
		if (fecha != null) {
			campos[8] = fecha.toString();
		} else {
			campos[8] = "";
		}

		if (guionistaActual().isEmpty()) {
			campos[9] = "";
		} else {
			campos[9] = guionistaActual();
		}

		if (dibujanteActual().isEmpty()) {
			campos[10] = "";
		} else {
			campos[10] = dibujanteActual();
		}

		if (cajaActual().isEmpty() || cajaActual().equals("0")) {
			campos[11] = "";
		} else {
			campos[11] = cajaActual();
		}

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

		String campos[] = new String[16];

		campos[0] = utilidad.comaPorGuion(nombreAni.getText());

		campos[1] = numeroComicNuevo();

		campos[2] = utilidad.comaPorGuion(varianteAni.getText());

		campos[3] = utilidad.comaPorGuion(firmaAni.getText());

		campos[4] = editorialAni.getText();

		campos[5] = formatoNuevo();

		campos[6] = procedenciaNueva();

		LocalDate fecha = fechaAni.getValue();
		if (fecha != null) {
			campos[7] = fecha.toString();
		} else {
			campos[7] = "2000-01-01";
		}

		campos[8] = utilidad.comaPorGuion(guionistaAni.getText());

		campos[9] = utilidad.comaPorGuion(dibujanteAni.getText());

		campos[10] = direccionImagen.getText();

		campos[11] = estadoNuevo();

		campos[12] = cajaNueva();

		campos[13] = nombreKeyIssue.getText();

		campos[14] = urlReferencia.getText();

		campos[15] = precioComic.getText();

		return campos;
	}

	@FXML
	void comicsKeyIssue(ActionEvent event) throws SQLException {
		prontInfo.setOpacity(0);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaKeyIssue(), tablaBBDD, columnList); // Llamada a funcion
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
		prontInfo.setOpacity(0);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaPuntuacion(), tablaBBDD, columnList); // Llamada a funcion

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
		prontInfo.setOpacity(0);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaVendidos(), tablaBBDD, columnList); // Llamada a funcion
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
		prontInfo.setOpacity(0);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaFirmados(), tablaBBDD, columnList); // Llamada a funcion
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
		prontInfo.setOpacity(0);
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaComprados(), tablaBBDD, columnList); // Llamada a funcion
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
		prontInfo.setOpacity(0);
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaPosesion(), tablaBBDD, columnList); // Llamada a funcion
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
		prontInfo.setText("Generando fichero de estadisticas . . . ");
		libreria.generar_fichero_estadisticas();
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