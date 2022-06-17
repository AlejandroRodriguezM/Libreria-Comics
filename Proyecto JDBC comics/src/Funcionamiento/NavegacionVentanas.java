package Funcionamiento;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Controladores.AccesoBBDDController;
import Controladores.EliminarDatosController;
import Controladores.IntroducirDatosController;
import Controladores.MenuPrincipalController;
import Controladores.ModificarDatosController;
import Controladores.RecomendacionesComic;
import SinUsar.MenuOpcionesController;
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
	public void verAccesoBBDD() {

		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/AccesoBBDD.fxml"));

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
	 * Metodo que carga y muestra de la base de datos.
	 * 
	 * @throws IOException
	 */
	public void verMenuPrincipal() throws IOException {

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
			stage.setOnCloseRequest(e -> {
				try {
					controlador.closeWindows();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

		} catch (IOException ex) {
			Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Metodo que carga y llama a la ventana de añadir comics
	 */
	public void verIntroducirDatos() {

		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/IntroducirDatos.fxml"));

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
			stage.setOnCloseRequest(e -> {
				try {
					controlador.closeWindows();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

			stage.setScene(scene);
			stage.show();

		} catch (IOException ex) {
			Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void verModificarDatos() {

		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/ModificarDatos.fxml"));

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
			stage.setOnCloseRequest(e -> {
				try {
					controlador.closeWindows();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

			stage.setScene(scene);
			stage.show();

		} catch (IOException ex) {
			Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void verRecomendacion() {

		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/RecomendacionComic.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			RecomendacionesComic controlador = loader.getController();

			// Creo la scene y el stage
			Scene scene = new Scene(root);
			Stage stage = new Stage();

			// Asocio el stage con el scene
			stage.setScene(scene);
			stage.show();

			// Indico que debe hacer al cerrar
			stage.setOnCloseRequest(e -> {
				try {
					controlador.closeWindows();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

			stage.setScene(scene);
			stage.show();

		} catch (IOException ex) {
			Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void verEliminarDatos() {

		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/EliminarDatos.fxml"));

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
			stage.setOnCloseRequest(e -> {
				try {
					controlador.closeWindows();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

			stage.setScene(scene);
			stage.show();

		} catch (IOException ex) {
			Logger.getLogger(MenuOpcionesController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 */
	public void cerrarVentanaMenuPrincipal() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/AccesoBBDD.fxml"));

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
	 * 
	 * @param event
	 */
	public boolean salirPrograma(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Saliendo");
		alert.setHeaderText("Estas apunto de salir.");
		alert.setContentText("¿Estas seguro que quieres salir?");

		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}

	}
}
