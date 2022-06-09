package Controladores;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import DBManager.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AniadirDatosController {

	@FXML
	private TextField anioPublicacion;

	@FXML
	private Button botonAniadirBBDD;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Label labelResultado;

	@FXML
	private TextField nombreComic;

	@FXML
	private TextField nombreDibujante;

	@FXML
	private TextField nombreEditorial;

	@FXML
	private TextField nombreFirma;

	@FXML
	private TextField nombreFormato;

	@FXML
	private TextField nombreGuionista;

	@FXML
	private TextField nombreProcedencia;

	@FXML
	private TextField nombreVariante;

	@FXML
	private TextField numeroComic;

	private static Connection conn = DBManager.conexion();

	// private static DBManager.DBManager.loadDriver();

	@FXML
	void BotonLimpiarComic(ActionEvent event) {

	}

	@FXML
	public void BotonAniadir(ActionEvent event) {

		String nombreCom, numeroCom, varianteCom, firmaCom, editorialCom, formatoCom, procedenciaCom, fechaCom,
				guionistaCom, dibujanteCom;

		String sentenciaSQL = "insert into comicsbbdd(nomComic,numComic,nomVariante,firma,nomEditorial,formato,procedencia,anioPubli,nomGuionista,nomDibujante) values (?,?,?,?,?,?,?,?,?,?)";

		nombreCom = nombreComic.getText();

		numeroCom = numeroComic.getText();

		varianteCom = nombreVariante.getText();

		firmaCom = nombreFirma.getText();

		editorialCom = nombreEditorial.getText();

		formatoCom = nombreFormato.getText();

		procedenciaCom = nombreProcedencia.getText();

		fechaCom = anioPublicacion.getText();

		guionistaCom = nombreGuionista.getText();

		dibujanteCom = nombreDibujante.getText();

		try {
			PreparedStatement statement = conn.prepareStatement(sentenciaSQL);
			statement.setString(1, nombreCom);
			statement.setString(2, numeroCom);

			if (varianteCom.length() > 0) {
				statement.setString(3, varianteCom);
			} else {
				statement.setString(3, "normal");
			}
			if (firmaCom.length() > 0) {
				statement.setString(4, firmaCom);
			} else {
				statement.setString(4, "No firmado");
			}
			statement.setString(5, editorialCom);
			statement.setString(6, formatoCom);
			statement.setString(7, procedenciaCom);
			statement.setString(8, fechaCom);
			statement.setString(9, guionistaCom);
			statement.setString(10, dibujanteCom);

			if (statement.executeUpdate() == 1) {
				labelResultado.setText("Comic añadido correctamente!" + "\nNombre del comic: " + nombreCom
						+ dibujanteCom + "\nNumero: " + numeroCom + "\nPortada variante: " + varianteCom + "\nFirma: "
						+ firmaCom + "\nEditorial: " + editorialCom + "\nFormato: " + formatoCom + "\nProcedencia: "
						+ procedenciaCom + "\nFecha de publicacion: " + fechaCom + "\nGuionista: " + guionistaCom
						+ "\nDibujante: " + dibujanteCom);
				statement.close();
			} else {
				labelResultado
						.setText("Se ha encontrado un error. No ha sido posible añadir el comic a la base de datos.");
			}
		} catch (SQLException ex) {
			System.err.println("Error al insertar un comic" + ex);
		}

	}

	@FXML
	void volverAlMenu(ActionEvent event) throws IOException {

		// Cargo la vista
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/verBBDD.fxml"));

		// Cargo el padre
		Parent root = loader.load();

		// Obtengo el controlador
		VerComicController controlador = loader.getController();

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

		if (alert.showAndWait().get() == ButtonType.OK) {
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