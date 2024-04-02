package Controladores.managment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONException;

import Apis.ApiISBNGeneral;
import Apis.ApiMarvel;
import Controladores.CargaComicsController;
import Funcionamiento.Utilidades;
import Funcionamiento.Ventanas;
import alarmas.AlarmaList;
import comicManagement.Comic;
import controlUI.AccionControlUI;
import controlUI.FuncionesComboBox;
import controlUI.FuncionesTableView;
import dbmanager.ComicManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.ListaComicsDAO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import webScrap.WebScrapGoogle;
import webScrap.WebScraperPreviewsWorld;

public class AccionFuncionesComunes {

	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private final static String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private final static String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	private final static String CARPETA_RAIZ_PORTADAS = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
			+ ConectManager.DB_NAME + File.separator;

	/**
	 * Construimos la ruta al directorio "libreria_comics" dentro de "Documents" y
	 * añadimos el nombre de la base de datos y la carpeta "portadas".
	 */
	public final static String SOURCE_PATH = DOCUMENTS_PATH + File.separator + "libreria_comics" + File.separator
			+ ConectManager.DB_NAME + File.separator + "portadas";

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	/**
	 * Tipo de acción a realizar en la interfaz.
	 */
	public static String TIPO_ACCION;

	public static AccionReferencias referenciaVentana = new AccionReferencias();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

	/**
	 * Procesa la información de un cómic, ya sea para realizar una modificación o
	 * una inserción en la base de datos.
	 *
	 * @param comic          El cómic con la información a procesar.
	 * @param esModificacion Indica si se está realizando una modificación (true) o
	 *                       una inserción (false).
	 * @throws Exception
	 */
	public void procesarComic(Comic comic, boolean esModificacion) throws Exception {
		final List<Comic> listaComics; // Declarar listaComics como final
		final List<Comic> comicsFinal;
		if (!ConectManager.conexionActiva()) {
			return;
		}

		referenciaVentana.getProntInfo().setOpacity(1);
		if (!accionRellenoDatos.camposComicSonValidos()) {
			String mensaje = "Error. Debes de introducir los datos correctos";
			AlarmaList.mostrarMensajePront(mensaje, false, referenciaVentana.getProntInfo());
			comicsFinal = listaComics = ListaComicsDAO.comicsImportados;
			Platform.runLater(() -> {
				FuncionesTableView.tablaBBDD(comicsFinal, referenciaVentana.getTablaBBDD());
			});

			return; // Agregar return para salir del método en este punto
		}

		String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);
		String mensaje = "";
		Utilidades.redimensionarYGuardarImagen(comic.getImagen(), codigo_imagen);

		comic.setImagen(SOURCE_PATH + File.separator + codigo_imagen + ".jpg");
		if (esModificacion) {
			comic.setID(referenciaVentana.getIdComicTratar_mod().getText());
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
			listaComics = ComicManagerDAO.verLibreria(sentenciaSQL);
			ComicManagerDAO.actualizarComicBBDD(comic, "modificar");
			mensaje = "Has modificado correctamente el cómic";
		} else {
			ComicManagerDAO.insertarDatos(comic, true);
			mensaje = " Has introducido correctamente el cómic";
			Comic newSelection = referenciaVentana.getTablaBBDD().getSelectionModel().getSelectedItem();

			if (newSelection != null) {
				listaComics = ListaComicsDAO.comicsImportados;
				String id_comic = newSelection.getID();
				ListaComicsDAO.comicsImportados.removeIf(c -> c.getID().equals(id_comic));
				referenciaVentana.getTablaBBDD().getItems().clear();
			} else {
				listaComics = null; // Inicializar listaComics en caso de que no haya ningún cómic seleccionado
			}
		}

		comicsFinal = listaComics; // Declarar otra variable final para listaComics

		Platform.runLater(() -> {
			FuncionesTableView.tablaBBDD(comicsFinal, referenciaVentana.getTablaBBDD());
		});

		referenciaVentana.getTablaBBDD().refresh();
		AlarmaList.mostrarMensajePront(mensaje, esModificacion, referenciaVentana.getProntInfo());
		procesarBloqueComun(comic);
	}

	/**
	 * Procesa el bloque común utilizado en la función procesarComic para actualizar
	 * la interfaz gráfica y realizar operaciones relacionadas con la manipulación
	 * de imágenes y la actualización de listas y combos.
	 *
	 * @param comic El objeto Comic que se está procesando.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	private void procesarBloqueComun(Comic comic) throws SQLException {
		File file = new File(comic.getImagen());
		Image imagen = new Image(file.toURI().toString(), 250, 0, true, true);
		referenciaVentana.getImagencomic().setImage(imagen);
		referenciaVentana.getImagencomic().setImage(imagen);

		List<ComboBox<String>> comboboxes = AccionReferencias.getComboboxes();

		ListaComicsDAO.listasAutoCompletado();
		FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD());
		FuncionesTableView.actualizarBusquedaRaw(referenciaVentana.getTablaBBDD());
		funcionesCombo.rellenarComboBox(comboboxes);
	}

	public static boolean procesarComicPorCodigo(String finalValorCodigo) {

		if (!ConectManager.conexionActiva()) {
			return false;
		}

		Comic comicInfo = obtenerComicInfo(finalValorCodigo);

		if (comprobarCodigo(comicInfo)) {
			rellenarTablaImport(comicInfo);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Funcion que escribe en el TextField de "Direccion de imagen" la dirrecion de
	 * la imagen
	 */
	public void subirPortada() {
		File file = Utilidades.tratarFichero().showOpenDialog(null); // Llamada a funcion
		if (file != null) {
			String nuevoNombreArchivo = Utilidades.generarCodigoUnico(CARPETA_RAIZ_PORTADAS);

			try {
				Utilidades.redimensionarYGuardarImagen(file.getAbsolutePath().toString(), nuevoNombreArchivo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			referenciaVentana.getDireccionImagen()
					.setText(CARPETA_RAIZ_PORTADAS + "portadas" + File.separator + nuevoNombreArchivo + ".jpg");

			String mensaje = "Portada subida correctamente.";

			AlarmaList.mostrarMensajePront(mensaje, true, referenciaVentana.getProntInfo());

		} else {
			String mensaje = "Has cancelado la subida de portada.";

			AlarmaList.mostrarMensajePront(mensaje, false, referenciaVentana.getProntInfo());
		}
	}

	/**
	 * Limpia y restablece todos los campos de datos en la sección de animaciones a
	 * sus valores predeterminados. Además, restablece la imagen de fondo y oculta
	 * cualquier mensaje de error o información.
	 */
	public void limpiarDatosPantallaAccion() {
		// Restablecer los campos de datos

		if (ListaComicsDAO.comicsImportados.size() > 0) {
			if (nav.alertaBorradoLista()) {
				referenciaVentana.getBotonGuardarComic().setVisible(false);
				referenciaVentana.getBotonEliminarImportadoComic().setVisible(false);

				ListaComicsDAO.comicsImportados.clear();
				referenciaVentana.getTablaBBDD().getItems().clear();
			}
		}

		referenciaVentana.getNombreComic().setText("");
		referenciaVentana.getVarianteComic().setText("");
		referenciaVentana.getFirmaComic().setText("");
		referenciaVentana.getEditorialComic().setText("");
		referenciaVentana.getFechaComic().setValue(null);
		referenciaVentana.getGuionistaComic().setText("");
		referenciaVentana.getDibujanteComic().setText("");
		referenciaVentana.getProntInfo().setText(null);
		referenciaVentana.getProntInfo().setOpacity(0);
		referenciaVentana.getNombreKeyIssue().setText("");
		referenciaVentana.getNumeroComic().getEditor().clear(); // Limpiar el texto en el ComboBox
		referenciaVentana.getFormatoComic().getEditor().clear(); // Limpiar el texto en el ComboBox
		referenciaVentana.getProcedenciaComic().getEditor().clear(); // Limpiar el texto en el ComboBox
		referenciaVentana.getEstadoComic().getEditor().clear(); // Limpiar el texto en el ComboBox
		referenciaVentana.getUrlReferencia().setText("");
		referenciaVentana.getPrecioComic().setText("");
		referenciaVentana.getDireccionImagen().setText("");
		referenciaVentana.getImagencomic().setImage(null);
		referenciaVentana.getNumeroCajaComic().getEditor().clear();
		referenciaVentana.getCodigoComicTratar().setText("");
		referenciaVentana.getIdComicTratar_mod().setText("");
		if ("modificar".equals(TIPO_ACCION)) {
			AccionControlUI.mostrarOpcion(TIPO_ACCION);
		}
		// Borrar cualquier mensaje de error presente
		borrarErrores();
		AccionControlUI.validarCamposClave(true);
		AccionControlUI.borrarDatosGraficos();
	}

	/**
	 * Comprueba la existencia de un cómic en la base de datos y realiza acciones
	 * dependiendo del resultado.
	 *
	 * @param ID El identificador del cómic a verificar.
	 * @return true si el cómic existe en la base de datos y se realizan las
	 *         acciones correspondientes, false de lo contrario.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	public boolean comprobarExistenciaComic(String ID) {

		// Verifica si la conexión está activa
		if (!ConectManager.conexionActiva()) {
			return false;
		}

		// Si el cómic existe en la base de datos
		if (ComicManagerDAO.comprobarIdentificadorComic(ID)) {
			FuncionesTableView.actualizarBusquedaRaw(referenciaVentana.getTablaBBDD());
			return true;
		} else { // Si el cómic no existe en la base de datos
			String mensaje = "ERROR. ID desconocido.";
			AlarmaList.mostrarMensajePront(mensaje, false, referenciaVentana.getProntInfo());
			return false;
		}
	}

	/**
	 * Verifica si se ha encontrado información válida para el cómic.
	 *
	 * @param comicInfo Un objeto Comic con la información del cómic.
	 * @return True si la información es válida y existe; de lo contrario, False.
	 */
	private static boolean comprobarCodigo(Comic comicInfo) {
		return comicInfo != null;
	}

	/**
	 * Rellena los campos de la interfaz gráfica con la información del cómic
	 * proporcionada.
	 *
	 * @param comicInfo Un arreglo de strings con información del cómic.
	 * @throws IOException
	 */
	private static void rellenarTablaImport(Comic comic) {
		Platform.runLater(() -> {

			if (!ConectManager.conexionActiva()) {
				return;
			}

			// Variables relacionadas con la importación de cómics
			String id = "A" + 0 + "" + (ListaComicsDAO.comicsImportados.size() + 1);
			String titulo = Utilidades.defaultIfNullOrEmpty(comic.getNombre(), "Vacio");
			String issueKey = Utilidades.defaultIfNullOrEmpty(comic.getKey_issue(), "Vacio");
			String numero = Utilidades.defaultIfNullOrEmpty(comic.getNumero(), "0");
			String variante = Utilidades.defaultIfNullOrEmpty(comic.getVariante(), "Vacio");
			String precio = Utilidades.defaultIfNullOrEmpty(comic.getPrecio_comic(), "0");
			String dibujantes = Utilidades.defaultIfNullOrEmpty(comic.getDibujante(), "Vacio");
			String escritores = Utilidades.defaultIfNullOrEmpty(comic.getGuionista(), "Vacio");
			String fechaVenta = comic.getFecha();
			LocalDate fecha = Utilidades.parseFecha(fechaVenta);

			// Variables relacionadas con la imagen del cómic
			String referencia = Utilidades.defaultIfNullOrEmpty(comic.getUrl_referencia(), "Vacio");
			String urlImagen = comic.getImagen();
			String editorial = Utilidades.defaultIfNullOrEmpty(comic.getEditorial(), "Vacio");
			File file = new File(urlImagen);

			// Manejo de la ruta de la imagen
			if (comic.getImagen() == null || comic.getImagen().isEmpty()) {
				String rutaImagen = "/Funcionamiento/sinPortada.jpg";
				URL url = Utilidades.class.getClass().getResource(rutaImagen);
				if (url != null) {
					urlImagen = url.toExternalForm();
				}
			} else {
				file = new File(urlImagen);
				urlImagen = file.toString();
			}

			String formato = Utilidades.defaultIfNullOrEmpty(comic.getFormato(), "Grapa (Issue individual)");
			String procedencia = Utilidades.defaultIfNullOrEmpty(comic.getProcedencia(),
					"Estados Unidos (United States)");

			// Corrección y generación de la URL final de la imagen
			String correctedUrl = urlImagen.replace("\\", "/").replace("http:", "https:").replace("https:", "https:/");
			String codigo_imagen = Utilidades.generarCodigoUnico(SOURCE_PATH + File.separator);
			URI uri = null;
			try {
				uri = new URI(correctedUrl);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			String urlFinal = SOURCE_PATH + File.separator + codigo_imagen + ".jpg";
			String codigo_comic = Utilidades.defaultIfNullOrEmpty(comic.getCodigo_comic(), "0");
			// Descarga y conversión asíncrona de la imagen
			Utilidades.descargarYConvertirImagenAsync(uri, SOURCE_PATH, codigo_imagen + ".jpg");

			// Creación del objeto Comic importado y actualización de la tabla
			Comic comicImport = new Comic(id, titulo, "0", numero, variante, "", editorial, formato, procedencia,
					fecha.toString(), escritores, dibujantes, "Comprado", issueKey, "Sin puntuar", urlFinal, referencia,
					precio, codigo_comic);

			ListaComicsDAO.comicsImportados.add(comicImport);

			FuncionesTableView.nombreColumnas(referenciaVentana.getTablaBBDD());
			FuncionesTableView.tablaBBDD(ListaComicsDAO.comicsImportados, referenciaVentana.getTablaBBDD());
		});
	}

	private static Comic obtenerComicInfo(String finalValorCodigo) {
		try {
			// Verificar si hay una conexión activa
			if (!ConectManager.conexionActiva()) {
				return null;
			}

			// Obtener información del cómic según la longitud del código
			if (finalValorCodigo.matches("[A-Z]{3}\\d{6}")) {

				return WebScraperPreviewsWorld.displayComicInfo(finalValorCodigo.trim(),
						referenciaVentana.getProntInfo());
			} else {
				// Si no, intentar obtener la información del cómic de diferentes fuentes
				Comic comicInfo = ApiMarvel.infoComicCode(finalValorCodigo.trim(), referenciaVentana.getProntInfo());
				if (comicInfo == null) {
					comicInfo = WebScrapGoogle.obtenerDatosDiv(finalValorCodigo.trim());
				}
				if (comicInfo == null) {
					ApiISBNGeneral isbnGeneral = new ApiISBNGeneral();
					comicInfo = isbnGeneral.getBookInfo(finalValorCodigo.trim(), referenciaVentana.getProntInfo());
				}

				if (comicInfo == null) {
					return null;
				}

				Comic.limpiarCamposComic(comicInfo);
				return comicInfo;
			}
		} catch (IOException | URISyntaxException | JSONException e) {
			// Manejar excepciones
			System.err.println("Error al obtener información del cómic: " + e.getMessage());
			return null;
		}
	}

	private static void cerrarExecutorService(ExecutorService executorService) {
		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	private static void actualizarInterfaz(AtomicInteger contadorErrores, StringBuilder codigoFaltante, String carpetaDatabase,
			AtomicInteger contadorTotal) {
		Platform.runLater(() -> {
			String mensaje = "";
			if (contadorErrores.get() > 0 && !carpetaDatabase.isEmpty()) {
				Utilidades.imprimirEnArchivo(codigoFaltante.toString(), carpetaDatabase);
			}

			if (!carpetaDatabase.isEmpty()) {
				mensaje = "Se han procesado: " + (contadorTotal.get() - contadorErrores.get()) + " de "
						+ contadorTotal.get();
			} else {
				mensaje = "Se han procesado: " + (contadorErrores.get()) + " de " + contadorTotal.get();

			}

			AlarmaList.mostrarMensajePront(mensaje, true, referenciaVentana.getProntInfo());
		});
	}

	/**
	 * Realiza una búsqueda utilizando un archivo de importaciones y muestra los
	 * resultados en la interfaz gráfica.
	 *
	 * @param fichero El archivo que contiene los códigos de importación a buscar.
	 */
	public static void busquedaPorCodigoImportacion(File fichero) {

		if (!ConectManager.conexionActiva() && !Utilidades.isInternetAvailable()) {
			return;
		}

		StringBuilder codigoFaltante = new StringBuilder();
		AtomicInteger contadorErrores = new AtomicInteger(0);
		AtomicInteger comicsProcesados = new AtomicInteger(0);
		AtomicInteger numLineas = new AtomicInteger(0); // Declarar como AtomicInteger
		numLineas.set(Utilidades.contarLineasFichero(fichero)); // Asignar el valor aquí
		AtomicReference<CargaComicsController> cargaComicsControllerRef = new AtomicReference<>();
		String mensaje = "ERROR. Has cancelado la subida de comics";
		nav.verCargaComics(cargaComicsControllerRef);
		Task<Void> tarea = new Task<>() {
			@Override
			protected Void call() {
				ExecutorService executorService = Executors
						.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

				try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
					AtomicReference<String> finalValorCodigoWrapper = new AtomicReference<>();
					reader.lines().forEach(linea -> {
						// Verifica si la tarea ha sido cancelada
						if (isCancelled()) {
							return; // Sale del método call() si la tarea ha sido cancelada
						}

						String finalValorCodigo = Utilidades.eliminarEspacios(linea).replace("-", "");
						finalValorCodigoWrapper.set(finalValorCodigo);

						if (!finalValorCodigo.isEmpty()) {
							final String[] texto = { "" }; // Envuelve la variable en un array de un solo elemento
							if (procesarComicPorCodigo(finalValorCodigoWrapper.get())) {
								texto[0] = "Comic: " + finalValorCodigoWrapper.get() + "\n";
							} else {
								codigoFaltante.append("Falta comic con codigo: ").append(finalValorCodigo).append("\n");
								texto[0] = "Comic no capturado: " + finalValorCodigoWrapper.get() + "\n";
								contadorErrores.getAndIncrement();
							}
							comicsProcesados.getAndIncrement();
							final long finalProcessedItems = comicsProcesados.get();

							// Update UI elements using Platform.runLater
							Platform.runLater(() -> {

								String textoFinal = texto[0];

								double progress = (double) finalProcessedItems / (numLineas.get() + 1);
								String porcentaje = String.format("%.2f%%", progress * 100);

								cargaComicsControllerRef.get().cargarDatosEnCargaComics(textoFinal, porcentaje,
										progress);
							});
						}
					});
				} catch (IOException e) {
					Utilidades.manejarExcepcion(e);
				} finally {
					cerrarExecutorService(executorService);
				}

				return null;
			}
		};

		tarea.setOnRunning(ev -> {
			AccionControlUI.limpiarAutorellenos();
			cambiarEstadoBotones(true);
			referenciaVentana.getImagencomic().setImage(null);
			referenciaVentana.getImagencomic().setVisible(true);

			AlarmaList.iniciarAnimacionCargaImagen(referenciaVentana.getCargaImagen());

			referenciaVentana.getMenu_Importar_Fichero_CodigoBarras().setDisable(true);
		});

		tarea.setOnSucceeded(ev -> {
			AlarmaList.detenerAnimacionCargaImagen(referenciaVentana.getCargaImagen());
			cambiarEstadoBotones(false);

			actualizarInterfaz(contadorErrores, codigoFaltante, CARPETA_RAIZ_PORTADAS, numLineas);

			Platform.runLater(() -> {
				cargaComicsControllerRef.get().cargarDatosEnCargaComics("", "100%", 100.0);
			});
			referenciaVentana.getMenu_Importar_Fichero_CodigoBarras().setDisable(false);
		});

		tarea.setOnCancelled(ev -> {
			cambiarEstadoBotones(false);
			AlarmaList.mostrarMensajePront(mensaje, false, referenciaVentana.getProntInfo());
			AlarmaList.detenerAnimacionCargaImagen(referenciaVentana.getCargaImagen()); // Detiene la animación de carga
		});

		Thread thread = new Thread(tarea);

		referenciaVentana.getBotonCancelarSubida().setOnAction(ev -> {
			actualizarInterfaz(comicsProcesados, codigoFaltante, "", numLineas);
			nav.cerrarCargaComics();
			cambiarEstadoBotones(false);
			tarea.cancel(true);
			referenciaVentana.getMenu_Importar_Fichero_CodigoBarras().setDisable(false);

		});

		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Elimina cualquier resaltado de campos en rojo que indique errores.
	 */
	public void borrarErrores() {

		referenciaVentana.getNombreComic().setStyle("");
		referenciaVentana.getNumeroComic().setStyle("");
		referenciaVentana.getEditorialComic().setStyle("");
		referenciaVentana.getGuionistaComic().setStyle("");
		referenciaVentana.getDibujanteComic().setStyle("");
	}

	/**
	 * Modifica la visibilidad y el estado de los elementos de búsqueda en la
	 * interfaz de usuario.
	 *
	 * @param mostrar True para mostrar los elementos de búsqueda, False para
	 *                ocultarlos.
	 */
	public void cambiarVisibilidadAvanzada() {

		List<Node> elementos = Arrays.asList(referenciaVentana.getBotonBusquedaCodigo(),
				referenciaVentana.getBusquedaCodigo());

		if (referenciaVentana.getBotonBusquedaCodigo().isVisible()) {
			Utilidades.cambiarVisibilidad(elementos, true);
		} else {
			Utilidades.cambiarVisibilidad(elementos, false);
		}
	}

	public static void cambiarEstadoBotones(boolean esCancelado) {

		List<Node> elementos = Arrays.asList(referenciaVentana.getBotonEliminarImportadoComic(),
				referenciaVentana.getBotonSubidaPortada(), referenciaVentana.getBotonGuardarComic());

		if (!TIPO_ACCION.equals("aniadir")) {
			elementos.add(referenciaVentana.getBotonBusquedaCodigo());
			elementos.add(referenciaVentana.getBusquedaCodigo());
		}

		referenciaVentana.getBotonCancelarSubida().setVisible(esCancelado);
		referenciaVentana.getBotonLimpiar().setDisable(esCancelado);
		referenciaVentana.getBotonBusquedaAvanzada().setDisable(esCancelado);
		referenciaVentana.getBotonGuardarCambioComic().setDisable(esCancelado);

		Utilidades.cambiarVisibilidad(elementos, esCancelado);
	}

	/**
	 * Establece el tipo de acción que se realizará en la ventana.
	 *
	 * @param tipoAccion El tipo de acción a realizar (por ejemplo, "aniadir",
	 *                   "modificar", "eliminar", "puntuar").
	 */
	public static void tipoAccion(String tipoAccion) {
		TIPO_ACCION = tipoAccion;
	}

}
