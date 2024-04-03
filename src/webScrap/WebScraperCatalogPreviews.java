package webScrap;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScraperCatalogPreviews {

    public static CompletableFuture<List<Map.Entry<String, String>>> urlPreviews() {
        return CompletableFuture.supplyAsync(() -> {
            List<Map.Entry<String, String>> resultList = new ArrayList<>();

            try {
                String urlWeb = "https://prhcomics.com/comics/";
                Document doc = Jsoup.connect(urlWeb).get();

                Map<String, String> mesesYEnlaces = new HashMap<>();
                Elements articulos = doc.select("div.catalog-item");

                for (Element articulo : articulos) {
                    String tituloCatalogo = articulo.select("div.catalog-meta-title").text();

                    // Selección de elementos dentro de div.catalog-link-pdf
                    Elements linksPDF = articulo.select("div.catalog-link-pdf a[href]");

                    // Iterar sobre los enlaces seleccionados
                    for (Element link : linksPDF) {
                        String textoEnlace = link.text();
                        String enlaceCatalogo = link.attr("href");
                        String tipoEnlace = obtenerTipoEnlace(textoEnlace);

                        if (tipoEnlace != null) {
                            mesesYEnlaces.put(tituloCatalogo + " (" + tipoEnlace + ")", enlaceCatalogo);
                        }
                    }
                }

                resultList.addAll(mesesYEnlaces.entrySet());
                
                // Ordenar la lista por mes antes de devolverla
                Collections.sort(resultList, (entry1, entry2) -> {
                    String mes1 = obtenerMes(entry1.getKey());
                    String mes2 = obtenerMes(entry2.getKey());
                    return mes1.compareTo(mes2);
                });
                
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultList;
        });
    }

    // Método para obtener el mes del título del catálogo
    private static String obtenerMes(String tituloCatalogo) {
        // Implementa aquí la lógica para extraer el mes del título del catálogo
        if (tituloCatalogo.contains("January")) {
            return "01";
        } else if (tituloCatalogo.contains("February")) {
            return "02";
        } else if (tituloCatalogo.contains("March")) {
            return "03";
        } else if (tituloCatalogo.contains("April")) {
            return "04";
        } else if (tituloCatalogo.contains("May")) {
            return "05";
        } else if (tituloCatalogo.contains("June")) {
            return "06";
        } else if (tituloCatalogo.contains("July")) {
            return "07";
        } else if (tituloCatalogo.contains("August")) {
            return "08";
        } else if (tituloCatalogo.contains("September")) {
            return "09";
        } else if (tituloCatalogo.contains("October")) {
            return "10";
        } else if (tituloCatalogo.contains("November")) {
            return "11";
        } else if (tituloCatalogo.contains("December")) {
            return "12";
        } else {
            return "Unknown"; // Si no puede determinar el mes, devuelve "Unknown"
        }
    }

    // Método para obtener el tipo de enlace (PRH, Marvel, IDW) según el texto del enlace
    private static String obtenerTipoEnlace(String textoEnlace) {
        if (textoEnlace.contains("Download Marvel PDF")) {
            return "Marvel";
        } else if (textoEnlace.contains("Download IDW PDF")) {
            return "IDW";
        } else if (textoEnlace.contains("Download PRH Panels PDF")) {
            return "PRH";
        } else {
            return null; // Si el tipo de enlace no es ninguno de los especificados, devuelve null
        }
    }
	
}
