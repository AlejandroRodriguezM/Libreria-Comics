package Funcionamiento;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Controladores.AniadirDatosController;
import Controladores.MenuPrincipalController;
import Controladores.VerComicController;
import SinUsar.MenuOpcionesController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavegacionVentanas {

	
	public void menuPrincipal() {

        try {
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
    		
    		DBManager.close();

        } catch (IOException ex) {
            Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	public void verBBDD() throws IOException {

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
		
		} catch (IOException ex) {
            Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	public void aniadirDatos() {

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

            stage.setScene(scene);
            stage.show();


        } catch (IOException ex) {
            Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	

}
