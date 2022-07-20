package Controladores;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Funcionamiento.ConexionBBDD;
import Funcionamiento.NavegacionVentanas;
import Funcionamiento.Utilidades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
	private Button botonTwitter;

	@FXML
	private Button botonCrearBBDD;

	@FXML
	private Button numeroVersion;

	@FXML
	private Button botonVerDDBB;

	@FXML
	private Button botonInformacion;
	
	@FXML
	private Button botonDescargaBBDD;

	@FXML
	private Label prontEstadoConexion;

	@FXML
	private TextArea prontInformacion;

	@FXML
	private TextArea informacion;

	@FXML
	public TextField nombreBBDD;

	@FXML
	public PasswordField pass;

	@FXML
	public TextField puertobbdd;

	@FXML
	public TextField usuario;

	private NavegacionVentanas nav = new NavegacionVentanas();

	/**
	 * Funcion para abrir el navegador y acceder a la URL
	 *
	 * @param event
	 */
	@FXML
	void accesoGitHub(ActionEvent event) {
		String url = "https://github.com/MisterioRojo/Proyecto-gui-bbdd/tree/V.F";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url);
			}
		}
	}

	/**
	 * Funcion para abrir el navegador y acceder a la URL
	 *
	 * @param event
	 */
	@FXML
	void accesoTwitter(ActionEvent event) {
		String url = "https://twitter.com/SilverAlox";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url);
			}
		}
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
	 * Funcion que permite el acceso a la ventana de menuPrincipal
	 * 
	 * @param event
	 */
	@FXML
	void entrarMenu(ActionEvent event) {

		if (Funcionamiento.ConexionBBDD.isConnected()) { // Siempre que el metodo de la clase DBManager sea true,
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
	 * Permite ver las bases de datos disponibles en MySql workbench
	 * 
	 * @param event
	 */
	@FXML
	void verDBDisponibles(ActionEvent event) {

		String url = "jdbc:mysql://" + ConexionBBDD.DB_HOST + ":" + puertobbdd.getText() + "?serverTimezone=UTC";

		try {
			Connection connection = DriverManager.getConnection(url, usuario.getText(), pass.getText());

			DatabaseMetaData meta = connection.getMetaData();
			ResultSet res = meta.getCatalogs();

			ArrayList<String> databases = new ArrayList<>(); // ArrayList que almacena el nombre de las bases de datos
																// que hay en MySql Workbench

			while (res.next()) {

				if (!res.getString("TABLE_CAT").equals("information_schema")
						&& !res.getString("TABLE_CAT").equals("mysql")
						&& !res.getString("TABLE_CAT").equals("performance_schema")) // elimina las bases de datos que
																						// no se utilizaran en este
																						// programa
				{
					databases.add(res.getString("TABLE_CAT") + "\n");
				}
			}
			res.close();

			prontInformacion.setStyle("-fx-background-color: #696969");
			String bbddNames = databases.toString().replace("[", "").replace("]", "").replace(",", "").replace(" ", "");
			prontInformacion.setText(bbddNames);

		} catch (SQLException e) {

			nav.alertaException(e.toString());
		}
	}

	/**
	 * Se ve informacion en el TextArea.
	 * 
	 * @param event
	 */
	@FXML
	void verInfo(ActionEvent event) {

		prontInformacion.setStyle("-fx-background-color: #A0F52D");
		prontInformacion.setText(
				"Programa creado por Alejandro Rodriguez. Es un proyecto personal para probar conocimientos adquiridos durante el primer curso de DAW. Esta aplicacion solamente puede usarse con una tabla de una forma estructurada de una forma concreta. Esta aplicacion ha sido creada con la intencion de ser usada junto a una base de datos MySql. Si la aplicacion MySql workbench no se encuentra instalada. Esta aplicacion no funcionara. La pagina oficial donde se encuentra la aplicacion es: https://dev.mysql.com/downloads/workbench/");

	}

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

		Funcionamiento.ConexionBBDD.loadDriver(); // Llamada a metodo que permite comprobar que el driver de conexion a
													// la
		// base de datos sea correcto y funcione
		envioDatosBBDD(); // Llamada a metodo que manda los datos de los textField de la ventana hacia la
		// clase DBManager.
		ConexionBBDD.conexion(); // Llamada a metodo que permite conectar con la base de datos.

		if (Funcionamiento.ConexionBBDD.isConnected()) { // Siempre que la base de datos se haya conectado de forma
			// correcta, mostrara el siguiente mensaje
			prontEstadoConexion.setStyle("-fx-background-color: #A0F52D");
			prontEstadoConexion.setText("Conectado");
		} else { // En caso contrario mostrara el siguiente mensaje
			pass.setText(""); // Limpia el campo de la contraseña en caso de que isConnected sea false.
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			prontEstadoConexion.setText("ERROR. Los datos son \nincorrectos. Revise \nlos datos.");
		}
	}

	/**
	 * Cierra la bbdd
	 *
	 * @param event
	 */
	@FXML
	void cerrarbbdd(ActionEvent event) {

		if (Funcionamiento.ConexionBBDD.isConnected()) { // Siempre que el metodo isConnected sea true, permitira cerrar
															// la
			// base de datos.
			prontEstadoConexion.setText("BBDD Cerrada con exito.\nNo conectado.");
			prontEstadoConexion.setStyle("-fx-background-color: #696969");
			Funcionamiento.ConexionBBDD.close();
		} else { // En caso contrario, mostrara el siguiente mensaje.
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			prontEstadoConexion.setText("ERROR. No se encuentra \nconectado a ninguna bbdd");
		}
	}

	/**
	 * Funcion que permite mandar los datos a la clase DBManager
	 *
	 * @return
	 */
	public void envioDatosBBDD() { // Metodo que manda toda la informacion de los textField a la clase DBManager.
		String datos[] = new String[4];
		datos[0] = puertobbdd.getText();
		datos[1] = nombreBBDD.getText();
		datos[2] = usuario.getText();
		datos[3] = pass.getText();
		ConexionBBDD.datosBBDD(datos);
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
