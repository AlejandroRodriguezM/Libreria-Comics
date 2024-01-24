package dbmanager;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Funcionamiento.Utilidades;
import comicManagement.Comic;

public class CommonFunctions {

	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private final static String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private final static String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	/**
	 * Construimos la ruta al directorio "libreria_comics" dentro de "Documents" y
	 * añadimos el nombre de la base de datos y la carpeta "portadas".
	 */
	private final static String SOURCE_PATH = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
			+ ConectManager.DB_NAME + File.separator + "portadas" + File.separator;

	public enum TipoBusqueda {
		POSESION, KEY_ISSUE, COMPLETA, VENDIDOS, COMPRADOS, PUNTUACION, FIRMADOS
	}

	/**
	 * Permite modificar un cómic en la base de datos.
	 *
	 * @param id           Identificador del cómic a modificar.
	 * @param sentenciaSQL La consulta SQL para modificar el cómic.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static void modificarDatos(String id, String sentenciaSQL) throws SQLException {

		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);) {
			String direccion_portada = SelectManager.obtenerDireccionPortada(id);
			Utilidades.eliminarFichero(direccion_portada);

			if (id.length() != 0) {
				stmt.setString(1, id);

				// Ejecutar la sentencia SQL
				stmt.executeUpdate();
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
	}

	public static void setParameters(PreparedStatement ps, Comic datos, boolean includeID) throws SQLException {
		ps.setString(1, datos.getNombre());
		ps.setString(2, datos.getNumCaja() == null ? "0" : datos.getNumCaja());
		ps.setString(3, datos.getPrecio_comic());
		ps.setString(4, datos.getCodigo_comic());
		ps.setString(5, datos.getNumero());
		ps.setString(6, datos.getVariante());
		ps.setString(7, datos.getFirma());
		ps.setString(8, datos.getEditorial());
		ps.setString(9, datos.getFormato());
		ps.setString(10, datos.getProcedencia());
		ps.setString(11, datos.getFecha());
		ps.setString(12, datos.getGuionista());
		ps.setString(13, datos.getDibujante());

		if (includeID) {
			ps.setString(14, datos.getKey_issue());
		} else {
			ps.setString(14, "Sin puntuar");
		}

		ps.setString(15, datos.getImagen());
		ps.setString(16, datos.getKey_issue());
		ps.setString(17, datos.getUrl_referencia());

		if (includeID) {
			ps.setString(18, datos.getID());
		} else {
			ps.setString(18, datos.getEstado());
		}
	}

	/**
	 * Función que guarda los datos para autocompletado en una lista.
	 * 
	 * @param sentenciaSQL La sentencia SQL para obtener los datos.
	 * @param columna      El nombre de la columna que contiene los datos para
	 *                     autocompletado.
	 * @return Una lista de cadenas con los datos para autocompletado.
	 * @throws SQLException Si ocurre algún error al ejecutar la consulta SQL.
	 */
	public static List<String> guardarDatosAutoCompletado(String sentenciaSQL, String columna) {
		List<String> listaAutoCompletado = new ArrayList<>();

		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery()) {

			if (rs != null && rs.first()) {
				do {
					String datosAutocompletado = rs.getString(columna);
					if (columna.equals("nomComic")) {
						listaAutoCompletado.add(datosAutocompletado.trim());
					} else if (columna.equals("portada")) {
						listaAutoCompletado
								.add(SOURCE_PATH + Utilidades.obtenerUltimoSegmentoRuta(datosAutocompletado));
					} else {
						String[] nombres = datosAutocompletado.split("-");
						for (String nombre : nombres) {
							nombre = nombre.trim();
							if (!nombre.isEmpty()) {
								listaAutoCompletado.add(nombre);
							}
						}
					}
				} while (rs.next());

				listaAutoCompletado = Utilidades.listaArregladaAutoComplete(listaAutoCompletado);
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return listaAutoCompletado;
	}
	


}
