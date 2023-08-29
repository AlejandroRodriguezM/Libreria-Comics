package Funcionamiento;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import JDBC.DBLibreriaManager;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class FuncionesTableView {

	private static final Font TOOLTIP_FONT = Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 13);

	private static DBLibreriaManager libreria = null;

	/**
	 * Funcion que permite que los diferentes raw de los TableColumn se puedan
	 * pinchar. Al hacer, se abre una URL en tu navegador
	 * 
	 * @param columna
	 */
	public static void busquedaHyperLink(TableColumn<Comic, String> columna) {
		columna.setCellFactory(column -> {
			return new TableCell<Comic, String>() {
				private VBox vbox = new VBox();
				private String lastItem = null;

				@Override
				protected void updateItem(String item, boolean empty) {
					if (empty || item == null) {
						setGraphic(null);
					} else {
						if (!item.equals(lastItem)) { // Verificar si el contenido ha cambiado
							lastItem = item;
							vbox.getChildren().clear();

							if (isValidUrl(item)) {
								ReferenciaHyperlink referenciaHyperlink = new ReferenciaHyperlink("Referencia", item);
								Hyperlink hyperlink = new Hyperlink(referenciaHyperlink.getDisplayText());
								hyperlink.setOnAction(event -> {
									if (Desktop.isDesktopSupported()) {
										try {
											Desktop.getDesktop().browse(new URI(referenciaHyperlink.getUrl()));
										} catch (IOException | URISyntaxException e) {
											e.printStackTrace();
										}
									}
								});

								hyperlink.getStyleClass().add("hyperlink");
								vbox.getChildren().add(hyperlink);
							} else {
								Text text = new Text("Sin Referencia");
								vbox.getChildren().add(text);
							}

							setGraphic(vbox);
						}
					}
				}
			};
		});
	}

	/**
	 * Comprueba si una cadena dada representa una URL válida.
	 *
	 * @param url La cadena que se va a comprobar.
	 * @return true si la cadena es una URL válida, de lo contrario, false.
	 */
	public static boolean isValidUrl(String url) {
		String urlRegex = "^(https?|ftp)://.*$"; // Expresión regular para verificar URLs
		Pattern pattern = Pattern.compile(urlRegex); // Compilar la expresión regular en un patrón
		Matcher matcher = pattern.matcher(url); // Crear un matcher para la cadena dada
		return matcher.matches(); // Devolver true si la cadena coincide con la expresión regular
	}

	/**
	 * Configura el efecto de resaltado y mensaje emergente al pasar el ratón por
	 * encima de una fila (raw) en la tabla.
	 *
	 * @param tablaBBDD La TableView en la que operar.
	 */
	public void seleccionarRaw(TableView<Comic> tablaBBDD) {
		tablaBBDD.setRowFactory(tv -> {
			TableRow<Comic> row = new TableRow<>();
			Tooltip tooltip = new Tooltip();
			tooltip.setShowDelay(Duration.ZERO);
			tooltip.setHideDelay(Duration.ZERO);
			tooltip.setFont(TOOLTIP_FONT);
			row.setOnMouseEntered(event -> {
				if (!row.isEmpty()) {
					row.setStyle("-fx-background-color: #BFEFFF;");

					Comic comic = row.getItem();
					if (comic != null && !tooltip.isShowing()) {
						String mensaje = "Nombre: " + comic.getNombre() + "\nNúmero: " + comic.getNumero()
								+ "\nVariante: " + comic.getVariante() + "\nPrecio: "
								+ (!comic.precio_comic.isEmpty() ? comic.precio_comic + " $" : "");

						if (!comic.getFirma().isEmpty()) {
							mensaje += "\nFirma: " + comic.getFirma();
						}
						tooltip.setText(mensaje);
						tooltip.show(row, event.getSceneX(), event.getSceneY());
						tooltip.setX(event.getScreenX() + 10);
						tooltip.setY(event.getScreenY() - 20);
					}
				}
			});

			row.setOnMouseExited(event -> {
				if (!row.isEmpty()) {
					row.setStyle("");
					tooltip.hide();
				}
			});

			return row;
		});

		// Deshabilitar el enfoque en el TableView
		tablaBBDD.setFocusTraversable(false);

		// Enfocar el VBox para evitar movimientos inesperados
		VBox root = (VBox) tablaBBDD.getScene().lookup("#rootVBox");
		if (root != null) {
			root.requestFocus();
		}
	}

	/**
	 * Funcion que permite que los diferentes raw de los TableColumn se puedan
	 * pinchar. Al hacer, genera un nuevo tipo de busqueda personalizada solo con el
	 * valor que hemos pinchado
	 * 
	 * @param columna
	 */
	public void actualizarBusquedaRaw(TableColumn<Comic, String> columna, TableView<Comic> tablaBBDD,
			List<TableColumn<Comic, String>> columnList) {
		columna.setCellFactory(column -> {
			return new TableCell<Comic, String>() {
				private VBox vbox = new VBox();
				private String lastItem = null;

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty || item == null) {
						setGraphic(null);
					} else {
						if (!item.equals(lastItem)) { // Verificar si el contenido ha cambiado
							lastItem = item;
							String[] nombres = item.split(" - ");
							vbox.getChildren().clear();

							for (String nombre : nombres) {
								if (!nombre.isEmpty()) {
									Label label;
									if (columna.getText().equalsIgnoreCase("referencia")) {
										label = new Label(nombre + "\n");
										busquedaHyperLink(column);
									} else if (columna.getText().equalsIgnoreCase("fecha")
											|| columna.getText().equalsIgnoreCase("editorial")
											|| columna.getText().equalsIgnoreCase("formato")
											|| columna.getText().equalsIgnoreCase("variante")
											|| columna.getText().equalsIgnoreCase("Nombre")
											|| columna.getText().equalsIgnoreCase("Nº")
											|| columna.getText().equalsIgnoreCase("Caja")
											|| columna.getText().equalsIgnoreCase("Origen")
											|| columna.getText().equalsIgnoreCase("firma")) {
										label = new Label(nombre + "\n");
									} else {
										label = new Label("◉ " + nombre + "\n");
									}
									label.getStyleClass().add("hyperlink");
									Hyperlink hyperlink = new Hyperlink();
									hyperlink.setGraphic(label);
									hyperlink.setOnAction(event -> {
										try {
											columnaSeleccionada(tablaBBDD, columnList, nombre);
										} catch (SQLException e) {
											e.printStackTrace();
										}
									});
									vbox.getChildren().add(hyperlink);
								}
							}
						}
						setGraphic(vbox);
					}
				}
			};
		});
	}

	/**
	 * Genera el resultado de búsqueda para un cómic y devuelve un TextArea con la
	 * información.
	 *
	 * @param comic El cómic para el que se generará el resultado de búsqueda.
	 * @return Un TextArea con el resultado de búsqueda.
	 * @throws SQLException Si ocurre un error de base de datos.
	 */
	public TextArea resultadoBusquedaPront(Comic comic) throws SQLException {
		libreria = new DBLibreriaManager();
		StringBuilder datoSeleccionadoBuilder = new StringBuilder();
		TextArea prontInfoTable = new TextArea(); // Crear un nuevo TextArea

		if (comic != null) {
			String[] campos = { comic.getNombre(), comic.getNumero(), comic.getVariante(), comic.getProcedencia(),
					comic.getFormato(), comic.getEditorial(), comic.getFecha(), comic.getNumCaja(),
					comic.getGuionista(), comic.getDibujante(), comic.getFirma() };

			int nonEmptyFieldCount = 0;
			for (String campo : campos) {
				if (!campo.isEmpty()) {
					nonEmptyFieldCount++;
					if (nonEmptyFieldCount > 1) {
						datoSeleccionadoBuilder.append(", ");
					}
					datoSeleccionadoBuilder.append(campo);
				}
			}
		}
		prontInfoTable.setOpacity(1);

		String datoSeleccionado = datoSeleccionadoBuilder.toString();
		if (!libreria.numeroResultados(comic) && !datoSeleccionado.isEmpty()) {
			// Show error message in red when no search fields are specified
			prontInfoTable.setStyle("-fx-text-fill: red;");
			prontInfoTable.setText("Error: No existe comic con los datos: " + datoSeleccionado);
		} else if (datoSeleccionado.isEmpty()) {
			prontInfoTable.setStyle("-fx-text-fill: red;");
			prontInfoTable.setText("Error: No has seleccionado ningun comic para filtrar, se muestran todos.");
		} else {
			int totalComics = libreria.numeroTotalSelecionado(comic);
			prontInfoTable.setStyle("-fx-text-fill: black;"); // Reset the text color to black
			prontInfoTable.setText(
					"El número de cómics donde aparece la búsqueda: " + datoSeleccionado + " es: " + totalComics);
		}

		return prontInfoTable;
	}

	/**
	 * Obtiene los datos de los comics de la base de datos y los devuelve en el
	 * textView
	 *
	 * @param listaComic
	 */
	public void tablaBBDD(List<Comic> listaComic, TableView<Comic> tablaBBDD,
			List<TableColumn<Comic, String>> columnList) {
		tablaBBDD.getColumns().setAll(columnList);
		tablaBBDD.getItems().setAll(listaComic);
	}

	/**
	 * Actualiza la tabla con los datos de un comic seleccionado.
	 *
	 * @param tablaBBDD      La TableView en la que se mostrarán los datos.
	 * @param columnList     La lista de columnas de la TableView.
	 * @param rawSelecionado El comic seleccionado en su forma cruda.
	 * @throws SQLException Si ocurre un error de base de datos.
	 */
	public void columnaSeleccionada(TableView<Comic> tablaBBDD, List<TableColumn<Comic, String>> columnList,
			String rawSelecionado) throws SQLException {
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas(columnList, tablaBBDD);
		tablaBBDD(libreria.libreriaSeleccionado(rawSelecionado), tablaBBDD, columnList);
	}

	/**
	 * Configura el nombre y la fábrica de valores para las columnas de una tabla de
	 * datos basado en una lista de columnas.
	 *
	 * @param columnList La lista de TableColumn a configurar.
	 * @param tablaBBDD  La TableView en la que se aplicarán las configuraciones.
	 */
	public void nombreColumnas(List<TableColumn<Comic, String>> columnList, TableView<Comic> tablaBBDD) {
		for (TableColumn<Comic, String> column : columnList) {
			String columnName = column.getText(); // Obtiene el nombre de la columna

			if (columnName.equalsIgnoreCase("Nº")) {
				columnName = "Numero";
			}

			if (columnName.equalsIgnoreCase("Caja")) {
				columnName = "numCaja";
			}

			if (columnName.equalsIgnoreCase("Referencia")) {
				columnName = "url_referencia";
			}

			if (columnName.equalsIgnoreCase("Origen")) {
				columnName = "Procedencia";
			}

			PropertyValueFactory<Comic, String> valueFactory = new PropertyValueFactory<>(columnName);
			column.setCellValueFactory(valueFactory);

			actualizarBusquedaRaw(column, tablaBBDD, columnList);
		}
	}

	/**
	 * Modifica los tamaños de las columnas de una TableView de acuerdo a los
	 * tamaños específicos definidos.
	 *
	 * @param tablaBBDD  La TableView cuyas columnas se van a modificar.
	 * @param columnList La lista de TableColumn correspondiente a las columnas de
	 *                   la tabla.
	 */
	public void modificarColumnas(TableView<Comic> tablaBBDD, List<TableColumn<Comic, String>> columnList) {
		// Definir los tamaños específicos para cada columna en la misma posición que en
		// columnList
		Double[] columnWidths = { 140.0, // nombre
				37.0, // caja
				47.0, // numero
				135.0, // variante
				85.0, // firma
				78.0, // editorial
				92.0, // formato
				75.0, // procedencia
				105.0, // fecha
				145.0, // guionista
				150.0, // dibujante
				92.0, // referencia

		};

		// Set the resizing policy to unconstrained
		tablaBBDD.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

		// Reiniciar el tamaño de las columnas
		tablaBBDD.refresh();

		// Aplicar los anchos específicos a cada columna
		for (int i = 0; i < columnList.size(); i++) {
			TableColumn<Comic, String> column = columnList.get(i);
			Double columnWidth = columnWidths[i];
			column.setPrefWidth(columnWidth);
		}
	}

	/**
	 * Ajusta el alto del VBox de acuerdo al contenido del TextArea.
	 *
	 * @param textArea El TextArea del cual obtener el contenido.
	 * @param vbox     El VBox al cual ajustar el alto.
	 */
	public void ajustarAnchoVBox(TextArea textArea, VBox vbox) {
		// Crear un objeto Text con el contenido del TextArea
		Text text = new Text(textArea.getText());

		// Configurar el mismo estilo que tiene el TextArea
		text.setFont(textArea.getFont());

		double textHeight = text.getLayoutBounds().getHeight();

		textArea.setPrefHeight(textHeight);
	}

	/**
	 * Restringe los símbolos no permitidos en el TextField y muestra un Tooltip
	 * informativo.
	 *
	 * @param textField El TextField en el cual restringir los símbolos.
	 */
	public static void restringirSimbolos(TextField textField) {
		Tooltip tooltip = new Tooltip();

		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			String allowedPattern = "[\\p{L}\\p{N}\\s,.-]*"; // Expresión regular para permitir letras, números,
																// espacios, ",", "-" y "."

			if (newValue != null && !newValue.matches(allowedPattern)) {
				textField.setText(oldValue);
			} else {
				String updatedValue = newValue.replaceAll("\\s*(?<![,-])(?=[,-])|(?<=[,-])\\s*", "");

				if (!updatedValue.equals(newValue)) {
					textField.setText(updatedValue);
				}
			}
		});
		tooltip.setFont(TOOLTIP_FONT);
		textField.setOnMouseEntered(event -> {
			tooltip.setShowDelay(Duration.ZERO);
			tooltip.setHideDelay(Duration.ZERO);

			String mensaje = "En caso de tener varios artistas en variante, guionista o dibujante, separalos usando una coma ',' o guion '-'";
			tooltip.setText(mensaje);
			tooltip.show(textField, event.getSceneX(), event.getSceneY());
			tooltip.setX(event.getScreenX() + 10); // Ajusta el desplazamiento X según tus necesidades
			tooltip.setY(event.getScreenY() - 20); // Ajusta el desplazamiento Y según tus necesidades
		});

		textField.setOnMouseExited(event -> {
			tooltip.hide();
		});
	}
}
