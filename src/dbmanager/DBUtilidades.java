package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import comicManagement.Comic;
import funciones_auxiliares.Utilidades;

public class DBUtilidades {

	public enum TipoBusqueda {
		POSESION, KEY_ISSUE, COMPLETA, VENDIDOS, COMPRADOS, PUNTUACION, FIRMADOS, GUARDADOS, ELIMINAR
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
		ps.setString(14, "Sin puntuar");
		ps.setString(15, datos.getImagen());
		ps.setString(16, datos.getKey_issue());
		ps.setString(17, datos.getUrl_referencia());
		ps.setString(18, datos.getEstado());
		if (includeID) {
			ps.setString(19, datos.getID());
		}
	}

//	############################################
//	###########SELECT FUNCTIONS#################
//	############################################

	public static String construirSentenciaSQL(TipoBusqueda tipoBusqueda) {

		switch (tipoBusqueda) {
		case POSESION:
			return SelectManager.SENTENCIA_POSESION;
		case KEY_ISSUE:
			return SelectManager.SENTENCIA_KEY_ISSUE;
		case COMPLETA:
			return SelectManager.SENTENCIA_COMPLETA;
		case VENDIDOS:
			return SelectManager.SENTENCIA_VENDIDOS;
		case COMPRADOS:
			return SelectManager.SENTENCIA_COMPRADOS;
		case PUNTUACION:
			return SelectManager.SENTENCIA_PUNTUACION;
		case FIRMADOS:
			return SelectManager.SENTENCIA_FIRMADOS;
		default:
			throw new IllegalArgumentException("Tipo de búsqueda no válido");
		}
	}

	/**
	 * Devuelve el número total de resultados de la búsqueda en varios campos.
	 *
	 * @param comic El dato de búsqueda introducido por el usuario
	 * @return El número total de resultados que coinciden con los criterios de
	 *         búsqueda
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public static int numeroTotalSelecionado(Comic comic) {
		String connector = " WHERE ";
		String sentenciaParametrosBusqueda = "SELECT * from comicsbbdd";

		StringBuilder sql = new StringBuilder(sentenciaParametrosBusqueda);

		connector = agregarCondicion(sql, connector, "ID", comic.getID());
		connector = agregarCondicionLike(sql, connector, "nomComic", comic.getNombre());
		connector = agregarCondicion(sql, connector, "caja_deposito", comic.getNumCaja());
		connector = agregarCondicion(sql, connector, "numComic", comic.getNumero());
		connector = agregarCondicionLike(sql, connector, "nomVariante", comic.getVariante());
		connector = agregarCondicionLike(sql, connector, "firma", comic.getFirma());
		connector = agregarCondicionLike(sql, connector, "nomEditorial", comic.getEditorial());
		connector = agregarCondicionLike(sql, connector, "formato", comic.getFormato());
		connector = agregarCondicionLike(sql, connector, "procedencia", comic.getProcedencia());
		connector = agregarCondicionLike(sql, connector, "fecha_publicacion", comic.getFecha());
		connector = agregarCondicionLike(sql, connector, "nomGuionista", comic.getGuionista());
		connector = agregarCondicionLike(sql, connector, "nomDibujante", comic.getDibujante());

		if (!Comic.validarComic(comic)) {
			return 0;
		}

		int count = ComicManagerDAO.countRows();
		return count; // Devolver la cadena SQL solo si se han agregado condiciones

	}

	public static String datosConcatenados(Comic comic) {
		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder(SelectManager.SENTENCIA_BUSQUEDA_COMPLETA);

		connector = agregarCondicion(sql, connector, "ID", comic.getID());
		connector = agregarCondicion(sql, connector, "nomComic", comic.getNombre());
		connector = agregarCondicion(sql, connector, "caja_deposito", comic.getNumCaja());
		connector = agregarCondicion(sql, connector, "numComic", comic.getNumero());
		connector = agregarCondicionLike(sql, connector, "nomVariante", comic.getVariante());
		connector = agregarCondicionLike(sql, connector, "firma", comic.getFirma());
		connector = agregarCondicionLike(sql, connector, "nomEditorial", comic.getEditorial());
		connector = agregarCondicionLike(sql, connector, "formato", comic.getFormato());
		connector = agregarCondicionLike(sql, connector, "procedencia", comic.getProcedencia());
		connector = agregarCondicionLike(sql, connector, "fecha_publicacion", comic.getFecha());
		connector = agregarCondicionLike(sql, connector, "nomGuionista", comic.getGuionista());
		connector = agregarCondicionLike(sql, connector, "nomDibujante", comic.getDibujante());
//		
//		if (!Comic.validarComic(comic)) {
//			return "";
//		}

		return (connector.length() > 0) ? sql.toString() : "";
	}

	/**
	 * Agrega una condición de igualdad a la consulta SQL si el valor no está vacío.
	 * 
	 * @param sql       El StringBuilder que representa la consulta SQL.
	 * @param connector El conector lógico a utilizar (AND o WHERE).
	 * @param columna   El nombre de la columna en la base de datos.
	 * @param valor     El valor a comparar.
	 * @return El nuevo conector lógico a utilizar en las siguientes condiciones.
	 */
	public static String agregarCondicion(StringBuilder sql, String connector, String columna, String valor) {
		if (valor.length() != 0) {
			sql.append(connector).append(columna).append(" = '").append(valor).append("'");
			return " AND ";
		}
		return connector;
	}

	/**
	 * Agrega una condición de búsqueda con operador LIKE a la consulta SQL si el
	 * valor no está vacío.
	 * 
	 * @param sql       El StringBuilder que representa la consulta SQL.
	 * @param connector El conector lógico a utilizar (AND o WHERE).
	 * @param columna   El nombre de la columna en la base de datos.
	 * @param valor     El valor a comparar.
	 * @return El nuevo conector lógico a utilizar en las siguientes condiciones.
	 */
	public static String agregarCondicionLike(StringBuilder sql, String connector, String columna, String valor) {
		if (valor.length() != 0) {
			sql.append(connector).append(columna).append(" LIKE '%").append(valor).append("%'");
			return " AND ";
		}
		return connector;
	}

	/**
	 * Función que construye una consulta SQL para buscar un identificador en la
	 * tabla utilizando diferentes criterios de búsqueda.
	 *
	 * @param tipoBusqueda    Tipo de búsqueda (nomComic, nomVariante, firma,
	 *                        nomGuionista, nomDibujante).
	 * @param busquedaGeneral Término de búsqueda.
	 * @return Consulta SQL generada.
	 */
	public static String datosGenerales(String tipoBusqueda, String busquedaGeneral) {
		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd ");

		switch (tipoBusqueda.toLowerCase()) {
		case "nomcomic":
			sql.append(connector).append("nomComic LIKE '%" + busquedaGeneral + "%'");
			break;
		case "nomvariante":
			sql.append(connector).append("nomVariante LIKE '%" + busquedaGeneral + "%'");
			break;
		case "firma":
			sql.append(connector).append("firma LIKE '%" + busquedaGeneral + "%'");
			break;
		case "nomguionista":
			sql.append(connector).append("nomGuionista LIKE '%" + busquedaGeneral + "%'");
			break;
		case "nomdibujante":
			sql.append(connector).append("nomDibujante LIKE '%" + busquedaGeneral + "%'");
			break;
		default:
			// Tipo de búsqueda no válido, puedes manejarlo según tus necesidades
			break;
		}

		return sql.toString();
	}

	/**
	 * Funcion que permite hacer una busqueda general mediante 1 sola palabra, hace
	 * una busqueda en ciertos identificadores de la tabla.
	 *
	 * @param sentencia
	 * @return
	 * @throws SQLException
	 */
	public static List<Comic> verBusquedaGeneral(String busquedaGeneral) {
		String sql1 = datosGenerales("nomcomic", busquedaGeneral);
		String sql2 = datosGenerales("nomvariante", busquedaGeneral);
		String sql3 = datosGenerales("firma", busquedaGeneral);
		String sql4 = datosGenerales("nomguionista", busquedaGeneral);
		String sql5 = datosGenerales("nomdibujante", busquedaGeneral);

		try (Connection conn = ConectManager.conexion();
				PreparedStatement ps1 = conn.prepareStatement(sql1);
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				PreparedStatement ps3 = conn.prepareStatement(sql3);
				PreparedStatement ps4 = conn.prepareStatement(sql4);
				PreparedStatement ps5 = conn.prepareStatement(sql5);
				ResultSet rs1 = ps1.executeQuery();
				ResultSet rs2 = ps2.executeQuery();
				ResultSet rs3 = ps3.executeQuery();
				ResultSet rs4 = ps4.executeQuery();
				ResultSet rs5 = ps5.executeQuery()) {

			ListaComicsDAO.listaComics.clear();

			agregarSiHayDatos(rs1);
			agregarSiHayDatos(rs2);
			agregarSiHayDatos(rs3);
			agregarSiHayDatos(rs4);
			agregarSiHayDatos(rs5);

			ListaComicsDAO.listaComics = ListaComicsDAO.listaArreglada(ListaComicsDAO.listaComics);
			return ListaComicsDAO.listaComics;

		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}

		return null;
	}

	/**
	 * Agrega el primer conjunto de resultados a la lista de cómics si hay datos en
	 * el ResultSet.
	 * 
	 * @param rs El ResultSet que contiene los resultados de la consulta.
	 * @throws SQLException Si se produce un error al acceder a los datos del
	 *                      ResultSet.
	 */
	public static void agregarSiHayDatos(ResultSet rs) throws SQLException {
		if (rs.next()) {
			obtenerComicDesdeResultSet(rs);
		}
	}

	/**
	 * Filtra y devuelve una lista de cómics de la base de datos según los datos
	 * proporcionados.
	 *
	 * @param datos Objeto Comic con los datos para filtrar.
	 * @return Lista de cómics filtrados.
	 */
	public static List<Comic> filtroBBDD(Comic datos) {
		// Reiniciar la lista de cómics antes de realizar el filtrado
		ListaComicsDAO.listaComics.clear();

		// Crear la consulta SQL a partir de los datos proporcionados
		String sql = datosConcatenados(datos);

		// Verificar si la consulta SQL no está vacía
		if (!sql.isEmpty()) {
			try (Connection conn = ConectManager.conexion();
					PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {

				// Llenar la lista de cómics con los resultados obtenidos
				while (rs.next()) {
					ListaComicsDAO.listaComics.add(obtenerComicDesdeResultSet(rs));
				}

				return ListaComicsDAO.listaComics;
			} catch (SQLException ex) {
				// Manejar la excepción según tus necesidades (en este caso, mostrar una alerta)
				Utilidades.manejarExcepcion(ex);
			}
		}

		return ListaComicsDAO.listaComics; // Devolver null si la consulta SQL está vacía
	}

	/**
	 * Devuelve una lista de valores de una columna específica de la base de datos.
	 *
	 * @param columna Nombre de la columna de la base de datos.
	 * @return Lista de valores de la columna.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static List<String> obtenerValoresColumna(String columna) {
		String sentenciaSQL = "SELECT " + columna + " FROM comicsbbdd ORDER BY " + columna + " ASC";
		ListaComicsDAO.listaComics.clear();
		return ListaComicsDAO.guardarDatosAutoCompletado(sentenciaSQL, columna);
	}

	/**
	 * Crea y devuelve un objeto Comic a partir de los datos del ResultSet.
	 * 
	 * @param rs El ResultSet que contiene los datos del cómic.
	 * @return Un objeto Comic con los datos obtenidos del ResultSet.
	 * @throws SQLException Si se produce un error al acceder a los datos del
	 *                      ResultSet.
	 */
	public static Comic obtenerComicDesdeResultSet(ResultSet rs) {
		try {
			String ID = rs.getString("ID");
			String nombre = rs.getString("nomComic");
			String numCaja = rs.getString("caja_deposito");
			String numero = rs.getString("numComic");
			String variante = rs.getString("nomVariante");
			String firma = rs.getString("firma");
			String editorial = rs.getString("nomEditorial");
			String formato = rs.getString("formato");
			String procedencia = rs.getString("procedencia");
			String fecha = rs.getString("fecha_publicacion");
			String guionista = rs.getString("nomGuionista");
			String dibujante = rs.getString("nomDibujante");
			String estado = rs.getString("estado");
			String key_issue = rs.getString("key_issue");
			String puntuacion = rs.getString("puntuacion");
			String imagen = rs.getString("portada");
			String url_referencia = rs.getString("url_referencia");
			String precio_comic = rs.getString("precio_comic");
			String codigo_comic = rs.getString("codigo_comic");

			return new Comic(ID, nombre, numCaja, numero, variante, firma, editorial, formato, procedencia, fecha,
					guionista, dibujante, estado, key_issue, puntuacion, imagen, url_referencia, precio_comic,
					codigo_comic);
		} catch (SQLException e) {
			// Manejar la excepción según tus necesidades
			Utilidades.manejarExcepcion(e);
			return null; // O lanza una excepción personalizada, según el caso
		}
	}
}
