package Funcionamiento;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;

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

import Controladores.IntroducirDatosController;

/**
 * Esta clase sirve para realizar las diferentes operaciones en la base de datos
 * que tenga que ver con la libreria de comics
 *
 * @author Alejandro Rodriguez
 */
public class FuncionesComicsBBDD extends Comic {

	public static List<Comic> listaComics = new ArrayList<>();
	public static List<Comic> listaPosesion = new ArrayList<>();
	public static List<Comic> listaTratamiento = new ArrayList<>();
	public static List<Comic> listaCompleta = new ArrayList<>();
	public static List<Comic> filtroComics = new ArrayList<>();

	private static Ventanas nav = new Ventanas();
	private static Connection conn = null;
	private static IntroducirDatosController introducirDatos = null;
	/**
	 * Devuelve todos los datos de la base de datos, tanto vendidos como no vendidos
	 *
	 * @return
	 */
	public Comic[] verLibreriaCompleta() {

		listaCompleta.clear();

		String sentenciaSql = "SELECT * from comicsbbdd";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;
		rs = obtenLibreria(sentenciaSql);
		listaCompleta = listaDatos(rs);
		comic = new Comic[listaCompleta.size()];
		comic = listaCompleta.toArray(comic);
		return comic;
	}

	/**
	 * Devuelve todos los datos de la base de datos que se encuentren en posesion
	 *
	 * @return
	 */
	public Comic[] verLibreriaPosesion() {

		listaPosesion.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where estado = 'En posesion'";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;

		rs = obtenLibreria(sentenciaSql);
		listaPosesion = listaDatos(rs);

		comic = new Comic[listaPosesion.size()];
		comic = listaPosesion.toArray(comic);

		return comic;
	}

	/**
	 * Funcion que permite guardar en un list el total de comics que se han vendido
	 *
	 * @return
	 */
	public Comic[] verLibreriaVendidos() {

		listaTratamiento.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where estado = 'Vendido'";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;

		rs = obtenLibreria(sentenciaSql);
		listaTratamiento = listaDatos(rs);

		comic = new Comic[listaTratamiento.size()];
		comic = listaTratamiento.toArray(comic);

		return comic;
	}

	/**
	 * Funcion que permite guardar en un list el total de comics que se han vendido
	 *
	 * @return
	 */
	public Comic[] verLibreriaEnVenta() {

		listaTratamiento.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where estado = 'En venta'";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;

		rs = obtenLibreria(sentenciaSql);
		listaTratamiento = listaDatos(rs);

		comic = new Comic[listaTratamiento.size()];
		comic = listaTratamiento.toArray(comic);

		return comic;
	}

	/**
	 * Funcion que permite guardar en un list el total de comics que se han vendido
	 *
	 * @return
	 */
	public Comic[] verLibreriaFirmados() {

		listaTratamiento.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where Firma <> ''";

		Comic comic[] = null;

		ResultSet rs;

		rs = obtenLibreria(sentenciaSql);
		listaTratamiento = listaDatos(rs);

		comic = new Comic[listaTratamiento.size()];
		comic = listaTratamiento.toArray(comic);

		return comic;
	}

	/**
	 * Funcion que permite guardar en un list el total de comics que tengan
	 * puntuacion
	 *
	 * @return
	 */
	public Comic[] verLibreriaPuntuacion() {

		listaTratamiento.clear();

		String sentenciaSql = "SELECT * from comicsbbdd where puntuacion <>''";

		Comic comic[] = null;

		reiniciarBBDD();

		ResultSet rs;

		rs = obtenLibreria(sentenciaSql);
		listaTratamiento = listaDatos(rs);

		comic = new Comic[listaTratamiento.size()];
		comic = listaTratamiento.toArray(comic);

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
		Connection conn = FuncionesConexionBBDD.conexion();

		filtroComics.clear();

		if (sql.length() != 0) {
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
		} else {
			comic = new Comic[filtroComics.size()];
		}
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
		Connection conn = FuncionesConexionBBDD.conexion();
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

		int datosRellenados = 0;

		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM comicsbbdd");

		if (comic.getID().length() != 0) {

			sql.append(connector).append("ID = " + comic.getID());
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getNombre().length() != 0) {

			sql.append(connector).append("nomComic like'%" + comic.getNombre() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getNumero().length() != 0) {
			sql.append(connector).append("numComic = " + comic.getNumero());
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getVariante().length() != 0) {
			sql.append(connector).append("nomVariante like'%" + comic.getVariante() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getFirma().length() != 0) {
			sql.append(connector).append("firma like'%" + comic.getFirma() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getEditorial().length() != 0) {
			sql.append(connector).append("nomEditorial like'%" + comic.getEditorial() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getFormato().length() != 0) {
			sql.append(connector).append("formato like'%" + comic.getFormato() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getProcedencia().length() != 0) {
			sql.append(connector).append("procedencia like'%" + comic.getProcedencia() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getFecha().length() != 0) {
			sql.append(connector).append("anioPubli like'%" + comic.getFecha() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getGuionista().length() != 0) {
			sql.append(connector).append("nomGuionista like'%" + comic.getGuionista() + "%'");
			connector = " AND ";
			datosRellenados++;
		}
		if (comic.getDibujante().length() != 0) {
			sql.append(connector).append("nomDibujante like'%" + comic.getDibujante() + "%'");
			connector = " AND ";
			datosRellenados++;
		}

		if (datosRellenados != 0) {
			return sql.toString();
		}

		return "";
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
							this.estado, this.puntuacion, "");

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
							this.puntuacion, "");

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
		listaComics.clear();
		listaCompleta.clear();
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

		rs = obtenLibreria(sentenciaSQL);

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
		conn = FuncionesConexionBBDD.conexion();
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
				}

			} catch (SQLException e) {
				nav.alertaException("No existe el " + identificador + " en la base de datos.");
			}
		}
		return false;
	}

	public void saveImageFromDataBase() {
		String sentenciaSQL = "SELECT * FROM comicsbbdd";
		conn = FuncionesConexionBBDD.conexion();
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sentenciaSQL);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String nombreImagen = rs.getString(1);
				Blob imagenBlob = rs.getBlob(13);
				File directorio = new File("imagenes de la base de datos");
				directorio.mkdir();
				FileOutputStream fileops = new FileOutputStream(
						directorio.getAbsoluteFile().toString() + "/" + nombreImagen + ".jpg");
				fileops.write(imagenBlob.getBytes(1, (int) imagenBlob.length()));
				fileops.close();

			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param id
	 * @param sentenciaSQL
	 */
	public boolean modificarDatos(String id, String sentenciaSQL) {
		PreparedStatement stmt;
		FuncionesBBDD bd = new FuncionesBBDD();
		Connection conn = FuncionesConexionBBDD.conexion();
		try {
			if(id.length() != 0)
			{
				stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE); // Permite leer y ejecutar la sentencia de MySql

				stmt.setString(1, id);
				if(stmt.executeUpdate() == 1)
				{
					bd.reloadID();
					return true;
				}
			}

			return false;
		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		}
		return false;
	}

	/**
	 * Funcion que permite cambiar de estado el comic a "Vendido" y hace que no se
	 * muestre en la bbdd
	 */
	public void venderComicBBDD(String id) {
		String sentenciaSQL;

		sentenciaSQL = "UPDATE comicsbbdd set estado = 'Vendido' where ID = ?";

		modificarDatos(id, sentenciaSQL);
	}

	/**
	 *
	 */
	public void eliminarComicBBDD(String id) {
		String sentenciaSQL;

		sentenciaSQL = "DELETE from comicsbbdd where ID = ?";

		modificarDatos(id, sentenciaSQL);
	}
	
	/**
	 * Devuelve un objeto ResultSet para realizar una sentencia en la bbdd
	 *
	 * @param sentenciaSQL
	 * @return
	 */
	public ResultSet obtenLibreria(String sentenciaSQL) {
		conn = FuncionesConexionBBDD.conexion();
		try {
			// Realizamos la consulta SQL
			PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			ResultSet rs = stmt.executeQuery();
			if (!rs.first()) {
				return null;
			}

			// Todo bien, devolvemos el cliente
			return rs;

		} catch (NullPointerException ex) {
			ex.printStackTrace();
			nav.alertaException(ex.toString());
		} catch (SQLException ex) {
			ex.printStackTrace();
			nav.alertaException(ex.toString());
		}
		return null;
	}
	
	/**
	 * Funcion que modifica 1 comic de la base de datos con los parametros que
	 * introduzcamos en los campos.
	 */
	public void insertarDatos(String datos[]) {

		introducirDatos = new IntroducirDatosController();
		String sentenciaSQL = "insert into comicsbbdd(nomComic,numComic,nomVariante,firma,nomEditorial,formato,procedencia,anioPubli,nomGuionista,nomDibujante,puntuacion,portada,estado) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

		if (nav.alertaInsertar()) { // Llamada a alerta de modificacion

			subidaBBDD(sentenciaSQL,datos); // Llamada a funcion que permite comprobar el cambio realizado en el comic

		} else { // Si se cancela el borra del comic, saltara el siguiente mensaje.
		}
	}

	/**
	 *
	 */
	public void subidaBBDD(String sentenciaSQL, String datos[]) {

		introducirDatos = new IntroducirDatosController();
		
		InputStream portada = introducirDatos.direccionImagen(datos[10]);
		
		FuncionesBBDD bd = new FuncionesBBDD();
		conn = FuncionesConexionBBDD.conexion();

		try {
			PreparedStatement statement = conn.prepareStatement(sentenciaSQL);

			statement.setString(1, datos[0]);
			statement.setString(2, datos[1]);
			statement.setString(3, datos[2]);
			statement.setString(4, datos[3]);
			statement.setString(5, datos[4]);
			statement.setString(6, datos[5]);
			statement.setString(7, datos[6]);
			statement.setString(8, datos[7]);
			statement.setString(9, datos[8]);
			statement.setString(10, datos[9]);
			statement.setString(11, "");

			if (datos[10].length() != 0) {
				statement.setBinaryStream(12, portada);
			} else {
				statement.setBinaryStream(12, portada);
			}
			statement.setString(13, datos[11]);

			if (statement.executeUpdate() == 1) { // Si el resultado del executeUpdate es 1, mostrara el mensaje
				// correcto.

//				Comic comic = new Comic("", datos[0], datos[1], datos[2], datos[3], datos[4], datos[5], datos[6],
//						datos[7], datos[8], datos[9], datos[11], "Sin puntuar", "");
				statement.close();

			} else { 

			}

		} catch (SQLException ex) {
			nav.alertaException(ex.toString());
		} finally {
			try {
				portada.close();
			} catch (IOException ex) {
				nav.alertaException(ex.toString());
			}
		}
		introducirDatos.deleteImage(datos[10]);
		bd.reloadID();
	}
}
