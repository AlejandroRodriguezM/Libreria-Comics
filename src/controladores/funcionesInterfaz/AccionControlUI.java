package controladores.funcionesInterfaz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import controladores.VentanaAccionController;
import controladores.managment.AccionAniadir;
import controladores.managment.AccionEliminar;
import controladores.managment.AccionFuncionesComunes;
import controladores.managment.AccionModificar;
import controladores.managment.AccionReferencias;
import controladores.managment.AccionSeleccionar;
import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.ListaComicsDAO;
import funciones_auxiliares.Utilidades;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class AccionControlUI {

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static VentanaAccionController accionController = new VentanaAccionController();

	private static AccionAniadir accionAniadir = new AccionAniadir();

	private static AccionEliminar accionEliminar = new AccionEliminar();

	private static AccionModificar accionModificar = new AccionModificar();

	public static void autoRelleno() {

		getReferenciaVentana().getIdComicTratar().textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				if (!rellenarCampos(newValue)) {
					limpiarAutorellenos(false);
					borrarDatosGraficos();
				}
			} else {
				limpiarAutorellenos(false);
				borrarDatosGraficos();
			}
		});
	}

	public static boolean rellenarCampos(String idComic) {
		Comic comicTemp = Comic.obtenerComic(idComic);
		if (comicTemp != null) {
			rellenarDatos(comicTemp);
			return true;
		}
		return false;
	}

	public static void mostrarOpcion(String opcion) {
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

	private static void mostrarElementos(List<Node> elementosAMostrarYHabilitar) {
		for (Node elemento : elementosAMostrarYHabilitar) {
			elemento.setVisible(true);
			elemento.setDisable(false);
		}

		if (!AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {
			autoRelleno();
		}

		if (!AccionFuncionesComunes.TIPO_ACCION.equals("aniadir")) {
			getReferenciaVentana().getNavegacion_cerrar().setDisable(true);
			getReferenciaVentana().getNavegacion_cerrar().setVisible(false);

			getReferenciaVentana().getIdComicTratar().setLayoutX(56);
			getReferenciaVentana().getIdComicTratar().setLayoutY(104);
			getReferenciaVentana().getLabel_id_mod().setLayoutX(3);
			getReferenciaVentana().getLabel_id_mod().setLayoutY(104);
		} else {
			getReferenciaVentana().getIdComicTratar().setEditable(false);
			getReferenciaVentana().getIdComicTratar().setOpacity(0.7);
		}
	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public static void ocultarCampos() {
		List<Node> elementos = Arrays.asList(getReferenciaVentana().getTablaBBDD(),
				getReferenciaVentana().getDibujanteComic(), getReferenciaVentana().getEditorialComic(),
				getReferenciaVentana().getEstadoComic(), getReferenciaVentana().getFechaComic(),
				getReferenciaVentana().getFirmaComic(), getReferenciaVentana().getFormatoComic(),
				getReferenciaVentana().getGuionistaComic(), getReferenciaVentana().getNombreKeyIssue(),
				getReferenciaVentana().getGradeoComic(), getReferenciaVentana().getProcedenciaComic(),
				getReferenciaVentana().getUrlReferencia(), getReferenciaVentana().getBotonBorrarOpinion(),
				getReferenciaVentana().getPuntuacionMenu(), getReferenciaVentana().getLabelPuntuacion(),
				getReferenciaVentana().getBotonAgregarPuntuacion(), getReferenciaVentana().getLabel_id_mod(),
				getReferenciaVentana().getBotonVender(), getReferenciaVentana().getBotonEliminar(),
				getReferenciaVentana().getBotonModificarComic(), getReferenciaVentana().getBotonBusquedaCodigo(),
				getReferenciaVentana().getBotonbbdd(), getReferenciaVentana().getPrecioComic(),
				getReferenciaVentana().getDireccionImagen(), getReferenciaVentana().getLabel_portada(),
				getReferenciaVentana().getLabel_precio(), getReferenciaVentana().getLabel_caja(),
				getReferenciaVentana().getLabel_dibujante(), getReferenciaVentana().getLabel_editorial(),
				getReferenciaVentana().getLabel_estado(), getReferenciaVentana().getLabel_fecha(),
				getReferenciaVentana().getLabel_firma(), getReferenciaVentana().getLabel_formato(),
				getReferenciaVentana().getLabel_guionista(), getReferenciaVentana().getLabel_key(),
				getReferenciaVentana().getLabel_procedencia(), getReferenciaVentana().getLabel_referencia(),
				getReferenciaVentana().getCodigoComicTratar(), getReferenciaVentana().getLabel_codigo_comic(),
				getReferenciaVentana().getBotonSubidaPortada());

		Utilidades.cambiarVisibilidad(elementos, true);
	}

	/**
	 * Establece los atributos del cómic basándose en el objeto Comic proporcionado.
	 * 
	 * @param comicTemp El objeto Comic que contiene los datos a establecer.
	 */
	public void setAtributosDesdeTabla(Comic comicTemp) {
		getReferenciaVentana().getNombreComic().setText(comicTemp.getNombre());

		String numeroNuevo = comicTemp.getNumero();
		getReferenciaVentana().getNumeroComic().getSelectionModel().select(numeroNuevo);

		getReferenciaVentana().getVarianteComic().setText(comicTemp.getVariante());

		getReferenciaVentana().getFirmaComic().setText(comicTemp.getFirma());

		getReferenciaVentana().getEditorialComic().setText(comicTemp.getEditorial());

		String formato = comicTemp.getFormato();
		getReferenciaVentana().getFormatoComic().getSelectionModel().select(formato);

		String procedencia = comicTemp.getProcedencia();
		getReferenciaVentana().getProcedenciaComic().getSelectionModel().select(procedencia);

		String fechaString = comicTemp.getFecha();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate fecha = LocalDate.parse(fechaString, formatter);
		getReferenciaVentana().getFechaComic().setValue(fecha);

		getReferenciaVentana().getGuionistaComic().setText(comicTemp.getGuionista());

		getReferenciaVentana().getDibujanteComic().setText(comicTemp.getDibujante());

		String cajaAni = comicTemp.getValorGradeo();
		getReferenciaVentana().getGradeoComic().getSelectionModel().select(cajaAni);

		getReferenciaVentana().getNombreKeyIssue().setText(comicTemp.getkeyIssue());
		getReferenciaVentana().getEstadoComic().getSelectionModel().select(comicTemp.getEstado());

		getReferenciaVentana().getPrecioComic().setText(comicTemp.getprecioComic());
		getReferenciaVentana().getUrlReferencia().setText(comicTemp.getUrlReferencia());

		getReferenciaVentana().getDireccionImagen().setText(comicTemp.getImagen());

		getReferenciaVentana().getCodigoComicTratar().setText(comicTemp.getcodigoComic());

		getReferenciaVentana().getIdComicTratar().setText(comicTemp.getid());

		Utilidades.cargarImagenAsync(comicTemp.getImagen(), getReferenciaVentana().getImagencomic());
	}

	private static void rellenarDatos(Comic comic) {
		getReferenciaVentana().getNumeroComic().getSelectionModel().clearSelection();
		getReferenciaVentana().getFormatoComic().getSelectionModel().clearSelection();
		getReferenciaVentana().getGradeoComic().getSelectionModel().clearSelection();

		getReferenciaVentana().getNombreComic().setText(comic.getNombre());
		getReferenciaVentana().getNumeroComic().getSelectionModel().select(comic.getNumero());
		getReferenciaVentana().getVarianteComic().setText(comic.getVariante());
		getReferenciaVentana().getFirmaComic().setText(comic.getFirma());
		getReferenciaVentana().getEditorialComic().setText(comic.getEditorial());
		getReferenciaVentana().getFormatoComic().getSelectionModel().select(comic.getFormato());
		getReferenciaVentana().getProcedenciaComic().getSelectionModel().select(comic.getProcedencia());

		LocalDate fecha = LocalDate.parse(comic.getFecha(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		getReferenciaVentana().getFechaComic().setValue(fecha);

		getReferenciaVentana().getGuionistaComic().setText(comic.getGuionista());
		getReferenciaVentana().getDibujanteComic().setText(comic.getDibujante());
		getReferenciaVentana().getGradeoComic().getSelectionModel().select(comic.getValorGradeo());
		getReferenciaVentana().getNombreKeyIssue().setText(comic.getkeyIssue());
		getReferenciaVentana().getEstadoComic().getSelectionModel().select(comic.getEstado());
		getReferenciaVentana().getPrecioComic().setText(comic.getprecioComic());
		getReferenciaVentana().getUrlReferencia().setText(comic.getUrlReferencia());
		getReferenciaVentana().getDireccionImagen().setText(comic.getImagen());

		getReferenciaVentana().getProntInfo().clear();
		getReferenciaVentana().getProntInfo().setOpacity(1);

		Image imagenComic = Utilidades.devolverImagenComic(comic.getImagen());
		getReferenciaVentana().getImagencomic().setImage(imagenComic);
	}

	/**
	 * Actualiza los campos únicos del objeto Comic con los valores ingresados en
	 * los campos de la interfaz gráfica.
	 * 
	 * @param comic El objeto Comic a actualizar.
	 */
	public void actualizarCamposUnicos(Comic comic) {

		comic.setkeyIssue(!getReferenciaVentana().getNombreKeyIssue().getText().isEmpty()
				? Utilidades.eliminarEspacios(getReferenciaVentana().getNombreKeyIssue().getText())
				: (!getReferenciaVentana().getNombreKeyIssue().getText().trim().isEmpty() && Pattern.compile(".*\\w+.*")
						.matcher(getReferenciaVentana().getNombreKeyIssue().getText().trim()).matches()
								? getReferenciaVentana().getNombreKeyIssue().getText().trim()
								: "Vacio"));

		comic.seturlReferencia(!getReferenciaVentana().getUrlReferencia().getText().isEmpty()
				? Utilidades.eliminarEspacios(getReferenciaVentana().getUrlReferencia().getText())
				: (getReferenciaVentana().getUrlReferencia().getText().isEmpty() ? "Sin referencia"
						: getReferenciaVentana().getUrlReferencia().getText()));
		comic.setprecioComic(!getReferenciaVentana().getPrecioComic().getText().isEmpty()
				? Utilidades.eliminarEspacios(getReferenciaVentana().getPrecioComic().getText())
				: (getReferenciaVentana().getPrecioComic().getText().isEmpty() ? "0"
						: getReferenciaVentana().getPrecioComic().getText()));
		comic.setcodigoComic(Utilidades.eliminarEspacios(getReferenciaVentana().getCodigoComicTratar().getText()));

		comic.setValorGradeo(comic.getValorGradeo().isEmpty() ? "0" : comic.getValorGradeo());
	}

	public static void validarCamposClave(boolean esBorrado) {
		List<TextField> camposUi = Arrays.asList(getReferenciaVentana().getNombreComic(),
				getReferenciaVentana().getVarianteComic(), getReferenciaVentana().getEditorialComic(),
				getReferenciaVentana().getPrecioComic(), getReferenciaVentana().getGuionistaComic(),
				getReferenciaVentana().getDibujanteComic());

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
		List<Control> camposUi = Arrays.asList(getReferenciaVentana().getNombreComic(),
				getReferenciaVentana().getVarianteComic(), getReferenciaVentana().getEditorialComic(),
				getReferenciaVentana().getPrecioComic(), getReferenciaVentana().getCodigoComicTratar(),
				getReferenciaVentana().getGuionistaComic(), getReferenciaVentana().getDibujanteComic(),
				getReferenciaVentana().getFechaComic());

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

	public static void comprobarListaValidacion(Comic c) {
		if (c.getNombre() == null || c.getNombre().isEmpty() || c.getNombre().equalsIgnoreCase("vacio")
				|| c.getNumero() == null || c.getNumero().isEmpty() || c.getNumero().equalsIgnoreCase("vacio")
				|| c.getVariante() == null || c.getVariante().isEmpty() || c.getVariante().equalsIgnoreCase("vacio")
				|| c.getEditorial() == null || c.getEditorial().isEmpty() || c.getEditorial().equalsIgnoreCase("vacio")
				|| c.getFormato() == null || c.getFormato().isEmpty() || c.getFormato().equalsIgnoreCase("vacio")
				|| c.getProcedencia() == null || c.getProcedencia().isEmpty()
				|| c.getProcedencia().equalsIgnoreCase("vacio") || c.getFecha() == null || c.getFecha().isEmpty()
				|| c.getGuionista() == null || c.getGuionista().isEmpty() || c.getGuionista().equalsIgnoreCase("vacio")
				|| c.getDibujante() == null || c.getDibujante().isEmpty() || c.getDibujante().equalsIgnoreCase("vacio")
				|| c.getEstado() == null || c.getEstado().isEmpty() || c.getEstado().equalsIgnoreCase("vacio")
				|| c.getValorGradeo() == null || c.getValorGradeo().isEmpty()
				|| c.getValorGradeo().equalsIgnoreCase("vacio") || c.getUrlReferencia() == null
				|| c.getUrlReferencia().isEmpty() || c.getUrlReferencia().equalsIgnoreCase("vacio")
				|| c.getprecioComic() == null || c.getprecioComic().isEmpty()
				|| c.getprecioComic().equalsIgnoreCase("vacio") || c.getcodigoComic() == null) {

			String mensajePront = "Revisa la lista, algunos comics estan mal rellenados.";
			AlarmaList.mostrarMensajePront(mensajePront, false, getReferenciaVentana().getProntInfo());

			return;
		}
	}

	/**
	 * Borra los datos del cómic
	 */
	public static void limpiarAutorellenos(boolean esPrincipal) {

		if (esPrincipal) {
			return;
		}

		getReferenciaVentana().getNombreComic().setText("");
		getReferenciaVentana().getNumeroComic().setValue("");
		getReferenciaVentana().getNumeroComic().getEditor().setText("");

		getReferenciaVentana().getVarianteComic().setText("");
		getReferenciaVentana().getFirmaComic().setText("");
		getReferenciaVentana().getEditorialComic().setText("");

		getReferenciaVentana().getFormatoComic().setValue("");
		getReferenciaVentana().getFormatoComic().getEditor().setText("");

		getReferenciaVentana().getProcedenciaComic().setValue("");
		getReferenciaVentana().getProcedenciaComic().getEditor().setText("");

		getReferenciaVentana().getFechaComic().setValue(null);
		getReferenciaVentana().getGuionistaComic().setText("");
		getReferenciaVentana().getDibujanteComic().setText("");
		getReferenciaVentana().getNombreKeyIssue().setText("");
		getReferenciaVentana().getFechaComic().setValue(null);

		getReferenciaVentana().getPrecioComic().setText("");
		getReferenciaVentana().getBusquedaCodigo().setText("");
		getReferenciaVentana().getCodigoComicTratar().setText("");
		getReferenciaVentana().getUrlReferencia().setText("");
		getReferenciaVentana().getGradeoComic().setValue("");
		getReferenciaVentana().getGradeoComic().getEditor().setText("");

		getReferenciaVentana().getNombreKeyIssue().setText("");
		getReferenciaVentana().getDireccionImagen().setText("");
		getReferenciaVentana().getImagencomic().setImage(null);
		getReferenciaVentana().getCodigoComicTratar().setText("");
		getReferenciaVentana().getIdComicTratar().setText("");

		if ("aniadir".equals(AccionFuncionesComunes.TIPO_ACCION)) {
			getReferenciaVentana().getIdComicTratar().setDisable(false);
			getReferenciaVentana().getIdComicTratar().setText("");
			getReferenciaVentana().getIdComicTratar().setDisable(true);
		}

		getReferenciaVentana().getFormatoComic().getSelectionModel().selectFirst();
		getReferenciaVentana().getProcedenciaComic().getSelectionModel().selectFirst();
		getReferenciaVentana().getEstadoComic().getSelectionModel().selectFirst();

		getReferenciaVentana().getProntInfo().setText(null);
		getReferenciaVentana().getProntInfo().setOpacity(0);
		getReferenciaVentana().getProntInfo().setStyle("");
		validarCamposClave(true);
	}

	public static void borrarDatosGraficos() {
		getReferenciaVentana().getProntInfo().setText(null);
		getReferenciaVentana().getProntInfo().setOpacity(0);
		getReferenciaVentana().getProntInfo().setStyle("");
	}

	/**
	 * Asigna tooltips a varios elementos en la interfaz gráfica. Estos tooltips
	 * proporcionan información adicional cuando el usuario pasa el ratón sobre los
	 * elementos.
	 */
	public static void establecerTooltips() {
		Platform.runLater(() -> {
			Map<Node, String> tooltipsMap = new HashMap<>();

			tooltipsMap.put(getReferenciaVentana().getNombreComic(), "Nombre de los cómics / libros / mangas");
			tooltipsMap.put(getReferenciaVentana().getNumeroComic(), "Número del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getVarianteComic(),
					"Nombre de la variante del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getBotonLimpiar(),
					"Limpia la pantalla y reinicia todos los valores");
			tooltipsMap.put(getReferenciaVentana().getBotonbbdd(), "Botón para acceder a la base de datos");
			tooltipsMap.put(getReferenciaVentana().getBotonSubidaPortada(), "Botón para subir una portada");
			tooltipsMap.put(getReferenciaVentana().getBotonEliminar(), "Botón para eliminar un cómic");
			tooltipsMap.put(getReferenciaVentana().getBotonVender(), "Botón para vender un cómic");
			tooltipsMap.put(getReferenciaVentana().getBotonParametroComic(),
					"Botón para buscar un cómic mediante una lista de parámetros");
			tooltipsMap.put(getReferenciaVentana().getBotonModificarComic(), "Botón para modificar un cómic");
			tooltipsMap.put(getReferenciaVentana().getBotonBorrarOpinion(), "Botón para borrar una opinión");
			tooltipsMap.put(getReferenciaVentana().getBotonAgregarPuntuacion(), "Botón para agregar una puntuación");
			tooltipsMap.put(getReferenciaVentana().getPuntuacionMenu(), "Selecciona una puntuación en el menú");

			tooltipsMap.put(getReferenciaVentana().getNombreFirma(), "Nombre de la firma del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getNombreGuionista(),
					"Nombre del guionista del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getNombreProcedencia(),
					"Nombre de la procedencia del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getNombreFormato(), "Nombre del formato del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getNombreEditorial(),
					"Nombre de la editorial del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getNombreDibujante(),
					"Nombre del dibujante del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getFechaPublicacion(), "Fecha del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getBusquedaGeneral(),
					"Puedes buscar de forma general los cómic / libro / manga / artistas / guionistas");
			tooltipsMap.put(getReferenciaVentana().getNumeroCaja(), "Caja donde guardas el cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getBotonIntroducir(),
					"Realizar una acción de introducción del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getBotonModificar(),
					"Realizar una acción de modificación del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getBotonEliminar(),
					"Realizar una acción de eliminación del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getBotonAgregarPuntuacion(),
					"Abrir una ventana para agregar puntuación del cómic / libro / manga");
			tooltipsMap.put(getReferenciaVentana().getBotonMostrarParametro(),
					"Buscar por parámetros según los datos rellenados");

			FuncionesTooltips.assignTooltips(tooltipsMap);
		});
	}

	public static void listas_autocompletado() {
		if (ConectManager.conexionActiva()) {
			FuncionesManejoFront.asignarAutocompletado(getReferenciaVentana().getNombreComic(),
					ListaComicsDAO.listaNombre);
			FuncionesManejoFront.asignarAutocompletado(getReferenciaVentana().getVarianteComic(),
					ListaComicsDAO.listaVariante);
			FuncionesManejoFront.asignarAutocompletado(getReferenciaVentana().getFirmaComic(),
					ListaComicsDAO.listaFirma);
			FuncionesManejoFront.asignarAutocompletado(getReferenciaVentana().getEditorialComic(),
					ListaComicsDAO.listaEditorial);
			FuncionesManejoFront.asignarAutocompletado(getReferenciaVentana().getGuionistaComic(),
					ListaComicsDAO.listaGuionista);
			FuncionesManejoFront.asignarAutocompletado(getReferenciaVentana().getDibujanteComic(),
					ListaComicsDAO.listaDibujante);
			FuncionesManejoFront.asignarAutocompletado(getReferenciaVentana().getNumeroComic().getEditor(),
					ListaComicsDAO.listaNumeroComic);
		}
	}

	public static void controlarEventosInterfaz() {
		listaElementosVentana();

		getReferenciaVentana().getProntInfo().textProperty().addListener((observable, oldValue, newValue) -> {
			FuncionesTableView.ajustarAnchoVBox();
		});

		// Desactivar el enfoque en el VBox para evitar que reciba eventos de teclado
		getReferenciaVentana().getRootVBox().setFocusTraversable(false);

		// Agregar un filtro de eventos para capturar el enfoque en el TableView y
		// desactivar el enfoque en el VBox
		getReferenciaVentana().getTablaBBDD().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			getReferenciaVentana().getRootVBox().setFocusTraversable(false);
			getReferenciaVentana().getTablaBBDD().requestFocus();
		});

		getReferenciaVentana().getImagencomic().imageProperty().addListener((observable, oldImage, newImage) -> {
			if (newImage != null) {
				// Cambiar la apariencia del cursor y la opacidad cuando la imagen se ha cargado
				getReferenciaVentana().getImagencomic().setOnMouseEntered(e -> {
					getReferenciaVentana().getImagencomic().setOpacity(0.7); // Cambiar la opacidad para indicar que es
					// clickable
					getReferenciaVentana().getImagencomic().setCursor(Cursor.HAND);
				});

				// Restaurar el cursor y la opacidad al salir del ImageView
				getReferenciaVentana().getImagencomic().setOnMouseExited(e -> {
					getReferenciaVentana().getImagencomic().setOpacity(1.0); // Restaurar la opacidad
					getReferenciaVentana().getImagencomic().setCursor(Cursor.DEFAULT);
				});
			} else {
				// Restaurar el cursor y la opacidad al salir del ImageView
				getReferenciaVentana().getImagencomic().setOnMouseEntered(e -> {
					getReferenciaVentana().getImagencomic().setCursor(Cursor.DEFAULT);
				});
			}
		});

	}

	public static void controlarEventosInterfazAccion() {
		controlarEventosInterfaz();

		// Establecemos un evento para detectar cambios en el segundo TextField
		getReferenciaVentana().getIdComicTratar().textProperty().addListener((observable, oldValue, newValue) -> {
			AccionSeleccionar.mostrarComic(getReferenciaVentana().getIdComicTratar().getText(), false);
		});
	}

	public static void controlarEventosInterfazPrincipal(AccionReferencias referenciaVentana) {
		controlarEventosInterfaz();

		referenciaVentana.getTablaBBDD().getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {

					if (newSelection != null) {
						Comic idRow = referenciaVentana.getTablaBBDD().getSelectionModel().getSelectedItem();

						if (idRow != null) {
							referenciaVentana.getBotonGuardarResultado().setVisible(true);
							referenciaVentana.getBotonGuardarResultado().setDisable(false);
						}
					}
				});

		ListaComicsDAO.comicsGuardadosList.addListener((ListChangeListener.Change<? extends Comic> change) -> {
			while (change.next()) {
				if (!ListaComicsDAO.comicsGuardadosList.isEmpty()) {
					referenciaVentana.getBotonImprimir().setVisible(true);
					referenciaVentana.getBotonImprimir().setDisable(false);
				}
			}
		});

		// Establecer un Listener para el tamaño del AnchorPane
		referenciaVentana.getRootAnchorPane().widthProperty().addListener((observable, oldValue, newValue) -> {

			FuncionesTableView.ajustarAnchoVBox();
			FuncionesTableView.seleccionarRaw();
			FuncionesTableView.modificarColumnas();
		});

		referenciaVentana.getBotonGuardarResultado().setOnMousePressed(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				// Si la lista está vacía, oculta el botón
				referenciaVentana.getBotonMostrarGuardados().setVisible(true);
			}
		});
	}

	public static void listaElementosVentana() {

//		accionController.listaImagenes = FXCollections.observableArrayList(referenciaVentana.getImagencomic());
//
//		accionController.columnListCarga = Arrays.asList(referenciaVentana.getNombre(), referenciaVentana.getVariante(),
//				referenciaVentana.getEditorial(), referenciaVentana.getGuionista(), referenciaVentana.getDibujante());
//		accionController.listaBotones = FXCollections.observableArrayList(referenciaVentana.getBotonLimpiar(),
//				referenciaVentana.getBotonbbdd(), referenciaVentana.getBotonbbdd(),
//				referenciaVentana.getBotonParametroComic(), referenciaVentana.getBotonLimpiar(),
//				referenciaVentana.getBotonBusquedaAvanzada(), referenciaVentana.getBotonBusquedaCodigo());
//		VentanaAccionController.comboboxesMod = Arrays.asList(referenciaVentana.getFormatoComic(),
//				referenciaVentana.getProcedenciaComic(), referenciaVentana.getEstadoComic(),
//				referenciaVentana.getPuntuacionMenu());
//
//		VentanaAccionController.columnList = accionController.columnListCarga;
	}

	public static Comic camposComic(List<Control> camposComic, boolean esAccion) {
		Comic comic = new Comic();

		LocalDate fecha = getReferenciaVentana().getFechaComic().getValue();
		String fechaComic = (fecha != null) ? fecha.toString() : "";

		List<String> valores = new ArrayList<>();

		for (Control control : camposComic) {
			if (control instanceof TextField) {
				valores.add(((TextField) control).getText());
			} else if (control instanceof ComboBox<?>) {
				Object selectedItem = ((ComboBox<?>) control).getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					valores.add(selectedItem.toString());
				} else {
					valores.add(""); // o algún valor predeterminado si deseas
				}
			}
		}

		// Asignar los valores a las variables correspondientes
		String nombreComic = valores.get(0);
		String numeroComic = valores.get(1);
		String varianteComic = valores.get(2);
		String firmaComic = valores.get(3);
		String editorialComic = valores.get(4);
		String formatoComic = valores.get(5);
		String procedenciaComic = valores.get(6);
		String guionistaComic = valores.get(7);
		String dibujanteComic = valores.get(8);
		String numeroCajaComic = valores.get(9);
		String direccionImagen = "";
		String estadoComic = "";
		String nombreKeyIssue = "";
		String precioComic = "";
		String urlReferencia = "";
		String codigoComicTratar = "";
		String idComicTratar_mod = "";

		if (esAccion) {
			direccionImagen = valores.get(10);
			estadoComic = valores.get(11);
			nombreKeyIssue = valores.get(12);
			precioComic = valores.get(13);
			urlReferencia = valores.get(14);
			codigoComicTratar = valores.get(15);
			idComicTratar_mod = valores.get(16);
		}

		comic.setNombre(Utilidades.defaultIfNullOrEmpty(nombreComic, ""));

		comic.setNumero(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(numeroComic), ""));
		comic.setVariante(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(varianteComic), ""));
		comic.setFirma(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(firmaComic), ""));
		comic.setEditorial(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(editorialComic), ""));
		comic.setFormato(Utilidades.defaultIfNullOrEmpty(formatoComic, ""));
		comic.setProcedencia(Utilidades.defaultIfNullOrEmpty(procedenciaComic, ""));
		comic.setFecha(fechaComic);
		comic.setGuionista(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(guionistaComic), ""));
		comic.setDibujante(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(dibujanteComic), ""));
		comic.setValorGradeo(Utilidades.defaultIfNullOrEmpty(numeroCajaComic, ""));
		comic.setImagen(Utilidades.defaultIfNullOrEmpty(direccionImagen, ""));
		comic.setEstado(Utilidades.defaultIfNullOrEmpty(estadoComic, ""));
		comic.setkeyIssue(Utilidades.defaultIfNullOrEmpty(nombreKeyIssue, ""));
		comic.setprecioComic(Utilidades.defaultIfNullOrEmpty(precioComic, ""));
		comic.seturlReferencia(Utilidades.defaultIfNullOrEmpty(urlReferencia, ""));

		comic.setcodigoComic(Utilidades.eliminarEspacios(codigoComicTratar));
		comic.setID(Utilidades.defaultIfNullOrEmpty(idComicTratar_mod, ""));

		return comic;
	}

	public static List<String> comprobarYDevolverLista(List<ComboBox<String>> comboBoxes,
			ObservableList<Control> observableList) {
		List<String> valores = new ArrayList<>();
		for (ComboBox<String> comboBox : comboBoxes) {
			valores.add(comboBox.getValue() != null ? comboBox.getValue() : "");
		}
		if (contieneNulo(comboBoxes)) {
			return Arrays.asList(observableList.stream()
					.map(control -> control instanceof TextInputControl ? ((TextInputControl) control).getText() : "")
					.toArray(String[]::new));
		} else {
			return valores;
		}
	}

	private static <T> boolean contieneNulo(List<T> lista) {

		if (lista == null) {
			return false;
		}

		for (T elemento : lista) {
			if (elemento == null) {
				return true;
			}
		}
		return false;
	}

	public static Comic comicModificado() {

		String id_comic = getReferenciaVentana().getIdComicTratar().getText();

		Comic comicTemp = ComicManagerDAO.comicDatos(id_comic);

		Comic datos = camposComic(getReferenciaVentana().getListaTextFields(), true);

		Comic comicModificado = new Comic();

		comicModificado.setNombre(Utilidades.defaultIfNullOrEmpty(datos.getNombre(), comicTemp.getNombre()));
		comicModificado.setNumero(Utilidades.defaultIfNullOrEmpty(datos.getNumero(), comicTemp.getNumero()));
		comicModificado.setVariante(Utilidades.defaultIfNullOrEmpty(datos.getVariante(), comicTemp.getVariante()));
		comicModificado.setFirma(Utilidades.defaultIfNullOrEmpty(datos.getFirma(), comicTemp.getFirma()));
		comicModificado.setEditorial(Utilidades.defaultIfNullOrEmpty(datos.getEditorial(), comicTemp.getEditorial()));
		comicModificado.setFormato(Utilidades.defaultIfNullOrEmpty(datos.getFormato(), comicTemp.getFormato()));
		comicModificado
				.setProcedencia(Utilidades.defaultIfNullOrEmpty(datos.getProcedencia(), comicTemp.getProcedencia()));
		comicModificado.setFecha(Utilidades.defaultIfNullOrEmpty(datos.getFecha(), comicTemp.getFecha()));
		comicModificado.setGuionista(Utilidades.defaultIfNullOrEmpty(datos.getGuionista(), comicTemp.getGuionista()));
		comicModificado.setDibujante(Utilidades.defaultIfNullOrEmpty(datos.getDibujante(), comicTemp.getDibujante()));
		comicModificado.setImagen(Utilidades.defaultIfNullOrEmpty(datos.getImagen(), comicTemp.getImagen()));
		comicModificado.setEstado(Utilidades.defaultIfNullOrEmpty(datos.getEstado(), comicTemp.getEstado()));
		comicModificado
				.setValorGradeo(Utilidades.defaultIfNullOrEmpty(datos.getValorGradeo(), comicTemp.getValorGradeo()));
		comicModificado.setPuntuacion(
				comicTemp.getPuntuacion().equals("Sin puntuar") ? "Sin puntuar" : comicTemp.getPuntuacion());

		String key_issue_sinEspacios = datos.getkeyIssue().trim();

		if (!key_issue_sinEspacios.isEmpty() && key_issue_sinEspacios.matches(".*\\w+.*")) {
			comicModificado.setkeyIssue(key_issue_sinEspacios);
		} else if (comicTemp != null && comicTemp.getkeyIssue() != null && !comicTemp.getkeyIssue().isEmpty()) {
			comicModificado.setkeyIssue(comicTemp.getkeyIssue());
		}

		String urlReferencia = Utilidades.defaultIfNullOrEmpty(datos.getUrlReferencia(), "");
		comicModificado.seturlReferencia(urlReferencia.isEmpty() ? "Sin referencia" : urlReferencia);

		String precioComic = Utilidades.defaultIfNullOrEmpty(datos.getprecioComic(), "0");
		comicModificado.setprecioComic(
				String.valueOf(Utilidades.convertirMonedaADolar(comicModificado.getProcedencia(), precioComic)));

		comicModificado
				.setcodigoComic(Utilidades.defaultIfNullOrEmpty(datos.getcodigoComic(), comicTemp.getcodigoComic()));

		return comicModificado;
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionControlUI.referenciaVentana = referenciaVentana;
	}

}
