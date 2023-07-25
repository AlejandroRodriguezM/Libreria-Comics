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
 *  Version 5.3
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
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
	private TableColumn<Comic, String> puntuacion;

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
	
	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static Utilidades utilidad = null;
	
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

		libreria = new DBLibreriaManager();
		try {
			libreria.listasAutoCompletado();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBLibreriaManager.limpiarListas();
		listas_autocompletado();
		modificarColumnas();
		rellenarComboBox();
		restringir_entrada_datos();
		seleccionarRaw();
		lecturaComboBox();

		restringirSimbolos(guionistaAni);
		restringirSimbolos(dibujanteAni);
		restringirSimbolos(varianteAni);
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
			limpiezaDatos();
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
				comic.getGuionista(), comic.getDibujante(), "", "", "");

		String sql = libreria.datosConcatenados(comicTemp);

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
		numeroAni.getEditor().setTextFormatter(validador_Nenteros());
		numeroCaja.getEditor().setTextFormatter(validador_Nenteros());
		numeroCajaAni.getEditor().setTextFormatter(validador_Nenteros());
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

		ObservableList<String> formatoNuevo = FXCollections.observableArrayList(DBLibreriaManager.listaFormato);
		formatoAni.setItems(formatoNuevo);

		ObservableList<String> numeroComicsNuevo = FXCollections
				.observableArrayList(DBLibreriaManager.listaNumeroComic);
		numeroAni.setItems(numeroComicsNuevo);

		ObservableList<String> procedenciaEstadoNuevo = FXCollections
				.observableArrayList(DBLibreriaManager.listaProcedencia);
		procedenciaAni.setItems(procedenciaEstadoNuevo);

		ObservableList<String> cajaComicsNuevo = FXCollections.observableArrayList(DBLibreriaManager.listaCaja);
		numeroCajaAni.setItems(cajaComicsNuevo);

		ObservableList<String> situacionEstado = FXCollections.observableArrayList("En posesion", "Comprado",
				"En venta");
		estadoComic.setItems(situacionEstado);
		estadoComic.getSelectionModel().selectFirst();
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
	}

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


		TextFields.bindAutoCompletion(nombreAni, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(varianteAni, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(firmaAni, DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(editorialAni, DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(guionistaAni, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(dibujanteAni, DBLibreriaManager.listaDibujante);
		TextFields.bindAutoCompletion(numeroAni.getEditor(), DBLibreriaManager.listaNumeroComic);
		TextFields.bindAutoCompletion(procedenciaAni.getEditor(), DBLibreriaManager.listaProcedencia);
		TextFields.bindAutoCompletion(formatoAni.getEditor(), DBLibreriaManager.listaFormato);
		TextFields.bindAutoCompletion(numeroCajaAni.getEditor(), DBLibreriaManager.listaCaja);
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
		numeroAni.getEditor().clear();
		nombreAni.setText("");
		firmaAni.setText("");
		editorialAni.setText("");
		fechaAni.setValue(null);
		guionistaAni.setText("");
		dibujanteAni.setText("");
		numeroCajaAni.getEditor().clear();
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		botonNuevaPortada.setStyle(null);
		imagencomic.setImage(null);
		borrarErrores();
	}

	public void limpiezaDatos() {
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
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);

		isUserInput = true; // Re-enable user input after cleanup
		rellenarComboBox();
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

		String formatoEstado = "Grapa";
		if (nombreFormato.getSelectionModel().getSelectedItem() != null) {
			formatoEstado = formatoAni.getSelectionModel().getSelectedItem().toString();
		}
		return formatoEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaNueva() {

		String procedenciaEstadoNuevo = "USA";
		if (nombreProcedencia.getSelectionModel().getSelectedItem() != null) {
			procedenciaEstadoNuevo = procedenciaAni.getSelectionModel().getSelectedItem().toString();
		}
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

	public void columnaSeleccionada(String rawSelecionado) throws SQLException {
		prontInfo.setOpacity(0);
		limpiezaDatosAni();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaSeleccionado(rawSelecionado));

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
		Comic comic = new Comic();
		String datos[] = camposComicActuales();
		String fecha = datos[8];

		if (datos[8].isEmpty()) {
			fecha = "";
		} else {
			fecha = datos[8];
		}

		comic = new Comic("", datos[1], datos[12], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7], fecha,
				datos[9], datos[10], "", "", null);

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

	/**
	 * funcion que obtiene los datos de los comics de la base de datos y los
	 * devuelve en el textView
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

				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("Error. Debes de introducir los datos correctos.");

				nav.alertaException(excepcion);
			} else {

				String codigo_imagen = Utilidades.generarCodigoUnico(sourcePath + File.separator);

				utilidad.nueva_imagen(portada, codigo_imagen); // Se genera el fichero del comic y el nuevo nombre de
																// este

//				String nueva_portada = utilidad.obtenerNombreCompleto(comic);
				comic.setImagen(sourcePath + File.separator + codigo_imagen + ".jpg");
				libreria.insertarDatos(comic);

				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo.setText(
						"Has introducido correctamente: " + comic.toString().replace("[", "").replace("]", ""));
				libreria.listasAutoCompletado();
				nombreColumnas(); // Llamada a funcion
				tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
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

		campos[1] = numeroComicNuevo();

		campos[2] = varianteAni.getText();

		campos[3] = firmaAni.getText();

		campos[4] = editorialAni.getText();

		campos[5] = formatoNuevo();

		campos[6] = procedenciaNueva();

		LocalDate fecha = fechaAni.getValue();
		if (fecha != null) {
			campos[7] = fecha.toString();
		} else {
			campos[7] = "2000-01-01";
		}

		campos[8] = guionistaAni.getText();

		campos[9] = dibujanteAni.getText();

		campos[10] = direccionImagen.getText();

		campos[11] = estadoNuevo();

		campos[12] = cajaNueva();

		return utilidad.comaPorGuion(campos);
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