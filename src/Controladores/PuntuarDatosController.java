package Controladores;

import java.io.IOException;

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
 *  Esta clase permite poder puntuar los comics que estan en la base de datos.
 *
 *  Version 4.1
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

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
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class PuntuarDatosController implements Initializable {

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
	private Button botonBorrarOpinion;

	@FXML
	private Button botonLeidos;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonAgregarPuntuacion;

	@FXML
	private Button botonSalir;

	@FXML
	private DatePicker fechaPublicacion;

	@FXML
	private TextField numeroID;

	@FXML
	private TextField nombreComic;

	@FXML
	private TextField nombreDibujante;

	@FXML
	private TextField nombreEditorial;

	@FXML
	private TextField nombreFirma;

	@FXML
	private TextField nombreGuionista;

	@FXML
	private TextField nombreVariante;

	@FXML
	private TextField numeroComic;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextField idPuntuar;

	@FXML
	private TextField numeroCaja;

	@FXML
	private TextArea pantallaInformativa;

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
	private ComboBox<String> procedenciaParametro;

	@FXML
	private ComboBox<String> puntuacionMenu;

	@FXML
	private ComboBox<String> nombreFormato;

	@FXML
	public TableView<Comic> tablaBBDD;

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

		ObservableList<String> puntuaciones = FXCollections.observableArrayList("0/0", "0.5/5", "1/5", "1.5/5", "2/5",
				"2.5/5", "3/5", "3.5/5", "4/5", "4.5/5", "5/5");
		puntuacionMenu.setItems(puntuaciones);
		puntuacionMenu.getSelectionModel().selectFirst();

		ObservableList<String> procedenciaEstado = FXCollections.observableArrayList("Spain", "USA", "Japon", "Italia",
				"Francia");
		procedenciaParametro.setItems(procedenciaEstado);
		procedenciaParametro.getSelectionModel().selectFirst();

		ObservableList<String> formatoActual = FXCollections.observableArrayList("Grapa", "Tapa dura", "Tapa blanda",
				"Manga", "Libro");
		nombreFormato.setItems(formatoActual);
		nombreFormato.getSelectionModel().selectFirst();

		libreria = new DBLibreriaManager();
		TextFields.bindAutoCompletion(nombreComic, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(nombreVariante, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(nombreFirma, DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(nombreEditorial, DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(nombreGuionista, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(nombreDibujante, DBLibreriaManager.listaDibujante);

		TextFormatter<Integer> textFormatterAni = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});
		numeroID.setTextFormatter(textFormatterAni);

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

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaActual() {

		String procedenciaEstado = procedenciaParametro.getSelectionModel().getSelectedItem().toString(); // Toma el
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
	public String formatoActual() {

		String formatoEstado = nombreFormato.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
		// "formato"
		return formatoEstado;
	}

	/**
	 * Funcion que permite mostrar la imagen de portada cuando clickeas en una
	 * tabla.
	 *
	 * @param event
	 * @throws IOException 
	 */
	@FXML
	void clickRaton(MouseEvent event) throws IOException {
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
	
	@FXML
	void teclasDireccion(KeyEvent event) throws IOException {
	    if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
	        libreria = new DBLibreriaManager();
	        libreria.libreriaCompleta();
	        utilidad = new Utilidades();
	        String ID;

	        Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

	        if (idRow != null) {
	            ID = idRow.getID();
	            pantallaInformativa.setOpacity(1);
	            pantallaInformativa.setText(libreria.comicDatos(ID).toString().replace("[", "").replace("]", ""));

	            imagencomic.setImage(libreria.selectorImage(ID));
	            utilidad.deleteImage();
	        }
	        DBManager.resetConnection();
	    }
	}

	/**
	 * Funcion que permite que al pulsar el boton 'botonOpinion' se modifique el
	 * dato "puntuacion" de un comic en concreto usando su ID"
	 *
	 * @param event
	 * @throws IOException 
	 */
	@FXML
	void agregarPuntuacion(ActionEvent event) throws IOException {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String ID = idPuntuar.getText();
		libreria.actualizarPuntuacion(ID, comicPuntuacion()); // Llamada a funcion
		datosOpinion("Opinion introducida con exito: ");
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
	}

	/**
	 * Funcion que permite borrar la opinion de un comic
	 *
	 * @param event
	 * @throws IOException 
	 */
	@FXML
	void borrarPuntuacion(ActionEvent event) throws IOException {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String ID = idPuntuar.getText();
		libreria.borrarPuntuacion(ID);
		datosOpinion("Opinion borrada con exito: ");
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
	}

	/*
	 * Muestra en TextArea un mensaje sobre puntuacion del comic introducido
	 */
	public void datosOpinion(String pantallaInfo) {
	    pantallaInformativa.setOpacity(1);
	    pantallaInformativa.setStyle("-fx-background-color: #A0F52D");

	    String listaComicsTexto = DBLibreriaManager.listaComics.toString();
	    int indexComa = listaComicsTexto.indexOf(",");
	    String listaComicsSinComa = listaComicsTexto.substring(0, indexComa).replace("[", "");

	    pantallaInformativa.setText(pantallaInfo + listaComicsSinComa);
	}


	/**
	 * Funcion que permite modificar la puntuacion de un comic, siempre y cuando el
	 * ID exista en la base de datos
	 *
	 * @param ps
	 * @return
	 */
	public String comicPuntuacion() {

		String puntuacion = puntuacionMenu.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
																								// "puntuacion"
		return puntuacion;
	}

	/**
	 * Limpia los diferentes datos que se ven en pantalla
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {

		numeroID.setText("");
		nombreComic.setText("");
		numeroComic.setText("");
		nombreVariante.setText("");
		nombreFirma.setText("");
		nombreEditorial.setText("");
		fechaPublicacion.setValue(null);
		nombreDibujante.setText("");
		nombreGuionista.setText("");
		busquedaGeneral.setText("");
		numeroCaja.setText("");
		busquedaGeneral.setText("");

		idPuntuar.setText("");

		idPuntuar.setStyle(null);

		pantallaInformativa.setText(null);

		pantallaInformativa.setOpacity(0);

		tablaBBDD.getItems().clear();

		imagencomic.setImage(null);
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
	 * @throws IOException 
	 */
	@FXML
	void verTodabbdd(ActionEvent event) throws IOException {
		imagencomic.setImage(null);
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion

	}

	/**
	 * Funcion que permite, que a la hora de pulsar el boton 'botonLeidos' se
	 * muestren aquellos comics que tengan una puntuacion
	 *
	 * @param event
	 */
	@FXML
	void verComicsLeidos(ActionEvent event) {
		imagencomic.setImage(null);
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaPuntuacion());
	}

	/**
	 * Obtiene los datos de los comics de la base de datos y los devuelve en el
	 * textView
	 *
	 * @param listaComic
	 */
	@SuppressWarnings("unchecked")
	public void tablaBBDD(List<Comic> listaComic) {
		tablaBBDD.getColumns().setAll(ID, nombre, caja, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante,puntuacion);
		tablaBBDD.getItems().setAll(listaComic);
	}

	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public String[] camposComic() {
		String campos[] = new String[12];

		campos[0] = numeroID.getText();

		campos[1] = nombreComic.getText();

		campos[2] = numeroComic.getText();

		campos[3] = nombreVariante.getText();

		campos[4] = nombreFirma.getText();

		campos[5] = nombreEditorial.getText();

		campos[6] = formatoActual();

		campos[7] = procedenciaActual();

		LocalDate fecha = fechaPublicacion.getValue();
		campos[8] = (fecha != null) ? fecha.toString() : "";

		campos[9] = nombreGuionista.getText();

		campos[10] = nombreDibujante.getText();

		campos[11] = numeroCaja.getText();

		return campos;
	}

	/**
	 * Funcion que comprueba segun los datos escritos en los textArea, que comic
	 * estas buscando.
	 */
	public void listaPorParametro() {
		String datosComic[] = camposComic();

		Comic comic = new Comic(datosComic[0], datosComic[1], datosComic[11], datosComic[2], datosComic[3],
				datosComic[4], datosComic[5], datosComic[6], datosComic[7], datosComic[8], datosComic[9],
				datosComic[10], "", "", null);

		tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()));
		busquedaGeneral.setText("");
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
		nav.verAccesoBBDD();
		DBManager.close();

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
