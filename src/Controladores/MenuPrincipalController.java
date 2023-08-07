package Controladores;

import java.io.BufferedReader;

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
 *  Por Alejandro Rodriguez
 *
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import Funcionamiento.Comic;
import Funcionamiento.FuncionesExcel;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

/**
 * Esta clase sirve viajar a las diferentes ventanas del programa, asi como
 * realizar diferentes diferentes funciones
 *
 * @author Alejandro Rodriguez
 */
public class MenuPrincipalController implements Initializable {

	@FXML
	private MenuItem menu_archivo_desconectar;

	@FXML
	private MenuItem menu_archivo_sobreMi;

	@FXML
	private MenuItem menu_archivo_backupbbdd;

	@FXML
	private MenuItem menu_archivo_cerrar;

	@FXML
	private MenuItem menu_archivo_delete;

	@FXML
	private MenuItem menu_archivo_excel;

	@FXML
	private MenuItem menu_archivo_importar;

	@FXML
	private MenuItem menu_comic_aniadir;

	@FXML
	private MenuItem menu_comic_eliminar;

	@FXML
	private MenuItem menu_comic_aleatoria;

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
	private Button botonbbdd;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonFrase;

	@FXML
	private DatePicker fechaPublicacion;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TableColumn<Comic, String> ID;

	@FXML
	private TableColumn<Comic, String> caja;

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
	private TableColumn<Comic, String> numero;

	@FXML
	private TableColumn<Comic, String> procedencia;

	@FXML
	private TableColumn<Comic, String> variante;

	@FXML
	private TableColumn<Comic, String> referencia;

	@FXML
	public TableView<Comic> tablaBBDD;

	@FXML
	private TextArea prontInfo;

	@FXML
	private TextArea prontFrases;

	@FXML
	private ImageView imagencomic;

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
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private VBox rootVBox;

	@FXML
	private VBox vboxContenido;

	@FXML
	private VBox vboxFrases;

	@FXML
	private ProgressIndicator progresoCarga;

	private static Ventanas nav = new Ventanas();

	private static DBLibreriaManager libreria = null;

	private static Utilidades utilidad = null;

	private static FuncionesExcel excelFuntions = null;

	private boolean isUserInput = true;
	private boolean updatingComboBoxes = false; // New variable to keep track of ComboBox updates

	private Map<ComboBox<String>, ObservableList<String>> originalComboBoxItems = new HashMap<>();
	private ComboBox<String> currentComboBox; // New variable to keep track of the current ComboBox

	private AutoCompletionBinding<String> nombreComicAutoCompletion;
	private AutoCompletionBinding<String> numeroComicAutoCompletion;
	private AutoCompletionBinding<String> nombreFirmaAutoCompletion;
	private AutoCompletionBinding<String> nombreGuionistaAutoCompletion;
	private AutoCompletionBinding<String> nombreVarianteAutoCompletion;
	private AutoCompletionBinding<String> numeroCajaAutoCompletion;
	private AutoCompletionBinding<String> nombreProcedenciaAutoCompletion;
	private AutoCompletionBinding<String> nombreFormatoAutoCompletion;
	private AutoCompletionBinding<String> nombreEditorialAutoCompletion;
	private AutoCompletionBinding<String> nombreDibujanteAutoCompletion;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		prontInfo.textProperty().addListener((observable, oldValue, newValue) -> {
			ajustarAnchoVBox(prontInfo, vboxContenido);
		});

//		prontFrases.textProperty().addListener((observable, oldValue, newValue) -> {
//			ajustarAnchoVBox(prontFrases, vboxFrases);
//		});

		// Asegurarnos de que el VBox ajuste su tamaño correctamente al inicio
		Platform.runLater(() -> ajustarAnchoVBox(prontInfo, vboxContenido));

		// Asegurarnos de que el VBox ajuste su tamaño correctamente al inicio
		Platform.runLater(() -> ajustarAnchoVBox(prontFrases, vboxContenido));

		Platform.runLater(() -> seleccionarRaw());
		Platform.runLater(() -> asignarTooltips());

		libreria = new DBLibreriaManager();
		try {
			libreria.listasAutoCompletado();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		listas_autocompletado();
		modificarColumnas();
		rellenarComboBox();
		restringir_entrada_datos();

		lecturaComboBox();

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
		prontFrases.setEditable(false);
	}

	private void ajustarAnchoVBox(TextArea textArea, VBox vbox) {
		// Crear un objeto Text con el contenido del TextArea
		Text text = new Text(textArea.getText());

		// Configurar el mismo estilo que tiene el TextArea
		text.setFont(textArea.getFont());

		double textHeight = text.getLayoutBounds().getHeight();

		textArea.setPrefHeight(textHeight);
	}

	private void asignarTooltips() {
		asignarTooltip(botonbbdd, "Muestra toda la base de datos");
		asignarTooltip(botonLimpiar, "Limpia la pantalla y reinicia todos los valores");
		asignarTooltip(botonMostrarParametro, "Muestra los comics o libros o mangas por parametro");
		asignarTooltip(botonFrase, "Muestra una frase aleatoria de personaje de comics");

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
	}

	private void asignarTooltip(Button boton, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
		boton.setTooltip(tooltip);
	}

	private void asignarTooltip(ComboBox<?> comboBox, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
		comboBox.setTooltip(tooltip);
	}

	private Comic getComicFromComboBoxes() {
		Comic comic = new Comic();
		comic.setNombre(nombreComic.getValue() != null ? nombreComic.getValue() : "");
		comic.setNumero(numeroComic.getValue() != null ? numeroComic.getValue() : "");
		comic.setFirma(nombreFirma.getValue() != null ? nombreFirma.getValue() : "");
		comic.setGuionista(nombreGuionista.getValue() != null ? nombreGuionista.getValue() : "");
		comic.setVariante(nombreVariante.getValue() != null ? nombreVariante.getValue() : "");
		comic.setNumCaja(numeroCaja.getValue() != null ? numeroCaja.getValue() : "");
		comic.setProcedencia(nombreProcedencia.getValue() != null ? nombreProcedencia.getValue() : "");
		comic.setFormato(nombreFormato.getValue() != null ? nombreFormato.getValue() : "");
		comic.setEditorial(nombreEditorial.getValue() != null ? nombreEditorial.getValue() : "");
		comic.setDibujante(nombreDibujante.getValue() != null ? nombreDibujante.getValue() : "");
		return comic;
	}

	/**
	 * Realiza la lectura y configuración de los ComboBoxes de la interfaz gráfica.
	 * Asigna escuchadores para detectar cambios en los ComboBoxes y en sus campos
	 * de texto. Actualiza los ComboBoxes según los cambios realizados por el
	 * usuario. Además, comprueba si solo un ComboBox está lleno y llama a la
	 * función rellenarComboBox().
	 */
	public void lecturaComboBox() {
		libreria = new DBLibreriaManager();
		isUserInput = true; // Establecemos isUserInput en true inicialmente.

		// Configuración de los escuchadores para cada ComboBox mediante un bucle
		for (ComboBox<String> comboBox : Arrays.asList(nombreComic, numeroComic, nombreFirma, nombreGuionista,
				nombreVariante, numeroCaja, nombreProcedencia, nombreFormato, nombreEditorial, nombreDibujante)) {

			// Guardar los elementos originales de cada ComboBox
			originalComboBoxItems.put(comboBox, FXCollections.observableArrayList(comboBox.getItems()));

			// Agregar el escuchador de cambios para detectar cuando se selecciona un valor
			// en el ComboBox
			comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
				if (!isUserInput || updatingComboBoxes) {
					return; // Ignorar cambios programáticos o cuando updatingComboBoxes es verdadero
				}

				if (newValue == null || newValue.isEmpty()) {
					handleComboBoxEmptyChange(comboBox);
					return;
				} else {
					Comic comic = getComicFromComboBoxes();
					actualizarComboBoxes(comic);
					return;
				}
			});

			// Agregar el escuchador de cambios para detectar cuando se borra el texto en el
			// ComboBox editor
			comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
				if (!isUserInput || updatingComboBoxes) {
					return; // Ignorar cambios programáticos o cuando updatingComboBoxes es verdadero
				}

				if (newValue == null || newValue.isEmpty()) {
					handleComboBoxEmptyChange(comboBox);
					return;
				} else {
					Comic comic = getComicFromComboBoxes();
					actualizarComboBoxes(comic);
					return;
				}
			});

			// Agregar el escuchador de cambios para detectar cuando se presiona "Backspace"
			// o "Delete"
			comboBox.setOnKeyReleased(event -> {
				KeyCode code = event.getCode();
				if (code == KeyCode.BACK_SPACE || code == KeyCode.DELETE) {
					comboBox.setValue(null); // Reset the ComboBox value
					comboBox.getEditor().clear(); // Clear the ComboBox editor text
					handleComboBoxEmptyChange(comboBox); // Handle the empty change after clearing
				}
			});
		}
	}

	/**
	 * Maneja los cambios en el ComboBox cuando su texto está vacío. Restablece el
	 * valor del ComboBox a null y verifica si todos los campos de texto de los
	 * ComboBoxes están vacíos. Si todos están vacíos, llama a la función
	 * "limpiezaDeDatos()".
	 *
	 * @param comboBox El ComboBox que ha cambiado su valor.
	 */
	private void handleComboBoxEmptyChange(ComboBox<String> comboBox) {
		isUserInput = false;

		// Restablecer el valor del ComboBox cuando el texto está vacío
		currentComboBox = comboBox; // Actualizar el ComboBox actual
		currentComboBox.setValue(null); // Establecer a null para restablecer la selección del ComboBox

		isUserInput = true;
		// Comprobar si todos los campos de texto de los ComboBoxes están vacíos
		boolean allEmpty = true;
		for (ComboBox<String> cb : originalComboBoxItems.keySet()) {
			if (!cb.getEditor().getText().isEmpty()) {
				allEmpty = false;
				break;
			}
		}

		// Si todos los campos de texto de los ComboBoxes están vacíos, llamar a
		// limpiezaDeDatos()
		if (allEmpty) {
			limpiezaDeDatos();
		}
	}

	/**
	 * Actualiza los ComboBoxes con los resultados obtenidos de la base de datos
	 * según el Comic proporcionado. Primero, se crea un Comic temporal sin algunos
	 * valores para utilizarlo en la consulta SQL. Luego, se construye la consulta
	 * SQL a partir del Comic temporal y se obtienen los resultados de la base de
	 * datos para cada campo de ComboBox. Finalmente, se actualizan los ComboBoxes
	 * con los nuevos datos obtenidos.
	 *
	 * @param comic El Comic que se utilizará como base para obtener los resultados
	 *              de la base de datos.
	 */
	public void actualizarComboBoxes(Comic comic) {

		Comic comicTemp = new Comic("", comic.getNombre(), comic.getNumCaja(), comic.getNumero(), comic.getVariante(),
				comic.getFirma(), comic.getEditorial(), comic.getFormato(), comic.getProcedencia(), "",
				comic.getGuionista(), comic.getDibujante(), "", "", "", "", "", "");

		String sql = libreria.datosConcatenados(comicTemp);

		comic.toString();

		if (!sql.isEmpty()) {

			isUserInput = false; // Disable user input during programmatic updates

			DBLibreriaManager.nombreComicList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "nomComic");
			DBLibreriaManager.nombreGuionistaList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "nomGuionista");
			DBLibreriaManager.numeroComicList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "numComic");
			DBLibreriaManager.nombreVarianteList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "nomVariante");
			DBLibreriaManager.numeroCajaList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "caja_deposito");
			DBLibreriaManager.nombreProcedenciaList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "procedencia");
			DBLibreriaManager.nombreFormatoList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "formato");
			DBLibreriaManager.nombreEditorialList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "nomEditorial");
			DBLibreriaManager.nombreDibujanteList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "nomDibujante");
			DBLibreriaManager.nombreFirmaList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "firma");

			if (!DBLibreriaManager.nombreComicList.isEmpty() || !comic.getNombre().isEmpty()) {
				ObservableList<String> nombresActuales = FXCollections
						.observableArrayList(DBLibreriaManager.nombreComicList);
				nombreComic.setItems(nombresActuales);
			}

			if (!DBLibreriaManager.numeroComicList.isEmpty()) {
				ObservableList<String> numerosActuales = FXCollections
						.observableArrayList(DBLibreriaManager.numeroComicList);
				numeroComic.setItems(numerosActuales);
			}

			if (!DBLibreriaManager.nombreGuionistaList.isEmpty()) {
				ObservableList<String> guionistasActuales = FXCollections
						.observableArrayList(DBLibreriaManager.nombreGuionistaList);
				nombreGuionista.setItems(guionistasActuales);
			}

			if (!DBLibreriaManager.nombreVarianteList.isEmpty()) {
				ObservableList<String> variantesActuales = FXCollections
						.observableArrayList(DBLibreriaManager.nombreVarianteList);
				nombreVariante.setItems(variantesActuales);
			}

			if (!DBLibreriaManager.numeroCajaList.isEmpty()) {

				ObservableList<String> cajaComics = FXCollections.observableArrayList(DBLibreriaManager.numeroCajaList);
				numeroCaja.setItems(cajaComics);
			}

			if (!DBLibreriaManager.nombreProcedenciaList.isEmpty()) {

				ObservableList<String> procedenciaEstadoActual = FXCollections
						.observableArrayList(DBLibreriaManager.nombreProcedenciaList);
				nombreProcedencia.setItems(procedenciaEstadoActual);
			}

			if (!DBLibreriaManager.nombreFormatoList.isEmpty()) {
				ObservableList<String> formatoActual = FXCollections
						.observableArrayList(DBLibreriaManager.nombreFormatoList);
				nombreFormato.setItems(formatoActual);
			}

			if (!DBLibreriaManager.nombreEditorialList.isEmpty()) {
				ObservableList<String> editoriales = FXCollections
						.observableArrayList(DBLibreriaManager.nombreEditorialList);
				nombreEditorial.setItems(editoriales);
			}

			if (!DBLibreriaManager.nombreDibujanteList.isEmpty()) {
				ObservableList<String> dibujantesActuales = FXCollections
						.observableArrayList(DBLibreriaManager.nombreDibujanteList);
				nombreDibujante.setItems(dibujantesActuales);
			}

			if (!DBLibreriaManager.nombreFirmaList.isEmpty()) {
				ObservableList<String> firmasActuales = FXCollections
						.observableArrayList(DBLibreriaManager.nombreFirmaList);
				nombreFirma.setItems(firmasActuales);
			}
			isUserInput = true; // Re-enable user input after programmatic updates
		}
	}

	/**
	 * Funcion que permite restringir entrada de datos de todo aquello que no sea un
	 * numero entero en los comboBox numeroComic y caja_comic
	 */
	public void restringir_entrada_datos() {
		numeroComic.getEditor().setTextFormatter(validador_Nenteros());
		numeroCaja.getEditor().setTextFormatter(validador_Nenteros());
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

	/**
	 * Permite rellenar los datos de los comboBox con los datos de las listas
	 */
	public void rellenarComboBox() {

		ObservableList<String> nombresActuales = FXCollections.observableArrayList(DBLibreriaManager.listaNombre);
		nombreComic.setItems(nombresActuales);

		ObservableList<String> variantesActuales = FXCollections.observableArrayList(DBLibreriaManager.listaVariante);
		nombreVariante.setItems(variantesActuales);

		ObservableList<String> guionistasActuales = FXCollections.observableArrayList(DBLibreriaManager.listaGuionista);
		nombreGuionista.setItems(guionistasActuales);

		ObservableList<String> dibujantesActuales = FXCollections.observableArrayList(DBLibreriaManager.listaDibujante);
		nombreDibujante.setItems(dibujantesActuales);

		ObservableList<String> firmasActuales = FXCollections.observableArrayList(DBLibreriaManager.listaFirma);
		nombreFirma.setItems(firmasActuales);

		ObservableList<String> formatoActual = FXCollections.observableArrayList(DBLibreriaManager.listaFormato);
		nombreFormato.setItems(formatoActual);

		ObservableList<String> editoriales = FXCollections.observableArrayList(DBLibreriaManager.listaEditorial);
		nombreEditorial.setItems(editoriales);

		ObservableList<String> numeroComics = FXCollections.observableArrayList(DBLibreriaManager.listaNumeroComic);
		numeroComic.setItems(numeroComics);

		ObservableList<String> procedenciaEstadoActual = FXCollections
				.observableArrayList(DBLibreriaManager.listaProcedencia);
		nombreProcedencia.setItems(procedenciaEstadoActual);

		ObservableList<String> cajaComics = FXCollections.observableArrayList(DBLibreriaManager.listaCaja);
		numeroCaja.setItems(cajaComics);
	}

	/**
	 * Cuando pasa el raton por encima, se colorea de color azul el raw donde el
	 * raton se encuentra y muestra un mensaje emergente con datos del comic
	 */
	public void seleccionarRaw() {
		tablaBBDD.setRowFactory(tv -> {
			TableRow<Comic> row = new TableRow<>();
			Tooltip tooltip = new Tooltip();
			tooltip.setShowDelay(Duration.ZERO);
			tooltip.setHideDelay(Duration.ZERO);

			row.setOnMouseEntered(event -> {
				if (!row.isEmpty()) {
					row.setStyle("-fx-background-color: #BFEFFF;");

					Comic comic = row.getItem();
					if (comic != null && !tooltip.isShowing()) {
						String mensaje = "Nombre: " + comic.getNombre() + "\nNumero: " + comic.getNumero()
								+ "\nVariante: " + comic.getVariante() + "\nGuionista: " + comic.getGuionista()
								+ "\nDibujante: " + comic.getDibujante();
						if (!comic.getFirma().isEmpty()) {
							mensaje += "\nFirma: " + comic.getFirma();
						}
						tooltip.setText(mensaje);
						tooltip.show(row, event.getSceneX(), event.getSceneY());
						tooltip.setX(event.getScreenX() + 10); // Ajusta el desplazamiento X según tus necesidades
						tooltip.setY(event.getScreenY() - 20); // Ajusta el desplazamiento Y según tus necesidades
					}
				}
			});

			row.setOnMouseExited(event -> {
				if (!row.isEmpty()) {
					row.setStyle("");
					tooltip.hide();
				}
			});

			return row;
		});

		// Deshabilitar el enfoque en el TableView
		tablaBBDD.setFocusTraversable(false);

		// Enfocar el VBox para evitar movimientos inesperados
		VBox root = (VBox) tablaBBDD.getScene().lookup("#rootVBox");
		if (root != null) {
			root.requestFocus();
		}
	}

	/**
	 * Funcion que permite el autocompletado en la parte Text del comboBox
	 */
	public void listas_autocompletado() {
		// Las vinculaciones se asignan a las variables miembro correspondientes
		nombreComicAutoCompletion = TextFields.bindAutoCompletion(nombreComic.getEditor(),
				DBLibreriaManager.listaNombre);
		numeroComicAutoCompletion = TextFields.bindAutoCompletion(numeroComic.getEditor(),
				DBLibreriaManager.listaNumeroComic);
		nombreFirmaAutoCompletion = TextFields.bindAutoCompletion(nombreFirma.getEditor(),
				DBLibreriaManager.listaFirma);
		nombreEditorialAutoCompletion = TextFields.bindAutoCompletion(nombreEditorial.getEditor(),
				DBLibreriaManager.listaEditorial);
		nombreGuionistaAutoCompletion = TextFields.bindAutoCompletion(nombreGuionista.getEditor(),
				DBLibreriaManager.listaGuionista);
		nombreVarianteAutoCompletion = TextFields.bindAutoCompletion(nombreVariante.getEditor(),
				DBLibreriaManager.listaVariante);
		nombreDibujanteAutoCompletion = TextFields.bindAutoCompletion(nombreDibujante.getEditor(),
				DBLibreriaManager.listaDibujante);
		nombreProcedenciaAutoCompletion = TextFields.bindAutoCompletion(nombreProcedencia.getEditor(),
				DBLibreriaManager.listaProcedencia);
		nombreFormatoAutoCompletion = TextFields.bindAutoCompletion(nombreFormato.getEditor(),
				DBLibreriaManager.listaFormato);
		numeroCajaAutoCompletion = TextFields.bindAutoCompletion(numeroCaja.getEditor(), DBLibreriaManager.listaCaja);

		// Las vinculaciones se asignan a las variables miembro correspondientes
		TextFields.bindAutoCompletion(nombreComic.getEditor(), DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(nombreVariante.getEditor(), DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(nombreFirma.getEditor(), DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(nombreEditorial.getEditor(), DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(nombreGuionista.getEditor(), DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(nombreDibujante.getEditor(), DBLibreriaManager.listaDibujante);
		TextFields.bindAutoCompletion(nombreProcedencia.getEditor(), DBLibreriaManager.listaProcedencia);
		TextFields.bindAutoCompletion(nombreFormato.getEditor(), DBLibreriaManager.listaFormato);
		TextFields.bindAutoCompletion(numeroCaja.getEditor(), DBLibreriaManager.listaCaja);

		// Additional bindings for the 'busquedaGeneral' TextField
		TextFields.bindAutoCompletion(busquedaGeneral, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(busquedaGeneral, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(busquedaGeneral, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(busquedaGeneral, DBLibreriaManager.listaDibujante);
		TextFields.bindAutoCompletion(busquedaGeneral, DBLibreriaManager.listaEditorial);

	}

	/**
	 * Funcion que permite resetear las listas de autocompletado
	 */
	public void listas_reseteo() {
		// Desvincular y limpiar los autocompletados
		nombreComicAutoCompletion.dispose();
		numeroComicAutoCompletion.dispose();
		nombreFirmaAutoCompletion.dispose();
		nombreEditorialAutoCompletion.dispose();
		nombreGuionistaAutoCompletion.dispose();
		nombreVarianteAutoCompletion.dispose();
		nombreDibujanteAutoCompletion.dispose();
		nombreProcedenciaAutoCompletion.dispose();
		nombreFormatoAutoCompletion.dispose();
		numeroCajaAutoCompletion.dispose();
	}

	public void listas_autocompletado_filtrado() {

		nombreComicAutoCompletion = TextFields.bindAutoCompletion(nombreComic.getEditor(),
				DBLibreriaManager.nombreComicList);
		numeroComicAutoCompletion = TextFields.bindAutoCompletion(numeroComic.getEditor(),
				DBLibreriaManager.numeroComicList);
		nombreFirmaAutoCompletion = TextFields.bindAutoCompletion(nombreFirma.getEditor(),
				DBLibreriaManager.nombreFirmaList);
		nombreEditorialAutoCompletion = TextFields.bindAutoCompletion(nombreEditorial.getEditor(),
				DBLibreriaManager.nombreEditorialList);
		nombreGuionistaAutoCompletion = TextFields.bindAutoCompletion(nombreGuionista.getEditor(),
				DBLibreriaManager.nombreGuionistaList);
		nombreDibujanteAutoCompletion = TextFields.bindAutoCompletion(nombreDibujante.getEditor(),
				DBLibreriaManager.nombreDibujanteList);
		nombreProcedenciaAutoCompletion = TextFields.bindAutoCompletion(nombreProcedencia.getEditor(),
				DBLibreriaManager.nombreProcedenciaList);
		nombreFormatoAutoCompletion = TextFields.bindAutoCompletion(nombreFormato.getEditor(),
				DBLibreriaManager.nombreFormatoList);
		numeroCajaAutoCompletion = TextFields.bindAutoCompletion(numeroCaja.getEditor(),
				DBLibreriaManager.numeroCajaList);

		TextFields.bindAutoCompletion(nombreComic.getEditor(), DBLibreriaManager.nombreComicList);
		TextFields.bindAutoCompletion(numeroComic.getEditor(), DBLibreriaManager.numeroCajaList);
		TextFields.bindAutoCompletion(nombreVariante.getEditor(), DBLibreriaManager.nombreVarianteList);
		TextFields.bindAutoCompletion(nombreFirma.getEditor(), DBLibreriaManager.nombreFirmaList);
		TextFields.bindAutoCompletion(nombreEditorial.getEditor(), DBLibreriaManager.nombreEditorialList);
		TextFields.bindAutoCompletion(nombreGuionista.getEditor(), DBLibreriaManager.nombreGuionistaList);
		TextFields.bindAutoCompletion(nombreDibujante.getEditor(), DBLibreriaManager.nombreDibujanteList);
		TextFields.bindAutoCompletion(nombreProcedencia.getEditor(), DBLibreriaManager.nombreProcedenciaList);
		TextFields.bindAutoCompletion(nombreFormato.getEditor(), DBLibreriaManager.nombreFormatoList);
		TextFields.bindAutoCompletion(numeroCaja.getEditor(), DBLibreriaManager.numeroCajaList);
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

	/////////////////////////////////
	//// METODOS LLAMADA A VENTANAS//
	/////////////////////////////////

	/**
	 * Permite abrir y cargar la ventana para IntroducirDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaAniadir(ActionEvent event) {

		nav.verIntroducirDatos();
		// DBManager.resetConnection();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana de EliminarDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaEliminar(ActionEvent event) {

		nav.verEliminarDatos();
		DBManager.resetConnection();

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
		DBManager.resetConnection();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana de RecomendacionesController
	 *
	 * @param event
	 */
	@FXML
	void ventanaRecomendar(ActionEvent event) {

		nav.verRecomendacion();
		DBManager.resetConnection();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana de PuntuarDatosController
	 * 
	 * @param event
	 */
	@FXML
	void ventanaPuntuar(ActionEvent event) {

		nav.verPuntuar();
		DBManager.resetConnection();
		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana de SobreMiController
	 *
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {

		nav.verSobreMi();
		DBManager.resetConnection();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Muestra en un textArea diferentes frases random de personajes de los comics.
	 *
	 * @param event
	 */
	@FXML
	void fraseRandom(ActionEvent event) {
		limpiezaDeDatos();
		modificarColumnas();
		modificarColumnas();
		prontInfo.setOpacity(0);
		prontFrases.setOpacity(1);
		prontFrases.setText(Comic.frasesComics());
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
		modificarColumnas();
		modificarColumnas();
		prontInfo.setOpacity(0);
		prontFrases.setOpacity(0);
		imagencomic.setImage(null);
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
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		modificarColumnas();
		modificarColumnas();
		tablaBBDD.refresh();
		prontInfo.setOpacity(0);
		prontFrases.setOpacity(0);
		imagencomic.setImage(null);

		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
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
		limpiezaDeDatos();
		tablaBBDD.refresh();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaPuntuacion());

	}

	public void columnaSeleccionada(String rawSelecionado) throws SQLException {
		prontInfo.setOpacity(0);
		limpiezaDeDatos();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaSeleccionado(rawSelecionado));

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
		limpiezaDeDatos();
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
		prontInfo.setOpacity(0);
		limpiezaDeDatos();
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
		prontInfo.setOpacity(0);
		limpiezaDeDatos();
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
		prontInfo.setOpacity(0);
		limpiezaDeDatos();
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaPosesion());
	}

	@FXML
	void comicsKeyIssue(ActionEvent event) throws SQLException {
		prontInfo.setOpacity(0);
		limpiezaDeDatos();
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaKeyIssue());
	}

	////////////////////////////
	/// METODOS PARA EXPORTAR///
	////////////////////////////

	/**
	 * Importa un fichero CSV compatible con el programa para copiar la informacion
	 * a la base de datos
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void importCSV(ActionEvent event) throws SQLException {

		String frase = "Fichero CSV";

		String formato = "*.csv";

		File fichero = tratarFichero(frase, formato).showOpenDialog(null); // Llamada a funcion

		importCSV(fichero);
		libreria.listasAutoCompletado();
	}

	/**
	 * Exporta un fichero CSV compatible con el programa que copia el contenido de
	 * la base de datos en un fichero CSV
	 *
	 * @param event
	 */
	@FXML
	void exportCSV(ActionEvent event) {

		String frase = "Fichero Excel xlsx";

		String formato = "*.xlsx";

		File fichero = tratarFichero(frase, formato).showSaveDialog(null); // Llamada a funcion

		makeExcel(fichero);
	}

	/**
	 * Exporta la base de datos en un fichero SQL
	 *
	 * @param event
	 */
	@FXML
	void exportarSQL(ActionEvent event) {

		String frase = "Fichero SQL";

		String formato = "*.sql";

		File fichero = tratarFichero(frase, formato).showSaveDialog(null); // Llamada a funcion

		makeSQL(fichero);

	}

	/**
	 * Funcion que abre una ventana que aceptara los formatos de archivos que le
	 * demos como parametro.
	 *
	 * @param frase   Descripción del filtro de archivo (p. ej., "Archivos CSV")
	 * @param formato Extensiones de archivo permitidas (p. ej., "*.csv")
	 * @return FileChooser si el usuario selecciona un fichero; null si el usuario
	 *         cancela la selección o cierra la ventana
	 */
	public static FileChooser tratarFichero(String frase, String formato) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter(frase, formato));

		return fileChooser; // Devuelve el FileChooser para que la interfaz gráfica lo utilice
	}

	/**
	 * Limpia los campos de pantalla donde se escriben los datos.
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		limpiezaDeDatos();
	}

	private void limpiezaDeDatos() {
		isUserInput = false; // Disable user input during cleanup

		// Clear all ComboBox text fields and values
		for (ComboBox<String> comboBox : Arrays.asList(nombreComic, numeroComic, nombreFirma, nombreGuionista,
				nombreVariante, numeroCaja, nombreProcedencia, nombreFormato, nombreEditorial, nombreDibujante)) {
			comboBox.setValue("");
			comboBox.getEditor().setText("");
		}

		// Restore the original items for each ComboBox
		for (ComboBox<String> comboBox : originalComboBoxItems.keySet()) {
			ObservableList<String> originalItems = originalComboBoxItems.get(comboBox);
			comboBox.setItems(originalItems);
		}

		// Clear additional UI elements
		fechaPublicacion.setValue(null);
		prontFrases.setText(null);
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		prontFrases.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);

		isUserInput = true; // Re-enable user input after cleanup
		rellenarComboBox();
	}

	/**
	 * Método manejador del evento de clic en el botón "Borrar Contenido de la
	 * Tabla". Este método borra el contenido de la tabla de la base de datos de
	 * forma asíncrona, basándose en la confirmación del usuario.
	 *
	 * @param event El evento de ActionEvent generado por el clic en el botón.
	 * @throws SQLException Si hay un error en la operación de la base de datos.
	 */
	@FXML
	void borrarContenidoTabla(ActionEvent event) throws SQLException {
		// Crear una tarea (Task) para realizar la operación de borrado de contenido de
		// la tabla
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				// Obtener la CompletableFuture<Boolean> del método borrarContenidoTabla de la
				// clase nav
				CompletableFuture<Boolean> futureResult = nav.borrarContenidoTabla();
				// Obtener el resultado real (Boolean) de la CompletableFuture utilizando join()
				boolean result = futureResult.join();
				return result; // Devolver el resultado actual
			}
		};

		// Configurar el comportamiento cuando la tarea está en ejecución
		task.setOnRunning(e -> {
			// Ocultar los elementos de información y frases
			prontInfo.setOpacity(0);
			prontInfo.setText(null);
			prontFrases.setOpacity(0);
			prontFrases.setText(null);
			// Iniciar la animación
			iniciarAnimacion();
		});

		// Configurar el comportamiento cuando la tarea se completa con éxito
		task.setOnSucceeded(e -> {
			// Obtener el resultado de la tarea
			Boolean resultado = task.getValue();
			/* Tu condición aquí basada en el resultado */

			if (resultado) {
				// Ejecutar el método deleteTable en su propio hilo
				Task<Boolean> deleteTask = new Task<Boolean>() {
					@Override
					protected Boolean call() throws Exception {
						// Verificar si la tabla tiene contenido
						boolean result = false;
						if (libreria.contenidoTabla()) {
							// Si hay contenido, borrar el contenido de la tabla en un hilo separado
							CompletableFuture<Boolean> futureResult = libreria.deleteTable();
							result = futureResult.join(); // Esperar a que la tarea asíncrona se complete y obtener el
															// resultado
						} else {
							// Si no hay contenido, mostrar un mensaje de error
							prontInfo.setOpacity(1);
							prontInfo.setStyle("-fx-background-color: #F53636");
							prontInfo.setText("No hay contenido en la base de datos");
							Platform.runLater(() -> nav.alertaException("Error. No hay contenido en la base de datos"));
							detenerAnimacion();
						}
						return result; // Devolver el resultado actual
					}
				};

				// Configurar el comportamiento cuando la tarea de borrado se completa con éxito
				deleteTask.setOnSucceeded(ev -> {
					// Obtener el resultado de la tarea de borrado
					boolean result = deleteTask.getValue();
					if (result) {
						// Mostrar el mensaje de éxito y limpiar la tabla y la imagen
						Platform.runLater(() -> {
							prontInfo.setOpacity(1);
							prontInfo.setStyle("-fx-background-color: #A0F52D");
							prontInfo.setText("Has borrado correctamente el contenido de la base de datos.");
							tablaBBDD.getItems().clear();
							imagencomic.setImage(null);
							detenerAnimacion();
						});
					}
				});

				// Configurar el comportamiento cuando la tarea de borrado falla
				deleteTask.setOnFailed(ev -> {
					// Detener la animación y mostrar el mensaje de error
					detenerAnimacion();
					Throwable exception = deleteTask.getException();
					if (exception != null) {
						exception.printStackTrace();
						Platform.runLater(() -> nav.alertaException(
								"Error al borrar el contenido de la base de datos: " + exception.getMessage()));
					} else {
						Platform.runLater(() -> nav
								.alertaException("Error desconocido al borrar el contenido de la base de datos"));
					}
				});

				// Iniciar la tarea de borrado en un hilo separado
				Thread deleteThread = new Thread(deleteTask);
				deleteThread.start();
			} else {
				// Si el resultado es falso, mostrar el mensaje de cancelación
				Platform.runLater(() -> {
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #F53636");
					prontInfo.setText("Has cancelado el borrado de la base de datos.");
					detenerAnimacion();
				});
			}
		});

		// Configurar el comportamiento cuando la tarea falla
		task.setOnFailed(e -> {
			// Detener la animación y mostrar el mensaje de error
			detenerAnimacion();
			Throwable exception = task.getException();
			if (exception != null) {
				exception.printStackTrace();
				Platform.runLater(
						() -> nav.alertaException("Error al importar el fichero CSV: " + exception.getMessage()));
			} else {
				Platform.runLater(() -> nav.alertaException("Error desconocido al importar el fichero CSV."));
			}
		});

		// Iniciar la tarea principal de borrado en un hilo separado
		Thread thread = new Thread(task);
		thread.start();
	}

	/**
	 * Se llama a funcion que permite ver las estadisticas de la bbdd
	 *
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void verEstadistica(ActionEvent event) throws IOException {
		prontInfo.setOpacity(0);
		libreria = new DBLibreriaManager();
		prontInfo.setOpacity(1);
		prontInfo.setText("Generando fichero de estadisticas . . . ");
		libreria.generar_fichero_estadisticas();
	}

	/////////////////////////////////
	//// FUNCIONES////////////////////
	/////////////////////////////////

	/**
	 * Permite dar valor a las celdas de la TableView
	 */
	private void nombreColumnas() {

		ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		nombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
		caja.setCellValueFactory(new PropertyValueFactory<>("numCaja"));
		numero.setCellValueFactory(new PropertyValueFactory<>("Numero"));
		variante.setCellValueFactory(new PropertyValueFactory<>("Variante"));
		firma.setCellValueFactory(new PropertyValueFactory<>("Firma"));
		editorial.setCellValueFactory(new PropertyValueFactory<>("Editorial"));
		formato.setCellValueFactory(new PropertyValueFactory<>("Formato"));
		procedencia.setCellValueFactory(new PropertyValueFactory<>("Procedencia"));
		fecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
		guionista.setCellValueFactory(new PropertyValueFactory<>("Guionista"));
		dibujante.setCellValueFactory(new PropertyValueFactory<>("Dibujante"));
		referencia.setCellValueFactory(new PropertyValueFactory<>("url_referencia"));

		busquedaRaw(nombre);
		busquedaRaw(variante);
		busquedaRaw(guionista);
		busquedaRaw(dibujante);
		busquedaRaw(firma);
		busquedaRaw(procedencia);
		busquedaRaw(formato);
		busquedaRaw(editorial);
		busquedaRaw(fecha);
		Utilidades.busquedaHyperLink(referencia);
	}

	/**
	 * Funcion que permite que los diferentes raw de los TableColumn se puedan
	 * pinchar. Al hacer, genera un nuevo tipo de busqueda personalizada solo con el
	 * valor que hemos pinchado
	 * 
	 * @param columna
	 */
	public void busquedaRaw(TableColumn<Comic, String> columna) {
		columna.setCellFactory(column -> {
			return new TableCell<Comic, String>() {
				private VBox vbox = new VBox();
				private String lastItem = null;

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty || item == null) {
						setGraphic(null);
					} else {
						if (!item.equals(lastItem)) { // Verificar si el contenido ha cambiado
							lastItem = item;
							String[] nombres = item.split(" - ");
							vbox.getChildren().clear();

							for (String nombre : nombres) {
								if (!nombre.isEmpty()) {
									Label label;
									if (columna.getText().equalsIgnoreCase("fecha")
											|| columna.getText().equalsIgnoreCase("editorial")
											|| columna.getText().equalsIgnoreCase("formato")
											|| columna.getText().equalsIgnoreCase("variante")
											|| columna.getText().equalsIgnoreCase("origen")) {
										label = new Label(nombre + "\n");
									} else {
										label = new Label("◉ " + nombre + "\n");
									}
									label.getStyleClass().add("hyperlink");
									Hyperlink hyperlink = new Hyperlink();
									hyperlink.setGraphic(label);
									hyperlink.setOnAction(event -> {
										try {
											prontFrases.setText(null);
											prontInfo.setText(null);
											prontInfo.setOpacity(0);
											prontFrases.setOpacity(0);

											columnaSeleccionada(nombre);
											prontInfo.setOpacity(1);
											prontInfo.setText("El número de cómics donde aparece la \nbúsqueda: "
													+ nombre + " es: " + libreria.numeroTotalSelecionado(nombre));
										} catch (SQLException e) {
											e.printStackTrace();
										}
									});
									vbox.getChildren().add(hyperlink);
								}
							}
						}
						setGraphic(vbox);
					}
				}
			};
		});
	}




	/**
	 * Funcion que modifica el tamaño de los TableColumn
	 */
	public void modificarColumnas() {

		// Set the initial widths of the columns
		nombre.setPrefWidth(140);
		caja.setPrefWidth(37);
		numero.setPrefWidth(45);
		firma.setPrefWidth(85);
		editorial.setPrefWidth(78);
		variante.setPrefWidth(135);
		procedencia.setPrefWidth(75);
		fecha.setPrefWidth(105);
		guionista.setPrefWidth(145);
		dibujante.setPrefWidth(150);
		referencia.setPrefWidth(90);
		formato.setPrefWidth(92);

		// Set the resizing policy to unconstrained
		tablaBBDD.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

		// Store the original widths
		@SuppressWarnings("rawtypes")
		Map<TableColumn, Double> originalWidths = new HashMap<>();
		originalWidths.put(nombre, nombre.getWidth());
		originalWidths.put(numero, numero.getWidth());
		originalWidths.put(firma, firma.getWidth());
		originalWidths.put(editorial, editorial.getWidth());
		originalWidths.put(variante, variante.getWidth());
		originalWidths.put(procedencia, procedencia.getWidth());
		originalWidths.put(fecha, fecha.getWidth());
		originalWidths.put(guionista, guionista.getWidth());
		originalWidths.put(dibujante, dibujante.getWidth());
		originalWidths.put(referencia, referencia.getWidth());
		originalWidths.put(formato, formato.getWidth());

		// Reiniciar el tamaño de las columnas
		tablaBBDD.refresh();

		// Reset the widths of the columns to their original sizes
		tablaBBDD.getColumns().forEach(column -> {
			Double originalWidth = originalWidths.get(column);
			if (originalWidth != null) {
				column.setPrefWidth(originalWidth);
			}
		});
	}

	/**
	 * Maneja el evento de clic del ratón en un elemento específico dentro del RAW y
	 * permite ver los datos de este mediante el TextArea, imagen incluida
	 *
	 * @param event El evento del ratón desencadenado por el clic.
	 * @throws IOException  Si ocurre una excepción de entrada o salida al trabajar
	 *                      con archivos.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	@FXML
	void clickRaton(MouseEvent event) throws IOException, SQLException {
		prontFrases.setOpacity(0);
		libreria = new DBLibreriaManager();
		libreria.libreriaCompleta();
		utilidad = new Utilidades();
		String ID;

		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (idRow != null) {
			ID = idRow.getID();
			prontInfo.setOpacity(1);
			prontInfo.setText(libreria.comicDatos(ID).toString().replace("[", "").replace("]", ""));

			imagencomic.setImage(libreria.selectorImage(ID));
			utilidad.deleteImage();
		}
	}

	/**
	 * Maneja el evento de teclas direccionales (arriba/abajo) presionadas dentro
	 * del RAW y permite ver los datos de este mediante el TextArea, imagen incluida
	 *
	 * @param event El evento de teclado desencadenado por la pulsación de teclas.
	 * @throws IOException  Si ocurre una excepción de entrada o salida al trabajar
	 *                      con archivos.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	@FXML
	void teclasDireccion(KeyEvent event) throws IOException, SQLException {
		if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
			prontFrases.setOpacity(0);
			libreria = new DBLibreriaManager();
			libreria.libreriaCompleta();
			utilidad = new Utilidades();
			String ID;

			Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

			if (idRow != null) {
				ID = idRow.getID();
				prontInfo.setOpacity(1);
				prontInfo.setText(libreria.comicDatos(ID).toString().replace("[", "").replace("]", ""));

				imagencomic.setImage(libreria.selectorImage(ID));
				utilidad.deleteImage();
			}
		}
	}

	/////////////////////////////////
	//// FUNCIONES CREACION FICHEROS//
	/////////////////////////////////

	/**
	 * Funcion que compruba si se ha creado el fichero Excel y CSV
	 *
	 * @param fichero
	 */
	public void makeExcel(File fichero) {
		excelFuntions = new FuncionesExcel();
		prontInfo.setOpacity(0);
		try {
			if (fichero != null) {
				if (excelFuntions.crearExcel(fichero)) { // Si el fichero XLSX y CSV se han creado se vera el siguiente
					// mensaje
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #A0F52D");
					prontInfo.setText("Fichero excel exportado de forma correcta");
				} else { // Si no se ha podido crear correctamente los ficheros se vera el siguiente
					// mensaje
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #F53636");
					prontInfo.setText("ERROR. No se ha podido exportar correctamente.");
				}
			} else { // En caso de cancelar la creacion de los ficheros, se mostrara el siguiente
				// mensaje.
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("ERROR. Se ha cancelado la exportacion.");
			}
		} catch (Exception e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Importa un archivo CSV y guarda su contenido en una base de datos de forma
	 * asíncrona.
	 *
	 * @param fichero El archivo CSV a importar.
	 */
	public void importCSV(File fichero) {
		FuncionesExcel excelFuntions = new FuncionesExcel(); // Crear una instancia de FuncionesExcel
		// Crear una tarea (Task) para realizar la importación del archivo CSV
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				try {
					if (fichero != null) {
						return true;
					}
				} catch (Exception e) {
					// Si ocurre un error desconocido al importar, mostrar un mensaje de error y
					// detener la animación
					e.printStackTrace();
					Platform.runLater(() -> nav
							.alertaException("Error desconocido al importar el fichero CSV: " + e.getMessage()));
					detenerAnimacion();

				}
				return false;
			}
		};

		// Configurar el comportamiento cuando la tarea está en ejecución
		task.setOnRunning(e -> {
			// Ocultar los elementos de información y frases
			prontInfo.setOpacity(0);
			prontInfo.setText(null);
			prontFrases.setOpacity(0);
			prontFrases.setText(null);
			// Iniciar la animación
			iniciarAnimacion();
		});

		// Configurar el comportamiento cuando la tarea se completa con éxito
		task.setOnSucceeded(e -> {
			// Obtener el resultado de la tarea
			Boolean resultado = task.getValue();
			/* Tu condición aquí basada en el resultado */

			if (resultado) {
				// Si la importación del CSV fue exitosa, continuar con la inserción en la base
				// de datos
				Platform.runLater(() -> {
					prontInfo.setOpacity(0); // Ocultar el mensaje inicial antes de iniciar la lectura y guardado

					String sql = "INSERT INTO comicsbbdd(ID,nomComic,caja_deposito,precio_comic,numComic,nomVariante,Firma,nomEditorial,Formato,Procedencia,fecha_publicacion,nomGuionista,nomDibujante,puntuacion,portada,key_issue,url_referencia,estado)"
							+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

					try {
						// Continuar con la conexión a la base de datos y el proceso de guardado
						BufferedReader lineReader = new BufferedReader(new FileReader(fichero));
						// Crear una nueva tarea (Task) para realizar la lectura y guardado en la base
						// de datos
						Task<Void> lecturaTask = excelFuntions.lecturaCSVTask(sql, lineReader);

						// Configurar el comportamiento cuando la tarea de lectura y guardado se
						// completa con éxito
						lecturaTask.setOnSucceeded(event -> {
							// La operación de inserción en la base de datos ha finalizado con éxito
							// Mostrar el mensaje de éxito después de la lectura y guardado
							prontInfo.setOpacity(1);
							prontInfo.setStyle("-fx-background-color: #A0F52D");
							prontInfo.setText("Fichero CSV importado de forma correcta");
							detenerAnimacion();
						});

						// Configurar el comportamiento cuando la tarea de lectura y guardado falla
						lecturaTask.setOnFailed(event -> {
							// Ha ocurrido un error durante la operación de inserción en la base de datos
							Throwable exception = lecturaTask.getException();
							if (exception != null) {
								exception.printStackTrace();
								Platform.runLater(() -> nav.alertaException(
										"Error al guardar datos en la base de datos: " + exception.getMessage()));
								detenerAnimacion();
							}
							prontInfo.setOpacity(1);
							prontInfo.setStyle("-fx-background-color: #F53636");
							prontInfo.setText("ERROR. No se ha podido guardar correctamente en la base de datos.");
							detenerAnimacion();
						});

						// Iniciar la tarea de lectura y guardado en un hilo separado
						Thread thread = new Thread(lecturaTask);
						thread.start();

					} catch (IOException ex) {
						// Si ocurre un error al importar, mostrar un mensaje de error y detener la
						// animación
						ex.printStackTrace();
						Platform.runLater(
								() -> nav.alertaException("Error al importar el fichero CSV: " + ex.getMessage()));
						prontInfo.setOpacity(1);
						prontInfo.setStyle("-fx-background-color: #F53636");
						prontInfo.setText("ERROR. No se ha podido importar correctamente.");
						detenerAnimacion();
					}
				});
			} else {
				// Si la importación del CSV falló, mostrar un mensaje de error
				Platform.runLater(() -> {
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #F53636");
					prontInfo.setText("ERROR. No se ha podido importar correctamente.");
					detenerAnimacion();
				});
			}
		});

		// Configurar el comportamiento cuando la tarea falla
		task.setOnFailed(e -> {
			// Detener la animación y mostrar un mensaje de error en caso de que la tarea
			// falle
			detenerAnimacion();
			Throwable exception = task.getException();
			if (exception != null) {
				exception.printStackTrace();
				Platform.runLater(
						() -> nav.alertaException("Error al importar el fichero CSV: " + exception.getMessage()));
				detenerAnimacion();
			} else {
				Platform.runLater(() -> nav.alertaException("Error desconocido al importar el fichero CSV."));
				detenerAnimacion();
			}
		});

		// Iniciar la tarea principal de importación en un hilo separado
		Thread thread = new Thread(task);
		thread.start();
	}

	// Función para iniciar la animación
	public void iniciarAnimacion() {
		progresoCarga.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
	}

	// Función para detener la animación
	public void detenerAnimacion() {
		progresoCarga.setProgress(0); // Establece el progreso en 0 para detener la animación
	}

	/**
	 * Funcion crea el fichero SQL segun el sistema operativo en el que te
	 * encuentres.
	 *
	 * @param fichero
	 */
	public void makeSQL(File fichero) {
		libreria = new DBLibreriaManager();
		prontInfo.setOpacity(0);
		if (fichero != null) {

			if (Utilidades.isWindows()) {
				libreria.backupWindows(fichero); // Llamada a funcion
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo.setText("Base de datos exportada \ncorrectamente");

			} else {
				if (Utilidades.isUnix()) {
					libreria.backupLinux(fichero); // Llamada a funcion
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #A0F52D");
					prontInfo.setText("Base de datos exportada \ncorrectamente");
				}
			}
		} else {
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("ERROR. Se ha cancelado la exportacion de la base de datos.");
		}
	}

	/**
	 * Funcion que comprueba segun los datos escritos en los textArea, que comic
	 * estas buscando.
	 * 
	 * @throws SQLException
	 */
	public void listaPorParametro() throws SQLException {
		libreria = new DBLibreriaManager();
		Comic comic = new Comic();
		String datos[] = camposComic();
		String fecha = datos[8];

		if (datos[8].isEmpty()) {
			fecha = "";
		} else {
			fecha = datos[8];
		}

		comic = new Comic("", datos[1], datos[12], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7], fecha,
				datos[9], datos[10], "", "", "", null, "", "");

		tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()));
		resultadoBusquedaPront(comic);
		busquedaGeneral.setText("");
	}

	/**
	 * Según el dato que busquemos a la hora de realizar la búsqueda, aparecerá un
	 * mensaje diferente en el pront.
	 * 
	 * @param comic El objeto Comic utilizado para la búsqueda.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	/**
	 * Según el dato que busquemos a la hora de realizar la búsqueda, aparecerá un
	 * mensaje diferente en el pront.
	 * 
	 * @param comic El objeto Comic utilizado para la búsqueda.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void resultadoBusquedaPront(Comic comic) throws SQLException {
		StringBuilder datoSeleccionadoBuilder = new StringBuilder();

		if (comic != null) {
			String[] campos = { comic.getNombre(), comic.getNumero(), comic.getVariante(), comic.getProcedencia(),
					comic.getFormato(), comic.getEditorial(), comic.getFecha(), comic.getNumCaja(),
					comic.getGuionista(), comic.getDibujante(), comic.getFirma() };

			int nonEmptyFieldCount = 0;
			for (String campo : campos) {
				if (!campo.isEmpty()) {
					nonEmptyFieldCount++;
					if (nonEmptyFieldCount > 1) {
						datoSeleccionadoBuilder.append(", ");
					}
					datoSeleccionadoBuilder.append(campo);
				}
			}
		}

		prontFrases.setText(null);
		prontInfo.setText(null);
		prontInfo.setOpacity(1);
		prontFrases.setOpacity(0);

		String datoSeleccionado = datoSeleccionadoBuilder.toString();
		if (!libreria.numeroResultados(comic) && !datoSeleccionado.isEmpty()) {
			// Show error message in red when no search fields are specified
			prontInfo.setStyle("-fx-text-fill: red;");
			prontInfo.setText("Error: No existe comic con los datos: " + datoSeleccionado);
		} else if (datoSeleccionado.isEmpty()) {
			prontInfo.setStyle("-fx-text-fill: red;");
			prontInfo.setText("Error: No has seleccionado ningun comic para filtrar, se muestran todos.");
		} else {
			int totalComics = libreria.numeroTotalSelecionado(comic);
			prontInfo.setStyle("-fx-text-fill: black;"); // Reset the text color to black
			prontInfo.setText(
					"El número de cómics donde aparece la búsqueda: " + datoSeleccionado + " es: " + totalComics);
		}
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public List<Comic> libreriaCompleta() throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		limpiezaDeDatos();
		List<Comic> listComic = FXCollections.observableArrayList(libreria.libreriaCompleta());

		return listComic;
	}

	/**
	 * Obtiene los datos de los comics de la base de datos y los devuelve en el
	 * textView
	 *
	 * @param listaComic
	 */
	@SuppressWarnings("unchecked")
	public void tablaBBDD(List<Comic> listaComic) {
		tablaBBDD.getColumns().setAll(nombre, caja, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante, referencia);
		tablaBBDD.getItems().setAll(listaComic);
	}

	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public String[] camposComic() {
		String campos[] = new String[13];

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
			campos[12] = "";
		} else {
			campos[12] = cajaActual();
		}

		return campos;
	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	void volverMenu(ActionEvent event) throws IOException {
		JDBC.DBManager.close();
		nav.verAccesoBBDD();

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
