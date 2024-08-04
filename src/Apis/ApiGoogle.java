package Apis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
/**
 * Clase que proporciona métodos para traducir texto utilizando la API de Google Translate.
 */
public class ApiGoogle {
	
	public static void main(String[] args) {
		
		System.out.println(translateText("Legion de fragmentados", "en"));
		
	}
	
	/**
	 * Traduce un texto desde el idioma de origen detectado al idioma de destino
	 * especificado.
	 *
	 * @param text       El texto que se desea traducir.
	 * @param targetLang El idioma de destino al que se desea traducir el texto.
	 * @return La traducción del texto en el idioma de destino, o null si ocurre un
	 *         error.
	 */
	public static String translateText(String text, String targetLang) {
		try {
			// Codificar el texto para que sea seguro en una URL
			String textEncoded = URLEncoder.encode(text, "UTF-8");

			// Primero, detectamos el idioma de origen del texto
			String detectUrl = "https://translate.googleapis.com/translate_a/single?client=gtx" + "&sl=auto&tl="
					+ targetLang + "&dt=t&q=" + textEncoded;

			// Convertir la URL a un objeto URI y luego a un objeto URL
			URI uriDetect = new URI(detectUrl);
			URL detectLangUrl = uriDetect.toURL();

			// Establecer la conexión HTTP para la detección del idioma
			HttpURLConnection detectConn = (HttpURLConnection) detectLangUrl.openConnection();
			detectConn.setRequestMethod("GET");

			// Leer la respuesta de la detección del idioma
			BufferedReader detectReader = new BufferedReader(new InputStreamReader(detectConn.getInputStream()));
			StringBuilder detectResponse = new StringBuilder();
			String detectLine;
			while ((detectLine = detectReader.readLine()) != null) {
				detectResponse.append(detectLine);
			}
			detectReader.close();

			// Analizar la respuesta JSON para obtener el idioma detectado
			JSONArray detectArray = new JSONArray(detectResponse.toString());
			String sourceLang = detectArray.getString(2);

			// Luego, traducimos el texto al idioma de destino
			String translateUrl = "https://translate.googleapis.com/translate_a/single?client=gtx" + "&sl=" + sourceLang
					+ "&tl=" + targetLang + "&dt=t&q=" + textEncoded;

			// Convertir la URL de traducción a un objeto URI y luego a un objeto URL
			URI uri = new URI(translateUrl);
			URL translateUrlObj = uri.toURL();

			// Establecer la conexión HTTP para la traducción
			HttpURLConnection translateConn = (HttpURLConnection) translateUrlObj.openConnection();
			translateConn.setRequestMethod("GET");

			// Leer la respuesta de la traducción
			BufferedReader reader = new BufferedReader(new InputStreamReader(translateConn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();

			// Procesar la respuesta JSON para obtener la traducción
			JSONArray jsonArray = new JSONArray(response.toString());
			JSONArray translationArray = jsonArray.getJSONArray(0);
			String translation = translationArray.getJSONArray(0).getString(0);

			return translation;
		} catch (Exception e) {
			// Manejar cualquier excepción que pueda ocurrir durante el proceso
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Procesa la respuesta JSON de la traducción para obtener la traducción
	 * completa.
	 *
	 * @param jsonResponse La respuesta JSON de la API de traducción.
	 * @return La traducción completa como una cadena de caracteres, o null si
	 *         ocurre un error.
	 */
    public static String parseTranslationResponse(String jsonResponse) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            StringBuilder translationBuilder = new StringBuilder();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray segmentArray = jsonArray.getJSONArray(i);

                String segment = segmentArray.getString(0);

                translationBuilder.append(segment);

                if (i < jsonArray.length() - 1) {
                    translationBuilder.append(" ");
                }
            }

            return translationBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}