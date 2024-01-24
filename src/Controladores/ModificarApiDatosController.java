package Controladores;

import java.net.URL;
import java.util.ResourceBundle;

import com.gluonhq.charm.glisten.control.ProgressIndicator;

import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import alarmas.AlarmaList;
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
		alarmaList.iniciarThreadChecker(true);

		alarmaList.iniciarAnimacionEspera(printInfo);
		
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
		if (Utilidades.verificarEstructuraClavesMarvel()) {

			String[] partes = Utilidades.clavesApiMarvel();

			String passPublica = partes[0];
			String passPrivada = partes[1];

			textFieldPublica.setText(passPublica);
			textFieldPrivada.setText(passPrivada);

		} else {
			// La cadena original no tiene el formato esperado
			System.out.println("La cadena no tiene el formato esperado");
			Utilidades.guardarDatosClavesMarvel();
		}
	}

	public void claveVineComic() {
		// Verificar si hay al menos dos partes
		if (Utilidades.verificarEstructuraApiComicVine()) {
			String claveVine = Utilidades.cargarApiComicVine();

			textFieldPrivada.setText(claveVine);

		} else {
			// La cadena original no tiene el formato esperado
			System.out.println("La cadena no tiene el formato esperado");
			Utilidades.guardarApiComicVine();
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

	    if ((esMarvel && Utilidades.verificarEstructuraClavesMarvel()) || (esVine && Utilidades.verificarEstructuraApiComicVine())) {
	        if (esMarvel) {
	            String clavePublica = textFieldPublica.getText();
	            Utilidades.reescribirClavesMarvel(clavePublica, clavePrivada);
	        } else if (esVine) {
	            Utilidades.reescribirClaveApiComicVine(clavePrivada);
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
		alarmaList.iniciarAnimacionEspera(printInfo);
	}

	@FXML
	void restablecerDatos(ActionEvent event) {

		if (nav.alertaRestablecerApi()) {
			if (TIPO_ACCION.equalsIgnoreCase("Marvel")) {
				Utilidades.guardarDatosClavesMarvel();
			} else {
				Utilidades.guardarApiComicVine();
			}
			AlarmaList.iniciarAnimacionReEstablecido(printInfo);
		}
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindow() {

		Stage myStage = (Stage) guardarDatosApi.getScene().getWindow();
		myStage.close();

	}

}
