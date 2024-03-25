package UNIT_TEST;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FilteredComboBoxExample extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/UNIT_TEST/test.fxml"));
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.setTitle("Filtered ComboBox Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}