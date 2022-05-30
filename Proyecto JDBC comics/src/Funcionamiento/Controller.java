package Funcionamiento;

import DBManager.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class Controller {

	private static final String DB_MSQ_CONN_OK = "CONEXION CORRECTA";
	private static final String DB_MSQ_CONN_NO = "ERROR EN LA CONEXION";

	@FXML
	private TextField anioPublicacion;

	@FXML
	private Button botonAniadirBBDD;

	@FXML
	private Button botonConexion;

	@FXML
	private RadioButton botonGrapa;

	@FXML
	private RadioButton botonTomo;

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

	public String nombrebaseDatos() {
		return nombreBBDD.getText();
	}
	
	@FXML
	public void BotonAniadir(ActionEvent event) {
		String nom_comic,nom_dibujante,nom_editorial,nom_guionista,nom_variante,num_comic;
		String sentenciaSQL = "insert into comics(nomComic,nomDibujante,nomEditorial,nomGuionista,nomVariante,numComic) values (?,?,?,?,?,?)";
		
		nom_comic = nombreComic.getText();
		nom_dibujante = nombreDibujante.getText();
		nom_editorial = nombreEditorial.getText();
		nom_guionista = nombreGuinista.getText();
		nom_variante = nombreVariante.getText();
		num_comic = numeroComic.getText();
		
		labelResultado.setText(nom_comic + " " + nom_dibujante);

	}

	@FXML
	public void BotonConectar(ActionEvent event) {

		DBManager.loadDriver();
		System.out.println("OK!");
		if(DBManager.isConnected())
		{   
			labelConexion.setStyle("-fx-background-color: #A0F52D");
			labelConexion.setText("Cargando Driver... " + "OK! \nConectando a la base de datos... \n" + DB_MSQ_CONN_OK);
			
		}
		else
		{
			labelConexion.setStyle("-fx-background-color: #DD370F");
			labelConexion.setText("Cargando Driver... " + "OK! \nConectando a la base de datos... \n" +DB_MSQ_CONN_NO);
		}
	}

}
