package Controladores.managment;

import java.util.Arrays;
import java.util.List;

import Funcionamiento.Ventanas;
import alarmas.AlarmaList;
import comicManagement.Comic;
import controlUI.AccionControlUI;
import controlUI.FuncionesComboBox;
import controlUI.FuncionesTableView;
import dbmanager.ComicManagerDAO;
import dbmanager.DBUtilidades;
import dbmanager.ListaComicsDAO;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class AccionEliminar {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();
	public static AccionReferencias referenciaVentana = new AccionReferencias();
	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	public void mostrarElementosEliminar(List<Node> elementosAMostrarYHabilitar) {
		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getLabel_id_mod(),
				referenciaVentana.getBotonVender(), referenciaVentana.getBotonEliminar(),
				referenciaVentana.getTablaBBDD(), referenciaVentana.getBotonbbdd(), referenciaVentana.getRootVBox(),
				referenciaVentana.getBotonParametroComic(), referenciaVentana.getIdComicTratar_mod()));
		referenciaVentana.getRootVBox().toFront();
	}

	public static void eliminarComic() {
		String id_comic = referenciaVentana.getIdComicTratar_mod().getText();
		referenciaVentana.getIdComicTratar_mod().setStyle("");
		if (accionFuncionesComunes.comprobarExistenciaComic(id_comic)) {
			if (nav.alertaAccionGeneral()) {
				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

				List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);

				ComicManagerDAO.borrarComic(id_comic);
				ListaComicsDAO.reiniciarListaComics();
				ListaComicsDAO.listasAutoCompletado();
				referenciaVentana.getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD());
				FuncionesTableView.actualizarBusquedaRaw(referenciaVentana.getTablaBBDD());
				FuncionesTableView.tablaBBDD(listaComics, referenciaVentana.getTablaBBDD());

				List<ComboBox<String>> comboboxes = referenciaVentana.getComboboxes();

				funcionesCombo.rellenarComboBox(comboboxes);
			} else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, referenciaVentana.getProntInfo());
			}
		}
	}

	public static void eliminarComicLista() {
		String id_comic = referenciaVentana.getIdComicTratar_mod().getText();

		if (nav.alertaEliminar()) {

			if (id_comic != null) {

				ListaComicsDAO.comicsImportados.removeIf(c -> c.getID().equals(id_comic));
				AccionControlUI.limpiarAutorellenos(false);
				FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD());

				FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados, referenciaVentana.getTablaBBDD());
				referenciaVentana.getTablaBBDD().refresh();

				if (ListaComicsDAO.comicsImportados.size() < 1) {
					AccionFuncionesComunes.cambiarEstadoBotones(false);
				}

			}
		}
	}

}
