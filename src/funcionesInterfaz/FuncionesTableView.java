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
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import funcionesManagment.AccionReferencias;
import funciones_auxiliares.Utilidades;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
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
		mensajeBuilder.append("Nombre: ").append(comic.getNombre()).append("\nNúmero: ").append(comic.getNumero())
				.append("\nVariante: ").append(comic.getVariante()).append("\nPrecio: ")
				.append(!comic.getprecioComic().isEmpty() ? comic.getprecioComic() + " $" : "");

		if (!comic.getFirma().isEmpty()) {
			mensajeBuilder.append("\nFirma: ").append(comic.getFirma());
		}
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
		getReferenciaVentana().getColumnasTabla()
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
		text.setWrappingWidth(columna.getWidth() - (columna.getWidth() * 0.3));
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
		getReferenciaVentana().getTablaBBDD().getColumns().setAll(getReferenciaVentana().getColumnasTabla());
		getReferenciaVentana().getTablaBBDD().getItems().setAll(listaComic);
		getReferenciaVentana().getImagencomic().setVisible(true);
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
		ListaComicsDAO.reiniciarListaComics();
		nombreColumnas();

		tablaBBDD(SelectManager.libreriaSeleccionado(rawSelecionado));

		// Deseleccionar la fila seleccionada
		tablaBBDD.getSelectionModel().clearSelection();
		getReferenciaVentana().getImagencomic().setVisible(true);
	}

	/**
	 * Configura el nombre y la fábrica de valores para las columnas de una tabla de
	 * datos basado en una lista de columnas.
	 *
	 * @param columnList La lista de TableColumn a configurar.
	 * @param tablaBBDD  La TableView en la que se aplicarán las configuraciones.
	 */
	public static void nombreColumnas() {
		for (TableColumn<Comic, String> column : referenciaVentana.getColumnasTabla()) {
			String columnName = column.getText(); // Obtiene el nombre de la columna

			// Realiza la correspondencia entre los nombres de columna y las propiedades de
			// Comic
			if (columnName.equalsIgnoreCase("Nº")) {
				columnName = "Numero";
			} else if (columnName.equalsIgnoreCase("Gradeo")) {
				columnName = "valorGradeo";
			} else if (columnName.equalsIgnoreCase("Referencia")) {
				columnName = "urlReferencia";
			} else if (columnName.equalsIgnoreCase("Origen")) {
				columnName = "Procedencia";
			}

			// Crea una PropertyValueFactory con el nombre de la propiedad actual
			PropertyValueFactory<Comic, String> valueFactory = new PropertyValueFactory<>(columnName);
			column.setCellValueFactory(valueFactory);
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
	public static void modificarColumnas(boolean esPrincipal) {

		for (TableColumn<Comic, String> column : getReferenciaVentana().getColumnasTabla()) {
			column.prefWidthProperty().unbind(); // Desvincular cualquier propiedad prefWidth existente
		}

		// Definir los tamaños específicos para cada columna en la misma posición que en
		// columnList
		Double[] columnWidths;

		if (esPrincipal) {
			columnWidths = new Double[] { 140.0, // nombre
//					40.0, // caja
					46.0, // numero
					140.0, // variante
					110.0, // firma
					75.0, // editorial
					97.0, // formato
					90.0, // procedencia
					95.0, // fecha
					150.0, // guionista
					150.0, // dibujante
					85.0, // referencia
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

		// Aplicar los anchos específicos a cada columna
		for (int i = 0; i < getReferenciaVentana().getColumnasTabla().size(); i++) {
			TableColumn<Comic, String> column = getReferenciaVentana().getColumnasTabla().get(i);
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
		Text text = new Text(getReferenciaVentana().getProntInfo().getText());

		// Configurar el mismo estilo que tiene el TextArea
		text.setFont(getReferenciaVentana().getProntInfo().getFont());

		double textHeight = text.getLayoutBounds().getHeight();

		getReferenciaVentana().getProntInfo().setPrefHeight(textHeight);
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		FuncionesTableView.referenciaVentana = referenciaVentana;
	}

}
