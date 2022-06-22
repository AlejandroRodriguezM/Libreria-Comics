package SinUsar;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import Controladores.ModificarDatosController;
import Funcionamiento.Comics;
import Funcionamiento.DBManager;
import Funcionamiento.NavegacionVentanas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class VentanaModificarController {

	@FXML
	private TextField anioPublicacion;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonModificarDatos;

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

	@FXML
	private Label pantallaInformativa;

	NavegacionVentanas nav = new NavegacionVentanas();

	ModificarDatosController datos = new ModificarDatosController();
	
	Comics comic = new Comics();

	@FXML
	void BotonLimpiarComic(ActionEvent event) {

	}

	@FXML
	void modificarDatos(ActionEvent event) throws SQLException {

		Comics comic = new Comics();
		String id = "";
//		String id = "",nombreCom = "", numeroCom = "", varianteCom = "", firmaCom = "", editorialCom = "", formatoCom = "", procedenciaCom = "", fechaCom = "",
//				guionistaCom = "", dibujanteCom = "", sentenciaSQL;

//		id = datos.devolverID();
		
		String sentenciaSQL = "UPDATE comicsbbdd set nomComic = ?,numComic = ?,nomVariante = ?,"
				+ "Firma = ?,nomEditorial = ?,formato = ?,Procedencia = ?,anioPubli = ?"
				+ "nomGuionista = ?,nomDibujante = ? where ID = " + id;
		
		List<Comics> listComics = FXCollections.observableArrayList(comic.filtadroBBDD(comic));

		pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
		pantallaInformativa.setText(listComics.toString());

		try {
			PreparedStatement ps = null;

			ps = DBManager.conexion().prepareStatement(sentenciaSQL);
			ps.setString(1, nombreComic.getText());
			ps.setString(2, numeroComic.getText());
			if (nombreVariante.getText().length() != 0) {
				ps.setString(3, nombreVariante.getText());
			} else {
				ps.setString(3, "No variante");
			}
			if (nombreFirma.getText().length() != 0) {
				ps.setString(4, nombreFirma.getText());
			} else {
				ps.setString(4, "No firmado");
			}
			ps.setString(5, nombreEditorial.getText());
			ps.setString(6, nombreFormato.getText());
			ps.setString(7, nombreProcedencia.getText());
			ps.setString(8, anioPublicacion.getText());
			ps.setString(9, nombreGuionista.getText());
			ps.setString(10, nombreDibujante.getText());

			ps.executeUpdate();
		}
		catch(SQLException ex)
		{
			System.out.println(ex);
		}
	}
	
	public static String idComic(String id)
	{
		return id;
	}

	/**
	 * Permite salir completamente del programa.
	 * 
	 * @param event
	 */
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

	/**
	 * Permite volver al menu de conexion a la base de datos.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void volverAlMenu(ActionEvent event) throws IOException {

		// Ciero la ventana donde estoy
		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Al cerrar la ventana, se cargara la ventana de verBBDD
	 */
	public void closeWindows() {

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

		//		try {
		//			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/ModificarComicBBDD.fxml"));
		//
		//			Parent root = loader.load();
		//
		//			Scene scene = new Scene(root);
		//			Stage stage = new Stage();
		//
		//			stage.setScene(scene);
		//			stage.show();
		//			Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		//			myStage.close();
		//		} catch (IOException ex) {
		//			Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
		//		}
	}

}
