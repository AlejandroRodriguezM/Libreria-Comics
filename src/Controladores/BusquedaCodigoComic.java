package Controladores;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;

import com.gluonhq.charm.glisten.control.ProgressIndicator;

import Apis.ApiMarvel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class BusquedaCodigoComic {

    @FXML
    private Button botonBusquedaCodigo;

    @FXML
    private TextField busquedaCodigo;

    @FXML
    private ComboBox<String> busquedaEditorial;

    @FXML
    private ProgressIndicator progresoCarga;

    @FXML
    private ImageView resultadoBusqueda;
    
    private TextArea prontInfo;
    
    private IntroducirDatosController datosIntroducir = null;

    @FXML
	void busquedaPorCodigo(ActionEvent event) throws IOException, JSONException, URISyntaxException {
    	
    	datosIntroducir = new IntroducirDatosController();
    	
		// Crear una tarea que se ejecutará en segundo plano
		Task<Boolean> tarea = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				String valorCodigo = busquedaCodigo.getText();
				String tipoEditorial = busquedaEditorial.getValue();
				String[] comicInfo = null;

				if (!valorCodigo.isEmpty() && !tipoEditorial.isEmpty()) {
					if (tipoEditorial.equalsIgnoreCase("marvel isbn")) {
						comicInfo = ApiMarvel.infoComicIsbn(valorCodigo.trim(), prontInfo);
					} else if (tipoEditorial.equalsIgnoreCase("marvel upc")) {
						comicInfo = ApiMarvel.infoComicUpc(valorCodigo.trim(), prontInfo);

					} else {
						// Hacer algo si el tipo de editorial no es válido
					}

					if (comprobarCodigo(comicInfo)) {
						// Rellenar campos con la información del cómic
//						datosIntroducir.rellenarCamposAni(comicInfo);
						return true;
					}
				}

				return false;
			}
		};

		progresoCarga.setVisible(true);

		// Configurar un manejador de eventos para actualizar la interfaz de usuario
		// cuando la tarea esté completa
		tarea.setOnSucceeded(ev -> {

			Platform.runLater(() -> {
				prontInfo.setOpacity(0);
				prontInfo.setText("");
				progresoCarga.setVisible(false);

			});

		});

		// Configurar el comportamiento cuando la tarea de borrado falla
		tarea.setOnFailed(ev -> {
			Platform.runLater(() -> {
				progresoCarga.setVisible(false);
			});
		});

		progresoCarga.setVisible(true);
		progresoCarga.progressProperty().bind(tarea.progressProperty());
		// Iniciar la tarea en un nuevo hilo
		Thread thread = new Thread(tarea);
		thread.setDaemon(true); // Hacer que el hilo sea demonio para que se cierre al
								// salir de la aplicación

		// Iniciar la tarea
		thread.start();

	}
    
	// Método para mostrar un mensaje de error en prontInfo
	private boolean comprobarCodigo(String[] comicInfo) {
		boolean existe = true;
		if (comicInfo == null || comicInfo.length <= 0) {
			if (comicInfo == null || comicInfo.length <= 0) {
				existe = false;
			}
		}
		return existe;
	}

}