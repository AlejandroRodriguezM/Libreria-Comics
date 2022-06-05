package Funcionamiento;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class MenuOpcionesController implements Initializable{

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}