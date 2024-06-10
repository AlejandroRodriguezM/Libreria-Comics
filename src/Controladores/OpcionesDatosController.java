/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
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
 *  Version 8.0.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import alarmas.AlarmaList;
import dbmanager.SQLiteManager;
import ficherosFunciones.FuncionesFicheros;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Esta clase sirve para configurar los datos de la base de datos para que el
 * programa pueda operar correctamente
 *
 * @author Alejandro Rodriguez
 */
public class OpcionesDatosController implements Initializable {

	@FXML
	private Pane panelMenu;

	@FXML
	private Label alarmaConexion;

	@FXML
	private Label alarmaConexionInternet;

	@FXML
	private Label alarmaConexionSql;

	@FXML
	private Button botonAbrir;

	@FXML
	private Button botonCrearBBDD;

	@FXML
	private Button botonGuardar;

	@FXML
	private Button botonRestaurar;

	@FXML
	private Button botonMenuBBDD;

	@FXML
	private ComboBox<String> nombreBBDD;

	@FXML
	private Label nombreLabel;

	@FXML
	private Label prontEstadoFichero;

	@FXML
	private Label prontInformativo;

	@FXML
	private Label labelInfoBBDD;

	@FXML
	private Rectangle barraSeparacion;

	@FXML
	private TextField nombreNuevaBBDD;

	@FXML
	private ImageView imagenAzul;

	// Tamaño original del Pane
	private final double originalHeight = 333;

	// Tamaño reducido del Pane
	private final double expandHeight = 490;

	boolean estaDesplegado = false;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();
	private static AlarmaList alarmaList = new AlarmaList();

	private static final String DB_FOLDER = System.getProperty("user.home") + File.separator + "AppData"
			+ File.separator + "Roaming" + File.separator + "libreria" + File.separator;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		alarmaList.setAlarmaConexion(alarmaConexion);
		alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.iniciarThreadChecker();
		FuncionesFicheros.crearEstructura();

		rellenarComboDB();
		String datosFichero = FuncionesFicheros.datosEnvioFichero();
		seleccionarValor(nombreBBDD,datosFichero);

		AlarmaList.iniciarAnimacionEspera(prontEstadoFichero);
		AlarmaList.iniciarAnimacionEspera(prontInformativo);
		AlarmaList.iniciarAnimacionAlarma(alarmaConexion);

	}

	/**
	 * Rellena el ComboBox con las bases de datos .db disponibles en el directorio
	 * DB_FOLDER.
	 */
	private void rellenarComboDB() {
		File directorio = new File(DB_FOLDER);
		File[] ficheros = directorio.listFiles();

		vaciarComboBox(nombreBBDD);

		if (ficheros != null) {
			List<String> basesDatos = new ArrayList<>();
			for (File fichero : ficheros) {
				if (fichero.isFile() && fichero.getName().toLowerCase().endsWith(".db")) {
					basesDatos.add(fichero.getName());
				}
			}
			// Ordenar los nombres alfabéticamente
			basesDatos.sort(String::compareToIgnoreCase);

			// Agregar las bases de datos al ComboBox
			nombreBBDD.getItems().addAll(basesDatos);

			// Seleccionar la primera opción si existe
			if (!basesDatos.isEmpty()) {
				nombreBBDD.getSelectionModel().select(0);
			}
		} else {
			nombreBBDD.getItems().clear();
		}
	}

	private void vaciarComboBox(ComboBox<String> comboBox) {
		ObservableList<String> items = FXCollections.observableArrayList();
		comboBox.setItems(items);
	}

	/**
	 * Abre la ubicación de la carpeta "libreria" en el sistema de archivos del
	 * usuario.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	void abrirUbicacion(ActionEvent event) {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming" + File.separator
				+ "libreria";

		File carpeta = new File(ubicacion);

		if (Desktop.isDesktopSupported() && carpeta.exists()) {
			try {
				Desktop.getDesktop().open(carpeta);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Función que guarda los datos de la nueva base de datos.
	 */
	public String datosBBDD() {
		String dbNombre = nombreNuevaBBDD.getText();

		if (comprobarEntradas(dbNombre)) {
			return dbNombre;
		}
		return "";
	}

	/**
	 * Función que permite comprobar si las entradas están rellenas o no.
	 *
	 * @param dbNombre el nombre de la base de datos a comprobar
	 * @return true si hay errores, false en caso contrario
	 */
	public boolean comprobarEntradas(String dbNombre) {
		String errorMessage = "";

		if (dbNombre.isEmpty()) {
			errorMessage += "El nombre de la base de datos está vacío.\n";
		} else if (!esNombreValido(dbNombre)) {
			errorMessage += "El nombre de la base de datos contiene caracteres no permitidos.\n";
		}

		if (!errorMessage.isEmpty()) {
			prontInformativo.setStyle("-fx-background-color: #DD370F");
			AlarmaList.iniciarAnimacionBaseError(errorMessage, prontInformativo);
			return false;
		}
		return true;
	}

	public static boolean esNombreValido(String nombre) {
		// Expresión regular para caracteres inválidos
		String invalidChars = "[,.:;<>\"'/\\\\|?*]";

		// Patrón para la expresión regular
		Pattern pattern = Pattern.compile(invalidChars);

		// Matcher para buscar coincidencias
		Matcher matcher = pattern.matcher(nombre);

		// Devuelve true si no hay coincidencias (es decir, no hay caracteres inválidos)
		return !matcher.find();
	}

	/**
	 * Abre la ventana para crear la base de datos.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	void crearBBDD(ActionEvent event) {
		String dbNombre = datosBBDD();

		if (!dbNombre.isEmpty()) {
			if (!SQLiteManager.checkDatabaseExists(dbNombre)) {
				AlarmaList.iniciarAnimacionBaseExiste(prontInformativo, dbNombre);
			} else {
				SQLiteManager.createTable(dbNombre);
				Utilidades.crearCarpeta();
				AlarmaList.iniciarAnimacionBaseCreada(prontInformativo, dbNombre);
				rellenarComboDB();

				seleccionarValor(nombreBBDD, dbNombre + ".db");
			}
		} else {
			String errorMessage = "El nombre de la base de datos está vacío.\n";
			AlarmaList.iniciarAnimacionBaseError(errorMessage, prontInformativo);
		}
	}

	// Método para seleccionar un valor en el ComboBox según el contenido
	private void seleccionarValor(ComboBox<String> comboBox, String contenido) {
		ObservableList<String> items = comboBox.getItems();
		for (String item : items) {
			if (item.contains(contenido)) {
				comboBox.setValue(item);
				break;
			}
		}
	}

	/**
	 * Guarda los datos en un archivo de configuración y realiza otras tareas
	 * relacionadas.
	 *
	 * @param event el evento que desencadena la acción
	 * @throws SQLException
	 */
	@FXML
	void guardarDatos(ActionEvent event) {
		String nombredb = nombreBBDD.getValue();

		FuncionesFicheros.guardarDatosBaseLocal(nombredb, prontEstadoFichero, alarmaConexion);

	}

	/**
	 * Restaura la configuración a los valores predeterminados.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	void restaurarConfiguracion(ActionEvent event) {
		if (nav.borrarContenidoConfiguracion()) {
			String userHome = System.getProperty("user.home");
			String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
			String ficheroLocal = ubicacion + File.separator + "libreria" + File.separator + "configuracion_local.conf";

			// Verificar y eliminar los archivos dentro de la carpeta "libreria"
			File ficheroConfiguracionLocal = new File(ficheroLocal);

			ficheroConfiguracionLocal.delete();

			FuncionesFicheros.crearEstructura();

			limpiarDatos();
			AlarmaList.detenerAnimacion();
			prontEstadoFichero.setStyle("-fx-background-color: #f5af2d");
			alarmaList.iniciarAnimacionRestaurado(prontEstadoFichero);
		} else {
			AlarmaList.detenerAnimacion();
			prontEstadoFichero.setStyle("-fx-background-color: #DD370F");
			alarmaList.iniciarAnimacionRestauradoError(prontEstadoFichero);
		}
	}

	// Función para cambiar la altura del Pane
	private void setPaneHeight(double height) {
		imagenAzul.setFitHeight(height + 3);
		imagenAzul.setFitWidth(269);

		miSceneVentana().getWindow().setHeight(height);

	}

	public void restoreOriginalSize() {
		setPaneHeight(originalHeight);
		botonMenuBBDD.setText("Abrir Menu creacion DB");
		mostrarElementos(false);
		cambiarPosicionOriginal();
		nombreNuevaBBDD.setText("");
		AlarmaList.iniciarAnimacionEspera(prontInformativo);
	}

	public void expandPane() {
		setPaneHeight(expandHeight);
		botonMenuBBDD.setText("Cerrar Menu creacion DB");
		mostrarElementos(true);
		cambiarPosicionExpandido();
	}

	@FXML
	void desplegarMenuDB(ActionEvent event) {

		Platform.runLater(() -> {
			if (estaDesplegado) {
				restoreOriginalSize();
				estaDesplegado = false;
			} else {
				expandPane();
				estaDesplegado = true;
			}
		});
	}

	// Función para hacer visibles y habilitar los nodos
	public void mostrarElementos(boolean esVisible) {
		ObservableList<javafx.scene.Node> nodos = FXCollections.observableArrayList(nombreNuevaBBDD, labelInfoBBDD,
				botonCrearBBDD, prontInformativo, barraSeparacion);

		// Iterar sobre la lista de nodos y hacerlos visibles y habilitados
		nodos.forEach(nodo -> {
			nodo.setVisible(esVisible);
			nodo.setDisable(!esVisible);
		});
	}

	public void cambiarPosicionOriginal() {
		nombreNuevaBBDD.setLayoutY(165);

		labelInfoBBDD.setLayoutY(165);

		botonCrearBBDD.setLayoutY(230);

		prontInformativo.setLayoutY(195);

		barraSeparacion.setLayoutY(143);
	}

	public void cambiarPosicionExpandido() {
		nombreNuevaBBDD.setLayoutY(323);

		labelInfoBBDD.setLayoutY(323);

		botonCrearBBDD.setLayoutY(388);

		prontInformativo.setLayoutY(353);

		barraSeparacion.setLayoutY(301);
	}

	/**
	 * Limpia los datos en los campos de texto.
	 */
	public void limpiarDatos() {

		nombreBBDD.getSelectionModel().clearSelection();
	}

	public Scene miSceneVentana() {

		return botonGuardar.getScene();

	}

	private Stage myStage() {
		return (Stage) miSceneVentana().getWindow();
	}

	/**
	 * Cierra el programa a la fuerza correctamente.
	 */
	public void closeWindows() {

		myStage().close();
	}

	public void stop() {
		alarmaList.detenerThreadChecker();
		closeWindows();
	}

}