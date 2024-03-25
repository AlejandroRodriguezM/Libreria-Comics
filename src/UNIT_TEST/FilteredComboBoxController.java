package UNIT_TEST;

import java.net.URL;
import java.util.ResourceBundle;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FilteredComboBoxController implements Initializable {

    @FXML
    private MFXFilterComboBox<String> filtro;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Crear una lista de elementos para el ComboBox
        ObservableList<String> items = FXCollections.observableArrayList(
                "Apple", "Banana", "Cherry", "Date", "Grape", "Lemon", "Mango", "Orange", "Peach");

        // Configurar los elementos del MFXFilterComboBox
        filtro.setItems(items);
    }
}
