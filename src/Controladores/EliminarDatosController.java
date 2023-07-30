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
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import Funcionamiento.Comic;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

/**
 * Esta clase sirve para eliminar datos de la base de datos, realiza la funcion
 * de cambiar el dato de "estado" de "En posesion" a "Vendido" o de eliminar
 * directamente.
 *
 *
 * @author Alejandro Rodriguez
 */
public class EliminarDatosController implements Initializable {

	@FXML
	private Label label_id_eliminar;

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
	private MenuItem menu_comic_aniadir;

	@FXML
	private MenuItem menu_comic_modificar;

	@FXML
	private MenuItem menu_comic_puntuar;

	@FXML
	private MenuBar menu_navegacion;

	@FXML
	private Menu navegacion_cerrar;

	@FXML
	private Menu navegacion_comic;

	@FXML
	private Menu navegacion_estadistica;

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
	private Button botonEliminar;

	@FXML
	private Button botonVender;

	@FXML
	private Button botonEnVenta;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonbbdd;

	@FXML
	private DatePicker fechaPublicacion;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextArea prontInfo;

	@FXML
	private TextField idComicTratar;

	@FXML
	public TableView<Comic> tablaBBDD;

	@FXML
	private TableColumn<Comic, String> ID;

	@FXML
	private TableColumn<Comic, String> caja;

	@FXML
	private TableColumn<Comic, String> numero;

	@FXML
	private TableColumn<Comic, String> procedencia;

	@FXML
	private TableColumn<Comic, String> variante;

	@FXML
	private TableColumn<Comic, String> dibujante;

	@FXML
	private TableColumn<Comic, String> editorial;

	@FXML
	private TableColumn<Comic, String> formato;

	@FXML
	private TableColumn<Comic, String> fecha;

	@FXML
	private TableColumn<Comic, String> firma;

	@FXML
	private TableColumn<Comic, String> guionista;

	@FXML
	private TableColumn<Comic, String> nombre;

	@FXML
	private TableColumn<Comic, String> puntuacion;

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
	private VBox rootVBox;

	@FXML
	private VBox vboxContenido;

	private Timeline parpadeo;

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

		prontInfo.textProperty().addListener((observable, oldValue, newValue) -> {
			ajustarAnchoVBox(prontInfo, vboxContenido);
		});

		// Asegurarnos de que el VBox ajuste su tamaño correctamente al inicio
		Platform.runLater(() -> ajustarAnchoVBox(prontInfo, vboxContenido));

		Platform.runLater(() -> asignarTooltips());
		Platform.runLater(() -> seleccionarRaw());

		libreria = new DBLibreriaManager();
		try {
			libreria.listasAutoCompletado();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		animacion();
		autoRelleno();
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
	}

	private void ajustarAnchoVBox(TextArea textArea, VBox vbox) {
		// Crear un objeto Text con el contenido del TextArea
		Text text = new Text(textArea.getText());

		// Configurar el mismo estilo que tiene el TextArea
		text.setFont(textArea.getFont());

		// Configurar el ancho y alto del VBox según los límites originales del VBox y
		// el ancho del texto más un margen (opcional)
		double maxWidth = vbox.getMaxWidth();
		double maxHeight = vbox.getMaxHeight();

		// Medir el ancho y alto del texto
		double textWidth = text.getLayoutBounds().getWidth();
		double textHeight = text.getLayoutBounds().getHeight() + 40;

		double newWidth = Math.min(textWidth + 20, maxWidth);
		double newHeight = Math.min(textHeight, maxHeight);

		vbox.setPrefWidth(newWidth);
		vbox.setPrefHeight(newHeight);

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
	}

	private void asignarTooltip(Button boton, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
		boton.setTooltip(tooltip);
	}

	private void asignarTooltip(ComboBox<?> comboBox, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
		comboBox.setTooltip(tooltip);
	}

	public void animacion() {
		parpadeo = new Timeline(
				new KeyFrame(Duration.seconds(0.5), new KeyValue(label_id_eliminar.borderProperty(), Border.EMPTY)),
				new KeyFrame(Duration.seconds(1), new KeyValue(label_id_eliminar.borderProperty(),
						new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)))));
		parpadeo.setCycleCount(Animation.INDEFINITE);
		parpadeo.setAutoReverse(true);

		// Configurar el estilo inicial del Label
		label_id_eliminar.setBorder(Border.EMPTY);
	}

	public void autoRelleno() {
		// Agregar el ChangeListener al TextField idComicTratar
		idComicTratar.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				boolean existeComic = false;
				try {
					existeComic = libreria.checkID(newValue);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (existeComic || newValue.isEmpty()) {

					Comic comic_temp = null;
					try {

						if (idComicTratar == null) {
							idComicTratar.setText("");
						}

						comic_temp = libreria.comicDatos(idComicTratar.getText());
					} catch (SQLException e) {
						e.printStackTrace();
					}

					idComicTratar.setText(idComicTratar.getText());

//	                restablecerCampos();

					String nombreCampo = comic_temp.getNombre();
					nombreComic.getSelectionModel().select(nombreCampo);

					String numeroCampo = comic_temp.getNumero();
					numeroComic.getSelectionModel().select(numeroCampo);

					String varianteCampo = comic_temp.getVariante();
					nombreVariante.getSelectionModel().select(varianteCampo);

					String firmaCampo = comic_temp.getFirma();
					nombreFirma.getSelectionModel().select(firmaCampo);

					String editorialCampo = comic_temp.getEditorial();
					nombreEditorial.getSelectionModel().select(editorialCampo);

					String formato = comic_temp.getFormato();
					nombreFormato.getSelectionModel().select(formato);

					String procedencia = comic_temp.getProcedencia();
					nombreProcedencia.getSelectionModel().select(procedencia);

					String fechaString = comic_temp.getFecha();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

					LocalDate fecha = LocalDate.parse(fechaString, formatter);
					fechaPublicacion.setValue(fecha);

					String guionistaCampo = comic_temp.getGuionista();
					nombreGuionista.getSelectionModel().select(guionistaCampo);

					String dibujanteCampo = comic_temp.getDibujante();
					nombreDibujante.getSelectionModel().select(dibujanteCampo);

					String cajaCampo = comic_temp.getNumCaja();
					numeroCaja.getSelectionModel().select(cajaCampo);

					prontInfo.clear();
					prontInfo.setOpacity(1);
					try {
						prontInfo.setText(libreria.comicDatos(idComicTratar.getText()).toString().replace("[", "")
								.replace("]", ""));
						imagencomic.setImage(libreria.selectorImage(idComicTratar.getText()));

					} catch (SQLException e) {
						e.printStackTrace();
					}
					imagencomic.setImage(libreria.selectorImage(idComicTratar.getText()));
				} else {
					limpiezaDeDatos();
				}
			} else {
				limpiarCombobox();
			}
		});
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

		// Verificar si el TextField idComicTratar está relleno
		idComicTratar.textProperty().addListener((observable, oldValue, newValue) -> {

			// Verificar si el nuevo valor del TextField está vacío
			if (!newValue.trim().isEmpty() && idComicTratar.getText() != null) {
				isUserInput = false; // Establecemos isUserInput en false si el TextField está relleno.
			} else {
				isUserInput = true; // Establecemos isUserInput en true si el TextField está vacío.
			}
			rellenarComboboxEscucha();

		});
	}

	private void rellenarComboboxEscucha() {

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
				comic.getGuionista(), comic.getDibujante(), "", "", "", "");

		String sql = libreria.datosConcatenados(comicTemp);

		if (!sql.isEmpty()) {

			isUserInput = false; // Disable user input during programmatic updates

			if(idComicTratar.getText().isEmpty()) {
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
			}
			


			if (!DBLibreriaManager.nombreComicList.isEmpty()) {
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

		TextFields.bindAutoCompletion(nombreComic.getEditor(), DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(numeroComic.getEditor(), DBLibreriaManager.listaNumeroComic);
		TextFields.bindAutoCompletion(nombreVariante.getEditor(), DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(nombreFirma.getEditor(), DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(nombreEditorial.getEditor(), DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(nombreGuionista.getEditor(), DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(nombreDibujante.getEditor(), DBLibreriaManager.listaDibujante);
		TextFields.bindAutoCompletion(nombreProcedencia.getEditor(), DBLibreriaManager.listaProcedencia);
		TextFields.bindAutoCompletion(nombreFormato.getEditor(), DBLibreriaManager.listaFormato);
		TextFields.bindAutoCompletion(numeroCaja.getEditor(), DBLibreriaManager.listaCaja);

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

	/**
	 * Funcion que permite limpiar los datos en pantalla
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) { // Metodo que permite limpiar los datos de los diferentes campos
		limpiezaDeDatos();
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
	}

	public void limpiezaDeDatos() {
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
		ID.setText("");
		// Clear additional UI elements
		fechaPublicacion.setValue(null);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);

		isUserInput = true; // Re-enable user input after cleanup
		rellenarComboBox();
	}
	
	public void limpiarCombobox() {
		
		isUserInput = false; // Disable user input during cleanup

		
		// Clear all ComboBox text fields and values
		for (ComboBox<String> comboBox : Arrays.asList(nombreComic, numeroComic, nombreFirma, nombreGuionista,
				nombreVariante, numeroCaja, nombreProcedencia, nombreFormato, nombreEditorial, nombreDibujante)) {
			comboBox.setValue("");
			comboBox.getEditor().setText("");
		}
		
		ID.setText("");
		// Clear additional UI elements
		fechaPublicacion.setValue(null);
	}

	public void restablecerCampos() {
		// Restablecer todos los campos de selección
		nombreComic.getSelectionModel().clearSelection();
		numeroComic.getSelectionModel().clearSelection();
		nombreVariante.getSelectionModel().clearSelection();
		nombreFirma.getSelectionModel().clearSelection();
		nombreEditorial.getSelectionModel().clearSelection();
		nombreFormato.getSelectionModel().clearSelection();
		nombreProcedencia.getSelectionModel().clearSelection();
		fechaPublicacion.setValue(null);
		nombreGuionista.getSelectionModel().clearSelection();
		nombreDibujante.getSelectionModel().clearSelection();
		numeroCaja.getSelectionModel().clearSelection();
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
		
		isUserInput = false;
		
		libreria = new DBLibreriaManager();
		libreria.libreriaCompleta();
		utilidad = new Utilidades();
		String id_comic;

		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (idRow != null) {
			id_comic = idRow.getID();
			idComicTratar.setStyle("");
			idComicTratar.setText(id_comic);
			prontInfo.setStyle("");
			prontInfo.setOpacity(1);
			prontInfo.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));

			imagencomic.setImage(libreria.selectorImage(id_comic));
			utilidad.deleteImage();
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
		
		isUserInput = false;
		
		if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
			libreria = new DBLibreriaManager();
			libreria.libreriaCompleta();
			utilidad = new Utilidades();
			String id_comic;

			Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

			if (idRow != null) {
				id_comic = idRow.getID();
				idComicTratar.setStyle("");
				idComicTratar.setText(id_comic);
				prontInfo.setStyle("");
				prontInfo.setOpacity(1);
				prontInfo.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));

				imagencomic.setImage(libreria.selectorImage(id_comic));
				utilidad.deleteImage();
			}
			DBManager.resetConnection();
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
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");

		if (id_comic.length() == 0 || !libreria.comprobarID(id_comic)) {
			String excepcion = "No puedes eliminar un comic si antes no pones un ID valido";
			nav.alertaException(excepcion);

			idComicTratar.setStyle("-fx-background-color: #FF0000;");

			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {
			modificarDatos(id_comic);
			libreria.eliminarComicBBDD(id_comic);
			libreria.reiniciarBBDD();
			libreria.listasAutoCompletado();
			nombreColumnas(); // Llamada a funcion
			tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
		}
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
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");

		if (id_comic.length() == 0 || !libreria.comprobarID(id_comic)) {
			String excepcion = "No puedes eliminar un comic si antes no pones un ID valido";

			nav.alertaException(excepcion);
			idComicTratar.setStyle("-fx-background-color: #FF0000;");
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {

			modificarDatos(id_comic);
			libreria.venderComicBBDD(id_comic);
			libreria.reiniciarBBDD();
			nombreColumnas(); // Llamada a funcion
			tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion

		}
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException {
		modificarColumnas();
		modificarColumnas();
		prontInfo.setOpacity(0);
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
		modificarColumnas();
		modificarColumnas();
//		limpiezaDeDatos();
		tablaBBDD.refresh();
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
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

		String datos[] = camposComic();

		Comic comic = new Comic("", datos[1], datos[11], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7],
				datos[8], datos[9], datos[10], "", "", "", null);

		tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()));
		busquedaGeneral.setText("");

		resultadoBusquedaPront(comic);
	}

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
		puntuacion.setCellValueFactory(new PropertyValueFactory<>("Puntuacion"));

		busquedaRaw(nombre);
		busquedaRaw(variante);
		busquedaRaw(guionista);
		busquedaRaw(dibujante);
		busquedaRaw(firma);
		busquedaRaw(procedencia);
		busquedaRaw(formato);
		busquedaRaw(editorial);
		busquedaRaw(fecha);

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

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty || item == null) {
						setGraphic(null);
					} else {
						String[] nombres = item.split(" - "); // Dividir el dato en caso de contener " - "
						vbox.getChildren().clear(); // Limpiar VBox antes de agregar nuevos elementos

						for (String nombre : nombres) {
							if (!nombre.isEmpty()) {
								Label label;
								if (columna.getText().equalsIgnoreCase("fecha")
										|| columna.getText().equalsIgnoreCase("editorial")
										|| columna.getText().equalsIgnoreCase("formato")
										|| columna.getText().equalsIgnoreCase("variante")
										|| columna.getText().equalsIgnoreCase("origen")) {
									label = new Label(nombre + "\n"); // No se agrega el símbolo en estas columnas
								} else {
									label = new Label("◉ " + nombre + "\n");
								}
								label.getStyleClass().add("hyperlink"); // Agregar clase CSS
								Hyperlink hyperlink = new Hyperlink();
								hyperlink.setGraphic(label);
								hyperlink.setOnAction(event -> {
									try {
										prontInfo.setText(null);
										prontInfo.setOpacity(0);

										columnaSeleccionada(nombre);
										prontInfo.setOpacity(1);
										prontInfo.setText("El número de cómics donde aparece la \nbúsqueda: " + nombre
												+ " es: " + libreria.numeroTotalSelecionado(nombre));
									} catch (SQLException e) {
										e.printStackTrace();
									}
								});
								vbox.getChildren().add(hyperlink);
							}
						}
						setGraphic(vbox);
					}
				}
			};
		});
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
	 * Funcion que modifica el tamaño de los TableColumn
	 */
	public void modificarColumnas() {

		// Set the initial widths of the columns
		nombre.setPrefWidth(140);
		caja.setPrefWidth(37);
		numero.setPrefWidth(45);
		firma.setPrefWidth(85);
		editorial.setPrefWidth(78);
		variante.setPrefWidth(148);
		procedencia.setPrefWidth(75);
		fecha.setPrefWidth(105);
		guionista.setPrefWidth(145);
		dibujante.setPrefWidth(150);
		puntuacion.setPrefWidth(85);
		formato.setPrefWidth(88);

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
		originalWidths.put(puntuacion, puntuacion.getWidth());
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
		tablaBBDD.getItems().clear();
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
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
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
		tablaBBDD.getItems().clear();
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
		tablaBBDD.getItems().clear();
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
		tablaBBDD.getItems().clear();
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
		prontInfo.setOpacity(1);
		prontInfo.setText("Generando fichero de estadisticas . . . ");
		libreria.generar_fichero_estadisticas();
	}

	/**
	 * Según el dato que busquemos a la hora de realizar la búsqueda, aparecerá un
	 * mensaje diferente en el pront.
	 * 
	 * @param comic El objeto Comic utilizado para la búsqueda.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	public void resultadoBusquedaPront(Comic comic) throws SQLException {
		String datoSeleccionado = "";

		if (comic != null) {
			String[] campos = { comic.getNombre(), comic.getVariante(), comic.getProcedencia(), comic.getFormato(),
					comic.getEditorial(), comic.getFecha(), comic.getNumCaja(), comic.getGuionista(),
					comic.getDibujante(), comic.getFirma() };

			for (String campo : campos) {
				if (!campo.isEmpty()) {
					datoSeleccionado = campo;
					break;
				}
			}
		}

		prontInfo.setText(null);
		prontInfo.setOpacity(0);

		if (!datoSeleccionado.isEmpty()) {
			prontInfo.setOpacity(1);
			prontInfo.setText("El número de cómics donde aparece la búsqueda: " + datoSeleccionado + " es: "
					+ libreria.numeroTotalSelecionado(datoSeleccionado));
		}
	}

	//////////////////////////
	//// FUNCIONES/////////////
	//////////////////////////

	/**
	 * Obtiene los datos de los comics de la base de datos y los devuelve en el
	 * textView
	 *
	 * @param listaComic
	 */
	@SuppressWarnings("unchecked")
	public void tablaBBDD(List<Comic> listaComic) {
		tablaBBDD.getColumns().setAll(nombre, caja, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante, puntuacion);
		tablaBBDD.getItems().setAll(listaComic);
	}

	/**
	 * Funcion que permite cambiar de estado o eliminar un comic de la base de
	 * datos.
	 *
	 * @param id
	 * @param sentenciaSQL
	 * @throws SQLException
	 */
	public boolean modificarDatos(String ID) throws SQLException {
		libreria = new DBLibreriaManager();
		if (nav.alertaEliminar()) {
			if (ID.length() != 0) {

				Comic comic = libreria.comicDatos(ID); // Llamada de metodo que contiene el comic que se desea eliminar

				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo
						.setText("Has modificado correctamente: " + comic.toString().replace("[", "").replace("]", ""));
				idComicTratar.setStyle(null);
				imagencomic.setImage(libreria.selectorImage(ID));
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

	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public String[] camposComic() {
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

	/////////////////////////////////
	//// METODO LLAMADA A VENTANA//
	/////////////////////////////////

	/**
	 * Permite abrir y cargar la ventana para IntroducirDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaAniadir(ActionEvent event) {

		nav.verIntroducirDatos();
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
	@FXML
	public void desconectar(ActionEvent event) throws IOException {
		DBManager.close();
		nav.verAccesoBBDD();

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
