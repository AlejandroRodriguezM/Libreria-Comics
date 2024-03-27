package Controladores.managment;

import java.util.Arrays;
import java.util.List;

import Controladores.VentanaAccionController;
import javafx.scene.Node;

public class AccionEliminar {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();

	private static VentanaAccionController accionController = new VentanaAccionController();

	public static AccionReferencias referenciaVentana = new AccionReferencias();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();
	
	public void mostrarElementosEliminar(List<Node> elementosAMostrarYHabilitar) {
	    elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getLabel_id_mod(), referenciaVentana.getBotonVender(), referenciaVentana.getBotonEliminar(), referenciaVentana.getTablaBBDD(), referenciaVentana.getBotonbbdd(),
	            referenciaVentana.getRootVBox(), referenciaVentana.getBotonParametroComic(), referenciaVentana.getIdComicTratar_mod()));
	    referenciaVentana.getRootVBox().toFront();
	}

}
