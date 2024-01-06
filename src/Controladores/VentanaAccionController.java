/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
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
 *  Version 8.0.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.controlsfx.control.textfield.TextFields;
import org.json.JSONException;

import Apis.ApiISBNGeneral;
import Apis.ApiMarvel;
import Apis.WebScraperPreviewsWorld;
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
import javafx.scene.Node;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Clase controladora para la ventana de acciones, que gestiona la interfaz de
 * usuario y las operaciones relacionadas con los cómics.
 */
public class VentanaAccionController implements Initializable {

	/**
	 * Campo de texto para la dirección de la imagen.
	 */
	@FXML
	private TextField direccionImagen;

	/**
	 * Columna de la tabla para mostrar el nombre del cómic.
	 */
	@FXML
	private TableColumn<Comic, String> nombre;

	/**
	 * Columna de la tabla para mostrar el número del cómic.
	 */
	@FXML
	private TableColumn<Comic, String> numero;

	/**
	 * Columna de la tabla para mostrar la variante del cómic.
	 */
	@FXML
	private TableColumn<Comic, String> variante;

	/**
	 * Columna de la tabla para mostrar la editorial del cómic.
	 */
	@FXML
	private TableColumn<Comic, String> editorial;

	/**
	 * Columna de la tabla para mostrar el guionista del cómic.
	 */
	@FXML
	private TableColumn<Comic, String> guionista;

	/**
	 * Columna de la tabla para mostrar el dibujante del cómic.
	 */
	@FXML
	private TableColumn<Comic, String> dibujante;

	/**
	 * Botón para agregar puntuación a un cómic.
	 */
	@FXML
	private Button botonAgregarPuntuacion;

	/**
	 * Botón para borrar una opinión.
	 */
	@FXML
	private Button botonBorrarOpinion;

	/**
	 * Botón para realizar una búsqueda por código.
	 */
	@FXML
	private Button botonBusquedaCodigo;

	/**
	 * Botón para realizar una búsqueda avanzada.
	 */
	@FXML
	private Button botonBusquedaAvanzada;

	/**
	 * Botón para eliminar un cómic.
	 */
	@FXML
	private Button botonEliminar;

	/**
	 * Botón para introducir un cómic.
	 */
	@FXML
	private Button botonIntroducir;

	/**
	 * Botón para limpiar campos.
	 */
	@FXML
	private Button botonLimpiar;

	/**
	 * Botón para modificar un cómic.
	 */
	@FXML
	private Button botonModificarComic;

	/**
	 * Botón para buscar mediante parametro un cómic.
	 */
	@FXML
	private Button botonParametroComic;

	/**
	 * Botón para vender un cómic.
	 */
	@FXML
	private Button botonVender;

	/**
	 * Botón para acceder a la base de datos.
	 */
	@FXML
	private Button botonbbdd;

	/**
	 * Botón para guardar un comic correctamente para el importado de comics
	 * mediante fichero.
	 */
	@FXML
	private Button botonGuardarComic;

	/**
	 * Boton que guarda un cambio en un comic especifico de los importados
	 */
	@FXML
	private Button botonGuardarCambioComic;

	/**
	 * Boton que elimina un comic seleccionado de los comics importados mediante
	 * fichero
	 */
	@FXML
	private Button botonEliminarImportadoComic;

	// Campos de texto (TextField)
	/**
	 * Campo de texto para la búsqueda por código.
	 */
	@FXML
	private TextField busquedaCodigo;

	/**
	 * Campo de texto para el dibujante del cómic.
	 */
	@FXML
	private TextField dibujanteComic;

	/**
	 * Campo de texto para la editorial del cómic.
	 */
	@FXML
	private TextField editorialComic;

	/**
	 * Campo de texto para la firma del cómic.
	 */
	@FXML
	private TextField firmaComic;

	/**
	 * Campo de texto para el guionista del cómic.
	 */
	@FXML
	private TextField guionistaComic;

	/**
	 * Campo de texto para el ID del cómic a tratar.
	 */
	@FXML
	private TextField idComicTratar;

	/**
	 * Campo de texto para el ID del cómic a tratar en modificacion.
	 */
	@FXML
	private TextField idComicTratar_mod;

	/**
	 * Campo de texto para el codigo del cómic a tratar en modificacion o para
	 * añadir.
	 */
	@FXML
	private TextField codigoComicTratar;

	/**
	 * Campo de texto para el nombre del cómic.
	 */
	@FXML
	private TextField nombreComic;

	/**
	 * Campo de texto para el nombre del Key Issue del cómic.
	 */
	@FXML
	private TextField nombreKeyIssue;

	/**
	 * Campo de texto para el precio del cómic.
	 */
	@FXML
	private TextField precioComic;

	/**
	 * Campo de texto para la URL de referencia del cómic.
	 */
	@FXML
	private TextField urlReferencia;

	/**
	 * Campo de texto para la variante del cómic.
	 */
	@FXML
	private TextField varianteComic;

	// Etiquetas (Label)
	/**
	 * Etiqueta para mostrar la puntuación.
	 */
	@FXML
	private Label labelPuntuacion;

	/**
	 * Etiqueta para mostrar la caja.
	 */
	@FXML
	private Label label_caja;

	/**
	 * Etiqueta para mostrar el dibujante.
	 */
	@FXML
	private Label label_dibujante;

	/**
	 * Etiqueta para mostrar la editorial.
	 */
	@FXML
	private Label label_editorial;

	/**
	 * Etiqueta para mostrar el estado.
	 */
	@FXML
	private Label label_estado;

	/**
	 * Etiqueta para mostrar la fecha.
	 */
	@FXML
	private Label label_fecha;

	/**
	 * Etiqueta para mostrar la firma.
	 */
	@FXML
	private Label label_firma;

	/**
	 * Etiqueta para mostrar el formato.
	 */
	@FXML
	private Label label_formato;

	/**
	 * Etiqueta para mostrar el guionista.
	 */
	@FXML
	private Label label_guionista;

	/**
	 * Etiqueta para mostrar el ID.
	 */
	@FXML
	private Label label_id;

	/**
	 * Etiqueta para mostrar el ID en modificacion.
	 */
	@FXML
	private Label label_id_mod;

	/**
	 * Etiqueta para mostrar el codigo en modificacion o aniadir.
	 */
	@FXML
	private Label label_codigo_comic;

	/**
	 * Etiqueta para mostrar el Key Issue.
	 */
	@FXML
	private Label label_key;

	/**
	 * Etiqueta para mostrar la portada.
	 */
	@FXML
	private Label label_portada;

	/**
	 * Etiqueta para mostrar el precio.
	 */
	@FXML
	private Label label_precio;

	/**
	 * Etiqueta para mostrar la procedencia.
	 */
	@FXML
	private Label label_procedencia;

	/**
	 * Etiqueta para mostrar la referencia.
	 */
	@FXML
	private Label label_referencia;

	// Otros controles (ComboBox, DatePicker, TableView, etc.)
	/**
	 * ComboBox para seleccionar el estado del cómic.
	 */
	@FXML
	private ComboBox<String> estadoComic;

	/**
	 * DatePicker para seleccionar la fecha de publicación del cómic.
	 */
	@FXML
	private DatePicker fechaComic;

	/**
	 * ComboBox para seleccionar el formato del cómic.
	 */
	@FXML
	private ComboBox<String> formatoComic;

	/**
	 * ComboBox para realizar búsquedas por editorial.
	 */
	@FXML
	private ComboBox<String> busquedaEditorial;

	/**
	 * ComboBox para seleccionar el número de caja del cómic.
	 */
	@FXML
	private ComboBox<String> numeroCajaComic;

	/**
	 * ComboBox para seleccionar el número del cómic.
	 */
	@FXML
	private ComboBox<String> numeroComic;

	/**
	 * ComboBox para seleccionar la procedencia del cómic.
	 */
	@FXML
	private ComboBox<String> procedenciaComic;

	/**
	 * ComboBox para seleccionar la puntuación en el menú.
	 */
	@FXML
	private ComboBox<String> puntuacionMenu;

	/**
	 * TableView para mostrar la lista de cómics.
	 */
	@FXML
	private TableView<Comic> tablaBBDD;

	/**
	 * ImageView para mostrar la imagen de fondo.
	 */
	@FXML
	private ImageView imagenFondo;

	/**
	 * ImageView para mostrar la imagen del cómic.
	 */
	@FXML
	private ImageView imagencomic;

	/**
	 * ImageView para mostrar la carga de imagen del comic.
	 */
	@FXML
	private ImageView cargaImagen;

	/**
	 * TextArea para mostrar información de texto.
	 */
	@FXML
	private TextArea prontInfo;

	/**
	 * VBox para el diseño de la interfaz.
	 */
	@FXML
	private VBox rootVBox;

//    @FXML
//    private Button botonImportarFicheroCodigoBarras;
//    
//    @FXML
//    private Button botonMenuCodigoBarras;

	@FXML
	private MenuItem menu_Importar_Fichero_CodigoBarras;

	@FXML
	private MenuItem menu_leer_CodigoBarras;

	@FXML
	private MenuItem menu_comic_aleatoria;

	@FXML
	private MenuItem menu_comic_aniadir;

	@FXML
	private MenuItem menu_comic_eliminar;

	@FXML
	private MenuItem menu_comic_modificar;

	@FXML
	private MenuItem menu_comic_puntuar;

	@FXML
	private MenuItem menu_estadistica_estadistica;

	@FXML
	private MenuBar menu_navegacion;

	@FXML
	private Menu navegacion_cerrar;

	@FXML
	private Menu navegacion_comic;

	@FXML
	private Menu navegacion_estadistica;

	/**
	 * Lista de columnas de la tabla de cómics.
	 */
	private List<TableColumn<Comic, String>> columnList;

	/**
	 * Línea de tiempo para animaciones.
	 */
	private Timeline timeline;

	/**
	 * Línea de tiempo para animaciones.
	 */
	private Timeline timelineCargaImagen;

	/**
	 * Referencia a la ventana (stage).
	 */
	private Stage stage;

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
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	/**
	 * Instancia de la clase FuncionesTableView para el manejo de TableView.
	 */
	private static FuncionesTableView funcionesTabla = new FuncionesTableView();

	/**
	 * Controlador de la ventana del menú principal.
	 */
	MenuPrincipalController menuPrincipal = null;

	/**
	 * Tipo de acción a realizar en la interfaz.
	 */
	private static String TIPO_ACCION;

	/**
	 * Declaramos una lista de ComboBox de tipo String
	 */
	private static List<ComboBox<String>> comboboxes;

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
	 * Mapa que contiene elementos de la interfaz y sus tooltips correspondientes.
	 */
	private static Map<Node, String> tooltipsMap = new HashMap<>();

	private static final String GIF_PATH = "/imagenes/cargaImagen.gif";

	public static List<Comic> comicsImportados = new ArrayList<Comic>();

	private static String id_comic_selecionado = "";

	/**
	 * Inicializa la interfaz de usuario y configura el comportamiento de los
	 * elementos al cargar la vista.
	 *
	 * @param location  La ubicación relativa del archivo FXML.
	 * @param resources Los recursos que pueden ser utilizados por el controlador.
	 */
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
				autoRelleno(idComicTratar_mod);
				rootVBox.toFront();
				mostrarOpcionModificar();
			} else if ("puntuar".equals(TIPO_ACCION)) {
				rootVBox.toFront();
				mostrarOpcionPuntuar();
			} else {
				closeWindow();
			}
		});

		if (!TIPO_ACCION.equals("modificar")) {
			autoRelleno(idComicTratar);
		}

		if (!TIPO_ACCION.equals("aniadir")) {
			navegacion_cerrar.setDisable(true);
			navegacion_cerrar.setVisible(false);
		}

		Platform.runLater(() -> asignarTooltips());

		numeroComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroCajaComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		idComicTratar.setTextFormatter(FuncionesComboBox.validador_Nenteros());
		precioComic.setTextFormatter(FuncionesComboBox.validador_Ndecimales());

		FuncionesTableView.eliminarEspacioInicial(nombreComic);
		FuncionesTableView.restringirSimbolos(editorialComic);
		FuncionesTableView.restringirSimbolos(guionistaComic);
		FuncionesTableView.restringirSimbolos(dibujanteComic);
		FuncionesTableView.restringirSimbolos(varianteComic);

		FuncionesTableView.reemplazarEspaciosMultiples(nombreComic);
		FuncionesTableView.reemplazarEspaciosMultiples(editorialComic);
		FuncionesTableView.reemplazarEspaciosMultiples(guionistaComic);
		FuncionesTableView.reemplazarEspaciosMultiples(dibujanteComic);
		FuncionesTableView.reemplazarEspaciosMultiples(varianteComic);
		FuncionesTableView.reemplazarEspacio(busquedaCodigo);

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

		String[] editorialBusquedas = { "Marvel", "Otras editoriales", "Diamond Code" };
		busquedaCodigo.setDisable(true); // Inicialmente deshabilitado

		// Agregar un controlador de eventos al ComboBox para habilitar/deshabilitar el
		// TextField
		busquedaEditorial.setOnAction(event -> {
			if (busquedaEditorial.getSelectionModel().getSelectedItem() != null) {

				busquedaCodigo.setText("");

				busquedaCodigo.setDisable(false);
			} else {
				busquedaCodigo.setDisable(true);
			}
		});

		busquedaEditorial.setItems(FXCollections.observableArrayList(editorialBusquedas));

		ObservableList<String> puntuaciones = FXCollections.observableArrayList("0/0", "0.5/5", "1/5", "1.5/5", "2/5",
				"2.5/5", "3/5", "3.5/5", "4/5", "4.5/5", "5/5");
		puntuacionMenu.setItems(puntuaciones);
		puntuacionMenu.getSelectionModel().selectFirst();

		establecerDinamismoAnchor();
	}

	/**
	 * Establece el dinamismo en la interfaz gráfica ajustando propiedades de
	 * elementos como tamaños, anchos y máximos.
	 */
	public void establecerDinamismoAnchor() {

		double numColumns = 13; // El número de columnas en tu TableView
		nombre.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		numero.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		variante.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		editorial.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		guionista.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		dibujante.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));

	}

	/**
	 * Establece una lista de ComboBoxes para su uso en la clase
	 * VentanaAccionController.
	 *
	 * @param comboBoxes La lista de ComboBoxes que se desea establecer.
	 */
	public void pasarComboBoxes(List<ComboBox<String>> comboBoxes) {
		VentanaAccionController.comboboxes = comboBoxes;
	}

	/**
	 * Obtiene la lista de ComboBoxes establecida previamente en la clase
	 * VentanaAccionController.
	 *
	 * @return La lista de ComboBoxes configurada en la clase
	 *         VentanaAccionController.
	 */
	public static List<ComboBox<String>> getComboBoxes() {
		return comboboxes;
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
		funcionesTabla.modificarColumnas(tablaBBDD, columnList);
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);
		listaPorParametro(); // Llamada a funcion
	}

	/**
	 * Realiza una búsqueda de cómics en la base de datos según los parámetros
	 * proporcionados. Muestra los resultados en una tabla en la interfaz gráfica y
	 * actualiza elementos visuales.
	 *
	 * @throws SQLException si hay un error al interactuar con la base de datos.
	 */
	public void listaPorParametro() throws SQLException {

		rootVBox.setVisible(true);
		rootVBox.setDisable(false);

		libreria = new DBLibreriaManager();
		utilidad = new Utilidades();
		Comic comic = new Comic();
		String datos[] = camposComicBusqueda();

		comic = new Comic("", // ID
				datos[0], // nombre
				datos[12], // numCaja
				datos[1], // numero
				datos[2], // variante
				datos[3], // firma
				datos[4], // editorial
				datos[5], // formato
				datos[6], // procedencia
				"", // fecha
				datos[8], // guionista
				datos[9], // dibujante
				datos[10], // estado
				"", // key_issue
				"", // puntuacion (no proporcionada en el código)
				null, // imagen
				"", // url_referencia
				"", // precio_comic
				"" // codigo_comic
		);

		funcionesTabla.tablaBBDD(libreria.busquedaParametro(comic, ""), tablaBBDD, columnList);
		prontInfo.setOpacity(1);
		prontInfo.setText(funcionesTabla.resultadoBusquedaPront(comic).getText());
	}

	/**
	 * Muestra todos los registros de la base de datos en la tabla de visualización
	 * y restaura la vista.
	 */
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
		funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);
		funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
	}

	/**
	 * Asigna tooltips a varios elementos en la interfaz gráfica. Estos tooltips
	 * proporcionan información adicional cuando el usuario pasa el ratón sobre los
	 * elementos.
	 */
	public void asignarTooltips() {
		tooltipsMap.put(nombreComic, "Nombre de los cómics / libros / mangas");
		tooltipsMap.put(numeroComic, "Número del cómic / libro / manga");
		tooltipsMap.put(varianteComic, "Nombre de la variante del cómic / libro / manga");

		tooltipsMap.put(botonbbdd, "Botón para acceder a la base de datos");
		tooltipsMap.put(botonLimpiar, "Limpia la pantalla y reinicia todos los valores");

		if ("aniadir".equals(TIPO_ACCION)) {
			tooltipsMap.put(botonIntroducir, "Botón para introducir un cómic");
			tooltipsMap.put(botonBusquedaAvanzada, "Botón para realizar una búsqueda avanzada");
			tooltipsMap.put(botonBusquedaCodigo,
					"Botón para realizar una búsqueda por código. \nEn caso de escoger el la categoria 'Diamond Code'. Añade un codigo valido como 'AUG239105'");
			tooltipsMap.put(busquedaCodigo, "Introduce el ISBN, UPC o Diamond Code");
			tooltipsMap.put(numeroCajaComic, "Número de la caja donde se guarda el cómic / libro / manga");
			tooltipsMap.put(procedenciaComic, "Nombre de la procedencia del cómic / libro / manga");
			tooltipsMap.put(formatoComic, "Nombre del formato del cómic / libro / manga");
			tooltipsMap.put(editorialComic, "Nombre de la editorial del cómic / libro / manga");
			tooltipsMap.put(direccionImagen, "Direccion HTTP de la imagen o local");

			tooltipsMap.put(dibujanteComic,
					"Nombre del dibujante del cómic / libro / manga \nEn caso de tener varios artistas en variante, guionista o dibujante, separalos usando una coma ',' o guion '-'");
			tooltipsMap.put(guionistaComic,
					"Nombre del guionista del cómic / libro / manga \nEn caso de tener varios artistas en variante, guionista o dibujante, separalos usando una coma ',' o guion '-'");
			tooltipsMap.put(nombreKeyIssue,
					"Aqui puedes añadir si el comic tiene o no alguna clave, esto es para coleccionistas. Puedes dejarlo vacío");
			tooltipsMap.put(estadoComic, "Selecciona el estado del cómic");
			tooltipsMap.put(fechaComic, "Selecciona la fecha de publicación del cómic");
			tooltipsMap.put(busquedaEditorial, "Selecciona la distribuidora para buscar");
			tooltipsMap.put(firmaComic, "Nombre de la firma del cómic / libro / manga");
			tooltipsMap.put(codigoComicTratar, "Codigo del comic, sea UPC, ISBN o Diamond Code");

		} else if ("eliminar".equals(TIPO_ACCION)) {
			tooltipsMap.put(botonEliminar, "Botón para eliminar un cómic");
			tooltipsMap.put(botonVender, "Botón para vender un cómic");
			tooltipsMap.put(idComicTratar, "El ID del comic");
			tooltipsMap.put(botonParametroComic, "Botón para buscar un cómic mediante una lista de parametros");

		} else if ("modificar".equals(TIPO_ACCION)) {
			tooltipsMap.put(botonModificarComic, "Botón para modificar un cómic");
			tooltipsMap.put(botonParametroComic, "Botón para buscar un cómic mediante una lista de parametros");
			tooltipsMap.put(busquedaCodigo, "Introduce el ISBN, UPC o Diamond Code");
			tooltipsMap.put(idComicTratar_mod, "El ID del comic");
			tooltipsMap.put(direccionImagen, "Direccion HTTP de la imagen o local");
			tooltipsMap.put(firmaComic, "Nombre de la firma del cómic / libro / manga");
			tooltipsMap.put(numeroCajaComic, "Número de la caja donde se guarda el cómic / libro / manga");
			tooltipsMap.put(procedenciaComic, "Nombre de la procedencia del cómic / libro / manga");
			tooltipsMap.put(formatoComic, "Nombre del formato del cómic / libro / manga");
			tooltipsMap.put(editorialComic, "Nombre de la editorial del cómic / libro / manga");
			tooltipsMap.put(dibujanteComic,
					"Nombre del dibujante del cómic / libro / manga \nEn caso de tener varios artistas en variante, guionista o dibujante, separalos usando una coma ',' o guion '-'");
			tooltipsMap.put(guionistaComic,
					"Nombre del guionista del cómic / libro / manga \nEn caso de tener varios artistas en variante, guionista o dibujante, separalos usando una coma ',' o guion '-'");
			tooltipsMap.put(nombreKeyIssue,
					"Aqui puedes añadir si el comic tiene o no alguna clave, esto es para coleccionistas. Puedes dejarlo vacío");
			tooltipsMap.put(estadoComic, "Selecciona el estado del cómic");
			tooltipsMap.put(fechaComic, "Selecciona la fecha de publicación del cómic");
			tooltipsMap.put(busquedaEditorial, "Selecciona la distribuidora para buscar");
			tooltipsMap.put(botonBusquedaCodigo,
					"Botón para realizar una búsqueda por código. \nEn caso de escoger el la categoria 'Diamond Code'. Añade un codigo valido como 'AUG239105'");

			tooltipsMap.put(codigoComicTratar, "Codigo del comic, sea UPC, ISBN o Diamond Code");

		} else if ("puntuar".equals(TIPO_ACCION)) {
			tooltipsMap.put(botonBorrarOpinion, "Botón para borrar una opinión");
			tooltipsMap.put(botonAgregarPuntuacion, "Botón para agregar una puntuación");
			tooltipsMap.put(puntuacionMenu, "Selecciona una puntuación en el menú");
			tooltipsMap.put(idComicTratar, "El ID del comic");
			tooltipsMap.put(botonParametroComic, "Botón para buscar un cómic mediante una lista de parametros");
		}

		FuncionesTooltips.assignTooltips(tooltipsMap);
	}

	@FXML
	void eliminarComicSeleccionado(ActionEvent event) {
		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (nav.alertaEliminar()) {

			if (idRow != null) {

				String id_comic = idRow.getID();
				comicsImportados.removeIf(c -> c.getID().equals(id_comic));

				tablaBBDD.getItems().clear();
				funcionesTabla.tablaBBDD(comicsImportados, tablaBBDD, columnList); // Llamada a funcion

				id_comic_selecionado = "";

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
				numeroComic.getEditor().clear(); // Limpiar el texto en el ComboBox
				formatoComic.getEditor().clear(); // Limpiar el texto en el ComboBox
				procedenciaComic.getEditor().clear(); // Limpiar el texto en el ComboBox
				estadoComic.getEditor().clear(); // Limpiar el texto en el ComboBox
				urlReferencia.setText("");
				precioComic.setText("");
				direccionImagen.setText("");
				imagencomic.setImage(null);
				numeroCajaComic.getEditor().clear();
				codigoComicTratar.setText("");
			}
		}
	}

	/**
	 * Método para seleccionar y mostrar detalles de un cómic en la interfaz
	 * gráfica. Si la lista de cómics importados no está vacía, utiliza la
	 * información de la lista; de lo contrario, consulta la base de datos para
	 * obtener la información del cómic.
	 * 
	 * @throws SQLException Si se produce un error al acceder a la base de datos.
	 */
	private void seleccionarComics() throws SQLException {
		if (DBLibreriaManager.listaComics.size() > 0) {
			libreria = new DBLibreriaManager();
			libreria.libreriaCompleta();
		}

		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();
		String id_comic = idRow.getID();
		prontInfo.setStyle("");

		if (idRow != null) {

			idComicTratar.setStyle("");
			idComicTratar.setText(id_comic);

			Comic comic_temp = new Comic();

			if (!comicsImportados.isEmpty()) {
				id_comic_selecionado = id_comic;
				comic_temp = devolverComic(id_comic);
			} else {
				comic_temp = libreria.comicDatos(id_comic);
			}

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

			direccionImagen.setText(comic_temp.getImagen());

			codigoComicTratar.setText(comic_temp.getCodigo_comic());

			if (!comicsImportados.isEmpty()) {
				String direccionImagenURL = "";
				direccionImagenURL = convertirRutaAURL(comic_temp.getImagen().replace("\\\\", "/"));

				if (direccionImagenURL == null || direccionImagenURL.isEmpty()) {

					String rutaImagen = "/Funcionamiento/sinPortada.jpg";
					Image imagen = new Image(getClass().getResourceAsStream(rutaImagen));
					imagencomic.setImage(imagen);

				} else {

					cargarImagenAsync(comic_temp.getImagen(), imagencomic);
				}

			} else {
				imagencomic.setImage(libreria.selectorImage(id_comic));
			}

			if (TIPO_ACCION.equals("modificar")) {
				mostrarOpcionModificar();
				idComicTratar_mod.setText(comic_temp.getID());
			}
			prontInfo.setOpacity(0);

		}
		DBManager.resetConnection();
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

		seleccionarComics();
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

			funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion

			seleccionarComics();
		}
	}

	/**
	 * Convierte una ruta de archivo a una URL válida.
	 * 
	 * @param rutaArchivo La ruta del archivo a convertir.
	 * @return La URL generada a partir de la ruta del archivo.
	 */
	private static String convertirRutaAURL(String rutaArchivo) {
		String rutaConBarrasInclinadas = "";

		if (rutaArchivo == null || rutaArchivo.isEmpty()) {
			return null;
		} else {
			rutaConBarrasInclinadas = "file:///" + rutaArchivo.replace("\\", "/");
		}
		return rutaConBarrasInclinadas;
	}

	/**
	 * Devuelve un objeto Comic correspondiente al ID proporcionado.
	 * 
	 * @param id_comic El ID del cómic a buscar.
	 * @return El objeto Comic correspondiente al ID proporcionado. Si no se
	 *         encuentra, devuelve null.
	 */
	private Comic devolverComic(String id_comic) {
		for (Comic comic : comicsImportados) {
			if (comic.getID().equals(id_comic)) {
				return comic;
			}
		}
		// Si no se encuentra el cómic con el ID proporcionado, devolver null
		return null;
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
		funcionesCombo.rellenarComboBoxEstaticos(comboboxesMod, TIPO_ACCION); // Llamada a la función para rellenar
																				// ComboBoxes
	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public void ocultarCampos() {
		// Lista de elementos que deseas ocultar y deshabilitar
		List<Node> elementos = Arrays.asList(tablaBBDD, dibujanteComic, editorialComic, estadoComic, fechaComic,
				firmaComic, formatoComic, guionistaComic, nombreKeyIssue, numeroCajaComic, procedenciaComic,
				urlReferencia, botonBorrarOpinion, puntuacionMenu, labelPuntuacion, botonAgregarPuntuacion, label_id,
				botonVender, botonEliminar, idComicTratar, botonModificarComic, botonBusquedaCodigo, botonIntroducir,
				botonbbdd, precioComic, direccionImagen, label_portada, label_precio, label_caja, label_dibujante,
				label_editorial, label_estado, label_fecha, label_firma, label_formato, label_guionista, label_key,
				label_procedencia, label_referencia, codigoComicTratar, label_codigo_comic);

		// Itera a través de los elementos y oculta/deshabilita cada uno
		for (Node elemento : elementos) {
			elemento.setVisible(false);
			elemento.setDisable(true);
		}
	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public void ocultarCamposMod() {
		// Lista de elementos que deseas ocultar y deshabilitar
		List<Node> elementos = Arrays.asList(nombreKeyIssue, urlReferencia, label_id_mod, idComicTratar_mod,
				precioComic, direccionImagen, label_portada, label_precio, label_key, label_referencia,
				botonModificarComic, codigoComicTratar, label_codigo_comic);

		// Itera a través de los elementos y oculta/deshabilita cada uno
		for (Node elemento : elementos) {
			elemento.setVisible(false);
			elemento.setDisable(true);
		}
	}

	/**
	 * Muestra la opción de eliminar en la interfaz gráfica y habilita los elementos
	 * relacionados.
	 */
	public void mostrarOpcionEliminar() {
		ocultarCampos();

		List<Node> elementosAMostrarYHabilitar = Arrays.asList(label_id, botonVender, botonEliminar, idComicTratar,
				tablaBBDD, botonbbdd, rootVBox, botonParametroComic);

		for (Node elemento : elementosAMostrarYHabilitar) {
			elemento.setVisible(true);
			elemento.setDisable(false);
		}
	}

	/**
	 * Muestra la opción de añadir en la interfaz gráfica y habilita los elementos
	 * relacionados.
	 */
	public void mostrarOpcionAniadir() {
		ocultarCampos();
		List<Node> elementosAMostrarYHabilitar = Arrays.asList(dibujanteComic, editorialComic, estadoComic, fechaComic,
				firmaComic, formatoComic, guionistaComic, nombreKeyIssue, numeroCajaComic, procedenciaComic,
				urlReferencia, botonIntroducir, botonBusquedaAvanzada, precioComic, direccionImagen, label_portada,
				label_precio, label_caja, label_dibujante, label_editorial, label_estado, label_fecha, label_firma,
				label_formato, label_guionista, label_key, label_procedencia, label_referencia, codigoComicTratar,
				label_codigo_comic, tablaBBDD, rootVBox);

		rootVBox.setPrefHeight(230);
		rootVBox.setLayoutY(370);

		for (Node elemento : elementosAMostrarYHabilitar) {
			elemento.setVisible(true);
			elemento.setDisable(false);
		}
	}

	/**
	 * Muestra la opción de modificar en la interfaz gráfica y habilita los
	 * elementos relacionados.
	 */
	public void mostrarOpcionModificar() {
		ocultarCampos();

		List<Node> elementosAMostrarYHabilitar = Arrays.asList(dibujanteComic, editorialComic, estadoComic, fechaComic,
				firmaComic, formatoComic, guionistaComic, nombreKeyIssue, numeroCajaComic, procedenciaComic,
				urlReferencia, botonModificarComic, precioComic, direccionImagen, tablaBBDD, label_portada,
				label_precio, label_caja, label_dibujante, label_editorial, label_estado, label_fecha, label_firma,
				label_formato, label_guionista, label_key, label_procedencia, label_referencia, botonbbdd,
				idComicTratar_mod, label_id_mod, botonParametroComic, codigoComicTratar, label_codigo_comic, rootVBox);

		for (Node elemento : elementosAMostrarYHabilitar) {
			elemento.setVisible(true);
			elemento.setDisable(false);
		}
//		rootVBox.setVisible(false);
	}

	/**
	 * Muestra la opción de puntuar en la interfaz gráfica y habilita los elementos
	 * relacionados.
	 */
	public void mostrarOpcionPuntuar() {
		ocultarCampos();

		List<Node> elementosAMostrarYHabilitar = Arrays.asList(botonBorrarOpinion, puntuacionMenu, labelPuntuacion,
				botonAgregarPuntuacion, idComicTratar, label_id, tablaBBDD, botonbbdd, rootVBox, botonParametroComic);

		for (Node elemento : elementosAMostrarYHabilitar) {
			elemento.setVisible(true);
			elemento.setDisable(false);
		}
	}

	/**
	 * Establece el tipo de acción que se realizará en la ventana.
	 *
	 * @param tipoAccion El tipo de acción a realizar (por ejemplo, "aniadir",
	 *                   "modificar", "eliminar", "puntuar").
	 */
	public static void tipoAccion(String tipoAccion) {
		TIPO_ACCION = tipoAccion;
	}

	/**
	 * Agrega datos a la base de datos y realiza acciones relacionadas al hacer clic
	 * en un botón o realizar una acción.
	 *
	 * @param event El evento que desencadena la acción.
	 * @throws IOException          Si ocurre un error de entrada/salida.
	 * @throws SQLException         Si ocurre un error de SQL.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@FXML
	public void agregarDatos(ActionEvent event)
			throws IOException, SQLException, InterruptedException, ExecutionException {
		libreria = new DBLibreriaManager();
		menuPrincipal = new MenuPrincipalController();

		if (nav.alertaAccionGeneral()) {
			aniadirComicAsync(); // Llama a la función para procesar la subida de un cómic

			libreria.reiniciarBBDD(); // Reinicia la base de datos
//			direccionImagen.setText(""); // Limpia el campo de texto de la dirección de la imagen

		} else {
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Se ha cancelado la subida del nuevo comic.");
		}

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
		if (comprobarID(id_comic)) {
			prontInfo.setOpacity(1);
			libreria.actualizarPuntuacion(id_comic, comicPuntuacion()); // Llamada a funcion
			prontInfo.setText("Deseo concedido. Has añadido el nuevo comic.");

			List<ComboBox<String>> comboboxes = VentanaAccionController.getComboBoxes();

			funcionesCombo.rellenarComboBox(comboboxes);

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

		if (comprobarID(id_comic)) {
			libreria.borrarPuntuacion(id_comic);
			prontInfo.setOpacity(1);
			prontInfo.setText("Deseo concedido. Has borrado la puntuacion del comic.");

			Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
			imagenFondo.setImage(nuevaImagen);

			List<ComboBox<String>> comboboxes = VentanaAccionController.getComboBoxes();

			funcionesCombo.rellenarComboBox(comboboxes);

		}
	}

	/**
	 * Maneja la acción de búsqueda avanzada, verifica las claves API de Marvel y
	 * Comic Vine.
	 *
	 * @param event El evento de acción.
	 */
	@FXML
	void busquedaAvanzada(ActionEvent event) {
		String claves[] = Utilidades.clavesApiMarvel();
		String apiKey = Utilidades.cargarApiComicVine();
		String exception = "";

		// Verificar si las claves API están ausentes o vacías
		if (claves.length == 0 || apiKey.isEmpty()) {
			if (claves.length == 0) {
				exception += "\nDebes obtener una clave API de Marvel. Visita https://developer.marvel.com/";
			}
			if (apiKey.isEmpty()) {
				exception += "\nDebes obtener una clave API de Comic Vine. Visita https://comicvine.gamespot.com/api/ (gratuito)";
			}
			nav.alertaNoApi(exception); // Mostrar alerta de error
		} else {
			// Continuar con la lógica cuando ambas claves están presentes
			if (busquedaEditorial.isVisible()) {
				ocultarElementosBusqueda();
			} else {
				mostrarElementosBusqueda();
			}
		}
	}

	/**
	 * Método asociado al evento de acción que se dispara al seleccionar la opción
	 * "Ver Menú Código de Barras". Invoca el método correspondiente en el objeto
	 * 'nav' para mostrar el menú de códigos de barras.
	 *
	 * @param event Objeto que representa el evento de acción.
	 */
	@FXML
	void verMenuCodigoBarras(ActionEvent event) {

		if ("aniadir".equals(TIPO_ACCION)) {
			nav.verMenuCodigosBarra();
		}
	}

	/**
	 * Método asociado al evento de acción que se dispara al seleccionar la opción
	 * "Importar Fichero Código de Barras". Este método aún no tiene implementación.
	 *
	 * @param evento Objeto que representa el evento de acción.
	 */
	@FXML
	void importarFicheroCodigoBarras(ActionEvent evento) {
		// Implementación pendiente

		String frase = "Fichero txt";

		String formato = "*.txt";

		File fichero = Utilidades.tratarFichero(frase, formato).showOpenDialog(null); // Llamada a funcion

		if (fichero != null) {
			Platform.runLater(() -> codigosFichero(fichero));

		}

	}

	/**
	 * Lee códigos desde un archivo y realiza búsquedas por código de importación.
	 *
	 * @param fichero El archivo que contiene los códigos a procesar.
	 */
	private void codigosFichero(File fichero) {
		try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = br.readLine()) != null) {

				busquedaPorCodigoImportacion(linea);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Oculta los elementos de búsqueda en la interfaz de usuario.
	 */
	private void ocultarElementosBusqueda() {
		botonBusquedaCodigo.setVisible(false);
		busquedaEditorial.setVisible(false);
		busquedaCodigo.setVisible(false);

		botonBusquedaCodigo.setDisable(true);
		busquedaEditorial.setDisable(true);

		// Restaurar valores predeterminados en el ComboBox
		busquedaEditorial.getSelectionModel().clearSelection(); // Desseleccionar cualquier elemento seleccionado
		busquedaEditorial.getEditor().clear(); // Limpiar el texto en el ComboBox
		busquedaEditorial.setPromptText("Buscar Editorial"); // Restaurar el texto de marcador de posición original
	}

	/**
	 * Muestra los elementos de búsqueda en la interfaz de usuario.
	 */
	private void mostrarElementosBusqueda() {
		botonBusquedaCodigo.setVisible(true);
		botonBusquedaCodigo.setDisable(false);

		busquedaEditorial.setVisible(true);
		busquedaCodigo.setVisible(true);

		busquedaEditorial.setDisable(false);

	}

	/**
	 * Realiza una búsqueda por código y muestra información del cómic
	 * correspondiente en la interfaz gráfica.
	 *
	 * @param event El evento que desencadena la acción.
	 * @throws IOException        Si ocurre un error de entrada/salida.
	 * @throws JSONException      Si ocurre un error al procesar datos JSON.
	 * @throws URISyntaxException Si ocurre un error de sintaxis de URI.
	 */
	@FXML
	void busquedaPorCodigo(ActionEvent event) throws IOException, JSONException, URISyntaxException {
//		limpiarDatosPantalla();

		ApiISBNGeneral isbnGeneral = new ApiISBNGeneral();
		WebScraperPreviewsWorld previewsScraper = new WebScraperPreviewsWorld();

		// Crear una tarea que se ejecutará en segundo plano
		Task<Boolean> tarea = new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {

				String valorCodigo = Utilidades.eliminarEspacios(busquedaCodigo.getText());
				String tipoEditorial = busquedaEditorial.getValue();
				String[] comicInfo = null;

				if (!valorCodigo.isEmpty() && !tipoEditorial.isEmpty()) {
					if (tipoEditorial.equalsIgnoreCase("marvel")) {
						comicInfo = ApiMarvel.infoComicCode(valorCodigo.trim(), prontInfo);
					} else if (tipoEditorial.equalsIgnoreCase("Diamond Code")) {
						comicInfo = previewsScraper.displayComicInfo(valorCodigo.trim(), prontInfo);
					} else {
						comicInfo = isbnGeneral.getBookInfo(valorCodigo.trim(), prontInfo);
					}

					if (comprobarCodigo(comicInfo)) {
						// Rellenar campos con la información del cómic

						rellenarCamposAni(comicInfo);
						codigoComicTratar.setText(valorCodigo.trim());

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

	/**
	 * Verifica si se ha encontrado información válida para el cómic.
	 *
	 * @param comicInfo Un arreglo de strings con información del cómic.
	 * @return True si la información es válida y existe; de lo contrario, False.
	 */
	private boolean comprobarCodigo(String[] comicInfo) {
		boolean existe = true;
		if (comicInfo == null || comicInfo.length <= 0) {
			existe = false;
		}
		return existe;
	}

	/**
	 * Rellena los campos de la interfaz gráfica con la información del cómic
	 * proporcionada.
	 *
	 * @param comicInfo Un arreglo de strings con información del cómic.
	 * @throws IOException
	 */
	private void rellenarCamposAni(String[] comicInfo) throws IOException {

		Platform.runLater(() -> {

			Image imagen = null;
			String titulo = comicInfo[0];
			String issueKey = comicInfo[1];
			String numero = comicInfo[2];
			String formato = comicInfo[3];
			String precio = comicInfo[4];
			String variante = comicInfo[5];
			String dibujantes = comicInfo[6];
			String escritores = comicInfo[7];
			String fechaVenta = comicInfo[8];
			String referencia = comicInfo[9];
			String urlImagen = comicInfo[10];
			String editorial = comicInfo[11];

			nombreComic.setText(titulo);

			numeroComic.setValue(numero);

			varianteComic.setText(variante);
			editorialComic.setText(editorial);
			formatoComic.setValue(formato);

			// Parsear y establecer la fecha
			LocalDate fecha;

			if (fechaVenta == null || fechaVenta.isEmpty()) {
				// La cadena de fecha no existe o es nula, establecer la fecha predeterminada
				fecha = LocalDate.of(2000, 1, 1);
			} else {
				// La cadena de fecha existe, parsearla
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				fecha = LocalDate.parse(fechaVenta, formatter);
			}

			if (urlImagen == null || urlImagen.isEmpty()) {
				// Cargar la imagen desde la URL
				String rutaImagen = "/Funcionamiento/sinPortada.jpg";
				imagen = new Image(getClass().getResourceAsStream(rutaImagen));
				imagencomic.setImage(imagen);
			} else {
				// Cargar la imagen desde la URL
				cargarImagenAsync(urlImagen, imagencomic);
			}
			fechaComic.setValue(fecha);

			guionistaComic.setText(escritores);
			dibujanteComic.setText(dibujantes);
			direccionImagen.setText(urlImagen);
			precioComic.setText(precio);
			urlReferencia.setText(referencia);

			nombreKeyIssue.setText(issueKey);
		});

	}

	/**
	 * Rellena los campos de la interfaz gráfica con la información del cómic
	 * proporcionada.
	 *
	 * @param comicInfo Un arreglo de strings con información del cómic.
	 * @throws IOException
	 */
	private void rellenarTablaImport(String[] comicInfo, String codigo_comic) throws IOException {
		Platform.runLater(() -> {
			String id = "A" + 0 + "" + comicsImportados.size() + 1;
			String titulo = defaultIfNullOrEmpty(comicInfo[0], "Vacio");
			String issueKey = defaultIfNullOrEmpty(comicInfo[1], "Vacio");
			String numero = defaultIfNullOrEmpty(comicInfo[2], "0");
			String variante = defaultIfNullOrEmpty(comicInfo[5], "Vacio");
			String precio = defaultIfNullOrEmpty(comicInfo[4], "0");
			String dibujantes = defaultIfNullOrEmpty(comicInfo[6], "Vacio");
			String escritores = defaultIfNullOrEmpty(comicInfo[7], "Vacio");
			String fechaVenta = comicInfo[8];
			LocalDate fecha = parseFecha(fechaVenta);

			String referencia = defaultIfNullOrEmpty(comicInfo[9], "Vacio");
			String urlImagen = comicInfo[10];
			String editorial = defaultIfNullOrEmpty(comicInfo[11], "Vacio");

			File file = new File(urlImagen);

			if (urlImagen == null) {
				// Si hubo un error al descargar la imagen, agregar la ruta de la imagen
				// predeterminada desde los recursos
				String rutaImagen = "/Funcionamiento/sinPortada.jpg";
				URL url = getClass().getResource(rutaImagen);
				if (url != null) {
					urlImagen = url.toExternalForm();
				} else {
					System.err.println("Error al obtener la ruta de la imagen predeterminada.");
				}
			} else {
				file = new File(urlImagen);
				urlImagen = file.toString();
			}

			String correctedUrl = urlImagen.replace("\\", "/").replace("http:", "https:").replace("https:", "https:/");

			String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);

			URI uri = null;
			try {
				uri = new URI(correctedUrl);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String urlFinal = SOURCE_PATH + File.separator + codigo_imagen + ".jpg";

			Utilidades.descargarYConvertirImagenAsync(uri, SOURCE_PATH, codigo_imagen);

			Comic comic = new Comic(id, titulo, "0", numero, variante, "", editorial, formato(), procedencia(),
					fecha.toString(), escritores, dibujantes, estado(), issueKey, "Sin puntuar", urlFinal, referencia,
					precio, codigo_comic);

			comicsImportados.add(comic);

			funcionesTabla.nombreColumnas(columnList, tablaBBDD);
			funcionesTabla.tablaBBDD(comicsImportados, tablaBBDD, columnList);
		});
	}

	/**
	 * Devuelve el valor predeterminado si la cadena dada es nula o vacía, de lo
	 * contrario, devuelve la cadena original.
	 *
	 * @param value        Cadena a ser verificada.
	 * @param defaultValue Valor predeterminado a ser devuelto si la cadena es nula
	 *                     o vacía.
	 * @return Cadena original o valor predeterminado.
	 */
	private String defaultIfNullOrEmpty(String value, String defaultValue) {
		return (value == null || value.isEmpty()) ? defaultValue : value;
	}

	/**
	 * Parsea la cadena de fecha y devuelve la fecha correspondiente. Si la cadena
	 * es nula o vacía, devuelve la fecha actual.
	 *
	 * @param fechaVenta Cadena de fecha a ser parseada.
	 * @return Objeto LocalDate que representa la fecha parseada.
	 */
	private LocalDate parseFecha(String fechaVenta) {
		if (fechaVenta == null || fechaVenta.isEmpty()) {
			return LocalDate.of(2000, 1, 1); // Obtener la fecha actual si la cadena de fecha no está presente
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			return LocalDate.parse(fechaVenta, formatter);
		}
	}

	/**
	 * Realiza una búsqueda utilizando un código de importación y muestra los
	 * resultados en la interfaz gráfica.
	 *
	 * @param valorCodigo El código de importación a buscar.
	 */
	private void busquedaPorCodigoImportacion(String valorCodigo) {
		limpiarDatosPantalla();
		ApiISBNGeneral isbnGeneral = new ApiISBNGeneral();
		WebScraperPreviewsWorld previewsScraper = new WebScraperPreviewsWorld();
		botonGuardarCambioComic.setVisible(true);
		botonGuardarComic.setVisible(true);
		botonEliminarImportadoComic.setVisible(true);
		// Crear una tarea que se ejecutará en segundo plano
		Task<Boolean> tarea = new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {

				final String finalValorCodigo = Utilidades.eliminarEspacios(valorCodigo).replace("-", "");

				String[] comicInfo = null;

				if (!finalValorCodigo.isEmpty()) {

					if (finalValorCodigo.length() == 9) {
						comicInfo = previewsScraper.displayComicInfo(finalValorCodigo.trim(), prontInfo);

					} else {
						comicInfo = ApiMarvel.infoComicCode(finalValorCodigo.trim(), prontInfo);

						if (comicInfo == null) {
							comicInfo = isbnGeneral.getBookInfo(finalValorCodigo.trim(), prontInfo);
						}
					}

					if (comprobarCodigo(comicInfo)) {
						rellenarTablaImport(comicInfo, finalValorCodigo.trim());
						return true;
					}
				}
				return false;
			}
		};

		iniciarAnimacionCargaImagen();

		// Configurar un manejador de eventos para actualizar la interfaz de usuario
		// cuando la tarea esté completa
		tarea.setOnSucceeded(ev -> {
			Platform.runLater(() -> {
				prontInfo.setOpacity(0);
				prontInfo.setText("");
				detenerAnimacionCargaImagen();
			});
		});

		tarea.setOnFailed(ev -> {
			Platform.runLater(() -> {
				detenerAnimacionCargaImagen();
			});
		});

		// Iniciar la tarea en un nuevo hilo
		Thread thread = new Thread(tarea);
		thread.setDaemon(true); // Hacer que el hilo sea demonio para que se cierre al
								// salir de la aplicación
		// Iniciar la tarea
		thread.start();
	}

	/**
	 * Carga una imagen de forma asíncrona desde una URL y la muestra en un
	 * ImageView.
	 *
	 * @param urlImagen La URL de la imagen a cargar.
	 * @param imageView El ImageView en el que se mostrará la imagen cargada.
	 */
	public void cargarImagenAsync(String urlImagen, ImageView imageView) {
		Task<Image> cargarImagenTask = new Task<Image>() {
			@Override
			protected Image call() throws Exception {

				return new Image(urlImagen, 250, 0, true, true);
			}
		};

		iniciarAnimacionCargaImagen();

		cargarImagenTask.setOnSucceeded(event -> {
			Image imagenCargada = cargarImagenTask.getValue();
			imageView.setImage(imagenCargada);
			detenerAnimacionCargaImagen();
			detenerAnimacionPront();
		});

		cargarImagenTask.setOnFailed(ev -> {
			Platform.runLater(() -> {
				detenerAnimacionCargaImagen();
				detenerAnimacionPront();
			});
		});

		Thread thread = new Thread(cargarImagenTask);
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Inicia una animación que alterna entre dos imágenes en un ImageView para
	 * lograr un efecto visual llamativo. La animación se ejecuta de forma
	 * indefinida y cambia las imágenes cada 0.1 segundos.
	 */
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
	 * Inicia la animación de carga de imagen, mostrando un GIF animado en un bucle
	 * continuo.
	 */
	private void iniciarAnimacionCargaImagen() {
		Image gif = cargarGif(GIF_PATH);

		// Establecer la imagen inicial en el ImageView
		cargaImagen.setImage(gif);

		// Configurar la opacidad inicial
		cargaImagen.setOpacity(1);

		timelineCargaImagen = new Timeline();
		timelineCargaImagen.setCycleCount(Timeline.INDEFINITE);

		// Agregar el keyframe para cambiar la imagen
		KeyFrame cambiarGif = new KeyFrame(Duration.ZERO, new KeyValue(cargaImagen.imageProperty(), gif));

		// Agregar el keyframe al timeline
		timelineCargaImagen.getKeyFrames().add(cambiarGif);

		// Iniciar la animación
		timelineCargaImagen.play();
	}

	/**
	 * Carga un GIF desde la ruta proporcionada.
	 *
	 * @param path Ruta del archivo GIF.
	 * @return La imagen del GIF cargado.
	 */
	private Image cargarGif(String path) {
		InputStream gifStream = getClass().getResourceAsStream(path);
		return new Image(gifStream);
	}

	/**
	 * Metodo que permite detener una animacion
	 */
	private void detenerAnimacionPront() {
		if (timeline != null) {
			timeline.stop();
			timeline = null; // Destruir el objeto timeline

			Platform.runLater(() -> {
				InputStream imagenStream = getClass().getResourceAsStream("/imagenes/accionComic.jpg");
				Image imagen = new Image(imagenStream);
				imagenFondo.setImage(imagen);
			});
		}
	}

	private void detenerAnimacionCargaImagen() {
		if (timelineCargaImagen != null) {
			timelineCargaImagen.stop();
			timelineCargaImagen = null; // Destruir el objeto timeline

			Platform.runLater(() -> {
				cargaImagen.setImage(null);
				cargaImagen.setVisible(false);
			});
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
	 * Funcion que permite cambiar de estado o eliminar un comic de la base de
	 * datos.
	 *
	 * @param id
	 * @param sentenciaSQL
	 * @throws SQLException
	 */
	public boolean comprobarID(String ID) throws SQLException {
		libreria = new DBLibreriaManager();
		if (nav.alertaAccionGeneral()) {
			if (libreria.checkID(ID)) {
				idComicTratar.setStyle(null);

				funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
				funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);
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
			idComicTratar.setStyle(null);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Accion cancelada");
			return false;
		}
	}

	/**
	 * Limpia los datos de la pantalla al hacer clic en el botón "Limpiar".
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		limpiarDatosPantalla();
		if ("modificar".equals(TIPO_ACCION)) {
			mostrarOpcionModificar();
		}

	}

	/**
	 * Limpia y restablece todos los campos de datos en la sección de animaciones a
	 * sus valores predeterminados. Además, restablece la imagen de fondo y oculta
	 * cualquier mensaje de error o información.
	 */
	public void limpiarDatosPantalla() {
		// Restablecer la imagen de fondo a su valor predeterminado
		Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComic.jpg"));
		imagenFondo.setImage(nuevaImagen);

		// Restablecer los campos de datos
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
		numeroComic.getEditor().clear(); // Limpiar el texto en el ComboBox
		formatoComic.getEditor().clear(); // Limpiar el texto en el ComboBox
		procedenciaComic.getEditor().clear(); // Limpiar el texto en el ComboBox
		estadoComic.getEditor().clear(); // Limpiar el texto en el ComboBox
		urlReferencia.setText("");
		precioComic.setText("");
		direccionImagen.setText("");
		imagencomic.setImage(null);
		numeroCajaComic.getEditor().clear();
		codigoComicTratar.setText("");
		botonGuardarComic.setVisible(false);
		botonGuardarCambioComic.setVisible(false);
		tablaBBDD.getItems().clear();
		botonEliminarImportadoComic.setVisible(false);

		if (comicsImportados.size() > 0) {
			comicsImportados.clear();
		}
		// Borrar cualquier mensaje de error presente
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
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@FXML
	void modificarDatos(ActionEvent event)
			throws NumberFormatException, SQLException, IOException, InterruptedException, ExecutionException {

		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar_mod.getText();
		idComicTratar.setStyle("");
		if (comprobarID(id_comic)) {
			modificacionComicAsync(); // Llamada a funcion que modificara el contenido de un comic especifico.
			libreria.reiniciarBBDD();
			libreria.listasAutoCompletado();

			List<ComboBox<String>> comboboxes = VentanaAccionController.getComboBoxes();

			if (comboboxes != null) {
				funcionesCombo.rellenarComboBox(comboboxes);
			}

		}

	}

	/**
	 * Funcion que permite la subida de una
	 *
	 * @param event
	 */
	@FXML
	void nuevaPortada(MouseEvent event) {
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

		detenerAnimacionPront();
		iniciarAnimacionCambioImagen();
		if (comprobarID(id_comic)) {
			libreria.venderComicBBDD(id_comic);
			libreria.reiniciarBBDD();

			prontInfo.setOpacity(1);
			prontInfo.setText("Deseo concedido. Has puesto a la venta el comic");
			Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
			imagenFondo.setImage(nuevaImagen);

			List<ComboBox<String>> comboboxes = VentanaAccionController.getComboBoxes();

			funcionesCombo.rellenarComboBox(comboboxes);

		}
		detenerAnimacionPront();

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

		String estadoNuevo = "En posesion";

		if (estadoComic.getSelectionModel().getSelectedItem() != null) {
			estadoNuevo = estadoComic.getSelectionModel().getSelectedItem().toString();
		}

		return estadoNuevo;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreFormato" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String formato() {

		String formatoEstado = "Grapa (Issue individual)";
		if (formatoComic.getSelectionModel().getSelectedItem() != null) {
			formatoEstado = formatoComic.getSelectionModel().getSelectedItem().toString();
		}
		return formatoEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedencia() {

		String procedenciaEstadoNuevo = "Estados Unidos (United States)";
		if (procedenciaComic.getSelectionModel().getSelectedItem() != null) {
			procedenciaEstadoNuevo = procedenciaComic.getSelectionModel().getSelectedItem().toString();
		}

		return procedenciaEstadoNuevo;
	}

	/**
	 * Método que maneja el evento de guardar los datos de un cómic.
	 * 
	 * @param event El evento de acción que desencadena la llamada al método.
	 */
	@FXML
	void guardarDatos(ActionEvent event) {

		LocalDate fecha_comic;
		
		if (id_comic_selecionado != null) {
			String id_comic = id_comic_selecionado;

			String datos[] = camposComic();

			String nombre = (datos[0] != null || !datos[0].isEmpty()) ? datos[0] : "Vacio";

			String numero = (datos[1] != null || !datos[1].isEmpty()) ? datos[1] : "0";

			String variante = (datos[2] != null || !datos[2].isEmpty()) ? datos[2] : "Vacio";

			String firma = (datos[3] != null || !datos[3].isEmpty()) ? datos[3] : "";

			String editorial = (datos[4] != null || !datos[4].isEmpty()) ? datos[4] : "Vacio";

			String formato = (datos[5] != null || !datos[5].isEmpty()) ? datos[5] : "Grapa (Issue individual)";

			String procedencia = (datos[6] != null || !datos[6].isEmpty()) ? datos[6]
					: "Estados Unidos (United States)";

			if (datos[7] == null) {
				datos[7] = "2000-01-01";
				fecha_comic = LocalDate.parse(datos[7]);
			} else {
				fecha_comic = LocalDate.parse(datos[7]);
			}

			String guionista = (datos[8] != null || !datos[8].isEmpty()) ? datos[8] : "Vacio";

			String dibujante = (datos[9] != null || !datos[9].isEmpty()) ? datos[9] : "Vacio";

			String portada = (datos[10] != null || !datos[10].isEmpty()) ? datos[10] : "";

			String estado = (datos[11] != null || !datos[11].isEmpty()) ? datos[11] : "Comprado";

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

			String url_referencia = (datos[14] != null || !datos[14].isEmpty()) ? datos[14] : "";
			String precio_comic = (datos[15] != null && !datos[15].isEmpty()) ? datos[15] : "0";

			double valor_comic = Double.parseDouble(precio_comic);

			precio_comic = String.valueOf(Utilidades.convertirMonedaADolar(procedencia, valor_comic));

			if (url_referencia.isEmpty()) {
				url_referencia = "Sin referencia";
			}

			String codigo_comic = datos[16];

			Comic comic = new Comic(id_comic, nombre, numCaja, numero, variante, firma, editorial, formato, procedencia,
					fecha_comic.toString(), guionista, dibujante, estado, key_issue, "Sin puntuar", portada,
					url_referencia, precio_comic, codigo_comic);

			for (Comic c : comicsImportados) {
				if (c.getID().equals(id_comic)) {
					comicsImportados.set(comicsImportados.indexOf(c), comic);
					break;
				}
			}

			tablaBBDD.getItems().clear();
			funcionesTabla.tablaBBDD(comicsImportados, tablaBBDD, columnList); // Llamada a funcion
		}

	}

	/**
	 * Método que maneja el evento de guardar la lista de cómics importados.
	 * 
	 * @param event El evento de acción que desencadena la llamada al método.
	 * @throws IOException        Si ocurre un error de entrada/salida.
	 * @throws SQLException       Si ocurre un error de base de datos.
	 * @throws URISyntaxException
	 */
	@FXML
	void guardarListaImportados(ActionEvent event) throws IOException, SQLException, URISyntaxException {
		if (comicsImportados.size() > 0) {
			if (nav.alertaInsertar()) {
				int contador = 0;
				String mensajePront = "";
				detenerAnimacionPront();
				iniciarAnimacionCambioImagen();
				utilidad = new Utilidades();

				Collections.sort(comicsImportados, Comparator.comparing(Comic::getNombre));

				for (Comic c : comicsImportados) {
					contador++;
//					String correctedUrl = c.getImagen().replace("\\", "/").replace("http:", "https:").replace("https:",
//							"https:/");

//					String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);
//
//					URI uri = new URI(correctedUrl);
//
//					Utilidades.descargarYConvertirImagenAsync(uri, SOURCE_PATH, codigo_imagen);

					c.setID("");
//					c.setImagen(SOURCE_PATH + File.separator + codigo_imagen + ".jpg");
					libreria.insertarDatos(c);

					mensajePront += "Comic " + contador + " introducido correctamente\n";
					libreria.listasAutoCompletado();

					Image imagenDeseo = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
					imagenFondo.setImage(imagenDeseo);
					List<ComboBox<String>> comboboxes = VentanaAccionController.getComboBoxes();

					funcionesCombo.rellenarComboBox(comboboxes);
				}
				comicsImportados.clear();
				tablaBBDD.getItems().clear();
				funcionesTabla.tablaBBDD(comicsImportados, tablaBBDD, columnList); // Llamada a funcion
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo.setText(mensajePront);

				detenerAnimacionPront();

			}
		}
	}

	/**
	 * Funcion que devuelve un array con los datos de los TextField del comic a
	 * introducir.
	 *
	 * @return
	 */
	public String[] camposComic() {

		utilidad = new Utilidades();

		String campos[] = new String[17];

		campos[0] = utilidad.comaPorGuion(nombreComic.getText());

		campos[1] = utilidad.comaPorGuion(numero());

		campos[2] = utilidad.comaPorGuion(varianteComic.getText());

		campos[3] = utilidad.comaPorGuion(firmaComic.getText());

		campos[4] = utilidad.comaPorGuion(editorialComic.getText());

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

		campos[12] = utilidad.comaPorGuion(caja());

		campos[13] = Utilidades.eliminarEspacios(nombreKeyIssue.getText());

		campos[14] = Utilidades.eliminarEspacios(urlReferencia.getText());

		campos[15] = Utilidades.eliminarEspacios(precioComic.getText());

		campos[16] = Utilidades.eliminarEspacios(codigoComicTratar.getText());

		return campos;
	}

	public String[] camposComicBusqueda() {
		utilidad = new Utilidades();

		String campos[] = new String[13];

		if (nombreComic.getText().isEmpty()) {
			campos[0] = "";
		} else {
			campos[0] = utilidad.comaPorGuion(nombreComic.getText());
		}

		if (numero().isEmpty()) {
			campos[1] = "";
		} else {
			campos[1] = numero();
		}

		if (varianteComic.getText().isEmpty()) {
			campos[2] = "";
		} else {
			campos[2] = utilidad.comaPorGuion(varianteComic.getText());
		}

		if (firmaComic.getText().isEmpty()) {
			campos[3] = "";
		} else {
			campos[3] = utilidad.comaPorGuion(firmaComic.getText());
		}

		if (editorialComic.getText().isEmpty()) {
			campos[4] = "";
		} else {
			campos[4] = utilidad.comaPorGuion(editorialComic.getText());
		}

		if (formato().isEmpty()) {
			campos[5] = "";
		} else {
			campos[5] = formato();
		}

		if (procedencia().isEmpty()) {
			campos[6] = "";
		} else {
			campos[6] = procedencia();
		}

		LocalDate fecha = fechaComic.getValue();
		if (fecha != null) {
			campos[7] = fecha.toString();
		} else {
			campos[7] = "";
		}

		if (guionistaComic.getText().isEmpty()) {
			campos[8] = "";
		} else {
			campos[8] = utilidad.comaPorGuion(guionistaComic.getText());
		}

		if (dibujanteComic.getText().isEmpty()) {
			campos[9] = "";
		} else {
			campos[9] = utilidad.comaPorGuion(dibujanteComic.getText());
		}

		if (direccionImagen.getText().isEmpty()) {
			campos[10] = "";
		} else {
			campos[10] = direccionImagen.getText();
		}

		if (estado().isEmpty()) {
			campos[11] = "";
		} else {
			campos[11] = estado();
		}

		if (caja().isEmpty() || caja().equals("0")) {
			campos[12] = "";
		} else {
			campos[12] = utilidad.comaPorGuion(caja());
		}

		return campos;
	}

	/**
	 * Valida los campos del cómic y resalta en rojo aquellos que estén vacíos.
	 *
	 * @param comic El cómic a validar.
	 */
	public void validateComicFields(Comic comic) {
		if (comic.getNombre().isEmpty()) {
			nombreComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo nombre
		}

		if (comic.getNumero().isEmpty()) {
			numeroComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo número
		}

		if (comic.getEditorial().isEmpty()) {
			editorialComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo editorial
		}

		if (comic.getGuionista().isEmpty()) {
			guionistaComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo guionista
		}

		if (comic.getDibujante().isEmpty()) {
			dibujanteComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo dibujante
		}
	}

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

//			if (Utilidades.isURL(datos[10])) {
//				Utilidades.borrarImagen(portada);
//			}

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

	/**
	 * Rellena automáticamente algunos campos basados en el ID del cómic.
	 */
	public void autoRelleno(TextField textField) {

		libreria = new DBLibreriaManager();

		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				boolean existeComic = false;
				try {
					existeComic = libreria.checkID(newValue);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				Comic comic_temp = null;

				if (existeComic || newValue.isEmpty()) {

					botonBusquedaAvanzada.setVisible(false);
					botonBusquedaAvanzada.setDisable(true);

					try {
						comic_temp = libreria.comicDatos(textField.getText());
					} catch (SQLException e) {
						e.printStackTrace();
					}

					textField.setText(textField.getText());
					// Limpiar selecciones previas en los ComboBox
					numeroComic.getSelectionModel().clearSelection();
					formatoComic.getSelectionModel().clearSelection();
					numeroCajaComic.getSelectionModel().clearSelection();

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

					direccionImagen.setText(comic_temp.getImagen());

					prontInfo.clear();
					prontInfo.setOpacity(1);
					imagencomic.setImage(libreria.selectorImage(textField.getText()));
				} else {
					borrar_datos_autorellenos();
				}
			} else {
				borrar_datos_autorellenos();
			}
		});
	}

	/**
	 * Borra los datos del cómic
	 */
	public void borrar_datos_autorellenos() {
		nombreComic.setText("");

		numeroComic.setValue("");
		numeroComic.getEditor().setText("");

		varianteComic.setText("");
		firmaComic.setText("");
		editorialComic.setText("");

		formatoComic.setValue("");
		formatoComic.getEditor().setText("");

		procedenciaComic.setValue("");
		procedenciaComic.getEditor().setText("");

		fechaComic.setValue(null);
		guionistaComic.setText("");
		dibujanteComic.setText("");
		nombreKeyIssue.setText("");

		numeroCajaComic.setValue("");
		numeroCajaComic.getEditor().setText("");

		nombreKeyIssue.setText("");
		direccionImagen.setText("");
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);

		botonBusquedaAvanzada.setVisible(false);
		botonBusquedaAvanzada.setDisable(true);
	}

	/**
	 * Establece la instancia de la ventana (Stage) asociada a este controlador.
	 *
	 * @param stage La instancia de la ventana (Stage) que se asocia con este
	 *              controlador.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Cierra la ventana asociada a este controlador, si está disponible. Si no se
	 * ha establecido una instancia de ventana (Stage), este método no realiza
	 * ninguna acción.
	 */
	public void closeWindow() {
		if (stage != null) {
			stage.close();
		}
	}
}
