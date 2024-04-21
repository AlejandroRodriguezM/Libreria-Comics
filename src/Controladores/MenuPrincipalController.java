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

import Controladores.managment.AccionEliminar;
import Controladores.managment.AccionFuncionesComunes;
import Controladores.managment.AccionModificar;
import Controladores.managment.AccionReferencias;
import Controladores.managment.AccionSeleccionar;
import alarmas.AlarmaList;
import comicManagement.Comic;
import controlUI.AccionControlUI;
import controlUI.FuncionesComboBox;
import controlUI.FuncionesManejoFront;
import controlUI.FuncionesTableView;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.DBUtilidades.TipoBusqueda;
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import ficherosFunciones.FuncionesExcel;
import funciones_auxiliares.Utilidades;
import funciones_auxiliares.Ventanas;
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
	private MenuItem menu_archivo_cerrar, menu_archivo_delete, menu_archivo_desconectar, menu_archivo_excel,
			menu_archivo_importar, menu_archivo_sobreMi;

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

	@FXML
	private VBox comboboxVbox;

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

	@FXML
	private Rectangle barraCambioAltura;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de FuncionesComboBox para funciones relacionadas con ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	public static AccionReferencias referenciaVentana = new AccionReferencias();

	public static FuncionesManejoFront funcionesFront = new FuncionesManejoFront();

	public static CompletableFuture<List<Entry<String, String>>> urlPreviews;

	double y = 0;

	public AccionReferencias guardarReferencia() {
		referenciaVentana.setAlarmaConexionSql(alarmaConexionSql);
		referenciaVentana.setID(ID);
		referenciaVentana.setCaja(caja);
		referenciaVentana.setDibujante(dibujante);
		referenciaVentana.setEditorial(editorial);
		referenciaVentana.setFecha(fecha);
		referenciaVentana.setFirma(firma);
		referenciaVentana.setFormato(formato);
		referenciaVentana.setGuionista(guionista);
		referenciaVentana.setNombre(nombre);
		referenciaVentana.setNumero(numero);
		referenciaVentana.setProcedencia(procedencia);
		referenciaVentana.setReferencia(referencia);
		referenciaVentana.setVariante(variante);
		referenciaVentana.setBotonAgregarPuntuacion(botonAgregarPuntuacion);
		referenciaVentana.setBotonEliminar(botonEliminar);
		referenciaVentana.setBotonLimpiar(botonLimpiar);
		referenciaVentana.setBotonbbdd(botonbbdd);
		referenciaVentana.setBotonMostrarParametro(botonMostrarParametro);
		referenciaVentana.setBotonImprimir(botonImprimir);
		referenciaVentana.setBotonGuardarResultado(botonGuardarResultado);
		referenciaVentana.setBotonMostrarGuardados(botonMostrarGuardados);
		referenciaVentana.setBusquedaCodigo(busquedaCodigo);
		referenciaVentana.setBusquedaGeneral(busquedaGeneral);
		referenciaVentana.setFechaPublicacion(fechaPublicacion);
		referenciaVentana.setImagencomic(imagencomic);
		referenciaVentana.setMenu_archivo_cerrar(menu_archivo_cerrar);
		referenciaVentana.setMenu_archivo_delete(menu_archivo_delete);
		referenciaVentana.setMenu_archivo_desconectar(menu_archivo_desconectar);
		referenciaVentana.setMenu_archivo_excel(menu_archivo_excel);
		referenciaVentana.setMenu_archivo_importar(menu_archivo_importar);
		referenciaVentana.setMenu_archivo_sobreMi(menu_archivo_sobreMi);
		referenciaVentana.setMenu_comic_aleatoria(menu_comic_aleatoria);
		referenciaVentana.setMenu_comic_aniadir(menu_comic_aniadir);
		referenciaVentana.setMenu_comic_eliminar(menu_comic_eliminar);
		referenciaVentana.setMenu_comic_modificar(menu_comic_modificar);
		referenciaVentana.setMenu_comic_puntuar(menu_comic_puntuar);
		referenciaVentana.setMenu_estadistica_comprados(menu_estadistica_comprados);
		referenciaVentana.setMenu_estadistica_estadistica(menu_estadistica_estadistica);
		referenciaVentana.setMenu_estadistica_firmados(menu_estadistica_firmados);
		referenciaVentana.setMenu_estadistica_key_issue(menu_estadistica_key_issue);
		referenciaVentana.setMenu_estadistica_posesion(menu_estadistica_posesion);
		referenciaVentana.setMenu_estadistica_puntuados(menu_estadistica_puntuados);
		referenciaVentana.setMenu_estadistica_vendidos(menu_estadistica_vendidos);
		referenciaVentana.setMenu_archivo_avanzado(menu_archivo_avanzado);
		referenciaVentana.setMenu_archivo_conexion(menu_archivo_conexion);
		referenciaVentana.setMenu_navegacion(menu_navegacion);
		referenciaVentana.setNavegacion_cerrar(navegacion_cerrar);
		referenciaVentana.setNavegacion_comic(navegacion_comic);
		referenciaVentana.setNavegacion_estadistica(navegacion_estadistica);
		referenciaVentana.setTituloComic(nombreComic);
		referenciaVentana.setNombreDibujante(nombreDibujante);
		referenciaVentana.setNombreEditorial(nombreEditorial);
		referenciaVentana.setNombreFirma(nombreFirma);
		referenciaVentana.setNombreFormato(nombreFormato);
		referenciaVentana.setNombreGuionista(nombreGuionista);
		referenciaVentana.setNombreProcedencia(nombreProcedencia);
		referenciaVentana.setNombreVariante(nombreVariante);
		referenciaVentana.setNumeroCaja(numeroCaja);
		referenciaVentana.setNumeroComic(numeroComic);
		referenciaVentana.setFechaComic(fechaPublicacion);
		referenciaVentana.setNumeroComic(numeroComic);
		referenciaVentana.setProntInfo(prontInfo);
		referenciaVentana.setProgresoCarga(progresoCarga);
		referenciaVentana.setID(ID);
		referenciaVentana.setCaja(caja);
		referenciaVentana.setDibujante(dibujante);
		referenciaVentana.setEditorial(editorial);
		referenciaVentana.setFecha(fecha);
		referenciaVentana.setFirma(firma);
		referenciaVentana.setFormato(formato);
		referenciaVentana.setGuionista(guionista);
		referenciaVentana.setNombre(nombre);
		referenciaVentana.setNumero(numero);
		referenciaVentana.setProcedencia(procedencia);
		referenciaVentana.setReferencia(referencia);
		referenciaVentana.setVariante(variante);
		referenciaVentana.setTablaBBDD(tablaBBDD);
		referenciaVentana.setRootVBox(rootVBox);
		referenciaVentana.setVboxContenido(vboxContenido);
		referenciaVentana.setBackgroundImage(backgroundImage);
		referenciaVentana.setRootAnchorPane(rootAnchorPane);
		referenciaVentana.setVboxImage(vboxImage);
		referenciaVentana.setAnchoPaneInfo(anchoPaneInfo);
		referenciaVentana.setBotonModificar(botonModificar);
		referenciaVentana.setBotonIntroducir(botonIntroducir);
		referenciaVentana.setBotonEliminar(botonEliminar);
		referenciaVentana.setBotonAgregarPuntuacion(botonAgregarPuntuacion);
		referenciaVentana.setAlarmaConexionSql(alarmaConexionSql);
		referenciaVentana.setStage(estadoStage());

		referenciaVentana.setComboBoxes(Arrays.asList(nombreComic, numeroComic, nombreVariante, nombreFirma,
				nombreEditorial, nombreFormato, nombreProcedencia, nombreGuionista, nombreDibujante, numeroCaja));

		AccionReferencias.setListaElementosFondo(FXCollections.observableArrayList(backgroundImage, menu_navegacion));

		AccionReferencias.setListaCamposTexto(FXCollections.observableArrayList(busquedaGeneral, fechaPublicacion));

		AccionReferencias.setListaBotones(FXCollections.observableArrayList(botonLimpiar, botonMostrarParametro,
				botonbbdd, botonImprimir, botonGuardarResultado));

		AccionReferencias.setColumnasTabla(Arrays.asList(nombre, caja, numero, variante, firma, editorial, formato,
				procedencia, fecha, guionista, dibujante, referencia));

		return referenciaVentana;
	}

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

			urlPreviews = WebScraperCatalogPreviews.urlPreviews();

			enviarReferencias();

			establecerDinamismoAnchor();

			cambiarTamanioTable();

			FuncionesTableView.ajustarAnchoVBox();
			FuncionesTableView.seleccionarRaw();

			FuncionesTableView.modificarColumnas();
			AccionControlUI.controlarEventosInterfazPrincipal(guardarReferencia());
		});

		AccionControlUI.establecerTooltips();

		formatearTextField();

		cargarDatosDataBase();

	}

	public void enviarReferencias() {

		AccionControlUI.referenciaVentana = guardarReferencia();

		AccionFuncionesComunes.referenciaVentana = guardarReferencia();

		FuncionesTableView.referenciaVentana = guardarReferencia();

		FuncionesManejoFront.referenciaVentana = guardarReferencia();

		AccionSeleccionar.referenciaVentana = guardarReferencia();

//		OpcionesAvanzadasController.referenciaVentana = guardarReferencia();

		AccionEliminar.referenciaVentana = guardarReferencia();

		AccionModificar.referenciaVentana = guardarReferencia();
	}

	@FXML
	void ampliarImagen(MouseEvent event) {

		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (idRow != null) {

			ImagenAmpliadaController.comicInfo = SelectManager.comicDatos(idRow.getID());

			nav.verVentanaImagen();
		}
	}

	@FXML
	public void cambiarTamanioTable() {
		// Vincular el ancho de barraCambioAltura con el ancho de rootVBox
		barraCambioAltura.widthProperty().bind(rootVBox.widthProperty());

		// Configurar eventos del ratón para redimensionar el rootVBox desde la parte
		// superior
		barraCambioAltura.setOnMousePressed(event -> {
			y = event.getScreenY();
		});

		barraCambioAltura.setOnMouseDragged(event -> {
			double deltaY = event.getScreenY() - y;
			double newHeight = rootVBox.getPrefHeight() - deltaY;
			double max_height = calcularMaxHeight(); // Calcula el máximo altura permitido
			double min_height = 250; // Límite mínimo de altura

			if (newHeight > min_height && newHeight <= max_height) {
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

		rootAnchorPane.heightProperty().addListener((observable, oldValue, newHeightValue) -> {
			rootVBox.setMaxHeight(calcularMaxHeight());
		});

		rootAnchorPane.widthProperty().addListener((observable, oldValue, newWidthValue) -> {
			double newWidth = newWidthValue.doubleValue();

			if (newWidth <= 1130) {

				botonIntroducir.setLayoutX(231);
				botonIntroducir.setLayoutY(199);

				botonEliminar.setLayoutX(231);
				botonEliminar.setLayoutY(240);

				botonModificar.setLayoutX(231);
				botonModificar.setLayoutY(280);

				botonAgregarPuntuacion.setLayoutX(231);
				botonAgregarPuntuacion.setLayoutY(321);

				botonGuardarResultado.setLayoutX(327);
				botonGuardarResultado.setLayoutY(32);

				botonImprimir.setLayoutX(327);
				botonImprimir.setLayoutY(74);

			} else if (newWidth >= 1131) {

				botonIntroducir.setLayoutX(340);
				botonIntroducir.setLayoutY(31);

				botonEliminar.setLayoutX(340);
				botonEliminar.setLayoutY(72);

				botonModificar.setLayoutX(439);
				botonModificar.setLayoutY(31);

				botonAgregarPuntuacion.setLayoutX(439);
				botonAgregarPuntuacion.setLayoutY(72);

				botonGuardarResultado.setLayoutX(231);
				botonGuardarResultado.setLayoutY(337);

				botonImprimir.setLayoutX(290);
				botonImprimir.setLayoutY(337);

			}
		});

	}

	// Método para calcular el máximo altura permitido
	private double calcularMaxHeight() {
		// Obtener el tamaño actual de la ventana
		Stage stage = (Stage) rootVBox.getScene().getWindow();
		double windowHeight = stage.getHeight();

		// Ajustar el máximo altura permitido según la posición del AnchorPane
		// numeroCaja
		double max_height = windowHeight - numeroCaja.getLayoutY() - 80;
		return max_height;
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
		imagencomic.setImage(null);

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		scheduler.schedule(() -> {

			Platform.runLater(() -> {

				ListaComicsDAO.listasAutoCompletado();

				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						funcionesCombo.rellenarComboBox(referenciaVentana.getComboboxes());
						funcionesCombo.lecturaComboBox(referenciaVentana.getComboboxes());
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

		FuncionesManejoFront.establecerFondoDinamico();

		FuncionesManejoFront.establecerAnchoColumnas(13);

		FuncionesManejoFront.establecerAnchoMaximoBotones(102.0);

		FuncionesManejoFront.establecerAnchoMaximoCamposTexto(162.0);

		FuncionesManejoFront.establecerAnchoMaximoComboBoxes(162.0);

		FuncionesManejoFront.establecerTamanioMaximoImagen(252.0, 337.0);

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
		enviarReferencias();

		// Convertir la lista de ComboBox<String> a una lista de Control
		List<Control> controls = new ArrayList<>();
		for (ComboBox<String> comboBox : referenciaVentana.getComboboxes()) {
			controls.add(comboBox);
		}

		Comic comic = AccionControlUI.camposComic(controls, false);

		if (!comic.estaVacio()) {

			mostrarComics(false);
		} else {
			String mensaje = "Debes de seleccionar algun valor de los combobox";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
		}
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
		enviarReferencias();
		mostrarComics(true);
	}

	private void mostrarComics(boolean esCompleto) {

		busquedaGeneral.setText("");
		imagencomic.setVisible(true);
		if (esCompleto) {
			AccionSeleccionar.verBasedeDatos(esCompleto, false, null);
		} else {

			// Convertir la lista de ComboBox<String> a una lista de Control
			List<Control> controls = new ArrayList<>();
			for (ComboBox<String> comboBox : referenciaVentana.getComboboxes()) {
				controls.add(comboBox);
			}

			Comic comic = AccionControlUI.camposComic(controls, false);

			AccionSeleccionar.verBasedeDatos(esCompleto, false, comic);
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
		ListaComicsDAO.reiniciarListaComics();
		FuncionesTableView.nombreColumnas(tablaBBDD);
		FuncionesTableView.actualizarBusquedaRaw(tablaBBDD);
		List<Comic> listaComics = new ArrayList<Comic>();
		if (esGuardado) {
			listaComics = ListaComicsDAO.comicsGuardadosList;
		} else {
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(tipoBusqueda);

			listaComics = SelectManager.verLibreria(sentenciaSQL, false);
		}

		FuncionesTableView.tablaBBDD(listaComics, tablaBBDD);
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

//		Utilidades.borrarArchivosNoEnLista(ListaComicsDAO.listaImagenes);

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

		boolean estaVacia = false;
		String mensaje = "";
		if (!ListaComicsDAO.listaNombre.isEmpty()) {
			limpiezaDeDatos();
			limpiarComboBox();
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

			List<Comic> listaComics = SelectManager.verLibreria(sentenciaSQL, false);

			cargaExportExcel(listaComics, DBUtilidades.TipoBusqueda.COMPLETA.toString());

			ListaComicsDAO.limpiarListaGuardados();

//			Utilidades.borrarArchivosNoEnLista(ListaComicsDAO.listaImagenes);
			estaVacia = true;
			mensaje = "Base de datos exportada correctamente";
		} else {
			mensaje = "La base de datos esta vacia. No hay nada que exportar";
		}
		AlarmaList.mostrarMensajePront(mensaje, estaVacia, prontInfo);

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
	 * Funcion que permite mostrar la imagen de portada cuando clickeas en una
	 * tabla.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void clickRaton(MouseEvent event) throws IOException, SQLException {
		enviarReferencias();
		AccionSeleccionar.seleccionarComics(true);
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
			enviarReferencias();
			AccionSeleccionar.seleccionarComics(true);
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

	@FXML
	void borrarContenidoTabla(ActionEvent event) {
		try {
			Thread borradoTablaThread = new Thread(() -> {
				try {
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

						crearExcelTask.setOnRunning(e -> {
							FuncionesManejoFront.cambiarEstadoMenuBar(true);
						});

						crearExcelTask.setOnSucceeded(e -> {

							boolean deleteCompleted;
							try {
								deleteCompleted = ComicManagerDAO.deleteTable().get();
								String mensaje = deleteCompleted ? "Base de datos borrada y reiniciada correctamente"
										: "ERROR. No se ha podido eliminar y reiniciar la base de datos";

								
								if (deleteCompleted) {
									AlarmaList.detenerAnimacionCarga(referenciaVentana.getProgresoCarga());
									Utilidades.eliminarArchivosEnCarpeta();
									ListaComicsDAO.limpiarListaGuardados();
								}
								FuncionesManejoFront.cambiarEstadoMenuBar(false);
								AlarmaList.mostrarMensajePront(mensaje, deleteCompleted,prontInfo);

							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ExecutionException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						});
						// Iniciar la tarea principal de creación de Excel en un hilo separado
						excelThread.start();
					} else {
						AlarmaList.detenerAnimacionCarga(referenciaVentana.getProgresoCarga());
						String mensaje = "ERROR. Has cancelado el borrado de la base de datos";
						AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
					}
				} catch (InterruptedException | ExecutionException e) {
					Utilidades.manejarExcepcion(e);
				}
			});

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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		// Configuración de la tarea para crear el archivo Excel
		Task<Boolean> crearExcelTask = excelFuntions.crearExcelTask(listaComics, tipoBusqueda, dateFormat);
		Thread excelThread = new Thread(crearExcelTask);

		crearExcelTask.setOnRunning(e -> {
			FuncionesManejoFront.cambiarEstadoMenuBar(true);
			AlarmaList.iniciarAnimacionCarga(progresoCarga);
		});

		crearExcelTask.setOnSucceeded(event -> {
			FuncionesManejoFront.cambiarEstadoMenuBar(false);
			AlarmaList.mostrarMensajePront(mensajeValido, true, prontInfo);
			AlarmaList.detenerAnimacionCarga(progresoCarga);

		});

		// Configuración del comportamiento cuando la tarea falla
		crearExcelTask.setOnFailed(event -> {

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

			alarmaList.manejarFallo(mensajeErrorExportar, prontInfo);
			FuncionesManejoFront.cambiarEstadoMenuBar(false);
			AlarmaList.detenerAnimacionCarga(progresoCarga);
		});

		// Configuración del comportamiento cuando la tarea es cancelada
		crearExcelTask.setOnCancelled(event -> {
			alarmaList.manejarFallo(mensajeCancelarExportar, prontInfo);
			FuncionesManejoFront.cambiarEstadoMenuBar(false);
			AlarmaList.detenerAnimacionCarga(progresoCarga);
		});

		// Iniciar la tarea principal de creación de Excel en un hilo separado
		excelThread.start();
	}

	public void guardarDatosCSV() {

		if (!ConectManager.conexionActiva()) {
			return;
		}
		String frase = "Fichero CSV";
		String formato = "*.csv";

		File fichero = Utilidades.tratarFichero(frase, formato).showOpenDialog(null);

		String mensajeValido = "Has importado correctamente la lista de comics en la base de datos";

		if (fichero != null) {
			Task<Boolean> lecturaTask = FuncionesExcel.procesarArchivoCSVTask(fichero);

			lecturaTask.setOnSucceeded(e -> {
				cargarDatosDataBase();
				AlarmaList.detenerAnimacion();
				AlarmaList.detenerAnimacionCarga(progresoCarga);

				FuncionesManejoFront.cambiarEstadoMenuBar(false);
				AlarmaList.mostrarMensajePront(mensajeValido, true, prontInfo);
			});

			lecturaTask.setOnRunning(e -> {
				FuncionesManejoFront.cambiarEstadoMenuBar(true);
				AlarmaList.iniciarAnimacionCarga(progresoCarga);
			});

			lecturaTask.setOnFailed(e -> {
				procesarResultadoImportacion(lecturaTask.getValue());
				FuncionesManejoFront.cambiarEstadoMenuBar(false);
				AlarmaList.detenerAnimacionCarga(progresoCarga);
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
		for (ComboBox<String> comboBox : referenciaVentana.getComboboxes()) {
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

		// Pasar la lista de ComboBoxes a VentanaAccionController
		referenciaVentana.setComboBoxes(referenciaVentana.getComboboxes());

		if (fuente instanceof Button) {
			Button botonPresionado = (Button) fuente;

			if (botonPresionado == botonIntroducir) {
				AccionFuncionesComunes.tipoAccion("aniadir");
			} else if (botonPresionado == botonModificar) {
				AccionFuncionesComunes.tipoAccion("modificar");
			} else if (botonPresionado == botonEliminar) {
				AccionFuncionesComunes.tipoAccion("eliminar");
			} else if (botonPresionado == botonAgregarPuntuacion) {
				AccionFuncionesComunes.tipoAccion("puntuar");
			}
		} else if (fuente instanceof MenuItem) {
			MenuItem menuItemPresionado = (MenuItem) fuente;

			if (menuItemPresionado == menu_comic_aniadir) {
				AccionFuncionesComunes.tipoAccion("aniadir");
			} else if (menuItemPresionado == menu_comic_modificar) {
				AccionFuncionesComunes.tipoAccion("modificar");
			} else if (menuItemPresionado == menu_comic_eliminar) {
				AccionFuncionesComunes.tipoAccion("eliminar");
			} else if (menuItemPresionado == menu_comic_puntuar) {
				AccionFuncionesComunes.tipoAccion("puntuar");
			}
		}

		imagencomic.setVisible(false);
		imagencomic.setImage(null);
		prontInfo.setOpacity(0);
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

		List<Stage> stageVentanas = FuncionesManejoFront.stageVentanas;

		// Assuming `stages` is a collection of stages you want to check against
		for (Stage stage : stageVentanas) {
			stage.close(); // Close the stage if it's not the current state
		}

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

	public Stage estadoStage() {

		return (Stage) menu_navegacion.getScene().getWindow();
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {

		Platform.exit();
	}
}
