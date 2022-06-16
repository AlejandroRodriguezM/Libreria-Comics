package Controladores;

import java.io.IOException;
import java.sql.Connection;

import Funcionamiento.NavegacionVentanas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MenuPrincipalController {

	// Conexión a la base de datos
	//	private static Connection conn;

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
	private Label estadoConexion;

	@FXML
	private TextArea informacion;

	@FXML
	public TextField nombreBBDD;

	@FXML
	private Button numeroVersion;

	@FXML
	public PasswordField pass;

	@FXML
	public TextField puertobbdd;

	@FXML
	public TextField usuario;

	NavegacionVentanas nav = new NavegacionVentanas();

	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void accesoGitHub(ActionEvent event) throws IOException {
		String url = "https://www.google.com";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void accesoTwitter(ActionEvent event) throws IOException {
		String url = "https://www.google.com";
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
	}

	/**
	 * 
	 * @param event
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@FXML
	void entrarMenu(ActionEvent event) throws InterruptedException, IOException {

		if (Funcionamiento.DBManager.isConnected()) {

			nav.verBBDD();
			datosBBDD();

			Stage myStage = (Stage) this.botonAccesobbdd.getScene().getWindow();
			myStage.close();
		}
		else {
			estadoConexion.setStyle("-fx-background-color: #DD370F");
			estadoConexion.setFont(new Font("Arial", 25));
			estadoConexion.setText("Conectate a la bbdd \nantes de continuar");
		}
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		nombreBBDD.setText("");
		usuario.setText("");
		pass.setText("");
		puertobbdd.setText("");
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	void cerrarbbdd(ActionEvent event) {

		if (Funcionamiento.DBManager.isConnected()) {
			estadoConexion.setText("BBDD Cerrada con existo.\nNo conectado.");
			estadoConexion.setStyle("-fx-background-color: #696969");
			Funcionamiento.DBManager.close();
		} else {
			estadoConexion.setStyle("-fx-background-color: #DD370F");
			estadoConexion.setFont(new Font("Arial", 22));
			estadoConexion.setText("ERROR. No se encuentra \nconectado a ninguna bbdd");
		}
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	void enviarDatos(ActionEvent event) {

		Funcionamiento.DBManager.loadDriver();
		conexionBBDD();

		//Funcionamiento.DBManager.conexion();

		if (Funcionamiento.DBManager.isConnected()) {
			estadoConexion.setStyle("-fx-background-color: #A0F52D");
			estadoConexion.setText("Conectado");
		} else {
			pass.setText("");
			estadoConexion.setStyle("-fx-background-color: #DD370F");
			estadoConexion.setFont(new Font("Arial", 22));
			estadoConexion.setText("ERROR. Los datos son \nincorrectos. Revise \nlos datos.");
		}
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Saliendo");
		alert.setHeaderText("Estas apunto de salir.");
		alert.setContentText("¿Estas seguro que quieres salir?");

		if(alert.showAndWait().get() == ButtonType.OK)
		{
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * 
	 * @return
	 */
	private Connection conexionBBDD() {
		return Funcionamiento.DBManager.conexion(puertobbdd.getText(), nombreBBDD.getText(), usuario.getText(),
				pass.getText());
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] datosBBDD() {
		String datos[] = new String[4];
//		datos[0] = puertobbdd.getText();
		datos[1] = nombreBBDD.getText();
		datos[2] = usuario.getText();
		datos[3] = pass.getText();
		return VerComicController.datos(datos);
	}

	/**
	 * 
	 */
	public void closeWindows() {
		Stage myStage = (Stage) this.botonEnviar.getScene().getWindow();
		myStage.close();
	}

}
