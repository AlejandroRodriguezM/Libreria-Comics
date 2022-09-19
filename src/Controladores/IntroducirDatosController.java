package Controladores;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase permite acceder a la ventana de introducir datos a la base de datos.
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import Funcionamiento.Comic;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Esta clase sirve para introducir datos a la base de datos
 *
 * @author Alejandro Rodriguez
 */
public class IntroducirDatosController implements Initializable {

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonIntroducir;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Button botonbbdd;

	@FXML
	private Button botonNuevaPortada;

	@FXML
	private TextArea pantallaInformativa;

	@FXML
	private TextField fechaPublicacion;

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
	private TextField nombreFormato;

	@FXML
	private TextField nombreGuionista;

	@FXML
	private TextField nombreProcedencia;

	@FXML
	private TextField nombreVariante;

	@FXML
	private TextField numeroComic;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextField nombreAni;

	@FXML
	private TextField numeroAni;

	@FXML
	private TextField varianteAni;

	@FXML
	private TextField firmaAni;

	@FXML
	private TextField editorialAni;

	@FXML
	private TextField formatoAni;

	@FXML
	private TextField procedenciaAni;

	@FXML
	private TextField fechaAni;

	@FXML
	private TextField guionistaAni;

	@FXML
	private TextField dibujanteAni;

	@FXML
	private TextField direccionImagen;

	@FXML
	private Label idMod;

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
	private ComboBox<String> estadoComic;

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
		estadoComic.getSelectionModel().selectFirst(); // Permite que no exista un valor null, escogiendo el primer
		// valor, que se encuentra vacio, en caso de querer borrar
		// la puntuacion.

		libreria = new DBLibreriaManager();
		TextFields.bindAutoCompletion(nombreComic, libreria.listaNombre());
		TextFields.bindAutoCompletion(nombreVariante, libreria.listaVariante());
		TextFields.bindAutoCompletion(nombreFirma, libreria.listaFirma());
		TextFields.bindAutoCompletion(nombreProcedencia, libreria.listaProcedencia());
		TextFields.bindAutoCompletion(nombreFormato, libreria.listaFormato());
		TextFields.bindAutoCompletion(nombreEditorial, libreria.listaEditorial());
		TextFields.bindAutoCompletion(nombreGuionista, libreria.listaGuionista());
		TextFields.bindAutoCompletion(nombreDibujante, libreria.listaDibujante());
		TextFields.bindAutoCompletion(fechaPublicacion, libreria.listaFecha());
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
	 * Metodo que limpia todos los datos de los textField que hay en pantalla
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
		nombreFormato.setText("");
		nombreProcedencia.setText("");
		fechaPublicacion.setText("");
		nombreDibujante.setText("");
		nombreGuionista.setText("");
		busquedaGeneral.setText("");

		// Campos de datos a modificar

		nombreAni.setText("");

		numeroAni.setText("");

		nombreAni.setText("");

		firmaAni.setText("");

		editorialAni.setText("");

		formatoAni.setText("");

		procedenciaAni.setText("");

		fechaAni.setText("");

		guionistaAni.setText("");

		dibujanteAni.setText("");

		pantallaInformativa.setText(null);

		pantallaInformativa.setOpacity(0);

		tablaBBDD.getItems().clear();

		botonNuevaPortada.setStyle(null);

		imagencomic.setImage(null);
	}

	/**
	 * Metodo que añade datos a la base de datos segun los parametros introducidos
	 * en los textField
	 *
	 * @param event
	 */
	@FXML
	public void agregarDatos(ActionEvent event) {

		libreria = new DBLibreriaManager();
		subidaComic();
		libreria.reiniciarBBDD();
		direccionImagen.setText("");
	}

	/**
	 * Funcion que permite la subida de una
	 *
	 * @param event
	 */
	@FXML
	void nuevaPortada(ActionEvent event) {
		imagencomic.setImage(null);
		subirPortada();

	}

	/**
	 * Permite abir una ventana para abrir ficheros de un determinado formato.
	 *
	 * @return
	 */
	public FileChooser tratarFichero() {
		FileChooser fileChooser = new FileChooser(); // Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("Subiendo imagen", "*.jpg", "*.png", "*.jpeg"));

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
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String estadoActual() {

		String situacionEstado = estadoComic.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
		// "puntuacion"
		return situacionEstado;
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

		Comic comic = new Comic(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7],
				datos[8], datos[9], datos[10], "", "", null);

		tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()));
		busquedaGeneral.setText("");
	}

	/**
	 * funcion que obtiene los datos de los comics de la base de datos y los
	 * devuelve en el textView
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
	 * Permite introducir un comic en la base de datos de forma manual
	 */
	public boolean subidaComic() {
		libreria = new DBLibreriaManager();
		utilidad = new Utilidades();
		String datos[] = camposComicIntroducir();
		if (nav.alertaInsertar()) {

			InputStream portada = utilidad.direccionImagen(datos[10]);
			libreria.insertarDatos(datos);
			Comic comic = new Comic("", datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7],
					datos[8], datos[9], datos[11], "Sin puntuar", portada);

			Image imagen = new Image(portada);
			imagencomic.setImage(imagen);

			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
			pantallaInformativa
					.setText("Has añadido correctamente: " + comic.toString().replace("[", "").replace("]", ""));
			libreria.listasAutoCompletado();
			try {
				portada.close();
				utilidad.deleteImage(datos[10]);
			} catch (IOException e) {
				nav.alertaException(e.toString());
			}
			return true;

		} else {
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("Se ha cancelado la subida del nuevo comic.");
			return false;
		}
	}

	/**
	 * Funcion que devuelve un array con los datos de los TextField correspondientes
	 * a la los comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public String[] camposComicActuales() {
		String campos[] = new String[12];

		campos[0] = numeroID.getText();

		campos[1] = nombreComic.getText();

		campos[2] = numeroComic.getText();

		campos[3] = nombreVariante.getText();

		campos[4] = nombreFirma.getText();

		campos[5] = nombreEditorial.getText();

		campos[6] = nombreFormato.getText();

		campos[7] = nombreProcedencia.getText();

		campos[8] = fechaPublicacion.getText();

		campos[9] = nombreGuionista.getText();

		campos[10] = nombreDibujante.getText();

		return campos;
	}

	/**
	 * Funcion que devuelve un array con los datos de los TextField del comic a
	 * introducir.
	 *
	 * @return
	 */
	public String[] camposComicIntroducir() {

		utilidad = new Utilidades();

		String campos[] = new String[12];

		campos[0] = nombreAni.getText();

		campos[1] = numeroAni.getText();

		campos[2] = varianteAni.getText();

		campos[3] = firmaAni.getText();

		campos[4] = editorialAni.getText();

		campos[5] = formatoAni.getText();

		campos[6] = procedenciaAni.getText();

		campos[7] = fechaAni.getText();

		campos[8] = guionistaAni.getText();

		campos[9] = dibujanteAni.getText();

		campos[10] = direccionImagen.getText();

		campos[11] = estadoActual();

		return utilidad.comaPorGuion(campos);
	}

	/**
	 * Permite dar valor a las celdas de la TableView
	 */
	private void nombreColumnas() {
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

	/**
	 * Permite salir completamente del programa.
	 *
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {

		if (nav.salirPrograma(event)) {
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Al cerrar la ventana, se cargara la ventana de verBBDD
	 *
	 */
	public void closeWindows() {

		nav.verMenuPrincipal();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}
}