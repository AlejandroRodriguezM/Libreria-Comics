/**
 * Contiene las clases que hacen funcionar las diferentes funciones de uso de back end y front de todo el proyecto
 *  
*/
package Funcionamiento;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Introducir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *  - Puntuar comics que se encuentren dentro de la base de datos.
 *  Esta clase permite acceder al menu principal donde se puede viajar a diferentes ventanas, etc.
 *
 *  Version 8.0.0.0
 *
 *  @author Alejandro Rodriguez
 *
 */
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Clase que proporciona funciones para asignar tooltips a botones, ComboBox y
 * TextField en la interfaz de usuario.
 */
public class FuncionesTooltips {

	/**
	 * Fuente utilizada para los tooltips en la interfaz gráfica.
	 */
	private static final Font TOOLTIP_FONT = Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 13);

	/**
	 * Asigna un tooltip a un botón con el mensaje dado.
	 *
	 * @param boton   El botón al que se va a asignar el tooltip.
	 * @param mensaje El mensaje que se mostrará en el tooltip.
	 */
	public static void assignTooltips(Map<Node, String> tooltipsMap) {
		for (Node elemento : tooltipsMap.keySet()) {
			String mensaje = tooltipsMap.get(elemento);
			assignTooltip(elemento, mensaje);
		}
	}

	private static void assignTooltip(Node elemento, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
		tooltip.setFont(TOOLTIP_FONT);
		Tooltip.install(elemento, tooltip);
	}

}