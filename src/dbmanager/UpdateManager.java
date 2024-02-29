package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Funcionamiento.Utilidades;
import comicManagement.Comic;

public class UpdateManager {

	private static final String UPDATE_PUNTUACION = "UPDATE comicsbbdd set puntuacion = ? where ID = ?";
	private static final String UPDATE_COMIC = "UPDATE comicsbbdd SET "
			+ "nomComic = ?, caja_deposito = ?, precio_comic = ?, codigo_comic = ?, numComic = ?, nomVariante = ?, "
			+ "Firma = ?, nomEditorial = ?, formato = ?, procedencia = ?, fecha_publicacion = ?, "
			+ "nomGuionista = ?, nomDibujante = ?,puntuacion = ?, portada = ?, key_issue = ?, "
			+ "url_referencia = ?, estado = ? " + "WHERE ID = ?";

	private static final String UPDATE_ESTADO_VENDIDO = "UPDATE comicsbbdd SET estado = 'Vendido' WHERE ID = ?";

	private static final String UPDATE_ESTADO_VENTA = "UPDATE comicsbbdd SET estado = 'En venta' WHERE ID = ?";

	/**
	 * Realiza acciones específicas en la base de datos para un comic según la
	 * operación indicada.
	 *
	 * @param id        El ID del comic a modificar.
	 * @param operacion La operación a realizar: "Vender", "En venta" o "Eliminar".
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static void actualizarComicBBDD(Comic comic, String operacion) throws SQLException {
		String sentenciaSQL = null;

		switch (operacion.toLowerCase()) {
		case "vender":
			sentenciaSQL = UPDATE_ESTADO_VENDIDO;
			break;
		case "en venta":
			sentenciaSQL = UPDATE_ESTADO_VENTA;
			break;
		case "modificar":
			sentenciaSQL = UPDATE_COMIC;
			break;
		default:
			// Manejar un caso no válido si es necesario
			throw new IllegalArgumentException("Operación no válida: " + operacion);
		}

		modificarComic(comic, sentenciaSQL);
	}

	/**
	 * Función que comprueba si la opinión ha sido introducida correctamente.
	 *
	 * @param sentenciaSQL La sentencia SQL a ejecutar
	 * @param ID           La ID del cómic
	 * @param puntuacion   La puntuación a insertar
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public static void actualizarOpinion(String idComic, String puntuacion) {
		ListaComicsDAO.listaComics.clear();

		try (Connection conn = ConectManager.conexion();
				PreparedStatement ps = conn.prepareStatement(UPDATE_PUNTUACION);) {
			if (SelectManager.comprobarIdentificadorComic(idComic)) { // Comprueba si la ID introducida existe en la
																		// base de datos
				Comic comic = SelectManager.comicDatos(idComic);
				ps.setString(1, puntuacion);
				ps.setString(2, idComic);
				if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, se añade el cómic a la lista
					ListaComicsDAO.listaComics.add(comic);
				}
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
	}

	// Método que fusiona las dos funciones originales
	public static void modificarComic(Comic datos, String sentenciaSQL) throws SQLException {
		if (SelectManager.comprobarIdentificadorComic(datos.getID())) {
			try (Connection conn = ConectManager.conexion();
					PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
				DBUtilidades.setParameters(ps, datos, true); // Configurar los parámetros de la consulta

				if (ps.executeUpdate() == 1) {
					ListaComicsDAO.listaComics.clear();
					ListaComicsDAO.listaComics.add(datos);
				}
			} catch (SQLException ex) {
				Utilidades.manejarExcepcion(ex);
				throw ex; // Relanzar la excepción para que se maneje adecuadamente en el nivel superior
			}
		}
	}

}
