package Funcionamiento;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

	private static final String DB_MSQ_CONN_OK = "CONEXION CORRECTA";
	private static final String DB_MSQ_CONN_NO = "ERROR EN LA CONEXION";

	// Conexión a la base de datos
	private static Connection conn;

	@FXML
	private TextField anioPublicacion;

	@FXML
	private Button botonAniadirBBDD;

	@FXML
	private Button botonConexion;


	@FXML
	private Label labelConexion;

	@FXML
	private Label labelResultado;

	@FXML
	private TextField nombreComic;

	@FXML
	private TextField nombreDibujante;

	@FXML
	private TextField nombreEditorial;

	@FXML
	private TextField nombreBBDD;

	@FXML
	private TextField nombreGuinista;

	@FXML
	private TextField nombreUsuario;

	@FXML
	private TextField nombreVariante;

	@FXML
	private TextField contraBBDD;

	@FXML
	private TextField numeroComic;

	@FXML
	private Label labelTestConexion;

	@FXML
	private TextField nombreFormato;

	@FXML
	public void BotonAniadir(ActionEvent event) {
		String nom_comic,nom_dibujante,nom_editorial,nom_guionista,nom_variante,nom_Formato,num_comic;
		String sentenciaSQL = "insert into comics(nomComic,nomDibujante,nomEditorial,nomGuionista,nomVariante,nomFormato,numComic) values (?,?,?,?,?,?,?)";

		nom_comic = nombreComic.getText();
		nom_dibujante = nombreDibujante.getText();
		nom_editorial = nombreEditorial.getText();
		nom_guionista = nombreGuinista.getText();
		nom_variante = nombreVariante.getText();
		nom_Formato = nombreFormato.getText();
		num_comic = numeroComic.getText();


		try {
			PreparedStatement statement = conn.prepareStatement(sentenciaSQL);
			statement.setString(1, nom_comic);
			statement.setString(2, nom_dibujante);
			statement.setString(3, nom_editorial);
			statement.setString(4, nom_guionista);
			statement.setString(5, nom_variante);
			statement.setString(6, nom_Formato);
			statement.setString(7, num_comic);

			statement.executeUpdate();
			statement.close();
			if(statement.executeUpdate() == 1)
			{
				labelConexion.setStyle("-fx-background-color: #A0F52D");
				labelResultado.setText("Comic añadido correctamente!" + "\nNombre del comic: " + nom_comic 
						+ "\nNombre del dibujante: " + nom_dibujante + "\nEditorial: " + nom_editorial 
						+ "\nGuinista: " + "\nVariante: " + nom_variante + "\nNumero del comic: " + num_comic);
			}
			else
			{
				labelConexion.setStyle("-fx-background-color: #DD370F");
				labelResultado.setText("Se ha encontrado un error. No ha sido posible añadir el comic a la base de datos.");
			}
		} catch (SQLException ex) {
			System.err.println("Error al insertar un Cliente ");
		}
	}

	@FXML
	public void BotonConectar(ActionEvent event) {

		loadDriver();
		conn = conexion();
		if(isConnected())
		{   
			labelTestConexion.setStyle("-fx-background-color: #A0F52D");
			labelTestConexion.setText("Conectado");
			labelConexion.setStyle("-fx-background-color: #A0F52D");
			labelConexion.setText("Cargando Driver... " + "OK! \nConectando a la base de datos... \n" + DB_MSQ_CONN_OK);
		}
		else
		{
			labelTestConexion.setStyle("-fx-background-color: #DD370F");
			labelTestConexion.setText("No conectado");
			labelConexion.setStyle("-fx-background-color: #DD370F");
			labelConexion.setText("Cargando Driver... " + "OK! \nConectando a la base de datos... \n" +DB_MSQ_CONN_NO);
		}
	}

	/**
	 * Conecta el proyecto con el driver JBDC
	 * 
	 * @return
	 */
	public boolean loadDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return true;
		} catch (ClassNotFoundException ex) {
			return false;
		}
	}

	/**
	 * Comprueba la conexión y muestra su estado por pantalla
	 *
	 * @return true si la conexión existe y es válida, false en caso contrario
	 */
	public boolean isConnected() {
		// Comprobamos estado de la conexión
		try {

			if (conn != null && conn.isValid(0)) {
				return true;
			}
		} catch (SQLException ex) {
			return false;
		}
		return false;
	}


	public Connection conexion() {

		// Configuración de la conexión a la base de datos
		String DB_HOST = "localhost";
		String DB_PORT = "3306";
		String DB_NAME = nombreBBDD.getText();
		String DB_USER = nombreUsuario.getText();
		String DB_PASS = contraBBDD.getText();
		String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
				+ "?serverTimezone=UTC";
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USER,DB_PASS);
			return conn;
		} catch (SQLException ex) {
			return null;
		}
	}

}
