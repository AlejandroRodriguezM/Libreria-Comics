package Funcionamiento;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VerComicController {

    @FXML
    private TextField anioPublicacion;

    @FXML
    private Button botonMostrarParametro;

    @FXML
    private Button botonMostrarParametro1;

    @FXML
    private Button botonVolver;

    @FXML
    private Button botonbbdd;

    @FXML
    private TextArea mostrarDatosBBDD;

    @FXML
    private TextField nombreComic;

    @FXML
    private TextField nombreDibujante;

    @FXML
    private TextField nombreEditorial;

    @FXML
    private TextField nombreFormato;

    @FXML
    private TextField nombreGuionista;

    @FXML
    private TextField nombreVariante;

    @FXML
    void escribirDibujante(ActionEvent event) {

    }

    @FXML
    void escribirEditorial(ActionEvent event) {

    }

    @FXML
    void escribirFecha(ActionEvent event) {

    }

    @FXML
    void escribirFormato(ActionEvent event) {

    }

    @FXML
    void escribirGuionista(ActionEvent event) {

    }

    @FXML
    void escribirNombre(ActionEvent event) {

    }

    @FXML
    void escribirVariante(ActionEvent event) {

    }

    @FXML
    void mostrarPorParametro(ActionEvent event) {

    }

    @FXML
    void verTodabbdd(ActionEvent event) {

    }
    
    @FXML
    void volverMenu(ActionEvent event) throws IOException {
		 // Cargo la vista
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuOpciones.fxml"));

        // Cargo el padre
        Parent root = loader.load();

        // Obtengo el controlador
        MenuOpcionesController controlador = loader.getController();

        // Creo la scene y el stage
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        // Asocio el stage con el scene
        stage.setScene(scene);
        stage.show();

        // Indico que debe hacer al cerrar
        stage.setOnCloseRequest(e -> controlador.closeWindows());

        // Ciero la ventana donde estoy
        Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
        myStage.close();
    }
    
    public void closeWindows() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/MenuOpciones.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
