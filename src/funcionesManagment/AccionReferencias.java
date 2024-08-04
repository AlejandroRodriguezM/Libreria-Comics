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

	private TableColumn<Comic, String> iDColumna;
	private TableColumn<Comic, String> tituloColumna;
	private TableColumn<Comic, String> numeroColumna;
	private TableColumn<Comic, String> editorColumna;
	private TableColumn<Comic, String> firmaColumna;
	private TableColumn<Comic, String> artistaColumna;
	private TableColumn<Comic, String> guionistaColumna;
	private TableColumn<Comic, String> varianteColumna;
	private TableColumn<Comic, String> precioColumna;
	private TableColumn<Comic, String> referenciaColumna;

	public TableView<Comic> tablaBBDD;

	private VBox rootVBox;
	private VBox vboxContenido;
	private VBox vboxImage;

	private AnchorPane rootAnchorPane;
	private AnchorPane anchoPaneInfo;

	private ImageView backgroundImage;
	private ImageView imagenComic;
	private ImageView cargaImagen;

	private Button botonClonarComic;
	private Button botonModificar;
	private Button botonIntroducir;
	private Button botonEliminar;
	private Button botonComprimirPortadas;
	private Button botonReCopiarPortadas;
	private Button botonCancelarSubida;
	private Button botonBusquedaCodigo;
	private Button botonBusquedaAvanzada;
	private Button botonLimpiar;
	private Button botonModificarComic;
	private Button botonIntroducirComic;
	private Button botonParametroComic;
	private Button botonbbdd;
	private Button botonGuardarComic;
	private Button botonGuardarCambioComic;
	private Button botonEliminarImportadoComic;
	private Button botonSubidaPortada;
	private Button botonMostrarParametro;
	private Button botonActualizarDatos;
	private Button botonActualizarPortadas;
	private Button botonActualizarSoftware;
	private Button botonActualizarTodo;

	private Button botonDescargarPdf;
	private Button botonDescargarSQL;
	private Button botonNormalizarDB;
	private Button botonGuardarListaComics;
	private Button botonEliminarImportadoListaComic;

	private Rectangle barraCambioAltura;

	private Label alarmaConexionInternet;
	private Label labelEditor;
	private Label labelKeyComic;
	private Label labelNombre;
	private Label labelIdMod;
	private Label labelPortada;
	private Label labelGradeo;
	private Label labelReferencia;
	private Label prontInfoLabel;
	private Label alarmaConexionSql;
	private Label labelComprobar;
	private Label labelVersion;
	private Label labelArtista;
	private Label labelGuionista;
	private Label labelVariante;
	private Label labelfechaG;
	private Label prontInfoEspecial;
	private Label prontInfoPreviews;
	private Label prontInfoPortadas;
	private Label labelAnio;
	private Label labelCodigo;

	private TextField busquedaCodigoTextField;
	private TextField tituloComicTextField;
	private TextField codigoComicTextField;
	private TextField nombreEditorTextField;
	private TextField artistaComicTextField;
	private TextField guionistaComicTextField;
	private TextField varianteTextField;
	private TextField idComicTratarTextField;
	private TextField direccionImagenTextField;
	private TextField urlReferenciaTextField;
	private TextField numeroComicTextField;
	private TextField precioComicTextField;
	private TextField firmaComicTextField;

	private TextField codigoComicTratarTextField;
	private TextField busquedaGeneralTextField;

	private ComboBox<String> tituloComicCombobox;
	private ComboBox<String> numeroComicCombobox;
	private ComboBox<String> nombreTiendaCombobox;
	private ComboBox<String> nombreEditorCombobox;
	private ComboBox<String> nombreFirmaCombobox;
	private ComboBox<String> nombreArtistaCombobox;
	private ComboBox<String> nombreGuionistaCombobox;
	private ComboBox<String> nombreVarianteCombobox;
	private ComboBox<String> comboPreviewsCombobox;
	private ComboBox<String> comboPreviews;
	
	private DatePicker dataPickFechaP;

	private TextArea prontInfoTextArea;
	private TextArea keyComicData;

	private MenuItem menuImportarFicheroCodigoBarras;
	private MenuItem menuComicAniadir;
	private MenuItem menuComicModificar;
	private MenuItem menuEstadisticaEstadistica;
	private MenuItem menuArchivoCerrar;
	private MenuItem menuArchivoDelete;
	private MenuItem menuArchivoDesconectar;
	private MenuItem menuArchivoExcel;
	private MenuItem menuArchivoImportar;
	private MenuItem menuArchivoSobreMi;
	private MenuItem menuEstadisticaSumaTotal;
	private MenuItem menuArchivoAvanzado;

	private Menu navegacionCerrar;
	private Menu navegacionComic;
	private Menu navegacionEstadistica;

	private MenuBar menuNavegacion;

	private ProgressIndicator progresoCarga;

	private CheckBox checkFirmas;

	private List<Control> controlAccion;

	private static List<ComboBox<String>> listaComboboxes;
	private static List<TableColumn<Comic, String>> listaColumnasTabla;
	private static ObservableList<Control> listaTextFields;
	private static ObservableList<Button> listaBotones;
	private static ObservableList<Node> listaElementosFondo;

	private Stage stageVentana;

	/**
	 * @return the iDColumna
	 */
	public TableColumn<Comic, String> getiDColumna() {
		return iDColumna;
	}

	/**
	 * @return the tituloColumna
	 */
	public TableColumn<Comic, String> getTituloColumna() {
		return tituloColumna;
	}

	/**
	 * @return the numeroColumna
	 */
	public TableColumn<Comic, String> getNumeroColumna() {
		return numeroColumna;
	}

	/**
	 * @return the editorColumna
	 */
	public TableColumn<Comic, String> getEditorColumna() {
		return editorColumna;
	}

	/**
	 * @return the firmaColumna
	 */
	public TableColumn<Comic, String> getFirmaColumna() {
		return firmaColumna;
	}

	/**
	 * @return the artistaColumna
	 */
	public TableColumn<Comic, String> getArtistaColumna() {
		return artistaColumna;
	}

	/**
	 * @return the guionistaColumna
	 */
	public TableColumn<Comic, String> getGuionistaColumna() {
		return guionistaColumna;
	}

	/**
	 * @return the varianteColumna
	 */
	public TableColumn<Comic, String> getVarianteColumna() {
		return varianteColumna;
	}

	/**
	 * @return the precioColumna
	 */
	public TableColumn<Comic, String> getPrecioColumna() {
		return precioColumna;
	}

	/**
	 * @return the referenciaColumna
	 */
	public TableColumn<Comic, String> getReferenciaColumna() {
		return referenciaColumna;
	}

	/**
	 * @return the tablaBBDD
	 */
	public TableView<Comic> getTablaBBDD() {
		return tablaBBDD;
	}

	/**
	 * @return the rootVBox
	 */
	public VBox getRootVBox() {
		return rootVBox;
	}

	/**
	 * @return the vboxContenido
	 */
	public VBox getVboxContenido() {
		return vboxContenido;
	}

	/**
	 * @return the vboxImage
	 */
	public VBox getVboxImage() {
		return vboxImage;
	}

	/**
	 * @return the rootAnchorPane
	 */
	public AnchorPane getRootAnchorPane() {
		return rootAnchorPane;
	}

	/**
	 * @return the anchoPaneInfo
	 */
	public AnchorPane getAnchoPaneInfo() {
		return anchoPaneInfo;
	}

	/**
	 * @return the backgroundImage
	 */
	public ImageView getBackgroundImage() {
		return backgroundImage;
	}

	/**
	 * @return the imagenComic
	 */
	public ImageView getImagenComic() {
		return imagenComic;
	}

	/**
	 * @return the cargaImagen
	 */
	public ImageView getCargaImagen() {
		return cargaImagen;
	}

	/**
	 * @return the botonClonarComic
	 */
	public Button getBotonClonarComic() {
		return botonClonarComic;
	}

	/**
	 * @return the botonModificar
	 */
	public Button getBotonModificar() {
		return botonModificar;
	}

	/**
	 * @return the botonIntroducir
	 */
	public Button getBotonIntroducir() {
		return botonIntroducir;
	}

	/**
	 * @return the botonEliminar
	 */
	public Button getBotonEliminar() {
		return botonEliminar;
	}

	/**
	 * @return the botonComprimirPortadas
	 */
	public Button getBotonComprimirPortadas() {
		return botonComprimirPortadas;
	}

	/**
	 * @return the botonReCopiarPortadas
	 */
	public Button getBotonReCopiarPortadas() {
		return botonReCopiarPortadas;
	}

	/**
	 * @return the botonCancelarSubida
	 */
	public Button getBotonCancelarSubida() {
		return botonCancelarSubida;
	}

	/**
	 * @return the botonBusquedaCodigo
	 */
	public Button getBotonBusquedaCodigo() {
		return botonBusquedaCodigo;
	}

	/**
	 * @return the botonBusquedaAvanzada
	 */
	public Button getBotonBusquedaAvanzada() {
		return botonBusquedaAvanzada;
	}

	/**
	 * @return the botonLimpiar
	 */
	public Button getBotonLimpiar() {
		return botonLimpiar;
	}

	/**
	 * @return the botonModificarComic
	 */
	public Button getBotonModificarComic() {
		return botonModificarComic;
	}

	/**
	 * @return the botonIntroducirComic
	 */
	public Button getBotonIntroducirComic() {
		return botonIntroducirComic;
	}

	/**
	 * @return the botonParametroComic
	 */
	public Button getBotonParametroComic() {
		return botonParametroComic;
	}

	/**
	 * @return the botonbbdd
	 */
	public Button getBotonbbdd() {
		return botonbbdd;
	}

	/**
	 * @return the botonGuardarComic
	 */
	public Button getBotonGuardarComic() {
		return botonGuardarComic;
	}

	/**
	 * @return the botonGuardarCambioComic
	 */
	public Button getBotonGuardarCambioComic() {
		return botonGuardarCambioComic;
	}

	/**
	 * @return the botonEliminarImportadoComic
	 */
	public Button getBotonEliminarImportadoComic() {
		return botonEliminarImportadoComic;
	}

	/**
	 * @return the botonSubidaPortada
	 */
	public Button getBotonSubidaPortada() {
		return botonSubidaPortada;
	}

	/**
	 * @return the botonMostrarParametro
	 */
	public Button getBotonMostrarParametro() {
		return botonMostrarParametro;
	}

	/**
	 * @return the botonActualizarDatos
	 */
	public Button getBotonActualizarDatos() {
		return botonActualizarDatos;
	}

	/**
	 * @return the botonActualizarPortadas
	 */
	public Button getBotonActualizarPortadas() {
		return botonActualizarPortadas;
	}

	/**
	 * @return the botonActualizarSoftware
	 */
	public Button getBotonActualizarSoftware() {
		return botonActualizarSoftware;
	}

	/**
	 * @return the botonActualizarTodo
	 */
	public Button getBotonActualizarTodo() {
		return botonActualizarTodo;
	}

	/**
	 * @return the botonDescargarPdf
	 */
	public Button getBotonDescargarPdf() {
		return botonDescargarPdf;
	}

	/**
	 * @return the botonDescargarSQL
	 */
	public Button getBotonDescargarSQL() {
		return botonDescargarSQL;
	}

	/**
	 * @return the botonNormalizarDB
	 */
	public Button getBotonNormalizarDB() {
		return botonNormalizarDB;
	}

	/**
	 * @return the botonGuardarListaComics
	 */
	public Button getBotonGuardarListaComics() {
		return botonGuardarListaComics;
	}

	/**
	 * @return the botonEliminarImportadoListaComic
	 */
	public Button getBotonEliminarImportadoListaComic() {
		return botonEliminarImportadoListaComic;
	}

	/**
	 * @return the barraCambioAltura
	 */
	public Rectangle getBarraCambioAltura() {
		return barraCambioAltura;
	}

	/**
	 * @return the alarmaConexionInternet
	 */
	public Label getAlarmaConexionInternet() {
		return alarmaConexionInternet;
	}

	/**
	 * @return the labelEditor
	 */
	public Label getLabelEditor() {
		return labelEditor;
	}

	/**
	 * @return the labelKeyComic
	 */
	public Label getLabelKeyComic() {
		return labelKeyComic;
	}

	/**
	 * @return the labelNombre
	 */
	public Label getLabelNombre() {
		return labelNombre;
	}

	/**
	 * @return the labelIdMod
	 */
	public Label getLabelIdMod() {
		return labelIdMod;
	}

	/**
	 * @return the labelPortada
	 */
	public Label getLabelPortada() {
		return labelPortada;
	}

	/**
	 * @return the labelGradeo
	 */
	public Label getLabelGradeo() {
		return labelGradeo;
	}

	/**
	 * @return the labelReferencia
	 */
	public Label getLabelReferencia() {
		return labelReferencia;
	}

	/**
	 * @return the prontInfoLabel
	 */
	public Label getProntInfoLabel() {
		return prontInfoLabel;
	}

	/**
	 * @return the alarmaConexionSql
	 */
	public Label getAlarmaConexionSql() {
		return alarmaConexionSql;
	}

	/**
	 * @return the labelComprobar
	 */
	public Label getLabelComprobar() {
		return labelComprobar;
	}

	/**
	 * @return the labelVersion
	 */
	public Label getLabelVersion() {
		return labelVersion;
	}

	/**
	 * @return the labelArtista
	 */
	public Label getLabelArtista() {
		return labelArtista;
	}

	/**
	 * @return the labelGuionista
	 */
	public Label getLabelGuionista() {
		return labelGuionista;
	}

	/**
	 * @return the labelVariante
	 */
	public Label getLabelVariante() {
		return labelVariante;
	}

	/**
	 * @return the labelfechaG
	 */
	public Label getLabelfechaG() {
		return labelfechaG;
	}

	/**
	 * @return the prontInfoEspecial
	 */
	public Label getProntInfoEspecial() {
		return prontInfoEspecial;
	}

	/**
	 * @return the prontInfoPreviews
	 */
	public Label getProntInfoPreviews() {
		return prontInfoPreviews;
	}

	/**
	 * @return the prontInfoPortadas
	 */
	public Label getProntInfoPortadas() {
		return prontInfoPortadas;
	}

	/**
	 * @return the labelAnio
	 */
	public Label getLabelAnio() {
		return labelAnio;
	}

	/**
	 * @return the labelCodigo
	 */
	public Label getLabelCodigo() {
		return labelCodigo;
	}

	/**
	 * @return the busquedaCodigoTextField
	 */
	public TextField getBusquedaCodigoTextField() {
		return busquedaCodigoTextField;
	}

	/**
	 * @return the tituloComicTextField
	 */
	public TextField getTituloComicTextField() {
		return tituloComicTextField;
	}

	/**
	 * @return the codigoComicTextField
	 */
	public TextField getCodigoComicTextField() {
		return codigoComicTextField;
	}

	/**
	 * @return the nombreEditorTextField
	 */
	public TextField getNombreEditorTextField() {
		return nombreEditorTextField;
	}

	/**
	 * @return the artistaComicTextField
	 */
	public TextField getArtistaComicTextField() {
		return artistaComicTextField;
	}

	/**
	 * @return the guionistaComicTextField
	 */
	public TextField getGuionistaComicTextField() {
		return guionistaComicTextField;
	}

	/**
	 * @return the varianteTextField
	 */
	public TextField getVarianteTextField() {
		return varianteTextField;
	}

	/**
	 * @return the idComicTratarTextField
	 */
	public TextField getIdComicTratarTextField() {
		return idComicTratarTextField;
	}

	/**
	 * @return the direccionImagenTextField
	 */
	public TextField getDireccionImagenTextField() {
		return direccionImagenTextField;
	}

	/**
	 * @return the urlReferenciaTextField
	 */
	public TextField getUrlReferenciaTextField() {
		return urlReferenciaTextField;
	}

	/**
	 * @return the numeroComicTextField
	 */
	public TextField getNumeroComicTextField() {
		return numeroComicTextField;
	}

	/**
	 * @return the precioComicTextField
	 */
	public TextField getPrecioComicTextField() {
		return precioComicTextField;
	}

	/**
	 * @return the firmaComicTextField
	 */
	public TextField getFirmaComicTextField() {
		return firmaComicTextField;
	}

	/**
	 * @return the codigoComicTratarTextField
	 */
	public TextField getCodigoComicTratarTextField() {
		return codigoComicTratarTextField;
	}

	/**
	 * @return the busquedaGeneralTextField
	 */
	public TextField getBusquedaGeneralTextField() {
		return busquedaGeneralTextField;
	}

	/**
	 * @return the tituloComicCombobox
	 */
	public ComboBox<String> getTituloComicCombobox() {
		return tituloComicCombobox;
	}

	/**
	 * @return the numeroComicCombobox
	 */
	public ComboBox<String> getNumeroComicCombobox() {
		return numeroComicCombobox;
	}

	/**
	 * @return the nombreTiendaCombobox
	 */
	public ComboBox<String> getNombreTiendaCombobox() {
		return nombreTiendaCombobox;
	}

	/**
	 * @return the nombreEditorCombobox
	 */
	public ComboBox<String> getNombreEditorCombobox() {
		return nombreEditorCombobox;
	}

	/**
	 * @return the nombreFirmaCombobox
	 */
	public ComboBox<String> getNombreFirmaCombobox() {
		return nombreFirmaCombobox;
	}

	/**
	 * @return the nombreArtistaCombobox
	 */
	public ComboBox<String> getNombreArtistaCombobox() {
		return nombreArtistaCombobox;
	}

	/**
	 * @return the nombreGuionistaCombobox
	 */
	public ComboBox<String> getNombreGuionistaCombobox() {
		return nombreGuionistaCombobox;
	}

	/**
	 * @return the nombreVarianteCombobox
	 */
	public ComboBox<String> getNombreVarianteCombobox() {
		return nombreVarianteCombobox;
	}

	/**
	 * @return the comboPreviewsCombobox
	 */
	public ComboBox<String> getComboPreviewsCombobox() {
		return comboPreviewsCombobox;
	}

	/**
	 * @return the comboPreviews
	 */
	public ComboBox<String> getComboPreviews() {
		return comboPreviews;
	}

	/**
	 * @return the dataPickFechaP
	 */
	public DatePicker getDataPickFechaP() {
		return dataPickFechaP;
	}

	/**
	 * @return the prontInfoTextArea
	 */
	public TextArea getProntInfoTextArea() {
		return prontInfoTextArea;
	}

	/**
	 * @return the keyComicData
	 */
	public TextArea getKeyComicData() {
		return keyComicData;
	}

	/**
	 * @return the menuImportarFicheroCodigoBarras
	 */
	public MenuItem getMenuImportarFicheroCodigoBarras() {
		return menuImportarFicheroCodigoBarras;
	}

	/**
	 * @return the menuComicAniadir
	 */
	public MenuItem getMenuComicAniadir() {
		return menuComicAniadir;
	}

	/**
	 * @return the menuComicModificar
	 */
	public MenuItem getMenuComicModificar() {
		return menuComicModificar;
	}

	/**
	 * @return the menuEstadisticaEstadistica
	 */
	public MenuItem getMenuEstadisticaEstadistica() {
		return menuEstadisticaEstadistica;
	}

	/**
	 * @return the menuArchivoCerrar
	 */
	public MenuItem getMenuArchivoCerrar() {
		return menuArchivoCerrar;
	}

	/**
	 * @return the menuArchivoDelete
	 */
	public MenuItem getMenuArchivoDelete() {
		return menuArchivoDelete;
	}

	/**
	 * @return the menuArchivoDesconectar
	 */
	public MenuItem getMenuArchivoDesconectar() {
		return menuArchivoDesconectar;
	}

	/**
	 * @return the menuArchivoExcel
	 */
	public MenuItem getMenuArchivoExcel() {
		return menuArchivoExcel;
	}

	/**
	 * @return the menuArchivoImportar
	 */
	public MenuItem getMenuArchivoImportar() {
		return menuArchivoImportar;
	}

	/**
	 * @return the menuArchivoSobreMi
	 */
	public MenuItem getMenuArchivoSobreMi() {
		return menuArchivoSobreMi;
	}

	/**
	 * @return the menuEstadisticaSumaTotal
	 */
	public MenuItem getMenuEstadisticaSumaTotal() {
		return menuEstadisticaSumaTotal;
	}

	/**
	 * @return the menuArchivoAvanzado
	 */
	public MenuItem getMenuArchivoAvanzado() {
		return menuArchivoAvanzado;
	}

	/**
	 * @return the navegacionCerrar
	 */
	public Menu getNavegacionCerrar() {
		return navegacionCerrar;
	}

	/**
	 * @return the navegacionComic
	 */
	public Menu getNavegacionComic() {
		return navegacionComic;
	}

	/**
	 * @return the navegacionEstadistica
	 */
	public Menu getNavegacionEstadistica() {
		return navegacionEstadistica;
	}

	/**
	 * @return the menuNavegacion
	 */
	public MenuBar getMenuNavegacion() {
		return menuNavegacion;
	}

	/**
	 * @return the progresoCarga
	 */
	public ProgressIndicator getProgresoCarga() {
		return progresoCarga;
	}

	/**
	 * @return the checkFirmas
	 */
	public CheckBox getCheckFirmas() {
		return checkFirmas;
	}

	/**
	 * @return the controlAccion
	 */
	public List<Control> getControlAccion() {
		return controlAccion;
	}

	/**
	 * @return the listaComboboxes
	 */
	public static List<ComboBox<String>> getListaComboboxes() {
		return listaComboboxes;
	}

	/**
	 * @return the listaColumnasTabla
	 */
	public static List<TableColumn<Comic, String>> getListaColumnasTabla() {
		return listaColumnasTabla;
	}

	/**
	 * @return the listaTextFields
	 */
	public static ObservableList<Control> getListaTextFields() {
		return listaTextFields;
	}

	/**
	 * @return the listaBotones
	 */
	public static ObservableList<Button> getListaBotones() {
		return listaBotones;
	}

	/**
	 * @return the listaElementosFondo
	 */
	public static ObservableList<Node> getListaElementosFondo() {
		return listaElementosFondo;
	}

	/**
	 * @return the stageVentana
	 */
	public Stage getStageVentana() {
		return stageVentana;
	}

	/**
	 * @param iDColumna the iDColumna to set
	 */
	public void setiDColumna(TableColumn<Comic, String> iDColumna) {
		this.iDColumna = iDColumna;
	}

	/**
	 * @param tituloColumna the tituloColumna to set
	 */
	public void setTituloColumna(TableColumn<Comic, String> tituloColumna) {
		this.tituloColumna = tituloColumna;
	}

	/**
	 * @param numeroColumna the numeroColumna to set
	 */
	public void setNumeroColumna(TableColumn<Comic, String> numeroColumna) {
		this.numeroColumna = numeroColumna;
	}

	/**
	 * @param editorColumna the editorColumna to set
	 */
	public void setEditorColumna(TableColumn<Comic, String> editorColumna) {
		this.editorColumna = editorColumna;
	}

	/**
	 * @param firmaColumna the firmaColumna to set
	 */
	public void setFirmaColumna(TableColumn<Comic, String> firmaColumna) {
		this.firmaColumna = firmaColumna;
	}

	/**
	 * @param artistaColumna the artistaColumna to set
	 */
	public void setArtistaColumna(TableColumn<Comic, String> artistaColumna) {
		this.artistaColumna = artistaColumna;
	}

	/**
	 * @param guionistaColumna the guionistaColumna to set
	 */
	public void setGuionistaColumna(TableColumn<Comic, String> guionistaColumna) {
		this.guionistaColumna = guionistaColumna;
	}

	/**
	 * @param varianteColumna the varianteColumna to set
	 */
	public void setVarianteColumna(TableColumn<Comic, String> varianteColumna) {
		this.varianteColumna = varianteColumna;
	}

	/**
	 * @param precioColumna the precioColumna to set
	 */
	public void setPrecioColumna(TableColumn<Comic, String> precioColumna) {
		this.precioColumna = precioColumna;
	}

	/**
	 * @param referenciaColumna the referenciaColumna to set
	 */
	public void setReferenciaColumna(TableColumn<Comic, String> referenciaColumna) {
		this.referenciaColumna = referenciaColumna;
	}

	/**
	 * @param tablaBBDD the tablaBBDD to set
	 */
	public void setTablaBBDD(TableView<Comic> tablaBBDD) {
		this.tablaBBDD = tablaBBDD;
	}

	/**
	 * @param rootVBox the rootVBox to set
	 */
	public void setRootVBox(VBox rootVBox) {
		this.rootVBox = rootVBox;
	}

	/**
	 * @param vboxContenido the vboxContenido to set
	 */
	public void setVboxContenido(VBox vboxContenido) {
		this.vboxContenido = vboxContenido;
	}

	/**
	 * @param vboxImage the vboxImage to set
	 */
	public void setVboxImage(VBox vboxImage) {
		this.vboxImage = vboxImage;
	}

	/**
	 * @param rootAnchorPane the rootAnchorPane to set
	 */
	public void setRootAnchorPane(AnchorPane rootAnchorPane) {
		this.rootAnchorPane = rootAnchorPane;
	}

	/**
	 * @param anchoPaneInfo the anchoPaneInfo to set
	 */
	public void setAnchoPaneInfo(AnchorPane anchoPaneInfo) {
		this.anchoPaneInfo = anchoPaneInfo;
	}

	/**
	 * @param backgroundImage the backgroundImage to set
	 */
	public void setBackgroundImage(ImageView backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	/**
	 * @param imagenComic the imagenComic to set
	 */
	public void setImagenComic(ImageView imagenComic) {
		this.imagenComic = imagenComic;
	}

	/**
	 * @param cargaImagen the cargaImagen to set
	 */
	public void setCargaImagen(ImageView cargaImagen) {
		this.cargaImagen = cargaImagen;
	}

	/**
	 * @param botonClonarComic the botonClonarComic to set
	 */
	public void setBotonClonarComic(Button botonClonarComic) {
		this.botonClonarComic = botonClonarComic;
	}

	/**
	 * @param botonModificar the botonModificar to set
	 */
	public void setBotonModificar(Button botonModificar) {
		this.botonModificar = botonModificar;
	}

	/**
	 * @param botonIntroducir the botonIntroducir to set
	 */
	public void setBotonIntroducir(Button botonIntroducir) {
		this.botonIntroducir = botonIntroducir;
	}

	/**
	 * @param botonEliminar the botonEliminar to set
	 */
	public void setBotonEliminar(Button botonEliminar) {
		this.botonEliminar = botonEliminar;
	}

	/**
	 * @param botonComprimirPortadas the botonComprimirPortadas to set
	 */
	public void setBotonComprimirPortadas(Button botonComprimirPortadas) {
		this.botonComprimirPortadas = botonComprimirPortadas;
	}

	/**
	 * @param botonReCopiarPortadas the botonReCopiarPortadas to set
	 */
	public void setBotonReCopiarPortadas(Button botonReCopiarPortadas) {
		this.botonReCopiarPortadas = botonReCopiarPortadas;
	}

	/**
	 * @param botonCancelarSubida the botonCancelarSubida to set
	 */
	public void setBotonCancelarSubida(Button botonCancelarSubida) {
		this.botonCancelarSubida = botonCancelarSubida;
	}

	/**
	 * @param botonBusquedaCodigo the botonBusquedaCodigo to set
	 */
	public void setBotonBusquedaCodigo(Button botonBusquedaCodigo) {
		this.botonBusquedaCodigo = botonBusquedaCodigo;
	}

	/**
	 * @param botonBusquedaAvanzada the botonBusquedaAvanzada to set
	 */
	public void setBotonBusquedaAvanzada(Button botonBusquedaAvanzada) {
		this.botonBusquedaAvanzada = botonBusquedaAvanzada;
	}

	/**
	 * @param botonLimpiar the botonLimpiar to set
	 */
	public void setBotonLimpiar(Button botonLimpiar) {
		this.botonLimpiar = botonLimpiar;
	}

	/**
	 * @param botonModificarComic the botonModificarComic to set
	 */
	public void setBotonModificarComic(Button botonModificarComic) {
		this.botonModificarComic = botonModificarComic;
	}

	/**
	 * @param botonIntroducirComic the botonIntroducirComic to set
	 */
	public void setBotonIntroducirComic(Button botonIntroducirComic) {
		this.botonIntroducirComic = botonIntroducirComic;
	}

	/**
	 * @param botonParametroComic the botonParametroComic to set
	 */
	public void setBotonParametroComic(Button botonParametroComic) {
		this.botonParametroComic = botonParametroComic;
	}

	/**
	 * @param botonbbdd the botonbbdd to set
	 */
	public void setBotonbbdd(Button botonbbdd) {
		this.botonbbdd = botonbbdd;
	}

	/**
	 * @param botonGuardarComic the botonGuardarComic to set
	 */
	public void setBotonGuardarComic(Button botonGuardarComic) {
		this.botonGuardarComic = botonGuardarComic;
	}

	/**
	 * @param botonGuardarCambioComic the botonGuardarCambioComic to set
	 */
	public void setBotonGuardarCambioComic(Button botonGuardarCambioComic) {
		this.botonGuardarCambioComic = botonGuardarCambioComic;
	}

	/**
	 * @param botonEliminarImportadoComic the botonEliminarImportadoComic to set
	 */
	public void setBotonEliminarImportadoComic(Button botonEliminarImportadoComic) {
		this.botonEliminarImportadoComic = botonEliminarImportadoComic;
	}

	/**
	 * @param botonSubidaPortada the botonSubidaPortada to set
	 */
	public void setBotonSubidaPortada(Button botonSubidaPortada) {
		this.botonSubidaPortada = botonSubidaPortada;
	}

	/**
	 * @param botonMostrarParametro the botonMostrarParametro to set
	 */
	public void setBotonMostrarParametro(Button botonMostrarParametro) {
		this.botonMostrarParametro = botonMostrarParametro;
	}

	/**
	 * @param botonActualizarDatos the botonActualizarDatos to set
	 */
	public void setBotonActualizarDatos(Button botonActualizarDatos) {
		this.botonActualizarDatos = botonActualizarDatos;
	}

	/**
	 * @param botonActualizarPortadas the botonActualizarPortadas to set
	 */
	public void setBotonActualizarPortadas(Button botonActualizarPortadas) {
		this.botonActualizarPortadas = botonActualizarPortadas;
	}

	/**
	 * @param botonActualizarSoftware the botonActualizarSoftware to set
	 */
	public void setBotonActualizarSoftware(Button botonActualizarSoftware) {
		this.botonActualizarSoftware = botonActualizarSoftware;
	}

	/**
	 * @param botonActualizarTodo the botonActualizarTodo to set
	 */
	public void setBotonActualizarTodo(Button botonActualizarTodo) {
		this.botonActualizarTodo = botonActualizarTodo;
	}

	/**
	 * @param botonDescargarPdf the botonDescargarPdf to set
	 */
	public void setBotonDescargarPdf(Button botonDescargarPdf) {
		this.botonDescargarPdf = botonDescargarPdf;
	}

	/**
	 * @param botonDescargarSQL the botonDescargarSQL to set
	 */
	public void setBotonDescargarSQL(Button botonDescargarSQL) {
		this.botonDescargarSQL = botonDescargarSQL;
	}

	/**
	 * @param botonNormalizarDB the botonNormalizarDB to set
	 */
	public void setBotonNormalizarDB(Button botonNormalizarDB) {
		this.botonNormalizarDB = botonNormalizarDB;
	}

	/**
	 * @param botonGuardarListaComics the botonGuardarListaComics to set
	 */
	public void setBotonGuardarListaComics(Button botonGuardarListaComics) {
		this.botonGuardarListaComics = botonGuardarListaComics;
	}

	/**
	 * @param botonEliminarImportadoListaComic the botonEliminarImportadoListaComic to set
	 */
	public void setBotonEliminarImportadoListaComic(Button botonEliminarImportadoListaComic) {
		this.botonEliminarImportadoListaComic = botonEliminarImportadoListaComic;
	}

	/**
	 * @param barraCambioAltura the barraCambioAltura to set
	 */
	public void setBarraCambioAltura(Rectangle barraCambioAltura) {
		this.barraCambioAltura = barraCambioAltura;
	}

	/**
	 * @param alarmaConexionInternet the alarmaConexionInternet to set
	 */
	public void setAlarmaConexionInternet(Label alarmaConexionInternet) {
		this.alarmaConexionInternet = alarmaConexionInternet;
	}

	/**
	 * @param labelEditor the labelEditor to set
	 */
	public void setLabelEditor(Label labelEditor) {
		this.labelEditor = labelEditor;
	}

	/**
	 * @param labelKeyComic the labelKeyComic to set
	 */
	public void setLabelKeyComic(Label labelKeyComic) {
		this.labelKeyComic = labelKeyComic;
	}

	/**
	 * @param labelNombre the labelNombre to set
	 */
	public void setLabelNombre(Label labelNombre) {
		this.labelNombre = labelNombre;
	}

	/**
	 * @param labelIdMod the labelIdMod to set
	 */
	public void setLabelIdMod(Label labelIdMod) {
		this.labelIdMod = labelIdMod;
	}

	/**
	 * @param labelPortada the labelPortada to set
	 */
	public void setLabelPortada(Label labelPortada) {
		this.labelPortada = labelPortada;
	}

	/**
	 * @param labelGradeo the labelGradeo to set
	 */
	public void setLabelGradeo(Label labelGradeo) {
		this.labelGradeo = labelGradeo;
	}

	/**
	 * @param labelReferencia the labelReferencia to set
	 */
	public void setLabelReferencia(Label labelReferencia) {
		this.labelReferencia = labelReferencia;
	}

	/**
	 * @param prontInfoLabel the prontInfoLabel to set
	 */
	public void setProntInfoLabel(Label prontInfoLabel) {
		this.prontInfoLabel = prontInfoLabel;
	}

	/**
	 * @param alarmaConexionSql the alarmaConexionSql to set
	 */
	public void setAlarmaConexionSql(Label alarmaConexionSql) {
		this.alarmaConexionSql = alarmaConexionSql;
	}

	/**
	 * @param labelComprobar the labelComprobar to set
	 */
	public void setLabelComprobar(Label labelComprobar) {
		this.labelComprobar = labelComprobar;
	}

	/**
	 * @param labelVersion the labelVersion to set
	 */
	public void setLabelVersion(Label labelVersion) {
		this.labelVersion = labelVersion;
	}

	/**
	 * @param labelArtista the labelArtista to set
	 */
	public void setLabelArtista(Label labelArtista) {
		this.labelArtista = labelArtista;
	}

	/**
	 * @param labelGuionista the labelGuionista to set
	 */
	public void setLabelGuionista(Label labelGuionista) {
		this.labelGuionista = labelGuionista;
	}

	/**
	 * @param labelVariante the labelVariante to set
	 */
	public void setLabelVariante(Label labelVariante) {
		this.labelVariante = labelVariante;
	}

	/**
	 * @param labelfechaG the labelfechaG to set
	 */
	public void setLabelfechaG(Label labelfechaG) {
		this.labelfechaG = labelfechaG;
	}

	/**
	 * @param prontInfoEspecial the prontInfoEspecial to set
	 */
	public void setProntInfoEspecial(Label prontInfoEspecial) {
		this.prontInfoEspecial = prontInfoEspecial;
	}

	/**
	 * @param prontInfoPreviews the prontInfoPreviews to set
	 */
	public void setProntInfoPreviews(Label prontInfoPreviews) {
		this.prontInfoPreviews = prontInfoPreviews;
	}

	/**
	 * @param prontInfoPortadas the prontInfoPortadas to set
	 */
	public void setProntInfoPortadas(Label prontInfoPortadas) {
		this.prontInfoPortadas = prontInfoPortadas;
	}

	/**
	 * @param labelAnio the labelAnio to set
	 */
	public void setLabelAnio(Label labelAnio) {
		this.labelAnio = labelAnio;
	}

	/**
	 * @param labelCodigo the labelCodigo to set
	 */
	public void setLabelCodigo(Label labelCodigo) {
		this.labelCodigo = labelCodigo;
	}

	/**
	 * @param busquedaCodigoTextField the busquedaCodigoTextField to set
	 */
	public void setBusquedaCodigoTextField(TextField busquedaCodigoTextField) {
		this.busquedaCodigoTextField = busquedaCodigoTextField;
	}

	/**
	 * @param tituloComicTextField the tituloComicTextField to set
	 */
	public void setTituloComicTextField(TextField tituloComicTextField) {
		this.tituloComicTextField = tituloComicTextField;
	}

	/**
	 * @param codigoComicTextField the codigoComicTextField to set
	 */
	public void setCodigoComicTextField(TextField codigoComicTextField) {
		this.codigoComicTextField = codigoComicTextField;
	}

	/**
	 * @param nombreEditorTextField the nombreEditorTextField to set
	 */
	public void setNombreEditorTextField(TextField nombreEditorTextField) {
		this.nombreEditorTextField = nombreEditorTextField;
	}

	/**
	 * @param artistaComicTextField the artistaComicTextField to set
	 */
	public void setArtistaComicTextField(TextField artistaComicTextField) {
		this.artistaComicTextField = artistaComicTextField;
	}

	/**
	 * @param guionistaComicTextField the guionistaComicTextField to set
	 */
	public void setGuionistaComicTextField(TextField guionistaComicTextField) {
		this.guionistaComicTextField = guionistaComicTextField;
	}

	/**
	 * @param varianteTextField the varianteTextField to set
	 */
	public void setVarianteTextField(TextField varianteTextField) {
		this.varianteTextField = varianteTextField;
	}

	/**
	 * @param idComicTratarTextField the idComicTratarTextField to set
	 */
	public void setIdComicTratarTextField(TextField idComicTratarTextField) {
		this.idComicTratarTextField = idComicTratarTextField;
	}

	/**
	 * @param direccionImagenTextField the direccionImagenTextField to set
	 */
	public void setDireccionImagenTextField(TextField direccionImagenTextField) {
		this.direccionImagenTextField = direccionImagenTextField;
	}

	/**
	 * @param urlReferenciaTextField the urlReferenciaTextField to set
	 */
	public void setUrlReferenciaTextField(TextField urlReferenciaTextField) {
		this.urlReferenciaTextField = urlReferenciaTextField;
	}

	/**
	 * @param numeroComicTextField the numeroComicTextField to set
	 */
	public void setNumeroComicTextField(TextField numeroComicTextField) {
		this.numeroComicTextField = numeroComicTextField;
	}

	/**
	 * @param precioComicTextField the precioComicTextField to set
	 */
	public void setPrecioComicTextField(TextField precioComicTextField) {
		this.precioComicTextField = precioComicTextField;
	}

	/**
	 * @param firmaComicTextField the firmaComicTextField to set
	 */
	public void setFirmaComicTextField(TextField firmaComicTextField) {
		this.firmaComicTextField = firmaComicTextField;
	}

	/**
	 * @param codigoComicTratarTextField the codigoComicTratarTextField to set
	 */
	public void setCodigoComicTratarTextField(TextField codigoComicTratarTextField) {
		this.codigoComicTratarTextField = codigoComicTratarTextField;
	}

	/**
	 * @param busquedaGeneralTextField the busquedaGeneralTextField to set
	 */
	public void setBusquedaGeneralTextField(TextField busquedaGeneralTextField) {
		this.busquedaGeneralTextField = busquedaGeneralTextField;
	}

	/**
	 * @param tituloComicCombobox the tituloComicCombobox to set
	 */
	public void setTituloComicCombobox(ComboBox<String> tituloComicCombobox) {
		this.tituloComicCombobox = tituloComicCombobox;
	}

	/**
	 * @param numeroComicCombobox the numeroComicCombobox to set
	 */
	public void setNumeroComicCombobox(ComboBox<String> numeroComicCombobox) {
		this.numeroComicCombobox = numeroComicCombobox;
	}

	/**
	 * @param nombreTiendaCombobox the nombreTiendaCombobox to set
	 */
	public void setNombreTiendaCombobox(ComboBox<String> nombreTiendaCombobox) {
		this.nombreTiendaCombobox = nombreTiendaCombobox;
	}

	/**
	 * @param nombreEditorCombobox the nombreEditorCombobox to set
	 */
	public void setNombreEditorCombobox(ComboBox<String> nombreEditorCombobox) {
		this.nombreEditorCombobox = nombreEditorCombobox;
	}

	/**
	 * @param nombreFirmaCombobox the nombreFirmaCombobox to set
	 */
	public void setNombreFirmaCombobox(ComboBox<String> nombreFirmaCombobox) {
		this.nombreFirmaCombobox = nombreFirmaCombobox;
	}

	/**
	 * @param nombreArtistaCombobox the nombreArtistaCombobox to set
	 */
	public void setNombreArtistaCombobox(ComboBox<String> nombreArtistaCombobox) {
		this.nombreArtistaCombobox = nombreArtistaCombobox;
	}

	/**
	 * @param nombreGuionistaCombobox the nombreGuionistaCombobox to set
	 */
	public void setNombreGuionistaCombobox(ComboBox<String> nombreGuionistaCombobox) {
		this.nombreGuionistaCombobox = nombreGuionistaCombobox;
	}

	/**
	 * @param nombreVarianteCombobox the nombreVarianteCombobox to set
	 */
	public void setNombreVarianteCombobox(ComboBox<String> nombreVarianteCombobox) {
		this.nombreVarianteCombobox = nombreVarianteCombobox;
	}

	/**
	 * @param comboPreviewsCombobox the comboPreviewsCombobox to set
	 */
	public void setComboPreviewsCombobox(ComboBox<String> comboPreviewsCombobox) {
		this.comboPreviewsCombobox = comboPreviewsCombobox;
	}

	/**
	 * @param comboPreviews the comboPreviews to set
	 */
	public void setComboPreviews(ComboBox<String> comboPreviews) {
		this.comboPreviews = comboPreviews;
	}

	/**
	 * @param dataPickFechaP the dataPickFechaP to set
	 */
	public void setDataPickFechaP(DatePicker dataPickFechaP) {
		this.dataPickFechaP = dataPickFechaP;
	}

	/**
	 * @param prontInfoTextArea the prontInfoTextArea to set
	 */
	public void setProntInfoTextArea(TextArea prontInfoTextArea) {
		this.prontInfoTextArea = prontInfoTextArea;
	}

	/**
	 * @param keyComicData the keyComicData to set
	 */
	public void setKeyComicData(TextArea keyComicData) {
		this.keyComicData = keyComicData;
	}

	/**
	 * @param menuImportarFicheroCodigoBarras the menuImportarFicheroCodigoBarras to set
	 */
	public void setMenuImportarFicheroCodigoBarras(MenuItem menuImportarFicheroCodigoBarras) {
		this.menuImportarFicheroCodigoBarras = menuImportarFicheroCodigoBarras;
	}

	/**
	 * @param menuComicAniadir the menuComicAniadir to set
	 */
	public void setMenuComicAniadir(MenuItem menuComicAniadir) {
		this.menuComicAniadir = menuComicAniadir;
	}

	/**
	 * @param menuComicModificar the menuComicModificar to set
	 */
	public void setMenuComicModificar(MenuItem menuComicModificar) {
		this.menuComicModificar = menuComicModificar;
	}

	/**
	 * @param menuEstadisticaEstadistica the menuEstadisticaEstadistica to set
	 */
	public void setMenuEstadisticaEstadistica(MenuItem menuEstadisticaEstadistica) {
		this.menuEstadisticaEstadistica = menuEstadisticaEstadistica;
	}

	/**
	 * @param menuArchivoCerrar the menuArchivoCerrar to set
	 */
	public void setMenuArchivoCerrar(MenuItem menuArchivoCerrar) {
		this.menuArchivoCerrar = menuArchivoCerrar;
	}

	/**
	 * @param menuArchivoDelete the menuArchivoDelete to set
	 */
	public void setMenuArchivoDelete(MenuItem menuArchivoDelete) {
		this.menuArchivoDelete = menuArchivoDelete;
	}

	/**
	 * @param menuArchivoDesconectar the menuArchivoDesconectar to set
	 */
	public void setMenuArchivoDesconectar(MenuItem menuArchivoDesconectar) {
		this.menuArchivoDesconectar = menuArchivoDesconectar;
	}

	/**
	 * @param menuArchivoExcel the menuArchivoExcel to set
	 */
	public void setMenuArchivoExcel(MenuItem menuArchivoExcel) {
		this.menuArchivoExcel = menuArchivoExcel;
	}

	/**
	 * @param menuArchivoImportar the menuArchivoImportar to set
	 */
	public void setMenuArchivoImportar(MenuItem menuArchivoImportar) {
		this.menuArchivoImportar = menuArchivoImportar;
	}

	/**
	 * @param menuArchivoSobreMi the menuArchivoSobreMi to set
	 */
	public void setMenuArchivoSobreMi(MenuItem menuArchivoSobreMi) {
		this.menuArchivoSobreMi = menuArchivoSobreMi;
	}

	/**
	 * @param menuEstadisticaSumaTotal the menuEstadisticaSumaTotal to set
	 */
	public void setMenuEstadisticaSumaTotal(MenuItem menuEstadisticaSumaTotal) {
		this.menuEstadisticaSumaTotal = menuEstadisticaSumaTotal;
	}

	/**
	 * @param menuArchivoAvanzado the menuArchivoAvanzado to set
	 */
	public void setMenuArchivoAvanzado(MenuItem menuArchivoAvanzado) {
		this.menuArchivoAvanzado = menuArchivoAvanzado;
	}

	/**
	 * @param navegacionCerrar the navegacionCerrar to set
	 */
	public void setNavegacionCerrar(Menu navegacionCerrar) {
		this.navegacionCerrar = navegacionCerrar;
	}

	/**
	 * @param navegacionComic the navegacionComic to set
	 */
	public void setNavegacionComic(Menu navegacionComic) {
		this.navegacionComic = navegacionComic;
	}

	/**
	 * @param navegacionEstadistica the navegacionEstadistica to set
	 */
	public void setNavegacionEstadistica(Menu navegacionEstadistica) {
		this.navegacionEstadistica = navegacionEstadistica;
	}

	/**
	 * @param menuNavegacion the menuNavegacion to set
	 */
	public void setMenuNavegacion(MenuBar menuNavegacion) {
		this.menuNavegacion = menuNavegacion;
	}

	/**
	 * @param progresoCarga the progresoCarga to set
	 */
	public void setProgresoCarga(ProgressIndicator progresoCarga) {
		this.progresoCarga = progresoCarga;
	}

	/**
	 * @param checkFirmas the checkFirmas to set
	 */
	public void setCheckFirmas(CheckBox checkFirmas) {
		this.checkFirmas = checkFirmas;
	}

	/**
	 * @param controlAccion the controlAccion to set
	 */
	public void setControlAccion(List<Control> controlAccion) {
		this.controlAccion = controlAccion;
	}

	/**
	 * @param listaComboboxes the listaComboboxes to set
	 */
	public static void setListaComboboxes(List<ComboBox<String>> listaComboboxes) {
		AccionReferencias.listaComboboxes = listaComboboxes;
	}

	/**
	 * @param listaColumnasTabla the listaColumnasTabla to set
	 */
	public static void setListaColumnasTabla(List<TableColumn<Comic, String>> listaColumnasTabla) {
		AccionReferencias.listaColumnasTabla = listaColumnasTabla;
	}

	/**
	 * @param listaTextFields the listaTextFields to set
	 */
	public static void setListaTextFields(ObservableList<Control> listaTextFields) {
		AccionReferencias.listaTextFields = listaTextFields;
	}

	/**
	 * @param listaBotones the listaBotones to set
	 */
	public static void setListaBotones(ObservableList<Button> listaBotones) {
		AccionReferencias.listaBotones = listaBotones;
	}

	/**
	 * @param listaElementosFondo the listaElementosFondo to set
	 */
	public static void setListaElementosFondo(ObservableList<Node> listaElementosFondo) {
		AccionReferencias.listaElementosFondo = listaElementosFondo;
	}

	/**
	 * @param stageVentana the stageVentana to set
	 */
	public void setStageVentana(Stage stageVentana) {
		this.stageVentana = stageVentana;
	}
}