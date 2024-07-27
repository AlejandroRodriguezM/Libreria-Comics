package UNIT_TEST;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

public class NumericInputDialogExample extends Application {

    private int userInputNumber;

    @Override
    public void start(Stage primaryStage) {
        // Crear el diálogo de entrada de texto
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Entrada de Número");
        dialog.setHeaderText("Por favor, ingrese un número:");
        dialog.setContentText("Número:");

        // Validar que solo se ingresen números
        Optional<String> result;
        boolean validInput = false;

        while (!validInput) {
            result = dialog.showAndWait();
            if (result.isPresent()) {
                String input = result.get();
                if (input.matches("\\d+")) { // Verificar que solo haya dígitos
                    userInputNumber = Integer.parseInt(input);
                    validInput = true;
                } else {
                	mostrarAlerta("Entrada inválida", "Por favor, ingrese solo números.");
                }
            } else {
                break; // El diálogo fue cancelado
            }
        }

        // Si la entrada es válida, preguntar si está seguro
        if (validInput) {
            boolean isConfirmed = mostrarDialogoConfirmacion("Confirmación", "¿Está seguro de que el número ingresado es " + userInputNumber + "?");
            if (isConfirmed) {
            	mostrarAlerta("Número ingresado", "Número ingresado: " + userInputNumber);
            } else {
                return; // Salir si el usuario no confirma
            }
        }
    }

    private static boolean mostrarDialogoConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
