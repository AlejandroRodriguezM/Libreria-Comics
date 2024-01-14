package Funcionamiento;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import JDBC.DBLibreriaManager;
import comicManagement.Comic;
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

/**
 * Clase que define funciones para el manejo y configuración de ComboBoxes en
 * una interfaz gráfica.
 */
public class FuncionesComboBox {

	private static DBLibreriaManager libreria = null;

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
					try {
						actualizarComboBoxes(totalComboboxes, comboboxes, comic);
					} catch (SQLException | InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
	public void actualizarComboBoxes(int totalComboboxes, List<ComboBox<String>> comboboxes, Comic comic)
			throws SQLException, InterruptedException, ExecutionException {

		libreria = new DBLibreriaManager();

		Comic comicTemp = new Comic("", comic.getNombre(), comic.getNumCaja(), comic.getNumero(), comic.getVariante(),
				comic.getFirma(), comic.getEditorial(), comic.getFormato(), comic.getProcedencia(), "",
				comic.getGuionista(), comic.getDibujante(), "", "", "", "", "", "", "");
		String sql = libreria.datosConcatenados(comicTemp);

		if (!sql.isEmpty()) {
			isUserInput = false; // Disable user input during programmatic updates

			DBLibreriaManager.nombreComicList = libreria.guardarDatosAutoCompletado(sql, "nomComic");
			DBLibreriaManager.nombreGuionistaList = libreria.guardarDatosAutoCompletado(sql, "nomGuionista");
			DBLibreriaManager.numeroComicList = convertirYOrdenarListaNumeros(
					libreria.guardarDatosAutoCompletado(sql, "numComic"));
			DBLibreriaManager.nombreVarianteList = libreria.guardarDatosAutoCompletado(sql, "nomVariante");
			DBLibreriaManager.numeroCajaList = convertirYOrdenarListaNumeros(
					libreria.guardarDatosAutoCompletado(sql, "caja_deposito"));
			DBLibreriaManager.nombreProcedenciaList = libreria.guardarDatosAutoCompletado(sql, "procedencia");
			DBLibreriaManager.nombreFormatoList = libreria.guardarDatosAutoCompletado(sql, "formato");
			DBLibreriaManager.nombreEditorialList = libreria.guardarDatosAutoCompletado(sql, "nomEditorial");
			DBLibreriaManager.nombreDibujanteList = libreria.guardarDatosAutoCompletado(sql, "nomDibujante");
			DBLibreriaManager.nombreFirmaList = libreria.guardarDatosAutoCompletado(sql, "firma");

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
		return numerosEnteros.stream().map(String::valueOf).collect(Collectors.toList());
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
			// Configurar el tamaño y apariencia del despliegue del ComboBox
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
			if (comboBox != null) {
				modificarPopup(comboBox);

				List<String> items = itemsList.get(i);
				try {
					final int currentIndex = i; // Copia final de i para usar en expresiones lambda

					if (items != null && !items.isEmpty()) {
						ObservableList<String> itemsCopy = FXCollections.observableArrayList(items);
						comboBox.setItems(itemsCopy);
					}

					// Configurar el tamaño y apariencia del despliegue del ComboBox
					modificarPopup(comboBox);

					comboBox.setOnMousePressed(event -> {
						comboBox.hide();
						if (!comboBox.isShowing()) {
							boolean atLeastOneNotEmpty = comboboxes.stream()
									.anyMatch(cb -> cb.getValue() != null && !cb.getValue().isEmpty());

							if (atLeastOneNotEmpty) {
								Comic comic = getComicFromComboBoxes(10, comboboxes);
								setupFilteredPopup(comboboxes, comboBox,
										DBLibreriaManager.listaOrdenada.get(currentIndex));
								try {
									actualizarComboBoxes(10, comboboxes, comic);
								} catch (SQLException | InterruptedException | ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								Comic comic = getComicFromComboBoxes(10, comboboxes);
								setupFilteredPopup(comboboxes, comboBox, items);
								try {
									actualizarComboBoxes(10, comboboxes, comic);
								} catch (SQLException | InterruptedException | ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}

				i++;
			}
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
		Bounds bounds = originalComboBox.localToScreen(originalComboBox.getBoundsInLocal());
		popup = createCustomPopup(originalComboBox, filterTextField, listView);

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
			Popup customPopup = new Popup();

			if (!popup.isShowing()) {
				customPopup = createCustomPopup(originalComboBox, filterTextField, listView);
				showFilteredPopup(customPopup, originalComboBox, filterTextField, filteredText, bounds);
			} else {
				originalComboBox.hide();
				popup.hide();
			}

			if (!listView.getItems().isEmpty()) {
				listView.requestFocus(); // Solicitar el enfoque en la lista
				listView.getSelectionModel().selectFirst();
			}
		});

		// Evento para manejar las teclas presionadas en el TextField de filtro
		filterTextField.setOnKeyPressed(event -> {
			KeyCode code = event.getCode();
			if (code == KeyCode.BACK_SPACE || code == KeyCode.DELETE) {
				modificarPopup(originalComboBox);
				originalComboBox.setValue("");
				originalComboBox.hide();
				boolean atLeastOneNotEmpty = comboboxes.stream()
						.anyMatch(cb -> cb.getValue() != null && !cb.getValue().isEmpty());

				if (atLeastOneNotEmpty) {
					Comic comic = getComicFromComboBoxes(10, comboboxes);
					try {
						actualizarComboBoxes(10, comboboxes, comic);
					} catch (SQLException | InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				originalComboBox.hide();
				popup.hide();
			}
		});

		popup.setOnHidden(event -> {
			listView.getSelectionModel().clearSelection();
			listView.scrollTo(0);

			String selectedItem = listView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				originalComboBox.setValue(selectedItem);
				originalComboBox.hide();

				updateOtherComboBoxes(comboboxes, currentIndex, selectedItem);
				filterTextField.setText(originalComboBox.getValue()); // Establecer el valor en el TextField
				popup.hide();
			}
		});

		originalComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.isEmpty()) {
				filterTextField.setText(newValue);
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
	 * Crea un TextFormatter para validar y permitir solo números decimales
	 * (double).
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
	public void rellenarComboBoxEstaticos(List<ComboBox<String>> comboboxes, String tipo_accion) {
		String[] formatos = { "Grapa (Issue individual)", "Tapa blanda (Paperback)", "Cómic de bolsillo (Pocket)",
				"Edición de lujo (Deluxe Edition)", "Edición omnibus (Omnibus)", "Edición integral (Integral)",
				"Tapa dura (Hardcover)", "eBook (libro electrónico)", "Cómic digital (Digital Comic)",
				"Manga digital (Digital Manga)", "Manga (Manga tome)", "PDF (Portable Document Format)",
				"Revista (Magazine)", "Edición de coleccionista (Collector's Edition)",
				"Edición especial (Special Edition)", "Edición con extras (Bonus Edition)", "Libro (Book)" };

		String[] procedenciaEstados = { "Estados Unidos (United States)", "Japón (Japan)", "Francia (France)",
				"Italia (Italy)", "España (Spain)", "Reino Unido (United Kingdom)", "Alemania (Germany)",
				"Brasil (Brazil)", "Corea del Sur (South Korea)", "México (Mexico)", "Canadá (Canada)", "China (China)",
				"Australia (Australia)", "Argentina (Argentina)", "India (India)", "Bélgica (Belgium)",
				"Países Bajos (Netherlands)", "Portugal (Portugal)", "Suecia (Sweden)", "Suiza (Switzerland)",
				"Finlandia (Finland)", "Noruega (Norway)", "Dinamarca (Denmark)" };

		String[] situacionEstados = { "En posesion", "Comprado", "En venta" };

		String[] editorialBusquedas = { "Marvel ISBN", "Marvel UPC", "Otras editoriales", "Ninguno" };

		// Verificar que la lista de ComboBox tenga al menos el mismo tamaño que los
		// arreglos
		int tamaño = Math.min(comboboxes.size(), Math.min(formatos.length, procedenciaEstados.length));

		for (int i = 0; i < tamaño; i++) {
			comboboxes.get(i).getItems().clear(); // Limpiar elementos anteriores si los hay
			comboboxes.get(i).getItems().addAll(
					i == 0 ? formatos : i == 1 ? procedenciaEstados : i == 2 ? situacionEstados : editorialBusquedas);

			if (tipo_accion.equalsIgnoreCase("aniadir")) {
				comboboxes.get(i).getSelectionModel().selectFirst();

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

		String puntuacion = puntuacionMenu.getSelectionModel().getSelectedItem().toString(); // Toma el valor del menu
																								// "puntuacion"
		return puntuacion;
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
			numComic = numeroComic.getSelectionModel().getSelectedItem().toString();
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
			cajaComics = numeroCajaComic.getSelectionModel().getSelectedItem().toString();
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
			estadoNuevo = estadoComic.getSelectionModel().getSelectedItem().toString();
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
			formatoEstado = formatoComic.getSelectionModel().getSelectedItem().toString();
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
			procedenciaEstadoNuevo = procedenciaComic.getSelectionModel().getSelectedItem().toString();
		}

		return procedenciaEstadoNuevo;
	}

}
