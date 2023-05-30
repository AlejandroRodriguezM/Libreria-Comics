package Controladores;

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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

/**
 * Esta clase sirve para almoner datos. No elimna los datos, realiza la funcion
 * de cambiar el dato de "estado" de "En posesion" a "Vendido"
 *
 * @author Alejandro Rodriguez
 */
public class EliminarDatosController implements Initializable {
	
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
    private MenuItem menu_comic_modificar;

    @FXML
    private MenuItem menu_comic_puntuar;

    @FXML
    private MenuBar menu_navegacion;

    @FXML
    private Menu navegacion_cerrar;

    @FXML
    private Menu navegacion_comic;
	
	@FXML
	private Button botonEliminar;

	@FXML
	private Button botonVender;

	@FXML
	private Button botonEnVenta;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonbbdd;

	@FXML
	private TextField idComic;

	@FXML
	private TextField nombreComic;

	@FXML
	private TextField numeroComic;

	@FXML
	private TextField nombreVariante;

	@FXML
	private TextField nombreEditorial;

	@FXML
	private DatePicker anioPublicacion;

	@FXML
	private TextField nombreFirma;

	@FXML
	private TextField nombreGuionista;

	@FXML
	private TextField nombreDibujante;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextArea pantallaInformativa;
	
    @FXML
    private TextField numeroCaja;

	@FXML
	private TextField idComicTratar;

	@FXML
	public TableView<Comic> tablaBBDD;

	@FXML
	private TableColumn<Comic, String> ID;
	
    @FXML
    private TableColumn<Comic, String> caja;

	@FXML
	private TableColumn<Comic, String> numero;

	@FXML
	private TableColumn<Comic, String> procedencia;

	@FXML
	private TableColumn<Comic, String> variante;

	@FXML
	private TableColumn<Comic, String> dibujante;

	@FXML
	private TableColumn<Comic, String> editorial;
	
	@FXML
	private TableColumn<Comic, String> formato;
	
	@FXML
	private TableColumn<Comic, String> fecha;

	@FXML
	private TableColumn<Comic, String> firma;

	@FXML
	private TableColumn<Comic, String> guionista;

	@FXML
	private TableColumn<Comic, String> nombre;
	
	@FXML
	private TableColumn<Comic, String> puntuacion;

	@FXML
	private ImageView imagencomic;
	
	@FXML
	private ComboBox<String> nombreProcedencia;
	
	@FXML
	private ComboBox<String> nombreFormato;
	

	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static Utilidades utilidad = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		libreria = new DBLibreriaManager();
		libreria.listasAutoCompletado();
		ObservableList<String> procedenciaEstadoActual = FXCollections.observableArrayList("Spain", "USA",
				"Japon","Italia","Francia");
		nombreProcedencia.setItems(procedenciaEstadoActual);
		nombreProcedencia.getSelectionModel().selectFirst();
		
		ObservableList<String> formatoActual = FXCollections.observableArrayList("Grapa", "Tapa dura","Tapa blanda",
				"Manga","Libro");
		nombreFormato.setItems(formatoActual);
		nombreFormato.getSelectionModel().selectFirst();
		
		listas_autocompletado();
		
	    TextFormatter<Integer> textFormatterAni = new TextFormatter<>(new IntegerStringConverter(), null,
	            change -> {
	                String newText = change.getControlNewText();
	                if (newText.matches("\\d*")) {
	                    return change;
	                }
	                return null;
	            });
	    idComic.setTextFormatter(textFormatterAni);

	    TextFormatter<Integer> textFormatterComic = new TextFormatter<>(new IntegerStringConverter(), null,
	            change -> {
	                String newText = change.getControlNewText();
	                if (newText.matches("\\d*")) {
	                    return change;
	                }
	                return null;
	            });
	    numeroComic.setTextFormatter(textFormatterComic);
	}
	
	public void listas_autocompletado() {
		TextFields.bindAutoCompletion(nombreComic, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(nombreVariante, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(nombreFirma, DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(nombreEditorial, DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(nombreGuionista, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(nombreDibujante, DBLibreriaManager.listaDibujante);
		DBLibreriaManager.listaNombre.clear();
	}
	
	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaActual() {

		String procedenciaEstadoNuevo = nombreProcedencia.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
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
	 * Funcion que permite limpiar los datos en pantalla
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) { // Metodo que permite limpiar los datos de los diferentes campos
		ID.setText("");
		nombreComic.setText("");
		numeroComic.setText("");
		nombreComic.setText("");
		numeroComic.setText("");
		nombreVariante.setText("");
		nombreFirma.setText("");
		nombreEditorial.setText("");
		anioPublicacion.setValue(null);
		nombreDibujante.setText("");
		nombreGuionista.setText("");
		pantallaInformativa.setText(null);
		pantallaInformativa.setOpacity(0);
		tablaBBDD.getItems().clear();
		idComicTratar.setStyle(null);
		imagencomic.setImage(null);
		numeroCaja.setText("");
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
	 * Funcion que elimina un comic de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	void eliminarDatos(ActionEvent event) {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String ID = idComicTratar.getText();
		modificarDatos(ID);
		libreria.eliminarComicBBDD(ID);
		libreria.reiniciarBBDD();
		libreria.listasAutoCompletado();
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
	}

	/**
	 * Metodo que permite cambiar de estado un comic, para que se deje de mostrar en
	 * el programa, pero este sigue estando dentro de la bbdd
	 *
	 * @param event
	 */
	@FXML
	void ventaComic(ActionEvent event) {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String ID = idComicTratar.getText();
		modificarDatos(ID);
		libreria.venderComicBBDD(ID);
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
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

		String datos[] = camposComics();

		Comic comic = new Comic(datos[0], datos[1],datos[11], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7],
				datos[8], datos[9], datos[10], "", "", null);

		tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()));
		busquedaGeneral.setText("");
	}

	/**
	 * Permite dar valor a las celdas de la TableView
	 */
	private void nombreColumnas() {
		ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		caja.setCellValueFactory(new PropertyValueFactory<>("numCaja"));
		numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		variante.setCellValueFactory(new PropertyValueFactory<>("variante"));
		firma.setCellValueFactory(new PropertyValueFactory<>("firma"));
		editorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
		formato.setCellValueFactory(new PropertyValueFactory<>("formato"));
		procedencia.setCellValueFactory(new PropertyValueFactory<>("procedencia"));
		fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		guionista.setCellValueFactory(new PropertyValueFactory<>("guionista"));
		dibujante.setCellValueFactory(new PropertyValueFactory<>("dibujante"));
		puntuacion.setCellValueFactory(new PropertyValueFactory<>("puntuacion"));
	}

	//////////////////////////
	//// FUNCIONES/////////////
	//////////////////////////

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
	 * Funcion que permite cambiar de estado o eliminar un comic de la base de
	 * datos.
	 *
	 * @param id
	 * @param sentenciaSQL
	 */
	public boolean modificarDatos(String ID) {
		libreria = new DBLibreriaManager();
		if (nav.alertaEliminar()) {
			if (ID.length() != 0) {

				Comic comic = libreria.comicDatos(ID); // Llamada de metodo que contiene el comic que se desea eliminar

				pantallaInformativa.setOpacity(1);
				pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
				pantallaInformativa
						.setText("Has modificado correctamente: " + comic.toString().replace("[", "").replace("]", ""));
				idComicTratar.setStyle(null);
				imagencomic.setImage(libreria.selectorImage(ID));
				return true;
			} else {
				pantallaInformativa.setOpacity(1);
				pantallaInformativa.setStyle("-fx-background-color: #F53636");
				pantallaInformativa.setText("ERROR. ID desconocido.");
				idComicTratar.setStyle("-fx-background-color: red");
				return false;
			}
		} else { // Si se cancela el borra del comic, saltara el siguiente mensaje.
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("Modificacion cancelada.");
			return false;
		}
	}

	/**
	 * Funcion que devuelve un array que contiene los datos de los datos
	 * introducidos por parametro por TextField
	 *
	 * @return
	 */
	public String[] camposComics() {
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

	/////////////////////////////////
	//// METODO LLAMADA A VENTANA//
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
	 * Permite el cambio de ventana a la ventana de ModificarDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaModificar(ActionEvent event) {

		nav.verModificarDatos();

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
