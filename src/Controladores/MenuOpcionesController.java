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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


public class MenuOpcionesController{

    @FXML
    private Button BotonVentanaAniadir;

    @FXML
    private Button botonGuardarBaseDatos;

    @FXML
    private Button botonVerBaseDatos;
    
    @FXML
    protected Button botonVolver;
    
    @FXML
    protected Button botonSalir;

    @FXML
    public void Guardarbbdd(ActionEvent event) {

    }

    @FXML
    public void VentanaAniadir(ActionEvent event) {

        try {
            // Cargo la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/AniadirComicsBBDD.fxml"));

            // Cargo el padre
            Parent root = loader.load();

            // Obtengo el controlador
            AniadirDatosController controlador = loader.getController();

            // Creo la scene y el stage
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            // Asocio el stage con el scene
            stage.setScene(scene);
            stage.show();

            // Indico que debe hacer al cerrar
            stage.setOnCloseRequest(e -> controlador.closeWindows());

            // Ciero la ventana donde estoy
            Stage myStage = (Stage) this.BotonVentanaAniadir.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    	
    }

    @FXML
    public void verBaseDatos(ActionEvent event) {
    	 try {
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
             Stage myStage = (Stage) this.botonVerBaseDatos.getScene().getWindow();
             myStage.close();

         } catch (IOException ex) {
             Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    @FXML
    public void volverInicio(ActionEvent event) throws IOException {
    	
		 // Cargo la vista
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuPrincipal.fxml"));

        // Cargo el padre
        Parent root = loader.load();

        // Obtengo el controlador
        MenuPrincipalController controlador = loader.getController();

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
        
        DBManager.DBManager.close();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuPrincipal.fxml"));

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