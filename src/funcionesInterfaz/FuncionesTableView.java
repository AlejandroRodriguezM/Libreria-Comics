/**
 * Contiene las clases que hacen funcionar las diferentes funciones de uso de back end y front de todo el proyecto
 *  
*/
package funcionesInterfaz;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import comicManagement.Comic;
import dbmanager.ListasComicsDAO;
import dbmanager.SelectManager;
import funcionesAuxiliares.Utilidades;
import funcionesManagment.AccionReferencias;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 * Clase que contiene diversas funciones relacionadas con TableView y
 * operaciones en la interfaz de usuario.
 */
public class FuncionesTableView {

	/**
	 * Fuente utilizada para los tooltips en la interfaz gráfica.
	 */
	private static final Font TOOLTIP_FONT = Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 13);
	private static Tooltip currentTooltip;

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	public static void busquedaHyperLink(TableColumn<Comic, String> columna) {
		columna.setCellFactory(column -> new TableCell<Comic, String>() {
			private VBox vbox = new VBox();
			private String lastItem = null;

			@Override
			protected void updateItem(String item, boolean empty) {
				if (empty || item == null) {
					setGraphic(null);
				} else {
					if (!item.equals(lastItem)) {
						lastItem = item;
						vbox.getChildren().clear();
						createContent(item);
						setGraphic(vbox);
					}
				}
			}

			private void createContent(String item) {
				if (isValidUrl(item)) {
					addHyperlink(item);
				} else {
					addText("Sin Referencia");
				}
			}

			private void addHyperlink(String url) {
				ReferenciaHyperlink referenciaHyperlink = new ReferenciaHyperlink("Referencia", url);
				Hyperlink hyperlink = createHyperlink(referenciaHyperlink);

				vbox.getChildren().add(hyperlink);
			}

			private Hyperlink createHyperlink(ReferenciaHyperlink referenciaHyperlink) {
				Hyperlink hyperlink = new Hyperlink(referenciaHyperlink.getDisplayText());
				hyperlink.setOnAction(event -> Utilidades.accesoWebWindows(referenciaHyperlink.getUrl()));
				hyperlink.getStyleClass().add("hyperlink");

				return hyperlink;
			}

			private void addText(String text) {
				Text txt = new Text(text);

				vbox.getChildren().add(txt);

			}
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

	public static void seleccionarRaw() {
		getReferenciaVentana().getTablaBBDD().setRowFactory(tv -> createRow());
		disableFocusTraversal();
	}

	private static TableRow<Comic> createRow() {
		TableRow<Comic> row = new TableRow<>();
		Tooltip tooltip = createTooltip();

		row.setOnMouseMoved(event -> showTooltip(event, row, tooltip));
		row.setOnMouseExited(event -> hideTooltipIfOutside(event, row, tooltip));

		return row;
	}

	private static Tooltip createTooltip() {
		Tooltip tooltip = new Tooltip();
		tooltip.setShowDelay(Duration.ZERO);
		tooltip.setHideDelay(Duration.ZERO);
		tooltip.setFont(TOOLTIP_FONT);
		return tooltip;
	}

	private static void showTooltip(MouseEvent event, TableRow<Comic> row, Tooltip tooltip) {
		if (!row.isEmpty()) {
			row.setStyle("-fx-background-color: #BFEFFF;");
			Comic comic = row.getItem();
			if (comic != null) {
				String mensaje = generateTooltipMessage(comic);
				adjustTooltipPosition(event, tooltip);
				tooltip.setText(mensaje);
				if (currentTooltip != null && currentTooltip.isShowing()) {
					currentTooltip.hide();
				}
				currentTooltip = tooltip;
				tooltip.show(row, event.getScreenX() + 10, event.getScreenY() - 20);
			}
		}
		// Cerrar el tooltip cuando se hace clic en una fila
		row.setOnMouseClicked(e -> {
			if (currentTooltip != null && currentTooltip.isShowing()) {
				currentTooltip.hide();
			}
		});
	}

	private static String generateTooltipMessage(Comic comic) {
		StringBuilder mensajeBuilder = new StringBuilder();

		mensajeBuilder.append("Nombre: ").append(comic.getTituloComic()).append("\n").append("Número: ")
				.append(comic.getNumeroComic()).append("\n").append("Editorial: ").append(comic.getEditorComic())
				.append("\n").append("\n");

		return mensajeBuilder.toString();
	}

	private static void adjustTooltipPosition(MouseEvent event, Tooltip tooltip) {
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double posX = event.getScreenX() + 10;
		double posY = event.getScreenY() - 20;

		if (posX + tooltip.getWidth() > screenBounds.getMaxX()) {
			posX = screenBounds.getMaxX() - tooltip.getWidth();
		}
		if (posY + tooltip.getHeight() > screenBounds.getMaxY()) {
			posY = screenBounds.getMaxY() - tooltip.getHeight();
		}
		tooltip.setX(posX);
		tooltip.setY(posY);
	}

	private static void hideTooltipIfOutside(MouseEvent event, TableRow<Comic> row, Tooltip tooltip) {
		if (!row.isEmpty() && !row.getBoundsInLocal().contains(event.getSceneX(), event.getSceneY())) {
			row.setStyle("");
			tooltip.hide();
		}
	}

	private static void disableFocusTraversal() {
		getReferenciaVentana().getTablaBBDD().setFocusTraversable(false);
	}

	public static void actualizarBusquedaRaw() {
		getReferenciaVentana();
		AccionReferencias.getListaColumnasTabla()
				.forEach(columna -> columna.setCellFactory(column -> new TableCell<Comic, String>() {
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
								vbox.getChildren().clear();
								createLabels(columna, item, vbox);

							}
							setGraphic(vbox);
						}
					}
				}));
	}

	private static void createLabels(TableColumn<Comic, String> columna, String item, VBox vbox) {
		String[] nombres = item.split(" - ");
		for (String nombre : nombres) {
			if (!nombre.isEmpty()) {
				Text text = createTextForColumn(columna, nombre);
				Hyperlink hyperlink = createHyperlinkForText(text, nombre, columna);
				hyperlink.getStyleClass().add("hyperlink");
				vbox.getChildren().add(hyperlink);

				adjustVBoxSizeOnContentChange(vbox);
			}
		}
	}

	private static Hyperlink createHyperlinkForText(Text text, String nombre, TableColumn<Comic, String> columna) {
		Hyperlink hyperlink = new Hyperlink();
		text.setWrappingWidth(columna.getWidth() - (columna.getWidth()));
		hyperlink.setGraphic(text);

		hyperlink.setOnAction(event -> columnaSeleccionada(getReferenciaVentana().getTablaBBDD(), nombre));
		return hyperlink;
	}

	private static void adjustVBoxSizeOnContentChange(VBox vbox) {
		vbox.heightProperty()
				.addListener((obs, oldHeight, newHeight) -> vbox.setMaxHeight(newHeight.doubleValue() - 100));

	}

	private static Text createTextForColumn(TableColumn<Comic, String> columna, String nombre) {
		Text text = new Text(nombre);
		text.setFont(Font.font("System", FontWeight.NORMAL, 13));
		if (columna.getText().equalsIgnoreCase("referencia")) {
			busquedaHyperLink(columna); // No estoy seguro de qué hace esta función, por lo que la he dejado aquí
		} else if (isSpecialColumn(columna.getText())) {
			text.setText("◉ " + nombre);
		} else if (columna.getText().equalsIgnoreCase("Fecha")) {
			text.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 12));
		}

		text.setFill(Color.web("#4ea0f2"));
		text.getStyleClass().add("hyperlink");

		return text;
	}

	private static boolean isSpecialColumn(String columnName) {
		return columnName.equalsIgnoreCase("variante") || columnName.equalsIgnoreCase("firma")
				|| columnName.equalsIgnoreCase("dibujante") || columnName.equalsIgnoreCase("guionista");
	}

	/**
	 * Obtiene los datos de los comics de la base de datos y los devuelve en el
	 * textView
	 *
	 * @param listaComic
	 */
	public static void tablaBBDD(List<Comic> listaComic) {
		getReferenciaVentana().getTablaBBDD().getColumns().setAll(AccionReferencias.getListaColumnasTabla());
		getReferenciaVentana().getTablaBBDD().getItems().setAll(listaComic);
		getReferenciaVentana().getImagenComic().setVisible(true);
	}

	/**
	 * Actualiza la tabla con los datos de un comic seleccionado.
	 *
	 * @param tablaBBDD      La TableView en la que se mostrarán los datos.
	 * @param columnList     La lista de columnas de la TableView.
	 * @param rawSelecionado El comic seleccionado en su forma cruda.
	 * @throws SQLException Si ocurre un error de base de datos.
	 */
	public static void columnaSeleccionada(TableView<Comic> tablaBBDD, String rawSelecionado) {
		ListasComicsDAO.reiniciarListaComics();
		nombreColumnas();

		tablaBBDD(SelectManager.libreriaSeleccionado(rawSelecionado));

		// Deseleccionar la fila seleccionada
		tablaBBDD.getSelectionModel().clearSelection();
		getReferenciaVentana().getImagenComic().setVisible(true);
	}

	/**
	 * Configura el nombre y la fábrica de valores para las columnas de una tabla de
	 * datos basado en una lista de columnas.
	 *
	 * @param columnList La lista de TableColumn a configurar.
	 * @param tablaBBDD  La TableView en la que se aplicarán las configuraciones.
	 */
	public static void nombreColumnas() {
		for (TableColumn<Comic, String> column : AccionReferencias.getListaColumnasTabla()) {
			String columnName = column.getText(); // Obtiene el nombre de la columna
//
			configureColumn(column, columnName);
		}
	}

	private static void configureColumn(TableColumn<Comic, String> column, String property) {

		switch (property) {

		case "Titulo":
			property = "tituloComic";
			break;
		case "Numero":
			property = "numeroComic";
			break;
		case "Valor":
			property = "precioComic";
			break;
		case "Editor":
			property = "editorComic";
			break;
		case "Referencia":
			property = "urlReferenciaComic";
			break;
		case "Dibujante":
			property = "artistaComic";
			break;
		case "Variante":
			property = "varianteComic";
			break;
		case "Guionista":
			property = "guionistaComic";
			break;
		case "ID":
			property = "idComic";
			break;
		case "Firma":
			property = "firmaComic";
			break;
		}
		column.setCellValueFactory(new PropertyValueFactory<>(property));
	}

	/**
	 * Modifica los tamaños de las columnas de una TableView de acuerdo a los
	 * tamaños específicos definidos.
	 *
	 * @param tablaBBDD  La TableView cuyas columnas se van a modificar.
	 * @param columnList La lista de TableColumn correspondiente a las columnas de
	 *                   la tabla.
	 */
	public static void modificarColumnas(boolean esPrincipal) {

		getReferenciaVentana();
		for (TableColumn<Comic, String> column : AccionReferencias.getListaColumnasTabla()) {
			column.prefWidthProperty().unbind(); // Desvincular cualquier propiedad prefWidth existente
		}

		// Definir los tamaños específicos para cada columna en la misma posición que en
		// columnList
		Double[] columnWidths;

		if (esPrincipal) {
			columnWidths = new Double[] { 150.0, // Titulo
					68.0, // Numero
					65.0, // Valor
					86.0, // editor
					105.0, // firma
					216.0, // dibujante
					216.0, // fecha
					216.0, // variante
					90.0 // guionista
			};
		} else {
			columnWidths = new Double[] { 140.0, // nombre
					140.0, // variante
					69.0, // editorial
					97.0, // formato
					150.0, // guionista
					145.0, // dibujante
					90.0 // caja
			};
		}

		getReferenciaVentana();
		// Aplicar los anchos específicos a cada columna
		for (int i = 0; i < AccionReferencias.getListaColumnasTabla().size(); i++) {
			getReferenciaVentana();
			TableColumn<Comic, String> column = AccionReferencias.getListaColumnasTabla().get(i);
			Double columnWidth = columnWidths[i];
			column.setPrefWidth(columnWidth);
		}

		// Configurar la política de redimensionamiento
		getReferenciaVentana().getTablaBBDD().setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
	}

	/**
	 * Ajusta el alto del VBox de acuerdo al contenido del TextArea.
	 *
	 * @param textArea El TextArea del cual obtener el contenido.
	 * @param vbox     El VBox al cual ajustar el alto.
	 */
	public static void ajustarAnchoVBox() {
		// Crear un objeto Text con el contenido del TextArea
		Text text = new Text(getReferenciaVentana().getProntInfoTextArea().getText());

		// Configurar el mismo estilo que tiene el TextArea
		text.setFont(getReferenciaVentana().getProntInfoTextArea().getFont());

		double textHeight = text.getLayoutBounds().getHeight();

		getReferenciaVentana().getProntInfoTextArea().setPrefHeight(textHeight);
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		FuncionesTableView.referenciaVentana = referenciaVentana;
	}

}