package Controladores;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import Funcionamiento.Comic;
import Funcionamiento.FuncionesComicsBBDD;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ModificarDatosController implements Initializable {

	@FXML
	private Button botonModificar;

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
	private Button botonNuevaPortada;

	@FXML
	private TextField anioPublicacion;

	@FXML
	private TextField anioPublicacionMod;

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
	private TextField nombreFormato;

	@FXML
	private TextField nombreFormatoMod;

	@FXML
	private TextField nombreGuionista;

	@FXML
	private TextField nombreGuionistaMod;

	@FXML
	private TextField nombreProcedencia;

	@FXML
	private TextField nombreProcedenciaMod;

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
	private TextArea pantallaInformativa;

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
	private static FuncionesComicsBBDD libreria = null;
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
	}
	
	/**
	 * Llamada a funcion que modifica los datos de 1 comic en la base de datos.
	 *
	 * @param event
	 */
	@FXML
	void modificarDatos(ActionEvent event) {
		libreria = new FuncionesComicsBBDD();
		modificacionComic(); // Llamada a funcion que modificara el contenido de un comic especifico.
		libreria.reiniciarBBDD();
	}

	@FXML
	void nuevaPortada(ActionEvent event) {
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
	 * Limpia todos los datos en pantalla.
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {

		// Campos de busqueda por parametro
		idComic.setText("");

		nombreComic.setText("");

		anioPublicacion.setText("");

		nombreComic.setText("");

		numeroComic.setText("");

		nombreDibujante.setText("");

		nombreEditorial.setText("");

		nombreFirma.setText("");

		nombreFormato.setText("");

		nombreGuionista.setText("");

		nombreProcedencia.setText("");

		nombreVariante.setText("");

		numeroComic.setText("");

		// Campos de datos a modificar
		idComicMod.setText("");

		nombreComicMod.setText("");

		numeroComicMod.setText("");

		nombreVarianteMod.setText("");

		nombreFirmaMod.setText("");

		nombreEditorialMod.setText("");

		nombreFormatoMod.setText("");

		nombreProcedenciaMod.setText("");

		anioPublicacionMod.setText("");

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

	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics se van a modificar
	 *
	 * @return
	 */
	public String[] camposComicModificar() {
		
		Utilidades utilidad = new Utilidades();
		
		String campos[] = new String[13];

		campos[0] = idComicMod.getText();

		campos[1] = nombreComicMod.getText();

		campos[2] = numeroComicMod.getText();

		campos[3] = nombreVarianteMod.getText();

		campos[4] = nombreFirmaMod.getText();

		campos[5] = nombreEditorialMod.getText();

		campos[6] = nombreFormatoMod.getText();

		campos[7] = nombreProcedenciaMod.getText();

		campos[8] = anioPublicacionMod.getText();

		campos[9] = nombreGuionistaMod.getText();

		campos[10] = nombreDibujanteMod.getText();

		campos[11] = direccionImagen.getText();

		campos[12] = estadoActual();

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

		libreria = new FuncionesComicsBBDD();
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

		utilidad = new Utilidades();
		libreria = new FuncionesComicsBBDD();
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(utilidad.libreriaCompleta()); // Llamada a funcion

	}

	/**
	 * Funcion que muestra los comics que coincidan con los parametros introducidos
	 * en los textField
	 *
	 * @return
	 */
	public void listaPorParametro() {
		utilidad = new Utilidades();
		String datos[] = camposComicActuales();

		Comic comic = new Comic(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7],
				datos[8], datos[9], datos[10], "", "", "");

		tablaBBDD(utilidad.busquedaParametro(comic, busquedaGeneral.getText()));
		busquedaGeneral.setText("");
	}
	
	public FileChooser tratarFichero() {
		FileChooser fileChooser = new FileChooser(); // Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters()
		.addAll(new FileChooser.ExtensionFilter("Subiendo imagen", "*.jpg", "*.png", "*.jpeg"));

		return fileChooser;
	}

	/**
	 * 
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
	 *
	 * @param listaComic
	 */
	// Llamada a funcion para comprobar si existe algun dato en la lista.
	public void comprobarLista(List<Comic> listaComic) {
		if (libreria.checkList(listaComic)) {
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("ERROR. No hay ningun dato en la base de datos");
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
		tablaBBDD.getColumns().setAll(ID, nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listaComic);
	}
	
	/**
	 *
	 */
	public boolean modificacionComic() {
		libreria = new FuncionesComicsBBDD();
		utilidad = new Utilidades();
		
		if (nav.alertaModificar()) {
			String datos[] = camposComicModificar();
			libreria.comprobarCambio(datos);

			InputStream portada = utilidad.direccionImagen(direccionImagen.getText());
			Image imagen = new Image(portada);
			imagencomic.setImage(imagen);
			
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
			pantallaInformativa.setText(
					"Has modificado correctamente: " + FuncionesComicsBBDD.listaTratamiento.toString().replace("[", "").replace("]", ""));

			
			return true;
		}
		else
		{
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

	/////////////////////////////////
	//// METODO LLAMADA A VENTANA////
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