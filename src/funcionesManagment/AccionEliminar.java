package funcionesManagment;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;

public class AccionEliminar {

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	public void mostrarElementosEliminar(List<Node> elementosAMostrarYHabilitar) {
		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getLabelIdMod(),
				referenciaVentana.getBotonEliminar(), referenciaVentana.getTablaBBDD(),
				referenciaVentana.getBotonbbdd(), referenciaVentana.getRootVBox(),
				referenciaVentana.getBotonParametroComic(), referenciaVentana.getIdComicTratarTextField()));

		referenciaVentana.getRootVBox().toFront();
	}

	

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionEliminar.referenciaVentana = referenciaVentana;
	}

}
