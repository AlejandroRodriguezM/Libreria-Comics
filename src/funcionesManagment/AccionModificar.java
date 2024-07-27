package funcionesManagment;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.DBUtilidades;
import dbmanager.ListasComicsDAO;
import dbmanager.SelectManager;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesInterfaz.FuncionesTableView;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import webScrap.WebScrapNodeJSInstall;

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

	public static void venderComic() throws SQLException {

		String idComic = getReferenciaVentana().getIdComicTratarTextField().getText();
		getReferenciaVentana().getIdComicTratarTextField().setStyle("");
		Comic comicActualizar = ComicManagerDAO.comicDatos(idComic);
		if (accionFuncionesComunes.comprobarExistenciaComic(idComic)) {
			if (nav.alertaAccionGeneral()) {
				ComicManagerDAO.actualizarComicBBDD(comicActualizar, "vender");
				ListasComicsDAO.reiniciarListaComics();
				String mensaje = ". Has puesto a la venta el comic";
				AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());

				getReferenciaVentana();
				List<ComboBox<String>> comboboxes = AccionReferencias.getListaComboboxes();

				funcionesCombo.rellenarComboBox(comboboxes);
				getReferenciaVentana().getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(ListasComicsDAO.comicsImportados);

			} else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
			}

		}
	}

	public static void modificarComic() throws IOException {

		String idComic = getReferenciaVentana().getIdComicTratarTextField().getText();
		getReferenciaVentana().getIdComicTratarTextField().setStyle("");

		if (accionFuncionesComunes.comprobarExistenciaComic(idComic)) {
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
			List<Comic> listaComics;
			if (nav.alertaAccionGeneral()) {

				Utilidades.convertirNombresCarpetas(AccionFuncionesComunes.carpetaPortadas(Utilidades.nombreDB()));

				Comic comicModificado = AccionControlUI.comicModificado();
				accionFuncionesComunes.procesarComic(comicModificado, true);

				ListasComicsDAO.listasAutoCompletado();

				listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
				getReferenciaVentana().getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(listaComics);
			} else {
				listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
				ListasComicsDAO.reiniciarListaComics();
				ListasComicsDAO.listasAutoCompletado();
				FuncionesTableView.nombreColumnas(); // Llamada a funcion
				FuncionesTableView.actualizarBusquedaRaw();
				FuncionesTableView.tablaBBDD(listaComics);
			}
		}
	}

	public static void actualizarComicLista() {

		if (!accionRellenoDatos.camposComicSonValidos()) {
			String mensaje = "Error. Debes de introducir los datos correctos";
			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
			return; // Agregar return para salir del método en este punto
		}

		List<Control> allControls = getReferenciaVentana().getControlAccion();
		List<String> valorControles = new ArrayList<>();
		for (Control control : allControls) {
			if (control instanceof TextField) {
				TextField textField = (TextField) control;
				String value = textField.getText();
				valorControles.add(value);
			} else if (control instanceof DatePicker) {
				DatePicker datePicker = (DatePicker) control;
				String value = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
				valorControles.add(value);
			} else if (control instanceof TextArea) {
				TextArea textArea = (TextArea) control;
				String value = textArea.getText();
				valorControles.add(value);
			}
		}

		Comic datos = AccionControlUI.camposComic(valorControles, true);
		if (!ListasComicsDAO.comicsImportados.isEmpty()) {

			if (datos.getIdComic() == null || datos.getIdComic().isEmpty()) {
				datos = ListasComicsDAO.buscarComicPorID(ListasComicsDAO.comicsImportados, datos.getIdComic());
			}

			// Si hay elementos en la lista
			for (Comic c : ListasComicsDAO.comicsImportados) {
				if (c.getIdComic().equals(datos.getIdComic())) {
					// Si se encuentra un cómic con el mismo ID, reemplazarlo con los nuevos datos
					ListasComicsDAO.comicsImportados.set(ListasComicsDAO.comicsImportados.indexOf(c), datos);
					break; // Salir del bucle una vez que se actualice el cómic
				}
			}
		} else {
			String id = "A" + 0 + "" + (ListasComicsDAO.comicsImportados.size() + 1);
			datos.setIdComic(id);
			ListasComicsDAO.comicsImportados.add(datos);
			getReferenciaVentana().getBotonGuardarListaComics().setVisible(true);
			getReferenciaVentana().getBotonEliminarImportadoListaComic().setVisible(true);

			getReferenciaVentana().getBotonGuardarListaComics().setDisable(false);
			getReferenciaVentana().getBotonEliminarImportadoListaComic().setDisable(false);
		}

		AccionFuncionesComunes.cambiarEstadoBotones(false);
		getReferenciaVentana().getBotonCancelarSubida().setVisible(false); // Oculta el botón de cancelar

		Comic.limpiarCamposComic(datos);
		AccionControlUI.limpiarAutorellenos(false);

		FuncionesTableView.nombreColumnas();
		FuncionesTableView.tablaBBDD(ListasComicsDAO.comicsImportados);
	}

	public void mostrarElementosModificar(List<Node> elementosAMostrarYHabilitar) {

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

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getNumeroComicCombobox(),
				getReferenciaVentana().getRootVBox(), getReferenciaVentana().getBotonSubidaPortada(),
				getReferenciaVentana().getBotonbbdd(), getReferenciaVentana().getTablaBBDD(),
				getReferenciaVentana().getBotonParametroComic(), getReferenciaVentana().getBotonEliminar()));

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getTituloComicTextField(),
				referenciaVentana.getNombreEditorTextField(), referenciaVentana.getBusquedaGeneralTextField(),
				referenciaVentana.getNumeroComicTextField(), referenciaVentana.getCodigoComicTratarTextField(),
				referenciaVentana.getDireccionImagenTextField(), referenciaVentana.getIdComicTratarTextField(),
				referenciaVentana.getUrlReferenciaTextField(), referenciaVentana.getCodigoComicTextField(),
				referenciaVentana.getArtistaComicTextField(), referenciaVentana.getGuionistaComicTextField(),
				referenciaVentana.getVarianteTextField(), referenciaVentana.getKeyComicData(),
				referenciaVentana.getNombreEditorTextField(), referenciaVentana.getDataPickFechaP()));

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getBotonSubidaPortada(),
				getReferenciaVentana().getBotonModificarComic()));

		getReferenciaVentana().getRootVBox().toFront();
	}

	public static void actualizarDatabase(String tipoUpdate, Stage ventanaOpciones) {

		if (!Utilidades.isInternetAvailable()) {
			return;
		}

		List<String> inputPortadas = DBUtilidades.obtenerValoresColumna("direccionImagenComic");
		Utilidades.borrarArchivosNoEnLista(inputPortadas);

		boolean estaBaseLlena = ListasComicsDAO.comprobarLista();

		if (!estaBaseLlena) {
			String cadenaCancelado = "La base de datos esta vacia";
			AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(), cadenaCancelado);
			return;
		}

		String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
		List<Comic> listaComicsDatabase = SelectManager.verLibreria(sentenciaSQL, true);

		Collections.sort(listaComicsDatabase, (comic1, comic2) -> {
			int id1 = Integer.parseInt(comic1.getIdComic());
			int id2 = Integer.parseInt(comic2.getIdComic());
			return Integer.compare(id1, id2);
		});

		List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();

		for (Stage stage : stageVentanas) {
			if (stage != ventanaOpciones && !stage.getTitle().equalsIgnoreCase("Menu principal")) {
				stage.close(); // Close the stage if it's not the current state
			}
		}

		if (WebScrapNodeJSInstall.checkNodeJSVersion()) {
			AccionFuncionesComunes.busquedaPorListaDatabase(listaComicsDatabase, tipoUpdate);

			if (getReferenciaVentana().getTablaBBDD() != null) {
				getReferenciaVentana().getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(ListasComicsDAO.comicsImportados);
			}
		}
	}

	public static void eliminarComic() {

		String idComic = getReferenciaVentana().getIdComicTratarTextField().getText();
		getReferenciaVentana().getIdComicTratarTextField().setStyle("");
		if (accionFuncionesComunes.comprobarExistenciaComic(idComic)) {
			if (nav.alertaAccionGeneral()) {

				ComicManagerDAO.borrarComic(idComic);
				ListasComicsDAO.reiniciarListaComics();
				ListasComicsDAO.listasAutoCompletado();

				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
				List<Comic> listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
				getReferenciaVentana().getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(listaComics);

				List<ComboBox<String>> comboboxes = getReferenciaVentana().getListaComboboxes();

				funcionesCombo.rellenarComboBox(comboboxes);
				getReferenciaVentana().getImagenComic().setImage(null);
				getReferenciaVentana().getImagenComic().setVisible(true);
				getReferenciaVentana().getProntInfoTextArea().clear();
				getReferenciaVentana().getProntInfoTextArea().setOpacity(0);
				AccionControlUI.limpiarAutorellenos(false);
			} else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
			}
		}
	}

	public static void eliminarComicLista() {
		String idComic = getReferenciaVentana().getIdComicTratarTextField().getText();

		if (nav.alertaEliminar() && idComic != null) {
			// Obtener la comic a eliminar
			Comic comicEliminar = ListasComicsDAO.comicsImportados.stream().filter(c -> c.getIdComic().equals(idComic))
					.findFirst().orElse(null);

			if (comicEliminar != null) {
				// Obtener la dirección de la imagen y verificar si existe
				String direccionImagen = comicEliminar.getDireccionImagenComic();
				if (direccionImagen != null && !direccionImagen.isEmpty()) {
					File archivoImagen = new File(direccionImagen);
					if (archivoImagen.exists()) {
						// Borrar el archivo de la imagen
						if (archivoImagen.delete()) {
							System.out.println("Archivo de imagen eliminado: " + direccionImagen);
						} else {
							System.err.println("No se pudo eliminar el archivo de imagen: " + direccionImagen);
							// Puedes lanzar una excepción aquí si lo prefieres
						}
					}
				}

				// Eliminar la comic de la lista
				ListasComicsDAO.comicsImportados.remove(comicEliminar);
				AccionControlUI.limpiarAutorellenos(false);
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(ListasComicsDAO.comicsImportados);
				getReferenciaVentana().getTablaBBDD().refresh();

				// Verificar si la lista está vacía y cambiar el estado de los botones
				if (ListasComicsDAO.comicsImportados.isEmpty()) {
					AccionFuncionesComunes.cambiarEstadoBotones(false);
				}
			}
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
