package Controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CargaComicsController {

    @FXML
    private ProgressBar cargaComics;

    @FXML
    private TextArea comicsCarga;

    @FXML
    private Label porcentajeCarga;
    
    private Stage stage; // Add this field to store the reference to the stage

    
    // Method to update the progress of the ProgressBar
    public void updateProgress(double progress) {
        cargaComics.setProgress(progress);
    }

    // Method to append text to the TextArea
    public void appendTextToTextArea(String text) {
        comicsCarga.appendText(text);
    }

    // Method to update the label text
    public void updateLabel(String text) {    	
        porcentajeCarga.setText(text);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void closeWindow() {
        if (stage != null) {
            stage.close();
        }
    }

}
