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
 *  Esta clase permite cambiar el estado de los comics para que no se vean en el MenuPrincipal
 *  
 *  Version 2.3
 *  
 *  Por Alejandro Rodriguez
 *  
 *  Twitter: @silverAlox
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class EliminarDatosController {

	@FXML
	private Button botonEliminar;

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

	Comics comic = new Comics();

	private static Connection conn = DBManager.conexion();

	NavegacionVentanas nav = new NavegacionVentanas();

	/**
	 * 
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) { //Metodo que permite limpiar los datos de los diferentes campos
		nombreComic.setText("");
		numeroComic.setText("");
		ID.setText("");
	}

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
	
	@FXML
	void eliminarDatos(ActionEvent event) throws SQLException { //Metodo que permite cambiar de estado un comic, para que se deje de mostrar en el programa, pero este sigue estando dentro de la bbdd
		String id, nombreCom = "", numeroCom = "", varianteCom = "", firmaCom = "", editorialCom = "", formatoCom = "",
				procedenciaCom = "", fechaCom = "", guionistaCom = "", dibujanteCom = "", sentenciaSQL;

		sentenciaSQL = "UPDATE comicsbbdd set estado = 'Vendido' where ID = ?";
		
		PreparedStatement stmt;
		
		try {
			if (nav.alertaEliminar()) { //Llamada a metodo que permite lanzar una alerta. En caso de aceptarlo permitira lo siguiente.
				
				id = idComic.getText();
				
				List<Comics> listComics = FXCollections.observableArrayList(comic.filtadroBBDD(id, nombreCom, numeroCom,
						varianteCom, firmaCom, editorialCom, formatoCom, procedenciaCom, fechaCom, guionistaCom, dibujanteCom)); //Lista que contiene toda los comics de la base de datos.
				if (id.length() != 0) {
					stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_UPDATABLE); //Permite leer y ejecutar la sentencia de MySql

					stmt.setString(1, id);
					if (stmt.executeUpdate() == 1) { //En caso de que el cambio de estado se haya realizado correctamente, mostrara lo siguiente
						pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
						pantallaInformativa.setText("Has eliminado correctamente: " + listComics.toString());
					} else { //En caso contrario mostrara lo siguiente
						pantallaInformativa.setStyle("-fx-background-color: #F53636");
						pantallaInformativa.setText("ERROR. ID desconocido.");
					}
				}
			} else { //Si se cancela el borra del comic, saltara el siguiente mensaje.
				pantallaInformativa.setStyle("-fx-background-color: #F53636");
				pantallaInformativa.setText("Borrado cancelado.");
			}
		} catch (SQLException ex) {
			System.err.println(ex);
		}
	}

	/**
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException { //Permite ver el contenido de la base de datos por marametro.

		String id, nombreCom, numeroCom, varianteCom = "", firmaCom = "", editorialCom = "", formatoCom = "",
				procedenciaCom = "", fechaCom = "", guionistaCom = "", dibujanteCom = "";

		nombreCom = nombreComic.getText();

		numeroCom = numeroComic.getText();

		id = idComic.getText();

		nombreColumnas(); //Llamada a metodo que servira para comprar que columnas hay

		List<Comics> listComics = FXCollections.observableArrayList(comic.filtadroBBDD(id, nombreCom, numeroCom,
				varianteCom, firmaCom, editorialCom, formatoCom, procedenciaCom, fechaCom, guionistaCom, dibujanteCom)); //Lista que contiene toda los comics de la base de datos.
		tablaBBDD.getColumns().setAll(ID, nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante); //Muestra los datos en las columnas 
		tablaBBDD.getItems().setAll(listComics); //Muestra el contenido de la bbdd en las columnas especificas.

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
	 * Muestra las columnas especificas del fichero FXML
	 */
	private void nombreColumnas() { //Funcion que especifica el total de columnas de la bbdd se mostraran en la ventana
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
	 * Permite salir completamente del programa.
	 * 
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) { //Metodo que poermite salir del programa

		if (nav.salirPrograma(event)) { //Llamada a metodo que permite salir del programa
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close(); //Cierra la ventana actual
		}
	}

	/**
	 * Al cerrar la ventana, se cargara la ventana de verBBDD
	 * 
	 * @throws IOException
	 */
	public void closeWindows() throws IOException { //Metodo que permite cerrar el programa a la fuerza. 

		nav.verMenuPrincipal(); //Llamada a metodo que carga y muestra la ventana de MenuPrincipal

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close(); //Cierra la ventana actual

	}

}
