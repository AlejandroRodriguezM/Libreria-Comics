package Controladores;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
	private TextField userBBDD;

	@FXML
	private TextField puertoBBDD;

	@FXML
	private PasswordField passBBDD;

	@FXML
	private Label prontInformativo;

	NavegacionVentanas nav = new NavegacionVentanas();

	@FXML
	void crearBBDD(ActionEvent event) throws SQLException {

		if(checkDatabase())
		{
			createDataBase();
			createTable();
			prontInformativo.setStyle("-fx-background-color: #A0F52D");
			prontInformativo.setText("Base de datos: " + nombreBBDD.getText() + " creada correctamente.");
		}
		
	}

	public void createDataBase() {
		
		String DB_HOST = "localhost";

		String sentenciaSQL = "CREATE DATABASE " + nombreBBDD.getText() + ";";
		
		String url = "jdbc:mysql://" + DB_HOST + ":" + puertoBBDD.getText() + "?serverTimezone=UTC";

		Statement statement;
		try {
			Connection connection = DriverManager.getConnection(url, userBBDD.getText(), passBBDD.getText());
			
			statement = connection.createStatement();
			statement.executeUpdate(sentenciaSQL);
				
		} catch (SQLException e) {
			
			System.out.println(e);
		}
	}
	
	public boolean checkDatabase()
	{
		boolean exists;
		String DB_HOST = "localhost";
		
		String sentenciaSQL = "SELECT COUNT(*)"
				+ "FROM information_schema.tables "
				+ "WHERE table_schema = '" + nombreBBDD.getText() + "';";
		
		String url = "jdbc:mysql://" + DB_HOST + ":" + puertoBBDD.getText() + "?serverTimezone=UTC";

		Statement statement;
		try {
			
			Connection connection = DriverManager.getConnection(url, userBBDD.getText(), passBBDD.getText());
			
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
		               ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = statement.executeQuery(sentenciaSQL);
			rs.next();
			
			exists = rs.getInt("COUNT(*)") < 1;
			
			if(exists) {
				return true;
			}


		} catch (SQLException e) {
			
			System.out.println(e);
		}
		prontInformativo.setStyle("-fx-background-color: #DD370F");
		prontInformativo.setText("ERROR. Ya existe una base de datos llamada: " + nombreBBDD.getText());
		return false;
	}

	public void createTable() throws SQLException
	{

		String DB_HOST = "localhost";
		String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + puertoBBDD.getText() + "/" + nombreBBDD.getText() + "?serverTimezone=UTC";


		String sentenciaSQL = "CREATE TABLE "
				+ " comicsbbdd ( ID int NOT NULL AUTO_INCREMENT," + "nomComic varchar(150) NOT NULL,"
				+ "numComic varchar(150) NOT NULL," + "nomVariante varchar(150) NOT NULL,"
				+ "Firma varchar(150) NOT NULL," + "nomEditorial varchar(150) NOT NULL,"
				+ "Formato varchar(150) NOT NULL," + "Procedencia varchar(150) NOT NULL,"
				+ "anioPubli varchar(150) NOT NULL," + "nomGuionista varchar(150) NOT NULL,"
				+ "nomDibujante varchar(150) NOT NULL,"
				+ "estado enum('En posesion','Vendido') DEFAULT 'En posesion'" + ",PRIMARY KEY (`ID`)) "
				+ "ENGINE=InnoDB AUTO_INCREMENT=320 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

		Statement statement;
		Connection connection = DriverManager.getConnection(DB_URL, userBBDD.getText(),passBBDD.getText());
		try {

			statement = connection.createStatement();
			statement.executeUpdate(sentenciaSQL);
			
		}
		catch (SQLException e ) {
			e.printStackTrace();
		}

	}

	@FXML
	void limpiarDatos(ActionEvent event) {

		userBBDD.setText("");
		passBBDD.setText("");
		puertoBBDD.setText("");
		nombreBBDD.setText("");
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

		nav.verAccesoBBDD();

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
