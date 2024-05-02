/**
 * Contiene las clases que hacen funcionar las diferentes funciones de uso de back end y front de todo el proyecto
 *  
*/
package controlUI;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Introducir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *  - Puntuar comics que se encuentren dentro de la base de datos.
 *  Esta clase permite acceder al menu principal donde se puede viajar a diferentes ventanas, etc.
 *
 *  Version 8.0.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controladores.managment.AccionReferencias;
import comicManagement.Comic;
import dbmanager.DBUtilidades;
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.stage.Screen;

/**
 * Clase que contiene diversas funciones relacionadas con TableView y
 * operaciones en la interfaz de usuario.
 */
public class FuncionesTableView {

	/**
	 * Fuente utilizada para los tooltips en la interfaz gráfica.
	 */
	private static final Font TOOLTIP_FONT = Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 13);

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

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

	public static void seleccionarRaw() {
		getReferenciaVentana().getTablaBBDD().setRowFactory(tv -> {
			TableRow<Comic> row = new TableRow<>();
			Tooltip tooltip = new Tooltip();
			tooltip.setShowDelay(Duration.ZERO);
			tooltip.setHideDelay(Duration.ZERO);
			tooltip.setFont(TOOLTIP_FONT);
			row.setOnMouseMoved(event -> {
				if (!row.isEmpty()) {
					row.setStyle("-fx-background-color: #BFEFFF;");

					Comic comic = row.getItem();
					if (comic != null && !tooltip.isShowing()) {
						String mensaje = "Nombre: " + comic.getNombre() + "\nNúmero: " + comic.getNumero()
								+ "\nVariante: " + comic.getVariante() + "\nPrecio: "
								+ (!comic.getprecioComic().isEmpty() ? comic.getprecioComic() + " $" : "");

						if (!comic.getFirma().isEmpty()) {
							mensaje += "\nFirma: " + comic.getFirma();
						}
						tooltip.setText(mensaje);

						// Obtener las dimensiones de la pantalla
						Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

						// Ajustar la posición del tooltip para que no salga del área de la aplicación
						double posX = event.getScreenX() + 10;
						double posY = event.getScreenY() - 20;

						// Verificar si el tooltip está fuera de los límites de la ventana principal
						if (posX + tooltip.getWidth() > screenBounds.getMaxX()) {
							posX = screenBounds.getMaxX() - tooltip.getWidth();
						}
						if (posY + tooltip.getHeight() > screenBounds.getMaxY()) {
							posY = screenBounds.getMaxY() - tooltip.getHeight();
						}

						tooltip.show(row, posX, posY);
					}
				}
			});

			row.setOnMouseExited(event -> {
				if (!row.isEmpty()) {
					Bounds rowBounds = row.getBoundsInLocal();
					double mouseX = event.getSceneX();
					double mouseY = event.getSceneY();

					if (!rowBounds.contains(mouseX, mouseY)) {
						row.setStyle(""); // Restaura el estilo por defecto solo si el ratón está fuera del área del
											// nodo
						tooltip.hide(); // Oculta el tooltip
					}
				}
			});

			return row;
		});

		// Deshabilitar el enfoque en el TableView
		getReferenciaVentana().getTablaBBDD().setFocusTraversable(false);

		Scene scene = getReferenciaVentana().getTablaBBDD().getScene();
		if (scene != null) {
			VBox root = (VBox) scene.lookup("#rootVBox");
			if (root != null) {
				root.requestFocus();
			}
		}
	}

	/**
	 * Funcion que permite que los diferentes raw de los TableColumn se puedan
	 * pinchar. Al hacer, genera un nuevo tipo de busqueda personalizada solo con el
	 * valor que hemos pinchado
	 * 
	 * @param columna
	 */
	public static void actualizarBusquedaRaw(TableView<Comic> tablaBBDD) {
		getReferenciaVentana().getColumnasTabla().forEach(columna -> {
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
											busquedaHyperLink(columna);

										} else if (columna.getText().equalsIgnoreCase("fecha")
												|| columna.getText().equalsIgnoreCase("editorial")
												|| columna.getText().equalsIgnoreCase("formato")
												|| columna.getText().equalsIgnoreCase("Nombre")
												|| columna.getText().equalsIgnoreCase("Nº")
												|| columna.getText().equalsIgnoreCase("Gradeo")
												|| columna.getText().equalsIgnoreCase("Origen")) {
											label = new Label(nombre + "\n");
										} else if (columna.getText().equalsIgnoreCase("variante")
												|| columna.getText().equalsIgnoreCase("firma")) {
											label = new Label("◉ " + nombre + "\n");
										} else {
											label = new Label("◉ " + nombre + "\n");
										}
										label.getStyleClass().add("hyperlink");
										Hyperlink hyperlink = new Hyperlink();
										hyperlink.setGraphic(label);
										hyperlink.setOnAction(event -> columnaSeleccionada(tablaBBDD, nombre));
										vbox.getChildren().add(hyperlink);
									}
								}
							}
							setGraphic(vbox);
						}
					}
				};
			});
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
	public static TextArea resultadoBusquedaPront(Comic comic) {
		TextArea prontInfoTable = new TextArea(); // Crear un nuevo TextArea

		if (Comic.validarComic(comic)) {
			int totalComics = DBUtilidades.numeroTotalSelecionado(comic);
			prontInfoTable.setStyle("-fx-text-fill: black;"); // Reset the text color to black
			prontInfoTable.setText("El número de cómics donde aparece la búsqueda es: " + totalComics + "\n \n \n");
		}

		return prontInfoTable;
	}

	/**
	 * Obtiene los datos de los comics de la base de datos y los devuelve en el
	 * textView
	 *
	 * @param listaComic
	 */
	public static void tablaBBDD(List<Comic> listaComic, TableView<Comic> tablaBBDD) {
		tablaBBDD.getColumns().setAll(getReferenciaVentana().getColumnasTabla());
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
	public static void columnaSeleccionada(TableView<Comic> tablaBBDD, String rawSelecionado) {
		ListaComicsDAO.reiniciarListaComics();
		nombreColumnas();

		tablaBBDD(SelectManager.libreriaSeleccionado(rawSelecionado), tablaBBDD);

		// Deseleccionar la fila seleccionada
		tablaBBDD.getSelectionModel().clearSelection();
	}

	/**
	 * Configura el nombre y la fábrica de valores para las columnas de una tabla de
	 * datos basado en una lista de columnas.
	 *
	 * @param columnList La lista de TableColumn a configurar.
	 * @param tablaBBDD  La TableView en la que se aplicarán las configuraciones.
	 */
	public static void nombreColumnas() {
		for (TableColumn<Comic, String> column : getReferenciaVentana().getColumnasTabla()) {
			String columnName = column.getText(); // Obtiene el nombre de la columna

			if (columnName.equalsIgnoreCase("Nº")) {
				columnName = "Numero";
			}

			if (columnName.equalsIgnoreCase("Gradeo")) {
				columnName = "gradeo";
			}

			if (columnName.equalsIgnoreCase("Referencia")) {
				columnName = "Referencia";
			}

			if (columnName.equalsIgnoreCase("Origen")) {
				columnName = "Procedencia";
			}

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
	public static void modificarColumnas() {

		for (TableColumn<Comic, String> column : getReferenciaVentana().getColumnasTabla()) {
			column.prefWidthProperty().unbind(); // Desvincular cualquier propiedad prefWidth existente
		}

		// Definir los tamaños específicos para cada columna en la misma posición que en
		// columnList
		Double[] columnWidths = { 140.0, // nombre
				37.0, // caja
				49.0, // numero
				135.0, // variante
				110.0, // firma
				78.0, // editorial
				92.0, // formato
				75.0, // procedencia
				105.0, // fecha
				145.0, // guionista
				150.0, // dibujante
				92.0, // referencia

		};

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
