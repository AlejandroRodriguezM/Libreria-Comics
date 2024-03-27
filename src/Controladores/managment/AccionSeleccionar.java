package Controladores.managment;

import java.sql.SQLException;

import Controladores.VentanaAccionController;
import Funcionamiento.FuncionesTableView;
import Funcionamiento.Utilidades;
import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ListaComicsDAO;

public class AccionSeleccionar {

	public static AccionReferencias referenciaVentana = new AccionReferencias();

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();

	private static VentanaAccionController accionController = new VentanaAccionController();

	private static AccionAniadir accionAniadir = new AccionAniadir();

	private static AccionEliminar accionEliminar = new AccionEliminar();

	private static AccionModificar accionModificar = new AccionModificar();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

	/**
	 * Método para seleccionar y mostrar detalles de un cómic en la interfaz
	 * gráfica. Si la lista de cómics importados no está vacía, utiliza la
	 * información de la lista; de lo contrario, consulta la base de datos para
	 * obtener la información del cómic.
	 * 
	 * @throws SQLException Si se produce un error al acceder a la base de datos.
	 */
	public void seleccionarComics() {
		FuncionesTableView.nombreColumnas(VentanaAccionController.columnList, referenciaVentana.getTablaBBDD()); // Llamada
																													// a
																													// funcion
		Utilidades.comprobacionListaComics();

		Comic newSelection = referenciaVentana.getTablaBBDD().getSelectionModel().getSelectedItem();

		// Verificar si idRow es nulo antes de intentar acceder a sus métodos
		if (newSelection != null) {
			String id_comic = newSelection.getID();

			mostrarComic(id_comic);
		}
	}

	public void mostrarComic(String idComic) {
		if (idComic == null || idComic.isEmpty()) {
			accionRellenoDatos.limpiarAutorellenos();
			accionRellenoDatos.borrarDatosGraficos();
			return;
		}

		Comic comicTemp = null;
		String mensaje = "";
		if (!ListaComicsDAO.comicsImportados.isEmpty()) {
			comicTemp = ListaComicsDAO.buscarComicPorID(ListaComicsDAO.comicsImportados, idComic);
		} else {
			comicTemp = ComicManagerDAO.comicDatos(idComic);
		}

		if (comicTemp == null) {
			accionRellenoDatos.limpiarAutorellenos();
			accionRellenoDatos.borrarDatosGraficos();
			AlarmaList.mostrarMensajePront("No existe comic con dicho ID", false, referenciaVentana.getProntInfo());
			return;
		}

		Comic.limpiarCamposComic(comicTemp);
		accionRellenoDatos.setAtributosDesdeTabla(comicTemp);
		accionRellenoDatos.validarCamposComic(false);
		referenciaVentana.getProntInfo().setOpacity(1);

		if (!ListaComicsDAO.comicsImportados.isEmpty() && ComicManagerDAO.comprobarIdentificadorComic(idComic)) {
			mensaje = ComicManagerDAO.comicDatos(idComic).toString().replace("[", "").replace("]", "");
		} else {
			mensaje = comicTemp.toString().replace("[", "").replace("]", "");
		}
		referenciaVentana.getProntInfo().setText(mensaje);

		if (AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {
			accionRellenoDatos.mostrarOpcion(AccionFuncionesComunes.TIPO_ACCION);
			referenciaVentana.getIdComicTratar_mod().setText(comicTemp.getID());
		}
	}

}
