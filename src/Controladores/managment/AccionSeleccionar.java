package Controladores.managment;

import java.sql.SQLException;
import java.util.List;

import alarmas.AlarmaList;
import comicManagement.Comic;
import controlUI.AccionControlUI;
import controlUI.FuncionesTableView;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.DBUtilidades.TipoBusqueda;
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import funciones_auxiliares.Utilidades;
import javafx.collections.FXCollections;

public class AccionSeleccionar {

	public static AccionReferencias referenciaVentana = new AccionReferencias();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

	/**
	 * Método para seleccionar y mostrar detalles de un cómic en la interfaz
	 * gráfica. Si la lista de cómics importados no está vacía, utiliza la
	 * información de la lista; de lo contrario, consulta la base de datos para
	 * obtener la información del cómic.
	 * 
	 * @throws SQLException Si se produce un error al acceder a la base de datos.
	 */
	public static void seleccionarComics(boolean esPrincipal) {
		FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD()); // funcion
		Utilidades.comprobacionListaComics();

		Comic newSelection = referenciaVentana.getTablaBBDD().getSelectionModel().getSelectedItem();

		// Verificar si idRow es nulo antes de intentar acceder a sus métodos
		if (newSelection != null) {
			String id_comic = newSelection.getID();
			
			mostrarComic(id_comic, esPrincipal);
		}
	}

	public static void mostrarComic(String idComic, boolean esPrincipal) {

		Comic comicTemp = null;
		AlarmaList.detenerAnimacion();
		String mensaje = "";
		
		if (!ListaComicsDAO.comicsImportados.isEmpty() && !esPrincipal) {
			comicTemp = ListaComicsDAO.buscarComicPorID(ListaComicsDAO.comicsImportados, idComic);
		} else {
			comicTemp = ComicManagerDAO.comicDatos(idComic);
		}

		if (idComic == null || idComic.isEmpty() || comicTemp == null) {
			AccionControlUI.limpiarAutorellenos(esPrincipal);
			AccionControlUI.borrarDatosGraficos();
			return;
		}

		Comic.limpiarCamposComic(comicTemp);

		if (!esPrincipal) {
			accionRellenoDatos.setAtributosDesdeTabla(comicTemp);
			AccionControlUI.validarCamposClave(false);

			if (AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {
				AccionControlUI.mostrarOpcion(AccionFuncionesComunes.TIPO_ACCION);
				referenciaVentana.getIdComicTratar_mod().setText(comicTemp.getID());
			}
		} else {
			Utilidades.cargarImagenAsync(comicTemp.getImagen(), referenciaVentana.getImagencomic());
		}

		referenciaVentana.getProntInfo().setOpacity(1);

		if (!ListaComicsDAO.comicsImportados.isEmpty() && ComicManagerDAO.comprobarIdentificadorComic(idComic)) {
			mensaje = ComicManagerDAO.comicDatos(idComic).toString().replace("[", "").replace("]", "");
		} else {
			mensaje = comicTemp.toString().replace("[", "").replace("]", "");
		}
		referenciaVentana.getProntInfo().clear();
		referenciaVentana.getProntInfo().setText(mensaje);

	}

	public static void verBasedeDatos(boolean completo, boolean esAccion, Comic comic) {		

		ListaComicsDAO.reiniciarListaComics();
		FuncionesTableView.modificarColumnas();
		referenciaVentana.getTablaBBDD().refresh();
		referenciaVentana.getProntInfo().setText(null);
		referenciaVentana.getProntInfo().clear();
//		referenciaVentana.getProntInfo().setOpacity(1);
//		referenciaVentana.getImagencomic().setImage(null);

		FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD());
		FuncionesTableView.actualizarBusquedaRaw(referenciaVentana.getTablaBBDD());

		if (ComicManagerDAO.countRows() > 0) {
			if (completo) {
				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(TipoBusqueda.COMPLETA);

				List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
				FuncionesTableView.tablaBBDD(listaComics, referenciaVentana.getTablaBBDD());

			} else {

				List<Comic> listaParametro = listaPorParametro(comic, esAccion);

				FuncionesTableView.tablaBBDD(listaParametro, referenciaVentana.getTablaBBDD());

				if (!esAccion) {
					if (!listaParametro.isEmpty()) {
						referenciaVentana.getBotonImprimir().setVisible(true);
						referenciaVentana.getBotonGuardarResultado().setVisible(true);
					} else {
						referenciaVentana.getBotonImprimir().setVisible(false);
						referenciaVentana.getBotonGuardarResultado().setVisible(false);
					}
					referenciaVentana.getBusquedaGeneral().setText("");
				}

			}
		} else {
			String mensaje = "ERROR. No hay datos en la base de datos";

			AlarmaList.mostrarMensajePront(mensaje, false, referenciaVentana.getProntInfo());
		}
	}

	/**
	 * Funcion que comprueba segun los datos escritos en los textArea, que comic
	 * estas buscando.
	 * 
	 * @throws SQLException
	 */
	public static List<Comic> listaPorParametro(Comic datos, boolean esAccion) {

		if (!ConectManager.conexionActiva()) {
			return null;
		}
		String busquedaGeneralTextField = "";

		if (!esAccion) {
			busquedaGeneralTextField = referenciaVentana.getBusquedaGeneral().getText();
		}

		List<Comic> listComic = FXCollections
				.observableArrayList(SelectManager.busquedaParametro(datos, busquedaGeneralTextField));

		if (listComic.size() > 0) {
			referenciaVentana.getProntInfo().setOpacity(1);
			referenciaVentana.getProntInfo().setText(FuncionesTableView.resultadoBusquedaPront(datos).getText());
		} else {
			referenciaVentana.getProntInfo().setOpacity(1);
			// Show error message in red when no search fields are specified
			referenciaVentana.getProntInfo().setStyle("-fx-text-fill: red;");
			referenciaVentana.getProntInfo()
					.setText("Error No existe comic con los datos: " + datos.toString() + "\n \n \n");
		}

		return listComic;
	}

}
