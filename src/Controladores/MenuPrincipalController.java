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
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase permite acceder al menu principal donde se puede viajar a diferentes ventanas, etc.
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import Funcionamiento.Comic;
import Funcionamiento.FuncionesExcel;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Esta clase sirve viajar a las diferentes ventanas del programa, asi como
 * realizar diferentes diferentes funciones
 *
 * @author Alejandro Rodriguez
 */
public class MenuPrincipalController implements Initializable {

	@FXML
	private Button botonEliminarComic;

	@FXML
	private Button botonModificarComic;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Button botonbbdd;

	@FXML
	private Button botonVentanaAniadir;

	@FXML
	private Button botonBackupBBDD;

	@FXML
	private Button botonVentanaEliminar;

	@FXML
	private Button botonVerRecomendacion;

	@FXML
	private Button botonVerPuntuar;

	@FXML
	private Button botonFrase;

	@FXML
	private Button botonImportarCSV;

	@FXML
	private Button botonGuardarCSV;

	@FXML
	private Button botonDelete;

	@FXML
	private Button botonEstadistica;

	@FXML
	private Button botonEnVenta;

	@FXML
	private Button botonVendidos;

	@FXML
	private Button botonPuntuacion;

	@FXML
	private Button botonFirmados;

	@FXML
	private TextField fechaPublicacion;
	
	//@FXML
	//private DatePicker fechaPublicacion;

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
	private TableColumn<Comic, String> ID;

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
	private TextArea prontInfo;

	@FXML
	private TextArea prontFrases;

	@FXML
	private ImageView imagencomic;
	
	@FXML
	private ComboBox<String> nombreProcedencia;
	
	@FXML
	private ComboBox<String> nombreFormato;
	


	private static Ventanas nav = new Ventanas();

	private static DBLibreriaManager libreria = null;

	private static Utilidades utilidad = null;

	private static FuncionesExcel excelFuntions = null;

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
		
		TextFields.bindAutoCompletion(nombreComic, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(nombreVariante, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(nombreFirma, DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(nombreEditorial, DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(nombreGuionista, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(nombreDibujante, DBLibreriaManager.listaDibujante);
		TextFields.bindAutoCompletion(fechaPublicacion, DBLibreriaManager.listaFecha);
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
		Stage myStage = (Stage) this.botonVentanaAniadir.getScene().getWindow();
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

		Stage myStage = (Stage) this.botonEliminarComic.getScene().getWindow();
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

		Stage myStage = (Stage) this.botonModificarComic.getScene().getWindow();
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

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}

	/**
	 *
	 * @param event
	 */
	@FXML
	void ventanaPuntuar(ActionEvent event) {

		nav.verPuntuar();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}

	/**
	 * Muestra en un textArea diferentes frases random de personajes de los comics.
	 *
	 * @param event
	 */
	@FXML
	void fraseRandom(ActionEvent event) {
		prontInfo.setOpacity(0);
		prontFrases.setOpacity(1);
		prontFrases.setText(Comic.frasesComics());
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) {
		prontInfo.setOpacity(0);
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
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion

	}

	/**
	 * Funcion que al pulsar el boton de 'botonPuntuacion' se muestran aquellos
	 * comics que tienen una puntuacion
	 *
	 * @param event
	 */
	@FXML
	void comicsPuntuacion(ActionEvent event) {
		prontInfo.setOpacity(0);
		limpiezaDeDatos();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaPuntuacion());

	}

	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 */
	@FXML
	void comicsVendidos(ActionEvent event) {
		prontInfo.setOpacity(0);
		limpiezaDeDatos();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaVendidos());

	}

	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 */
	@FXML
	void comicsFirmados(ActionEvent event) {
		prontInfo.setOpacity(0);
		limpiezaDeDatos();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaFirmados());
	}

	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 */
	@FXML
	void comicsEnVenta(ActionEvent event) {
		prontInfo.setOpacity(0);
		limpiezaDeDatos();
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaEnVenta());
	}

	////////////////////////////
	/// METODOS PARA EXPORTAR///
	////////////////////////////

	/**
	 * Importa un fichero CSV compatible con el programa para copiar la informacion
	 * a la base de datos
	 *
	 * @param event
	 */
	@FXML
	void importCSV(ActionEvent event) {

		String frase = "Fichero CSV";

		String formato = "*.csv";

		File fichero = tratarFichero(frase, formato).showOpenDialog(null); // Llamada a funcion

		importCSV(fichero);
		libreria.listasAutoCompletado();
	}

	/**
	 * Exporta un fichero CSV compatible con el programa que copia el contenido de
	 * la base de datos en un fichero CSV
	 *
	 * @param event
	 */
	@FXML
	void exportCSV(ActionEvent event) {

		String frase = "Fichero Excel xlsx";

		String formato = "*.xlsx";

		File fichero = tratarFichero(frase, formato).showSaveDialog(null); // Llamada a funcion

		makeExcel(fichero);
	}

	/**
	 * Exporta la base de datos en un fichero SQL
	 *
	 * @param event
	 */
	@FXML
	void exportarSQL(ActionEvent event) {

		String frase = "Fichero SQL";

		String formato = "*.sql";

		File fichero = tratarFichero(frase, formato).showSaveDialog(null); // Llamada a funcion

		makeSQL(fichero);

	}

	/**
	 * Funcion que abre una ventana que aceptara los formatos de archivos que le
	 * demos como parametro.
	 *
	 * @param frase
	 * @param formato
	 * @return
	 */
	public FileChooser tratarFichero(String frase, String formato) {
		FileChooser fileChooser = new FileChooser(); // Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(frase, formato));

		return fileChooser;
	}

	/**
	 * Limpia los campos de pantalla donde se escriben los datos.
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		limpiezaDeDatos();
	}

	public void limpiezaDeDatos() {
		numeroID.setText("");
		nombreComic.setText("");
		numeroComic.setText("");
		nombreVariante.setText("");
		nombreFirma.setText("");
		nombreEditorial.setText("");
		fechaPublicacion.setText("");
		nombreDibujante.setText("");
		nombreGuionista.setText("");
		prontFrases.setText(null);
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		prontFrases.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);
	}

	/**
	 * Borra el contenido de la base de datos, soltamente el contenido de las
	 * tablas.
	 *
	 * @param event
	 */
	@FXML
	void borrarContenidoTabla(ActionEvent event) {

		libreria = new DBLibreriaManager();
		prontInfo.setOpacity(0);
		if (nav.borrarContenidoTabla()) {
			libreria.ejecucionPreparedStatement(libreria.deleteTable());
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #A0F52D");
			prontInfo.setText("Has borrado correctamente el contenido de la base de datos.");
			tablaBBDD.getItems().clear();
		} else {
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Has cancelado el borrado de la base de datos.");
		}
	}

	/**
	 * Se llama a funcion que permite ver las estadisticas de la bbdd
	 *
	 * @param event
	 */
	@FXML
	void verEstadistica(ActionEvent event) {
		prontInfo.setOpacity(0);
		libreria = new DBLibreriaManager();
		prontInfo.setOpacity(1);
		prontInfo.setText(libreria.procedimientosEstadistica());
	}

	/////////////////////////////////
	//// FUNCIONES////////////////////
	/////////////////////////////////

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
		puntuacion.setCellValueFactory(new PropertyValueFactory<>("puntuacion"));
	}

	@FXML
	void clickRaton(MouseEvent event) {
		libreria = new DBLibreriaManager();
		libreria.libreriaCompleta();
		utilidad = new Utilidades();
		String ID;

		Comic idRow;

		idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		ID = idRow.getID();
		prontInfo.setOpacity(1);
		prontInfo.setText(libreria.comicDatos(ID).toString().replace("[", "").replace("]", ""));

		imagencomic.setImage(libreria.selectorImage(ID));
		utilidad.deleteImage();

	}

	/////////////////////////////////
	//// FUNCIONES CREACION FICHEROS//
	/////////////////////////////////

	/**
	 * Funcion que compruba si se ha creado el fichero Excel y CSV
	 *
	 * @param fichero
	 */
	public void makeExcel(File fichero) {
		excelFuntions = new FuncionesExcel();
		prontInfo.setOpacity(0);
		try {
			if (fichero != null) {
				if (excelFuntions.crearExcel(fichero)) { // Si el fichero XLSX y CSV se han creado se vera el siguiente
					// mensaje
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #A0F52D");
					prontInfo.setText("Fichero excel exportado de forma correcta");
				} else { // Si no se ha podido crear correctamente los ficheros se vera el siguiente
					// mensaje
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #F53636");
					prontInfo.setText("ERROR. No se ha podido exportar correctamente.");
				}
			} else { // En caso de cancelar la creacion de los ficheros, se mostrara el siguiente
				// mensaje.
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("ERROR. Se ha cancelado la exportacion.");
			}
		} catch (Exception e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Funcion que compruba si se ha importado el fichero CSV
	 *
	 * @param fichero
	 */
	public void importCSV(File fichero) {
		excelFuntions = new FuncionesExcel();
		prontInfo.setOpacity(0);
		try {
			if (fichero != null) {
				if (excelFuntions.importarCSV(fichero)) { // Si se ha importado el fichero CSV correctamente, se vera el
					// siguiente mensaje
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #A0F52D");
					prontInfo.setText("Fichero CSV importado de forma correcta");
				} else { // Si no se ha podido crear importar el fichero se vera el siguiente mensaje
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #F53636");
					prontInfo.setText("ERROR. No se ha podido importar correctamente.");
				}
			} else { // En caso de cancelar la importacion del fichero, se mostrara el siguiente
				// mensaje.
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("ERROR. Se ha cancelado la importacion.");
			}
		} catch (Exception e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Funcion crea el fichero SQL segun el sistema operativo en el que te
	 * encuentres.
	 *
	 * @param fichero
	 */
	public void makeSQL(File fichero) {
		libreria = new DBLibreriaManager();
		prontInfo.setOpacity(0);
		if (fichero != null) {

			if (Utilidades.isWindows()) {
				libreria.backupWindows(fichero); // Llamada a funcion
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo.setText("Base de datos exportada \ncorrectamente");

			} else {
				if (Utilidades.isUnix()) {
					libreria.backupLinux(fichero); // Llamada a funcion
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #A0F52D");
					prontInfo.setText("Base de datos exportada \ncorrectamente");
				}
			}
		} else {
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("ERROR. Se ha cancelado la exportacion de la base de datos.");
		}
	}

	/**
	 * Funcion que comprueba segun los datos escritos en los textArea, que comic
	 * estas buscando.
	 */
	public void listaPorParametro() {
		libreria = new DBLibreriaManager();

		String datos[] = camposComic();

		Comic comic = new Comic(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7],
				datos[8], datos[9], datos[10], "", "", null);

		tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()));
		busquedaGeneral.setText("");
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return
	 */
	public List<Comic> libreriaCompleta() {
		libreria = new DBLibreriaManager();
		limpiezaDeDatos();
		List<Comic> listComic = FXCollections.observableArrayList(libreria.libreriaCompleta());

		return listComic;
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
				guionista, dibujante, puntuacion);
		tablaBBDD.getItems().setAll(listaComic);
	}

	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public String[] camposComic() {
		String campos[] = new String[11];

		campos[0] = numeroID.getText();

		campos[1] = nombreComic.getText();

		campos[2] = numeroComic.getText();

		campos[3] = nombreVariante.getText();

		campos[4] = nombreFirma.getText();

		campos[5] = nombreEditorial.getText();

		campos[6] = formatoActual();

		campos[7] = procedenciaActual();

		campos[8] = fechaPublicacion.getText();

		campos[9] = nombreGuionista.getText();

		campos[10] = nombreDibujante.getText();

		return campos;
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
	public void volverMenu(ActionEvent event) throws IOException {

		nav.verAccesoBBDD();
		DBManager.close();
		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite salir completamente del programa
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
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {

		nav.verAccesoBBDD();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}

}
