package funcionesManagment;

import java.sql.SQLException;
import java.util.List;

import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.DBUtilidades;
import dbmanager.DBUtilidades.TipoBusqueda;
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesTableView;
import funciones_auxiliares.Utilidades;
import javafx.collections.FXCollections;

public class AccionSeleccionar {

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

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

		FuncionesTableView.nombreColumnas(); // funcion
		Utilidades.comprobacionListaComics();

		Comic newSelection = getReferenciaVentana().getTablaBBDD().getSelectionModel().getSelectedItem();

		// Verificar si idRow es nulo antes de intentar acceder a sus métodos
		if (newSelection != null) {
			String idComic = newSelection.getid();

			mostrarComic(idComic, esPrincipal);
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
			return;
		}

		Comic.limpiarCamposComic(comicTemp);

		if (!esPrincipal) {
			accionRellenoDatos.setAtributosDesdeTabla(comicTemp);
			AccionControlUI.validarCamposClave(false);

			if (AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {
				AccionControlUI.mostrarOpcion(AccionFuncionesComunes.TIPO_ACCION);
				getReferenciaVentana().getIdComicTratar().setText(comicTemp.getid());
			}
		} else {
			Utilidades.cargarImagenAsync(comicTemp.getImagen(), getReferenciaVentana().getImagencomic());
		}

		getReferenciaVentana().getProntInfo().setOpacity(1);

		if (!ListaComicsDAO.comicsImportados.isEmpty() && ComicManagerDAO.comprobarIdentificadorComic(idComic)) {
			mensaje = ComicManagerDAO.comicDatos(idComic).toString().replace("[", "").replace("]", "");
		} else {
			mensaje = comicTemp.toString().replace("[", "").replace("]", "");
		}
		getReferenciaVentana().getProntInfo().clear();
		getReferenciaVentana().getProntInfo().setText(mensaje);

	}

	public static void verBasedeDatos(boolean completo, boolean esAccion, Comic comic) {

		ListaComicsDAO.reiniciarListaComics();
		getReferenciaVentana().getTablaBBDD().refresh();
		getReferenciaVentana().getProntInfo().setOpacity(0);
		getReferenciaVentana().getImagencomic().setVisible(false);
		getReferenciaVentana().getImagencomic().setImage(null);
		getReferenciaVentana().getProntInfo().setText(null);
		getReferenciaVentana().getProntInfo().clear();

		FuncionesTableView.nombreColumnas();
		FuncionesTableView.actualizarBusquedaRaw();

		if (ComicManagerDAO.countRows() > 0) {
			if (completo) {

				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(TipoBusqueda.COMPLETA);

				List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);

				FuncionesTableView.tablaBBDD(listaComics);

			} else {

				List<Comic> listaParametro = listaPorParametro(comic, esAccion);

				FuncionesTableView.tablaBBDD(listaParametro);

				if (!esAccion) {
					if (!listaParametro.isEmpty()) {
						getReferenciaVentana().getBotonImprimir().setVisible(true);
						getReferenciaVentana().getBotonGuardarResultado().setVisible(true);
					} else {
						getReferenciaVentana().getBotonImprimir().setVisible(false);
						getReferenciaVentana().getBotonGuardarResultado().setVisible(false);
					}
					getReferenciaVentana().getBusquedaGeneral().setText("");
				}

			}
		} else {
			String mensaje = "ERROR. No hay datos en la base de datos";

			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfo());
		}
	}

	/**
	 * Funcion que comprueba segun los datos escritos en los textArea, que comic
	 * estas buscando.
	 * 
	 * @throws SQLException
	 */
	public static List<Comic> listaPorParametro(Comic datos, boolean esAccion) {
		String busquedaGeneralTextField = "";

		if (!esAccion) {
			busquedaGeneralTextField = getReferenciaVentana().getBusquedaGeneral().getText();
		}

		List<Comic> listComic = FXCollections
				.observableArrayList(SelectManager.busquedaParametro(datos, busquedaGeneralTextField));

		if (!listComic.isEmpty()) {
			getReferenciaVentana().getProntInfo().setOpacity(1);
			getReferenciaVentana().getProntInfo().setStyle("-fx-text-fill: black;"); // Reset the text color to black
			getReferenciaVentana().getProntInfo()
					.setText("El número de cómics donde aparece la búsqueda es: " + listComic.size() + "\n \n \n");
		} else if (listComic.isEmpty() && esAccion) {
			getReferenciaVentana().getProntInfo().setOpacity(1);
			// Show error message in red when no search fields are specified
			getReferenciaVentana().getProntInfo().setStyle("-fx-text-fill: red;");
			getReferenciaVentana().getProntInfo().setText("Error. No existen con dichos parametros.");
		}

		return listComic;
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionSeleccionar.referenciaVentana = referenciaVentana;
	}

}
