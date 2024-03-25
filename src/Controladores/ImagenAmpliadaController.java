package Controladores;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import Funcionamiento.Utilidades;
import comicManagement.Comic;
import dbmanager.ConectManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImagenAmpliadaController implements Initializable {

	/**
	 * Referencia a la ventana (stage).
	 */
	private Stage stage;

	public static Comic comicInfo;

	@FXML
	private ImageView imagenAmpliada;

	@FXML
	private TextArea infoComic;

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mostrarImagen();
		String infoComicString = "";
		// Crear el menú contextual
		ContextMenu contextMenu = new ContextMenu();
		MenuItem guardarItem = new MenuItem("Guardar imagen");
		guardarItem.setOnAction(event -> guardarImagen(comicInfo.getImagen()));
		contextMenu.getItems().add(guardarItem);

		// Manejar el evento de clic derecho para mostrar el menú contextual
		imagenAmpliada.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				contextMenu.show(imagenAmpliada, event.getScreenX(), event.getScreenY());
			}
		});

		if (!comicInfo.devolverKeyIssue().isEmpty()) {
			infoComicString = comicInfo.infoComic() + "\n" + comicInfo.devolverKeyIssue();

		} else {
			infoComicString = comicInfo.infoComic();

		}

		infoComic.setText(infoComicString);

		// Obtener el ancho del TextArea desde el FXML
		double textAreaWidth = infoComic.getPrefWidth();
		double textAreaHeight = infoComic.getPrefHeight();

		infoComic.setPrefHeight(computeTextHeight(infoComicString, infoComic.getFont(), textAreaWidth, textAreaHeight));

	}

	// Método para calcular la altura del texto de manera dinámica
	private double computeTextHeight(String text, Font font, double textAreaWidth, double textAreaHeight) {
		// Crear un nodo Text para medir el tamaño real del texto
		Text textNode = new Text(text);
		textNode.setFont(font);

		// Establecer el ancho del nodo Text para envolver el texto correctamente
		textNode.setWrappingWidth(textAreaWidth);

		// Calcular la altura necesaria para mostrar todo el texto
		double totalHeight = textNode.getLayoutBounds().getHeight();

		return totalHeight * 1.1;
	}

	public Scene miStageVentana() {
		Node rootNode = imagenAmpliada;
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

	public void mostrarImagen() {

		String direccionFinalImg = "";
		Image imagenCargada = null;
		if (Utilidades.existePortada(comicInfo.getImagen())) {
			direccionFinalImg = comicInfo.getImagen();
			imagenCargada = new Image(new File(direccionFinalImg).toURI().toString(), true);
		} else {
			InputStream is = getClass().getResourceAsStream("/imagenes/sinPortada.jpg");
			imagenCargada = new Image(is, 250, 0, true, true);
		}

		// Configura el ImageView con la imagen cargada
		imagenAmpliada.setImage(imagenCargada);
		imagenAmpliada.setPreserveRatio(true); // Mantiene la relación de aspecto de la imagen
	}

	// Método para guardar la imagen
	private void guardarImagen(String filePath) {

		String nombreFichero = Utilidades.obtenerNombrePortada(false, filePath);

		// Crear un FileChooser para permitir al usuario seleccionar la ubicación de
		// guardado
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG files (*.jpg)", "*.jpg"));
		fileChooser.setInitialFileName(nombreFichero); // Nombre inicial del archivo

		// Mostrar el cuadro de diálogo de guardado y obtener la ubicación seleccionada
		File file = fileChooser.showSaveDialog(stage);

		// Verificar si el usuario seleccionó una ubicación válida
		if (file != null) {
			// Verificar si el archivo ya existe
			if (file.exists()) {
				// Si el archivo existe, encontrar un nombre único agregando un número entre
				// paréntesis
				int i = 1;
				String nombreBase = nombreFichero.substring(0, nombreFichero.lastIndexOf('.'));
				String extension = nombreFichero.substring(nombreFichero.lastIndexOf('.'));
				do {
					nombreFichero = nombreBase + "(" + i + ")" + extension;
					file = new File(file.getParent(), nombreFichero);
					i++;
				} while (file.exists());
			}

			try {
				// Copiar el archivo de la ubicación dada al lugar seleccionado por el usuario
				Files.copy(Paths.get(filePath), file.toPath());
			} catch (IOException e) {
				Utilidades.manejarExcepcion(e);
			}
		}
	}

	/**
	 * Establece la instancia de la ventana (Stage) asociada a este controlador.
	 *
	 * @param stage La instancia de la ventana (Stage) que se asocia con este
	 *              controlador.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Cierra la ventana asociada a este controlador, si está disponible. Si no se
	 * ha establecido una instancia de ventana (Stage), este método no realiza
	 * ninguna acción.
	 */
	public void closeWindow() {
		if (stage != null) {
			stage.close();
		}
	}
}
