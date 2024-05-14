package funcionesManagment;

import java.util.List;

import comicManagement.Comic;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class AccionReferencias {

	/**
	 * Columna de la tabla para la procedencia.
	 */
	private TableColumn<Comic, String> procedencia;

	/**
	 * Columna de la tabla para la referencia.
	 */
	private TableColumn<Comic, String> referencia;

	/**
	 * Columna de la tabla para la variante.
	 */
	private TableColumn<Comic, String> variante;

	/**
	 * Tabla que muestra información sobre cómics.
	 */
	public TableView<Comic> tablaBBDD;

	/**
	 * Contenedor de la interfaz gráfica.
	 */
	private VBox rootVBox;

	/**
	 * Contenedor del contenido.
	 */
	private VBox vboxContenido;

	/**
	 * Imagen de fondo.
	 */
	private ImageView backgroundImage;

	/**
	 * Panel de anclaje principal.
	 */
	private AnchorPane rootAnchorPane;

	/**
	 * Contenedor de imágenes.
	 */
	private VBox vboxImage;

	/**
	 * Panel de anclaje para información.
	 */
	private AnchorPane anchoPaneInfo;

	/**
	 * Botón para modificar información.
	 */
	private Button botonModificar;

	/**
	 * Botón para introducir información.
	 */
	private Button botonIntroducir;

	/**
	 * Botón para eliminar información.
	 */
	private Button botonEliminar;

	private Button botonComprimirPortadas;

	private Button botonReCopiarPortadas;

	/**
	 * Botón para agregar una puntuación.
	 */
	private Button botonAgregarPuntuacion;

	private Rectangle barraCambioAltura;

	private Label alarmaConexionInternet;

	/**
	 * Campo de texto para la dirección de la imagen.
	 */
	private TextField direccionImagen;

	/**
	 * Columna de la tabla para el ID.
	 */
	private TableColumn<Comic, String> ID;

	/**
	 * Columna de la tabla para la gradeo.
	 */
	private TableColumn<Comic, String> gradeo;

	/**
	 * Columna de la tabla para la fecha.
	 */
	private TableColumn<Comic, String> fecha;

	/**
	 * Columna de la tabla para la firma.
	 */
	private TableColumn<Comic, String> firma;

	/**
	 * Columna de la tabla para el formato.
	 */
	private TableColumn<Comic, String> formato;

	/**
	 * Columna de la tabla para mostrar el nombre del cómic.
	 */
	private TableColumn<Comic, String> nombre;

	/**
	 * Columna de la tabla para mostrar el número del cómic.
	 */
	private TableColumn<Comic, String> numero;

	/**
	 * Columna de la tabla para mostrar la editorial del cómic.
	 */
	private TableColumn<Comic, String> editorial;

	/**
	 * Columna de la tabla para mostrar el guionista del cómic.
	 */
	private TableColumn<Comic, String> guionista;

	/**
	 * Columna de la tabla para mostrar el dibujante del cómic.
	 */
	private TableColumn<Comic, String> dibujante;

	/**
	 * Botón para cancelar la subida de imagenes.
	 */
	private Button botonCancelarSubida;

	/**
	 * Botón para borrar una opinión.
	 */
	private Button botonBorrarOpinion;

	/**
	 * Botón para realizar una búsqueda por código.
	 */
	private Button botonBusquedaCodigo;

	/**
	 * Botón para realizar una búsqueda avanzada.
	 */
	private Button botonBusquedaAvanzada;

	/**
	 * Botón para limpiar campos.
	 */
	private Button botonLimpiar;

	/**
	 * Botón para modificar un cómic.
	 */
	private Button botonModificarComic;

	/**
	 * Botón para buscar mediante parametro un cómic.
	 */
	private Button botonParametroComic;

	/**
	 * Botón para vender un cómic.
	 */
	private Button botonVender;

	/**
	 * Botón para acceder a la base de datos.
	 */
	private Button botonbbdd;

	/**
	 * Botón para guardar un comic correctamente para el importado de comics
	 * mediante fichero.
	 */
	private Button botonGuardarComic;

	/**
	 * Boton que guarda un cambio en un comic especifico de los importados
	 */
	private Button botonGuardarCambioComic;

	/**
	 * Boton que elimina un comic seleccionado de los comics importados mediante
	 * fichero
	 */
	private Button botonEliminarImportadoComic;

	/**
	 * Boton que sirve para subir una imagen a un comic que escojamos
	 */
	private Button botonSubidaPortada;

	// Campos de texto (TextField)
	/**
	 * Campo de texto para la búsqueda por código.
	 */
	private TextField busquedaCodigo;

	/**
	 * Campo de texto para el dibujante del cómic.
	 */
	private TextField dibujanteComic;

	/**
	 * Campo de texto para la editorial del cómic.
	 */
	private TextField editorialComic;

	/**
	 * Campo de texto para la firma del cómic.
	 */
	private TextField firmaComic;

	/**
	 * Campo de texto para el guionista del cómic.
	 */
	private TextField guionistaComic;

	/**
	 * Campo de texto para el ID del cómic a tratar en modificacion.
	 */
	private TextField idComicTratar;

	/**
	 * Campo de texto para el codigo del cómic a tratar en modificacion o para
	 * añadir.
	 */
	private TextField codigoComicTratar;

	/**
	 * Campo de texto para el nombre del cómic.
	 */
	private TextField nombreComic;

	/**
	 * Campo de texto para el nombre del Key Issue del cómic.
	 */
	private TextField nombreKeyIssue;

	/**
	 * Campo de texto para el precio del cómic.
	 */
	private TextField precioComic;

	/**
	 * Campo de texto para la URL de referencia del cómic.
	 */
	private TextField urlReferencia;

	/**
	 * Campo de texto para la variante del cómic.
	 */
	private TextField varianteComic;

	// Etiquetas (Label)
	/**
	 * Etiqueta para mostrar la puntuación.
	 */
	private Label labelPuntuacion;

	/**
	 * Etiqueta para mostrar la gradeo.
	 */
	private Label label_gradeo;

	/**
	 * Etiqueta para mostrar el dibujante.
	 */
	private Label label_dibujante;

	/**
	 * Etiqueta para mostrar la editorial.
	 */
	private Label label_editorial;

	/**
	 * Etiqueta para mostrar el estado.
	 */
	private Label label_estado;

	/**
	 * Etiqueta para mostrar la fecha.
	 */
	private Label label_fecha;

	/**
	 * Etiqueta para mostrar la firma.
	 */
	private Label label_firma;

	/**
	 * Etiqueta para mostrar el formato.
	 */
	private Label label_formato;

	/**
	 * Etiqueta para mostrar el guionista.
	 */
	private Label label_guionista;

	/**
	 * Etiqueta para mostrar el ID en modificacion.
	 */
	private Label label_id_mod;

	/**
	 * Etiqueta para mostrar el codigo en modificacion o aniadir.
	 */
	private Label label_codigo_comic;

	/**
	 * Etiqueta para mostrar el Key Issue.
	 */
	private Label label_key;

	/**
	 * Etiqueta para mostrar la portada.
	 */
	private Label label_portada;

	/**
	 * Etiqueta para mostrar el precio.
	 */
	private Label label_precio;

	/**
	 * Etiqueta para mostrar la procedencia.
	 */
	private Label label_procedencia;

	/**
	 * Etiqueta para mostrar la referencia.
	 */
	private Label label_referencia;

	// Otros controles (ComboBox, DatePicker, TableView, etc.)
	/**
	 * ComboBox para seleccionar el estado del cómic.
	 */
	private ComboBox<String> estadoComic;

	/**
	 * DatePicker para seleccionar la fecha de publicación del cómic.
	 */
	private DatePicker fechaComic;

	/**
	 * ComboBox para seleccionar el formato del cómic.
	 */
	private ComboBox<String> tituloComic;

	/**
	 * ComboBox para seleccionar el formato del cómic.
	 */
	private ComboBox<String> formatoComic;

	/**
	 * ComboBox para seleccionar el número de gradeo del cómic.
	 */
	private ComboBox<String> gradeoComic;

	/**
	 * ComboBox para seleccionar el número del cómic.
	 */
	private ComboBox<String> numeroComic;

	/**
	 * ComboBox para seleccionar la procedencia del cómic.
	 */
	private ComboBox<String> procedenciaComic;

	/**
	 * ComboBox para seleccionar la puntuación en el menú.
	 */
	private ComboBox<String> puntuacionMenu;

	/**
	 * ImageView para mostrar la imagen del cómic.
	 */
	private ImageView imagencomic;

	/**
	 * ImageView para mostrar la carga de imagen del comic.
	 */
	private ImageView cargaImagen;

	/**
	 * TextArea para mostrar información de texto.
	 */
	private TextArea prontInfo;

	private Label prontInfoLabel;

	private MenuItem menu_Importar_Fichero_CodigoBarras;

	private MenuItem menuComprobarApis;

	private MenuItem menu_leer_CodigoBarras;

	private MenuItem menu_comic_aleatoria;

	private MenuItem menu_comic_aniadir;

	private MenuItem menu_comic_eliminar;

	private MenuItem menu_comic_modificar;

	private MenuItem menu_comic_puntuar;

	private MenuItem menu_estadistica_estadistica;

	private MenuBar menu_navegacion;

	private Menu navegacion_cerrar;

	private Menu navegacion_comic;

	private Menu navegacion_estadistica;

	private Label alarmaConexionSql;

	/**
	 * Botón para mostrar un parámetro.
	 */

	private Button botonMostrarParametro;

	/**
	 * Botón que permite imprimir el resultado de una busqueda por parametro
	 */

	private Button botonImprimir;

	/**
	 * Botón que permite guardar el resultado de una busqueda por parametro
	 */

	private Button botonGuardarResultado;

	private Button botonMostrarGuardados;

	/**
	 * Campo de texto para realizar una búsqueda general.
	 */

	private TextField busquedaGeneral;

	/**
	 * Selector de fecha de publicación.
	 */

	private DatePicker fechaPublicacion;

	/**
	 * Menú de archivo con opciones relacionadas con la base de datos.
	 */

	private MenuItem menu_archivo_cerrar, menu_archivo_delete, menu_archivo_desconectar, menu_archivo_excel,
			menu_archivo_importar, menu_archivo_sobreMi;

	/**
	 * Menú relacionado con estadísticas de cómics.
	 */

	private MenuItem menu_estadistica_comprados, menu_estadistica_firmados, menu_estadistica_key_issue,
			menu_estadistica_posesion, menu_estadistica_puntuados, menu_estadistica_vendidos;

	private MenuItem menu_archivo_avanzado;

	/**
	 * Selector para el nombre del dibujante.
	 */
	private ComboBox<String> nombreDibujante;

	/**
	 * Selector para el nombre de la editorial.
	 */
	private ComboBox<String> nombreEditorial;

	/**
	 * Selector para el nombre de la firma.
	 */
	private ComboBox<String> nombreFirma;

	/**
	 * Selector para el nombre del formato.
	 */
	private ComboBox<String> nombreFormato;

	/**
	 * Selector para el nombre del guionista.
	 */
	private ComboBox<String> nombreGuionista;

	/**
	 * Selector para el nombre de la procedencia.
	 */
	private ComboBox<String> nombreProcedencia;

	/**
	 * Selector para el nombre de la variante.
	 */
	private ComboBox<String> nombreVariante;

	/**
	 * Declaramos una lista de ComboBox de tipo String
	 */
	private static List<ComboBox<String>> comboboxes;

	private static ObservableList<Control> listaTextFields;
	private static ObservableList<Button> listaBotones;
	private static ObservableList<Node> listaElementosFondo;

	private static List<TableColumn<Comic, String>> columnasTabla;

	private ProgressIndicator progresoCarga;

	private Button botonActualizarDatos;

	private Button botonActualizarPortadas;

	private Button botonActualizarSoftware;

	private Button botonActualizarTodo;

	private Button botonDescargarPdf;

	private Button botonDescargarSQL;

	private Button botonNormalizarDB;

	private CheckBox checkFirmas;

	private ComboBox<String> comboPreviews;

	private Label labelComprobar;

	private Label labelVersion;

	private Label prontInfoEspecial;

	private Label prontInfoPreviews;

	private Label prontInfoPortadas;

	private Stage stage;

	public TableColumn<Comic, String> getProcedencia() {
		return procedencia;
	}

	public TableColumn<Comic, String> getReferencia() {
		return referencia;
	}

	public TableColumn<Comic, String> getVariante() {
		return variante;
	}

	public TableView<Comic> getTablaBBDD() {
		return tablaBBDD;
	}

	public VBox getRootVBox() {
		return rootVBox;
	}

	public VBox getVboxContenido() {
		return vboxContenido;
	}

	public ImageView getBackgroundImage() {
		return backgroundImage;
	}

	public AnchorPane getRootAnchorPane() {
		return rootAnchorPane;
	}

	public VBox getVboxImage() {
		return vboxImage;
	}

	public AnchorPane getAnchoPaneInfo() {
		return anchoPaneInfo;
	}

	public Button getBotonModificar() {
		return botonModificar;
	}

	public Button getBotonIntroducir() {
		return botonIntroducir;
	}

	public Button getBotonEliminar() {
		return botonEliminar;
	}

	public Button getBotonAgregarPuntuacion() {
		return botonAgregarPuntuacion;
	}

	public Rectangle getBarraCambioAltura() {
		return barraCambioAltura;
	}

	public Label getAlarmaConexionInternet() {
		return alarmaConexionInternet;
	}

	public TextField getDireccionImagen() {
		return direccionImagen;
	}

	public TableColumn<Comic, String> getID() {
		return ID;
	}

	public TableColumn<Comic, String> getGradeo() {
		return gradeo;
	}

	public TableColumn<Comic, String> getFecha() {
		return fecha;
	}

	public TableColumn<Comic, String> getFirma() {
		return firma;
	}

	public TableColumn<Comic, String> getFormato() {
		return formato;
	}

	public TableColumn<Comic, String> getNombre() {
		return nombre;
	}

	public TableColumn<Comic, String> getNumero() {
		return numero;
	}

	public TableColumn<Comic, String> getEditorial() {
		return editorial;
	}

	public TableColumn<Comic, String> getGuionista() {
		return guionista;
	}

	public TableColumn<Comic, String> getDibujante() {
		return dibujante;
	}

	public Button getBotonCancelarSubida() {
		return botonCancelarSubida;
	}

	public Button getBotonBorrarOpinion() {
		return botonBorrarOpinion;
	}

	public Button getBotonBusquedaCodigo() {
		return botonBusquedaCodigo;
	}

	public Button getBotonBusquedaAvanzada() {
		return botonBusquedaAvanzada;
	}

	public Button getBotonLimpiar() {
		return botonLimpiar;
	}

	public Button getBotonModificarComic() {
		return botonModificarComic;
	}

	public Button getBotonParametroComic() {
		return botonParametroComic;
	}

	public Button getBotonVender() {
		return botonVender;
	}

	public Button getBotonbbdd() {
		return botonbbdd;
	}

	public Button getBotonGuardarComic() {
		return botonGuardarComic;
	}

	public Button getBotonGuardarCambioComic() {
		return botonGuardarCambioComic;
	}

	public Button getBotonEliminarImportadoComic() {
		return botonEliminarImportadoComic;
	}

	public Button getBotonSubidaPortada() {
		return botonSubidaPortada;
	}

	public Label getLabelPuntuacion() {
		return labelPuntuacion;
	}

	public Label getLabel_gradeo() {
		return label_gradeo;
	}

	public Label getLabel_dibujante() {
		return label_dibujante;
	}

	public Label getLabel_editorial() {
		return label_editorial;
	}

	public Label getLabel_estado() {
		return label_estado;
	}

	public Label getLabel_fecha() {
		return label_fecha;
	}

	public Label getLabel_firma() {
		return label_firma;
	}

	public Label getLabel_formato() {
		return label_formato;
	}

	public Label getLabel_guionista() {
		return label_guionista;
	}

	public Label getLabel_id_mod() {
		return label_id_mod;
	}

	public Label getLabel_codigo_comic() {
		return label_codigo_comic;
	}

	public Label getLabel_key() {
		return label_key;
	}

	public Label getLabel_portada() {
		return label_portada;
	}

	public Label getLabel_precio() {
		return label_precio;
	}

	public Label getLabel_procedencia() {
		return label_procedencia;
	}

	public Label getLabel_referencia() {
		return label_referencia;
	}

	public ImageView getImagencomic() {
		return imagencomic;
	}

	public ImageView getCargaImagen() {
		return cargaImagen;
	}

	public TextArea getProntInfo() {
		return prontInfo;
	}

	public MenuItem getMenu_Importar_Fichero_CodigoBarras() {
		return menu_Importar_Fichero_CodigoBarras;
	}

	public MenuItem getMenu_leer_CodigoBarras() {
		return menu_leer_CodigoBarras;
	}

	public MenuItem getMenu_comic_aleatoria() {
		return menu_comic_aleatoria;
	}

	public MenuItem getMenu_comic_aniadir() {
		return menu_comic_aniadir;
	}

	public MenuItem getMenu_comic_eliminar() {
		return menu_comic_eliminar;
	}

	public MenuItem getMenu_comic_modificar() {
		return menu_comic_modificar;
	}

	public MenuItem getMenu_comic_puntuar() {
		return menu_comic_puntuar;
	}

	public MenuItem getMenu_estadistica_estadistica() {
		return menu_estadistica_estadistica;
	}

	public MenuBar getMenu_navegacion() {
		return menu_navegacion;
	}

	public Menu getNavegacion_cerrar() {
		return navegacion_cerrar;
	}

	public Menu getNavegacion_comic() {
		return navegacion_comic;
	}

	public Menu getNavegacion_estadistica() {
		return navegacion_estadistica;
	}

	public Label getAlarmaConexionSql() {
		return alarmaConexionSql;
	}

	public Button getBotonMostrarParametro() {
		return botonMostrarParametro;
	}

	public Button getBotonImprimir() {
		return botonImprimir;
	}

	public Button getBotonGuardarResultado() {
		return botonGuardarResultado;
	}

	public Button getBotonMostrarGuardados() {
		return botonMostrarGuardados;
	}

	public TextField getBusquedaGeneral() {
		return busquedaGeneral;
	}

	public DatePicker getFechaPublicacion() {
		return fechaPublicacion;
	}

	public MenuItem getMenu_archivo_cerrar() {
		return menu_archivo_cerrar;
	}

	public MenuItem getMenu_archivo_delete() {
		return menu_archivo_delete;
	}

	public MenuItem getMenu_archivo_desconectar() {
		return menu_archivo_desconectar;
	}

	public MenuItem getMenu_archivo_excel() {
		return menu_archivo_excel;
	}

	public MenuItem getMenu_archivo_importar() {
		return menu_archivo_importar;
	}

	public MenuItem getMenu_archivo_sobreMi() {
		return menu_archivo_sobreMi;
	}

	public MenuItem getMenu_estadistica_comprados() {
		return menu_estadistica_comprados;
	}

	public MenuItem getMenu_estadistica_firmados() {
		return menu_estadistica_firmados;
	}

	public MenuItem getMenu_estadistica_key_issue() {
		return menu_estadistica_key_issue;
	}

	public MenuItem getMenu_estadistica_posesion() {
		return menu_estadistica_posesion;
	}

	public MenuItem getMenu_estadistica_puntuados() {
		return menu_estadistica_puntuados;
	}

	public MenuItem getMenu_estadistica_vendidos() {
		return menu_estadistica_vendidos;
	}

	public MenuItem getMenu_archivo_avanzado() {
		return menu_archivo_avanzado;
	}

	public ProgressIndicator getProgresoCarga() {
		return progresoCarga;
	}

	public List<ComboBox<String>> getComboboxes() {
		return comboboxes;
	}

	public static ObservableList<Button> getListaBotones() {
		return listaBotones;
	}

	public ObservableList<Node> getListaElementosFondo() {
		return listaElementosFondo;
	}

	public List<TableColumn<Comic, String>> getColumnasTabla() {
		return columnasTabla;
	}

	public ObservableList<Control> getListaTextFields() {
		return listaTextFields;
	}

	public Button getBotonActualizarDatos() {
		return botonActualizarDatos;
	}

	public Button getBotonActualizarPortadas() {
		return botonActualizarPortadas;
	}

	public Button getBotonActualizarSoftware() {
		return botonActualizarSoftware;
	}

	public Button getBotonActualizarTodo() {
		return botonActualizarTodo;
	}

	public Button getBotonDescargarPdf() {
		return botonDescargarPdf;
	}

	public Button getBotonDescargarSQL() {
		return botonDescargarSQL;
	}

	public Button getBotonNormalizarDB() {
		return botonNormalizarDB;
	}

	public CheckBox getCheckFirmas() {
		return checkFirmas;
	}

	public ComboBox<String> getComboPreviews() {
		return comboPreviews;
	}

	public Label getLabelComprobar() {
		return labelComprobar;
	}

	public Label getLabelVersion() {
		return labelVersion;
	}

	public Label getProntInfoEspecial() {
		return prontInfoEspecial;
	}

	public Label getProntInfoPreviews() {
		return prontInfoPreviews;
	}

	public Label getProntInfoPortadas() {
		return prontInfoPortadas;
	}

	public Label getProntInfoLabel() {
		return prontInfoLabel;
	}

	public Stage getStage() {
		return stage;
	}

	public Button getBotonComprimirPortadas() {
		return botonComprimirPortadas;
	}

	public Button getBotonReCopiarPortadas() {
		return botonReCopiarPortadas;
	}

	public void setBotonComprimirPortadas(Button botonComprimirPortadas) {
		this.botonComprimirPortadas = botonComprimirPortadas;
	}

	public void setBotonReCopiarPortadas(Button botonReCopiarPortadas) {
		this.botonReCopiarPortadas = botonReCopiarPortadas;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setBotonActualizarDatos(Button botonActualizarDatos) {
		this.botonActualizarDatos = botonActualizarDatos;
	}

	public void setBotonActualizarPortadas(Button botonActualizarPortadas) {
		this.botonActualizarPortadas = botonActualizarPortadas;
	}

	public void setBotonActualizarSoftware(Button botonActualizarSoftware) {
		this.botonActualizarSoftware = botonActualizarSoftware;
	}

	public void setBotonActualizarTodo(Button botonActualizarTodo) {
		this.botonActualizarTodo = botonActualizarTodo;
	}

	public void setBotonDescargarPdf(Button botonDescargarPdf) {
		this.botonDescargarPdf = botonDescargarPdf;
	}

	public void setBotonDescargarSQL(Button botonDescargarSQL) {
		this.botonDescargarSQL = botonDescargarSQL;
	}

	public void setBotonNormalizarDB(Button botonNormalizarDB) {
		this.botonNormalizarDB = botonNormalizarDB;
	}

	public void setCheckFirmas(CheckBox checkFirmas) {
		this.checkFirmas = checkFirmas;
	}

	public void setComboPreviews(ComboBox<String> comboPreviews) {
		this.comboPreviews = comboPreviews;
	}

	public void setLabelComprobar(Label labelComprobar) {
		this.labelComprobar = labelComprobar;
	}

	public void setLabelVersion(Label labelVersion) {
		this.labelVersion = labelVersion;
	}

	public void setProntInfoEspecial(Label prontInfoEspecial) {
		this.prontInfoEspecial = prontInfoEspecial;
	}

	public void setProntInfoPreviews(Label prontInfoPreviews) {
		this.prontInfoPreviews = prontInfoPreviews;
	}

	public void setProntInfoPortadas(Label prontInfoPortadas) {
		this.prontInfoPortadas = prontInfoPortadas;
	}

	public void setListaTextFields(ObservableList<Control> listaTextFields) {
		AccionReferencias.listaTextFields = listaTextFields;
	}

	public static void setColumnasTabla(List<TableColumn<Comic, String>> columnasTabla) {
		AccionReferencias.columnasTabla = columnasTabla;
	}

	public static void setComboboxes(List<ComboBox<String>> comboboxes) {
		AccionReferencias.comboboxes = comboboxes;
	}

	public static void setListaBotones(ObservableList<Button> listaBotones) {
		AccionReferencias.listaBotones = listaBotones;
	}

	public static void setListaElementosFondo(ObservableList<Node> listaElementosFondo) {
		AccionReferencias.listaElementosFondo = listaElementosFondo;
	}

	public void setComboBoxes(List<ComboBox<String>> comboBoxes) {
		comboboxes = comboBoxes;
	}

	public void setProcedencia(TableColumn<Comic, String> procedencia) {
		this.procedencia = procedencia;
	}

	public void setReferencia(TableColumn<Comic, String> referencia) {
		this.referencia = referencia;
	}

	public void setVariante(TableColumn<Comic, String> variante) {
		this.variante = variante;
	}

	public void setTablaBBDD(TableView<Comic> tablaBBDD) {
		this.tablaBBDD = tablaBBDD;
	}

	public void setRootVBox(VBox rootVBox) {
		this.rootVBox = rootVBox;
	}

	public void setVboxContenido(VBox vboxContenido) {
		this.vboxContenido = vboxContenido;
	}

	public void setBackgroundImage(ImageView backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public void setRootAnchorPane(AnchorPane rootAnchorPane) {
		this.rootAnchorPane = rootAnchorPane;
	}

	public void setVboxImage(VBox vboxImage) {
		this.vboxImage = vboxImage;
	}

	public void setAnchoPaneInfo(AnchorPane anchoPaneInfo) {
		this.anchoPaneInfo = anchoPaneInfo;
	}

	public void setBotonModificar(Button botonModificar) {
		this.botonModificar = botonModificar;
	}

	public void setBotonIntroducir(Button botonIntroducir) {
		this.botonIntroducir = botonIntroducir;
	}

	public void setBotonEliminar(Button botonEliminar) {
		this.botonEliminar = botonEliminar;
	}

	public void setBotonAgregarPuntuacion(Button botonAgregarPuntuacion) {
		this.botonAgregarPuntuacion = botonAgregarPuntuacion;
	}

	public void setBarraCambioAltura(Rectangle barraCambioAltura) {
		this.barraCambioAltura = barraCambioAltura;
	}

	public void setAlarmaConexionInternet(Label alarmaConexionInternet) {
		this.alarmaConexionInternet = alarmaConexionInternet;
	}

	public void setDireccionImagen(TextField direccionImagen) {
		this.direccionImagen = direccionImagen;
	}

	public void setID(TableColumn<Comic, String> iD) {
		ID = iD;
	}

	public void setGradeo(TableColumn<Comic, String> gradeo) {
		this.gradeo = gradeo;
	}

	public void setFecha(TableColumn<Comic, String> fecha) {
		this.fecha = fecha;
	}

	public void setFirma(TableColumn<Comic, String> firma) {
		this.firma = firma;
	}

	public void setFormato(TableColumn<Comic, String> formato) {
		this.formato = formato;
	}

	public void setNombre(TableColumn<Comic, String> nombre) {
		this.nombre = nombre;
	}

	public void setNumero(TableColumn<Comic, String> numero) {
		this.numero = numero;
	}

	public void setEditorial(TableColumn<Comic, String> editorial) {
		this.editorial = editorial;
	}

	public void setGuionista(TableColumn<Comic, String> guionista) {
		this.guionista = guionista;
	}

	public void setDibujante(TableColumn<Comic, String> dibujante) {
		this.dibujante = dibujante;
	}

	public void setBotonCancelarSubida(Button botonCancelarSubida) {
		this.botonCancelarSubida = botonCancelarSubida;
	}

	public void setBotonBorrarOpinion(Button botonBorrarOpinion) {
		this.botonBorrarOpinion = botonBorrarOpinion;
	}

	public void setBotonBusquedaCodigo(Button botonBusquedaCodigo) {
		this.botonBusquedaCodigo = botonBusquedaCodigo;
	}

	public void setBotonBusquedaAvanzada(Button botonBusquedaAvanzada) {
		this.botonBusquedaAvanzada = botonBusquedaAvanzada;
	}

	public void setBotonLimpiar(Button botonLimpiar) {
		this.botonLimpiar = botonLimpiar;
	}

	public void setBotonModificarComic(Button botonModificarComic) {
		this.botonModificarComic = botonModificarComic;
	}

	public void setBotonParametroComic(Button botonParametroComic) {
		this.botonParametroComic = botonParametroComic;
	}

	public void setBotonVender(Button botonVender) {
		this.botonVender = botonVender;
	}

	public void setBotonbbdd(Button botonbbdd) {
		this.botonbbdd = botonbbdd;
	}

	public void setBotonGuardarComic(Button botonGuardarComic) {
		this.botonGuardarComic = botonGuardarComic;
	}

	public void setBotonGuardarCambioComic(Button botonGuardarCambioComic) {
		this.botonGuardarCambioComic = botonGuardarCambioComic;
	}

	public void setBotonEliminarImportadoComic(Button botonEliminarImportadoComic) {
		this.botonEliminarImportadoComic = botonEliminarImportadoComic;
	}

	public void setBotonSubidaPortada(Button botonSubidaPortada) {
		this.botonSubidaPortada = botonSubidaPortada;
	}

	public void setLabelPuntuacion(Label labelPuntuacion) {
		this.labelPuntuacion = labelPuntuacion;
	}

	public void setLabel_gradeo(Label label_gradeo) {
		this.label_gradeo = label_gradeo;
	}

	public void setLabel_dibujante(Label label_dibujante) {
		this.label_dibujante = label_dibujante;
	}

	public void setLabel_editorial(Label label_editorial) {
		this.label_editorial = label_editorial;
	}

	public void setLabel_estado(Label label_estado) {
		this.label_estado = label_estado;
	}

	public void setLabel_fecha(Label label_fecha) {
		this.label_fecha = label_fecha;
	}

	public void setLabel_firma(Label label_firma) {
		this.label_firma = label_firma;
	}

	public void setLabel_formato(Label label_formato) {
		this.label_formato = label_formato;
	}

	public void setLabel_guionista(Label label_guionista) {
		this.label_guionista = label_guionista;
	}

	public void setLabel_id_mod(Label label_id_mod) {
		this.label_id_mod = label_id_mod;
	}

	public void setLabel_codigo_comic(Label label_codigo_comic) {
		this.label_codigo_comic = label_codigo_comic;
	}

	public void setLabel_key(Label label_key) {
		this.label_key = label_key;
	}

	public void setLabel_portada(Label label_portada) {
		this.label_portada = label_portada;
	}

	public void setLabel_precio(Label label_precio) {
		this.label_precio = label_precio;
	}

	public void setLabel_procedencia(Label label_procedencia) {
		this.label_procedencia = label_procedencia;
	}

	public void setLabel_referencia(Label label_referencia) {
		this.label_referencia = label_referencia;
	}

	public void setImagencomic(ImageView imagencomic) {
		this.imagencomic = imagencomic;
	}

	public void setCargaImagen(ImageView cargaImagen) {
		this.cargaImagen = cargaImagen;
	}

	public void setProntInfo(TextArea prontInfo) {
		this.prontInfo = prontInfo;
	}

	public void setProntInfoLabel(Label prontInfoLabel) {
		this.prontInfoLabel = prontInfoLabel;
	}

	public void setMenu_Importar_Fichero_CodigoBarras(MenuItem menu_Importar_Fichero_CodigoBarras) {
		this.menu_Importar_Fichero_CodigoBarras = menu_Importar_Fichero_CodigoBarras;
	}

	public void setMenu_comprobar_apis(MenuItem menuComprobarApis) {
		this.menuComprobarApis = menuComprobarApis;
	}

	public MenuItem getMenu_comprobar_apis() {
		return menuComprobarApis;
	}

	public void setMenu_leer_CodigoBarras(MenuItem menu_leer_CodigoBarras) {
		this.menu_leer_CodigoBarras = menu_leer_CodigoBarras;
	}

	public void setMenu_comic_aleatoria(MenuItem menu_comic_aleatoria) {
		this.menu_comic_aleatoria = menu_comic_aleatoria;
	}

	public void setMenu_comic_aniadir(MenuItem menu_comic_aniadir) {
		this.menu_comic_aniadir = menu_comic_aniadir;
	}

	public void setMenu_comic_eliminar(MenuItem menu_comic_eliminar) {
		this.menu_comic_eliminar = menu_comic_eliminar;
	}

	public void setMenu_comic_modificar(MenuItem menu_comic_modificar) {
		this.menu_comic_modificar = menu_comic_modificar;
	}

	public void setMenu_comic_puntuar(MenuItem menu_comic_puntuar) {
		this.menu_comic_puntuar = menu_comic_puntuar;
	}

	public void setMenu_estadistica_estadistica(MenuItem menu_estadistica_estadistica) {
		this.menu_estadistica_estadistica = menu_estadistica_estadistica;
	}

	public void setMenu_navegacion(MenuBar menu_navegacion) {
		this.menu_navegacion = menu_navegacion;
	}

	public void setNavegacion_Opciones(Menu navegacion_cerrar) {
		this.navegacion_cerrar = navegacion_cerrar;
	}

	public void setNavegacion_comic(Menu navegacion_comic) {
		this.navegacion_comic = navegacion_comic;
	}

	public void setNavegacion_estadistica(Menu navegacion_estadistica) {
		this.navegacion_estadistica = navegacion_estadistica;
	}

	public void setAlarmaConexionSql(Label alarmaConexionSql) {
		this.alarmaConexionSql = alarmaConexionSql;
	}

	public void setBotonMostrarParametro(Button botonMostrarParametro) {
		this.botonMostrarParametro = botonMostrarParametro;
	}

	public void setBotonImprimir(Button botonImprimir) {
		this.botonImprimir = botonImprimir;
	}

	public void setBotonGuardarResultado(Button botonGuardarResultado) {
		this.botonGuardarResultado = botonGuardarResultado;
	}

	public void setBotonMostrarGuardados(Button botonMostrarGuardados) {
		this.botonMostrarGuardados = botonMostrarGuardados;
	}

	public void setFechaPublicacion(DatePicker fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public void setMenu_archivo_cerrar(MenuItem menu_archivo_cerrar) {
		this.menu_archivo_cerrar = menu_archivo_cerrar;
	}

	public void setMenu_archivo_delete(MenuItem menu_archivo_delete) {
		this.menu_archivo_delete = menu_archivo_delete;
	}

	public void setMenu_archivo_desconectar(MenuItem menu_archivo_desconectar) {
		this.menu_archivo_desconectar = menu_archivo_desconectar;
	}

	public void setMenu_archivo_excel(MenuItem menu_archivo_excel) {
		this.menu_archivo_excel = menu_archivo_excel;
	}

	public void setMenu_archivo_importar(MenuItem menu_archivo_importar) {
		this.menu_archivo_importar = menu_archivo_importar;
	}

	public void setMenu_archivo_sobreMi(MenuItem menu_archivo_sobreMi) {
		this.menu_archivo_sobreMi = menu_archivo_sobreMi;
	}

	public void setMenu_estadistica_comprados(MenuItem menu_estadistica_comprados) {
		this.menu_estadistica_comprados = menu_estadistica_comprados;
	}

	public void setMenu_estadistica_firmados(MenuItem menu_estadistica_firmados) {
		this.menu_estadistica_firmados = menu_estadistica_firmados;
	}

	public void setMenu_estadistica_key_issue(MenuItem menu_estadistica_key_issue) {
		this.menu_estadistica_key_issue = menu_estadistica_key_issue;
	}

	public void setMenu_estadistica_posesion(MenuItem menu_estadistica_posesion) {
		this.menu_estadistica_posesion = menu_estadistica_posesion;
	}

	public void setMenu_estadistica_puntuados(MenuItem menu_estadistica_puntuados) {
		this.menu_estadistica_puntuados = menu_estadistica_puntuados;
	}

	public void setMenu_estadistica_vendidos(MenuItem menu_estadistica_vendidos) {
		this.menu_estadistica_vendidos = menu_estadistica_vendidos;
	}

	public void setMenu_archivo_avanzado(MenuItem menu_archivo_avanzado) {
		this.menu_archivo_avanzado = menu_archivo_avanzado;
	}

	public void setProgresoCarga(ProgressIndicator progresoCarga) {
		this.progresoCarga = progresoCarga;
	}

	// ComboBox

	public ComboBox<String> getEstadoComic() {
		return estadoComic;
	}

	public ComboBox<String> getFormatoComic() {
		return formatoComic;
	}

	public ComboBox<String> getGradeoComic() {
		return gradeoComic;
	}

	public ComboBox<String> getNumeroComic() {
		return numeroComic;
	}

	public ComboBox<String> getProcedenciaComic() {
		return procedenciaComic;
	}

	public ComboBox<String> getPuntuacionMenu() {
		return puntuacionMenu;
	}

	public ComboBox<String> getNombreDibujante() {
		return nombreDibujante;
	}

	public ComboBox<String> getNombreEditorial() {
		return nombreEditorial;
	}

	public ComboBox<String> getNombreFirma() {
		return nombreFirma;
	}

	public ComboBox<String> getNombreFormato() {
		return nombreFormato;
	}

	public ComboBox<String> getNombreGuionista() {
		return nombreGuionista;
	}

	public ComboBox<String> getNombreProcedencia() {
		return nombreProcedencia;
	}

	public ComboBox<String> getNombreVariante() {
		return nombreVariante;
	}

	public ComboBox<String> getTituloComic() {
		return tituloComic;
	}

	public void setEstadoComic(ComboBox<String> estadoComic) {
		this.estadoComic = estadoComic;
	}

	public void setFechaComic(DatePicker fechaComic) {
		this.fechaComic = fechaComic;
	}

	public void setTituloComic(ComboBox<String> tituloComic) {
		this.tituloComic = tituloComic;
	}

	public void setNombreDibujante(ComboBox<String> nombreDibujante) {
		this.nombreDibujante = nombreDibujante;
	}

	public void setNombreEditorial(ComboBox<String> nombreEditorial) {
		this.nombreEditorial = nombreEditorial;
	}

	public void setNombreFirma(ComboBox<String> nombreFirma) {
		this.nombreFirma = nombreFirma;
	}

	public void setNombreFormato(ComboBox<String> nombreFormato) {
		this.nombreFormato = nombreFormato;
	}

	public void setNombreGuionista(ComboBox<String> nombreGuionista) {
		this.nombreGuionista = nombreGuionista;
	}

	public void setNombreProcedencia(ComboBox<String> nombreProcedencia) {
		this.nombreProcedencia = nombreProcedencia;
	}

	public void setNombreVariante(ComboBox<String> nombreVariante) {
		this.nombreVariante = nombreVariante;
	}

	public void setFormatoComic(ComboBox<String> formatoComic) {
		this.formatoComic = formatoComic;
	}

	public void setGradeoComic(ComboBox<String> gradeoComic) {
		this.gradeoComic = gradeoComic;
	}

	public void setNumeroComic(ComboBox<String> numeroComic) {
		this.numeroComic = numeroComic;
	}

	public void setProcedenciaComic(ComboBox<String> procedenciaComic) {
		this.procedenciaComic = procedenciaComic;
	}

	public void setPuntuacionMenu(ComboBox<String> puntuacionMenu) {
		this.puntuacionMenu = puntuacionMenu;
	}

	// TextField

	public void setBusquedaCodigo(TextField busquedaCodigo) {
		this.busquedaCodigo = busquedaCodigo;
	}

	public void setDibujanteComic(TextField dibujanteComic) {
		this.dibujanteComic = dibujanteComic;
	}

	public void setEditorialComic(TextField editorialComic) {
		this.editorialComic = editorialComic;
	}

	public void setFirmaComic(TextField firmaComic) {
		this.firmaComic = firmaComic;
	}

	public void setGuionistaComic(TextField guionistaComic) {
		this.guionistaComic = guionistaComic;
	}

	public void setIdComicTratar(TextField idComicTratar) {
		this.idComicTratar = idComicTratar;
	}

	public void setCodigoComicTratar(TextField codigoComicTratar) {
		this.codigoComicTratar = codigoComicTratar;
	}

	public void setNombreComic(TextField nombreComic) {
		this.nombreComic = nombreComic;
	}

	public void setNombreKeyIssue(TextField nombreKeyIssue) {
		this.nombreKeyIssue = nombreKeyIssue;
	}

	public void setPrecioComic(TextField precioComic) {
		this.precioComic = precioComic;
	}

	public void setUrlReferencia(TextField urlReferencia) {
		this.urlReferencia = urlReferencia;
	}

	public void setVarianteComic(TextField varianteComic) {
		this.varianteComic = varianteComic;
	}

	public void setBusquedaGeneral(TextField busquedaGeneral) {
		this.busquedaGeneral = busquedaGeneral;
	}

	public TextField getBusquedaCodigo() {
		return busquedaCodigo;
	}

	public TextField getDibujanteComic() {
		return dibujanteComic;
	}

	public TextField getEditorialComic() {
		return editorialComic;
	}

	public TextField getFirmaComic() {
		return firmaComic;
	}

	public TextField getGuionistaComic() {
		return guionistaComic;
	}

	public TextField getIdComicTratar() {
		return idComicTratar;
	}

	public TextField getCodigoComicTratar() {
		return codigoComicTratar;
	}

	public TextField getNombreComic() {
		return nombreComic;
	}

	public TextField getNombreKeyIssue() {
		return nombreKeyIssue;
	}

	public TextField getPrecioComic() {
		return precioComic;
	}

	public TextField getUrlReferencia() {
		return urlReferencia;
	}

	public TextField getVarianteComic() {
		return varianteComic;
	}

	public DatePicker getFechaComic() {
		return fechaComic;
	}

}
