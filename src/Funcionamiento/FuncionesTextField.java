package Funcionamiento;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javafx.scene.control.TextField;
import javafx.util.Callback;

public class FuncionesTextField {

	private static final List<Character> simbolos = Arrays.asList(',', '-', '!', '@', '#', '$', '%', '^', '&', '*', '(',
			')', '[', ']', '{', '}', ';', ':', '|', '\\', '<', '>', '/', '?', '~', '`', '+', '=', '.');

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
	            if (simbolos.contains(lastChar) && (newValue.length() == 1 || !simbolos.contains(newValue.charAt(newValue.length() - 2)))) {
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