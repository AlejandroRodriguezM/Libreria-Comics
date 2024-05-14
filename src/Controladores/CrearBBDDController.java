/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import alarmas.AlarmaList;
import dbmanager.SQLiteManager;
import funciones_auxiliares.Utilidades;
import funciones_auxiliares.Ventanas;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Esta clase sirve para crear una base de datos donde poder tratar nuestra
 * libreria
 *
 * @author Alejandro Rodriguez
 */
public class CrearBBDDController implements Initializable {

    @FXML
    private Button botonCrearBBDD;

    @FXML
    private Button botonLimpiarDatos;

    @FXML
    private Button botonSalir;

    @FXML
    private Button botonVolver;

    @FXML
    private TextField nombreBBDD;

    @FXML
    private Label prontInformativo;

	/**
	 * Controlador para la navegación entre ventanas.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AlarmaList.detenerAnimacion();
		AlarmaList.iniciarAnimacionEsperaCreacion(prontInformativo);
	}

	/**
	 * Funcion que guarda los datos de la nueva base de datos.
	 */
	public String datosBBDD() {

		String dbNombre = nombreBBDD.getText();

		if (!comprobarEntradas(dbNombre)) {
			return dbNombre;
		}
		return "";
	}

	/**
	 * Funcion que permite comprobar si las entradas estan rellenas o no.
	 * 
	 * @return
	 */
	public boolean comprobarEntradas(String dbNombre) {
		String errorMessage = "";
		if (dbNombre.isEmpty()) {
			errorMessage += "El nombre de la base de datos está vacío.\n";
		}

		if (!errorMessage.isEmpty()) {
			prontInformativo.setStyle("-fx-background-color: #DD370F");
			AlarmaList.iniciarAnimacionBaseError(errorMessage, prontInformativo);
			return true;
		}
		return false;
	}

	/**
	 * Metodo que permite llamada a metodos donde se crean la bbdd y las tablas y
	 * procedimientos almacenados
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void crearBBDD(ActionEvent event) {
		String dbName = nombreBBDD.getText();
		if (!datosBBDD().isEmpty() && SQLiteManager.checkDatabaseExists(dbName)) {

			SQLiteManager.createTable(dbName);

			Utilidades.crearCarpeta();

			AlarmaList.iniciarAnimacionBaseCreada(prontInformativo, dbName);

		} else {
			AlarmaList.iniciarAnimacionBaseExiste(prontInformativo, dbName);
		}
	}

	/**
	 * Limpia los datos en pantalla
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		nombreBBDD.setText("");
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

		nav.verOpciones(); // Llamada a metodo para abrir la ventana anterior

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
	 */
	public void closeWindows() {

		Platform.exit();
	}

}
