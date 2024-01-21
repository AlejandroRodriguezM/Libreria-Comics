/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import alarmas.AlarmaList;
import dbmanager.ConectManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
	 * Botón para descargar la base de datos.
	 */
	@FXML
	private Button botonDescargaBBDD;

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

	/**
	 * Estado del botón de alternancia del ojo.
	 */
	private boolean estadoOjo = false;

	/**
	 * Objeto para gestionar las ventanas de navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Utilidades.guardarDatosClavesMarvel();
		Utilidades.cargarTasasDeCambioDesdeArchivo();
		Utilidades.guardarApiComicVine();

		AlarmaList alarmaList = new AlarmaList();

		alarmaList.setAlarmaConexion(alarmaConexion);
		alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.setAlarmaConexionPrincipal(prontEstadoConexion);

		alarmaList.iniciarThreadChecker();

		Utilidades.crearEstructura();

		Utilidades.comprobarApisComics();

	}

	/**
	 * Funcion para abrir el navegador y acceder a la URL
	 *
	 * @param event
	 */
	@FXML
	void accesoMySqlWorkbench(ActionEvent event) {
		String url1 = "https://dev.mysql.com/downloads/windows/installer/8.0.html";
		String url2 = "https://www.youtube.com/watch?v=FvXQBKsp0OI&ab_channel=MisterioRojo";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url1); // Llamada a funcion
			Utilidades.accesoWebWindows(url2); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url1); // Llamada a funcion
				Utilidades.accesoWebLinux(url2); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url1); // Llamada a funcion
				Utilidades.accesoWebMac(url2); // Llamada a funcion
			}
		}
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
	 * Configura el formulario en línea con la información almacenada en el archivo
	 * de configuración.
	 */
	public void formulario_online() {

		Map<String, String> datosConfiguracion = Utilidades.devolverDatosConfig();

		String usuarioTexto = datosConfiguracion.get("Usuario");
		String passwordTexto = datosConfiguracion.get("Password");

		nomUsuarioText.setText(usuarioTexto);

		passUsuarioText.setText(passwordTexto);

		checkRecordar.setSelected(true); // Marcar el CheckBox

	}

	/**
	 * Funcion que permite el acceso a la ventana de menuPrincipal
	 *
	 * @param event
	 */
	@FXML
	void entrarMenu(ActionEvent event) {

		if (!Utilidades.isMySQLServiceRunning(ConectManager.DB_HOST, ConectManager.DB_PORT)) {
			AlarmaList.iniciarAnimacionErrorMySql(prontEstadoConexion);
			AlarmaList.iniciarAnimacionAlarma(alarmaConexion);
			return;
		}

		if (ConectManager.estadoConexion) { // Siempre que el metodo de la clase DBManager sea true, permitira acceder
			ConectManager.resetConnection();
			// al menu principal
			nav.verMenuPrincipal();
			closeWindows();
		} else { // En caso contrario mostrara el siguiente mensaje.
			AlarmaList.detenerAnimacion();
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			AlarmaList.iniciarAnimacionConexion(prontEstadoConexion);
		}
	}

	/**
	 * Maneja el evento de enviar datos a la base de datos.
	 *
	 * @param event El evento de acción que desencadenó la función.
	 * @throws SQLException
	 */
	@FXML
	void enviarDatos(ActionEvent event) throws SQLException {

		String[] datosFichero = Utilidades.datosEnvioFichero();

		if (configurarConexion()) {

			ConectManager.datosBBDD(datosFichero);
			if (ConectManager.isConnected()) {
				AlarmaList.detenerAnimacion();
				AlarmaList.iniciarAnimacionConectado(prontEstadoConexion);
				AlarmaList.manejarConexionExitosa(datosFichero, prontEstadoConexion);
				
			} else {
				AlarmaList.manejarErrorConexion("No estás conectado a la base de datos.", prontEstadoConexion);
			}
		}
	}

	/**
	 * Configura la conexión a la base de datos.
	 * 
	 * @return true si la configuración es exitosa, false de lo contrario.
	 */
	private boolean configurarConexion() {

		if (!ConectManager.loadDriver()) {
			return false;
		}

		return true;
	}

	public Scene miStageVentana() {

		Scene scene = botonEnviar.getScene();
		return scene;

	}

	/**
	 * Permite salir completamente del programa.
	 *
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {

		if (nav.salirPrograma(event)) { // Llamada a metodo que permite salir completamente del programa
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Método que se ejecuta al hacer clic en el botón de opciones del programa.
	 * Abre la ventana de opciones y cierra la ventana actual.
	 */
	@FXML
	void opcionesPrograma(ActionEvent event) {

		nav.verOpciones();

		Stage myStage = (Stage) this.botonOpciones.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Cierra el programa a la fuerza correctamente.
	 */
	public void closeWindows() { // Metodo que permite cerrar completamente el programa en caso de cerrar a la //
		// fuerza.
		Stage myStage = (Stage) this.botonEnviar.getScene().getWindow();
		myStage.close();
	}
}
