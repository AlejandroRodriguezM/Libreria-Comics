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
 *  - Eliminar comics de la base de datos o modificar su 'estado' de "En posesion" a "Vendido", estos ultimos no se veran en la busqueda general de la base de datos
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *  - Puntuar comics que se encuentren dentro de la base de datos.
 *
 *  Esta clase permite poder puntuar los comics que estan en la base de datos.
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import Funcionamiento.Comic;
import Funcionamiento.FuncionesConexionBBDD;
import Funcionamiento.FuncionesComicsBBDD;
import Funcionamiento.Ventanas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PuntuarDatosController implements Initializable {

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonOpinion;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Button botonbbdd;

	@FXML
	private Button botonLeidos;

	@FXML
	private Button botonBorrarOpinion;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextField dibujanteParametro;

	@FXML
	private TextField editorialParametro;

	@FXML
	private TextField fechaParametro;

	@FXML
	private TextField firmaParametro;

	@FXML
	private TextField formatoParametro;

	@FXML
	private TextField guionistaParametro;

	@FXML
	private TextField idParametro;

	@FXML
	private TextField idPuntuar;

	@FXML
	private TextField nombreParametro;

	@FXML
	private TextField numeroParametro;

	@FXML
	private TextArea pantallaInformativa;

	@FXML
	private TextField procedenciaParametro;

	@FXML
	private TextField varianteParametro;

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
	private ComboBox<String> puntuacionMenu;

	@FXML
	public TableView<Comic> tablaBBDD;

	private static Connection conn = FuncionesConexionBBDD.conexion();

	private static Ventanas nav = new Ventanas();

	private static FuncionesComicsBBDD libreria = new FuncionesComicsBBDD();

	/**
	 * Funcion que permite hacer funcionar la lista de puntuacion.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ObservableList<String> puntuaciones = FXCollections.observableArrayList("0/0", "0.5/5", "1/5", "1.5/5", "2/5",
				"2.5/5", "3/5", "3.5/5", "4/5", "4.5/5", "5/5");
		puntuacionMenu.setItems(puntuaciones);
		puntuacionMenu.getSelectionModel().selectFirst(); // Permite que no exista un valor null, escogiendo el primer
															// valor, que se encuentra vacio, en caso de querer borrar
															// la puntuacion.
	}

	/**
	 * Funcion que permite que al pulsar el boton 'botonOpinion' se modifique el
	 * dato "puntuacion" de un comic en concreto usando su ID"
	 *
	 * @param event
	 */
	@FXML
	void agregarOpinion(ActionEvent event) {

		insertarOpinion(); // Llamada a funcion
	}

	/**
	 * Funcion que permite insertar una puntuacion a un comic segun la ID
	 * introducida.
	 */
	public void insertarOpinion() {

		String sentenciaSQL = "UPDATE comicsbbdd set puntuacion = ? where ID = ?";

		if (nav.alertaAgregarPuntuacion()) { // Llamada a alerta de modificacion

			comprobarOpinionInsertada(sentenciaSQL); // Llamada a funcion que permite comprobar el cambio realizado en
														// el comic

		} else { // Si se cancela la opinion del comic, saltara el siguiente mensaje.
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("Opinion cancelada.");
		}
	}

	/**
	 * Funcion que permite borrar la opinion de un comic
	 *
	 * @param event
	 */
	@FXML
	void borrarOpinion(ActionEvent event) {

		String sentenciaSQL = "UPDATE comicsbbdd set puntuacion = '' where ID = ?";

		if (nav.alertaBorrarPuntuacion()) { // Llamada a alerta de modificacion

			comprobarOpinionBorrada(sentenciaSQL); // Llamada a funcion que permite comprobar el cambio realizado en el
													// comic

		} else { // Si se cancela la opinion del comic, saltara el siguiente mensaje.
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("Opinion cancelada.");
		}

	}

	/**
	 * Funcion que comprueba si la opinion se ha introducida correctamente
	 *
	 * @param ps
	 * @return
	 */
	public void comprobarOpinionInsertada(String sentenciaSQL) {

		String identificador = idPuntuar.getText();

		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sentenciaSQL);
			if (comprobarID()) // Comprueba si la ID introducida existe en la base de datos
			{
				Comic comic = libreria.comicDatos(identificador);
				ps.setString(2, idPuntuar.getText());
				comicPuntuacion(ps); // Llama a funcion que permite añadir la opinion al comic

				if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, saltara el siguiente mensaje
					pantallaInformativa.setOpacity(1);
					pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
					pantallaInformativa.setText("Opinion introducida con exito: " + comic.toString());
				}
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
	}

	/**
	 * Funcion que comprueba si la opinion se ha introducida correctamente
	 *
	 * @param ps
	 * @return
	 */
	public void comprobarOpinionBorrada(String sentenciaSQL) {

		String identificador = idPuntuar.getText();

		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sentenciaSQL);
			if (comprobarID()) // Comprueba si la ID introducida existe en la base de datos
			{
				Comic comic = libreria.comicDatos(identificador);
				ps.setString(1, idPuntuar.getText());

				if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, saltara el siguiente mensaje
					pantallaInformativa.setOpacity(1);
					pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
					pantallaInformativa.setText("Opinion borrada con exito: " + comic.toString());
				}
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
	}

	/**
	 * Comprueba que el ID introducido existe
	 *
	 * @return
	 */
	public boolean comprobarID() {
		String identificador = idPuntuar.getText();

		if (identificador.length() != 0) { // Si has introducido ID a la hora de realizar la modificacion, permitira lo
											// siguiente
			if (libreria.chechID(identificador)) {
				return true;
			} else // En caso contrario lanzara el siguiente mensaje en pantalla
			{
				pantallaInformativa.setOpacity(1);
				pantallaInformativa.setStyle("-fx-background-color: #F53636");
				pantallaInformativa.setText("No existe el " + identificador + " en la base de datos.");
				return false;
			}
		} else {
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			idPuntuar.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("ERROR. No ha puesto ningun \nID en la busqueda.");
			return false;
		}
	}

	/**
	 * Funcion que permite modificar la puntuacion de un comic, siempre y cuando el
	 * ID exista en la base de datos
	 *
	 * @param ps
	 * @return
	 */
	public void comicPuntuacion(PreparedStatement ps) {

		String puntuacion = puntuacionMenu.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
																								// "puntuacion"

		try {
			if (puntuacion.length() != 0) {
				ps.setString(1, puntuacion);
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
	}

	/**
	 * Limpia los diferentes datos que se ven en pantalla
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {

		// Campos de busqueda por parametro
		idParametro.setText("");

		nombreParametro.setText("");

		numeroParametro.setText("");

		varianteParametro.setText("");

		firmaParametro.setText("");

		editorialParametro.setText("");

		formatoParametro.setText("");

		procedenciaParametro.setText("");

		fechaParametro.setText("");

		guionistaParametro.setText("");

		dibujanteParametro.setText("");

		busquedaGeneral.setText("");

		idPuntuar.setText("");

		idPuntuar.setStyle(null);

		pantallaInformativa.setText(null);

		pantallaInformativa.setOpacity(0);

		tablaBBDD.getItems().clear();

	}

	/**
	 * Muestra la bbdd segun los parametros introducidos en los TextField
	 *
	 * @param event
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) {
		libreria.reiniciarBBDD();
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
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreriaPosesion());
	}

	/**
	 * Funcion que permite, que a la hora de pulsar el boton 'botonLeidos' se
	 * muestren aquellos comics que tengan una puntuacion
	 *
	 * @param event
	 */
	@FXML
	void verComicsLeidos(ActionEvent event) {
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreriaPuntuacion());
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

		campos[0] = idParametro.getText();

		campos[1] = nombreParametro.getText();

		campos[2] = numeroParametro.getText();

		campos[3] = varianteParametro.getText();

		campos[4] = firmaParametro.getText();

		campos[5] = editorialParametro.getText();

		campos[6] = formatoParametro.getText();

		campos[7] = procedenciaParametro.getText();

		campos[8] = fechaParametro.getText();

		campos[9] = guionistaParametro.getText();

		campos[10] = dibujanteParametro.getText();

		return campos;
	}

	/**
	 * Funcion que comprueba segun los datos escritos en los textArea, que comic
	 * estas buscando.
	 */
	public void listaPorParametro() {
		String datosComic[] = camposComic();

		Comic comic = new Comic(datosComic[0], datosComic[1], datosComic[2], datosComic[3], datosComic[4],
				datosComic[5], datosComic[6], datosComic[7], datosComic[8], datosComic[9], datosComic[10], "", "", "");

		tablaBBDD(busquedaParametro(comic));
	}

	/**
	 * Devuelve una lista de los comics cuyos datos han sido introducidos mediante
	 * parametros en los textField
	 *
	 * @param comic
	 * @return
	 */
	public List<Comic> busquedaParametro(Comic comic) {

		List<Comic> listComic;

		if (busquedaGeneral.getText().length() != 0) {
			listComic = FXCollections.observableArrayList();
			listComic = FXCollections.observableArrayList(libreria.verBusquedaGeneral(busquedaGeneral.getText()));
			busquedaGeneral.setText("");
		} else {
			listComic = FXCollections.observableArrayList(libreria.filtadroBBDD(comic));

			if (listComic.size() == 0) {
				pantallaInformativa.setOpacity(1);
				pantallaInformativa.setStyle("-fx-background-color: #F53636");
				pantallaInformativa.setText("ERROR. No hay ningun dato escrito para poder realizar la busqueda");
			}
		}

		return listComic;
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return
	 */
	public List<Comic> libreriaPosesion() {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.verLibreriaPosesion());

		if (listComic.size() == 0) {
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("ERROR. La base de datos se encuentra vacia");
		}

		return listComic;
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return
	 */
	public List<Comic> libreriaPuntuacion() {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.verLibreriaPuntuacion());

		if (listComic.size() == 0) {
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("ERROR. La base de datos se encuentra vacia");
		}

		return listComic;
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
		puntuacion.setCellValueFactory(new PropertyValueFactory<>("puntuacion"));
	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

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
