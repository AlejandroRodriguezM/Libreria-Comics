package Funcionamiento;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - AÃ±adir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase permite acceder a la base de datos introduciendo los diferentes datos que nos pide.
 *
 *  Version 2.5
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.io.IOException;

import Controladores.AccesoBBDDController;
import Controladores.CrearBBDDController;
import Controladores.EliminarDatosController;
import Controladores.IntroducirDatosController;
import Controladores.MenuPrincipalController;
import Controladores.ModificarDatosController;
import Controladores.RecomendacionesComic;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class NavegacionVentanas {


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
			stage.setTitle("Aplicacion bbdd comics"); // Titulo de la aplicacion.

			stage.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			stage.setScene(scene);
			stage.show();

			// Indico que debe hacer al cerrar
			stage.setOnCloseRequest(e -> controlador.closeWindows());

		} catch (IOException ex) {
			alertaException(ex.toString());
		}
	}


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
			stage.setResizable(false);
			stage.setTitle("Menu principal"); // Titulo de la aplicacion.
			stage.getIcons().add(new Image("/Icono/icon2.png"));

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
			alertaException(ex.toString());
		}
	}


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
			stage.setResizable(false);
			stage.setTitle("Introducir datos"); // Titulo de la aplicacion.
			stage.getIcons().add(new Image("/Icono/icon2.png"));

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
			alertaException(ex.toString());
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
			stage.setResizable(false);
			stage.setTitle("Modificar datos"); // Titulo de la aplicacion.
			stage.getIcons().add(new Image("/Icono/icon2.png"));

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
			alertaException(ex.toString());
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
			stage.setResizable(false);
			stage.setTitle("Recomendaciones"); // Titulo de la aplicacion.
			stage.getIcons().add(new Image("/Icono/icon2.png"));

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
			alertaException(ex.toString());
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
			stage.setResizable(false);
			stage.setTitle("Eliminar comics"); // Titulo de la aplicacion.
			stage.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			stage.setScene(scene);
			stage.show();

			// Indico que debe hacer al cerrar
			stage.setOnCloseRequest(e -> {
		
			try {
				controlador.closeWindows();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				alertaException(e1.toString());
			}
				
			});

			stage.setScene(scene);
			stage.show();

		} catch (IOException ex) {
			alertaException(ex.toString());
		}
	}


	public void verCrearBBDD() {

		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/CrearBBDD.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			CrearBBDDController controlador = loader.getController();

			// Creo la scene y el stage
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.setTitle("Menu de creacion"); // Titulo de la aplicacion.
			stage.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			stage.setScene(scene);
			stage.show();

			// Indico que debe hacer al cerrar
			stage.setOnCloseRequest(e -> {
				try {
					controlador.closeWindows();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					alertaException(e1.toString());
				}
			});

			stage.setScene(scene);
			stage.show();

		} catch (IOException ex) {
			alertaException(ex.toString());
		}
	}


	public boolean salirPrograma(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setResizable(false);
		stage.getIcons().add(new Image("/Icono/exit.png")); // To add an icon
		alert.setTitle("Saliendo");
		alert.setHeaderText("Estas apunto de salir.");
		alert.setContentText("¿Estas seguro que quieres salir?");

		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}


	public boolean alertaEliminar() {
		Alert alert = new Alert(AlertType.CONFIRMATION);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Eliminando . . .");
		alert.setHeaderText("Estas apunto de eliminar datos.");
		alert.setContentText("¿Estas seguro que quieres eliminar el comic?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}


	public boolean alertaModificar() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Modificando . . .");
		alert.setHeaderText("Estas apunto de modificar datos.");
		alert.setContentText("¿Estas seguro que quieres modificar el comic?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}


	public void alertaException(String excepcion)
	{
		Platform.runLater(() -> {
			Alert dialog = new Alert(AlertType.ERROR, excepcion, ButtonType.OK);
			dialog.show();
		});
	}
}
