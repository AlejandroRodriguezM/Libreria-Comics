package funcionesManagment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Controladores.ImagenAmpliadaController;
import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.DBUtilidades;
import dbmanager.DBUtilidades.TipoBusqueda;
import dbmanager.ListasComicsDAO;
import dbmanager.SelectManager;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesTableView;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

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

		FuncionesTableView.nombreColumnas();
		Utilidades.comprobacionListaComics();
		getReferenciaVentana().getImagenComic().setOpacity(1);
		Comic newSelection = getReferenciaVentana().getTablaBBDD().getSelectionModel().getSelectedItem();

		Scene scene = getReferenciaVentana().getTablaBBDD().getScene();
		@SuppressWarnings("unchecked")
		final List<Node>[] elementos = new ArrayList[1];
		elementos[0] = new ArrayList<>();

		if (!esPrincipal) {
			elementos[0] = AccionControlUI.modificarInterfazAccion(AccionFuncionesComunes.getTipoAccion());
		}

		if (scene != null) {
			scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				getReferenciaVentana().getImagenComic().setVisible(true);
				if (!getReferenciaVentana().getTablaBBDD().isHover()) {
					if (esPrincipal) {
						getReferenciaVentana().getTablaBBDD().getSelectionModel().clearSelection();
					} else {

						if (!"aniadir".equals(AccionFuncionesComunes.TIPO_ACCION)) {
							getReferenciaVentana().getTablaBBDD().getSelectionModel().clearSelection();
						}

						if ("modificar".equals(AccionFuncionesComunes.TIPO_ACCION)) {
							AccionControlUI.mostrarOpcion(AccionFuncionesComunes.TIPO_ACCION);

							Utilidades.cambiarVisibilidad(elementos[0], true);

						}

						if (!getReferenciaVentana().getIdComicTratarTextField().getText().isEmpty()) {
							Utilidades.cambiarVisibilidad(elementos[0], false);
						}
						
						// Borrar cualquier mensaje de error presente
						AccionFuncionesComunes.borrarErrores();
						AccionControlUI.validarCamposClave(true);
					}
				}
			});
		}
		

		// Verificar si idRow es nulo antes de intentar acceder a sus métodos
		if (newSelection != null) {
			String idComic = newSelection.getIdComic();
			mostrarComic(idComic, esPrincipal);
			Utilidades.cambiarVisibilidad(elementos[0], false);
		}

		getReferenciaVentana().getImagenComic().setOnMouseClicked(event -> {
			Comic comic = newSelection;
			ImagenAmpliadaController.setComicCache(comic);
			Ventanas.verVentanaImagen();
			getReferenciaVentana().getImagenComic().setVisible(false);
			AccionControlUI.limpiarAutorellenos(esPrincipal);
		});

	}

	public static void actualizarRefrenciaClick(AccionReferencias referenciaFXML) {
		Scene scene = getReferenciaVentana().getTablaBBDD().getScene();

		if (scene != null) {
			scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				setReferenciaVentana(referenciaFXML);
			});
		}
	}

	public static void mostrarComic(String idComic, boolean esPrincipal) {

		Comic comicTemp = null;
		AlarmaList.detenerAnimacion();
		String mensaje = "";

		if (!ListasComicsDAO.comicsImportados.isEmpty() && !esPrincipal) {
			comicTemp = ListasComicsDAO.buscarComicPorID(ListasComicsDAO.comicsImportados, idComic);

		} else if (ComicManagerDAO.comprobarIdentificadorComic(idComic)) {
			comicTemp = ComicManagerDAO.comicDatos(idComic);
		}

		if (idComic == null || idComic.isEmpty() || comicTemp == null) {
			AccionControlUI.limpiarAutorellenos(esPrincipal);
			return;
		}
		referenciaVentana.getImagenComic().setOpacity(1);
		if (!esPrincipal) {
			accionRellenoDatos.setAtributosDesdeTabla(comicTemp);
			AccionControlUI.validarCamposClave(false);

			Utilidades.setDatePickerValue(getReferenciaVentana().getDataPickFechaP(), comicTemp.getFechaGradeo());
			
			if (AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {
				AccionControlUI.mostrarOpcion(AccionFuncionesComunes.TIPO_ACCION);
				getReferenciaVentana().getIdComicTratarTextField().setText(comicTemp.getIdComic());
			}
		} else {
			Utilidades.cargarImagenAsync(comicTemp.getDireccionImagenComic(), getReferenciaVentana().getImagenComic());
		}

		getReferenciaVentana().getProntInfoTextArea().setOpacity(1);

		if (!ListasComicsDAO.comicsImportados.isEmpty() && ComicManagerDAO.comprobarIdentificadorComic(idComic)) {
			mensaje = ComicManagerDAO.comicDatos(idComic).toString().replace("[", "").replace("]", "");
		} else {
			mensaje = comicTemp.toString().replace("[", "").replace("]", "");
		}
		getReferenciaVentana().getProntInfoTextArea().clear();
		getReferenciaVentana().getProntInfoTextArea().setText(mensaje);

	}

	public static void verBasedeDatos(boolean completo, boolean esAccion, Comic comic) {

		ListasComicsDAO.reiniciarListaComics();
		getReferenciaVentana().getTablaBBDD().refresh();
		getReferenciaVentana().getProntInfoTextArea().setOpacity(0);
		getReferenciaVentana().getImagenComic().setVisible(false);
		getReferenciaVentana().getImagenComic().setImage(null);
		getReferenciaVentana().getProntInfoTextArea().setText(null);
		getReferenciaVentana().getProntInfoTextArea().clear();

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
					getReferenciaVentana().getBusquedaGeneralTextField().setText("");
				}

			}
		} else {
			String mensaje = "ERROR. No hay datos en la base de datos";

			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
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
			busquedaGeneralTextField = getReferenciaVentana().getBusquedaGeneralTextField().getText();
		}

		List<Comic> listComic = FXCollections
				.observableArrayList(SelectManager.busquedaParametro(datos, busquedaGeneralTextField));

		if (!listComic.isEmpty()) {
			getReferenciaVentana().getProntInfoTextArea().setOpacity(1);
			getReferenciaVentana().getProntInfoTextArea().setStyle("-fx-text-fill: black;"); // Reset the text color to
																								// black
			getReferenciaVentana().getProntInfoTextArea()
					.setText("El número de cómics donde aparece la búsqueda es: " + listComic.size() + "\n \n \n");
		} else if (listComic.isEmpty() && esAccion) {
			getReferenciaVentana().getProntInfoTextArea().setOpacity(1);
			// Show error message in red when no search fields are specified
			getReferenciaVentana().getProntInfoTextArea().setStyle("-fx-text-fill: red;");
			getReferenciaVentana().getProntInfoTextArea().setText("Error. No existen con dichos parametros.");
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
