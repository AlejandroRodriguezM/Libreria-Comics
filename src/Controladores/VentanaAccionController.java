/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import org.json.JSONException;

import Apis.ApiISBNGeneral;
import Apis.ApiMarvel;
import Funcionamiento.FuncionesComboBox;
import Funcionamiento.FuncionesTableView;
import Funcionamiento.FuncionesTextField;
import Funcionamiento.FuncionesTooltips;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.ListaComicsDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
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
import webScrap.WebScrapGoogle;
import webScrap.WebScraperPreviewsWorld;

/**
 * Clase controladora para la ventana de acciones, que gestiona la interfaz de
 * usuario y las operaciones relacionadas con los cómics.
 */
public class VentanaAccionController implements Initializable {

	@FXML
	private Label alarmaConexionInternet;

	@FXML
	private Label alarmaConexionSql;

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
	 * Botón para cancelar la subida de imagenes.
	 */
	@FXML
	private Button botonCancelarSubida;

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

	/**
	 * Boton que sirve para subir una imagen a un comic que escojamos
	 */
	@FXML
	private Button botonSubidaPortada;

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

	@FXML
	private MenuItem menu_Importar_Fichero_CodigoBarras;

	@FXML
	private MenuItem menu_archivo_conexion;

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
	private static ListaComicsDAO libreria = null;

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

	private final String CARPETA_RAIZ_PORTADAS = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
			+ ConectManager.DB_NAME + File.separator;

	/**
	 * Construimos la ruta al directorio "libreria_comics" dentro de "Documents" y
	 * añadimos el nombre de la base de datos y la carpeta "portadas".
	 */
	private final String SOURCE_PATH = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
			+ ConectManager.DB_NAME + File.separator + "portadas";

	/**
	 * Mapa que contiene elementos de la interfaz y sus tooltips correspondientes.
	 */
	private static Map<Node, String> tooltipsMap = new HashMap<>();

//	public static String id_comic_selecionado = "";

	public static String apiKey = Utilidades.cargarApiComicVine();
	public static String clavesMarvel[] = Utilidades.clavesApiMarvel();

	private static AlarmaList alarmaList = new AlarmaList();

	/**
	 * Inicializa la interfaz de usuario y configura el comportamiento de los
	 * elementos al cargar la vista.
	 *
	 * @param location  La ubicación relativa del archivo FXML.
	 * @param resources Los recursos que pueden ser utilizados por el controlador.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
		alarmaList.iniciarThreadChecker(true);

		listas_autocompletado();

		Platform.runLater(() -> {

			rellenarCombosEstaticos();

			mostrarOpcion(TIPO_ACCION);
		});

		ListaComicsDAO.comicsImportados.clear();

		asignarTooltips();

		formatearInterfaz();

		controlarEventosInterfaz();

		establecerDinamismoAnchor();
	}

	public void listas_autocompletado() {
		if (ConectManager.conexionActiva()) {
			FuncionesTextField.asignarAutocompletado(nombreComic, ListaComicsDAO.listaNombre);
			FuncionesTextField.asignarAutocompletado(varianteComic, ListaComicsDAO.listaVariante);
			FuncionesTextField.asignarAutocompletado(firmaComic, ListaComicsDAO.listaFirma);
			FuncionesTextField.asignarAutocompletado(editorialComic, ListaComicsDAO.listaEditorial);
			FuncionesTextField.asignarAutocompletado(guionistaComic, ListaComicsDAO.listaGuionista);
			FuncionesTextField.asignarAutocompletado(dibujanteComic, ListaComicsDAO.listaDibujante);
			FuncionesTextField.asignarAutocompletado(numeroComic.getEditor(), ListaComicsDAO.listaNumeroComic);
		}
	}

	/**
	 * Establece una lista de ComboBoxes para su uso en la clase
	 * VentanaAccionController.
	 *
	 * @param comboBoxes La lista de ComboBoxes que se desea establecer.
	 */
	public void setComboBoxes(List<ComboBox<String>> comboBoxes) {
		comboboxes = comboBoxes;
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
	 * Controla los eventos de la interfaz, desactivando el enfoque en el VBox para
	 * evitar eventos de teclado, y añadiendo filtros y controladores de eventos
	 * para gestionar el enfoque entre el VBox y el TableView.
	 */
	private void controlarEventosInterfaz() {
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

		// Establecemos un evento para detectar cambios en el segundo TextField
		idComicTratar_mod.textProperty().addListener((observable, oldValue, newValue) -> {
			mostrarComic(idComicTratar_mod.getText());
		});
	}

	/**
	 * Realiza la formateo específico de la interfaz, configurando validadores,
	 * formateadores y restringiendo ciertos caracteres en campos específicos.
	 */
	private void formatearInterfaz() {

		List<TableColumn<Comic, String>> columnListCarga = Arrays.asList(nombre, numero, variante, editorial, guionista,
				dibujante);
		columnList = columnListCarga;

		numeroComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroCajaComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		idComicTratar_mod.setTextFormatter(FuncionesComboBox.validador_Nenteros());
		precioComic.setTextFormatter(FuncionesComboBox.validador_Ndecimales());

		if (TIPO_ACCION.equalsIgnoreCase("aniadir")) {
			idComicTratar_mod.setTextFormatter(FuncionesComboBox.desactivarValidadorNenteros());
		}

		FuncionesTextField.eliminarEspacioInicial(nombreComic);
		FuncionesTextField.restringirSimbolos(editorialComic);
		FuncionesTextField.restringirSimbolos(guionistaComic);
		FuncionesTextField.restringirSimbolos(dibujanteComic);
		FuncionesTextField.restringirSimbolos(varianteComic);

		FuncionesTextField.reemplazarEspaciosMultiples(nombreComic);
		FuncionesTextField.reemplazarEspaciosMultiples(editorialComic);
		FuncionesTextField.reemplazarEspaciosMultiples(guionistaComic);
		FuncionesTextField.reemplazarEspaciosMultiples(dibujanteComic);
		FuncionesTextField.reemplazarEspaciosMultiples(varianteComic);
		FuncionesTextField.reemplazarEspacio(busquedaCodigo);

		FuncionesTextField.permitirUnSimbolo(nombreComic);
		FuncionesTextField.permitirUnSimbolo(editorialComic);
		FuncionesTextField.permitirUnSimbolo(guionistaComic);
		FuncionesTextField.permitirUnSimbolo(dibujanteComic);
		FuncionesTextField.permitirUnSimbolo(varianteComic);
		FuncionesTextField.permitirUnSimbolo(busquedaCodigo);

		busquedaCodigo.setDisable(true); // Inicialmente deshabilitado

		ObservableList<String> puntuaciones = FXCollections.observableArrayList("0/0", "0.5/5", "1/5", "1.5/5", "2/5",
				"2.5/5", "3/5", "3.5/5", "4/5", "4.5/5", "5/5");
		puntuacionMenu.setItems(puntuaciones);
		puntuacionMenu.getSelectionModel().selectFirst();
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
	 * Funcion que comprueba segun los datos escritos en los textArea, que comic
	 * estas buscando.
	 * 
	 * @throws SQLException
	 */
	public List<Comic> listaPorParametro() {
		libreria = new ListaComicsDAO();
		libreria.reiniciarBBDD();
		utilidad = new Utilidades();
		Comic datos = camposComic();

		rootVBox.setVisible(true);
		rootVBox.setDisable(false);

		prontInfo.setOpacity(1);
		prontInfo.setText(funcionesTabla.resultadoBusquedaPront(datos).getText());

		List<Comic> listComic = FXCollections.observableArrayList(ComicManagerDAO.busquedaParametro(datos, ""));

		return listComic;
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException {
		verBasedeDatos(false);
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
		verBasedeDatos(true);
	}

	public void verBasedeDatos(boolean completo) {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		libreria = new ListaComicsDAO();
		libreria.reiniciarBBDD();
		funcionesTabla.modificarColumnas(tablaBBDD, columnList);
		tablaBBDD.refresh();
		borrar_datos_autorellenos();
		borrarDatosGraficos();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);

		if (ComicManagerDAO.hayDatosEnLibreria("")) {
			if (completo) {
				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

				List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
				funcionesTabla.tablaBBDD(listaComics, tablaBBDD, columnList);

			} else {
				funcionesTabla.tablaBBDD(listaPorParametro(), tablaBBDD, columnList); // Llamada a funcion
			}
		} else {

			String mensaje = "ERROR. No hay datos en la base de datos";

			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);

		}

	}

	/**
	 * Asigna tooltips a varios elementos en la interfaz gráfica. Estos tooltips
	 * proporcionan información adicional cuando el usuario pasa el ratón sobre los
	 * elementos.
	 */
	public void asignarTooltips() {
		Platform.runLater(() -> {
			// Configuración de tooltips comunes
			configurarTooltipsComunes(tooltipsMap);

			// Configuración de tooltips según el tipo de acción
			if ("aniadir".equals(TIPO_ACCION)) {
				configurarTooltipsAniadir(tooltipsMap);
			} else if ("eliminar".equals(TIPO_ACCION)) {
				configurarTooltipsEliminar(tooltipsMap);
			} else if ("modificar".equals(TIPO_ACCION)) {
				configurarTooltipsModificar(tooltipsMap);
			} else if ("puntuar".equals(TIPO_ACCION)) {
				configurarTooltipsPuntuar(tooltipsMap);
			}

			// Asignación de tooltips mediante la función externa
			FuncionesTooltips.assignTooltips(tooltipsMap);
		});
	}

	// Método para configurar tooltips comunes
	private void configurarTooltipsComunes(Map<Node, String> tooltipsMap2) {
		tooltipsMap2.put(nombreComic, "Nombre de los cómics / libros / mangas");
		tooltipsMap2.put(numeroComic, "Número del cómic / libro / manga");
		tooltipsMap2.put(varianteComic, "Nombre de la variante del cómic / libro / manga");
		tooltipsMap2.put(botonbbdd, "Botón para acceder a la base de datos");
		tooltipsMap2.put(botonLimpiar, "Limpia la pantalla y reinicia todos los valores");
	}

	// Método para configurar tooltips específicos al añadir
	private void configurarTooltipsAniadir(Map<Node, String> tooltipsMap2) {
		configurarTooltipsComunes(tooltipsMap2); // Hereda los tooltips comunes
		tooltipsMap2.put(botonSubidaPortada, "Botón para subir una portada");
	}

	// Método para configurar tooltips específicos al eliminar
	private void configurarTooltipsEliminar(Map<Node, String> tooltipsMap2) {
		configurarTooltipsComunes(tooltipsMap2); // Hereda los tooltips comunes
		tooltipsMap2.put(botonEliminar, "Botón para eliminar un cómic");
		tooltipsMap2.put(botonVender, "Botón para vender un cómic");
		tooltipsMap2.put(botonParametroComic, "Botón para buscar un cómic mediante una lista de parámetros");
		// Agregar más tooltips específicos al eliminar según sea necesario...
	}

	// Método para configurar tooltips específicos al modificar
	private void configurarTooltipsModificar(Map<Node, String> tooltipsMap2) {
		configurarTooltipsComunes(tooltipsMap2); // Hereda los tooltips comunes
		tooltipsMap2.put(botonSubidaPortada, "Botón para subir una portada");
		tooltipsMap2.put(botonModificarComic, "Botón para modificar un cómic");
		tooltipsMap2.put(botonParametroComic, "Botón para buscar un cómic mediante una lista de parámetros");
		// Agregar más tooltips específicos al modificar según sea necesario...
	}

	// Método para configurar tooltips específicos al puntuar
	private void configurarTooltipsPuntuar(Map<Node, String> tooltipsMap2) {
		configurarTooltipsComunes(tooltipsMap2); // Hereda los tooltips comunes
		tooltipsMap2.put(botonBorrarOpinion, "Botón para borrar una opinión");
		tooltipsMap2.put(botonAgregarPuntuacion, "Botón para agregar una puntuación");
		tooltipsMap2.put(puntuacionMenu, "Selecciona una puntuación en el menú");
		tooltipsMap2.put(botonParametroComic, "Botón para buscar un cómic mediante una lista de parámetros");
		// Agregar más tooltips específicos al puntuar según sea necesario...
	}

	@FXML
	void eliminarComicSeleccionado(ActionEvent event) {
		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (nav.alertaEliminar()) {

			if (idRow != null) {

				String id_comic = idRow.getID();
				ListaComicsDAO.comicsImportados.removeIf(c -> c.getID().equals(id_comic));

				tablaBBDD.getItems().clear();
				funcionesTabla.tablaBBDD(ListaComicsDAO.comicsImportados, tablaBBDD, columnList); // Llamada a funcion

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

				if (ListaComicsDAO.comicsImportados.size() < 1) {
					cambiarEstadoBotones(false);
				}
				borrar_datos_autorellenos();
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
	private void seleccionarComics() {
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		Utilidades.comprobacionListaComics();

		Comic newSelection = tablaBBDD.getSelectionModel().getSelectedItem();

		// Verificar si idRow es nulo antes de intentar acceder a sus métodos
		if (newSelection != null) {
			String id_comic = newSelection.getID();

			mostrarComic(id_comic);
		}
	}

	public void mostrarComic(String idComic) {
		if (idComic == null || idComic.isEmpty()) {
			borrar_datos_autorellenos();
			borrarDatosGraficos();
			return;
		}

		prontInfo.setStyle("");
		Comic comicTemp = null;
		if (!ListaComicsDAO.comicsImportados.isEmpty()) {
			comicTemp = Utilidades.devolverComic(idComic);
		} else {
			comicTemp = ComicManagerDAO.comicDatos(idComic);
		}

		if (comicTemp == null) {
			borrar_datos_autorellenos();
			borrarDatosGraficos();
			AlarmaList.mostrarMensajePront("No existe comic con dicho ID", false, prontInfo);
			return;
		}

		Comic.limpiarCamposComic(comicTemp);
		setAtributosDesdeTabla(comicTemp);
		validarCamposComic(false);
		prontInfo.setOpacity(1);

		String mensaje;
		if (!ListaComicsDAO.comicsImportados.isEmpty() && ComicManagerDAO.comprobarIdentificadorComic(idComic)) {
			mensaje = ComicManagerDAO.comicDatos(idComic).toString().replace("[", "").replace("]", "");
		} else {
			mensaje = comicTemp.toString().replace("[", "").replace("]", "");
		}
		prontInfo.setText(mensaje);

		if (TIPO_ACCION.equals("modificar")) {
			mostrarOpcion(TIPO_ACCION);
			idComicTratar_mod.setText(comicTemp.getID());
		}
	}

	/**
	 * Establece los atributos del cómic basándose en el objeto Comic proporcionado.
	 * 
	 * @param comic_temp El objeto Comic que contiene los datos a establecer.
	 */
	private void setAtributosDesdeTabla(Comic comic_temp) {
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

		idComicTratar_mod.setText(comic_temp.getID());

		Utilidades.cargarImagenAsync(comic_temp.getImagen(), imagencomic);
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

			seleccionarComics();
		}
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
	 * Muestra la opción correspondiente en la interfaz gráfica y habilita los
	 * elementos relacionados según el tipo de opción especificado.
	 *
	 * @param opcion Tipo de opción a mostrar ("eliminar", "aniadir", "modificar" o
	 *               "puntuar").
	 */
	public void mostrarOpcion(String opcion) {
		ocultarCampos();

		List<Node> elementosAMostrarYHabilitar = new ArrayList<>();

		switch (opcion.toLowerCase()) {
		case "eliminar":
			elementosAMostrarYHabilitar.addAll(Arrays.asList(label_id_mod, botonVender, botonEliminar, tablaBBDD,
					botonbbdd, rootVBox, botonParametroComic, idComicTratar_mod));

			rootVBox.toFront();
			break;
		case "aniadir":
			elementosAMostrarYHabilitar.addAll(Arrays.asList(dibujanteComic, editorialComic, estadoComic, fechaComic,
					firmaComic, formatoComic, guionistaComic, nombreKeyIssue, numeroCajaComic, procedenciaComic,
					urlReferencia, botonBusquedaAvanzada, precioComic, direccionImagen, label_portada, label_precio,
					label_caja, label_dibujante, label_editorial, label_estado, label_fecha, label_firma, label_formato,
					label_guionista, label_key, label_procedencia, label_referencia, codigoComicTratar,
					label_codigo_comic, tablaBBDD, rootVBox, botonSubidaPortada, idComicTratar_mod, label_id_mod,
					botonGuardarCambioComic));

			break;
		case "modificar":
			elementosAMostrarYHabilitar.addAll(Arrays.asList(dibujanteComic, editorialComic, estadoComic, fechaComic,
					firmaComic, formatoComic, guionistaComic, nombreKeyIssue, numeroCajaComic, procedenciaComic,
					urlReferencia, botonModificarComic, precioComic, direccionImagen, tablaBBDD, label_portada,
					label_precio, label_caja, label_dibujante, label_editorial, label_estado, label_fecha, label_firma,
					label_formato, label_guionista, label_key, label_procedencia, label_referencia, botonbbdd,
					idComicTratar_mod, label_id_mod, botonParametroComic, codigoComicTratar, label_codigo_comic,
					rootVBox, botonSubidaPortada));
			rootVBox.toFront();

			break;
		case "puntuar":
			elementosAMostrarYHabilitar
					.addAll(Arrays.asList(botonBorrarOpinion, puntuacionMenu, labelPuntuacion, botonAgregarPuntuacion,
							label_id_mod, tablaBBDD, botonbbdd, rootVBox, botonParametroComic, idComicTratar_mod));
			rootVBox.toFront();

			break;
		default:
			closeWindow();
			return;
		}

		for (Node elemento : elementosAMostrarYHabilitar) {
			elemento.setVisible(true);
			elemento.setDisable(false);
		}

		if (!TIPO_ACCION.equals("modificar")) {
			autoRelleno();
		}

		if (!TIPO_ACCION.equals("aniadir")) {
			navegacion_cerrar.setDisable(true);
			navegacion_cerrar.setVisible(false);

			idComicTratar_mod.setLayoutX(56); // Nueva coordenada X para el TextField
			idComicTratar_mod.setLayoutY(104); // Nueva coordenada Y para el TextField

			label_id_mod.setLayoutX(3); // Nueva coordenada X para el Label
			label_id_mod.setLayoutY(104); // Nueva coordenada Y para el Label
		} else {
			idComicTratar_mod.setEditable(false);
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
	 * Realiza la acción de borrar la puntuación de un cómic.
	 * 
	 * @param event Evento que desencadena la acción.
	 * @throws IOException  Excepción lanzada en caso de errores de entrada/salida.
	 * @throws SQLException Excepción lanzada en caso de errores de acceso a la base
	 *                      de datos.
	 */
	@FXML
	void borrarPuntuacion(ActionEvent event) {
		accionPuntuar(false);
	}

	/**
	 * Realiza la acción de agregar una nueva puntuación a un cómic.
	 * 
	 * @param event Evento que desencadena la acción.
	 * @throws IOException  Excepción lanzada en caso de errores de entrada/salida.
	 * @throws SQLException Excepción lanzada en caso de errores de acceso a la base
	 *                      de datos.
	 */
	@FXML
	void agregarPuntuacion(ActionEvent event) {
		accionPuntuar(true);
	}

	/**
	 * Realiza la acción de puntuar un cómic, ya sea agregar una nueva puntuación o
	 * borrar la existente.
	 * 
	 * @param esAgregar Indica si la acción es agregar una nueva puntuación (true) o
	 *                  borrar la existente (false).
	 * @throws SQLException Excepción lanzada en caso de errores de acceso a la base
	 *                      de datos.
	 */
	private void accionPuntuar(boolean esAgregar) {

		if (ConectManager.conexionActiva()) {
			libreria = new ListaComicsDAO();
			String id_comic = idComicTratar_mod.getText();
			if (comprobarExistenciaComic(id_comic)) {
				if (nav.alertaAccionGeneral()) {
					if (esAgregar) {
						ComicManagerDAO.actualizarOpinion(id_comic,
								FuncionesComboBox.puntuacionCombobox(puntuacionMenu));
					} else {
						ComicManagerDAO.actualizarOpinion(id_comic, "0");
					}
					String mensaje = ". Has borrado la puntuacion del comic.";

					AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

					List<ComboBox<String>> comboboxes = getComboBoxes();

					funcionesCombo.rellenarComboBox(comboboxes);
				} else {
					String mensaje = "Accion cancelada";
					AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
				}

			}
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
		// Verificar si las claves API están ausentes o vacías
		if (!Utilidades.verificarClavesAPI(clavesMarvel, apiKey)) {
			nav.alertaException("Revisa las APIS de MArvel y Vine, estan incorrectas o no funcionan");
			return;
		} else {
			// Continuar con la lógica cuando ambas claves están presente
			cambiarVisibilidad();
		}
	}

	/**
	 * Modifica la visibilidad y el estado de los elementos de búsqueda en la
	 * interfaz de usuario.
	 *
	 * @param mostrar True para mostrar los elementos de búsqueda, False para
	 *                ocultarlos.
	 */
	private void cambiarVisibilidad() {

		if (botonBusquedaCodigo.isVisible()) {
			botonBusquedaCodigo.setVisible(false);
			botonBusquedaCodigo.setDisable(true);
			busquedaCodigo.setVisible(false);
			busquedaCodigo.setDisable(true);
		} else {
			botonBusquedaCodigo.setVisible(true);
			botonBusquedaCodigo.setDisable(false);
			busquedaCodigo.setVisible(true);
			busquedaCodigo.setDisable(false);
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

		if (Utilidades.verificarClavesAPI(clavesMarvel, apiKey)) {
			if (Utilidades.isInternetAvailable()) {

				borrar_datos_autorellenos();
				borrarDatosGraficos();
				String frase = "Fichero txt";

				String formato = "*.txt";

				File fichero = Utilidades.tratarFichero(frase, formato).showOpenDialog(null); // Llamada a funcion

				if (fichero != null) {
					Platform.runLater(() -> {
						busquedaPorCodigoImportacion(fichero);
					});

				}
			}
		}
	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 *
	 * @param elementos Lista de elementos que deseas ocultar y deshabilitar.
	 */
	private void ocultarElementos(List<Node> elementos) {
		// Itera a través de los elementos y oculta/deshabilita cada uno
		for (Node elemento : elementos) {
			elemento.setVisible(false);
			elemento.setDisable(true);
		}
	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public void ocultarCampos() {
		List<Node> elementos = Arrays.asList(tablaBBDD, dibujanteComic, editorialComic, estadoComic, fechaComic,
				firmaComic, formatoComic, guionistaComic, nombreKeyIssue, numeroCajaComic, procedenciaComic,
				urlReferencia, botonBorrarOpinion, puntuacionMenu, labelPuntuacion, botonAgregarPuntuacion,
				label_id_mod, botonVender, botonEliminar, botonModificarComic, botonBusquedaCodigo, botonbbdd,
				precioComic, direccionImagen, label_portada, label_precio, label_caja, label_dibujante, label_editorial,
				label_estado, label_fecha, label_firma, label_formato, label_guionista, label_key, label_procedencia,
				label_referencia, codigoComicTratar, label_codigo_comic, botonSubidaPortada);

		ocultarElementos(elementos);
	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public void ocultarCamposMod() {
		List<Node> elementos = Arrays.asList(nombreKeyIssue, urlReferencia, label_id_mod, idComicTratar_mod,
				precioComic, direccionImagen, label_portada, label_precio, label_key, label_referencia,
				botonModificarComic, codigoComicTratar, label_codigo_comic);

		ocultarElementos(elementos);
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
	void busquedaPorCodigo(ActionEvent event) {

		Platform.runLater(() -> {

			try {
				if (!ConectManager.conexionActiva() || !Utilidades.isInternetAvailable()) {
					return;
				}

				if (!Utilidades.verificarClavesAPI(clavesMarvel, apiKey)) {

					prontInfo.setText("No estás conectado a internet. Revisa tu conexión");
					return;

				}

				String valorCodigo = Utilidades.eliminarEspacios(busquedaCodigo.getText());

				if (valorCodigo.isEmpty()) {
					return;
				}

				AtomicBoolean isCancelled = new AtomicBoolean(true);
				prontInfo.setText(null);
				prontInfo.setVisible(false);
				Task<Void> tarea = new Task<>() {
					@Override
					protected Void call() throws Exception {

						if (procesarComicPorCodigo(valorCodigo)) {
							String mensaje = "Comic encontrado correctamente";
							AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
						} else {
							String mensaje = "La busqueda del comic ha salido mal. Revisa el codigo";
							AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
							AlarmaList.detenerAnimacionCargaImagen(cargaImagen);

						}

						return null;
					}
				};

				tarea.setOnRunning(ev -> {
					borrar_datos_autorellenos();
					desactivarBotones(true);
					cambiarEstadoBotones(true);
					imagencomic.setImage(null);
					imagencomic.setVisible(true);
					botonCancelarSubida.setVisible(true);
					AlarmaList.iniciarAnimacionCargaImagen(cargaImagen);
					menu_Importar_Fichero_CodigoBarras.setDisable(true);

				});

				tarea.setOnSucceeded(ev -> {
					botonCancelarSubida.setVisible(false);
					AlarmaList.detenerAnimacionCargaImagen(cargaImagen);
					menu_Importar_Fichero_CodigoBarras.setDisable(false);
					cambiarEstadoBotones(false);
					desactivarBotones(false);

					if (ListaComicsDAO.comicsImportados.size() > 0) {
						botonEliminarImportadoComic.setVisible(true);
						botonGuardarCambioComic.setVisible(true);
						botonGuardarComic.setVisible(true);
					} else {
						botonEliminarImportadoComic.setVisible(false);
						botonGuardarCambioComic.setVisible(false);
						botonGuardarComic.setVisible(false);
					}

				});

				tarea.setOnCancelled(ev -> {
					String mensaje = "Ha cancelado la búsqueda del cómic";
					AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
					botonCancelarSubida.setVisible(false); // Oculta el botón de cancelar
					AlarmaList.detenerAnimacionCargaImagen(cargaImagen); // Detiene la animación de carga
				});

				Thread thread = new Thread(tarea);

				botonCancelarSubida.setOnAction(ev -> {

					desactivarBotones(false);
					isCancelled.set(true);
					tarea.cancel(true);
					menu_Importar_Fichero_CodigoBarras.setDisable(false);

					AlarmaList.detenerAnimacionCargaImagen(cargaImagen); // Detiene la animación de carga
					botonCancelarSubida.setVisible(false); // Oculta el botón de cancelar
				});

				thread.setDaemon(true);
				thread.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	/**
	 * Realiza una búsqueda utilizando un archivo de importaciones y muestra los
	 * resultados en la interfaz gráfica.
	 *
	 * @param fichero El archivo que contiene los códigos de importación a buscar.
	 */
	private void busquedaPorCodigoImportacion(File fichero) {

		if (!ConectManager.conexionActiva() && !Utilidades.isInternetAvailable()) {
			return;
		}

		StringBuilder codigoFaltante = new StringBuilder();
		AtomicInteger contadorErrores = new AtomicInteger(0);
		AtomicInteger comicsProcesados = new AtomicInteger(0);
		AtomicInteger numLineas = new AtomicInteger(0); // Declarar como AtomicInteger
		numLineas.set(Utilidades.contarLineas(fichero)); // Asignar el valor aquí
		AtomicReference<CargaComicsController> cargaComicsControllerRef = new AtomicReference<>();
		String mensaje = "ERROR. Has cancelado la subida de comics";
		nav.verCargaComics(cargaComicsControllerRef);
		Task<Void> tarea = new Task<>() {
			@Override
			protected Void call() {
				ExecutorService executorService = Executors
						.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

				try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
					AtomicReference<String> finalValorCodigoWrapper = new AtomicReference<>();
					reader.lines().forEach(linea -> {
						// Verifica si la tarea ha sido cancelada
						if (isCancelled()) {
							return; // Sale del método call() si la tarea ha sido cancelada
						}

						String finalValorCodigo = Utilidades.eliminarEspacios(linea).replace("-", "");
						finalValorCodigoWrapper.set(finalValorCodigo);

						if (!finalValorCodigo.isEmpty()) {
							final String[] texto = { "" }; // Envuelve la variable en un array de un solo elemento
							if (procesarComicPorCodigo(finalValorCodigoWrapper.get())) {
								texto[0] = "Comic: " + finalValorCodigoWrapper.get() + "\n";
							} else {
								codigoFaltante.append("Falta comic con codigo: ").append(finalValorCodigo).append("\n");
								texto[0] = "Comic no capturado: " + finalValorCodigoWrapper.get() + "\n";
								contadorErrores.getAndIncrement();
							}
							comicsProcesados.getAndIncrement();
							final long finalProcessedItems = comicsProcesados.get();
							System.out.println(finalProcessedItems);

							// Update UI elements using Platform.runLater
							Platform.runLater(() -> {

								String textoFinal = texto[0];

								double progress = (double) finalProcessedItems / (numLineas.get() + 1);
								String porcentaje = String.format("%.2f%%", progress * 100);

								cargaComicsControllerRef.get().cargarDatosEnCargaComics(textoFinal, porcentaje,
										progress);
							});
						}
					});
				} catch (IOException e) {
					Utilidades.manejarExcepcion(e);
				} finally {
					cerrarExecutorService(executorService);
				}

				return null;
			}
		};

		tarea.setOnRunning(ev -> {
			borrar_datos_autorellenos();
			cambiarEstadoBotones(true);
			desactivarBotones(true);
			imagencomic.setImage(null);
			imagencomic.setVisible(true);
			botonCancelarSubida.setVisible(true);
			AlarmaList.iniciarAnimacionCargaImagen(cargaImagen);
			menu_Importar_Fichero_CodigoBarras.setDisable(true);
		});

		tarea.setOnSucceeded(ev -> {
			AlarmaList.detenerAnimacionCargaImagen(cargaImagen);
			cambiarEstadoBotones(false);
			desactivarBotones(false);
			botonCancelarSubida.setVisible(false);

			actualizarInterfaz(contadorErrores, codigoFaltante, CARPETA_RAIZ_PORTADAS, numLineas);

			Platform.runLater(() -> {
				cargaComicsControllerRef.get().cargarDatosEnCargaComics("", "100%", 100.0);
			});
			menu_Importar_Fichero_CodigoBarras.setDisable(false);
		});

		tarea.setOnCancelled(ev -> {
			desactivarBotones(false);
			cambiarEstadoBotones(false);
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			botonCancelarSubida.setVisible(false); // Oculta el botón de cancelar
			AlarmaList.detenerAnimacionCargaImagen(cargaImagen); // Detiene la animación de carga
		});

		Thread thread = new Thread(tarea);

		botonCancelarSubida.setOnAction(ev -> {
			actualizarInterfaz(comicsProcesados, codigoFaltante, "", numLineas);
			nav.cerrarCargaComics();
			botonCancelarSubida.setVisible(false);
			tarea.cancel(true);
			menu_Importar_Fichero_CodigoBarras.setDisable(false);

		});

		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Verifica si se ha encontrado información válida para el cómic.
	 *
	 * @param comicInfo Un objeto Comic con la información del cómic.
	 * @return True si la información es válida y existe; de lo contrario, False.
	 */
	private boolean comprobarCodigo(Comic comicInfo) {
		return comicInfo != null;
	}

	/**
	 * Rellena los campos de la interfaz gráfica con la información del cómic
	 * proporcionada.
	 *
	 * @param comicInfo Un arreglo de strings con información del cómic.
	 * @throws IOException
	 */
	private void rellenarTablaImport(Comic comic) {
		Platform.runLater(() -> {

			if (!ConectManager.conexionActiva()) {
				return;
			}

			// Variables relacionadas con la importación de cómics
			String id = "A" + 0 + "" + (ListaComicsDAO.comicsImportados.size() + 1);
			String titulo = Utilidades.defaultIfNullOrEmpty(comic.getNombre(), "Vacio");
			String issueKey = Utilidades.defaultIfNullOrEmpty(comic.getKey_issue(), "Vacio");
			String numero = Utilidades.defaultIfNullOrEmpty(comic.getNumero(), "0");
			String variante = Utilidades.defaultIfNullOrEmpty(comic.getVariante(), "Vacio");
			String precio = Utilidades.defaultIfNullOrEmpty(comic.getPrecio_comic(), "0");
			String dibujantes = Utilidades.defaultIfNullOrEmpty(comic.getDibujante(), "Vacio");
			String escritores = Utilidades.defaultIfNullOrEmpty(comic.getGuionista(), "Vacio");
			String fechaVenta = comic.getFecha();
			LocalDate fecha = Utilidades.parseFecha(fechaVenta);

			// Variables relacionadas con la imagen del cómic
			String referencia = Utilidades.defaultIfNullOrEmpty(comic.getUrl_referencia(), "Vacio");
			String urlImagen = comic.getImagen();
			String editorial = Utilidades.defaultIfNullOrEmpty(comic.getEditorial(), "Vacio");
			File file = new File(urlImagen);

			// Manejo de la ruta de la imagen
			if (comic.getImagen() == null || comic.getImagen().isEmpty()) {
				String rutaImagen = "/Funcionamiento/sinPortada.jpg";
				URL url = getClass().getResource(rutaImagen);
				if (url != null) {
					urlImagen = url.toExternalForm();
				}
			} else {
				file = new File(urlImagen);
				urlImagen = file.toString();
			}

			String formato = Utilidades.defaultIfNullOrEmpty(comic.getFormato(), "Grapa (Issue individual)");
			String procedencia = Utilidades.defaultIfNullOrEmpty(comic.getProcedencia(),
					"Estados Unidos (United States)");

			// Corrección y generación de la URL final de la imagen
			String correctedUrl = urlImagen.replace("\\", "/").replace("http:", "https:").replace("https:", "https:/");
			String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);
			URI uri = null;
			try {
				uri = new URI(correctedUrl);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			String urlFinal = SOURCE_PATH + File.separator + codigo_imagen + ".jpg";
			String codigo_comic = Utilidades.defaultIfNullOrEmpty(comic.getCodigo_comic(), "0");
			// Descarga y conversión asíncrona de la imagen
			Utilidades.descargarYConvertirImagenAsync(uri, SOURCE_PATH, codigo_imagen);

			// Creación del objeto Comic importado y actualización de la tabla
			Comic comicImport = new Comic(id, titulo, "0", numero, variante, "", editorial, formato, procedencia,
					fecha.toString(), escritores, dibujantes, "Comprado", issueKey, "Sin puntuar", urlFinal, referencia,
					precio, codigo_comic);

			ListaComicsDAO.comicsImportados.add(comicImport);

			funcionesTabla.nombreColumnas(columnList, tablaBBDD);
			funcionesTabla.tablaBBDD(ListaComicsDAO.comicsImportados, tablaBBDD, columnList);
		});
	}

	public void cambiarEstadoBotones(boolean esCancelado) {
		if (esCancelado) {
			botonCancelarSubida.setVisible(true);
		} else {
			botonEliminarImportadoComic.setVisible(true);
			botonGuardarCambioComic.setVisible(true);
			botonGuardarComic.setVisible(true);
		}
	}

	public void desactivarBotones(boolean fueIportado) {
		botonEliminarImportadoComic.setDisable(fueIportado);
		botonGuardarCambioComic.setDisable(fueIportado);
		botonGuardarComic.setDisable(fueIportado);
		botonLimpiar.setDisable(fueIportado);
		botonSubidaPortada.setDisable(fueIportado);
		botonBusquedaCodigo.setDisable(fueIportado);
		botonBusquedaAvanzada.setDisable(fueIportado);
	}

	private Comic obtenerComicInfo(String finalValorCodigo) {
		try {
			// Verificar si hay una conexión activa
			if (!ConectManager.conexionActiva()) {
				return null;
			}

			// Obtener información del cómic según la longitud del código
			if (finalValorCodigo.length() == 9) {

				return WebScraperPreviewsWorld.displayComicInfo(finalValorCodigo.trim(), prontInfo);
			} else {
				// Si no, intentar obtener la información del cómic de diferentes fuentes
				Comic comicInfo = ApiMarvel.infoComicCode(finalValorCodigo.trim(), prontInfo);
				if (comicInfo == null) {
					comicInfo = WebScrapGoogle.obtenerDatosDiv(finalValorCodigo.trim());
				}
				if (comicInfo == null) {
					ApiISBNGeneral isbnGeneral = new ApiISBNGeneral();
					comicInfo = isbnGeneral.getBookInfo(finalValorCodigo.trim(), prontInfo);
				}

				if (comicInfo == null) {
					return null;
				}

				Comic.limpiarCamposComic(comicInfo);
				return comicInfo;
			}
		} catch (IOException | URISyntaxException | JSONException e) {
			// Manejar excepciones
			System.err.println("Error al obtener información del cómic: " + e.getMessage());
			return null;
		}
	}

	private void cerrarExecutorService(ExecutorService executorService) {
		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	private void actualizarInterfaz(AtomicInteger contadorErrores, StringBuilder codigoFaltante, String carpetaDatabase,
			AtomicInteger contadorTotal) {
		Platform.runLater(() -> {
			String mensaje = "";
			if (contadorErrores.get() > 0 && !carpetaDatabase.isEmpty()) {
				Utilidades.imprimirEnArchivo(codigoFaltante.toString(), carpetaDatabase);
			}

			if (!carpetaDatabase.isEmpty()) {
				mensaje = "Se han procesado: " + (contadorTotal.get() - contadorErrores.get()) + " de "
						+ contadorTotal.get();
			} else {
				mensaje = "Se han procesado: " + (contadorErrores.get()) + " de " + contadorTotal.get();

			}

			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
		});
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

		if (!ConectManager.conexionActiva()) {
			return;
		}

		libreria = new ListaComicsDAO();
		String id_comic = idComicTratar_mod.getText();
		idComicTratar_mod.setStyle("");
		if (comprobarExistenciaComic(id_comic)) {
			if (nav.alertaAccionGeneral()) {
				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

				List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);

				ComicManagerDAO.borrarComic(id_comic);
				libreria.reiniciarBBDD();
				ListaComicsDAO.listasAutoCompletado();
				funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
				funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);
				funcionesTabla.tablaBBDD(listaComics, tablaBBDD, columnList);

				List<ComboBox<String>> comboboxes = getComboBoxes();

				funcionesCombo.rellenarComboBox(comboboxes);
			}

			else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			}
		}
	}

	/**
	 * Comprueba la existencia de un cómic en la base de datos y realiza acciones
	 * dependiendo del resultado.
	 *
	 * @param ID El identificador del cómic a verificar.
	 * @return true si el cómic existe en la base de datos y se realizan las
	 *         acciones correspondientes, false de lo contrario.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	public boolean comprobarExistenciaComic(String ID) {

		// Verifica si la conexión está activa
		if (!ConectManager.conexionActiva()) {
			return false;
		}

		// Instancia de ListaComicsDAO
		libreria = new ListaComicsDAO();

		// Si el cómic existe en la base de datos
		if (ComicManagerDAO.comprobarIdentificadorComic(ID)) {
			// Restaura el estilo del campo de ID
			idComicTratar_mod.setStyle(null);
			// Construye la sentencia SQL para una búsqueda completa
//			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
			// Obtiene la lista de cómics de la base de datos
//			List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
			// Llama a funciones para actualizar la tabla de cómics
//			funcionesTabla.nombreColumnas(columnList, tablaBBDD);
			funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);
//			funcionesTabla.tablaBBDD(listaComics, tablaBBDD, columnList);
			return true;
		} else { // Si el cómic no existe en la base de datos
			// Establece un estilo de fondo rojo para el campo de ID
			idComicTratar_mod.setStyle("-fx-background-color: red");
			// Muestra un mensaje de error
			String mensaje = "ERROR. ID desconocido.";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			return false;
		}

	}

	/**
	 * Limpia los datos de la pantalla al hacer clic en el botón "Limpiar".
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		limpiarDatosPantalla();
	}

	/**
	 * Limpia y restablece todos los campos de datos en la sección de animaciones a
	 * sus valores predeterminados. Además, restablece la imagen de fondo y oculta
	 * cualquier mensaje de error o información.
	 */
	public void limpiarDatosPantalla() {
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

		if (ListaComicsDAO.comicsImportados.size() > 0) {
			if (nav.alertaBorradoLista()) {
				botonGuardarComic.setVisible(false);
				botonEliminarImportadoComic.setVisible(false);

				ListaComicsDAO.comicsImportados.clear();
				tablaBBDD.getItems().clear();
			}
		}

		if ("modificar".equals(TIPO_ACCION)) {
			mostrarOpcion(TIPO_ACCION);
		}
		// Borrar cualquier mensaje de error presente
		borrarErrores();
		validarCamposComic(true);
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

		if (!ConectManager.conexionActiva()) {
			return;
		}
		libreria = new ListaComicsDAO();
		String id_comic = idComicTratar_mod.getText();
		idComicTratar_mod.setStyle("");
		if (comprobarExistenciaComic(id_comic)) {
			if (nav.alertaAccionGeneral()) {
				accionComicAsync(true); // Llamada a funcion que modificara el contenido de un comic especifico.
				ListaComicsDAO.listasAutoCompletado();

				List<ComboBox<String>> comboboxes = getComboBoxes();
				tablaBBDD.refresh();
				if (comboboxes != null) {
					funcionesCombo.rellenarComboBox(comboboxes);
				}
			}

			else {
				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

				List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);

				ComicManagerDAO.borrarComic(id_comic);
				libreria.reiniciarBBDD();
				ListaComicsDAO.listasAutoCompletado();
				funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
				funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);
				funcionesTabla.tablaBBDD(listaComics, tablaBBDD, columnList);

				List<ComboBox<String>> comboboxes = getComboBoxes();

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

		if (!ConectManager.conexionActiva()) {
			return;
		}

		libreria = new ListaComicsDAO();
		String id_comic = idComicTratar_mod.getText();
		idComicTratar_mod.setStyle("");
		Comic comicActualizar = ComicManagerDAO.comicDatos(id_comic);
		if (comprobarExistenciaComic(id_comic)) {
			if (nav.alertaAccionGeneral()) {
				ComicManagerDAO.actualizarComicBBDD(comicActualizar, "vender");
				libreria.reiniciarBBDD();

				String mensaje = ". Has puesto a la venta el comic";
				AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);

				List<ComboBox<String>> comboboxes = getComboBoxes();

				funcionesCombo.rellenarComboBox(comboboxes);
			} else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			}

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

			String mensaje = "Portada subida correctamente.";

			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

		} else {
			String mensaje = "Has cancelado la subida de portada.";

			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
		}
	}

//	/**
//	 * Método que maneja el evento de guardar los datos de un cómic.
//	 * 
//	 * @param event El evento de acción que desencadena la llamada al método.
//	 */
//	@FXML
//	void guardarDatos(ActionEvent event) {
//
//		utilidad = new Utilidades();
//
//		if (!camposComicSonValidos()) {
//			String mensaje = "Error. Debes de introducir los datos correctos";
//			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
//			return; // Agregar return para salir del método en este punto
//		}
//		Comic datos = camposComic();
//		if (datos.getID() == null || datos.getID().isEmpty()) {
////			String nuevoId = "A" + 0 + "" + (ListaComicsDAO.comicsImportados.size() + 1);
////			datos.setID(nuevoId);
////			rellenarTablaImport(datos);
////			portadaOriginal = datos.getImagen();
////		} else {
//			datos = Utilidades.buscarComicPorID(ListaComicsDAO.comicsImportados, datos.getID());
//		}
//
////		// Initialize other String variables based on the properties of the 'datos'
////		// object.
////		String nombre = Utilidades.defaultIfNullOrEmpty(datos.getNombre(), "Vacio");
////		String numero = Utilidades.defaultIfNullOrEmpty(datos.getNumero(), "0");
////		String variante = Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(datos.getVariante()),
////				"Vacio");
////		String firma = Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(datos.getFirma()), "");
////		String editorial = Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(datos.getEditorial()),
////				"Vacio");
////		String formato = Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(datos.getFormato()),
////				"Grapa (Issue individual)");
////		String procedencia = Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(datos.getProcedencia()),
////				"Estados Unidos (United States)");
////		String fecha_comic = datos.getFecha();
////		String guionista = Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(datos.getGuionista()),
////				"Vacio");
////		String dibujante = Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(datos.getDibujante()),
////				"Vacio");
////		String estado = Utilidades.defaultIfNullOrEmpty(datos.getEstado(), "Comprado");
////		String numCaja = Utilidades.defaultIfNullOrEmpty(datos.getNumCaja(), "0");
////
////		String key_issue = "Vacio";
////		String key_issue_sinEspacios = datos.getKey_issue().trim();
////
////		Pattern pattern = Pattern.compile(".*\\w+.*");
////		Matcher matcher = pattern.matcher(key_issue_sinEspacios);
////
////		if (!key_issue_sinEspacios.isEmpty() && matcher.matches()) {
////			key_issue = key_issue_sinEspacios;
////		}
////
////		String url_referencia = Utilidades.defaultIfNullOrEmpty(datos.getUrl_referencia(), "Sin referencia");
////		String precio_comic = Utilidades.defaultIfNullOrEmpty(datos.getPrecio_comic(), "0");
////
////		precio_comic = String.valueOf(Utilidades.convertirMonedaADolar(procedencia, precio_comic));
////
////		String codigo_comic = Utilidades.defaultIfNullOrEmpty(datos.getCodigo_comic(), "");
////		String portada = Utilidades.defaultIfNullOrEmpty(datos.getImagen(), "");
////
////		if (!portadaOriginal.equals(direccionImagen.getText())) {
////			String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);
////			utilidad.nueva_imagen(direccionImagen.getText(), codigo_imagen);
////			portada = SOURCE_PATH + File.separator + codigo_imagen + ".jpg";
////		}
////
////		Comic comic = new Comic(datos.getID(), nombre, numCaja, numero, variante, firma, editorial, formato,
////				procedencia, fecha_comic.toString(), guionista, dibujante, estado, key_issue, "Sin puntuar", portada,
////				url_referencia, precio_comic, codigo_comic);
//
//		Comic.limpiarCamposComic(datos);
//
//		for (Comic c : ListaComicsDAO.comicsImportados) {
//			System.out.println(c.getID().equals(datos.getID()));
//			if (c.getID().equals(datos.getID())) {
//				ListaComicsDAO.comicsImportados.set(ListaComicsDAO.comicsImportados.indexOf(c), datos);
//				break;
//			}
//		}
//
//		
//		
//		desactivarBotones(false);
//		cambiarEstadoBotones(false);
//		botonCancelarSubida.setVisible(false); // Oculta el botón de cancelar
//
////		tablaBBDD.getItems().clear();
//		borrar_datos_autorellenos();
//		funcionesTabla.tablaBBDD(ListaComicsDAO.comicsImportados, tablaBBDD, columnList); // Llamada a funcion
//	}

	/**
	 * Método que maneja el evento de guardar los datos de un cómic.
	 * 
	 * @param event El evento de acción que desencadena la llamada al método.
	 */
	@FXML
	void guardarDatos(ActionEvent event) {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		if (!camposComicSonValidos()) {
			String mensaje = "Error. Debes de introducir los datos correctos";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			return; // Agregar return para salir del método en este punto
		}
		Comic datos = camposComic();
		if (datos.getID() == null || datos.getID().isEmpty()) {
			datos = Utilidades.buscarComicPorID(ListaComicsDAO.comicsImportados, datos.getID());
		}

		Comic.limpiarCamposComic(datos);

		for (Comic c : ListaComicsDAO.comicsImportados) {
			System.out.println(c.getID().equals(datos.getID()));
			if (c.getID().equals(datos.getID())) {
				ListaComicsDAO.comicsImportados.set(ListaComicsDAO.comicsImportados.indexOf(c), datos);
				break;
			}
		}

		desactivarBotones(false);
		cambiarEstadoBotones(false);
		botonCancelarSubida.setVisible(false); // Oculta el botón de cancelar

		borrar_datos_autorellenos();
		funcionesTabla.tablaBBDD(ListaComicsDAO.comicsImportados, tablaBBDD, columnList); // Llamada a funcion
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

		if (!ConectManager.conexionActiva()) {
			return;
		}

		if (ListaComicsDAO.comicsImportados.size() > 0) {
			if (nav.alertaInsertar()) {
				libreria = new ListaComicsDAO();

				utilidad = new Utilidades();

				Collections.sort(ListaComicsDAO.comicsImportados, Comparator.comparing(Comic::getNombre));

				for (Comic c : ListaComicsDAO.comicsImportados) {

					if (!comprobarListaValidacion(c)) {
						return;
					}
					ComicManagerDAO.insertarDatos(c, true);
				}

				ListaComicsDAO.listasAutoCompletado();
				List<ComboBox<String>> comboboxes = getComboBoxes();
				funcionesCombo.rellenarComboBox(comboboxes);

				ListaComicsDAO.comicsImportados.clear();
				tablaBBDD.getItems().clear();
				validarCamposComic(true);
				funcionesTabla.tablaBBDD(ListaComicsDAO.comicsImportados, tablaBBDD, columnList); // Llamada a funcion

				String mensajePront = "Has introducido los comics correctamente\n";
				AlarmaList.mostrarMensajePront(mensajePront, true, prontInfo);
				borrar_datos_autorellenos();
			}
		}
	}

	public boolean comprobarListaValidacion(Comic c) {
		if (c.getNombre() == null || c.getNombre().isEmpty() || c.getNombre().equalsIgnoreCase("vacio")
				|| c.getNumero() == null || c.getNumero().isEmpty() || c.getNumero().equalsIgnoreCase("vacio")
				|| c.getVariante() == null || c.getVariante().isEmpty() || c.getVariante().equalsIgnoreCase("vacio")
				|| c.getEditorial() == null || c.getEditorial().isEmpty() || c.getEditorial().equalsIgnoreCase("vacio")
				|| c.getFormato() == null || c.getFormato().isEmpty() || c.getFormato().equalsIgnoreCase("vacio")
				|| c.getProcedencia() == null || c.getProcedencia().isEmpty()
				|| c.getProcedencia().equalsIgnoreCase("vacio") || c.getFecha() == null || c.getFecha().isEmpty()
				|| c.getGuionista() == null || c.getGuionista().isEmpty() || c.getGuionista().equalsIgnoreCase("vacio")
				|| c.getDibujante() == null || c.getDibujante().isEmpty() || c.getDibujante().equalsIgnoreCase("vacio")
				|| c.getEstado() == null || c.getEstado().isEmpty() || c.getEstado().equalsIgnoreCase("vacio")
				|| c.getNumCaja() == null || c.getNumCaja().isEmpty() || c.getNumCaja().equalsIgnoreCase("vacio")
				|| c.getUrl_referencia() == null || c.getUrl_referencia().isEmpty()
				|| c.getUrl_referencia().equalsIgnoreCase("vacio") || c.getPrecio_comic() == null
				|| c.getPrecio_comic().isEmpty() || c.getPrecio_comic().equalsIgnoreCase("vacio")
				|| c.getCodigo_comic() == null) {

			String mensajePront = "Revisa la lista, algunos comics estan mal rellenados.";
			AlarmaList.mostrarMensajePront(mensajePront, false, prontInfo);

			return false;
		}
		return true;
	}

	/**
	 * Funcion que devuelve un array con los datos de los TextField del comic a
	 * introducir.
	 *
	 * @return
	 */
	public Comic camposComic() {
		Comic comic = new Comic();

		LocalDate fecha = fechaComic.getValue();
		String fechaComic = (fecha != null) ? fecha.toString() : "";

		comic.setNombre(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(nombreComic.getText()), ""));
		comic.setNumero(Utilidades.defaultIfNullOrEmpty(
				Utilidades.comaYGuionPorEspaciado(FuncionesComboBox.numeroCombobox(numeroComic)), ""));
		comic.setVariante(
				Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(varianteComic.getText()), ""));
		comic.setFirma(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(firmaComic.getText()), ""));
		comic.setEditorial(
				Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(editorialComic.getText()), ""));
		comic.setFormato(Utilidades.defaultIfNullOrEmpty(FuncionesComboBox.formatoCombobox(formatoComic), ""));
		comic.setProcedencia(
				Utilidades.defaultIfNullOrEmpty(FuncionesComboBox.procedenciaCombobox(procedenciaComic), ""));
		comic.setFecha(fechaComic);
		comic.setGuionista(
				Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(guionistaComic.getText()), ""));
		comic.setDibujante(
				Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(dibujanteComic.getText()), ""));
		comic.setImagen(Utilidades.defaultIfNullOrEmpty(direccionImagen.getText(), ""));
		comic.setEstado(Utilidades.defaultIfNullOrEmpty(FuncionesComboBox.estadoCombobox(estadoComic), ""));
		comic.setNumCaja(Utilidades.defaultIfNullOrEmpty(FuncionesComboBox.cajaCombobox(numeroCajaComic), ""));
		comic.setKey_issue(Utilidades.defaultIfNullOrEmpty(nombreKeyIssue.getText().trim(), ""));
		comic.setUrl_referencia((Utilidades.defaultIfNullOrEmpty(urlReferencia.getText().trim(), "")));
		comic.setPrecio_comic((Utilidades.defaultIfNullOrEmpty(precioComic.getText().trim(), "")));
		comic.setCodigo_comic(Utilidades.eliminarEspacios(codigoComicTratar.getText()));
		comic.setID(Utilidades.defaultIfNullOrEmpty(idComicTratar_mod.getText().trim(), ""));

		return comic;
	}

	/**
	 * Actualiza los campos únicos del objeto Comic con los valores ingresados en
	 * los campos de la interfaz gráfica.
	 * 
	 * @param comic El objeto Comic a actualizar.
	 */
	public void actualizarCamposUnicos(Comic comic) {

		comic.setKey_issue(!nombreKeyIssue.getText().isEmpty() ? Utilidades.eliminarEspacios(nombreKeyIssue.getText())
				: (!nombreKeyIssue.getText().trim().isEmpty()
						&& Pattern.compile(".*\\w+.*").matcher(nombreKeyIssue.getText().trim()).matches()
								? nombreKeyIssue.getText().trim()
								: "Vacio"));

		comic.setUrl_referencia(
				!urlReferencia.getText().isEmpty() ? Utilidades.eliminarEspacios(urlReferencia.getText())
						: (urlReferencia.getText().isEmpty() ? "Sin referencia" : urlReferencia.getText()));
		comic.setPrecio_comic(!precioComic.getText().isEmpty() ? Utilidades.eliminarEspacios(precioComic.getText())
				: (precioComic.getText().isEmpty() ? "0" : precioComic.getText()));
		comic.setCodigo_comic(Utilidades.eliminarEspacios(codigoComicTratar.getText()));

		comic.setNumCaja(comic.getNumCaja().isEmpty() ? "0" : comic.getNumCaja());
	}

	public void validarCamposComic(boolean esBorrado) {
		List<TextField> camposUi = Arrays.asList(nombreComic, varianteComic, editorialComic, precioComic,
				guionistaComic, dibujanteComic);

		for (TextField campoUi : camposUi) {
			String datoComic = campoUi.getText();

			if (esBorrado) {
				if (datoComic == null || datoComic.isEmpty() || datoComic.equalsIgnoreCase("vacio")) {
					campoUi.setStyle("");
				}
			} else {
				// Verificar si el campo está vacío, es nulo o tiene el valor "Vacio"
				if (datoComic == null || datoComic.isEmpty() || datoComic.equalsIgnoreCase("vacio")) {
					campoUi.setStyle("-fx-background-color: red;");
				} else {
					campoUi.setStyle("");
				}
			}

		}
	}

	public boolean camposComicSonValidos() {
		List<Control> camposUi = Arrays.asList(nombreComic, varianteComic, editorialComic, precioComic,
				codigoComicTratar, guionistaComic, dibujanteComic, fechaComic);

		for (Control campoUi : camposUi) {
			if (campoUi instanceof TextField) {
				String datoComic = ((TextField) campoUi).getText();

				// Verificar si el campo está vacío, es nulo o tiene el valor "Vacio"
				if (datoComic == null || datoComic.isEmpty() || datoComic.equalsIgnoreCase("vacio")) {
					campoUi.setStyle("-fx-background-color: #FF0000;");
					return false; // Devolver false si al menos un campo no es válido
				} else {
					campoUi.setStyle("");
				}
			} else if (campoUi instanceof DatePicker) {
				LocalDate fecha = ((DatePicker) campoUi).getValue();

				// Verificar si la fecha está vacía
				if (fecha == null) {
					campoUi.setStyle("-fx-background-color: #FF0000;");
					return false; // Devolver false si al menos un campo no es válido
				} else {
					campoUi.setStyle("");
				}
			}
		}

		return true; // Devolver true si todos los campos son válidos
	}

	/**
	 * Permite introducir un comic en la base de datos de forma manual
	 * 
	 * @throws Exception
	 */
	public void subidaComic() throws Exception {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		Utilidades.convertirNombresCarpetas(SOURCE_PATH);

		Comic comic = camposComic();
		actualizarCamposUnicos(comic);

		prontInfo.setOpacity(1);

		procesarComic(comic, false);
	}

	/**
	 * Funcion que permite modificar un comic, segun los datos introducidos
	 * 
	 * @throws Exception
	 */
	public void modificacionComic() throws Exception {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		String id_comic = idComicTratar_mod.getText();

		Comic comic_temp = ComicManagerDAO.comicDatos(id_comic);
		Comic datos = camposComic();
		Comic comicModificado = new Comic();

		Utilidades.convertirNombresCarpetas(SOURCE_PATH);

		comicModificado.setNombre(Utilidades.defaultIfNullOrEmpty(datos.getNombre(), comic_temp.getNombre()));
		comicModificado.setNumero(Utilidades.defaultIfNullOrEmpty(datos.getNumero(), comic_temp.getNumero()));
		comicModificado.setVariante(Utilidades.defaultIfNullOrEmpty(datos.getVariante(), comic_temp.getVariante()));
		comicModificado.setFirma(Utilidades.defaultIfNullOrEmpty(datos.getFirma(), comic_temp.getFirma()));
		comicModificado.setEditorial(Utilidades.defaultIfNullOrEmpty(datos.getEditorial(), comic_temp.getEditorial()));
		comicModificado.setFormato(Utilidades.defaultIfNullOrEmpty(datos.getFormato(), comic_temp.getFormato()));
		comicModificado
				.setProcedencia(Utilidades.defaultIfNullOrEmpty(datos.getProcedencia(), comic_temp.getProcedencia()));
		comicModificado.setFecha(Utilidades.defaultIfNullOrEmpty(datos.getFecha(), comic_temp.getFecha()));
		comicModificado.setGuionista(Utilidades.defaultIfNullOrEmpty(datos.getGuionista(), comic_temp.getGuionista()));
		comicModificado.setDibujante(Utilidades.defaultIfNullOrEmpty(datos.getDibujante(), comic_temp.getDibujante()));
		comicModificado.setImagen(Utilidades.defaultIfNullOrEmpty(datos.getImagen(), comic_temp.getImagen()));
		comicModificado.setEstado(Utilidades.defaultIfNullOrEmpty(datos.getEstado(), comic_temp.getEstado()));
		comicModificado.setNumCaja(Utilidades.defaultIfNullOrEmpty(datos.getNumCaja(), comic_temp.getNumCaja()));
		comicModificado.setPuntuacion(
				comic_temp.getPuntuacion().equals("Sin puntuar") ? "Sin puntuar" : comic_temp.getPuntuacion());

		String key_issue_sinEspacios = datos.getKey_issue().trim();

		if (!key_issue_sinEspacios.isEmpty() && key_issue_sinEspacios.matches(".*\\w+.*")) {
			comicModificado.setKey_issue(key_issue_sinEspacios);
		} else if (comic_temp != null && comic_temp.getKey_issue() != null && !comic_temp.getKey_issue().isEmpty()) {
			comicModificado.setKey_issue(comic_temp.getKey_issue());
		}

		String url_referencia = Utilidades.defaultIfNullOrEmpty(datos.getUrl_referencia(), "");
		comicModificado.setUrl_referencia(url_referencia.isEmpty() ? "Sin referencia" : url_referencia);

		String precio_comic = Utilidades.defaultIfNullOrEmpty(datos.getPrecio_comic(), "0");
		comicModificado.setPrecio_comic(
				String.valueOf(Utilidades.convertirMonedaADolar(comicModificado.getProcedencia(), precio_comic)));

		comicModificado.setCodigo_comic(Utilidades.defaultIfNullOrEmpty(datos.getCodigo_comic(), ""));

		procesarComic(comicModificado, true);
	}

	private boolean procesarComicPorCodigo(String finalValorCodigo) {

		if (!ConectManager.conexionActiva()) {
			return false;
		}

		Comic comicInfo = obtenerComicInfo(finalValorCodigo);

		if (comprobarCodigo(comicInfo)) {
			rellenarTablaImport(comicInfo);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Procesa la información de un cómic, ya sea para realizar una modificación o
	 * una inserción en la base de datos.
	 *
	 * @param comic          El cómic con la información a procesar.
	 * @param esModificacion Indica si se está realizando una modificación (true) o
	 *                       una inserción (false).
	 * @throws Exception
	 */
	public void procesarComic(Comic comic, boolean esModificacion) throws Exception {
		final List<Comic> listaComics; // Declarar listaComics como final

		if (!ConectManager.conexionActiva()) {
			return;
		}

		libreria = new ListaComicsDAO();
		utilidad = new Utilidades();
		prontInfo.setOpacity(1);
		if (!camposComicSonValidos()) {
			String mensaje = "Error. Debes de introducir los datos correctos";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			return; // Agregar return para salir del método en este punto
		}

		String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);
		String mensaje = "";
		utilidad.nueva_imagen(comic.getImagen(), codigo_imagen);

		comic.setImagen(SOURCE_PATH + File.separator + codigo_imagen + ".jpg");
		if (esModificacion) {
			comic.setID(idComicTratar_mod.getText());
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
			listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
			ComicManagerDAO.actualizarComicBBDD(comic, "modificar");
			mensaje = "Has modificado correctamente el cómic";
		} else {
			ComicManagerDAO.insertarDatos(comic, true);
			mensaje = " Has introducido correctamente el cómic";
			Comic newSelection = tablaBBDD.getSelectionModel().getSelectedItem();

			if (newSelection != null) {
				listaComics = ListaComicsDAO.comicsImportados;
				String id_comic = newSelection.getID();
				ListaComicsDAO.comicsImportados.removeIf(c -> c.getID().equals(id_comic));
				tablaBBDD.getItems().clear();
			} else {
				listaComics = null; // Inicializar listaComics en caso de que no haya ningún cómic seleccionado
			}
		}

		final List<Comic> comicsFinal = listaComics; // Declarar otra variable final para listaComics

		Platform.runLater(() -> {
			funcionesTabla.tablaBBDD(comicsFinal, tablaBBDD, columnList);
		});

		tablaBBDD.refresh();
		AlarmaList.mostrarMensajePront(mensaje, esModificacion, prontInfo);
		procesarBloqueComun(comic);
	}

	/**
	 * Procesa el bloque común utilizado en la función procesarComic para actualizar
	 * la interfaz gráfica y realizar operaciones relacionadas con la manipulación
	 * de imágenes y la actualización de listas y combos.
	 *
	 * @param comic El objeto Comic que se está procesando.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	private void procesarBloqueComun(Comic comic) throws SQLException {
		File file = new File(comic.getImagen());
		Image imagen = new Image(file.toURI().toString(), 250, 0, true, true);
		imagencomic.setImage(imagen);

		List<ComboBox<String>> comboboxes = getComboBoxes();

		ListaComicsDAO.listasAutoCompletado();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD);
		funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);
		funcionesCombo.rellenarComboBox(comboboxes);
	}

	/**
	 * Realiza la acción asociada a la gestión de cómics de forma asíncrona.
	 *
	 * @param esModificacion Indica si la acción es una modificación de cómic.
	 */
	public void accionComicAsync(boolean esModificacion) {

		// Crear un nuevo hilo para la ejecución asíncrona
		Thread updateThread = new Thread(() -> {
			try {

				if (!ConectManager.conexionActiva()) {
					return;
				}

				// Iniciar la animación de cambio de imagen
				if (esModificacion) {
					modificacionComic();
				} else {
					subidaComic();
				}
				borrar_datos_autorellenos();
			} catch (Exception e) {
				Utilidades.manejarExcepcion(e);

			}
		});

		// Configurar el hilo como daemon (hilo en segundo plano)
		updateThread.setDaemon(true);

		// Iniciar el hilo
		updateThread.start();
	}

	public void autoRelleno() {

		idComicTratar_mod.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				if (!rellenarCampos(newValue)) {
					borrar_datos_autorellenos();
					borrarDatosGraficos();
				}
			} else {
				borrar_datos_autorellenos();
				borrarDatosGraficos();
			}
		});
	}

	private boolean rellenarCampos(String idComic) {
		Comic comic_temp = Comic.obtenerComic(idComic);
		if (comic_temp != null) {
			rellenarDatos(comic_temp);
			return true;
		}
		return false;
	}

	private void rellenarDatos(Comic comic) {
		numeroComic.getSelectionModel().clearSelection();
		formatoComic.getSelectionModel().clearSelection();
		numeroCajaComic.getSelectionModel().clearSelection();

		nombreComic.setText(comic.getNombre());
		numeroComic.getSelectionModel().select(comic.getNumero());
		varianteComic.setText(comic.getVariante());
		firmaComic.setText(comic.getFirma());
		editorialComic.setText(comic.getEditorial());
		formatoComic.getSelectionModel().select(comic.getFormato());
		procedenciaComic.getSelectionModel().select(comic.getProcedencia());

		LocalDate fecha = LocalDate.parse(comic.getFecha(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		fechaComic.setValue(fecha);

		guionistaComic.setText(comic.getGuionista());
		dibujanteComic.setText(comic.getDibujante());
		numeroCajaComic.getSelectionModel().select(comic.getNumCaja());
		nombreKeyIssue.setText(comic.getKey_issue());
		estadoComic.getSelectionModel().select(comic.getEstado());
		precioComic.setText(comic.getPrecio_comic());
		urlReferencia.setText(comic.getUrl_referencia());
		direccionImagen.setText(comic.getImagen());

		prontInfo.clear();
		prontInfo.setOpacity(1);

		Image imagenComic = Utilidades.pasarImagenComic(comic.getImagen());
		imagencomic.setImage(imagenComic);
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

		precioComic.setText("");
		codigoComicTratar.setText("");
		urlReferencia.setText("");
		numeroCajaComic.setValue("");
		numeroCajaComic.getEditor().setText("");

		nombreKeyIssue.setText("");
		direccionImagen.setText("");
		imagencomic.setImage(null);
		codigoComicTratar.setText("");
		idComicTratar_mod.setText("");

		formatoComic.getSelectionModel().selectFirst();
		procedenciaComic.getSelectionModel().selectFirst();
		estadoComic.getSelectionModel().selectFirst();

		validarCamposComic(true);
	}

	public void borrarDatosGraficos() {
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
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

	@FXML
	void verEstadoConexion(ActionEvent event) {
		nav.verEstadoConexion();

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

	public Scene miStageVentana() {
		Node rootNode = botonLimpiar;
		while (rootNode.getParent() != null) {
			rootNode = rootNode.getParent();
		}

		if (rootNode instanceof Parent) {
			Scene scene = ((Parent) rootNode).getScene();
			ConectManager.activeScenes.add(scene);
			return scene;
		} else {
			// Manejar el caso en el que no se pueda encontrar un nodo raíz adecuado
			return null;
		}
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
