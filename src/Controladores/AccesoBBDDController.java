package Controladores;

import java.awt.Desktop;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xls
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Añadir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *  
 *  Esta clase permite acceder a la base de datos introduciendo los diferentes datos que nos pide.
 *  
 *  Version 2.3
 *  
 *  Por Alejandro Rodriguez
 *  
 *  Twitter: @silverAlox
 */

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Funcionamiento.DBManager;
import Funcionamiento.NavegacionVentanas;
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
	private Label estadoConexion;

	@FXML
	private Label prontBBDDNames;

	@FXML
	private TextArea informacion;

	@FXML
	public TextField nombreBBDD;

	@FXML
	private Button numeroVersion;

	@FXML
	private Button botonVerDDBB;

	@FXML
	public PasswordField pass;

	@FXML
	public TextField puertobbdd;

	@FXML
	public TextField usuario;

	NavegacionVentanas nav = new NavegacionVentanas();

	/**
	 * Metodo de acceso a pagina web
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void accesoGitHub(ActionEvent event) throws IOException {
		String url = "https://github.com/MisterioRojo/Proyecto-gui-bbdd";

		if (Desktop.isDesktopSupported()) {
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url)); // Metodo que abre tu navegador por defecto
			// y muestra la url que se encuentra en el
			// String
		} else {
			Desktop desktop = Desktop.getDesktop();
			if (!desktop.isSupported(Desktop.Action.BROWSE)) {
				desktop.browse(URI.create(url));
			}
		}
	}

	/**
	 * MEtodo de acceso a pagina web
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void accesoTwitter(ActionEvent event) throws IOException {
		String url = "https://twitter.com/home";

		if (Desktop.isDesktopSupported()) {
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url)); // Metodo que abre tu navegador por defecto
			// y muestra la url que se encuentra en el // String
		} else {
			Desktop desktop = Desktop.getDesktop();
			if (!desktop.isSupported(Desktop.Action.BROWSE)) {
				desktop.browse(URI.create(url));
			}
		}
	}

	/**
	 * Permite entrar dentro del menuPrincipal
	 * 
	 * @param event
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@FXML
	void entrarMenu(ActionEvent event) throws InterruptedException, IOException {

		if (Funcionamiento.DBManager.isConnected()) { // Siempre que el metodo de la clase DBManager sea true, permitira
			// acceder al menu principal

			nav.verMenuPrincipal(); // Llamada a metodo de la clase NavegacionVentanas. Permite cargar y mostrar el
			// menu principal
			envioDatosBBDD();

			Stage myStage = (Stage) this.botonAccesobbdd.getScene().getWindow();
			myStage.close();
		} else { // En caso contrario mostrara el siguiente mensaje.
			estadoConexion.setStyle("-fx-background-color: #DD370F");
			estadoConexion.setFont(new Font("Arial", 25));
			estadoConexion.setText("Conectate a la bbdd \nantes de continuar");
		}
	}

	@FXML
	void crearBBDD(ActionEvent event) {

		nav.verCrearBBDD();

		Stage myStage = (Stage) this.botonAccesobbdd.getScene().getWindow();
		myStage.close();

	}

	@FXML
	void verDBDisponibles(ActionEvent event) {

		String url = "jdbc:mysql://" + DBManager.DB_HOST + ":" + puertobbdd.getText() + "?serverTimezone=UTC";

		try {
			Connection connection = DriverManager.getConnection(url, usuario.getText(), pass.getText());

			DatabaseMetaData meta = connection.getMetaData();
			ResultSet res = meta.getCatalogs();

			ArrayList<String> databases = new ArrayList<String>();

			while (res.next()) {
				
				if(!res.getString("TABLE_CAT").equals("information_schema") && !res.getString("TABLE_CAT").equals("mysql") && !res.getString("TABLE_CAT").equals("performance_schema"))
				{
					databases.add(res.getString("TABLE_CAT") + "\n");
				}
			}
			res.close();

			prontBBDDNames.setStyle("-fx-background-color: #696969");
			String bbddNames = databases.toString().replace("[", "").replace("]", "").replace(",", "").replace(" ", "");
			prontBBDDNames.setText(bbddNames);

		} catch (SQLException e) {

			System.out.println(e);
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
	 * Cierra la bbdd
	 * 
	 * @param event
	 */
	@FXML
	void cerrarbbdd(ActionEvent event) {

		if (Funcionamiento.DBManager.isConnected()) { // Siempre que el metodo isConnected sea true, permitira cerrar la
			// base de datos.
			estadoConexion.setText("BBDD Cerrada con exito.\nNo conectado.");
			estadoConexion.setStyle("-fx-background-color: #696969");
			Funcionamiento.DBManager.close();
		} else { // En caso contrario, mostrara el siguiente mensaje.
			estadoConexion.setStyle("-fx-background-color: #DD370F");
			estadoConexion.setFont(new Font("Arial", 22));
			estadoConexion.setText("ERROR. No se encuentra \nconectado a ninguna bbdd");
		}
	}

	/**
	 * Envia los datos a la clase DBManager y permite conectarse a esta.
	 * 
	 * @param event
	 */
	@FXML
	void enviarDatos(ActionEvent event) {

		Funcionamiento.DBManager.loadDriver(); // Llamada a metodo que permite comprobar que el driver de conexion a la
		// base de datos sea correcto y funcione
		envioDatosBBDD(); // Llamada a metodo que manda los datos de los textField de la ventana hacia la
		// clase DBManager.
		DBManager.conexion(); // Llamada a metodo que permite conectar con la base de datos.

		if (Funcionamiento.DBManager.isConnected()) { // Siempre que la base de datos se haya conectado de forma
			// correcta, mostrara el siguiente mensaje
			estadoConexion.setStyle("-fx-background-color: #A0F52D");
			estadoConexion.setText("Conectado");
		} else { // En caso contrario mostrara el siguiente mensaje
			pass.setText(""); // Limpia el campo de la contraseña en caso de que isConnected sea false.
			estadoConexion.setStyle("-fx-background-color: #DD370F");
			estadoConexion.setFont(new Font("Arial", 22));
			estadoConexion.setText("ERROR. Los datos son \nincorrectos. Revise \nlos datos.");
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
		DBManager.datosBBDD(datos); // llamada a metodo que permite mandar los datos de los TextField a la clase
		// DBManager
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
	public void closeWindows() { // Metodo que permite cerrar completamente el programa en caso de cerrar a la
		// fuerza.
		Stage myStage = (Stage) this.botonEnviar.getScene().getWindow();
		myStage.close();
	}

}
