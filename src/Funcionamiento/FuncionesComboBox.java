package Funcionamiento;

import java.util.ArrayList;
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
	private ComboBox<String> currentComboBox; // New variable to keep track of the current ComboBox

	/**
	 * Crea y devuelve un objeto Comic con valores obtenidos de los ComboBoxes.
	 *
	 * @param totalComboboxes El número total de ComboBoxes.
	 * @param comboboxes      La lista de ComboBoxes que contienen los valores del Comic.
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
				comic.setFirma(value);
				break;
			case 3:
				comic.setGuionista(value);
				break;
			case 4:
				comic.setVariante(value);
				break;
			case 5:
				comic.setNumCaja(value);
				break;
			case 6:
				comic.setProcedencia(value);
				break;
			case 7:
				comic.setFormato(value);
				break;
			case 8:
				comic.setEditorial(value);
				break;
			case 9:
				comic.setDibujante(value);
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
		libreria = new DBLibreriaManager();
		isUserInput = true; // Establecemos isUserInput en true inicialmente.

		// Configuración de los escuchadores para cada ComboBox mediante un bucle
		for (int i = 0; i < totalComboboxes; i++) {
			ComboBox<String> comboBox = comboboxes.get(i);

			// Guardar los elementos originales de cada ComboBox
			originalComboBoxItems.put(comboBox, FXCollections.observableArrayList(comboBox.getItems()));

			comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
				if (!isUserInput || updatingComboBoxes) {
					return; // Ignorar cambios programáticos o cuando updatingComboBoxes es verdadero
				}

				if (newValue == null || newValue.isEmpty()) {
					handleComboBoxEmptyChange(comboBox);
					return;
				} else {
					Comic comic = getComicFromComboBoxes(totalComboboxes, comboboxes);
					actualizarComboBoxes(totalComboboxes, comboboxes, comic);
					return;
				}
			});

			comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
				if (!isUserInput || updatingComboBoxes) {
					return; // Ignorar cambios programáticos o cuando updatingComboBoxes es verdadero
				}

				if (newValue == null || newValue.isEmpty()) {
					handleComboBoxEmptyChange(comboBox);
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
					handleComboBoxEmptyChange(comboBox); // Handle the empty change after clearing
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
	private void handleComboBoxEmptyChange(ComboBox<String> comboBox) {
		isUserInput = false;

		// Restablecer el valor del ComboBox cuando el texto está vacío
		currentComboBox = comboBox; // Actualizar el ComboBox actual
		currentComboBox.setValue(null); // Establecer a null para restablecer la selección del ComboBox

		isUserInput = true;
		// Comprobar si todos los campos de texto de los ComboBoxes están vacíos
		boolean allEmpty = true;
		for (ComboBox<String> cb : originalComboBoxItems.keySet()) {
			if (!cb.getEditor().getText().isEmpty()) {
				allEmpty = false;
				break;
			}
		}

		// Si todos los campos de texto de los ComboBoxes están vacíos, llamar a
		// limpiezaDeDatos()
		if (allEmpty) {
			limpiezaDeDatos(new ArrayList<>(originalComboBoxItems.keySet()));
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
		Comic comicTemp = new Comic("", comic.getNombre(), comic.getNumCaja(), comic.getNumero(), comic.getVariante(),
				comic.getFirma(), comic.getEditorial(), comic.getFormato(), comic.getProcedencia(), "",
				comic.getGuionista(), comic.getDibujante(), "", "", "", "", "", "");

		String sql = libreria.datosConcatenados(comicTemp);

		comic.toString();

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
				ObservableList<String> itemsActuales = FXCollections.observableArrayList(listaOrdenada.get(i));
				comboboxes.get(i).setItems(itemsActuales);
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

		for (int i = 0; i < comboboxes.size(); i++) {
			ComboBox<String> comboBox = comboboxes.get(i);
			List<String> items = itemsList.get(i);
			ObservableList<String> observableItems = FXCollections.observableArrayList(items);

			if (items != null) {
				comboBox.setItems(observableItems);
			}
		}
	}

}
