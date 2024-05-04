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

		referenciaVentana.getIdComicTratar().textProperty().addListener((observable, oldValue, newValue) -> {
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
			referenciaVentana.getNavegacion_cerrar().setDisable(true);
			referenciaVentana.getNavegacion_cerrar().setVisible(false);

			referenciaVentana.getIdComicTratar().setLayoutX(56);
			referenciaVentana.getIdComicTratar().setLayoutY(104);
			referenciaVentana.getLabel_id_mod().setLayoutX(3);
			referenciaVentana.getLabel_id_mod().setLayoutY(104);
		} else {
			referenciaVentana.getIdComicTratar().setEditable(false);
			referenciaVentana.getIdComicTratar().setOpacity(0.7);
		}
	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public static void ocultarCampos() {
		List<Node> elementos = Arrays.asList(referenciaVentana.getTablaBBDD(), referenciaVentana.getDibujanteComic(),
				referenciaVentana.getEditorialComic(), referenciaVentana.getEstadoComic(),
				referenciaVentana.getFechaComic(), referenciaVentana.getFirmaComic(),
				referenciaVentana.getFormatoComic(), referenciaVentana.getGuionistaComic(),
				referenciaVentana.getNombreKeyIssue(), referenciaVentana.getGradeoComic(),
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
	 * @param comicTemp El objeto Comic que contiene los datos a establecer.
	 */
	public void setAtributosDesdeTabla(Comic comicTemp) {
		referenciaVentana.getNombreComic().setText(comicTemp.getNombre());

		String numeroNuevo = comicTemp.getNumero();
		referenciaVentana.getNumeroComic().getSelectionModel().select(numeroNuevo);

		referenciaVentana.getVarianteComic().setText(comicTemp.getVariante());

		referenciaVentana.getFirmaComic().setText(comicTemp.getFirma());

		referenciaVentana.getEditorialComic().setText(comicTemp.getEditorial());

		String formato = comicTemp.getFormato();
		referenciaVentana.getFormatoComic().getSelectionModel().select(formato);

		String procedencia = comicTemp.getProcedencia();
		referenciaVentana.getProcedenciaComic().getSelectionModel().select(procedencia);

		String fechaString = comicTemp.getFecha();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate fecha = LocalDate.parse(fechaString, formatter);
		referenciaVentana.getFechaComic().setValue(fecha);

		referenciaVentana.getGuionistaComic().setText(comicTemp.getGuionista());

		referenciaVentana.getDibujanteComic().setText(comicTemp.getDibujante());

		String cajaAni = comicTemp.getValorGradeo();
		referenciaVentana.getGradeoComic().getSelectionModel().select(cajaAni);

		referenciaVentana.getNombreKeyIssue().setText(comicTemp.getkeyIssue());
		referenciaVentana.getEstadoComic().getSelectionModel().select(comicTemp.getEstado());

		referenciaVentana.getPrecioComic().setText(comicTemp.getprecioComic());
		referenciaVentana.getUrlReferencia().setText(comicTemp.getUrlReferencia());

		referenciaVentana.getDireccionImagen().setText(comicTemp.getImagen());

		referenciaVentana.getCodigoComicTratar().setText(comicTemp.getcodigoComic());

		referenciaVentana.getIdComicTratar().setText(comicTemp.getid());

		Utilidades.cargarImagenAsync(comicTemp.getImagen(), referenciaVentana.getImagencomic());
	}

	private static void rellenarDatos(Comic comic) {
		referenciaVentana.getNumeroComic().getSelectionModel().clearSelection();
		referenciaVentana.getFormatoComic().getSelectionModel().clearSelection();
		referenciaVentana.getGradeoComic().getSelectionModel().clearSelection();

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
		referenciaVentana.getGradeoComic().getSelectionModel().select(comic.getValorGradeo());
		referenciaVentana.getNombreKeyIssue().setText(comic.getkeyIssue());
		referenciaVentana.getEstadoComic().getSelectionModel().select(comic.getEstado());
		referenciaVentana.getPrecioComic().setText(comic.getprecioComic());
		referenciaVentana.getUrlReferencia().setText(comic.getUrlReferencia());
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

		comic.setkeyIssue(!referenciaVentana.getNombreKeyIssue().getText().isEmpty()
				? Utilidades.eliminarEspacios(referenciaVentana.getNombreKeyIssue().getText())
				: (!referenciaVentana.getNombreKeyIssue().getText().trim().isEmpty() && Pattern.compile(".*\\w+.*")
						.matcher(referenciaVentana.getNombreKeyIssue().getText().trim()).matches()
								? referenciaVentana.getNombreKeyIssue().getText().trim()
								: "Vacio"));

		comic.seturlReferencia(!referenciaVentana.getUrlReferencia().getText().isEmpty()
				? Utilidades.eliminarEspacios(referenciaVentana.getUrlReferencia().getText())
				: (referenciaVentana.getUrlReferencia().getText().isEmpty() ? "Sin referencia"
						: referenciaVentana.getUrlReferencia().getText()));
		comic.setprecioComic(!referenciaVentana.getPrecioComic().getText().isEmpty()
				? Utilidades.eliminarEspacios(referenciaVentana.getPrecioComic().getText())
				: (referenciaVentana.getPrecioComic().getText().isEmpty() ? "0"
						: referenciaVentana.getPrecioComic().getText()));
		comic.setcodigoComic(Utilidades.eliminarEspacios(referenciaVentana.getCodigoComicTratar().getText()));

		comic.setValorGradeo(comic.getValorGradeo().isEmpty() ? "0" : comic.getValorGradeo());
	}

	public static void validarCamposClave(boolean esBorrado) {
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
			AlarmaList.mostrarMensajePront(mensajePront, false, referenciaVentana.getProntInfo());

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
		referenciaVentana.getGradeoComic().setValue("");
		referenciaVentana.getGradeoComic().getEditor().setText("");

		referenciaVentana.getNombreKeyIssue().setText("");
		referenciaVentana.getDireccionImagen().setText("");
		referenciaVentana.getImagencomic().setImage(null);
		referenciaVentana.getCodigoComicTratar().setText("");
		referenciaVentana.getIdComicTratar().setText("");

		if ("aniadir".equals(AccionFuncionesComunes.TIPO_ACCION)) {
			referenciaVentana.getIdComicTratar().setDisable(false);
			referenciaVentana.getIdComicTratar().setText("");
			referenciaVentana.getIdComicTratar().setDisable(true);
		}

		referenciaVentana.getFormatoComic().getSelectionModel().selectFirst();
		referenciaVentana.getProcedenciaComic().getSelectionModel().selectFirst();
		referenciaVentana.getEstadoComic().getSelectionModel().selectFirst();

		referenciaVentana.getProntInfo().setText(null);
		referenciaVentana.getProntInfo().setOpacity(0);
		referenciaVentana.getProntInfo().setStyle("");
		validarCamposClave(true);
	}

	public static void borrarDatosGraficos() {
		referenciaVentana.getProntInfo().setText(null);
		referenciaVentana.getProntInfo().setOpacity(0);
		referenciaVentana.getProntInfo().setStyle("");
	}

	/**
	 * Asigna tooltips a varios elementos en la interfaz gráfica. Estos tooltips
	 * proporcionan información adicional cuando el usuario pasa el ratón sobre los
	 * elementos.
	 */
	public static void establecerTooltips() {
		Platform.runLater(() -> {
			Map<Node, String> tooltipsMap = new HashMap<>();

			tooltipsMap.put(referenciaVentana.getNombreComic(), "Nombre de los cómics / libros / mangas");
			tooltipsMap.put(referenciaVentana.getNumeroComic(), "Número del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getVarianteComic(), "Nombre de la variante del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonLimpiar(), "Limpia la pantalla y reinicia todos los valores");
			tooltipsMap.put(referenciaVentana.getBotonbbdd(), "Botón para acceder a la base de datos");
			tooltipsMap.put(referenciaVentana.getBotonSubidaPortada(), "Botón para subir una portada");
			tooltipsMap.put(referenciaVentana.getBotonEliminar(), "Botón para eliminar un cómic");
			tooltipsMap.put(referenciaVentana.getBotonVender(), "Botón para vender un cómic");
			tooltipsMap.put(referenciaVentana.getBotonParametroComic(),
					"Botón para buscar un cómic mediante una lista de parámetros");
			tooltipsMap.put(referenciaVentana.getBotonModificarComic(), "Botón para modificar un cómic");
			tooltipsMap.put(referenciaVentana.getBotonBorrarOpinion(), "Botón para borrar una opinión");
			tooltipsMap.put(referenciaVentana.getBotonAgregarPuntuacion(), "Botón para agregar una puntuación");
			tooltipsMap.put(referenciaVentana.getPuntuacionMenu(), "Selecciona una puntuación en el menú");

			tooltipsMap.put(referenciaVentana.getNombreFirma(), "Nombre de la firma del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreGuionista(), "Nombre del guionista del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreProcedencia(),
					"Nombre de la procedencia del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreFormato(), "Nombre del formato del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreEditorial(), "Nombre de la editorial del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreDibujante(), "Nombre del dibujante del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getFechaPublicacion(), "Fecha del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBusquedaGeneral(),
					"Puedes buscar de forma general los cómic / libro / manga / artistas / guionistas");
			tooltipsMap.put(referenciaVentana.getGradeoComic(), "Gradeo del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonIntroducir(),
					"Realizar una acción de introducción del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonModificar(),
					"Realizar una acción de modificación del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonEliminar(),
					"Realizar una acción de eliminación del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonAgregarPuntuacion(),
					"Abrir una ventana para agregar puntuación del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonMostrarParametro(),
					"Buscar por parámetros según los datos rellenados");

			FuncionesTooltips.assignTooltips(tooltipsMap);
		});
	}

	public static void listas_autocompletado() {
		if (ConectManager.conexionActiva()) {
			FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getNombreComic(), ListaComicsDAO.listaNombre);
			FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getVarianteComic(),
					ListaComicsDAO.listaVariante);
			FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getFirmaComic(), ListaComicsDAO.listaFirma);
			FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getEditorialComic(),
					ListaComicsDAO.listaEditorial);
			FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getGuionistaComic(),
					ListaComicsDAO.listaGuionista);
			FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getDibujanteComic(),
					ListaComicsDAO.listaDibujante);
			FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getNumeroComic().getEditor(),
					ListaComicsDAO.listaNumeroComic);
		}
	}

	public static void controlarEventosInterfaz() {

		referenciaVentana.getProntInfo().textProperty().addListener((observable, oldValue, newValue) -> {
			FuncionesTableView.ajustarAnchoVBox();
		});

		// Desactivar el enfoque en el VBox para evitar que reciba eventos de teclado
		referenciaVentana.getRootVBox().setFocusTraversable(false);

		// Agregar un filtro de eventos para capturar el enfoque en el TableView y
		// desactivar el enfoque en el VBox
		referenciaVentana.getTablaBBDD().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			referenciaVentana.getRootVBox().setFocusTraversable(false);
			referenciaVentana.getTablaBBDD().requestFocus();
		});

		referenciaVentana.getImagencomic().imageProperty().addListener((observable, oldImage, newImage) -> {
			if (newImage != null) {
				// Cambiar la apariencia del cursor y la opacidad cuando la imagen se ha cargado
				referenciaVentana.getImagencomic().setOnMouseEntered(e -> {
					referenciaVentana.getImagencomic().setOpacity(0.7); // Cambiar la opacidad para indicar que es
					// clickable
					referenciaVentana.getImagencomic().setCursor(Cursor.HAND);
				});

				// Restaurar el cursor y la opacidad al salir del ImageView
				referenciaVentana.getImagencomic().setOnMouseExited(e -> {
					referenciaVentana.getImagencomic().setOpacity(1.0); // Restaurar la opacidad
					referenciaVentana.getImagencomic().setCursor(Cursor.DEFAULT);
				});
			} else {
				// Restaurar el cursor y la opacidad al salir del ImageView
				referenciaVentana.getImagencomic().setOnMouseEntered(e -> {
					referenciaVentana.getImagencomic().setCursor(Cursor.DEFAULT);
				});
			}
		});

	}

	public static void controlarEventosInterfazAccion() {
		controlarEventosInterfaz();

		// Establecemos un evento para detectar cambios en el segundo TextField
		referenciaVentana.getIdComicTratar().textProperty().addListener((observable, oldValue, newValue) -> {
			AccionSeleccionar.mostrarComic(referenciaVentana.getIdComicTratar().getText(), false);
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

	public static Comic camposComic(List<String> camposComic, boolean esAccion) {
		Comic comic = new Comic();

		// Asignar los valores a las variables correspondientes
		String nombreComic = camposComic.get(0);
		String numeroComic = camposComic.get(1);
		String varianteComic = camposComic.get(2);
		String procedenciaComic = camposComic.get(3);
		String formatoComic = camposComic.get(4);
		String dibujanteComic = camposComic.get(5);
		String guionistaComic = camposComic.get(6);
		String editorialComic = camposComic.get(7);
		String firmaComic = camposComic.get(8);
		String valorGradeoComic = camposComic.get(9);
		LocalDate fecha = referenciaVentana.getFechaComic().getValue();
		String fechaComic = (fecha != null) ? fecha.toString() : "";
		String direccionImagen = "";
		String estadoComic = "";
		String nombreKeyIssue = "";
		String precioComic = "";
		String urlReferencia = "";
		String codigoComicTratar = "";
		String idComicTratar = "";

		if (esAccion) {
			direccionImagen = camposComic.get(10);
			estadoComic = camposComic.get(11);
			nombreKeyIssue = camposComic.get(12);
			precioComic = camposComic.get(13);
			urlReferencia = camposComic.get(14);
			codigoComicTratar = camposComic.get(15);
			idComicTratar = camposComic.get(16);
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
		comic.setValorGradeo(Utilidades.defaultIfNullOrEmpty(valorGradeoComic, ""));
		comic.setImagen(Utilidades.defaultIfNullOrEmpty(direccionImagen, ""));
		comic.setEstado(Utilidades.defaultIfNullOrEmpty(estadoComic, ""));
		comic.setkeyIssue(Utilidades.defaultIfNullOrEmpty(nombreKeyIssue, ""));
		comic.setprecioComic(Utilidades.defaultIfNullOrEmpty(precioComic, ""));
		comic.seturlReferencia(Utilidades.defaultIfNullOrEmpty(urlReferencia, ""));

		comic.setcodigoComic(Utilidades.eliminarEspacios(codigoComicTratar));
		comic.setID(Utilidades.defaultIfNullOrEmpty(idComicTratar, ""));

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

		String id_comic = referenciaVentana.getIdComicTratar().getText();

		Comic comicTemp = ComicManagerDAO.comicDatos(id_comic);

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

		Comic datos = camposComic(controls, true);

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
