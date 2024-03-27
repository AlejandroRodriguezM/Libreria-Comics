package Controladores.managment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import Controladores.VentanaAccionController;
import Funcionamiento.Utilidades;
import comicManagement.Comic;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class AccionControlUI {

	public static AccionReferencias referenciaVentana = new AccionReferencias();

	private static VentanaAccionController accionController = new VentanaAccionController();

	private static AccionAniadir accionAniadir = new AccionAniadir();

	private static AccionEliminar accionEliminar = new AccionEliminar();

	private static AccionModificar accionModificar = new AccionModificar();

	public void autoRelleno() {

		referenciaVentana.getIdComicTratar_mod().textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				if (!rellenarCampos(newValue)) {
					limpiarAutorellenos();
					borrarDatosGraficos();
				}
			} else {
				limpiarAutorellenos();
				borrarDatosGraficos();
			}
		});
	}

	public boolean rellenarCampos(String idComic) {
		Comic comic_temp = Comic.obtenerComic(idComic);
		if (comic_temp != null) {
			rellenarDatos(comic_temp);
			return true;
		}
		return false;
	}

	public void mostrarOpcion(String opcion) {
		ocultarCampos();

		List<Node> elementosAMostrarYHabilitar = new ArrayList<>();

		switch (opcion.toLowerCase()) {
		case "eliminar":
			accionEliminar.mostrarElementosEliminar(elementosAMostrarYHabilitar);
			break;
		case "aniadir":
			accionAniadir.mostrarElementosAniadir(elementosAMostrarYHabilitar);
			break;
		case "modificar":
			accionModificar.mostrarElementosModificar(elementosAMostrarYHabilitar);
			break;
		case "puntuar":
			accionModificar.mostrarElementosPuntuar(elementosAMostrarYHabilitar);
			break;
		default:
			accionController.closeWindow();
			return;
		}

		mostrarElementos(elementosAMostrarYHabilitar);
	}

	private void mostrarElementos(List<Node> elementosAMostrarYHabilitar) {
		for (Node elemento : elementosAMostrarYHabilitar) {
			elemento.setVisible(true);
			elemento.setDisable(false);
		}

		if (!AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {
			autoRelleno();
		}

		if (!AccionFuncionesComunes.TIPO_ACCION.equals("aniadir")) {
			referenciaVentana.getNavegacion_cerrar().setDisable(true);
			referenciaVentana.getNavegacion_cerrar().setVisible(false);

			referenciaVentana.getIdComicTratar_mod().setLayoutX(56);
			referenciaVentana.getIdComicTratar_mod().setLayoutY(104);
			referenciaVentana.getLabel_id_mod().setLayoutX(3);
			referenciaVentana.getLabel_id_mod().setLayoutY(104);
		} else {
			referenciaVentana.getIdComicTratar_mod().setEditable(false);
			referenciaVentana.getIdComicTratar_mod().setOpacity(0.7);
		}
	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public void ocultarCampos() {
		List<Node> elementos = Arrays.asList(referenciaVentana.getTablaBBDD(), referenciaVentana.getDibujanteComic(),
				referenciaVentana.getEditorialComic(), referenciaVentana.getEstadoComic(),
				referenciaVentana.getFechaComic(), referenciaVentana.getFirmaComic(),
				referenciaVentana.getFormatoComic(), referenciaVentana.getGuionistaComic(),
				referenciaVentana.getNombreKeyIssue(), referenciaVentana.getNumeroCajaComic(),
				referenciaVentana.getProcedenciaComic(), referenciaVentana.getUrlReferencia(),
				referenciaVentana.getBotonBorrarOpinion(), referenciaVentana.getPuntuacionMenu(),
				referenciaVentana.getLabelPuntuacion(), referenciaVentana.getBotonAgregarPuntuacion(),
				referenciaVentana.getLabel_id_mod(), referenciaVentana.getBotonVender(),
				referenciaVentana.getBotonEliminar(), referenciaVentana.getBotonModificarComic(),
				referenciaVentana.getBotonBusquedaCodigo(), referenciaVentana.getBotonbbdd(),
				referenciaVentana.getPrecioComic(), referenciaVentana.getDireccionImagen(),
				referenciaVentana.getLabel_portada(), referenciaVentana.getLabel_precio(),
				referenciaVentana.getLabel_caja(), referenciaVentana.getLabel_dibujante(),
				referenciaVentana.getLabel_editorial(), referenciaVentana.getLabel_estado(),
				referenciaVentana.getLabel_fecha(), referenciaVentana.getLabel_firma(),
				referenciaVentana.getLabel_formato(), referenciaVentana.getLabel_guionista(),
				referenciaVentana.getLabel_key(), referenciaVentana.getLabel_procedencia(),
				referenciaVentana.getLabel_referencia(), referenciaVentana.getCodigoComicTratar(),
				referenciaVentana.getLabel_codigo_comic(), referenciaVentana.getBotonSubidaPortada());

		Utilidades.cambiarVisibilidad(elementos, true);
	}

	/**
	 * Establece los atributos del cómic basándose en el objeto Comic proporcionado.
	 * 
	 * @param comic_temp El objeto Comic que contiene los datos a establecer.
	 */
	public void setAtributosDesdeTabla(Comic comic_temp) {
		referenciaVentana.getNombreComic().setText(comic_temp.getNombre());

		String numeroNuevo = comic_temp.getNumero();
		referenciaVentana.getNumeroComic().getSelectionModel().select(numeroNuevo);

		referenciaVentana.getVarianteComic().setText(comic_temp.getVariante());

		referenciaVentana.getFirmaComic().setText(comic_temp.getFirma());

		referenciaVentana.getEditorialComic().setText(comic_temp.getEditorial());

		String formato = comic_temp.getFormato();
		referenciaVentana.getFormatoComic().getSelectionModel().select(formato);

		String procedencia = comic_temp.getProcedencia();
		referenciaVentana.getProcedenciaComic().getSelectionModel().select(procedencia);

		String fechaString = comic_temp.getFecha();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate fecha = LocalDate.parse(fechaString, formatter);
		referenciaVentana.getFechaComic().setValue(fecha);

		referenciaVentana.getGuionistaComic().setText(comic_temp.getGuionista());

		referenciaVentana.getDibujanteComic().setText(comic_temp.getDibujante());

		String cajaAni = comic_temp.getNumCaja();
		referenciaVentana.getNumeroCajaComic().getSelectionModel().select(cajaAni);

		referenciaVentana.getNombreKeyIssue().setText(comic_temp.getKey_issue());
		referenciaVentana.getEstadoComic().getSelectionModel().select(comic_temp.getEstado());

		referenciaVentana.getPrecioComic().setText(comic_temp.getPrecio_comic());
		referenciaVentana.getUrlReferencia().setText(comic_temp.getUrl_referencia());

		referenciaVentana.getDireccionImagen().setText(comic_temp.getImagen());

		referenciaVentana.getCodigoComicTratar().setText(comic_temp.getCodigo_comic());

		referenciaVentana.getIdComicTratar_mod().setText(comic_temp.getID());

		Utilidades.cargarImagenAsync(comic_temp.getImagen(), referenciaVentana.getImagencomic());
	}

	private void rellenarDatos(Comic comic) {
		referenciaVentana.getNumeroComic().getSelectionModel().clearSelection();
		referenciaVentana.getFormatoComic().getSelectionModel().clearSelection();
		referenciaVentana.getNumeroCajaComic().getSelectionModel().clearSelection();

		referenciaVentana.getNombreComic().setText(comic.getNombre());
		referenciaVentana.getNumeroComic().getSelectionModel().select(comic.getNumero());
		referenciaVentana.getVarianteComic().setText(comic.getVariante());
		referenciaVentana.getFirmaComic().setText(comic.getFirma());
		referenciaVentana.getEditorialComic().setText(comic.getEditorial());
		referenciaVentana.getFormatoComic().getSelectionModel().select(comic.getFormato());
		referenciaVentana.getProcedenciaComic().getSelectionModel().select(comic.getProcedencia());

		LocalDate fecha = LocalDate.parse(comic.getFecha(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		referenciaVentana.getFechaComic().setValue(fecha);

		referenciaVentana.getGuionistaComic().setText(comic.getGuionista());
		referenciaVentana.getDibujanteComic().setText(comic.getDibujante());
		referenciaVentana.getNumeroCajaComic().getSelectionModel().select(comic.getNumCaja());
		referenciaVentana.getNombreKeyIssue().setText(comic.getKey_issue());
		referenciaVentana.getEstadoComic().getSelectionModel().select(comic.getEstado());
		referenciaVentana.getPrecioComic().setText(comic.getPrecio_comic());
		referenciaVentana.getUrlReferencia().setText(comic.getUrl_referencia());
		referenciaVentana.getDireccionImagen().setText(comic.getImagen());

		referenciaVentana.getProntInfo().clear();
		referenciaVentana.getProntInfo().setOpacity(1);

		Image imagenComic = Utilidades.devolverImagenComic(comic.getImagen());
		referenciaVentana.getImagencomic().setImage(imagenComic);
	}

	/**
	 * Actualiza los campos únicos del objeto Comic con los valores ingresados en
	 * los campos de la interfaz gráfica.
	 * 
	 * @param comic El objeto Comic a actualizar.
	 */
	public void actualizarCamposUnicos(Comic comic) {

		comic.setKey_issue(!referenciaVentana.getNombreKeyIssue().getText().isEmpty()
				? Utilidades.eliminarEspacios(referenciaVentana.getNombreKeyIssue().getText())
				: (!referenciaVentana.getNombreKeyIssue().getText().trim().isEmpty() && Pattern.compile(".*\\w+.*")
						.matcher(referenciaVentana.getNombreKeyIssue().getText().trim()).matches()
								? referenciaVentana.getNombreKeyIssue().getText().trim()
								: "Vacio"));

		comic.setUrl_referencia(!referenciaVentana.getUrlReferencia().getText().isEmpty()
				? Utilidades.eliminarEspacios(referenciaVentana.getUrlReferencia().getText())
				: (referenciaVentana.getUrlReferencia().getText().isEmpty() ? "Sin referencia"
						: referenciaVentana.getUrlReferencia().getText()));
		comic.setPrecio_comic(!referenciaVentana.getPrecioComic().getText().isEmpty()
				? Utilidades.eliminarEspacios(referenciaVentana.getPrecioComic().getText())
				: (referenciaVentana.getPrecioComic().getText().isEmpty() ? "0"
						: referenciaVentana.getPrecioComic().getText()));
		comic.setCodigo_comic(Utilidades.eliminarEspacios(referenciaVentana.getCodigoComicTratar().getText()));

		comic.setNumCaja(comic.getNumCaja().isEmpty() ? "0" : comic.getNumCaja());
	}

	public void validarCamposComic(boolean esBorrado) {
		List<TextField> camposUi = Arrays.asList(referenciaVentana.getNombreComic(),
				referenciaVentana.getVarianteComic(), referenciaVentana.getEditorialComic(),
				referenciaVentana.getPrecioComic(), referenciaVentana.getGuionistaComic(),
				referenciaVentana.getDibujanteComic());

		for (TextField campoUi : camposUi) {
			String datoComic = campoUi.getText();

			if (esBorrado) {
				if (datoComic == null || datoComic.isEmpty() || datoComic.equalsIgnoreCase("vacio")) {
					campoUi.setStyle("");
				}
			} else {
				// Verificar si el campo está vacío, es nulo o tiene el valor "Vacio"
				if (datoComic == null || datoComic.isEmpty() || datoComic.equalsIgnoreCase("vacio")) {
					campoUi.setStyle("-fx-background-color: red;");
				} else {
					campoUi.setStyle("");
				}
			}

		}
	}

	public boolean camposComicSonValidos() {
		List<Control> camposUi = Arrays.asList(referenciaVentana.getNombreComic(), referenciaVentana.getVarianteComic(),
				referenciaVentana.getEditorialComic(), referenciaVentana.getPrecioComic(),
				referenciaVentana.getCodigoComicTratar(), referenciaVentana.getGuionistaComic(),
				referenciaVentana.getDibujanteComic(), referenciaVentana.getFechaComic());

		for (Control campoUi : camposUi) {
			if (campoUi instanceof TextField) {
				String datoComic = ((TextField) campoUi).getText();

				// Verificar si el campo está vacío, es nulo o tiene el valor "Vacio"
				if (datoComic == null || datoComic.isEmpty() || datoComic.equalsIgnoreCase("vacio")) {
					campoUi.setStyle("-fx-background-color: #FF0000;");
					return false; // Devolver false si al menos un campo no es válido
				} else {
					campoUi.setStyle("");
				}
			} else if (campoUi instanceof DatePicker) {
				LocalDate fecha = ((DatePicker) campoUi).getValue();

				// Verificar si la fecha está vacía
				if (fecha == null) {
					campoUi.setStyle("-fx-background-color: #FF0000;");
					return false; // Devolver false si al menos un campo no es válido
				} else {
					campoUi.setStyle("");
				}
			}
		}

		return true; // Devolver true si todos los campos son válidos
	}

	/**
	 * Borra los datos del cómic
	 */
	public void limpiarAutorellenos() {
		referenciaVentana.getNombreComic().setText("");
		referenciaVentana.getNumeroComic().setValue("");
		referenciaVentana.getNumeroComic().getEditor().setText("");

		referenciaVentana.getVarianteComic().setText("");
		referenciaVentana.getFirmaComic().setText("");
		referenciaVentana.getEditorialComic().setText("");

		referenciaVentana.getFormatoComic().setValue("");
		referenciaVentana.getFormatoComic().getEditor().setText("");

		referenciaVentana.getProcedenciaComic().setValue("");
		referenciaVentana.getProcedenciaComic().getEditor().setText("");

		referenciaVentana.getFechaComic().setValue(null);
		referenciaVentana.getGuionistaComic().setText("");
		referenciaVentana.getDibujanteComic().setText("");
		referenciaVentana.getNombreKeyIssue().setText("");
		referenciaVentana.getFechaComic().setValue(null);

		referenciaVentana.getPrecioComic().setText("");
		referenciaVentana.getBusquedaCodigo().setText("");
		referenciaVentana.getCodigoComicTratar().setText("");
		referenciaVentana.getUrlReferencia().setText("");
		referenciaVentana.getNumeroCajaComic().setValue("");
		referenciaVentana.getNumeroCajaComic().getEditor().setText("");

		referenciaVentana.getNombreKeyIssue().setText("");
		referenciaVentana.getDireccionImagen().setText("");
		referenciaVentana.getImagencomic().setImage(null);
		referenciaVentana.getCodigoComicTratar().setText("");
		referenciaVentana.getIdComicTratar_mod().setText("");

		if ("aniadir".equals(AccionFuncionesComunes.TIPO_ACCION)) {
			referenciaVentana.getIdComicTratar_mod().setDisable(false);
			referenciaVentana.getIdComicTratar_mod().setText("");
			referenciaVentana.getIdComicTratar_mod().setDisable(true);

		}

		referenciaVentana.getFormatoComic().getSelectionModel().selectFirst();
		referenciaVentana.getProcedenciaComic().getSelectionModel().selectFirst();
		referenciaVentana.getEstadoComic().getSelectionModel().selectFirst();

		referenciaVentana.getProntInfo().setText(null);
		referenciaVentana.getProntInfo().setOpacity(0);
		referenciaVentana.getProntInfo().setStyle("");
		validarCamposComic(true);
	}

	public void borrarDatosGraficos() {
		referenciaVentana.getProntInfo().setText(null);
		referenciaVentana.getProntInfo().setOpacity(0);
		referenciaVentana.getProntInfo().setStyle("");
		referenciaVentana.getTablaBBDD().getItems().clear();
	}

}
