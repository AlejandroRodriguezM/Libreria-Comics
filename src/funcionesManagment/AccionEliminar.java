package funcionesManagment;

import java.util.Arrays;
import java.util.List;

import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.ListaComicsDAO;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesTableView;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class AccionEliminar {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();
	private static AccionReferencias referenciaVentana = getReferenciaVentana();
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
				referenciaVentana.getBotonParametroComic(), referenciaVentana.getIdComicTratar()));
		referenciaVentana.getRootVBox().toFront();
	}

	public static void eliminarComic() {
		
		String idComic = getReferenciaVentana().getIdComicTratar().getText();
		getReferenciaVentana().getIdComicTratar().setStyle("");
		if (accionFuncionesComunes.comprobarExistenciaComic(idComic)) {
			if (nav.alertaAccionGeneral()) {

				ComicManagerDAO.borrarComic(idComic);
				ListaComicsDAO.reiniciarListaComics();
				ListaComicsDAO.listasAutoCompletado();

				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
				List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
				getReferenciaVentana().getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(listaComics);

				List<ComboBox<String>> comboboxes = getReferenciaVentana().getComboboxes();

				funcionesCombo.rellenarComboBox(comboboxes);
				getReferenciaVentana().getImagencomic().setImage(null);
				getReferenciaVentana().getImagencomic().setVisible(true);
				getReferenciaVentana().getProntInfo().clear();
				getReferenciaVentana().getProntInfo().setOpacity(0);

			} else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfo());
			}
		}
	}

	public static void eliminarComicLista() {
		
		String idComic = getReferenciaVentana().getIdComicTratar().getText();

		if (nav.alertaEliminar() && idComic != null) {

				ListaComicsDAO.comicsImportados.removeIf(c -> c.getid().equals(idComic));
				AccionControlUI.limpiarAutorellenos(false);
				FuncionesTableView.nombreColumnas();

				FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados);
				getReferenciaVentana().getTablaBBDD().refresh();

				if (ListaComicsDAO.comicsImportados.isEmpty()) {
					AccionFuncionesComunes.cambiarEstadoBotones(false);
				}

			}
		
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionEliminar.referenciaVentana = referenciaVentana;
	}

}
