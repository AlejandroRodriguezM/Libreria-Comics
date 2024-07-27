package funcionesManagment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.ListasComicsDAO;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesTableView;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class AccionAniadir {

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

	/**
	 * Permite introducir un comic en la base de datos de forma manual
	 * 
	 * @throws Exception
	 */
	public void subidaComic() throws Exception {

		Utilidades.convertirNombresCarpetas(AccionFuncionesComunes.carpetaPortadas(ConectManager.DB_NAME));

		List<String> controls = new ArrayList<>();

		for (Control control : AccionReferencias.getListaTextFields()) {
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

		Comic comic = AccionControlUI.camposComic(controls, true);
//		accionRellenoDatos.actualizarCamposUnicos(comic);

		referenciaVentana.getProntInfoTextArea().setOpacity(1);

		accionFuncionesComunes.procesarComic(comic, false);
	}

	public static void guardarContenidoLista(boolean esLista, Comic comic) {

		if (!ListasComicsDAO.comicsImportados.isEmpty() && nav.alertaInsertar()) {
			Collections.sort(ListasComicsDAO.comicsImportados, Comparator.comparing(Comic::getTituloComic));
			String mensajePront = "";
			if (esLista) {
				for (Comic c : ListasComicsDAO.comicsImportados) {
					if (AccionControlUI.comprobarListaValidacion(c)) {
						ComicManagerDAO.insertarDatos(c, true);
					}
				}
				ListasComicsDAO.comicsImportados.clear();
				mensajePront = "Has introducido las comics correctamente\n\n";
			} else {
				ComicManagerDAO.insertarDatos(comic, true);

				ListasComicsDAO.comicsImportados.removeIf(c -> c.getIdComic().equals(comic.getIdComic()));

				mensajePront = "Has introducido la comic correctamente\n\n";
			}

			ListasComicsDAO.listasAutoCompletado();
			getReferenciaVentana();
			List<ComboBox<String>> comboboxes = AccionReferencias.getListaComboboxes();
			funcionesCombo.rellenarComboBox(comboboxes);

			referenciaVentana.getTablaBBDD().getItems().clear();
			AccionControlUI.validarCamposClave(true);
			FuncionesTableView.tablaBBDD(ListasComicsDAO.comicsImportados);
			AccionControlUI.limpiarAutorellenos(false);

			AlarmaList.mostrarMensajePront(mensajePront, true, referenciaVentana.getProntInfoTextArea());
		}

	}

	public void mostrarElementosAniadir(List<Node> elementosAMostrarYHabilitar) {

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getLabelAnio(), // Etiqueta para el año
				referenciaVentana.getLabelCodigo(), // Etiqueta para el código
				referenciaVentana.getLabelArtista(), // Etiqueta para el artista
				referenciaVentana.getLabelGuionista(), // Etiqueta para el guionista
				referenciaVentana.getLabelVariante(), // Etiqueta para la variante
				referenciaVentana.getLabelfechaG(), // Etiqueta para la fecha de gradeo
				referenciaVentana.getLabelEditor(), // Etiqueta para el editor
				referenciaVentana.getLabelKeyComic(), // Etiqueta para los comentarios clave
				referenciaVentana.getLabelNombre(), // Etiqueta para el nombre
				referenciaVentana.getLabelIdMod(), // Etiqueta para el ID de modificación
				referenciaVentana.getLabelPortada(), // Etiqueta para la portada
				referenciaVentana.getLabelGradeo(), // Etiqueta para el gradeo
				referenciaVentana.getLabelReferencia() // Etiqueta para la referencia
		));

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getTituloComicTextField(),
				referenciaVentana.getNombreEditorTextField(), referenciaVentana.getBusquedaGeneralTextField(),
				referenciaVentana.getNumeroComicTextField(), referenciaVentana.getCodigoComicTratarTextField(),
				referenciaVentana.getDireccionImagenTextField(), referenciaVentana.getIdComicTratarTextField(),
				referenciaVentana.getUrlReferenciaTextField(), referenciaVentana.getCodigoComicTextField(),
				referenciaVentana.getArtistaComicTextField(), referenciaVentana.getGuionistaComicTextField(),
				referenciaVentana.getVarianteTextField(), referenciaVentana.getKeyComicData(),
				referenciaVentana.getNombreEditorTextField(), referenciaVentana.getDataPickFechaP()));

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getBotonSubidaPortada(),
				referenciaVentana.getBotonBusquedaAvanzada(), referenciaVentana.getBotonGuardarCambioComic(),
				referenciaVentana.getBotonEliminarImportadoListaComic(),
				referenciaVentana.getBotonGuardarListaComics()));
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionAniadir.referenciaVentana = referenciaVentana;
	}

}
