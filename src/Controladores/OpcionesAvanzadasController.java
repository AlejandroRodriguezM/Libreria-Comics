package Controladores;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import Controladores.managment.AccionFuncionesComunes;
import Controladores.managment.AccionModificar;
import Controladores.managment.AccionReferencias;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import Funcionamiento.VersionService;
import alarmas.AlarmaList;
import dbmanager.ConectManager;
import dbmanager.DatabaseManagerDAO;
import dbmanager.ListaComicsDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class OpcionesAvanzadasController implements Initializable {

	@FXML
	private Button botonActualizarDatos;

	@FXML
	private Button botonActualizarPortadas;

	@FXML
	private Button botonActualizarSoftware;

	@FXML
	private Button botonActualizarTodo;

	@FXML
	private Button botonDescargarPdf;

	@FXML
	private Button botonDescargarSQL;

	@FXML
	private Button botonNormalizarDB;

	@FXML
	private Button botonCancelarSubida;

	@FXML
	private CheckBox checkFirmas;

	@FXML
	private ComboBox<String> comboPreviews;

	@FXML
	private Label labelComprobar;

	@FXML
	private Label labelVersion;

	@FXML
	private Label prontInfo;

	@FXML
	private Label prontInfoEspecial;

	@FXML
	private Label prontInfoPreviews;

	public static ObservableList<String> urlActualizados = FXCollections.observableArrayList();

	/**
	 * Referencia a la ventana (stage).
	 */
	private Stage stage;

	public static boolean estaActualizado = true;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	public static AtomicBoolean actualizarFima = new AtomicBoolean(false);

	public AccionReferencias guardarReferencia() {
		AccionReferencias referenciaVentana = new AccionReferencias();

		referenciaVentana.setBotonCancelarSubida(botonCancelarSubida);
		referenciaVentana.setBotonActualizarDatos(botonActualizarDatos);
		referenciaVentana.setBotonActualizarPortadas(botonActualizarPortadas);
		referenciaVentana.setBotonActualizarSoftware(botonActualizarSoftware);
		referenciaVentana.setBotonActualizarTodo(botonActualizarTodo);
		referenciaVentana.setBotonDescargarPdf(botonDescargarPdf);
		referenciaVentana.setBotonDescargarSQL(botonDescargarSQL);
		referenciaVentana.setBotonNormalizarDB(botonNormalizarDB);
		referenciaVentana.setCheckFirmas(checkFirmas);
		referenciaVentana.setComboPreviews(comboPreviews);
		referenciaVentana.setLabelComprobar(labelComprobar);
		referenciaVentana.setLabelVersion(labelVersion);
		referenciaVentana.setProntInfoLabel(prontInfo);
		referenciaVentana.setProntInfoEspecial(prontInfoEspecial);
		referenciaVentana.setProntInfoPreviews(prontInfoPreviews);

		return referenciaVentana;
	}

	/**
	 * Inicializa la interfaz de usuario y configura el comportamiento de los
	 * elementos al cargar la vista.
	 *
	 * @param location  La ubicación relativa del archivo FXML.
	 * @param resources Los recursos que pueden ser utilizados por el controlador.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(() -> {
			AccionModificar.referenciaVentana = guardarReferencia();
			AccionFuncionesComunes.referenciaVentana = guardarReferencia();
			rellenarComboboxPreviews();
		});
		miStageVentana();
		obtenerVersionDesdeOtraClase();
		AlarmaList.iniciarAnimacionEspera(prontInfo);
		AlarmaList.iniciarAnimacionEspera(prontInfoEspecial);
		AlarmaList.iniciarAnimacionEspera(prontInfoPreviews);

		checkFirmas.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				if (nav.alertaFirmaActivada()) {
					actualizarFima.set(true);
				} else {
					// Si la alerta no está activada, desmarcar el CheckBox
					checkFirmas.setSelected(false);
				}
			} else {
				// Cuando el CheckBox se desmarca, actualiza actualizarFima a false
				actualizarFima.set(false);
			}
		});

	}

	@FXML
	void descargarActualizacion(ActionEvent event) {
		Utilidades.descargarYAbrirEjecutableDesdeGitHub(stage);
	}

	@FXML
	void descargarSQL(ActionEvent event) {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		AlarmaList.detenerAnimacionEspera();
		DatabaseManagerDAO.makeSQL(prontInfo);
		Utilidades.borrarArchivosNoEnLista(ListaComicsDAO.listaImagenes);

	}

	@FXML
	void comprobarVersion(ActionEvent event) {
		String versionSW = VersionService.obtenerVersion();
		String versionLocal = VersionService.leerVersionDelArchivo();
		AlarmaList.detenerAnimacionEspera();
		labelComprobar.setStyle("-fx-text-fill: white;");
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
			botonActualizarSoftware.setVisible(true);
		}
	}

	@FXML
	void normalizarDataBase(ActionEvent event) {
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

	@FXML
	void descargarPreview(ActionEvent evento) {
		if (Utilidades.isInternetAvailable()) {
			Platform.runLater(() -> {

				int indiceSeleccionado = comboPreviews.getSelectionModel().getSelectedIndex();
				String urlSeleccionada = OpcionesAvanzadasController.urlActualizados.get(indiceSeleccionado);

				if (urlSeleccionada.equals("No hay previews")) {
					String cadenaError = "No existe PDF que descargar";
					AlarmaList.iniciarAnimacionAvanzado(prontInfoPreviews, cadenaError);
					return;
				}

				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
				File file = fileChooser.showSaveDialog(new Stage());

				if (file != null) {
					Utilidades.descargarPDFAsync(file, comboPreviews);
					String cadenaAfirmativo = "PDF descargado exitosamente.";
					AlarmaList.iniciarAnimacionAvanzado(prontInfoPreviews, cadenaAfirmativo);
				} else {
					String cadenaCancelado = "Has cancelado la descarga del PDF.";
					AlarmaList.iniciarAnimacionAvanzado(prontInfoPreviews, cadenaCancelado);
				}
			});
		} else {
			String cadenaCancelado = "No se puede descargar, no hay internet";
			AlarmaList.iniciarAnimacionAvanzado(prontInfoPreviews, cadenaCancelado);
		}
	}

	private void rellenarComboboxPreviews() {
		ObservableList<String> meses = FXCollections.observableArrayList();

		if (Utilidades.isInternetAvailable()) {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					List<Map.Entry<String, String>> previews = MenuPrincipalController.urlPreviews.get();
					ObservableList<String> mesesActualizados = FXCollections.observableArrayList();
					for (Map.Entry<String, String> entry : previews) {
						urlActualizados.add(entry.getValue()); // Agregar la URL a la lista global
						mesesActualizados.add(entry.getKey());
					}

					Platform.runLater(() -> {
						comboPreviews.setItems(mesesActualizados);
						comboPreviews.getSelectionModel().selectFirst();
					});

					return null;
				}
			};

			new Thread(task).start();
		} else {
			meses.add("No hay previews");
			comboPreviews.setItems(meses);
		}
	}

	@FXML
	void actualizarCompletoComic(ActionEvent event) {

		AccionModificar.referenciaVentana = guardarReferencia();
		AccionFuncionesComunes.referenciaVentana = guardarReferencia();
		AccionModificar.actualizarDatabase("modificar", actualizarFima.get());

	}

	@FXML
	void actualizarDatosComic(ActionEvent event) {
		AccionModificar.referenciaVentana = guardarReferencia();
		AccionFuncionesComunes.referenciaVentana = guardarReferencia();
		AccionModificar.actualizarDatabase("actualizar datos", actualizarFima.get());
	}

	@FXML
	void actualizarPortadaComic(ActionEvent event) {
		AccionModificar.referenciaVentana = guardarReferencia();
		AccionFuncionesComunes.referenciaVentana = guardarReferencia();
		AccionModificar.actualizarDatabase("actualizar portadas", actualizarFima.get());
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
