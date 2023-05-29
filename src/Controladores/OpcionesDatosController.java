package Controladores;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Funcionamiento.Ventanas;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class OpcionesDatosController implements Initializable {

	@FXML
	private Menu navegacion_cerrar;

	@FXML
	private Menu navegacion_comic;

	@FXML
	private Button botonCrearBBDD;

	@FXML
	private Button boton_abrir;

	@FXML
	private Button boton_guardar;

	@FXML
	private Button boton_restaurar;

	@FXML
	private ToggleGroup estado;

	@FXML
	private Label etiquetaHost;

	@FXML
	private RadioButton noOffline;

	@FXML
	private TextField nombreBBDD;

	@FXML
	private PasswordField nombreHost;

	@FXML
	private PasswordField pass;

	@FXML
	private TextField puertobbdd;

	@FXML
	private TextField usuario;

	@FXML
	private Label prontEstadoFichero;

	@FXML
	private RadioButton siOnline;

	private static Ventanas nav = new Ventanas();
//	private static Utilidades utilidad = new Utilidades();
	private static AccesoBBDDController acceso = new AccesoBBDDController();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		TextFormatter<Integer> textFormatterAni = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});
		puertobbdd.setTextFormatter(textFormatterAni);

		acceso.crearEstructura();

		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion.conf";

		try (BufferedReader reader = new BufferedReader(new FileReader(archivoConfiguracion))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Usuario: ")) {
					String usuarioTexto = line.substring("Usuario: ".length());
					usuario.setText(usuarioTexto);
				} else if (line.startsWith("Password: ")) {
					String passwordTexto = line.substring("Password: ".length());
					pass.setText(passwordTexto);
				} else if (line.startsWith("Puerto: ")) {
					String puertoTexto = line.substring("Puerto: ".length());
					puertobbdd.setText(puertoTexto);
				} else if (line.startsWith("Database: ")) {
					String databaseTexto = line.substring("Database: ".length());
					nombreBBDD.setText(databaseTexto);
				} else if (line.startsWith("Hosting: ")) {
					String hostingTexto = line.substring("Hosting: ".length());
					nombreHost.setText(hostingTexto);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void abrirUbicacion(ActionEvent event) {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming" + File.separator
				+ "libreria";

		File carpeta = new File(ubicacion);

		if (Desktop.isDesktopSupported() && carpeta.exists()) {
			try {
				Desktop.getDesktop().open(carpeta);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void crearBBDD(ActionEvent event) {
		nav.verCrearBBDD();

		Stage myStage = (Stage) this.botonCrearBBDD.getScene().getWindow();
		myStage.close();
	}

	@FXML
	void guardarDatos(ActionEvent event) {

		acceso.crearEstructura();

		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String carpetaBackup = carpetaLibreria + File.separator + nombreBBDD.getText() + File.separator + "backups";
		String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion.conf";

		try {
			FileWriter fileWriter = new FileWriter(archivoConfiguracion);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write("###############################");
			bufferedWriter.newLine();
			bufferedWriter.write("Fichero de configuracion de la libreria");
			bufferedWriter.newLine();
			bufferedWriter.write("###############################");
			bufferedWriter.newLine();
			bufferedWriter.write("Usuario: " + usuario.getText());
			bufferedWriter.newLine();
			bufferedWriter.write("Password: " + pass.getText());
			bufferedWriter.newLine();
			bufferedWriter.write("Puerto: " + puertobbdd.getText());
			bufferedWriter.newLine();
			bufferedWriter.write("Database: " + nombreBBDD.getText());
			bufferedWriter.newLine();
			bufferedWriter.write("Hosting: " + nombreHost.getText());
			bufferedWriter.newLine();

			bufferedWriter.close();

			File carpeta_backupsFile = new File(carpetaBackup);
			if (!carpeta_backupsFile.exists()) {
				carpeta_backupsFile.mkdir();
			}

			prontEstadoFichero.setStyle("-fx-background-color: #A0F52D");
			prontEstadoFichero.setText("Fichero guardado");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void restaurarConfiguracion(ActionEvent event) {
		try {
			if (nav.borrarContenidoConfiguracion()) {
				String userHome = System.getProperty("user.home");
				String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
				String carpetaLibreria = ubicacion + "\\libreria";
				String archivoConfiguracion = carpetaLibreria + File.separator + "configuracion.conf";

				// Verificar y borrar la carpeta "libreria" si existe
				File carpetaLibreriaFile = new File(carpetaLibreria);

				// Crear la carpeta "libreria"
				carpetaLibreriaFile.mkdir();

				// Verificar y crear el archivo "configuracion.conf"
				File archivoConfiguracionFile = new File(archivoConfiguracion);

				archivoConfiguracionFile.createNewFile();

				// Escribir líneas en el archivo
				FileWriter fileWriter = new FileWriter(archivoConfiguracionFile);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write("###############################");
				bufferedWriter.newLine();
				bufferedWriter.write("Fichero de configuracion de la libreria");
				bufferedWriter.newLine();
				bufferedWriter.write("###############################");
				bufferedWriter.newLine();
				bufferedWriter.write("Usuario:");
				bufferedWriter.newLine();
				bufferedWriter.write("Password:");
				bufferedWriter.newLine();
				bufferedWriter.write("Puerto:");
				bufferedWriter.newLine();
				bufferedWriter.write("Database:");
				bufferedWriter.newLine();
				bufferedWriter.write("Hosting:");
				bufferedWriter.newLine();

				bufferedWriter.close();

				limpiar_datos();
				prontEstadoFichero.setStyle("-fx-background-color: #A0F52D");
				prontEstadoFichero.setText("Ficheros restaurados correctamente");
			} else {
				prontEstadoFichero.setStyle("-fx-background-color: #DD370F");
				prontEstadoFichero.setText("Has cancelado la restauracion de ficheros..");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void limpiar_datos() {
		usuario.setText("");

		pass.setText("");

		puertobbdd.setText("");

		nombreBBDD.setText("");

		nombreHost.setText("");
	}

	@FXML
	void selectorBotonHost(ActionEvent event) {
		selectorHost();
	}

	/**
	 * Funcion que permite conectarse a un host online o usar el local.
	 *
	 * @return
	 */
	public String selectorHost() {

		if (siOnline.isSelected()) {
			etiquetaHost.setText("Nombre del host: ");
			nombreHost.setDisable(false);
			nombreHost.setOpacity(1);
			return nombreHost.getText();
		}
		if (noOffline.isSelected()) {
			etiquetaHost.setText("Offline");
			nombreHost.setDisable(true);
			nombreHost.setOpacity(0);
			return "localhost";
		}
		return "localhost";
	}
	
	/////////////////////////////////
	//// METODOS LLAMADA A VENTANAS//
	/////////////////////////////////

	/**
	 * Permite abrir y cargar la ventana para IntroducirDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaAniadir(ActionEvent event) {

		nav.verIntroducirDatos();
	    Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
	    myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana de EliminarDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaEliminar(ActionEvent event) {

		nav.verEliminarDatos();

	    Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
	    myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana de ModificarDatosController
	 *
	 * @param event
	 */
	@FXML
	public void ventanaModificar(ActionEvent event) {

		nav.verModificarDatos();

	    Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
	    myStage.close();
	}

	/**
	 * Permite el cambio de ventana a la ventana deRecomendacionesController
	 *
	 * @param event
	 */
	@FXML
	void ventanaRecomendar(ActionEvent event) {

		nav.verRecomendacion();

	    Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
	    myStage.close();
	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void volverMenu(ActionEvent event) throws IOException {
		nav.verMenuPrincipal();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	@FXML
	public void salirPrograma(ActionEvent event) {
		// Logic to handle the "Eliminar" action
		nav.salirPrograma(event);

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {

		Platform.exit();

	}

}