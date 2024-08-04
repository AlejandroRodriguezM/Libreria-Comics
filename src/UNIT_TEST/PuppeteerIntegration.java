package UNIT_TEST;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ficherosFunciones.FuncionesFicheros;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PuppeteerIntegration {
    /**
     * Analiza una cadena de texto de fecha en el formato "MMMM d, yyyy" a un objeto Date.
     *
     * @param dateText El texto de la fecha a analizar.
     * @return El objeto Date analizado o null si hay un error de análisis.
     */
    private static Date parseReleaseDate(String dateText) {
        // Verificar si el texto de la fecha es "N/A" o está vacío
        if ("N/A".equals(dateText) || dateText.trim().isEmpty()) {
            // Si es "N/A" o está vacío, devolver la fecha predeterminada
            return getDefaultDate();
        }

        try {
            // Intentar parsear la fecha con el formato esperado
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            Date parsedDate = dateFormat.parse(dateText);
            return parsedDate != null ? parsedDate : getDefaultDate();
        } catch (ParseException e) {
            // En caso de error de análisis, imprime el error y devuelve la fecha predeterminada
            e.printStackTrace();
            return getDefaultDate();
        }
    }

    /**
     * Convierte un objeto Date a una cadena en el formato "yyyy-MM-dd" para SQL.
     *
     * @param date El objeto Date a convertir.
     * @return La cadena formateada para SQL.
     */
    private static String formatForSQL(Date date) {
        SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sqlFormat.format(date);
    }

    private static Date getDefaultDate() {
        try {
            SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd");
            return defaultFormat.parse("2000-01-01");
        } catch (ParseException ex) {
            // En caso de que ocurra un error al parsear la fecha predeterminada, devuelve null
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String dateText = "February 22, 2023";
        Date parsedDate = parseReleaseDate(dateText);
        String sqlDate = formatForSQL(parsedDate);
        System.out.println("Parsed Date: " + parsedDate);
        System.out.println("SQL Date: " + sqlDate);
    }
}