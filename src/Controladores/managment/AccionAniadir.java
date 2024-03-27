package Controladores.managment;

import java.util.Arrays;
import java.util.List;

import Controladores.VentanaAccionController;
import Funcionamiento.Utilidades;
import comicManagement.Comic;
import dbmanager.ConectManager;
import javafx.scene.Node;

public class AccionAniadir {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();

	private static VentanaAccionController accionController = new VentanaAccionController();

	public static AccionReferencias referenciaVentana = new AccionReferencias();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

	/**
	 * Permite introducir un comic en la base de datos de forma manual
	 * 
	 * @throws Exception
	 */
	public void subidaComic() throws Exception {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		Utilidades.convertirNombresCarpetas(accionFuncionesComunes.SOURCE_PATH);

		Comic comic = accionController.camposComic();
		accionRellenoDatos.actualizarCamposUnicos(comic);

		referenciaVentana.getProntInfo().setOpacity(1);

		accionFuncionesComunes.procesarComic(comic, false);
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
