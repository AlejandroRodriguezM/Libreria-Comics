/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

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
 *  - Puntuar comics que se encuentren dentro de la base de datos.
 *  Esta clase permite acceder al menu principal donde se puede viajar a diferentes ventanas, etc.
 *
 *  Version 8.0.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import java.util.Random;
import java.util.ResourceBundle;

import Controladores.managment.AccionFuncionesComunes;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.SelectManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Esta clase sirve para visualizar de forma aleatoria un comic de la base de
 * datos para poder leer
 *
 * @author Alejandro Rodriguez
 */
public class RecomendacionesController implements Initializable {

	/**
	 * Elemento del menú para desconectar.
	 */
	@FXML
	private MenuItem menu_archivo_desconectar;

	/**
	 * Elemento del menú para mostrar información sobre el autor.
	 */
	@FXML
	private MenuItem menu_archivo_sobreMi;

	/**
	 * Elemento del menú para cerrar la aplicación.
	 */
	@FXML
	private MenuItem menu_archivo_cerrar;

	/**
	 * Elemento del menú para volver atrás.
	 */
	@FXML
	private MenuItem menu_archivo_volver;

	/**
	 * Elemento del menú para añadir un cómic.
	 */
	@FXML
	private MenuItem menu_comic_aniadir;

	/**
	 * Elemento del menú para eliminar un cómic.
	 */
	@FXML
	private MenuItem menu_comic_eliminar;

	/**
	 * Elemento del menú para modificar un cómic.
	 */
	@FXML
	private MenuItem menu_comic_modificar;

	/**
	 * Elemento del menú para puntuar un cómic.
	 */
	@FXML
	private MenuItem menu_comic_puntuar;

	/**
	 * Barra de menú principal.
	 */
	@FXML
	private MenuBar menu_navegacion;

	/**
	 * Menú para la navegación y cierre.
	 */
	@FXML
	private Menu navegacion_cerrar;

	/**
	 * Menú para la navegación relacionada con cómics.
	 */
	@FXML
	private Menu navegacion_comic;

	/**
	 * Menú para la navegación relacionada con estadísticas.
	 */
	@FXML
	private Menu navegacion_estadistica;

	/**
	 * Botón para elegir un archivo.
	 */
	@FXML
	private Button botonElegir;

	/**
	 * Botón para salir de la aplicación.
	 */
	@FXML
	private Button botonSalir;

	/**
	 * Botón para volver atrás.
	 */
	@FXML
	private Button botonVolver;

	/**
	 * Botón para volver al menú principal.
	 */
	@FXML
	private Button botonVolverMenu;

	/**
	 * Área de texto para mostrar información sobre cómics recomendados.
	 */
	@FXML
	private TextArea printComicRecomendado;

	/**
	 * Imagen de cómic recomendado.
	 */
	@FXML
	private ImageView imagencomic;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de Utilidades para funciones auxiliares.
	 */
	private static Utilidades utilidad = null;

	@FXML
	private Label alarmaConexionSql;

	private static AlarmaList alarmaList = new AlarmaList();

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.iniciarThreadChecker(true);

		imagencomic.imageProperty().addListener((observable, oldImage, newImage) -> {
			if (newImage != null) {
				// Cambiar la apariencia del cursor y la opacidad cuando la imagen se ha cargado
				imagencomic.setOnMouseEntered(e -> {
					imagencomic.setOpacity(0.7); // Cambiar la opacidad para indicar que es clickable
					imagencomic.setCursor(Cursor.HAND);
				});

				// Restaurar el cursor y la opacidad al salir del ImageView
				imagencomic.setOnMouseExited(e -> {
					imagencomic.setOpacity(1.0); // Restaurar la opacidad
					imagencomic.setCursor(Cursor.DEFAULT);
				});
			} else {
				// Restaurar el cursor y la opacidad al salir del ImageView
				imagencomic.setOnMouseEntered(e -> {
					imagencomic.setCursor(Cursor.DEFAULT);
				});
			}
		});
	}

	@FXML
	void ampliarImagen(MouseEvent event) {

		if (imagencomic.getImage() != null) {
			nav.verVentanaImagen();
		}

	}

	/**
	 * Llama a funcion que genera una lectura recomendada
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void eligePorMi(ActionEvent event) throws IOException, SQLException {
		printComicRecomendado.setOpacity(1);

		printComicRecomendado.setText(generarLectura());

	}

	/**
	 * Funcion que devuelve un comic al azar de toda la base de datos.
	 *
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public String generarLectura() throws IOException, SQLException {

		if (!ConectManager.conexionActiva()) {
			return null;
		}

		Random r = new Random();
		utilidad = new Utilidades();
		String id_comic;
		String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
		List<Comic> listaComics = SelectManager.verLibreria(sentenciaSQL, false);
		limpiarPront(); // Llamada a función para limpiar la pantalla "TextArea"

		if (listaComics.size() != 0) {
			int n = r.nextInt(listaComics.size());

			id_comic = listaComics.get(n).getID();

			String direccionImagen = SelectManager.obtenerDireccionPortada(id_comic);
			Image imagenComic = Utilidades.devolverImagenComic(direccionImagen);

			imagencomic.setImage(imagenComic);
			utilidad.deleteImage();

			ImagenAmpliadaController.comicInfo = SelectManager.comicDatos(id_comic);

			return listaComics.get(n).toString(); // Devuelve un cómic de la
													// lista
			// de cómics
		} else {
			printComicRecomendado.setText("ERROR. No hay ningún dato en la base de datos");
			printComicRecomendado.setStyle("-fx-background-color: #F53636");
		}
		return "";
	}

	/**
	 * Se limpia el pront de informacion de la ventana
	 */
	public void limpiarPront() {
		printComicRecomendado.setText("");
	}

	/////////////////////////////////
	//// METODOS LLAMADA A VENTANAS//
	/////////////////////////////////

	/**
	 * Maneja la acción del usuario en relación a los cómics, como agregar,
	 * modificar, eliminar o puntuar un cómic.
	 *
	 * @param event El evento de acción que desencadenó la llamada a esta función.
	 */
	@FXML
	void accionComic(ActionEvent event) {

		Object fuente = event.getSource();

		if (fuente instanceof MenuItem) {
			MenuItem menuItemPresionado = (MenuItem) fuente;

			if (menuItemPresionado == menu_comic_aniadir) {
				AccionFuncionesComunes.tipoAccion("aniadir");
			} else if (menuItemPresionado == menu_comic_modificar) {
				AccionFuncionesComunes.tipoAccion("modificar");
			} else if (menuItemPresionado == menu_comic_eliminar) {
				AccionFuncionesComunes.tipoAccion("eliminar");
			} else if (menuItemPresionado == menu_comic_puntuar) {
				AccionFuncionesComunes.tipoAccion("puntuar");
			}
		}

		nav.verAccionComic();
	}

	/**
	 * Metodo que permite abrir la ventana "sobreMiController"
	 *
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {

		nav.verSobreMi();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void desconectar(ActionEvent event) throws IOException {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		nav.verAccesoBBDD();
		ConectManager.close();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	public Scene miStageVentana() {
		Node rootNode = botonElegir;
		while (rootNode.getParent() != null) {
			rootNode = rootNode.getParent();
		}

		if (rootNode instanceof Parent) {
			Scene scene = ((Parent) rootNode).getScene();
			ConectManager.activeScenes.add(scene);
			return scene;
		} else {
			// Manejar el caso en el que no se pueda encontrar un nodo raíz adecuado
			return null;
		}
	}

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	public void volverMenu(ActionEvent event) throws IOException {
		nav.verMenuPrincipal();

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
	}

	/**
	 * Maneja la acción de salida del programa.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {
		// Lógica para manejar la acción de "Salir"
		if (nav.salirPrograma(event)) {
			Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {

		Stage myStage = (Stage) menu_navegacion.getScene().getWindow();
		myStage.close();
		nav.verAccesoBBDD();

	}

}