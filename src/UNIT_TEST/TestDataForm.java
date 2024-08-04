package UNIT_TEST;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.poi.ss.usermodel.DateUtil;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestDataForm extends Application {
    private static final DateTimeFormatter SQLITE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DatePicker datePicker = new DatePicker();

        String initialDate = "2023-07-19"; // Ejemplo de fecha en formato yyyy-MM-dd
       setDatePickerValue(datePicker, initialDate);

        
        VBox vbox = new VBox(datePicker);
        Scene scene = new Scene(vbox, 200, 200);

        primaryStage.setScene(scene);
        primaryStage.show();

        datePicker.setOnAction(event -> {
            String formattedDate = parseDate(datePicker);
            System.out.println("Fecha para SQLite: " + formattedDate);
        });
    }

    public static String parseDate(DatePicker datePicker) {
        LocalDate date = datePicker.getValue();
        if (date != null) {
            return date.format(SQLITE_DATE_FORMATTER);
        } else {
            return "";
        }
    }

    public static LocalDate parseStringToDate(String dateString) {
        try {
            return LocalDate.parse(dateString, SQLITE_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static void setDatePickerValue(DatePicker datePicker, String dateString) {
        LocalDate date = parseStringToDate(dateString);
        if (date != null) {
            datePicker.setValue(date);
        }
    }
}
