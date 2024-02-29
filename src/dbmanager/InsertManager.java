package dbmanager;

import java.io.IOException;
import java.sql.SQLException;

import comicManagement.Comic;

public class InsertManager {

	public final static String INSERT_SENTENCIA = "INSERT INTO comicsbbdd ("
			+ "nomComic, caja_deposito, precio_comic, codigo_comic, numComic, nomVariante, firma, nomEditorial, "
			+ "formato, procedencia, fecha_publicacion, nomGuionista, nomDibujante, puntuacion, portada, "
			+ "key_issue, url_referencia, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Inserta los datos de un cómic en la base de datos.
	 *
	 * @param comicDatos los datos del cómic a insertar
	 * @throws IOException  si ocurre un error al manejar el archivo de imagen
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public static void insertarDatos(Comic datos, boolean esImportar) {

		DatabaseManagerDAO.subirComic(datos, esImportar);
	}

}
