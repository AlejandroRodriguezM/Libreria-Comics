package Controladores;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Funcionamiento.Comics;
import Funcionamiento.DBManager;
import Funcionamiento.NavegacionVentanas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class EliminarDatosController{

	@FXML
    private Button botonEliminar;

    @FXML
    private Button botonLimpiarComic;

    @FXML
    private Button botonMostrarParametro;

    @FXML
    private Button botonSalir;

    @FXML
    private Button botonVolver;

    @FXML
    private TextField nombreComic;


    @FXML
    private TextField numeroComic;

    @FXML
    private TextField numeroID;

    @FXML
    private Label pantallaInformativa;
    
	@FXML
	public TableView<Comics> tablaBBDD;

    @FXML
    private TableColumn<Comics,String> ID;
	
	@FXML
	private TableColumn<Comics, String> numero;

	@FXML
	private TableColumn<Comics, String> procedencia;

	@FXML
	private TableColumn<Comics, String> variante;
	
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

    
	private static Connection conn = DBManager.conexion();
	
	NavegacionVentanas nav = new NavegacionVentanas();

	/**
	 * 
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		nombreComic.setText("");
		numeroComic.setText("");
		ID.setText("");
	}

    @FXML
    void eliminarDatos(ActionEvent event) {

    }
    
    @SuppressWarnings("unchecked")
	@FXML
    void mostrarPorParametro(ActionEvent event) throws SQLException {
    	
		String id,nombreCom, numeroCom, varianteCom = "", firmaCom = "", editorialCom = "", formatoCom = "", procedenciaCom = "", fechaCom = "",
		guionistaCom = "", dibujanteCom = "";

		nombreCom = nombreComic.getText();

		numeroCom = numeroComic.getText();
		
		id = numeroID.getText();

		nombreColumnas();

		List<Comics> listComics = FXCollections.observableArrayList(Comics.filtadroBBDD(id,nombreCom, numeroCom,
				varianteCom, firmaCom, editorialCom, formatoCom, procedenciaCom, fechaCom, guionistaCom, dibujanteCom));
		tablaBBDD.getColumns().setAll(ID,nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listComics);

    }
    
	/**
	 * 
	 */
	private void nombreColumnas()
	{
		ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
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
	}

	/**
	 * Permite volver al menu de conexion a la base de datos.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void volverAlMenu(ActionEvent event) throws IOException {
		
		nav.verBBDD();

		// Ciero la ventana donde estoy
		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite salir completamente del programa.
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
	 * Al cerrar la ventana, se cargara la ventana de verBBDD
	 */
	public void closeWindows() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/verBBDD.fxml"));

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
