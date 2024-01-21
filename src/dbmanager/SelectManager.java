package dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Controladores.CrearBBDDController;
import Funcionamiento.Utilidades;
import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.CommonFunctions.TipoBusqueda;
import javafx.scene.control.Label;

public class SelectManager {


	
	/**
	 * Realiza llamadas para inicializar listas de autocompletado.
	 *
	 * @throws SQLException si ocurre un error al acceder a la base de datos.
	 */
	public static void listasAutoCompletado() {
		DBLibreriaManager.listaNombre = obtenerValoresColumna("nomComic");
		DBLibreriaManager.listaNumeroComic = obtenerValoresColumna("numComic");
		DBLibreriaManager.listaVariante = obtenerValoresColumna("nomVariante");
		DBLibreriaManager.listaFirma = obtenerValoresColumna("firma");
		DBLibreriaManager.listaFormato = obtenerValoresColumna("formato");
		DBLibreriaManager.listaEditorial = obtenerValoresColumna("nomEditorial");
		DBLibreriaManager.listaGuionista = obtenerValoresColumna("nomGuionista");
		DBLibreriaManager.listaDibujante = obtenerValoresColumna("nomDibujante");
		DBLibreriaManager.listaProcedencia = obtenerValoresColumna("procedencia");
		DBLibreriaManager.listaCaja = obtenerValoresColumna("caja_deposito");
		DBLibreriaManager.listaImagenes = obtenerValoresColumna("portada");

		DBLibreriaManager.itemsList = Arrays.asList(DBLibreriaManager.listaNombre, DBLibreriaManager.listaNumeroComic,
				DBLibreriaManager.listaVariante, DBLibreriaManager.listaProcedencia, DBLibreriaManager.listaFormato,
				DBLibreriaManager.listaDibujante, DBLibreriaManager.listaGuionista, DBLibreriaManager.listaEditorial,
				DBLibreriaManager.listaFirma, DBLibreriaManager.listaCaja);
	}

	/**
	 * Funcion que permite contar cuantas filas hay en la base de datos.
	 *
	 * @return
	 */
	public static int countRows() {
		String sql = "SELECT COUNT(*) FROM comicsbbdd";
		try (Connection conn = ConectManager.conexion();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
			return 0;
		}
		return 0;
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
		DBLibreriaManager.listaComics.clear();
		return CommonFunctions.guardarDatosAutoCompletado(sentenciaSQL, columna);
	}

	/**
	 * Método que muestra los cómics de la librería según la sentencia SQL
	 * proporcionada.
	 * 
	 * @param sentenciaSQL La sentencia SQL para obtener los cómics de la librería.
	 * @return Una lista de objetos Comic que representan los cómics de la librería.
	 * @throws SQLException Si ocurre algún error al ejecutar la consulta SQL.
	 */
	public static List<Comic> verLibreria(String sentenciaSQL) {
		DBLibreriaManager.listaComics.clear(); // Limpiar la lista existente de cómics
		List<Comic> listaComics = new ArrayList<>();

		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				listaComics.add(obtenerComicDesdeResultSet(rs));
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return listaComics;
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
		DBLibreriaManager.listaComics.clear();

		// Crear la consulta SQL a partir de los datos proporcionados
		String sql = datosConcatenados(datos);

		// Verificar si la consulta SQL no está vacía
		if (!sql.isEmpty()) {
			try (Connection conn = ConectManager.conexion();
					PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {

				// Llenar la lista de cómics con los resultados obtenidos
				while (rs.next()) {
					DBLibreriaManager.listaComics.add(obtenerComicDesdeResultSet(rs));
				}

				return DBLibreriaManager.listaComics;
			} catch (SQLException ex) {
				// Manejar la excepción según tus necesidades (en este caso, mostrar una alerta)
				Utilidades.manejarExcepcion(ex);
			}
		}

		return DBLibreriaManager.listaComics; // Devolver null si la consulta SQL está vacía
	}

	/**
	 * Concatena los datos del objeto Comic para formar una consulta SQL SELECT.
	 * 
	 * @param comic El objeto Comic que contiene los datos de búsqueda.
	 * @return Una cadena SQL SELECT con las condiciones de búsqueda.
	 */
	public static String datosConcatenados(Comic comic) {
		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder("SELECT * FROM comicsbbdd");

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

			DBLibreriaManager.listaComics.clear();

			agregarSiHayDatos(rs1);
			agregarSiHayDatos(rs2);
			agregarSiHayDatos(rs3);
			agregarSiHayDatos(rs4);
			agregarSiHayDatos(rs5);

			DBLibreriaManager.listaComics = Utilidades.listaArreglada(DBLibreriaManager.listaComics);
			return DBLibreriaManager.listaComics;

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
	 * Devuelve un objeto Comic cuya ID coincida con el parámetro de búsqueda.
	 *
	 * @param identificador el ID del cómic a buscar
	 * @return el objeto Comic encontrado, o null si no se encontró ningún cómic con
	 *         ese ID
	 * @throws SQLException si ocurre algún error al ejecutar la consulta SQL
	 */
	public static Comic comicDatos(String identificador) {
		Comic comic = null;
		String sentenciaSQL = "SELECT * FROM comicsbbdd WHERE ID = ?";

		try (Connection conn = ConectManager.conexion();
				PreparedStatement statement = conn.prepareStatement(sentenciaSQL)) {
			statement.setString(1, identificador);

			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					comic = obtenerComicDesdeResultSet(rs);
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

		String sentenciaSQL = "SELECT 1 FROM comicsbbdd WHERE ID = ? LIMIT 1";
		boolean existe = false; // Variable para almacenar si el identificador existe en la base de datos

		try (Connection conn = ConectManager.conexion();
				PreparedStatement preparedStatement = conn.prepareStatement(sentenciaSQL)) {

			preparedStatement.setString(1, identificador.trim());

			try (ResultSet rs = preparedStatement.executeQuery()) {
				existe = rs.next(); // Si hay al menos una fila, el identificador existe
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
			return false; // Devolver false en caso de error
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
		String query = "SELECT portada FROM comicsbbdd WHERE ID = ?";
		try (Connection conn = ConectManager.conexion(); PreparedStatement ps = conn.prepareStatement(query)) {
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
		String sentenciaSQL = "SELECT * from comicsbbdd";
		boolean existeBase = hayDatosEnLibreria(sentenciaSQL);

		List<Comic> listComic = new ArrayList<Comic>();
		if (existeBase) {
			if (busquedaGeneral.length() != 0) {
				return listComic = verBusquedaGeneral(busquedaGeneral);
			} else {
				try {
					return listComic = filtroBBDD(comic);
				} catch (NullPointerException ex) {
					Utilidades.manejarExcepcion(ex);
				}
			}
		}
		return listComic;
	}

	public static boolean hayDatosEnLibreria(String sentenciaSQL) {

		if (sentenciaSQL.isEmpty()) {
			sentenciaSQL = "SELECT * from comicsbbdd";
		}

		if (!Utilidades.validarDatosConexion()) {
			return false;
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

	public static List<Comic> buscarEnLibreria(TipoBusqueda tipoBusqueda) {
		String sentenciaSQL = "";

		switch (tipoBusqueda) {
		case POSESION:
			sentenciaSQL = "SELECT * from comicsbbdd where estado = 'En posesion' ORDER BY nomComic, fecha_publicacion, numComic";
			break;
		case KEY_ISSUE:
			sentenciaSQL = "SELECT * FROM comicsbbdd WHERE key_issue <> 'Vacio' ORDER BY nomComic, fecha_publicacion, numComic";
			break;
		case COMPLETA:
			sentenciaSQL = "SELECT * FROM comicsbbdd ORDER BY nomComic, fecha_publicacion, numComic";
			break;
		case VENDIDOS:
			sentenciaSQL = "SELECT * from comicsbbdd where estado = 'Vendido' ORDER BY nomComic,fecha_publicacion,numComic";
			break;
		case COMPRADOS:
			sentenciaSQL = "SELECT * from comicsbbdd where estado = 'Comprado' ORDER BY nomComic,fecha_publicacion,numComic";
			break;
		case PUNTUACION:
			sentenciaSQL = "SELECT * from comicsbbdd where puntuacion <> 'Sin puntuar' ORDER BY nomComic,fecha_publicacion,numComic";
			break;
		case FIRMADOS:
			sentenciaSQL = "SELECT * from comicsbbdd where Firma <> '' ORDER BY nomComic,fecha_publicacion,numComic";
			break;
		}

		return verLibreria(sentenciaSQL);
	}



	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public static List<Comic> libreriaSeleccionado(String datoSeleccionado) throws SQLException {
		String sentenciaSQL = "SELECT * FROM comicsbbdd " + "WHERE nomVariante LIKE '%" + datoSeleccionado
				+ "%' OR nomComic LIKE '%" + datoSeleccionado + "%' OR nomGuionista LIKE '%" + datoSeleccionado
				+ "%' OR firma LIKE '%" + datoSeleccionado + "%' OR nomDibujante LIKE '%" + datoSeleccionado
				+ "%' OR nomComic LIKE '%" + datoSeleccionado + "%' OR nomEditorial LIKE '%" + datoSeleccionado
				+ "%' OR caja_deposito LIKE '%" + datoSeleccionado + "%' OR formato LIKE '%" + datoSeleccionado
				+ "%' OR fecha_publicacion LIKE '%" + datoSeleccionado + "%' OR procedencia LIKE '%" + datoSeleccionado
				+ "%' ORDER BY nomComic, fecha_publicacion, numComic ASC";

		return verLibreria(sentenciaSQL);
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
		// Inicializar con una condición verdadera
		String sentenciaSQL = "SELECT COUNT(*) FROM comicsbbdd WHERE 1=1";

		// Lista de condiciones de búsqueda
		List<String> condiciones = new ArrayList<>();

		// Agregar condiciones según los campos no vacíos o no nulos en el objeto Comic
		agregarCondicion(!comic.getVariante().isEmpty(), "nomVariante = ?", condiciones);
		agregarCondicion(!comic.getNombre().isEmpty(), "nomComic = ?", condiciones);
		agregarCondicion(!comic.getNumero().isEmpty(), "numComic = ?", condiciones);
		agregarCondicion(!comic.getGuionista().isEmpty(), "nomGuionista = ?", condiciones);
		agregarCondicion(!comic.getDibujante().isEmpty(), "nomDibujante = ?", condiciones);
		agregarCondicion(!comic.getFirma().isEmpty(), "firma = ?", condiciones);
		agregarCondicion(!comic.getEditorial().isEmpty(), "nomEditorial = ?", condiciones);
		agregarCondicion(!comic.getNumCaja().isEmpty(), "caja_deposito = ?", condiciones);
		agregarCondicion(!comic.getFormato().isEmpty(), "formato = ?", condiciones);
		agregarCondicion(!comic.getFecha().isEmpty(), "fecha_publicacion = ?", condiciones);
		agregarCondicion(!comic.getProcedencia().isEmpty(), "procedencia = ?", condiciones);

		// Combinar las condiciones en la sentencia SQL
		if (!condiciones.isEmpty()) {
			sentenciaSQL += " AND " + String.join(" AND ", condiciones);
		}

		int count = 0;

		try (Connection conn = ConectManager.conexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
			// Establecer parámetros según las condiciones
			int paramIndex = 1;
			for (String condicion : condiciones) {
				if (condicion.endsWith("?")) {
					ps.setString(paramIndex++, obtenerValorSeguro(extraerValorCondicion(condicion, comic)));
				}
			}

			try (ResultSet resultado = ps.executeQuery()) {
				if (resultado.next()) {
					count = resultado.getInt(1);
				}
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);

		}

		return count;
	}

	/**
	 * Comprueba si existe una base de datos con el nombre especificado para la
	 * creación.
	 *
	 * @return true si la base de datos no existe, false si ya existe o si hay un
	 *         error en la conexión
	 */
	public static boolean checkDatabaseExists(Label prontInformativo, String nombreDataBase) {
		AlarmaList.detenerAnimacion();
		boolean exists = false;
		String sentenciaSQL = "SELECT COUNT(*) FROM information_schema.tables";

		if (!CrearBBDDController.DB_NAME.isEmpty()) {
			sentenciaSQL += " WHERE table_schema = '" + CrearBBDDController.DB_NAME + "'";
		}

		try (ResultSet rs = comprobarDataBase(sentenciaSQL)) {
			int count = rs.getInt("COUNT(*)");
			exists = count < 1;

			if (exists) {
				return true;
			} else {
				AlarmaList.iniciarAnimacionBaseExiste(prontInformativo, CrearBBDDController.DB_NAME);
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
		return false;
	}

	// Método auxiliar para agregar condiciones a la lista
	private static void agregarCondicion(boolean condicion, String sqlFragment, List<String> condiciones) {
		if (condicion) {
			condiciones.add(sqlFragment);
		}
	}

	// Método auxiliar para extraer el valor de la condición (sin el operador LIKE)
	private static String extraerValorCondicion(String condicion, Comic comic) {
		// Suponemos que la condición siempre tiene el formato "campo LIKE ?"
		String campo = condicion.split(" ")[0];
		switch (campo) {
		case "nomVariante":
			return comic.getVariante();
		case "nomComic":
			return comic.getNombre();
		case "numComic":
			return comic.getNumero();
		case "nomGuionista":
			return comic.getGuionista();
		case "nomDibujante":
			return comic.getDibujante();
		case "firma":
			return comic.getFirma();
		case "nomEditorial":
			return comic.getEditorial();
		case "caja_deposito":
			return comic.getNumCaja();
		case "formato":
			return comic.getFormato();
		case "fecha_publicacion":
			return comic.getFecha();
		case "procedencia":
			return comic.getProcedencia();
		default:
			return "";
		}
	}

	// Método auxiliar para obtener un valor seguro (no nulo)
	private static String obtenerValorSeguro(String valor) {
		return (valor != null) ? valor : "''";
	}

	/**
	 * Verifica la base de datos ejecutando una sentencia SQL y devuelve el
	 * ResultSet correspondiente.
	 * 
	 * @param sentenciaSQL la sentencia SQL a ejecutar
	 * @return el ResultSet que contiene los resultados de la consulta
	 */
	public static ResultSet comprobarDataBase(String sentenciaSQL) {

		String url = "jdbc:mysql://" + CrearBBDDController.DB_HOST + ":" + CrearBBDDController.DB_PORT
				+ "?serverTimezone=UTC";
		Statement statement;

		try {
			Connection connection = DriverManager.getConnection(url, CrearBBDDController.DB_USER,
					CrearBBDDController.DB_PASS);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = statement.executeQuery(sentenciaSQL);

			if (rs.next()) {
				return rs;
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return null;
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
