package Controladores;

import Funcionamiento.NavegacionVentanas;
import Funcionamiento.Utilidades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SobreMiController {

    @FXML
    private Label TextoInfo;
	
    @FXML
    private Button botonSalir;

    @FXML
    private Button botonTwitch;

    @FXML
    private Button botonTwitter;

    @FXML
    private Button botonVolver;

    @FXML
    private Button botonYoutube;

    @FXML
    private Button numeroVersion;
    
	private NavegacionVentanas nav = new NavegacionVentanas();

    @FXML
    void accesoGitHub(ActionEvent event) {

		String url = "https://github.com/MisterioRojo/Proyecto-gui-bbdd/tree/V.F";
		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url);

			}
		}
    	
    }

    @FXML
    void accesoTwitter(ActionEvent event) {

		String url = "https://twitter.com/SilverAlox";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url);
			}
		}	
    }
    
    @FXML
    void accesoYoutube(ActionEvent event) {

		String url = "https://www.youtube.com/playlist?list=PL7MV626sbFp6EY0vP8gEEgrVCryitFXCM";
		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url);

			}
		}
    }
    
    @FXML
    void accesoTwitch(ActionEvent event) {

		String url = "https://www.twitch.tv/misteriorojo";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url);
			}
		}	
    }

	/////////////////////////////////
	//// METODO LLAMADA A VENTANA//
	/////////////////////////////////

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void volverMenu(ActionEvent event) {

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

		if (nav.salirPrograma(event)) {
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {

		nav.verAccesoBBDD(); // Llamada a metodo para abrir la ventana anterior

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}

}