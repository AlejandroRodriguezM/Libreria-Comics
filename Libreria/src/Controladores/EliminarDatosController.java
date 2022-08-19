package Controladores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Funcionamiento.BBDD;
import Funcionamiento.Comic;
import Funcionamiento.ConexionBBDD;
import Funcionamiento.Libreria;
import Funcionamiento.NavegacionVentanas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Esta clase sirve para eliminar datos. No elimna los datos, realiza la funcion
 * de cambiar el dato de "estado" de "En posesion" a "Vendido"
 *
 * @author Alejandro Rodriguez
 */
public class EliminarDatosController {

	@FXML
	private Button botonEliminar;

	@FXML
	private Button botonVender;

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

	private Connection conn = ConexionBBDD.conexion();

	private BBDD bd = new BBDD();

	/**
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
	}

	/**
	 *
	 * @param event
	 */
	@FXML
	void eliminarDatos(ActionEvent event) {
		deleteDataBBDD();
		libreria.reiniciarBBDD();
	}

	/**
	 * Metodo que permite cambiar de estado un comic, para que se deje de mostrar en
	 * el programa, pero este sigue estando dentro de la bbdd
	 *
	 * @param event
	 */
	@FXML
	void ventaDatos(ActionEvent event) { //
		deleteDataSell();
		libreria.reiniciarBBDD();
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
	void verTodabbdd(ActionEvent event) throws SQLException {
		nombreColumnas();
		tablaBBDD(libreriaCompleta());
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
	 * Almacena los datos introducidos en los TextField
	 *
	 */
	public void listaPorParametro() {
		String datosComic[] = camposComics();

		Comic comic = new Comic(datosComic[0], datosComic[1], datosComic[2], datosComic[3], datosComic[4],
				datosComic[5], datosComic[6], datosComic[7], datosComic[8], datosComic[9], datosComic[10], "", "");

		tablaBBDD(busquedaParametro(comic));
	}

	/**
	 * Devuelve el comic buscado por parametros
	 *
	 * @param comic
	 * @return
	 */
	public List<Comic> busquedaParametro(Comic comic) {
		List<Comic> listComic;

		if (busquedaGeneral.getText().length() != 0) {
			listComic = FXCollections.observableArrayList(libreria.verBusquedaGeneral(busquedaGeneral.getText()));
			busquedaGeneral.setText("");
		} else {
			listComic = FXCollections.observableArrayList(libreria.filtadroBBDD(comic));
			if (listComic.size() == 0) {
				pantallaInformativa.setOpacity(1);
				pantallaInformativa.setStyle("-fx-background-color: #F53636");
				pantallaInformativa.setText("ERROR. No hay ningun dato en la base de datos");
			}
		}

		return listComic;
	}

	/**
	 * Devuelto una lista con todos los comics existentes en la bbdd
	 *
	 * @return
	 */
	public List<Comic> libreriaCompleta() {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.verLibreria());

		if (listComic.size() == 0) {
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("ERROR. No hay ningun dato en la base de datos");
		}

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
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listaComic);
	}

	/**
	 *
	 * @param id
	 * @param sentenciaSQL
	 */
	public void modifyDataBase(String id, String sentenciaSQL) {
		PreparedStatement stmt;

		Comic comic = libreria.comicDatos(id); // Llamada de metodo que contiene el comic que se desea eliminar

		try {
			if (nav.alertaEliminar()) { // Llamada a metodo que permite lanzar una alerta. En caso de aceptarlo
				// permitira lo siguiente.

				if (id.length() != 0) {
					stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_UPDATABLE); // Permite leer y ejecutar la sentencia de MySql

					stmt.setString(1, id);
					if (stmt.executeUpdate() == 1) { // En caso de que el cambio de estado se haya realizado
						// correctamente, mostrara lo siguiente
						pantallaInformativa.setOpacity(1);
						pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
						pantallaInformativa.setText(
								"Has eliminado correctamente: " + comic.toString().replace("[", "").replace("]", ""));
					} else { // En caso contrario mostrara lo siguiente
						pantallaInformativa.setOpacity(1);
						pantallaInformativa.setStyle("-fx-background-color: #F53636");
						pantallaInformativa.setText("ERROR. ID desconocido.");
					}
				}
			} else { // Si se cancela el borra del comic, saltara el siguiente mensaje.
				pantallaInformativa.setStyle("-fx-background-color: #F53636");
				pantallaInformativa.setText("Borrado cancelado.");
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
	}

	/**
	 * Funcion que permite cambiar de estado el comic a "Vendido" y hace que no se
	 * muestre en la bbdd
	 */
	public void deleteDataSell() {
		String id, sentenciaSQL;

		sentenciaSQL = "UPDATE Comicsbbdd set estado = 'Vendido' where ID = ?";

		id = idComic.getText();

		modifyDataBase(id, sentenciaSQL);

	}

	/**
	 *
	 */
	public void deleteDataBBDD() {
		String id, sentenciaSQL;

		sentenciaSQL = "DELETE from Comicsbbdd where ID = ?";

		id = idComic.getText();

		modifyDataBase(id, sentenciaSQL);

		bd.reloadID();

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
	 *
	 */
	public void closeWindows() { // Metodo que permite cerrar el programa a la fuerza.

		nav.verMenuPrincipal(); // Llamada a metodo que carga y muestra la ventana de MenuPrincipal

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close(); // Cierra la ventana actual

	}

}
