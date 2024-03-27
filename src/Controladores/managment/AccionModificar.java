package Controladores.managment;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import Controladores.VentanaAccionController;
import Funcionamiento.FuncionesComboBox;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class AccionModificar {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();

	private static VentanaAccionController accionController = new VentanaAccionController();

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
	 * Funcion que permite modificar un comic, segun los datos introducidos
	 * 
	 * @throws Exception
	 */
	public static void modificacionComic() throws Exception {

		if (!ConectManager.conexionActiva()) {
			return;
		}

		String id_comic = referenciaVentana.getIdComicTratar_mod().getText();

		Comic comic_temp = ComicManagerDAO.comicDatos(id_comic);
		Comic datos = accionController.camposComic();
		Comic comicModificado = new Comic();

		Utilidades.convertirNombresCarpetas(accionFuncionesComunes.SOURCE_PATH);

		comicModificado.setNombre(Utilidades.defaultIfNullOrEmpty(datos.getNombre(), comic_temp.getNombre()));
		comicModificado.setNumero(Utilidades.defaultIfNullOrEmpty(datos.getNumero(), comic_temp.getNumero()));
		comicModificado.setVariante(Utilidades.defaultIfNullOrEmpty(datos.getVariante(), comic_temp.getVariante()));
		comicModificado.setFirma(Utilidades.defaultIfNullOrEmpty(datos.getFirma(), comic_temp.getFirma()));
		comicModificado.setEditorial(Utilidades.defaultIfNullOrEmpty(datos.getEditorial(), comic_temp.getEditorial()));
		comicModificado.setFormato(Utilidades.defaultIfNullOrEmpty(datos.getFormato(), comic_temp.getFormato()));
		comicModificado
				.setProcedencia(Utilidades.defaultIfNullOrEmpty(datos.getProcedencia(), comic_temp.getProcedencia()));
		comicModificado.setFecha(Utilidades.defaultIfNullOrEmpty(datos.getFecha(), comic_temp.getFecha()));
		comicModificado.setGuionista(Utilidades.defaultIfNullOrEmpty(datos.getGuionista(), comic_temp.getGuionista()));
		comicModificado.setDibujante(Utilidades.defaultIfNullOrEmpty(datos.getDibujante(), comic_temp.getDibujante()));
		comicModificado.setImagen(Utilidades.defaultIfNullOrEmpty(datos.getImagen(), comic_temp.getImagen()));
		comicModificado.setEstado(Utilidades.defaultIfNullOrEmpty(datos.getEstado(), comic_temp.getEstado()));
		comicModificado.setNumCaja(Utilidades.defaultIfNullOrEmpty(datos.getNumCaja(), comic_temp.getNumCaja()));
		comicModificado.setPuntuacion(
				comic_temp.getPuntuacion().equals("Sin puntuar") ? "Sin puntuar" : comic_temp.getPuntuacion());

		String key_issue_sinEspacios = datos.getKey_issue().trim();

		if (!key_issue_sinEspacios.isEmpty() && key_issue_sinEspacios.matches(".*\\w+.*")) {
			comicModificado.setKey_issue(key_issue_sinEspacios);
		} else if (comic_temp != null && comic_temp.getKey_issue() != null && !comic_temp.getKey_issue().isEmpty()) {
			comicModificado.setKey_issue(comic_temp.getKey_issue());
		}

		String url_referencia = Utilidades.defaultIfNullOrEmpty(datos.getUrl_referencia(), "");
		comicModificado.setUrl_referencia(url_referencia.isEmpty() ? "Sin referencia" : url_referencia);

		String precio_comic = Utilidades.defaultIfNullOrEmpty(datos.getPrecio_comic(), "0");
		comicModificado.setPrecio_comic(
				String.valueOf(Utilidades.convertirMonedaADolar(comicModificado.getProcedencia(), precio_comic)));

		comicModificado.setCodigo_comic(Utilidades.defaultIfNullOrEmpty(datos.getCodigo_comic(), ""));

		accionFuncionesComunes.procesarComic(comicModificado, true);
	}

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
	public void accionPuntuar(boolean esAgregar) {

		if (ConectManager.conexionActiva()) {
			String id_comic = referenciaVentana.getIdComicTratar_mod().getText();
			if (accionFuncionesComunes.comprobarExistenciaComic(id_comic)) {
				if (nav.alertaAccionGeneral()) {
					if (esAgregar) {
						ComicManagerDAO.actualizarOpinion(id_comic,
								FuncionesComboBox.puntuacionCombobox(referenciaVentana.getPuntuacionMenu()));
					} else {
						ComicManagerDAO.actualizarOpinion(id_comic, "0");
					}
					String mensaje = ". Has borrado la puntuacion del comic.";

					AlarmaList.mostrarMensajePront(mensaje, true, referenciaVentana.getProntInfo());

					List<ComboBox<String>> comboboxes = VentanaAccionController.getComboBoxes();

					funcionesCombo.rellenarComboBox(comboboxes);
				} else {
					String mensaje = "Accion cancelada";
					AlarmaList.mostrarMensajePront(mensaje, false, referenciaVentana.getProntInfo());
				}

			}
		}

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

}
