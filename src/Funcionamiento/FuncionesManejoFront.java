package Funcionamiento;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.DBUtilidades.TipoBusqueda;
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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

	private static TextArea prontInfo;

	private static TextField busquedaGeneral;

	private static Button botonImprimir;

	private static Button botonGuardarResultado;

	private static List<TableColumn<Comic, String>> columnList;

	@SuppressWarnings("rawtypes")
	static ObservableList<ComboBox> listaComboBoxes;
	@SuppressWarnings("rawtypes")

	static ObservableList<TableColumn> listaColumnas;
	static ObservableList<Control> listaCamposTexto;
	static ObservableList<Button> listaBotones;
	static ObservableList<Node> listaElementosFondo;
	static ObservableList<ImageView> listaImagenes;

	public void setTableView(TableView<Comic> tablaComic) {
		tablaBBDD = tablaComic;
	}

	public static void setAnchorPane(AnchorPane anchorPane) {
		rootAnchorPane = anchorPane;
	}

	public void copiarListas(List<ComboBox<String>> comboboxes, List<TableColumn<Comic, String>> columnList2,
			ObservableList<Control> textFieldList, ObservableList<Button> buttonList, ObservableList<Node> nodeList,
			ObservableList<ImageView> imageViewList) {
		listaComboBoxes = (comboboxes != null) ? FXCollections.observableArrayList(comboboxes)
				: FXCollections.observableArrayList();
		listaColumnas = (columnList2 != null) ? FXCollections.observableArrayList(columnList2)
				: FXCollections.observableArrayList();
		listaCamposTexto = (textFieldList != null) ? FXCollections.observableArrayList(textFieldList)
				: FXCollections.observableArrayList();
		listaBotones = (buttonList != null) ? FXCollections.observableArrayList(buttonList)
				: FXCollections.observableArrayList();
		listaElementosFondo = (nodeList != null) ? FXCollections.observableArrayList(nodeList)
				: FXCollections.observableArrayList();
		listaImagenes = (imageViewList != null) ? FXCollections.observableArrayList(imageViewList)
				: FXCollections.observableArrayList();
	}

	public void copiarElementos(TextArea prontInfoGeneral, Button botonImprimirGeneral,
			Button botonGuardarResultadoGeneral, TextField busquedaGeneralGeneral,
			List<TableColumn<Comic, String>> columnListGeneral) {

		prontInfo = prontInfoGeneral;

		busquedaGeneral = busquedaGeneralGeneral;

		botonImprimir = botonImprimirGeneral;

		botonGuardarResultado = botonGuardarResultadoGeneral;

		columnList = columnListGeneral;

	}

	private static final List<Character> simbolos = Arrays.asList(',', '-', '!', '@', '#', '$', '%', '^', '&', '*', '(',
			')', '[', ']', '{', '}', ';', ':', '|', '\\', '<', '>', '/', '?', '~', '`', '+', '=', '.');

	public static void establecerFondoDinamico() {
		for (Node elemento : listaElementosFondo) {
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
	public static void establecerAnchoColumnas(double numColumns) {
		for (TableColumn<Comic, ?> columna : listaColumnas) {
			columna.prefWidthProperty().bind(tablaBBDD.widthProperty().divide(numColumns));
		}
	}

	public static void establecerAnchoMaximoBotones(double maxButtonWidth) {
		for (Button boton : listaBotones) {
			boton.maxWidthProperty().bind(Bindings.max(maxButtonWidth, boton.widthProperty()));
		}
	}

	public static void establecerAnchoMaximoCamposTexto(double maxTextComboWidth) {
		for (Control campo : listaCamposTexto) {
			if (campo instanceof TextField) {
				TextField campoTexto = (TextField) campo;
				Platform.runLater(() -> campoTexto.maxWidthProperty()
						.bind(Bindings.max(maxTextComboWidth, campoTexto.widthProperty())));
			}
		}
	}

	public static void establecerAnchoMaximoComboBoxes(double maxTextComboWidth) {
		for (ComboBox<?> comboBox : listaComboBoxes) {
			comboBox.maxWidthProperty().bind(Bindings.max(maxTextComboWidth, comboBox.widthProperty()));
		}
	}

	public static void establecerTamanioMaximoImagen(double maxWidth, double maxHeight) {
		for (ImageView imagen : listaImagenes) {
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

	public static void verBasedeDatos(boolean completo, boolean esAccion, Comic comic) {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		ListaComicsDAO.reiniciarListaComics();
		FuncionesTableView.modificarColumnas(tablaBBDD, columnList);
		tablaBBDD.refresh();
		prontInfo.clear();
		prontInfo.setOpacity(0);
		listaImagenes.get(0).setImage(null);

		FuncionesTableView.nombreColumnas(columnList, tablaBBDD); // Llamada a funcion
		FuncionesTableView.actualizarBusquedaRaw(tablaBBDD, columnList);

		if (ComicManagerDAO.countRows(SelectManager.TAMANIO_DATABASE) > 0) {
			if (completo) {
				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(TipoBusqueda.COMPLETA);

				List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
				FuncionesTableView.tablaBBDD(listaComics, tablaBBDD, columnList);

			} else {

				List<Comic> listaParametro = listaPorParametro(comic, esAccion);

				FuncionesTableView.tablaBBDD(listaParametro, tablaBBDD, columnList); // Llamada a funcion

				if (!esAccion) {
					if (!listaParametro.isEmpty()) {
						botonImprimir.setVisible(true);
						botonGuardarResultado.setVisible(true);
					} else {
						botonImprimir.setVisible(false);
						botonGuardarResultado.setVisible(false);
					}
					busquedaGeneral.setText("");
				}

			}
		} else {
			String mensaje = "ERROR. No hay datos en la base de datos";

			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
		}
	}

	/**
	 * Funcion que comprueba segun los datos escritos en los textArea, que comic
	 * estas buscando.
	 * 
	 * @throws SQLException
	 */
	public static List<Comic> listaPorParametro(Comic datos, boolean esAccion) {

		if (!ConectManager.conexionActiva()) {
			return null;
		}
		String busquedaGeneralTextField = "";

		if (!esAccion) {
			busquedaGeneralTextField = busquedaGeneral.getText();
		}

		List<Comic> listComic = FXCollections
				.observableArrayList(SelectManager.busquedaParametro(datos, busquedaGeneralTextField));

		if (listComic.size() > 0) {
			prontInfo.setOpacity(1);
			prontInfo.setText(FuncionesTableView.resultadoBusquedaPront(datos).getText());
		} else {
			prontInfo.setOpacity(1);
			// Show error message in red when no search fields are specified
			prontInfo.setStyle("-fx-text-fill: red;");
			prontInfo.setText("Error No existe comic con los datos: " + datos.toString() + "\n \n \n");
		}

		return listComic;
	}

}
