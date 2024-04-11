package Controladores.managment;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import alarmas.AlarmaList;
import comicManagement.Comic;
import controlUI.AccionControlUI;
import controlUI.FuncionesComboBox;
import controlUI.FuncionesManejoFront;
import controlUI.FuncionesTableView;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.ListaComicsDAO;
import dbmanager.SelectManager;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AccionModificar {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();

	public static AccionReferencias referenciaVentana = new AccionReferencias();

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
		List<Node> elementos = Arrays.asList(referenciaVentana.getNombreKeyIssue(),
				referenciaVentana.getUrlReferencia(), referenciaVentana.getLabel_id_mod(),
				referenciaVentana.getIdComicTratar_mod(), referenciaVentana.getPrecioComic(),
				referenciaVentana.getDireccionImagen(), referenciaVentana.getLabel_portada(),
				referenciaVentana.getLabel_precio(), referenciaVentana.getLabel_key(),
				referenciaVentana.getLabel_referencia(), referenciaVentana.getBotonModificarComic(),
				referenciaVentana.getCodigoComicTratar(), referenciaVentana.getLabel_codigo_comic());

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
			String id_comic = referenciaVentana.getIdComicTratar_mod().getText();
			if (accionFuncionesComunes.comprobarExistenciaComic(id_comic)) {
				if (nav.alertaAccionGeneral()) {
					String mensaje = "";
					if (esAgregar) {
						ComicManagerDAO.actualizarOpinion(id_comic,
								FuncionesComboBox.puntuacionCombobox(referenciaVentana.getPuntuacionMenu()));
						mensaje = "Has agregado una puntuacion correctamente";
					} else {
						ComicManagerDAO.actualizarOpinion(id_comic, "0");
						mensaje = "Has borrado correctamente la puntuacion";
					}

					AlarmaList.mostrarMensajePront(mensaje, true, referenciaVentana.getProntInfo());

					List<ComboBox<String>> comboboxes = referenciaVentana.getComboboxes();

					funcionesCombo.rellenarComboBox(comboboxes);
					referenciaVentana.getTablaBBDD().refresh();
					FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD());
					FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados, referenciaVentana.getTablaBBDD());
				} else {
					String mensaje = "Accion cancelada";
					AlarmaList.mostrarMensajePront(mensaje, false, referenciaVentana.getProntInfo());
				}

			}
		}
	}

	public static void venderComic() throws SQLException {
		String id_comic = referenciaVentana.getIdComicTratar_mod().getText();
		referenciaVentana.getIdComicTratar_mod().setStyle("");
		Comic comicActualizar = ComicManagerDAO.comicDatos(id_comic);
		if (accionFuncionesComunes.comprobarExistenciaComic(id_comic)) {
			if (nav.alertaAccionGeneral()) {
				ComicManagerDAO.actualizarComicBBDD(comicActualizar, "vender");
				ListaComicsDAO.reiniciarListaComics();
				String mensaje = ". Has puesto a la venta el comic";
				AlarmaList.mostrarMensajePront(mensaje, false, referenciaVentana.getProntInfo());

				List<ComboBox<String>> comboboxes = referenciaVentana.getComboboxes();

				funcionesCombo.rellenarComboBox(comboboxes);
				referenciaVentana.getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD());
				FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados, referenciaVentana.getTablaBBDD());

			} else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, referenciaVentana.getProntInfo());
			}

		}
	}

	public static void modificarComic() throws Exception {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		String id_comic = referenciaVentana.getIdComicTratar_mod().getText();
		referenciaVentana.getIdComicTratar_mod().setStyle("");

		if (accionFuncionesComunes.comprobarExistenciaComic(id_comic)) {
			if (nav.alertaAccionGeneral()) {

				Utilidades.convertirNombresCarpetas(AccionFuncionesComunes.SOURCE_PATH);

				Comic comicModificado = AccionControlUI.comicModificado();

				accionFuncionesComunes.procesarComic(comicModificado, true);

				ListaComicsDAO.listasAutoCompletado();
				referenciaVentana.getTablaBBDD().refresh();

				List<ComboBox<String>> comboboxes = referenciaVentana.getComboboxes();
				referenciaVentana.getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD());
				FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados, referenciaVentana.getTablaBBDD());
				if (comboboxes != null) {
					funcionesCombo.rellenarComboBox(comboboxes);
				}
			}

			else {
				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

				List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);

				ComicManagerDAO.borrarComic(id_comic);
				ListaComicsDAO.reiniciarListaComics();
				ListaComicsDAO.listasAutoCompletado();
				FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD()); // Llamada a funcion
				FuncionesTableView.actualizarBusquedaRaw(referenciaVentana.getTablaBBDD());
				FuncionesTableView.tablaBBDD(listaComics, referenciaVentana.getTablaBBDD());

				List<ComboBox<String>> comboboxes = referenciaVentana.getComboboxes();

				funcionesCombo.rellenarComboBox(comboboxes);
			}
		}
	}

	public static void actualizarComicLista() {

		if (!accionRellenoDatos.camposComicSonValidos()) {
			String mensaje = "Error. Debes de introducir los datos correctos";
			AlarmaList.mostrarMensajePront(mensaje, false, referenciaVentana.getProntInfo());
			return; // Agregar return para salir del método en este punto
		}
		Comic datos = AccionControlUI.camposComic(referenciaVentana.getListaTextFields(), true);

		if (ListaComicsDAO.comicsImportados.size() > 0) {

			if (datos.getID() == null || datos.getID().isEmpty()) {
				datos = ListaComicsDAO.buscarComicPorID(ListaComicsDAO.comicsImportados, datos.getID());
			}

			// Si hay elementos en la lista
			for (Comic c : ListaComicsDAO.comicsImportados) {
				if (c.getID().equals(datos.getID())) {
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
		referenciaVentana.getBotonCancelarSubida().setVisible(false); // Oculta el botón de cancelar

		Comic.limpiarCamposComic(datos);
		AccionControlUI.limpiarAutorellenos(false);

		FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD());
		FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados, referenciaVentana.getTablaBBDD());
	}

	public void mostrarElementosPuntuar(List<Node> elementosAMostrarYHabilitar) {
		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getBotonBorrarOpinion(),
				referenciaVentana.getPuntuacionMenu(), referenciaVentana.getLabelPuntuacion(),
				referenciaVentana.getBotonAgregarPuntuacion(), referenciaVentana.getLabel_id_mod(),
				referenciaVentana.getTablaBBDD(), referenciaVentana.getBotonbbdd(), referenciaVentana.getRootVBox(),
				referenciaVentana.getBotonParametroComic(), referenciaVentana.getIdComicTratar_mod()));
		referenciaVentana.getRootVBox().toFront();
	}

	public void mostrarElementosModificar(List<Node> elementosAMostrarYHabilitar) {
		elementosAMostrarYHabilitar
				.addAll(Arrays.asList(referenciaVentana.getDibujanteComic(), referenciaVentana.getEditorialComic(),
						referenciaVentana.getEstadoComic(), referenciaVentana.getFechaComic(),
						referenciaVentana.getFirmaComic(), referenciaVentana.getFormatoComic(),
						referenciaVentana.getGuionistaComic(), referenciaVentana.getNombreKeyIssue(),
						referenciaVentana.getNumeroCajaComic(), referenciaVentana.getProcedenciaComic(),
						referenciaVentana.getUrlReferencia(), referenciaVentana.getBotonModificarComic(),
						referenciaVentana.getPrecioComic(), referenciaVentana.getDireccionImagen(),
						referenciaVentana.getTablaBBDD(), referenciaVentana.getLabel_portada(),
						referenciaVentana.getLabel_precio(), referenciaVentana.getLabel_caja(),
						referenciaVentana.getLabel_dibujante(), referenciaVentana.getLabel_editorial(),
						referenciaVentana.getLabel_estado(), referenciaVentana.getLabel_fecha(),
						referenciaVentana.getLabel_firma(), referenciaVentana.getLabel_formato(),
						referenciaVentana.getLabel_guionista(), referenciaVentana.getLabel_key(),
						referenciaVentana.getLabel_procedencia(), referenciaVentana.getLabel_referencia(),
						referenciaVentana.getBotonbbdd(), referenciaVentana.getIdComicTratar_mod(),
						referenciaVentana.getLabel_id_mod(), referenciaVentana.getBotonParametroComic(),
						referenciaVentana.getCodigoComicTratar(), referenciaVentana.getLabel_codigo_comic(),
						referenciaVentana.getRootVBox(), referenciaVentana.getBotonSubidaPortada()));
		referenciaVentana.getRootVBox().toFront();
	}

	public static void actualizarDatabase(String tipoUpdate, boolean actualizarFima, Stage ventanaOpciones) {

		String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
		List<Comic> listaComicsDatabase = SelectManager.verLibreria(sentenciaSQL, true);

		// Ordenar la lista según el ID convertido a número
		Collections.sort(listaComicsDatabase, new Comparator<Comic>() {
			@Override
			public int compare(Comic comic1, Comic comic2) {
				int id1 = Integer.parseInt(comic1.getID());
				int id2 = Integer.parseInt(comic2.getID());
				return Integer.compare(id1, id2);
			}
		});

		List<Stage> stageVentanas = FuncionesManejoFront.stageVentanas;

		// Assuming `stages` is a collection of stages you want to check against
		for (Stage stage : stageVentanas) {
			if (stage != ventanaOpciones) {
				stage.close(); // Close the stage if it's not the current state
			}
		}

		AccionFuncionesComunes.busquedaPorListaDatabase(listaComicsDatabase, tipoUpdate, actualizarFima);
		referenciaVentana.getTablaBBDD().refresh();
		FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD());
		FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados, referenciaVentana.getTablaBBDD());
	}

}
