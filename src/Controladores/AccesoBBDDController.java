package Controladores;

import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Esta clase sirve para acceder a la base de datos y poder realizar diferentes
 * funciones.
 *
 * @author Alejandro Rodriguez
 */
public class AccesoBBDDController {

	@FXML
	private Button botonAccesobbdd;

	@FXML
	private Button botonCerrar;

	@FXML
	private Button botonEnviar;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonCrearBBDD;

	@FXML
	private Button botonVerDDBB;

	@FXML
	private Button botonSobreMi;

	@FXML
	private Button botonDescargaBBDD;

	@FXML
	private Label prontEstadoConexion;

	@FXML
	private TextArea informacion;

	@FXML
	public TextField nombreBBDD;

	@FXML
	private PasswordField nombreHost;

	@FXML
	public PasswordField pass;

	@FXML
	public TextField puertobbdd;

	@FXML
	public TextField usuario;

	@FXML
	private RadioButton noOffline;

	@FXML
	private RadioButton siOnline;

	@FXML
	private Label etiquetaHost;

	private static Ventanas nav = new Ventanas();

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
	 * Funcion que permite el acceso a la ventana de menuPrincipal
	 *
	 * @param event
	 */
	@FXML
	void entrarMenu(ActionEvent event) {

		if (JDBC.DBManager.isConnected()) { // Siempre que el metodo de la clase DBManager sea
														// true,
			// permitira acceder al menu principal
			nav.verMenuPrincipal(); // Llamada a metodo de la clase NavegacionVentanas. Permite cargar y mostrar el
			// menu principal
			Stage myStage = (Stage) this.botonAccesobbdd.getScene().getWindow();
			myStage.close();
		} else { // En caso contrario mostrara el siguiente mensaje.
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			prontEstadoConexion.setFont(new Font("Arial", 25));
			prontEstadoConexion.setText("Conectate a la bbdd antes de continuar");
		}
	}

	/**
	 * Funcion que permite entrar en la ventana de creacion de base de datos.
	 *
	 * @param event
	 */
	@FXML
	void crearBBDD(ActionEvent event) {

		nav.verCrearBBDD();

		Stage myStage = (Stage) this.botonAccesobbdd.getScene().getWindow();
		myStage.close();

	}

	/**
	 * MEtodo que permite abrir la ventana "sobreMiController"
	 *
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {

		nav.verSobreMi();

		Stage myStage = (Stage) this.botonSobreMi.getScene().getWindow();
		myStage.close();
	}

//	/**
//	 * Permite ver las bases de datos disponibles en MySql workbench
//	 *
//	 * @param event
//	 */
//	@FXML
//	void verDBDisponibles(ActionEvent event) {
//
//		String url = "jdbc:mysql://" + ConexionBBDD.DB_HOST + ":" + puertobbdd.getText() + "?serverTimezone=UTC";
//
//		try {
//			Connection connection = DriverManager.getConnection(url, usuario.getText(), pass.getText());
//
//			DatabaseMetaData meta = connection.getMetaData();
//			ResultSet res = meta.getCatalogs();
//
//			ArrayList<String> databases = new ArrayList<>(); // ArrayList que almacena el nombre de las bases de datos
//			// que hay en MySql Workbench
//
//			while (res.next()) {
//
//				if (!res.getString("TABLE_CAT").equals("information_schema")
//						&& !res.getString("TABLE_CAT").equals("mysql")
//						&& !res.getString("TABLE_CAT").equals("performance_schema")) // elimina las bases de datos que
//					// no se utilizaran en este
//					// programa
//				{
//					databases.add(res.getString("TABLE_CAT") + "\n"); // elimina las bases de datos que
//					// no se utilizaran en este
//					// programa
//				}
//			}
//			res.close();
//
//			prontInformacion.setStyle("-fx-background-color: #696969");
//			String bbddNames = databases.toString().replace("[", "").replace("]", "").replace(",", "").replace(" ", "");
//			prontInformacion.setText(bbddNames);
//
//		} catch (SQLException e) {
//
//			nav.alertaException(e.toString());
//		}
//	}

	/**
	 * Limpia los datos de los campos
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) { // Metodo que permite limpiar todos textField de la ventna.
		nombreBBDD.setText("");
		usuario.setText("");
		pass.setText("");
		puertobbdd.setText("");
	}

	/**
	 * Envia los datos a la clase DBManager y permite conectarse a esta.
	 *
	 * @param event
	 */
	@FXML
	void enviarDatos(ActionEvent event) {

		JDBC.DBManager.loadDriver(); // Llamada a metodo que permite comprobar que el driver de conexion a la
												// base de datos sea correcto y funcione
		envioDatosBBDD(); // Llamada a metodo que manda los datos de los textField de la ventana hacia la
							// clase DBManager.
		DBManager.conexion(); // Llamada a metodo que permite conectar con la base de datos.

		if (JDBC.DBManager.isConnected()) { // Siempre que la base de datos se haya conectado de // forma
			// correcta, mostrara el siguiente mensaje
			prontEstadoConexion.setStyle("-fx-background-color: #A0F52D");
			prontEstadoConexion.setText("Conectado");
		} else { // En caso contrario mostrara el siguiente mensaje
			pass.setText(""); // Limpia el campo de la contraseña en caso de que isConnected sea false.
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			prontEstadoConexion.setText("ERROR. Los datos son \nincorrectos. Revise los datos.");
		}
	}

	/**
	 * Cierra la bbdd
	 *
	 * @param event
	 */
	@FXML
	void cerrarbbdd(ActionEvent event) {

		if (JDBC.DBManager.isConnected()) { // Siempre que el metodo isConnected sea true,
														// permitira cerrar
			// la
			// base de datos.
			prontEstadoConexion.setText("BBDD Cerrada con exito.\nEstado: Desconectado.");
			prontEstadoConexion.setStyle("-fx-background-color: #696969");
			JDBC.DBManager.close();
		} else { // En caso contrario, mostrara el siguiente mensaje.
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			prontEstadoConexion.setText("ERROR. No se encuentra conectado a ninguna bbdd");
		}
	}

	/**
	 * Metodo que permite seleccionar un host online o el publico.
	 *
	 * @param event
	 */
	@FXML
	void selectorBotonHost(ActionEvent event) {
		selectorHost();
	}

	/**
	 * Funcion que permite mandar los datos a la clase DBManager
	 *
	 * @return
	 */
	public void envioDatosBBDD() { // Metodo que manda toda la informacion de los textField a la clase DBManager.
		String datos[] = new String[5];
		datos[0] = puertobbdd.getText();
		datos[1] = nombreBBDD.getText();
		datos[2] = usuario.getText();
		datos[3] = pass.getText();
		datos[4] = selectorHost();
		DBManager.datosBBDD(datos);
	}

	/**
	 * Funcion que permite conectarse a un host online o usar el local.
	 *
	 * @return
	 */
	public String selectorHost() {

		if (siOnline.isSelected()) {
			etiquetaHost.setText("Nombre del host: ");
			nombreHost.setDisable(false);
			nombreHost.setOpacity(1);
			return nombreHost.getText();
		}
		if (noOffline.isSelected()) {
			etiquetaHost.setText("Offline");
			nombreHost.setDisable(true);
			nombreHost.setOpacity(0);
			return "localhost";
		}
		return "localhost";
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
	 * Cierra el programa a la fuerza correctamente.
	 */
	public void closeWindows() { // Metodo que permite cerrar completamente el programa en caso de cerrar a la //
		// fuerza.
		Stage myStage = (Stage) this.botonEnviar.getScene().getWindow();
		myStage.close();
	}

}
