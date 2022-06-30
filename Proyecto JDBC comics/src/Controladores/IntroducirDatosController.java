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
import javafx.scene.control.TextArea;
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
    private TextArea prontDatos;

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

	private NavegacionVentanas nav = new NavegacionVentanas();

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

		IntroducirDatos();
	}

	/**
	 *
	 * @return
	 */
	public String[] camposComics() {
		String campos[] = new String[10];

		campos[0] = nombreComic.getText();

		campos[1] = numeroComic.getText();

		campos[2] = nombreVariante.getText();

		campos[3] = nombreFirma.getText();

		campos[4] = nombreEditorial.getText();

		campos[5] = nombreFormato.getText();

		campos[6] = nombreProcedencia.getText();

		campos[7] = anioPublicacion.getText();

		campos[8] = nombreGuionista.getText();

		campos[9] = nombreDibujante.getText();

		return campos;
	}

	/**
	 *
	 */
	public void IntroducirDatos()
	{
		DBManager.loadDriver();

		String sentenciaSQL = "insert into comicsbbdd(nomComic,numComic,nomVariante,firma,nomEditorial,formato,procedencia,anioPubli,nomGuionista,nomDibujante) values (?,?,?,?,?,?,?,?,?,?)";

		String datos[] = camposComics();

		try {
			PreparedStatement statement = conn.prepareStatement(sentenciaSQL);
			statement.setString(1, datos[0]);
			statement.setString(2, datos[1]);
			statement.setString(3, datos[2]);

			if (datos[3].length() != 0) {
				statement.setString(4, datos[3]);
			} else {
				statement.setString(4, "No firmado");
			}
			statement.setString(5, datos[4]);
			statement.setString(6, datos[5]);
			statement.setString(7, datos[6]);
			statement.setString(8, datos[7]);
			statement.setString(9, datos[8]);
			statement.setString(10, datos[9]);

			if (statement.executeUpdate() == 1) {
				prontDatos.setText("Comic añadido correctamente!" + "\nNombre del comic: " + datos[0]
						+ "\nNumero: " + datos[1] + "\nPortada variante: " + datos[2] + "\nFirma: " + datos[3]
						+ "\nEditorial: " + datos[4] + "\nFormato: " + datos[5] + "\nProcedencia: "
						+ datos[6] + "\nFecha de publicacion: " + datos[7] + "\nGuionista: " + datos[8]
						+ "\nDibujante: " + datos[9]);
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