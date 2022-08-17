package Funcionamiento;

/**
 * Programa que permite el acceso a una base de datos de comics. Mediante JDBC con mySql
 * Las ventanas graficas se realizan con JavaFX.
 * El programa permite:
 *  - Conectarse a la base de datos.
 *  - Ver la base de datos completa o parcial segun parametros introducidos.
 *  - Guardar el contenido de la base de datos en un fichero .txt y .xlsx,CSV
 *  - Copia de seguridad de la base de datos en formato .sql
 *  - Introducir comics a la base de datos.
 *  - Modificar comics de la base de datos.
 *  - Eliminar comics de la base de datos(Solamente cambia el estado de "En posesion" a "Vendido". Los datos siguen en la bbdd pero estos no los muestran el programa
 *  - Ver frases de personajes de comics
 *  - Opcion de escoger algo para leer de forma aleatoria.
 *
 *  Esta clase permite realizar operaciones con la libreria de comics de la base de datos.
 *
 *  Version Final
 *
 *  Por Alejandro Rodriguez
 *
 *  Twitter: @silverAlox
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase sirve para realizar las diferentes operaciones en la base de datos
 * que tenga que ver con la libreria de comics
 *
 * @author Alejandro Rodriguez
 */
public class Libreria extends Comic {

	public static List<Comic> listaComics = new ArrayList<>();
	public static List<Comic> listaPosesion = new ArrayList<>();
	public static List<Comic> listaVendidos = new ArrayList<>();
	public static List<Comic> listaPuntuacion = new ArrayList<>();
	public static List<Comic> listaCompleta = new ArrayList<>();
	public static List<Comic> filtroComics = new ArrayList<>();

	private Connection conn = ConexionBBDD.conexion();
	private NavegacionVentanas nav = new NavegacionVentanas();

	/**
	 * Devuelve todos los datos de la base de datos.
	 *
	 * @return
	 */
	public Comic[] verLibreria() {

		listaPosesion.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where estado = 'En posesion'";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;

		rs = ConexionBBDD.getComic(sentenciaSql);
		listaPosesion = listaDatos(rs);

		comic = new Comic[listaPosesion.size()];
		comic = listaPosesion.toArray(comic);

		return comic;
	}

	/**
	 *
	 * @return
	 */
	public Comic[] verLibreriaVendidos() {

		listaVendidos.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where estado = 'Vendido'";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;

		rs = ConexionBBDD.getComic(sentenciaSql);
		listaVendidos = listaDatos(rs);

		comic = new Comic[listaVendidos.size()];
		comic = listaVendidos.toArray(comic);

		return comic;
	}

	/**
	 *
	 * @return
	 */
	public Comic[] verLibreriaPuntuacion() {

		listaPuntuacion.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where puntuacion <>''";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;

		rs = ConexionBBDD.getComic(sentenciaSql);
		listaPuntuacion = listaDatos(rs);

		comic = new Comic[listaPuntuacion.size()];
		comic = listaPuntuacion.toArray(comic);

		return comic;
	}

	/**
	 * Devuelve todos los datos de la base de datos.
	 *
	 * @return
	 */
	public Comic[] verLibreriaCompleta() {

		listaCompleta.clear();

		String sentenciaSql = "SELECT * from comicsbbdd";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;
		rs = ConexionBBDD.getComic(sentenciaSql);
		listaCompleta = listaDatos(rs);
		comic = new Comic[listaCompleta.size()];
		comic = listaCompleta.toArray(comic);
		return comic;
	}

	/**
	 * Devuelve datos de la base de datos segun el parametro.
	 *
	 * @param datos
	 * @return
	 */
	public Comic[] filtadroBBDD(Comic datos) {

		reiniciarBBDD();

		Comic comic[] = null;

		String sql = datosConcatenados(datos);

		filtroComics.clear();

		try {
			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				filtroComics = listaDatos(rs);
			}
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}

		comic = new Comic[filtroComics.size()];
		comic = filtroComics.toArray(comic);
		return comic;
	}

	/**
	 * Funcion que permite hacer una busqueda general mediante 1 sola palabra, hace
	 * una busqueda en ciertos identificadores de la tabla.
	 *
	 * @param sentencia
	 * @return
	 */
	public Comic[] verBusquedaGeneral(String busquedaGeneral) {

		String sql1 = datosGeneralesNombre(busquedaGeneral);
		String sql2 = datosGeneralesVariante(busquedaGeneral);
		String sql3 = datosGeneralesFirma(busquedaGeneral);
		String sql4 = datosGeneralesGuionista(busquedaGeneral);
		String sql5 = datosGeneralesDibujante(busquedaGeneral);

		Comic comic[] = null;

		reiniciarBBDD();

		filtroComics.clear();

		try {
			PreparedStatement ps1 = conn.prepareStatement(sql1);
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			PreparedStatement ps3 = conn.prepareStatement(sql3);
			PreparedStatement ps4 = conn.prepareStatement(sql4);
			PreparedStatement ps5 = conn.prepareStatement(sql5);

			ResultSet rs1 = ps1.executeQuery();
			ResultSet rs2 = ps2.executeQuery();
			ResultSet rs3 = ps3.executeQuery();
			ResultSet rs4 = ps4.executeQuery();
			ResultSet rs5 = ps5.executeQuery();

			if (rs1.next()) {
				filtroComics = listaDatos(rs1);
			}
			if (rs2.next()) {
				filtroComics = listaDatos(rs2);
			}
			if (rs3.next()) {
				filtroComics = listaDatos(rs3);
			}
			if (rs4.next()) {
				filtroComics = listaDatos(rs4);
			}
			if (rs5.next()) {
				filtroComics = listaDatos(rs5);
			}

			filtroComics = Utilidades.listaArreglada(filtroComics);

		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}

		ordenarBBDD();

		comic = new Comic[filtroComics.size()];

		comic = filtroComics.toArray(comic);

		return comic;
	}

	/**
	 * Funcion que segun los datos introducir mediante parametros, concatenara las
	 * siguientes cadenas de texto. Sirve para hacer busqueda en una base de datos
	 *
	 * @param datos
	 * @return
	 */
	public String datosConcatenados(Comic comic) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		if (comic.getID().length() != 0) {

			sql.append(connector).append("ID = " + comic.getID());
			connector = " AND ";
		}
		if (comic.getNombre().length() != 0) {

			sql.append(connector).append("nomComic like'%" + comic.getNombre() + "%'");
			connector = " AND ";
		}
		if (comic.getNumero().length() != 0) {
			sql.append(connector).append("numComic = " + comic.getNumero());
			connector = " AND ";
		}
		if (comic.getVariante().length() != 0) {
			sql.append(connector).append("nomVariante like'%" + comic.getVariante() + "%'");
			connector = " AND ";
		}
		if (comic.getFirma().length() != 0) {
			sql.append(connector).append("firma like'%" + comic.getFirma() + "%'");
			connector = " AND ";
		}
		if (comic.getEditorial().length() != 0) {
			sql.append(connector).append("nomEditorial like'%" + comic.getEditorial() + "%'");
			connector = " AND ";
		}
		if (comic.getFormato().length() != 0) {
			sql.append(connector).append("formato like'%" + comic.getFormato() + "%'");
			connector = " AND ";
		}
		if (comic.getProcedencia().length() != 0) {
			sql.append(connector).append("procedencia like'%" + comic.getProcedencia() + "%'");
			connector = " AND ";
		}
		if (comic.getFecha().length() != 0) {
			sql.append(connector).append("anioPubli like'%" + comic.getFecha() + "%'");
			connector = " AND ";
		}
		if (comic.getGuionista().length() != 0) {
			sql.append(connector).append("nomGuionista like'%" + comic.getGuionista() + "%'");
			connector = " AND ";
		}
		if (comic.getDibujante().length() != 0) {
			sql.append(connector).append("nomDibujante like'%" + comic.getDibujante() + "%'");
			connector = " AND ";
		}

		return sql.toString();
	}

	/**
	 * Funcion que hace una busqueda de un identificador en concreto de la tabla
	 *
	 * @param datos
	 * @return
	 */
	public String datosGeneralesNombre(String busquedaGeneral) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		sql.append(connector).append("nomComic like'%" + busquedaGeneral + "%'");

		return sql.toString();
	}

	/**
	 * Funcion que hace una busqueda de un identificador en concreto de la tabla
	 *
	 * @param datos
	 * @return
	 */
	public String datosGeneralesVariante(String busquedaGeneral) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		sql.append(connector).append("nomVariante like'%" + busquedaGeneral + "%'");

		return sql.toString();
	}

	/**
	 * Funcion que hace una busqueda de un identificador en concreto de la tabla
	 *
	 * @param datos
	 * @return
	 */
	public String datosGeneralesFirma(String busquedaGeneral) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		sql.append(connector).append("firma like'%" + busquedaGeneral + "%'");

		return sql.toString();
	}

	/**
	 * Funcion que hace una busqueda de un identificador en concreto de la tabla
	 *
	 * @param datos
	 * @return
	 */
	public String datosGeneralesGuionista(String busquedaGeneral) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		sql.append(connector).append("nomGuionista like'%" + busquedaGeneral + "%'");

		return sql.toString();
	}

	/**
	 * Funcion que hace una busqueda de un identificador en concreto de la tabla
	 *
	 * @param datos
	 * @return
	 */
	public String datosGeneralesDibujante(String busquedaGeneral) {
		String connector = " AND ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd where estado = 'En posesion'");

		sql.append(connector).append("nomDibujante like'%" + busquedaGeneral + "%'");

		return sql.toString();
	}

	/**
	 * Devuelve una lista con todos los datos de los comics de la base de datos
	 *
	 * @param rs
	 * @return
	 */
	public List<Comic> listaDatos(ResultSet rs) {

		try {
			if (rs != null) {
				do {

					this.ID = rs.getString("ID");
					this.nombre = rs.getString("nomComic");
					this.numero = rs.getString("numComic");
					this.variante = rs.getString("nomVariante");
					this.firma = rs.getString("firma");
					this.editorial = rs.getString("nomEditorial");
					this.formato = rs.getString("formato");
					this.procedencia = rs.getString("procedencia");
					this.fecha = rs.getString("anioPubli");
					this.guionista = rs.getString("nomGuionista");
					this.dibujante = rs.getString("nomDibujante");
					this.estado = rs.getString("estado");
					this.puntuacion = rs.getString("puntuacion");

					Comic comic = new Comic(this.ID, this.nombre, this.numero, this.variante, this.firma,
							this.editorial, this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante,
							this.estado, this.puntuacion);

					listaComics.add(comic);

				} while (rs.next());
			}
		} catch (SQLException e) {
			nav.alertaException("Datos introducidos incorrectos.");
			e.printStackTrace();
		}

		return listaComics;
	}

	/**
	 * Devuelve solamente 1 comics de la base de datos.
	 *
	 * @param rs
	 * @return
	 */
	public Comic datosIndividual(ResultSet rs) {
		Comic comic = new Comic();

		try {
			if (rs != null) {
				do {
					this.ID = rs.getString("ID");
					this.nombre = rs.getString("nomComic");
					this.numero = rs.getString("numComic");
					this.variante = rs.getString("nomVariante");
					this.firma = rs.getString("firma");
					this.editorial = rs.getString("nomEditorial");
					this.formato = rs.getString("formato");
					this.procedencia = rs.getString("procedencia");
					this.fecha = rs.getString("anioPubli");
					this.guionista = rs.getString("nomGuionista");
					this.dibujante = rs.getString("nomDibujante");
					this.estado = rs.getString("estado");
					this.puntuacion = rs.getString("puntuacion");

					comic = new Comic(this.ID, this.nombre, this.numero, this.variante, this.firma, this.editorial,
							this.formato, this.procedencia, this.fecha, this.guionista, this.dibujante, this.estado,
							this.puntuacion);

				} while (rs.next());
			}
		} catch (SQLException e) {
			nav.alertaException("Datos introducidos incorrectos.");
		}
		return comic;
	}

	/**
	 * Comprueba si la lista de comics contiene o no algun dato
	 *
	 * @param listaComic
	 */
	// Funcion que comprueba si existe algun dato relacionado con la busqueda por
	// parametro del comic
	public boolean checkList(List<Comic> listaComic) {
		if (listaComic.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Permite reiniciar la pantalla donde se muestran los datos
	 */
	public void reiniciarBBDD() {
		filtroComics.clear();
		listaPosesion.clear();
	}

	/**
	 * Funcion que devuelve un comic cuya ID este como parametro de busqueda
	 *
	 * @param id
	 * @return
	 */
	public Comic comicDatos(String identificador) {
		Comic comic = new Comic();

		String sentenciaSQL = "select * from comicsbbdd where ID = " + identificador;

		ResultSet rs;

		rs = ConexionBBDD.getComic(sentenciaSQL);

		comic = datosIndividual(rs);

		return comic;
	}

	/**
	 * Comprueba que el ID introducido existe
	 *
	 * @return
	 */
	public boolean chechID(String identificador) {
		String sentenciaSQL = "select * from comicsbbdd where ID = " + identificador;

		if (identificador.length() != 0) { // Si has introducido ID a la hora de realizar la modificacion, permitira lo
			// siguiente

			try {
				Statement consultaID = conn.createStatement();
				ResultSet rs = consultaID.executeQuery(sentenciaSQL); // Realiza la consulta en la base de datos con la
				// sentencia de seleccionar datos de un comic
				// segun su ID

				if (rs.next()) // En caso de existir el dato, devolvera true
				{
					return true;
				} else {
					return false;
				}

			} catch (SQLException e) {
				nav.alertaException("No existe el " + identificador + " en la base de datos.");
			}
		}
		return false;
	}

	/**
	 *

	 */
	public void ordenarBBDD() {
		String sql = "SELECT * FROM comicsbbdd ORDER BY ID;";
		Statement st;
		try {
			st = conn.createStatement();
			st.execute(sql);
		} catch (SQLException e) {
			nav.alertaException(e.toString());
		}

	}
}
