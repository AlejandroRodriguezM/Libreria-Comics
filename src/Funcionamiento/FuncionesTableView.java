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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class FuncionesTableView {

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
					super.updateItem(item, empty);
					if (empty || item == null) {
						setGraphic(null);
						System.out.println("Hola");
					} 
					else {
						if (!item.equals(lastItem)) { // Verificar si el contenido ha cambiado
							lastItem = item;
							vbox.getChildren().clear();

								VBox hyperlinkVBox = new VBox();
								Hyperlink hyperlink;
								if (isValidUrl(item)) {
									ReferenciaHyperlink referenciaHyperlink = new ReferenciaHyperlink("Referencia",
											item);
									hyperlink = new Hyperlink(referenciaHyperlink.getDisplayText());
									hyperlink.setOnAction(event -> {
										// Implement the behavior when the hyperlink is clicked
										if (Desktop.isDesktopSupported()) {
											try {
												Desktop.getDesktop().browse(new URI(referenciaHyperlink.getUrl()));
											} catch (IOException | URISyntaxException e) {
												e.printStackTrace();
											}
										}
									});
								} else {
									// Not a valid URL, set the displayed text as "Sin Referencia" (non-clickable)
									Text text = new Text("Sin \nReferencia");
									hyperlinkVBox.getChildren().add(text);
									hyperlink = new Hyperlink();
								}

								hyperlink.getStyleClass().add("hyperlink");
								hyperlinkVBox.getChildren().add(hyperlink);
								vbox.getChildren().add(hyperlinkVBox);
						}
						setGraphic(vbox);
					}
				}
			};
		});
	}

	// Check if a given string is a valid URL
	public static boolean isValidUrl(String url) {
		String urlRegex = "^(https?|ftp)://.*$";
		Pattern pattern = Pattern.compile(urlRegex);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}
	
	/**
	 * Cuando pasa el raton por encima, se colorea de color azul el raw donde el
	 * raton se encuentra y muestra un mensaje emergente con datos del comic
	 */
	public void seleccionarRaw(TableView<Comic> tablaBBDD) {
		tablaBBDD.setRowFactory(tv -> {
			TableRow<Comic> row = new TableRow<>();
			Tooltip tooltip = new Tooltip();
			tooltip.setShowDelay(Duration.ZERO);
			tooltip.setHideDelay(Duration.ZERO);

			row.setOnMouseEntered(event -> {
				if (!row.isEmpty()) {
					row.setStyle("-fx-background-color: #BFEFFF;");

					Comic comic = row.getItem();
					if (comic != null && !tooltip.isShowing()) {
						String mensaje = "Nombre: " + comic.getNombre() + "\nNumero: " + comic.getNumero()
								+ "\nVariante: " + comic.getVariante() + "\nGuionista: " + comic.getGuionista()
								+ "\nDibujante: " + comic.getDibujante();
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
	public void actualizarBusquedaRaw(TableColumn<Comic, String> columna, TableView<Comic> tablaBBDD, List<TableColumn<Comic, String>> columnList) {
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
	                                        || columna.getText().equalsIgnoreCase("Origen")) {
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
	public void tablaBBDD(List<Comic> listaComic, TableView<Comic> tablaBBDD, List<TableColumn<Comic, String>> columnList) {
	    tablaBBDD.getColumns().setAll(columnList);
	    tablaBBDD.getItems().setAll(listaComic);
	}


	public void columnaSeleccionada( TableView<Comic> tablaBBDD,List<TableColumn<Comic, String>> columnList,String rawSelecionado) throws SQLException {
		libreria = new DBLibreriaManager();
		libreria.reiniciarBBDD();
		nombreColumnas(columnList,tablaBBDD);
		tablaBBDD(libreria.libreriaSeleccionado(rawSelecionado),tablaBBDD,columnList);
	}
	
	/**
	 * Permite dar valor a las celdas de la TableView
	 */
	public void nombreColumnas(List<TableColumn<Comic, String>> columnList, TableView<Comic> tablaBBDD) {
	    for (TableColumn<Comic, String> column : columnList) {
	        String columnName = column.getText(); // Obtiene el nombre de la columna
	        
	        if(columnName.equalsIgnoreCase("Nº")) {
	        	columnName = "Numero";
	        }
	        
	        if(columnName.equalsIgnoreCase("Caja")) {
	        	columnName = "numCaja";
	        }
	        
	        if(columnName.equalsIgnoreCase("Referencia")) {
	        	columnName = "url_referencia";
	        }
	        
	        if(columnName.equalsIgnoreCase("Origen")) {
	        	columnName = "Procedencia";
	        }
	        
	        
	        PropertyValueFactory<Comic, String> valueFactory = new PropertyValueFactory<>(columnName);
	        column.setCellValueFactory(valueFactory);
	        
	        actualizarBusquedaRaw(column,tablaBBDD,columnList);
	    }
	}
	
	/**
	 * Funcion que modifica el tamaño de los TableColumn
	 */
	public void modificarColumnas(TableView<Comic> tablaBBDD, List<TableColumn<Comic, String>> columnList) {
	    // Definir los tamaños específicos para cada columna en la misma posición que en columnList
	    Double[] columnWidths = {
	        140.0,  // nombre
	        37.0,   // caja
	        45.0,   // numero
	        135.0,  // variante
	        85.0,   // firma
	        78.0,   // editorial
	        92.0,    // formato
	        75.0,   // procedencia
	        105.0,  // fecha
	        145.0,  // guionista
	        150.0,  // dibujante
	        90.0,   // referencia
	        
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
	
}
