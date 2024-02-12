package Funcionamiento;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import comicManagement.Comic;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class FuncionesManejoFront {

	/**
	 * Panel de anclaje principal.
	 */
	public static AnchorPane rootAnchorPane;

	/**
	 * Tabla que muestra información sobre cómics.
	 */
	public static TableView<Comic> tablaBBDD;

	public void setTableView(TableView<Comic> tablaComic) {
		tablaBBDD = tablaComic;
	}

	public void setAnchorPane(AnchorPane anchorPane) {
		rootAnchorPane = anchorPane;
	}

	public void setImagenes(ObservableList<ImageView> imagenes) {
		rootAnchorPane.getChildren().addAll(imagenes);
	}

	public void setComboBoxes(ObservableList<ComboBox<String>> comboBoxes) {
		rootAnchorPane.getChildren().addAll(comboBoxes);
	}

	public void setCamposTexto(ObservableList<TextField> camposTexto) {
		rootAnchorPane.getChildren().addAll(camposTexto);
	}

	public void setBotones(ObservableList<Button> botones) {
		rootAnchorPane.getChildren().addAll(botones);
	}

	public void setColumnas(ObservableList<TableColumn<Comic, ?>> columnas) {
		tablaBBDD.getColumns().addAll(columnas);
	}

	public void setElementosFondo(ObservableList<Node> elementosFondo) {
		rootAnchorPane.getChildren().addAll(elementosFondo);
	}

	private static final List<Character> simbolos = Arrays.asList(',', '-', '!', '@', '#', '$', '%', '^', '&', '*', '(',
			')', '[', ']', '{', '}', ';', ':', '|', '\\', '<', '>', '/', '?', '~', '`', '+', '=', '.');

	public static void establecerFondoDinamico(ObservableList<Node> elementos) {
		for (Node elemento : elementos) {
			if (elemento instanceof ImageView || elemento instanceof TableView || elemento instanceof AnchorPane) {
				if (elemento instanceof ImageView) {
					((ImageView) elemento).fitWidthProperty().bind(rootAnchorPane.widthProperty());
					((ImageView) elemento).fitHeightProperty().bind(rootAnchorPane.heightProperty());
				} else if (elemento instanceof TableView) {
					((TableView<?>) elemento).prefWidthProperty().bind(rootAnchorPane.widthProperty());
				} else if (elemento instanceof AnchorPane) {
					((AnchorPane) elemento).prefWidthProperty().bind(rootAnchorPane.widthProperty());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void establecerAnchoColumnas(@SuppressWarnings("rawtypes") ObservableList<TableColumn> columnas,
			double numColumns) {
		for (TableColumn<Comic, ?> columna : columnas) {
			columna.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		}
	}

	public static void establecerAnchoMaximoBotones(ObservableList<Button> botones, double maxButtonWidth) {
		for (Button boton : botones) {
			boton.maxWidthProperty().bind(Bindings.max(maxButtonWidth, boton.widthProperty()));
		}
	}

	public static void establecerAnchoMaximoCamposTexto(ObservableList<Control> camposTexto2,
			double maxTextComboWidth) {
		for (Control campo : camposTexto2) {
			if (campo instanceof TextField) {
				TextField campoTexto = (TextField) campo;
				Platform.runLater(() -> campoTexto.maxWidthProperty()
						.bind(Bindings.max(maxTextComboWidth, campoTexto.widthProperty())));
			}
		}
	}

	public static void establecerAnchoMaximoComboBoxes(
			@SuppressWarnings("rawtypes") ObservableList<ComboBox> comboBoxes, double maxTextComboWidth) {
		for (ComboBox<?> comboBox : comboBoxes) {
			comboBox.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, comboBox.widthProperty()));
		}
	}

	public static void establecerTamanioMaximoImagen(ObservableList<ImageView> imagenes, double maxWidth,
			double maxHeight) {
		for (ImageView imagen : imagenes) {
			imagen.fitWidthProperty().bind(Bindings.min(maxWidth, rootAnchorPane.widthProperty()));
			imagen.fitHeightProperty().bind(Bindings.min(maxHeight, rootAnchorPane.heightProperty()));
			imagen.setPreserveRatio(true);
		}
	}

	////////////////////////////////////////////////////
	////// FUNCIONES TEXTFIELD///////////////////////////
	////////////////////////////////////////////////////

	public static void asignarAutocompletado(TextField textField, List<String> listaCompleta) {

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

	/**
	 * Elimina un espacio en blanco al principio del texto en un TextField si
	 * existe.
	 *
	 * @param textField El TextField al que se aplicará la eliminación del espacio
	 *                  en blanco inicial.
	 * @return El mismo TextField después de la modificación.
	 */
	public static TextField eliminarEspacioInicial(TextField textField) {
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
		return textField;
	}

	public static TextField permitirUnSimbolo(TextField textField) {
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
		return textField;
	}

	/**
	 * Restringe los símbolos no permitidos en el TextField y muestra un Tooltip
	 * informativo.
	 *
	 * @param textField El TextField en el cual restringir los símbolos.
	 */
	public static void restringirSimbolos(TextField textField) {

		final TextField finalTextField = eliminarEspacioInicial(textField);

		finalTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			String allowedPattern = "[\\p{L}\\p{N}\\s,.!-]*"; // Expresión regular para permitir letras, números,
																// espacios, ",", "-" y "."

			if (newValue != null) {

				// Elimina espacios al principio de la cadena.
				newValue = newValue.trim();

				if (!newValue.matches(allowedPattern)) {
					// Si el valor no coincide con el patrón permitido, restaura el valor anterior.
					finalTextField.setText(oldValue);
				} else {
					String updatedValue = newValue.replaceAll("\\s*(?<![,-])(?=[,-])|(?<=[,-])\\s*", "");

					if (!updatedValue.equals(newValue)) {
						finalTextField.setText(updatedValue);
					}
				}
			}
		});
	}

	/**
	 * Reemplaza múltiples espacios seguidos por un solo espacio en un TextField.
	 *
	 * @param textField El TextField al que se aplicará la eliminación de espacios
	 *                  múltiples.
	 */
	public static void reemplazarEspaciosMultiples(TextField textField) {
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				// Reemplaza múltiples espacios seguidos por un solo espacio.
				newValue = newValue.replaceAll("\\s+", " ");

				textField.setText(newValue); // Actualiza el valor del TextField
			}
		});
	}

	/**
	 * Elimina espacios de un TextField.
	 *
	 * @param textField El TextField al que se aplicará la eliminación de espacios
	 *                  múltiples.
	 */
	public static void reemplazarEspacio(TextField textField) {
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				// Reemplaza múltiples espacios seguidos por un solo espacio.
				newValue = newValue.replaceAll(" ", "");

				textField.setText(newValue); // Actualiza el valor del TextField
			}
		});
	}

}
