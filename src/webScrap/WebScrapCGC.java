package webScrap;

import java.io.File;
import java.util.List;

import comicManagement.Comic;
import ficherosFunciones.FuncionesFicheros;

public class WebScrapCGC {

	public static Comic extraerDatosMTG(String codigoCarta) {
		String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrapCGC.js";
		List<String> data = FuncionesScrapeoComunes.getCartaFromPuppeteer(codigoCarta, scriptPath);
		String certNumber = "";
		String titulo = "";
		String numero = "";
		String editor = "";
		String fechaG = "";
		String referencia = "";
		String dibujante = "";
		String variante = "";
		String key = "";
		String guionista = "";
		String imageUrl = "";

		for (String line : data) {
			if (line.startsWith("Titulo: ")) {
				titulo = line.substring("Titulo: ".length()).trim();
			} else if (line.startsWith("Certificado: ")) {
				certNumber = line.substring("Certificado: ".length()).trim();
			} else if (line.startsWith("Numero: ")) {
				numero = line.substring("Numero: ".length()).trim();
			} else if (line.startsWith("Editor: ")) {
				editor = line.substring("Editor: ".length()).trim();
			} else if (line.startsWith("FechaG: ")) {
				fechaG = line.substring("FechaG: ".length()).trim();
			} else if (line.startsWith("Referencia: ")) {
				referencia = line.substring("Referencia: ".length()).trim();
			} else if (line.startsWith("Dibujante: ")) {
				dibujante = line.substring("Dibujante: ".length()).trim();
			} else if (line.startsWith("Variante: ")) {
				variante = line.substring("Variante: ".length()).trim();
			} else if (line.startsWith("Guionista: ")) {
				guionista = line.substring("Guionista: ".length()).trim();
			} else if (line.startsWith("Imagen: ")) {
				imageUrl = line.substring("Imagen: ".length()).trim();
			} else if (line.startsWith("KeyC: ")) {
				key = line.substring("KeyC: ".length()).trim();
			}
		}
		return new Comic.ComicGradeoBuilder("", titulo).codigoComic(certNumber).numeroComic(numero).fechaGradeo(fechaG)
				.editorComic(editor).keyComentarios(key).artistaComic(dibujante).guionistaComic(guionista)
				.varianteComic(variante).direccionImagenComic(imageUrl).urlReferenciaComic(referencia).build();
	}

	public static Comic devolverCartaBuscada(String urlCarta) {
		return extraerDatosMTG(urlCarta);
	}
}
