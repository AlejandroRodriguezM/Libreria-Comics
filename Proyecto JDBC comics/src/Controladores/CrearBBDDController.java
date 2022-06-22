package Controladores;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import Funcionamiento.NavegacionVentanas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CrearBBDDController {

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
	private TextField nombreTabla;

	@FXML
	private TextField userBBDD;

	@FXML
	private TextField puertoBBDD;

	@FXML
	private PasswordField passBBDD;

	@FXML
	private Label prontInformativo;

	NavegacionVentanas nav = new NavegacionVentanas();

	@FXML
	void crearBBDD(ActionEvent event) throws SQLException, ClassNotFoundException {

		createDataBase();
		createTable();
	}

	public void createDataBase() throws SQLException
	{
		String sentenciaSQL = "CREATE DATABASE " + nombreBBDD.getText() + ";";

		String url = "jdbc:mysql://localhost:" + puertoBBDD.getText() + "/mysql?zeroDateTimeBehavior=convertToNull";
		Connection connection = DriverManager.getConnection(url,userBBDD.getText(),passBBDD.getText());

		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sentenciaSQL);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createTable() throws SQLException, ClassNotFoundException
	{
		String url = "jdbc:mysql://localhost:" + puertoBBDD.getText() + "/mysql?zeroDateTimeBehavior=convertToNull";


		String sentenciaSQL = "CREATE TABLE " + nombreTabla.getText()
		+ "( `ID` int NOT NULL AUTO_INCREMENT," + "`nomComic` varchar(150) NOT NULL,"
		+ "`numComic` varchar(150) NOT NULL," + "`nomVariante` varchar(150) NOT NULL,"
		+ "`Firma` varchar(150) NOT NULL," + "`nomEditorial` varchar(150) NOT NULL,"
		+ "`Formato` varchar(150) NOT NULL," + "`Procedencia` varchar(150) NOT NULL,"
		+ "`anioPubli` varchar(150) NOT NULL," + "`nomGuionista` varchar(150) NOT NULL,"
		+ "`nomDibujante` varchar(150) NOT NULL,"
		+ "`estado` enum('En posesion','Vendido') DEFAULT 'En posesion'" + ",PRIMARY KEY (`ID`)) "
		+ "ENGINE=InnoDB AUTO_INCREMENT=320 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

		Statement statement;
		Connection connection = DriverManager.getConnection(url + nombreBBDD.getText(), userBBDD.getText(),passBBDD.getText());
		try {

			statement = connection.createStatement();
			//This line has the issue
			statement.executeUpdate(sentenciaSQL);
			System.out.println("Table Created");
		}
		catch (SQLException e ) {
			System.out.println("An error has occured on Table Creation");
			e.printStackTrace();
		}

	}

	@FXML
	void limpiarDatos(ActionEvent event) {

		nombreBBDD.setText("");
		nombreTabla.setText("");

	}

	/////////////////////////////////
	//// METODO LLAMADA A VENTANA//
	/////////////////////////////////

	/**
	 * Permite volver al menu de conexion a la base de datos.
	 *
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void volverMenu(ActionEvent event) throws IOException {

		nav.verCrearBBDD();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();
	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	/**
	 * Permite salir completamente del programa.
	 * 
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) { // Metodo que poermite salir del programa

		if (nav.salirPrograma(event)) { // Llamada a metodo que permite salir del programa
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close(); // Cierra la ventana actual
		}
	}

	/**
	 * Al cerrar la ventana, se cargara la ventana de verBBDD
	 * 
	 * @throws IOException
	 */
	public void closeWindows() throws IOException { // Metodo que permite cerrar el programa a la fuerza.

		nav.verMenuPrincipal(); // Llamada a metodo que carga y muestra la ventana de MenuPrincipal

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close(); // Cierra la ventana actual

	}

}
