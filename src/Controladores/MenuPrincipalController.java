/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

import java.io.File;
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
import Funcionamiento.FuncionesManejoFront;
import Funcionamiento.FuncionesTableView;
import Funcionamiento.FuncionesTooltips;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.DBUtilidades.TipoBusqueda;
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
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

	@FXML
	private Button botonMostrarGuardados;

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
	private MenuItem menu_archivo_cerrar, menu_archivo_delete, menu_archivo_desconectar,
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
	
    @FXML
    private MenuItem menu_archivo_avanzado;

	@FXML
	private MenuItem menu_archivo_conexion;

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

	@FXML
	private Label alarmaConexionSql;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de FuncionesComboBox para funciones relacionadas con ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	/**
	 * Lista de columnas de la tabla de cómics.
	 */
	private List<TableColumn<Comic, String>> columnList;

	ObservableList<ImageView> listaImagenes;

	List<ComboBox<String>> comboboxes;
	@SuppressWarnings("rawtypes")
	ObservableList<TableColumn> listaColumnas;
	ObservableList<Control> listaCamposTexto;
	ObservableList<Button> listaBotones;
	ObservableList<Node> listaElementosFondo;

	private Map<Node, String> tooltipsMap = new HashMap<>();

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AlarmaList alarmaList = new AlarmaList();

		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.iniciarThreadChecker(true);

		Platform.runLater(() -> {
			FuncionesTableView.ajustarAnchoVBox(prontInfo, vboxContenido);
			FuncionesTableView.seleccionarRaw(tablaBBDD);
			asignarTooltips();
			FuncionesTableView.modificarColumnas(tablaBBDD, columnList);
		});

		listaElementosVentana();

		controlarEventosInterfaz();

		formatearTextField();

		cargarDatosDataBase();

		establecerDinamismoAnchor();
	}

	@FXML
	void ampliarImagen(MouseEvent event) {

		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (idRow != null) {
			String direccionImagen = idRow.getImagen();

			ImagenAmpliadaController.direccionImagen = direccionImagen;

			nav.verVentanaImagen();
		}
	}

	/**
	 * Controla los eventos de la interfaz, desactivando el enfoque en el VBox para
	 * evitar eventos de teclado, y añadiendo filtros y controladores de eventos
	 * para gestionar el enfoque entre el VBox y el TableView.
	 */
	private void controlarEventosInterfaz() {

		List<TableColumn<Comic, String>> columnListCarga = Arrays.asList(nombre, caja, numero, variante, firma,
				editorial, formato, procedencia, fecha, guionista, dibujante, referencia);
		columnList = columnListCarga;

		prontInfo.textProperty().addListener((observable, oldValue, newValue) -> {
			FuncionesTableView.ajustarAnchoVBox(prontInfo, vboxContenido);
		});

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

		imagencomic.imageProperty().addListener((observable, oldImage, newImage) -> {
			if (newImage != null) {
				// Cambiar la apariencia del cursor y la opacidad cuando la imagen se ha cargado
				imagencomic.setOnMouseEntered(e -> {
					imagencomic.setOpacity(0.7); // Cambiar la opacidad para indicar que es clickable
					imagencomic.setCursor(Cursor.HAND);
				});

				// Restaurar el cursor y la opacidad al salir del ImageView
				imagencomic.setOnMouseExited(e -> {
					imagencomic.setOpacity(1.0); // Restaurar la opacidad
					imagencomic.setCursor(Cursor.DEFAULT);
				});
			} else {
				// Restaurar el cursor y la opacidad al salir del ImageView
				imagencomic.setOnMouseEntered(e -> {
					imagencomic.setCursor(Cursor.DEFAULT);
				});
			}
		});

		botonGuardarResultado.setOnMousePressed(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				// Si la lista está vacía, oculta el botón
				botonMostrarGuardados.setVisible(true);
			}
		});

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

		comboboxes = Arrays.asList(nombreComic, numeroComic, nombreVariante, nombreProcedencia, nombreFormato,
				nombreDibujante, nombreGuionista, nombreEditorial, nombreFirma, numeroCaja);

		tablaBBDD.refresh();
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		scheduler.schedule(() -> {

			Platform.runLater(() -> {

				ListaComicsDAO.listasAutoCompletado();

				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						funcionesCombo.rellenarComboBox(comboboxes);
						funcionesCombo.lecturaComboBox(comboboxes);
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

	@SuppressWarnings("unchecked")
	public void listaElementosVentana() {
		listaImagenes = FXCollections.observableArrayList(imagencomic);
		listaColumnas = FXCollections.observableArrayList(ID, nombre, caja, numero, variante, firma, editorial, formato,
				procedencia, fecha, guionista, dibujante, referencia);
		listaCamposTexto = FXCollections.observableArrayList(busquedaGeneral, fechaPublicacion);
		listaBotones = FXCollections.observableArrayList(botonLimpiar, botonMostrarParametro, botonbbdd, botonImprimir,
				botonGuardarResultado);

		comboboxes = FXCollections.observableArrayList(nombreComic, numeroComic, nombreVariante, nombreProcedencia,
				nombreFormato, nombreDibujante, nombreGuionista, nombreEditorial, nombreFirma, numeroCaja);

		listaElementosFondo = FXCollections.observableArrayList(backgroundImage, menu_navegacion);
	}

	/**
	 * Establece el dinamismo en la interfaz gráfica ajustando propiedades de
	 * elementos como tamaños, anchos y máximos.
	 */
	public void establecerDinamismoAnchor() {

		FuncionesManejoFront manejoFront = new FuncionesManejoFront();

		manejoFront.copiarListas(comboboxes, columnList, listaCamposTexto, listaBotones, listaElementosFondo,
				listaImagenes);

		manejoFront.copiarElementos(prontInfo, botonImprimir, botonGuardarResultado, busquedaGeneral, columnList);

		FuncionesManejoFront.setAnchorPane(rootAnchorPane);
		manejoFront.setTableView(tablaBBDD);

		FuncionesManejoFront.establecerFondoDinamico();

		FuncionesManejoFront.establecerAnchoColumnas(13);

		FuncionesManejoFront.establecerAnchoMaximoBotones(102.0);

		FuncionesManejoFront.establecerAnchoMaximoCamposTexto(162.0);

		FuncionesManejoFront.establecerAnchoMaximoComboBoxes(162.0);

		FuncionesManejoFront.establecerTamanioMaximoImagen(252.0, 337.0);

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
	public void formatearTextField() {

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
		ConectManager.resetConnection();

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
		ConectManager.resetConnection();

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

		mostrarComics(false);
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

		mostrarComics(true);
	}

	private void mostrarComics(boolean esCompleto) {

		FuncionesManejoFront manejoFront = new FuncionesManejoFront();
		manejoFront.setTableView(tablaBBDD);
		manejoFront.copiarElementos(prontInfo, botonImprimir, botonGuardarResultado, busquedaGeneral, columnList);

		List<Node> elementos = Arrays.asList(botonImprimir, botonGuardarResultado);

		Utilidades.cambiarVisibilidad(elementos, false);

		busquedaGeneral.setText("");

		if (esCompleto) {
			FuncionesManejoFront.verBasedeDatos(true, false, null);
		} else {
			Comic comicBusqueda = camposComic();

			FuncionesManejoFront.verBasedeDatos(false, false, comicBusqueda);
		}

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
		imprimirComicsEstado(TipoBusqueda.PUNTUACION, false);
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
		imprimirComicsEstado(TipoBusqueda.VENDIDOS, false);
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
		imprimirComicsEstado(TipoBusqueda.FIRMADOS, false);
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
		imprimirComicsEstado(TipoBusqueda.COMPRADOS, false);
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
		imprimirComicsEstado(TipoBusqueda.POSESION, false);
	}

	@FXML
	void comicsGuardados(ActionEvent event) throws SQLException {
		imprimirComicsEstado(null, true);

	}
	
	@FXML
	void verOpcionesAvanzadas(ActionEvent event) throws SQLException {
		nav.verOpcionesAvanzadas();

	}
	
	/**
	 * Maneja la acción de mostrar los cómics considerados "Key Issue".
	 *
	 * @param event El evento que desencadenó esta acción.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	@FXML
	void comicsKeyIssue(ActionEvent event) throws SQLException {
		imprimirComicsEstado(TipoBusqueda.KEY_ISSUE, false);
	}

	private void imprimirComicsEstado(TipoBusqueda tipoBusqueda, boolean esGuardado) {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		limpiezaDeDatos();
		limpiarComboBox();
		ListaComicsDAO.reiniciarListaComics(); // Reiniciar la base de datos si es necesario
		FuncionesTableView.nombreColumnas(columnList, tablaBBDD); // Llamada a la función para establecer nombres de
		FuncionesTableView.actualizarBusquedaRaw(tablaBBDD, columnList); // columnas
		List<Comic> listaComics = new ArrayList<Comic>();
		if (esGuardado) {
			listaComics = ListaComicsDAO.comicsGuardadosList;
		} else {
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(tipoBusqueda);

			listaComics = SelectManager.verLibreria(sentenciaSQL);
		}

		FuncionesTableView.tablaBBDD(listaComics, tablaBBDD, columnList);
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

		if (!ConectManager.conexionActiva()) {
			return;
		}

		limpiezaDeDatos();
		limpiarComboBox();

		guardarDatosCSV();

		ListaComicsDAO.listasAutoCompletado();

		ListaComicsDAO.limpiarListaGuardados();

		Utilidades.borrarArchivosNoEnLista(ListaComicsDAO.listaImagenes);

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

		if (!ConectManager.conexionActiva()) {
			return;
		}

		limpiezaDeDatos();
		limpiarComboBox();
		String tipoBusqueda = "completa";
		String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

		List<Comic> listaComics = SelectManager.verLibreria(sentenciaSQL);

		cargaExportExcel(listaComics, tipoBusqueda);

		ListaComicsDAO.limpiarListaGuardados();

		Utilidades.borrarArchivosNoEnLista(ListaComicsDAO.listaImagenes);
	}

	/**
	 * Limpia los campos de pantalla donde se escriben los datos.
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		limpiezaDeDatos();
		limpiarComboBox();
		botonImprimir.setVisible(false);
		botonImprimir.setDisable(true);
		botonGuardarResultado.setVisible(false);
		botonGuardarResultado.setDisable(true);

		int tamanioListaGuardada = ListaComicsDAO.comicsGuardadosList.size();

		if (tamanioListaGuardada > 0 && nav.borrarListaGuardada()) {

			ListaComicsDAO.limpiarListaGuardados();

			String mensaje = "Has eliminado el contenido de la lista guardada que contenia un total de: "
					+ tamanioListaGuardada + " comics guardados.\n \n \n";

			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

			botonMostrarGuardados.setVisible(false);
		}
	}

	@FXML
	void borrarContenidoTabla(ActionEvent event) {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		if (ComicManagerDAO.countRows(SelectManager.TAMANIO_DATABASE) < 1) {
			String mensaje = "ERROR. La base de datos ya se encuentra vacia";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			return;
		}

		CompletableFuture.supplyAsync(this::deleteTableAsync).thenAccept(result -> handleDeleteResult(result));
	}

	private boolean deleteTableAsync() {
		try {

			Utilidades.borrarArchivosNoEnLista(ListaComicsDAO.listaImagenes);

			CompletableFuture<Boolean> borradoTablaFuture = nav.borrarContenidoTabla();
			boolean confirmacionBorrado = borradoTablaFuture.get(); // Espera a que el CompletableFuture se complete
																	// y obtiene el resultado
			if (confirmacionBorrado) {
				CompletableFuture<Boolean> deleteResult = ComicManagerDAO.deleteTable();
				return deleteResult.get(); // Espera a que el CompletableFuture se complete y obtiene el resultado
			} else {
				String mensaje = "ERROR. Has cancelado el borrado de la base de datos";
				AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
				return false;
			}

		} catch (Exception e) {
			Utilidades.manejarExcepcion(e);
			return false;
		}
	}

	private void handleDeleteResult(boolean deleteResult) {

		String mensaje = "";
		if (deleteResult) {
			mensaje = "Base de datos borrada y reiniciada correctamente";
			limpiezaDeDatos();
		} else {
			mensaje = "ERROR. No se ha podido eliminar y reiniciar la base de datos";
		}
		AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
	}

	/**
	 * Se llama a funcion que permite ver las estadisticas de la bbdd
	 *
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void verEstadistica(ActionEvent event) throws IOException {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		AlarmaList.iniciarAnimacionEstadistica(prontInfo);
		ListaComicsDAO.generar_fichero_estadisticas();
		AlarmaList.detenerAnimacionPront(prontInfo);
		String mensaje = "Fichero creado correctamente";

		AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
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

		if (!ConectManager.conexionActiva()) {
			return;
		}

		String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

		SelectManager.verLibreria(sentenciaSQL);

		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();
		// Verificar si idRow es nulo antes de intentar acceder a sus métodos
		if (idRow != null) {
			AlarmaList.detenerAnimacion();
			String id_comic = idRow.getID();

			String mensaje = SelectManager.comicDatos(id_comic).toString().replace("[", "").replace("]", "");
			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
			FuncionesTableView.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
			FuncionesTableView.actualizarBusquedaRaw(tablaBBDD, columnList);

			String direccionImagen = SelectManager.obtenerDireccionPortada(id_comic);

			Utilidades.cargarImagenAsync(direccionImagen, imagencomic);
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

		if (!ConectManager.conexionActiva()) {
			return;
		}

		prontInfo.clear();
		String tipoBusqueda = "Parcial";

		if (ListaComicsDAO.comicsGuardadosList.size() > 0) {
			cargaExportExcel(ListaComicsDAO.comicsGuardadosList, tipoBusqueda);

			String mensaje = "Lista guardada de forma correcta";
			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

		} else {
			String mensaje = "La lista esta vacia";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
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

		if (!ConectManager.conexionActiva()) {
			return;
		}

		Comic comicRaw = tablaBBDD.getSelectionModel().getSelectedItem();
		String mensaje = "";
		if (comicRaw != null) {
			boolean existeComic = ListaComicsDAO.verificarIDExistente(comicRaw.getID(), true);
			if (existeComic) {
				mensaje = "Este comic con dicha ID: " + comicRaw.getID() + " ya existe. No se ha guardado \n \n \n";
				AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
				return;
			}

			ListaComicsDAO.agregarElementoUnico(comicRaw);

			mensaje = "Hay un total de: " + ListaComicsDAO.comicsGuardadosList.size()
					+ ". Comics guardados a la espera de ser impresos \n \n \n";
			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

		} else {
			mensaje = "Debes de clickar en el comic que quieras guardar \n \n \n";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
		}

	}

	/**
	 * Carga y ejecuta una tarea para exportar datos a un archivo Excel.
	 *
	 * @param fichero     El archivo Excel de destino.
	 * @param listaComics La lista de cómics a exportar.
	 */
	private void cargaExportExcel(List<Comic> listaComics, String tipoBusqueda) {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		FuncionesExcel excelFuntions = new FuncionesExcel();
		AlarmaList alarmaList = new AlarmaList();
		String mensajeErrorExportar = "ERROR. No se ha podido exportar correctamente.";
		String mensajeCancelarExportar = "ERROR. Se ha cancelado la exportación.";
		String mensajeValido = "Has exportado el fichero excel correctamente";

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
				procesarResultadoImportacion(crearExcelTask.getValue());

			} else {
				// La tarea no se completó correctamente, muestra un mensaje de error.

				procesarResultadoImportacion(crearExcelTask.getValue());

			}
			AlarmaList.detenerAnimacionPront(prontInfo);
			AlarmaList.detenerAnimacionCarga(progresoCarga);

			// Detener el hilo de la tarea
			excelThread.interrupt();
		});

		crearExcelTask.setOnRunning(event -> cambiarEstadoMenuBar(true));

		crearExcelTask.setOnSucceeded(event -> {
			cambiarEstadoMenuBar(false);
			AlarmaList.mostrarMensajePront(mensajeValido, true, prontInfo);
		});

		// Configuración del comportamiento cuando la tarea falla
		crearExcelTask.setOnFailed(event -> {
			alarmaList.manejarFallo(mensajeErrorExportar, prontInfo);
			cambiarEstadoMenuBar(false);
		});

		// Configuración del comportamiento cuando la tarea es cancelada
		crearExcelTask.setOnCancelled(event -> {
			alarmaList.manejarFallo(mensajeCancelarExportar, prontInfo);
			cambiarEstadoMenuBar(false);
		});

		// Iniciar la tarea principal de creación de Excel en un hilo separado
		excelThread.start();
	}

	public void cambiarEstadoMenuBar(boolean estadoAccion) {

		menu_archivo_excel.setDisable(estadoAccion);
		menu_archivo_importar.setDisable(estadoAccion);
		menu_archivo_delete.setDisable(estadoAccion);
		menu_comic_aniadir.setDisable(estadoAccion);
		menu_comic_eliminar.setDisable(estadoAccion);
		menu_comic_modificar.setDisable(estadoAccion);
		menu_comic_puntuar.setDisable(estadoAccion);
		menu_comic_aleatoria.setDisable(estadoAccion);
		botonIntroducir.setDisable(estadoAccion);
		botonModificar.setDisable(estadoAccion);
		botonEliminar.setDisable(estadoAccion);
		botonAgregarPuntuacion.setDisable(estadoAccion);
		botonLimpiar.setDisable(estadoAccion);
		botonMostrarParametro.setDisable(estadoAccion);
		botonImprimir.setDisable(estadoAccion);
		botonGuardarResultado.setDisable(estadoAccion);
		botonbbdd.setDisable(estadoAccion);

		nombreComic.setDisable(estadoAccion);
		nombreDibujante.setDisable(estadoAccion);
		nombreEditorial.setDisable(estadoAccion);
		nombreFirma.setDisable(estadoAccion);
		nombreFormato.setDisable(estadoAccion);
		nombreGuionista.setDisable(estadoAccion);
		nombreProcedencia.setDisable(estadoAccion);
		nombreVariante.setDisable(estadoAccion);
		numeroCaja.setDisable(estadoAccion);
		numeroComic.setDisable(estadoAccion);
	}

	public void guardarDatosCSV() {

		if (!ConectManager.conexionActiva()) {
			return;
		}
		String frase = "Fichero CSV";
		String formato = "*.csv";
		FuncionesExcel funcionesExcel = new FuncionesExcel();

		File fichero = Utilidades.tratarFichero(frase, formato).showOpenDialog(null);

		String mensajeValido = "Has importado correctamente la lista de comics en la base de datos";

		if (fichero != null) {
			Task<Boolean> lecturaTask = funcionesExcel.procesarArchivoCSVTask(fichero);

			lecturaTask.setOnRunning(e -> AlarmaList.iniciarAnimacionCarga(progresoCarga));

			lecturaTask.setOnSucceeded(e -> {
				cargarDatosDataBase();
				procesarResultadoImportacion(lecturaTask.getValue());
				AlarmaList.detenerAnimacion();
				AlarmaList.detenerAnimacionCarga(progresoCarga);
			});

			lecturaTask.setOnRunning(e -> cambiarEstadoMenuBar(true));

			lecturaTask.setOnFailed(e -> {
				procesarResultadoImportacion(lecturaTask.getValue());
				cambiarEstadoMenuBar(false);
			});

			lecturaTask.setOnSucceeded(e -> {
				cambiarEstadoMenuBar(false);
				AlarmaList.mostrarMensajePront(mensajeValido, true, prontInfo);

				cargarDatosDataBase();
			});

			// Iniciar la tarea principal de importación en un hilo separado
			new Thread(lecturaTask).start();
		}
	}

	private void procesarResultadoImportacion(Boolean resultado) {
		String mensaje = "";
		prontInfo.clear();
		if (resultado) {
			mensaje = "Operacion realizada con exito";
		} else {
			mensaje = "ERROR. No se ha podido completar la operacion";
		}

		AlarmaList.detenerAnimacion();
		AlarmaList.mostrarMensajePront(mensaje, resultado, prontInfo);
	}



	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public Comic camposComic() {
		Comic comic = new Comic();
		LocalDate fecha = fechaPublicacion.getValue();
		String fechaComic = (fecha != null) ? fecha.toString() : "";

		comic.setNombre(Utilidades.defaultIfNullOrEmpty(nombreComic.getValue(), ""));
		comic.setNumero(Utilidades.defaultIfNullOrEmpty(
				Utilidades.comaYGuionPorEspaciado(FuncionesComboBox.numeroCombobox(numeroComic)), ""));
		comic.setVariante(
				Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(nombreVariante.getValue()), ""));
		comic.setFirma(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(nombreFirma.getValue()), ""));
		comic.setEditorial(
				Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(nombreEditorial.getValue()), ""));
		comic.setFormato(Utilidades.defaultIfNullOrEmpty(FuncionesComboBox.formatoCombobox(nombreFormato), ""));
		comic.setProcedencia(
				Utilidades.defaultIfNullOrEmpty(FuncionesComboBox.procedenciaCombobox(nombreProcedencia), ""));
		comic.setFecha(fechaComic);
		comic.setGuionista(
				Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(nombreGuionista.getValue()), ""));
		comic.setDibujante(
				Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(nombreDibujante.getValue()), ""));
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

		// Limpiar elementos adicionales de la interfaz
		fechaPublicacion.setValue(null);
		prontInfo.clear();
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);
		tablaBBDD.refresh();
	}

	private void limpiarComboBox() {

		// Iterar sobre todos los ComboBox para realizar la limpieza
		for (ComboBox<String> comboBox : comboboxes) {
			// Limpiar el campo
			comboBox.setValue("");
			comboBox.getEditor().setText("");
		}

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

	/**
	 * Maneja la acción del usuario en relación a los cómics, como agregar,
	 * modificar, eliminar o puntuar un cómic.
	 *
	 * @param event El evento de acción que desencadenó la llamada a esta función.
	 */
	@FXML
	void modificarApiMarvel(ActionEvent event) {
		tablaBBDD.getItems().clear();
		ModificarApiDatosController.tipoAccion("Marvel");
		nav.verModificarApis(true);
	}

	/**
	 * Maneja la acción del usuario en relación a los cómics, como agregar,
	 * modificar, eliminar o puntuar un cómic.
	 *
	 * @param event El evento de acción que desencadenó la llamada a esta función.
	 */
	@FXML
	void modificarApiVine(ActionEvent event) {
		tablaBBDD.getItems().clear();

		ModificarApiDatosController.tipoAccion("Vine");

		nav.verModificarApis(false);
	}

	@FXML
	void verEstadoConexion(ActionEvent event) {
		nav.verEstadoConexion();

	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	public Scene miStageVentana() {
		Node rootNode = menu_navegacion;
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
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	void volverMenu(ActionEvent event) throws IOException {
		ConectManager.close();
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
