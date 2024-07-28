
package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.ListasComicsDAO;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesInterfaz.FuncionesTableView;
import funcionesManagment.AccionAniadir;
import funcionesManagment.AccionEliminar;
import funcionesManagment.AccionFuncionesComunes;
import funcionesManagment.AccionModificar;
import funcionesManagment.AccionReferencias;
import funcionesManagment.AccionSeleccionar;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import javafx.stage.Stage;

/**
 * Clase controladora para la ventana de acciones, que gestiona la interfaz de
 * usuario y las operaciones relacionadas con los cómics.
 */
public class VentanaAccionController implements Initializable {
	@FXML
	private Label alarmaConexionInternet;

	@FXML
	private Label alarmaConexionSql;

	@FXML
	private Button botonBusquedaAvanzada;

	@FXML
	private Button botonBusquedaCodigo;

	@FXML
	private Button botonCancelarSubida;

	@FXML
	private Button botonClonarComic;

	@FXML
	private Button botonEliminar;

	@FXML
	private Button botonEliminarImportadoComic;

	@FXML
	private Button botonEliminarImportadoListaComic;

	@FXML
	private Button botonGuardarCambioComic;

	@FXML
	private Button botonGuardarComic;

	@FXML
	private Button botonGuardarListaComics;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonModificarComic;

	@FXML
	private Button botonParametroComic;

	@FXML
	private Button botonSubidaPortada;

	@FXML
	private Button botonbbdd;

	@FXML
	private TextField busquedaCodigo;

	@FXML
	private ImageView cargaImagen;

	@FXML
	private TableColumn<Comic, String> columnaArtista;

	@FXML
	private TableColumn<Comic, String> columnaPrecio;

	@FXML
	private TableColumn<Comic, String> columnaGuionista;

	@FXML
	private TableColumn<Comic, String> columnaVariante;

	@FXML
	private TableColumn<Comic, String> columnaNombre;

	@FXML
	private TableColumn<Comic, String> columnaNumero;

	@FXML
	private ComboBox<String> comboBoxTienda;

	@FXML
	private DatePicker dataPickFechaP;

	@FXML
	private TableColumn<Comic, String> id;

	@FXML
	private ImageView imagencomic;

	@FXML
	private Label labelArtista;

	@FXML
	private Label labelCodigo;

	@FXML
	private Label labelEditor;

	@FXML
	private Label labelFechaG;

	@FXML
	private Label labelGuionista;

	@FXML
	private Label labelId;

	@FXML
	private Label labelKey;

	@FXML
	private Label labelNombre;

	@FXML
	private Label labelPortada;

	@FXML
	private Label labelReferencia;

	@FXML
	private Label labelVariante;

	@FXML
	private Label labelPrecio;

	@FXML
	private MenuItem menuImportarFichero;

	@FXML
	private MenuBar menuNavegacion;

	@FXML
	private Menu navegacionEstadistica;

	@FXML
	private MenuItem navegacionMostrarEstadistica;

	@FXML
	private Menu navegacionOpciones;

	@FXML
	private ProgressIndicator progresoCarga;

	@FXML
	private TextArea prontInfo;

	@FXML
	private VBox rootVBox;

	@FXML
	private TableView<Comic> tablaBBDD;

	@FXML
	private TextArea textAreaKeyComic;

	@FXML
	private TextField textFieldArtistaComic;

	@FXML
	private TextField textFieldCodigoComic;

	@FXML
	private TextField textFieldDireccionComic;

	@FXML
	private TextField textFieldEditorComic;

	@FXML
	private TextField textFieldGuionistaComic;

	@FXML
	private TextField textFieldIdComic;

	@FXML
	private TextField textFieldNombreComic;

	@FXML
	private TextField textFieldNumeroComic;

	@FXML
	private TextField textFieldUrlComic;

	@FXML
	private TextField textFieldVarianteComic;

	@FXML
	private TextField textFieldPrecioComic;

	@FXML
	private TextField textFieldFirmaComic;

	@FXML
	private VBox vboxImage;

	/**
	 * Referencia a la ventana (stage).
	 */
	private Stage stage;

	public Comic comicCache;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	public static final AlarmaList alarmaList = new AlarmaList();

	public static final AccionReferencias referenciaVentana = new AccionReferencias();

	public static AccionReferencias referenciaVentanaPrincipal = new AccionReferencias();

	private static final Logger logger = Logger.getLogger(Utilidades.class.getName());

	public AccionReferencias guardarReferencia() {

		referenciaVentana.setTituloComicTextField(textFieldNombreComic);
		referenciaVentana.setCodigoComicTextField(textFieldCodigoComic);
		referenciaVentana.setNumeroComicTextField(textFieldNumeroComic);
		referenciaVentana.setDataPickFechaP(dataPickFechaP);
		referenciaVentana.setArtistaComicTextField(textFieldArtistaComic);
		referenciaVentana.setVarianteTextField(textFieldVarianteComic);
		referenciaVentana.setGuionistaComicTextField(textFieldGuionistaComic);
		referenciaVentana.setUrlReferenciaTextField(textFieldUrlComic);
		referenciaVentana.setDireccionImagenTextField(textFieldDireccionComic);
		referenciaVentana.setNombreTiendaCombobox(comboBoxTienda);
		referenciaVentana.setNombreEditorTextField(textFieldEditorComic);
		referenciaVentana.setIdComicTratarTextField(textFieldIdComic);
		referenciaVentana.setBusquedaCodigoTextField(busquedaCodigo);
		referenciaVentana.setPrecioComicTextField(textFieldPrecioComic);
		referenciaVentana.setFirmaComicTextField(textFieldFirmaComic);

		referenciaVentana.setKeyComicData(textAreaKeyComic);

		referenciaVentana.setBotonClonarComic(botonClonarComic);
		referenciaVentana.setBotonCancelarSubida(botonCancelarSubida);
		referenciaVentana.setBotonBusquedaCodigo(botonBusquedaCodigo);
		referenciaVentana.setBotonBusquedaAvanzada(botonBusquedaAvanzada);
		referenciaVentana.setBotonEliminar(botonEliminar);
		referenciaVentana.setBotonLimpiar(botonLimpiar);
		referenciaVentana.setBotonModificarComic(botonModificarComic);
		referenciaVentana.setBotonParametroComic(botonParametroComic);
		referenciaVentana.setBotonbbdd(botonbbdd);
		referenciaVentana.setBotonGuardarCambioComic(botonGuardarCambioComic);

		referenciaVentana.setBotonGuardarComic(botonGuardarComic);
		referenciaVentana.setBotonEliminarImportadoComic(botonEliminarImportadoComic);

		referenciaVentana.setBotonEliminarImportadoListaComic(botonEliminarImportadoListaComic);
		referenciaVentana.setBotonGuardarListaComics(botonGuardarListaComics);

		referenciaVentana.setBotonSubidaPortada(botonSubidaPortada);
		referenciaVentana.setBusquedaCodigoTextField(busquedaCodigo);

		referenciaVentana.setProgresoCarga(progresoCarga);

		referenciaVentana.setLabelIdMod(labelId);
		referenciaVentana.setLabelNombre(labelNombre);
		referenciaVentana.setLabelfechaG(labelFechaG);
		referenciaVentana.setLabelArtista(labelArtista);
		referenciaVentana.setLabelVariante(labelVariante);
		referenciaVentana.setLabelGuionista(labelGuionista);

		referenciaVentana.setLabelCodigo(labelCodigo);
		referenciaVentana.setLabelEditor(labelEditor);
		referenciaVentana.setLabelReferencia(labelReferencia);
		referenciaVentana.setLabelPortada(labelPortada);

		referenciaVentana.setAlarmaConexionInternet(alarmaConexionInternet);
		referenciaVentana.setAlarmaConexionSql(alarmaConexionSql);

		referenciaVentana.setTablaBBDD(tablaBBDD);
		referenciaVentana.setImagenComic(imagencomic);
		referenciaVentana.setCargaImagen(cargaImagen);
		referenciaVentana.setProntInfoTextArea(prontInfo);
		referenciaVentana.setRootVBox(rootVBox);
		referenciaVentana.setMenuImportarFicheroCodigoBarras(menuImportarFichero);
		referenciaVentana.setMenuEstadisticaEstadistica(navegacionMostrarEstadistica);
		referenciaVentana.setMenuNavegacion(menuNavegacion);
		referenciaVentana.setNavegacionCerrar(navegacionOpciones);
		referenciaVentana.setNavegacionEstadistica(navegacionEstadistica);
		referenciaVentana.setStageVentana(estadoStage());
		AccionReferencias.setListaTextFields(
				FXCollections.observableArrayList(Arrays.asList(textFieldNombreComic, textFieldNumeroComic,
						textFieldEditorComic, textFieldArtistaComic, textFieldVarianteComic, textFieldGuionistaComic,
						textFieldEditorComic, textFieldIdComic, textFieldDireccionComic, textFieldUrlComic)));

		referenciaVentana.setControlAccion(Arrays.asList(textFieldNombreComic, textFieldNumeroComic, dataPickFechaP,
				textFieldArtistaComic, textFieldVarianteComic, textFieldGuionistaComic, textFieldUrlComic,
				textFieldDireccionComic, textAreaKeyComic, textFieldEditorComic, textFieldCodigoComic,
				textFieldPrecioComic, textFieldFirmaComic, textFieldIdComic));

		AccionReferencias.setListaColumnasTabla(Arrays.asList(columnaNombre, columnaNumero, columnaArtista,
				columnaGuionista, columnaVariante, columnaPrecio));

		return referenciaVentana;
	}

	public void enviarReferencias() {
		AccionControlUI.setReferenciaVentana(guardarReferencia());
		AccionFuncionesComunes.setReferenciaVentana(guardarReferencia());
		AccionFuncionesComunes.setReferenciaVentanaPrincipal(referenciaVentanaPrincipal);
		FuncionesTableView.setReferenciaVentana(guardarReferencia());
		FuncionesManejoFront.setReferenciaVentana(guardarReferencia());

		AccionSeleccionar.setReferenciaVentana(guardarReferencia());
		AccionAniadir.setReferenciaVentana(guardarReferencia());
		AccionEliminar.setReferenciaVentana(guardarReferencia());
		AccionModificar.setReferenciaVentana(guardarReferencia());
		Utilidades.setReferenciaVentana(guardarReferencia());
		Ventanas.setReferenciaVentana(guardarReferencia());
		DBUtilidades.setReferenciaVentana(guardarReferencia());
	}

	/**
	 * Inicializa la interfaz de usuario y configura el comportamiento de los
	 * elementos al cargar la vista.
	 *
	 * @param location  La ubicación relativa del archivo FXML.
	 * @param resources Los recursos que pueden ser utilizados por el controlador.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		menuImportarFichero.setGraphic(Utilidades.createIcon("/Icono/Archivo/importar.png", 16, 16));

		navegacionMostrarEstadistica.setGraphic(Utilidades.createIcon("/Icono/Estadistica/descarga.png", 16, 16));

		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
		alarmaList.iniciarThreadChecker();

		Platform.runLater(() -> {

			enviarReferencias();

			AccionControlUI.controlarEventosInterfazAccion();

			AccionControlUI.autocompletarListas();

			rellenarCombosEstaticos();

			AccionControlUI.mostrarOpcion(AccionFuncionesComunes.getTipoAccion());

			FuncionesManejoFront.getStageVentanas().add(estadoStage());

			estadoStage().setOnCloseRequest(event -> stop());
			AccionSeleccionar.actualizarRefrenciaClick(guardarReferencia());
		});

		ListasComicsDAO.comicsImportados.clear();

		AccionControlUI.establecerTooltips();

		formatearTextField();

	}

	@FXML
	void ampliarImagen(MouseEvent event) {
		enviarReferencias();
		Comic comic = guardarReferencia().getTablaBBDD().getSelectionModel().getSelectedItem();
		ImagenAmpliadaController.setComicCache(comic);
		if (ImagenAmpliadaController.getComicCache() != null) {
			if (guardarReferencia().getImagenComic().getOpacity() != 0) {
				Ventanas.verVentanaImagen();
			}
		}
	}

	/**
	 * Rellena los combos estáticos en la interfaz. Esta función llena los
	 * ComboBoxes con opciones estáticas predefinidas.
	 */
	public void rellenarCombosEstaticos() {
		List<ComboBox<String>> listaComboboxes = new ArrayList<>();
		listaComboboxes.add(comboBoxTienda);
		FuncionesComboBox.rellenarComboBoxEstaticos(listaComboboxes);
	}

	public void formatearTextField() {
		FuncionesManejoFront.eliminarEspacioInicialYFinal(textFieldNombreComic);
		FuncionesManejoFront.eliminarSimbolosEspeciales(textFieldNombreComic);
		FuncionesManejoFront.restringirSimbolos(textFieldEditorComic);
//		FuncionesManejoFront.restringirSimbolos(textFieldGradeoComic);

		FuncionesManejoFront.reemplazarEspaciosMultiples(textFieldNombreComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(textFieldArtistaComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(textFieldArtistaComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(textFieldVarianteComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(textFieldGuionistaComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(textFieldCodigoComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(textFieldNumeroComic);
		FuncionesManejoFront.restringirSimboloClave(textAreaKeyComic);
		FuncionesManejoFront.restringirSimboloClave(textFieldEditorComic);
		FuncionesManejoFront.permitirUnSimbolo(textFieldNombreComic);
		FuncionesManejoFront.permitirUnSimbolo(busquedaCodigo);
		textFieldIdComic.setTextFormatter(FuncionesComboBox.validadorNenteros());

		if (AccionFuncionesComunes.TIPO_ACCION.equalsIgnoreCase("aniadir")) {
			textFieldIdComic.setTextFormatter(FuncionesComboBox.desactivarValidadorNenteros());
		}
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

		List<String> controls = new ArrayList<>();

		// Iterar sobre los TextField y ComboBox en referenciaVentana
		for (Control control : AccionReferencias.getListaTextFields()) {
			if (control instanceof TextField) {
				controls.add(((TextField) control).getText());
			} else if (control instanceof ComboBox<?>) {
				Object selectedItem = ((ComboBox<?>) control).getSelectionModel().getSelectedItem();
				controls.add(selectedItem != null ? selectedItem.toString() : "");
			}
		}

		// Añadir valores de los ComboBoxes de getListaComboboxes() a controls
		for (ComboBox<?> comboBox : AccionReferencias.getListaComboboxes()) {
			Object selectedItem = comboBox.getSelectionModel().getSelectedItem();
			controls.add(selectedItem != null ? selectedItem.toString() : "");
		}

		// Crear y procesar la Comic con los controles recogidos
		Comic comic = AccionControlUI.camposComic(controls, true);
		AccionSeleccionar.verBasedeDatos(false, true, comic);
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
		AccionControlUI.limpiarAutorellenos(false);
		AccionSeleccionar.verBasedeDatos(true, true, null);
	}

	/**
	 * Método que maneja el evento de guardar los datos de un cómic.
	 * 
	 * @param event El evento de acción que desencadena la llamada al método.
	 */
	@FXML
	void guardarDatos(ActionEvent event) {
		enviarReferencias();
		rellenarCombosEstaticos();
		nav.cerrarMenuOpciones();
		AccionModificar.actualizarComicLista();
		imagencomic.setImage(null);
		ImagenAmpliadaController.setComicCache(null);
		ocultarBotonesComics();
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
	void guardarComicImportados(ActionEvent event) throws IOException, SQLException {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionAniadir.guardarContenidoLista(false, ImagenAmpliadaController.getComicCache());
		rellenarCombosEstaticos();
		imagencomic.setImage(null);
		ImagenAmpliadaController.setComicCache(null);
		ocultarBotonesComics();
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
	void guardarListaImportados(ActionEvent event) throws IOException, SQLException {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionAniadir.guardarContenidoLista(true, null);
		rellenarCombosEstaticos();
		imagencomic.setImage(null);
		ImagenAmpliadaController.setComicCache(null);
		ocultarBotonesComics();
	}

	/**
	 * Llamada a funcion que modifica los datos de 1 comic en la base de datos.
	 *
	 * @param event
	 * @throws Exception
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@FXML
	void modificarDatos(ActionEvent event) throws Exception {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionModificar.modificarComic();
		rellenarCombosEstaticos();
		imagencomic.setImage(null);
		ImagenAmpliadaController.setComicCache(null);
	}

	@FXML
	void clonarComicSeleccionada(ActionEvent event) {

		int num = Ventanas.verVentanaNumero();
		Comic comic = guardarReferencia().getTablaBBDD().getSelectionModel().getSelectedItem();
		ImagenAmpliadaController.setComicCache(comic);

		for (int i = 0; i < num; i++) {
			Comic comicModificada = AccionFuncionesComunes.copiarComicClon(comic);
			AccionFuncionesComunes.procesarComicPorCodigo(comicModificada, true);
		}
	}

	@FXML
	void eliminarComicSeleccionado(ActionEvent event) {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionModificar.eliminarComicLista();
		rellenarCombosEstaticos();
		imagencomic.setImage(null);
		ImagenAmpliadaController.setComicCache(null);
		ocultarBotonesComics();
	}

	@FXML
	void eliminarListaComics(ActionEvent event) {
		enviarReferencias();
		nav.cerrarMenuOpciones();

		if (!ListasComicsDAO.comicsImportados.isEmpty() && nav.alertaBorradoLista()) {
			// Ocultar botones relacionados con comics
			ocultarBotonesComics();

			// Eliminar cada comic de la lista
			for (Comic comic : ListasComicsDAO.comicsImportados) {
				// Eliminar archivo de imagen asociado a la comic
				eliminarArchivoImagen(comic.getDireccionImagenComic());
			}

			// Limpiar la lista de comics y la tabla de la interfaz
			ListasComicsDAO.comicsImportados.clear();
			guardarReferencia().getTablaBBDD().getItems().clear();

			// Reiniciar la imagen de la comic y limpiar la ventana
			imagencomic.setImage(null);
			limpiarVentana();
		}

		// Rellenar combos estáticos después de la operación
		rellenarCombosEstaticos();
	}

	// Función para eliminar archivo de imagen
	private void eliminarArchivoImagen(String direccionImagen) {
		if (direccionImagen != null && !direccionImagen.isEmpty()) {
			File archivoImagen = new File(direccionImagen);
			if (archivoImagen.exists()) {
				// Intentar borrar el archivo de la imagen
				if (archivoImagen.delete()) {
					System.out.println("Archivo de imagen eliminado: " + direccionImagen);
				} else {
					System.err.println("No se pudo eliminar el archivo de imagen: " + direccionImagen);
					// Puedes lanzar una excepción aquí si lo prefieres
				}
			}
		}
	}

	// Función para ocultar botones relacionados con comics
	private void ocultarBotonesComics() {
		guardarReferencia().getBotonClonarComic().setVisible(false);
		guardarReferencia().getBotonGuardarComic().setVisible(false);
		guardarReferencia().getBotonEliminarImportadoComic().setVisible(false);
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
	void clickRaton(MouseEvent event) {
		enviarReferencias();
		if (!tablaBBDD.isDisabled()) {

			Comic comic = guardarReferencia().getTablaBBDD().getSelectionModel().getSelectedItem();
			ImagenAmpliadaController.setComicCache(comic);
			AccionSeleccionar.seleccionarComics(false);
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
			AccionSeleccionar.seleccionarComics(false);
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
		enviarReferencias();

		// Verificar si las claves API están ausentes o vacías
		if (!Utilidades.isInternetAvailable()) {
			nav.alertaException("Revisa las APIS de Marvel y Vine, estan incorrectas o no funcionan");
		} else {
			// Continuar con la lógica cuando ambas claves están presente
			AccionFuncionesComunes.cambiarVisibilidadAvanzada();

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
		enviarReferencias();
		if (Utilidades.isInternetAvailable()) {
			nav.cerrarMenuOpciones();
			AccionControlUI.limpiarAutorellenos(false);
			AccionControlUI.borrarDatosGraficos();
			String frase = "Fichero txt";

			String formato = "*.txt";
			File fichero = Utilidades.tratarFichero(frase, formato, false);

			// Verificar si se obtuvo un objeto FileChooser válido
			if (fichero != null) {
				enviarReferencias();
				rellenarCombosEstaticos();

				AccionFuncionesComunes.busquedaPorCodigoImportacion(fichero, "");

			}
		}

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
	public void busquedaPorCodigo(ActionEvent event) throws IOException, URISyntaxException {
		enviarReferencias();
		if (Utilidades.isInternetAvailable()) {
			String valorCodigo = busquedaCodigo.getText();
			String tipoTienda = comboBoxTienda.getValue();
			if (valorCodigo.isEmpty() || tipoTienda.isEmpty()) {
				return;
			}
			nav.cerrarMenuOpciones();
			AccionControlUI.borrarDatosGraficos();

			AccionFuncionesComunes.cargarRuning();

			// Aquí se asigna el CompletableFuture, este es un ejemplo de cómo podría ser
			// asignado:
			CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> {
				// Lógica para obtener los enlaces, esto es solo un ejemplo
				return obtenerEnlaces(valorCodigo);
			});

			// Si el future es null, entonces es necesario inicializarlo apropiadamente
			// antes de usarlo
			if (future != null) {
				future.thenAccept(enlaces -> {

					File fichero;
					try {
						fichero = createTempFile(enlaces);

						if (fichero != null) {
							enviarReferencias();
							rellenarCombosEstaticos();
							AccionFuncionesComunes.busquedaPorCodigoImportacion(fichero, tipoTienda);
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				});

				future.exceptionally(ex -> {
					ex.printStackTrace();
					return null; // Manejar errores aquí según sea necesario
				});
			}
		}
	}

	// Ejemplo de método para obtener los enlaces
	private List<String> obtenerEnlaces(String valorCodigo) {

		List<String> codigoGradeo = new ArrayList<>();
		codigoGradeo.add(valorCodigo);

		return codigoGradeo;
	}

	public File createTempFile(List<String> data) throws IOException {

		String tempDirectory = System.getProperty("java.io.tmpdir");

		// Create a temporary file in the system temporary directory
		Path tempFilePath = Files.createTempFile(Paths.get(tempDirectory), "tempFile", ".txt");
		logger.log(Level.INFO, "Temporary file created at: " + tempFilePath.toString());

		// Write data to the temporary file
		Files.write(tempFilePath, data, StandardOpenOption.WRITE);

		// Convert the Path to a File and return it
		return tempFilePath.toFile();
	}

	public void deleteFile(Path filePath) throws IOException {
		Files.delete(filePath);
	}

	/**
	 * Limpia los datos de la pantalla al hacer clic en el botón "Limpiar".
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		limpiarVentana();
	}

	public void limpiarVentana() {
		enviarReferencias();
		AccionFuncionesComunes.limpiarDatosPantallaAccion();
		rellenarCombosEstaticos();
		ImagenAmpliadaController.setComicCache(null);
	}

	/**
	 * Funcion que permite la subida de una
	 *
	 * @param event
	 */
	@FXML
	void nuevaPortada(ActionEvent event) {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionFuncionesComunes.subirPortada();
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
	void eliminarDatos(ActionEvent event) {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionModificar.eliminarComic();
		rellenarCombosEstaticos();

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

	public Stage estadoStage() {

		return (Stage) botonLimpiar.getScene().getWindow();
	}

	/**
	 * Cierra la ventana asociada a este controlador, si está disponible. Si no se
	 * ha establecido una instancia de ventana (Stage), este método no realiza
	 * ninguna acción.
	 */
	public void closeWindow() {

		stage = estadoStage();
		ImagenAmpliadaController.setComicCache(null);
		if (stage != null) {

			if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
				FuncionesManejoFront.getStageVentanas().remove(estadoStage());
			}
			nav.cerrarCargaComics();
			stage.close();
		}
	}

	public void stop() {

		if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
			FuncionesManejoFront.getStageVentanas().remove(estadoStage());
		}

		Utilidades.cerrarCargaComics();
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		VentanaAccionController.referenciaVentanaPrincipal = referenciaVentana;
	}
}
