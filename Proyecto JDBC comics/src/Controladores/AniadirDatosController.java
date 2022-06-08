package Controladores;

import java.io.IOException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AniadirDatosController {


//	@FXML
//	public void BotonAniadir(ActionEvent event) {
//
//		DBManager.DBManager.loadDriver();
//		conn = conexionBBDD();
//		String nom_comic, nom_dibujante, nom_editorial, nom_guionista, nom_variante, nom_Formato, num_comic;
//		String sentenciaSQL = "insert into comics(nomComic,nomDibujante,nomEditorial,nomGuionista,nomVariante,nomFormato,numComic) values (?,?,?,?,?,?,?)";
//
//		nom_comic = nombreComic.getText();
//		nom_dibujante = nombreDibujante.getText();
//		nom_editorial = nombreEditorial.getText();
//		nom_guionista = nombreGuinista.getText();
//		nom_variante = nombreVariante.getText();
//		nom_Formato = nombreFormato.getText();
//		num_comic = numeroComic.getText();
//
//		if(pruebaConexion())
//		{
//			try {
//				PreparedStatement statement = conn.prepareStatement(sentenciaSQL);
//				statement.setString(1, nom_comic);
//				statement.setString(2, nom_dibujante);
//				statement.setString(3, nom_editorial);
//				statement.setString(4, nom_guionista);
//				statement.setString(5, nom_variante);
//				statement.setString(6, nom_Formato);
//				statement.setString(7, num_comic);
//
//				if (statement.executeUpdate() == 1) {
//					labelConexion.setStyle("-fx-background-color: #A0F52D");
//					labelResultado.setText("Comic añadido correctamente!" + "\nNombre del comic: " + nom_comic
//							+ "\nNombre del dibujante: " + nom_dibujante + "\nEditorial: " + nom_editorial
//							+ "\nGuinista: " + nom_guionista + "\nVariante: " + nom_variante + "\nNumero del comic: "
//							+ num_comic);
//					statement.close();
//				} else {
//					labelConexion.setStyle("-fx-background-color: #DD370F");
//					labelResultado.setText(
//							"Se ha encontrado un error. No ha sido posible añadir el comic a la base de datos.");
//				}
//			} catch (SQLException ex) {
//				System.err.println("Error al insertar un comic" + ex);
//			}
//		}
//	}
	

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
    private Label labelTestConexion;

    @FXML
    private TextField nombreComic;

    @FXML
    private TextField nombreDibujante;

    @FXML
    private TextField nombreEditorial;

    @FXML
    private TextField nombreFormato;

    @FXML
    private TextField nombreGuinista;

    @FXML
    private TextField nombreVariante;

    @FXML
    private TextField numeroComic;

    @FXML
    void BotonAniadir(ActionEvent event) {

    }

    @FXML
    void BotonLimpiarComic(ActionEvent event) {

    }

    @FXML
    void BotonVolver(ActionEvent event) {

    }

    @FXML
    void volverAlMenu(ActionEvent event) throws IOException {

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