package Controladores.managment;

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
import controlUI.FuncionesTableView;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.ListaComicsDAO;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class AccionAniadir {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();

	public static AccionReferencias referenciaVentana = new AccionReferencias();

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

		if (!ConectManager.conexionActiva()) {
			return;
		}

		Utilidades.convertirNombresCarpetas(AccionFuncionesComunes.SOURCE_PATH);

		Comic comic = AccionControlUI.camposComic(referenciaVentana.getListaCamposTexto(), true);
		accionRellenoDatos.actualizarCamposUnicos(comic);

		referenciaVentana.getProntInfo().setOpacity(1);

		accionFuncionesComunes.procesarComic(comic, false);
	}

	public static void guardarContenidoLista() {
		if (ListaComicsDAO.comicsImportados.size() > 0 && nav.alertaInsertar()) {
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
			FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados, referenciaVentana.getTablaBBDD()); // Llamada
																												// a
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
				referenciaVentana.getNombreKeyIssue(), referenciaVentana.getNumeroCajaComic(),
				referenciaVentana.getProcedenciaComic(), referenciaVentana.getUrlReferencia(),
				referenciaVentana.getBotonBusquedaAvanzada(), referenciaVentana.getPrecioComic(),
				referenciaVentana.getDireccionImagen(), referenciaVentana.getLabel_portada(),
				referenciaVentana.getLabel_precio(), referenciaVentana.getLabel_caja(),
				referenciaVentana.getLabel_dibujante(), referenciaVentana.getLabel_editorial(),
				referenciaVentana.getLabel_estado(), referenciaVentana.getLabel_fecha(),
				referenciaVentana.getLabel_firma(), referenciaVentana.getLabel_formato(),
				referenciaVentana.getLabel_guionista(), referenciaVentana.getLabel_key(),
				referenciaVentana.getLabel_procedencia(), referenciaVentana.getLabel_referencia(),
				referenciaVentana.getCodigoComicTratar(), referenciaVentana.getLabel_codigo_comic(),
				referenciaVentana.getTablaBBDD(), referenciaVentana.getRootVBox(),
				referenciaVentana.getBotonSubidaPortada(), referenciaVentana.getIdComicTratar_mod(),
				referenciaVentana.getLabel_id_mod(), referenciaVentana.getBotonGuardarCambioComic()));
	}

}
