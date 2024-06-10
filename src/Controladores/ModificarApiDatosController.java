package Controladores;

import java.net.URL;
import java.util.ResourceBundle;

import com.gluonhq.charm.glisten.control.ProgressIndicator;

import alarmas.AlarmaList;
import apisFunciones.FuncionesApis;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.FuncionesManejoFront;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModificarApiDatosController implements Initializable {

	@FXML
	private Label alarmaConexionInternet;

	@FXML
	private Label alarmaConexionSql;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button guardarDatosApi;

	@FXML
	private Label printInfo;

	@FXML
	private ProgressIndicator progresoCarga;

	@FXML
	private Button restablecerDatos;

	@FXML
	private TextField textFieldPrivada;

	@FXML
	private TextField textFieldPublica;

	private static AlarmaList alarmaList = new AlarmaList();

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Tipo de acción a realizar en la interfaz.
	 */
	private static String TIPO_ACCION;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
		alarmaList.iniciarThreadChecker();

		AlarmaList.iniciarAnimacionEspera(printInfo);
		Platform.runLater(() -> {
			FuncionesManejoFront.getStageVentanas().add(estadoStage());
		});
		if (TIPO_ACCION.equalsIgnoreCase("Marvel")) {
			textFieldPublica.setDisable(false);
			textFieldPublica.setVisible(true);
			claveMarvel();
		} else {
			claveVineComic();
		}
	}

	public void claveMarvel() {

		// Verificar si hay al menos dos partes
		if (FuncionesApis.verificarEstructuraClavesMarvel()) {

			String[] partes = FuncionesApis.clavesApiMarvel();

			String passPrivada = partes[0];
			String passPublica = partes[1];

			textFieldPublica.setText(passPublica);
			textFieldPrivada.setText(passPrivada);

		} else {
			// La cadena original no tiene el formato esperado
			System.out.println("La cadena no tiene el formato esperado");
			FuncionesApis.guardarDatosClavesMarvel();
		}
	}

	public void claveVineComic() {
		// Verificar si hay al menos dos partes
		if (FuncionesApis.verificarEstructuraApiComicVine()) {
			String claveVine = FuncionesApis.cargarApiComicVine();

			textFieldPrivada.setText(claveVine);

		} else {
			// La cadena original no tiene el formato esperado
			System.out.println("La cadena no tiene el formato esperado");
			FuncionesApis.guardarApiComicVine();
		}
	}

	/**
	 * Establece el tipo de acción que se realizará en la ventana.
	 *
	 * @param tipoAccion El tipo de acción a realizar (por ejemplo, "aniadir",
	 *                   "modificar", "eliminar", "puntuar").
	 */
	public static void tipoAccion(String tipoAccion) {
		TIPO_ACCION = tipoAccion;
	}

	@FXML
	void guardarDatos(ActionEvent event) {
		String clavePrivada = textFieldPrivada.getText();

		boolean esMarvel = TIPO_ACCION.equalsIgnoreCase("Marvel");
		boolean esVine = TIPO_ACCION.equalsIgnoreCase("Vine");

		if ((esMarvel && FuncionesApis.verificarEstructuraClavesMarvel())
				|| (esVine && FuncionesApis.verificarEstructuraApiComicVine())) {
			if (esMarvel) {
				String clavePublica = textFieldPublica.getText();
				FuncionesApis.reescribirClavesMarvel(clavePublica, clavePrivada);
			} else if (esVine) {
				FuncionesApis.reescribirClaveApiComicVine(clavePrivada);
			}

			AlarmaList.iniciarAnimacionGuardado(printInfo);
		} else {
			AlarmaList.iniciarAnimacionError(printInfo);
		}
	}

	@FXML
	void limpiarDatos(ActionEvent event) {

		if (TIPO_ACCION.equalsIgnoreCase("Marvel")) {
			textFieldPublica.setText("");
		}
		textFieldPrivada.setText("");
		AlarmaList.detenerAnimacion();
		AlarmaList.iniciarAnimacionEspera(printInfo);
	}

	@FXML
	void restablecerDatos(ActionEvent event) {

		if (nav.alertaRestablecerApi()) {
			if (TIPO_ACCION.equalsIgnoreCase("Marvel")) {
				FuncionesApis.guardarDatosClavesMarvel();
			} else {
				FuncionesApis.guardarApiComicVine();
			}
			AlarmaList.iniciarAnimacionReEstablecido(printInfo);
		}
	}

	public Stage estadoStage() {

		return (Stage) guardarDatosApi.getScene().getWindow();
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindow() {

		if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
			FuncionesManejoFront.getStageVentanas().remove(estadoStage());
		}

		Stage myStage = (Stage) guardarDatosApi.getScene().getWindow();
		myStage.close();

	}
	
	public void stop() {
		alarmaList.detenerThreadChecker();
	}

}
