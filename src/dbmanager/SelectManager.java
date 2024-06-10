package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import comicManagement.Comic;
import funcionesAuxiliares.Utilidades;

public class SelectManager {

	public static final String TAMANIO_DATABASE = "SELECT COUNT(*) FROM comicsbbdd;";
	private static final String SENTENCIA_BUSQUEDA_INDIVIDUAL = "SELECT * FROM comicsbbdd WHERE ID = ?;";
	private static final String SENTENCIA_CONTAR_COMICS_POR_ID = "SELECT COUNT(*) FROM comicsbbdd WHERE ID = ?;";
	private static final String SENTENCIA_BUSCAR_PORTADA = "SELECT portada FROM comicsbbdd WHERE ID = ?;";
	public static final String SENTENCIA_BUSQUEDA_COMPLETA = "SELECT * FROM comicsbbdd";
	public static final String SENTENCIA_TOTAL_BUSQUEDA = "SELECT COUNT(*) FROM comicsbbdd WHERE 1=1;";

	public static final String SENTENCIA_POSESION = "SELECT * FROM comicsbbdd WHERE estado = 'En posesion' ORDER BY nomComic, fecha_publicacion, numComic;";
	public static final String SENTENCIA_KEY_ISSUE = "SELECT * FROM comicsbbdd WHERE key_issue <> 'Vacio' ORDER BY nomComic, fecha_publicacion, numComic;";
	public static final String SENTENCIA_COMPLETA = "SELECT * FROM comicsbbdd ORDER BY nomComic, fecha_publicacion, numComic;";
	public static final String SENTENCIA_VENDIDOS = "SELECT * FROM comicsbbdd WHERE estado = 'Vendido' ORDER BY nomComic, fecha_publicacion, numComic;";
	public static final String SENTENCIA_COMPRADOS = "SELECT * FROM comicsbbdd WHERE estado = 'Comprado' ORDER BY nomComic, fecha_publicacion, numComic;";
	public static final String SENTENCIA_PUNTUACION = "SELECT * FROM comicsbbdd WHERE puntuacion <> 'Sin puntuar' ORDER BY nomComic, fecha_publicacion, numComic;";
	public static final String SENTENCIA_FIRMADOS = "SELECT * FROM comicsbbdd WHERE Firma <> '' ORDER BY nomComic, fecha_publicacion, numComic;";

	/**
	 * Funcion que permite contar cuantas filas hay en la base de datos.
	 *
	 * @return
	 */
	public static int countRows() {

		try (Connection conn = ConectManager.conexion();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(TAMANIO_DATABASE)) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
		return -1;
	}

	public static int getCount(String sql) {

		int count = 0;
		try (Connection conn = ConectManager.conexion();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * Devuelve un objeto Comic cuya ID coincida con el parámetro de búsqueda.
	 *
	 * @param identificador el ID del cómic a buscar
	 * @return el objeto Comic encontrado, o null si no se encontró ningún cómic con
	 *         ese ID
	 * @throws SQLException si ocurre algún error al ejecutar la consulta SQL
	 */
	public static Comic comicDatos(String identificador) {

		Comic comic = null;

		try (Connection conn = ConectManager.conexion();
				PreparedStatement statement = conn.prepareStatement(SENTENCIA_BUSQUEDA_INDIVIDUAL)) {
			statement.setString(1, identificador);

			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					comic = DBUtilidades.obtenerComicDesdeResultSet(rs);
				}
			} catch (SQLException e) {
				Utilidades.manejarExcepcion(e);
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return comic;
	}

	/**
	 * Comprueba si el identificador introducido existe en la base de datos.
	 *
	 * @param identificador El identificador a verificar.
	 * @return true si el identificador existe en la base de datos, false si no
	 *         existe.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static boolean comprobarIdentificadorComic(String identificador) {

		if (identificador == null || identificador.trim().isEmpty()) {
			return false; // Si el identificador es nulo o está vacío, se considera que no existe
		}

		boolean existe = false; // Variable para almacenar si el identificador existe en la base de datos

		try (Connection conn = ConectManager.conexion();
				PreparedStatement preparedStatement = conn.prepareStatement(SENTENCIA_CONTAR_COMICS_POR_ID)) {

			preparedStatement.setString(1, identificador.trim());

			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					int rowCount = rs.getInt(1);
					existe = (rowCount > 0);
				}
			}
		} catch (Exception e) {
			Utilidades.manejarExcepcion(e);
			// Manejar error de sintaxis SQL de manera específica
			return false;
		}

		return existe; // Devolver si el identificador existe en la base de datos o no
	}

	/**
	 * Obtiene la dirección de la portada de un cómic.
	 *
	 * @param idComic ID del cómic
	 * @return Dirección de la portada del cómic
	 * @throws SQLException Si ocurre algún error de SQL
	 */
	public static String obtenerDireccionPortada(String idComic) {

		try (Connection conn = ConectManager.conexion();
				PreparedStatement ps = conn.prepareStatement(SENTENCIA_BUSCAR_PORTADA)) {
			ps.setString(1, idComic);
			try (ResultSet resultado = ps.executeQuery()) {
				if (resultado.next()) {
					String portada = resultado.getString("portada");
					if (portada != null && !portada.isEmpty()) {
						return portada;
					}
				}
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}

		return null;
	}

	/**
	 * Función que busca en el ArrayList el o los cómics que tengan coincidencia con
	 * los datos introducidos en el TextField.
	 *
	 * @param comic           el cómic con los parámetros de búsqueda
	 * @param busquedaGeneral el texto de búsqueda general
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException si ocurre un error al acceder a la base de datos
	 */
	public static List<Comic> busquedaParametro(Comic comic, String busquedaGeneral) {

		List<Comic> listComic = new ArrayList<>();
		if (ComicManagerDAO.countRows() > 0) {
			if (!busquedaGeneral.isEmpty()) {

				listComic = SelectManager.libreriaSeleccionado(busquedaGeneral);

				if (listComic.isEmpty() && Comic.validarComic(comic)) {
					return DBUtilidades.filtroBBDD(comic, busquedaGeneral);
				}

				return listComic;
			} else {
				if (Comic.validarComic(comic)) {
					return DBUtilidades.filtroBBDD(comic, busquedaGeneral);
				}
			}

		}
		return listComic;
	}

	public static boolean hayDatosEnLibreria(String sentenciaSQL) {
		if (sentenciaSQL.isEmpty()) {
			sentenciaSQL = SENTENCIA_BUSQUEDA_COMPLETA;
		}

		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery()) {

			// Si hay resultados, devolver true
			return rs.first();

		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
		return false;
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public static List<Comic> libreriaSeleccionado(String datoSeleccionado) {
		String sentenciaSQL = "SELECT * FROM comicsbbdd " + "WHERE nomVariante LIKE '%" + datoSeleccionado + "%' OR "
				+ "nomComic LIKE '%" + datoSeleccionado + "%' OR " + "nomGuionista LIKE '%" + datoSeleccionado
				+ "%' OR " + "firma LIKE '%" + datoSeleccionado + "%' OR " + "nomDibujante LIKE '%" + datoSeleccionado
				+ "%' OR " + "CAST(numComic AS TEXT) LIKE '%" + datoSeleccionado + "%' OR " + "nomEditorial LIKE '%"
				+ datoSeleccionado + "%' OR " + "nivel_gradeo LIKE '%" + datoSeleccionado + "%' OR " + "formato LIKE '%"
				+ datoSeleccionado + "%' OR " + "fecha_publicacion LIKE '%" + datoSeleccionado + "%' OR "
				+ "procedencia LIKE '%" + datoSeleccionado + "%' "
				+ "ORDER BY nomComic, numComic ASC, fecha_publicacion";

		return verLibreria(sentenciaSQL, false);
	}

	/**
	 * Método que muestra los cómics de la librería según la sentencia SQL
	 * proporcionada.
	 * 
	 * @param sentenciaSQL La sentencia SQL para obtener los cómics de la librería.
	 * @return Una lista de objetos Comic que representan los cómics de la librería.
	 */
	public static List<Comic> verLibreria(String sentenciaSQL, boolean esActualizacion) {
		ListaComicsDAO.listaComics.clear(); // Limpiar la lista existente de cómics
		List<Comic> listaComics = new ArrayList<>();

		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery()) {

			if (esActualizacion) {
				while (rs.next()) {
					Comic comic = DBUtilidades.obtenerComicDesdeResultSet(rs);
					if (!comic.getcodigoComic().isEmpty() && !comic.getcodigoComic().equals("0")) {
						listaComics.add(comic);
					}
				}
			} else {
				while (rs.next()) {
					listaComics.add(DBUtilidades.obtenerComicDesdeResultSet(rs));
				}
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return listaComics;
	}

}
