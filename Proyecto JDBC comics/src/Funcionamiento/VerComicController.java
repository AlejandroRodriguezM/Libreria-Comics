package Funcionamiento;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class VerComicController {

	
	private static final String DB_CLI_SELECT = "SELECT * FROM comics.comicsbbdd";


	@FXML
	private TextField anioPublicacion;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonMostrarParametro1;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Button botonbbdd;

	@FXML
	private TextArea mostrarDatosBBDD;

	@FXML
	private TextField nombreComic;

	@FXML
	private TextField nombreDibujante;

	@FXML
	private TextField nombreEditorial;

	@FXML
	private TextField nombreFormato;

	@FXML
	private TextField nombreGuionista;

	@FXML
	private TextField nombreVariante;


	@FXML
	void limpiarDatos(ActionEvent event) {
		anioPublicacion.setText("");
		nombreComic.setText("");
		nombreDibujante.setText("");
		nombreEditorial.setText("");
		nombreFormato.setText("");
		nombreGuionista.setText("");
		nombreVariante.setText("");

	}

	@FXML
	void mostrarPorParametro(ActionEvent event) {

	}

	@FXML
	void verTodabbdd(ActionEvent event) {

		try {
			ResultSet rs = getTablaComics(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			while (rs.next()) {
				String nombre = rs.getString("nomComic");
				String numero = rs.getString("numComic");
				String variante = rs.getString("nomVariante");
				String firma = rs.getString("firma");
				String editorial = rs.getString("nomEditorial");
				String formato = rs.getString("formato");
				String procedencia = rs.getString("procedencia");
				String anioPubli = rs.getString("anioPubli");
				String guionista = rs.getString("nomguionista");
				String dibujante = rs.getString("nomDibujante");
				mostrarDatosBBDD.setText(nombre + "\t" + numero + "\t" + variante + "\t" + firma + "\t" + editorial 
						+ "\t" + formato + "\t" + procedencia + "\t" + anioPubli + "\t" + guionista + "\t" + dibujante);
			}
			rs.close();
		} catch (SQLException ex) {
			System.err.println("ERROR. No se ha encontrado la tabla Clientes");
		} 
	}

	/**
	 * ÇMetodo para conectar con la tabla de cuentas.
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @return
	 */
	private static ResultSet getTablaComics(int resultSetType, int resultSetConcurrency) {
		try {
			MenuPrincipalController controlador = new MenuPrincipalController();
			Connection conn = controlador.conexionBBDD();

			PreparedStatement stmt = conn.prepareStatement(DB_CLI_SELECT, resultSetType, resultSetConcurrency);
			ResultSet rs = stmt.executeQuery();
			return rs;
		} catch (SQLException ex) {
			System.err.println("ERROR. No se ha podido buscar la tabla 'clientes'.");
			return null;
		}
	}

	@FXML
	void volverMenu(ActionEvent event) throws IOException {
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
		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}

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

	public void closeWindows() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuOpciones.fxml"));

			Parent root = loader.load();

			Scene scene = new Scene(root);
			Stage stage = new Stage();

			stage.setScene(scene);
			stage.show();

			Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
			myStage.close();

		} catch (IOException ex) {
			Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
