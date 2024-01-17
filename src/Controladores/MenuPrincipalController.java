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
 *  Por Alejandro Rodriguez
 *
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Funcionamiento.FuncionesComboBox;
import Funcionamiento.FuncionesExcel;
import Funcionamiento.FuncionesTableView;
import Funcionamiento.FuncionesTooltips;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import alarmas.AlarmaList;
import comicManagement.Comic;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Esta clase sirve viajar a las diferentes ventanas del programa, asi como
 * realizar diferentes diferentes funciones
 *
 * @author Alejandro Rodriguez
 */
public class MenuPrincipalController implements Initializable {

	/**
	 * Botón para realizar una limpieza.
	 */
	@FXML
	private Button botonLimpiar;

	/**
	 * Botón para mostrar un parámetro.
	 */
	@FXML
	private Button botonMostrarParametro;

	/**
	 * Botón que permite imprimir el resultado de una busqueda por parametro
	 */
	@FXML
	private Button botonImprimir;

	/**
	 * Botón que permite guardar el resultado de una busqueda por parametro
	 */
	@FXML
	private Button botonGuardarResultado;

	/**
	 * Botón para acceder a la base de datos.
	 */
	@FXML
	private Button botonbbdd;

	/**
	 * Campo de texto para realizar una búsqueda general.
	 */
	@FXML
	private TextField busquedaGeneral;

	/**
	 * Campo de texto para ingresar un código de búsqueda.
	 */
	@FXML
	private TextField busquedaCodigo;

	/**
	 * Selector de fecha de publicación.
	 */
	@FXML
	private DatePicker fechaPublicacion;

	/**
	 * Imagen de un cómic.
	 */
	@FXML
	private ImageView imagencomic;

	/**
	 * Menú de archivo con opciones relacionadas con la base de datos.
	 */
	@FXML
	private MenuItem menu_archivo_backupbbdd, menu_archivo_cerrar, menu_archivo_delete, menu_archivo_desconectar,
			menu_archivo_excel, menu_archivo_importar, menu_archivo_sobreMi;

	/**
	 * Menú relacionado con operaciones de cómic.
	 */
	@FXML
	private MenuItem menu_comic_aleatoria, menu_comic_aniadir, menu_comic_eliminar, menu_comic_modificar,
			menu_comic_puntuar;

	/**
	 * Menú relacionado con estadísticas de cómics.
	 */
	@FXML
	private MenuItem menu_estadistica_comprados, menu_estadistica_estadistica, menu_estadistica_firmados,
			menu_estadistica_key_issue, menu_estadistica_posesion, menu_estadistica_puntuados,
			menu_estadistica_vendidos;

	/**
	 * Barra de menús de navegación.
	 */
	@FXML
	private MenuBar menu_navegacion;

	/**
	 * Menú de navegación para cerrar.
	 */
	@FXML
	private Menu navegacion_cerrar;

	/**
	 * Menú de navegación relacionado con cómics.
	 */
	@FXML
	private Menu navegacion_comic;

	/**
	 * Menú de navegación relacionado con estadísticas.
	 */
	@FXML
	private Menu navegacion_estadistica;

	/**
	 * Selector para el nombre del cómic.
	 */
	@FXML
	private ComboBox<String> nombreComic;

	/**
	 * Selector para el nombre del dibujante.
	 */
	@FXML
	private ComboBox<String> nombreDibujante;

	/**
	 * Selector para el nombre de la editorial.
	 */
	@FXML
	private ComboBox<String> nombreEditorial;

	/**
	 * Selector para el nombre de la firma.
	 */
	@FXML
	private ComboBox<String> nombreFirma;

	/**
	 * Selector para el nombre del formato.
	 */
	@FXML
	private ComboBox<String> nombreFormato;

	/**
	 * Selector para el nombre del guionista.
	 */
	@FXML
	private ComboBox<String> nombreGuionista;

	/**
	 * Selector para el nombre de la procedencia.
	 */
	@FXML
	private ComboBox<String> nombreProcedencia;

	/**
	 * Selector para el nombre de la variante.
	 */
	@FXML
	private ComboBox<String> nombreVariante;

	/**
	 * Selector para el número de caja.
	 */
	@FXML
	private ComboBox<String> numeroCaja;

	/**
	 * Selector para el número del cómic.
	 */
	@FXML
	private ComboBox<String> numeroComic;

	/**
	 * Área de texto para mostrar información.
	 */
	@FXML
	private TextArea prontInfo;

	/**
	 * Indicador de progreso.
	 */
	@FXML
	private ProgressIndicator progresoCarga;

	/**
	 * Columna de la tabla para el ID.
	 */
	@FXML
	private TableColumn<Comic, String> ID;

	/**
	 * Columna de la tabla para la caja.
	 */
	@FXML
	private TableColumn<Comic, String> caja;

	/**
	 * Columna de la tabla para el dibujante.
	 */
	@FXML
	private TableColumn<Comic, String> dibujante;

	/**
	 * Columna de la tabla para la editorial.
	 */
	@FXML
	private TableColumn<Comic, String> editorial;

	/**
	 * Columna de la tabla para la fecha.
	 */
	@FXML
	private TableColumn<Comic, String> fecha;

	/**
	 * Columna de la tabla para la firma.
	 */
	@FXML
	private TableColumn<Comic, String> firma;

	/**
	 * Columna de la tabla para el formato.
	 */
	@FXML
	private TableColumn<Comic, String> formato;

	/**
	 * Columna de la tabla para el guionista.
	 */
	@FXML
	private TableColumn<Comic, String> guionista;

	/**
	 * Columna de la tabla para el nombre.
	 */
	@FXML
	private TableColumn<Comic, String> nombre;

	/**
	 * Columna de la tabla para el número.
	 */
	@FXML
	private TableColumn<Comic, String> numero;

	/**
	 * Columna de la tabla para la procedencia.
	 */
	@FXML
	private TableColumn<Comic, String> procedencia;

	/**
	 * Columna de la tabla para la referencia.
	 */
	@FXML
	private TableColumn<Comic, String> referencia;

	/**
	 * Columna de la tabla para la variante.
	 */
	@FXML
	private TableColumn<Comic, String> variante;

	/**
	 * Tabla que muestra información sobre cómics.
	 */
	@FXML
	public TableView<Comic> tablaBBDD;

	/**
	 * Contenedor de la interfaz gráfica.
	 */
	@FXML
	private VBox rootVBox;

	/**
	 * Contenedor del contenido.
	 */
	@FXML
	private VBox vboxContenido;

	/**
	 * Imagen de fondo.
	 */
	@FXML
	private ImageView backgroundImage;

	/**
	 * Panel de anclaje principal.
	 */
	@FXML
	private AnchorPane rootAnchorPane;

	/**
	 * Contenedor de imágenes.
	 */
	@FXML
	private VBox vboxImage;

	/**
	 * Panel de anclaje para información.
	 */
	@FXML
	private AnchorPane anchoPaneInfo;

	/**
	 * Botón para modificar información.
	 */
	@FXML
	private Button botonModificar;

	/**
	 * Botón para introducir información.
	 */
	@FXML
	private Button botonIntroducir;

	/**
	 * Botón para eliminar información.
	 */
	@FXML
	private Button botonEliminar;

	/**
	 * Botón para agregar una puntuación.
	 */
	@FXML
	private Button botonAgregarPuntuacion;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de DBLibreriaManager para la gestión de la base de datos.
	 */
	private static DBLibreriaManager libreria = null;

	/**
	 * Instancia de FuncionesComboBox para funciones relacionadas con ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	/**
	 * Instancia de FuncionesTableView para funciones relacionadas con TableView.
	 */
	private static FuncionesTableView funcionesTabla = new FuncionesTableView();

	/**
	 * Lista de columnas de la tabla de cómics.
	 */
	private List<TableColumn<Comic, String>> columnList;

	private Map<Node, String> tooltipsMap = new HashMap<>();

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		libreria = new DBLibreriaManager();

		prontInfo.textProperty().addListener((observable, oldValue, newValue) -> {
			funcionesTabla.ajustarAnchoVBox(prontInfo, vboxContenido);
		});

		// Asegurarnos de que el VBox ajuste su tamaño correctamente al inicio
		Platform.runLater(() -> funcionesTabla.ajustarAnchoVBox(prontInfo, vboxContenido));

		Platform.runLater(() -> funcionesTabla.seleccionarRaw(tablaBBDD));

		Platform.runLater(() -> asignarTooltips());

		Platform.runLater(() -> funcionesTabla.modificarColumnas(tablaBBDD, columnList));

		List<TableColumn<Comic, String>> columnListCarga = Arrays.asList(nombre, caja, numero, variante, firma,
				editorial, formato, procedencia, fecha, guionista, dibujante, referencia);
		columnList = columnListCarga;

		restringir_entrada_datos();

		cargarDatosDataBase();

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

		establecerDinamismoAnchor();
	}

	/**
	 * Rellena los ComboBoxes estáticos con datos predefinidos. Los ComboBoxes se
	 * pasan como una lista en el orden: formato, procedencia, editorial.
	 */
	public void rellenarCombosEstaticos() {
		List<ComboBox<String>> comboboxesMod = Arrays.asList(nombreFormato, nombreProcedencia, nombreEditorial);
		funcionesCombo.rellenarComboBoxEstaticos(comboboxesMod, ""); // Llamada a la función para rellenar ComboBoxes
	}

	/**
	 * Carga los datos de la base de datos en los ComboBox proporcionados después de
	 * un segundo de retraso. Esta función utiliza un ScheduledExecutorService para
	 * programar la tarea.
	 *
	 * @param comboboxes Una lista de ComboBox que se actualizarán con los datos de
	 *                   la base de datos.
	 */
	public void cargarDatosDataBase() {

		List<ComboBox<String>> comboboxes = Arrays.asList(nombreComic, numeroComic, nombreVariante, nombreProcedencia,
				nombreFormato, nombreDibujante, nombreGuionista, nombreEditorial, nombreFirma, numeroCaja);

		int totalComboboxes = comboboxes.size();

		tablaBBDD.refresh();
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.schedule(() -> {

			Platform.runLater(() -> {
				libreria.listasAutoCompletado();

				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						funcionesCombo.rellenarComboBox(comboboxes);
						funcionesCombo.lecturaComboBox(totalComboboxes, comboboxes);
						return null;
					}
				};

				// Iniciar el Task en un nuevo hilo
				Thread thread = new Thread(task);
				thread.start();

				// Cuando la tarea haya terminado, apaga el scheduler
				task.setOnSucceeded(event -> {
					scheduler.shutdown();
				});
			});
		}, 0, TimeUnit.SECONDS);
	}

	/**
	 * Establece el dinamismo en la interfaz gráfica ajustando propiedades de
	 * elementos como tamaños, anchos y máximos.
	 */
	public void establecerDinamismoAnchor() {
		backgroundImage.fitWidthProperty().bind(rootAnchorPane.widthProperty());
		backgroundImage.fitHeightProperty().bind(rootAnchorPane.heightProperty());

		// Vinculación del ancho de la TableView al ancho del AnchorPane
		tablaBBDD.prefWidthProperty().bind(rootAnchorPane.widthProperty());

		// Vinculación del ancho de las columnas al ancho de la TableView dividido por
		// el número de columnas
		double numColumns = 13; // El número de columnas en tu TableView
		ID.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		nombre.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		caja.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		numero.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		variante.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		firma.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		editorial.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		formato.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		procedencia.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		fecha.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		guionista.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		dibujante.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		referencia.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));

		menu_navegacion.prefWidthProperty().bind(rootAnchorPane.widthProperty());

		double maxButtonWidth = 102.0; // Cambia esto al valor máximo deseado

		// Establecer un tamaño máximo para los botones
		botonLimpiar.maxWidthProperty().bind(Bindings.max(maxButtonWidth, botonLimpiar.widthProperty()));
		botonMostrarParametro.maxWidthProperty()
				.bind(Bindings.max(maxButtonWidth, botonMostrarParametro.widthProperty()));
		botonbbdd.maxWidthProperty().bind(Bindings.max(maxButtonWidth, botonbbdd.widthProperty()));

		double maxTextComboWidth = 162.0;

		Platform.runLater(() -> {
			// Ajustar el ancho de los campos de texto al ancho de la ventana principal
			busquedaGeneral.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, busquedaGeneral.widthProperty()));
		});

		// Ajustar el DatePicker
		fechaPublicacion.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, fechaPublicacion.widthProperty()));

		// Establecer un ancho máximo para cada ComboBox
		nombreComic.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, nombreComic.widthProperty()));
		nombreDibujante.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, nombreDibujante.widthProperty()));
		nombreEditorial.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, nombreEditorial.widthProperty()));
		nombreFirma.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, nombreFirma.widthProperty()));
		nombreFormato.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, nombreFormato.widthProperty()));
		nombreGuionista.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, nombreGuionista.widthProperty()));
		nombreProcedencia.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, nombreProcedencia.widthProperty()));
		nombreVariante.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, nombreVariante.widthProperty()));
		numeroCaja.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, numeroCaja.widthProperty()));
		numeroComic.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, numeroComic.widthProperty()));

		// Tamaño máximo predefinido para la imagen
		double maxWidth = 252.0; // Cambia esto al valor deseado
		double maxHeight = 337.0; // Cambia esto al valor deseado

		// Ajustar el tamaño máximo de la imagen
		imagencomic.fitWidthProperty().bind(Bindings.min(maxWidth, rootAnchorPane.widthProperty()));
		imagencomic.fitHeightProperty().bind(Bindings.min(maxHeight, rootAnchorPane.heightProperty()));

		// Asegúrate de que la relación de aspecto de la imagen se mantenga
		imagencomic.setPreserveRatio(true);
	}

	/**
	 * Asigna tooltips a varios elementos en la interfaz gráfica. Estos tooltips
	 * proporcionan información adicional cuando el usuario pasa el ratón sobre los
	 * elementos.
	 */
	public void asignarTooltips() {
		tooltipsMap.put(botonbbdd, "Muestra toda la base de datos");
		tooltipsMap.put(botonLimpiar, "Limpia la pantalla y reinicia todos los valores");
		tooltipsMap.put(nombreComic, "Nombre de los cómics / libros / mangas");
		tooltipsMap.put(numeroComic, "Número del cómic / libro / manga");
		tooltipsMap.put(nombreFirma, "Nombre de la firma del cómic / libro / manga");
		tooltipsMap.put(nombreGuionista, "Nombre del guionista del cómic / libro / manga");
		tooltipsMap.put(nombreVariante, "Nombre de la variante del cómic / libro / manga");
		tooltipsMap.put(numeroCaja, "Número de la caja donde se guarda el cómic / libro / manga");
		tooltipsMap.put(nombreProcedencia, "Nombre de la procedencia del cómic / libro / manga");
		tooltipsMap.put(nombreFormato, "Nombre del formato del cómic / libro / manga");
		tooltipsMap.put(nombreEditorial, "Nombre de la editorial del cómic / libro / manga");
		tooltipsMap.put(nombreDibujante, "Nombre del dibujante del cómic / libro / manga");
		tooltipsMap.put(fechaPublicacion, "Fecha del cómic / libro / manga");
		tooltipsMap.put(numeroCaja, "Caja donde guardas el cómic / libro / manga");
		tooltipsMap.put(busquedaGeneral,
				"Puedes buscar de forma general los cómic / libro / manga / artistas / guionistas");
		tooltipsMap.put(botonIntroducir, "Realizar una acción de introducción del cómic / libro / manga");
		tooltipsMap.put(botonModificar, "Realizar una acción de modificación del cómic / libro / manga");
		tooltipsMap.put(botonEliminar, "Realizar una acción de eliminación del cómic / libro / manga");
		tooltipsMap.put(botonAgregarPuntuacion, "Abrir una ventana para agregar puntuación del cómic / libro / manga");
		tooltipsMap.put(botonMostrarParametro, "Buscar por parámetros según los datos rellenados");

		FuncionesTooltips.assignTooltips(tooltipsMap);

	}

	/**
	 * Funcion que permite restringir entrada de datos de todo aquello que no sea un
	 * numero entero en los comboBox numeroComic y caja_comic
	 */
	public void restringir_entrada_datos() {
		numeroComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroCaja.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
	}

	/////////////////////////////////
	//// METODOS LLAMADA A VENTANAS//
	/////////////////////////////////

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

		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.modificarColumnas(tablaBBDD, columnList);
		tablaBBDD.refresh();
		prontInfo.clear();
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);

		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);

		if (completo) {
			funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion

			botonImprimir.setVisible(false);
			botonGuardarResultado.setVisible(false);

		} else {
			funcionesTabla.tablaBBDD(listaPorParametro(), tablaBBDD, columnList); // Llamada a funcion

			if (!listaPorParametro().isEmpty()) {
				botonImprimir.setVisible(true);
				botonGuardarResultado.setVisible(true);
			}
			busquedaGeneral.setText("");
		}
		Utilidades.borrarArchivosNoEnLista(DBLibreriaManager.listaImagenes);
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

		imprimirComicsEstado("puntuados");

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
		imprimirComicsEstado("vendidos");
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
		imprimirComicsEstado("firmados");
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
		imprimirComicsEstado("comprados");
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
		imprimirComicsEstado("posesion");

	}

	/**
	 * Maneja la acción de mostrar los cómics considerados "Key Issue".
	 *
	 * @param event El evento que desencadenó esta acción.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	@FXML
	void comicsKeyIssue(ActionEvent event) throws SQLException {
		imprimirComicsEstado("key");

	}

	private void imprimirComicsEstado(String tipoBusqueda) {

		prontInfo.setOpacity(0); // Ocultar la información en pantalla
		libreria = new DBLibreriaManager(); // Crear una instancia del gestor de la base de datos
		libreria.reiniciarBBDD(); // Reiniciar la base de datos si es necesario
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a la función para establecer nombres de
		funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList); // columnas

		switch (tipoBusqueda) {
		case "key": {
			funcionesTabla.tablaBBDD(libreria.libreriaKeyIssue(), tablaBBDD, columnList);
			break;
		}
		case "posesion": {
			funcionesTabla.tablaBBDD(libreria.libreriaPosesion(), tablaBBDD, columnList); // Llamada a funcion
			break;
		}

		case "comprados": {
			funcionesTabla.tablaBBDD(libreria.libreriaComprados(), tablaBBDD, columnList); // Llamada a funcion

			break;
		}
		case "firmados": {
			funcionesTabla.tablaBBDD(libreria.libreriaFirmados(), tablaBBDD, columnList); // Llamada a funcion

			break;
		}
		case "vendidos": {
			funcionesTabla.tablaBBDD(libreria.libreriaVendidos(), tablaBBDD, columnList); // Llamada a funcion

			break;
		}
		case "puntuados": {
			funcionesTabla.tablaBBDD(libreria.libreriaPuntuacion(), tablaBBDD, columnList); // Llamada a funcion

			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + tipoBusqueda);
		}

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
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@FXML
	void importCSV(ActionEvent event) throws SQLException, InterruptedException, ExecutionException {

		guardarDatosCSV();

		libreria.listasAutoCompletado();

		DBLibreriaManager.limpiarListaGuardados();

		prontInfo.clear();
	}

	/**
	 * Exporta un fichero CSV compatible con el programa que copia el contenido de
	 * la base de datos en un fichero CSV
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void exportCSV(ActionEvent event) throws SQLException {

		List<Comic> listaComics = libreria.libreriaCompleta();

		String tipoBusqueda = "completa";

		cargaExportExcel(listaComics, tipoBusqueda);
		
		prontInfo.clear();

		DBLibreriaManager.limpiarListaGuardados();
	}

	/**
	 * Exporta la base de datos en un fichero SQL
	 *
	 * @param event
	 */
	@FXML
	void exportarSQL(ActionEvent event) {

		makeSQL();

		limpiezaDeDatos();

	}

	/**
	 * Limpia los campos de pantalla donde se escriben los datos.
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		limpiezaDeDatos();
		botonImprimir.setVisible(false);
		botonImprimir.setDisable(true);
		botonGuardarResultado.setVisible(false);
		botonGuardarResultado.setDisable(true);

		int tamanioListaGuardada = DBLibreriaManager.comicsGuardadosList.size();

		if (tamanioListaGuardada > 0 && nav.borrarListaGuardada()) {

			DBLibreriaManager.limpiarListaGuardados();

			String mensaje = "Has eliminado el contenido de la lista guardada que contenia un total de: "
					+ tamanioListaGuardada + " comics guardados.\n \n \n";

			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
		}
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

		// Crear tarea para borrar contenido de la tabla
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				boolean result = false;
				if (!DBLibreriaManager.listaNombre.isEmpty()) {
					CompletableFuture<Boolean> futureResult = nav.borrarContenidoTabla();
					result = futureResult.join();
				}
				return result;
			}
		};

		// Configurar comportamiento cuando la tarea está en ejecución
		task.setOnRunning(e -> AlarmaList.iniciarAnimacionCarga(progresoCarga));

		// Configurar comportamiento cuando la tarea se completa con éxito
		task.setOnSucceeded(e -> {
			Boolean resultado = task.getValue();

			if (resultado) {
				limpiarInformacion();

				Task<Boolean> deleteTask = createDeleteTask();

				// Configurar comportamiento cuando la tarea de borrado se completa con éxito
				deleteTask.setOnSucceeded(ev -> {
					Platform.runLater(() -> {
						limpiezaDeDatos();
						mostrarMensajeExito();
						cargarDatosDataBase();
						DBLibreriaManager.limpiarListaGuardados();
						detenerAnimaciones();
					});
				});

				// Configurar comportamiento cuando la tarea de borrado falla
				deleteTask.setOnFailed(ev -> handleDeleteTaskFailure(deleteTask));

				// Iniciar la tarea de borrado en un hilo separado
				new Thread(deleteTask).start();
			} else {
				mostrarMensajeCancelacion();
				detenerAnimaciones();
			}
		});

		// Configurar comportamiento cuando la tarea falla
		task.setOnFailed(e -> handleTaskFailure(task));

		nav.ventanaAbierta();

		// Iniciar la tarea principal de borrado en un hilo separado
		new Thread(task).start();
	}

	private Task<Boolean> createDeleteTask() {
		return new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				boolean result = false;
				if (libreria.countRows() > 0) {
					CompletableFuture<Boolean> futureResult = libreria.deleteTable();
					result = futureResult.join();
				} else {
					showEmptyDatabaseError();
				}
				return result;
			}
		};
	}

	private void handleDeleteTaskFailure(Task<Boolean> deleteTask) {
		AlarmaList.detenerAnimacion();
		Throwable exception = deleteTask.getException();
		if (exception != null) {
			mostrarErrorBorrado(exception.getMessage());
		} else {
			mostrarErrorDesconocidoBorrado();
		}
	}

	private void handleTaskFailure(Task<Boolean> task) {
		AlarmaList.detenerAnimacion();
		Throwable exception = task.getException();
		if (exception != null) {
			nav.alertaException("Error al importar el fichero CSV: " + exception.getMessage());
		} else {
			nav.alertaException("Error desconocido al importar el fichero CSV.");
		}
	}

	private void limpiarInformacion() {
		prontInfo.clear();
		prontInfo.setStyle(null);
		new Thread(() -> AlarmaList.iniciarAnimacionBajada(prontInfo)).start();
	}

	private void mostrarMensajeExito() {
		String mensaje = "Has borrado correctamente el contenido de la base de datos.";
		AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
	}

	private void mostrarMensajeCancelacion() {
		prontInfo.clear();
		String mensaje = "Has cancelado el borrado de la base de datos.";
		AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
	}

	private void showEmptyDatabaseError() {
		Platform.runLater(() -> {
			String mensaje = "La base de datos ya se encuentra vacía.";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			detenerAnimaciones();
		});
	}

	private void mostrarErrorBorrado(String errorMessage) {
		prontInfo.clear();
		String mensaje = "ERROR. No se ha podido borrar el contenido de la base de datos. " + errorMessage;
		AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
		detenerAnimaciones();
	}

	private void mostrarErrorDesconocidoBorrado() {
		prontInfo.clear();
		Platform.runLater(() -> nav.alertaException("Error desconocido al borrar el contenido de la base de datos"));
		detenerAnimaciones();
	}

	private void detenerAnimaciones() {
		AlarmaList.detenerAnimacion();
		AlarmaList.detenerAnimacionCarga(progresoCarga);
		prontInfo.clear();
	}

	/**
	 * Se llama a funcion que permite ver las estadisticas de la bbdd
	 *
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void verEstadistica(ActionEvent event) throws IOException {

		AlarmaList alarmaList = new AlarmaList();
		libreria = new DBLibreriaManager();
		prontInfo.clear();
		prontInfo.setOpacity(1);
		alarmaList.iniciarAnimacionEstadistica(prontInfo);
		libreria.generar_fichero_estadisticas();
		AlarmaList.detenerAnimacionPront(prontInfo);
		prontInfo.setText("Fichero creado correctamente");
	}

	/////////////////////////////////
	//// FUNCIONES////////////////////
	/////////////////////////////////

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
		seleccionarComics();
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
			seleccionarComics();
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
		libreria = new DBLibreriaManager();
		libreria.libreriaCompleta();

		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();
		// Verificar si idRow es nulo antes de intentar acceder a sus métodos
		if (idRow != null) {
			prontInfo.clear();
			String id_comic = idRow.getID();

			String mensaje = libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", "");
			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
			funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
			funcionesTabla.actualizarBusquedaRaw(tablaBBDD, columnList);
			imagencomic.setImage(libreria.selectorImage(id_comic));
		}
	}

	/////////////////////////////////
	//// FUNCIONES CREACION FICHEROS//
	/////////////////////////////////

	/**
	 * Maneja la acción de impresión del resultado. Obtiene una lista de cómics
	 * según los parámetros especificados y realiza la exportación de la información
	 * a un archivo Excel.
	 *
	 * @param event El evento de acción que desencadena la impresión del resultado.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	@FXML
	void imprimirResultado(ActionEvent event) throws SQLException {

		List<Comic> listaComics = listaPorParametro();

		String tipoBusqueda = "Parcial";
		prontInfo.clear();
		if (DBLibreriaManager.comicsGuardadosList.size() > 0) {
			cargaExportExcel(DBLibreriaManager.comicsGuardadosList, tipoBusqueda);
			
			String mensaje = "Lista guardada de forma correcta";
			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
			
		} else {
			cargaExportExcel(listaComics, tipoBusqueda);

		}
	}

	/**
	 * Guarda los resultados de la lista de cómics en la base de datos de la
	 * librería, asegurándose de mantener una lista única de cómics en la base de
	 * datos. Además, realiza la limpieza de datos y actualiza la visibilidad y
	 * desactiva los botones de guardar resultado e imprimir.
	 *
	 * @param event El evento que desencadenó la llamada a esta función.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	@FXML
	void guardarResultado(ActionEvent event) throws SQLException {

		List<Comic> listaComics = listaPorParametro();

		String mensaje = "";

		if (listaPorParametro().size() > 0 && listaPorParametro() != null) {
			DBLibreriaManager.agregarElementosUnicos(listaComics);

			mensaje = "Hay un total de: " + DBLibreriaManager.comicsGuardadosList.size()
					+ ". Comics guardados a la espera de ser impresos \n \n \n";
			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

		} else {
			mensaje = "No se esta mostrando ningun comic, prueba a buscar por parametro \n \n \n";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
		}

		limpiezaDeDatos();

	}

	/**
	 * Carga y ejecuta una tarea para exportar datos a un archivo Excel.
	 *
	 * @param fichero     El archivo Excel de destino.
	 * @param listaComics La lista de cómics a exportar.
	 */
	private void cargaExportExcel(List<Comic> listaComics, String tipoBusqueda) {

		FuncionesExcel excelFuntions = new FuncionesExcel();
		AlarmaList alarmaList = new AlarmaList();
		String mensajeErrorExportar = "ERROR. No se ha podido exportar correctamente.";
		String mensajeCancelarExportar = "ERROR. Se ha cancelado la exportación.";

		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);
		prontInfo.setOpacity(0);

		// Configuración de la tarea para crear el archivo Excel
		Task<Boolean> crearExcelTask = excelFuntions.crearExcelTask(listaComics, tipoBusqueda);
		Thread excelThread = new Thread(crearExcelTask);

		// Configuración del comportamiento cuando la tarea está en ejecución
		crearExcelTask.setOnRunning(e -> {
			// Iniciar la animación
			AlarmaList.iniciarAnimacionCarga(progresoCarga);
		});

		// Configuración del comportamiento cuando la tarea tiene éxito
		crearExcelTask.setOnSucceeded(event -> {
			boolean result = crearExcelTask.getValue();
			if (result) {
				// Tarea completada con éxito, muestra el mensaje de éxito.
				String mensaje = "Fichero excel exportado de forma correcta";
				AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

			} else {
				// La tarea no se completó correctamente, muestra un mensaje de error.
				String mensaje = "ERROR. No se ha podido exportar correctamente.";
				AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);

			}
			AlarmaList.detenerAnimacionPront(prontInfo);
			AlarmaList.detenerAnimacionCarga(progresoCarga);

			// Detener el hilo de la tarea
			excelThread.interrupt();
		});

		// Configuración del comportamiento cuando la tarea falla
		crearExcelTask.setOnFailed(event -> alarmaList.manejarFallo(mensajeErrorExportar, prontInfo));

		// Configuración del comportamiento cuando la tarea es cancelada
		crearExcelTask.setOnCancelled(event -> alarmaList.manejarFallo(mensajeCancelarExportar, prontInfo));

		// Iniciar la tarea principal de creación de Excel en un hilo separado
		excelThread.start();
	}

	public void guardarDatosCSV() {
		String frase = "Fichero CSV";
		String formato = "*.csv";

		File fichero = Utilidades.tratarFichero(frase, formato).showOpenDialog(null);

		if (fichero != null) {
			Task<Boolean> task = crearTareaImportacionCSV(fichero);

			// Configurar el comportamiento cuando la tarea está en ejecución
			task.setOnRunning(e -> AlarmaList.iniciarAnimacionCarga(progresoCarga));

			// Configurar el comportamiento cuando la tarea se completa con éxito
			task.setOnSucceeded(e -> procesarResultadoImportacion(task.getValue(), fichero));

			// Configurar el comportamiento cuando la tarea falla
			task.setOnFailed(e -> AlarmaList.manejarFalloImportacion(task.getException(), prontInfo));

			// Iniciar la tarea principal de importación en un hilo separado
			new Thread(task).start();
		}
	}

	private Task<Boolean> crearTareaImportacionCSV(File fichero) {
		return new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				// Lógica para verificar y realizar la importación CSV
				return true; // O false si falla la importación
			}
		};
	}

	private void procesarResultadoImportacion(Boolean resultado, File fichero) {

		if (resultado) {
			// Lógica después de una importación exitosa
			ejecutarOperacionLecturaGuardadoBD(fichero);
			AlarmaList.detenerAnimacion();
			
			String mensaje = "Datos importados correctamente.";
			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
		} else {
			// Lógica después de una importación fallida
			String mensaje = "No se ha podido importar correctamente.";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
		}
	}

	private void ejecutarOperacionLecturaGuardadoBD(File fichero) {
		FuncionesExcel funcionesExcel = new FuncionesExcel();
		AlarmaList alarmaList = new AlarmaList();
		Platform.runLater(() -> {
			prontInfo.setStyle(null);
			prontInfo.clear();
			prontInfo.setOpacity(1);
			Thread animationThread = new Thread(() -> alarmaList.iniciarAnimacionSubida(prontInfo));
			animationThread.start();

			String sql = "INSERT INTO comicsbbdd(ID,nomComic,caja_deposito,precio_comic,codigo_comic,numComic,nomVariante,Firma,nomEditorial,Formato,Procedencia,fecha_publicacion,nomGuionista,nomDibujante,puntuacion,portada,key_issue,url_referencia,estado)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			try {
				int numLineas = Utilidades.contarLineas(fichero);
				BufferedReader lineReader = new BufferedReader(new FileReader(fichero));
				Task<Void> lecturaTask = funcionesExcel.lecturaCSVTask(sql, lineReader, numLineas);

				lecturaTask.setOnSucceeded(event -> {
					// Lógica después de la inserción exitosa en la base de datos
					String mensaje = "Fichero CSV importado de forma correcta";
					AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
					cargarDatosDataBase();
				});

				lecturaTask.setOnFailed(event -> {
					// Lógica después de una inserción fallida en la base de datos
					AlarmaList.manejarFalloGuardadoBD(lecturaTask.getException(), prontInfo);
				});

				new Thread(lecturaTask).start();

			} catch (IOException ex) {
				// Lógica después de un error al importar
				AlarmaList.manejarFalloImportacion(ex, prontInfo);
			}

//			AlarmaList.detenerAnimacionPront(prontInfo);
			AlarmaList.detenerAnimacion();
			AlarmaList.detenerAnimacionCarga(progresoCarga);
		});
	}

	/**
	 * Funcion crea el fichero SQL segun el sistema operativo en el que te
	 * encuentres.
	 *
	 * @param fichero
	 */
	public void makeSQL() {

		libreria = new DBLibreriaManager();

		String frase = "Fichero SQL";

		String formato = "*.sql";

		File fichero = Utilidades.tratarFichero(frase, formato).showSaveDialog(null); // Llamada a funcion

		prontInfo.setOpacity(0);
		if (fichero != null) {

			if (Utilidades.isWindows()) {
				libreria.backupWindows(fichero); // Llamada a funcion
				String mensaje = "Base de datos exportada \ncorrectamente";

				AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

			} else {
				if (Utilidades.isUnix()) {
					libreria.backupLinux(fichero); // Llamada a funcion
					String mensaje = "Base de datos exportada \ncorrectamente";

					AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
				}
			}
		} else {
			String mensaje = "ERROR. Se ha cancelado la exportacion de la base de datos.";

			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
		}
	}

	/**
	 * Funcion que comprueba segun los datos escritos en los textArea, que comic
	 * estas buscando.
	 * 
	 * @throws SQLException
	 */
	public List<Comic> listaPorParametro() {
		libreria = new DBLibreriaManager();
		Comic datos = camposComic();
		prontInfo.setOpacity(1);
		prontInfo.setText(funcionesTabla.resultadoBusquedaPront(datos).getText());
		busquedaGeneral.setText("");

		List<Comic> listComic = FXCollections
				.observableArrayList(libreria.busquedaParametro(datos, busquedaGeneral.getText()));

		return listComic;
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
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public Comic camposComic() {
		Utilidades utilidad = new Utilidades();
		Comic comic = new Comic();
		LocalDate fecha = fechaPublicacion.getValue();
		String fechaComic = (fecha != null) ? fecha.toString() : "";

		comic.setNombre(Utilidades.defaultIfNullOrEmpty(utilidad.comaPorGuion(nombreComic.getValue()), ""));
		comic.setNumero(Utilidades
				.defaultIfNullOrEmpty(utilidad.comaPorGuion(FuncionesComboBox.numeroCombobox(numeroComic)), ""));
		comic.setVariante(Utilidades.defaultIfNullOrEmpty(utilidad.comaPorGuion(nombreVariante.getValue()), ""));
		comic.setFirma(Utilidades.defaultIfNullOrEmpty(utilidad.comaPorGuion(nombreFirma.getValue()), ""));
		comic.setEditorial(Utilidades.defaultIfNullOrEmpty(utilidad.comaPorGuion(nombreEditorial.getValue()), ""));
		comic.setFormato(Utilidades.defaultIfNullOrEmpty(FuncionesComboBox.formatoCombobox(nombreFormato), ""));
		comic.setProcedencia(
				Utilidades.defaultIfNullOrEmpty(FuncionesComboBox.procedenciaCombobox(nombreProcedencia), ""));
		comic.setFecha(fechaComic);
		comic.setGuionista(Utilidades.defaultIfNullOrEmpty(utilidad.comaPorGuion(nombreGuionista.getValue()), ""));
		comic.setDibujante(Utilidades.defaultIfNullOrEmpty(utilidad.comaPorGuion(nombreDibujante.getValue()), ""));
		comic.setImagen("");
		comic.setEstado("");
		comic.setNumCaja(Utilidades.defaultIfNullOrEmpty(FuncionesComboBox.cajaCombobox(numeroCaja), ""));
		comic.setKey_issue("");
		comic.setUrl_referencia("");
		comic.setPrecio_comic("");
		comic.setCodigo_comic("");

		return comic;
	}

	/**
	 * Realiza la limpieza de datos en la interfaz gráfica.
	 */
	private void limpiezaDeDatos() {

		List<ComboBox<String>> listaComboBox = new ArrayList<>(
				List.of(nombreComic, numeroComic, nombreVariante, nombreProcedencia, nombreFormato, nombreDibujante,
						nombreGuionista, nombreEditorial, nombreFirma, numeroCaja));

		// Iterar sobre todos los ComboBox para realizar la limpieza
		for (ComboBox<String> comboBox : listaComboBox) {
			// Limpiar el campo
			comboBox.setValue("");
			comboBox.getEditor().setText("");
		}

		// Limpiar elementos adicionales de la interfaz
		fechaPublicacion.setValue(null);
		prontInfo.clear();
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);
		tablaBBDD.refresh();

	}

	/**
	 * Maneja la acción del usuario en relación a los cómics, como agregar,
	 * modificar, eliminar o puntuar un cómic.
	 *
	 * @param event El evento de acción que desencadenó la llamada a esta función.
	 */
	@FXML
	void accionComic(ActionEvent event) {
		Object fuente = event.getSource();
		tablaBBDD.getItems().clear();

		VentanaAccionController ventanaAccion = new VentanaAccionController();

		// Crear la lista de ComboBoxes
		List<ComboBox<String>> comboboxes = Arrays.asList(nombreComic, numeroComic, nombreVariante, nombreProcedencia,
				nombreFormato, nombreDibujante, nombreGuionista, nombreEditorial, nombreFirma, numeroCaja);

		// Pasar la lista de ComboBoxes a VentanaAccionController
		ventanaAccion.setComboBoxes(comboboxes);

		if (fuente instanceof Button) {
			Button botonPresionado = (Button) fuente;

			if (botonPresionado == botonIntroducir) {
				VentanaAccionController.tipoAccion("aniadir");
			} else if (botonPresionado == botonModificar) {
				VentanaAccionController.tipoAccion("modificar");
			} else if (botonPresionado == botonEliminar) {
				VentanaAccionController.tipoAccion("eliminar");
			} else if (botonPresionado == botonAgregarPuntuacion) {
				VentanaAccionController.tipoAccion("puntuar");
			}
		} else if (fuente instanceof MenuItem) {
			MenuItem menuItemPresionado = (MenuItem) fuente;

			if (menuItemPresionado == menu_comic_aniadir) {
				VentanaAccionController.tipoAccion("aniadir");
			} else if (menuItemPresionado == menu_comic_modificar) {
				VentanaAccionController.tipoAccion("modificar");
			} else if (menuItemPresionado == menu_comic_eliminar) {
				VentanaAccionController.tipoAccion("eliminar");
			} else if (menuItemPresionado == menu_comic_puntuar) {
				VentanaAccionController.tipoAccion("puntuar");
			}
		}

		nav.verAccionComic();
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
		DBManager.close();
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
