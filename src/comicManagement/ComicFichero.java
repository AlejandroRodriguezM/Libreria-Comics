package comicManagement;

import java.io.File;

import ficherosFunciones.FuncionesExcel;
import funciones_auxiliares.Utilidades;

public class ComicFichero extends Comic {

	public static Comic datosComicFichero(String lineText) {

		// Verificar si la línea está vacía
		if (lineText == null || lineText.trim().isEmpty()) {
			// Si la línea está vacía, devuelve null para indicar que la línea debe ser
			// ignorada
			return null;
		}

		String[] data = lineText.split(";");

		// Verificar si hay suficientes elementos en el array 'data'
		if (data.length >= 19) { // Ajusta este valor según la cantidad de campos esperados

			String nombre = data[1];
			String numCaja = data[2];
			String precioComic = data[3];
			String codigoComic = data[4];
			String numero = data[5];
			String variante = data[6];
			String firma = data[7];
			String editorial = data[8];
			String formato = data[9];
			String procedencia = obtenerProcedencia(data[10]);
			String fecha = Utilidades.convertirFormatoFecha(data[11]);
			String guionista = data[12];
			String dibujante = data[13];
			String puntuacion = obtenerPuntuacion(data[13], data[14]);
			String direccionPortada = data[15];
			String nombrePortada = Utilidades.obtenerNombrePortada(false, direccionPortada);
			String imagen = FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH + File.separator + nombrePortada;

			String keyIssue = data[16];
			keyIssue = keyIssue.replaceAll("\\r|\\n", "");
			String urlReferencia = data[17];
			String estado = data[18];

			// Verificaciones y asignaciones predeterminadas
			precioComic = (precioComic.isEmpty()) ? "0" : precioComic;
			codigoComic = (codigoComic.isEmpty()) ? "0" : codigoComic;
			urlReferencia = (urlReferencia.isEmpty()) ? "Sin referencia" : urlReferencia;

			return new Comic.ComicBuilder("", nombre).valorGradeo(numCaja).numero(numero).variante(variante)
					.firma(firma).editorial(editorial).formato(formato).procedencia(procedencia).fecha(fecha)
					.guionista(guionista).dibujante(dibujante).estado(estado).keyIssue(keyIssue).puntuacion(puntuacion)
					.imagen(imagen).urlReferencia(urlReferencia).precioComic(precioComic).codigoComic(codigoComic)
					.build();

		} else {
			return null;
		}

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
