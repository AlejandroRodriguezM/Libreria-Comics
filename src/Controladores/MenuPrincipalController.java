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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Funcionamiento.Comic;
import Funcionamiento.FuncionesComboBox;
import Funcionamiento.FuncionesExcel;
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
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Esta clase sirve viajar a las diferentes ventanas del programa, asi como
 * realizar diferentes diferentes funciones
 *
 * @author Alejandro Rodriguez
 */
public class MenuPrincipalController implements Initializable {

	@FXML
	private TableColumn<Comic, String> ID;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonbbdd;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TableColumn<Comic, String> caja;

	@FXML
	private TableColumn<Comic, String> dibujante;

	@FXML
	private TableColumn<Comic, String> editorial;

	@FXML
	private TableColumn<Comic, String> fecha;

	@FXML
	private DatePicker fechaPublicacion;

	@FXML
	private TableColumn<Comic, String> firma;

	@FXML
	private TableColumn<Comic, String> formato;

	@FXML
	private TableColumn<Comic, String> guionista;

	@FXML
	private ImageView imagencomic;

	@FXML
	private MenuItem menu_archivo_backupbbdd;

	@FXML
	private MenuItem menu_archivo_cerrar;

	@FXML
	private MenuItem menu_archivo_delete;

	@FXML
	private MenuItem menu_archivo_desconectar;

	@FXML
	private MenuItem menu_archivo_excel;

	@FXML
	private MenuItem menu_archivo_importar;

	@FXML
	private MenuItem menu_archivo_sobreMi;

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
	private MenuItem menu_estadistica_comprados;

	@FXML
	private MenuItem menu_estadistica_estadistica;

	@FXML
	private MenuItem menu_estadistica_firmados;

	@FXML
	private MenuItem menu_estadistica_key_issue;

	@FXML
	private MenuItem menu_estadistica_posesion;

	@FXML
	private MenuItem menu_estadistica_puntuados;

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
	private TableColumn<Comic, String> nombre;

	@FXML
	private ComboBox<String> nombreComic;

	@FXML
	private ComboBox<String> nombreDibujante;

	@FXML
	private ComboBox<String> nombreEditorial;

	@FXML
	private ComboBox<String> nombreFirma;

	@FXML
	private ComboBox<String> nombreFormato;

	@FXML
	private ComboBox<String> nombreGuionista;

	@FXML
	private ComboBox<String> nombreProcedencia;

	@FXML
	private ComboBox<String> nombreVariante;

	@FXML
	private TableColumn<Comic, String> numero;

	@FXML
	private ComboBox<String> numeroCaja;

	@FXML
	private ComboBox<String> numeroComic;

	@FXML
	private TableColumn<Comic, String> procedencia;

	@FXML
	private ProgressIndicator progresoCarga;

	@FXML
	private TextArea prontInfo;

	@FXML
	private TableColumn<Comic, String> referencia;

	@FXML
	private VBox rootVBox;

	@FXML
	public TableView<Comic> tablaBBDD;

	@FXML
	private TableColumn<Comic, String> variante;

	@FXML
	private VBox vboxContenido;

	private Timeline timeline;

	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static Utilidades utilidad = null;
	private static FuncionesExcel excelFuntions = null;
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
			funcionesTabla.ajustarAnchoVBox(prontInfo, vboxContenido);
		});

		// Asegurarnos de que el VBox ajuste su tamaño correctamente al inicio
		Platform.runLater(() -> funcionesTabla.ajustarAnchoVBox(prontInfo, vboxContenido));

		Platform.runLater(() -> funcionesTabla.seleccionarRaw(tablaBBDD));

		Platform.runLater(() -> asignarTooltips());

		List<TableColumn<Comic, String>> columnListCarga = Arrays.asList(nombre, caja, numero, variante, firma,
				editorial, formato, procedencia, fecha, guionista, dibujante, referencia);
		columnList = columnListCarga;

		funcionesTabla.modificarColumnas(tablaBBDD, columnList);

		restringir_entrada_datos();

		List<ComboBox<String>> comboboxes = Arrays.asList(nombreComic, numeroComic, nombreVariante, nombreProcedencia,
				nombreFormato, nombreDibujante, nombreGuionista, nombreEditorial, nombreFirma, numeroCaja);

		int totalComboboxes = comboboxes.size();

		// Crear un ScheduledExecutorService para ejecutar la tarea después de un 1 segundo
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.schedule(() -> {

			Platform.runLater(() -> {
				
				try {
					libreria.listasAutoCompletado();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
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
			});
		}, 0, TimeUnit.SECONDS);

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

	/**
	 * Asigna tooltips a varios elementos en la interfaz gráfica. Estos tooltips
	 * proporcionan información adicional cuando el usuario pasa el ratón sobre los
	 * elementos.
	 */
	public void asignarTooltips() {
		List<Object> elementos = new ArrayList<>();

		// Agregar elementos a la lista para los cuales se asignarán tooltips
		elementos.add(botonbbdd);
		elementos.add(botonLimpiar);
		elementos.add(botonMostrarParametro);
		elementos.add(nombreComic);
		elementos.add(numeroComic);
		elementos.add(nombreFirma);
		elementos.add(nombreGuionista);
		elementos.add(nombreVariante);
		elementos.add(numeroCaja);
		elementos.add(nombreProcedencia);
		elementos.add(nombreFormato);
		elementos.add(nombreEditorial);
		elementos.add(nombreDibujante);

		// Llamar a la función para asignar tooltips a los elementos de la lista
		FuncionesTooltips.asignarTooltips(elementos);
	}

	/**
	 * Funcion que permite restringir entrada de datos de todo aquello que no sea un
	 * numero entero en los comboBox numeroComic y caja_comic
	 */
	public void restringir_entrada_datos() {
		numeroComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroCaja.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
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
		funcionesTabla.modificarColumnas(tablaBBDD, columnList);
		tablaBBDD.refresh();
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);

		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
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
		limpiezaDeDatos();
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
		limpiezaDeDatos();
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
		limpiezaDeDatos();
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
		limpiezaDeDatos();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaPosesion(), tablaBBDD, columnList); // Llamada a funcion
	}

	/**
	 * Maneja la acción de mostrar los cómics considerados "Key Issue".
	 *
	 * @param event El evento que desencadenó esta acción.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	@FXML
	void comicsKeyIssue(ActionEvent event) throws SQLException {
		prontInfo.setOpacity(0); // Ocultar la información en pantalla
		libreria = new DBLibreriaManager(); // Crear una instancia del gestor de la base de datos
		libreria.reiniciarBBDD(); // Reiniciar la base de datos si es necesario
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a la función para establecer nombres de
																// columnas
		funcionesTabla.tablaBBDD(libreria.libreriaKeyIssue(), tablaBBDD, columnList); // Llamada a la función para
																						// llenar la tabla con cómics
																						// "Key Issue"
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
			// Iniciar la animación
			iniciarAnimacion();
		});

		// Configurar el comportamiento cuando la tarea se completa con éxito
		task.setOnSucceeded(e -> {
			// Obtener el resultado de la tarea
			Boolean resultado = task.getValue();
			/* Tu condición aquí basada en el resultado */

			if (resultado) {
				prontInfo.clear();
				prontInfo.setStyle(null);
				Thread animationThread = new Thread(this::iniciarAnimacionBajada);
				animationThread.start();

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
							prontInfo.clear();
							detenerAnimacionPront();
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
							prontInfo.clear();
							detenerAnimacionPront();
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
						prontInfo.clear();
						detenerAnimacionPront();
						exception.printStackTrace();
						Platform.runLater(() -> nav.alertaException(
								"Error al borrar el contenido de la base de datos: " + exception.getMessage()));
					} else {
						prontInfo.clear();
						detenerAnimacionPront();
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
					prontInfo.clear();
					detenerAnimacionPront();
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
				prontInfo.clear();
				detenerAnimacionPront();
				exception.printStackTrace();
				Platform.runLater(

						() -> nav.alertaException("Error al importar el fichero CSV: " + exception.getMessage()));
			} else {
				prontInfo.clear();
				detenerAnimacionPront();
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
		iniciarAnimacionEstadistica();
		libreria.generar_fichero_estadisticas();
		detenerAnimacionPront();
		prontInfo.setText("Fichero creado correctamente");
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	private void iniciarAnimacionEstadistica() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarDescarga1 = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInfo.textProperty(), "Generando fichero de estadisticas ."));
		KeyFrame mostrarDescarga2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontInfo.textProperty(), "Generando fichero de estadisticas .."));
		KeyFrame mostrarDescarga3 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInfo.textProperty(), "Generando fichero de estadisticas ..."));
		KeyFrame mostrarDescarga4 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontInfo.textProperty(), "Generando fichero de estadisticas ...."));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarDescarga1, mostrarDescarga2, mostrarDescarga3, mostrarDescarga4);

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
			prontInfo.setText("Fichero creado correctamente");
		}
	}

	/**
	 * Metodo que permite crear una animacion
	 */
	private void iniciarAnimacionSubida() {
		prontInfo.setOpacity(1);
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarSubida1 = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInfo.textProperty(), "Subido datos a la " + DBManager.DB_NAME + " ."));
		KeyFrame mostrarSubida2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontInfo.textProperty(), "Subido datos a la " + DBManager.DB_NAME + " .."));
		KeyFrame mostrarSubida3 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInfo.textProperty(), "Subido datos a la " + DBManager.DB_NAME + " ..."));
		KeyFrame mostrarSubida4 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontInfo.textProperty(), "Subido datos a la " + DBManager.DB_NAME + " ...."));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarSubida1, mostrarSubida2, mostrarSubida3, mostrarSubida4);

		// Iniciar la animación
		timeline.play();
	}

	private void iniciarAnimacionBajada() {
		prontInfo.setOpacity(1);
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarBajada1 = new KeyFrame(Duration.ZERO,
				new KeyValue(prontInfo.textProperty(), "Eliminando base de datos ."));
		KeyFrame mostrarBajada2 = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontInfo.textProperty(), "Eliminando base de datos .."));
		KeyFrame mostrarBajada3 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontInfo.textProperty(), "Eliminando base de datos ..."));
		KeyFrame mostrarBajada4 = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontInfo.textProperty(), "Eliminando base de datos ...."));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarBajada1, mostrarBajada2, mostrarBajada3, mostrarBajada4);

		// Iniciar la animación
		timeline.play();
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
		libreria = new DBLibreriaManager();
		libreria.libreriaCompleta();
		utilidad = new Utilidades();
		String ID;

		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (idRow != null) {
			ID = idRow.getID();
			prontInfo.setOpacity(1);
			prontInfo.setText(libreria.comicDatos(ID).toString().replace("[", "").replace("]", ""));
			funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
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
					prontInfo.setStyle(null);
					prontInfo.clear();

					prontInfo.setOpacity(1); // Ocultar el mensaje inicial antes de iniciar la lectura y guardado
					Thread animationThread = new Thread(this::iniciarAnimacionSubida);
					animationThread.start();
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
							prontInfo.clear();
							detenerAnimacionPront();
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
							}
							prontInfo.clear();
							detenerAnimacionPront();
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
						prontInfo.clear();
						detenerAnimacionPront();
						prontInfo.setOpacity(1);
						prontInfo.setStyle("-fx-background-color: #F53636");
						prontInfo.setText("ERROR. No se ha podido importar correctamente.");
						detenerAnimacion();
					}
				});
			} else {
				// Si la importación del CSV falló, mostrar un mensaje de error
				Platform.runLater(() -> {
					prontInfo.clear();
					detenerAnimacionPront();
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
				prontInfo.clear();
				detenerAnimacionPront();
				Platform.runLater(
						() -> nav.alertaException("Error al importar el fichero CSV: " + exception.getMessage()));
				detenerAnimacion();
			} else {
				prontInfo.clear();
				detenerAnimacionPront();
				Platform.runLater(() -> nav.alertaException("Error desconocido al importar el fichero CSV."));
				detenerAnimacion();
			}
		});

		// Iniciar la tarea principal de importación en un hilo separado
		Thread thread = new Thread(task);
		thread.start();
	}

	/**
	 * Inicia la animación del progreso de carga.
	 */
	public void iniciarAnimacion() {
		progresoCarga.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
	}

	/**
	 * Detiene la animación del progreso de carga.
	 */
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
		utilidad = new Utilidades();
		Comic comic = new Comic();
		String datos[] = camposComic();
		String fecha = datos[8];

		if (datos[8].isEmpty()) {
			fecha = "";
		} else {
			fecha = datos[8];
		}

		comic = new Comic("", datos[1], datos[11], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7], fecha,
				datos[9], datos[10], "", "", "", null, "", "");

		funcionesTabla.tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()), tablaBBDD, columnList);
		prontInfo.setOpacity(1);
		prontInfo.setText(funcionesTabla.resultadoBusquedaPront(comic).getText());
		busquedaGeneral.setText("");
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

	/**
	 * Realiza la limpieza de datos en la interfaz gráfica.
	 */
	private void limpiezaDeDatos() {
		// Limpiar todos los campos de ComboBox y sus valores
		for (ComboBox<String> comboBox : Arrays.asList(nombreComic, numeroComic, nombreFirma, nombreGuionista,
				nombreVariante, numeroCaja, nombreProcedencia, nombreFormato, nombreEditorial, nombreDibujante)) {
			comboBox.setValue("");
			comboBox.getEditor().setText("");
		}

		// Limpiar elementos adicionales de la interfaz
		fechaPublicacion.setValue(null);
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);
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
