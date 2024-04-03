package comicManagement;

import java.io.File;

import Funcionamiento.Utilidades;
import ficherosFunciones.FuncionesExcel;

public class ComicFichero extends Comic {

	public static Comic datosComicFichero(String lineText) {
		String[] data = lineText.split(";");
		String nombre = data[1];
		String numCaja = data[2];
		String precio_comic = data[3];
		String codigo_comic = data[4];
		String numero = data[5];
		String variante = data[6];
		String firma = data[7];
		String editorial = data[8];
		String formato = data[9];
		String procedencia = obtenerProcedencia(data[10]);
		String fecha = data[11];
		String guionista = data[12];
		String dibujante = data[13];
		String puntuacion = obtenerPuntuacion(data[13], data[14]);
		String direccion_portada = data[15];
		String nombre_portada = Utilidades.obtenerNombrePortada(false,direccion_portada);
		
		String nombre_modificado = Utilidades.convertirNombreArchivo(nombre_portada);
		
		String nombre_completo_portada = FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH + File.separator + nombre_modificado;
		
		String nombreUnico = FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH + File.separator
				+ Utilidades.copiarConNombreAleatorio(nombre_completo_portada);

		String key_issue = data[16];
		key_issue = key_issue.replaceAll("\\r|\\n", "");
		String url_referencia = data[17];
		String estado = data[18];

		// Verificaciones y asignaciones predeterminadas
		precio_comic = (precio_comic.isEmpty()) ? "0" : precio_comic;
		codigo_comic = (codigo_comic.isEmpty()) ? "0" : codigo_comic;
		url_referencia = (url_referencia.isEmpty()) ? "Sin referencia" : url_referencia;
		
		Comic comic = new Comic("", nombre, numCaja, numero, variante, firma, editorial, formato, procedencia, fecha,
				guionista, dibujante, estado, key_issue, puntuacion, nombreUnico, url_referencia, precio_comic,
				codigo_comic);
		return comic;
	}

	/**
	 * Función que obtiene la procedencia según el país
	 *
	 * @param pais El país de origen
	 * @return La procedencia actualizada
	 */
	private static String obtenerProcedencia(String pais) {
		String procedencia;
		if (pais.toLowerCase().contains("españa")) {
			procedencia = pais.toLowerCase().replace("españa", "Spain");
		} else {
			procedencia = pais;
		}
		return procedencia;
	}

	/**
	 * Función que obtiene la puntuación del cómic
	 *
	 * @param dibujante  El nombre del dibujante
	 * @param puntuacion La puntuación actual
	 * @return La puntuación actualizada
	 */
	private static String obtenerPuntuacion(String dibujante, String puntuacion) {
		if (dibujante.length() != 0) {
			return puntuacion;
		} else {
			return "Sin puntuación";
		}
	}

}
