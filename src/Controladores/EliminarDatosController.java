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
 *  Version 5.5.0.1
 *
 *  @author Alejandro Rodriguez
 *
 */
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Funcionamiento.Comic;
import Funcionamiento.FuncionesComboBox;
import Funcionamiento.FuncionesTableView;
import Funcionamiento.FuncionesTooltips;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Esta clase sirve para eliminar datos de la base de datos, realiza la funcion
 * de cambiar el dato de "estado" de "En posesion" a "Vendido" o de eliminar
 * directamente.
 *
 *
 * @author Alejandro Rodriguez
 */
public class EliminarDatosController implements Initializable {

	@FXML
	private Label label_id_eliminar;

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
	private MenuItem menu_comic_modificar;

	@FXML
	private MenuItem menu_comic_puntuar;

	@FXML
	private MenuBar menu_navegacion;

	@FXML
	private Menu navegacion_cerrar;

	@FXML
	private Menu navegacion_comic;

	@FXML
	private Menu navegacion_estadistica;

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
	private MenuItem menu_estadistica_key_issue;

	@FXML
	private Button botonEliminar;

	@FXML
	private Button botonVender;

	@FXML
	private Button botonEnVenta;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonbbdd;

	@FXML
	private DatePicker fechaPublicacion;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextArea prontInfo;

	@FXML
	private TextField idComicTratar;

	@FXML
	public TableView<Comic> tablaBBDD;

	@FXML
	private TableColumn<Comic, String> ID;

	@FXML
	private TableColumn<Comic, String> caja;

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
	private TableColumn<Comic, String> formato;

	@FXML
	private TableColumn<Comic, String> fecha;

	@FXML
	private TableColumn<Comic, String> firma;

	@FXML
	private TableColumn<Comic, String> guionista;

	@FXML
	private TableColumn<Comic, String> nombre;

	@FXML
	private TableColumn<Comic, String> referencia;

	@FXML
	private ImageView imagencomic;

	@FXML
	private ComboBox<String> nombreComic;

	@FXML
	private ComboBox<String> numeroComic;

	@FXML
	private ComboBox<String> nombreFirma;

	@FXML
	private ComboBox<String> nombreGuionista;

	@FXML
	private ComboBox<String> nombreVariante;

	@FXML
	private ComboBox<String> numeroCaja;

	@FXML
	private ComboBox<String> nombreProcedencia;

	@FXML
	private ComboBox<String> nombreFormato;

	@FXML
	private ComboBox<String> nombreEditorial;

	@FXML
	private ComboBox<String> nombreDibujante;

	@FXML
	private VBox rootVBox;

	@FXML
	private VBox vboxContenido;

	private Timeline parpadeo;

	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static Utilidades utilidad = null;
	private static FuncionesTableView funcionesTabla = new FuncionesTableView();
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();
	private List<TableColumn<Comic, String>> columnList;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		libreria = new DBLibreriaManager();
		utilidad = new Utilidades();

		prontInfo.textProperty().addListener((observable, oldValue, newValue) -> {
			funcionesTabla.ajustarAnchoVBox(prontInfo, vboxContenido);
		});

		// Asegurarnos de que el VBox ajuste su tamaño correctamente al inicio
		Platform.runLater(() -> funcionesTabla.ajustarAnchoVBox(prontInfo, vboxContenido));

		Platform.runLater(() -> funcionesTabla.seleccionarRaw(tablaBBDD));
		Platform.runLater(() -> asignarTooltips());

		List<TableColumn<Comic, String>> columnListCarga = Arrays.asList(nombre, caja, numero, variante, firma,
				editorial, formato, procedencia, fecha, guionista, dibujante, referencia);
		columnList = columnListCarga;

		animacion();
		autoRelleno();
		funcionesTabla.modificarColumnas(tablaBBDD, columnList);
		restringir_entrada_datos();
		List<ComboBox<String>> comboboxes = Arrays.asList(nombreComic, numeroComic, nombreVariante, nombreProcedencia,
				nombreFormato, nombreDibujante, nombreGuionista, nombreEditorial, nombreFirma, numeroCaja);

		int totalComboboxes = comboboxes.size();

		// Crear un ScheduledExecutorService para ejecutar la tarea después de un 1 segundo
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.schedule(() -> {

			Platform.runLater(() -> {
				
				try {
					libreria.listasAutoCompletado();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						funcionesCombo.rellenarComboBox(comboboxes);
						funcionesCombo.lecturaComboBox(totalComboboxes, comboboxes);
						return null;
					}
				};

				// Iniciar el Task en un nuevo hilo
				Thread thread = new Thread(task);
				thread.start();
			});
		}, 0, TimeUnit.SECONDS);
		// Desactivar el enfoque en el VBox para evitar que reciba eventos de teclado
		rootVBox.setFocusTraversable(false);

		// Agregar un filtro de eventos para capturar el enfoque en el TableView y
		// desactivar el enfoque en el VBox
		tablaBBDD.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			rootVBox.setFocusTraversable(false);
			tablaBBDD.requestFocus();
		});

		// Agregar un filtro de eventos para capturar el enfoque en el VBox y desactivar
		// el enfoque en el TableView
		rootVBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			tablaBBDD.setFocusTraversable(false);
			rootVBox.requestFocus();
		});

		prontInfo.setEditable(false);

	}

	/**
	 * Asigna tooltips a varios elementos.
	 */
	public void asignarTooltips() {
		// Crear una lista de elementos que necesitan tooltips
		List<Object> elementos = new ArrayList<>();
		elementos.add(botonbbdd);
		elementos.add(botonLimpiarComic);
		elementos.add(botonMostrarParametro);
		elementos.add(nombreComic);
		elementos.add(numeroComic);
		elementos.add(nombreFirma);
		elementos.add(nombreGuionista);
		elementos.add(nombreVariante);
		elementos.add(numeroCaja);
		elementos.add(nombreProcedencia);
		elementos.add(nombreFormato);
		elementos.add(nombreEditorial);
		elementos.add(nombreDibujante);

		// Asignar tooltips a los elementos utilizando la clase FuncionesTooltips
		FuncionesTooltips.asignarTooltips(elementos);
	}

	/**
	 * Configura una animación de parpadeo para un Label.
	 */
	public void animacion() {
		// Crear una Timeline para la animación de parpadeo
		parpadeo = new Timeline(
				new KeyFrame(Duration.seconds(0.5), new KeyValue(label_id_eliminar.borderProperty(), Border.EMPTY)),
				new KeyFrame(Duration.seconds(1), new KeyValue(label_id_eliminar.borderProperty(),
						new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)))));
		parpadeo.setCycleCount(Animation.INDEFINITE);
		parpadeo.setAutoReverse(true);

		// Configurar el estilo inicial del Label
		label_id_eliminar.setBorder(Border.EMPTY);
	}

	/**
	 * Realiza el auto-rellenado de información en base al valor del TextField
	 * idComicTratar.
	 */
	public void autoRelleno() {
		// Agregar un ChangeListener al TextField idComicTratar
		idComicTratar.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				boolean existeComic = false;
				try {
					existeComic = libreria.checkID(newValue);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (existeComic || newValue.isEmpty()) {

					if (idComicTratar == null) {
						idComicTratar.setText("");
					}

					idComicTratar.setText(idComicTratar.getText());

					prontInfo.clear();
					prontInfo.setOpacity(1);
					try {
						prontInfo.setText(libreria.comicDatos(idComicTratar.getText()).toString().replace("[", "")
								.replace("]", ""));
						imagencomic.setImage(libreria.selectorImage(idComicTratar.getText()));

					} catch (SQLException e) {
						e.printStackTrace();
					}
					imagencomic.setImage(libreria.selectorImage(idComicTratar.getText()));
				} else {
					imagencomic.setImage(null);
					prontInfo.clear();
					prontInfo.setOpacity(0);
				}
			} else {
				imagencomic.setImage(null);
				prontInfo.clear();
				prontInfo.setOpacity(0);
			}
		});
	}

	/**
	 * Funcion que permite restringir entrada de datos de todo aquello que no sea un
	 * numero entero en los comboBox numeroComic y caja_comic
	 */
	public void restringir_entrada_datos() {
		numeroComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroCaja.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaActual() {

		String procedenciaEstadoNuevo = "";
		if (nombreProcedencia.getSelectionModel().getSelectedItem() != null) {
			procedenciaEstadoNuevo = nombreProcedencia.getSelectionModel().getSelectedItem().toString();
		}
		return procedenciaEstadoNuevo;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreFormato" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String formatoActual() {

		String formatoEstado = "";
		if (nombreFormato.getSelectionModel().getSelectedItem() != null) {
			formatoEstado = nombreFormato.getSelectionModel().getSelectedItem().toString();
		}
		return formatoEstado;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreEditorial" y lo
	 * devuelve, para la busqueda de comic
	 * 
	 * @return
	 */
	public String editorialActual() {

		String editorialComic = "";

		if (nombreEditorial.getSelectionModel().getSelectedItem() != null) {
			editorialComic = nombreEditorial.getSelectionModel().getSelectedItem().toString();
		}

		return editorialComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreDibujante" y lo
	 * devuelve, para la busqueda de comic
	 * 
	 * @return
	 */
	public String dibujanteActual() {
		String dibujanteComic = "";

		if (nombreDibujante.getSelectionModel().getSelectedItem() != null) {
			dibujanteComic = nombreDibujante.getSelectionModel().getSelectedItem().toString();
		}

		return dibujanteComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreGuionista" y lo
	 * devuelve, para la busqueda de comic
	 * 
	 * @return
	 */
	public String guionistaActual() {
		String guionistaComic = "";

		if (nombreGuionista.getSelectionModel().getSelectedItem() != null) {
			guionistaComic = nombreGuionista.getSelectionModel().getSelectedItem().toString();
		}

		return guionistaComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreFirma" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String firmaActual() {
		String firmaComic = "";

		if (nombreFirma.getSelectionModel().getSelectedItem() != null) {
			firmaComic = nombreFirma.getSelectionModel().getSelectedItem().toString();
		}

		return firmaComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreComic" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String nombreActual() {
		String nombreComics = "";

		if (nombreComic.getSelectionModel().getSelectedItem() != null) {
			nombreComics = nombreComic.getSelectionModel().getSelectedItem().toString();
		}

		return nombreComics;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "numeroComic" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String numeroComicActual() {
		String numComic = "";

		if (numeroComic.getSelectionModel().getSelectedItem() != null) {
			numComic = numeroComic.getSelectionModel().getSelectedItem().toString();
		}

		return numComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreVariante" y lo
	 * devuelve, para la busqueda de comic
	 * 
	 * @return
	 */
	public String varianteActual() {
		String varianteComics = "";

		if (nombreVariante.getSelectionModel().getSelectedItem() != null) {
			varianteComics = nombreVariante.getSelectionModel().getSelectedItem().toString();
		}

		return varianteComics;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String cajaActual() {

		String cajaComics = "";

		if (numeroCaja.getSelectionModel().getSelectedItem() != null) {
			cajaComics = numeroCaja.getSelectionModel().getSelectedItem().toString();
		}

		return cajaComics;
	}

	/**
	 * Funcion que permite limpiar los datos en pantalla
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) { // Metodo que permite limpiar los datos de los diferentes campos
		limpiezaDeDatos();
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
	}

	/**
	 * Realiza una limpieza de datos en la interfaz.
	 */
	public void limpiezaDeDatos() {
		// Limpiar todos los ComboBox
		for (ComboBox<String> comboBox : Arrays.asList(nombreComic, numeroComic, nombreFirma, nombreGuionista,
				nombreVariante, numeroCaja, nombreProcedencia, nombreFormato, nombreEditorial, nombreDibujante)) {
			comboBox.setValue(""); // Borrar el valor seleccionado
			comboBox.getEditor().setText(""); // Borrar el texto del editor
		}

		ID.setText(""); // Borrar el texto del campo ID

		// Limpiar elementos adicionales de la interfaz
		fechaPublicacion.setValue(null); // Borrar el valor de la fecha
		tablaBBDD.getItems().clear(); // Limpiar los elementos de la tabla
		imagencomic.setImage(null); // Borrar la imagen
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
			idComicTratar.setStyle("");
			idComicTratar.setText(id_comic);
			prontInfo.setStyle("");
			prontInfo.setOpacity(1);
			prontInfo.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));

			imagencomic.setImage(libreria.selectorImage(id_comic));
			utilidad.deleteImage();
		}
		DBManager.resetConnection();
	}

	/**
	 * Funcion que permite mostrar la imagen de portada cuando usas las teclas de
	 * direccion en una tabla.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
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
				idComicTratar.setStyle("");
				idComicTratar.setText(id_comic);
				prontInfo.setStyle("");
				prontInfo.setOpacity(1);
				prontInfo.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));

				imagencomic.setImage(libreria.selectorImage(id_comic));
				utilidad.deleteImage();
			}
			DBManager.resetConnection();
		}
	}

	/**
	 * Funcion que elimina un comic de la base de datos.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void eliminarDatos(ActionEvent event) throws IOException, SQLException {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");

		if (id_comic.length() == 0 || !libreria.comprobarID(id_comic)) {
			String excepcion = "No puedes eliminar un comic si antes no pones un ID valido";
			nav.alertaException(excepcion);

			idComicTratar.setStyle("-fx-background-color: #FF0000;");

			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {
			modificarDatos(id_comic);
			libreria.eliminarComicBBDD(id_comic);
			libreria.reiniciarBBDD();
			libreria.listasAutoCompletado();
			funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
			funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
		}
	}

	/**
	 * Metodo que permite cambiar de estado un comic, para que se deje de mostrar en
	 * el programa, pero este sigue estando dentro de la bbdd
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void ventaComic(ActionEvent event) throws IOException, SQLException {
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");

		if (id_comic.length() == 0 || !libreria.comprobarID(id_comic)) {
			String excepcion = "No puedes eliminar un comic si antes no pones un ID valido";

			nav.alertaException(excepcion);
			idComicTratar.setStyle("-fx-background-color: #FF0000;");
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {

			modificarDatos(id_comic);
			libreria.venderComicBBDD(id_comic);
			libreria.reiniciarBBDD();
			funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
			funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion

		}
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException {
		funcionesTabla.modificarColumnas(tablaBBDD, columnList);
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
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
		funcionesTabla.modificarColumnas(tablaBBDD, columnList);
//		limpiezaDeDatos();
		tablaBBDD.refresh();
		prontInfo.setOpacity(0);
		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
	}

	/**
	 * Funcion que muestra los comics que coincidan con los parametros introducidos
	 * en los textField
	 *
	 * @return
	 * @throws SQLException
	 */
	public void listaPorParametro() throws SQLException {
		libreria = new DBLibreriaManager();

		String datos[] = camposComic();

		Comic comic = new Comic("", datos[1], datos[11], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7],
				datos[8], datos[9], datos[10], "", "", "", null, "", "");

		funcionesTabla.tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()), tablaBBDD, columnList); // Llamada
																														// a
																														// funcion
		busquedaGeneral.setText("");

		prontInfo.setText(funcionesTabla.resultadoBusquedaPront(comic).getText());
	}

	/**
	 * Maneja el evento de búsqueda de cómics por Key Issue.
	 * @param event El evento generado al hacer clic en el botón.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	@FXML
	void comicsKeyIssue(ActionEvent event) throws SQLException {
		prontInfo.setOpacity(0);
		limpiezaDeDatos();
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaKeyIssue(), tablaBBDD, columnList); // Llamada a funcion
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
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaPuntuacion(), tablaBBDD, columnList); // Llamada a funcion

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
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaVendidos(), tablaBBDD, columnList); // Llamada a funcion
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
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaFirmados(), tablaBBDD, columnList); // Llamada a funcion
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
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaComprados(), tablaBBDD, columnList); // Llamada a funcion
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
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		utilidad = new Utilidades();
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaPosesion(), tablaBBDD, columnList); // Llamada a funcion
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
		prontInfo.setOpacity(1);
		prontInfo.setText("Generando fichero de estadisticas . . . ");
		libreria.generar_fichero_estadisticas();
	}

	//////////////////////////
	//// FUNCIONES/////////////
	//////////////////////////

	/**
	 * Funcion que permite cambiar de estado o eliminar un comic de la base de
	 * datos.
	 *
	 * @param id
	 * @param sentenciaSQL
	 * @throws SQLException
	 */
	public boolean modificarDatos(String ID) throws SQLException {
		libreria = new DBLibreriaManager();
		if (nav.alertaEliminar()) {
			if (ID.length() != 0) {

				Comic comic = libreria.comicDatos(ID); // Llamada de metodo que contiene el comic que se desea eliminar

				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo
						.setText("Has modificado correctamente: " + comic.toString().replace("[", "").replace("]", ""));
				idComicTratar.setStyle(null);
				imagencomic.setImage(libreria.selectorImage(ID));
				return true;
			} else {
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("ERROR. ID desconocido.");
				idComicTratar.setStyle("-fx-background-color: red");
				return false;
			}
		} else { // Si se cancela el borra del comic, saltara el siguiente mensaje.
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Modificacion cancelada.");
			return false;
		}
	}

	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public String[] camposComic() {
		String campos[] = new String[12];

		if (nombreActual().isEmpty()) {
			campos[1] = "";
		} else {
			campos[1] = nombreActual();
		}

		if (numeroComicActual().isEmpty()) {
			campos[2] = "";
		} else {
			campos[2] = numeroComicActual();
		}

		if (varianteActual().isEmpty()) {
			campos[3] = "";
		} else {
			campos[3] = varianteActual();
		}

		if (firmaActual().isEmpty()) {
			campos[4] = "";
		} else {
			campos[4] = firmaActual();
		}

		if (editorialActual().isEmpty()) {
			campos[5] = "";
		} else {
			campos[5] = editorialActual();
		}

		if (formatoActual().isEmpty()) {
			campos[6] = "";
		} else {
			campos[6] = formatoActual();
		}

		if (procedenciaActual().isEmpty()) {
			campos[7] = "";
		} else {
			campos[7] = procedenciaActual();
		}
		LocalDate fecha = fechaPublicacion.getValue();
		if (fecha != null) {
			campos[8] = fecha.toString();
		} else {
			campos[8] = "";
		}

		if (guionistaActual().isEmpty()) {
			campos[9] = "";
		} else {
			campos[9] = guionistaActual();
		}

		if (dibujanteActual().isEmpty()) {
			campos[10] = "";
		} else {
			campos[10] = dibujanteActual();
		}

		if (cajaActual().isEmpty() || cajaActual().equals("0")) {
			campos[11] = "";
		} else {
			campos[11] = cajaActual();
		}

		return campos;
	}

	/////////////////////////////////
	//// METODO LLAMADA A VENTANA//
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
	 *
	 * @param event
	 */
	@FXML
	void ventanaPuntuar(ActionEvent event) {

		nav.verPuntuar();

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
		DBManager.close();
		nav.verAccesoBBDD();

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
