package Funcionamiento;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Controladores.IntroducirDatosController;
import Controladores.EliminarDatosController;
import Controladores.AccesoBBDDController;
import Controladores.ModificarDatosController;
import Controladores.MenuPrincipalController;
import SinUsar.MenuOpcionesController;
import SinUsar.VentanaModificarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class NavegacionVentanas {

	/**
	 * Metodo que carga y muestra la ventana del menu principal
	 */
	public void menuPrincipal() {

        try {
    		// Cargo la vista
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuPrincipal.fxml"));

    		// Cargo el padre
    		Parent root = loader.load();

    		// Obtengo el controlador
    		AccesoBBDDController controlador = loader.getController();

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
	
	/**
	 * Metodo que carga y muestra la ventana de la muestra de la base de datos.
	 * @throws IOException
	 */
	public void verBBDD() throws IOException {

		try {
		// Cargo la vista
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/verBBDD.fxml"));

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
		
		} catch (IOException ex) {
            Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	/**
	 * Metodo que carga y llama a la ventana de añadir comics
	 */
	public void aniadirDatos() {

        try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/AniadirComicsBBDD.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			IntroducirDatosController controlador = loader.getController();

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
	
	public void ModificarDatos() {

        try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/ModificarComicBBDD.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			ModificarDatosController controlador = loader.getController();

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
	
	public void VentanaModificacion() {

        try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaDatosModificar.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			VentanaModificarController controlador = loader.getController();

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
	
	public void EliminarDatos() {

        try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/EliminarComicBBDD.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			EliminarDatosController controlador = loader.getController();

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
	
	/**
	 * Al cerrar la ventana, se cargara la ventana de verBBDD
	 */
	public void cerrarVentanaSubMenu() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/verBBDD.fxml.fxml"));

			Parent root = loader.load();

			Scene scene = new Scene(root);
			Stage stage = new Stage();

			stage.setScene(scene);
			stage.show();

		} catch (IOException ex) {
			Logger.getLogger(AccesoBBDDController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 */
	public void cerrarVentanaMenuPrincipal() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuPrincipal.fxml"));

			Parent root = loader.load();

			Scene scene = new Scene(root);
			Stage stage = new Stage();

			stage.setScene(scene);
			stage.show();



		} catch (IOException ex) {
			Logger.getLogger(AccesoBBDDController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Permite salir completamente del programa.
	 * @param event
	 */
	public boolean salirPrograma(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Saliendo");
		alert.setHeaderText("Estas apunto de salir.");
		alert.setContentText("¿Estas seguro que quieres salir?");

		if(alert.showAndWait().get() == ButtonType.OK)
		{
			return true;
		}
		else
		{
			return false;
		}

	}
}
