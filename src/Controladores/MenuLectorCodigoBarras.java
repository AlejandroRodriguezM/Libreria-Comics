package Controladores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dbmanager.ConectManager;
import funcionesAuxiliares.Utilidades;
import funcionesInterfaz.FuncionesManejoFront;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controlador para la interfaz de usuario del lector de códigos de barras.
 */
public class MenuLectorCodigoBarras {

	@FXML
	private TextField barcodeField;

	/**
	 * Botón asociado a la acción de borrar todas las entradas de códigos de barras.
	 */
	@FXML
	private Button botonBorrarTodo;

	/**
	 * Botón asociado a la acción de descargar un archivo de texto (.txt) con los
	 * códigos de barras.
	 */
	@FXML
	private Button botonDescargar;

	/**
	 * Botón asociado a una acción que aún no se ha definido (puedes añadir la
	 * lógica según tus necesidades).
	 */
	@FXML
	private Button botonDeshacer;

	/**
	 * Área de texto utilizada para mostrar y editar los códigos de barras.
	 */
	@FXML
	private TextArea codigoBarrasTextArea;

	/** Campo de texto donde se introduce el código de barras. */
	@FXML
	private TextField campoCodigoTexto;

	/** Botón utilizado para introducir el código de barras. */
	@FXML
	private Button botonIntroducirCodigo;

	/** Pane principal que contiene los elementos de la interfaz gráfica. */
	@FXML
	private Pane contenedorPrincipal;

	/**
	 * Lista que guarda los codigos que recibe del scaner de codigo de barras
	 */
	private List<String> listaCodigosBarras = new ArrayList<>();

	/**
	 * Campo para almacenar la referencia a la ventana (Stage).
	 */
	private Stage stage; // Campo para almacenar la referencia a la ventana

	private String codigoEscaneado = "";

	private static final long UMBRAL_TIEMPO_ENTRE_TECLAS = 100; // Ajusta según sea necesario
	private long ultimoTiempo = 0;

	/**
	 * Inicializa la lógica y los eventos al cargar la interfaz gráfica.
	 */
	@FXML
	void initialize() {
		// Agrega un evento para quitar el foco cuando se hace clic en el contenedor
		// principal

//		Platform.runLater(() -> ConectManager.startCheckerTimer(miStageVentana()));

		contenedorPrincipal.setOnMouseClicked(event -> {
			campoCodigoTexto.getParent().requestFocus();
		});

		campoCodigoTexto.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				long tiempoActual = System.currentTimeMillis();
				long tiempoDiferencia = tiempoActual - ultimoTiempo;

				if (tiempoDiferencia > UMBRAL_TIEMPO_ENTRE_TECLAS) {
					codigoEscaneado = campoCodigoTexto.getText();
					campoCodigoTexto.clear();
				}

				// Actualizar el último tiempo
				ultimoTiempo = tiempoActual;
				agregarCodigoBarras(codigoEscaneado);
				// Consumir el evento para evitar que se propague más allá de este punto
				event.consume();
			}
		});
		Platform.runLater(() -> {
			FuncionesManejoFront.getStageVentanas().add(estadoStage());
		});
	}

	/**
	 * Borra todas las entradas de la lista de códigos de barras y actualiza el
	 * TextArea. Si la lista está vacía, no realiza ninguna acción.
	 *
	 * @param event Evento de acción asociado al botón.
	 */
	@FXML
	void borrarTodasEntradas(ActionEvent event) {
		listaCodigosBarras.clear(); // Limpiar la lista de códigos de barras
		actualizarTextArea();
		((Button) event.getSource()).getScene().getWindow().getScene().getRoot().requestFocus();
		event.consume();
	}

	/**
	 * Borra la última entrada de la lista de códigos de barras y actualiza el
	 * TextArea. Si la lista está vacía, no realiza ninguna acción.
	 *
	 * @param event Evento de acción asociado al botón.
	 */
	@FXML
	void borrarUltimaEntrada(ActionEvent event) {
		if (!listaCodigosBarras.isEmpty()) {
			listaCodigosBarras.remove(listaCodigosBarras.size() - 1);
			actualizarTextArea();

		}
		((Button) event.getSource()).getScene().getWindow().getScene().getRoot().requestFocus();
		event.consume();
	}

	/**
	 * Descarga un archivo de texto (.txt) con el contenido de la lista de códigos
	 * de barras. Si la lista está vacía, no realiza ninguna acción. En caso
	 * contrario, permite al usuario elegir la ubicación y el nombre del archivo.
	 *
	 * @param evento Evento de acción asociado al botón.
	 */
	@FXML
	void descargarFichero(ActionEvent event) {
		if (!listaCodigosBarras.isEmpty()) {
			try {
				String frase = "Fichero Codigos";
				String formato = "*.txt";

				// Muestra el diálogo para seleccionar la ubicación del archivo
				File archivo = Utilidades.tratarFichero(frase, formato, false);

				// Verifica si se seleccionó un archivo
				if (archivo != null) {
					try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
						// Itera sobre la lista de códigos y escribe cada código en una nueva línea
						for (String codigo : listaCodigosBarras) {
							escritor.write(codigo);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace(); // Manejo de errores, puedes personalizar según tus necesidades.
			}
		}
		((Button) event.getSource()).getScene().getWindow().getScene().getRoot().requestFocus();
		event.consume();
	}

	/**
	 * Maneja el evento de teclado.
	 *
	 * @param event El evento de teclado.
	 */
	@FXML
	void manejarEventoTeclado(KeyEvent event) {
		// Obtener el código de la tecla presionada
		KeyCode keyCode = event.getCode();

		// Verificar si la tecla presionada es un dígito o ENTER
		if (keyCode.isDigitKey() && !campoCodigoTexto.isFocused()
				|| keyCode == KeyCode.ENTER && !campoCodigoTexto.isFocused()) {

			campoCodigoTexto.setText("");

			// Obtener el texto del evento (suponiendo que es el código de barras)
			String codigoLeido = event.getText();

			// Concatenar el código escaneado al String
			codigoEscaneado += codigoLeido;

			// Verificar si se presionó la tecla ENTER
			if (keyCode == KeyCode.ENTER) {
				// Verificar la validez del código de barras antes de procesarlo
				if (Utilidades.esCodigoValido(codigoEscaneado.trim())) {
					agregarCodigoBarras(codigoEscaneado);
				}

				// Reiniciar el código escaneado para el próximo código de barras
				codigoEscaneado = "";
			}

			// Consumir el evento para indicar que ha sido manejado
			event.consume();
		}
	}

	/**
	 * Agrega un código de barras a la lista y actualiza el TextArea.
	 *
	 * @param codigoBarras El código de barras a agregar.
	 */
	private void agregarCodigoBarras(String codigoBarras) {
		listaCodigosBarras.add(codigoBarras);

		actualizarTextArea();
		barcodeField.setText("");
	}

	/**
	 * Maneja el evento de acción cuando se introduce un código desde un botón. Si
	 * el campo de código no está vacío, agrega el código de barras.
	 *
	 * @param event El evento de acción asociado.
	 */
	@FXML
	void introducirCodigoDesdeBoton(ActionEvent event) {
		String codigo = campoCodigoTexto.getText();
		if (Utilidades.esCodigoValido(codigo)) {
			agregarCodigoBarras(codigo);
			campoCodigoTexto.setText("");
		}
		event.consume();
	}

	/**
	 * Actualiza el contenido del TextArea con los códigos de barras de la lista.
	 */
	private void actualizarTextArea() {
		StringBuilder contenidoTextArea = new StringBuilder();
		for (String codigo : listaCodigosBarras) {
			contenidoTextArea.append(codigo).append("\n");
		}
		codigoBarrasTextArea.setText(contenidoTextArea.toString());

	}

	public Scene miStageVentana() {
		Node rootNode = botonIntroducirCodigo;
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

	public Stage estadoStage() {

		return (Stage) botonIntroducirCodigo.getScene().getWindow();
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
	 * Cierra la ventana actual si está abierta.
	 */
	public void closeWindow() {
		if (stage != null) {

			if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
				FuncionesManejoFront.getStageVentanas().remove(estadoStage());
			}

			stage.close();
		}
	}

}