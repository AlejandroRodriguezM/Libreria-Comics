package Funcionamiento;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JDBC.DBLibreriaManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;

public class FuncionesComboBox {

	private static DBLibreriaManager libreria = null;

	private Map<ComboBox<String>, ObservableList<String>> originalComboBoxItems = new HashMap<>();
	private boolean isUserInput = true;
	private boolean updatingComboBoxes = false; // New variable to keep track of ComboBox updates

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
			ComboBox<String> comboBox = comboboxes.get(i);

			originalComboBoxItems.put(comboBox, FXCollections.observableArrayList(comboBox.getItems()));

			comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
				if (!isUserInput || updatingComboBoxes) {
					return; // Ignorar cambios programáticos o cuando updatingComboBoxes es verdadero
				}
				if (newValue == null || newValue.isEmpty()) {
					handleComboBoxEmptyChange(comboBox, comboboxes);
					return;
				} else {
					Comic comic = getComicFromComboBoxes(totalComboboxes, comboboxes);
					actualizarComboBoxes(totalComboboxes, comboboxes, comic);
					return;
				}
			});

			comboBox.setOnKeyReleased(event -> {
				KeyCode code = event.getCode();
				if (code == KeyCode.BACK_SPACE || code == KeyCode.DELETE) {
					comboBox.setValue(null); // Reset the ComboBox value
					comboBox.getEditor().clear(); // Clear the ComboBox editor text
					handleComboBoxEmptyChange(comboBox, comboboxes); // Handle the empty change after clearing

					// Verificar si algún ComboBox no está vacío y actualizar los ComboBoxes en
					// consecuencia
					boolean atLeastOneNotEmpty = comboboxes.stream()
							.anyMatch(cb -> cb.getValue() != null && !cb.getValue().isEmpty());
					if (atLeastOneNotEmpty) {
						Comic comic = getComicFromComboBoxes(totalComboboxes, comboboxes);
						actualizarComboBoxes(totalComboboxes, comboboxes, comic);
					}
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
		isUserInput = false;

		comboBox.setValue(null); // Establecer a null para restablecer la selección del ComboBox

		isUserInput = true;

		// Comprobar si todos los campos de texto de los ComboBoxes están vacíos
		boolean allEmpty = originalComboBoxItems.keySet().stream().allMatch(cb -> cb.getEditor().getText().isEmpty());

		// Si todos los campos de texto de los ComboBoxes están vacíos, llamar a
		// limpiezaDeDatos()
		if (allEmpty) {
			limpiezaDeDatos(comboboxes);
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

			List<List<String>> listaOrdenada = Arrays.asList(DBLibreriaManager.nombreComicList,
					DBLibreriaManager.numeroComicList, DBLibreriaManager.nombreVarianteList,
					DBLibreriaManager.nombreProcedenciaList, DBLibreriaManager.nombreFormatoList,
					DBLibreriaManager.nombreDibujanteList, DBLibreriaManager.nombreGuionistaList,
					DBLibreriaManager.nombreEditorialList, DBLibreriaManager.nombreFirmaList,
					DBLibreriaManager.numeroCajaList);

			for (int i = 0; i < totalComboboxes; i++) {
				List<String> itemsActuales = listaOrdenada.get(i);
				if (itemsActuales != null && !itemsActuales.isEmpty()) {
					ObservableList<String> itemsObservable = FXCollections.observableArrayList(itemsActuales);
					comboboxes.get(i).setItems(itemsObservable);
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
			comboBox.setValue("");
			comboBox.getEditor().setText("");
		}

		// Restaurar los elementos originales para cada ComboBox
		for (ComboBox<String> comboBox : originalComboBoxItems.keySet()) {
			ObservableList<String> originalItems = originalComboBoxItems.get(comboBox);
			comboBox.setItems(originalItems);
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
			List<String> items = itemsList.get(i);

			try {
				if (items != null && !items.isEmpty()) {
					ObservableList<String> itemsCopy = FXCollections.observableArrayList(items);
					comboBox.setItems(itemsCopy);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			i++;
		}
	}

}
