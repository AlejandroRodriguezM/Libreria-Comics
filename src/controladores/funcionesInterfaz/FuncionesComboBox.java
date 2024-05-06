package controladores.funcionesInterfaz;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import comicManagement.Comic;
import dbmanager.DBUtilidades;
import dbmanager.ListaComicsDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * Clase que define funciones para el manejo y configuración de ComboBoxes en
 * una interfaz gráfica.
 */
public class FuncionesComboBox {

	// Mapa que almacena los elementos originales de los ComboBox junto a los
	// ComboBox correspondientes
	private Map<ComboBox<String>, ObservableList<String>> originalComboBoxItems = new HashMap<>();

	// Variable que controla si la entrada es proporcionada por el usuario
	private boolean isUserInput = true;

	// Variable que indica si se están actualizando los ComboBoxes
	private boolean updatingComboBoxes = false;

	private Popup popup = new Popup();

	/**
	 * Crea y devuelve un objeto Comic con valores obtenidos de los ComboBoxes.
	 *
	 * @param totalComboboxes El número total de ComboBoxes.
	 * @param comboboxes      La lista de ComboBoxes que contienen los valores del
	 *                        Comic.
	 * @return Un objeto Comic con los valores seleccionados en los ComboBoxes.
	 */
	private Comic getComicFromComboBoxes(List<ComboBox<String>> comboboxes) {
		Comic comic = new Comic();
		int cantidadDeComboBoxes = comboboxes.size();
		for (int i = 0; i < cantidadDeComboBoxes; i++) {
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
				comic.setValorGradeo(value);
				break;
			// Add more cases for additional comboboxes if needed
			default:
				break;
			}
		}
		return comic;
	}

	/**
	 * Maneja los cambios en el ComboBox cuando su texto está vacío. Restablece el
	 * valor del ComboBox a null y verifica si todos los campos de texto de los
	 * ComboBoxes están vacíos. Si todos están vacíos, llama a la función
	 * "limpiezaDeDatos()".
	 *
	 * @param comboBox   El ComboBox que ha cambiado su valor.
	 * @param comboboxes Lista de ComboBoxes para verificar si todos los campos de
	 *                   texto están vacíos.
	 */
	private void handleComboBoxEmptyChange(ComboBox<String> comboBox, List<ComboBox<String>> comboboxes) {
		// Verificar si la lista original de elementos del ComboBox no es nula
		if (originalComboBoxItems != null) {
			// Deshabilitar la detección de entrada de usuario para evitar bucles no
			// deseados
			isUserInput = false;
			// Realizar modificaciones necesarias en el popup del ComboBox
			modificarPopup(comboBox);
			// Establecer el valor del ComboBox a null para restablecer la selección
			comboBox.setValue(null);

			// Habilitar nuevamente la detección de entrada de usuario
			isUserInput = true;

			// Comprobar si todos los campos de texto de los ComboBoxes están vacíos
			boolean allEmpty = comboboxes.stream().allMatch(cb -> cb.getValue() == null || cb.getValue().isEmpty());

			// Si todos los campos de texto de los ComboBoxes están vacíos, llamar a
			// limpiezaDeDatos()
			if (allEmpty) {
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
	 * @throws SQLException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void actualizarComboBoxes(List<ComboBox<String>> comboboxes, Comic comic) {

		Comic comicTemp = new Comic.ComicBuilder("", comic.getNombre()).valorGradeo("")
				.numero(comic.getNumero()).variante(comic.getVariante()).firma(comic.getFirma())
				.editorial(comic.getEditorial()).formato(comic.getFormato()).procedencia(comic.getProcedencia())
				.fecha("").guionista(comic.getGuionista()).dibujante(comic.getDibujante()).estado("").keyIssue("")
				.puntuacion("").imagen("").referenciaComic("").precioComic("").codigoComic("").build();

		String sql = DBUtilidades.datosConcatenados(comicTemp);
		if (!sql.isEmpty()) {
			isUserInput = false; // Disable user input during programmatic updates

			ListaComicsDAO.actualizarDatosAutoCompletado(sql);
			int cantidadDeComboBoxes = comboboxes.size();
			for (int i = 0; i < cantidadDeComboBoxes; i++) {
				comboboxes.get(i).hide();
				List<String> itemsActuales = ListaComicsDAO.listaOrdenada.get(i);
				if (itemsActuales != null && !itemsActuales.isEmpty()) {
					ObservableList<String> itemsObservable = FXCollections.observableArrayList(itemsActuales);
					comboboxes.get(i).setItems(itemsObservable);
				}
			}

			isUserInput = true; // Re-enable user input after programmatic updates
		}
	}

	public static boolean sonListasSimilares(List<List<String>> lista1, List<List<String>> lista2) {

		// Comparamos cada sublista
		for (int i = 0; i < lista1.size(); i++) {
			List<String> sublista1 = lista1.get(i);
			List<String> sublista2 = lista2.get(i);

			// Verificamos si hay al menos un elemento en común
			if (tieneElementoComun(sublista1, sublista2)) {
				return true;
			}
		}

		// Si llegamos aquí, no hay coincidencias en ninguna sublista
		return false;
	}

	private static boolean tieneElementoComun(List<String> lista1, List<String> lista2) {
		// Verificamos si hay al menos un elemento en común entre las listas
		for (String elemento : lista1) {
			if (lista2.contains(elemento)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Convierte una lista de cadenas que representan números en una lista de
	 * cadenas ordenada de menor a mayor.
	 *
	 * @param listaNumeros La lista de cadenas que representa números.
	 * @return Una lista de cadenas ordenada de menor a mayor.
	 */
	public List<String> convertirYOrdenarListaNumeros(List<String> listaNumeros) {
		// Convierte la lista de cadenas a una lista de enteros
		List<Integer> numerosEnteros = listaNumeros.stream().map(Integer::parseInt).collect(Collectors.toList());

		// Ordena la lista de enteros de menor a mayor
		Collections.sort(numerosEnteros);

		// Convierte la lista de enteros de nuevo a una lista de cadenas
		return numerosEnteros.stream().map(String::valueOf).toList();
	}

	/**
	 * Limpia los datos de los ComboBoxes y restablece sus valores originales.
	 *
	 * @param comboboxes La lista de ComboBoxes a limpiar y restablecer.
	 */
	private void limpiezaDeDatos(List<ComboBox<String>> comboboxes) {

		isUserInput = false; // Deshabilitar la entrada del usuario durante la limpieza

		// Restaurar los elementos originales para cada ComboBox
		for (Map.Entry<ComboBox<String>, ObservableList<String>> entry : originalComboBoxItems.entrySet()) {
			ComboBox<String> comboBox = entry.getKey();
			ObservableList<String> originalItems = entry.getValue();
			comboBox.setItems(originalItems);

			// Configurar el tamaño y apariencia del despliegue del ComboBox
			modificarPopup(comboBox);
		}

		isUserInput = true; // Habilitar nuevamente la entrada del usuario después de la limpieza
		rellenarComboBox(comboboxes); // Rellenar los ComboBoxes con nuevos datos
	}

	public void lecturaComboBox(List<ComboBox<String>> comboboxes) {
		isUserInput = true; // Establecemos isUserInput en true inicialmente.

		// Configuración de los escuchadores para cada ComboBox mediante un bucle
		for (ComboBox<String> comboBox : comboboxes) {
			originalComboBoxItems.put(comboBox, FXCollections.observableArrayList(comboBox.getItems()));

			comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
				if (!isUserInput || updatingComboBoxes) {
					return; // Ignorar cambios programáticos o cuando updatingComboBoxes es verdadero
				}

				if (newValue == null || newValue.isEmpty()) {
					modificarPopup(comboBox);
					handleComboBoxEmptyChange(comboBox, comboboxes);
				} else {
					modificarPopup(comboBox);
					Comic comic = getComicFromComboBoxes(comboboxes);
					actualizarComboBoxes(comboboxes, comic);
				}
			});
		}
	}

	public void rellenarComboBox(List<ComboBox<String>> comboboxes) {

		for (int i = 0; i < comboboxes.size(); i++) {
			ComboBox<String> comboBox = comboboxes.get(i);
			if (comboBox == null) {
				continue; // Evitar operaciones en ComboBox nulos
			}

			List<String> items = ListaComicsDAO.itemsList.get(i);
			if (items != null && !items.isEmpty() && !comboBox.getItems().equals(items)) {
				comboBox.setItems(FXCollections.observableArrayList(items));
			}

			// Configurar el evento de clic del mouse fuera del bucle
			setupComboBoxMouseClickEvent(comboboxes, comboBox, i);
		}
	}

	private void setupComboBoxMouseClickEvent(List<ComboBox<String>> comboboxes, ComboBox<String> comboBox, int index) {
		comboBox.setOnMousePressed(event -> {
		});

		comboBox.setOnMouseClicked(event -> handleComboBoxEvent(comboboxes, comboBox, index));
	}

	private void handleComboBoxEvent(List<ComboBox<String>> comboboxes, ComboBox<String> comboBox, int index) {
		boolean atLeastOneNotEmpty = comboboxes.stream()
				.anyMatch(cb -> cb.getValue() != null && !cb.getValue().isEmpty());

		List<String> currentItems = ListaComicsDAO.listaOrdenada.get(index);

		setupFilteredPopup(comboboxes, comboBox,
				atLeastOneNotEmpty ? currentItems : ListaComicsDAO.itemsList.get(index));

		Comic comic = getComicFromComboBoxes(comboboxes);
		actualizarComboBoxes(comboboxes, comic);
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

		Popup popup = createCustomPopup(originalComboBox, filterTextField, listView);

		Map<ComboBox<String>, Popup> comboBoxPopupMap = new HashMap<>();
		comboBoxPopupMap.put(originalComboBox, popup);

		int currentIndex = comboboxes.indexOf(originalComboBox);
		Map<ComboBox<String>, List<String>> comboBoxFilteredItemsMap = new HashMap<>();
		for (ComboBox<String> comboBox : comboboxes) {
			comboBoxFilteredItemsMap.put(comboBox, new ArrayList<>(filteredItems));
		}

		filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			List<String> currentFilteredItems = comboBoxFilteredItemsMap.get(originalComboBox);
			if (newValue != null && !newValue.isEmpty()) {

				List<String> newFilteredItems = ListaComicsDAO.listaOrdenada.get(comboboxes.indexOf(originalComboBox))
						.stream().filter(item -> item.toLowerCase().contains(newValue.toLowerCase())).toList();

				currentFilteredItems.clear();
				currentFilteredItems.addAll(newFilteredItems);
				listView.setItems(FXCollections.observableArrayList(currentFilteredItems));
			} else {
				originalComboBox.setValue("");
				Comic comic = getComicFromComboBoxes(comboboxes);
				actualizarComboBoxes(comboboxes, comic);
				List<String> allFilteredItems = new ArrayList<>(
						ListaComicsDAO.listaOrdenada.get(comboboxes.indexOf(originalComboBox)));
				currentFilteredItems.clear();
				currentFilteredItems.addAll(allFilteredItems);
				listView.setItems(FXCollections.observableArrayList(currentFilteredItems));
			}
		});

		originalComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.isEmpty()) {
				filterTextField.setText(newValue);
			}
		});

		EventHandler<KeyEvent> enterEventHandler = event -> {
			modificarPopup(originalComboBox);
			if (event.getCode() == KeyCode.ENTER) {
				String selectedItem = listView.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					originalComboBox.setValue(selectedItem);
					originalComboBox.hide();
					updateOtherComboBoxes(comboboxes, currentIndex, selectedItem);
					filterTextField.setText(selectedItem);
					comboBoxPopupMap.get(originalComboBox).hide();
				}
			}
		};
		listView.setOnKeyPressed(enterEventHandler);
		listView.setOnMouseClicked(event -> enterEventHandler
				.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false, false, false, false)));

		if (!popup.isShowing()) {
			showFilteredPopup(popup, originalComboBox, filterTextField, filteredText);
		} else {
			originalComboBox.hide();
			popup.hide();
		}

		// Después de crear el ListView
		listView.setCellFactory(list -> new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null) {
					setText(null);
					setStyle(null);
				} else {
					setText(item);

					setOnMouseEntered(event -> {
						setStyle("-fx-control-inner-background: #008ECC;");
					});
					setOnMouseExited(event -> {
						setStyle("-fx-control-inner-background: white;");
					});

				}
			}
		});
	}

	/**
	 * Crea un Popup personalizado con el contenido proporcionado.
	 *
	 * @param originalComboBox El ComboBox original.
	 * @param filterTextField  El TextField de filtro.
	 * @param listView         El ListView con elementos filtrados.
	 * @return El Popup personalizado.
	 */
	private Popup createCustomPopup(ComboBox<String> originalComboBox, TextField filterTextField,
			ListView<String> listView) {

		VBox popupContent = new VBox(filterTextField, listView);
		popupContent.setSpacing(-2);

		ScrollPane scrollPane = new ScrollPane(popupContent);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		scrollPane.setPrefSize(300, 250);

		popup.getContent().add(scrollPane);
		popup.setAutoHide(true);

		return popup;
	}

	/**
	 * Muestra el Popup filtrado.
	 *
	 * @param popup            El Popup que se va a mostrar.
	 * @param originalComboBox El ComboBox original.
	 * @param filterTextField  El TextField de filtrado.
	 * @param filteredText     La propiedad del texto filtrado.
	 * @param bounds           Los límites del área de visualización.
	 */
	private void showFilteredPopup(Popup popup, ComboBox<String> originalComboBox, TextField filterTextField,
			StringProperty filteredText) {

		Bounds comboBoxBounds = originalComboBox.getBoundsInLocal();
		Bounds screenBounds = originalComboBox.localToScreen(comboBoxBounds);

// Configurar las coordenadas para mostrar el Popup en el ComboBox que se hace clic
		double x = screenBounds.getMinX() + comboBoxBounds.getMinX();
		double y = screenBounds.getMaxY();

// Ajustar la posición del Popup si se sale de la pantalla en el eje Y
		if (screenBounds.getMaxY() + popup.getHeight() > Screen.getPrimary().getBounds().getMaxY()) {
			y = screenBounds.getMinY() - popup.getHeight();
		}

		popup.show(originalComboBox, x, y);

// Configurar el texto filtrado y solicitar el enfoque en el campo de texto
		filterTextField.setText(filteredText.get());
		filterTextField.requestFocus();
	}

	/**
	 * Modifica la apariencia del despliegue del ComboBox y lo oculta.
	 *
	 * @param originalComboBox El ComboBox original.
	 */
	private void modificarPopup(ComboBox<String> originalComboBox) {
		if (originalComboBox != null) {
			originalComboBox.hide();

			// Configurar el tamaño y apariencia del despliegue del ComboBox
			originalComboBox.setCellFactory(param -> new ListCell<String>() {
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
		}
	}

	/**
	 * Actualiza otros ComboBoxes cuando se selecciona un elemento.
	 *
	 * @param comboboxes   La lista de ComboBoxes.
	 * @param currentIndex El índice del ComboBox actual.
	 * @param selectedItem El elemento seleccionado.
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
							.filter(item -> item.toLowerCase().contains(selectedItem.toLowerCase())).toList());

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
		return new TextFormatter<>(new IntegerStringConverter(), null, change -> {
			if (change.getControlNewText().matches("\\d*")) {
				return change;
			}
			return null;
		});

	}

	public static TextFormatter<String> desactivarValidadorNenteros() {
		// Crear un TextFormatter que permita cualquier tipo de entrada
		return new TextFormatter<>(new DefaultStringConverter());

	}

	/**
	 * Crea un TextFormatter para validar y permitir solo números decimales
	 * (double).
	 *
	 * @return El TextFormatter para números decimales.
	 */
	public static TextFormatter<Double> validador_Ndecimales() {
		// Crear un validador para permitir solo números decimales (double)
		return new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*\\.?\\d*")) {
				return change;
			}
			return null;
		});
	}

	/**
	 * Rellena los ComboBoxes con valores estáticos predefinidos.
	 *
	 * @param comboboxes La lista de ComboBoxes a rellenar.
	 */
	public static void rellenarComboBoxEstaticos(List<ComboBox<String>> comboboxes) {
		String[][] valores = {
				{ "Grapa (Issue individual)", "Tapa blanda (Paperback)", "Edicion absolute (Absolute Edition)",
						"Cómic de bolsillo (Pocket)", "Edicion de lujo (Deluxe Edition)", "Edicion omnibus (Omnibus)",
						"Edicion integral (Integral)", "Tapa dura (Hardcover)", "eBook (libro electrónico)",
						"Comic digital (Digital Comic)", "Manga digital (Digital Manga)", "Manga (Manga tome)",
						"PDF (Portable Document Format)", "Revista (Magazine)",
						"Edicion de coleccionista (Collector's Edition)", "Edicion especial (Special Edition)",
						"Edicion con extras (Bonus Edition)", "Libro (Book)" },
				{ "NM (Noir Medium)", "SM (Standard Medium)", "LM (Light Medium)", "FL (Fine Light)", "VF (Very Fine)",
						"M (Medium)", "F (Fine)" },
				{ "En posesion", "Comprado", "En venta" },
				{ "Estados Unidos (United States)", "Japón (Japan)", "Francia (France)", "Italia (Italy)",
						"España (Spain)", "Reino Unido (United Kingdom)", "Alemania (Germany)", "Brasil (Brazil)",
						"Corea del Sur (South Korea)", "México (Mexico)", "Canadá (Canada)", "China (China)",
						"Australia (Australia)", "Argentina (Argentina)", "India (India)", "Bélgica (Belgium)",
						"Países Bajos (Netherlands)", "Portugal (Portugal)", "Suecia (Sweden)", "Suiza (Switzerland)",
						"Finlandia (Finland)", "Noruega (Norway)", "Dinamarca (Denmark)" },
				{ "0/0", "0.5/5", "1/5", "1.5/5", "2/5", "2.5/5", "3/5", "3.5/5", "4/5", "4.5/5", "5/5" } };

		String[] ids = { "formatoComic", "gradeoComic", "estadoComic", "procedenciaComic", "puntuacionMenu" };

		// Verificar que la lista de ComboBox tenga al menos el mismo tamaño que los
		// arreglos
		int tamanio = Math.min(comboboxes.size(), ids.length);

		for (int i = 0; i < tamanio; i++) {
			String id = ids[i];
			ComboBox<String> comboBox = comboboxes.stream().filter(cb -> cb.getId().equals(id)).findFirst()
					.orElse(null);
			if (comboBox != null) {
				comboBox.getItems().clear(); // Limpiar elementos anteriores si los hay
				comboBox.getItems().addAll(valores[i]);
				comboBox.getSelectionModel().selectFirst();
			}
		}
	}

	/**
	 * Funcion que permite modificar la puntuacion de un comic, siempre y cuando el
	 * ID exista en la base de datos
	 *
	 * @param ps
	 * @return
	 */
	public static String puntuacionCombobox(ComboBox<String> puntuacionMenu) {

		return puntuacionMenu.getSelectionModel().getSelectedItem();
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "numeroComic" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public static String numeroCombobox(ComboBox<String> numeroComic) {
		String numComic = "";

		if (numeroComic.getSelectionModel().getSelectedItem() != null) {
			numComic = numeroComic.getSelectionModel().getSelectedItem();
		}

		return numComic;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public static String cajaCombobox(ComboBox<String> numeroCajaComic) {

		String cajaComics = "";

		if (numeroCajaComic.getSelectionModel().getSelectedItem() != null) {
			cajaComics = numeroCajaComic.getSelectionModel().getSelectedItem();
		}

		return cajaComics;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "caja_actual" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public static String estadoCombobox(ComboBox<String> estadoComic) {

		String estadoNuevo = "";

		if (estadoComic.getSelectionModel().getSelectedItem() != null) {
			estadoNuevo = estadoComic.getSelectionModel().getSelectedItem();
		}

		return estadoNuevo;
	}

	/**
	 * Funcion que permite seleccionar en el comboBox "nombreFormato" y lo devuelve,
	 * para la busqueda de comic
	 * 
	 * @return
	 */
	public static String formatoCombobox(ComboBox<String> formatoComic) {

		String formatoEstado = "";
		if (formatoComic.getSelectionModel().getSelectedItem() != null) {
			formatoEstado = formatoComic.getSelectionModel().getSelectedItem();
		}
		return formatoEstado;
	}

	/**
	 * Funcion que permite modificar el estado de un comic.
	 *
	 * @param ps
	 * @return
	 */
	public static String procedenciaCombobox(ComboBox<String> procedenciaComic) {

		String procedenciaEstadoNuevo = "";
		if (procedenciaComic.getSelectionModel().getSelectedItem() != null) {
			procedenciaEstadoNuevo = procedenciaComic.getSelectionModel().getSelectedItem();
		}

		return procedenciaEstadoNuevo;
	}

}
