package Controladores;

import java.awt.TextArea;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Añadir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase permite acceder a la base de datos introduciendo los diferentes datos que nos pide.
 *
 *  Version 2.5
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import Funcionamiento.BaseDeDatos;
import Funcionamiento.Comic;
import Funcionamiento.DBManager;
import Funcionamiento.Libreria;
import Funcionamiento.NavegacionVentanas;
import Funcionamiento.Utilidades;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	private Label prontInformacion;

    @FXML
    private Label prontFrases;

	private NavegacionVentanas nav = new NavegacionVentanas();

	private Libreria libreria = new Libreria();

	private BaseDeDatos db= new BaseDeDatos();

	//	private DBManager dbmanager = new DBManager();

	//	private Connection conn =dbmanager.conexion();


	/////////////////////////////////
	//// METODOS LLAMADA A VENTANAS//
	/////////////////////////////////

	/**
	 * Permite abrir y cargar la ventana para añadir datos.
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
	 *
	 * @param event
	 */
	@FXML
	public void ventanaEliminar(ActionEvent event) {

		nav.verEliminarDatos();

		Stage myStage = (Stage) this.BotonEliminarComic.getScene().getWindow();
		myStage.close();
	}

	/**
	 *
	 * @param event
	 */
	@FXML
	public void ventanaModificar(ActionEvent event) {

		nav.verModificarDatos();

		Stage myStage = (Stage) this.BotonModificarComic.getScene().getWindow();
		myStage.close();
	}

	/**
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
	void fraseRandom(ActionEvent event) {

		prontFrases.setStyle("-fx-background-color: #D3D3D3");
		prontFrases.setText(Comic.frasesComics());
	}

	/**
	 * Muestra la bbdd segun los parametros introducidos en los TextField
	 *
	 * @param event
	 * @throws SQLException
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
	 * @throws SQLException
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
	 *
	 * @param event
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@FXML
	void importCSV(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichero CSV", "*.csv"));
		File fichero = fileChooser.showOpenDialog(null);

		importCSV(fichero);

	}

	/**
	 * Guarda los datos de la base de datos en un fichero.
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void exportFichero(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichero bloc de notas", "*.txt"));
		File fichero = fileChooser.showSaveDialog(null);

		makeFile(fichero);
	}

	/**
	 *
	 * @param event
	 */
	@FXML
	void exportCSV(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichero Excel xlsx", "*.xlsx"));
		File fichero = fileChooser.showSaveDialog(null);

		makeExcel(fichero);
	}

	/**
	 *
	 * @param event
	 */
	@FXML
	void exportarSQL(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichero SQL", "*.sql"));
		File fichero = fileChooser.showSaveDialog(null);
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
	}
	
    @FXML
    void borrarContenidoTabla(ActionEvent event) {

    	if(db.borrarContenidoTabla())
    	{
    		prontInformacion.setStyle("-fx-background-color: #A0F52D");
			prontInformacion.setText("Has borrado correctamente el contenido de la base de datos.");
    	}
    	else
    	{
    		prontInformacion.setStyle("-fx-background-color: #F53636");
			prontInformacion.setText("Has cancelado el borrado de la base de datos.");
    	}
    }

	/////////////////////////////////
	//// FUNCIONES////////////////////
	/////////////////////////////////


	/**
	 * 
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
	 *
	 * @param fichero
	 */
	public void makeSQL(File fichero) {
		if (fichero != null) {

			if (Utilidades.isWindows()) {
				backupWindows(fichero);
			} else {
				if (Utilidades.isUnix()) {
					backupLinux(fichero);
				} else {

				}
			}
			prontInformacion.setStyle("-fx-background-color: #A0F52D");
			prontInformacion.setText("Base de datos exportada \ncorrectamente");
		} else {
			prontInformacion.setStyle("-fx-background-color: #F53636");
			prontInformacion.setText("ERROR. Base de datos \nexportada cancelada.");
		}
	}

	/**
	 *
	 * @param fichero
	 */
	public void makeExcel(File fichero) {
		try {

			if (fichero != null) {
				if (db.crearExcel(fichero)) {
					prontInformacion.setStyle("-fx-background-color: #A0F52D");
					prontInformacion.setText("Fichero excel exportado de forma correcta");
				} else {
					prontInformacion.setStyle("-fx-background-color: #F53636");
					prontInformacion.setText("ERROR. No se ha podido exportar correctamente.");
				}
			}
			else {
				prontInformacion.setStyle("-fx-background-color: #F53636");
				prontInformacion.setText("ERROR. Se ha cancelado la exportacion.");
			}
		} catch (Exception e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 *
	 * @param fichero
	 */
	public void importCSV(File fichero) {
		try {

			if (fichero != null) {
				if (db.importarCSV(fichero)) {
					prontInformacion.setStyle("-fx-background-color: #A0F52D");
					prontInformacion.setText("Fichero CSV importado de forma correcta");
				} else {
					prontInformacion.setStyle("-fx-background-color: #F53636");
					prontInformacion.setText("ERROR. No se ha podido importar correctamente.");
				}
			}
			else {
				prontInformacion.setStyle("-fx-background-color: #F53636");
				prontInformacion.setText("ERROR. Se ha cancelado la importacion.");
			}
		} catch (Exception e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 *
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
				prontInformacion.setStyle("-fx-background-color: #A0F52D");
				prontInformacion.setText("Fichero creado correctamente");
				guardarDatos.close();
			} else {
				prontInformacion.setStyle("-fx-background-color: #F53636");
				prontInformacion.setText("ERROR. Contenido de la bbdd \n cancelada.");
			}
		} catch (IOException e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 *
	 * @param fichero
	 */
	public void backupLinux(File fichero) {
		try {
			fichero.createNewFile();
			String command[] = new String[] { "mysqldump", "-u" + DBManager.DB_USER, "-p" + DBManager.DB_PASS, "-B",
					DBManager.DB_NAME, "--result-file=" + fichero };
			ProcessBuilder pb = new ProcessBuilder(Arrays.asList(command));
			pb.redirectError(Redirect.INHERIT);
			pb.redirectOutput(Redirect.to(fichero));
			pb.start();

		} catch (IOException e) {
			nav.alertaException(e.toString());
		}
	}

	/**
	 *
	 * @param fichero
	 */
	public void backupWindows(File fichero) {
		try {
			fichero.createNewFile();

			String mysqlDump = "C:/Program Files/MySQL/MySQL Workbench 8.0 CE/mysqldump";

			String command[] = new String[] { mysqlDump, "-u" + DBManager.DB_USER, "-p" + DBManager.DB_PASS, "-B",
					DBManager.DB_NAME, "--result-file=" + fichero };
			ProcessBuilder pb = new ProcessBuilder(Arrays.asList(command));
			pb.redirectError(Redirect.INHERIT);
			pb.redirectOutput(Redirect.to(fichero));
			pb.start();

		} catch (Exception e) {
			nav.alertaException(e.toString());
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
	 *
	 * @param comic
	 * @return
	 * @throws SQLException
	 */
	public List<Comic> libreriaParametro(Comic comic) {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.filtadroBBDD(comic));

		if(listComic.size() == 0)
		{
			prontInformacion.setStyle("-fx-background-color: #F53636");
			prontInformacion.setText("ERROR. No hay ningun dato en la base de datos");
		}
		return listComic;
		
	}

	/**
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<Comic> libreriaPosesion() {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.verLibreria());
		
		if(listComic.size() == 0)
		{
			prontInformacion.setStyle("-fx-background-color: #F53636");
			prontInformacion.setText("ERROR. No hay ningun dato en la base de datos");
		}

		return listComic;
	}

	/**
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<Comic> libreriaCompleta()  {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.verLibreriaCompleta());
		
		if(listComic.size() == 0)
		{
			prontInformacion.setStyle("-fx-background-color: #F53636");
			prontInformacion.setText("ERROR. No hay ningun dato en la base de datos");
		}

		return listComic;
	}

	/**
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
	 * @throws IOException
	 */
	@FXML
	public void volverMenu(ActionEvent event) throws IOException {

		nav.verAccesoBBDD();
		DBManager.close();
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
	 * @throws IOException
	 */
	public void closeWindows() {

		nav.verAccesoBBDD();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}
}
