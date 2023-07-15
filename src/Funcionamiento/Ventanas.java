package Funcionamiento;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Introducir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase permite acceder a la base de datos introduciendo los diferentes datos que nos pide.
 *
 *  Version Final
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
import Controladores.PuntuarDatosController;
import Controladores.RecomendacionesController;
import Controladores.SobreMiController;
import JDBC.DBManager;
import Controladores.OpcionesDatosController;
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

/**
 * Esta clase sirve para cambiar entre ventanas graficas
 *
 * @author Alejandro Rodriguez
 */
public class Ventanas {

	/**
	 * Llamada a ventana para el acceso a la base de datos
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
			scene.getStylesheets().add(getClass().getResource("/style/acces_style.css").toExternalForm());
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

	/**
	 * Llamada a ventana para el menu principal
	 */
	public void verMenuPrincipal() {

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
				controlador.closeWindows();
			});

		} catch (IOException ex) {
			alertaException(ex.toString());
			ex.printStackTrace();
		}
	}

	/**
	 * Llamada a ventana para el acceso a introducir datos a la base de datos
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
			stage.setResizable(false);
			stage.setTitle("Introducir datos"); // Titulo de la aplicacion.
			stage.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			stage.setScene(scene);
			stage.show();

			// Indico que debe hacer al cerrar
			stage.setOnCloseRequest(e -> {
				controlador.closeWindows();
			});

			DBManager.resetConnection();
		} catch (IOException ex) {
			alertaException(ex.toString());
		}
	}

	/**
	 * Llamada a ventana para el acceso a modificar datos a la base de datos
	 */
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
				controlador.closeWindows();
			});
			DBManager.resetConnection();
		} catch (IOException ex) {
			alertaException(ex.toString());
		}
	}

	/**
	 * Llamada a ventana para el acceso comics recomendados
	 */
	public void verRecomendacion() {

		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/RecomendacionComic.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			RecomendacionesController controlador = loader.getController();

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
				controlador.closeWindows();
			});

			DBManager.resetConnection();

		} catch (IOException ex) {
			alertaException(ex.toString());
		}
	}

	/**
	 * Llamada a ventana para el acceso a eliminar datos a la base de datos
	 */
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

				controlador.closeWindows();

			});
			DBManager.resetConnection();

		} catch (IOException ex) {
			alertaException(ex.toString());
		}
	}

	/**
	 * Llamada a ventana para el acceso a creacion de bases de datos
	 */
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
			scene.getStylesheets().add(getClass().getResource("/style/opciones_style.css").toExternalForm());

			Stage stage = new Stage();
			stage.setResizable(false);
			stage.setTitle("Menu de creacion"); // Titulo de la aplicacion.
			stage.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			stage.setScene(scene);
			stage.show();

			// Indico que debe hacer al cerrar
			stage.setOnCloseRequest(e -> {
				controlador.closeWindows();
			});

		} catch (IOException ex) {
			alertaException(ex.toString());
			ex.printStackTrace();
		}
	}

	/**
	 * Llamada a ventana para el acceso a creacion de bases de datos
	 */
	public void verOpciones() {

		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/opcionesAcceso.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			OpcionesDatosController controlador = loader.getController();

			// Creo la scene y el stage
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/style/acces_style.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("/style/opciones_style.css").toExternalForm());

			Stage stage = new Stage();
			stage.setResizable(false);
			stage.setTitle("Opciones"); // Titulo de la aplicacion.
			stage.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			stage.setScene(scene);
			stage.show();

			// Indico que debe hacer al cerrar
			stage.setOnCloseRequest(e -> {
				controlador.closeWindows();
			});
		} catch (IOException ex) {
			alertaException(ex.toString());
		}
	}

	/**
	 * Llamada a ventana para el acceso a creacion de bases de datos
	 */
	public void verSobreMi() {

		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/SobreMi.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			SobreMiController controlador = loader.getController();

			// Creo la scene y el stage
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/style/sobremi_style.css").toExternalForm());
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.setTitle("Sobre mi"); // Titulo de la aplicacion.
			stage.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			stage.setScene(scene);
			stage.show();

			// Indico que debe hacer al cerrar
			stage.setOnCloseRequest(e -> {
				controlador.closeWindows();
			});
			DBManager.resetConnection();

		} catch (IOException ex) {
			alertaException(ex.toString());
			ex.printStackTrace();
		}
	}

	/**
	 * Llamada a ventana para el acceso a creacion de bases de datos
	 */
	public void verPuntuar() {

		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/PuntuarDatos.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			PuntuarDatosController controlador = loader.getController();

			// Creo la scene y el stage
			Scene scene = new Scene(root);
//			scene.getStylesheets().add(getClass().getResource("/style/genericos_style.css").toExternalForm());

			Stage stage = new Stage();
			stage.setResizable(false);
			stage.setTitle("Puntuacion de comic"); // Titulo de la aplicacion.
			stage.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			stage.setScene(scene);
			stage.show();

			// Indico que debe hacer al cerrar
			stage.setOnCloseRequest(e -> {
				controlador.closeWindows();
			});

		} catch (IOException ex) {
			alertaException(ex.toString());
			ex.printStackTrace();
		}
	}

	/**
	 * Permite salir del programa completamente
	 *
	 * @param event
	 * @return
	 */
	public boolean salirPrograma(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setResizable(false);
		stage.getIcons().add(new Image("/Icono/exit.png")); // To add an icon
		alert.setTitle("Saliendo");
		alert.setHeaderText("Estas apunto de salir.");
		alert.setContentText(" Estas seguro que quieres salir?");

		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Llama a una ventana de alarma para eliminar datos
	 *
	 * @return
	 */
	public boolean alertaEliminar() {
		Alert alert = new Alert(AlertType.CONFIRMATION);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Eliminando . . .");
		alert.setHeaderText("Estas apunto de eliminar datos.");
		alert.setContentText(" Estas seguro que quieres eliminar el comic?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Llama a una ventana de alarma que avisa si se va a modificar un dato
	 *
	 * @return
	 */
	public boolean alertaModificar() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Modificando . . .");
		alert.setHeaderText("Estas apunto de modificar datos.");
		alert.setContentText(" Estas seguro que quieres modificar el comic?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Llama a una ventana de alarma que avisa si se va a modificar un dato
	 *
	 * @return
	 */
	public boolean alertaBorrarPuntuacion() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Borrando puntuacion . . .");
		alert.setHeaderText("Estas apunto de borrar la puntuacion.");
		alert.setContentText(" Estas seguro que borrar la puntuacion del comic?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Llama a una ventana de alarma que avisa si se va a modificar un dato
	 *
	 * @return
	 */
	public boolean alertaAgregarPuntuacion() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Agregando puntuacion . . .");
		alert.setHeaderText("Estas apunto de agregar la puntuacion.");
		alert.setContentText(" Estas seguro que agregar la puntuacion del comic?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Llama a una ventana de alarma que avisa si se va a introducir un dato
	 *
	 * @return
	 */
	public boolean alertaInsertar() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Insertando . . .");
		alert.setHeaderText("Estas apunto de introducir datos.");
		alert.setContentText(" Estas seguro que quieres introducir el comic?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Llama a una ventana de alarma que avisa si se va a introducir un dato
	 *
	 * @return
	 */
	public boolean alertaPortadaVacia() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Insertando . . .");
		alert.setHeaderText("Parece que te has olvidado de la portada.");
		alert.setContentText(" Estas seguro que quieres introducir el comic sin portada??");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Llama a una ventana de alarma que avisa si se va reconstruir la base de datos
	 *
	 * @return
	 */
	public boolean alertaTablaError() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Tablas no encontradas");
		alert.setHeaderText("ERROR. No tienes la base de datos bien construida.");
		alert.setContentText("¿Quieres recontruir la base de datos?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Funcion que permite borrar el contenido de la tabla de la base de datos.
	 *
	 * @return
	 */
	public boolean borrarContenidoTabla() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		alert.setTitle("Borrando . . .");
		alert.setHeaderText("Estas a punto de borrar el contenido.");
		alert.setContentText("Estas seguro que quieres borrarlo todo?");
		if (alert.showAndWait().get() == ButtonType.OK) {

			Alert alert2 = new Alert(AlertType.CONFIRMATION);
			Stage stage2 = (Stage) alert2.getDialogPane().getScene().getWindow();
			stage2.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
			alert.setTitle("Borrando . . .");
			alert.setHeaderText("Estas seguro?");
			alert.setContentText("De verdad de verdad quieres borrarlo todo?");
			if (alert.showAndWait().get() == ButtonType.OK) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Funcion que permite borrar el contenido de la tabla de la base de datos.
	 *
	 * @return
	 */
	public boolean borrarContenidoConfiguracion() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		alert.setTitle("Borrando . . .");
		alert.setHeaderText("Estas a punto de borrar el contenido.");
		alert.setContentText("¿Estas seguro que quieres restaurar la configuracion original?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Llama a una ventana de alarma que avisa si hay una excepcion.
	 *
	 * @param excepcion
	 */
	public void alertaException(String excepcion) {
	    Platform.runLater(() -> {
	        Alert dialog = new Alert(AlertType.ERROR, excepcion, ButtonType.OK);
	        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
	        stage.getIcons().add(new Image("/Icono/icon.png"));  // Reemplaza "path/to/your/icon.png" con la ruta de tu icono
	        
	        dialog.show();
	    });
	}
}
