package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import comicManagement.Comic;
import funcionesAuxiliares.Utilidades;

public class UpdateManager {

	private static final String UPDATE_COMIC = "UPDATE comicsbbdd SET " + "tituloComic = ?, " + "codigoComic = ?, "
			+ "numeroComic = ?, " + "precioComic = ?, " + "fechaGradeo = ?, " + "editorComic = ?, "
			+ "keyComentarios = ?, " + "firmaComic = ?, " + "artistaComic = ?, " + "guionistaComic = ?, " + "varianteComic = ?, "
			+ "direccionImagenComic = ?, " + "urlReferenciaComic = ? " + "WHERE idComic = ?";

	/**
	 * Realiza acciones específicas en la base de datos para un comic según la
	 * operación indicada.
	 *
	 * @param id        El ID del comic a modificar.
	 * @param operacion La operación a realizar: "Vender", "En venta" o "Eliminar".
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static void actualizarComicBBDD(Comic comic, String operacion) {
		String sentenciaSQL = null;

		switch (operacion.toLowerCase()) {
		case "modificar":
			sentenciaSQL = UPDATE_COMIC;
			break;
		case "portada":
			sentenciaSQL = UPDATE_COMIC;
			break;
		default:
			// Manejar un caso no válido si es necesario
			throw new IllegalArgumentException("Operación no válida: " + operacion);
		}
		modificarComic(comic, sentenciaSQL);
	}

	public static void modificarComic(Comic datos, String sentenciaSQL) {

		// Comprobar si el identificador de la comic es válido
		if (SelectManager.comprobarIdentificadorComic(datos.getIdComic())) {
			// Usar try-with-resources para asegurar el cierre de recursos
			try (Connection conn = ConectManager.conexion();
					PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {

				// Configurar los parámetros de la consulta
				DBUtilidades.setParameters(ps, datos, true);
				// Ejecutar la actualización
				if (ps.executeUpdate() == 1) {
					// Limpiar y actualizar la lista de comics
					ListasComicsDAO.listaComics.clear();
					ListasComicsDAO.listaComics.add(datos);
				} else {
					System.out.println("No se actualizó ningún registro. Puede que el idComic no exista.");
				}
			} catch (SQLException ex) {
				Utilidades.manejarExcepcion(ex);
			}
		} else {
			System.out.println("El identificador de la comic no es válido.");
		}
	}

}
