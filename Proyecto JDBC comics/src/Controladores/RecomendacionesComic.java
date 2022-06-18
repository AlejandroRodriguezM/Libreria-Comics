package Controladores;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xls
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Añadir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria
 *  
 *  Esta clase te permite ver un comic aleatorio de la base de datos.
 *  
 *  Version 2.3
 *  
 *  Por Alejandro Rodriguez
 *  
 *  Twitter: @silverAlox
 */

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

	/**
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void eligePorMi(ActionEvent event) throws SQLException {

		printComicRecomendado.setText(generarLectura());
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String generarLectura() throws SQLException
	{
		Random r = new Random();

		int n;

		Comics comic = new Comics();

		comic.verTodo();

		n = (int) (Math.random() * r.nextInt(comic.verTodo().length));

		limpiarPront();
		
		return comic.verTodo()[n].toString();
		
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