/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.DBUtilidades.TipoBusqueda;
import dbmanager.ListasComicsDAO;
import dbmanager.SelectManager;
import ficherosFunciones.FuncionesExcel;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesInterfaz.FuncionesTableView;
import funcionesManagment.AccionEliminar;
import funcionesManagment.AccionFuncionesComunes;
import funcionesManagment.AccionModificar;
import funcionesManagment.AccionReferencias;
import funcionesManagment.AccionSeleccionar;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import webScrap.WebScraperCatalogPreviews;

/**
 * Esta clase sirve viajar a las diferentes ventanas del programa, asi como
 * realizar diferentes diferentes funciones
 *
 * @author Alejandro Rodriguez
 */
public class MenuPrincipalController implements Initializable {
	@FXML
	private Label alarmaConexionInternet;

	@FXML
	private AnchorPane anchoPaneInfo;

	@FXML
	private ImageView backgroundImage;

	@FXML
	private Rectangle barraCambioAltura;

	@FXML
	private Button botonCancelarSubida;

	@FXML
	private Button botonIntroducirComic;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonModificarComic;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonbbdd;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TableColumn<Comic, String> columnaPrecio;

	@FXML
	private TableColumn<Comic, String> columnaDibujante;

	@FXML
	private TableColumn<Comic, String> columnaEditor;

	@FXML
	private TableColumn<Comic, String> columnaGuionista;

	@FXML
	private TableColumn<Comic, String> columnaId;

	@FXML
	private TableColumn<Comic, String> columnaNumero;

	@FXML
	private TableColumn<Comic, String> columnaReferencia;

	@FXML
	private TableColumn<Comic, String> columnaTitulo;

	@FXML
	private TableColumn<Comic, String> columnaVariante;

	@FXML
	private TableColumn<Comic, String> columnaFirma;

	@FXML
	private ComboBox<String> comboboxDibujanteComic;

	@FXML
	private ComboBox<String> comboboxEditorComic;

	@FXML
	private ComboBox<String> comboboxGuionistaComic;

	@FXML
	private ComboBox<String> comboboxNumeroComic;

	@FXML
	private ComboBox<String> comboboxTituloComic;

	@FXML
	private ComboBox<String> comboboxVarianteComic;

	@FXML
	private ComboBox<String> comboboxFirmaComic;

	@FXML
	private VBox comboboxVbox;

	@FXML
	private ImageView imagenComic;

	@FXML
	private MenuItem menuArchivoAvanzado;

	@FXML
	private MenuItem menuArchivoCerrar;

	@FXML
	private MenuItem menuArchivoDelete;

	@FXML
	private MenuItem menuArchivoDesconectar;

	@FXML
	private MenuItem menuArchivoExcel;

	@FXML
	private MenuItem menuArchivoImportar;

	@FXML
	private MenuItem menuArchivoSobreMi;

	@FXML
	private MenuItem menuComicAniadir;

	@FXML
	private MenuItem menuComicModificar;

	@FXML
	private MenuItem menuComicAleatoria;

	@FXML
	private MenuItem menuEstadisticaEstadistica;

	@FXML
	private MenuBar menuNavegacion;

	@FXML
	private MenuItem menuPrecioTotal;

	@FXML
	private Menu navegacionComic;

	@FXML
	private Menu navegacionCerrar;

	@FXML
	private Menu navegacionEstadistica;

	@FXML
	private ProgressIndicator progresoCarga;

	@FXML
	private TextArea prontInfo;

	@FXML
	private AnchorPane rootAnchorPane;

	@FXML
	private VBox rootVBox;

	@FXML
	private TableView<Comic> tablaBBDD;

	@FXML
	private VBox vboxContenido;

	@FXML
	private VBox vboxImage;

	public Comic comicCache;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de FuncionesComboBox para funciones relacionadas con ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	public static final AccionReferencias referenciaVentana = new AccionReferencias();

	public static CompletableFuture<List<Entry<String, String>>> urlPreviews;

	public static final AlarmaList alarmaList = new AlarmaList();

	double y = 0;

	public AccionReferencias guardarReferencia() {
		// Labels
		referenciaVentana.setAlarmaConexionInternet(alarmaConexionInternet);

		referenciaVentana.setBotonLimpiar(botonLimpiar);
		referenciaVentana.setBotonbbdd(botonbbdd);
		referenciaVentana.setBotonMostrarParametro(botonMostrarParametro);
		referenciaVentana.setBotonModificar(botonModificarComic);
		referenciaVentana.setBotonIntroducir(botonIntroducirComic);
		referenciaVentana.setBotonCancelarSubida(botonCancelarSubida);

		referenciaVentana.setBusquedaGeneralTextField(busquedaGeneral);

		// ImageViews
		referenciaVentana.setImagenComic(imagenComic);
		referenciaVentana.setBackgroundImage(backgroundImage);

		// MenuItems
		referenciaVentana.setMenuArchivoCerrar(menuArchivoCerrar);
		referenciaVentana.setMenuArchivoDelete(menuArchivoDelete);
		referenciaVentana.setMenuArchivoDesconectar(menuArchivoDesconectar);
		referenciaVentana.setMenuArchivoExcel(menuArchivoExcel);
		referenciaVentana.setMenuArchivoImportar(menuArchivoImportar);
		referenciaVentana.setMenuArchivoSobreMi(menuArchivoSobreMi);
		referenciaVentana.setMenuComicAniadir(menuComicAniadir);
		referenciaVentana.setMenuComicModificar(menuComicModificar);
		referenciaVentana.setMenuEstadisticaEstadistica(menuEstadisticaEstadistica);
		referenciaVentana.setMenuArchivoAvanzado(menuArchivoAvanzado);
		referenciaVentana.setMenuEstadisticaSumaTotal(menuPrecioTotal);

		// Menus
		referenciaVentana.setMenuNavegacion(menuNavegacion);
		referenciaVentana.setNavegacionCerrar(navegacionCerrar);
		referenciaVentana.setNavegacionComic(navegacionComic);
		referenciaVentana.setNavegacionEstadistica(navegacionEstadistica);

		// TableColumns
		referenciaVentana.setiDColumna(columnaId);
		referenciaVentana.setTituloColumna(columnaTitulo);
		referenciaVentana.setNumeroColumna(columnaNumero);
		referenciaVentana.setEditorColumna(columnaEditor);
		referenciaVentana.setFirmaColumna(columnaFirma);
		referenciaVentana.setArtistaColumna(columnaDibujante);
		referenciaVentana.setGuionistaColumna(columnaGuionista);
		referenciaVentana.setVarianteColumna(columnaVariante);
		referenciaVentana.setPrecioColumna(columnaPrecio);
		referenciaVentana.setReferenciaColumna(columnaReferencia);

		// ComboBoxes
		referenciaVentana.setTituloComicCombobox(comboboxTituloComic);
		referenciaVentana.setNumeroComicCombobox(comboboxNumeroComic);
		referenciaVentana.setNombreEditorCombobox(comboboxEditorComic);
		referenciaVentana.setNombreFirmaCombobox(comboboxFirmaComic);

		referenciaVentana.setNombreGuionistaCombobox(comboboxGuionistaComic);
		referenciaVentana.setNombreArtistaCombobox(comboboxDibujanteComic);
		referenciaVentana.setNombreVarianteCombobox(comboboxVarianteComic);

		// Others
		referenciaVentana.setProntInfoTextArea(prontInfo);
		referenciaVentana.setProgresoCarga(progresoCarga);
		referenciaVentana.setTablaBBDD(tablaBBDD);
		referenciaVentana.setRootVBox(rootVBox);
		referenciaVentana.setVboxContenido(vboxContenido);
		referenciaVentana.setVboxImage(vboxImage);
		referenciaVentana.setAnchoPaneInfo(anchoPaneInfo);
		referenciaVentana.setRootAnchorPane(rootAnchorPane);
		referenciaVentana.setBarraCambioAltura(barraCambioAltura);
		referenciaVentana.setStageVentana(estadoStage());

		// ComboBox List
		AccionReferencias
				.setListaComboboxes(Arrays.asList(comboboxTituloComic, comboboxNumeroComic, comboboxEditorComic,
						comboboxFirmaComic, comboboxGuionistaComic, comboboxVarianteComic, comboboxDibujanteComic));

		// FXCollections Lists
		AccionReferencias.setListaElementosFondo(FXCollections.observableArrayList(backgroundImage, menuNavegacion));
		AccionReferencias.setListaBotones(
				FXCollections.observableArrayList(botonLimpiar, botonMostrarParametro, botonbbdd, botonCancelarSubida));

		AccionReferencias.setListaColumnasTabla(Arrays.asList(columnaTitulo, columnaNumero, columnaPrecio,
				columnaEditor, columnaFirma, columnaDibujante, columnaVariante, columnaGuionista, columnaReferencia));

		return referenciaVentana;
	}

	public void enviarReferencias() {
		AccionControlUI.setReferenciaVentana(guardarReferencia());
		AccionFuncionesComunes.setReferenciaVentana(guardarReferencia());
		FuncionesTableView.setReferenciaVentana(guardarReferencia());
		FuncionesManejoFront.setReferenciaVentana(guardarReferencia());
		AccionSeleccionar.setReferenciaVentana(guardarReferencia());
		AccionEliminar.setReferenciaVentana(guardarReferencia());
		AccionModificar.setReferenciaVentana(guardarReferencia());
		Utilidades.setReferenciaVentana(guardarReferencia());
		Utilidades.setReferenciaVentanaPrincipal(guardarReferencia());
		VentanaAccionController.setReferenciaVentana(guardarReferencia());
		OpcionesAvanzadasController.setReferenciaVentanaPrincipal(guardarReferencia());
		Ventanas.setReferenciaVentanaPrincipal(guardarReferencia());
		DBUtilidades.setReferenciaVentana(guardarReferencia());
	}

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		menuArchivoExcel.setGraphic(Utilidades.createIcon("/Icono/Archivo/exportar.png", 16, 16));
		menuArchivoImportar.setGraphic(Utilidades.createIcon("/Icono/Archivo/importar.png", 16, 16));
		menuArchivoDelete.setGraphic(Utilidades.createIcon("/Icono/Archivo/basura.png", 16, 16));
		menuArchivoSobreMi.setGraphic(Utilidades.createIcon("/Icono/Archivo/about.png", 16, 16));
		menuArchivoAvanzado.setGraphic(Utilidades.createIcon("/Icono/Archivo/configuraciones.png", 16, 16));
		menuArchivoDesconectar.setGraphic(Utilidades.createIcon("/Icono/Archivo/apagado.png", 16, 16));
		menuArchivoCerrar.setGraphic(Utilidades.createIcon("/Icono/Archivo/salir.png", 16, 16));

		menuComicAniadir.setGraphic(Utilidades.createIcon("/Icono/Ventanas/add.png", 16, 16));
		menuComicModificar.setGraphic(Utilidades.createIcon("/Icono/Ventanas/modify.png", 16, 16));

		menuPrecioTotal.setGraphic(Utilidades.createIcon("/Icono/Estadistica/posesion.png", 16, 16));
		menuEstadisticaEstadistica.setGraphic(Utilidades.createIcon("/Icono/Estadistica/descarga.png", 16, 16));
		menuComicAleatoria.setGraphic(Utilidades.createIcon("/Icono/Ventanas/aleatorio.png", 16, 16));

		Platform.runLater(() -> {
			estadoStage().setOnCloseRequest(event -> stop());

			alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
			alarmaList.iniciarThreadChecker();
			urlPreviews = WebScraperCatalogPreviews.urlPreviews();

			enviarReferencias();

			establecerDinamismoAnchor();

			cambiarTamanioTable();

			FuncionesTableView.ajustarAnchoVBox();
			FuncionesTableView.seleccionarRaw();

			FuncionesTableView.modificarColumnas(true);
			AccionControlUI.controlarEventosInterfazPrincipal(guardarReferencia());
			FuncionesManejoFront.getStageVentanas().add(estadoStage());
			cargarDatosDataBase();
			AccionSeleccionar.actualizarRefrenciaClick(guardarReferencia());
		});

		AccionControlUI.establecerTooltips();

		formatearTextField();

	}

	@FXML
	void ampliarImagen(MouseEvent event) {
		enviarReferencias();
		Comic comic = guardarReferencia().getTablaBBDD().getSelectionModel().getSelectedItem();
		ImagenAmpliadaController.setComicCache(comic);
		if (ImagenAmpliadaController.getComicCache() != null
				&& guardarReferencia().getImagenComic().getOpacity() != 0) {
			Ventanas.verVentanaImagen();
		}
	}

	@FXML
	public void cambiarTamanioTable() {

		if (!barraCambioAltura.isDisable()) {
			// Vincular el ancho de barraCambioAltura con el ancho de rootVBox
			barraCambioAltura.widthProperty().bind(rootVBox.widthProperty());

			// Configurar eventos del ratón para redimensionar el rootVBox desde la parte
			// superior
			barraCambioAltura.setOnMousePressed(event -> y = event.getScreenY());

			barraCambioAltura.setOnMouseDragged(event -> {
				double deltaY = event.getScreenY() - y;
				double newHeight = rootVBox.getPrefHeight() - deltaY;
				double maxHeight = calcularMaxHeight(); // Calcula el máximo altura permitido
				double minHeight = 200; // Límite mínimo de altura

				if (newHeight > minHeight && newHeight <= maxHeight) {
					rootVBox.setPrefHeight(newHeight);
					rootVBox.setLayoutY(tablaBBDD.getLayoutY() + deltaY);
					tablaBBDD.setPrefHeight(newHeight);
					tablaBBDD.setLayoutY(tablaBBDD.getLayoutY() + deltaY);

					y = event.getScreenY();
				}
			});

			// Cambiar el cursor cuando se pasa sobre la barra de redimensionamiento
			barraCambioAltura.setOnMouseMoved(event -> {
				if (event.getY() <= 5) {
					barraCambioAltura.setCursor(Cursor.N_RESIZE);
				} else {
					barraCambioAltura.setCursor(Cursor.DEFAULT);
				}
			});

			rootAnchorPane.heightProperty()
					.addListener((observable, oldValue, newHeightValue) -> rootVBox.setMaxHeight(calcularMaxHeight()));

			rootAnchorPane.widthProperty().addListener((observable, oldValue, newWidthValue) -> {
				double newWidth = newWidthValue.doubleValue();

				if (newWidth <= 1130) {

					botonIntroducirComic.setLayoutX(231);
					botonIntroducirComic.setLayoutY(159);

					botonModificarComic.setLayoutX(231);
					botonModificarComic.setLayoutY(197);

				} else if (newWidth >= 1131) {

					botonIntroducirComic.setLayoutX(329);
					botonIntroducirComic.setLayoutY(31);

					botonModificarComic.setLayoutX(329);
					botonModificarComic.setLayoutY(72);

				}
			});
		}

	}

	// Método para calcular el máximo altura permitido
	private double calcularMaxHeight() {
		// Obtener el tamaño actual de la ventana
		Stage stage = (Stage) rootVBox.getScene().getWindow();
		double windowHeight = stage.getHeight();

		// Ajustar el máximo altura permitido según la posición del AnchorPane
		// numeroCaja
		return windowHeight - vboxContenido.getLayoutY() - 400;
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
		tablaBBDD.refresh();
		prontInfo.setOpacity(0);
		imagenComic.setImage(null);

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		try {
			scheduler.schedule(() -> Platform.runLater(() -> {
				ListasComicsDAO.listasAutoCompletado();

				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						funcionesCombo.rellenarComboBox(AccionReferencias.getListaComboboxes());
						funcionesCombo.lecturaComboBox(AccionReferencias.getListaComboboxes());
						return null;
					}
				};

				// Iniciar el Task en un nuevo hilo
				Thread thread = new Thread(task);
				thread.setDaemon(true);
				thread.start();

				// Manejar la cancelación
				botonCancelarSubida.setOnAction(ev -> {
					botonCancelarSubida.setVisible(false);
					task.cancel();
				});

				// Cuando la tarea haya terminado, apaga el scheduler
				task.setOnSucceeded(event -> {
					botonCancelarSubida.setVisible(false);
					scheduler.shutdown();
				});

				// Cuando la tarea haya terminado, apaga el scheduler
				task.setOnRunning(event -> botonCancelarSubida.setVisible(true));
			}), 0, TimeUnit.SECONDS);
		} catch (Exception e) {
			Utilidades.manejarExcepcion(e);
		} finally {
			scheduler.shutdown();
		}
	}

	/**
	 * Establece el dinamismo en la interfaz gráfica ajustando propiedades de
	 * elementos como tamaños, anchos y máximos.
	 */
	public void establecerDinamismoAnchor() {

		FuncionesManejoFront.establecerFondoDinamico();

		FuncionesManejoFront.establecerAnchoColumnas(13);

		FuncionesManejoFront.establecerAnchoMaximoBotones(102.0);

		FuncionesManejoFront.establecerAnchoMaximoCamposTexto(162.0);

		FuncionesManejoFront.establecerAnchoMaximoComboBoxes(162.0);

		FuncionesManejoFront.establecerTamanioMaximoImagen(252.0, 325.0);
	}

	/**
	 * Funcion que permite restringir entrada de datos de todo aquello que no sea un
	 * numero entero en los comboBox numeroComic y caja_comic
	 */
	public void formatearTextField() {
		comboboxNumeroComic.getEditor().setTextFormatter(FuncionesComboBox.validadorNenteros());
	}

	/////////////////////////////////
	//// METODOS LLAMADA A VENTANAS//
	/////////////////////////////////

	/**
	 * Permite el cambio de ventana a la ventana de SobreMiController
	 *
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {
		enviarReferencias();
		nav.verSobreMi();
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) {
		enviarReferencias();
		mostrarComics(false);
		modificarEstadoTabla(349, 1);
	}

	/**
	 * Metodo que muestra toda la base de datos.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void verTodabbdd(ActionEvent event) {
		enviarReferencias();
		mostrarComics(true);
		modificarEstadoTabla(349, 1);
	}

	public void modificarEstadoTabla(double altura, double opacidad) {
		rootVBox.setPrefHeight(altura);
		tablaBBDD.setPrefHeight(altura);
		tablaBBDD.setOpacity(opacidad);
	}

	private void mostrarComics(boolean esCompleto) {

		if (esCompleto) {
			AccionSeleccionar.verBasedeDatos(esCompleto, false, null);
		} else {
			List<String> controls = new ArrayList<>();
			List<ComboBox<String>> listaComboboxes = AccionReferencias.getListaComboboxes();

			// Iterar sobre los ComboBox en orden
			for (ComboBox<String> comboBox : listaComboboxes) {
				controls.add(comboBox.getSelectionModel().getSelectedItem());
			}

			Comic comic = AccionControlUI.camposComic(controls, false);

			AccionSeleccionar.verBasedeDatos(esCompleto, false, comic);
		}
	}

	@FXML
	void verOpcionesAvanzadas(ActionEvent event) {
		enviarReferencias();
		Ventanas.verOpcionesAvanzadas();

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
	void importCSV(ActionEvent event) {
		enviarReferencias();
		limpiezaDeDatos();
		limpiarComboBox();

		guardarDatosCSV();

		ListasComicsDAO.listasAutoCompletado();

	}

	/**
	 * Exporta un fichero CSV compatible con el programa que copia el contenido de
	 * la base de datos en un fichero CSV
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void exportCSV(ActionEvent event) {
		enviarReferencias();
		String mensaje = "";
		if (!ListasComicsDAO.listaNombre.isEmpty()) {
			limpiezaDeDatos();
			limpiarComboBox();
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

			List<Comic> listaComics = SelectManager.verLibreria(sentenciaSQL, false);

			cargaExportExcel(listaComics, DBUtilidades.TipoBusqueda.COMPLETA.toString());
		} else {
			mensaje = "La base de datos esta vacia. No hay nada que exportar";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
		}

	}

	/**
	 * Limpia los campos de pantalla donde se escriben los datos.
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {

		enviarReferencias();
		limpiezaDeDatos();
		limpiarComboBox();
	}

	/**
	 * Se llama a funcion que permite ver las estadisticas de la bbdd
	 *
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void verEstadistica(ActionEvent event) {
		enviarReferencias();
		AlarmaList.iniciarAnimacionEstadistica(prontInfo);
//		ListasComicsDAO.generar_fichero_estadisticas();
		AlarmaList.detenerAnimacionPront(prontInfo);
		String mensaje = "Fichero creado correctamente";

		AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
	}

	/////////////////////////////////
	//// FUNCIONES////////////////////
	/////////////////////////////////

	/**
	 * Funcion que permite mostrar la imagen de portada cuando clickeas en una
	 * tabla.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void clickRaton(MouseEvent event) {
		enviarReferencias();
		if (!tablaBBDD.isDisabled()) {

			Comic comic = guardarReferencia().getTablaBBDD().getSelectionModel().getSelectedItem();
			ImagenAmpliadaController.setComicCache(comic);
			AccionSeleccionar.seleccionarComics(true);
		}
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
	void teclasDireccion(KeyEvent event) {
		enviarReferencias();
		if ((event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) && !tablaBBDD.isDisabled()) {

			Comic comic = guardarReferencia().getTablaBBDD().getSelectionModel().getSelectedItem();
			ImagenAmpliadaController.setComicCache(comic);
			AccionSeleccionar.seleccionarComics(true);
		}

	}

	/////////////////////////////////
	//// FUNCIONES CREACION FICHEROS//
	/////////////////////////////////

	@FXML
	void borrarContenidoTabla(ActionEvent event) {
		enviarReferencias();
		try {
			Thread borradoTablaThread = new Thread(() -> {
				try {
					FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
					boolean confirmacionBorrado = nav.borrarContenidoTabla().get();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
					if (confirmacionBorrado) {

						AlarmaList.iniciarAnimacionCarga(referenciaVentana.getProgresoCarga());
						String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

						List<Comic> listaComics = SelectManager.verLibreria(sentenciaSQL, false);
						FuncionesExcel excelFuntions = new FuncionesExcel();
						// Configuración de la tarea para crear el archivo Excel

						Task<Boolean> crearExcelTask = excelFuntions.crearExcelTask(listaComics,
								TipoBusqueda.ELIMINAR.toString(), dateFormat);
						Thread excelThread = new Thread(crearExcelTask);

						if (crearExcelTask == null) {
							botonCancelarSubida.setVisible(false);
							FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
							AlarmaList.detenerAnimacionPront(prontInfo);
							AlarmaList.detenerAnimacionCarga(progresoCarga);

							// Detener el hilo de la tarea
							excelThread.interrupt();
						} else {

							crearExcelTask.setOnRunning(e -> {

								estadoStage().setOnCloseRequest(closeEvent -> {
									crearExcelTask.cancel(true);
									excelThread.interrupt(); // Interrumpir el hilo
									Utilidades.cerrarCargaComics();
								});

								cerradoPorOperaciones();
								botonCancelarSubida.setVisible(true);
								FuncionesManejoFront.cambiarEstadoMenuBar(true, guardarReferencia());
								limpiezaDeDatos();
							});

							crearExcelTask.setOnSucceeded(e -> {

								botonCancelarSubida.setVisible(false);
								boolean deleteCompleted;
								try {
									deleteCompleted = ComicManagerDAO.deleteTable().get();
									String mensaje = deleteCompleted
											? "Base de datos borrada y reiniciada correctamente"
											: "ERROR. No se ha podido eliminar y reiniciar la base de datos";

									if (deleteCompleted) {
										AlarmaList.detenerAnimacionCarga(referenciaVentana.getProgresoCarga());
										Utilidades.eliminarContenidoCarpeta(FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH);
									}
									FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
									AlarmaList.mostrarMensajePront(mensaje, deleteCompleted, prontInfo);

								} catch (InterruptedException | ExecutionException e1) {
									crearExcelTask.cancel(true);
									excelThread.interrupt();
									Utilidades.manejarExcepcion(e1);
								}
							});

							crearExcelTask.setOnFailed(e -> {
								botonCancelarSubida.setVisible(false);
								FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
							});

							crearExcelTask.setOnCancelled(e -> {
								FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
								AlarmaList.detenerAnimacionCarga(referenciaVentana.getProgresoCarga());
								String mensaje = "Has cancelado el borrado de la base de datos";
								AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

							});

							// Manejar la cancelación
							botonCancelarSubida.setOnAction(ev -> {
								botonCancelarSubida.setVisible(false);
								AlarmaList.detenerAnimacionCarga(referenciaVentana.getProgresoCarga());

								crearExcelTask.cancel(true);
								excelThread.interrupt();
							});
						}
						// Iniciar la tarea principal de creación de Excel en un hilo separado
						excelThread.start();

					} else {
						AlarmaList.detenerAnimacionCarga(referenciaVentana.getProgresoCarga());
						String mensaje = "ERROR. Has cancelado el borrado de la base de datos";
						AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
						FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
					}
				} catch (InterruptedException | ExecutionException e) {
					Utilidades.manejarExcepcion(e);
				}
			});
			borradoTablaThread.setDaemon(true); // Marcar el hilo como demonio
			borradoTablaThread.start();

		} catch (Exception e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Carga y ejecuta una tarea para exportar datos a un archivo Excel.
	 *
	 * @param fichero     El archivo Excel de destino.
	 * @param listaComics La lista de cómics a exportar.
	 */
	private void cargaExportExcel(List<Comic> listaComics, String tipoBusqueda) {
		enviarReferencias();
		FuncionesExcel excelFuntions = new FuncionesExcel();
		String mensajeErrorExportar = "ERROR. No se ha podido exportar correctamente.";
		String mensajeCancelarExportar = "ERROR. Se ha cancelado la exportación.";
		String mensajeValido = "Has exportado el fichero excel correctamente";

		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagenComic.setImage(null);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		// Configuración de la tarea para crear el archivo Excel
		Task<Boolean> crearExcelTask = excelFuntions.crearExcelTask(listaComics, tipoBusqueda, dateFormat);
		Thread excelThread = new Thread(crearExcelTask);

		if (crearExcelTask == null) {
			botonCancelarSubida.setVisible(false);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
			AlarmaList.detenerAnimacionPront(prontInfo);
			AlarmaList.detenerAnimacionCarga(progresoCarga);

			// Detener el hilo de la tarea
			excelThread.interrupt();
			AlarmaList.mostrarMensajePront(mensajeCancelarExportar, false, prontInfo);
		} else {
			crearExcelTask.setOnRunning(e -> {

				estadoStage().setOnCloseRequest(event -> {
					crearExcelTask.cancel(true);
					Utilidades.cerrarCargaComics();
				});

				cerradoPorOperaciones();
				botonCancelarSubida.setVisible(true);
				FuncionesManejoFront.cambiarEstadoMenuBar(true, guardarReferencia());
				AlarmaList.iniciarAnimacionCarga(progresoCarga);
				limpiezaDeDatos();

			});

			crearExcelTask.setOnSucceeded(event -> {
				botonCancelarSubida.setVisible(false);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.mostrarMensajePront(mensajeValido, true, prontInfo);
				AlarmaList.detenerAnimacionCarga(progresoCarga);
			});

			// Configuración del comportamiento cuando la tarea falla
			crearExcelTask.setOnFailed(event -> {
				botonCancelarSubida.setVisible(false);
				procesarResultadoImportacion(false);
				AlarmaList.detenerAnimacionPront(prontInfo);
				AlarmaList.detenerAnimacionCarga(progresoCarga);

				// Detener el hilo de la tarea
				excelThread.interrupt();
				alarmaList.manejarFallo(mensajeErrorExportar, prontInfo);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.detenerAnimacionCarga(progresoCarga);
				AlarmaList.mostrarMensajePront(mensajeCancelarExportar, false, prontInfo);
			});

			// Configuración del comportamiento cuando la tarea es cancelada
			crearExcelTask.setOnCancelled(event -> {
				alarmaList.manejarFallo(mensajeCancelarExportar, prontInfo);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.detenerAnimacionCarga(progresoCarga);
				AlarmaList.mostrarMensajePront(mensajeCancelarExportar, false, prontInfo);
				// Detener el hilo de la tarea
				excelThread.interrupt();
			});
		}

		// Manejar la cancelación
		botonCancelarSubida.setOnAction(ev -> {
			botonCancelarSubida.setVisible(false);

			crearExcelTask.cancel(true);
			excelThread.interrupt();
		});
		excelThread.setDaemon(true); // Establecer como daemon
		// Iniciar la tarea principal de creación de Excel en un hilo separado
		excelThread.start();
	}

	public void guardarDatosCSV() {
		enviarReferencias();
		String frase = "Fichero CSV";
		String formatoFichero = "*.csv";

		File fichero = Utilidades.tratarFichero(frase, formatoFichero, false);

		// Verificar si se obtuvo un objeto FileChooser válido
		if (fichero != null) {

			String mensajeValido = "Has importado correctamente la lista de comics en la base de datos";

			Task<Boolean> lecturaTask = FuncionesExcel.procesarArchivoCSVTask(fichero);

			lecturaTask.setOnSucceeded(e -> {
				cargarDatosDataBase();
				AlarmaList.detenerAnimacion();
				AlarmaList.detenerAnimacionCarga(progresoCarga);
				botonCancelarSubida.setVisible(false);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.mostrarMensajePront(mensajeValido, true, prontInfo);
			});

			lecturaTask.setOnRunning(e -> {
				estadoStage().setOnCloseRequest(event -> {
					lecturaTask.cancel(true);
					Utilidades.cerrarCargaComics();
				});
				cerradoPorOperaciones();
				FuncionesManejoFront.cambiarEstadoMenuBar(true, guardarReferencia());
				botonCancelarSubida.setVisible(true);
				AlarmaList.iniciarAnimacionCarga(progresoCarga);
				limpiezaDeDatos();
			});

			lecturaTask.setOnFailed(e -> {
				botonCancelarSubida.setVisible(false);
				procesarResultadoImportacion(lecturaTask.getValue());
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.detenerAnimacionCarga(progresoCarga);
			});

			// Manejar la cancelación
			botonCancelarSubida.setOnAction(ev -> {
				lecturaTask.cancel(true); // true indica que la tarea debe ser interrumpida si ya está en ejecución
				botonCancelarSubida.setVisible(false);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.detenerAnimacionCarga(progresoCarga);

				procesarResultadoImportacion(false);
			});

			// Iniciar la tarea principal de importación en un hilo separado
			Thread hiloImportacion = new Thread(lecturaTask);
			hiloImportacion.setDaemon(true); // Marcar el hilo como demonio
			hiloImportacion.start();
		}
	}

	private void procesarResultadoImportacion(Boolean resultado) {
		String mensaje = "";
		prontInfo.clear();
		if (Boolean.TRUE.equals(resultado)) {
			mensaje = "Operacion realizada con exito";
		} else {
			mensaje = "ERROR. No se ha podido completar la operacion";
		}

		AlarmaList.detenerAnimacion();
		AlarmaList.mostrarMensajePront(mensaje, resultado, prontInfo);
	}

	/**
	 * Realiza la limpieza de datos en la interfaz gráfica.
	 */
	private void limpiezaDeDatos() {
		enviarReferencias();
		prontInfo.clear();
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		tablaBBDD.refresh();
		imagenComic.setImage(null);
		imagenComic.setOpacity(0);

		modificarEstadoTabla(349, 0.6);
	}

	private void limpiarComboBox() {
		// Iterar sobre todos los ComboBox para realizar la limpieza
		for (ComboBox<String> comboBox : AccionReferencias.getListaComboboxes()) {
			// Limpiar el campo
			comboBox.setValue("");
			comboBox.getEditor().setText("");
		}

	}

	/**
	 * Permite el cambio de ventana a la ventana de RecomendacionesController
	 *
	 * @param event
	 */
	@FXML
	void ventanaRecomendar(ActionEvent event) {
		Ventanas.verRecomendacion();
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

		// Pasar la lista de ComboBoxes a VentanaAccionController
		AccionReferencias.setListaComboboxes(AccionReferencias.getListaComboboxes());

		if (fuente instanceof Button botonPresionado) {
			if (botonPresionado == botonIntroducirComic) {
				AccionFuncionesComunes.setTipoAccion("aniadir");
			} else if (botonPresionado == botonModificarComic) {
				AccionFuncionesComunes.setTipoAccion("modificar");
			}
		} else if (fuente instanceof MenuItem menuItemPresionado) {
			if (menuItemPresionado == menuComicAniadir) {
				AccionFuncionesComunes.setTipoAccion("aniadir");
			} else if (menuItemPresionado == menuComicModificar) {
				AccionFuncionesComunes.setTipoAccion("modificar");
			}
		}
		modificarEstadoTabla(259, 0.6);
		imagenComic.setVisible(false);
		imagenComic.setImage(null);
		prontInfo.setOpacity(0);
		Ventanas.verAccionComic();
	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	public Scene miStageVentana() {
		Node rootNode = menuNavegacion;
		while (rootNode.getParent() != null) {
			rootNode = rootNode.getParent();
		}

		if (rootNode instanceof Parent parent) {
			Scene scene = parent.getScene();
			ConectManager.activeScenes.add(scene);
			return scene;
		} else {
			return null;
		}
	}

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	void volverMenu(ActionEvent event) {

		if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
			FuncionesManejoFront.getStageVentanas().remove(estadoStage());
		}

		List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();

		// Assuming `stages` is a collection of stages you want to check against
		for (Stage stage : stageVentanas) {
			stage.close(); // Close the stage if it's not the current state
		}

		ConectManager.close();
		nav.cerrarCargaComics();
		nav.verAccesoBBDD();
		estadoStage().close();

	}

	/**
	 * Maneja la acción de salida del programa.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {
		// Lógica para manejar la acción de "Salir"
		nav.cerrarCargaComics();
		if (nav.salirPrograma(event)) {
			estadoStage().close();
		}
	}

	public void cerradoPorOperaciones() {
		List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();

		for (Stage stage : stageVentanas) {

			if (!stage.getTitle().equalsIgnoreCase("Menu principal")) {
				stage.close();
			}
		}

		if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
			FuncionesManejoFront.getStageVentanas().remove(estadoStage());
		}
	}

	public Stage estadoStage() {

		return (Stage) botonLimpiar.getScene().getWindow();
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {
		nav.cerrarCargaComics();
		Platform.exit();
	}

	public void stop() {

		cerradoPorOperaciones();
		alarmaList.detenerThreadChecker();
		nav.cerrarMenuOpciones();
		nav.cerrarCargaComics();
		nav.cerrarVentanaAccion();
		Utilidades.cerrarOpcionesAvanzadas();

		Platform.exit();
	}
}
