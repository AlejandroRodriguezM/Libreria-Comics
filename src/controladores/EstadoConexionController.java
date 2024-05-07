package controladores;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gluonhq.charm.glisten.control.ProgressIndicator;

import alarmas.AlarmaList;
import controladores.funcionesInterfaz.FuncionesManejoFront;
import ficherosFunciones.FuncionesFicheros;
import funciones_auxiliares.Utilidades;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EstadoConexionController implements Initializable {

	@FXML
	private Label alarmaConexionInternet;

	@FXML
	private Label alarmaConexionSql;

	@FXML
	private Button botonReconectarInternet;

	@FXML
	private Button botonReconectarSql;

	@FXML
	private Label labelConexionInternet;

	@FXML
	private Label labelConexionSql;

	@FXML
	private ProgressIndicator progresoCarga;

	private static AlarmaList alarmaList = new AlarmaList();

	/**
	 * Referencia a la ventana (stage).
	 */
	private Stage stage;

	private ExecutorService executorService;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.setAlarmaInternetLabel(labelConexionInternet);
		alarmaList.setAlarmaConexionPrincipal(labelConexionSql);

		alarmaList.iniciarThreadChecker();
		Platform.runLater(() -> {
			progresoCarga.getScene().getWindow().setOnHidden(e -> {
				if (executorService != null && !executorService.isShutdown()) {
					executorService.shutdownNow();
				}
			});
			FuncionesManejoFront.getStageVentanas().add(estadoStage());
		});
	}

	@FXML
	void reActivarConexion(ActionEvent event) {
		Map<String, String> datosConfiguracion = FuncionesFicheros.devolverDatosConfig();
		String port = datosConfiguracion.get("Puerto");
		String host = datosConfiguracion.get("Hosting");

		if (!Utilidades.isMySQLServiceRunning(host, port)) {
			// Crear una tarea (Task) para iniciar XAMPP
			Task<Boolean> iniciarXAMPPTask = new Task<Boolean>() {
				@Override
				protected Boolean call() throws Exception {
					// Realizar las operaciones de inicio de XAMPP aquí
					return Utilidades.iniciarXAMPP();
				}
			};

			// Configurar eventos onRunning y onSucceeded
			iniciarXAMPPTask.setOnRunning(e -> {
				AlarmaList.iniciarAnimacionCarga(progresoCarga);
				AlarmaList.iniciarAnimacionDesconectado(labelConexionSql);
			});

			iniciarXAMPPTask.setOnSucceeded(e -> {
				boolean exito = iniciarXAMPPTask.getValue();

				Platform.runLater(() -> {
					if (exito) {
						AlarmaList.iniciarAnimacionConectado(labelConexionSql);
						AlarmaList.detenerAnimacionCarga(progresoCarga);
					} else {
						AlarmaList.detenerAnimacionCarga(progresoCarga);
					}
				});

				// Cerrar el hilo después de completar la tarea
				executorService.shutdown();
			});

			// Iniciar la tarea en un nuevo hilo utilizando un ExecutorService
			executorService = Executors.newSingleThreadExecutor();
			executorService.submit(iniciarXAMPPTask);
		}
	}

	public Stage estadoStage() {

		return (Stage) progresoCarga.getScene().getWindow();
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

	/**
	 * Cierra la ventana actual si está abierta.
	 */
	public void closeWindow() {
		if (stage != null) {

			if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
				FuncionesManejoFront.getStageVentanas().remove(estadoStage());
			}

			stage.close();
		}
	}

	public void stop() {
		alarmaList.detenerThreadChecker();
	}

}
