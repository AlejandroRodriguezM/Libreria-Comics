package comicManagement;

import java.io.File;

import ficherosFunciones.FuncionesExcel;
import funcionesAuxiliares.Utilidades;

public class ComicFichero {

	public static Comic datosComicFichero(String lineText) {
		// Verificar si la línea está vacía
		if (lineText == null || lineText.trim().isEmpty()) {
			return null;
		}

		String[] data = lineText.split(";");

		// Verificar si hay suficientes elementos en el array 'data'
		if (data.length >= 13) { // Ajusta este valor según la cantidad de campos esperados
			String tituloComic = data[1];
			String codigoComic = data[2];
			String numeroComic = data[3];
			String precioComic = data[4];
			String fechaGradeo = data[5];
			String editorComic = data[6];
			String keyComentarios = data[7];
			String firmaComic = data[8];
			String artistaComic = data[9];
			String guionistaComic = data[10];
			String varianteComic = data[11];
			String direccionImagenComic = data[12];
			String urlReferenciaComic = data[13];

			String nombrePortada = Utilidades.obtenerNombrePortada(false, direccionImagenComic);
			String imagen = FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH + File.separator + nombrePortada;

			urlReferenciaComic = (urlReferenciaComic.isEmpty()) ? "Sin referencia" : urlReferenciaComic;

			return new Comic.ComicGradeoBuilder("", tituloComic).codigoComic(codigoComic).precioComic(precioComic)
					.numeroComic(numeroComic).fechaGradeo(fechaGradeo).editorComic(editorComic)
					.keyComentarios(keyComentarios).firmaComic(firmaComic).artistaComic(artistaComic)
					.guionistaComic(guionistaComic).varianteComic(varianteComic).direccionImagenComic(imagen)
					.urlReferenciaComic(urlReferenciaComic).build();
		} else {
			return null;
		}
	}

	public static String limpiarPrecio(String precioStr) {
		// Verificar si el precio es nulo o está vacío
		if (precioStr == null || precioStr.isEmpty()) {
			return "0";
		}

		// Eliminar espacios en blanco al inicio y al final
		precioStr = precioStr.trim();

		// Paso 1: Eliminar símbolos repetidos y dejar solo uno
		precioStr = precioStr.replaceAll("([€$])\\1+", "$1");

		// Paso 2: Si hay varios símbolos, mantener solo uno y eliminar el resto
		precioStr = precioStr.replaceAll("([€$])(.*)([€$])", "$1$2");

		// Extraer el símbolo monetario, si existe
		String symbol = "";
		if (precioStr.startsWith("€") || precioStr.startsWith("$")) {
			symbol = precioStr.substring(0, 1);
			precioStr = precioStr.substring(1);
		}

		// Eliminar caracteres no numéricos excepto el primer punto decimal
		precioStr = precioStr.replaceAll("[^\\d.]", "");
		int dotIndex = precioStr.indexOf('.');
		if (dotIndex != -1) {
			precioStr = precioStr.substring(0, dotIndex + 1) + precioStr.substring(dotIndex + 1).replace("\\.", "");
		}

		// Verificar si el precio contiene solo un punto decimal y ningún otro número
		if (precioStr.equals(".") || precioStr.equals(".0")) {
			return "0";
		}

		// Si el precio después de limpiar es vacío, retornar "0"
		if (precioStr.isEmpty()) {
			return "0";
		}

		// Retornar el precio limpiado con el símbolo monetario
		return symbol + precioStr;
	}

}
