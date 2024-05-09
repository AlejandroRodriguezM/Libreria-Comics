package funcionesInterfaz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.controlsfx.control.textfield.TextFields;

import alarmas.AlarmaList;
import comicManagement.Comic;
import funcionesManagment.AccionFuncionesComunes;
import funcionesManagment.AccionReferencias;
import funciones_auxiliares.Utilidades;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
		for (Node elemento : referenciaVentana.getListaElementosFondo()) {
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
		for (Control campo : referenciaVentana.getComboboxes()) {
			if (campo instanceof TextField campoTexto) {
				Platform.runLater(() -> campoTexto.maxWidthProperty()
						.bind(Bindings.max(maxTextComboWidth, campoTexto.widthProperty())));
			}
		}
	}

	public static void establecerAnchoMaximoComboBoxes(double maxTextComboWidth) {
		for (ComboBox<String> comboBox : referenciaVentana.getComboboxes()) {

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
				String allowedPattern = "[\\p{L}\\p{N}\\s,.!'`´\"-]*"; // Expresión regular para permitir letras,
																		// números, espacios, ",", "-", "'", "`", "´", y
																		// '"'

				if (newValue != null) {

					// Elimina espacios al principio y al final de la cadena.
					newValue = newValue.trim();

					if (!newValue.matches(allowedPattern)) {
						// Si el valor no coincide con el patrón permitido, restaura el valor anterior.
						textField.setText(oldValue);
					} else {
						String updatedValue = newValue.replaceAll("\\s*(?<![,'\"`´-])(?=[,'\"`´-])|(?<=[,'\"`´-])\\s*",
								"");

						if (!updatedValue.equals(newValue)) {
							textField.setText(updatedValue);
						}
					}
				}
			});
		}
	}

	public static void eliminarSimbolosEspeciales(TextField textField) {

		if (textField != null) {

			textField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue != null) {

					// Elimina los símbolos especiales ' " ! ? # @
					String cleanedValue = newValue.replaceAll("[\\'\"!\\?#@,]", "");

					// Reemplaza ' seguido de números con -
					cleanedValue = cleanedValue.replaceAll("\\'(?=\\d)", "-");

					// Actualiza el valor del campo de texto
					textField.setText(cleanedValue);
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
					newValue = newValue.replace(" ", "");

					textField.setText(newValue); // Actualiza el valor del TextField
				}
			});

		}
	}

	public static void manejarMensajeTextArea(String mensaje) {
		AlarmaList.iniciarAnimacionTextArea(referenciaVentana.getProntInfo(), mensaje);
	}

	public static void cambiarEstadoMenuBar(boolean estadoAccion, AccionReferencias referenciaVentana) {

		if (referenciaVentana == null) {
			return;
		}

		disableMenuItems(estadoAccion, referenciaVentana.getMenu_archivo_excel(),
				referenciaVentana.getMenu_archivo_importar(), referenciaVentana.getMenu_archivo_delete(),
				referenciaVentana.getMenu_comic_aniadir(), referenciaVentana.getMenu_comic_eliminar(),
				referenciaVentana.getMenu_comic_modificar(), referenciaVentana.getMenu_comic_puntuar(),
				referenciaVentana.getMenu_comic_aleatoria(), referenciaVentana.getMenu_archivo_avanzado(),
				referenciaVentana.getMenu_leer_CodigoBarras(),
				referenciaVentana.getMenu_Importar_Fichero_CodigoBarras(), referenciaVentana.getMenu_archivo_conexion(),
				referenciaVentana.getNavegacion_estadistica(),
				referenciaVentana.getMenu_Importar_Fichero_CodigoBarras(), referenciaVentana.getMenu_archivo_sobreMi(),
				referenciaVentana.getMenu_comprobar_apis(), referenciaVentana.getMenu_archivo_desconectar(),
				referenciaVentana.getMenu_archivo_cerrar());

		disableButtons(estadoAccion, referenciaVentana.getBotonIntroducir(), referenciaVentana.getBotonModificar(),
				referenciaVentana.getBotonEliminar(), referenciaVentana.getBotonAgregarPuntuacion(),
				referenciaVentana.getBotonLimpiar(), referenciaVentana.getBotonMostrarParametro(),
				referenciaVentana.getBotonImprimir(), referenciaVentana.getBotonGuardarResultado(),
				referenciaVentana.getBotonbbdd(), referenciaVentana.getBotonLimpiar(),
				referenciaVentana.getBotonBusquedaAvanzada(), referenciaVentana.getBotonLimpiar(),
				referenciaVentana.getBotonSubidaPortada(), referenciaVentana.getBotonGuardarComic(),
				referenciaVentana.getBotonGuardarCambioComic(), referenciaVentana.getBotonEliminarImportadoComic(),
				referenciaVentana.getBotonParametroComic(), referenciaVentana.getBotonbbdd(),
				referenciaVentana.getBotonBusquedaCodigo());

		disableControls(estadoAccion, referenciaVentana.getTituloComic(), referenciaVentana.getNombreDibujante(),
				referenciaVentana.getNombreEditorial(), referenciaVentana.getNombreFirma(),
				referenciaVentana.getNombreFormato(), referenciaVentana.getNombreGuionista(),
				referenciaVentana.getNombreProcedencia(), referenciaVentana.getNombreVariante(),
				referenciaVentana.getGradeoComic(), referenciaVentana.getNumeroComic(),
				referenciaVentana.getFechaPublicacion(), referenciaVentana.getBusquedaGeneral(),
				referenciaVentana.getEstadoComic(), referenciaVentana.getProcedenciaComic(),
				referenciaVentana.getNumeroComic(), referenciaVentana.getFormatoComic(),
				referenciaVentana.getFechaComic(), referenciaVentana.getBusquedaCodigo());

		disableTextFields(estadoAccion, referenciaVentana.getNombreComic(), referenciaVentana.getVarianteComic(),
				referenciaVentana.getFirmaComic(), referenciaVentana.getEditorialComic(),
				referenciaVentana.getPrecioComic(), referenciaVentana.getCodigoComicTratar(),
				referenciaVentana.getDireccionImagen(), referenciaVentana.getIdComicTratar(),
				referenciaVentana.getGuionistaComic(), referenciaVentana.getDibujanteComic(),
				referenciaVentana.getNombreKeyIssue(), referenciaVentana.getUrlReferencia());

		if (referenciaVentana.getNombreComic() != null) {
			List<Node> elementos = Arrays.asList(getReferenciaVentana().getBotonBusquedaCodigo(),
					getReferenciaVentana().getBusquedaCodigo());
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

	private static void disableTextFields(boolean estadoAccion, TextField... textFields) {
		for (TextField textfield : textFields) {
			if (textfield != null) {
				textfield.setDisable(estadoAccion);
			}
		}
	}

	private static void clearAndRefreshTableView(TableView<?> tableView) {
		if (tableView != null) {
//			tableView.getItems().clear();
			tableView.refresh();
		}
	}

	private static void hideImageAndClearText(Node imageNode, TextArea textArea) {
		if (imageNode != null) {
			imageNode.setVisible(false);
		}
		if (textArea != null) {
			textArea.clear();
			textArea.setText(null);
			textArea.setOpacity(0);
		}
	}

	public static void cambiarEstadoOpcionesAvanzadas(boolean estadoAccion, AccionReferencias referenciaVentana) {

		referenciaVentana.getBotonActualizarDatos().setDisable(estadoAccion);
		referenciaVentana.getBotonActualizarPortadas().setDisable(estadoAccion);
		referenciaVentana.getBotonActualizarSoftware().setDisable(estadoAccion);
		referenciaVentana.getBotonActualizarTodo().setDisable(estadoAccion);
		referenciaVentana.getBotonComprimirPortadas().setDisable(estadoAccion);
		referenciaVentana.getBotonReCopiarPortadas().setDisable(estadoAccion);
		referenciaVentana.getBotonDescargarPdf().setDisable(estadoAccion);
		referenciaVentana.getBotonDescargarSQL().setDisable(estadoAccion);
		referenciaVentana.getBotonNormalizarDB().setDisable(estadoAccion);

		referenciaVentana.getCheckFirmas().setDisable(estadoAccion);
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