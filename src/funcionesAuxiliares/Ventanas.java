/**
 * Contiene las clases que hacen funcionar las diferentes funciones de uso de back end y front de todo el proyecto
 *  
*/
package funcionesAuxiliares;

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
 *  - Puntuar comics que se encuentren dentro de la base de datos.
 *  Esta clase permite acceder al menu principal donde se puede viajar a diferentes ventanas, etc.
 *
 *  Version 8.0.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import Controladores.AccesoBBDDController;
import Controladores.CargaComicsController;
import Controladores.ImagenAmpliadaController;
import Controladores.MenuPrincipalController;
import Controladores.OpcionesAvanzadasController;
import Controladores.OpcionesDatosController;
import Controladores.RecomendacionesController;
import Controladores.SobreMiController;
import Controladores.VentanaAccionController;
import dbmanager.ConectManager;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesManagment.AccionReferencias;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Esta clase sirve para cambiar entre ventanas graficas
 *
 * @author Alejandro Rodriguez
 */
public class Ventanas {

	private Alert dialog = null;

	private Stage accesoBBDDStage = null;

	private Stage estadoConexionStage = null;

	private Stage estadoApiStage = null;

	private Stage menuCodigoBarras = null;

	public Stage cargaComics = null;

	private Stage menuPrincipal = null;

	private static Stage imagenAmpliada = null;
	private static Stage comicRecomendacion = null;
	private static Stage accionComic = null;
	private static Stage opcionesAvanzadasStage = null;
	private Stage opcionesDB = null;

	private boolean ventanaCerrada = false; // Variable para almacenar el estado de la ventana

	private static AccionReferencias referenciaVentanaPrincipal = getReferenciaVentanaPrincipal();

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	public void cerrarStages() {
		cerrarStage(accesoBBDDStage);
		cerrarStage(estadoConexionStage);
		cerrarStage(estadoApiStage);
		cerrarStage(menuCodigoBarras);
		cerrarStage(accionComic);
		cerrarStage(opcionesAvanzadasStage);
		cerrarStage(cargaComics);
		cerrarStage(menuPrincipal);
		cerrarStage(imagenAmpliada);
		cerrarStage(comicRecomendacion);
	}

	private void cerrarStage(Stage stage) {
		if (stage != null) {
			stage.close();
		}
	}

	/**
	 * Abre una ventana para el acceso a la base de datos. Carga la vista y muestra
	 * una nueva ventana con el controlador correspondiente.
	 */
	/**
	 * Muestra la ventana de acceso a la base de datos.
	 */
	public void verAccesoBBDD() {
		Platform.runLater(() -> {
			if (accesoBBDDStage == null || !accesoBBDDStage.isShowing()) {
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
					accesoBBDDStage = new Stage();
					accesoBBDDStage.setResizable(false);
					accesoBBDDStage.setTitle("Aplicacion bbdd comics");

					accesoBBDDStage.getIcons().add(new Image("/Icono/icon2.png"));

					// Asocio el stage con el scene
					accesoBBDDStage.setScene(scene);
					accesoBBDDStage.show();

					// Indico que debe hacer al cerrar
					accesoBBDDStage.setOnCloseRequest(e -> controlador.closeWindow());

				} catch (IOException ex) {
					alertaException(ex.toString());
				}
			}
		});
	}

	/**
	 * Abre la ventana del menú principal de la aplicación. Carga la vista de la
	 * ventana del menú principal y muestra la ventana correspondiente con su
	 * controlador. Define el tamaño mínimo y máximo de la ventana según la
	 * resolución de la pantalla. Asocia el comportamiento de cierre de la ventana.
	 */
	public void verMenuPrincipal() {
//		Platform.runLater(() -> {
		try {
			ventanaAbierta(menuPrincipal);

			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuPrincipal.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			MenuPrincipalController controlador = loader.getController();

			// Crea la escena
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/style/custom-combobox.css").toExternalForm());

			menuPrincipal = new Stage();
			// Determine the screen's dimensions
			Screen screen = Screen.getPrimary();
			Rectangle2D bounds = screen.getVisualBounds();

			double minWidth = 974;
			double minHeight = 607;

			// Set the minimum size
			menuPrincipal.setMinWidth(minWidth);
			menuPrincipal.setMinHeight(minHeight);

			// Verifica si la resolución es menor que las dimensiones mínimas
			if (bounds.getWidth() <= minWidth || bounds.getHeight() <= minHeight) {
				menuPrincipal.setWidth(minWidth);
				menuPrincipal.setHeight(minHeight);
			}

			// Set the maximum size to the screen's size
			menuPrincipal.setMaxWidth(bounds.getWidth());
			menuPrincipal.setMaxHeight(bounds.getHeight());

			menuPrincipal.setTitle("Menu principal");
			menuPrincipal.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			menuPrincipal.setScene(scene);
			menuPrincipal.show();

			// Indico qué hacer al cerrar
			menuPrincipal.setOnCloseRequest(e -> {
				cerrarStages();
				controlador.closeWindows();
				controlador.stop();

				menuPrincipal = null;
			});

		} catch (IOException ex) {
			alertaException(ex.toString());
			ex.printStackTrace();
		}
//		});
	}

	public static synchronized void ventanaAbierta(Stage ventanaActual) {
		if (ventanaActual != null) {
			ventanaActual.close();
			ventanaActual = null;
		}
	}

	/**
	 * Abre una ventana para realizar acciones en un cómic. Verifica si hay una
	 * ventana abierta y la cierra si es necesario. Carga la vista de la ventana de
	 * acciones del cómic y muestra la ventana correspondiente con su controlador.
	 * Define el comportamiento de cierre de la ventana y actualiza la referencia a
	 * la ventana actual.
	 */
	public static synchronized void verAccionComic() {
		try {
			// Verifica si hay una ventana abierta y ciérrala si es necesario
			ventanaAbierta(accionComic);

			FXMLLoader loader = new FXMLLoader(Ventanas.class.getResource("/ventanas/PantallaAccionComic.fxml"));

			Parent root = loader.load();

			VentanaAccionController controlador = loader.getController();

			Scene scene = new Scene(root);
			accionComic = new Stage();
			accionComic.setResizable(false);
			accionComic.setTitle("Acciones comic"); // Titulo de la aplicación.

			accionComic.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			accionComic.setScene(scene);
			accionComic.show();

			// Indico que debe hacer al cerrar
			accionComic.setOnCloseRequest(e -> {
				controlador.closeWindow();
				controlador.stop();
				accionComic = null; // Establece la ventana actual a null cuando se cierra
			});
			ConectManager.resetConnection();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Abre una ventana para realizar acciones en un cómic. Verifica si hay una
	 * ventana abierta y la cierra si es necesario. Carga la vista de la ventana de
	 * acciones del cómic y muestra la ventana correspondiente con su controlador.
	 * Define el comportamiento de cierre de la ventana y actualiza la referencia a
	 * la ventana actual.
	 */
	public static synchronized void verOpcionesAvanzadas() {
		try {
			// Verifica si hay una ventana abierta y ciérrala si es necesario
			ventanaAbierta(opcionesAvanzadasStage);

			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(Ventanas.class.getResource("/ventanas/OpcionesAvanzadas.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			OpcionesAvanzadasController controlador = loader.getController();

			// Creo la scene y el stage
			Scene scene = new Scene(root);
			opcionesAvanzadasStage = new Stage();
			opcionesAvanzadasStage.setResizable(false);
			opcionesAvanzadasStage.setTitle("Opciones avanzadas"); // Titulo de la aplicación.

			opcionesAvanzadasStage.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			opcionesAvanzadasStage.setScene(scene);
			opcionesAvanzadasStage.show();

			// Indico que debe hacer al cerrar
			opcionesAvanzadasStage.setOnCloseRequest(e -> {
				controlador.closeWindow();
				opcionesAvanzadasStage = null; // Establece la ventana actual a null cuando se cierra
			});
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Abre la ventana de recomendaciones de cómics. Carga la vista de la ventana de
	 * recomendaciones y muestra la ventana correspondiente con su controlador.
	 * Define el tamaño de la ventana, la asocia con el comportamiento de cierre y
	 * resetea la conexión a la base de datos.
	 */
	public static synchronized void verRecomendacion() {

		try {
			ventanaAbierta(comicRecomendacion);
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(Ventanas.class.getResource("/ventanas/RecomendacionComic.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			RecomendacionesController controlador = loader.getController();

			// Crea la escena y el escenario
			Scene scene = new Scene(root);

			comicRecomendacion = new Stage();
			comicRecomendacion.setResizable(false);
			comicRecomendacion.setTitle("Recomendaciones"); // Titulo de la aplicacion.
			comicRecomendacion.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			comicRecomendacion.setScene(scene);
			comicRecomendacion.show();
			// Indico que debe hacer al cerrar
			comicRecomendacion.setOnCloseRequest(e -> {
				controlador.closeWindows();
				comicRecomendacion = null; // Establece la ventana actual a null cuando se cierra
			});
			ConectManager.resetConnection();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Abre una ventana para realizar acciones en un cómic. Verifica si hay una
	 * ventana abierta y la cierra si es necesario. Carga la vista de la ventana de
	 * acciones del cómic y muestra la ventana correspondiente con su controlador.
	 * Define el comportamiento de cierre de la ventana y actualiza la referencia a
	 * la ventana actual.
	 */
	public static synchronized void verVentanaImagen() {

		try {
			// Verifica si hay una ventana abierta y ciérrala si es necesario
			ventanaAbierta(imagenAmpliada);

			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(Ventanas.class.getResource("/ventanas/ImagenAmpliadaComic.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			ImagenAmpliadaController controlador = loader.getController();

			// Creo la scene y el stage
			Scene scene = new Scene(root);
			imagenAmpliada = new Stage();
			imagenAmpliada.setResizable(false);
			imagenAmpliada.setTitle("Imagen ampliada"); // Titulo de la aplicación.

			imagenAmpliada.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			imagenAmpliada.setScene(scene);
			imagenAmpliada.show();

			// Indico que debe hacer al cerrar
			imagenAmpliada.setOnCloseRequest(e -> {
				controlador.closeWindow();
				imagenAmpliada = null; // Establece la ventana actual a null cuando se cierra
			});
			ConectManager.resetConnection();
		} catch (IOException ex) {
//			alertaException(ex.toString());
			ex.printStackTrace();
		}
	}

	public static int verVentanaNumero() {
		try {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Entrada de Número");
			dialog.setHeaderText("Por favor, ingrese un número (mayor que 0 y máximo 10):");
			dialog.setContentText("Número:");

			while (true) {
				Optional<String> result = dialog.showAndWait();

				if (result.isPresent()) {
					String input = result.get();
					if (isValidInteger(input)) {
						int inputValue = Integer.parseInt(input);
						if (inputValue > 0 && inputValue <= 10) {
							boolean isConfirmed = mostrarDialogoConfirmacion("Confirmación",
									"Se va a crear un clon de la comic un total de: " + inputValue
											+ " veces. ¿Estas de acuerdo?");
							if (isConfirmed) {
								return inputValue;
							}
						} else {
							mostrarAlerta("Entrada inválida", "Por favor, ingrese un número mayor que 0 y máximo 10.");
						}
					} else {
						mostrarAlerta("Entrada inválida",
								"Por favor, ingrese un número entero válido mayor que 0 y máximo 10.");
					}
				} else {
					return -1; // El diálogo fue cancelado
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			mostrarAlerta("Error", "Ocurrió un error al procesar la entrada.");
		}
		return -1;
	}

	private static boolean isValidInteger(String input) {
		try {
			int number = Integer.parseInt(input);
			return number > 0 && number <= 10;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean mostrarDialogoConfirmacion(String titulo, String mensaje) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensaje);

		Optional<ButtonType> result = alert.showAndWait();
		return result.isPresent() && result.get() == ButtonType.OK;
	}

	private static void mostrarAlerta(String titulo, String mensaje) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}

	/**
	 * Abre la ventana de opciones de acceso. Carga la vista de la ventana de
	 * opciones de acceso y muestra la ventana correspondiente con su controlador.
	 * Define el tamaño de la ventana, la asocia con el comportamiento de cierre y
	 * aplica estilos de hojas de estilo.
	 */
	public void verOpciones() {

		try {

			ventanaAbierta(opcionesDB);

			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/OpcionesAcceso.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			OpcionesDatosController controlador = loader.getController();

			// Crea la escena y el escenario
			Scene scene = new Scene(root);
//			scene.getStylesheets().add(getClass().getResource("/style/acces_style.css").toExternalForm());
//			scene.getStylesheets().add(getClass().getResource("/style/opciones_style.css").toExternalForm());

			opcionesDB = new Stage();
//			opcionesDB.setResizable(false);
			opcionesDB.setTitle("Opciones"); // Titulo de la aplicacion.
			opcionesDB.getIcons().add(new Image("/Icono/icon2.png"));

			// Asocio el stage con el scene
			opcionesDB.setScene(scene);
			opcionesDB.show();
			// Indico que debe hacer al cerrar
			opcionesDB.setOnCloseRequest(e -> {
				controlador.closeWindows();
				controlador.stop();
			});
		} catch (IOException ex) {
			alertaException(ex.toString());
		}
	}

	/**
	 * Abre la ventana "Sobre Mi". Carga la vista de la ventana "Sobre Mi" y muestra
	 * la ventana correspondiente con su controlador. Define el tamaño de la
	 * ventana, la asocia con el comportamiento de cierre y aplica estilos de hojas
	 * de estilo. Además, resetea la conexión a la base de datos.
	 */
	public void verSobreMi() {

		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/SobreMi.fxml"));

			// Cargo el padre
			Parent root = loader.load();

			// Obtengo el controlador
			SobreMiController controlador = loader.getController();

			// Crea la escena y el escenario
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
			stage.setOnCloseRequest(e -> controlador.closeWindows());

		} catch (IOException ex) {
			alertaException(ex.toString());
			ex.printStackTrace();
		}
	}

	/**
	 * Muestra una ventana de carga para la carga de cómics.
	 */
	public void verCargaComics(AtomicReference<CargaComicsController> cargaComicsControllerRef) {
		Platform.runLater(() -> {
			try {

				ventanaAbierta(cargaComics);
				ventanaCerrada = false; // Marcar la ventana como cerrada
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/PantallaCargaComic.fxml"));
				Parent root = loader.load();

				CargaComicsController cargaComicsController = loader.getController();
				cargaComicsControllerRef.set(cargaComicsController);

				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setResizable(false);
				stage.setTitle("Carga de comics"); // Titulo de la aplicacion.
				stage.getIcons().add(new Image("/Icono/icon2.png"));

				cargaComics = stage;
				FuncionesManejoFront.getStageVentanas().add(cargaComics);
				// Asocio el stage con el scene
				stage.setScene(scene);
				stage.show();

				stage.setOnCloseRequest(e -> {
					cargaComicsController.closeWindow();
					ventanaCerrada = true; // Marcar la ventana como cerrada
				});

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
	}

	public Stage devolverCargaComics() {
		return cargaComics;
	}

	// Método para obtener el estado de la ventana
	public boolean isVentanaCerrada() {

		if (cargaComics != null) {
			return ventanaCerrada;
		}
		return ventanaCerrada;
	}

	public boolean comprobarVentanaPrincipal() {
		return menuPrincipal != null;
	}

	public void cerrarCargaComics() {
		if (this.cargaComics != null) {
			this.cargaComics.close();
		}
	}

	public void cerrarVentanaAccion() {
		if (Ventanas.accionComic != null) {
			Ventanas.accionComic.close();
		}
	}

	public void cerrarMenuOpciones() {
		Platform.runLater(() -> {
			if (opcionesAvanzadasStage != null) {
				opcionesAvanzadasStage.close();
			}
		});
	}

	public void cerrarOpcionesDB() {
		if (this.opcionesDB != null) {
			this.opcionesDB.close();
		}
	}

	public static void cerrarVentanaActual(Stage nodoEscena) {
		Stage stage = (Stage) nodoEscena.getScene().getWindow();
		stage.close();
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
	public boolean alertaAccionGeneral() {
		Alert alert = new Alert(AlertType.CONFIRMATION);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Accion . . .");
		alert.setHeaderText("Vas a realizar una accion.");
		alert.setContentText("¿Estas seguro que quieres realizar la accion para el comic?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Llama a una ventana de alarma para eliminar datos
	 *
	 * @return
	 */
	public boolean alertaAccionNavegador() {
		Alert alert = new Alert(AlertType.CONFIRMATION);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Accion . . .");
		alert.setHeaderText("Vas a realizar una accion.");
		alert.setContentText(
				"Se va a abrir el navegador para descargar nodeJS ¿Estas seguro? (Sin nodeJS la funcionalidad del programa deja de ser automatica)");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Llama a una ventana de alarma para eliminar datos
	 *
	 * @return
	 */
	public boolean alertaFirmaActivada() {
		Alert alert = new Alert(AlertType.CONFIRMATION);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Accion . . .");
		alert.setHeaderText("Vas a realizar una accion.");
		alert.setContentText("¿Estas seguro que quieres actualizar comics con firmas tambien?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Llama a una ventana de alarma para eliminar datos
	 *
	 * @return
	 */
	public boolean alertaBorradoLista() {
		Alert alert = new Alert(AlertType.CONFIRMATION);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Accion . . .");
		alert.setHeaderText("Vas a realizar un borrado de las listas importadas");
		alert.setContentText("¿Estas seguro que quieres realizar el borrado de la lista temporal?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		}
		return false;
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
		alert.setContentText(" Estas seguro que quieres introducir el comic/comics?");
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
	public boolean alertaCreacionDB() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		stage.setResizable(false);
		alert.setTitle("Creando DB . . .");
		alert.setHeaderText("No existen base de datos en la carpeta raiz.");
		alert.setContentText(
				" Quieres crear una base de datos predeterminada? (Puedes crear otra de forma manual si lo deseas)");
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
	 * Solicita confirmación al usuario antes de borrar el contenido de la tabla.
	 *
	 * @return Un objeto CompletableFuture que se completará con true si el usuario
	 *         confirma la eliminación, o con false si el usuario cancela la
	 *         operación.
	 */
	public CompletableFuture<Boolean> borrarContenidoTabla() {
		CompletableFuture<Boolean> futureResult = new CompletableFuture<>();

		Platform.runLater(() -> {
			FuncionesManejoFront.cambiarEstadoMenuBar(true, getReferenciaVentanaPrincipal());

			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
			Alert alert1 = new Alert(AlertType.CONFIRMATION);
			Stage stage1 = (Stage) alert1.getDialogPane().getScene().getWindow();
			stage1.setResizable(false);
			stage1.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
			alert1.setTitle("Borrando . . .");
			alert1.setHeaderText("Estás a punto de borrar el contenido.");
			alert1.setContentText("¿Estás seguro que quieres borrarlo todo?");

			Optional<ButtonType> result1 = alert1.showAndWait();
			result1.ifPresent(result -> {
				if (result == ButtonType.OK) {
					Alert alert2 = new Alert(AlertType.CONFIRMATION);
					Stage stage2 = (Stage) alert2.getDialogPane().getScene().getWindow();
					stage2.setResizable(false);
					stage2.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
					alert2.setTitle("Borrando . . .");
					alert2.setHeaderText("¿Estás seguro?");
					alert2.setContentText("¿De verdad de verdad quieres borrarlo todo?");

					alert2.showAndWait().ifPresent(result2 -> futureResult.complete(result2 == ButtonType.OK));
				} else {
					FuncionesManejoFront.cambiarEstadoMenuBar(false, getReferenciaVentanaPrincipal());
					futureResult.complete(false);
				}
			});
		});

		return futureResult;
	}

	/**
	 * Solicita confirmación al usuario antes de borrar el contenido de la
	 * configuración.
	 *
	 * @return true si el usuario confirma la eliminación, o false si el usuario
	 *         cancela la operación.
	 */
	public boolean reiniciarOrdenadorVentana() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setResizable(false);
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		alert.setTitle("Borrando . . .");
		alert.setHeaderText("Estas a punto de reiniciar el sistema");
		alert.setContentText(
				"Para poder utilizar el programar de forma correcta, se recomienda la primera vez que se reinicie el sistema "
						+ "para asegurar que nodejs esta instalado correctamente. ¿Quieres reiniciar el ordenador ahora?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Solicita confirmación al usuario antes de borrar el contenido de la
	 * configuración.
	 *
	 * @return true si el usuario confirma la eliminación, o false si el usuario
	 *         cancela la operación.
	 */
	public boolean borrarContenidoConfiguracion() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setResizable(false);
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
	 * Solicita confirmación al usuario antes de borrar el contenido de la lista de
	 * comics guardados.
	 *
	 * @return true si el usuario confirma la eliminación, o false si el usuario
	 *         cancela la operación.
	 */
	public boolean borrarListaGuardada() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setResizable(false);
		stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
		alert.setTitle("Borrando . . .");
		alert.setHeaderText("Estas a punto de borrar el contenido.");
		alert.setContentText("¿Estas seguro que quieres borrar la lista guardada?");
		if (alert.showAndWait().get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Solicita confirmación al usuario antes de cancelar la subida de portadas.
	 *
	 * @return Un objeto CompletableFuture que se completará con true si el usuario
	 *         confirma la cancelación, o con false si el usuario decide continuar
	 *         la subida.
	 */
	public CompletableFuture<Boolean> cancelarSubidaPortadas() {
		CompletableFuture<Boolean> futureResult = new CompletableFuture<>();

		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("/Icono/warning.jpg")); // To add an icon
			alert.setTitle("Subiendo datos . . .");
			alert.setHeaderText("Estas apunto de seleccionar la carpeta con las portadas");
			alert.setContentText("¿Quieres continuar? En caso de cancelar, se subirán portadas predeterminadas.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				futureResult.complete(true);
			} else {
				futureResult.complete(false);
			}
		});

		return futureResult;
	}

	/**
	 * Llama a una ventana de alarma que avisa si hay una excepción.
	 *
	 * @param excepcion La excepción que se mostrará en la ventana de alerta.
	 */
	public void alertaException(String excepcion) {
		Platform.runLater(() -> {
			if (dialog == null || !dialog.isShowing()) {
				dialog = new Alert(AlertType.ERROR, excepcion, ButtonType.OK);
				Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("/Icono/icon2.png")); // Reemplaza con la ruta de tu icono

				dialog.showAndWait(); // Mostrar y esperar hasta que se cierre
			}
		});
	}

	/**
	 * Llama a una ventana de alarma que avisa si hay una excepcion.
	 *
	 * @param excepcion
	 */
	public void alertaNoApi(String excepcion) {
		Platform.runLater(() -> {
			if (dialog == null || !dialog.isShowing()) {
				dialog = new Alert(AlertType.ERROR, excepcion, ButtonType.OK);
				Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("/Icono/icon2.png"));
				dialog.showAndWait(); // Mostrar y esperar hasta que se cierre
			}
		});
	}

	public static AccionReferencias getReferenciaVentanaPrincipal() {
		return referenciaVentanaPrincipal;
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentanaPrincipal(AccionReferencias referenciaVentana) {
		Ventanas.referenciaVentanaPrincipal = referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		Ventanas.referenciaVentana = referenciaVentana;
	}
}