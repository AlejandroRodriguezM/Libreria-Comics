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
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import Funcionamiento.BBDD;
import Funcionamiento.Comic;
import Funcionamiento.ConexionBBDD;
import Funcionamiento.ExcelFuntions;
import Funcionamiento.Libreria;
import Funcionamiento.NavegacionVentanas;
import Funcionamiento.Utilidades;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MenuPrincipalController {

	@FXML
	private Button BotonEliminarComic;

	@FXML
	private Button BotonModificarComic;

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
	private Button botonGuardarFichero;

	@FXML
	private Button BotonVentanaAniadir;

	@FXML
	private Button botonBackupBBDD;

	@FXML
	private Button BotonVentanaEliminar;

	@FXML
	private Button botonVerRecomendacion;

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
	private Button botonCompra;

	@FXML
	private TextField anioPublicacion;

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
	private TableColumn<Comic, String> ID;

	@FXML
	private TableColumn<Comic, String> numero;

	@FXML
	private TableColumn<Comic, String> procedencia;

	@FXML
	private TableColumn<Comic, String> variante;

	@FXML
	public TableView<Comic> tablaBBDD;

	@FXML
	private TextArea prontInfo;

	@FXML
	private TextArea prontFrases;

	private NavegacionVentanas nav = new NavegacionVentanas();

	private Libreria libreria = new Libreria();

	private BBDD db = new BBDD();

	private ExcelFuntions excelFuntions = new ExcelFuntions();

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

		Stage myStage = (Stage) this.BotonVentanaAniadir.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana de EliminarDatosController
	 * @param event
	 */
	@FXML
	public void ventanaEliminar(ActionEvent event) {

		nav.verEliminarDatos();

		Stage myStage = (Stage) this.BotonEliminarComic.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana de ModificarDatosController
	 * @param event
	 */
	@FXML
	public void ventanaModificar(ActionEvent event) {

		nav.verModificarDatos();

		Stage myStage = (Stage) this.BotonModificarComic.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana deRecomendacionesController
	 * @param event
	 */
	@FXML
	void ventanaRecomendar(ActionEvent event) {

		nav.verRecomendacion();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Muestra en un textArea diferentes frases random de personajes de los comics.
	 * @param event
	 */
	@FXML
	void fraseRandom(ActionEvent event) {
		prontFrases.setOpacity(1);
		prontFrases.setText(Comic.frasesComics());
	}

	/**
	 * Muestra la bbdd segun los parametros introducidos en los TextField
	 *
	 * @param event
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) {
		nombreColumnas();
		listaPorParametro();
	}

	/**
	 * Muestra toda la base de datos.
	 *
	 * @param event
	 */
	@FXML
	void verTodabbdd(ActionEvent event) {
		nombreColumnas();
		tablaBBDD(libreriaPosesion());
	}

	////////////////////////////
	/// METODOS PARA EXPORTAR///
	////////////////////////////

	/**
	 * Importa un fichero CSV compatible con el programa para copiar la informacion a la base de datos
	 * @param event
	 */
	@FXML
	void importCSV(ActionEvent event) {

		FileChooser fileChooser = new FileChooser(); //Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichero CSV", "*.csv")); //Permite escoger solamente ficheros cuya extension es CSV
		File fichero = fileChooser.showOpenDialog(null); //Hace que el fileChooser sea solamente para abrir el fichero

		importCSV(fichero);

	}

	/**
	 * Guarda los datos de la base de datos en un fichero txt.
	 *
	 * @param event
	 */
	@FXML
	void exportFichero(ActionEvent event) {
		FileChooser fileChooser = new FileChooser(); //Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichero bloc de notas", "*.txt"));  //Permite escoger solamente ficheros cuya extension es txt
		File fichero = fileChooser.showSaveDialog(null); //Hace que el fileChooser sea solamente para guardar el fichero

		makeFile(fichero);
	}

	/**
	 * Exporta un fichero CSV compatible con el programa que copia el contenido de la base de datos en un fichero CSV
	 * @param event
	 */
	@FXML
	void exportCSV(ActionEvent event) {

		FileChooser fileChooser = new FileChooser(); //Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichero Excel xlsx", "*.xlsx"));  //Permite escoger solamente ficheros cuya extension es .xlsx
		File fichero = fileChooser.showSaveDialog(null);  //Hace que el fileChooser sea solamente para guardar el fichero

		makeExcel(fichero);
	}

	/**
	 * Exporta la base de datos en un fichero SQL
	 * @param event
	 */
	@FXML
	void exportarSQL(ActionEvent event) {

		FileChooser fileChooser = new FileChooser(); //Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichero SQL", "*.sql")); //Permite escoger solamente ficheros cuya extension es .xlsx
		File fichero = fileChooser.showSaveDialog(null); //Hace que el fileChooser sea solamente para guardar el fichero

		makeSQL(fichero);


	}

	/**
	 * Limpia los campos de pantalla donde se escriben los datos.
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
		procedencia.setText("");
		anioPublicacion.setText("");
		nombreDibujante.setText("");
		nombreGuionista.setText("");
		prontInfo.setText(null);
		prontFrases.setText(null);
		prontInfo.setOpacity(0);
		prontFrases.setOpacity(0);
	}

	/**
	 * Borra el contenido de la base de datos, soltamente el contenido de las tablas.
	 * @param event
	 */
	@FXML
	void borrarContenidoTabla(ActionEvent event) {

		if (db.borrarContenidoTabla()) {
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
	 * @param event
	 */
	@FXML
	void verEstadistica(ActionEvent event) {
		prontInfo.setOpacity(1);
		prontInfo.setText(db.procedimientosEstadistica());
	}

	/**
	 * Se llama a funcion que permite abrir 2 direccionesd web junto al navegador predeterminado
	 * @param event
	 */
	@FXML
	void comprarComic(ActionEvent event) {
		verPagina();
	}

	/////////////////////////////////
	//// FUNCIONES////////////////////
	/////////////////////////////////

	/**
	 * Funcion que permite llamar al navegador predeterminado del sistema y abrir 2 paginas web.
	 */
	public void verPagina()
	{
		String url1 = "https://www.radarcomics.com/es/";
		String url2 = "https://www.panini.es/shp_esp_es/comics.html";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url1); //Llamada a funcion
			Utilidades.accesoWebWindows(url2); //Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url1); //Llamada a funcion
				Utilidades.accesoWebLinux(url2); //Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url1);
				Utilidades.accesoWebMac(url2);
			}
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
	//// FUNCIONES CREACION FICHEROS//
	/////////////////////////////////

	/**
	 * Funcion que compruba si se ha creado el fichero Excel y CSV
	 * @param fichero
	 */
	public void makeExcel(File fichero) {
		try {

			if (fichero != null) {
				if (excelFuntions.crearExcel(fichero)) { //Si el fichero XLSX y CSV se han creado se vera el siguiente mensaje
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #A0F52D");
					prontInfo.setText("Fichero excel exportado de forma correcta");
				} else { //Si no se ha podido crear correctamente los ficheros se vera el siguiente mensaje
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #F53636");
					prontInfo.setText("ERROR. No se ha podido exportar correctamente.");
				}
			} else { //En caso de cancelar la creacion de los ficheros, se mostrara el siguiente mensaje.
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("ERROR. Se ha cancelado la exportacion.");
			}
		} catch (Exception e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Funcion que compruba si se ha importado el fichero  CSV
	 * @param fichero
	 */
	public void importCSV(File fichero) {
		try {

			if (fichero != null) {
				if (excelFuntions.importarCSV(fichero)) { //Si se ha importado el fichero CSV correctamente, se vera el siguiente mensaje
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #A0F52D");
					prontInfo.setText("Fichero CSV importado de forma correcta");
				} else { //Si no se ha podido crear importar el fichero se vera el siguiente mensaje
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #F53636");
					prontInfo.setText("ERROR. No se ha podido importar correctamente.");
				}
			} else { //En caso de cancelar la importacion del fichero, se mostrara el siguiente mensaje.
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("ERROR. Se ha cancelado la importacion.");
			}
		} catch (Exception e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Funcion que compruba si se ha creado el fichero txt
	 * @param fichero
	 */
	public void makeFile(File fichero) {

		try {
			if (fichero != null) {
				fichero.createNewFile();

				nombreColumnas();
				tablaBBDD(libreriaPosesion());

				FileWriter guardarDatos = new FileWriter(fichero);

				for (int i = 0; i < libreria.verLibreriaCompleta().length; i++) {
					guardarDatos.write(libreriaCompleta().get(i) + "\n");
					System.out.println(libreriaCompleta().get(i) + "\n");
				}
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo.setText("Fichero creado correctamente");
				guardarDatos.close();
			} else {
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("ERROR. Contenido de la bbdd \n cancelada.");
			}
		} catch (IOException e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 * Funcion crea el fichero SQL segun el sistema operativo en el que te encuentres.
	 * @param fichero
	 */
	public void makeSQL(File fichero) {
		if (fichero != null) {

			if (Utilidades.isWindows()) {
				db.backupWindows(fichero); //Llamada a funcion
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo.setText("Base de datos exportada \ncorrectamente");

			} else {
				if (Utilidades.isUnix()) {
					db.backupLinux(fichero); //Llamada a funcion
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #A0F52D");
					prontInfo.setText("Base de datos exportada \ncorrectamente");
				} 
			}
		}
		else
		{
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("ERROR. Se ha cancelado la exportacion de la base de datos.");
		}
	}


	/**
	 *
	 * @throws SQLException
	 */
	public void listaPorParametro() {
		String datosComic[] = camposComic();

		Comic comic = new Comic(datosComic[0], datosComic[1], datosComic[2], datosComic[3], datosComic[4],
				datosComic[5], datosComic[6], datosComic[7], datosComic[8], datosComic[9], datosComic[10], "");

		tablaBBDD(libreriaParametro(comic));
	}

	/**
	 * Devuelve una lista de los comics cuyos datos han sido introducidos mediante parametros en los textField
	 * @param comic
	 * @return
	 */
	public List<Comic> libreriaParametro(Comic comic) {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.filtadroBBDD(comic));

		if (listComic.size() == 0) {
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("ERROR. No hay ningun dato en la base de datos");
		}
		return listComic;

	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran "En posesion"
	 * @return
	 */
	public List<Comic> libreriaPosesion() {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.verLibreria());

		if (listComic.size() == 0) {
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("ERROR. No hay ningun dato en la base de datos");
		}

		return listComic;
	}

	/** 
	 * Devuelve una lista con todos los comics de la base de datos.
	 * @return
	 */
	public List<Comic> libreriaCompleta() {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.verLibreriaCompleta());

		if (listComic.size() == 0) {
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("ERROR. No hay ningun dato en la base de datos");
		}

		return listComic;
	}

	/**
	 * Obtiene los datos de los comics de la base de datos y los devuelve en el textView
	 * @param listaComic
	 */
	@SuppressWarnings("unchecked")
	public void tablaBBDD(List<Comic> listaComic) {
		tablaBBDD.getColumns().setAll(ID, nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listaComic);
	}

	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los comics que se encuentran en la bbdd
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

		campos[6] = nombreFormato.getText();

		campos[7] = nombreProcedencia.getText();

		campos[8] = anioPublicacion.getText();

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
		ConexionBBDD.close();
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
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {

		nav.verAccesoBBDD();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}
}