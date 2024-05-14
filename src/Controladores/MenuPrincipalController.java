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
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import ficherosFunciones.FuncionesExcel;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesInterfaz.FuncionesTableView;
import funcionesManagment.AccionEliminar;
import funcionesManagment.AccionFuncionesComunes;
import funcionesManagment.AccionModificar;
import funcionesManagment.AccionReferencias;
import funcionesManagment.AccionSeleccionar;
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

	@FXML
	private Button botonCancelarSubida;

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

	/**
	 * Menú relacionado con operaciones de cómic.
	 */
	@FXML
	private MenuItem menuComicAleatoria;
	@FXML
	private MenuItem menuComicAniadir;
	@FXML
	private MenuItem menuComicEliminar;
	@FXML
	private MenuItem menuComicModificar;
	@FXML
	private MenuItem menuComicPuntuar;

	/**
	 * Menú relacionado con estadísticas de cómics.
	 */
	@FXML
	private MenuItem menuEstadisticaComprados;
	@FXML
	private MenuItem menuEstadisticaEstadistica;
	@FXML
	private MenuItem menuEstadisticaFirmados;
	@FXML
	private MenuItem menuEstadisticaKeyIssue;
	@FXML
	private MenuItem menuEstadisticaPosesion;
	@FXML
	private MenuItem menuEstadisticaPuntuados;
	@FXML
	private MenuItem menuEstadisticaVendidos;

	@FXML
	private MenuItem menuArchivoAvanzado;

	@FXML
	private MenuItem menuArchivoApiMarvel;

	@FXML
	private MenuItem menuArchivoApiVine;

	/**
	 * Barra de menús de navegación.
	 */
	@FXML
	private MenuBar menuNavegacion;

	/**
	 * Menú de navegación para cerrar.
	 */
	@FXML
	private Menu navegacionCerrar;

	/**
	 * Menú de navegación relacionado con cómics.
	 */
	@FXML
	private Menu navegacionComic;

	@FXML
	private Menu menuComprobarApis;

	/**
	 * Menú de navegación relacionado con estadísticas.
	 */
	@FXML
	private Menu navegacionEstadistica;

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
	private ComboBox<String> gradeoComic;

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
	 * Columna de la tabla para elid.
	 */
	@FXML
	private TableColumn<Comic, String> id;

	/**
	 * Columna de la tabla para la caja.
	 */
	@FXML
	private TableColumn<Comic, String> gradeo;

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
	private Label alarmaConexionInternet;

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

	public static final AccionReferencias referenciaVentana = new AccionReferencias();

	public static CompletableFuture<List<Entry<String, String>>> urlPreviews;

	public static final AlarmaList alarmaList = new AlarmaList();

	double y = 0;

	public AccionReferencias guardarReferencia() {
		referenciaVentana.setAlarmaConexionInternet(alarmaConexionInternet);
		referenciaVentana.setID(id);
		referenciaVentana.setGradeo(gradeo);
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
		referenciaVentana.setMenu_archivo_cerrar(menuArchivoCerrar);
		referenciaVentana.setMenu_archivo_delete(menuArchivoDelete);
		referenciaVentana.setMenu_archivo_desconectar(menuArchivoDesconectar);
		referenciaVentana.setMenu_archivo_excel(menuArchivoExcel);
		referenciaVentana.setMenu_archivo_importar(menuArchivoImportar);
		referenciaVentana.setMenu_archivo_sobreMi(menuArchivoSobreMi);
		referenciaVentana.setMenu_comic_aleatoria(menuComicAleatoria);
		referenciaVentana.setMenu_comic_aniadir(menuComicAniadir);
		referenciaVentana.setMenu_comic_eliminar(menuComicEliminar);
		referenciaVentana.setMenu_comic_modificar(menuComicModificar);
		referenciaVentana.setMenu_comic_puntuar(menuComicPuntuar);
		referenciaVentana.setMenu_estadistica_comprados(menuEstadisticaComprados);
		referenciaVentana.setMenu_estadistica_estadistica(menuEstadisticaEstadistica);
		referenciaVentana.setMenu_estadistica_firmados(menuEstadisticaFirmados);
		referenciaVentana.setMenu_estadistica_key_issue(menuEstadisticaKeyIssue);
		referenciaVentana.setMenu_estadistica_posesion(menuEstadisticaPosesion);
		referenciaVentana.setMenu_estadistica_puntuados(menuEstadisticaPuntuados);
		referenciaVentana.setMenu_estadistica_vendidos(menuEstadisticaVendidos);
		referenciaVentana.setMenu_archivo_avanzado(menuArchivoAvanzado);
		referenciaVentana.setMenu_comprobar_apis(menuComprobarApis);
		referenciaVentana.setMenu_navegacion(menuNavegacion);
		referenciaVentana.setNavegacion_Opciones(navegacionCerrar);
		referenciaVentana.setNavegacion_comic(navegacionComic);
		referenciaVentana.setNavegacion_estadistica(navegacionEstadistica);
		referenciaVentana.setTituloComic(nombreComic);
		referenciaVentana.setNombreDibujante(nombreDibujante);
		referenciaVentana.setNombreEditorial(nombreEditorial);
		referenciaVentana.setNombreFirma(nombreFirma);
		referenciaVentana.setNombreFormato(nombreFormato);
		referenciaVentana.setNombreGuionista(nombreGuionista);
		referenciaVentana.setNombreProcedencia(nombreProcedencia);
		referenciaVentana.setNombreVariante(nombreVariante);
		referenciaVentana.setGradeoComic(gradeoComic);
		referenciaVentana.setNumeroComic(numeroComic);
		referenciaVentana.setFechaComic(fechaPublicacion);
		referenciaVentana.setNumeroComic(numeroComic);
		referenciaVentana.setProntInfo(prontInfo);
		referenciaVentana.setProgresoCarga(progresoCarga);
		referenciaVentana.setID(id);
		referenciaVentana.setGradeo(gradeo);
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
		referenciaVentana.setStage(estadoStage());
		referenciaVentana.setBotonCancelarSubida(botonCancelarSubida);
		referenciaVentana.setBarraCambioAltura(barraCambioAltura);

		referenciaVentana.setComboBoxes(Arrays.asList(nombreComic, numeroComic, nombreVariante, nombreProcedencia,
				nombreFormato, nombreDibujante, nombreGuionista, nombreEditorial, nombreFirma, gradeoComic));

		AccionReferencias.setListaElementosFondo(FXCollections.observableArrayList(backgroundImage, menuNavegacion));

		AccionReferencias.setListaBotones(FXCollections.observableArrayList(botonLimpiar, botonMostrarParametro,
				botonbbdd, botonImprimir, botonGuardarResultado));

		AccionReferencias.setColumnasTabla(Arrays.asList(nombre, numero, variante, firma, editorial, formato,
				procedencia, fecha, guionista, dibujante, referencia));

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
		menuComprobarApis.setGraphic(Utilidades.createIcon("/Icono/Archivo/apis_check.png", 16, 16));
		menuArchivoApiMarvel.setGraphic(Utilidades.createIcon("/Icono/Archivo/check_apis.png", 16, 16));
		menuArchivoApiVine.setGraphic(Utilidades.createIcon("/Icono/Archivo/check_apis.png", 16, 16));
		menuArchivoDesconectar.setGraphic(Utilidades.createIcon("/Icono/Archivo/apagado.png", 16, 16));
		menuArchivoCerrar.setGraphic(Utilidades.createIcon("/Icono/Archivo/salir.png", 16, 16));

		menuComicAniadir.setGraphic(Utilidades.createIcon("/Icono/Ventanas/add.png", 16, 16));
		menuComicEliminar.setGraphic(Utilidades.createIcon("/Icono/Ventanas/delete.png", 16, 16));
		menuComicModificar.setGraphic(Utilidades.createIcon("/Icono/Ventanas/modify.png", 16, 16));
		menuComicPuntuar.setGraphic(Utilidades.createIcon("/Icono/Ventanas/puntuar.png", 16, 16));
		menuComicAleatoria.setGraphic(Utilidades.createIcon("/Icono/Ventanas/aleatorio.png", 16, 16));

		menuEstadisticaPosesion.setGraphic(Utilidades.createIcon("/Icono/Estadistica/posesion.png", 16, 16));
		menuEstadisticaComprados.setGraphic(Utilidades.createIcon("/Icono/Estadistica/comprado.png", 16, 16));
		menuEstadisticaVendidos.setGraphic(Utilidades.createIcon("/Icono/Estadistica/vendido.png", 16, 16));
		menuEstadisticaPuntuados.setGraphic(Utilidades.createIcon("/Icono/Estadistica/puntuados.png", 16, 16));
		menuEstadisticaFirmados.setGraphic(Utilidades.createIcon("/Icono/Estadistica/firmados.png", 16, 16));
		menuEstadisticaKeyIssue.setGraphic(Utilidades.createIcon("/Icono/Estadistica/keys.png", 16, 16));
		menuEstadisticaEstadistica.setGraphic(Utilidades.createIcon("/Icono/Estadistica/descarga.png", 16, 16));

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

		});

		AccionControlUI.establecerTooltips();

		formatearTextField();

	}

	@FXML
	void ampliarImagen(MouseEvent event) {

		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (idRow != null) {

			ImagenAmpliadaController.comicInfo = SelectManager.comicDatos(idRow.getid());

			nav.verVentanaImagen();
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
				double minHeight = 250; // Límite mínimo de altura

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

	}

	// Método para calcular el máximo altura permitido
	private double calcularMaxHeight() {
		// Obtener el tamaño actual de la ventana
		Stage stage = (Stage) rootVBox.getScene().getWindow();
		double windowHeight = stage.getHeight();

		// Ajustar el máximo altura permitido según la posición del AnchorPane
		// numeroCaja
		return windowHeight - gradeoComic.getLayoutY() - 80;
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

		try (ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1)) {
			scheduler.schedule(() -> Platform.runLater(() -> {
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

				// Manejar la cancelación
				botonCancelarSubida.setOnAction(ev -> {
					botonCancelarSubida.setVisible(false);
					task.cancel();
					scheduler.shutdown();
				});

				// Cuando la tarea haya terminado, apaga el scheduler
				task.setOnSucceeded(event -> {
					botonCancelarSubida.setVisible(false);
					scheduler.shutdown();
				});

				// Cuando la tarea haya terminado, apaga el scheduler
				task.setOnRunning(event -> botonCancelarSubida.setVisible(true));
			}), 0, TimeUnit.SECONDS);
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

		FuncionesManejoFront.establecerTamanioMaximoImagen(252.0, 337.0);

	}

	/**
	 * Funcion que permite restringir entrada de datos de todo aquello que no sea un
	 * numero entero en los comboBox numeroComic y caja_comic
	 */
	public void formatearTextField() {
		numeroComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
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
	}

	/**
	 * Permite el cambio de ventana a la ventana de SobreMiController
	 *
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {
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
	}

	private void mostrarComics(boolean esCompleto) {

		if (esCompleto) {
			AccionSeleccionar.verBasedeDatos(esCompleto, false, null);
		} else {
			List<String> controls = new ArrayList<>();
			for (ComboBox<String> comboBox : referenciaVentana.getComboboxes()) {
				controls.add(comboBox.getSelectionModel().getSelectedItem());
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
	void comicsPuntuacion(ActionEvent event) {
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
	void comicsVendidos(ActionEvent event) {
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
	void comicsFirmados(ActionEvent event) {
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
	void comicsComprados(ActionEvent event) {
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
	void comicsEnPosesion(ActionEvent event) {
		imprimirComicsEstado(TipoBusqueda.POSESION, false);
	}

	@FXML
	void comicsGuardados(ActionEvent event) {
		imprimirComicsEstado(null, true);

	}

	@FXML
	void verOpcionesAvanzadas(ActionEvent event) {
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

		limpiezaDeDatos();
		limpiarComboBox();
		ListaComicsDAO.reiniciarListaComics();
		FuncionesTableView.nombreColumnas();
		FuncionesTableView.actualizarBusquedaRaw();
		List<Comic> listaComics;
		if (esGuardado) {
			listaComics = ListaComicsDAO.comicsGuardadosList;
		} else {
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(tipoBusqueda);

			System.out.println(sentenciaSQL);

			listaComics = SelectManager.verLibreria(sentenciaSQL, false);
		}

		FuncionesTableView.tablaBBDD(listaComics);
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

		limpiezaDeDatos();
		limpiarComboBox();

		guardarDatosCSV();

		ListaComicsDAO.listasAutoCompletado();

		ListaComicsDAO.limpiarListaGuardados();

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

		boolean estaVacia = false;
		String mensaje = "";
		if (!ListaComicsDAO.listaNombre.isEmpty()) {
			limpiezaDeDatos();
			limpiarComboBox();
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

			List<Comic> listaComics = SelectManager.verLibreria(sentenciaSQL, false);

			cargaExportExcel(listaComics, DBUtilidades.TipoBusqueda.COMPLETA.toString());

			ListaComicsDAO.limpiarListaGuardados();

			estaVacia = true;
		} else {
			mensaje = "La base de datos esta vacia. No hay nada que exportar";
			AlarmaList.mostrarMensajePront(mensaje, estaVacia, prontInfo);
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
	void verEstadistica(ActionEvent event) {

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
	void clickRaton(MouseEvent event) {

		if (!tablaBBDD.isDisabled()) {
			enviarReferencias();
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
		if ((event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) && !tablaBBDD.isDisabled()) {
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

		prontInfo.clear();
		String tipoBusqueda = "Parcial";

		if (!ListaComicsDAO.comicsGuardadosList.isEmpty()) {
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

		Comic comicRaw = tablaBBDD.getSelectionModel().getSelectedItem();
		String mensaje = "";
		if (comicRaw != null) {
			boolean existeComic = ListaComicsDAO.verificarIDExistente(comicRaw.getid(), true);
			if (existeComic) {
				mensaje = "Este comic con dichaid: " + comicRaw.getid() + " ya existe. No se ha guardado \n \n \n";
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
										ListaComicsDAO.limpiarListaGuardados();
										Utilidades.eliminarContenidoCarpeta(FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH);
									}
									FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
									AlarmaList.mostrarMensajePront(mensaje, deleteCompleted, prontInfo);
									botonGuardarResultado.setVisible(false);

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

		FuncionesExcel excelFuntions = new FuncionesExcel();
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
			new Thread(lecturaTask).start();
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

		if (fuente instanceof Button botonPresionado) {
			if (botonPresionado == botonIntroducir) {
				AccionFuncionesComunes.setTipoAccion("aniadir");
			} else if (botonPresionado == botonModificar) {
				AccionFuncionesComunes.setTipoAccion("modificar");
			} else if (botonPresionado == botonEliminar) {
				AccionFuncionesComunes.setTipoAccion("eliminar");
			} else if (botonPresionado == botonAgregarPuntuacion) {
				AccionFuncionesComunes.setTipoAccion("puntuar");
			}
		} else if (fuente instanceof MenuItem menuItemPresionado) {
			if (menuItemPresionado == menuComicAniadir) {
				AccionFuncionesComunes.setTipoAccion("aniadir");
			} else if (menuItemPresionado == menuComicModificar) {
				AccionFuncionesComunes.setTipoAccion("modificar");
			} else if (menuItemPresionado == menuComicEliminar) {
				AccionFuncionesComunes.setTipoAccion("eliminar");
			} else if (menuItemPresionado == menuComicPuntuar) {
				AccionFuncionesComunes.setTipoAccion("puntuar");
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

		return (Stage) menuNavegacion.getScene().getWindow();
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
