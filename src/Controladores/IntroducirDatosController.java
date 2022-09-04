package Controladores;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Introducir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase permite acceder a la ventana de introducir datos a la base de datos.
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.shape.Path;
import Funcionamiento.BBDD;
import Funcionamiento.Comic;
import Funcionamiento.ConexionBBDD;
import Funcionamiento.Libreria;
import Funcionamiento.NavegacionVentanas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Esta clase sirve para introducir datos a la base de datos
 *
 * @author Alejandro Rodriguez
 */
public class IntroducirDatosController implements Initializable {

	@FXML
	private Button botonLimpiarComic;

	@FXML
	private Button botonIntroducir;

	@FXML
	private Button botonMostrarParametro;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private Button botonbbdd;

	@FXML
	private Button botonNuevaPortada;

	@FXML
	private TextArea pantallaInformativa;

	@FXML
	private TextField idParametro;

	@FXML
	private TextField nombreParametro;

	@FXML
	private TextField numeroParametro;

	@FXML
	private TextField varianteParametro;

	@FXML
	private TextField firmaParametro;

	@FXML
	private TextField editorialParametro;

	@FXML
	private TextField formatoParametro;

	@FXML
	private TextField procedenciaParametro;

	@FXML
	private TextField fechaParametro;

	@FXML
	private TextField guionistaParametro;

	@FXML
	private TextField dibujanteParametro;

	@FXML
	private TextField nombreAni;

	@FXML
	private TextField numeroAni;

	@FXML
	private TextField varianteAni;

	@FXML
	private TextField firmaAni;

	@FXML
	private TextField editorialAni;

	@FXML
	private TextField formatoAni;

	@FXML
	private TextField procedenciaAni;

	@FXML
	private TextField fechaAni;

	@FXML
	private TextField guionistaAni;

	@FXML
	private TextField dibujanteAni;

	@FXML
	private TextField busquedaGeneral;

	@FXML
	private TextField direccionImagen;

	@FXML
	private Label idMod;

	@FXML
	public TableView<Comic> tablaBBDD;

	@FXML
	private TableColumn<Comic, String> ID;

	@FXML
	private TableColumn<Comic, String> numero;

	@FXML
	private TableColumn<Comic, String> procedencia;

	@FXML
	private TableColumn<Comic, String> variante;

	@FXML
	private TableColumn<Comic, String> dibujante;

	@FXML
	private TableColumn<Comic, String> editorial;

	@FXML
	private TableColumn<Comic, String> fecha;

	@FXML
	private TableColumn<Comic, String> firma;

	@FXML
	private TableColumn<Comic, String> formato;

	@FXML
	private TableColumn<Comic, String> guionista;

	@FXML
	private TableColumn<Comic, String> nombre;

	@FXML
	private ComboBox<String> estadoComic;

	@FXML
	private ImageView imagencomic;

	private static Connection conn = ConexionBBDD.conexion();

	private static NavegacionVentanas nav = new NavegacionVentanas();

	private static Libreria libreria = new Libreria();

	private static BBDD bd = new BBDD();

	/**
	 * Funcion que permite hacer funcionar la lista de puntuacion.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ObservableList<String> situacionEstado = FXCollections.observableArrayList("En posesion", "Vendido",
				"En venta");
		estadoComic.setItems(situacionEstado);
		estadoComic.getSelectionModel().selectFirst(); // Permite que no exista un valor null, escogiendo el primer
		// valor, que se encuentra vacio, en caso de querer borrar
		// la puntuacion.
	}

	/**
	 * Metodo que limpia todos los datos de los textField que hay en pantalla
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {

		// Campos de busqueda por parametro
		idParametro.setText("");

		nombreParametro.setText("");

		numeroParametro.setText("");

		varianteParametro.setText("");

		firmaParametro.setText("");

		editorialParametro.setText("");

		formatoParametro.setText("");

		procedenciaParametro.setText("");

		fechaParametro.setText("");

		guionistaParametro.setText("");

		dibujanteParametro.setText("");

		busquedaGeneral.setText("");

		// Campos de datos a modificar

		nombreAni.setText("");

		numeroAni.setText("");

		nombreAni.setText("");

		firmaAni.setText("");

		editorialAni.setText("");

		formatoAni.setText("");

		procedenciaAni.setText("");

		fechaAni.setText("");

		guionistaAni.setText("");

		dibujanteAni.setText("");

		pantallaInformativa.setText(null);

		pantallaInformativa.setOpacity(0);

		tablaBBDD.getItems().clear();
	}

	//	public void selectorImage()
	//	{
	//		String q="SELECT image FROM comicsbbdd where image = " +
	//
	//		try {
	//			ResultSet rs = db.ejecucionSQL(q);
	//
	//			InputStream is= rs.getBinaryStream("image");
	//			OutputStream os=new FileOutputStream(new File("img.jpg"));
	//			byte [] content= new byte[1024];
	//			int size=0;
	//
	//			while ((size=is.read(content))!=-1){
	//
	//				os.write(content, 0, size);
	//			}
	//			os.close();
	//			is.close();
	//		} catch (IOException | SQLException e) {
	//			e.printStackTrace();
	//		}
	//
	//		javafx.scene.image.Image image1=new Image("file:img.jpg", imagencomic.getFitWidth(), imagencomic.getFitHeight(), true, true);
	//		imagencomic.setImage(image1);
	//		imagencomic.setPreserveRatio(true);
	//	}

	/**
	 * Metodo que añade datos a la base de datos segun los parametros introducidos
	 * en los textField
	 *
	 * @param event
	 */
	@FXML
	public void agregarDatos(ActionEvent event) {

		introducirDatos();
		libreria.reiniciarBBDD();
	}

	@FXML
	void nuevaPortada(ActionEvent event) {
		subirPortada();
	}

	/**
	 *
	 * @return
	 */
	public FileChooser tratarFichero() {
		FileChooser fileChooser = new FileChooser(); // Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters()
		.addAll(new FileChooser.ExtensionFilter("Subiendo imagen", "*.jpg", "*.png", "*.jpeg"));

		return fileChooser;
	}

	public void subirPortada() {
		File file = tratarFichero().showOpenDialog(null); // Llamada a funcion
		if(file != null)
		{
			direccionImagen.setText(file.getAbsolutePath().toString());
		}
	}

	public InputStream direccionImagen(String direccion) {
		try {

			if (direccion.length() != 0) {
				File file = new File(direccion);

				if(file != null)
				{
					InputStream input = new FileInputStream(direccion);
					return input;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String estadoActual() {

		String situacionEstado = estadoComic.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
		// "puntuacion"
		return situacionEstado;

	}


	/**
	 *
	 */
	public void introducirDatos() {
		ConexionBBDD.loadDriver();

		String sentenciaSQL = "insert into comicsbbdd(nomComic,numComic,nomVariante,firma,nomEditorial,formato,procedencia,anioPubli,nomGuionista,nomDibujante,puntuacion,image,estado) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

		String datos[] = camposComicIntroducir();
		
		boolean portadaPredeterminada = false;

		try {
			PreparedStatement statement = conn.prepareStatement(sentenciaSQL);

			statement.setString(1, datos[0]);
			statement.setString(2, datos[1]);
			statement.setString(3, datos[2]);
			statement.setString(4, datos[3]);
			statement.setString(5, datos[4]);
			statement.setString(6, datos[5]);
			statement.setString(7, datos[6]);
			statement.setString(8, datos[7]);
			statement.setString(9, datos[8]);
			statement.setString(10, datos[9]);
			statement.setString(11, "");

			if (datos[10].length() != 0) {
				statement.setBinaryStream(12, direccionImagen(datos[10]));
			} else {
				datos[10] = "sinPortada.jpg";
				InputStream fstream = this.getClass().getResourceAsStream("sinPortada.jpg");
				statement.setBinaryStream(12, fstream);	
				portadaPredeterminada = true;
			}
			statement.setString(13, datos[11]);

			if (nav.alertaInsertar()) {

				if (statement.executeUpdate() == 1) { // Sie el resultado del executeUpdate es 1, mostrara el mensaje
					// correcto.

					pantallaInformativa.setOpacity(1);
					pantallaInformativa.setStyle("-fx-background-color: #A0F52D");
					pantallaInformativa.setText("Comic introducido correctamente!" + "\nNombre del comic: " + datos[0]
							+ "\nNumero: " + datos[1] + "\nPortada variante: " + datos[2] + "\nFirma: " + datos[3]
									+ "\nEditorial: " + datos[4] + "\nFormato: " + datos[5] + "\nProcedencia: " + datos[6]
											+ "\nFecha de publicacion: " + datos[7] + "\nGuionista: " + datos[8] + "\nDibujante: "
											+ datos[9] + "\nEstado: " + datos[11]);
					if(!portadaPredeterminada)
					{
						Image imagex = new Image("file:" + datos[10], 250, 250, true, true);
						imagencomic.setImage(imagex);
					}
					else
					{
						InputStream fstream = this.getClass().getResourceAsStream("sinPortada.jpg");
						Image imagex = new Image(fstream);
						imagencomic.setImage(imagex);
					}
					
					statement.close();

				} else { // En caso de no haber sido posible Introducir el comic, se vera el siguiente
					// mensaje.
					pantallaInformativa.setOpacity(1);
					pantallaInformativa.setStyle("-fx-background-color: #F53636");
					pantallaInformativa.setText(
							"Se ha encontrado un error. No ha sido posible introducir el comic a la base de datos.");
				}
			} else { // Si se cancela el borra del comic, saltara el siguiente mensaje.
				pantallaInformativa.setOpacity(1);
				pantallaInformativa.setStyle("-fx-background-color: #F53636");
				pantallaInformativa.setText("Insertado cancelado..");
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
		direccionImagen.setText("");
		bd.reloadID();
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) {
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		listaPorParametro(); // Llamada a funcion
	}

	/**
	 * Metodo que muestra toda la base de datos.
	 *
	 * @param event
	 */
	@FXML
	void verTodabbdd(ActionEvent event) {
		libreria.reiniciarBBDD();
		nombreColumnas(); // Llamada a funcion
		tablaBBDD(libreriaCompleta()); // Llamada a funcion

	}

	/**
	 * Funcion que muestra los comics que coincidan con los parametros introducidos
	 * en los textField
	 *
	 * @return
	 */
	public void listaPorParametro() {
		libreria.reiniciarBBDD();
		String datosComic[] = camposComicActuales();

		Comic comic = new Comic(datosComic[0], datosComic[1], datosComic[2], datosComic[3], datosComic[4],
				datosComic[5], datosComic[6], datosComic[7], datosComic[8], datosComic[9], datosComic[10], "", "");

		tablaBBDD(busquedaParametro(comic));
	}

	/**
	 * Funcion que busca en el arrayList el o los comics que tengan coincidencia con
	 * los datos introducidos en el TextField
	 *
	 * @param comic
	 * @return
	 */
	public List<Comic> busquedaParametro(Comic comic) {
		List<Comic> listComic;

		if (busquedaGeneral.getText().length() != 0) {
			listComic = FXCollections.observableArrayList(libreria.verBusquedaGeneral(busquedaGeneral.getText()));
			busquedaGeneral.setText("");
		} else {
			listComic = FXCollections.observableArrayList(libreria.filtadroBBDD(comic));
			if (listComic.size() == 0) {
				pantallaInformativa.setOpacity(1);
				pantallaInformativa.setStyle("-fx-background-color: #F53636");
				pantallaInformativa.setText("ERROR. No hay ningun dato en la base de datos");
			}
		}

		return listComic;
	}

	/**
	 * Funcion que muestra todos los comics de la base de datos
	 *
	 * @return
	 */
	public List<Comic> libreriaCompleta() {
		List<Comic> listComic = FXCollections.observableArrayList(libreria.verLibreriaPosesion());

		if (listComic.size() == 0) {
			pantallaInformativa.setOpacity(1);
			pantallaInformativa.setStyle("-fx-background-color: #F53636");
			pantallaInformativa.setText("ERROR. No hay ningun dato en la base de datos");
		}

		return listComic;
	}

	/**
	 * funcion que obtiene los datos de los comics de la base de datos y los
	 * devuelve en el textView
	 *
	 * @param listaComic
	 */
	@SuppressWarnings("unchecked")
	public void tablaBBDD(List<Comic> listaComic) {
		tablaBBDD.getColumns().setAll(ID, nombre, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante);
		tablaBBDD.getItems().setAll(listaComic);
	}

	/**
	 * Funcion que devuelve un array con los datos de los TextField correspondientes
	 * a la los comics que se encuentran en la bbdd
	 *
	 * @return
	 */
	public String[] camposComicActuales() {
		String campos[] = new String[11];

		campos[0] = idParametro.getText();

		campos[1] = nombreParametro.getText();

		campos[2] = numeroParametro.getText();

		campos[3] = varianteParametro.getText();

		campos[4] = firmaParametro.getText();

		campos[5] = editorialParametro.getText();

		campos[6] = formatoParametro.getText();

		campos[7] = procedenciaParametro.getText();

		campos[8] = fechaParametro.getText();

		campos[9] = guionistaParametro.getText();

		campos[10] = dibujanteParametro.getText();

		return campos;
	}

	/**
	 * Funcion que devuelve un array con los datos de los TextField del comic a
	 * Introducir.
	 *
	 * @return
	 */
	public String[] camposComicIntroducir() {
		String campos[] = new String[12];

		campos[0] = nombreAni.getText();

		campos[1] = numeroAni.getText();

		campos[2] = varianteAni.getText();

		campos[3] = firmaAni.getText();

		campos[4] = editorialAni.getText();

		campos[5] = formatoAni.getText();

		campos[6] = procedenciaAni.getText();

		campos[7] = fechaAni.getText();

		campos[8] = guionistaAni.getText();

		campos[9] = dibujanteAni.getText();

		campos[10] = direccionImagen.getText();

		campos[11] = estadoActual();

		return comaPorGuion(campos);
	}

	/**
	 * Funcion que cambia una ',' por un guion '-'
	 *
	 * @param campos
	 * @return
	 */
	public String[] comaPorGuion(String[] campos) {
		for (int i = 0; i < campos.length; i++) {

			if (campos[i].contains(",")) {
				campos[i] = campos[i].replace(",", "-");
			}
		}

		return campos;
	}

	/**
	 * Permite dar valor a las celdas de la TableView
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
	 * Permite volver al menu de conexion a la base de datos.
	 *
	 * @param event
	 */
	@FXML
	void volverMenu(ActionEvent event) {

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
	 */
	public void closeWindows() {

		nav.verMenuPrincipal();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}






}