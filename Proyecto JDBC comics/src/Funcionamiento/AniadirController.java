package Funcionamiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AniadirController {

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
	private static TextField nombreBBDD;

	@FXML
	private TextField nombreGuinista;

	@FXML
	private static TextField nombreUsuario;

	@FXML
	private TextField nombreVariante;

	@FXML
	private static PasswordField contraBBDD;

	@FXML
	private TextField numeroComic;

	@FXML
	private Label labelTestConexion;

	@FXML
	private TextField nombreFormato;

	@FXML
	private static TextField numeroPuerto;

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonLimpiarDatos;

	@FXML
	public void BotonAniadir(ActionEvent event) {

		DBManager.DBManager.loadDriver();
		conn = conexionBBDD();
		String nom_comic, nom_dibujante, nom_editorial, nom_guionista, nom_variante, nom_Formato, num_comic;
		String sentenciaSQL = "insert into comics(nomComic,nomDibujante,nomEditorial,nomGuionista,nomVariante,nomFormato,numComic) values (?,?,?,?,?,?,?)";

		nom_comic = nombreComic.getText();
		nom_dibujante = nombreDibujante.getText();
		nom_editorial = nombreEditorial.getText();
		nom_guionista = nombreGuinista.getText();
		nom_variante = nombreVariante.getText();
		nom_Formato = nombreFormato.getText();
		num_comic = numeroComic.getText();

		if(pruebaConexion())
		{
			try {
				PreparedStatement statement = conn.prepareStatement(sentenciaSQL);
				statement.setString(1, nom_comic);
				statement.setString(2, nom_dibujante);
				statement.setString(3, nom_editorial);
				statement.setString(4, nom_guionista);
				statement.setString(5, nom_variante);
				statement.setString(6, nom_Formato);
				statement.setString(7, num_comic);

				if (statement.executeUpdate() == 1) {
					labelConexion.setStyle("-fx-background-color: #A0F52D");
					labelResultado.setText("Comic añadido correctamente!" + "\nNombre del comic: " + nom_comic
							+ "\nNombre del dibujante: " + nom_dibujante + "\nEditorial: " + nom_editorial
							+ "\nGuinista: " + nom_guionista + "\nVariante: " + nom_variante + "\nNumero del comic: "
							+ num_comic);
					statement.close();
				} else {
					labelConexion.setStyle("-fx-background-color: #DD370F");
					labelResultado.setText(
							"Se ha encontrado un error. No ha sido posible añadir el comic a la base de datos.");
				}
			} catch (SQLException ex) {
				System.err.println("Error al insertar un comic" + ex);
			}
		}
	}

	@FXML
	public void BotonConectar(ActionEvent event) {

		DBManager.DBManager.loadDriver();
		if (DBManager.DBManager.isConnected()) {
			labelTestConexion.setStyle("-fx-background-color: #A0F52D");
			labelTestConexion.setText("Conectado");
			labelConexion.setStyle("-fx-background-color: #A0F52D");
			labelConexion.setText("Cargando Driver... " + "OK! \nConectando a la base de datos... \n" + DB_MSQ_CONN_OK);
		} else {
			contraBBDD.setText("");
			labelTestConexion.setStyle("-fx-background-color: #DD370F");
			labelTestConexion.setText("No conectado");
			labelConexion.setStyle("-fx-background-color: #DD370F");
			labelConexion.setText("Cargando Driver... " + "OK! \nConectando a la base de datos... \n" + DB_MSQ_CONN_NO);
		}
	}

	private static Connection conexionBBDD() {
		return DBManager.DBManager.conexion(numeroPuerto.getText(), nombreBBDD.getText(), nombreUsuario.getText(),
				contraBBDD.getText());
	}

	private boolean pruebaConexion() {
		if(DBManager.DBManager.isConnected())
		{
			return true;
		}
		labelConexion.setStyle("-fx-background-color: #DD370F");
		labelConexion.setText("ERROR. Conecta primero a \nla bbdd");
		return false;

	}

	@FXML
	private void BotonLimpiarBBDD(ActionEvent event) {
		nombreBBDD.setText("");
		nombreUsuario.setText("");
		contraBBDD.setText("");
		numeroPuerto.setText("");
	}

	@FXML
	private void BotonLimpiarComic(ActionEvent event) {
		anioPublicacion.setText("");
		nombreComic.setText("");
		nombreDibujante.setText("");
		nombreEditorial.setText("");
		nombreBBDD.setText("");
		nombreGuinista.setText("");
		nombreVariante.setText("");
		nombreFormato.setText("");
		numeroPuerto.setText("");
	}
}