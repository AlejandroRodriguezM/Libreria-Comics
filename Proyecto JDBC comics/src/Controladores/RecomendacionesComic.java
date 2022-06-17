package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import Funcionamiento.Comics;
import Funcionamiento.NavegacionVentanas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class RecomendacionesComic {

	@FXML
	private Button botonElegir;

	@FXML
	private Button botonSalir;

	@FXML
	private Button botonVolver;

	@FXML
	private TextArea printComicRecomendado;

	NavegacionVentanas nav = new NavegacionVentanas();

	@FXML
	void eligePorMi(ActionEvent event) throws SQLException {

		Random r = new Random();

		int n;

		Comics comic = new Comics();

		comic.verTodo();

		n = (int) (Math.random() * r.nextInt(comic.verTodo().length));

		limpiarPront();
		
		printComicRecomendado.setText(comic.verTodo()[n].toString());

	}
	
	public void limpiarPront()
	{
		printComicRecomendado.setText("");
	}

	@FXML
	void salirPrograma(ActionEvent event) {

		if (nav.salirPrograma(event)) {
			Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
			myStage.close();
		}
	}

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void volverMenu(ActionEvent event) throws IOException {

		nav.verMenuPrincipal();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}

	/**
	 * Al cerrar la ventana, se cargara la ventana de verBBDD
	 * 
	 * @throws IOException
	 */
	public void closeWindows() throws IOException {

		nav.verMenuPrincipal();

		Stage myStage = (Stage) this.botonVolver.getScene().getWindow();
		myStage.close();

	}

}