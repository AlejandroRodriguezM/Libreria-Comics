package funcionesManagment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesInterfaz.FuncionesTableView;
import funciones_auxiliares.Utilidades;
import funciones_auxiliares.Ventanas;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AccionModificar {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static AccionReferencias referenciaVentanaPrincipal = getReferenciaVentanaPrincipal();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

	/**
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public void ocultarCamposMod() {
		List<Node> elementos = Arrays.asList(getReferenciaVentana().getNombreKeyIssue(),
				getReferenciaVentana().getUrlReferencia(), getReferenciaVentana().getLabel_id_mod(),
				getReferenciaVentana().getIdComicTratar(), getReferenciaVentana().getPrecioComic(),
				getReferenciaVentana().getDireccionImagen(), getReferenciaVentana().getLabel_portada(),
				getReferenciaVentana().getLabel_precio(), getReferenciaVentana().getLabel_key(),
				getReferenciaVentana().getLabel_referencia(), getReferenciaVentana().getBotonModificarComic(),
				getReferenciaVentana().getCodigoComicTratar(), getReferenciaVentana().getLabel_codigo_comic());

		Utilidades.cambiarVisibilidad(elementos, true);
	}

	/**
	 * Realiza la acción de puntuar un cómic, ya sea agregar una nueva puntuación o
	 * borrar la existente.
	 * 
	 * @param esAgregar Indica si la acción es agregar una nueva puntuación (true) o
	 *                  borrar la existente (false).
	 * @throws SQLException Excepción lanzada en caso de errores de acceso a la base
	 *                      de datos.
	 */
	public static void accionPuntuar(boolean esAgregar) {

		if (ConectManager.conexionActiva()) {
			String idComic = getReferenciaVentana().getIdComicTratar().getText();
			if (accionFuncionesComunes.comprobarExistenciaComic(idComic)) {
				if (nav.alertaAccionGeneral()) {
					String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
					List<Comic> listaComics;
					String mensaje = "";
					if (esAgregar) {
						ComicManagerDAO.actualizarOpinion(idComic,
								FuncionesComboBox.puntuacionCombobox(getReferenciaVentana().getPuntuacionMenu()));
						mensaje = "Has agregado una puntuacion correctamente";
					} else {
						ComicManagerDAO.actualizarOpinion(idComic, "0");
						mensaje = "Has borrado correctamente la puntuacion";
					}

					AlarmaList.mostrarMensajePront(mensaje, true, getReferenciaVentana().getProntInfo());

					listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
					getReferenciaVentana().getTablaBBDD().refresh();
					FuncionesTableView.nombreColumnas();
					FuncionesTableView.tablaBBDD(listaComics);

				} else {
					String mensaje = "Accion cancelada";
					AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfo());
				}

			}
		}
	}

	public static void venderComic() throws SQLException {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		String idComic = getReferenciaVentana().getIdComicTratar().getText();
		getReferenciaVentana().getIdComicTratar().setStyle("");
		Comic comicActualizar = ComicManagerDAO.comicDatos(idComic);
		if (accionFuncionesComunes.comprobarExistenciaComic(idComic)) {
			if (nav.alertaAccionGeneral()) {
				ComicManagerDAO.actualizarComicBBDD(comicActualizar, "vender");
				ListaComicsDAO.reiniciarListaComics();
				String mensaje = ". Has puesto a la venta el comic";
				AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfo());

				List<ComboBox<String>> comboboxes = getReferenciaVentana().getComboboxes();

				funcionesCombo.rellenarComboBox(comboboxes);
				getReferenciaVentana().getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados);

			} else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfo());
			}

		}
	}

	public static void modificarComic() throws Exception {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		String idComic = getReferenciaVentana().getIdComicTratar().getText();
		getReferenciaVentana().getIdComicTratar().setStyle("");

		if (accionFuncionesComunes.comprobarExistenciaComic(idComic)) {
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
			List<Comic> listaComics;
			if (nav.alertaAccionGeneral()) {

				Utilidades.convertirNombresCarpetas(AccionFuncionesComunes.SOURCE_PATH);

				Comic comicModificado = AccionControlUI.comicModificado();

				accionFuncionesComunes.procesarComic(comicModificado, true);

				ListaComicsDAO.listasAutoCompletado();

				listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
				getReferenciaVentana().getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(listaComics);
			}

			else {

				listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);

				ComicManagerDAO.borrarComic(idComic);
				ListaComicsDAO.reiniciarListaComics();
				ListaComicsDAO.listasAutoCompletado();
				FuncionesTableView.nombreColumnas(); // Llamada a funcion
				FuncionesTableView.actualizarBusquedaRaw();
				FuncionesTableView.tablaBBDD(listaComics);
			}
		}
	}

	public static void actualizarComicLista() {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		if (!accionRellenoDatos.camposComicSonValidos()) {
			String mensaje = "Error. Debes de introducir los datos correctos";
			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfo());
			return; // Agregar return para salir del método en este punto
		}

		List<String> controls = new ArrayList<>();

		for (Control control : referenciaVentana.getListaTextFields()) {
			if (control instanceof TextField) {
				controls.add(((TextField) control).getText()); // Add the Control object itself
			} else if (control instanceof ComboBox<?>) {
				Object selectedItem = ((ComboBox<?>) control).getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					controls.add(selectedItem.toString());
				} else {
					controls.add(""); // Add the Control object itself
				}
			}
		}
		Comic datos = AccionControlUI.camposComic(controls, true);

		if (!ListaComicsDAO.comicsImportados.isEmpty()) {

			if (datos.getid() == null || datos.getid().isEmpty()) {
				datos = ListaComicsDAO.buscarComicPorID(ListaComicsDAO.comicsImportados, datos.getid());
			}

			// Si hay elementos en la lista
			for (Comic c : ListaComicsDAO.comicsImportados) {
				if (c.getid().equals(datos.getid())) {
					// Si se encuentra un cómic con el mismo ID, reemplazarlo con los nuevos datos
					ListaComicsDAO.comicsImportados.set(ListaComicsDAO.comicsImportados.indexOf(c), datos);
					break; // Salir del bucle una vez que se actualice el cómic
				}
			}
		} else {
			String id = "A" + 0 + "" + (ListaComicsDAO.comicsImportados.size() + 1);
			datos.setID(id);
			ListaComicsDAO.comicsImportados.add(datos);
		}

		AccionFuncionesComunes.cambiarEstadoBotones(false);
		getReferenciaVentana().getBotonCancelarSubida().setVisible(false); // Oculta el botón de cancelar

		Comic.limpiarCamposComic(datos);
		AccionControlUI.limpiarAutorellenos(false);

		FuncionesTableView.nombreColumnas();
		FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados);
	}

	public void mostrarElementosPuntuar(List<Node> elementosAMostrarYHabilitar) {
		elementosAMostrarYHabilitar.addAll(Arrays.asList(getReferenciaVentana().getBotonBorrarOpinion(),
				getReferenciaVentana().getPuntuacionMenu(), getReferenciaVentana().getLabelPuntuacion(),
				getReferenciaVentana().getBotonAgregarPuntuacion(), getReferenciaVentana().getLabel_id_mod(),
				getReferenciaVentana().getTablaBBDD(), getReferenciaVentana().getBotonbbdd(),
				getReferenciaVentana().getRootVBox(), getReferenciaVentana().getBotonParametroComic(),
				getReferenciaVentana().getIdComicTratar()));
		getReferenciaVentana().getRootVBox().toFront();
	}

	public void mostrarElementosModificar(List<Node> elementosAMostrarYHabilitar) {
		elementosAMostrarYHabilitar.addAll(
				Arrays.asList(getReferenciaVentana().getDibujanteComic(), getReferenciaVentana().getEditorialComic(),
						getReferenciaVentana().getEstadoComic(), getReferenciaVentana().getFechaComic(),
						getReferenciaVentana().getFirmaComic(), getReferenciaVentana().getFormatoComic(),
						getReferenciaVentana().getGuionistaComic(), getReferenciaVentana().getNombreKeyIssue(),
						getReferenciaVentana().getGradeoComic(), getReferenciaVentana().getProcedenciaComic(),
						getReferenciaVentana().getUrlReferencia(), getReferenciaVentana().getBotonModificarComic(),
						getReferenciaVentana().getPrecioComic(), getReferenciaVentana().getDireccionImagen(),
						getReferenciaVentana().getTablaBBDD(), getReferenciaVentana().getLabel_portada(),
						getReferenciaVentana().getLabel_precio(), getReferenciaVentana().getLabel_gradeo(),
						getReferenciaVentana().getLabel_dibujante(), getReferenciaVentana().getLabel_editorial(),
						getReferenciaVentana().getLabel_estado(), getReferenciaVentana().getLabel_fecha(),
						getReferenciaVentana().getLabel_firma(), getReferenciaVentana().getLabel_formato(),
						getReferenciaVentana().getLabel_guionista(), getReferenciaVentana().getLabel_key(),
						getReferenciaVentana().getLabel_procedencia(), getReferenciaVentana().getLabel_referencia(),
						getReferenciaVentana().getBotonbbdd(), getReferenciaVentana().getIdComicTratar(),
						getReferenciaVentana().getLabel_id_mod(), getReferenciaVentana().getBotonParametroComic(),
						getReferenciaVentana().getCodigoComicTratar(), getReferenciaVentana().getLabel_codigo_comic(),
						getReferenciaVentana().getRootVBox(), getReferenciaVentana().getBotonSubidaPortada()));
		getReferenciaVentana().getRootVBox().toFront();
	}

	public static void actualizarDatabase(String tipoUpdate, boolean actualizarFima, Stage ventanaOpciones) {

		boolean estaBaseLlena = ListaComicsDAO.comprobarLista();

		if (!estaBaseLlena) {
			String cadenaCancelado = "La base de datos esta vacia";
			AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(), cadenaCancelado);
			return;
		}

		String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
		List<Comic> listaComicsDatabase = SelectManager.verLibreria(sentenciaSQL, true);

		Collections.sort(listaComicsDatabase, (comic1, comic2) -> {
			int id1 = Integer.parseInt(comic1.getid());
			int id2 = Integer.parseInt(comic2.getid());
			return Integer.compare(id1, id2);
		});

		List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();

		// Assuming `stages` is a collection of stages you want to check against
		for (Stage stage : stageVentanas) {
			if (stage != ventanaOpciones && !stage.getTitle().equalsIgnoreCase("Menu principal")) {
				stage.close(); // Close the stage if it's not the current state
			}
		}

		AccionFuncionesComunes.busquedaPorListaDatabase(listaComicsDatabase, tipoUpdate, actualizarFima);

		if (getReferenciaVentana().getTablaBBDD() != null) {
			getReferenciaVentana().getTablaBBDD().refresh();
			FuncionesTableView.nombreColumnas();
			FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados);
		}

	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static AccionReferencias getReferenciaVentanaPrincipal() {
		return referenciaVentanaPrincipal;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionModificar.referenciaVentana = referenciaVentana;
	}

	public static void setReferenciaVentanaPrincipal(AccionReferencias referenciaVentana) {
		AccionModificar.referenciaVentanaPrincipal = referenciaVentana;
	}

}
