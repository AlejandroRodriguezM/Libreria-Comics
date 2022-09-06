package Controladores;

import Funcionamiento.Ventanas;
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

	@FXML
	private Button botonCompra;

	private static Ventanas nav = new Ventanas();

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

	/**
	 * Se llama a funcion que permite abrir 2 direcciones web junto al navegador
	 * predeterminado
	 *
	 * @param event
	 */
	@FXML
	void comprarComic(ActionEvent event) {
		verPagina();
	}

	/**
	 * Funcion que permite llamar al navegador predeterminado del sistema y abrir 2
	 * paginas web.
	 */
	public void verPagina() {
		String url1 = "https://www.radarcomics.com/es/";
		String url2 = "https://www.panini.es/shp_esp_es/comics.html";

		if (Utilidades.isWindows()) {
			Utilidades.accesoWebWindows(url1); // Llamada a funcion
			Utilidades.accesoWebWindows(url2); // Llamada a funcion
		} else {
			if (Utilidades.isUnix()) {
				Utilidades.accesoWebLinux(url1); // Llamada a funcion
				Utilidades.accesoWebLinux(url2); // Llamada a funcion
			} else {
				Utilidades.accesoWebMac(url1);
				Utilidades.accesoWebMac(url2);
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