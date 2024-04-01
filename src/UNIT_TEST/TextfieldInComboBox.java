package UNIT_TEST;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TextfieldInComboBox extends Application {

    @Override
    public void start(Stage primaryStage) {
        ComboBox<String> itemSelector = new ComboBox<>();
        itemSelector.getItems().addAll("Item 1", "Item 2", "Item 3");

        TextField entryField = new TextField();
        entryField.setEditable(true);

        Label instructions = new Label("Enter a text and click the 'Add' button to add it to the ComboBox.");
        instructions.setLayoutX(10);
        instructions.setLayoutY(10);

        Scene scene = new Scene(itemSelector, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TextField in ComboBox");
        primaryStage.show();

        // Add filter
        TextField filterField = new TextField();
        filterField.setEditable(true);
        filterField.setLayoutX(10);
        filterField.setLayoutY(50);
//        scene.getRoot().getChildren().add(filterField);

        // Listen for changes in the filter field
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the ComboBox items based on the entered filter text
            if (!newValue.isEmpty()) {
                itemSelector.getItems().clear();
                itemSelector.getItems().addAll(itemSelector.getItems().stream()
                        .filter(item -> item.toLowerCase().contains(newValue.toLowerCase()))
                        .toList());
            } else {
                itemSelector.getItems().clear();
                itemSelector.getItems().addAll(itemSelector.getItems());
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}