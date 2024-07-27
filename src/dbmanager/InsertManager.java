package dbmanager;

import java.io.IOException;
import java.sql.SQLException;

import comicManagement.Comic;

public class InsertManager {

	public static final String INSERT_SENTENCIA = "INSERT INTO comicsbbdd ("
			+ "tituloComic, codigoComic, numeroComic,precioComic, fechaGradeo, editorComic, "
			+ " keyComentarios, firmaComic, artistaComic, guionistaComic, varianteComic, direccionImagenComic, urlReferenciaComic) "
			+ "VALUES ( ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?)";

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
