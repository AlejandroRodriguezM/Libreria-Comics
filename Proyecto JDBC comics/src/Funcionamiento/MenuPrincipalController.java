package Funcionamiento;

import java.sql.Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class MenuPrincipalController {

	@FXML
	private Button botonAccesobbdd;

	@FXML
	private Button botonEnviar;

	@FXML
	private Button botonLimpiar;

	@FXML
	private TextArea informacion;

	@FXML
	private Label estadoConexion;

	@FXML
	private static TextField nombreBBDD;

	@FXML
	private static PasswordField pass;

	@FXML
	private static TextField puertobbdd;

	@FXML
	private static TextField usuario;

	@FXML
	void entrarMenu(ActionEvent event) {
		System.out.println(nombreBBDD);
	}

	@FXML
	void enviarDatos(ActionEvent event) {

		
		conexionBBDD();
		DBManager.DBManager.loadDriver();

		if (DBManager.DBManager.isConnected()) {
			estadoConexion.setStyle("-fx-background-color: #A0F52D");
			estadoConexion.setText("Conectado");
		} else {
			pass.setText("");
			estadoConexion.setStyle("-fx-background-color: #DD370F");
			estadoConexion.setText("ERROR.");
		}
	}

	@FXML
	void limpiarDatos(ActionEvent event) {
		nombreBBDD.setText("");
		usuario.setText("");
		pass.setText("");
		puertobbdd.setText("");
	}

	private static Connection conexionBBDD() {
		return DBManager.DBManager.conexion(puertobbdd.getText(), nombreBBDD.getText(), usuario.getText(),
				pass.getText());
	}

}
