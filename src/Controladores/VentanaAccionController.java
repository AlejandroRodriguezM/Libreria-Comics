package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.controlsfx.control.textfield.TextFields;

import Funcionamiento.Comic;
import Funcionamiento.FuncionesComboBox;
import Funcionamiento.FuncionesTableView;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import JDBC.DBLibreriaManager;
import JDBC.DBManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VentanaAccionController implements Initializable {

	@FXML
	private Button botonAgregarPuntuacion;

	@FXML
	private Button botonBorrarOpinion;

	@FXML
	private Button botonBusquedaCodigo;

	@FXML
	private Button botonEliminar;

	@FXML
	private Button botonIntroducir;

	@FXML
	private Button botonLimpiar;

	@FXML
	private Button botonModificar;

	@FXML
	private Button botonVender;

	@FXML
	private TextField dibujanteComic;

	@FXML
	private TextField direccionImagen;

	@FXML
	private TextField editorialComic;

	@FXML
	private ComboBox<String> estadoComic;

	@FXML
	private DatePicker fechaComic;

	@FXML
	private TextField firmaComic;

	@FXML
	private ComboBox<String> formatoComic;

	@FXML
	private TextField guionistaComic;

	@FXML
	private TextField idComicTratar;

	@FXML
	private ImageView imagenFondo;

	@FXML
	private Label labelPuntuacion;

	@FXML
	private Label label_id;

	@FXML
	private TextField nombreComic;

	@FXML
	private TextField nombreKeyIssue;

	@FXML
	private ComboBox<String> numeroCajaComic;

	@FXML
	private ComboBox<String> numeroComic;

	@FXML
	private TextField precioComic;

	@FXML
	private ComboBox<String> procedenciaComic;

	@FXML
	private TextArea prontInfo;

	@FXML
	private ComboBox<String> puntuacionMenu;

	@FXML
	private TextField urlReferencia;

	@FXML
	private TextField varianteComic;

	@FXML
	private Button botonSubida;

	@FXML
	private ImageView imagencomic;

	private Stage stage; // Add this field to store the reference to the stage
	private static Ventanas nav = new Ventanas();
	private static DBLibreriaManager libreria = null;
	private static Utilidades utilidad = null;
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();
	private static String TIPO_ACCION;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(() -> {

			listas_autocompletado();

			rellenarCombosEstaticos();

			System.out.println(TIPO_ACCION);
			if ("aniadir".equals(TIPO_ACCION)) {
				mostrarOpcionAniadir();
			} else if ("eliminar".equals(TIPO_ACCION)) {
				mostrarOpcionEliminar();
			} else if ("modificar".equals(TIPO_ACCION)) {
				mostrarOpcionModificar();
			} else if ("puntuar".equals(TIPO_ACCION)) {
				mostrarOpcionPuntuar();
			} else {
				closeWindow();
			}
		});

		numeroComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		numeroCajaComic.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		idComicTratar.setTextFormatter(FuncionesComboBox.validador_Nenteros());
		precioComic.setTextFormatter(FuncionesComboBox.validador_Ndecimales());

		FuncionesTableView.restringirSimbolos(guionistaComic);
		FuncionesTableView.restringirSimbolos(dibujanteComic);
		FuncionesTableView.restringirSimbolos(varianteComic);
	}

	/**
	 * Asigna autocompletado a campos de texto en la interfaz.
	 */
	public void listas_autocompletado() {
		TextFields.bindAutoCompletion(nombreComic, DBLibreriaManager.listaNombre);
		TextFields.bindAutoCompletion(varianteComic, DBLibreriaManager.listaVariante);
		TextFields.bindAutoCompletion(firmaComic, DBLibreriaManager.listaFirma);
		TextFields.bindAutoCompletion(editorialComic, DBLibreriaManager.listaEditorial);
		TextFields.bindAutoCompletion(guionistaComic, DBLibreriaManager.listaGuionista);
		TextFields.bindAutoCompletion(dibujanteComic, DBLibreriaManager.listaDibujante);
		TextFields.bindAutoCompletion(numeroComic.getEditor(), DBLibreriaManager.listaNumeroComic);
	}

	/**
	 * Rellena los combos estáticos en la interfaz. Esta función llena los
	 * ComboBoxes con opciones estáticas predefinidas.
	 */
	public void rellenarCombosEstaticos() {
		List<ComboBox<String>> comboboxesMod = Arrays.asList(formatoComic, procedenciaComic, estadoComic);
		funcionesCombo.rellenarComboBoxEstaticos(comboboxesMod); // Llamada a la función para rellenar ComboBoxes
	}

	public void ocultarOpciones() {
		if (botonBorrarOpinion.isVisible()) {
			botonBorrarOpinion.setVisible(false);
			botonBorrarOpinion.setDisable(true);
		}

		if (puntuacionMenu.isVisible()) {
			puntuacionMenu.setVisible(false);
			puntuacionMenu.setDisable(true);
		}

		if (labelPuntuacion.isVisible()) {
			labelPuntuacion.setVisible(false);
			labelPuntuacion.setDisable(true);
		}

		if (botonAgregarPuntuacion.isVisible()) {
			botonAgregarPuntuacion.setVisible(false);
			botonAgregarPuntuacion.setDisable(true);
		}

		if (label_id.isVisible()) {
			label_id.setVisible(false);
			label_id.setDisable(true);
		}

		if (botonVender.isVisible()) {
			botonVender.setVisible(false);
			botonVender.setDisable(true);
		}

		if (botonEliminar.isVisible()) {
			botonEliminar.setVisible(false);
			botonEliminar.setDisable(true);
		}

		if (idComicTratar.isVisible()) {
			idComicTratar.setVisible(false);
			idComicTratar.setDisable(true);
		}

		if (botonModificar.isVisible()) {
			botonModificar.setVisible(false);
			botonModificar.setDisable(true);
		}

		if (botonBusquedaCodigo.isVisible()) {
			botonBusquedaCodigo.setVisible(false);
			botonBusquedaCodigo.setDisable(true);
		}

		if (botonIntroducir.isVisible()) {
			botonIntroducir.setVisible(false);
			botonIntroducir.setDisable(true);
		}
	}

	public void mostrarOpcionEliminar() {
		ocultarOpciones();

		label_id.setVisible(true);
		botonVender.setVisible(true);
		botonEliminar.setVisible(true);
		idComicTratar.setVisible(true);

		label_id.setDisable(false);
		botonVender.setDisable(false);
		botonEliminar.setDisable(false);
		idComicTratar.setDisable(false);
	}

	public void mostrarOpcionAniadir() {
		ocultarOpciones();

		botonIntroducir.setVisible(true);
		botonBusquedaCodigo.setVisible(true);

		botonIntroducir.setDisable(false);
		botonBusquedaCodigo.setDisable(false);
	}

	public void mostrarOpcionModificar() {
		ocultarOpciones();

		label_id.setVisible(true);
		botonModificar.setVisible(true);
		idComicTratar.setVisible(true);

		label_id.setDisable(false);
		botonModificar.setDisable(false);
		idComicTratar.setDisable(false);

	}

	public void mostrarOpcionPuntuar() {
		ocultarOpciones();

		botonBorrarOpinion.setVisible(true);
		puntuacionMenu.setVisible(true);
		labelPuntuacion.setVisible(true);
		botonAgregarPuntuacion.setVisible(true);
		idComicTratar.setVisible(true);
		label_id.setVisible(true);

		botonBorrarOpinion.setDisable(false);
		puntuacionMenu.setDisable(false);
		labelPuntuacion.setDisable(false);
		botonAgregarPuntuacion.setDisable(false);
		label_id.setDisable(false);
		idComicTratar.setDisable(false);
	}

	public static void tipoAccion(String tipoAccion) {
		TIPO_ACCION = tipoAccion;
	}

	@FXML
	public void agregarDatos(ActionEvent event) throws IOException, SQLException {

		libreria = new DBLibreriaManager();
		subidaComic();
		libreria.reiniciarBBDD();
		direccionImagen.setText("");
	}

	/**
	 * Funcion que permite que al pulsar el boton 'botonOpinion' se modifique el
	 * dato "puntuacion" de un comic en concreto usando su ID"
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void agregarPuntuacion(ActionEvent event) throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");
		if (id_comic.length() == 0 || !libreria.checkID(id_comic)) {

			idComicTratar.setStyle("-fx-background-color: #FF0000;");
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {
			libreria.actualizarPuntuacion(id_comic, comicPuntuacion()); // Llamada a funcion
			prontInfo.setText("Deseo concedido. Has añadido el nuevo comic.");
		}
	}

	/**
	 * Funcion que permite borrar la opinion de un comic
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void borrarPuntuacion(ActionEvent event) throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");

		if (id_comic.length() == 0 || !libreria.checkID(id_comic)) {
			idComicTratar.setStyle("-fx-background-color: #FF0000;");
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {
			libreria.borrarPuntuacion(id_comic);
			prontInfo.setText("Deseo concedido. Has borrado la puntuacion del comic.");

			Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
			imagenFondo.setImage(nuevaImagen);
		}
	}

	@FXML
	void busquedaPorCodigo(ActionEvent event) {

	}

	/**
	 * Funcion que elimina un comic de la base de datos.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void eliminarDatos(ActionEvent event) throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");

		if (id_comic.length() == 0 || !libreria.checkID(id_comic)) {

			idComicTratar.setStyle("-fx-background-color: #FF0000;");

			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {
			cambioEstado(id_comic);
			libreria.eliminarComicBBDD(id_comic);
			libreria.reiniciarBBDD();
			libreria.listasAutoCompletado();

			Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
			imagenFondo.setImage(nuevaImagen);
		}
	}

	/**
	 * Funcion que permite cambiar de estado o eliminar un comic de la base de
	 * datos.
	 *
	 * @param id
	 * @param sentenciaSQL
	 * @throws SQLException
	 */
	public boolean cambioEstado(String ID) throws SQLException {
		libreria = new DBLibreriaManager();
		if (nav.alertaEliminar()) {
			if (ID.length() != 0) {

				Comic comic = libreria.comicDatos(ID); // Llamada de metodo que contiene el comic que se desea eliminar

				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo
						.setText("Has modificado correctamente: " + comic.toString().replace("[", "").replace("]", ""));
				idComicTratar.setStyle(null);
				return true;
			} else {
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("ERROR. ID desconocido.");
				idComicTratar.setStyle("-fx-background-color: red");
				return false;
			}
		} else { // Si se cancela el borra del comic, saltara el siguiente mensaje.
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Modificacion cancelada.");
			return false;
		}
	}

	@FXML
	void limpiarDatos(ActionEvent event) {
		// Campos de datos a modificar en la sección de animaciones

		Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComic.jpg"));
		imagenFondo.setImage(nuevaImagen);

		nombreComic.setText("");
		varianteComic.setText("");
		firmaComic.setText("");
		editorialComic.setText("");
		fechaComic.setValue(null);
		guionistaComic.setText("");
		dibujanteComic.setText("");
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		nombreKeyIssue.setText("");
		borrarErrores();
	}

	/**
	 * Elimina cualquier resaltado de campos en rojo que indique errores.
	 */
	public void borrarErrores() {
		// Restaurar el estilo de fondo de los campos a su estado original
		nombreComic.setStyle("");
		numeroComic.setStyle("");
		editorialComic.setStyle("");
		guionistaComic.setStyle("");
		dibujanteComic.setStyle("");
	}

	/**
	 * Llamada a funcion que modifica los datos de 1 comic en la base de datos.
	 *
	 * @param event
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	@FXML
	void modificarDatos(ActionEvent event) throws NumberFormatException, SQLException, IOException {

		libreria = new DBLibreriaManager();
		modificacionComic(); // Llamada a funcion que modificara el contenido de un comic especifico.
		libreria.reiniciarBBDD();
		direccionImagen.setText("");
		libreria.listasAutoCompletado();
	}

	/**
	 * Funcion que permite la subida de una
	 *
	 * @param event
	 */
	@FXML
	void nuevaPortada(ActionEvent event) {
		subirPortada();
	}

	/**
	 * Metodo que permite cambiar de estado un comic, para que se deje de mostrar en
	 * el programa, pero este sigue estando dentro de la bbdd
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void ventaComic(ActionEvent event) throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		String id_comic = idComicTratar.getText();
		idComicTratar.setStyle("");

		if (id_comic.length() == 0 || !libreria.checkID(id_comic)) {
			idComicTratar.setStyle("-fx-background-color: #FF0000;");
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Error. Debes de introducir los datos correctos");
		} else {
			cambioEstado(id_comic);
			libreria.venderComicBBDD(id_comic);
			libreria.reiniciarBBDD();

			prontInfo.setText("Deseo concedido. Has puesto a la venta el comic");

			Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
			imagenFondo.setImage(nuevaImagen);
		}
	}

	/**
	 * Permite abir una ventana para abrir ficheros de un determinado formato.
	 *
	 * @return
	 */
	public FileChooser tratarFichero() {
		FileChooser fileChooser = new FileChooser(); // Permite escoger donde se encuentra el fichero
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Subiendo imagen", "*.jpg"));

		return fileChooser;
	}

	/**
	 * Funcion que escribe en el TextField de "Direccion de imagen" la dirrecion de
	 * la imagen
	 */
	public void subirPortada() {
		File file = tratarFichero().showOpenDialog(null); // Llamada a funcion
		if (file != null) {
			direccionImagen.setText(file.getAbsolutePath().toString());
		} else {
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Has cancelado la subida de portada.");
		}
	}

	/**
	 * Funcion que permite modificar un comic, segun los datos introducidos
	 * 
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	public void modificacionComic() throws NumberFormatException, SQLException, IOException {
		libreria = new DBLibreriaManager();
		Comic comic_temp = new Comic();
		Image imagen = null;
		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + DBManager.DB_NAME
				+ File.separator + "portadas";
		Utilidades.convertirNombresCarpetas(sourcePath);

		if (nav.alertaModificar()) {

			String datos[] = camposComic();

			String id_comic = datos[0];

			if (libreria.checkID(id_comic)) {
				comic_temp = libreria.comicDatos(id_comic);

				String nombre = "";

				String numero = "";

				String variante = "";

				String firma = "";

				String editorial = "";

				String formato = "";

				String procedencia = "";

				String fecha = "";
				
				String guionista = datos[8];

				String dibujante = datos[9];

				String estado = datos[11];

				String numCaja = "";

				String portada = "";

				String puntuacion = "";

				String nombreKeyIssue = "";

				String url_referencia = "";

				String precio_comic = "";

				if (datos[0].isEmpty()) {
					nombre = comic_temp.getNombre();
				} else {
					nombre = datos[0];
				}

				if (datos[1].isEmpty()) {
					numero = comic_temp.getNumero();
				} else {
					numero = datos[1];
				}

				if (datos[2].isEmpty()) {
					variante = comic_temp.getVariante();
				} else {
					variante = datos[2];
				}

				if (datos[3].isEmpty()) {
					firma = comic_temp.getFirma();
				} else {
					firma = datos[3];
				}

				if (datos[4].isEmpty()) {
					editorial = comic_temp.getEditorial();
				} else {
					editorial = datos[4];
				}

				if (datos[5].isEmpty()) {
					formato = comic_temp.getFormato();
				} else {
					formato = datos[5];
				}

				if (datos[6].isEmpty()) {
					procedencia = comic_temp.getProcedencia();
				} else {
					procedencia = datos[6];
				}

				if (datos[7].isEmpty()) {
					fecha = comic_temp.getFecha();
				} else {
					fecha = datos[7];
				}

				if (datos[8].isEmpty()) {
					guionista = comic_temp.getGuionista();
				} else {
					guionista = datos[8];
				}

				if (datos[9].isEmpty()) {
					dibujante = comic_temp.getDibujante();
				} else {
					dibujante = datos[9];
				}

				if (datos[10].isEmpty()) {
					portada = comic_temp.getImagen();
					imagen = new Image(portada);
				} else {
					portada = datos[10];
					imagen = new Image(portada);
				}

				if (datos[11].isEmpty()) {
					estado = comic_temp.getEstado();
				} else {
					estado = datos[11];
				}

				if (datos[12].isEmpty()) {
					numCaja = comic_temp.getNumCaja();

				} else {
					numCaja = datos[12];
				}

				if (!comic_temp.getPuntuacion().equals("Sin puntuar")) {
					puntuacion = comic_temp.getPuntuacion();
				} else {
					puntuacion = "Sin puntuar";
				}

				nombreKeyIssue = "Vacio";
				String key_issue_sinEspacios = datos[13].trim();

				Pattern pattern = Pattern.compile(".*\\w+.*");
				Matcher matcher = pattern.matcher(key_issue_sinEspacios);

				if (!key_issue_sinEspacios.isEmpty() && matcher.matches()) {
					nombreKeyIssue = key_issue_sinEspacios;
				}

				if (datos[14].isEmpty()) {
					url_referencia = comic_temp.getUrl_referencia();
				} else {
					url_referencia = datos[14];
				}

				if (datos[15].isEmpty()) {
					precio_comic = comic_temp.getPrecio_comic();
				} else {
					precio_comic = datos[15];
				}

				double valor_comic = Double.parseDouble(precio_comic);

				precio_comic = String.valueOf(Utilidades.convertirMonedaADolar(procedencia, valor_comic));

				Comic comic = new Comic(id_comic, nombre, numCaja, numero, variante, firma, editorial, formato,
						procedencia, fecha, guionista, dibujante, estado, nombreKeyIssue, puntuacion, portada,
						url_referencia, precio_comic);

				if (id_comic.length() == 0 || !libreria.checkID(id_comic) || nombre.length() == 0
						|| numero.length() == 0 || editorial.length() == 0 || guionista.length() == 0
						|| dibujante.length() == 0 || procedencia.length() == 0) {

					String excepcion = "ERROR.Faltan datos por rellenar";
					nav.alertaException(excepcion);
					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #F53636");
					prontInfo.setText("Error. Debes de introducir los datos correctos");
				} else {
					libreria.actualizarComic(comic);
					Utilidades.eliminarFichero(comic_temp.getImagen());

					prontInfo.setOpacity(1);
					prontInfo.setStyle("-fx-background-color: #A0F52D");
					prontInfo.setText(
							"Has modificado correctamente: " + comic.toString().replace("[", "").replace("]", ""));
					libreria.listasAutoCompletado();

					imagencomic.setImage(imagen);

					Image nuevaImagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
					imagenFondo.setImage(nuevaImagen);
				}
			} else {
				String excepcion = "No puedes modificar un comic si antes no pones un ID valido";
				nav.alertaException(excepcion);
				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("Error. Debes de introducir un ID valido");
			}
		} else {
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Se ha cancelado la modificacion del comic.");
		}
	}

	/**
	 * Funcion que permite modificar la puntuacion de un comic, siempre y cuando el
	 * ID exista en la base de datos
	 *
	 * @param ps
	 * @return
	 */
	public String comicPuntuacion() {

		String puntuacion = puntuacionMenu.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
																								// "puntuacion"
		return puntuacion;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "numeroComic" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String numero() {
		String numComic = "0";

		if (numeroComic.getSelectionModel().getSelectedItem() != null) {
			numComic = numeroComic.getSelectionModel().getSelectedItem().toString();
		}

		return numComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String caja() {

		String cajaComics = "0";

		if (numeroCajaComic.getSelectionModel().getSelectedItem() != null) {
			cajaComics = numeroCajaComic.getSelectionModel().getSelectedItem().toString();
		}

		return cajaComics;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String estado() {

		String estadoNuevo = estadoComic.getSelectionModel().getSelectedItem().toString();

		return estadoNuevo;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreFormato" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public String formato() {

		String formatoEstado = formatoComic.getSelectionModel().getSelectedItem().toString();

		return formatoEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public String procedencia() {

		String procedenciaEstadoNuevo = procedenciaComic.getSelectionModel().getSelectedItem().toString();

		return procedenciaEstadoNuevo;
	}

	/**
	 * Funcion que devuelve un array con los datos de los TextField del comic a
	 * introducir.
	 *
	 * @return
	 */
	public String[] camposComic() {

		utilidad = new Utilidades();

		String campos[] = new String[16];

		campos[0] = utilidad.comaPorGuion(nombreComic.getText());

		campos[1] = numero();

		campos[2] = utilidad.comaPorGuion(varianteComic.getText());

		campos[3] = utilidad.comaPorGuion(firmaComic.getText());

		campos[4] = editorialComic.getText();

		campos[5] = formato();

		campos[6] = procedencia();

		LocalDate fecha = fechaComic.getValue();
		if (fecha != null) {
			campos[7] = fecha.toString();
		} else {
			campos[7] = "2000-01-01";
		}

		campos[8] = utilidad.comaPorGuion(guionistaComic.getText());

		campos[9] = utilidad.comaPorGuion(dibujanteComic.getText());

		campos[10] = direccionImagen.getText();

		campos[11] = estado();

		campos[12] = caja();

		campos[13] = nombreKeyIssue.getText();

		campos[14] = urlReferencia.getText();

		campos[15] = precioComic.getText();

		for (String string : campos) {
			System.out.println(string);
		}

		return campos;
	}

	/**
	 * Valida los campos del cómic y resalta en rojo aquellos que estén vacíos.
	 *
	 * @param comic El cómic a validar.
	 */
	public void validateComicFields(Comic comic) {
		if (comic.getNombre().length() == 0) {
			nombreComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo nombre
		}

		if (comic.getNumero().length() == 0) {
			numeroComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo número
		}

		if (comic.getEditorial().length() == 0) {
			editorialComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo editorial
		}

		if (comic.getGuionista().length() == 0) {
			guionistaComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo guionista
		}

		if (comic.getDibujante().length() == 0) {
			dibujanteComic.setStyle("-fx-background-color: #FF0000;"); // Resaltar en rojo el campo dibujante
		}
	}

	/**
	 * Permite introducir un comic en la base de datos de forma manual
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public void subidaComic() throws IOException, SQLException {
		libreria = new DBLibreriaManager();
		utilidad = new Utilidades();
		new MenuPrincipalController();
		File file;
		LocalDate fecha_comic;

		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String sourcePath = documentsPath + File.separator + "libreria_comics" + File.separator + DBManager.DB_NAME
				+ File.separator + "portadas";
		Utilidades.convertirNombresCarpetas(sourcePath);

		if (nav.alertaInsertar()) {

			String datos[] = camposComic();

			String portada = "";

			String nombre = datos[0];

			String numero = datos[1];

			String variante = datos[2];

			String firma = datos[3];

			String editorial = datos[4];

			String formato = datos[5];

			String procedencia = datos[6];

			if (datos[7] == null) {
				datos[7] = "2000-01-01";
				fecha_comic = LocalDate.parse(datos[7]);
			} else {
				fecha_comic = LocalDate.parse(datos[7]);
			}

			String guionista = datos[8];

			String dibujante = datos[9];

			if (datos[10] != "") {

				Image imagen = null;

				if (Utilidades.isURL(datos[10])) {
					// Es una URL en internet
					portada = Utilidades.descargarImagen(datos[10], documentsPath);
				} else {

					file = new File(datos[10]);
					if (!file.exists()) {
						portada = "Funcionamiento/sinPortada.jpg";
						imagen = new Image(portada);

					} else {
						portada = datos[10];
						imagen = new Image(portada);
					}
				}

				imagencomic.setImage(imagen);
			}

			String estado = datos[11];

			String numCaja = datos[12];

			if (numCaja.isEmpty()) {
				numCaja = "0";
			}

			String key_issue = "Vacio";
			String key_issue_sinEspacios = datos[13].trim();

			Pattern pattern = Pattern.compile(".*\\w+.*");
			Matcher matcher = pattern.matcher(key_issue_sinEspacios);

			if (!key_issue_sinEspacios.isEmpty() && matcher.matches()) {
				key_issue = key_issue_sinEspacios;
			}

			String url_referencia = datos[14];
			String precio_comic = datos[15];

			if (precio_comic.isEmpty()) {
				precio_comic = "0";
			}

			double valor_comic = Double.parseDouble(precio_comic);

			precio_comic = String.valueOf(Utilidades.convertirMonedaADolar(procedencia, valor_comic));

			if (url_referencia.isEmpty()) {
				url_referencia = "Sin referencia";
			}

			Comic comic = new Comic("", nombre, numCaja, numero, variante, firma, editorial, formato, procedencia,
					fecha_comic.toString(), guionista, dibujante, estado, key_issue, "Sin puntuar", portada,
					url_referencia, precio_comic);

			if (nombre.isEmpty() || numero.isEmpty() || editorial.isEmpty() || guionista.isEmpty()
					|| dibujante.isEmpty()) {
				String excepcion = "No puedes introducir un comic si no has completado todos los datos";

				validateComicFields(comic);

				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #F53636");
				prontInfo.setText("Error. Debes de introducir los datos correctos.");

				nav.alertaException(excepcion);
			} else {

				String codigo_imagen = Utilidades.generarCodigoUnico(sourcePath + File.separator);

				utilidad.nueva_imagen(portada, codigo_imagen);
				comic.setImagen(sourcePath + File.separator + codigo_imagen + ".jpg");
				libreria.insertarDatos(comic);

				prontInfo.setOpacity(1);
				prontInfo.setStyle("-fx-background-color: #A0F52D");
				prontInfo.setText(
						"Has introducido correctamente: \n" + comic.toString().replace("[", "").replace("]", ""));
				libreria.listasAutoCompletado();

				if (Utilidades.isURL(datos[10])) {
					Utilidades.borrarImagen(portada);
				}

				Image imagen = new Image(getClass().getResourceAsStream("/imagenes/accionComicDeseo.jpg"));
				imagenFondo.setImage(imagen);
			}
		} else {
			prontInfo.setOpacity(1);
			prontInfo.setStyle("-fx-background-color: #F53636");
			prontInfo.setText("Se ha cancelado la subida del nuevo comic.");
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void closeWindow() {
		if (stage != null) {
			stage.close();
		}
	}

}
