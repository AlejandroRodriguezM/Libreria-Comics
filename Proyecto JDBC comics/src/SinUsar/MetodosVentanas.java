package SinUsar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import Controladores.AccesoBBDDController;
import Funcionamiento.Comics;
import Funcionamiento.DBManager;
import Funcionamiento.NavegacionVentanas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

public class MetodosVentanas {

	private TextField anioPublicacion;

	private TextField numeroID;

	private TextField nombreComic;

	private TextField nombreDibujante;

	private TextField nombreEditorial;

	private TextField nombreFirma;

	private TextField nombreFormato;

	private TextField nombreGuionista;

	private TextField nombreProcedencia;

	private TextField nombreVariante;

	private TextField numeroComic;

	private TableColumn<Comics, String> dibujante;

	private TableColumn<Comics, String> editorial;

	private TableColumn<Comics, String> fecha;

	private TableColumn<Comics, String> firma;

	private TableColumn<Comics, String> formato;

	private TableColumn<Comics, String> guionista;

	private TableColumn<Comics, String> nombre;

	private TableColumn<Comics, String> ID;

	private TableColumn<Comics, String> numero;

	private TableColumn<Comics, String> procedencia;

	private TableColumn<Comics, String> variante;

	public TableView<Comics> tablaBBDD;
	
	private Label labelDatosGuardados;
	
	private static String nombreBBDD;

	private static String usuarioBBDD;

	private static String passBBDD;
	
	NavegacionVentanas nav = new NavegacionVentanas();

	AccesoBBDDController datos = new AccesoBBDDController();
	
	Comics comic = new Comics();

	private static Connection conn = DBManager.conexion();

	/**
	 * 
	 */
	public void limpiarDatos() {

		numeroID.setText("");
		nombreComic.setText("");
		numeroComic.setText("");
		nombreVariante.setText("");
		nombreFirma.setText("");
		nombreEditorial.setText("");
		nombreFormato.setText("");
		nombreProcedencia.setText("");
		anioPublicacion.setText("");
		nombreDibujante.setText("");
		nombreGuionista.setText("");
	}

	/**
	 * Muestra la bbdd segun los parametros introducidos en los TextField
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void mostrarPorParametro(ActionEvent event) throws SQLException {

		String idCom, nombreCom, numeroCom, varianteCom, firmaCom, editorialCom, formatoCom, procedenciaCom, fechaCom,
		guionistaCom, dibujanteCom;

		idCom = numeroID.getText();

		nombreCom = nombreComic.getText();

		numeroCom = numeroComic.getText();

		varianteCom = nombreVariante.getText();

		firmaCom = nombreFirma.getText();

		editorialCom = nombreEditorial.getText();

		formatoCom = nombreFormato.getText();

		procedenciaCom = procedencia.getText();

		fechaCom = anioPublicacion.getText();

		guionistaCom = nombreGuionista.getText();

		dibujanteCom = nombreDibujante.getText();

		nombreColumnas();

		List<Comics> listComics = FXCollections.observableArrayList(comic.filtadroBBDD(idCom, nombreCom, numeroCom,
				varianteCom, firmaCom, editorialCom, formatoCom, procedenciaCom, fechaCom, guionistaCom, dibujanteCom));
		tablaBBDD.getColumns().setAll(ID, nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listComics);
	}

	/**
	 * 
	 */
	private void nombreColumnas() {
		ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		variante.setCellValueFactory(new PropertyValueFactory<>("variante"));
		firma.setCellValueFactory(new PropertyValueFactory<>("firma"));
		editorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
		formato.setCellValueFactory(new PropertyValueFactory<>("formato"));
		procedencia.setCellValueFactory(new PropertyValueFactory<>("procedencia"));
		fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		guionista.setCellValueFactory(new PropertyValueFactory<>("guionista"));
		dibujante.setCellValueFactory(new PropertyValueFactory<>("dibujante"));
	}

	/**
	 * Guarda los datos de la base de datos en un fichero.
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	void Guardarbbdd(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		File fichero = fileChooser.showSaveDialog(null);

		try {
			if (fichero != null) {
				fichero.createNewFile();

				nombreColumnas();

				List<Comics> listComics = FXCollections.observableArrayList(comic.verTodo());
				tablaBBDD.getColumns().setAll(nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
						guionista, dibujante);

				FileWriter guardarDatos = new FileWriter(fichero);
				for (int i = 0; i < listComics.size(); i++) {
					guardarDatos.write(listComics.get(i) + "\n");
				}
				guardarDatos.close();
			} else {
				labelDatosGuardados.setStyle("-fx-background-color: #F53636");
				labelDatosGuardados.setText("ERROR. Contenido de la bbdd \n cancelada.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void GuardarbbddExcel(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		File fichero = fileChooser.showSaveDialog(null);


		try {
			String query="select * from comicsbbdd";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			BufferedWriter fw = new BufferedWriter(new FileWriter(fichero + ".xls"));
			fw.write("ID,NomComic,numComic,nomVariante,firma,nomEditorial,formato,procedencia,anioPubli,nomGuionista,nomDibujante,estado");

			while(rs.next()){
				String id = (rs.getString("ID"));
				String nombre = (rs.getString("nomComic"));
				String numero = (rs.getString("numComic"));
				String variante = (rs.getString("nomVariante"));
				String firma = (rs.getString("firma"));
				String editorial = (rs.getString("nomEditorial"));
				String formato = (rs.getString("formato"));
				String procedencia = (rs.getString("procedencia"));
				String fecha = (rs.getString("anioPubli"));
				String guionista = (rs.getString("nomGuionista"));
				String dibujante = (rs.getString("nomDibujante"));
				String estado = (rs.getString("estado"));

				String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
						id, nombre, numero, variante, firma,editorial,formato,procedencia,fecha,guionista,dibujante,estado);

				fw.newLine();
				fw.write(line);
			}

			statement.close();
			fw.close();

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		} 
	}
	
	//FUNCIONA SOLO EN LINUX
	/**
	 * 
	 * @param event
	 */
	void exportarBBDD(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		File fichero = fileChooser.showSaveDialog(null);
		if (fichero != null) {
			try {
				fichero.createNewFile();
				String mysqlCom = String.format("mysqldump -u%s -p%s %s", usuarioBBDD, passBBDD, nombreBBDD);
				String[] command = new String[] { "/bin/bash", "-c", mysqlCom };
				ProcessBuilder pb = new ProcessBuilder(Arrays.asList(command));
				pb.redirectError(Redirect.INHERIT);
				pb.redirectOutput(Redirect.to(fichero));
				pb.start();
				labelDatosGuardados.setStyle("-fx-background-color: #A0F52D");
				labelDatosGuardados.setText("Base de datos exportada \ncorrectamente");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} else {
			labelDatosGuardados.setStyle("-fx-background-color: #F53636");
			labelDatosGuardados.setText("ERROR. Base de datos \nexportada cancelada.");
		}
	}
	
}
