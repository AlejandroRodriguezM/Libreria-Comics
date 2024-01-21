package dbmanager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Funcionamiento.Utilidades;
import comicManagement.Comic;

public class UpdateManager {
	
	/**
	 * Realiza acciones específicas en la base de datos para un comic según la
	 * operación indicada.
	 *
	 * @param id        El ID del comic a modificar.
	 * @param operacion La operación a realizar: "Vender", "En venta" o "Eliminar".
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static void actualizarComicBBDD(String id, String operacion) throws SQLException {
		String sentenciaSQL = null;

		switch (operacion.toLowerCase()) {
		case "vender":
			sentenciaSQL = "UPDATE comicsbbdd SET estado = 'Vendido' WHERE ID = ?";
			break;
		case "en venta":
			sentenciaSQL = "UPDATE comicsbbdd SET estado = 'En venta' WHERE ID = ?";
			break;
		default:
			// Manejar un caso no válido si es necesario
			throw new IllegalArgumentException("Operación no válida: " + operacion);
		}

		CommonFunctions.modificarDatos(id, sentenciaSQL);
	}
	
	/**
	 * Función que actualiza los datos de un cómic en la base de datos.
	 *
	 * @param datos Los datos del cómic a actualizar.
	 * @throws Exception
	 */
	public static void actualizarComic(Comic datos) throws Exception {

		if (SelectManager.comprobarIdentificadorComic(datos.getID())) {
			String sentenciaSQL = "UPDATE comicsbbdd SET "
					+ "nomComic = ?, caja_deposito = ?, precio_comic = ?, codigo_comic = ?, numComic = ?, nomVariante = ?, "
					+ "Firma = ?, nomEditorial = ?, formato = ?, procedencia = ?, fecha_publicacion = ?, "
					+ "nomGuionista = ?, nomDibujante = ?, portada = ?, key_issue = ?, "
					+ "url_referencia = ?, estado = ? " + "WHERE ID = ?";

			comicModificar(sentenciaSQL, datos);
		}
	}
	
	/**
	 * Modifica la dirección de la portada de un cómic en la base de datos.
	 *
	 * @param nombre_completo el nombre completo de la imagen de la portada
	 * @param ID              el ID del cómic a modificar
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public static void modificarImagenComic(String nombre_completo, String ID) throws SQLException {
		String extension = ".jpg";
		String nuevoNombreArchivo = nombre_completo + extension;

		String userDir = System.getProperty("user.home");
		String documentsPath = userDir + File.separator + "Documents";
		String imagePath = documentsPath + File.separator + "libreria_comics" + File.separator + ConectManager.DB_NAME
				+ File.separator + "portadas" + File.separator + nuevoNombreArchivo;

		String sql = "UPDATE comicsbbdd SET portada = ? WHERE ID = ?";
		try (Connection conn = ConectManager.conexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, imagePath);
			ps.setString(2, ID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void actualizarListaComics(Comic datos) throws SQLException, IOException {

		String idComic = datos.getID();
		datos.setPuntuacion("");
		datos.setID("");

		String carpeta = Utilidades
				.eliminarDespuesUltimoPortadas(Utilidades.generarCodigoUnico(datos.getImagen() + File.separator));
		new Utilidades().nueva_imagen(datos.getImagen(), carpeta);
		modificarImagenComic(datos.getCodigo_comic(), idComic);

		DBLibreriaManager.listaComics.add(datos);
	}
	
	/**
	 * Modifica los datos de un cómic en la base de datos.
	 *
	 * @param sentenciaSQL la sentencia SQL para modificar el cómic
	 * @param datos        los nuevos datos del cómic
	 * @throws Exception
	 * @throws SQLException si ocurre un error al acceder a la base de datos
	 * @throws IOException  si ocurre un error de lectura/escritura al manejar las
	 *                      imágenes
	 */
	public static void comicModificar(String sentenciaSQL, Comic datos) throws Exception {

		try (Connection conn = ConectManager.conexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
			CommonFunctions.setParameters(ps, datos, true); // Configurar los parámetros de la consulta

			if (ps.executeUpdate() == 1) {
				DBLibreriaManager.listaComics.clear();
				actualizarListaComics(datos);
			}
		} catch (SQLException | IOException ex) {
			Utilidades.manejarExcepcion(ex);
			throw ex; // Relanzar la excepción para que se maneje adecuadamente en el nivel superior
		}
	}
	

	
	/**
	 * Función que comprueba si la opinión ha sido introducida correctamente.
	 *
	 * @param sentenciaSQL La sentencia SQL a ejecutar
	 * @param ID           La ID del cómic
	 * @param puntuacion   La puntuación a insertar
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public static void actualizarOpinion(String idComic, String puntuacion) throws SQLException {
		DBLibreriaManager.listaComics.clear();
		String sentenciaSQL = "UPDATE comicsbbdd set puntuacion = ? where ID = ?";

		try (Connection conn = ConectManager.conexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL);) {
			if (SelectManager.comprobarIdentificadorComic(idComic)) { // Comprueba si la ID introducida existe en la base de datos
				Comic comic = SelectManager.comicDatos(idComic);
				ps.setString(1, puntuacion);
				ps.setString(2, idComic);
				if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, se añade el cómic a la lista
					DBLibreriaManager.listaComics.add(comic);
				}
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
	}

}
