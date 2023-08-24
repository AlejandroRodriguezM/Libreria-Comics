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

	
	private static String[] mensajes = {

			"Muestra toda la base de datos", "Limpia la pantalla y reinicia todos los valores",
			"Muestra los comics o libros o mangas por parametro", "Nombre de los cómics / libros / mangas",
			"Número del cómic / libro / manga", "Nombre de la firma del cómic / libro / manga",
			"Nombre del guionista del cómic / libro / manga", "Nombre de la variante del cómic / libro / manga",
			"Número de la caja donde se guarda el cómic / libro / manga",
			"Nombre de la procedencia del cómic / libro / manga", "Nombre del formato del cómic / libro / manga",
			"Nombre de la editorial del cómic / libro / manga", "Nombre del dibujante del cómic / libro / manga",
			"Aqui puedes añadir si el comic tiene o no alguna clave, esto es para coleccionistas. Puedes dejarlo vacio" };

	public static void asignarTooltip(Button boton, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
        tooltip.setFont(TOOLTIP_FONT);
		boton.setTooltip(tooltip);
	}

	public static void asignarTooltip(ComboBox<String> comboBox, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
        tooltip.setFont(TOOLTIP_FONT);
		comboBox.setTooltip(tooltip);
	}

	private static void asignarTooltip(TextField textField, String mensaje) {
		Tooltip tooltip = new Tooltip(mensaje);
        tooltip.setFont(TOOLTIP_FONT);
		textField.setTooltip(tooltip);
	}

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

	private static String obtenerMensajeTooltip(int posicion) {
		if (posicion >= 0 && posicion < mensajes.length) {
			return mensajes[posicion];
		}
		return "Mensaje de tooltip por defecto";
	}

}
