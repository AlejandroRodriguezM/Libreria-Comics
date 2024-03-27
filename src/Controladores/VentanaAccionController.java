/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONException;

import Controladores.managment.AccionAniadir;
import Controladores.managment.AccionControlUI;
import Controladores.managment.AccionEliminar;
import Controladores.managment.AccionFuncionesComunes;
import Controladores.managment.AccionModificar;
import Controladores.managment.AccionReferencias;
import Controladores.managment.AccionSeleccionar;
import Funcionamiento.FuncionesApis;
import Funcionamiento.FuncionesComboBox;
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
import dbmanager.ListaComicsDAO;
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

	/**
	 * Campo de texto para la dirección de la imagen.
	 */
	@FXML
	private TextField direccionImagen;

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
	public static List<TableColumn<Comic, String>> columnList;

	/**
	 * Referencia a la ventana (stage).
	 */
	private Stage stage;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	/**
	 * Declaramos una lista de ComboBox de tipo String
	 */
	private static List<ComboBox<String>> comboboxes;

	public static String apiKey = FuncionesApis.cargarApiComicVine();
	public static String clavesMarvel[] = FuncionesApis.clavesApiMarvel();

	ObservableList<ImageView> listaImagenes;

	ObservableList<ComboBox<String>> listaComboBoxes;
	@SuppressWarnings("rawtypes")
	ObservableList<TableColumn> listaColumnas;
	List<TableColumn<Comic, String>> columnListCarga;
	ObservableList<Control> listaCamposTexto;
	ObservableList<Button> listaBotones;
	ObservableList<Node> listaElementosFondo;
	ObservableList<TextField> listaTextField;
	List<ComboBox<String>> comboboxesMod;

	private static AccionAniadir accionAniadir = new AccionAniadir();

	private static AccionEliminar accionEliminar = new AccionEliminar();

	private static AccionSeleccionar accionSeleccionar = new AccionSeleccionar();

	private static AccionModificar accionModificar = new AccionModificar();

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();
	
	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

	public AccionReferencias guardarReferencia() {
		AccionReferencias referenciaVentana = new AccionReferencias();
		referenciaVentana.setAlarmaConexionInternet(alarmaConexionInternet);
		referenciaVentana.setAlarmaConexionSql(alarmaConexionSql);
		referenciaVentana.setDireccionImagen(direccionImagen);
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
		referenciaVentana.setBotonCancelarSubida(botonCancelarSubida);
		referenciaVentana.setBotonAgregarPuntuacion(botonAgregarPuntuacion);
		referenciaVentana.setBotonBorrarOpinion(botonBorrarOpinion);
		referenciaVentana.setBotonBusquedaCodigo(botonBusquedaCodigo);
		referenciaVentana.setBotonBusquedaAvanzada(botonBusquedaAvanzada);
		referenciaVentana.setBotonEliminar(botonEliminar);
		referenciaVentana.setBotonLimpiar(botonLimpiar);
		referenciaVentana.setBotonModificarComic(botonModificarComic);
		referenciaVentana.setBotonParametroComic(botonParametroComic);
		referenciaVentana.setBotonVender(botonVender);
		referenciaVentana.setBotonbbdd(botonbbdd);
		referenciaVentana.setBotonGuardarComic(botonGuardarComic);
		referenciaVentana.setBotonGuardarCambioComic(botonGuardarCambioComic);
		referenciaVentana.setBotonEliminarImportadoComic(botonEliminarImportadoComic);
		referenciaVentana.setBotonSubidaPortada(botonSubidaPortada);
		referenciaVentana.setBusquedaCodigo(busquedaCodigo);
		referenciaVentana.setDibujanteComic(dibujanteComic);
		referenciaVentana.setEditorialComic(editorialComic);
		referenciaVentana.setFirmaComic(firmaComic);
		referenciaVentana.setGuionistaComic(guionistaComic);
		referenciaVentana.setIdComicTratar_mod(idComicTratar_mod);
		referenciaVentana.setCodigoComicTratar(codigoComicTratar);
		referenciaVentana.setNombreComic(nombreComic);
		referenciaVentana.setNombreKeyIssue(nombreKeyIssue);
		referenciaVentana.setPrecioComic(precioComic);
		referenciaVentana.setUrlReferencia(urlReferencia);
		referenciaVentana.setVarianteComic(varianteComic);
		referenciaVentana.setLabelPuntuacion(labelPuntuacion);
		referenciaVentana.setLabel_caja(label_caja);
		referenciaVentana.setLabel_dibujante(label_dibujante);
		referenciaVentana.setLabel_editorial(label_editorial);
		referenciaVentana.setLabel_estado(label_estado);
		referenciaVentana.setLabel_fecha(label_fecha);
		referenciaVentana.setLabel_firma(label_firma);
		referenciaVentana.setLabel_formato(label_formato);
		referenciaVentana.setLabel_guionista(label_guionista);
		referenciaVentana.setLabel_id_mod(label_id_mod);
		referenciaVentana.setLabel_codigo_comic(label_codigo_comic);
		referenciaVentana.setLabel_key(label_key);
		referenciaVentana.setLabel_portada(label_portada);
		referenciaVentana.setLabel_precio(label_precio);
		referenciaVentana.setLabel_procedencia(label_procedencia);
		referenciaVentana.setLabel_referencia(label_referencia);
		referenciaVentana.setEstadoComic(estadoComic);
		referenciaVentana.setFechaComic(fechaComic);
		referenciaVentana.setFormatoComic(formatoComic);
		referenciaVentana.setNumeroCajaComic(numeroCajaComic);
		referenciaVentana.setNumeroComic(numeroComic);
		referenciaVentana.setProcedenciaComic(procedenciaComic);
		referenciaVentana.setPuntuacionMenu(puntuacionMenu);
		referenciaVentana.setTablaBBDD(tablaBBDD);
		referenciaVentana.setImagencomic(imagencomic);
		referenciaVentana.setCargaImagen(cargaImagen);
		referenciaVentana.setProntInfo(prontInfo);
		referenciaVentana.setRootVBox(rootVBox);
		referenciaVentana.setMenu_Importar_Fichero_CodigoBarras(menu_Importar_Fichero_CodigoBarras);
		referenciaVentana.setMenu_archivo_conexion(menu_archivo_conexion);
		referenciaVentana.setMenu_leer_CodigoBarras(menu_leer_CodigoBarras);
		referenciaVentana.setMenu_comic_aleatoria(menu_comic_aleatoria);
		referenciaVentana.setMenu_comic_aniadir(menu_comic_aniadir);
		referenciaVentana.setMenu_comic_eliminar(menu_comic_eliminar);
		referenciaVentana.setMenu_comic_modificar(menu_comic_modificar);
		referenciaVentana.setMenu_comic_puntuar(menu_comic_puntuar);
		referenciaVentana.setMenu_estadistica_estadistica(menu_estadistica_estadistica);
		referenciaVentana.setMenu_navegacion(menu_navegacion);
		referenciaVentana.setNavegacion_cerrar(navegacion_cerrar);
		referenciaVentana.setNavegacion_comic(navegacion_comic);
		referenciaVentana.setNavegacion_estadistica(navegacion_estadistica);

		return referenciaVentana;
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
	 * Inicializa la interfaz de usuario y configura el comportamiento de los
	 * elementos al cargar la vista.
	 *
	 * @param location  La ubicación relativa del archivo FXML.
	 * @param resources Los recursos que pueden ser utilizados por el controlador.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AlarmaList alarmaList = new AlarmaList();

		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
		alarmaList.iniciarThreadChecker(true);

		Platform.runLater(() -> {
			AccionFuncionesComunes.referenciaVentana = guardarReferencia();
			
			AccionSeleccionar.referenciaVentana = guardarReferencia();
			
			AccionAniadir.referenciaVentana = guardarReferencia();
			
			AccionControlUI.referenciaVentana = guardarReferencia();
			
			AccionModificar.referenciaVentana = guardarReferencia();
			
			AccionEliminar.referenciaVentana = guardarReferencia();
			
			listas_autocompletado();

			rellenarCombosEstaticos();

			accionRellenoDatos.mostrarOpcion(AccionFuncionesComunes.TIPO_ACCION);

		});

		ListaComicsDAO.comicsImportados.clear();

		establecerTooltips();

		formatearTextField();

		controlarEventosInterfaz();

	}

	@FXML
	void ampliarImagen(MouseEvent event) {

		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (idRow != null) {

			ImagenAmpliadaController.comicInfo = idRow;

			nav.verVentanaImagen();
		}
	}

	/**
	 * Asigna tooltips a varios elementos en la interfaz gráfica. Estos tooltips
	 * proporcionan información adicional cuando el usuario pasa el ratón sobre los
	 * elementos.
	 */
	public void establecerTooltips() {
		Platform.runLater(() -> {
			Map<Node, String> tooltipsMap = new HashMap<>();

			tooltipsMap.put(nombreComic, "Nombre de los cómics / libros / mangas");
			tooltipsMap.put(numeroComic, "Número del cómic / libro / manga");
			tooltipsMap.put(varianteComic, "Nombre de la variante del cómic / libro / manga");
			tooltipsMap.put(botonLimpiar, "Limpia la pantalla y reinicia todos los valores");
			tooltipsMap.put(botonbbdd, "Botón para acceder a la base de datos");
			tooltipsMap.put(botonSubidaPortada, "Botón para subir una portada");
			tooltipsMap.put(botonEliminar, "Botón para eliminar un cómic");
			tooltipsMap.put(botonVender, "Botón para vender un cómic");
			tooltipsMap.put(botonParametroComic, "Botón para buscar un cómic mediante una lista de parámetros");
			tooltipsMap.put(botonModificarComic, "Botón para modificar un cómic");
			tooltipsMap.put(botonBorrarOpinion, "Botón para borrar una opinión");
			tooltipsMap.put(botonAgregarPuntuacion, "Botón para agregar una puntuación");
			tooltipsMap.put(puntuacionMenu, "Selecciona una puntuación en el menú");

			FuncionesTooltips.assignTooltips(tooltipsMap);
		});
	}

	public void listas_autocompletado() {
		if (ConectManager.conexionActiva()) {
			FuncionesManejoFront.asignarAutocompletado(nombreComic, ListaComicsDAO.listaNombre);
			FuncionesManejoFront.asignarAutocompletado(varianteComic, ListaComicsDAO.listaVariante);
			FuncionesManejoFront.asignarAutocompletado(firmaComic, ListaComicsDAO.listaFirma);
			FuncionesManejoFront.asignarAutocompletado(editorialComic, ListaComicsDAO.listaEditorial);
			FuncionesManejoFront.asignarAutocompletado(guionistaComic, ListaComicsDAO.listaGuionista);
			FuncionesManejoFront.asignarAutocompletado(dibujanteComic, ListaComicsDAO.listaDibujante);
			FuncionesManejoFront.asignarAutocompletado(numeroComic.getEditor(), ListaComicsDAO.listaNumeroComic);
		}
	}

	/**
	 * Controla los eventos de la interfaz, desactivando el enfoque en el VBox para
	 * evitar eventos de teclado, y añadiendo filtros y controladores de eventos
	 * para gestionar el enfoque entre el VBox y el TableView.
	 */
	private void controlarEventosInterfaz() {

		listaElementosVentana();

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
			accionSeleccionar.mostrarComic(idComicTratar_mod.getText());
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
	}

	public void listaElementosVentana() {
		FuncionesManejoFront manejoFront = new FuncionesManejoFront();
		manejoFront.setTableView(tablaBBDD);

		listaImagenes = FXCollections.observableArrayList(imagencomic);
		listaColumnas = FXCollections.observableArrayList(nombre, numero, variante, editorial, guionista, dibujante);
		columnListCarga = Arrays.asList(nombre, variante, editorial, guionista, dibujante);
		listaBotones = FXCollections.observableArrayList(botonLimpiar, botonbbdd, botonbbdd, botonParametroComic,
				botonLimpiar, botonBusquedaAvanzada, botonBusquedaCodigo);
		comboboxesMod = Arrays.asList(formatoComic, procedenciaComic, estadoComic, puntuacionMenu);

		columnList = columnListCarga;

		manejoFront.copiarListas(listaComboBoxes, columnList, listaCamposTexto, listaBotones, listaElementosFondo,
				listaImagenes);

		manejoFront.copiarElementos(prontInfo, null, null, null, columnList);
	}

	/**
	 * Rellena los combos estáticos en la interfaz. Esta función llena los
	 * ComboBoxes con opciones estáticas predefinidas.
	 */
	public void rellenarCombosEstaticos() {
		FuncionesComboBox.rellenarComboBoxEstaticos(comboboxesMod, AccionFuncionesComunes.TIPO_ACCION); // Llamada a la
																										// función para
																										// rellenar
		// // // ComboBoxes
	}

	public void formatearTextField() {
		// Agrupar funciones relacionadas
		limpiarTextField();
		restringirSimbolos();
		reemplazarEspaciosMultiples();
		permitirUnSimbolo();
		configurarValidadores();
		desactivarValidadorIdSiEsAccionAniadir();
	}

	private void limpiarTextField() {
		listaTextField = FXCollections.observableArrayList(nombreComic, editorialComic, guionistaComic, dibujanteComic,
				varianteComic);
		FuncionesManejoFront.eliminarEspacioInicial(nombreComic);
	}

	private void restringirSimbolos() {
		FuncionesManejoFront.restringirSimbolos(editorialComic);
		FuncionesManejoFront.restringirSimbolos(guionistaComic);
		FuncionesManejoFront.restringirSimbolos(dibujanteComic);
		FuncionesManejoFront.restringirSimbolos(varianteComic);
	}

	private void reemplazarEspaciosMultiples() {
		FuncionesManejoFront.reemplazarEspaciosMultiples(nombreComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(editorialComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(guionistaComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(dibujanteComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(varianteComic);
	}

	private void permitirUnSimbolo() {
		FuncionesManejoFront.permitirUnSimbolo(nombreComic);
		FuncionesManejoFront.permitirUnSimbolo(editorialComic);
		FuncionesManejoFront.permitirUnSimbolo(guionistaComic);
		FuncionesManejoFront.permitirUnSimbolo(dibujanteComic);
		FuncionesManejoFront.permitirUnSimbolo(varianteComic);
		FuncionesManejoFront.permitirUnSimbolo(busquedaCodigo);
	}

	private void configurarValidadores() {
		numeroComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroCajaComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		idComicTratar_mod.setTextFormatter(FuncionesComboBox.validador_Nenteros());
		precioComic.setTextFormatter(FuncionesComboBox.validador_Ndecimales());
	}

	private void desactivarValidadorIdSiEsAccionAniadir() {
		if (AccionFuncionesComunes.TIPO_ACCION.equalsIgnoreCase("aniadir")) {
			idComicTratar_mod.setTextFormatter(FuncionesComboBox.desactivarValidadorNenteros());
		}
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException {

		accionRellenoDatos.borrarDatosGraficos();

		Comic comic = camposComic();

		FuncionesManejoFront.verBasedeDatos(false, true, comic);
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

		accionRellenoDatos.limpiarAutorellenos();
		accionRellenoDatos.borrarDatosGraficos();

		FuncionesManejoFront.verBasedeDatos(true, true, null);
	}

	@FXML
	void eliminarComicSeleccionado(ActionEvent event) {
		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (nav.alertaEliminar()) {

			if (idRow != null) {

				String id_comic = idRow.getID();
				ListaComicsDAO.comicsImportados.removeIf(c -> c.getID().equals(id_comic));
				accionRellenoDatos.limpiarAutorellenos();
				FuncionesTableView.nombreColumnas(columnList, tablaBBDD);

				FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados, tablaBBDD, columnList);
				tablaBBDD.refresh();

				if (ListaComicsDAO.comicsImportados.size() < 1) {
					accionFuncionesComunes.cambiarEstadoBotones(false);
				}

			}
		}
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

		accionSeleccionar.seleccionarComics();
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

			accionSeleccionar.seleccionarComics();
		}
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
		accionModificar.accionPuntuar(false);
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
		accionModificar.accionPuntuar(true);
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
		if (!FuncionesApis.verificarClavesAPI(clavesMarvel, apiKey)) {
			nav.alertaException("Revisa las APIS de Marvel y Vine, estan incorrectas o no funcionan");
			return;
		} else {
			// Continuar con la lógica cuando ambas claves están presente
			accionFuncionesComunes.cambiarVisibilidadAvanzada();
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

		if (FuncionesApis.verificarClavesAPI(clavesMarvel, apiKey)) {
			if (Utilidades.isInternetAvailable()) {

				accionRellenoDatos.limpiarAutorellenos();
				accionRellenoDatos.borrarDatosGraficos();
				String frase = "Fichero txt";

				String formato = "*.txt";

				File fichero = Utilidades.tratarFichero(frase, formato).showOpenDialog(null); // Llamada a funcion

				if (fichero != null) {
					Platform.runLater(() -> {
						accionFuncionesComunes.busquedaPorCodigoImportacion(fichero);
					});

				}
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
	void busquedaPorCodigo(ActionEvent event) {

		Platform.runLater(() -> {

			try {
				if (!ConectManager.conexionActiva() || !Utilidades.isInternetAvailable()) {
					return;
				}

				if (!FuncionesApis.verificarClavesAPI(clavesMarvel, apiKey)) {

					prontInfo.setText("No estás conectado a internet. Revisa tu conexión");
					return;

				}

				String valorCodigo = Utilidades.eliminarEspacios(busquedaCodigo.getText());

				accionRellenoDatos.limpiarAutorellenos();
//				borrarDatosGraficos();

				if (valorCodigo.isEmpty()) {
					return;
				}

				AtomicBoolean isCancelled = new AtomicBoolean(true);

				Task<Void> tarea = new Task<>() {
					@Override
					protected Void call() throws Exception {

						if (accionFuncionesComunes.procesarComicPorCodigo(valorCodigo)) {
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
					accionRellenoDatos.limpiarAutorellenos();
					accionFuncionesComunes.cambiarEstadoBotones(true);
					imagencomic.setImage(null);
					imagencomic.setVisible(true);
					botonCancelarSubida.setVisible(true);

					AlarmaList.iniciarAnimacionCargaImagen(cargaImagen);
					menu_Importar_Fichero_CodigoBarras.setDisable(true);

				});

				tarea.setOnSucceeded(ev -> {
					AlarmaList.detenerAnimacionCargaImagen(cargaImagen);
					menu_Importar_Fichero_CodigoBarras.setDisable(false);
					botonCancelarSubida.setVisible(false);
					accionFuncionesComunes.cambiarEstadoBotones(false);

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
	 * Limpia los datos de la pantalla al hacer clic en el botón "Limpiar".
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		accionFuncionesComunes.limpiarDatosPantallaAccion();
	}

	/**
	 * Funcion que permite la subida de una
	 *
	 * @param event
	 */
	@FXML
	void nuevaPortada(ActionEvent event) {
		accionFuncionesComunes.subirPortada();
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
	 * Método asociado al evento de acción que se dispara al seleccionar la opción
	 * "Ver Menú Código de Barras". Invoca el método correspondiente en el objeto
	 * 'nav' para mostrar el menú de códigos de barras.
	 *
	 * @param event Objeto que representa el evento de acción.
	 */
	@FXML
	void verMenuCodigoBarras(ActionEvent event) {

		if ("aniadir".equals(AccionFuncionesComunes.TIPO_ACCION)) {
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

		if (!accionRellenoDatos.camposComicSonValidos()) {
			String mensaje = "Error. Debes de introducir los datos correctos";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			return; // Agregar return para salir del método en este punto
		}
		Comic datos = camposComic();
		if (datos.getID() == null || datos.getID().isEmpty()) {
			datos = ListaComicsDAO.buscarComicPorID(ListaComicsDAO.comicsImportados, datos.getID());
		}

		Comic.limpiarCamposComic(datos);

		for (Comic c : ListaComicsDAO.comicsImportados) {
			if (c.getID().equals(datos.getID())) {
				ListaComicsDAO.comicsImportados.set(ListaComicsDAO.comicsImportados.indexOf(c), datos);
				break;
			}
		}

		accionFuncionesComunes.cambiarEstadoBotones(false);
		botonCancelarSubida.setVisible(false); // Oculta el botón de cancelar

		accionRellenoDatos.limpiarAutorellenos();
		FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados, tablaBBDD, columnList); // Llamada a funcion
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
				accionRellenoDatos.validarCamposComic(true);
				FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados, tablaBBDD, columnList); // Llamada a
				accionRellenoDatos.limpiarAutorellenos();

				String mensajePront = "Has introducido los comics correctamente\n";
				AlarmaList.mostrarMensajePront(mensajePront, true, prontInfo);
			}
		}
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
		String id_comic = idComicTratar_mod.getText();
		idComicTratar_mod.setStyle("");
		if (accionFuncionesComunes.comprobarExistenciaComic(id_comic)) {
			if (nav.alertaAccionGeneral()) {
				accionFuncionesComunes.accionComicAsync(true); // Llamada a funcion que modificara el contenido de un
																// comic especifico.
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
				ListaComicsDAO.reiniciarListaComics();
				ListaComicsDAO.listasAutoCompletado();
				FuncionesTableView.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
				FuncionesTableView.actualizarBusquedaRaw(tablaBBDD, columnList);
				FuncionesTableView.tablaBBDD(listaComics, tablaBBDD, columnList);

				List<ComboBox<String>> comboboxes = getComboBoxes();

				funcionesCombo.rellenarComboBox(comboboxes);
			}
		}
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

		String id_comic = idComicTratar_mod.getText();
		idComicTratar_mod.setStyle("");
		Comic comicActualizar = ComicManagerDAO.comicDatos(id_comic);
		if (accionFuncionesComunes.comprobarExistenciaComic(id_comic)) {
			if (nav.alertaAccionGeneral()) {
				ComicManagerDAO.actualizarComicBBDD(comicActualizar, "vender");
				ListaComicsDAO.reiniciarListaComics();
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

		String id_comic = idComicTratar_mod.getText();
		idComicTratar_mod.setStyle("");
		if (accionFuncionesComunes.comprobarExistenciaComic(id_comic)) {
			if (nav.alertaAccionGeneral()) {
				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

				List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);

				ComicManagerDAO.borrarComic(id_comic);
				ListaComicsDAO.reiniciarListaComics();
				ListaComicsDAO.listasAutoCompletado();
				FuncionesTableView.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
				FuncionesTableView.actualizarBusquedaRaw(tablaBBDD, columnList);
				FuncionesTableView.tablaBBDD(listaComics, tablaBBDD, columnList);

				List<ComboBox<String>> comboboxes = getComboBoxes();

				funcionesCombo.rellenarComboBox(comboboxes);
			}

			else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			}
		}
	}
}
