package Funcionamiento;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
	private Button numeroVersion;

	@FXML
	private Button botonTwitter;

	@FXML
	void accesoGitHub(ActionEvent event) throws IOException {
        String url = "https://www.google.com";
        java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
	}

	@FXML
	void accesoTwitter(ActionEvent event) throws IOException {
        String url = "https://www.google.com";
        java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
	}

	@FXML
	void entrarMenu(ActionEvent event) throws InterruptedException, IOException {

		if (DBManager.DBManager.isConnected()) {
			estadoConexion.setStyle("-fx-background-color: #A0F52D");
			estadoConexion.setText("Conectando . . . ");
//			Thread.sleep(3*1000);
			
			 // Cargo la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuOpciones.fxml"));

            // Cargo el padre
            Parent root = loader.load();

            // Obtengo el controlador
            MenuOpcionesController controlador = loader.getController();

            // Creo la scene y el stage
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            // Asocio el stage con el scene
            stage.setScene(scene);
            stage.show();

            // Indico que debe hacer al cerrar
            stage.setOnCloseRequest(e -> controlador.closeWindows());

            // Ciero la ventana donde estoy
            Stage myStage = (Stage) this.botonAccesobbdd.getScene().getWindow();
            myStage.close();
			



		} else {
			estadoConexion.setStyle("-fx-background-color: #DD370F");
			estadoConexion.setFont(new Font("Arial", 25));
			estadoConexion.setText("Conectate a la bbdd \nantes de continuar");
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


	@FXML
	void enviarDatos(ActionEvent event) {

		DBManager.DBManager.loadDriver();
		conn = conexionBBDD();

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

	private Connection conexionBBDD() {
		return DBManager.DBManager.conexion(puertobbdd.getText(), nombreBBDD.getText(), usuario.getText(),
				pass.getText());
	}
	
	public void closeWindows() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/MenuVista.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            Stage myStage = (Stage) this.botonEnviar.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
