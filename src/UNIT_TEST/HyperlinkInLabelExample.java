package UNIT_TEST;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HyperlinkInLabelExample extends Application {
	
    double y = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Crear TableView
        TableView<Object> tableView = new TableView<>();
        tableView.setPrefHeight(200); // Altura inicial

        // Configurar eventos del ratón para redimensionar el TableView
        tableView.setOnMousePressed(event -> {
            y = event.getSceneY();
        });

        tableView.setOnMouseDragged(event -> {
            double deltaY = event.getSceneY() - y;
            double newHeight = tableView.getPrefHeight() + deltaY;
            if (newHeight > 0) {
                tableView.setPrefHeight(newHeight);
                y = event.getSceneY();
            }
        });

        // Cambiar el cursor cuando se pasa sobre el borde inferior del TableView
        tableView.setOnMouseMoved(event -> {
            if (event.getY() >= tableView.getHeight() - 5) {
                tableView.setCursor(javafx.scene.Cursor.S_RESIZE);
            } else {
                tableView.setCursor(javafx.scene.Cursor.DEFAULT);
            }
        });

        // Configurar el diseño de la interfaz
        VBox root = new VBox(tableView);
        Scene scene = new Scene(root, 400, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Adjust TableView Height with Mouse");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}