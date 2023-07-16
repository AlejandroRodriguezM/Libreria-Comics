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
 *  - Puntuar comics que se encuentren dentro de la base de datos.
 *  Esta clase permite acceder al menu principal donde se puede viajar a diferentes ventanas, etc.
 *
 *  Version 5.3
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.io.IOException;

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
 *  Version 4.1
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import Funcionamiento.Comic;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;


/**
 * Esta clase sirve para poder puntuar comics que tenemos en la base de datos
 *
 * @author Alejandro Rodriguez
 */
public class PuntuarDatosController implements Initializable {

	@FXML
	private MenuItem menu_archivo_desconectar;

	@FXML
	private MenuItem menu_archivo_sobreMi;

	@FXML
	private MenuItem menu_archivo_cerrar;

	@FXML
	private MenuItem menu_archivo_volver;

	@FXML
	private MenuItem menu_comic_aleatoria;

	@FXML
	private MenuItem menu_comic_aniadir;

	@FXML
	private MenuItem menu_comic_eliminar;

	@FXML
	private MenuItem menu_comic_modificar;
	
	@FXML
	private MenuItem menu_estadistica_estadistica;

	@FXML
	private MenuItem menu_estadistica_firmados;

	@FXML
	private MenuItem menu_estadistica_puntuados;

	@FXML
	private MenuItem menu_estadistica_comprados;

	@FXML
	private MenuItem menu_estadistica_posesion;
	
	@FXML
	private MenuItem menu_estadistica_vendidos;

	@FXML
	private MenuBar menu_navegacion;
	
	@FXML
	private Menu navegacion_estadistica;

	@FXML
	private Button botonBorrarOpinion;

	@FXML
	private Button botonLeidos;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonAgregarPuntuacion;

	@FXML
	private Button botonSalir;

	@FXML
	private DatePicker fechaPublicacion;

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
	private TextField idPuntuar;

	@FXML
	private TextField numeroCaja;

	@FXML
	private TextArea pantallaInformativa;

	@FXML
	private TableColumn<Comic, String> ID;

	@FXML
	private TableColumn<Comic, String> caja;

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
	private ComboBox<String> procedenciaParametro;

	@FXML
	private ComboBox<String> puntuacionMenu;

	@FXML
	private ComboBox<String> nombreFormato;

	@FXML
	public TableView<Comic> tablaBBDD;

	@FXML
	private ImageView imagencomic;

	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static Utilidades utilidad = null;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		libreria = new DBLibreriaManager();
		try {
			libreria.listasAutoCompletado();
			listas_autocompletado();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ObservableList<String> puntuaciones = FXCollections.observableArrayList("0/0", "0.5/5", "1/5", "1.5/5", "2/5",
				"2.5/5", "3/5", "3.5/5", "4/5", "4.5/5", "5/5");
		puntuacionMenu.setItems(puntuaciones);
		puntuacionMenu.getSelectionModel().selectFirst();

		ObservableList<String> procedenciaEstadoActual = FXCollections.observableArrayList("Todo","Spain", "USA", "Japon",
				"Italia", "Francia");
		procedenciaParametro.setItems(procedenciaEstadoActual);
		procedenciaParametro.getSelectionModel().selectFirst();

		ObservableList<String> formatoActual = FXCollections.observableArrayList("Todo","Grapa", "Tapa dura", "Tapa blanda",
				"Manga", "Libro");
		nombreFormato.setItems(formatoActual);
		nombreFormato.getSelectionModel().selectFirst();

		TextFormatter<Integer> textFormatterComic = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});

		TextFormatter<Integer> textFormatterComic2 = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});

		TextFormatter<Integer> textFormatterComic4 = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});

		numeroComic.setTextFormatter(textFormatterComic);
		numeroCaja.setTextFormatter(textFormatterComic2);
		idPuntuar.setTextFormatter(textFormatterComic4);

		// Agregar el ChangeListener al TextField idComicMod
		idPuntuar.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				boolean existeComic = false;
				try {
					existeComic = libreria.checkID(newValue);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (existeComic || newValue.isEmpty()) {

					Comic comic_temp = null;
					try {
						comic_temp = libreria.comicDatos(idPuntuar.getText());
					} catch (SQLException e) {
						e.printStackTrace();
					}

					idPuntuar.setText(idPuntuar.getText());

					nombreComic.setText(comic_temp.getNombre());

					numeroComic.setText(comic_temp.getNumero());

					nombreVariante.setText(comic_temp.getVariante());

					nombreFirma.setText(comic_temp.getFirma());

					nombreEditorial.setText(comic_temp.getEditorial());

					String formato = comic_temp.getFormato();
					nombreFormato.getSelectionModel().select(formato);

					String procedencia = comic_temp.getProcedencia();
					procedenciaParametro.getSelectionModel().select(procedencia);

					String puntuacion = comic_temp.getPuntuacion();
					puntuacionMenu.getSelectionModel().select(puntuacion);

					String fechaString = comic_temp.getFecha();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

					LocalDate fecha = LocalDate.parse(fechaString, formatter);
					fechaPublicacion.setValue(fecha);

					nombreGuionista.setText(comic_temp.getGuionista());

					nombreDibujante.setText(comic_temp.getDibujante());

					numeroCaja.setText(comic_temp.getNumCaja());

					pantallaInformativa.setOpacity(1);
					try {
						pantallaInformativa.setText(
								libreria.comicDatos(idPuntuar.getText()).toString().replace("[", "").replace("]", ""));
						imagencomic.setImage(libreria.selectorImage(idPuntuar.getText()));

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else {
					borrar_datos();
				}
			} else {
				borrar_datos();
			}
		});
	}

	/**
	 * Metodo que permite actualizar los valores de las listas de los
	 * autocompletados
	 */
	public void listas_autocompletado()  throws SQLException {
		TextFields.bindAutoCompletion(nombreComic, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(nombreVariante, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(nombreFirma, DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(nombreEditorial, DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(nombreGuionista, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(nombreDibujante, DBLibreriaManager.listaDibujante);
	}

	
	public void borrar_datos() {

		nombreComic.setText("");

		numeroComic.setText("");

		nombreVariante.setText("");

		nombreFirma.setText("");

		nombreEditorial.setText("");

		nombreFormato.getSelectionModel().select("");

		procedenciaParametro.getSelectionModel().select("");

		puntuacionMenu.getSelectionModel().select("");

		fechaPublicacion.setValue(null);

		nombreGuionista.setText("");

		nombreDibujante.setText("");

		numeroCaja.setText("");

		imagencomic.setImage(null);

		pantallaInformativa.setOpacity(0);
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaActual() {

		String procedenciaEstado = procedenciaParametro.getSelectionModel().getSelectedItem().toString(); // Toma el
																											// valor del
																											// menu
		// "puntuacion"
		return procedenciaEstado;
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

	/**
	 * Funcion que permite mostrar la imagen de portada cuando clickeas en una
	 * tabla.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void clickRaton(MouseEvent event) throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		libreria.libreriaCompleta();
		utilidad = new Utilidades();
		String id_comic;

		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (idRow != null) {
			id_comic = idRow.getID();
			idPuntuar.setStyle("");
			idPuntuar.setText(id_comic);
			pantallaInformativa.setStyle("");
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));

			imagencomic.setImage(libreria.selectorImage(id_comic));
			utilidad.deleteImage();
		}
		DBManager.resetConnection();
	}

	@FXML
	void teclasDireccion(KeyEvent event) throws IOException, SQLException {
		if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
			libreria = new DBLibreriaManager();
			libreria.libreriaCompleta();
			utilidad = new Utilidades();
			String id_comic;

			Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

			if (idRow != null) {
				id_comic = idRow.getID();
				idPuntuar.setStyle("");
				idPuntuar.setText(id_comic);
				pantallaInformativa.setStyle("");
				pantallaInformativa.setOpacity(1);
				pantallaInformativa.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));

				imagencomic.setImage(libreria.selectorImage(id_comic));
				utilidad.deleteImage();
			}
			DBManager.resetConnection();
		}
	}

	/**
	 * Funcion que permite que al pulsar el boton 'botonOpinion' se modifique el
	 * dato "puntuacion" de un comic en concreto usando su ID"
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void agregarPuntuacion(ActionEvent event) throws IOException, SQLException {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String id_comic = idPuntuar.getText();
		idPuntuar.setStyle("");
		if (id_comic.length() == 0 || !libreria.comprobarID(id_comic)) {
			String excepcion = "No puedes puntuar un comic si antes no pones un ID valido";

			nav.alertaException(excepcion);
			idPuntuar.setStyle("-fx-background-color: #FF0000;");
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("Error. Debes de introducir los datos correctos");
		} else {
			libreria.actualizarPuntuacion(id_comic, comicPuntuacion()); // Llamada a funcion
			datosOpinion("Opinion introducida con exito: ");
			nombreColumnas(); // Llamada a funcion
			tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
		}
	}

	/**
	 * Funcion que permite borrar la opinion de un comic
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void borrarPuntuacion(ActionEvent event) throws IOException, SQLException {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String id_comic = idPuntuar.getText();
		idPuntuar.setStyle("");

		if (id_comic.length() == 0 || !libreria.comprobarID(id_comic)) {
			String excepcion = "No puedes puntuar un comic si antes no pones un ID valido";

			nav.alertaException(excepcion);
			idPuntuar.setStyle("-fx-background-color: #FF0000;");
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("Error. Debes de introducir los datos correctos");
		} else {
			libreria.borrarPuntuacion(id_comic);
			datosOpinion("Opinion borrada con exito: ");
			nombreColumnas(); // Llamada a funcion
			tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion
		}
	}

	/*
	 * Muestra en TextArea un mensaje sobre puntuacion del comic introducido
	 */
	public void datosOpinion(String pantallaInfo) {
		pantallaInformativa.setOpacity(1);
		pantallaInformativa.setStyle("-fx-background-color: #A0F52D");

		String listaComicsTexto = DBLibreriaManager.listaComics.toString();
		int indexComa = listaComicsTexto.indexOf(",");
		String listaComicsSinComa = listaComicsTexto.substring(0, indexComa).replace("[", "");

		pantallaInformativa.setText(pantallaInfo + listaComicsSinComa);
	}

	/**
	 * Funcion que permite modificar la puntuacion de un comic, siempre y cuando el
	 * ID exista en la base de datos
	 *
	 * @param ps
	 * @return
	 */
	public String comicPuntuacion() {

		String puntuacion = puntuacionMenu.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
																								// "puntuacion"
		return puntuacion;
	}

	/**
	 * Limpia los diferentes datos que se ven en pantalla
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {

		nombreComic.setText("");
		numeroComic.setText("");
		nombreVariante.setText("");
		nombreFirma.setText("");
		nombreEditorial.setText("");
		fechaPublicacion.setValue(null);
		nombreDibujante.setText("");
		nombreGuionista.setText("");
		busquedaGeneral.setText("");
		numeroCaja.setText("");
		busquedaGeneral.setText("");
		idPuntuar.setText("");
		idPuntuar.setStyle(null);
		pantallaInformativa.setText(null);
		pantallaInformativa.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException {
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
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void verTodabbdd(ActionEvent event) throws IOException, SQLException {
		imagencomic.setImage(null);
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreria.libreriaCompleta()); // Llamada a funcion

	}

	/**
	 * Funcion que permite, que a la hora de pulsar el boton 'botonLeidos' se
	 * muestren aquellos comics que tengan una puntuacion
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void verComicsLeidos(ActionEvent event) throws SQLException {
		imagencomic.setImage(null);
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaPuntuacion());
	}

	/**
	 * Obtiene los datos de los comics de la base de datos y los devuelve en el
	 * textView
	 *
	 * @param listaComic
	 */
	@SuppressWarnings("unchecked")
	public void tablaBBDD(List<Comic> listaComic) {
		tablaBBDD.getColumns().setAll(nombre, caja, numero, variante, firma, editorial, formato, procedencia, fecha,
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
		String campos[] = new String[12];

		campos[1] = nombreComic.getText();

		campos[2] = numeroComic.getText();

		campos[3] = nombreVariante.getText();

		campos[4] = nombreFirma.getText();

		campos[5] = nombreEditorial.getText();

		if(formatoActual() == "Todo") {
			campos[6] = "";
		}else {
			campos[6] = formatoActual();
		}
		
		if(procedenciaActual() == "Todo") {
			campos[7] = "";
		}else {
			campos[7] = procedenciaActual();

		}

		LocalDate fecha = fechaPublicacion.getValue();
		campos[8] = (fecha != null) ? fecha.toString() : "";

		campos[9] = nombreGuionista.getText();

		campos[10] = nombreDibujante.getText();

		campos[11] = numeroCaja.getText();

		return campos;
	}

	/**
	 * Funcion que comprueba segun los datos escritos en los textArea, que comic
	 * estas buscando.
	 * 
	 * @throws SQLException
	 */
	public void listaPorParametro() throws SQLException {
		String datosComic[] = camposComic();

		Comic comic = new Comic("", datosComic[1], datosComic[11], datosComic[2], datosComic[3],
				datosComic[4], datosComic[5], datosComic[6], datosComic[7], datosComic[8], datosComic[9],
				datosComic[10], "", "", null);

		tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()));
		busquedaGeneral.setText("");
	}

	/**
	 * Permite dar valor a las celdas de la TableView
	 */
	private void nombreColumnas() {
		ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		nombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
		caja.setCellValueFactory(new PropertyValueFactory<>("numCaja"));
		numero.setCellValueFactory(new PropertyValueFactory<>("Numero"));
		variante.setCellValueFactory(new PropertyValueFactory<>("Variante"));
		firma.setCellValueFactory(new PropertyValueFactory<>("Firma"));
		editorial.setCellValueFactory(new PropertyValueFactory<>("Editorial"));
		formato.setCellValueFactory(new PropertyValueFactory<>("Formato"));
		procedencia.setCellValueFactory(new PropertyValueFactory<>("Procedencia"));
		fecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
		guionista.setCellValueFactory(new PropertyValueFactory<>("Guionista"));
		dibujante.setCellValueFactory(new PropertyValueFactory<>("Dibujante"));
		puntuacion.setCellValueFactory(new PropertyValueFactory<>("Puntuacion"));
	}
	
	/**
	 * Funcion que al pulsar el boton de 'botonPuntuacion' se muestran aquellos
	 * comics que tienen una puntuacion
	 *
	 * @param event
	 * @throws SQLException 
	 */
	@FXML
	void comicsPuntuacion(ActionEvent event) throws SQLException {
		pantallaInformativa.setOpacity(0);
		tablaBBDD.getItems().clear();
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
	 * @throws SQLException 
	 */
	@FXML
	void comicsVendidos(ActionEvent event) throws SQLException {
		pantallaInformativa.setOpacity(0);
		tablaBBDD.getItems().clear();
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
	 * @throws SQLException 
	 */
	@FXML
	void comicsFirmados(ActionEvent event) throws SQLException {
		pantallaInformativa.setOpacity(0);
		tablaBBDD.getItems().clear();
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
	 * @throws SQLException 
	 */
	@FXML
	void comicsComprados(ActionEvent event) throws SQLException {
		pantallaInformativa.setOpacity(0);
		tablaBBDD.getItems().clear();
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaComprados());
	}
	
	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 * @throws SQLException 
	 */
	@FXML
	void comicsEnPosesion(ActionEvent event) throws SQLException {
		pantallaInformativa.setOpacity(0);
		tablaBBDD.getItems().clear();
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas();
		tablaBBDD(libreria.libreriaPosesion());
	}
	
	/**
	 * Se llama a funcion que permite ver las estadisticas de la bbdd
	 *
	 * @param event
	 */
	@FXML
	void verEstadistica(ActionEvent event) {
		tablaBBDD.getItems().clear();
		libreria = new DBLibreriaManager();
		pantallaInformativa.setOpacity(1);
		pantallaInformativa.setText(libreria.procedimientosEstadistica());
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
		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
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

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
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

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
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

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Metodo que permite abrir la ventana "sobreMiController"
	 *
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {

		nav.verSobreMi();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
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
	public void desconectar(ActionEvent event) throws IOException {
		nav.verAccesoBBDD();
		DBManager.close();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void volverMenu(ActionEvent event) throws IOException {
		nav.verMenuPrincipal();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Maneja la acción de salida del programa.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {
		// Lógica para manejar la acción de "Salir"
		if (nav.salirPrograma(event)) {
			Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {

		Platform.exit();

	}
}
