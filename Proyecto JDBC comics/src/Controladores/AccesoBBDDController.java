package Controladores;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Añadir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase permite acceder a la base de datos introduciendo los diferentes datos que nos pide.
 *
 *  Version 2.5
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Funcionamiento.DBManager;
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
	private DBManager dbmanager = new DBManager();

	/**
	 * Metodo de acceso a pagina web
	 *
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void accesoGitHub(ActionEvent event) throws IOException {
		String url = "https://github.com/MisterioRojo/Proyecto-gui-bbdd/tree/V2.5";

		if (Utilidades.isWindows()) {
			accesoGitHubWindows(url);
		} else {
			if (Utilidades.isUnix()) {
				accesoGitHubLinux(url);
			} else {

			}
		}
	}

	/**
	 * Metodo de acceso a pagina web
	 *
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void accesoTwitter(ActionEvent event) throws IOException {
		String url = "https://twitter.com/home";

		if (Utilidades.isWindows()) {
			accesoTwitterWindows(url);
		} else {
			if (Utilidades.isUnix()) {
				accesoTwitterLinux(url);
			} else {

			}
		}
	}
	
	public void accesoGitHubLinux(String url) throws IOException
	{
		Runtime rt = Runtime.getRuntime();
		String[] browsers = { "google-chrome", "firefox", "mozilla", "epiphany", "konqueror",
		                                 "netscape", "opera", "links", "lynx" };
		 
		StringBuffer cmd = new StringBuffer();
		for (int i = 0; i < browsers.length; i++)
		    if(i == 0)
		        cmd.append(String.format(    "%s \"%s\"", browsers[i], url));
		    else
		        cmd.append(String.format(" || %s \"%s\"", browsers[i], url)); 
		    // If the first didn't work, try the next browser and so on

		rt.exec(new String[] { "sh", "-c", cmd.toString() });
	}
	
	public void accesoGitHubWindows(String url) throws IOException
	{
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url)); // Metodo que abre tu navegador por defecto
	}
	
	public void accesoTwitterLinux(String url) throws IOException
	{
		Runtime rt = Runtime.getRuntime();
		String[] browsers = { "google-chrome", "firefox", "mozilla", "epiphany", "konqueror",
		                                 "netscape", "opera", "links", "lynx" };
		 
		StringBuffer cmd = new StringBuffer();
		for (int i = 0; i < browsers.length; i++)
		    if(i == 0)
		        cmd.append(String.format(    "%s \"%s\"", browsers[i], url));
		    else
		        cmd.append(String.format(" || %s \"%s\"", browsers[i], url)); 
		    // If the first didn't work, try the next browser and so on

		rt.exec(new String[] { "sh", "-c", cmd.toString() });
	}
	
	public void accesoTwitterWindows(String url) throws IOException
	{
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url)); // Metodo que abre tu navegador por defecto
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

			Stage myStage = (Stage) this.botonAccesobbdd.getScene().getWindow();
			myStage.close();
		} else { // En caso contrario mostrara el siguiente mensaje.
			prontEstadoConexion.setStyle("-fx-background-color: #DD370F");
			prontEstadoConexion.setFont(new Font("Arial", 25));
			prontEstadoConexion.setText("Conectate a la bbdd \nantes de continuar");
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

			ArrayList<String> databases = new ArrayList<>();

			while (res.next()) {

				if(!res.getString("TABLE_CAT").equals("information_schema") && !res.getString("TABLE_CAT").equals("mysql") && !res.getString("TABLE_CAT").equals("performance_schema"))
				{
					databases.add(res.getString("TABLE_CAT") + "\n");
				}
			}
			res.close();

			prontInformacion.setStyle("-fx-background-color: #696969");
			String bbddNames = databases.toString().replace("[", "").replace("]", "").replace(",", "").replace(" ", "");
			prontInformacion.setText(bbddNames);

		} catch (SQLException e) {

			System.out.println(e);
		}
	}

    @FXML
    void verInfo(ActionEvent event) {

    	prontInformacion.setStyle("-fx-background-color: #A0F52D");
		prontInformacion.setText("Programa creado por Alejandro Rodriguez. Es un proyecto personal para probar conocimientos adquiridos durante el primer curso de DAW. Esta aplicacion solamente puede usarse con una tabla de una forma estructurada de una forma concreta. Esta aplicacion ha sido creada con la intencion de ser usada junto a una base de datos MySql. Si la aplicacion MySql workbench no se encuentra instalada. Esta aplicacion no funcionara. La pagina oficial donde se encuentra la aplicacion es: https://dev.mysql.com/downloads/workbench/");

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

		Funcionamiento.DBManager.loadDriver(); // Llamada a metodo que permite comprobar que el driver de conexion a la
		// base de datos sea correcto y funcione
		envioDatosBBDD(); // Llamada a metodo que manda los datos de los textField de la ventana hacia la
		// clase DBManager.
		dbmanager.conexion(); // Llamada a metodo que permite conectar con la base de datos.

		if (Funcionamiento.DBManager.isConnected()) { // Siempre que la base de datos se haya conectado de forma
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

		if (Funcionamiento.DBManager.isConnected()) { // Siempre que el metodo isConnected sea true, permitira cerrar la
			// base de datos.
			prontEstadoConexion.setText("BBDD Cerrada con exito.\nNo conectado.");
			prontEstadoConexion.setStyle("-fx-background-color: #696969");
			Funcionamiento.DBManager.close();
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
