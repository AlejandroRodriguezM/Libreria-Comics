package Funcionamiento;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class VerComicController implements Initializable{

	
//	private static final String DB_CLI_SELECT = "SELECT * FROM comics.comicsbbdd";
//	private static Connection conn = DBManager.DBManager.conexion();
	private EntityManager entityManager;

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
    private TableColumn<Comic, String> dibujante;

    @FXML
    private TableColumn<Comic, String> editorial;

    @FXML
    private TableColumn<Comic, String>fecha;

    @FXML
    private TableColumn<Comic, String>firma;

    @FXML
    private TableColumn<Comic, String>formato;

    @FXML
    private TableColumn<Comic, String> guionista;

    @FXML
    private TableColumn<Comic, String> nombre;

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
    private TableColumn<Comic, String>numero;

    @FXML
    private TableColumn<Comic, String> procedencia;

    @FXML
    private TableView<Comic> tablaBBDD;

    @FXML
    private TableColumn <Comic, String> variante;
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nombre.setCellValueFactory(new PropertyValueFactory<>("nomComics"));
		numero.setCellValueFactory(new PropertyValueFactory<>("numComic"));
		variante.setCellValueFactory(new PropertyValueFactory<>("nomVariante"));
		firma.setCellValueFactory(new PropertyValueFactory<>("firma"));
		editorial.setCellValueFactory(new PropertyValueFactory<>("nomEditorial"));
		formato.setCellValueFactory(new PropertyValueFactory<>("formato"));
		procedencia.setCellValueFactory(new PropertyValueFactory<>("procedencia"));
		fecha.setCellValueFactory(new PropertyValueFactory<>("anioPubli"));
		guionista.setCellValueFactory(new PropertyValueFactory<>("nomGuionista"));
		dibujante.setCellValueFactory(new PropertyValueFactory<>("nomDibujante"));
		
	}
    
    
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
		
	    Query queryPersonaFindAll = entityManager.createNamedQuery("Comic.findAll");
	    List<Comic> listComics = queryPersonaFindAll.getResultList();
	    tablaBBDD.setItems(FXCollections.observableArrayList(listComics));


		
		
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
