package Funcionamiento;

import java.sql.Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class MenuPrincipalController {

	// Conexión a la base de datos
	private static Connection conn;

	@FXML
	private Button botonAccesobbdd;

	@FXML
	private Button botonEnviar;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonCerrar;

	@FXML
	private TextArea informacion;

	@FXML
	private Label estadoConexion;

	@FXML
	private TextField nombreBBDD;

	@FXML
	private PasswordField pass;

	@FXML
	private TextField puertobbdd;

	@FXML
	private TextField usuario;

	@FXML
	void entrarMenu(ActionEvent event) {

		if (DBManager.DBManager.isConnected()) {
			estadoConexion.setStyle("-fx-background-color: #A0F52D");
			estadoConexion.setText("Conectado");
			
			
			
		} else {
			estadoConexion.setStyle("-fx-background-color: #DD370F");
			estadoConexion.setFont(new Font("Arial", 25));
			estadoConexion.setText("Conectate a la bbdd \nantes de continuar");
		}
	}

	@FXML
	void enviarDatos(ActionEvent event) {

		DBManager.DBManager.loadDriver();
		conn = conexionBBDD();

//		System.out.println(DBManager.DBManager.isConnected());
		
		if (DBManager.DBManager.isConnected()) {
			estadoConexion.setStyle("-fx-background-color: #A0F52D");
			estadoConexion.setText("Conectado");
		} else {
			pass.setText("");
			estadoConexion.setStyle("-fx-background-color: #DD370F");
			estadoConexion.setFont(new Font("Arial", 22));
			estadoConexion.setText("ERROR. Los datos son \nincorrectos. Revise \nlos datos.");
//			conn = null;
		}
	}

	@FXML
	void limpiarDatos(ActionEvent event) {
		nombreBBDD.setText("");
		usuario.setText("");
		pass.setText("");
		puertobbdd.setText("");
	}

	@FXML
	void cerrarbbdd(ActionEvent event) {

		if (DBManager.DBManager.isConnected()) {
			estadoConexion.setText("BBDD Cerrada con existo.\nNo conectado.");
			estadoConexion.setStyle("-fx-background-color: #696969");
			DBManager.DBManager.close();
		} else {
			estadoConexion.setStyle("-fx-background-color: #DD370F");
			estadoConexion.setFont(new Font("Arial", 22));
			estadoConexion.setText("ERROR. No se encuentra \nconectado a ninguna bbdd");
		}
	}

	private Connection conexionBBDD() {
		return DBManager.DBManager.conexion(puertobbdd.getText(), nombreBBDD.getText(), usuario.getText(),
				pass.getText());
	}

}
