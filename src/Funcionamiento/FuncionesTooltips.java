package Funcionamiento;

import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FuncionesTooltips {

    private static final Font TOOLTIP_FONT = Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 13);

    /**
     * Array de mensajes para los tooltips.
     */
	private static String[] mensajes = {

			"Muestra toda la base de datos", "Limpia la pantalla y reinicia todos los valores",
			"Muestra los comics o libros o mangas por parametro", "Nombre de los cómics / libros / mangas",
			"Número del cómic / libro / manga", "Nombre de la firma del cómic / libro / manga",
			"Nombre del guionista del cómic / libro / manga", "Nombre de la variante del cómic / libro / manga",
			"Número de la caja donde se guarda el cómic / libro / manga",
			"Nombre de la procedencia del cómic / libro / manga", "Nombre del formato del cómic / libro / manga",
			"Nombre de la editorial del cómic / libro / manga", "Nombre del dibujante del cómic / libro / manga",
			"Aqui puedes añadir si el comic tiene o no alguna clave, esto es para coleccionistas. Puedes dejarlo vacio" };

	/**
	 * Asigna un tooltip a un botón con el mensaje dado.
	 *
	 * @param boton El botón al que se va a asignar el tooltip.
	 * @param mensaje El mensaje que se mostrará en el tooltip.
	 */
	public static void asignarTooltip(Button boton, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
        tooltip.setFont(TOOLTIP_FONT);
		boton.setTooltip(tooltip);
	}

	/**
	 * Asigna un tooltip a un ComboBox con el mensaje dado.
	 *
	 * @param comboBox El ComboBox al que se va a asignar el tooltip.
	 * @param mensaje El mensaje que se mostrará en el tooltip.
	 */
	public static void asignarTooltip(ComboBox<String> comboBox, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
        tooltip.setFont(TOOLTIP_FONT);
		comboBox.setTooltip(tooltip);
	}

	/**
	 * Asigna un tooltip a un TextField con el mensaje dado.
	 *
	 * @param textField El TextField al que se va a asignar el tooltip.
	 * @param mensaje El mensaje que se mostrará en el tooltip.
	 */
	private static void asignarTooltip(TextField textField, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
        tooltip.setFont(TOOLTIP_FONT);
		textField.setTooltip(tooltip);
	}

	/**
	 * Asigna tooltips a una lista de elementos que pueden ser botones, ComboBox o TextField.
	 *
	 * @param elementos La lista de elementos a los que se les asignarán los tooltips.
	 */
	@SuppressWarnings("unchecked")
	public static void asignarTooltips(List<Object> elementos) {
		for (int i = 0; i < elementos.size(); i++) {
			Object elemento = elementos.get(i);

			if (elemento instanceof Button) {
				asignarTooltip((Button) elemento, obtenerMensajeTooltip(i));
			} else if (elemento instanceof ComboBox) {
				asignarTooltip((ComboBox<String>) elemento, obtenerMensajeTooltip(i));
			} else if (elemento instanceof TextField) {
				asignarTooltip((TextField) elemento, obtenerMensajeTooltip(i));
			}
		}
	}

	/**
	 * Obtiene el mensaje de tooltip correspondiente a una posición dada.
	 *
	 * @param posicion La posición del mensaje en el array mensajes.
	 * @return El mensaje de tooltip correspondiente.
	 */
	private static String obtenerMensajeTooltip(int posicion) {
		if (posicion >= 0 && posicion < mensajes.length) {
			return mensajes[posicion];
		}
		return "Mensaje de tooltip por defecto";
	}

}
