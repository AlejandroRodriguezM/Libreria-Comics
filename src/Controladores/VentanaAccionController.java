package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.controlsfx.control.textfield.TextFields;
import org.json.JSONException;

import Apis.ApiMarvel;
import Funcionamiento.Comic;
import Funcionamiento.FuncionesComboBox;
import Funcionamiento.FuncionesTableView;
import Funcionamiento.FuncionesTooltips;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;

public class VentanaAccionController implements Initializable {

	@FXML
	private TextField direccionImagen;

	@FXML
	private TableColumn<Comic, String> nombre;

	@FXML
	private TableColumn<Comic, String> numero;

	@FXML
	private TableColumn<Comic, String> variante;

	@FXML
	private TableColumn<Comic, String> editorial;

	@FXML
	private TableColumn<Comic, String> guionista;

	@FXML
	private TableColumn<Comic, String> dibujante;

	@FXML
	private Button botonAgregarPuntuacion;

	@FXML
	private Button botonBorrarOpinion;

	@FXML
	private Button botonBusquedaCodigo;

	@FXML
	private Button botonBusquedaAvanzada;

	@FXML
	private Button botonEliminar;

	@FXML
	private Button botonIntroducir;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonModificar;

	@FXML
	private Button botonSubidaPortada;

	@FXML
	private Button botonVender;
	
    @FXML
    private Button botonbbdd;

	// Campos de texto (TextField)
	@FXML
	private TextField busquedaCodigo;

	@FXML
	private TextField dibujanteComic;

	@FXML
	private TextField editorialComic;

	@FXML
	private TextField firmaComic;

	@FXML
	private TextField guionistaComic;

	@FXML
	private TextField idComicTratar;

	@FXML
	private TextField nombreComic;

	@FXML
	private TextField nombreKeyIssue;

	@FXML
	private TextField precioComic;

	@FXML
	private TextField urlReferencia;

	@FXML
	private TextField varianteComic;

	// Etiquetas (Label)
	@FXML
	private Label labelPuntuacion;

	@FXML
	private Label label_busquedaCodigo;

	@FXML
	private Label label_caja;

	@FXML
	private Label label_dibujante;

	@FXML
	private Label label_editorial;

	@FXML
	private Label label_estado;

	@FXML
	private Label label_fecha;

	@FXML
	private Label label_firma;

	@FXML
	private Label label_formato;

	@FXML
	private Label label_guionista;

	@FXML
	private Label label_id;

	@FXML
	private Label label_key;

	@FXML
	private Label label_portada;

	@FXML
	private Label label_precio;

	@FXML
	private Label label_procedencia;

	@FXML
	private Label label_referencia;

	// Otros controles (ComboBox, DatePicker, TableView, etc.)
	@FXML
	private ComboBox<String> estadoComic;

	@FXML
	private DatePicker fechaComic;

	@FXML
	private ComboBox<String> formatoComic;

	@FXML
	private ComboBox<String> busquedaEditorial;

	@FXML
	private ComboBox<String> numeroCajaComic;

	@FXML
	private ComboBox<String> numeroComic;

	@FXML
	private ComboBox<String> procedenciaComic;

	@FXML
	private ComboBox<String> puntuacionMenu;

	@FXML
	private TableView<Comic> tablaBBDD;

	@FXML
	private ImageView imagenFondo;

	@FXML
	private ImageView imagencomic;

	@FXML
	private TextArea prontInfo;

	@FXML
	private VBox rootVBox;

	private List<TableColumn<Comic, String>> columnList;
	private Timeline timeline;
	private Stage stage; // Add this field to store the reference to the stage
	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static Utilidades utilidad = null;
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();
	private static FuncionesTableView funcionesTabla = new FuncionesTableView();
	private static String TIPO_ACCION;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(() -> {

			listas_autocompletado();

			rellenarCombosEstaticos();

			if ("aniadir".equals(TIPO_ACCION)) {
				mostrarOpcionAniadir();
			} else if ("eliminar".equals(TIPO_ACCION)) {
				rootVBox.toFront();
				mostrarOpcionEliminar();
			} else if ("modificar".equals(TIPO_ACCION)) {
				rootVBox.setLayoutY(422.0); // Cambia 422.0 al valor que desees

				rootVBox.toFront();
				mostrarOpcionModificar();
			} else if ("puntuar".equals(TIPO_ACCION)) {
				rootVBox.toFront();
				mostrarOpcionPuntuar();
			} else {
				closeWindow();
			}
		});

		Platform.runLater(() -> asignarTooltips());

		numeroComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroCajaComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		idComicTratar.setTextFormatter(FuncionesComboBox.validador_Nenteros());
		precioComic.setTextFormatter(FuncionesComboBox.validador_Ndecimales());

		FuncionesTableView.restringirSimbolos(guionistaComic);
		FuncionesTableView.restringirSimbolos(dibujanteComic);
		FuncionesTableView.restringirSimbolos(varianteComic);

		List<TableColumn<Comic, String>> columnListCarga = Arrays.asList(nombre, numero, variante, editorial, guionista,
				dibujante);
		columnList = columnListCarga;

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

		String[] editorialBusquedas = { "Marvel ISBN", "Marvel UPC", "Otras editoriales", "Ninguno" };

		busquedaEditorial.setItems(FXCollections.observableArrayList(editorialBusquedas));
		
		ObservableList<String> puntuaciones = FXCollections.observableArrayList("0/0", "0.5/5", "1/5", "1.5/5", "2/5",
				"2.5/5", "3/5", "3.5/5", "4/5", "4.5/5", "5/5");
		puntuacionMenu.setItems(puntuaciones);
		puntuacionMenu.getSelectionModel().selectFirst();
	}

	@FXML
	void verTodabbdd() {

		rootVBox.setVisible(true);
		rootVBox.setDisable(false);

		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		tablaBBDD.refresh();
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);

		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
	}

	/**
	 * Asigna tooltips a varios elementos en la interfaz gráfica. Estos tooltips
	 * proporcionan información adicional cuando el usuario pasa el ratón sobre los
	 * elementos.
	 */
	public void asignarTooltips() {
		List<Object> elementos = new ArrayList<>();

		// Agregar elementos a la lista para los cuales se asignarán tooltips
		elementos.add(botonLimpiar);
		elementos.add(nombreComic);
		elementos.add(numeroComic);
		elementos.add(firmaComic);
		elementos.add(guionistaComic);
		elementos.add(varianteComic);
		elementos.add(numeroCajaComic);
		elementos.add(procedenciaComic);
		elementos.add(formatoComic);
		elementos.add(editorialComic);
		elementos.add(dibujanteComic);

		// Llamar a la función para asignar tooltips a los elementos de la lista
		FuncionesTooltips.asignarTooltips(elementos);
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

			idComicTratar.setStyle("");
			idComicTratar.setText(id_comic);

			Comic comic_temp = libreria.comicDatos(id_comic);

			nombreComic.setText(comic_temp.getNombre());

			String numeroNuevo = comic_temp.getNumero();
			numeroComic.getSelectionModel().select(numeroNuevo);

			varianteComic.setText(comic_temp.getVariante());

			firmaComic.setText(comic_temp.getFirma());

			editorialComic.setText(comic_temp.getEditorial());

			String formato = comic_temp.getFormato();
			formatoComic.getSelectionModel().select(formato);

			String procedencia = comic_temp.getProcedencia();
			procedenciaComic.getSelectionModel().select(procedencia);

			String fechaString = comic_temp.getFecha();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate fecha = LocalDate.parse(fechaString, formatter);
			fechaComic.setValue(fecha);

			guionistaComic.setText(comic_temp.getGuionista());

			dibujanteComic.setText(comic_temp.getDibujante());

			String cajaAni = comic_temp.getNumCaja();
			numeroCajaComic.getSelectionModel().select(cajaAni);

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

	/**
	 * Funcion que permite mostrar la imagen de portada cuando usas las teclas de
	 * direccion en una tabla.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void teclasDireccion(KeyEvent event) throws IOException, SQLException {
		if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
			libreria = new DBLibreriaManager();
			libreria.libreriaCompleta();
			String id_comic;

			Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

			if (idRow != null) {
				id_comic = idRow.getID();

				idComicTratar.setStyle("");
				idComicTratar.setText(id_comic);

				Comic comic_temp = libreria.comicDatos(id_comic);

				nombreComic.setText(comic_temp.getNombre());

				String numeroNuevo = comic_temp.getNumero();
				numeroComic.getSelectionModel().select(numeroNuevo);

				varianteComic.setText(comic_temp.getVariante());

				firmaComic.setText(comic_temp.getFirma());

				editorialComic.setText(comic_temp.getEditorial());

				String formato = comic_temp.getFormato();
				formatoComic.getSelectionModel().select(formato);

				String procedencia = comic_temp.getProcedencia();
				procedenciaComic.getSelectionModel().select(procedencia);

				String fechaString = comic_temp.getFecha();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				LocalDate fecha = LocalDate.parse(fechaString, formatter);
				fechaComic.setValue(fecha);

				guionistaComic.setText(comic_temp.getGuionista());

				dibujanteComic.setText(comic_temp.getDibujante());

				String cajaAni = comic_temp.getNumCaja();
				numeroCajaComic.getSelectionModel().select(cajaAni);

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
	 * Asigna autocompletado a campos de texto en la interfaz.
	 */
	public void listas_autocompletado() {
		TextFields.bindAutoCompletion(nombreComic, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(varianteComic, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(firmaComic, DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(editorialComic, DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(guionistaComic, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(dibujanteComic, DBLibreriaManager.listaDibujante);
		TextFields.bindAutoCompletion(numeroComic.getEditor(), DBLibreriaManager.listaNumeroComic);
	}

	/**
	 * Rellena los combos estáticos en la interfaz. Esta función llena los
	 * ComboBoxes con opciones estáticas predefinidas.
	 */
	public void rellenarCombosEstaticos() {
		List<ComboBox<String>> comboboxesMod = Arrays.asList(formatoComic, procedenciaComic, estadoComic);
		funcionesCombo.rellenarComboBoxEstaticos(comboboxesMod); // Llamada a la función para rellenar ComboBoxes
	}

	public void ocultarCampos() {

		if (tablaBBDD.isVisible()) {
			tablaBBDD.setVisible(false);
			tablaBBDD.setDisable(true);
		}
		// Verifica si los campos y elementos son visibles antes de ocultarlos y
		// deshabilitarlos
		if (dibujanteComic.isVisible()) {
			dibujanteComic.setVisible(false);
			dibujanteComic.setDisable(true);
		}

		if (editorialComic.isVisible()) {
			editorialComic.setVisible(false);
			editorialComic.setDisable(true);
		}

		if (estadoComic.isVisible()) {
			estadoComic.setVisible(false);
			estadoComic.setDisable(true);
		}

		if (fechaComic.isVisible()) {
			fechaComic.setVisible(false);
			fechaComic.setDisable(true);
		}

		if (firmaComic.isVisible()) {
			firmaComic.setVisible(false);
			firmaComic.setDisable(true);
		}

		if (formatoComic.isVisible()) {
			formatoComic.setVisible(false);
			formatoComic.setDisable(true);
		}

		if (guionistaComic.isVisible()) {
			guionistaComic.setVisible(false);
			guionistaComic.setDisable(true);
		}

		if (nombreKeyIssue.isVisible()) {
			nombreKeyIssue.setVisible(false);
			nombreKeyIssue.setDisable(true);
		}

		if (numeroCajaComic.isVisible()) {
			numeroCajaComic.setVisible(false);
			numeroCajaComic.setDisable(true);
		}

		if (procedenciaComic.isVisible()) {
			procedenciaComic.setVisible(false);
			procedenciaComic.setDisable(true);
		}

		if (urlReferencia.isVisible()) {
			urlReferencia.setVisible(false);
			urlReferencia.setDisable(true);
		}

		// Agrega aquí la lógica de ocultar los elementos que mencionaste en
		// ocultarCampos()
		if (botonBorrarOpinion.isVisible()) {
			botonBorrarOpinion.setVisible(false);
			botonBorrarOpinion.setDisable(true);
		}

		if (puntuacionMenu.isVisible()) {
			puntuacionMenu.setVisible(false);
			puntuacionMenu.setDisable(true);
		}

		if (labelPuntuacion.isVisible()) {
			labelPuntuacion.setVisible(false);
			labelPuntuacion.setDisable(true);
		}

		if (botonAgregarPuntuacion.isVisible()) {
			botonAgregarPuntuacion.setVisible(false);
			botonAgregarPuntuacion.setDisable(true);
		}

		if (label_id.isVisible()) {
			label_id.setVisible(false);
			label_id.setDisable(true);
		}

		if (botonVender.isVisible()) {
			botonVender.setVisible(false);
			botonVender.setDisable(true);
		}

		if (botonEliminar.isVisible()) {
			botonEliminar.setVisible(false);
			botonEliminar.setDisable(true);
		}

		if (idComicTratar.isVisible()) {
			idComicTratar.setVisible(false);
			idComicTratar.setDisable(true);
		}

		if (botonModificar.isVisible()) {
			botonModificar.setVisible(false);
			botonModificar.setDisable(true);
		}

		if (botonBusquedaCodigo.isVisible()) {
			botonBusquedaCodigo.setVisible(false);
			botonBusquedaCodigo.setDisable(true);
		}

		if (botonIntroducir.isVisible()) {
			botonIntroducir.setVisible(false);
			botonIntroducir.setDisable(true);
		}
		
		if(botonbbdd.isVisible()){
			botonbbdd.setVisible(false);
			botonbbdd.setDisable(true);
		}

		if (precioComic.isVisible()) {
			precioComic.setVisible(false);
			precioComic.setDisable(true);
		}

		if (direccionImagen.isVisible()) {
			direccionImagen.setVisible(false);
			direccionImagen.setDisable(true);
		}

		if (label_portada.isVisible()) {
			label_portada.setVisible(false);
		}

		if (label_precio.isVisible()) {
			label_precio.setVisible(false);
		}

		if (label_caja.isVisible()) {
			label_caja.setVisible(false);
		}

		if (label_dibujante.isVisible()) {
			label_dibujante.setVisible(false);
		}

		if (label_editorial.isVisible()) {
			label_editorial.setVisible(false);
		}

		if (label_estado.isVisible()) {
			label_estado.setVisible(false);
		}

		if (label_fecha.isVisible()) {
			label_fecha.setVisible(false);
		}

		if (label_firma.isVisible()) {
			label_firma.setVisible(false);
		}

		if (label_formato.isVisible()) {
			label_formato.setVisible(false);
		}

		if (label_guionista.isVisible()) {
			label_guionista.setVisible(false);
		}

		if (label_key.isVisible()) {
			label_key.setVisible(false);
		}

		if (label_procedencia.isVisible()) {
			label_procedencia.setVisible(false);
		}

		if (label_referencia.isVisible()) {
			label_referencia.setVisible(false);
		}
	}

	public void mostrarOpcionEliminar() {
		ocultarCampos();

		label_id.setVisible(true);
		botonVender.setVisible(true);
		botonEliminar.setVisible(true);
		idComicTratar.setVisible(true);

		tablaBBDD.setVisible(true);
		tablaBBDD.setDisable(false);

		label_id.setDisable(false);
		botonVender.setDisable(false);
		botonEliminar.setDisable(false);
		idComicTratar.setDisable(false);
		
		botonbbdd.setVisible(true);
		botonbbdd.setDisable(false);
		
		rootVBox.setDisable(false);
		tablaBBDD.setDisable(false);
	}

	public void mostrarOpcionAniadir() {
		ocultarCampos();

		// Mostrar los elementos que fueron ocultados en ocultarCampos()
		dibujanteComic.setVisible(true);
		dibujanteComic.setDisable(false);

		editorialComic.setVisible(true);
		editorialComic.setDisable(false);

		estadoComic.setVisible(true);
		estadoComic.setDisable(false);

		fechaComic.setVisible(true);
		fechaComic.setDisable(false);

		firmaComic.setVisible(true);
		firmaComic.setDisable(false);

		formatoComic.setVisible(true);
		formatoComic.setDisable(false);

		guionistaComic.setVisible(true);
		guionistaComic.setDisable(false);

		nombreKeyIssue.setVisible(true);
		nombreKeyIssue.setDisable(false);

		numeroCajaComic.setVisible(true);
		numeroCajaComic.setDisable(false);

		procedenciaComic.setVisible(true);
		procedenciaComic.setDisable(false);

		urlReferencia.setVisible(true);
		urlReferencia.setDisable(false);

		// Mostrar los elementos que mencionaste en mostrarOpcionAniadir()
		botonIntroducir.setVisible(true);
		botonBusquedaAvanzada.setVisible(true);

		botonIntroducir.setDisable(false);
		botonBusquedaAvanzada.setDisable(false);

		precioComic.setVisible(true);
		direccionImagen.setVisible(true);

		precioComic.setDisable(false);
		direccionImagen.setDisable(false);

		label_portada.setVisible(true);

		label_precio.setVisible(true);
		label_caja.setVisible(true);
		label_dibujante.setVisible(true);
		label_editorial.setVisible(true);
		label_estado.setVisible(true);
		label_fecha.setVisible(true);
		label_firma.setVisible(true);
		label_formato.setVisible(true);
		label_guionista.setVisible(true);
		label_key.setVisible(true);
		label_procedencia.setVisible(true);
		label_referencia.setVisible(true);

		botonSubidaPortada.setVisible(true);
		botonSubidaPortada.setDisable(false);

	}

	public void mostrarOpcionModificar() {
		ocultarCampos();

		dibujanteComic.setVisible(true);
		dibujanteComic.setDisable(false);

		editorialComic.setVisible(true);
		editorialComic.setDisable(false);

		estadoComic.setVisible(true);
		estadoComic.setDisable(false);

		fechaComic.setVisible(true);
		fechaComic.setDisable(false);

		firmaComic.setVisible(true);
		firmaComic.setDisable(false);

		formatoComic.setVisible(true);
		formatoComic.setDisable(false);

		guionistaComic.setVisible(true);
		guionistaComic.setDisable(false);

		nombreKeyIssue.setVisible(true);
		nombreKeyIssue.setDisable(false);

		numeroCajaComic.setVisible(true);
		numeroCajaComic.setDisable(false);

		procedenciaComic.setVisible(true);
		procedenciaComic.setDisable(false);

		urlReferencia.setVisible(true);
		urlReferencia.setDisable(false);

		botonModificar.setVisible(true);

		botonModificar.setDisable(false);

		precioComic.setVisible(true);
		direccionImagen.setVisible(true);

		precioComic.setDisable(false);
		direccionImagen.setDisable(false);

		tablaBBDD.setVisible(true);
		tablaBBDD.setDisable(false);

		label_portada.setVisible(true);
		label_precio.setVisible(true);
		label_caja.setVisible(true);
		label_dibujante.setVisible(true);
		label_editorial.setVisible(true);
		label_estado.setVisible(true);
		label_fecha.setVisible(true);
		label_firma.setVisible(true);
		label_formato.setVisible(true);
		label_guionista.setVisible(true);
		label_key.setVisible(true);
		label_procedencia.setVisible(true);
		label_referencia.setVisible(true);

		botonSubidaPortada.setVisible(true);
		botonSubidaPortada.setDisable(false);
		
		botonbbdd.setVisible(true);
		botonbbdd.setDisable(false);
		
		rootVBox.setDisable(false);
		tablaBBDD.setDisable(false);

	}

	public void mostrarOpcionPuntuar() {
		ocultarCampos();

		botonBorrarOpinion.setVisible(true);
		puntuacionMenu.setVisible(true);
		labelPuntuacion.setVisible(true);
		botonAgregarPuntuacion.setVisible(true);
		idComicTratar.setVisible(true);
		label_id.setVisible(true);

		botonBorrarOpinion.setDisable(false);
		puntuacionMenu.setDisable(false);
		botonAgregarPuntuacion.setDisable(false);
		idComicTratar.setDisable(false);

		tablaBBDD.setVisible(true);
		tablaBBDD.setDisable(false);
		
		botonbbdd.setVisible(true);
		botonbbdd.setDisable(false);
		
		rootVBox.setDisable(false);
		tablaBBDD.setDisable(false);
	}

	public static void tipoAccion(String tipoAccion) {
		TIPO_ACCION = tipoAccion;
	}

	@FXML
	public void agregarDatos(ActionEvent event) throws IOException, SQLException {

		libreria = new DBLibreriaManager();
		subidaComic();
		libreria.reiniciarBBDD();
		direccionImagen.setText("");
	}

	/**
	 * Funcion que permite que al pulsar el boton 'botonOpinion' se modifique el
	 * dato "puntuacion" de un comic en concreto usando su ID"
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void agregarPuntuacion(ActionEvent event) throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");
		if (id_comic.length() == 0 || !libreria.checkID(id_comic)) {

			idComicTratar.setStyle("-fx-background-color: #FF0000;");
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {
			libreria.actualizarPuntuacion(id_comic, comicPuntuacion()); // Llamada a funcion
			prontInfo.setText("Deseo concedido. Has añadido el nuevo comic.");
		}
	}

	/**
	 * Funcion que permite borrar la opinion de un comic
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void borrarPuntuacion(ActionEvent event) throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");

		if (id_comic.length() == 0 || !libreria.checkID(id_comic)) {
			idComicTratar.setStyle("-fx-background-color: #FF0000;");
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {
			libreria.borrarPuntuacion(id_comic);
			prontInfo.setText("Deseo concedido. Has borrado la puntuacion del comic.");

			Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
			imagenFondo.setImage(nuevaImagen);
		}
	}

	@FXML
	void busquedaAvanzada(ActionEvent event) {

		if (busquedaEditorial.isVisible()) {

			botonBusquedaCodigo.setVisible(false);
			label_busquedaCodigo.setVisible(false);
			busquedaEditorial.setVisible(false);
			busquedaCodigo.setVisible(false);
			botonBusquedaCodigo.setVisible(false);

			botonBusquedaCodigo.setDisable(true);
			label_busquedaCodigo.setDisable(true);
			busquedaEditorial.setDisable(true);
			busquedaCodigo.setDisable(true);
			botonBusquedaCodigo.setDisable(true);

		} else {
			botonBusquedaCodigo.setVisible(true);
			botonBusquedaCodigo.setDisable(false);

			label_busquedaCodigo.setVisible(true);
			busquedaEditorial.setVisible(true);
			busquedaCodigo.setVisible(true);
			botonBusquedaCodigo.setVisible(true);

			label_busquedaCodigo.setDisable(false);
			busquedaEditorial.setDisable(false);
			busquedaCodigo.setDisable(false);
			botonBusquedaCodigo.setDisable(false);
		}
	}

	@FXML
	void busquedaPorCodigo(ActionEvent event) throws IOException, JSONException, URISyntaxException {
		// Crear una tarea que se ejecutará en segundo plano
		Task<Boolean> tarea = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				String valorCodigo = busquedaCodigo.getText();
				String tipoEditorial = busquedaEditorial.getValue();
				String[] comicInfo = null;

				if (!valorCodigo.isEmpty() && !tipoEditorial.isEmpty()) {
					if (tipoEditorial.equalsIgnoreCase("marvel isbn")) {
						comicInfo = ApiMarvel.infoComicIsbn(valorCodigo.trim(), prontInfo);
					} else if (tipoEditorial.equalsIgnoreCase("marvel upc")) {
						comicInfo = ApiMarvel.infoComicUpc(valorCodigo.trim(), prontInfo);

					} else {
						// Hacer algo si el tipo de editorial no es válido
					}

					if (comprobarCodigo(comicInfo)) {
						// Rellenar campos con la información del cómic
						rellenarCamposAni(comicInfo);
						return true;
					}
				}

				return false;
			}
		};

		iniciarAnimacionCambioImagen();

		// Configurar un manejador de eventos para actualizar la interfaz de usuario
		// cuando la tarea esté completa
		tarea.setOnSucceeded(ev -> {

			Platform.runLater(() -> {
				prontInfo.setOpacity(0);
				prontInfo.setText("");
				detenerAnimacionPront();

			});

		});

		tarea.setOnFailed(ev -> {
			Platform.runLater(() -> {
				detenerAnimacionPront();
			});
		});

		// Iniciar la tarea en un nuevo hilo
		Thread thread = new Thread(tarea);
		thread.setDaemon(true); // Hacer que el hilo sea demonio para que se cierre al
								// salir de la aplicación

		// Iniciar la tarea
		thread.start();

	}

	// Método para mostrar un mensaje de error en prontInfo
	private boolean comprobarCodigo(String[] comicInfo) {
		boolean existe = true;
		if (comicInfo == null || comicInfo.length <= 0) {
			if (comicInfo == null || comicInfo.length <= 0) {
				existe = false;
			}
		}
		return existe;
	}

	private void rellenarCamposAni(String[] comicInfo) {

		int i = 0;
		for (String info : comicInfo) {
			System.out.println(i + ": " + info);
			i++;
		}

		Platform.runLater(() -> {
			String titulo = comicInfo[0];
			String numero = comicInfo[1];
			String formato = comicInfo[2];
			String precio = comicInfo[3];
			String variante = comicInfo[4];
			String dibujantes = comicInfo[5];
			String escritores = comicInfo[6];
			String fechaVenta = comicInfo[7];
			String referencia = comicInfo[8];
			String urlImagen = comicInfo[9];
			String editorial = comicInfo[10];

			if (comicInfo.length >= 12) {
				String descripcion = comicInfo[11];
				descripcion = Utilidades.replaceHtmlEntities(descripcion);

				nombreKeyIssue.setText(descripcion);
			}

			nombreComic.setText(titulo);
			numeroComic.setValue(numero);
			varianteComic.setText(variante);
			editorialComic.setText(editorial);
			formatoComic.setValue(formato);

			// Parsear y establecer la fecha
			String fechaString = fechaVenta;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate fecha = LocalDate.parse(fechaString, formatter);
			fechaComic.setValue(fecha);

			guionistaComic.setText(escritores);
			dibujanteComic.setText(dibujantes);
			direccionImagen.setText(urlImagen);
			precioComic.setText(precio);
			urlReferencia.setText(referencia);

			// Cargar la imagen desde la URL
			Image imagen = new Image(urlImagen, true);
			imagencomic.setImage(imagen);
		});
	}

	private void iniciarAnimacionCambioImagen() {

		// Agrega las imágenes que deseas mostrar en cada KeyFrame
		InputStream imagenStream1 = getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg");
		InputStream imagenStream2 = getClass().getResourceAsStream("/imagenes/accionComic.jpg");

		// Convierte las corrientes de entrada en objetos Image
		Image imagen1 = new Image(imagenStream1);
		Image imagen2 = new Image(imagenStream2);

		// Establece la imagen inicial en el ImageView
		imagenFondo.setImage(imagen1);

		// Configura la opacidad inicial
		imagenFondo.setOpacity(1);

		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar la imagen
		KeyFrame cambiarImagen1 = new KeyFrame(Duration.ZERO, new KeyValue(imagenFondo.imageProperty(), imagen1));
		KeyFrame cambiarImagen2 = new KeyFrame(Duration.seconds(0.1),
				new KeyValue(imagenFondo.imageProperty(), imagen2));
		KeyFrame cambiarImagen3 = new KeyFrame(Duration.seconds(0.2),
				new KeyValue(imagenFondo.imageProperty(), imagen1));
		KeyFrame cambiarImagen4 = new KeyFrame(Duration.seconds(0.3),
				new KeyValue(imagenFondo.imageProperty(), imagen2));
		KeyFrame cambiarImagen5 = new KeyFrame(Duration.seconds(0.4),
				new KeyValue(imagenFondo.imageProperty(), imagen1));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(cambiarImagen1, cambiarImagen2, cambiarImagen3, cambiarImagen4, cambiarImagen5);

		// Iniciar la animación
		timeline.play();
	}

	/**
	 * Metodo que permite detener una animacion
	 */
	private void detenerAnimacionPront() {
		if (timeline != null) {
			timeline.stop();
			timeline = null; // Destruir el objeto timeline

			InputStream imagenStream = getClass().getResourceAsStream("/imagenes/accionComic.jpg");
			Image imagen = new Image(imagenStream);

			imagenFondo.setImage(imagen);

		}
	}

	/**
	 * Funcion que elimina un comic de la base de datos.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void eliminarDatos(ActionEvent event) throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");

		if (id_comic.length() == 0 || !libreria.checkID(id_comic)) {

			idComicTratar.setStyle("-fx-background-color: #FF0000;");

			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {
			cambioEstado(id_comic);
			libreria.eliminarComicBBDD(id_comic);
			libreria.reiniciarBBDD();
			libreria.listasAutoCompletado();
			funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
			funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
			
			
			Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
			imagenFondo.setImage(nuevaImagen);
		}
	}


	/**
	 * Funcion que permite cambiar de estado o eliminar un comic de la base de
	 * datos.
	 *
	 * @param id
	 * @param sentenciaSQL
	 * @throws SQLException
	 */
	public boolean cambioEstado(String ID) throws SQLException {
		libreria = new DBLibreriaManager();
		if (nav.alertaEliminar()) {
			if (ID.length() != 0) {

				Comic comic = libreria.comicDatos(ID); // Llamada de metodo que contiene el comic que se desea eliminar

				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo
						.setText("Has borrado/vendido correctamente: " + comic.toString().replace("[", "").replace("]", ""));
				idComicTratar.setStyle(null);
				
				funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
				funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
				
				return true;
			} else {
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("ERROR. ID desconocido.");
				idComicTratar.setStyle("-fx-background-color: red");
				return false;
			}
		} else { // Si se cancela el borra del comic, saltara el siguiente mensaje.
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Modificacion cancelada.");
			return false;
		}
	}

	@FXML
	void limpiarDatos(ActionEvent event) {
		// Campos de datos a modificar en la sección de animaciones

		Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComic.jpg"));
		imagenFondo.setImage(nuevaImagen);

		nombreComic.setText("");
		varianteComic.setText("");
		firmaComic.setText("");
		editorialComic.setText("");
		fechaComic.setValue(null);
		guionistaComic.setText("");
		dibujanteComic.setText("");
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		nombreKeyIssue.setText("");
		borrarErrores();
	}

	/**
	 * Elimina cualquier resaltado de campos en rojo que indique errores.
	 */
	public void borrarErrores() {
		// Restaurar el estilo de fondo de los campos a su estado original
		nombreComic.setStyle("");
		numeroComic.setStyle("");
		editorialComic.setStyle("");
		guionistaComic.setStyle("");
		dibujanteComic.setStyle("");
	}

	/**
	 * Llamada a funcion que modifica los datos de 1 comic en la base de datos.
	 *
	 * @param event
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	@FXML
	void modificarDatos(ActionEvent event) throws NumberFormatException, SQLException, IOException {

		libreria = new DBLibreriaManager();
		modificacionComic(); // Llamada a funcion que modificara el contenido de un comic especifico.
		libreria.reiniciarBBDD();
		direccionImagen.setText("");
		libreria.listasAutoCompletado();
	}

	/**
	 * Funcion que permite la subida de una
	 *
	 * @param event
	 */
	@FXML
	void nuevaPortada(ActionEvent event) {
		subirPortada();
	}

	/**
	 * Metodo que permite cambiar de estado un comic, para que se deje de mostrar en
	 * el programa, pero este sigue estando dentro de la bbdd
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void ventaComic(ActionEvent event) throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");

		if (id_comic.length() == 0 || !libreria.checkID(id_comic)) {
			idComicTratar.setStyle("-fx-background-color: #FF0000;");
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {
			cambioEstado(id_comic);
			libreria.venderComicBBDD(id_comic);
			libreria.reiniciarBBDD();

			prontInfo.setText("Deseo concedido. Has puesto a la venta el comic");

			Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
			imagenFondo.setImage(nuevaImagen);
		}
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
	 * Funcion que permite modificar un comic, segun los datos introducidos
	 * 
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	public void modificacionComic() throws NumberFormatException, SQLException, IOException {
		libreria = new DBLibreriaManager();
		Comic comic_temp = new Comic();
		Image imagen = null;
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + DBManager.DB_NAME
				+ File.separator + "portadas";
		Utilidades.convertirNombresCarpetas(sourcePath);

		if (nav.alertaModificar()) {

			String datos[] = camposComic();

			String id_comic = idComicTratar.getText();
			
			System.out.println(id_comic);

			if (libreria.checkID(id_comic)) {
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

				if (datos[10].isEmpty()) {
					portada = comic_temp.getImagen();
					imagen = new Image(portada);
				} else {
					portada = datos[10];
					imagen = new Image(portada);
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

				Comic comic = new Comic(id_comic, nombre, numCaja, numero, variante, firma, editorial, formato,
						procedencia, fecha, guionista, dibujante, estado, nombreKeyIssue, puntuacion, portada,
						url_referencia, precio_comic);

				if (id_comic.length() == 0 || !libreria.checkID(id_comic) || nombre.length() == 0
						|| numero.length() == 0 || editorial.length() == 0 || guionista.length() == 0
						|| dibujante.length() == 0 || procedencia.length() == 0) {

					String excepcion = "ERROR.Faltan datos por rellenar";
					nav.alertaException(excepcion);
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #F53636");
					prontInfo.setText("Error. Debes de introducir los datos correctos");
				} else {
					libreria.actualizarComic(comic);
					Utilidades.eliminarFichero(comic_temp.getImagen());

					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #A0F52D");
					prontInfo.setText(
							"Has modificado correctamente: " + comic.toString().replace("[", "").replace("]", ""));
					libreria.listasAutoCompletado();
					funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
					funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
					
					imagencomic.setImage(imagen);

					Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
					imagenFondo.setImage(nuevaImagen);
				}
			} else {
				String excepcion = "No puedes modificar un comic si antes no pones un ID valido";
				nav.alertaException(excepcion);
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("Error. Debes de introducir un ID valido");
			}
		} else {
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Se ha cancelado la modificacion del comic.");
		}
	}

	/**
	 * Funcion que permite modificar la puntuacion de un comic, siempre y cuando el
	 * ID exista en la base de datos
	 *
	 * @param ps
	 * @return
	 */
	public String comicPuntuacion() {

		String puntuacion = puntuacionMenu.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
																								// "puntuacion"
		return puntuacion;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "numeroComic" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String numero() {
		String numComic = "0";

		if (numeroComic.getSelectionModel().getSelectedItem() != null) {
			numComic = numeroComic.getSelectionModel().getSelectedItem().toString();
		}

		return numComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String caja() {

		String cajaComics = "0";

		if (numeroCajaComic.getSelectionModel().getSelectedItem() != null) {
			cajaComics = numeroCajaComic.getSelectionModel().getSelectedItem().toString();
		}

		return cajaComics;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String estado() {

		String estadoNuevo = estadoComic.getSelectionModel().getSelectedItem().toString();

		return estadoNuevo;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreFormato" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String formato() {

		String formatoEstado = formatoComic.getSelectionModel().getSelectedItem().toString();

		return formatoEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedencia() {

		String procedenciaEstadoNuevo = procedenciaComic.getSelectionModel().getSelectedItem().toString();

		return procedenciaEstadoNuevo;
	}

	/**
	 * Funcion que devuelve un array con los datos de los TextField del comic a
	 * introducir.
	 *
	 * @return
	 */
	public String[] camposComic() {

		utilidad = new Utilidades();

		String campos[] = new String[16];

		campos[0] = utilidad.comaPorGuion(nombreComic.getText());

		campos[1] = numero();

		campos[2] = utilidad.comaPorGuion(varianteComic.getText());

		campos[3] = utilidad.comaPorGuion(firmaComic.getText());

		campos[4] = editorialComic.getText();

		campos[5] = formato();

		campos[6] = procedencia();

		LocalDate fecha = fechaComic.getValue();
		if (fecha != null) {
			campos[7] = fecha.toString();
		} else {
			campos[7] = "2000-01-01";
		}

		campos[8] = utilidad.comaPorGuion(guionistaComic.getText());

		campos[9] = utilidad.comaPorGuion(dibujanteComic.getText());

		campos[10] = direccionImagen.getText();

		campos[11] = estado();

		campos[12] = caja();

		campos[13] = nombreKeyIssue.getText();

		campos[14] = urlReferencia.getText();

		campos[15] = precioComic.getText();

		return campos;
	}

	/**
	 * Valida los campos del cómic y resalta en rojo aquellos que estén vacíos.
	 *
	 * @param comic El cómic a validar.
	 */
	public void validateComicFields(Comic comic) {
		if (comic.getNombre().length() == 0) {
			nombreComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo nombre
		}

		if (comic.getNumero().length() == 0) {
			numeroComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo número
		}

		if (comic.getEditorial().length() == 0) {
			editorialComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo editorial
		}

		if (comic.getGuionista().length() == 0) {
			guionistaComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo guionista
		}

		if (comic.getDibujante().length() == 0) {
			dibujanteComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo dibujante
		}
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
		new MenuPrincipalController();
		File file;
		LocalDate fecha_comic;

		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + DBManager.DB_NAME
				+ File.separator + "portadas";
		Utilidades.convertirNombresCarpetas(sourcePath);

		if (nav.alertaInsertar()) {

			String datos[] = camposComic();

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

				Image imagen = null;

				if (Utilidades.isURL(datos[10])) {
					// Es una URL en internet
					portada = Utilidades.descargarImagen(datos[10], documentsPath);
				} else {

					file = new File(datos[10]);
					if (!file.exists()) {
						portada = "Funcionamiento/sinPortada.jpg";
						imagen = new Image(portada);

					} else {
						portada = datos[10];
						imagen = new Image(portada);
					}
				}

				imagencomic.setImage(imagen);
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

				if (Utilidades.isURL(datos[10])) {
					Utilidades.borrarImagen(portada);
				}

				Image imagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
				imagenFondo.setImage(imagen);
			}
		} else {
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Se ha cancelado la subida del nuevo comic.");
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void closeWindow() {
		if (stage != null) {
			stage.close();
		}
	}

}
