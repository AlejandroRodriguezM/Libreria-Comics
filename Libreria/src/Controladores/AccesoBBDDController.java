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
 *
 *  Esta clase permite acceder al programa, es la 1� ventana del programa.
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

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
	 * 
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {

		nav.verSobreMi();

		Stage myStage = (Stage) this.botonSobreMi.getScene().getWindow();
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
					databases.add(res.getString("TABLE_CAT") + "\n"); // elimina las bases de datos que
					// no se utilizaran en este
					// programa
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
