
package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONException;

import Controladores.managment.AccionAniadir;
import Controladores.managment.AccionEliminar;
import Controladores.managment.AccionFuncionesComunes;
import Controladores.managment.AccionModificar;
import Controladores.managment.AccionReferencias;
import Controladores.managment.AccionSeleccionar;
import alarmas.AlarmaList;
import apisFunciones.FuncionesApis;
import comicManagement.Comic;
import controlUI.AccionControlUI;
import controlUI.FuncionesComboBox;
import controlUI.FuncionesManejoFront;
import dbmanager.ConectManager;
import dbmanager.ListaComicsDAO;
import funciones_auxiliares.Utilidades;
import funciones_auxiliares.Ventanas;
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

	public static String apiKey = FuncionesApis.cargarApiComicVine();
	public static String clavesMarvel[] = FuncionesApis.clavesApiMarvel();

	public ObservableList<ImageView> listaImagenes;

	public ObservableList<ComboBox<String>> listaComboBoxes;
	public ObservableList<TableColumn<Comic, String>> listaColumnas;
	public List<TableColumn<Comic, String>> columnListCarga;
	public ObservableList<Control> listaCamposTexto;
	public ObservableList<Button> listaBotones;
	public ObservableList<Node> listaElementosFondo;
	public ObservableList<TextField> listaTextField;

	public static List<ComboBox<String>> comboboxesMod;

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();

	public static AccionReferencias referenciaVentana = new AccionReferencias();

	public AccionReferencias guardarReferencia() {
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
		referenciaVentana.setNombreComic(nombreComic);
		referenciaVentana.setNombreKeyIssue(nombreKeyIssue);
		referenciaVentana.setPrecioComic(precioComic);
		referenciaVentana.setUrlReferencia(urlReferencia);
		referenciaVentana.setVarianteComic(varianteComic);
		referenciaVentana.setEstadoComic(estadoComic);
		referenciaVentana.setFechaComic(fechaComic);
		referenciaVentana.setFormatoComic(formatoComic);
		referenciaVentana.setNumeroCajaComic(numeroCajaComic);
		referenciaVentana.setNumeroComic(numeroComic);
		referenciaVentana.setProcedenciaComic(procedenciaComic);
		referenciaVentana.setIdComicTratar_mod(idComicTratar_mod);
		referenciaVentana.setCodigoComicTratar(codigoComicTratar);
		referenciaVentana.setStage(estadoStage());

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

		AccionReferencias.setListaTextFields(FXCollections.observableArrayList(
				Arrays.asList(nombreComic, numeroComic, varianteComic, firmaComic, editorialComic, formatoComic,
						procedenciaComic, guionistaComic, dibujanteComic, numeroCajaComic, direccionImagen, estadoComic,
						nombreKeyIssue, precioComic, urlReferencia, codigoComicTratar, idComicTratar_mod)));

		AccionReferencias.setColumnasTabla(Arrays.asList(nombre, variante, editorial, guionista, dibujante));

		return referenciaVentana;
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

			enviarReferencias();

			AccionControlUI.controlarEventosInterfazAccion();

			AccionControlUI.listas_autocompletado();

			rellenarCombosEstaticos();

			AccionControlUI.mostrarOpcion(AccionFuncionesComunes.TIPO_ACCION);

			FuncionesManejoFront.stageVentanas.add(estadoStage());
			System.out.println(FuncionesManejoFront.stageVentanas.size());
		});

		ListaComicsDAO.comicsImportados.clear();

		AccionControlUI.establecerTooltips();

		formatearTextField();

	}

	public void enviarReferencias() {
		AccionFuncionesComunes.referenciaVentana = guardarReferencia();

		AccionControlUI.referenciaVentana = guardarReferencia();

		AccionSeleccionar.referenciaVentana = guardarReferencia();

		AccionAniadir.referenciaVentana = guardarReferencia();

		AccionModificar.referenciaVentana = guardarReferencia();

		AccionEliminar.referenciaVentana = guardarReferencia();
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
	 * Rellena los combos estáticos en la interfaz. Esta función llena los
	 * ComboBoxes con opciones estáticas predefinidas.
	 */
	public void rellenarCombosEstaticos() {
		FuncionesComboBox.rellenarComboBoxEstaticos(comboboxesMod, AccionFuncionesComunes.TIPO_ACCION);
	}

	public void formatearTextField() {
		listaTextField = FXCollections.observableArrayList(nombreComic, editorialComic, guionistaComic, dibujanteComic,
				varianteComic);
		FuncionesManejoFront.eliminarEspacioInicialYFinal(nombreComic);
		FuncionesManejoFront.eliminarSimbolosEspeciales(nombreComic);
		FuncionesManejoFront.restringirSimbolos(editorialComic);
		FuncionesManejoFront.restringirSimbolos(guionistaComic);
		FuncionesManejoFront.restringirSimbolos(dibujanteComic);
		FuncionesManejoFront.restringirSimbolos(varianteComic);

		FuncionesManejoFront.reemplazarEspaciosMultiples(nombreComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(editorialComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(guionistaComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(dibujanteComic);
		FuncionesManejoFront.reemplazarEspaciosMultiples(varianteComic);

		FuncionesManejoFront.permitirUnSimbolo(nombreComic);
		FuncionesManejoFront.permitirUnSimbolo(editorialComic);
		FuncionesManejoFront.permitirUnSimbolo(guionistaComic);
		FuncionesManejoFront.permitirUnSimbolo(dibujanteComic);
		FuncionesManejoFront.permitirUnSimbolo(varianteComic);
		FuncionesManejoFront.permitirUnSimbolo(busquedaCodigo);

		numeroComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroCajaComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		idComicTratar_mod.setTextFormatter(FuncionesComboBox.validador_Nenteros());
		precioComic.setTextFormatter(FuncionesComboBox.validador_Ndecimales());

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
		enviarReferencias();
		AccionControlUI.borrarDatosGraficos();

		Comic comic = AccionControlUI.camposComic(referenciaVentana.getListaCamposTexto(), true);

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
	void verTodabbdd(ActionEvent event) throws IOException, SQLException {
		enviarReferencias();
		AccionControlUI.limpiarAutorellenos(false);
		AccionControlUI.borrarDatosGraficos();

		AccionSeleccionar.verBasedeDatos(true, true, null);
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
		if (!ConectManager.conexionActiva()) {
			return;
		}
		AccionModificar.modificarComic();
	}

	@FXML
	void eliminarComicSeleccionado(ActionEvent event) {
		enviarReferencias();

		AccionEliminar.eliminarComicLista();

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
		enviarReferencias();
		AccionSeleccionar.seleccionarComics(false);
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
			AccionSeleccionar.seleccionarComics(false);
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
		enviarReferencias();
		AccionModificar.accionPuntuar(false);
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
		enviarReferencias();
		AccionModificar.accionPuntuar(true);
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

				AccionControlUI.limpiarAutorellenos(false);
				AccionControlUI.borrarDatosGraficos();
				String frase = "Fichero txt";

				String formato = "*.txt";

				File fichero = Utilidades.tratarFichero(frase, formato, false);

				// Verificar si se obtuvo un objeto FileChooser válido
				if (fichero != null) {
					AccionFuncionesComunes.busquedaPorCodigoImportacion(fichero);
					enviarReferencias();
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
		enviarReferencias();
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

				if (valorCodigo.isEmpty()) {
					return;
				}

				AccionControlUI.limpiarAutorellenos(false);

				AtomicBoolean isCancelled = new AtomicBoolean(true);

				Task<Void> tarea = new Task<>() {
					@Override
					protected Void call() throws Exception {

						if (isCancelled() || !referenciaVentana.getStage().isShowing()) {
							return null; // Exit the call() method if the task has been canceled or the stage is not
											// showing
						}

						if (AccionFuncionesComunes.procesarComicPorCodigo(valorCodigo)) {
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
					AccionControlUI.limpiarAutorellenos(false);
					AccionFuncionesComunes.cambiarEstadoBotones(true);
					imagencomic.setImage(null);
					imagencomic.setVisible(true);
					botonCancelarSubida.setVisible(true);
					botonBusquedaCodigo.setDisable(true);
					botonSubidaPortada.setDisable(true);
					referenciaVentana.getMenu_Importar_Fichero_CodigoBarras().setDisable(true);
					AlarmaList.iniciarAnimacionCargaImagen(cargaImagen);
					menu_Importar_Fichero_CodigoBarras.setDisable(true);
					FuncionesManejoFront.cambiarEstadoMenuBar(true);
				});

				tarea.setOnSucceeded(ev -> {
					AlarmaList.detenerAnimacionCargaImagen(cargaImagen);
					menu_Importar_Fichero_CodigoBarras.setDisable(false);
					botonCancelarSubida.setVisible(false);
					botonBusquedaCodigo.setDisable(false);
					botonSubidaPortada.setDisable(false);
					referenciaVentana.getMenu_Importar_Fichero_CodigoBarras().setDisable(false);
					AccionFuncionesComunes.cambiarEstadoBotones(false);
					FuncionesManejoFront.cambiarEstadoMenuBar(false);
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
					botonBusquedaCodigo.setDisable(false);
					botonSubidaPortada.setDisable(false);
					referenciaVentana.getMenu_Importar_Fichero_CodigoBarras().setDisable(false);
					AlarmaList.detenerAnimacionCargaImagen(cargaImagen); // Detiene la animación de carga
					AccionFuncionesComunes.cambiarEstadoBotones(false);
					FuncionesManejoFront.cambiarEstadoMenuBar(false);
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
		enviarReferencias();
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

	/**
	 * Método asociado al evento de acción que se dispara al seleccionar la opción
	 * "Ver Menú Código de Barras". Invoca el método correspondiente en el objeto
	 * 'nav' para mostrar el menú de códigos de barras.
	 *
	 * @param event Objeto que representa el evento de acción.
	 */
	@FXML
	void verMenuCodigoBarras(ActionEvent event) {
		enviarReferencias();
		if ("aniadir".equals(AccionFuncionesComunes.TIPO_ACCION)) {
			nav.verMenuCodigosBarra();
		}
	}

	@FXML
	void verEstadoConexion(ActionEvent event) {
		enviarReferencias();
		nav.verEstadoConexion();

	}

	/**
	 * Método que maneja el evento de guardar los datos de un cómic.
	 * 
	 * @param event El evento de acción que desencadena la llamada al método.
	 */
	@FXML
	void guardarDatos(ActionEvent event) {
		enviarReferencias();
		if (!ConectManager.conexionActiva()) {
			return;
		}

		AccionModificar.actualizarComicLista();

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
		enviarReferencias();
		if (!ConectManager.conexionActiva()) {
			return;
		}
		AccionAniadir.guardarContenidoLista();

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
		enviarReferencias();
		if (!ConectManager.conexionActiva()) {
			return;
		}

		AccionModificar.venderComic();
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
		enviarReferencias();
		if (!ConectManager.conexionActiva()) {
			return;
		}

		AccionEliminar.eliminarComic();

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

		if (stage != null) {

			if (FuncionesManejoFront.stageVentanas.contains(estadoStage())) {
				FuncionesManejoFront.stageVentanas.remove(estadoStage());
			}
			nav.cerrarCargaComics();
			stage.close();
		}
	}
}
