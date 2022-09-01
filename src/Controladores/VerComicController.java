package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Funcionamiento.Comics;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class VerComicController implements Initializable {

	@FXML
	private TextField anioPublicacion;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Button botonbbdd;

	@FXML
	private TableColumn<Comics, String> dibujante;

	@FXML
	private TableColumn<Comics, String> editorial;

	@FXML
	private TableColumn<Comics, String> fecha;

	@FXML
	private TableColumn<Comics, String> firma;

	@FXML
	private TableColumn<Comics, String> formato;

	@FXML
	private TableColumn<Comics, String> guionista;

	@FXML
	private TableColumn<Comics, String> nombre;

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
	private TableColumn<Comics, String> numero;

	@FXML
	private TableColumn<Comics, String> procedencia;

	@FXML
	public TableView<Comics> tablaBBDD;

	@FXML
	private TableColumn<Comics, String> variante;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	void limpiarDatos(ActionEvent event) {
		
		nombreComic.setText("");
		nombreDibujante.setText("");
		nombreEditorial.setText("");
		nombreFormato.setText("");
		nombreGuionista.setText("");
		nombreVariante.setText("");
		anioPublicacion.setText("");

	}

	@SuppressWarnings("unchecked")
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException {
		
		String nombreCom,numeroCom,varianteCom,firmaCom,editorialCom,formatoCom,procedenciaCom,fechaCom,guionistaCom,dibujanteCom;

	    nombreCom = nombreComic.getText();

	    numeroCom = numeroComic.getText();

	    varianteCom = nombreEditorial.getText();

	    firmaCom = nombreFirma.getText();

	    editorialCom = nombreFormato.getText();

	    formatoCom = nombreGuionista.getText();

	    procedenciaCom = nombreProcedencia.getText();

	    fechaCom = nombreVariante.getText();
	    
		guionistaCom = anioPublicacion.getText();
		
		dibujanteCom = nombreDibujante.getText();
		
		nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		variante.setCellValueFactory(new PropertyValueFactory<>("variante"));
		firma.setCellValueFactory(new PropertyValueFactory<>("firma"));
		editorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
		formato.setCellValueFactory(new PropertyValueFactory<>("formato"));
		procedencia.setCellValueFactory(new PropertyValueFactory<>("procedencia"));
		fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		guionista.setCellValueFactory(new PropertyValueFactory<>("guionista"));
		dibujante.setCellValueFactory(new PropertyValueFactory<>("dibujante"));

		List<Comics> listComics = FXCollections.observableArrayList(Comics.filtadroBBDD(nombreCom,numeroCom,varianteCom,firmaCom,editorialCom,formatoCom,procedenciaCom,fechaCom,guionistaCom,dibujanteCom));
		tablaBBDD.getColumns().setAll(nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listComics);
	}

	@SuppressWarnings("unchecked")
	@FXML
	void verTodabbdd(ActionEvent event) throws SQLException {

		nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		variante.setCellValueFactory(new PropertyValueFactory<>("variante"));
		firma.setCellValueFactory(new PropertyValueFactory<>("firma"));
		editorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
		formato.setCellValueFactory(new PropertyValueFactory<>("formato"));
		procedencia.setCellValueFactory(new PropertyValueFactory<>("procedencia"));
		fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		guionista.setCellValueFactory(new PropertyValueFactory<>("guionista"));
		dibujante.setCellValueFactory(new PropertyValueFactory<>("dibujante"));

		List<Comics> listComics = FXCollections.observableArrayList(Comics.verTodo());
		tablaBBDD.getColumns().setAll(nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listComics);

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
