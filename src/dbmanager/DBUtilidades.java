package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import Controladores.OpcionesAvanzadasController;
import comicManagement.Comic;
import funcionesManagment.AccionReferencias;
import funciones_auxiliares.Utilidades;
import funciones_auxiliares.Ventanas;

public class DBUtilidades {

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	public enum TipoBusqueda {
		POSESION, KEY_ISSUE, COMPLETA, VENDIDOS, COMPRADOS, PUNTUACION, FIRMADOS, GUARDADOS, ELIMINAR
	}

	public static void setParameters(PreparedStatement ps, Comic datos, boolean includeID) throws SQLException {
		ps.setString(1, datos.getNombre());
		ps.setString(2, datos.getValorGradeo() == null ? "0" : datos.getValorGradeo());
		ps.setString(3, datos.getprecioComic());
		ps.setString(4, datos.getcodigoComic());
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
		ps.setString(16, datos.getkeyIssue());
		ps.setString(17, datos.getUrlReferencia());
		ps.setString(18, datos.getEstado());
		if (includeID) {
			ps.setString(19, datos.getid());
		}
	}

//	############################################
//	###########SELECT FUNCTIONS#################
//	############################################

	public static String construirSentenciaSQL(TipoBusqueda tipoBusqueda) {

		System.out.println(ConectManager.DB_NAME);

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

	public static String datosConcatenados(Comic comic) {
		String connector = " WHERE ";

		StringBuilder sql = new StringBuilder(SelectManager.SENTENCIA_BUSQUEDA_COMPLETA);

		connector = agregarCondicion(sql, connector, "ID", comic.getid());
		connector = agregarCondicion(sql, connector, "nomComic", comic.getNombre());
		connector = agregarCondicion(sql, connector, "nivel_gradeo", comic.getValorGradeo());
		connector = agregarCondicion(sql, connector, "numComic", comic.getNumero());
		connector = agregarCondicionLike(sql, connector, "nomVariante", comic.getVariante());
		connector = agregarCondicionLike(sql, connector, "firma", comic.getFirma());
		connector = agregarCondicionLike(sql, connector, "nomEditorial", comic.getEditorial());
		connector = agregarCondicionLike(sql, connector, "formato", comic.getFormato());
		connector = agregarCondicionLike(sql, connector, "procedencia", comic.getProcedencia());
		connector = agregarCondicionLike(sql, connector, "fecha_publicacion", comic.getFecha());
		connector = agregarCondicionLike(sql, connector, "nomGuionista", comic.getGuionista());
		connector = agregarCondicionLike(sql, connector, "nomDibujante", comic.getDibujante());

		if (connector.trim().equalsIgnoreCase("where")) {
			return "";
		}

		return (connector.length() > 0) ? sql.toString() : "";
	}

	public static String agregarCondicion(StringBuilder sql, String connector, String columna, String valor) {
		if (!valor.isEmpty()) {
			sql.append(connector).append(columna).append(" = '").append(valor).append("'");
			return " AND ";
		}
		return connector;
	}

	public static String agregarCondicionLike(StringBuilder sql, String connector, String columna, String valor) {
		if (!valor.isEmpty()) {
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
		sql.append("SELECT * FROM comicsbbdd");

		switch (tipoBusqueda.toLowerCase()) {
		case "nomcomic":
			sql.append(connector).append("nomComic LIKE '%" + busquedaGeneral + "%';");
			break;
		case "nomvariante":
			sql.append(connector).append("nomVariante LIKE '%" + busquedaGeneral + "%';");
			break;
		case "firma":
			sql.append(connector).append("firma LIKE '%" + busquedaGeneral + "%';");
			break;
		case "nomguionista":
			sql.append(connector).append("nomGuionista LIKE '%" + busquedaGeneral + "%';");
			break;
		case "nomdibujante":
			sql.append(connector).append("nomDibujante LIKE '%" + busquedaGeneral + "%';");
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

		System.out.println(sql1);
		System.out.println(sql2);
		System.out.println(sql3);
		System.out.println(sql4);
		System.out.println(sql5);

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

		return Collections.emptyList();
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
	public static List<Comic> filtroBBDD(Comic datos, String busquedaGeneral) {



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
		} else {
			getReferenciaVentana().getProntInfo().setOpacity(1);
			// Show error message in red when no search fields are specified
			getReferenciaVentana().getProntInfo().setStyle("-fx-text-fill: red;");
			getReferenciaVentana().getProntInfo()
					.setText("Error No existe comic con los datos: " + busquedaGeneral + datos.toString());
		}

		if (sql.isEmpty() && busquedaGeneral.isEmpty()) {
			getReferenciaVentana().getProntInfo().setOpacity(1);
			// Show error message in red when no search fields are specified
			getReferenciaVentana().getProntInfo().setStyle("-fx-text-fill: red;");
			getReferenciaVentana().getProntInfo().setText("Todos los campos estan vacios");
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
			String id = rs.getString("ID");
			String nombre = rs.getString("nomComic");
			String valorGradeo = rs.getString("nivel_gradeo");
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
			String keyIssue = rs.getString("key_issue");
			String puntuacion = rs.getString("puntuacion");
			String imagen = rs.getString("portada");
			String urlReferencia = rs.getString("url_referencia");
			String precioComic = rs.getString("precio_comic");
			String codigoComic = rs.getString("codigo_comic");

			return new Comic.ComicBuilder(id, nombre).valorGradeo(valorGradeo).numero(numero).variante(variante)
					.firma(firma).editorial(editorial).formato(formato).procedencia(procedencia).fecha(fecha)
					.guionista(guionista).dibujante(dibujante).estado(estado).keyIssue(keyIssue).puntuacion(puntuacion)
					.imagen(imagen).referenciaComic(urlReferencia).precioComic(precioComic).codigoComic(codigoComic)
					.build();
		} catch (SQLException e) {
			// Manejar la excepción según tus necesidades
			Utilidades.manejarExcepcion(e);
			return null; // O lanza una excepción personalizada, según el caso
		}
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		DBUtilidades.referenciaVentana = referenciaVentana;
	}
}
