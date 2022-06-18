package Controladores;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xls
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Añadir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
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

import Funcionamiento.Comics;
import Funcionamiento.DBManager;
import Funcionamiento.NavegacionVentanas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	private TextField nombreComic;

	@FXML
	private TextField anioPublicacion;

	@FXML
	private TextField nombreComicMod;

	@FXML
	private TextField numeroComicMod;

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
	private TextField idComic;

	@FXML
	private Label pantallaInformativa;

	@FXML
	public TableView<Comics> tablaBBDD;

	@FXML
	private TableColumn<Comics, String> ID;

	@FXML
	private TableColumn<Comics, String> numero;

	@FXML
	private TableColumn<Comics, String> procedencia;

	@FXML
	private TableColumn<Comics, String> variante;

	@FXML
	private TableColumn<Comics, String> dibujante;

	@FXML
	private TableColumn<Comics, String> editorial;

	@FXML
	private TableColumn<Comics, String> fecha;

	@FXML
	private TableColumn<Comics, String> firma;

	@FXML
	private TableColumn<Comics, String> formato;

	@FXML
	private TableColumn<Comics, String> guionista;

	@FXML
	private TableColumn<Comics, String> nombre;

	NavegacionVentanas nav = new NavegacionVentanas();

	Comics comic = new Comics();

	/**
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {

		nombreComic.setText("");
		anioPublicacion.setText("");
		nombreComicMod.setText("");
		numeroComicMod.setText("");
		nombreDibujante.setText("");
		nombreEditorial.setText("");
		nombreFirma.setText("");
		nombreFormato.setText("");
		nombreGuionista.setText("");
		nombreProcedencia.setText("");
		nombreVariante.setText("");
		numeroComic.setText("");
		idComic.setText("");
	}

	/**
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void modificarDatos(ActionEvent event) {

		modificacionDatos();
	}

	/////////////////////////////////
	//// FUNCIONES////////////////////
	/////////////////////////////////

	public String[] camposComics() {
		String campos[] = new String[11];

		campos[0] = idComic.getText();

		campos[1] = nombreComicMod.getText();

		campos[2] = numeroComicMod.getText();

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
	 */
	public void modificacionDatos() {

		String sentenciaSQL = "UPDATE comicsbbdd set nomComic = ?,numComic = ?,nomVariante = ?,"
				+ "Firma = ?,nomEditorial = ?,formato = ?,Procedencia = ?,anioPubli = ?,"
				+ "nomGuionista = ?,nomDibujante = ? where ID = ?";

		String datos[] = camposComics();
		if (nav.alertaModificar()) {
			if (datos[0].length() != 0) {
				try {
					PreparedStatement ps = null;

					ps = DBManager.conexion().prepareStatement(sentenciaSQL);
					ps.setString(1, datos[1]);
					ps.setString(2, datos[2]);
					if (datos[3].length() != 0) {
						ps.setString(3, datos[3]);
					} else {
						ps.setString(3, "No variante");
					}
					if (datos[4].length() != 0) {
						ps.setString(4, datos[4]);
					} else {
						ps.setString(4, "No firmado");
					}
					ps.setString(5, datos[5]);
					ps.setString(6, datos[6]);
					ps.setString(7, datos[7]);
					ps.setString(8, datos[8]);
					ps.setString(9, datos[9]);
					ps.setString(10, datos[10]);
					ps.setString(11, datos[0]);

					if (ps.executeUpdate() == 1) {
						pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
						pantallaInformativa.setText("Ha modificado correctamente: \n" + "\nNombre del comic: " + datos[1]
								+ "\nNumero: " + datos[2] + "\nPortada variante: " + datos[3] + "\nFirma: " + datos[4]
										+ "\nEditorial: " + datos[5] + "\nFormato: " + datos[6] + "\nProcedencia: "
										+ datos[7] + "\nFecha de publicacion: " + datos[8] + "\nGuionista: " + datos[9]
												+ "\nDibujante: " + datos[10]);
					} else {
						pantallaInformativa.setStyle("-fx-background-color: #F53636");
						pantallaInformativa.setText("ERROR. Comic no modificado de forma correcta.");
					}
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			} else {
				pantallaInformativa.setStyle("-fx-background-color: #F53636");
				pantallaInformativa.setText("ERROR. No ha puesto ningun \nID en la busqueda.");
			}
		} else {
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("ERROR. Se ha cancelado la modificacion.");
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException {

		String id, nombreCom, numeroCom, varianteCom = "", firmaCom = "", editorialCom = "", formatoCom = "",
				procedenciaCom = "", fechaCom = "", guionistaCom = "", dibujanteCom = "";

		id = idComic.getText();

		nombreCom = nombreComic.getText();

		numeroCom = numeroComic.getText();

		nombreColumnas();

		List<Comics> listComics = FXCollections.observableArrayList(comic.filtadroBBDD(id, nombreCom, numeroCom,
				varianteCom, firmaCom, editorialCom, formatoCom, procedenciaCom, fechaCom, guionistaCom, dibujanteCom));
		tablaBBDD.getColumns().setAll(ID, nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listComics);
	}

	/**
	 * Muestra toda la base de datos.
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void verTodabbdd(ActionEvent event) throws SQLException {

		nombreColumnas();

		List<Comics> listComics = FXCollections.observableArrayList(comic.verTodo());
		tablaBBDD.getColumns().setAll(ID, nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listComics);

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