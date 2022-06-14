package Controladores;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

import Funcionamiento.Comics;
import Funcionamiento.DBManager;
import Funcionamiento.NavegacionVentanas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class VerComicController {

	@FXML
	private TextField anioPublicacion;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Button botonbbdd;

	@FXML
	private TableColumn<Comics, String> dibujante;

	@FXML
	private TableColumn<Comics, String> editorial;

	@FXML
	private TableColumn<Comics, String> fecha;

	@FXML
	private TableColumn<Comics, String> firma;

	@FXML
	private TableColumn<Comics, String> formato;

	@FXML
	private TableColumn<Comics, String> guionista;

	@FXML
	private TableColumn<Comics, String> nombre;

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

	@FXML
	private TableColumn<Comics, String> numero;

	@FXML
	private TableColumn<Comics, String> procedencia;

	@FXML
	public TableView<Comics> tablaBBDD;

	@FXML
	private TableColumn<Comics, String> variante;

	@FXML
	private Button botonGuardarBaseDatos;

	@FXML
	private Button BotonVentanaAniadir;

	@FXML
	private Button botonExportarBBDD;
	
    @FXML
    private Button BotonVentanaEliminar;
    
    @FXML
    private Button BotonModificarComic;
    
    @FXML
    private Button BotonEliminarComic;

	@FXML
	private Label labelDatosGuardados;

	@FXML
	private Button botonGuardarBaseDatosExcel;
	
	NavegacionVentanas nav = new NavegacionVentanas();

	@SuppressWarnings("unused")
	private static Connection conn = DBManager.conexion();

	/**
	 * Limpia los campos de pantalla donde se escriben los datos.
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
		procedencia.setText("");
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

		String nombreCom, numeroCom, varianteCom, firmaCom, editorialCom, formatoCom, procedenciaCom, fechaCom,
		guionistaCom, dibujanteCom;

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

		nombreColumnas();

		List<Comics> listComics = FXCollections.observableArrayList(Comics.filtadroBBDD(nombreCom, numeroCom,
				varianteCom, firmaCom, editorialCom, formatoCom, procedenciaCom, fechaCom, guionistaCom, dibujanteCom));
		tablaBBDD.getColumns().setAll(nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listComics);
	}

	/**
	 * Muestra toda la base de datos.
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void verTodabbdd(ActionEvent event) throws SQLException {

		nombreColumnas();

		List<Comics> listComics = FXCollections.observableArrayList(Comics.verTodo());
		tablaBBDD.getColumns().setAll(nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listComics);

	}

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void volverMenu(ActionEvent event) throws IOException {
		
		nav.menuPrincipal();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}

	/**
	 * Permite abrir y cargar la ventana para añadir datos.
	 * 
	 * @param event
	 */
	@FXML
	public void VentanaAniadir(ActionEvent event) {

		nav.aniadirDatos();
		
		Stage myStage = (Stage) this.BotonVentanaAniadir.getScene().getWindow();
		myStage.close();

	}
	
    @FXML
    void ventanaEliminar(ActionEvent event) {
    	
    	
		Stage myStage = (Stage) this.BotonVentanaAniadir.getScene().getWindow();
		myStage.close();

    }
    
    @FXML
    void VentanaModificar(ActionEvent event) {
    	
    	
		Stage myStage = (Stage) this.BotonVentanaAniadir.getScene().getWindow();
		myStage.close();

    }

	/**
	 * Guarda los datos de la base de datos en un fichero.
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void Guardarbbdd(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		File fichero = fileChooser.showSaveDialog(null);

		try {
			if(fichero != null)
			{
				fichero.createNewFile();

				nombreColumnas();

				List<Comics> listComics = FXCollections.observableArrayList(Comics.verTodo());
				tablaBBDD.getColumns().setAll(nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
						guionista, dibujante);

				FileWriter guardarDatos = new FileWriter(fichero);
				for (int i = 0; i < listComics.size(); i++) {
					guardarDatos.write(listComics.get(i) + "\n");
				}
				guardarDatos.close();
			}
			else
			{

				labelDatosGuardados.setStyle("-fx-background-color: #F53636");
				labelDatosGuardados.setText("ERROR. Contenido de la bbdd \n cancelada.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void nombreColumnas()
	{
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

	@FXML
	void GuardarbbddExcel(ActionEvent event) {

		// ConvertirAExcel.export();

		FileChooser fileChooser = new FileChooser();
		File fichero = fileChooser.showSaveDialog(null);

		try {
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet("new sheet");

			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell((short) 0).setCellValue("Nombre");
			rowhead.createCell((short) 1).setCellValue("Numero");
			rowhead.createCell((short) 2).setCellValue("Variante");
			rowhead.createCell((short) 3).setCellValue("Firma");
			rowhead.createCell((short) 4).setCellValue("Editorial");
			rowhead.createCell((short) 5).setCellValue("Formato");
			rowhead.createCell((short) 6).setCellValue("Procedencia");
			rowhead.createCell((short) 7).setCellValue("Publicacion");
			rowhead.createCell((short) 8).setCellValue("Guionista");
			rowhead.createCell((short) 9).setCellValue("Dibujante");

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("Select * from comicsbbdd");
			int i = 1;
			while (rs.next()) {
				HSSFRow row = sheet.createRow((short) i);
				row.createCell((short) 0).setCellValue(rs.getString("nombreCom"));
				row.createCell((short) 1).setCellValue(rs.getString("numeroCom"));
				row.createCell((short) 2).setCellValue(rs.getString("varianteCom"));
				row.createCell((short) 3).setCellValue(rs.getString("firmaCom"));
				row.createCell((short) 4).setCellValue(rs.getString("editorialCom"));
				row.createCell((short) 5).setCellValue(rs.getString("formatoCom"));
				row.createCell((short) 6).setCellValue(rs.getString("procedenciaCom"));
				row.createCell((short) 7).setCellValue(rs.getString("fechaCom"));
				row.createCell((short) 8).setCellValue(rs.getString("guionistaCom"));
				row.createCell((short) 9).setCellValue(rs.getString("dibujanteCom"));
				i++;
			}
			FileOutputStream fileOut = new FileOutputStream(fichero);
			hwb.write(fileOut);
			fileOut.close();
			hwb.close();
			labelDatosGuardados.setStyle("-fx-background-color: #F53636");
			labelDatosGuardados.setText("Se ha generado el Excel correctamente");

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	void exportarBBDD(ActionEvent event) {

		String copiaSeguridad = "";
		FileChooser fileChooser = new FileChooser();
		try {
			File fichero = fileChooser.showSaveDialog(null);
			if(fichero != null)
			{
				fichero.createNewFile();

				copiaSeguridad = "mysqldump --opt -u" + "root" + " -p" + "Forosonanime13!" + " -B " + "comics" + " -r "
						+ fichero + ".sql";
				Runtime rt = Runtime.getRuntime();

				labelDatosGuardados.setStyle("-fx-background-color: #A0F52D");
				labelDatosGuardados.setText("Base de datos exportada \ncorrectamente");

				rt.exec(copiaSeguridad);
			}
			else
			{
				labelDatosGuardados.setStyle("-fx-background-color: #F53636");
				labelDatosGuardados.setText("ERROR. Base de datos \nexportada cancelada.");
			}

		} catch (IOException ex) {

			System.out.println(ex);
		}
	}

	/**
	 * Permite salir totalmente del programa.
	 * 
	 * @param event
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Saliendo");
		alert.setHeaderText("Estas apunto de salir.");
		alert.setContentText("¿Estas seguro que quieres salir?");

		if (alert.showAndWait().get() == ButtonType.OK) {
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 */
	public void closeWindows() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuPrincipal.fxml"));

			Parent root = loader.load();

			Scene scene = new Scene(root);
			Stage stage = new Stage();

			stage.setScene(scene);
			stage.show();

			Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
			myStage.close();

		} catch (IOException ex) {
			Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
