package controlUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import Controladores.managment.AccionReferencias;
import comicManagement.Comic;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FuncionesManejoFront {

	static ImageView imagenComic;

	public static AccionReferencias referenciaVentana = new AccionReferencias();
	
	public static List<Stage> stageVentanas = new ArrayList<Stage>();

	private static final List<Character> simbolos = Arrays.asList(',', '-', '!', '@', '#', '$', '%', '^', '&', '*', '(',
			')', '[', ']', '{', '}', ';', ':', '|', '\\', '<', '>', '/', '?', '~', '`', '+', '=', '.');

	public static void establecerFondoDinamico() {
		for (Node elemento : referenciaVentana.getListaElementosFondo()) {
			if (elemento instanceof ImageView || elemento instanceof TableView || elemento instanceof AnchorPane) {
				if (elemento instanceof ImageView) {
					((ImageView) elemento).fitWidthProperty()
							.bind(referenciaVentana.getRootAnchorPane().widthProperty());
					((ImageView) elemento).fitHeightProperty()
							.bind(referenciaVentana.getRootAnchorPane().heightProperty());
				} else if (elemento instanceof TableView) {
					((TableView<?>) elemento).prefWidthProperty()
							.bind(referenciaVentana.getRootAnchorPane().widthProperty());
				} else if (elemento instanceof AnchorPane) {
					((AnchorPane) elemento).prefWidthProperty()
							.bind(referenciaVentana.getRootAnchorPane().widthProperty());
				}
			}
		}
	}

	public static void establecerAnchoColumnas(double numColumns) {
		for (TableColumn<Comic, String> columna : referenciaVentana.getColumnasTabla()) {
			columna.prefWidthProperty().bind(referenciaVentana.getTablaBBDD().widthProperty().divide(numColumns));
		}
	}

	public static void establecerAnchoMaximoBotones(double maxButtonWidth) {
		for (Button boton : AccionReferencias.getListaBotones()) {
			boton.maxWidthProperty().bind(Bindings.max(maxButtonWidth, boton.widthProperty()));
		}
	}

	public static void establecerAnchoMaximoCamposTexto(double maxTextComboWidth) {
		for (Control campo : referenciaVentana.getListaCamposTexto()) {
			if (campo instanceof TextField) {
				TextField campoTexto = (TextField) campo;
				Platform.runLater(() -> campoTexto.maxWidthProperty()
						.bind(Bindings.max(maxTextComboWidth, campoTexto.widthProperty())));
			}
		}
	}

	public static void establecerAnchoMaximoComboBoxes(double maxTextComboWidth) {
		for (ComboBox<?> comboBox : referenciaVentana.getComboboxes()) {
			comboBox.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, comboBox.widthProperty()));
		}
	}

	public static void establecerTamanioMaximoImagen(double maxWidth, double maxHeight) {
		referenciaVentana.getImagencomic().fitWidthProperty()
				.bind(Bindings.min(maxWidth, referenciaVentana.getRootAnchorPane().widthProperty()));
		referenciaVentana.getImagencomic().fitHeightProperty()
				.bind(Bindings.min(maxHeight, referenciaVentana.getRootAnchorPane().heightProperty()));
		referenciaVentana.getImagencomic().setPreserveRatio(true);
	}

	////////////////////////////////////////////////////
	////// FUNCIONES TEXTFIELD///////////////////////////
	////////////////////////////////////////////////////

	public static void asignarAutocompletado(TextField textField, List<String> listaCompleta) {

		if (textField != null) {

			final String[] textBeforeLastComma = { "" };
			final String[] textAfterLastComma = { "" };
			final int[] lastCommaIndex = { 0 };

			TextFields.bindAutoCompletion(textField,
					new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
						@Override
						public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
							String userText = textField.getText();
							lastCommaIndex[0] = userText.lastIndexOf(',');
							if (lastCommaIndex[0] != -1) {
								textBeforeLastComma[0] = userText.substring(0, lastCommaIndex[0] + 1);
								textAfterLastComma[0] = userText.substring(lastCommaIndex[0] + 1).trim();
							} else {
								textBeforeLastComma[0] = "";
								textAfterLastComma[0] = userText;
							}
							Collection<String> filteredList = listaCompleta.stream()
									.filter(item -> item.toLowerCase().startsWith(textAfterLastComma[0].toLowerCase()))
									.toList();

							return filteredList;
						}
					}).setOnAutoCompleted(event -> {

						String textoCompleto = textBeforeLastComma[0] + textField.getText();

						textField.setText(textoCompleto);

						event.consume();
					});
		}
	}

	/**
	 * Elimina un espacio en blanco al principio del texto en un TextField si
	 * existe.
	 *
	 * @param textField El TextField al que se aplicará la eliminación del espacio
	 *                  en blanco inicial.
	 * @return El mismo TextField después de la modificación.
	 */
	public static void eliminarEspacioInicial(TextField textField) {

		if (textField != null) {

			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue != null && !newValue.isEmpty()) {
					char firstChar = newValue.charAt(0);
					// Si el primer carácter es un espacio en blanco o un símbolo
					if (Character.isWhitespace(firstChar) || simbolos.contains(firstChar)) {
						// Eliminar el espacio en blanco inicial o el símbolo
						textField.setText(newValue.substring(1));
					}
				}
			});
		}
	}

	public static void permitirUnSimbolo(TextField textField) {

		if (textField != null) {
			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue != null && !newValue.isEmpty()) {
					char lastChar = newValue.charAt(newValue.length() - 1);
					// Si el último carácter es un símbolo y también el anterior no lo es
					if (simbolos.contains(lastChar)
							&& (newValue.length() == 1 || !simbolos.contains(newValue.charAt(newValue.length() - 2)))) {
						// No hacer nada
					} else if (simbolos.contains(lastChar)) {
						// Eliminar el último carácter ingresado si es un símbolo
						textField.setText(oldValue);
					}
				}
			});
		}
	}

	/**
	 * Restringe los símbolos no permitidos en el TextField y muestra un Tooltip
	 * informativo.
	 *
	 * @param textField El TextField en el cual restringir los símbolos.
	 */
	public static void restringirSimbolos(TextField textField) {

		if (textField != null) {

			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				String allowedPattern = "[\\p{L}\\p{N}\\s,.!-]*"; // Expresión regular para permitir letras, números,
																	// espacios, ",", "-" y "."

				if (newValue != null) {

					// Elimina espacios al principio de la cadena.
					newValue = newValue.trim();

					if (!newValue.matches(allowedPattern)) {
						// Si el valor no coincide con el patrón permitido, restaura el valor anterior.
						textField.setText(oldValue);
					} else {
						String updatedValue = newValue.replaceAll("\\s*(?<![,-])(?=[,-])|(?<=[,-])\\s*", "");

						if (!updatedValue.equals(newValue)) {
							textField.setText(updatedValue);
						}
					}
				}
			});
		}
	}

	/**
	 * Reemplaza múltiples espacios seguidos por un solo espacio en un TextField.
	 *
	 * @param textField El TextField al que se aplicará la eliminación de espacios
	 *                  múltiples.
	 */
	public static void reemplazarEspaciosMultiples(TextField textField) {

		if (textField != null) {

			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue != null) {
					// Reemplaza múltiples espacios seguidos por un solo espacio.
					newValue = newValue.replaceAll("\\s+", " ");

					textField.setText(newValue); // Actualiza el valor del TextField
				}
			});

		}
	}

	/**
	 * Elimina espacios de un TextField.
	 *
	 * @param textField El TextField al que se aplicará la eliminación de espacios
	 *                  múltiples.
	 */
	public static void reemplazarEspacio(TextField textField) {

		if (textField != null) {

			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue != null) {
					// Reemplaza múltiples espacios seguidos por un solo espacio.
					newValue = newValue.replaceAll(" ", "");

					textField.setText(newValue); // Actualiza el valor del TextField
				}
			});

		}
	}

	public static void cambiarEstadoMenuBar(boolean estadoAccion) {

		referenciaVentana.getMenu_archivo_excel().setDisable(estadoAccion);
		referenciaVentana.getMenu_archivo_importar().setDisable(estadoAccion);
		referenciaVentana.getMenu_archivo_delete().setDisable(estadoAccion);
		referenciaVentana.getMenu_comic_aniadir().setDisable(estadoAccion);
		referenciaVentana.getMenu_comic_eliminar().setDisable(estadoAccion);
		referenciaVentana.getMenu_comic_modificar().setDisable(estadoAccion);
		referenciaVentana.getMenu_comic_puntuar().setDisable(estadoAccion);
		referenciaVentana.getMenu_comic_aleatoria().setDisable(estadoAccion);
		referenciaVentana.getBotonIntroducir().setDisable(estadoAccion);
		referenciaVentana.getBotonModificar().setDisable(estadoAccion);
		referenciaVentana.getBotonEliminar().setDisable(estadoAccion);
		referenciaVentana.getBotonAgregarPuntuacion().setDisable(estadoAccion);
		referenciaVentana.getBotonLimpiar().setDisable(estadoAccion);
		referenciaVentana.getBotonMostrarParametro().setDisable(estadoAccion);
		referenciaVentana.getBotonImprimir().setDisable(estadoAccion);
		referenciaVentana.getBotonGuardarResultado().setDisable(estadoAccion);
		referenciaVentana.getBotonbbdd().setDisable(estadoAccion);

		referenciaVentana.getTituloComic().setDisable(estadoAccion);
		referenciaVentana.getNombreDibujante().setDisable(estadoAccion);
		referenciaVentana.getNombreEditorial().setDisable(estadoAccion);
		referenciaVentana.getNombreFirma().setDisable(estadoAccion);
		referenciaVentana.getNombreFormato().setDisable(estadoAccion);
		referenciaVentana.getNombreGuionista().setDisable(estadoAccion);
		referenciaVentana.getNombreProcedencia().setDisable(estadoAccion);
		referenciaVentana.getNombreVariante().setDisable(estadoAccion);
		referenciaVentana.getNumeroCaja().setDisable(estadoAccion);
		referenciaVentana.getNumeroComic().setDisable(estadoAccion);
	}

}