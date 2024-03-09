package UNIT_TEST;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HyperlinkInLabelExample extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Crear un Hyperlink
        Hyperlink hyperlink = new Hyperlink("Haz clic aquí para ir a mi página web");

        // Establecer el evento de acción al hacer clic en el Hyperlink
        hyperlink.setOnAction(e -> {
            // Aquí puedes definir la acción que deseas realizar al hacer clic en el enlace
            System.out.println("Abriendo la página web...");
        });

        // Agregar el Hyperlink a un contenedor
        VBox root = new VBox();
        root.getChildren().add(hyperlink);

        // Crear la escena y mostrarla
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ejemplo de Hyperlink en JavaFX");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}