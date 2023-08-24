package Funcionamiento;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import JDBC.DBLibreriaManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class FuncionesComboBox {

	private static DBLibreriaManager libreria = null;

	// Mapa que almacena los elementos originales de los ComboBox junto a los ComboBox correspondientes
	private Map<ComboBox<String>, ObservableList<String>> originalComboBoxItems = new HashMap<>();

	// Variable que controla si la entrada es proporcionada por el usuario
	private boolean isUserInput = true;

	// Variable que indica si se están actualizando los ComboBoxes
	private boolean updatingComboBoxes = false;


	/**
	 * Crea y devuelve un objeto Comic con valores obtenidos de los ComboBoxes.
	 *
	 * @param totalComboboxes El número total de ComboBoxes.
	 * @param comboboxes      La lista de ComboBoxes que contienen los valores del
	 *                        Comic.
	 * @return Un objeto Comic con los valores seleccionados en los ComboBoxes.
	 */
	private Comic getComicFromComboBoxes(int totalComboboxes, List<ComboBox<String>> comboboxes) {
		Comic comic = new Comic();

		for (int i = 0; i < totalComboboxes; i++) {
			ComboBox<String> comboBox = comboboxes.get(i);
			modificarPopup(comboBox);
			String value = comboBox.getValue() != null ? comboBox.getValue() : "";

			switch (i) {
			case 0:
				comic.setNombre(value);
				break;
			case 1:
				comic.setNumero(value);
				break;
			case 2:
				comic.setVariante(value);
				break;
			case 3:
				comic.setProcedencia(value);
				break;
			case 4:
				comic.setFormato(value);
				break;
			case 5:
				comic.setDibujante(value);
				break;
			case 6:
				comic.setGuionista(value);
				break;
			case 7:
				comic.setEditorial(value);
				break;
			case 8:
				comic.setFirma(value);
				break;
			case 9:
				comic.setNumCaja(value);
				break;
			// Add more cases for additional comboboxes if needed
			default:
				break;
			}
		}
		return comic;
	}

	/**
	 * Realiza la lectura y configuración de los ComboBoxes de la interfaz gráfica.
	 * Asigna escuchadores para detectar cambios en los ComboBoxes y en sus campos
	 * de texto. Actualiza los ComboBoxes según los cambios realizados por el
	 * usuario. Además, comprueba si solo un ComboBox está lleno y llama a la
	 * función rellenarComboBox().
	 *
	 * @param totalComboboxes El número total de ComboBoxes.
	 * @param comboboxes      La lista de ComboBoxes.
	 */
	public void lecturaComboBox(int totalComboboxes, List<ComboBox<String>> comboboxes) {
		isUserInput = true; // Establecemos isUserInput en true inicialmente.

		// Configuración de los escuchadores para cada ComboBox mediante un bucle
		for (int i = 0; i < totalComboboxes; i++) {

			final int currentIndex = i; // Copia final de i para usar en expresiones lambda

			ComboBox<String> comboBox = comboboxes.get(i);

			originalComboBoxItems.put(comboBox, FXCollections.observableArrayList(comboBox.getItems()));

			comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
				comboBox.hide();
				if (!isUserInput || updatingComboBoxes) {
					return; // Ignorar cambios programáticos o cuando updatingComboBoxes es verdadero
				}
				if (newValue == null || newValue.isEmpty()) {
					modificarPopup(comboBox);
					handleComboBoxEmptyChange(comboBox, comboboxes);
				} else {
					modificarPopup(comboBox);
					Comic comic = getComicFromComboBoxes(totalComboboxes, comboboxes);
					actualizarComboBoxes(totalComboboxes, comboboxes, comic);
					updateOtherComboBoxes(comboboxes, currentIndex, newValue); // Call the new method here

				}

			});
		}
	}

	/**
	 * Maneja los cambios en el ComboBox cuando su texto está vacío. Restablece el
	 * valor del ComboBox a null y verifica si todos los campos de texto de los
	 * ComboBoxes están vacíos. Si todos están vacíos, llama a la función
	 * "limpiezaDeDatos()".
	 *
	 * @param comboBox El ComboBox que ha cambiado su valor.
	 */
	private void handleComboBoxEmptyChange(ComboBox<String> comboBox, List<ComboBox<String>> comboboxes) {
		if (originalComboBoxItems != null) {
			isUserInput = false;
			modificarPopup(comboBox);
			comboBox.setValue(null); // Establecer a null para restablecer la selección del ComboBox

			isUserInput = true;

			// Comprobar si algún campo de texto del ComboBox actual está vacío
			boolean anyEmpty = comboboxes.stream().anyMatch(cb -> cb.getEditor().getText().isEmpty());

			// Si todos los campos de texto de los ComboBoxes están vacíos, llamar a
			// limpiezaDeDatos()
			if (anyEmpty) {
				limpiezaDeDatos(comboboxes);
			}
		}
	}

	/**
	 * Actualiza los ComboBoxes con los resultados obtenidos de la base de datos
	 * según el Comic proporcionado.
	 *
	 * @param totalComboboxes El número total de ComboBoxes.
	 * @param comboboxes      La lista de ComboBoxes.
	 * @param comic           El Comic que se utilizará como base para obtener los
	 *                        resultados de la base de datos.
	 */
	public void actualizarComboBoxes(int totalComboboxes, List<ComboBox<String>> comboboxes, Comic comic) {

		libreria = new DBLibreriaManager();

		Comic comicTemp = new Comic("", comic.getNombre(), comic.getNumCaja(), comic.getNumero(), comic.getVariante(),
				comic.getFirma(), comic.getEditorial(), comic.getFormato(), comic.getProcedencia(), "",
				comic.getGuionista(), comic.getDibujante(), "", "", "", "", "", "");
		String sql = libreria.datosConcatenados(comicTemp);

		if (!sql.isEmpty()) {
			isUserInput = false; // Disable user input during programmatic updates

			DBLibreriaManager.nombreComicList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "nomComic");
			DBLibreriaManager.nombreGuionistaList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "nomGuionista");
			DBLibreriaManager.numeroComicList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "numComic");
			DBLibreriaManager.nombreVarianteList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "nomVariante");
			DBLibreriaManager.numeroCajaList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "caja_deposito");
			DBLibreriaManager.nombreProcedenciaList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "procedencia");
			DBLibreriaManager.nombreFormatoList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "formato");
			DBLibreriaManager.nombreEditorialList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "nomEditorial");
			DBLibreriaManager.nombreDibujanteList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "nomDibujante");
			DBLibreriaManager.nombreFirmaList = libreria.obtenerResultadosDeLaBaseDeDatos(sql, "firma");

			DBLibreriaManager.listaOrdenada = Arrays.asList(DBLibreriaManager.nombreComicList,
					DBLibreriaManager.numeroComicList, DBLibreriaManager.nombreVarianteList,
					DBLibreriaManager.nombreProcedenciaList, DBLibreriaManager.nombreFormatoList,
					DBLibreriaManager.nombreDibujanteList, DBLibreriaManager.nombreGuionistaList,
					DBLibreriaManager.nombreEditorialList, DBLibreriaManager.nombreFirmaList,
					DBLibreriaManager.numeroCajaList);

			for (int i = 0; i < totalComboboxes; i++) {
				comboboxes.get(i).hide();
				List<String> itemsActuales = DBLibreriaManager.listaOrdenada.get(i);
				if (itemsActuales != null && !itemsActuales.isEmpty()) {
					ObservableList<String> itemsObservable = FXCollections.observableArrayList(itemsActuales);
					comboboxes.get(i).setItems(itemsObservable);

					itemsActuales = FXCollections.observableArrayList(itemsActuales);
				}
			}
			isUserInput = true; // Re-enable user input after programmatic updates
		}
	}

	/**
	 * Limpia los datos de los ComboBoxes y restablece sus valores originales.
	 *
	 * @param comboboxes La lista de ComboBoxes a limpiar y restablecer.
	 */
	private void limpiezaDeDatos(List<ComboBox<String>> comboboxes) {

		isUserInput = false; // Deshabilitar la entrada del usuario durante la limpieza

		// Limpiar todos los campos de texto y valores de los ComboBoxes
		for (ComboBox<String> comboBox : comboboxes) {
			modificarPopup(comboBox);
			comboBox.setValue("");
			comboBox.getEditor().setText("");
		}

		// Restaurar los elementos originales para cada ComboBox
		for (ComboBox<String> comboBox : originalComboBoxItems.keySet()) {
			ObservableList<String> originalItems = originalComboBoxItems.get(comboBox);
			comboBox.setItems(originalItems);

			// Configurar el tamaño y apariencia del despliegue del ComboBox
			modificarPopup(comboBox);
		}
		isUserInput = true; // Habilitar nuevamente la entrada del usuario después de la limpieza
		rellenarComboBox(comboboxes); // Rellenar los ComboBoxes con nuevos datos
	}

	/**
	 * Rellena los ComboBoxes con los elementos proporcionados en la lista de
	 * elementos.
	 *
	 * @param comboboxes La lista de ComboBoxes a rellenar.
	 */
	public void rellenarComboBox(List<ComboBox<String>> comboboxes) {

		List<List<String>> itemsList = Arrays.asList(DBLibreriaManager.listaNombre, DBLibreriaManager.listaNumeroComic,
				DBLibreriaManager.listaVariante, DBLibreriaManager.listaProcedencia, DBLibreriaManager.listaFormato,
				DBLibreriaManager.listaDibujante, DBLibreriaManager.listaGuionista, DBLibreriaManager.listaEditorial,
				DBLibreriaManager.listaFirma, DBLibreriaManager.listaCaja);

		int i = 0;
		for (ComboBox<String> comboBox : comboboxes) {
			modificarPopup(comboBox);

			List<String> items = itemsList.get(i);
			try {
				final int currentIndex = i; // Copia final de i para usar en expresiones lambda

				if (items != null && !items.isEmpty()) {
					ObservableList<String> itemsCopy = FXCollections.observableArrayList(items);
					comboBox.setItems(itemsCopy);
				}

				// Configurar el tamaño y apariencia del despliegue del ComboBox
				comboBox.setCellFactory(param -> new ListCell<String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setGraphic(null);
							setPrefHeight(0); // Ajusta la altura
							setPrefWidth(0); // Ajusta el ancho
						} else {
							setText(item);
							setPrefHeight(-1); // Ajusta la altura
							setPrefWidth(-1); // Ajusta el ancho
						}
					}
				});

				comboBox.setOnMouseClicked(event -> {
					comboBox.hide();
					if (!comboBox.isShowing()) {
						boolean atLeastOneNotEmpty = comboboxes.stream()
								.anyMatch(cb -> cb.getValue() != null && !cb.getValue().isEmpty());

						if (atLeastOneNotEmpty) {
							Comic comic = getComicFromComboBoxes(10, comboboxes);
							setupFilteredPopup(comboboxes, comboBox, DBLibreriaManager.listaOrdenada.get(currentIndex));
							actualizarComboBoxes(10, comboboxes, comic);
						} else {
							Comic comic = getComicFromComboBoxes(10, comboboxes);
							setupFilteredPopup(comboboxes, comboBox, items);
							actualizarComboBoxes(10, comboboxes, comic);
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			i++;
		}
	}

	/**
	 * Configura el Popup filtrado con las opciones proporcionadas.
	 *
	 * @param comboboxes       La lista de ComboBoxes en la que operar.
	 * @param originalComboBox El ComboBox original.
	 * @param filteredItems    Los elementos filtrados a mostrar.
	 */
	private void setupFilteredPopup(List<ComboBox<String>> comboboxes, ComboBox<String> originalComboBox,
	        List<String> filteredItems) {
	    modificarPopup(originalComboBox);

	    ListView<String> listView = new ListView<>(FXCollections.observableArrayList(filteredItems));

	    TextField filterTextField = new TextField();
	    filterTextField.setPromptText("Filtro...");

	    StringProperty filteredText = new SimpleStringProperty();
	    filterTextField.textProperty().bindBidirectional(filteredText);

	    // Listener para el cambio de texto en el TextField de filtro
	    filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
	        List<String> newFilteredItems = filteredItems.stream()
	                .filter(item -> item.toLowerCase().contains(newValue.toLowerCase())).collect(Collectors.toList());
	        listView.setItems(FXCollections.observableArrayList(newFilteredItems));
	    });

	    VBox popupContent = new VBox(filterTextField, listView);
	    popupContent.setSpacing(-2);

	    ScrollPane scrollPane = new ScrollPane(popupContent);
	    scrollPane.setFitToHeight(true);
	    scrollPane.setFitToWidth(true);
	    scrollPane.setPrefSize(300, 250);

	    Popup popup = new Popup();
	    popup.getContent().add(scrollPane);
	    popup.setAutoHide(true);
	    Bounds bounds = originalComboBox.localToScreen(originalComboBox.getBoundsInLocal());

	    int currentIndex = comboboxes.indexOf(originalComboBox);

	    // Evento para manejar la tecla Enter en la lista
	    listView.setOnKeyPressed(event -> {
	        modificarPopup(originalComboBox);

	        if (event.getCode() == KeyCode.ENTER) {
	            String selectedItem = listView.getSelectionModel().getSelectedItem();
	            if (selectedItem != null) {
	                originalComboBox.setValue(selectedItem);
	                originalComboBox.hide();

	                updateOtherComboBoxes(comboboxes, currentIndex, selectedItem);
	                filterTextField.setText(selectedItem); // Establecer el valor en el TextField
	                popup.hide();
	            }
	        }
	    });

	    // Evento para manejar el clic en un elemento de la lista
	    listView.setOnMouseClicked(event -> {
	        modificarPopup(originalComboBox);

	        String selectedItem = listView.getSelectionModel().getSelectedItem();
	        if (selectedItem != null) {
	            originalComboBox.setValue(selectedItem);
	            originalComboBox.hide();

	            updateOtherComboBoxes(comboboxes, currentIndex, selectedItem);
	            filterTextField.setText(selectedItem); // Establecer el valor en el TextField
	            popup.hide();
	        }
	    });

	    // Evento para manejar el clic en el ComboBox original
	    originalComboBox.setOnMouseClicked(event -> {
	        modificarPopup(originalComboBox);

	        if (!popup.isShowing()) {
	            showFilteredPopup(popup, originalComboBox, filterTextField, filteredText, bounds);
	        } else {
	            originalComboBox.hide();
	            popup.hide();
	        }
	    });

	    // Evento para manejar las teclas presionadas en el TextField de filtro
	    filterTextField.setOnKeyPressed(event -> {
	        modificarPopup(originalComboBox);
	        KeyCode code = event.getCode();
	        if (code == KeyCode.BACK_SPACE || code == KeyCode.DELETE) {
	            originalComboBox.setValue("");
	            originalComboBox.hide();
	        }
	    });

	    // Evento para manejar las teclas liberadas en el ComboBox original
	    originalComboBox.setOnKeyReleased(event -> {
	        KeyCode code = event.getCode();
	        if (code == KeyCode.BACK_SPACE || code == KeyCode.DELETE) {
	            modificarPopup(originalComboBox);
	            originalComboBox.hide();
	            boolean atLeastOneNotEmpty = comboboxes.stream()
	                    .anyMatch(cb -> cb.getValue() != null && !cb.getValue().isEmpty());

	            System.out.println(atLeastOneNotEmpty);

	            if (atLeastOneNotEmpty) {
	                Comic comic = getComicFromComboBoxes(10, comboboxes);
	                actualizarComboBoxes(10, comboboxes, comic);
	            }
	        }
	    });

	    // Mostrar el popup personalizado al hacer clic en el ComboBox
	    showFilteredPopup(popup, originalComboBox, filterTextField, filteredText, bounds);

	    if (!listView.getItems().isEmpty()) {
	        listView.requestFocus(); // Solicitar el enfoque en la lista
	        listView.getSelectionModel().selectFirst();
	    }
	}


	/**
	 * Muestra el Popup filtrado.
	 *
	 * @param popup             El Popup que se va a mostrar.
	 * @param originalComboBox  El ComboBox original.
	 * @param filterTextField   El TextField de filtrado.
	 * @param filteredText      La propiedad del texto filtrado.
	 * @param bounds            Los límites del área de visualización.
	 */
	private void showFilteredPopup(Popup popup, ComboBox<String> originalComboBox, TextField filterTextField,
	                               StringProperty filteredText, Bounds bounds) {

	    Bounds comboBoxBounds = originalComboBox.getBoundsInLocal();
	    Bounds screenBounds = originalComboBox.localToScreen(comboBoxBounds);

	    double defaultX = screenBounds.getMinX() + comboBoxBounds.getMinX();
	    double defaultY = screenBounds.getMaxY();

	    popup.show(originalComboBox, defaultX, defaultY);

	    // filterTextField.requestFocus(); // Solicitar el enfoque en el campo de texto
	    filterTextField.setText(filteredText.get()); // Configurar el texto filtrado
	}

	/**
	 * Modifica el Popup del ComboBox.
	 *
	 * @param originalComboBox  El ComboBox original.
	 */
	private void modificarPopup(ComboBox<String> originalComboBox) {
	    originalComboBox.hide();
	    // Configurar el tamaño y apariencia del despliegue del ComboBox
	    originalComboBox.setCellFactory(param -> new ListCell<String>() {
	        @Override
	        protected void updateItem(String item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty || item == null) {
	                setGraphic(null);
	                setPrefHeight(0); // Ajusta la altura
	                setPrefWidth(0);  // Ajusta el ancho
	            } else {
	                setText(item);
	                setPrefHeight(-1); // Ajusta la altura
	                setPrefWidth(-1);  // Ajusta el ancho
	            }
	        }
	    });
	}

	/**
	 * Actualiza otros ComboBoxes cuando se selecciona un elemento.
	 *
	 * @param comboboxes     La lista de ComboBoxes.
	 * @param currentIndex   El índice del ComboBox actual.
	 * @param selectedItem  El elemento seleccionado.
	 */
	private void updateOtherComboBoxes(List<ComboBox<String>> comboboxes, int currentIndex, String selectedItem) {
	    isUserInput = false; // Deshabilitar la entrada del usuario durante actualizaciones programáticas

	    // Iterar a través de todos los ComboBoxes excepto el actual
	    for (int i = 0; i < comboboxes.size(); i++) {
	        if (i != currentIndex) {
	            ComboBox<String> comboBox = comboboxes.get(i);
	            @SuppressWarnings("unchecked")
	            List<String> originalItems = (List<String>) comboBox.getUserData();

	            if (originalItems != null && !originalItems.isEmpty()) {
	                ObservableList<String> filteredItems = FXCollections.observableArrayList(originalItems.stream()
	                        .filter(item -> item.toLowerCase().contains(selectedItem.toLowerCase()))
	                        .collect(Collectors.toList()));
	                comboBox.setItems(filteredItems);
	            }
	        }
	    }

	    isUserInput = true; // Re-habilitar la entrada del usuario después de actualizaciones programáticas
	}


	/**
	 * Crea un TextFormatter para validar y permitir solo números enteros.
	 *
	 * @return El TextFormatter para números enteros.
	 */
	public static TextFormatter<Integer> validador_Nenteros() {
	    // Crear un validador para permitir solo números enteros
	    TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
	        if (change.getControlNewText().matches("\\d*")) {
	            return change;
	        }
	        return null;
	    });

	    return textFormatter;
	}

	/**
	 * Crea un TextFormatter para validar y permitir solo números decimales (double).
	 *
	 * @return El TextFormatter para números decimales.
	 */
	public static TextFormatter<Double> validador_Ndecimales() {
	    // Crear un validador para permitir solo números decimales (double)
	    TextFormatter<Double> textFormatter = new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
	        String newText = change.getControlNewText();
	        if (newText.matches("\\d*\\.?\\d*")) {
	            return change;
	        }
	        return null;
	    });

	    return textFormatter;
	}

	/**
	 * Rellena los ComboBoxes con valores estáticos predefinidos.
	 *
	 * @param comboboxes La lista de ComboBoxes a rellenar.
	 */
	public void rellenarComboBoxEstaticos(List<ComboBox<String>> comboboxes) {
	    ObservableList<String> formatoNuevo = FXCollections.observableArrayList(/* ... */);
	    comboboxes.get(0).setItems(formatoNuevo);
	    comboboxes.get(0).getSelectionModel().selectFirst();

	    ObservableList<String> procedenciaEstadoNuevo = FXCollections.observableArrayList(/* ... */);
	    comboboxes.get(1).setItems(procedenciaEstadoNuevo);
	    comboboxes.get(1).getSelectionModel().selectFirst();

	    ObservableList<String> situacionEstado = FXCollections.observableArrayList(/* ... */);
	    comboboxes.get(2).setItems(situacionEstado);
	    comboboxes.get(2).getSelectionModel().selectFirst();
	}


}
