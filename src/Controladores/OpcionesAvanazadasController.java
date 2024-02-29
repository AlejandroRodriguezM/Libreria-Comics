package Controladores;

import dbmanager.ConectManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class OpcionesAvanazadasController {

	@FXML
	private Button botonActualizarSoftware;

	@FXML
	private Button botonDescargarExcel;

	@FXML
	private Button botonDescargarPortadas;

	@FXML
	private Button botonDescargarSQL;

	@FXML
	private Button botonNormalizarDB;

	@FXML
	private Label labelVersion;

	/**
	 * Referencia a la ventana (stage).
	 */
	private Stage stage;
	
    private static final String GITHUB_URL = "https://raw.githubusercontent.com/AlejandroRodriguezM/Libreria-Comics/main/version.txt";


	@FXML
	void descargarExcel(MouseEvent event) {

	}

	@FXML
	void descargarSQL(MouseEvent event) {

	}

	@FXML
	void comprobarVersion(MouseEvent event) {

	}

	@FXML
	void descargarPortadas(MouseEvent event) {

	}

	@FXML
	void normalizarDataBase(MouseEvent event) {

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

	public Scene miStageVentana() {
		Node rootNode = botonActualizarSoftware;
		while (rootNode.getParent() != null) {
			rootNode = rootNode.getParent();
		}

		if (rootNode instanceof Parent) {
			Scene scene = ((Parent) rootNode).getScene();
			ConectManager.activeScenes.add(scene);
			return scene;
		} else {
			// Manejar el caso en el que no se pueda encontrar un nodo raíz adecuado
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
