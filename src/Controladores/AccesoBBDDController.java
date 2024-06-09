/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gluonhq.charm.glisten.control.ProgressIndicator;

import alarmas.AlarmaList;
import apisFunciones.FuncionesApis;
import dbmanager.ConectManager;
import dbmanager.DatabaseManagerDAO;
import dbmanager.ListaComicsDAO;
import ficherosFunciones.FuncionesFicheros;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.FuncionesManejoFront;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Esta clase sirve para acceder a la base de datos y poder realizar diferentes
 * funciones.
 *
 * @author Alejandro Rodriguez
 */
public class AccesoBBDDController implements Initializable {

	/**
	 * Etiqueta para mostrar una alarma relacionada con la conexión.
	 */
	@FXML
	private Label alarmaConexion;

	/**
	 * Etiqueta para mostrar una alarma relacionada con la conexión a Internet.
	 */
	@FXML
	private Label alarmaConexionInternet;

	/**
	 * Etiqueta para mostrar una alarma relacionada con la conexión a la base de
	 * datos SQL.
	 */
	@FXML
	private Label alarmaConexionSql;

	/**
	 * Botón para acceder a la base de datos.
	 */
	@FXML
	private Button botonAccesobbdd;

	/**
	 * Botón para acceder a la base de datos en línea.
	 */
	@FXML
	private Button botonAccesobbddOnline;

	/**
	 * Botón para enviar datos.
	 */
	@FXML
	private Button botonEnviar;

	/**
	 * Botón para acceder a las opciones.
	 */
	@FXML
	private Button botonOpciones;

	/**
	 * Botón para salir de la aplicación.
	 */
	@FXML
	private Button botonSalir;

	/**
	 * Casilla de verificación para recordar la configuración.
	 */
	@FXML
	private CheckBox checkRecordar;

	/**
	 * Etiqueta para mostrar el servidor.
	 */
	@FXML
	private Label labelServidor;

	/**
	 * Etiqueta para mostrar el nombre de usuario.
	 */
	@FXML
	private Label nomUsuarioLabel;

	/**
	 * Campo de texto para ingresar el nombre de usuario.
	 */
	@FXML
	private TextField nomUsuarioText;

	/**
	 * Etiqueta para mostrar la contraseña del usuario.
	 */
	@FXML
	private Label passUsuarioLabel;

	/**
	 * Campo de contraseña para ingresar la contraseña del usuario.
	 */
	@FXML
	private PasswordField passUsuarioText;

	/**
	 * Etiqueta para mostrar el estado de la conexión.
	 */
	@FXML
	private Label prontEstadoConexion;

	/**
	 * Botón de alternancia para cambiar entre dos estados.
	 */
	@FXML
	private ToggleButton toogleButton;

	/**
	 * Botón de alternancia para mostrar u ocultar la contraseña.
	 */
	@FXML
	private ToggleButton toggleEye;

	/**
	 * Vista de imagen para el botón de alternancia para mostrar u ocultar la
	 * contraseña.
	 */
	@FXML
	private ImageView toggleEyeImageView;

	/**
	 * Campo de texto para ingresar la contraseña del usuario.
	 */
	@FXML
	private TextField passUsuarioTextField;

	@FXML
	private ProgressIndicator progresoCarga;

	/**
	 * Estado del botón de alternancia del ojo.
	 */
	private boolean estadoOjo = false;

	/**
	 * Objeto para gestionar las ventanas de navegación.
	 */
	private static Ventanas nav = new Ventanas();

	private static AlarmaList alarmaList = new AlarmaList();

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		System.out.println(Utilidades.verificarVersionJava());

		FuncionesApis.guardarDatosClavesMarvel();

		FuncionesApis.guardarApiComicVine();

		alarmaList.setAlarmaConexion(alarmaConexion);
		alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.setAlarmaConexionPrincipal(prontEstadoConexion);

		List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();

		for (Stage stage : stageVentanas) {
			stage.close(); // Close the stage if it's not the current state
		}

		Platform.runLater(() -> {

			myStage().setOnCloseRequest(event -> stop());
			Utilidades.crearDBPRedeterminada();

			ConectManager.asignarValoresPorDefecto();
			ListaComicsDAO.reiniciarListas();
			ConectManager.close();

			FuncionesApis.comprobarApisComics();

			alarmaList.iniciarThreadChecker();

			if (Utilidades.isInternetAvailable()) {
				Utilidades.cargarTasasDeCambioDesdeArchivo();
			}

			// Crear estructura si no existe
			FuncionesFicheros.crearEstructura();
			Utilidades.crearCarpeta();
			ConectManager.closeConnection();
		});

	}

	/**
	 * Maneja el evento de entrar en línea. Guarda el usuario y contraseña si se
	 * seleccionó la opción "Recordar".
	 *
	 * @param event El evento de acción que desencadenó la función.
	 * @throws IOException Si ocurre un error de entrada/salida.
	 */
	@FXML
	void entrarOnline(ActionEvent event) throws IOException {

		if (checkRecordar.isSelected()) {
			String valorPass;
			if (estadoOjo) {
				valorPass = passUsuarioTextField.getText();
				passUsuarioText.setText(valorPass);
			} else {
				valorPass = passUsuarioText.getText();
				passUsuarioTextField.setText(valorPass);
			}

			Utilidades.guardarUsuario(nomUsuarioText, valorPass);
		}
	}

	/**
	 * Funcion que permite el acceso a la ventana de menuPrincipal
	 *
	 * @param event
	 */
	@FXML
	void entrarMenu(ActionEvent event) {

		if (ConectManager.estadoConexion) { // Siempre que el metodo de la clase DBManager sea true, permitira acceder
			ConectManager.resetConnection();
			// al menu principal
			nav.verMenuPrincipal();
			closeWindow();
		} else { // En caso contrario mostrara el siguiente mensaje.
			AlarmaList.detenerAnimacion();
			ConectManager.asignarValoresPorDefecto();
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			String mensaje = "ERROR. Conectate primero";
			AlarmaList.iniciarAnimacionConexionError(prontEstadoConexion, mensaje);
		}
		alarmaList.detenerThreadChecker();
	}

	/**
	 * Maneja el evento de enviar datos a la base de datos.
	 *
	 * @param event El evento de acción que desencadenó la función.
	 * @throws SQLException
	 */
	@FXML
	void enviarDatos(ActionEvent event) {

		String datosFichero = FuncionesFicheros.datosEnvioFichero();

		if (Utilidades.comprobarDB() && ConectManager.loadDriver()
				&& DatabaseManagerDAO.checkTablesAndColumns(datosFichero)) {

			if (ConectManager.conexion() != null) {

				AlarmaList.detenerAnimacion();
				AlarmaList.iniciarAnimacionConectado(prontEstadoConexion);
				alarmaList.manejarConexionExitosa(prontEstadoConexion);
			} else {
				AlarmaList.detenerAnimacion();
				String mensaje = "ERROR. No estás conectado a la base de datos.";
				AlarmaList.iniciarAnimacionConexionError(prontEstadoConexion, mensaje);
			}
		} else {
			AlarmaList.detenerAnimacion();
			String mensaje1 = "ERROR. Ve a opciones y guarda la base de datos o en su defecto, crea otra.";
			String mensaje2 = "ERROR. Noo hay guardada niguna DB, ve a opciones.";
			nav.alertaException(mensaje1);
			AlarmaList.iniciarAnimacionConexionError(prontEstadoConexion, mensaje2);
		}

	}

	public Scene miStageVentana() {

		return botonEnviar.getScene();

	}

	/**
	 * Permite salir completamente del programa.
	 *
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {

		if (nav.salirPrograma(event)) { // Llamada a metodo que permite salir completamente del programa
			myStage().close();
			stop();
		}
	}

	public Stage myStage() {
		return (Stage) this.botonSalir.getScene().getWindow();

	}

	/**
	 * Método que se ejecuta al hacer clic en el botón de opciones del programa.
	 * Abre la ventana de opciones y cierra la ventana actual.
	 */
	@FXML
	void opcionesPrograma(ActionEvent event) {

		nav.verOpciones();
		alarmaList.detenerThreadChecker();
		myStage().close();
	}

	/**
	 * Cierra el programa a la fuerza correctamente.
	 */
	public void closeWindow() { // Metodo que permite cerrar completamente el programa en caso de cerrar a la //

		myStage().close();
	}

	public void stop() {
		Platform.exit();
	}
}
