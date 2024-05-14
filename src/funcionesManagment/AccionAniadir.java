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
import dbmanager.ListaComicsDAO;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesTableView;
import funciones_auxiliares.Utilidades;
import funciones_auxiliares.Ventanas;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class AccionAniadir {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

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
		
		Comic comic = AccionControlUI.camposComic(controls, true);
		accionRellenoDatos.actualizarCamposUnicos(comic);

		referenciaVentana.getProntInfo().setOpacity(1);

		accionFuncionesComunes.procesarComic(comic, false);
	}

	public static void guardarContenidoLista() {
		
		if (!ListaComicsDAO.comicsImportados.isEmpty() && nav.alertaInsertar()) {
			Collections.sort(ListaComicsDAO.comicsImportados, Comparator.comparing(Comic::getNombre));

			for (Comic c : ListaComicsDAO.comicsImportados) {
				AccionControlUI.comprobarListaValidacion(c);
				ComicManagerDAO.insertarDatos(c, true);
			}

			ListaComicsDAO.listasAutoCompletado();
			List<ComboBox<String>> comboboxes = referenciaVentana.getComboboxes();
			funcionesCombo.rellenarComboBox(comboboxes);

			ListaComicsDAO.comicsImportados.clear();
			referenciaVentana.getTablaBBDD().getItems().clear();
			AccionControlUI.validarCamposClave(true);
			FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados);
			AccionControlUI.limpiarAutorellenos(false);

			String mensajePront = "Has introducido los comics correctamente\n";
			AlarmaList.mostrarMensajePront(mensajePront, true, referenciaVentana.getProntInfo());
		}

	}

	public void mostrarElementosAniadir(List<Node> elementosAMostrarYHabilitar) {
		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getDibujanteComic(),
				referenciaVentana.getEditorialComic(), referenciaVentana.getEstadoComic(),
				referenciaVentana.getFechaComic(), referenciaVentana.getFirmaComic(),
				referenciaVentana.getFormatoComic(), referenciaVentana.getGuionistaComic(),
				referenciaVentana.getNombreKeyIssue(), referenciaVentana.getGradeoComic(),
				referenciaVentana.getProcedenciaComic(), referenciaVentana.getUrlReferencia(),
				referenciaVentana.getBotonBusquedaAvanzada(), referenciaVentana.getPrecioComic(),
				referenciaVentana.getDireccionImagen(), referenciaVentana.getLabel_portada(),
				referenciaVentana.getLabel_precio(), referenciaVentana.getLabel_gradeo(),
				referenciaVentana.getLabel_dibujante(), referenciaVentana.getLabel_editorial(),
				referenciaVentana.getLabel_estado(), referenciaVentana.getLabel_fecha(),
				referenciaVentana.getLabel_firma(), referenciaVentana.getLabel_formato(),
				referenciaVentana.getLabel_guionista(), referenciaVentana.getLabel_key(),
				referenciaVentana.getLabel_procedencia(), referenciaVentana.getLabel_referencia(),
				referenciaVentana.getCodigoComicTratar(), referenciaVentana.getLabel_codigo_comic(),
				referenciaVentana.getTablaBBDD(), referenciaVentana.getRootVBox(),
				referenciaVentana.getBotonSubidaPortada(), referenciaVentana.getIdComicTratar(),
				referenciaVentana.getLabel_id_mod(), referenciaVentana.getBotonGuardarCambioComic()));
	}
	
	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionAniadir.referenciaVentana = referenciaVentana;
	}

}
