package Controladores;

/**
 * Programa que permite el acceso a una base de datos de Comic. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xls
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Añadir Comic a la base de datos.
 *  - Modificar Comic de la base de datos.
 *  - Eliminar Comic de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de Comic
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *  
 *  Esta clase modifica los datos de un comic en concreto
 *  
 *  Version 2.3
 *  
 *  Por Alejandro Rodriguez
 *  
 *  Twitter: @silverAlox
 */

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import Funcionamiento.Comic;
import Funcionamiento.DBManager;
import Funcionamiento.Libreria;
import Funcionamiento.NavegacionVentanas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ModificarDatosController {

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

	private NavegacionVentanas nav = new NavegacionVentanas();
	
	private Libreria libreria = new Libreria();

	private Comic comic = new Comic();

	/**
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
	}

	/**
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void modificarDatos(ActionEvent event) throws SQLException {

		modificacionDatos();
	}

	/////////////////////////////////
	//// FUNCIONES////////////////////
	/////////////////////////////////

	/**
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
	 * 
	 * @return
	 */
	public String[] camposComicModificar() {
		String campos[] = new String[11];

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

		return campos;
	}

	/**
	 * @throws SQLException
	 * 
	 */
	public void modificacionDatos() throws SQLException {

		String nombre, numero, variante, firma, editorial, formato, procedencia, fecha, guionista, dibujante;

		String sentenciaSQL = "UPDATE Comicsbbdd set nomComic = ?,numComic = ?,nomVariante = ?,"
				+ "Firma = ?,nomEditorial = ?,formato = ?,Procedencia = ?,anioPubli = ?,"
				+ "nomGuionista = ?,nomDibujante = ? where ID = ?";

		String datosActuales[] = camposComicActuales();

		String datosModificados[] = camposComicModificar();

		listaPorParametro();

		if (alertaModificacion()) {
			if (datosActuales[0].length() != 0) {
				try {
					PreparedStatement ps = null;

					ps = DBManager.conexion().prepareStatement(sentenciaSQL);

					if (datosModificados[1].length() != 0) {
						ps.setString(1, datosModificados[1]);
						nombre = datosModificados[1];
					} else {
						ps.setString(1, comic.getNombre());
						nombre = comic.getNombre();
					}
					if (datosModificados[2].length() != 0) {
						ps.setString(2, datosModificados[2]);
						numero = datosModificados[2];
					} else {
						ps.setString(2, comic.getNumero());
						numero = comic.getNumero();
					}
					if (datosModificados[3].length() != 0) {
						ps.setString(3, datosModificados[3]);
						variante = datosModificados[3];
					} else {
						ps.setString(3, comic.getVariante());
						variante = comic.getVariante();
					}
					if (datosModificados[4].length() != 0) {
						ps.setString(4, datosModificados[4]);
						firma = datosModificados[4];
					} else {
						ps.setString(4, comic.getFirma());
						firma = comic.getFirma();
					}
					if (datosModificados[5].length() != 0) {
						ps.setString(5, datosModificados[5]);
						editorial = datosModificados[5];
					} else {
						ps.setString(5, comic.getEditorial());
						editorial = comic.getEditorial();
					}
					if (datosModificados[6].length() != 0) {
						ps.setString(6, datosModificados[6]);
						formato = datosModificados[6];
					} else {
						ps.setString(6, comic.getFormato());
						formato = comic.getFormato();
					}
					if (datosModificados[7].length() != 0) {
						ps.setString(7, datosModificados[7]);
						procedencia = datosModificados[7];
					} else {
						ps.setString(7, comic.getProcedencia());
						procedencia = comic.getProcedencia();
					}
					if (datosModificados[8].length() != 0) {
						ps.setString(8, datosModificados[8]);
						fecha = datosModificados[8];
					} else {
						ps.setString(8, comic.getFecha());
						fecha = comic.getFecha();
					}
					if (datosModificados[9].length() != 0) {
						ps.setString(9, datosModificados[9]);
						guionista = datosModificados[9];
					} else {
						ps.setString(9, comic.getGuionista());
						guionista = comic.getGuionista();
					}
					if (datosModificados[10].length() != 0) {
						ps.setString(10, datosModificados[10]);
						dibujante = datosModificados[10];
					} else {
						ps.setString(10, comic.getDibujante());
						dibujante = comic.getDibujante();
					}

					if (datosModificados[0].length() != 0) {
						ps.setString(11, datosModificados[0]);
					} else {
						pantallaInformativa.setStyle("-fx-background-color: #F53636");
						idComicMod.setStyle("-fx-background-color: #F53636");
						idMod.setStyle("-fx-background-color: #F53636");
						pantallaInformativa.setText("ERROR. No ha puesto ningun \nID en la busqueda.");
					}

					if (ps.executeUpdate() == 1) {
						pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
						pantallaInformativa.setText("Ha modificado correctamente: \n" + "\nNombre del comic: " + nombre
								+ "\nNumero: " + numero + "\nPortada variante: " + variante + "\nFirma: " + firma
								+ "\nEditorial: " + editorial + "\nFormato: " + formato + "\nProcedencia: "
								+ procedencia + "\nFecha de publicacion: " + fecha + "\nGuionista: " + guionista
								+ "\nDibujante: " + dibujante);
					}
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			}
		}
	}

	/**
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException {

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
	void verTodabbdd(ActionEvent event) throws SQLException {

		nombreColumnas();
		tablaBBDD(libreriaCompleta());

	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public void listaPorParametro() throws SQLException {
		String datosComic[] = camposComicActuales();

		Comic comic = new Comic(datosComic[0], datosComic[1], datosComic[2], datosComic[3], datosComic[4],
				datosComic[5], datosComic[6], datosComic[7], datosComic[8], datosComic[9], datosComic[10],"");

		tablaBBDD(libreriaParametro(comic));
	}

	/**
	 * 
	 * @param comic
	 * @return
	 * @throws SQLException
	 */
	public List<Comic> libreriaParametro(Comic comic) throws SQLException {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.filtadroBBDD(comic));

		return listComic;
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Comic> libreriaCompleta() throws SQLException {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.verLibreria());

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
	public boolean alertaModificacion() {
		if (nav.alertaModificar()) {
			return true;
		} else {
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("ERROR. Se ha cancelado la modificacion.");
			return false;
		}
	}

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
	//// METODO LLAMADA A VENTANA////
	/////////////////////////////////

	/**
	 * Permite volver al menu de conexion a la base de datos.
	 *
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void volverMenu(ActionEvent event) throws IOException {

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
	 * @throws IOException
	 */
	public void closeWindows() throws IOException {

		nav.verMenuPrincipal();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}
}