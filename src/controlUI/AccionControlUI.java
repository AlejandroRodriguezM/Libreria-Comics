package controlUI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import Controladores.VentanaAccionController;
import Controladores.managment.AccionAniadir;
import Controladores.managment.AccionEliminar;
import Controladores.managment.AccionFuncionesComunes;
import Controladores.managment.AccionModificar;
import Controladores.managment.AccionReferencias;
import Controladores.managment.AccionSeleccionar;
import Funcionamiento.Utilidades;
import alarmas.AlarmaList;
import comicManagement.Comic;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.ListaComicsDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class AccionControlUI {

	public static AccionReferencias referenciaVentana = new AccionReferencias();

	private static VentanaAccionController accionController = new VentanaAccionController();

	private static AccionAniadir accionAniadir = new AccionAniadir();

	private static AccionEliminar accionEliminar = new AccionEliminar();

	private static AccionModificar accionModificar = new AccionModificar();

	public static void autoRelleno() {

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

	public static boolean rellenarCampos(String idComic) {
		Comic comic_temp = Comic.obtenerComic(idComic);
		if (comic_temp != null) {
			rellenarDatos(comic_temp);
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
	public static void ocultarCampos() {
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

	private static void rellenarDatos(Comic comic) {
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
				|| c.getNumCaja() == null || c.getNumCaja().isEmpty() || c.getNumCaja().equalsIgnoreCase("vacio")
				|| c.getUrl_referencia() == null || c.getUrl_referencia().isEmpty()
				|| c.getUrl_referencia().equalsIgnoreCase("vacio") || c.getPrecio_comic() == null
				|| c.getPrecio_comic().isEmpty() || c.getPrecio_comic().equalsIgnoreCase("vacio")
				|| c.getCodigo_comic() == null) {

			String mensajePront = "Revisa la lista, algunos comics estan mal rellenados.";
			AlarmaList.mostrarMensajePront(mensajePront, false, referenciaVentana.getProntInfo());

			return;
		}
	}

	/**
	 * Borra los datos del cómic
	 */
	public static void limpiarAutorellenos() {
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
		validarCamposClave(true);
	}

	public static void borrarDatosGraficos() {
		referenciaVentana.getProntInfo().setText(null);
		referenciaVentana.getProntInfo().setOpacity(0);
		referenciaVentana.getProntInfo().setStyle("");
		referenciaVentana.getTablaBBDD().getItems().clear();
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
			tooltipsMap.put(referenciaVentana.getNumeroCaja(), "Caja donde guardas el cómic / libro / manga");
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
		listaElementosVentana();

		referenciaVentana.getProntInfo().textProperty().addListener((observable, oldValue, newValue) -> {
			FuncionesTableView.ajustarAnchoVBox(referenciaVentana.getProntInfo(), referenciaVentana.getVboxContenido());
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
		referenciaVentana.getIdComicTratar_mod().textProperty().addListener((observable, oldValue, newValue) -> {
			AccionSeleccionar.mostrarComic(referenciaVentana.getIdComicTratar_mod().getText(), false);
		});
	}

	public static void controlarEventosInterfazPrincipal() {
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
				if (ListaComicsDAO.comicsGuardadosList.size() > 0) {
					referenciaVentana.getBotonImprimir().setVisible(true);
					referenciaVentana.getBotonImprimir().setDisable(false);
				}
			}
		});

		// Establecer un Listener para el tamaño del AnchorPane
		referenciaVentana.getRootAnchorPane().widthProperty().addListener((observable, oldValue, newValue) -> {

//			principalController.establecerDinamismoAnchor();

			FuncionesTableView.ajustarAnchoVBox(referenciaVentana.getProntInfo(), referenciaVentana.getVboxContenido());
			FuncionesTableView.seleccionarRaw(referenciaVentana.getTablaBBDD());
			FuncionesTableView.modificarColumnas(referenciaVentana.getTablaBBDD());
		});

		referenciaVentana.getBotonGuardarResultado().setOnMousePressed(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				// Si la lista está vacía, oculta el botón
				referenciaVentana.getBotonMostrarGuardados().setVisible(true);
			}
		});
	}

	public static void listaElementosVentana() {

		accionController.listaImagenes = FXCollections.observableArrayList(referenciaVentana.getImagencomic());

		accionController.columnListCarga = Arrays.asList(referenciaVentana.getNombre(), referenciaVentana.getVariante(),
				referenciaVentana.getEditorial(), referenciaVentana.getGuionista(), referenciaVentana.getDibujante());
		accionController.listaBotones = FXCollections.observableArrayList(referenciaVentana.getBotonLimpiar(),
				referenciaVentana.getBotonbbdd(), referenciaVentana.getBotonbbdd(),
				referenciaVentana.getBotonParametroComic(), referenciaVentana.getBotonLimpiar(),
				referenciaVentana.getBotonBusquedaAvanzada(), referenciaVentana.getBotonBusquedaCodigo());
		VentanaAccionController.comboboxesMod = Arrays.asList(referenciaVentana.getFormatoComic(),
				referenciaVentana.getProcedenciaComic(), referenciaVentana.getEstadoComic(),
				referenciaVentana.getPuntuacionMenu());

		VentanaAccionController.columnList = accionController.columnListCarga;
	}

	public static Comic camposComic() {
		Comic comic = new Comic();

		LocalDate fecha = referenciaVentana.getFechaComic().getValue();
		String fechaComic = (fecha != null) ? fecha.toString() : "";

		comic.setNombre(Utilidades.defaultIfNullOrEmpty(
				Utilidades.comaYGuionPorEspaciado(referenciaVentana.getNombreComic().getText()), ""));
		comic.setNumero(Utilidades.defaultIfNullOrEmpty(
				Utilidades.comaYGuionPorEspaciado(FuncionesComboBox.numeroCombobox(referenciaVentana.getNumeroComic())),
				""));
		comic.setVariante(Utilidades.defaultIfNullOrEmpty(
				Utilidades.comaYGuionPorEspaciado(referenciaVentana.getVarianteComic().getText()), ""));
		comic.setFirma(Utilidades.defaultIfNullOrEmpty(
				Utilidades.comaYGuionPorEspaciado(referenciaVentana.getFirmaComic().getText()), ""));
		comic.setEditorial(Utilidades.defaultIfNullOrEmpty(
				Utilidades.comaYGuionPorEspaciado(referenciaVentana.getEditorialComic().getText()), ""));
		comic.setFormato(Utilidades
				.defaultIfNullOrEmpty(FuncionesComboBox.formatoCombobox(referenciaVentana.getFormatoComic()), ""));
		comic.setProcedencia(Utilidades.defaultIfNullOrEmpty(
				FuncionesComboBox.procedenciaCombobox(referenciaVentana.getProcedenciaComic()), ""));
		comic.setFecha(fechaComic);
		comic.setGuionista(Utilidades.defaultIfNullOrEmpty(
				Utilidades.comaYGuionPorEspaciado(referenciaVentana.getGuionistaComic().getText()), ""));
		comic.setDibujante(Utilidades.defaultIfNullOrEmpty(
				Utilidades.comaYGuionPorEspaciado(referenciaVentana.getDibujanteComic().getText()), ""));
		comic.setImagen(Utilidades.defaultIfNullOrEmpty(referenciaVentana.getDireccionImagen().getText(), ""));
		comic.setEstado(Utilidades
				.defaultIfNullOrEmpty(FuncionesComboBox.estadoCombobox(referenciaVentana.getEstadoComic()), ""));
		comic.setNumCaja(Utilidades
				.defaultIfNullOrEmpty(FuncionesComboBox.cajaCombobox(referenciaVentana.getNumeroCajaComic()), ""));
		comic.setKey_issue(Utilidades.defaultIfNullOrEmpty(referenciaVentana.getNombreKeyIssue().getText().trim(), ""));
		comic.setUrl_referencia(
				(Utilidades.defaultIfNullOrEmpty(referenciaVentana.getUrlReferencia().getText().trim(), "")));
		comic.setPrecio_comic(
				(Utilidades.defaultIfNullOrEmpty(referenciaVentana.getPrecioComic().getText().trim(), "")));
		comic.setCodigo_comic(Utilidades.eliminarEspacios(referenciaVentana.getCodigoComicTratar().getText()));
		comic.setID(Utilidades.defaultIfNullOrEmpty(referenciaVentana.getIdComicTratar_mod().getText().trim(), ""));

		return comic;
	}

	public static Comic comicModificado() {

		String id_comic = referenciaVentana.getIdComicTratar_mod().getText();

		Comic comic_temp = ComicManagerDAO.comicDatos(id_comic);

		Comic datos = camposComic();

		Comic comicModificado = new Comic();

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

		return comicModificado;
	}

}
