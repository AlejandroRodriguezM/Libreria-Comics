package Controladores;

import java.net.URL;
import java.util.ResourceBundle;

import Funcionamiento.Utilidades;
import Funcionamiento.VersionService;
import alarmas.AlarmaList;
import dbmanager.ConectManager;
import dbmanager.DatabaseManagerDAO;
import dbmanager.ListaComicsDAO;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class OpcionesAvanzadasController implements Initializable {

	@FXML
	private Button botonActualizarSoftware;

	@FXML
	private Button botonDescargarSQL;

	@FXML
	private Button botonDescargarSW;

	@FXML
	private Button botonNormalizarDB;

	@FXML
	private Label labelComprobar;

	@FXML
	private Label labelVersion;

	@FXML
	private Label prontInfo;

	/**
	 * Referencia a la ventana (stage).
	 */
	private Stage stage;

	boolean estaActualizado = true;

	/**
	 * Inicializa la interfaz de usuario y configura el comportamiento de los
	 * elementos al cargar la vista.
	 *
	 * @param location  La ubicación relativa del archivo FXML.
	 * @param resources Los recursos que pueden ser utilizados por el controlador.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		miStageVentana();
		obtenerVersionDesdeOtraClase();
		AlarmaList.iniciarAnimacionEspera(prontInfo);
	}

	@FXML
	void descargarActualizacion(MouseEvent event) {
		Utilidades.descargarYAbrirEjecutableDesdeGitHub(stage);
	}

	@FXML
	void descargarSQL(MouseEvent event) {

		if (!ConectManager.conexionActiva()) {
			return;
		}
		AlarmaList.detenerAnimacionEspera();
		DatabaseManagerDAO.makeSQL(prontInfo);
		Utilidades.borrarArchivosNoEnLista(ListaComicsDAO.listaImagenes);

	}

	@FXML
	void comprobarVersion(MouseEvent event) {
		String versionSW = VersionService.obtenerVersion();
		String versionLocal = VersionService.leerVersionDelArchivo();
		AlarmaList.detenerAnimacionEspera();
		labelComprobar.setStyle("-fx-text-fill: white;");
		botonDescargarSW.setVisible(false);
		String cadena = "";
        if (compareVersions(versionSW, versionLocal) > 0) {
            estaActualizado = false;
            labelComprobar.setStyle("-fx-text-fill: red;");
            cadena = "Versión desactualizada";
        } else {
            estaActualizado = true;
            cadena = "Versión actualizada";
        }
		
		AlarmaList.iniciarAnimacionAvanzado(prontInfo, cadena);

		if (!estaActualizado) {
			botonDescargarSW.setVisible(true);
		}
	}

	@FXML
	void normalizarDataBase(MouseEvent event) {
		AlarmaList.detenerAnimacionEspera();
		DatabaseManagerDAO.comprobarNormalizado("nomGuionista", prontInfo);
		DatabaseManagerDAO.comprobarNormalizado("nomDibujante", prontInfo);
	}

	public void obtenerVersionDesdeOtraClase() {
		VersionService versionService = new VersionService();

		versionService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				String version = VersionService.leerVersionDelArchivo();
				labelVersion.setText(version);
			}
		});

		versionService.start();
	}

	private static int compareVersions(String version1, String version2) {
		String[] parts1 = version1.split("\\.");
		String[] parts2 = version2.split("\\.");

		int minLength = Math.min(parts1.length, parts2.length);

		for (int i = 0; i < minLength; i++) {
			int part1 = Integer.parseInt(parts1[i]);
			int part2 = Integer.parseInt(parts2[i]);

			if (part1 != part2) {
				return Integer.compare(part1, part2);
			}
		}

		return Integer.compare(parts1.length, parts2.length);
	}

	/**
	 * Establece la instancia de la ventana (Stage) asociada a este controlador.
	 *
	 * @param stage La instancia de la ventana (Stage) que se asocia con este
	 *              controlador.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage miStageVentana() {
		Scene scene = botonActualizarSoftware.getScene();

		if (scene != null) {
			// Devolver el Stage de la escena si no es nulo
			return (Stage) scene.getWindow();
		} else {
			// Manejar el caso en el que no se pueda encontrar la escena
			return null;
		}
	}

	/**
	 * Cierra la ventana asociada a este controlador, si está disponible. Si no se
	 * ha establecido una instancia de ventana (Stage), este método no realiza
	 * ninguna acción.
	 */
	public void closeWindow() {
		if (stage != null) {
			stage.close();
		}
	}
}
