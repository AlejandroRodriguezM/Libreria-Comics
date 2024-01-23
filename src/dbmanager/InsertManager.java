package dbmanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Funcionamiento.Utilidades;
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

		subirComic(INSERT_SENTENCIA, datos, esImportar);
	}

	/**
	 * Permite introducir un nuevo cómic en la base de datos.
	 *
	 * @param sentenciaSQL la sentencia SQL para insertar el cómic
	 * @param datos        los datos del cómic a insertar
	 * @throws IOException  si ocurre un error al manejar el archivo de imagen
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public static void subirComic(String sentenciaSQL, Comic datos, boolean esImportar) {
		try (Connection conn = ConectManager.conexion();
				PreparedStatement statement = conn.prepareStatement(sentenciaSQL)) {

			CommonFunctions.setParameters(statement, datos, false);

			if (esImportar) {
				statement.addBatch();
			}
			statement.executeUpdate();
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
	}

}
