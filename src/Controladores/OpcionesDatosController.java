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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

public class OpcionesDatosController implements Initializable {

	@FXML
	private Label password_label;

	@FXML
	private Label puerto_label;

	@FXML
	private Label nombre_label;

	@FXML
	private Label host_label;

	@FXML
	private Label etiquetaHost;

	@FXML
	private Label prontEstadoFichero;

	@FXML
	private Button botonCrearBBDD;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Button boton_abrir;

	@FXML
	private Button boton_guardar;

	@FXML
	private Button boton_restaurar;

	@FXML
	private ToggleGroup estado;

	@FXML
	private RadioButton noOffline;

	@FXML
	private TextField nombreBBDD;

	@FXML
	private TextField nombreHost;

	@FXML
	private PasswordField pass;

	@FXML
	private TextField puertobbdd;

	@FXML
	private TextField usuario;

	@FXML
	private RadioButton siOnline;

	private Timeline timeline;

	private static Ventanas nav = new Ventanas();
	private static AccesoBBDDController acceso = new AccesoBBDDController();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		iniciarAnimacionEspera();

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

			detenerAnimacion();
			prontEstadoFichero.setStyle("-fx-background-color: #A0F52D");
			iniciarAnimacionConectado();
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
				detenerAnimacion();
				prontEstadoFichero.setStyle("-fx-background-color: #A0F52D");
				iniciarAnimacionRestaurado();
			} else {
				detenerAnimacion();
				prontEstadoFichero.setStyle("-fx-background-color: #DD370F");
				iniciarAnimacionRestauradoError();
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

		String host = "localhost";

		if (siOnline.isSelected()) {
			etiquetaHost.setText("Nombre del host: ");
			nombreHost.setDisable(false);
			nombreHost.setOpacity(1);
			host = nombreHost.getText();
		}
		if (noOffline.isSelected()) {
			etiquetaHost.setText("Offline");
			nombreHost.setDisable(true);
			nombreHost.setOpacity(0);
			host = "localhost";
		}
		return host;
	}

	private void iniciarAnimacionEspera() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarEsperando = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "Esperando entrada de datos"));
		KeyFrame mostrarPunto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoFichero.textProperty(), "Esperando entrada de datos."));
		KeyFrame mostrarDosPuntos = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoFichero.textProperty(), "Esperando entrada de datos.."));
		KeyFrame mostrarTresPuntos = new KeyFrame(Duration.seconds(1.5),
				new KeyValue(prontEstadoFichero.textProperty(), "Esperando entrada de datos..."));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(2), new KeyValue(prontEstadoFichero.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarEsperando, mostrarPunto, mostrarDosPuntos, mostrarTresPuntos,
				ocultarTexto);

		// Iniciar la animación
		timeline.play();
	}

	private void iniciarAnimacionConectado() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarConectado = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "Fichero guardado"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.6),
				new KeyValue(prontEstadoFichero.textProperty(), ""));
		KeyFrame mostrarConectado2 = new KeyFrame(Duration.seconds(1.1),
				new KeyValue(prontEstadoFichero.textProperty(), ""));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarConectado, ocultarTexto, mostrarConectado2);

		// Iniciar la animación
		timeline.play();
	}

	private void iniciarAnimacionRestaurado() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "Fichero restaurado correctamente"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoFichero.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoFichero.textProperty(), "ERROR"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	private void iniciarAnimacionRestauradoError() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		// Agregar los keyframes para cambiar el texto
		KeyFrame mostrarError = new KeyFrame(Duration.ZERO,
				new KeyValue(prontEstadoFichero.textProperty(), "No se ha podido restaurar correctamente"));
		KeyFrame ocultarTexto = new KeyFrame(Duration.seconds(0.5),
				new KeyValue(prontEstadoFichero.textProperty(), ""));
		KeyFrame mostrarError2 = new KeyFrame(Duration.seconds(1),
				new KeyValue(prontEstadoFichero.textProperty(), "ERROR"));

		// Agregar los keyframes al timeline
		timeline.getKeyFrames().addAll(mostrarError, ocultarTexto, mostrarError2);

		// Iniciar la animación
		timeline.play();
	}

	private void detenerAnimacion() {
		if (timeline != null) {
			timeline.stop();
			timeline = null; // Destruir el objeto timeline
		}
	}

	@FXML
	void volverPrograma(ActionEvent event) {
		nav.verAccesoBBDD(); // Llamada a metodo para abrir la ventana anterior

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Permite salir completamente del programa.
	 *
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {

		if (nav.salirPrograma(event)) { // Llamada a metodo que permite salir completamente del programa
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Cierra el programa a la fuerza correctamente.
	 */
	public void closeWindows() { // Metodo que permite cerrar completamente el programa en caso de cerrar a la //
		// fuerza.
		Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
		myStage.close();
	}

}