package funcionesInterfaz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.controlsfx.control.textfield.TextFields;

import alarmas.AlarmaList;
import comicManagement.Comic;
import funcionesAuxiliares.Utilidades;
import funcionesManagment.AccionReferencias;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FuncionesManejoFront {

	public static ImageView imagenComic;

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static List<Stage> stageVentanas = new ArrayList<>();

	private static final List<Character> simbolos = Arrays.asList(',', '-', '!', '@', '#', '$', '%', '^', '&', '*', '(',
			')', '[', ']', '{', '}', ';', ':', '|', '\\', '<', '>', '/', '?', '~', '`', '+', '=', '.');

	public static void establecerFondoDinamico() {
		for (Node elemento : AccionReferencias.getListaElementosFondo()) {
			if (elemento instanceof ImageView imageview) {
				imageview.fitWidthProperty().bind(referenciaVentana.getRootAnchorPane().widthProperty());
				imageview.fitHeightProperty().bind(referenciaVentana.getRootAnchorPane().heightProperty());
			} else if (elemento instanceof TableView<?> tableview) {
				tableview.prefWidthProperty().bind(referenciaVentana.getRootAnchorPane().widthProperty());
			} else if (elemento instanceof AnchorPane anchorpane) {
				anchorpane.prefWidthProperty().bind(referenciaVentana.getRootAnchorPane().widthProperty());
			}
		}
	}

	public static void establecerAnchoColumnas(double numColumns) {
		for (TableColumn<Comic, String> columna : AccionReferencias.getListaColumnasTabla()) {
			columna.prefWidthProperty().bind(referenciaVentana.getTablaBBDD().widthProperty().divide(numColumns));
		}
	}

	public static void establecerAnchoMaximoBotones(double maxButtonWidth) {
		for (Button boton : AccionReferencias.getListaBotones()) {
			boton.maxWidthProperty().bind(Bindings.max(maxButtonWidth, boton.widthProperty()));
		}
	}

	public static void establecerAnchoMaximoCamposTexto(double maxTextComboWidth) {
		for (Control campo : AccionReferencias.getListaComboboxes()) {
			if (campo instanceof TextField campoTexto) {
				Platform.runLater(() -> campoTexto.maxWidthProperty()
						.bind(Bindings.max(maxTextComboWidth, campoTexto.widthProperty())));
			}
		}
	}

	public static void establecerAnchoMaximoComboBoxes(double maxTextComboWidth) {
		for (ComboBox<String> comboBox : AccionReferencias.getListaComboboxes()) {

			comboBox.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, comboBox.widthProperty()));
		}
	}

	public static void establecerTamanioMaximoImagen(double maxWidth, double maxHeight) {
		referenciaVentana.getImagenComic().fitWidthProperty()
				.bind(Bindings.min(maxWidth, referenciaVentana.getRootAnchorPane().widthProperty()));
		referenciaVentana.getImagenComic().fitHeightProperty()
				.bind(Bindings.min(maxHeight, referenciaVentana.getRootAnchorPane().heightProperty()));
		referenciaVentana.getImagenComic().setPreserveRatio(false);

	}

	////////////////////////////////////////////////////
	////// FUNCIONES TEXTFIELD///////////////////////////
	////////////////////////////////////////////////////

	public static void asignarAutocompletado(TextField textField, List<String> listaCompleta) {

		if (textField != null) {

			final String[] textBeforeLastComma = { "" };
			final String[] textAfterLastComma = { "" };
			final int[] lastCommaIndex = { 0 };

			TextFields.bindAutoCompletion(textField, param -> {
				String userText = textField.getText();
				lastCommaIndex[0] = userText.lastIndexOf(',');
				if (lastCommaIndex[0] != -1) {
					textBeforeLastComma[0] = userText.substring(0, lastCommaIndex[0] + 1);
					textAfterLastComma[0] = userText.substring(lastCommaIndex[0] + 1).trim();
				} else {
					textBeforeLastComma[0] = "";
					textAfterLastComma[0] = userText;
				}
				return listaCompleta.stream()
						.filter(item -> item.toLowerCase().startsWith(textAfterLastComma[0].toLowerCase())).toList();
			}).setOnAutoCompleted(event -> {
				String textoCompleto = textBeforeLastComma[0] + textField.getText();
				textField.setText(textoCompleto);
				event.consume();
			});
		}
	}

	public static void eliminarEspacioInicialYFinal(TextField textField) {

		if (textField != null) {

			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue != null && !newValue.isEmpty()) {
					// Eliminar espacio en blanco inicial
					char firstChar = newValue.charAt(0);
					if (Character.isWhitespace(firstChar) || simbolos.contains(firstChar)) {
						newValue = newValue.substring(1);
					}

					// Eliminar espacio en blanco final
					char lastChar = newValue.charAt(newValue.length() - 1);
					if (Character.isWhitespace(lastChar)) {
						newValue = newValue.substring(0, newValue.length() - 1);
					}

					// Actualizar el valor del campo de texto
					textField.setText(newValue);
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
						newValue = newValue.replace(";", "");
						textField.setText(oldValue);
					}
				}
			});
		}
	}

	// Conjunto de símbolos permitidos
	private static final Set<Character> simbolosPermitidos = new HashSet<>();
	static {
		simbolosPermitidos.add('$');
		simbolosPermitidos.add('€');
	}

	public static void permitirSimbolosEspecificos(TextField textField) {
		if (textField != null) {
			textField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue != null) {
						StringBuilder filteredValue = new StringBuilder();
						boolean puntoPermitido = true; // Para controlar que solo haya un punto
						boolean simboloPermitido = true; // Para controlar que solo haya un símbolo

						// Recorrer cada caracter del nuevo valor
						for (int i = 0; i < newValue.length(); i++) {
							char c = newValue.charAt(i);

							if (simbolosPermitidos.contains(c) && simboloPermitido) {
								// Si es un símbolo permitido y aún no se ha agregado uno, añadirlo al valor
								// filtrado
								filteredValue.append(c);
								simboloPermitido = false; // Marcar que ya se añadió un símbolo
								puntoPermitido = true; // Reiniciar la bandera de punto permitido
							} else if (Character.isDigit(c)) {
								// Si es un dígito, añadirlo al valor filtrado
								filteredValue.append(c);
								simboloPermitido = true; // Reiniciar la bandera de símbolo permitido
							} else if (c == '.' && puntoPermitido) {
								// Si es un punto y aún no se ha agregado uno, añadirlo al valor filtrado
								filteredValue.append(c);
								puntoPermitido = false; // Marcar que ya se añadió un punto
								simboloPermitido = true; // Reiniciar la bandera de símbolo permitido
							}
						}

						// Establecer el texto filtrado en el TextField
						textField.setText(filteredValue.toString());
					}
				}
			});
		}
	}

	public static void restringirSimbolos(TextField textField) {
		if (textField != null) {
			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				String allowedPattern = "[\\p{L}\\p{N}\\s!`´\"\\-]*"; // Permitimos solo letras, números, espacios,
																		// signos específicos y acentos

				if (newValue != null) {
					// Elimina espacios al principio y al final de la cadena.
					newValue = newValue.trim();
					newValue = newValue.replace(";", "");
					// Remueve caracteres no permitidos según allowedPattern
					newValue = newValue.replaceAll("[^\\p{L}\\p{N}\\s!`´\"\\-,.'\"]", "");

					// Reemplazar letras con acentos por sus equivalentes sin acento
					String updatedValue = removeAccents(newValue);

					if (!updatedValue.equals(newValue)) {
						textField.setText(updatedValue);
					}

					if (!updatedValue.matches(allowedPattern)) {
						// Si el valor no coincide con el patrón permitido, restaura el valor anterior.
						textField.setText(oldValue);
					}
				}
			});
		}
	}

	public static void restringirSimboloClave(Object control) {
		if (control instanceof TextField) {
			TextField textField = (TextField) control;
			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue != null) {
					// Elimina espacios al principio y al final de la cadena.
					newValue = newValue.trim();

					// Remueve el punto y coma
					newValue = newValue.replace(";", "");

					// Establece el nuevo valor en el campo de texto
					if (!newValue.equals(textField.getText())) {
						textField.setText(newValue);
					}
				}
			});
		}
	}

	private static String removeAccents(String input) {
		return input.replaceAll("[áÁ]", "a").replaceAll("[éÉ]", "e").replaceAll("[íÍ]", "i").replaceAll("[óÓ]", "o")
				.replaceAll("[úÚ]", "u").replaceAll("[üÜ]", "u"); // Añadir más reemplazos según sea necesario
	}

	public static void eliminarSimbolosEspeciales(TextField textField) {
		if (textField != null) {
			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue != null) {
					// Patrón permitido: letras, números, espacios, comas, puntos, y otros
					// caracteres específicos
					String allowedPattern = "[\\p{L}\\p{N}\\s!`´\"\\-,.'@]*";
					newValue = newValue.replace(";", "");
					// Elimina los símbolos especiales ' " ! ? # @
					String cleanedValue = newValue.replaceAll("[\\'\"!\\?#@]", "");

					// Reemplaza ' seguido de números con -
					cleanedValue = cleanedValue.replaceAll("\\'(?=\\d)", "-");

					// Aplica el patrón permitido para mantener solo los caracteres válidos
					cleanedValue = cleanedValue.replaceAll("[^\\p{L}\\p{N}\\s!`´\"\\-,.'@]", "");

					// Reemplazar letras con acentos por sus equivalentes sin acento
					String updatedValue = removeAccents(cleanedValue);

					if (!updatedValue.equals(newValue)) {
						textField.setText(updatedValue);
					}

					if (!updatedValue.matches(allowedPattern)) {
						// Si el valor no coincide con el patrón permitido, restaura el valor anterior.
						textField.setText(oldValue);
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
	public static void reemplazarEspaciosMultiples(Control control) {
		if (control instanceof TextInputControl) {
			TextInputControl textInputControl = (TextInputControl) control;
			textInputControl.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue != null) {
						newValue = newValue.replace(";", "");
						// Reemplaza múltiples espacios seguidos por un solo espacio.
						String textoActualizado = newValue.replaceAll("\\s+", " ");

						// Actualiza el valor del Control
						textInputControl.setText(textoActualizado); // Asigna el texto actualizado
					}
				}
			});
		} else {
			throw new IllegalArgumentException("El control proporcionado no es un TextInputControl.");
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
					newValue = newValue.replace(" ", "");

					textField.setText(newValue); // Actualiza el valor del TextField
				}
			});

		}
	}

	public static void manejarMensajeTextArea(String mensaje) {
		AlarmaList.iniciarAnimacionTextArea(referenciaVentana.getProntInfoTextArea(), mensaje);
	}

	public static void cambiarEstadoMenuBar(boolean estadoAccion, AccionReferencias referenciaVentana) {

		if (referenciaVentana == null) {
			return;
		}

		disableMenuItems(estadoAccion, referenciaVentana.getMenuArchivoExcel(),
				referenciaVentana.getMenuArchivoImportar(), referenciaVentana.getMenuArchivoDelete(),
				referenciaVentana.getMenuComicAniadir(), referenciaVentana.getMenuComicModificar(),
				referenciaVentana.getMenuArchivoAvanzado(), referenciaVentana.getMenuImportarFicheroCodigoBarras(),
				referenciaVentana.getNavegacionEstadistica(), referenciaVentana.getMenuImportarFicheroCodigoBarras(),
				referenciaVentana.getMenuArchivoSobreMi(), referenciaVentana.getMenuArchivoDesconectar(),
				referenciaVentana.getMenuArchivoCerrar());

		disableButtons(estadoAccion, referenciaVentana.getBotonIntroducir(), referenciaVentana.getBotonModificar(),
				referenciaVentana.getBotonEliminar(), referenciaVentana.getBotonLimpiar(),
				referenciaVentana.getBotonMostrarParametro(), referenciaVentana.getBotonbbdd(),
				referenciaVentana.getBotonLimpiar(), referenciaVentana.getBotonBusquedaAvanzada(),
				referenciaVentana.getBotonLimpiar(), referenciaVentana.getBotonSubidaPortada(),
				referenciaVentana.getBotonGuardarComic(), referenciaVentana.getBotonGuardarCambioComic(),
				referenciaVentana.getBotonEliminarImportadoComic(), referenciaVentana.getBotonParametroComic(),
				referenciaVentana.getBotonbbdd(), referenciaVentana.getBotonBusquedaCodigo(),
				referenciaVentana.getBotonEliminarImportadoListaComic(),
				referenciaVentana.getBotonGuardarListaComics());

		disableControls(estadoAccion, referenciaVentana.getTituloComicCombobox(),
				referenciaVentana.getNumeroComicCombobox(), referenciaVentana.getNombreVarianteCombobox(),
				referenciaVentana.getNombreEditorCombobox(), referenciaVentana.getNombreTiendaCombobox(),
				referenciaVentana.getNombreArtistaCombobox(), referenciaVentana.getNombreGuionistaCombobox(),
				referenciaVentana.getNombreFirmaCombobox());

		disableTextFields(estadoAccion, referenciaVentana.getTituloComicTextField(), // Título del cómic
				referenciaVentana.getNombreEditorTextField(), // Nombre del editor
				referenciaVentana.getBusquedaGeneralTextField(), // Campo de búsqueda general
				referenciaVentana.getNumeroComicTextField(), // Número del cómic
				referenciaVentana.getCodigoComicTratarTextField(), // Código del cómic a tratar
				referenciaVentana.getDireccionImagenTextField(), // Dirección de la imagen
				referenciaVentana.getIdComicTratarTextField(), // ID del cómic a tratar
				referenciaVentana.getUrlReferenciaTextField(), // URL de referencia
				referenciaVentana.getCodigoComicTextField(), // Código del cómic
				referenciaVentana.getArtistaComicTextField(), // Artista del cómic
				referenciaVentana.getGuionistaComicTextField(), // Guionista del cómic
				referenciaVentana.getVarianteTextField(), // Variante del cómic
				referenciaVentana.getKeyComicData(), // Área de texto para comentarios clave
				referenciaVentana.getNombreEditorTextField(), // Nombre del editor (duplicado aquí, posiblemente un
																// error en el código anterior)
				referenciaVentana.getDataPickFechaP() // Año de publicación
		);

		if (referenciaVentana.getBotonModificar() != null) {
			// Limpiar elementos adicionales de la interfaz
			referenciaVentana.getBarraCambioAltura().setDisable(estadoAccion);
			referenciaVentana.getTablaBBDD().getItems().clear();
			referenciaVentana.getImagenComic().setImage(null);
			referenciaVentana.getTablaBBDD().refresh();
			referenciaVentana.getTablaBBDD().setDisable(estadoAccion);
		}

		if (referenciaVentana.getTituloComicTextField() != null) {
			List<Node> elementos = Arrays.asList(getReferenciaVentana().getBotonBusquedaCodigo(),
					getReferenciaVentana().getBusquedaCodigoTextField(),
					getReferenciaVentana().getNombreTiendaCombobox());
			Utilidades.cambiarVisibilidad(elementos, true);
		}

		clearAndRefreshTableView(referenciaVentana.getTablaBBDD());
	}

	private static void disableMenuItems(boolean estadoAccion, MenuItem... items) {
		for (MenuItem item : items) {
			if (item != null) {
				item.setDisable(estadoAccion);
			}
		}
	}

	private static void disableButtons(boolean estadoAccion, ButtonBase... buttons) {
		for (ButtonBase button : buttons) {
			if (button != null) {
				button.setDisable(estadoAccion);
			}
		}
	}

	private static void disableControls(boolean estadoAccion, Control... controls) {
		for (Control control : controls) {
			if (control != null) {
				control.setDisable(estadoAccion);
			}
		}
	}

	private static void disableTextFields(boolean estadoAccion, Control... controls) {
		for (Control control : controls) {
			if (control != null) {
				control.setDisable(estadoAccion);
			}
		}
	}

	private static void clearAndRefreshTableView(TableView<?> tableView) {
		if (tableView != null) {
//			tableView.getItems().clear();
			tableView.refresh();
		}
	}

	public static void cambiarEstadoOpcionesAvanzadas(boolean estadoAccion, AccionReferencias referenciaVentana) {
		if (referenciaVentana != null) {
			deshabilitarSiNoNulo(referenciaVentana.getBotonActualizarDatos(), estadoAccion);
			deshabilitarSiNoNulo(referenciaVentana.getBotonActualizarPortadas(), estadoAccion);
			deshabilitarSiNoNulo(referenciaVentana.getBotonActualizarSoftware(), estadoAccion);
			deshabilitarSiNoNulo(referenciaVentana.getBotonActualizarTodo(), estadoAccion);
			deshabilitarSiNoNulo(referenciaVentana.getBotonComprimirPortadas(), estadoAccion);
			deshabilitarSiNoNulo(referenciaVentana.getBotonReCopiarPortadas(), estadoAccion);
			deshabilitarSiNoNulo(referenciaVentana.getBotonDescargarPdf(), estadoAccion);
			deshabilitarSiNoNulo(referenciaVentana.getBotonDescargarSQL(), estadoAccion);
			deshabilitarSiNoNulo(referenciaVentana.getCheckFirmas(), estadoAccion);
			deshabilitarSiNoNulo(referenciaVentana.getBotonEliminarImportadoListaComic(), estadoAccion);
			deshabilitarSiNoNulo(referenciaVentana.getBotonGuardarListaComics(), estadoAccion);
		}
	}

	private static void deshabilitarSiNoNulo(Control control, boolean estado) {
		if (control != null) {
			control.setDisable(estado);
		}
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {

		FuncionesManejoFront.referenciaVentana = referenciaVentana;
	}

	public static List<Stage> getStageVentanas() {
		return stageVentanas;
	}

	public static void setStageVentanas(List<Stage> stageVentanas) {
		FuncionesManejoFront.stageVentanas = stageVentanas;
	}

}