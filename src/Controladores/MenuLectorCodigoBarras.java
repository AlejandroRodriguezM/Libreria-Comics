package Controladores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Funcionamiento.Utilidades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

	@FXML
	private TextField campoCodigoTexto;

	@FXML
	private Button botonIntroducirCodigo;

	/**
	 * Lista que guarda los codigos que recibe del scaner de codigo de barras
	 */
	private List<String> listaCodigosBarras = new ArrayList<>();

	/**
	 * Variable global que recibe el codigo escaneado completo
	 */
	private String codigoEscaneado = "";

	/**
	 * Campo para almacenar la referencia a la ventana (Stage).
	 */
	private Stage stage; // Campo para almacenar la referencia a la ventana

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
				File archivo = Utilidades.tratarFichero(frase, formato).showSaveDialog(null);

				// Verifica si se seleccionó un archivo
				if (archivo != null) {
					try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
						// Itera sobre la lista de códigos y escribe cada código en una nueva línea
						System.out.println("Tamaño de la lista de códigos de barras: " + listaCodigosBarras.size());
						for (String codigo : listaCodigosBarras) {
							escritor.write(codigo);
						}
					}
				} else {

					System.out.println("No se seleccionó ningún archivo");
				}
			} catch (IOException e) {
				e.printStackTrace(); // Manejo de errores, puedes personalizar según tus necesidades.
			}
		} else {
			System.out.println("La lista de códigos de barras está vacía");
		}
		((Button) event.getSource()).getScene().getWindow().getScene().getRoot().requestFocus();
	}

	/**
	 * Maneja los eventos de teclado, detectando la entrada del código de barras y
	 * llamando a procesarCodigoBarras.
	 *
	 * @param event Evento de teclado.
	 */
	@FXML
	void manejarEventoTeclado(KeyEvent event) {

		// Obtener el código de la tecla presionada
		KeyCode keyCode = event.getCode();

		// Verificar si la tecla presionada es un dígito o ENTER
		if (keyCode.isDigitKey() || keyCode == KeyCode.ENTER) {

			// Obtener el texto del evento (suponiendo que es el código de barras)
			String codigoLeido = event.getText();

			// Concatenar el código escaneado al String
			codigoEscaneado += codigoLeido;

			// Verificar si se presionó la tecla ENTER
			if (keyCode == KeyCode.ENTER) {
				// Verificar la validez del código de barras antes de procesarlo
				if (esCodigoBarrasValido(codigoEscaneado)) {

					agregarCodigoBarras(codigoEscaneado);

				} else {
					// Manejar caso de código de barras no válido
					System.out.println("Código de barras no válido: " + codigoEscaneado);
				}

				// Limpiar el campo de texto después de procesar el código de barras
				barcodeField.clear();

				// Reiniciar el código escaneado para el próximo código de barras
				codigoEscaneado = "";
			}
		}
	}

	/**
	 * Verifica la validez de un código de barras.
	 *
	 * @param codigoBarras Código de barras a verificar.
	 * @return true si el código de barras es válido, false de lo contrario.
	 */
	private boolean esCodigoBarrasValido(String codigoBarras) {
		// Agrega la lógica para validar el código de barras según tus requisitos
		// Por ejemplo, puedes verificar la longitud, el formato, etc.
		return codigoBarras.length() >= 8; // Ejemplo: Considerar válido si tiene al menos 8 caracteres
	}

	/**
	 * Agrega un código de barras a la lista y actualiza el TextArea.
	 *
	 * @param codigoBarras El código de barras a agregar.
	 */
	private void agregarCodigoBarras(String codigoBarras) {
		listaCodigosBarras.add(codigoBarras);
		actualizarTextArea();
	}

	/**
	 * Maneja el evento de tecla cuando se introduce manualmente un código. Si la
	 * tecla presionada es ENTER y el campo de código no está vacío, agrega el
	 * código de barras.
	 *
	 * @param event El evento de tecla asociado.
	 */
	@FXML
	void introducirCodigoManualmente(KeyEvent event) {

		String codigo = campoCodigoTexto.getText();
		KeyCode keyCode = event.getCode();

		if (esCodigoValido(codigo) && keyCode == KeyCode.ENTER) {
			agregarCodigoBarras(codigo);
		}

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

		if (esCodigoValido(codigo)) {
			agregarCodigoBarras(codigo);
		}
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
	
	/**
	 * Verifica si el código es válido, es decir, contiene solo números y letras y tiene una longitud mayor a 0.
	 *
	 * @param codigo El código a validar.
	 * @return true si el código es válido, false de lo contrario.
	 */
	private boolean esCodigoValido(String codigo) {
	    return codigo.length() > 0 && codigo.matches("[a-zA-Z0-9]+");
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
			stage.close();
		}
	}

}