package Controladores;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xls
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Añadir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria
 *  
 *  Esta clase permite añadir comics en la base de datos.
 *  
 *  Version 2.3
 *  
 *  Por Alejandro Rodriguez
 *  
 *  Twitter: @silverAlox
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Funcionamiento.DBManager;
import Funcionamiento.NavegacionVentanas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class IntroducirDatosController {

	@FXML
	private Button botonAniadirBBDD;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Label prontDatos;

	@FXML
	private TextField anioPublicacion;

	@FXML
	private TextField nombreComic;

	@FXML
	private TextField nombreDibujante;

	@FXML
	private TextField nombreEditorial;

	@FXML
	private TextField nombreFirma;

	@FXML
	private TextField nombreFormato;

	@FXML
	private TextField nombreGuionista;

	@FXML
	private TextField nombreProcedencia;

	@FXML
	private TextField nombreVariante;

	@FXML
	private TextField numeroComic;

	private static Connection conn = DBManager.conexion();

	NavegacionVentanas nav = new NavegacionVentanas();

	/**
	 * 
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		nombreComic.setText("");
		numeroComic.setText("");
		nombreVariante.setText("");
		nombreFirma.setText("");
		nombreEditorial.setText("");
		nombreFormato.setText("");
		anioPublicacion.setText("");
		anioPublicacion.setText("");
		nombreDibujante.setText("");
		nombreGuionista.setText("");
	}

	/**
	 * Añade datos a la base de datos segun los parametros introducidos en los
	 * textField
	 * 
	 * @param event
	 */
	@FXML
	public void agregarDatos(ActionEvent event) {

		String nombreCom, numeroCom, varianteCom, firmaCom, editorialCom, formatoCom, procedenciaCom, fechaCom,
				guionistaCom, dibujanteCom;

		DBManager.loadDriver();

		String sentenciaSQL = "insert into comicsbbdd(nomComic,numComic,nomVariante,firma,nomEditorial,formato,procedencia,anioPubli,nomGuionista,nomDibujante) values (?,?,?,?,?,?,?,?,?,?)";

		nombreCom = nombreComic.getText();

		numeroCom = numeroComic.getText();

		varianteCom = nombreVariante.getText();

		firmaCom = nombreFirma.getText();

		editorialCom = nombreEditorial.getText();

		formatoCom = nombreFormato.getText();

		procedenciaCom = nombreProcedencia.getText();

		fechaCom = anioPublicacion.getText();

		guionistaCom = nombreGuionista.getText();

		dibujanteCom = nombreDibujante.getText();

		try {
			PreparedStatement statement = conn.prepareStatement(sentenciaSQL);
			statement.setString(1, nombreCom);
			statement.setString(2, numeroCom);

			if (varianteCom.length() != 0) {
				statement.setString(3, varianteCom);
			} else {
				statement.setString(3, "Normal");
			}
			if (firmaCom.length() != 0) {
				statement.setString(4, firmaCom);
			} else {
				statement.setString(4, "No firmado");
			}
			statement.setString(5, editorialCom);
			statement.setString(6, formatoCom);
			statement.setString(7, procedenciaCom);
			statement.setString(8, fechaCom);
			statement.setString(9, guionistaCom);
			statement.setString(10, dibujanteCom);

			if (statement.executeUpdate() == 1) {
				prontDatos.setText("Comic añadido correctamente!" + "\nNombre del comic: " + nombreCom + dibujanteCom
						+ "\nNumero: " + numeroCom + "\nPortada variante: " + varianteCom + "\nFirma: " + firmaCom
						+ "\nEditorial: " + editorialCom + "\nFormato: " + formatoCom + "\nProcedencia: "
						+ procedenciaCom + "\nFecha de publicacion: " + fechaCom + "\nGuionista: " + guionistaCom
						+ "\nDibujante: " + dibujanteCom);
				statement.close();
			} else {
				prontDatos.setText("Se ha encontrado un error. No ha sido posible añadir el comic a la base de datos.");
			}
		} catch (SQLException ex) {
			System.err.println("Error al insertar un comic" + ex);
		}
	}

	/**
	 * Permite volver al menu de conexion a la base de datos.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void volverMenu(ActionEvent event) throws IOException {

		nav.verMenuPrincipal();

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
	 * Al cerrar la ventana, se cargara la ventana de verBBDD
	 * 
	 * @throws IOException
	 */
	public void closeWindows() throws IOException {

		nav.verMenuPrincipal();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}
}