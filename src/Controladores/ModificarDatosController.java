package Controladores;

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
 *  - Eliminar comics de la base de datos o modificar su 'estado' de "En posesion" a "Vendido", estos ultimos no se veran en la busqueda general de la base de datos
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *  - Puntuar comics que se encuentren dentro de la base de datos.
 *
 *  Esta clase permite poder modificar los comics que estan en la base de datos
 *
 *  Version 4.1
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import Funcionamiento.Comic;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class ModificarDatosController implements Initializable {

    @FXML
    private MenuItem menu_archivo_desconectar;
	
    @FXML
    private MenuItem menu_archivo_sobreMi;
	
    @FXML
    private MenuItem menu_archivo_cerrar;

    @FXML
    private MenuItem menu_archivo_volver;

    @FXML
    private MenuItem menu_comic_aleatoria;

    @FXML
    private MenuItem menu_comic_aniadir;

    @FXML
    private MenuItem menu_comic_eliminar;

    @FXML
    private MenuItem menu_comic_modificar;

    @FXML
    private MenuBar menu_navegacion;

    @FXML
    private Menu navegacion_cerrar;

    @FXML
    private Menu navegacion_comic;
	
	@FXML
	private Button botonModificar;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonbbdd;

	@FXML
	private Button botonNuevaPortada;

	@FXML
	private DatePicker anioPublicacion;

	@FXML
	private DatePicker anioPublicacionMod;

	@FXML
	private TextField idComic;

	@FXML
	private TextField idComicMod;

	@FXML
	private TextField nombreComic;

	@FXML
	private TextField nombreComicMod;

	@FXML
	private TextField nombreDibujante;

	@FXML
	private TextField nombreDibujanteMod;

	@FXML
	private TextField nombreEditorial;

	@FXML
	private TextField nombreEditorialMod;

	@FXML
	private TextField nombreFirma;

	@FXML
	private TextField nombreFirmaMod;

	@FXML
	private TextField nombreGuionista;

	@FXML
	private TextField nombreGuionistaMod;

	@FXML
	private TextField nombreVariante;

	@FXML
	private TextField nombreVarianteMod;

	@FXML
	private TextField numeroComic;

	@FXML
	private TextField numeroComicMod;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextField direccionImagen;
	
    @FXML
    private TextField numeroCaja;
    
    @FXML
    private TextField numeroCajaMod;

	@FXML
	private TextArea pantallaInformativa;

	@FXML
	private Label idMod;

	@FXML
	private TableColumn<Comic, String> ID;
	
    @FXML
    private TableColumn<Comic, String> caja;

	@FXML
	private TableColumn<Comic, String> dibujante;

	@FXML
	private TableColumn<Comic, String> editorial;

	@FXML
	private TableColumn<Comic, String> fecha;

	@FXML
	private TableColumn<Comic, String> firma;

	@FXML
	private TableColumn<Comic, String> formato;

	@FXML
	private TableColumn<Comic, String> guionista;

	@FXML
	private TableColumn<Comic, String> nombre;

	@FXML
	private TableColumn<Comic, String> numero;

	@FXML
	private TableColumn<Comic, String> procedencia;

	@FXML
	private TableColumn<Comic, String> variante;

	@FXML
	private TableColumn<Comic, String> puntuacion;

	@FXML
	public TableView<Comic> tablaBBDD;
	@FXML
	private ComboBox<String> nombreProcedencia;

	@FXML
	private ComboBox<String> nombreProcedenciaMod;

	@FXML
	private ComboBox<String> estadoComic;

	@FXML
	private ComboBox<String> nombreFormato;

	@FXML
	private ComboBox<String> nombreFormatoMod;

	@FXML
	private ImageView imagencomic;

	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static Utilidades utilidad = null;

	/**
	 * Funcion que permite hacer funcionar la lista de puntuacion.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> situacionEstado = FXCollections.observableArrayList("En posesion", "Vendido",
				"En venta");
		estadoComic.setItems(situacionEstado);
		estadoComic.getSelectionModel().selectFirst();

		ObservableList<String> procedenciaEstadoMod = FXCollections.observableArrayList("Spain", "USA", "Japon",
				"Italia", "Francia");
		nombreProcedenciaMod.setItems(procedenciaEstadoMod);
		nombreProcedenciaMod.getSelectionModel().selectFirst();

		ObservableList<String> procedenciaEstadoActual = FXCollections.observableArrayList("Spain", "USA", "Japon",
				"Italia", "Francia");
		nombreProcedencia.setItems(procedenciaEstadoActual);
		nombreProcedencia.getSelectionModel().selectFirst();

		ObservableList<String> formatoActual = FXCollections.observableArrayList("Grapa", "Tapa dura", "Tapa blanda",
				"Manga", "Libro");
		nombreFormato.setItems(formatoActual);
		nombreFormato.getSelectionModel().selectFirst();

		ObservableList<String> formatoNuevo = FXCollections.observableArrayList("Grapa", "Tapa dura", "Tapa blanda",
				"Manga", "Libro");
		nombreFormatoMod.setItems(formatoNuevo);
		nombreFormatoMod.getSelectionModel().selectFirst();

		listas_autocompletado();
				TextFormatter<Integer> textFormatterComic = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});
		
		TextFormatter<Integer> textFormatterComic2 = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});
		numeroComic.setTextFormatter(textFormatterComic);
		numeroCaja.setTextFormatter(textFormatterComic2);

	}

	public void listas_autocompletado() {
		libreria = new DBLibreriaManager();
		libreria.listasAutoCompletado();
		TextFields.bindAutoCompletion(nombreComic, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(nombreVariante, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(nombreFirma, DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(nombreEditorial, DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(nombreGuionista, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(nombreDibujante, DBLibreriaManager.listaDibujante);

		TextFields.bindAutoCompletion(nombreComicMod, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(nombreVarianteMod, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(nombreFirmaMod, DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(nombreEditorialMod, DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(nombreGuionistaMod, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(nombreDibujanteMod, DBLibreriaManager.listaDibujante);

		DBLibreriaManager.listaNombre.clear();
	}

	/**
	 * Funcion que permite mostrar la imagen de portada cuando clickeas en una
	 * tabla.
	 *
	 * @param event
	 */
	@FXML
	void clickRaton(MouseEvent event) {
		libreria = new DBLibreriaManager();
		libreria.libreriaCompleta();
		utilidad = new Utilidades();
		String ID;

		Comic idRow;

		idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		ID = idRow.getID();
		pantallaInformativa.setOpacity(1);
		pantallaInformativa.setText(libreria.comicDatos(ID).toString().replace("[", "").replace("]", ""));
		imagencomic.setImage(libreria.selectorImage(ID));
		utilidad.deleteImage();

	}

	/**
	 * Llamada a funcion que modifica los datos de 1 comic en la base de datos.
	 *
	 * @param event
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	@FXML
	void modificarDatos(ActionEvent event) throws NumberFormatException, SQLException, IOException {

		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		modificacionComic(); // Llamada a funcion que modificara el contenido de un comic especifico.
		libreria.reiniciarBBDD();
		direccionImagen.setText("");
		libreria.listasAutoCompletado();
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
	}

	// Funcion que permite seleccionar una imagen para subirla junto al comic
	@FXML
	void nuevaPortada(ActionEvent event) {
		imagencomic.setImage(null);
		subirPortada();
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String estadoActual() {

		String situacionEstado = estadoComic.getSelectionModel().getSelectedItem().toString();
		return situacionEstado;

	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaMod() {

		String procedenciaEstado = nombreProcedenciaMod.getSelectionModel().getSelectedItem().toString(); // Toma el
																											// valor del
																											// menu
		// "puntuacion"
		return procedenciaEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaActual() {

		String procedenciaEstadoNuevo = nombreProcedencia.getSelectionModel().getSelectedItem().toString(); // Toma el
																											// valor del
																											// menu
		// "puntuacion"
		return procedenciaEstadoNuevo;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String formatoActual() {

		String formatoEstado = nombreFormato.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
		// "formato"
		return formatoEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String formatoNuevo() {

		String formatoEstadoNuevo = nombreFormatoMod.getSelectionModel().getSelectedItem().toString(); // Toma el valor
																										// del menu
		// "formato"
		return formatoEstadoNuevo;
	}

	/**
	 * Limpia todos los datos en pantalla.
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {

		// Campos de busqueda por parametro
		idComic.setText("");

		nombreComic.setText("");

		anioPublicacion.setValue(null);

		nombreComic.setText("");

		numeroComic.setText("");

		nombreDibujante.setText("");

		nombreEditorial.setText("");

		nombreFirma.setText("");

		nombreGuionista.setText("");

		nombreVariante.setText("");

		numeroComic.setText("");

		// Campos de datos a modificar
		idComicMod.setText("");

		nombreComicMod.setText("");

		numeroComicMod.setText("");

		nombreVarianteMod.setText("");

		nombreFirmaMod.setText("");

		nombreEditorialMod.setText("");

		anioPublicacionMod.setValue(null);

		nombreGuionistaMod.setText("");

		nombreDibujanteMod.setText("");

		pantallaInformativa.setText(null);

		idComicMod.setStyle(null);

		pantallaInformativa.setOpacity(0);

		tablaBBDD.getItems().clear();

		direccionImagen.setText("");

		imagencomic.setImage(null);
	}

	/////////////////////////////////
	//// FUNCIONES////////////////////
	/////////////////////////////////

	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public String[] camposComicActuales() {
		String campos[] = new String[12];

		campos[0] = idComic.getText();

		campos[1] = nombreComic.getText();

		campos[2] = numeroComic.getText();

		campos[3] = nombreVariante.getText();

		campos[4] = nombreFirma.getText();

		campos[5] = nombreEditorial.getText();

		campos[6] = formatoActual();

		campos[7] = procedenciaActual();

		LocalDate fecha = anioPublicacion.getValue();
		campos[8] = (fecha != null) ? fecha.toString() : "";

		campos[9] = nombreGuionista.getText();

		campos[10] = nombreDibujante.getText();
		
		campos[11] = numeroCaja.getText();

		return campos;
	}

	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics se van a modificar
	 *
	 * @return
	 */
	public String[] camposComicModificar() {

		Utilidades utilidad = new Utilidades();

		String campos[] = new String[14];

		campos[0] = idComicMod.getText();

		campos[1] = nombreComicMod.getText();

		campos[2] = numeroComicMod.getText();

		campos[3] = nombreVarianteMod.getText();

		campos[4] = nombreFirmaMod.getText();

		campos[5] = nombreEditorialMod.getText();

		campos[6] = formatoNuevo();

		campos[7] = procedenciaMod();

		LocalDate fecha = anioPublicacionMod.getValue();
		campos[8] = (fecha != null) ? fecha.toString() : "";

		campos[9] = nombreGuionistaMod.getText();

		campos[10] = nombreDibujanteMod.getText();

		campos[11] = direccionImagen.getText();

		campos[12] = estadoActual();
		
		
		if(numeroCajaMod.getText() != null) {
			campos[13] = "0";
		}else {
			campos[13] = numeroCajaMod.getText();
		}

		return utilidad.comaPorGuion(campos);
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		listaPorParametro(); // Llamada a funcion
		busquedaGeneral.setText("");
	}

	/**
	 * Metodo que muestra toda la base de datos.
	 *
	 * @param event
	 */
	@FXML
	void verTodabbdd(ActionEvent event) {
		imagencomic.setImage(null);
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion

	}

	/**
	 * Funcion que muestra los comics que coincidan con los parametros introducidos
	 * en los textField
	 *
	 * @return
	 */
	public void listaPorParametro() {
		libreria = new DBLibreriaManager();

		String datos[] = camposComicActuales();

		Comic comic = new Comic(datos[0], datos[1],datos[11], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7],
				datos[8], datos[9], datos[10], "", "", null);

		tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()));
		busquedaGeneral.setText("");
	}

	/**
	 * Permite abir una ventana para abrir ficheros de un determinado formato.
	 *
	 * @return
	 */
	public FileChooser tratarFichero() {
		FileChooser fileChooser = new FileChooser(); // Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Subiendo imagen", "*.jpg"));

		return fileChooser;
	}

	/**
	 * Funcion que escribe en el TextField de "Direccion de imagen" la dirrecion de
	 * la imagen
	 */
	public void subirPortada() {
		File file = tratarFichero().showOpenDialog(null); // Llamada a funcion
		if (file != null) {
			direccionImagen.setText(file.getAbsolutePath().toString());
		} else {
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("Has cancelado la subida de portada.");
		}
	}

	/**
	 * Obtiene los datos de los comics de la base de datos y los devuelve en el
	 * textView
	 *
	 * @param listaComic
	 */
	@SuppressWarnings("unchecked")
	public void tablaBBDD(List<Comic> listaComic) {
		tablaBBDD.getColumns().setAll(ID, nombre,caja, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listaComic);
	}

	/**
	 * Funcion que permite modificar un comic, segun los datos introducidos
	 * 
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	public boolean modificacionComic() throws NumberFormatException, SQLException, IOException {
		libreria = new DBLibreriaManager();
		utilidad = new Utilidades();
		File file;
		if (nav.alertaModificar()) {

			String datos[] = camposComicModificar();

			String portada = "";

			String id_comic = datos[0];

			String nombre = datos[1];

			String numero = datos[2];

			String variante = datos[3];

			String firma = datos[4];

			String editorial = datos[5];

			String formato = datos[6];

			String procedencia = datos[7];

			String fecha = datos[8];

			String guionista = datos[9];

			String dibujante = datos[10];
			
			Comic comic_temp = libreria.comicDatos(id_comic);

			if (datos[11] != "") {
				portada = datos[11];

				file = new File(portada);

				if (!file.exists()) {
					Image imagen = new Image(comic_temp.getImagen());
					imagencomic.setImage(imagen);
				} else {
					Image imagen = new Image(portada);
					imagencomic.setImage(imagen);
				}
			}

			String estado = datos[12];
			
			String numCaja = "";
			if(datos[13] != "") {
				numCaja = datos[13];
			}else {
				numCaja = comic_temp.getNumCaja();
			}

			Comic comic = new Comic(id_comic, nombre,numCaja,numero, variante, firma, editorial, formato, procedencia, fecha,
					guionista, dibujante, estado, "Sin puntuar", portada);

			libreria.actualizar_comic(comic);

			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
			pantallaInformativa.setText("Has modificado correctamente: "
					+ comic.toString().replace("[", "").replace("]", ""));
			libreria.listasAutoCompletado();

			return true;
		} else {
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("Se ha cancelado la modificacion del comic.");
			return false;
		}
	}

	/**
	 * Permite dar valor a las celdas de la TableView
	 */
	private void nombreColumnas() {
		ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		nombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
		caja.setCellValueFactory(new PropertyValueFactory<>("numCaja"));
		numero.setCellValueFactory(new PropertyValueFactory<>("Numero"));
		variante.setCellValueFactory(new PropertyValueFactory<>("Variante"));
		firma.setCellValueFactory(new PropertyValueFactory<>("Firma"));
		editorial.setCellValueFactory(new PropertyValueFactory<>("Editorial"));
		formato.setCellValueFactory(new PropertyValueFactory<>("Formato"));
		procedencia.setCellValueFactory(new PropertyValueFactory<>("Procedencia"));
		fecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
		guionista.setCellValueFactory(new PropertyValueFactory<>("Guionista"));
		dibujante.setCellValueFactory(new PropertyValueFactory<>("Dibujante"));
		puntuacion.setCellValueFactory(new PropertyValueFactory<>("Puntuacion"));
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

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana deRecomendacionesController
	 *
	 * @param event
	 */
	@FXML
	void ventanaRecomendar(ActionEvent event) {

		nav.verRecomendacion();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 *
	 * @param event
	 */
	@FXML
	void ventanaPuntuar(ActionEvent event) {

		nav.verPuntuar();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}
	
	/**
	 * Metodo que permite abrir la ventana "sobreMiController"
	 *
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {

		nav.verSobreMi();

	    Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
	    myStage.close();
	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void desconectar(ActionEvent event) throws IOException {
	    DBManager.close();
	    nav.verAccesoBBDD();

	    Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
	    myStage.close();
	}
	
	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void volverMenu(ActionEvent event) throws IOException {
		nav.verMenuPrincipal();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	@FXML
	public void salirPrograma(ActionEvent event) {
		// Logic to handle the "Eliminar" action
		nav.salirPrograma(event);

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {

		Platform.exit();

	}
}