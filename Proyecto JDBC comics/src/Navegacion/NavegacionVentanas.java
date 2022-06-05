package Navegacion;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Funcionamiento.MenuOpcionesController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavegacionVentanas {
	
	public void menuOpciones() {

//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuOpciones.fxml"));
//
//            Parent root = loader.load();
//
//            Scene scene = new Scene(root);
//            Stage stage = new Stage();
//
//            stage.setScene(scene);
//            stage.show();
//
//
//        } catch (IOException ex) {
//            Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
	
	public void menuBBDD() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuPrincipal.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();


        } catch (IOException ex) {
            Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	public void verBBDD() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/verBBDD.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();


        } catch (IOException ex) {
            Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	public void aniadirDatos() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/aniadirComicsBBDD.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();


        } catch (IOException ex) {
            Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	

}
