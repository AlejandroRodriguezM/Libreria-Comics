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
 *  Version 6.1.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.controlsfx.control.textfield.TextFields;

import Funcionamiento.Comic;
import Funcionamiento.FuncionesComboBox;
import Funcionamiento.FuncionesTableView;
import Funcionamiento.FuncionesTooltips;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Esta clase sirve para modificar posibles datos de un comic en concreto
 *
 * @author Alejandro Rodriguez
 */
public class ModificarDatosController implements Initializable {

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
	private MenuItem menu_estadistica_key_issue;

	@FXML
	private MenuBar menu_navegacion;

	@FXML
	private Menu navegacion_cerrar;

	@FXML
	private Menu navegacion_comic;

	@FXML
	private Menu navegacion_estadistica;

	@FXML
	private Button botonModificar;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonbbdd;

	@FXML
	private Button botonNuevaPortada;

	@FXML
	private DatePicker fechaPublicacion;

	@FXML
	private DatePicker anioPublicacionMod;

	@FXML
	private TextField idComicMod;

	@FXML
	private ComboBox<String> nombreComic;

	@FXML
	private TextField nombreComicMod;

	@FXML
	private ComboBox<String> nombreDibujante;

	@FXML
	private TextField nombreDibujanteMod;

	@FXML
	private ComboBox<String> nombreEditorial;

	@FXML
	private TextField nombreEditorialMod;

	@FXML
	private ComboBox<String> nombreFirma;

	@FXML
	private TextField nombreFirmaMod;

	@FXML
	private ComboBox<String> nombreGuionista;

	@FXML
	private TextField nombreGuionistaMod;

	@FXML
	private ComboBox<String> nombreVariante;

	@FXML
	private TextField nombreVarianteMod;

	@FXML
	private ComboBox<String> numeroComic;

	@FXML
	private ComboBox<String> numeroComicMod;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextField direccionImagen;

	@FXML
	private TextField nombreKeyIssue;

	@FXML
	private TextField precioComic;

	@FXML
	private TextField urlReferencia;

	@FXML
	private ComboBox<String> numeroCaja;

	@FXML
	private ComboBox<String> numeroCajaMod;

	@FXML
	private TextArea prontInfo;

	@FXML
	private Label idMod;

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
	private TableColumn<Comic, String> referencia;

	@FXML
	public TableView<Comic> tablaBBDD;

	@FXML
	private ComboBox<String> nombreProcedencia;

	@FXML
	private ComboBox<String> nombreProcedenciaMod;

	@FXML
	private ComboBox<String> estadoComic;

	@FXML
	private ComboBox<String> nombreFormato;

	@FXML
	private ComboBox<String> nombreFormatoMod;

	@FXML
	private ImageView imagencomic;

	@FXML
	private VBox rootVBox;

	@FXML
	private VBox vboxContenido;

	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();
	private static FuncionesTableView funcionesTabla = new FuncionesTableView();
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
		new Utilidades();
		
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
		
		autoRelleno();
		funcionesTabla.modificarColumnas(tablaBBDD,columnList);
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
				
			    // Cuando la tarea haya terminado, apaga el scheduler
			    task.setOnSucceeded(event -> {
			        scheduler.shutdown();
			    });
			});
		}, 0, TimeUnit.SECONDS);
		rellenarCombosEstaticos();
		
		FuncionesTableView.restringirSimbolos(nombreGuionistaMod);
		FuncionesTableView.restringirSimbolos(nombreDibujanteMod);
		FuncionesTableView.restringirSimbolos(nombreVarianteMod);
		
		listas_autocompletado();
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
	}
	
	/**
	 * Rellena los ComboBox con opciones estáticas.
	 */
	public void rellenarCombosEstaticos() {
	    List<ComboBox<String>> comboboxesMod = Arrays.asList(nombreFormatoMod, nombreProcedenciaMod, estadoComic);
	    funcionesCombo.rellenarComboBoxEstaticos(comboboxesMod);
	}

	/**
	 * Asigna tooltips a elementos en la interfaz gráfica.
	 */
	public void asignarTooltips() {
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
	    elementos.add(nombreKeyIssue);
	    
	    FuncionesTooltips.asignarTooltips(elementos);
	}

	/**
	 * Rellena automáticamente algunos campos basados en el ID del cómic.
	 */
	public void autoRelleno() {
	    idComicMod.textProperty().addListener((observable, oldValue, newValue) -> {
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
	                    comic_temp = libreria.comicDatos(idComicMod.getText());
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	                
	                idComicMod.setText(idComicMod.getText());
	                // Limpiar selecciones previas en los ComboBox
	                numeroComicMod.getSelectionModel().clearSelection();
	                nombreFormatoMod.getSelectionModel().clearSelection();
	                numeroCajaMod.getSelectionModel().clearSelection();
	                
	                nombreComicMod.setText(comic_temp.getNombre());
	                // ... Otros campos ...
	                
	                prontInfo.clear();
	                prontInfo.setOpacity(1);
	                try {
	                    prontInfo.setText(libreria.comicDatos(idComicMod.getText()).toString().replace("[", "").replace("]", ""));
	                    imagencomic.setImage(libreria.selectorImage(idComicMod.getText()));
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	                imagencomic.setImage(libreria.selectorImage(idComicMod.getText()));
	            } else {
	                borrar_datos_mod();
	            }
	        } else {
	            borrar_datos_mod();
	        }
	    });
	}

	/**
	 * Asigna autocompletado a campos de texto en la interfaz.
	 */
	public void listas_autocompletado() {
	    TextFields.bindAutoCompletion(nombreComicMod, DBLibreriaManager.listaNombre);
	    TextFields.bindAutoCompletion(nombreVarianteMod, DBLibreriaManager.listaVariante);
	    TextFields.bindAutoCompletion(nombreFirmaMod, DBLibreriaManager.listaFirma);
	    TextFields.bindAutoCompletion(nombreEditorialMod, DBLibreriaManager.listaEditorial);
	    TextFields.bindAutoCompletion(nombreGuionistaMod, DBLibreriaManager.listaGuionista);
	    TextFields.bindAutoCompletion(nombreDibujanteMod, DBLibreriaManager.listaDibujante);
	    TextFields.bindAutoCompletion(numeroComicMod.getEditor(), DBLibreriaManager.listaNumeroComic);
	    TextFields.bindAutoCompletion(nombreProcedenciaMod.getEditor(), DBLibreriaManager.listaProcedencia);
	    TextFields.bindAutoCompletion(nombreFormatoMod.getEditor(), DBLibreriaManager.listaFormato);
	    TextFields.bindAutoCompletion(numeroCajaMod.getEditor(), DBLibreriaManager.listaCaja);
	}


	/**
	 * Funcion que permite restringir entrada de datos de todo aquello que no sea un
	 * numero entero en los comboBox numeroComic y caja_comic
	 */
	public void restringir_entrada_datos() {
		idComicMod.setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroComicMod.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroCaja.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroCajaMod.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		precioComic.setTextFormatter(FuncionesComboBox.validador_Ndecimales());
	}

	/**
	 * Permite rellenar los datos de los comboBox con los datos de las listas
	 */


	public void borrar_datos() {

		// Clear all ComboBox text fields and values
		for (ComboBox<String> comboBox : Arrays.asList(nombreComic, numeroComic, nombreFirma, nombreGuionista,
				nombreVariante, numeroCaja, nombreProcedencia, nombreFormato, nombreEditorial, nombreDibujante)) {
			comboBox.setValue("");
			comboBox.getEditor().setText("");
		}

		// Clear additional UI elements
		fechaPublicacion.setValue(null);
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagencomic.setImage(null);

		rellenarCombosEstaticos();
	}

	public void borrar_datos_mod() {
//		idComicMod.setText("");
		nombreComicMod.setText("");

		numeroComicMod.setValue("");
		numeroComicMod.getEditor().setText("");

		nombreVarianteMod.setText("");
		nombreFirmaMod.setText("");
		nombreEditorialMod.setText("");

		nombreFormatoMod.setValue("");
		nombreFormatoMod.getEditor().setText("");

		nombreProcedenciaMod.setValue("");
		nombreProcedenciaMod.getEditor().setText("");

		anioPublicacionMod.setValue(null);
		nombreGuionistaMod.setText("");
		nombreDibujanteMod.setText("");
		nombreKeyIssue.setText("");

		numeroCajaMod.setValue("");
		numeroCajaMod.getEditor().setText("");

		nombreKeyIssue.setText("");
		direccionImagen.setText("");
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		botonNuevaPortada.setStyle(null);
		imagencomic.setImage(null);
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
		String id_comic;
		Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();
		prontInfo.setStyle("");

		if (idRow != null) {
			id_comic = idRow.getID();

			idComicMod.setStyle("");
			idComicMod.setText(id_comic);

			Comic comic_temp = libreria.comicDatos(id_comic);

			nombreComicMod.setText(comic_temp.getNombre());

			String numeroNuevo = comic_temp.getNumero();
			numeroComicMod.getSelectionModel().select(numeroNuevo);

			nombreVarianteMod.setText(comic_temp.getVariante());

			nombreFirmaMod.setText(comic_temp.getFirma());

			nombreEditorialMod.setText(comic_temp.getEditorial());

			String formato = comic_temp.getFormato();
			nombreFormatoMod.getSelectionModel().select(formato);

			String procedencia = comic_temp.getProcedencia();
			nombreProcedenciaMod.getSelectionModel().select(procedencia);

			String fechaString = comic_temp.getFecha();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate fecha = LocalDate.parse(fechaString, formatter);
			anioPublicacionMod.setValue(fecha);

			nombreGuionistaMod.setText(comic_temp.getGuionista());

			nombreDibujanteMod.setText(comic_temp.getDibujante());

			String cajaAni = comic_temp.getNumCaja();
			numeroCajaMod.getSelectionModel().select(cajaAni);

			nombreKeyIssue.setText(comic_temp.getKey_issue());
			estadoComic.getSelectionModel().select(comic_temp.getEstado());

			precioComic.setText(comic_temp.getPrecio_comic());
			urlReferencia.setText(comic_temp.getUrl_referencia());

			prontInfo.setOpacity(1);
			prontInfo.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));
			imagencomic.setImage(libreria.selectorImage(id_comic));
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
			String id_comic;

			Comic idRow = tablaBBDD.getSelectionModel().getSelectedItem();

			if (idRow != null) {
				id_comic = idRow.getID();

				idComicMod.setStyle("");
				idComicMod.setText(id_comic);

				Comic comic_temp = libreria.comicDatos(id_comic);

				nombreComicMod.setText(comic_temp.getNombre());

				String numeroNuevo = comic_temp.getNumero();
				numeroComicMod.getSelectionModel().select(numeroNuevo);

				nombreVarianteMod.setText(comic_temp.getVariante());

				nombreFirmaMod.setText(comic_temp.getFirma());

				nombreEditorialMod.setText(comic_temp.getEditorial());

				String formato = comic_temp.getFormato();
				nombreFormatoMod.getSelectionModel().select(formato);

				String procedencia = comic_temp.getProcedencia();
				nombreProcedenciaMod.getSelectionModel().select(procedencia);

				String fechaString = comic_temp.getFecha();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				LocalDate fecha = LocalDate.parse(fechaString, formatter);
				anioPublicacionMod.setValue(fecha);

				nombreGuionistaMod.setText(comic_temp.getGuionista());

				nombreDibujanteMod.setText(comic_temp.getDibujante());

				String cajaAni = comic_temp.getNumCaja();
				numeroCajaMod.getSelectionModel().select(cajaAni);

				nombreKeyIssue.setText(comic_temp.getKey_issue());
				estadoComic.getSelectionModel().select(comic_temp.getEstado());

				precioComic.setText(comic_temp.getPrecio_comic());
				urlReferencia.setText(comic_temp.getUrl_referencia());

				prontInfo.setOpacity(1);
				prontInfo.setText(libreria.comicDatos(id_comic).toString().replace("[", "").replace("]", ""));
				imagencomic.setImage(libreria.selectorImage(id_comic));
			}
			DBManager.resetConnection();
		}
	}

	/**
	 * Llamada a funcion que modifica los datos de 1 comic en la base de datos.
	 *
	 * @param event
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	@FXML
	void modificarDatos(ActionEvent event) throws NumberFormatException, SQLException, IOException {

		imagencomic.setImage(null);
		libreria = new DBLibreriaManager();
		modificacionComic(); // Llamada a funcion que modificara el contenido de un comic especifico.
		libreria.reiniciarBBDD();
		direccionImagen.setText("");
		libreria.listasAutoCompletado();
		funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		funcionesTabla.tablaBBDD(libreria.libreriaCompleta(), tablaBBDD, columnList); // Llamada a funcion
	}

	// Funcion que permite seleccionar una imagen para subirla junto al comic
	@FXML
	void nuevaPortada(ActionEvent event) {
		imagencomic.setImage(null);
		subirPortada();
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
	 * Funcion que permite seleccionar en el comboBox "numeroComic" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String numeroComicNuevo() {
		String numComic = "";

		if (numeroComicMod.getSelectionModel().getSelectedItem() != null) {
			numComic = numeroComicMod.getSelectionModel().getSelectedItem().toString();
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

		String cajaComics = "0";

		if (numeroCaja.getSelectionModel().getSelectedItem() != null) {
			cajaComics = numeroCaja.getSelectionModel().getSelectedItem().toString();
		}

		return cajaComics;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String cajaNueva() {

		String cajaComics = "0";

		if (numeroCajaMod.getSelectionModel().getSelectedItem() != null) {
			cajaComics = numeroCajaMod.getSelectionModel().getSelectedItem().toString();
		}

		return cajaComics;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreFormato" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String formatoNuevo() {

		String formatoEstado = nombreFormatoMod.getSelectionModel().getSelectedItem().toString();
		return formatoEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedenciaMod() {

		String procedenciaEstadoNuevo = nombreProcedenciaMod.getSelectionModel().getSelectedItem().toString();
		return procedenciaEstadoNuevo;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String estadoNuevo() {

		String estadoNuevo = estadoComic.getSelectionModel().getSelectedItem().toString();

		return estadoNuevo;
	}

	/**
	 * Limpia todos los datos en pantalla.
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		idComicMod.setText("");
		borrar_datos_mod();
		borrar_datos();
	}

	/**
	 * Maneja la acción de mostrar los cómics considerados "Key Issue".
	 *
	 * @param event El evento que desencadenó esta acción.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	@FXML
	void comicsKeyIssue(ActionEvent event) throws SQLException {
	    prontInfo.setOpacity(0); // Ocultar la información en pantalla
	    libreria = new DBLibreriaManager(); // Crear una instancia del gestor de la base de datos
	    libreria.reiniciarBBDD(); // Reiniciar la base de datos si es necesario
	    funcionesTabla.nombreColumnas(columnList, tablaBBDD); // Llamada a la función para establecer nombres de columnas
	    funcionesTabla.tablaBBDD(libreria.libreriaKeyIssue(), tablaBBDD, columnList); // Llamada a la función para llenar la tabla con cómics "Key Issue"
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

	/////////////////////////////////
	//// FUNCIONES////////////////////
	/////////////////////////////////

	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public String[] camposComicActuales() {
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

	/**
	 * Devuelve un array con los datos de los TextField correspondientes a la los
	 * comics se van a modificar
	 *
	 * @return
	 */
	public String[] camposComicModificar() {

		Utilidades utilidad = new Utilidades();

		String campos[] = new String[17];

		campos[0] = idComicMod.getText();

		campos[1] = nombreComicMod.getText();

		campos[2] = numeroComicNuevo();

		campos[3] = utilidad.comaPorGuion(nombreVarianteMod.getText());

		campos[4] = utilidad.comaPorGuion(nombreFirmaMod.getText());

		campos[5] = nombreEditorialMod.getText();

		campos[6] = formatoNuevo();

		campos[7] = procedenciaMod();

		LocalDate fecha = anioPublicacionMod.getValue();
		campos[8] = (fecha != null) ? fecha.toString() : "";

		campos[9] = utilidad.comaPorGuion(nombreGuionistaMod.getText());

		campos[10] = utilidad.comaPorGuion(nombreDibujanteMod.getText());

		campos[11] = direccionImagen.getText();

		campos[12] = estadoNuevo();

		campos[13] = cajaNueva();

		campos[14] = nombreKeyIssue.getText();

		if (campos[14].isEmpty() || campos[14] == null) {
			campos[14] = "";
		}

		campos[15] = urlReferencia.getText();

		campos[16] = precioComic.getText();

		return campos;
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException {
		funcionesTabla.modificarColumnas(tablaBBDD,columnList);
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
		funcionesTabla.modificarColumnas(tablaBBDD,columnList);

		idComicMod.setText("");
		borrar_datos_mod();
		borrar_datos();
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
		String datos[] = camposComicActuales();

		Comic comic = new Comic("", datos[1], datos[11], datos[2], datos[3], datos[4], datos[5], datos[6], datos[7],
				datos[8], datos[9], datos[10], "", "", "", null, "", "");

		funcionesTabla.tablaBBDD(libreria.busquedaParametro(comic, busquedaGeneral.getText()), tablaBBDD, columnList); // Llamada a funcion
		prontInfo.setText(funcionesTabla.resultadoBusquedaPront(comic).getText());
		busquedaGeneral.setText("");
	}

	/**
	 * Permite abir una ventana para abrir ficheros de un determinado formato.
	 *
	 * @return
	 */
	public FileChooser tratarFichero() {
		FileChooser fileChooser = new FileChooser(); // Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Subiendo imagen", "*.jpg"));

		return fileChooser;
	}

	/**
	 * Funcion que escribe en el TextField de "Direccion de imagen" la dirrecion de
	 * la imagen
	 */
	public void subirPortada() {
		File file = tratarFichero().showOpenDialog(null); // Llamada a funcion
		if (file != null) {
			direccionImagen.setText(file.getAbsolutePath().toString());
		} else {
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Has cancelado la subida de portada.");
		}
	}


	/**
	 * Funcion que permite modificar un comic, segun los datos introducidos
	 * 
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	public void modificacionComic() throws NumberFormatException, SQLException, IOException {
		libreria = new DBLibreriaManager();
		Comic comic_temp = new Comic();
		Image imagen = null;
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + DBManager.DB_NAME
				+ File.separator + "portadas";
		Utilidades.convertirNombresCarpetas(sourcePath);

		if (nav.alertaModificar()) {

			String datos[] = camposComicModificar();

			String id_comic = datos[0];

			if (libreria.checkID(id_comic)) {
				comic_temp = libreria.comicDatos(id_comic);

				String nombre = "";

				String numero = "";

				String variante = "";

				String firma = "";

				String editorial = "";

				String formato = "";

				String procedencia = "";

				String fecha = "";

				String guionista = datos[9];

				String dibujante = datos[10];

				String estado = datos[12];

				String numCaja = "";

				String portada = "";

				String puntuacion = "";

				String nombreKeyIssue = "";

				String url_referencia = "";

				String precio_comic = "";

				if (datos[1].isEmpty()) {
					nombre = comic_temp.getNombre();
				} else {
					nombre = datos[1];
				}

				if (datos[2].isEmpty()) {
					numero = comic_temp.getNumero();
				} else {
					numero = datos[2];
				}

				if (datos[3].isEmpty()) {
					variante = comic_temp.getVariante();
				} else {
					variante = datos[3];
				}

				if (datos[4].isEmpty()) {
					firma = comic_temp.getFirma();
				} else {
					firma = datos[4];
				}

				if (datos[5].isEmpty()) {
					editorial = comic_temp.getEditorial();
				} else {
					editorial = datos[5];
				}

				if (datos[6].isEmpty()) {
					formato = comic_temp.getFormato();
				} else {
					formato = datos[6];
				}

				if (datos[7].isEmpty()) {
					procedencia = comic_temp.getProcedencia();
				} else {
					procedencia = datos[7];
				}

				if (datos[8].isEmpty()) {
					fecha = comic_temp.getFecha();
				} else {
					fecha = datos[8];
				}

				if (datos[9].isEmpty()) {
					guionista = comic_temp.getGuionista();
				} else {
					guionista = datos[9];
				}

				if (datos[10].isEmpty()) {
					dibujante = comic_temp.getDibujante();
				} else {
					dibujante = datos[10];
				}

				if (datos[11].isEmpty()) {
					portada = comic_temp.getImagen();
					imagen = new Image(portada);
				} else {
					portada = datos[11];
					imagen = new Image(portada);
				}

				if (datos[12].isEmpty()) {
					estado = comic_temp.getEstado();
				} else {
					estado = datos[12];
				}

				if (datos[13].isEmpty()) {
					numCaja = comic_temp.getNumCaja();

				} else {
					numCaja = datos[13];
				}

				if (!comic_temp.getPuntuacion().equals("Sin puntuar")) {
					puntuacion = comic_temp.getPuntuacion();
				} else {
					puntuacion = "Sin puntuar";
				}

				nombreKeyIssue = "Vacio";
				String key_issue_sinEspacios = datos[14].trim();

				Pattern pattern = Pattern.compile(".*\\w+.*");
				Matcher matcher = pattern.matcher(key_issue_sinEspacios);

				if (!key_issue_sinEspacios.isEmpty() && matcher.matches()) {
					nombreKeyIssue = key_issue_sinEspacios;
				}

				if (datos[15].isEmpty()) {
					url_referencia = comic_temp.getUrl_referencia();
				} else {
					url_referencia = datos[15];
				}

				if (datos[16].isEmpty()) {
					precio_comic = comic_temp.getPrecio_comic();
				} else {
					precio_comic = datos[16];
				}

				double valor_comic = Double.parseDouble(precio_comic);

				precio_comic = String.valueOf(Utilidades.convertirMonedaADolar(procedencia, valor_comic));

				Comic comic = new Comic(id_comic, nombre, numCaja, numero, variante, firma, editorial, formato,
						procedencia, fecha, guionista, dibujante, estado, nombreKeyIssue, puntuacion, portada,
						url_referencia, precio_comic);

				if (id_comic.length() == 0 || !libreria.checkID(id_comic) || nombre.length() == 0
						|| numero.length() == 0 || editorial.length() == 0 || guionista.length() == 0
						|| dibujante.length() == 0 || procedencia.length() == 0) {

					String excepcion = "ERROR.Faltan datos por rellenar";
					nav.alertaException(excepcion);
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #F53636");
					prontInfo.setText("Error. Debes de introducir los datos correctos");
				} else {
					libreria.actualizarComic(comic);
					Utilidades.eliminarFichero(comic_temp.getImagen());

					imagencomic.setImage(imagen);
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #A0F52D");
					prontInfo.setText(
							"Has modificado correctamente: " + comic.toString().replace("[", "").replace("]", ""));
					libreria.listasAutoCompletado();
				}
			} else {
				String excepcion = "No puedes modificar un comic si antes no pones un ID valido";
				nav.alertaException(excepcion);
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("Error. Debes de introducir un ID valido");
			}
		} else {
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Se ha cancelado la modificacion del comic.");
		}
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