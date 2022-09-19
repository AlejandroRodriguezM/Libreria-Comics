package Controladores;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import Funcionamiento.Comic;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Esta clase sirve para eliminar datos. No elimna los datos, realiza la funcion
 * de cambiar el dato de "estado" de "En posesion" a "Vendido"
 *
 * @author Alejandro Rodriguez
 */
public class EliminarDatosController implements Initializable {

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
	private Button botonSalir;

	@FXML
	private Button botonVolver;

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
	private TextField anioPublicacion;

	@FXML
	private TextField nombreFirma;

	@FXML
	private TextField nombreFormato;

	@FXML
	private TextField nombreProcedencia;

	@FXML
	private TextField nombreGuionista;

	@FXML
	private TextField nombreDibujante;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextArea pantallaInformativa;

	@FXML
	private TextField idComicTratar;

	@FXML
	public TableView<Comic> tablaBBDD;

	@FXML
	private TableColumn<Comic, String> ID;

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
	private ImageView imagencomic;

	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static Utilidades utilidad = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		libreria = new DBLibreriaManager();
		TextFields.bindAutoCompletion(nombreComic, libreria.listaNombre());
		TextFields.bindAutoCompletion(nombreVariante, libreria.listaVariante());
		TextFields.bindAutoCompletion(nombreFirma, libreria.listaFirma());
		TextFields.bindAutoCompletion(nombreProcedencia, libreria.listaProcedencia());
		TextFields.bindAutoCompletion(nombreFormato, libreria.listaFormato());
		TextFields.bindAutoCompletion(nombreEditorial, libreria.listaEditorial());
		TextFields.bindAutoCompletion(nombreGuionista, libreria.listaGuionista());
		TextFields.bindAutoCompletion(nombreDibujante, libreria.listaDibujante());
		TextFields.bindAutoCompletion(anioPublicacion, libreria.listaFecha());
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
		nombreFormato.setText("");
		nombreProcedencia.setText("");
		anioPublicacion.setText("");
		nombreDibujante.setText("");
		nombreGuionista.setText("");
		pantallaInformativa.setText(null);
		pantallaInformativa.setOpacity(0);
		tablaBBDD.getItems().clear();
		idComicTratar.setStyle(null);
		imagencomic.setImage(null);
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
	}

	/**
	 * Metodo que permite cambiar de estado un comic, para que se deje de mostrar en
	 * el programa, pero este sigue estando dentro de la bbdd
	 *
	 * @param event
	 */
	@FXML
	void ventaDatos(ActionEvent event) {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String ID = idComicTratar.getText();
		modificarDatos(ID);
		libreria.venderComicBBDD(ID);
		libreria.reiniciarBBDD();
	}

	/**
	 * Metodo que permite cambiar de estado un comic, para que se deje de mostrar en
	 * el programa, pero este sigue estando dentro de la bbdd
	 *
	 * @param event
	 */
	@FXML
	void enVentaDatos(ActionEvent event) {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String ID = idComicTratar.getText();
		modificarDatos(ID);
		libreria.enVentaComicBBDD(ID);
		libreria.reiniciarBBDD();
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

		Comic comic = new Comic(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7],
				datos[8], datos[9], datos[10], "", "", null);

		tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()));
		busquedaGeneral.setText("");
	}

	/**
	 * Muestra las columnas especificas del fichero FXML
	 */
	private void nombreColumnas() { // Funcion que especifica el total de columnas de la bbdd se mostraran en la
		// ventana
		ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		variante.setCellValueFactory(new PropertyValueFactory<>("variante"));
		firma.setCellValueFactory(new PropertyValueFactory<>("firma"));
		editorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
		formato.setCellValueFactory(new PropertyValueFactory<>("formato"));
		procedencia.setCellValueFactory(new PropertyValueFactory<>("procedencia"));
		fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		guionista.setCellValueFactory(new PropertyValueFactory<>("guionista"));
		dibujante.setCellValueFactory(new PropertyValueFactory<>("dibujante"));
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
		tablaBBDD.getColumns().setAll(ID, nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
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
		String campos[] = new String[11];

		campos[0] = idComic.getText();

		campos[1] = nombreComic.getText();

		campos[2] = numeroComic.getText();

		campos[3] = nombreVariante.getText();

		campos[4] = nombreFirma.getText();

		campos[5] = nombreEditorial.getText();

		campos[6] = nombreFormato.getText();

		campos[7] = nombreProcedencia.getText();

		campos[8] = anioPublicacion.getText();

		campos[9] = nombreGuionista.getText();

		campos[10] = nombreDibujante.getText();

		return campos;
	}

	/////////////////////////////////
	//// METODO LLAMADA A VENTANA//
	/////////////////////////////////

	/**
	 * Permite volver al menu de conexion a la base de datos.
	 *
	 * @param event
	 */
	@FXML
	void volverMenu(ActionEvent event) {

		nav.verMenuPrincipal();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	/**
	 * Permite salir completamente del programa.
	 *
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) { // Metodo que poermite salir del programa

		if (nav.salirPrograma(event)) { // Llamada a metodo que permite salir del programa
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close(); // Cierra la ventana actual
		}
	}

	/**
	 * Al cerrar la ventana, se cargara la ventana de verBBDD
	 */
	public void closeWindows() { // Metodo que permite cerrar el programa a la fuerza.

		nav.verMenuPrincipal(); // Llamada a metodo que carga y muestra la ventana de MenuPrincipal

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close(); // Cierra la ventana actual

	}

}
